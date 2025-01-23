package com.nouros.hrms.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.document.Document;
// import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.ai.model.rag.VectorMetaData;
import com.enttribe.commons.ai.rag.VectorService;
//import com.enttribe.commons.ai.model.rag.VectorMetaData;
 //import com.enttribe.commons.ai.rag.VectorService;
import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.document.rest.IDocumentStreamRest;
import com.enttribe.platform.utility.notification.mail.model.NotificationAttachment;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.usermanagement.user.wrapper.UserDetailWrapper;
import com.enttribe.usermanagement.user.wrapper.UserProviderWrapper;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nouros.hrms.integration.service.DocumentIntegrationService;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.Division;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeComplianceLegal;
import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.model.EmployeeEmergencyContact;
import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.model.Location;
import com.nouros.hrms.model.Skill;
import com.nouros.hrms.repository.DepartmentRepository;
import com.nouros.hrms.repository.DesignationRepository;
import com.nouros.hrms.repository.EmployeeCertificationRepository;
import com.nouros.hrms.repository.EmployeeDependentDetailsRepository;
import com.nouros.hrms.repository.EmployeeLanguageRepository;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.EmployeeWorkExperienceRepository;
import com.nouros.hrms.repository.EmployeeeducationdetailsRepository;
import com.nouros.hrms.repository.LocationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.DepartmentService;
import com.nouros.hrms.service.DesignationService;
import com.nouros.hrms.service.DivisionService;
import com.nouros.hrms.service.EmployeeComplianceLegalService;
import com.nouros.hrms.service.EmployeeDependentDetailsService;
import com.nouros.hrms.service.EmployeeEmergencyContactService;
import com.nouros.hrms.service.EmployeeLeaveTypeService;
import com.nouros.hrms.service.EmployeeNationalIdentificationService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.LeaveTypeService;
import com.nouros.hrms.service.LocationService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.CreateAdUserDto;
import com.nouros.hrms.wrapper.DepartmentDetailsWrapper;
import com.nouros.hrms.wrapper.DivisionDto;
import com.nouros.hrms.wrapper.EmployeeCompleteDetailsDto;
import com.nouros.hrms.wrapper.EmployeeComplianceWrapper;
import com.nouros.hrms.wrapper.EmployeeDataWrapper;
import com.nouros.hrms.wrapper.EmployeeDependentWrapper;
import com.nouros.hrms.wrapper.EmployeeDetailsDto;
import com.nouros.hrms.wrapper.EmployeeDto;
import com.nouros.hrms.wrapper.EmployeeEmergencyWrapper;
import com.nouros.hrms.wrapper.EmployeeNationalWrapper;
import com.nouros.hrms.wrapper.EmployeeOrgChartDto;
import com.nouros.hrms.wrapper.EmployeeWrapper;
import com.nouros.hrms.wrapper.OrgnisationDto;
import com.nouros.hrms.wrapper.ReportingHeirarchyWrapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;


/**
 * This is a class named "EmployeeServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "EmployeeService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Employee and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Employee
 * Employee) for exporting the Employee data into excel file by reading the
 * template and mapping the Employee details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeServiceImpl extends AbstractService<Integer, Employee> implements EmployeeService {

	private static final String IMPORTEMPLOYEE_RESULTS_XLSX = "importemployee_results.xlsx";
	private static final String INSIDE_IF_TO_FETCH_EMPLOYEE_BY_ID = "Inside if to fetch Employee by Id ";
	private static final String INSIDE_IF_TO_FETCH_EMPLOYEE_BY_FULL_NAME = "Inside if to fetch Employee by Full Name ";
	private static final String EMPLOYMENT_TYPE = "employmentType";

	private static final Logger log = LogManager.getLogger(EmployeeServiceImpl.class);

	/**
	 *
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Employee entities.
	 */

	private static final String LDAPURL = "LDAPURL";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String DOMAIN = "DOMAIN";
	private static final String JKS_FILE_PATH = "JKS_FILE_PATH";
	private static final String JKS_PASS = "JKS_PASS";
	private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&+?";
	private static final int ADS_UF_NORMAL_ACCOUNT = 0x0200;
	private static final String INSIDE_EMPLOYEESERVICE_LOG = "Inside @Class EmployeeServiceImpl @Method findEmployeeByDateAndMonthWhenMatchDob";
	private static final String CAUGHT_ERROR_FOR_CREATING_AND_INSERTING_VECTOR = "Caught error for creating and inserting Vector : {} ";
	public static final String SOMETHING_WENT_WRONG = "Something Went Wrong : {} "; 
	
	public EmployeeServiceImpl(GenericRepository<Employee> repository) {
		super(repository, Employee.class);
	}

	@Autowired
	private EntityManager entityManager;

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private DesignationService designationService;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private LocationService locationService;

	@Autowired
	private LeaveTypeService leaveTypeService;

	@Autowired
	private EmployeeLeaveTypeService employeeLeaveTypeService;

	@Autowired
	private IDocumentStreamRest documentStreamRest;

	@Autowired
	private UserRest userRest;

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;

	@Autowired
	private NotificationIntegration notificationIntegration;

	@Autowired
	private DocumentIntegrationService documentIntegrationService;
	
	@Autowired
	private EmployeeCertificationRepository employeeCertificationRepository;
	
	@Autowired
	private EmployeeDependentDetailsRepository employeeDependentDetailsRepository;
	
	@Autowired
	private EmployeeLanguageRepository employeeLanguageRepository;
	
	@Autowired
	private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;
	
	@Autowired
	private EmployeeWorkExperienceRepository employeeWorkExperienceRepository;
	
	@Autowired
	private EmployeeeducationdetailsRepository employeeeducationdetailsRepository;
	
	@Autowired
	private CommonUtils commonUtils;
//	
	@Autowired
	private VectorService vectorService;
//	
//	    @Autowired
//    private VectorStore knowledgeGraphVectorStore1;


	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	/**
	 * This method is responsible for soft-deleting an Employee record in the
	 * database. The method takes in an int id which represents the id of the
	 * Employee that needs to be soft-deleted. It uses the id to find the Employee
	 * by calling the EmployeeRepository.findById method. If the Employee is found,
	 * it sets the "deleted" field to true, save the Employee in the repository, and
	 * saves it in the database
	 *
	 * @param id an int representing the id of the Employee that needs to be
	 *           soft-deleted
	 */
	@Override
	public void softDelete(int id) {

		Employee employee = super.findById(id);

		if (employee != null) {

			Employee employee1 = employee;
			employee1.setDeleted(true);
			employeeRepository.save(employee1);

		}
	}

	/**
	 * This method is responsible for soft-deleting multiple Employee records in the
	 * database in bulk. The method takes in a List of integers, each representing
	 * the id of an Employee that needs to be soft-deleted. It iterates through the
	 * list, calling the softDelete method for each id passed in the list.
	 *
	 * @param list a List of integers representing the ids of the Employee that need
	 *             to be soft-deleted
	 */
	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

	@Override
	public Employee createWithNaming(Employee employee) {

		String firstName = employee.getFirstName();
		String lastName = employee.getLastName();
		String fullName = firstName + " " + lastName;

		if (employee.getReportingManager() != null && employee.getReportingManager().getFullName() != null
				&& !employee.getReportingManager().getFullName().equalsIgnoreCase(fullName)) {
			Map<String, String> mp = new HashMap<>();
			String generatedName = null;
			NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
			try {
				nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("employeeRule", mp,
						Status.ALLOCATED);
				log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
				generatedName = nameGenerationWrapperV2.getGeneratedName();
				employee.setEmployeeId(generatedName);
			} catch (Exception e) {
				logger.error("Failed to create/generate Naming Id For Employee");
			}
			log.debug("Going to Set Full Name ");
			employee.setFullName(employee.getFirstName() + " " + employee.getLastName());
			employee.setArabicFullName(employee.getArabicFirstName() + " " + employee.getArabicLastName());
			User user = userRest.byUserName(employee.getWorkEmailAddress());
			if (user != null) {
				updateUserBasicDetails(employee);
			} else {
				try {
					log.info("Going to Create User , based on employee Object Coming");
					Map<String, String> response = userRest.createFromIDP(createUserData(employee));
					log.debug("response Map generated is : {} ", response);
					User userCreated = userRest.byUserName(employee.getWorkEmailAddress());
					int userId = userCreated.getUserid();
					employee.setUserId(userId);
				} catch (BusinessException be) {
					log.error(" Error in setting Data to Wrapper from Employee");
				} catch (Exception ex) {
					log.error("Some Error While Creating User From Employee Object : {} ", ex.getMessage());
				}
			}
			if (employee.getWorkEmailAddress() != null) {
				Employee alreadyCreated = employeeRepository.findByEmailId(employee.getWorkEmailAddress());
				if (alreadyCreated != null) {
					throw new BusinessException("Email Address Already In Use, Kindly Provide Different Email Address");
				}
			}
			setReportingManagerUserId(employee);
			Employee emp = employeeRepository.save(employee);
			try {
				List<LeaveType> leaveType = populateLeaveDataForEmployee(emp);
				log.debug("LeaveType Object based on Employee Object is : {} ", leaveType);
				setEmployeeLeaveTypeObject(emp, leaveType);
				log.debug("Employee Leave Type Populated SuccessFully ");
			} catch (BusinessException be) {
				log.error(" Error in setting Data to Wrapper from Employee");
			} catch (Exception ex) {
				log.error("Some Error While Creating User From Employee Object : {} ", ex.getMessage());
			}
			return emp;
		} else {
			throw new BusinessException(
					"employee cannot report to itself , please check reporting manager for employee");
		}
	}

	/**
	 * Creates a new vendor.
	 *
	 * @param employee The employee object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Employee update(Employee employee) {

		String firstName = employee.getFirstName();
		String lastName = employee.getLastName();
		String fullName = firstName + " " + lastName;

		if (employee.getReportingManager() == null) {
			return employeeRepository.save(employee);
		}

		if (employee.getReportingManager() != null && employee.getReportingManager().getFullName() != null
				&& !employee.getReportingManager().getFullName().equalsIgnoreCase(fullName)) {
			log.info("Inside Method update ");
			User user = userRest.userByEmail(employee.getWorkEmailAddress());
			log.info("User fetched is : {} ", user);
			if (user != null) {
				updateUserBasicDetails(employee);
			}
			log.info("Now Going to Finally Save the updated Employee");
			if (employee.getArabicFirstName() != null && employee.getArabicLastName() != null) {
				employee.setArabicFullName(employee.getArabicFirstName() + " " + employee.getArabicLastName());
			}
			return employeeRepository.save(employee);

		} else {
			throw new BusinessException(
					"employee cannot report to itself , please check reporting manager for employee");
		}

	}

	/**
	 * @param employee
	 */
	private void updateUserBasicDetails(Employee employee) {
		log.info("Inside updateUserBasicDetails");
		try {
			log.info("Going to Update User , to update Um Details based on employee Object Coming");
			UserDetailWrapper userDetailWrapper = new UserDetailWrapper();
			Integer photoDocumentId = fetchDocumentFromEmployeePhoto(employee);
			byte[] input = null;
			try {
				log.info("Starting to download document with photoDocumentId: {}", photoDocumentId);
				input = documentStreamRest.fileDownloadByteArray(photoDocumentId);
				if (input != null) {
					log.debug("length of input array photoDocumentId : {}", input.length);
					String encodedImage = Base64.getEncoder().encodeToString(input);
					userDetailWrapper.setImage(encodedImage);
					log.debug("Successfully downloaded and encoded image for photoDocumentId: {}", photoDocumentId);
				} else {
					log.debug("Received null byte array for photoDocumentId: {}", photoDocumentId);
				}
			} catch (Exception e) {
				log.error("Document failed to download with photoDocumentId: {}. Error: {}", photoDocumentId,
						e.getMessage(), e);
			}
			userDetailWrapper.setUserId(employee.getUserId());
			userDetailWrapper.setOrgRoleName(employee.getDesignation().getName());
			userDetailWrapper.setDepartmentName(employee.getDepartment().getName());
			userDetailWrapper.setReportingManager(employee.getReportingManager().getWorkEmailAddress());
			log.debug("Final userDetailWrapper Formed is : {}", userDetailWrapper);
			String response = userRest.updateUserBasicDetail(userDetailWrapper);
			log.debug("response String generated is : {} ", response);
		} catch (BusinessException be) {
			log.error(" Error in Updating User Details in UM For Provided Details");
		} catch (Exception ex) {
			log.error("Some Error While Updating  User From Employee Object Details : {} ", ex.getMessage());
		}
	}

	private int fetchDocumentFromEmployeePhoto(Employee employee) {
		log.info("inside fetchDocumentFromEmployeePhoto");
		String jsonString = employee.getEmployeePhoto();

		int id = 0;
		if (jsonString != null) {
			JSONObject jsonObject = new JSONObject(jsonString);
			if (jsonObject.has("ids")) {
				JSONArray idsArray = jsonObject.getJSONArray("ids");
				log.debug("id's coming from ResumeAttachment is : {} ", idsArray);
				for (int i = 0; i < idsArray.length(); i++) {
					id = idsArray.getInt(i);
				}
			}
			return id;
		}
		return id;
	}

	@Override
	public void setReportingManagerUserId(Employee employee) {
		log.info("Inside setReportingManagerUserId");
		if (employee.getReportingManager() != null && employee.getReportingManager().getUserId() != null) {
			Integer reportingManagerUserId = employee.getReportingManager().getUserId();
			log.debug("Reporting Manager's User ID set to : {}", reportingManagerUserId);
			employee.setReportingManagerUserId(reportingManagerUserId);
		}
	}

	public static String extractFirstLetters(String input) {
		StringBuilder result = new StringBuilder();
		String[] words = input.split("\\s+");
		for (int i = 0; i < Math.min(words.length, 4); i++) {
			String word = words[i];
			if (!word.isEmpty()) {
				result.append(word.charAt(0));
			}
		}
		return result.toString();
	}

	private void setEmployeeLeaveTypeObject(Employee employee, List<LeaveType> leaveTypes) {
		log.info("Inside Method setEmployeeLeaveTypeObject");
		log.debug("List of LeaveType is : {} ", leaveTypes);
		for (LeaveType leaveType : leaveTypes) {
			EmployeeLeaveType employeeLeaveType = new EmployeeLeaveType();
			setEmployeeLeaveType(employee, leaveType, employeeLeaveType);
			log.debug("Employee Leave Type Object To be Populated is : {} ", employeeLeaveType);
			employeeLeaveTypeService.create(employeeLeaveType);
		}
	}

	private void setEmployeeLeaveType(Employee employee, LeaveType leaveType, EmployeeLeaveType employeeLeaveType) {
		if (employee != null) {
			employeeLeaveType.setEmployeeId(employee);
		}
		if (leaveType != null) {
			employeeLeaveType.setLeaveType(leaveType);
		}
		if (leaveType != null && leaveType.getOpeningBalance() != null) {
			employeeLeaveType.setBalance(leaveType.getOpeningBalance());
		}
		if (leaveType != null && leaveType.getMaximumBalance() != null) {
			employeeLeaveType.setTotalBalance(leaveType.getMaximumBalance());
		}
		if (leaveType != null && leaveType.getValidityTo() != null) {
			employeeLeaveType.setYearEndDate(leaveType.getValidityTo());
		}
		if (leaveType != null && leaveType.getValidityFrom() != null) {
			employeeLeaveType.setYearStartDate(leaveType.getValidityFrom());
		}
	}

	private List<LeaveType> populateLeaveDataForEmployee(Employee emp) {
		log.info("Inside Method populateLeaveDataForEmployee");
		String departmentName = "";
		String designationName = "";
		String locationName = "";
		List<LeaveType> leaveTypeList = new ArrayList<>();
		if (emp.getDepartment() != null && emp.getDesignation() != null && emp.getLocation() != null) {
			Department department = departmentService.findById(emp.getDepartment().getId());
			if (department != null) {
				departmentName = department.getName();
			}
			Designation designation = designationService.findById(emp.getDesignation().getId());
			if (designation != null) {
				designationName = designation.getName();
			}
			Location location = locationService.findById(emp.getLocation().getId());
			if (location != null) {
				locationName = location.getName();
			}
			try {
				leaveTypeList = leaveTypeService.findByDepartmentNameAndDesignationNameAndLocationName(departmentName,
						designationName, locationName);
				return leaveTypeList;
			} catch (BusinessException be) {
				log.debug("No Leave Type Found on basis Of departmentName,designationName,locationName");
			} catch (Exception be) {
				log.debug("SomeThing Went Wrongfor Leave Type Fetching");
			}
		} else {
			throw new BusinessException("Department , Designation , Location Can Not Be Null For Any Employee");
		}
		return leaveTypeList;
	}

	/**
	 * @param employee
	 * @return Object of UserProviderWrapper
	 */
	private UserProviderWrapper createUserData(Employee employee) {
		log.info("Inside Method createUserData");
		UserProviderWrapper userProviderWrapper = new UserProviderWrapper();
		try {
			log.debug("Object of Employee to set Data is  : {} ", employee);
			userProviderWrapper.setContactNumber(employee.getPersonalMobileNumber());
			userProviderWrapper.setCountryName(employee.getCountryOfResidence());
			userProviderWrapper.setFirstname(employee.getFirstName());
			userProviderWrapper.setLastname(employee.getLastName());
			userProviderWrapper.setUsername(employee.getWorkEmailAddress());
			List<String> roleList = new ArrayList<>();
			userProviderWrapper.setRoleName(roleList);
			userProviderWrapper.setEmail(employee.getWorkEmailAddress());
			userProviderWrapper.setDefaultProfile("HRMS Employee profile");
			userProviderWrapper.setLanguage("en");
			Map<String, String> attributeMap = new HashMap<>();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
			String strBirthDate = dateFormat.format(employee.getDateOfBirth());
			attributeMap.put("BIRTH_DATE", strBirthDate);
			attributeMap.put("GENDER", employee.getGender());
			userProviderWrapper.setAttributeMap(attributeMap);
			log.debug("userProviderWrapper is : {} ", userProviderWrapper);
		} catch (Exception e) {
			throw new BusinessException(
					"Any of the Data is Null for creating User , check Country,PersonalMobileNumber, firstName,lastName, userName,Email Of Employee Object");
		}
		return userProviderWrapper;
	}

	/**
	 * Creates a new vendor.
	 *
	 * @param employee The employee object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Employee create(Employee employee) {
		return employeeRepository.save(employee);
	}

	/**
	 * @param employeeWrapper containg Id and emailId of Employee
	 * @return List of ReportingHeirarchyWrapper based on id, emailId
	 */
	@Override
	public List<ReportingHeirarchyWrapper> getAllParentChildEmployeeObjects(EmployeeWrapper employeeWrapper) {
		log.info("Inside Method getAllParentChildEmployee ");

		Employee employeeWithId = getEmployeeByIdOrEmail(employeeWrapper);
		List<ReportingHeirarchyWrapper> employeeList = new ArrayList<>();

		if (employeeWithId != null) {
			log.debug("Employee Object fetched on basis of Id And EmailId is : {} ", employeeWithId);

			ReportingHeirarchyWrapper reportingHeirarchyWrapper = setHeirarchyData(employeeWithId);
			employeeList.add(reportingHeirarchyWrapper);

			fetchDownLevelEmployees(employeeWithId, employeeList);
			fetchUpLevelEmployees(employeeWithId, employeeList);
		}

		return employeeList;
	}

	private Employee getEmployeeByIdOrEmail(EmployeeWrapper employeeWrapper) {
		if (employeeWrapper.getId() != null) {
			log.info(INSIDE_IF_TO_FETCH_EMPLOYEE_BY_ID);
			return employeeRepository.fetchById(employeeWrapper.getId());
		} else if (employeeWrapper.getUserId() != null) {
			log.info("method @getEmployeeByIdOrEmail  Inside else if to fetch Employee by UserId ");
			return employeeRepository.findByUserId(employeeWrapper.getUserId());
		} else if (employeeWrapper.getEmailId() != null) {
			log.info("Inside else if to fetch Employee by workEmailAddress ");
			return employeeRepository.findByEmailId(employeeWrapper.getEmailId());
		}
		return null;
	}

	private void fetchDownLevelEmployees(Employee employee, List<ReportingHeirarchyWrapper> employeeList) {
		int downReportManagerL1ID = employee.getId();
		List<Employee> reportingManagerEmployeesDL1 = findByReportingManagerIdDownLevel(downReportManagerL1ID);

		log.debug("Down level 1 Employee Object is : {} ", reportingManagerEmployeesDL1);

		List<ReportingHeirarchyWrapper> employeeListDownLevel = new ArrayList<>();
		for (Employee emp : reportingManagerEmployeesDL1) {
			ReportingHeirarchyWrapper reportingHeirarchyWrapper = setHeirarchyData(emp);
			employeeListDownLevel.add(reportingHeirarchyWrapper);
			log.debug("reportingHeirarchyWrapper inside loop is : {} ", reportingHeirarchyWrapper);
		}

		log.debug("employeeListDownLevel is : {} ", employeeListDownLevel);

		if (CollectionUtils.isNotEmpty(reportingManagerEmployeesDL1)) {
			for (Employee downReportManagerL2 : reportingManagerEmployeesDL1) {
				List<Employee> reportingManagerEmployeesDL2 = findByReportingManagerIdDownLevel(
						downReportManagerL2.getId());
				log.debug("Down level 2 Employee Object is : {} ", reportingManagerEmployeesDL2);
				for (Employee downLevel2 : reportingManagerEmployeesDL2) {
					ReportingHeirarchyWrapper reportingHeirarchyWrapper2 = setHeirarchyData(downLevel2);
					log.debug("reportingHeirarchyWrapper2 is : {} ", reportingHeirarchyWrapper2);
					employeeListDownLevel.add(reportingHeirarchyWrapper2);
				}
			}
			employeeList.addAll(employeeListDownLevel);
		}
	}

	private void fetchUpLevelEmployees(Employee employee, List<ReportingHeirarchyWrapper> employeeList) {
		if (employee.getReportingManager() != null) {
			int upReportManagerUL1 = employee.getReportingManager().getId();
			List<Employee> reportingManagerEmployeesUL1 = fetchDataBasedOnReportingManagerUpLevel(upReportManagerUL1);

			log.debug("Up level 1 Employee Object is : {} ", reportingManagerEmployeesUL1);

			List<ReportingHeirarchyWrapper> employeeListUpLevel = new ArrayList<>();
			for (Employee emp2 : reportingManagerEmployeesUL1) {
				ReportingHeirarchyWrapper reportingHeirarchyWrapper3 = setHeirarchyData(emp2);
				log.debug("reportingHeirarchyWrapper3 is : {} ", reportingHeirarchyWrapper3);
				employeeListUpLevel.add(reportingHeirarchyWrapper3);
			}

			if (CollectionUtils.isNotEmpty(reportingManagerEmployeesUL1)) {
				log.info("inside if condition check for ");
				fetchUpLevel2Employees(reportingManagerEmployeesUL1, employeeListUpLevel);
				employeeList.addAll(employeeListUpLevel);
			}
		}
	}

	private void fetchUpLevel2Employees(List<Employee> reportingManagerEmployeesUL1,
			List<ReportingHeirarchyWrapper> employeeListUpLevel) {
		for (Employee upReportManagerL2 : reportingManagerEmployeesUL1) {
			if (upReportManagerL2.getReportingManager() != null) {
				List<Employee> reportingManagerEmployeesUL2 = fetchDataBasedOnReportingManagerUpLevel(
						upReportManagerL2.getReportingManager().getId());
				log.debug("UP level 2 Employee Object is : {}", reportingManagerEmployeesUL2);

				for (Employee downReportManagerL2 : reportingManagerEmployeesUL2) {
					ReportingHeirarchyWrapper reportingHeirarchyWrapper4 = setHeirarchyData(downReportManagerL2);
					log.debug("reportingHeirarchyWrapper4 is : {} ", reportingHeirarchyWrapper4);
					employeeListUpLevel.add(reportingHeirarchyWrapper4);
					log.debug("All UP level Object's are : {} ", employeeListUpLevel);
				}
			}
		}
	}

	private ReportingHeirarchyWrapper setHeirarchyData(Employee emp) {
		log.info("Inside Method setHeirarchyData");
		ReportingHeirarchyWrapper reportingHeirarchyWrapper = new ReportingHeirarchyWrapper();
		reportingHeirarchyWrapper.setId(emp.getId());
		reportingHeirarchyWrapper.setName(emp.getFirstName() + " " + emp.getLastName());
		reportingHeirarchyWrapper.setDepartment(emp.getDepartment().getName());
		reportingHeirarchyWrapper.setDesignation(emp.getDesignation().getName());
		if (emp.getWorkEmailAddress() != null) {
			reportingHeirarchyWrapper.setEmailId(emp.getWorkEmailAddress());
		}
		if (emp.getReportingManager() != null) {
			reportingHeirarchyWrapper.setReportingManagerId(emp.getReportingManager().getId());
		}
		if (emp.getUserId() != null) {
			reportingHeirarchyWrapper.setUserId(emp.getUserId());
		}
		if (emp.getEmployeeId() != null) {
			reportingHeirarchyWrapper.setEmployeeId(emp.getEmployeeId());
		}
		return reportingHeirarchyWrapper;
	}

	private List<Employee> fetchDataBasedOnReportingManagerUpLevel(int id) {
		return employeeRepository.findByReportingManagerIdUpLevel(id);
	}

	private List<Employee> findByReportingManagerIdDownLevel(int id) {
		return employeeRepository.findByReportingManagerIdDownLevel(id);
	}

	@Override
	public Employee findByProcessInstanceId(String processInstanceId) {
		return employeeRepository.findByProcessInstanceId(processInstanceId);
	}

	@Override
	public String execute(Integer id) {
		try {
			log.info("inside @class CreateADUser @method execute");
			String ldapUrl = ConfigUtils.getString(LDAPURL);
			String username = ConfigUtils.getString(USERNAME);
			String password = ConfigUtils.getString(PASSWORD);
			String domainName = ConfigUtils.getString(DOMAIN);
			String trustStorePassword = ConfigUtils.getString(JKS_PASS);

			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);

			Employee optionalEmployee = employeeService.findById(id);
			if (optionalEmployee == null) {
				String errorMessage = "Employee not found with id: " + id;
				throw new BusinessException(errorMessage);
			}
			Employee employee = optionalEmployee;
			CreateAdUserDto userDto = new CreateAdUserDto();
			if (Objects.nonNull(employee)) {
				userDto.setFirstName(employee.getFirstName());
				userDto.setLastName(employee.getLastName());
				String email = employee.getFirstName() + "." + employee.getLastName() + "@" + domainName;
				String trimEmail = email.replace(" ", "").toLowerCase();
				userDto.setEmail(trimEmail);
				userDto.setGivenName(employee.getFirstName());
				String samAccount = userDto.getFirstName() + "." + userDto.getLastName();
				String trimSamAccount = samAccount.replace(" ", "").toLowerCase();
				userDto.setSamAccountName(trimSamAccount);
				userDto.setUserPrincipalName(trimEmail);
			}
			log.debug("inside @class CreateADUser @method execute Connected to LDAP checking user userDto {}", userDto);
			createLDAPUser(ldapUrl, username, password, trustStorePassword, userDto);
			log.info("inside @class CreateADUser @method execute sucessFully createAdUser : {} ",
					userDto.getSamAccountName());
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			String errorMessage = "An error occurred in @method : execute inside @EmployeeServiceImpl class";
			throw new BusinessException(errorMessage, e);
		}

	}

	public void createLDAPUser(String ldapUrl, String username, String password, String trustStorePassword,
			CreateAdUserDto userDto) throws NamingException {

		Map<String, String> env = new HashMap<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, username);
		env.put(Context.SECURITY_CREDENTIALS, password);

		File file = new File(ConfigUtils.getString(JKS_FILE_PATH));
		String trustStoreFilePath = file.getAbsolutePath();
		System.setProperty("javax.net.ssl.trustStore", trustStoreFilePath);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
		DirContext ctx = new InitialDirContext();
		log.info("inside @class createLDAPUser @method execute Connected to LDAP");
		Attributes userAttributes = new BasicAttributes(true);

		Attribute objClasses = new BasicAttribute("objectClass");
		objClasses.add("top");
		objClasses.add("person");
		objClasses.add("organizationalPerson");
		objClasses.add("user");
		Attribute userAccountControl = new BasicAttribute("userAccountControl",
				Integer.toString(ADS_UF_NORMAL_ACCOUNT));
		userAttributes.put(userAccountControl);
		userAttributes.put(objClasses);
		userAttributes.put("samAccountName", userDto.getSamAccountName());
		userAttributes.put("userAccountControl", 512);
		userAttributes.put("name", userDto.getFirstName() + " " + userDto.getLastName());
		userAttributes.put("userPrincipalName", userDto.getEmail());
		userAttributes.put("givenName", userDto.getGivenName());
		userAttributes.put("sn", userDto.getLastName());
		userAttributes.put("displayName", userDto.getFirstName() + " " + userDto.getLastName());
		userAttributes.put("unicodePwd:\\\\UNI", generatePassword());
		String newUserDN = "CN=" + userDto.getFirstName() + ",";
		newUserDN += "OU=Testusers-nourOS,DC=ad,DC=aramcodigital,DC=com";
		log.info("inside @class createLDAPUser @method execute going to createSubcontext");
		ctx.createSubcontext(newUserDN, userAttributes);
		ctx.close();
	}

	public static String generatePassword() {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(8);
		for (int i = 0; i < 8; i++) {
			password.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
		}
		log.info("inside @method generatePassword is : {}", password);
		return password.toString();
	}

	@Override
	public Map<String, Integer> getCountOfEmployeesByEmploymentType(Integer departmentId) {
		log.info("Inside Method getCountOfEmployeesByEmploymentTypeAndDepartmentId");
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<Employee> employeeRoot = query.from(Employee.class);
		query.multiselect(employeeRoot.get(EMPLOYMENT_TYPE), cb.count(employeeRoot));
		query.where(cb.equal(employeeRoot.get("department").get("id"), departmentId));
		query.groupBy(employeeRoot.get(EMPLOYMENT_TYPE));
		List<Object[]> resultList = entityManager.createQuery(query).getResultList();
		Map<String, Integer> countMap = new HashMap<>();
		for (Object[] result : resultList) {
			String employmentType = (String) result[0];
			Long count = (Long) result[1];
			countMap.put(employmentType, count.intValue());
		}
		return countMap;
	}

	@Override
	public Employee getEmployeeByUserId(Integer empId) {
		try {
			log.debug("contextUser user Id is : {}", empId);
			return employeeRepository.findByUserId(empId);
		} catch (Exception e) {
			throw new BusinessException("Employee not found");
		}

	}

	@Override
	public OrgnisationDto getOrgChart(String departmentName) {
		OrgnisationDto orgnisationDto = new OrgnisationDto();
		try {

			if (departmentName.equalsIgnoreCase("CEO")) {
				log.debug("Inside getOrgChart customerId is : {}", commonUtils.getCustomerId());
				Employee employee = employeeRepository.fetchByDepartmentName(departmentName, commonUtils.getCustomerId());
				orgnisationDto.setName(employee.getFullName());
				orgnisationDto.setTitle(employee.getDepartment().getName());
				orgnisationDto.setCountry(employee.getCountryOfResidence());
				List<DivisionDto> divisionList = new ArrayList<>();
				DivisionService devisionService = ApplicationContextProvider.getApplicationContext()
						.getBean(DivisionService.class);
				List<Division> divisions = devisionService.findAll();
				log.debug("Inside @Class employeeService @Method getOrgChart divisions :{}", divisions.size());
				for (Division division : divisions) {
					DivisionDto divisionDto = new DivisionDto();
					log.debug("Inside @Class employeeService @Method getOrgChart divisions :{}", division.getId());
					divisionDto.setName(division.getName());
					divisionDto.setEmployeesLevel1(getEmployeeByDivisionId(employee.getId(), division.getId()));
					divisionDto.setDepartmentDetail(getDepartmentDetail(division.getId()));
					divisionList.add(divisionDto);
				}
				orgnisationDto.setDivisions(divisionList);
			} else {
				Department department = departmentRepository.findByName(departmentName);
				if (department != null) {
					orgnisationDto.setName(department.getName());
					List<Employee> employees = employeeRepository.fetchDeptHeadByDeptIdAndLeadID(department.getId(),
							department.getDepartmentLead());
					List<EmployeeDto> employeeDtoList = new ArrayList<>();
					getEmployeeDtoList(employeeDtoList, employees);
					orgnisationDto.setEmployeesLevel1(employeeDtoList);
					orgnisationDto.setDepartmentDetail(getDepartmenetDetail(department, null));
				}
			}

		} catch (Exception e) {
			String errorMessage = "Error inside @class EmployeeServiceImpl @method getOrgChart";
			throw new BusinessException(errorMessage, e);
		}
		return orgnisationDto;
	}

	private DepartmentDetailsWrapper getDepartmentDetail(Integer id) {
		List<Integer> departmentsId = departmentRepository.getDepartmentIdsByDevisionId(id);
		DepartmentService departmentService = ApplicationContextProvider.getApplicationContext()
				.getBean(DepartmentService.class);
		return departmentService.getDepartmentWiseDetails(departmentsId);
	}

	@Override
	public EmployeeOrgChartDto getEmployeeOrgChart(String employeeName, Integer employeeUserId) {
		EmployeeOrgChartDto dto = new EmployeeOrgChartDto();
		try {
			Employee employee = null;
			if (employeeName != null) {
				log.info(INSIDE_IF_TO_FETCH_EMPLOYEE_BY_FULL_NAME);
				employee = findByFullName(employeeName);
			} else if (employeeUserId != null) {
				log.info("Inside else if to fetch Employee by UserId ");
				employee = getEmployeeByUserId(employeeUserId);
			}
			if (employee == null) {
				throw new BusinessException("No data found with employee name  or User Id " + employeeName);
			} else {
				dto.setName(employee.getFullName());
				dto.setTitle(employee.getDepartment().getName());
				dto.setCountry(employee.getCountryOfResidence());
				Integer devisionId = null;
				if (employee.getDepartment() != null && null != employee.getDepartment().getDivision()) {
					devisionId = employee.getDepartment().getDivision().getId();
				}
				dto.setEmployeesLevel1(getEmployeeByDivisionId(employee.getId(), devisionId));
				dto.setDepartmentDetail(getDepartmenetDetail(employee.getDepartment(), employee));
			}
		} catch (Exception e) {
			String errorMessage = "Error inside @class EmployeeServiceImpl @method getEmployeeOrgChart";
			throw new BusinessException(errorMessage, e);
		}
		return dto;
	}

	private Employee findByFullName(String employeeName) {
		log.info("Inside Method findByFullName");
		try {
			return employeeRepository.findByFullName(employeeName);
		} catch (Exception e) {
			log.error("Error inside @class EmployeeServiceImpl @method findByFullName");
			return null;
		}
	}

	private DepartmentDetailsWrapper getDepartmenetDetail(Department department, Employee employee) {
		DepartmentService departmentService = ApplicationContextProvider.getApplicationContext()
				.getBean(DepartmentService.class);
		return departmentService.setDepartmentHeirarchyDataWithEmployee(department, employee);
	}

	private List<EmployeeDto> getEmployeeByDivisionId(Integer empId, Integer divisionId) {
		List<EmployeeDto> employeeDtoListOne = new ArrayList<>();
		List<Employee> employees;
		if (divisionId != null) {
			employees = employeeRepository.findByRMAndDivision(empId, divisionId);
		} else {
			employees = employeeRepository.findByReportingManagerIdDownLevel(empId);
		}
		log.debug("Inside @Class employeeService @Method getEmployeeByDivisionId employees :{}", employees.size());
		getEmployeeDtoList(employeeDtoListOne, employees);
		return employeeDtoListOne;
	}

	private void getEmployeeDtoList(List<EmployeeDto> employeeDtoList, List<Employee> employees) {
		for (Employee employee : employees) {
			EmployeeDto empDto = new EmployeeDto();
			empDto.setId(employee.getId());
			empDto.setName(employee.getFullName());
			empDto.setTitle(employee.getDepartment().getName());
			empDto.setCountry(employee.getCountryOfResidence());
			log.debug("Inside @Class employeeService @Method getEmployeeDtoList employees :{}", employees.size());
			empDto.setEmployeesLevel2(getEmployeeDtoListTwo(employee));
			empDto.setDepartmentDetail(getDepartmenetDetail(employee.getDepartment(), employee));
			employeeDtoList.add(empDto);
		}
	}

	private List<EmployeeDto> getEmployeeDtoListTwo(Employee emp) {
		List<Employee> employees = employeeRepository.findByReportingManagerIdDownLevel(emp.getId());
		List<EmployeeDto> employeeDtoListTwo = new ArrayList<>();
		log.debug("Inside @Class employeeService @Method getEmployeeDtoListTwo employees :{}", employees.size());
		for (Employee employee : employees) {
			EmployeeDto empDto = new EmployeeDto();
			empDto.setId(employee.getId());
			empDto.setName(employee.getFullName());
			empDto.setTitle(employee.getDepartment().getName());
			empDto.setCountry(employee.getCountryOfResidence());
			employeeDtoListTwo.add(empDto);
		}
		return employeeDtoListTwo;
	}

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	@Override
	public Map<String, Integer> getCountOfEmployeesByEmploymentType(List<Integer> departmentIds) {
		log.info("Inside Method getCountOfEmployeesByEmploymentTypeAndDepartmentId");
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<Employee> employeeRoot = query.from(Employee.class);
		query.multiselect(employeeRoot.get(EMPLOYMENT_TYPE), cb.count(employeeRoot));
		query.where(employeeRoot.get("department").get("id").in(departmentIds));
		query.groupBy(employeeRoot.get(EMPLOYMENT_TYPE));
		List<Object[]> resultList = entityManager.createQuery(query).getResultList();
		Map<String, Integer> countMap = new HashMap<>();
		for (Object[] result : resultList) {
			String employmentType = (String) result[0];
			Long count = (Long) result[1];
			countMap.put(employmentType, count.intValue());
		}
		return countMap;
	}

	@Override
	public EmployeeDetailsDto getSelfEmployeeDetails(Integer id, Integer userId) {
		log.info("Inside Method getSelfEmployeeDetails");
		EmployeeDetailsDto employeeDetailsDto = new EmployeeDetailsDto();
		Employee employee = getEmployeeByIdOrUserId(id, userId);
		if (employee != null) {
			setEmployeeAndSubDetails(employeeDetailsDto, employee);
		} else {
			throw new BusinessException("Employee Not Found For Given Details");
		}
		return employeeDetailsDto;
	}

	private void setEmployeeAndSubDetails(EmployeeDetailsDto employeeDetailsDto, Employee employee) {
		log.info("Inside Method setEmployeeAndSubDetails");
		employeeDetailsDto.setId(employee.getId());
		employeeDetailsDto.setUserId(employee.getUserId());
		employeeDetailsDto.setFirstName(employee.getFirstName());
		employeeDetailsDto.setLastName(employee.getLastName());
		employeeDetailsDto.setMiddleName(employee.getMiddleName());
		employeeDetailsDto.setGivenName(employee.getGivenName());
		employeeDetailsDto.setWorkPhoneNumber(employee.getWorkPhoneNumber());
		employeeDetailsDto.setCountryOfResidence(employee.getCountryOfResidence());
		employeeDetailsDto.setMaritalStatus(employee.getMaritalStatus());
		employeeDetailsDto.setPersonalEmailAddress(employee.getPersonalEmailAddress());
		employeeDetailsDto.setPersonalMobileNumber(employee.getPersonalMobileNumber());
		employeeDetailsDto.setPrimaryAddressBuildingNumber(employee.getPrimaryAddressBuildingNumber());
		employeeDetailsDto.setPrimaryAddressCity(employee.getPrimaryAddressCity());
		employeeDetailsDto.setPrimaryAddressCountry(employee.getPrimaryAddressCountry());
		employeeDetailsDto.setPrimaryAddressPostalCode(employee.getPrimaryAddressPostalCode());
		employeeDetailsDto.setPrimaryShortAddress(employee.getPrimaryShortAddress());
		employeeDetailsDto.setReligion(employee.getReligion());
		employeeDetailsDto.setBloodGroup(employee.getBloodGroup());
		employeeDetailsDto.setArabicFirstName(employee.getArabicFirstName());
		employeeDetailsDto.setArabicMiddleName(employee.getArabicMiddleName());
		employeeDetailsDto.setArabicLastName(employee.getArabicLastName());
		if (employee.getReportingManager() != null) {
			employeeDetailsDto.setReportingManagersUserId(employee.getReportingManager().getUserId());
			employeeDetailsDto.setReportingManagersFullName(employee.getReportingManager().getFullName());
		}
		setEmployeeDependentDetails(employeeDetailsDto, employee);
		setEmployeeEmergencyDetails(employeeDetailsDto, employee);
		setEmployeeComplianceDetails(employeeDetailsDto, employee);
		setNationalIdentificationDetails(employeeDetailsDto, employee);
		log.debug("Final employeeDetailsDto Object formed is : {} ", employeeDetailsDto);
	}

	private void setNationalIdentificationDetails(EmployeeDetailsDto employeeDetailsDto, Employee employee) {
		log.info("Inside Method setNationalIdentificationDetails");
		EmployeeNationalIdentificationService employeeNationalIdentificationService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeNationalIdentificationService.class);
		EmployeeNationalIdentification employeeNationalIdentification = employeeNationalIdentificationService
				.findNationalIdentificationDetailsOfEmployee(employee.getId());
		if (employeeNationalIdentification != null) {
			EmployeeNationalWrapper employeeNationalWrapper = new EmployeeNationalWrapper();
			employeeNationalWrapper.setId(employeeNationalIdentification.getId());
			employeeNationalWrapper.setBorderNumber(employeeNationalIdentification.getBorderNumber());
			employeeNationalWrapper.setExpiryDate(employeeNationalIdentification.getExpiryDate());
			employeeNationalWrapper.setIdentificationNumber(employeeNationalIdentification.getIdentificationNumber());
			employeeNationalWrapper.setScannedImage(employeeNationalIdentification.getScannedImage());
			employeeNationalWrapper.setType(employeeNationalIdentification.getType());
			employeeDetailsDto.setEmployeeNational(employeeNationalWrapper);
		}
	}

	private void setEmployeeComplianceDetails(EmployeeDetailsDto employeeDetailsDto, Employee employee) {
		log.info("Inside Method setEmployeeComplianceDetails");
		EmployeeComplianceLegalService employeeComplianceLegalService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeComplianceLegalService.class);
		EmployeeComplianceLegal employeeComplianceLegal = employeeComplianceLegalService
				.findComplianceLegalDetailsOfEmployee(employee.getId());
		if (employeeComplianceLegal != null) {
			EmployeeComplianceWrapper employeeComplianceWrapper = new EmployeeComplianceWrapper();
			employeeComplianceWrapper.setId(employeeComplianceLegal.getId());
			employeeComplianceWrapper.setCodeOfConduct(employeeComplianceLegal.getCodeOfConduct());
			employeeComplianceWrapper.setCompanyAssetAgreement(employeeComplianceLegal.getCompanyAssetAgreement());
			employeeComplianceWrapper.setConflictOfInterest(employeeComplianceLegal.getConflictOfInterest());
			employeeComplianceWrapper.setCyberCompliance(employeeComplianceLegal.getCyberCompliance());
			employeeComplianceWrapper.setEmploymentContract(employeeComplianceLegal.getEmploymentContract());
			employeeComplianceWrapper.setIbanCertificate(employeeComplianceLegal.getIbanCertificate());
			employeeComplianceWrapper
					.setNationalAddressCertificate(employeeComplianceLegal.getNationalAddressCertificate());
			employeeComplianceWrapper
					.setNationalIdentificationNumber(employeeComplianceLegal.getNationalIdentificationNumber());
			employeeComplianceWrapper.setNationalIdentificationNumberPassport(
					employeeComplianceLegal.getNationalIdentificationNumberPassport());
			employeeDetailsDto.setEmployeeCompliance(employeeComplianceWrapper);
		}
	}

	private void setEmployeeEmergencyDetails(EmployeeDetailsDto employeeDetailsDto, Employee employee) {
		log.info("Inside Method setEmployeeEmergencyDetails");
		EmployeeEmergencyContactService employeeEmergencyContactService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeEmergencyContactService.class);
		EmployeeEmergencyContact employeeEmergencyContact = employeeEmergencyContactService
				.findEmergencyContactOfEmployee(employee.getId());
		if (employeeEmergencyContact != null) {
			EmployeeEmergencyWrapper employeeEmergencyWrapper = new EmployeeEmergencyWrapper();
			employeeEmergencyWrapper.setId(employeeEmergencyContact.getId());
			employeeEmergencyWrapper.setEmergencyContactNumber(employeeEmergencyContact.getEmergencyContactNumber());
			employeeEmergencyWrapper
					.setEmergencyContactEmailAddress(employeeEmergencyContact.getEmergencyContactEmailAddress());
			employeeEmergencyWrapper
					.setEmergencyContactFirstName(employeeEmergencyContact.getEmergencyContactFirstName());
			employeeEmergencyWrapper
					.setEmergencyContactLastName(employeeEmergencyContact.getEmergencyContactLastName());
			employeeEmergencyWrapper
					.setEmergencyContactMiddleName(employeeEmergencyContact.getEmergencyContactMiddleName());
			employeeEmergencyWrapper.setRelationship(employeeEmergencyContact.getRelationship());
			employeeDetailsDto.setEmergencyDetails(employeeEmergencyWrapper);
		}
	}

	private void setEmployeeDependentDetails(EmployeeDetailsDto employeeDetailsDto, Employee employee) {
		log.info("Inside Method setEmployeeDependentDetails");
		List<EmployeeDependentWrapper> dependentDetails = new ArrayList<>();
		EmployeeDependentDetailsService employeeDependentService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeDependentDetailsService.class);
		List<EmployeeDependentDetails> employeeDependentDetails = employeeDependentService
				.findDependentDetailsOfEmployee(employee.getId());
		if (employeeDependentDetails != null && !employeeDependentDetails.isEmpty()) {
			log.debug("employeeDependentDetails Fetched are : {} ", employeeDependentDetails);
			for (EmployeeDependentDetails employeeDependent : employeeDependentDetails) {
				EmployeeDependentWrapper employeeDependentWrapper = new EmployeeDependentWrapper();
				employeeDependentWrapper.setId(employeeDependent.getId());
				employeeDependentWrapper.setContactNumber(employeeDependent.getContactNumber());
				employeeDependentWrapper.setDependentIdentification(employeeDependent.getDependentIdentification());
				employeeDependentWrapper.setFirstName(employeeDependent.getFirstName());
				employeeDependentWrapper.setLastName(employeeDependent.getLastName());
				employeeDependentWrapper.setFullName(employeeDependent.getFullName());
				employeeDependentWrapper.setRelationship(employeeDependent.getRelationship());
				dependentDetails.add(employeeDependentWrapper);
			}
		}
		employeeDetailsDto.setDependentDetails(dependentDetails);
	}

	@Override
	public Employee getEmployeeByIdOrUserId(Integer id, Integer userId) {
		if (id != null) {
			log.info(INSIDE_IF_TO_FETCH_EMPLOYEE_BY_ID);
			return employeeRepository.fetchById(id);
		} else if (userId != null) {
			log.info("Inside else if to fetch Employee by UserId ");
			return employeeRepository.findByUserId(userId);
		} else {
			User contextUser = getUserContext();
			log.debug("Inside getEmployeeByIdOrUserId contextUser user Id is : {}", contextUser.getUserid());
			Employee employee = employeeRepository.findByUserId(contextUser.getUserid());
			return (employee != null ? employee : null);
		}
	}

	@Override
	public EmployeeDetailsDto updateSelfEmployeeDetails(EmployeeDetailsDto employeeDetailsDto) {
		log.info("Inside Method updateSelfEmployeeDetails");
		try {
			Employee employee = getEmployeeByIdOrUserId(employeeDetailsDto.getId(), employeeDetailsDto.getUserId());
			if (employee != null) {
				updateEmployeeDetails(employeeDetailsDto, employee);
			}
			if (employeeDetailsDto.getEmployeeNational() != null) {
				updateNationalIdentificationDetails(employeeDetailsDto.getEmployeeNational());
			}
			if (employeeDetailsDto.getEmergencyDetails() != null) {
				updateEmergencyContact(employeeDetailsDto.getEmergencyDetails());
			}
			if (employeeDetailsDto.getEmployeeCompliance() != null) {
				updateEmployeeComplianceDetail(employeeDetailsDto.getEmployeeCompliance());
			}
			if (employeeDetailsDto.getDependentDetails() != null
					&& !employeeDetailsDto.getDependentDetails().isEmpty()) {
				updateEmployeeDependentDetails(employeeDetailsDto.getDependentDetails());
			}
			return this.getSelfEmployeeDetails(employeeDetailsDto.getId(), employeeDetailsDto.getUserId());
		} catch (Exception e) {
			String errorMessage = "Error inside @class EmployeeServiceImpl @method updateSelfEmployeeDetails";
			throw new BusinessException(errorMessage, e);

		}
	}

	private void updateEmployeeDependentDetails(List<EmployeeDependentWrapper> dependentDetails) {
		log.info("Inside Method updateEmployeeDependentDetails");
		EmployeeDependentDetailsService employeeDependentService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeDependentDetailsService.class);
		for (EmployeeDependentWrapper employeeDependent : dependentDetails) {
			EmployeeDependentDetails employeeDependentDetailsObject = employeeDependentService
					.findById(employeeDependent.getId());
			if (employeeDependentDetailsObject != null) {
				EmployeeDependentDetails employeeDependentDetails = employeeDependentDetailsObject;
				employeeDependentDetails.setContactNumber(employeeDependent.getContactNumber());
				employeeDependentDetails.setDependentIdentification(employeeDependent.getDependentIdentification());
				employeeDependentDetails.setFirstName(employeeDependent.getFirstName());
				employeeDependentDetails.setLastName(employeeDependent.getLastName());
				employeeDependentDetails.setFullName(employeeDependent.getFullName());
				employeeDependentDetails.setRelationship(employeeDependent.getRelationship());
				employeeDependentService.create(employeeDependentDetails);
			}
		}
	}

	private void updateEmployeeComplianceDetail(EmployeeComplianceWrapper employeeCompliance) {
		log.info("Inside Method updateEmployeeComplianceDetail");
		EmployeeComplianceLegalService employeeComplianceLegalService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeComplianceLegalService.class);
		EmployeeComplianceLegal employeeComplianceLegalObject = employeeComplianceLegalService
				.findById(employeeCompliance.getId());
		if (employeeComplianceLegalObject != null) {
			EmployeeComplianceLegal employeeComplianceLegal = employeeComplianceLegalObject;
			employeeComplianceLegal.setCodeOfConduct(employeeCompliance.getCodeOfConduct());
			employeeComplianceLegal.setCompanyAssetAgreement(employeeCompliance.getCompanyAssetAgreement());
			employeeComplianceLegal.setConflictOfInterest(employeeCompliance.getConflictOfInterest());
			employeeComplianceLegal.setCyberCompliance(employeeCompliance.getCyberCompliance());
			employeeComplianceLegal.setEmploymentContract(employeeCompliance.getEmploymentContract());
			employeeComplianceLegal.setIbanCertificate(employeeCompliance.getIbanCertificate());
			employeeComplianceLegal.setNationalAddressCertificate(employeeCompliance.getNationalAddressCertificate());
			employeeComplianceLegal
					.setNationalIdentificationNumber(employeeCompliance.getNationalIdentificationNumber());
			employeeComplianceLegal.setNationalIdentificationNumberPassport(
					employeeCompliance.getNationalIdentificationNumberPassport());
			employeeComplianceLegalService.create(employeeComplianceLegal);
		}
	}

	private void updateEmergencyContact(EmployeeEmergencyWrapper emergencyDetails) {
		log.info("Inside Method updateEmergencyContact");
		EmployeeEmergencyContactService employeeEmergencyContactService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeEmergencyContactService.class);
		EmployeeEmergencyContact employeeEmergencyContactObject = employeeEmergencyContactService
				.findById(emergencyDetails.getId());
		if (employeeEmergencyContactObject != null) {
			EmployeeEmergencyContact employeeEmergencyContact = employeeEmergencyContactObject;
			employeeEmergencyContact.setEmergencyContactNumber(emergencyDetails.getEmergencyContactNumber());
			employeeEmergencyContact
					.setEmergencyContactEmailAddress(emergencyDetails.getEmergencyContactEmailAddress());
			employeeEmergencyContact.setEmergencyContactFirstName(emergencyDetails.getEmergencyContactFirstName());
			employeeEmergencyContact.setEmergencyContactLastName(emergencyDetails.getEmergencyContactLastName());
			employeeEmergencyContact.setEmergencyContactMiddleName(emergencyDetails.getEmergencyContactMiddleName());
			employeeEmergencyContact.setRelationship(emergencyDetails.getRelationship());
			employeeEmergencyContactService.create(employeeEmergencyContact);
		}
	}

	private void updateNationalIdentificationDetails(EmployeeNationalWrapper employeeNational) {
		log.info("Inside Method updateNationalIdentificationDetails");
		EmployeeNationalIdentificationService employeeNationalIdentificationService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeNationalIdentificationService.class);
		EmployeeNationalIdentification employeeNationalObject = employeeNationalIdentificationService
				.findById(employeeNational.getId());
		if (employeeNationalObject != null) {
			EmployeeNationalIdentification employeeNationalIdentification = employeeNationalObject;
			employeeNationalIdentification.setBorderNumber(employeeNational.getBorderNumber());
			employeeNationalIdentification.setExpiryDate(employeeNational.getExpiryDate());
			employeeNationalIdentification.setIdentificationNumber(employeeNational.getIdentificationNumber());
			employeeNationalIdentification.setScannedImage(employeeNational.getScannedImage());
			employeeNationalIdentification.setType(employeeNational.getType());
			employeeNationalIdentificationService.create(employeeNationalIdentification);
		}
	}

	private void updateEmployeeDetails(EmployeeDetailsDto employeeDetailsDto, Employee employee) {
		log.info("Inside Method updateEmployeeDetails");
		if (employeeDetailsDto.getFirstName() != null) {
			employee.setFirstName(employeeDetailsDto.getFirstName());
		}
		if (employeeDetailsDto.getLastName() != null) {
			employee.setLastName(employeeDetailsDto.getLastName());
		}
		if (employeeDetailsDto.getFirstName() != null && employeeDetailsDto.getLastName() != null) {
			employee.setFullName(employeeDetailsDto.getFirstName() + " " + employeeDetailsDto.getLastName());
		}
		if (employeeDetailsDto.getMiddleName() != null) {
			employee.setMiddleName(employeeDetailsDto.getMiddleName());
		}
		if (employeeDetailsDto.getArabicFirstName() != null) {
			employee.setArabicFirstName(employeeDetailsDto.getArabicFirstName());
		}
		if (employeeDetailsDto.getArabicLastName() != null) {
			employee.setArabicLastName(employeeDetailsDto.getArabicLastName());
		}
		if (employeeDetailsDto.getArabicMiddleName() != null) {
			employee.setArabicMiddleName(employeeDetailsDto.getArabicMiddleName());
		}
		if (employeeDetailsDto.getBloodGroup() != null) {
			employee.setBloodGroup(employeeDetailsDto.getBloodGroup());
		}
		if (employeeDetailsDto.getArabicFirstName() != null && employeeDetailsDto.getArabicLastName() != null) {
			employee.setArabicFullName(
					employeeDetailsDto.getArabicFirstName() + " " + employeeDetailsDto.getArabicLastName());
		}
		if (employeeDetailsDto.getGivenName() != null) {
			employee.setGivenName(employeeDetailsDto.getGivenName());
		}
		if (employeeDetailsDto.getCountryOfResidence() != null) {
			employee.setCountryOfResidence(employeeDetailsDto.getCountryOfResidence());
		}
		if (employeeDetailsDto.getMaritalStatus() != null) {
			employee.setMaritalStatus(employeeDetailsDto.getMaritalStatus());
		}
		if (employeeDetailsDto.getPersonalEmailAddress() != null) {
			employee.setPersonalEmailAddress(employeeDetailsDto.getPersonalEmailAddress());
		}
		if (employeeDetailsDto.getPersonalMobileNumber() != null) {
			employee.setPersonalMobileNumber(employeeDetailsDto.getPersonalMobileNumber());
		}
		if (employeeDetailsDto.getPrimaryAddressBuildingNumber() != null) {
			employee.setPrimaryAddressBuildingNumber(employeeDetailsDto.getPrimaryAddressBuildingNumber());
		}
		if (employeeDetailsDto.getPrimaryAddressCity() != null) {
			employee.setPrimaryAddressCity(employeeDetailsDto.getPrimaryAddressCity());
		}
		if (employeeDetailsDto.getPrimaryAddressCountry() != null) {
			employee.setPrimaryAddressCountry(employeeDetailsDto.getPrimaryAddressCountry());
		}
		if (employeeDetailsDto.getPrimaryAddressPostalCode() != null) {
			employee.setPrimaryAddressPostalCode(employeeDetailsDto.getPrimaryAddressPostalCode());
		}
		if (employeeDetailsDto.getPrimaryShortAddress() != null) {
			employee.setPrimaryShortAddress(employeeDetailsDto.getPrimaryShortAddress());
		}
		if (employeeDetailsDto.getReligion() != null) {
			employee.setReligion(employeeDetailsDto.getReligion());
		}
		if (employeeDetailsDto.getWorkPhoneNumber() != null) {
			employee.setWorkPhoneNumber(employeeDetailsDto.getWorkPhoneNumber());
		}
		employeeRepository.save(employee);
	}

	@Override
	public List<EmployeeCompleteDetailsDto> getEmployeeDetailsForAdmin(Integer id, String fullName) {
		log.info("Inside Method getEmployeeDetailsForAdmin");
		Employee employee = null;
		List<EmployeeCompleteDetailsDto> employeeCompleteDetailsDtoList = new ArrayList<>();
		EmployeeCompleteDetailsDto employeeCompleteDetailsDto = new EmployeeCompleteDetailsDto();
		if (id != null || fullName != null) {
			employee = getEmployeeByIdOrFullName(id, fullName);
		} else {
			User contextUser = getUserContext();
			log.debug("Inside getEmployeeDetailsForAdmin contextUser user Id is : {}", contextUser.getUserid());
			employee = employeeRepository.findByUserId(contextUser.getUserid());
		}
		if (employee != null) {
			employeeCompleteDetailsDto.setEmployee(employee);
			setCompleteEmployeeAndSubDetails(employeeCompleteDetailsDto, employee);
		} else {
			throw new BusinessException("Employee Not Found For Given Details");
		}
		employeeCompleteDetailsDtoList.add(employeeCompleteDetailsDto);
		log.debug("employeeCompleteDetailsDtoList formed is : {}", employeeCompleteDetailsDtoList);
		return employeeCompleteDetailsDtoList;
	}

	private void setCompleteEmployeeAndSubDetails(EmployeeCompleteDetailsDto employeeCompleteDetailsDto,
			Employee employee) {
		log.info("Inside Method setCompleteEmployeeAndSubDetails");
		setCompleteEmployeeDependentDetails(employeeCompleteDetailsDto, employee);
		setCompleteEmployeeEmergencyDetails(employeeCompleteDetailsDto, employee);
		setCompleteEmployeeComplianceDetails(employeeCompleteDetailsDto, employee);
		setCompleteNationalIdentificationDetails(employeeCompleteDetailsDto, employee);
		log.debug("Final employeeCompleteDetailsDto Object formed is : {} ", employeeCompleteDetailsDto);
	}

	private void setCompleteNationalIdentificationDetails(EmployeeCompleteDetailsDto employeeCompleteDetailsDto,
			Employee employee) {
		log.info("Inside Method setCompleteNationalIdentificationDetails");
		EmployeeNationalIdentificationService employeeNationalIdentificationService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeNationalIdentificationService.class);
		EmployeeNationalIdentification employeeNationalIdentification = employeeNationalIdentificationService
				.findNationalIdentificationDetailsOfEmployee(employee.getId());
		if (employeeNationalIdentification != null) {
			employeeCompleteDetailsDto.setNationalIdentificationDetails(employeeNationalIdentification);
		}
	}

	private void setCompleteEmployeeComplianceDetails(EmployeeCompleteDetailsDto employeeCompleteDetailsDto,
			Employee employee) {
		log.info("Inside Method setCompleteEmployeeComplianceDetails");
		EmployeeComplianceLegalService employeeComplianceLegalService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeComplianceLegalService.class);
		EmployeeComplianceLegal employeeComplianceLegal = employeeComplianceLegalService
				.findComplianceLegalDetailsOfEmployee(employee.getId());
		if (employeeComplianceLegal != null) {
			employeeCompleteDetailsDto.setComplianceDetails(employeeComplianceLegal);
		}
	}

	private void setCompleteEmployeeEmergencyDetails(EmployeeCompleteDetailsDto employeeCompleteDetailsDto,
			Employee employee) {
		log.info("Inside Method setCompleteEmployeeEmergencyDetails");
		EmployeeEmergencyContactService employeeEmergencyContactService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeEmergencyContactService.class);
		EmployeeEmergencyContact employeeEmergencyContact = employeeEmergencyContactService
				.findEmergencyContactOfEmployee(employee.getId());
		if (employeeEmergencyContact != null) {
			employeeCompleteDetailsDto.setEmergencyDetails(employeeEmergencyContact);
		}
	}

	private void setCompleteEmployeeDependentDetails(EmployeeCompleteDetailsDto employeeCompleteDetailsDto,
			Employee employee) {
		log.info("Inside Method setEmployeeDependentDetails");
		EmployeeDependentDetailsService employeeDependentService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeDependentDetailsService.class);
		List<EmployeeDependentDetails> employeeDependentDetails = employeeDependentService
				.findDependentDetailsOfEmployee(employee.getId());
		if (employeeDependentDetails != null && !employeeDependentDetails.isEmpty()) {
			employeeCompleteDetailsDto.setDependentDetails(employeeDependentDetails);
		}
	}

	private Employee getEmployeeByIdOrFullName(Integer id, String fullName) {
		if (id != null) {
			log.info(INSIDE_IF_TO_FETCH_EMPLOYEE_BY_ID);
			return employeeRepository.fetchById(id);
		} else {
			log.info("method@ getEmployeeByIdOrFullName Inside if to fetch Employee by Full Name ");
			Employee employee = employeeRepository.findByFullName(fullName);
			return (employee != null ? employee : null);
		}
	}

	@Override
	public List<EmployeeCompleteDetailsDto> updateEmployeeDetailsForAdmin(
			EmployeeCompleteDetailsDto employeeCompleteDetailsDto) {
		log.info("Inside Method updateEmployeeDetailsForAdmin");
		try {
			if (employeeCompleteDetailsDto != null) {
				if (employeeCompleteDetailsDto.getEmployee() != null) {
					employeeRepository.save(employeeCompleteDetailsDto.getEmployee());
				}
				if (employeeCompleteDetailsDto.getNationalIdentificationDetails() != null) {
					updateNationalIdentificationDetailsForAdmin(
							employeeCompleteDetailsDto.getNationalIdentificationDetails());
				}
				if (employeeCompleteDetailsDto.getEmergencyDetails() != null) {
					updateEmergencyContactForAdmin(employeeCompleteDetailsDto.getEmergencyDetails());
				}
				if (employeeCompleteDetailsDto.getComplianceDetails() != null) {
					updateEmployeeComplianceDetailForAdmin(employeeCompleteDetailsDto.getComplianceDetails());
				}
				if (employeeCompleteDetailsDto.getDependentDetails() != null
						&& !employeeCompleteDetailsDto.getDependentDetails().isEmpty()) {
					updateEmployeeDependentDetailsForAdmin(employeeCompleteDetailsDto.getDependentDetails());
				}
				return this.getEmployeeDetailsForAdmin(employeeCompleteDetailsDto.getEmployee().getId(),
						employeeCompleteDetailsDto.getEmployee().getFullName());
			}
			return new ArrayList<EmployeeCompleteDetailsDto>();
		} catch (Exception e) {
			String errorMessage = "Error inside @class EmployeeServiceImpl @method updateEmployeeDetailsForAdmin";
			throw new BusinessException(errorMessage, e);

		}
	}

	private void updateEmployeeDependentDetailsForAdmin(List<EmployeeDependentDetails> employeeDependentDetailsList) {
		log.info("Inside Method updateEmployeeDependentDetailsForAdmin");
		EmployeeDependentDetailsService employeeDependentService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeDependentDetailsService.class);
		for (EmployeeDependentDetails employeeDependent : employeeDependentDetailsList) {
			employeeDependentService.create(employeeDependent);
		}
	}

	private void updateEmployeeComplianceDetailForAdmin(EmployeeComplianceLegal employeeComplianceLegal) {
		log.info("Inside Method updateEmployeeComplianceDetailForAdmin");
		EmployeeComplianceLegalService employeeComplianceLegalService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeComplianceLegalService.class);
		if (employeeComplianceLegal != null) {
			employeeComplianceLegalService.create(employeeComplianceLegal);
		}
	}

	private void updateEmergencyContactForAdmin(EmployeeEmergencyContact employeeEmergencyContact) {
		log.info("Inside Method updateEmergencyContactForAdmin");
		EmployeeEmergencyContactService employeeEmergencyContactService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeEmergencyContactService.class);
		if (employeeEmergencyContact != null) {
			employeeEmergencyContactService.create(employeeEmergencyContact);
		}
	}

	private void updateNationalIdentificationDetailsForAdmin(
			EmployeeNationalIdentification employeeNationalIdentifiaction) {
		log.info("Inside Method updateNationalIdentificationDetailsForAdmin");
		EmployeeNationalIdentificationService employeeNationalIdentificationService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeNationalIdentificationService.class);
		if (employeeNationalIdentifiaction != null) {
			employeeNationalIdentificationService.create(employeeNationalIdentifiaction);
		}
	}

	@Override
	public List<EmployeeDataWrapper> findEmployeeByDateAndMonthWhenMatchDob() {
		try {
			log.info(INSIDE_EMPLOYEESERVICE_LOG);
			log.debug("Inside findEmployeeByDateAndMonthWhenMatchDob customerId is : {}", commonUtils.getCustomerId());
			List<Employee> employees = employeeRepository.findEmployeeByMatchOfDateAndMonthWithDob( commonUtils.getCustomerId());
			log.debug(INSIDE_EMPLOYEESERVICE_LOG + " employees :{}", employees);
			if (employees.isEmpty()) {
				throw new BusinessException("There is no Employee BirthDay On a Given Date");
			}
			List<EmployeeDataWrapper> result = new ArrayList<>();
			for (Employee employee : employees) {
				if (employee.getFullName() != null && employee.getDepartment().getName() != null) {
					EmployeeDataWrapper employeeDataWrapper = new EmployeeDataWrapper();
					employeeDataWrapper.setFullName(employee.getFullName());
					employeeDataWrapper.setDepartmentName(employee.getDepartment().getName());
					employeeDataWrapper.setTodayDate(getCurrentDate());
					employeeDataWrapper.setToEmail(employee.getWorkEmailAddress());
					result.add(employeeDataWrapper);
				} else {
					throw new BusinessException("For the present Employee FullName and DepartmentName is null");
				}
			}
			log.info(INSIDE_EMPLOYEESERVICE_LOG + " works successfully");
			return result;
		} catch (Exception e) {
			log.error(INSIDE_EMPLOYEESERVICE_LOG + " exception occurs :{} :{}", e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException(INSIDE_EMPLOYEESERVICE_LOG + "Exception occurs");
		}
	}

	private String getCurrentDate() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
		return sdf.format(now);
	}

	@Override
	public void sendBirthDayGreeting() {
		List<EmployeeDataWrapper> employeeDtos = findEmployeeByDateAndMonthWhenMatchDob();
		NotificationTemplate notificationTemplate = notificationIntegration
				.getTemplte(ConfigUtils.getString("GREETING_TEMPLATE_NAME"));
		log.debug("Inside @class employeeServiceimpl @method  sendBirthDayGreeting  notificationTemplate:{}",
				notificationTemplate);
		for (EmployeeDataWrapper employeeDto : employeeDtos) {
			JSONObject object = new JSONObject(employeeDto);
			notificationIntegration.sendEmail(notificationTemplate, object, employeeDto.getToEmail(), null,
					Collections.emptyList());
		}
	}

	@Override
	@Transactional
	public List<Employee> findEmployeesWhoCompletedThreeMonths() {
		log.debug("Inside findEmployeesWhoCompletedThreeMonths customerId is : {}", commonUtils.getCustomerId());
		return employeeRepository.findEmployeesWhoCompletedThreeMonths( commonUtils.getCustomerId());
	}

	@Override
	public Employee getEmployeeByCreator(Integer creator) {
		log.info("Inside @class employeeServiceimpl @method getEmployeeByCreator ");
		try {
			log.debug("Inside @class employeeServiceimpl @method getEmployeeByCreator customerId is : {}", commonUtils.getCustomerId());
			Optional<Employee> optionalEmployee = employeeRepository.findByCreator(creator, commonUtils.getCustomerId());
			if (optionalEmployee.isPresent()) {
				Employee employee = optionalEmployee.get();
				return employee;
			} else {
				log.info("Employee Not present ");
				return null;
			}
		} catch (Exception e) {
			log.error("Inside @class employeeServiceimpl @method getEmployeeByCreator :{} :{}", e.getMessage(),
					e.getStackTrace());
			throw new BusinessException();
		}
	}

	@Override
	public String importEmployeeData(MultipartFile excelFile) {
		log.info("Inside @class EmployeeServiceImpl @method importEmployeeData");

		try {
			uploadInputDocument(excelFile);
		} catch (Exception e) {
			log.error("Error while uploading input file");
		}
		CompletableFuture<Void> processFuture = CompletableFuture.runAsync(() -> {

			try (Workbook workbook = new XSSFWorkbook(excelFile.getInputStream());
					Workbook resultWorkbook = new XSSFWorkbook()) {

				// Create sheet for logging results
				Sheet resultSheet = resultWorkbook.createSheet("Import Results");
				Row resultHeaderRow = resultSheet.createRow(0);
				resultHeaderRow.createCell(0).setCellValue("EMPLOYEE_ID");
				resultHeaderRow.createCell(1).setCellValue("RESPONSE");

				Sheet sheet = workbook.getSheetAt(0);
				Row headerRow = sheet.getRow(0);
				Map<Integer, String> headerMap = new HashMap<>();

				for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
					headerMap.put(i, headerRow.getCell(i).getStringCellValue().trim().toUpperCase().replace(" ", "_"));
				}

				int resultRowIndex = 1;

				for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					if (row == null) {
						continue;
					}

					String employeeId = getCellValue(row, getHeaderIndex(headerMap, "EMPLOYEE_ID"));
					StringBuilder responseMessage = new StringBuilder();
					boolean updatedSuccessfully = true;
					Row resultRow = resultSheet.createRow(resultRowIndex++);
					log.debug("@method importEmployeeData Employee id is  : {}", employeeId);
					resultRow.createCell(0).setCellValue(employeeId);

					try {
						log.debug("@method importEmployeeData customerId is : {}", commonUtils.getCustomerId());
						Employee employee = employeeRepository.findByEmployeeId(employeeId, commonUtils.getCustomerId());
						log.debug("@method importEmployeeData Employee is : {}", employee);

						if (employee == null) {
							employee = new Employee();
						}

						setEmployeeIdForImport(employeeId, employee);

						updatedSuccessfully = setDataInEmployeeForMandatoryFields(headerMap, row, responseMessage,
								updatedSuccessfully, employee);

						updatedSuccessfully = setRemainingFieldInEmployee(headerMap, row, responseMessage,
								updatedSuccessfully, employee);

						employee.setGender(getCellValue(row, getHeaderIndex(headerMap, "GENDER")));
						employee.setMaritalStatus(getCellValue(row, getHeaderIndex(headerMap, "MARITAL_STATUS")));
						employee.setDateOfBirth(
								convertToDate(getCellValue(row, getHeaderIndex(headerMap, "DATE_OF_BIRTH"))));
						employee.setSourceHire(getCellValue(row, getHeaderIndex(headerMap, "SOURCE_HIRE")));
						employee.setAlternativePhoneNumber(
								getCellValue(row, getHeaderIndex(headerMap, "ALTERNATIVE_PHONE_NUMBER")));
						employee.setPersonalEmailAddress(
								getCellValue(row, getHeaderIndex(headerMap, "PERSONAL_EMAIL_ADDRESS")));
						employee.setBloodGroup(getCellValue(row, getHeaderIndex(headerMap, "BLOOD_GROUP")));
						employee.setPersonalMobileNumber(
								getCellValue(row, getHeaderIndex(headerMap, "PERSONAL_MOBILE_NUMBER")));
						employee.setDateOfExit(
								convertToDate(getCellValue(row, getHeaderIndex(headerMap, "DATE_OF_EXIT"))));
						employee.setCitizenship(getCellValue(row, getHeaderIndex(headerMap, "CITIZENSHIP")));
						employee.setCompany(getCellValue(row, getHeaderIndex(headerMap, "COMPANY")));
						employee.setPrimaryShortAddress(
								getCellValue(row, getHeaderIndex(headerMap, "PRIMARY_SHORT_ADDRESS")));
						employee.setPrimaryAddressBuildingNumber(
								getCellValue(row, getHeaderIndex(headerMap, "PRIMARY_ADDRESS_BUILDING_NUMBER")));
						employee.setPrimaryAddressPostalCode(
								getCellValue(row, getHeaderIndex(headerMap, "PRIMARY_ADDRESS_POSTAL_CODE")));
						employee.setPersonalEmailAddress(
								getCellValue(row, getHeaderIndex(headerMap, "PERSONAL_EMAIL_ADDRESS")));
						employee.setPrimaryAddressCity(
								getCellValue(row, getHeaderIndex(headerMap, "PRIMARY_ADDRESS_CITY")));
						employee.setPrimaryAddressCountry(
								getCellValue(row, getHeaderIndex(headerMap, "PRIMARY_ADDRESS_COUNTRY")));
						employee.setCountryOfResidence(
								getCellValue(row, getHeaderIndex(headerMap, "COUNTRY_OF_RESIDENCE")));

						log.debug("@method Employee to be save : {}", employee);

						if (updatedSuccessfully) {
							employeeRepository.save(employee);
							responseMessage.append("Employee Import Successfully");
						}

					} catch (BusinessException e) {
						log.error("Error processing employee with EMP_ID: " + employeeId, e);
						responseMessage.append("Not Updated: ").append(e.getMessage()).append("; ");
						updatedSuccessfully = false;
					} catch (Exception e) {
						log.error("Unexpected error for employee with EMP_ID: " + employeeId, e);

						updatedSuccessfully = false;
					}

					if (!updatedSuccessfully) {
						responseMessage.insert(0, "Not Updated: ");
					}

					resultRow.createCell(1).setCellValue(responseMessage.toString());
				}

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				resultWorkbook.write(bos);

				ByteArrayInputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
				MultipartFile resultFile = new MockMultipartFile(IMPORTEMPLOYEE_RESULTS_XLSX, inputStream);
				log.info("Going to upload file in document");
				Integer documentId = documentIntegrationService
						.uploadProtectedAndUnProtectedFileOnDocument(IMPORTEMPLOYEE_RESULTS_XLSX, resultFile);

				log.debug("Document id of ResultFile is: {}", documentId);

				User contextUser = getUserContext();

				JSONObject object = createCompletionNotificationData();
				sendEmailToEmployee(documentId, contextUser, object);

			} catch (IOException e) {
				log.error("Error reading Excel file: " + excelFile.getOriginalFilename(), e);

			}
		});
		return APIConstants.SUCCESS_JSON;
	}

	private void setEmployeeIdForImport(String employeeId, Employee employee) {
		if (employeeId != null) {
			log.debug("Set EmployeeId is : {}", employeeId);
			employee.setEmployeeId(employeeId);
		}
	}

	private void sendEmailToEmployee(Integer documentId, User contextUser, JSONObject object) {
		if (documentId != null && contextUser != null && contextUser.getEmail() != null) {
			String attachmentFilePath = ConfigUtils.getPlainString("ATTACHMENT_FILE_PATH") + documentId;
			List<NotificationAttachment> notificationAttachmentList = new ArrayList<>();
			NotificationAttachment notificationAttachment = new NotificationAttachment();
			notificationAttachment.setIsPublic(true);
			notificationAttachment.setPath(attachmentFilePath);
			notificationAttachment.setName(IMPORTEMPLOYEE_RESULTS_XLSX);
			notificationAttachmentList.add(notificationAttachment);
			log.debug("Notification Attachment List is : {}", notificationAttachmentList);
			NotificationTemplate template = notificationIntegration.getTemplte("Employee_Import");
			notificationIntegration.sendEmail(template, object,contextUser.getEmail(), null,
					notificationAttachmentList);
		}
	}

	private boolean setRemainingFieldInEmployee(Map<Integer, String> headerMap, Row row, StringBuilder responseMessage,
			boolean updatedSuccessfully, Employee employee) {
		String financeDepartment = getCellValue(row, getHeaderIndex(headerMap, "FINANCE_DEPARTMENT(COST_CENTER_CODE)"));
		if (financeDepartment == null) {
			log.error("Finance Department (Cost Center Code) cannot be null");
			responseMessage.append("Finance Department (Cost Center Code) is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("Finance Department (Cost Center Code): {}", financeDepartment);
			employee.setText1(financeDepartment);
		}

		String locationName = getCellValue(row, getHeaderIndex(headerMap, "OFFICE_LOCATION"));
		log.debug("setRemainingFieldInEmployee customerId is : {}", commonUtils.getCustomerId());
		Location location = locationRepository.findByName(locationName, commonUtils.getCustomerId());
		log.debug("@method importEmployeeData location is : {}", location);
		if (location == null) {
			log.error("location not found for Employee");
			responseMessage.append("location not found for Employee, ");
			updatedSuccessfully = false;

		} else {
			employee.setLocation(location);
		}

		String employmentType = getCellValue(row, getHeaderIndex(headerMap, "EMPLOYMENT_TYPE"));
		if (employmentType == null) {
			log.error("Employment Type cannot be null");
			responseMessage.append("Employment Type is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("Employment Type: {}", employmentType);
			employee.setEmploymentType(employmentType);
		}

		employee.setEmploymentStatus(getCellValue(row, getHeaderIndex(headerMap, "EMPLOYMENT_STATUS")));

		String dateOfJoiningStr = getCellValue(row, getHeaderIndex(headerMap, "DATE_OF_JOINING"));
		if (dateOfJoiningStr == null) {
			log.error("Date Of Joining cannot be null");
			responseMessage.append("Date Of Joining is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			employee.setDateOfJoining(convertToDate(dateOfJoiningStr));
		}

		String reportingManagerName = getCellValue(row, getHeaderIndex(headerMap, "REPORTING_MANAGER"));
		Employee employee1 = findEmployeeByFullName(reportingManagerName);
		log.debug("@method importEmployeeData employee is : {}", employee1);
		if (employee1 != null) {
			log.debug("Reporting manager is : {}", employee1);
			employee.setReportingManager(employee1);
		}

		String workPhoneNumber = getCellValue(row, getHeaderIndex(headerMap, "WORK_PHONE_NUMBER"));
		if (workPhoneNumber == null) {
			log.error("Work Phone Number cannot be null");

			responseMessage.append("Work Phone Number is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			employee.setWorkPhoneNumber(workPhoneNumber);
		}

		String email = getCellValue(row, getHeaderIndex(headerMap, "WORK_EMAIL_ADDRESS"));
		if (email == null) {
			log.error("Work Email cannot be null");
			responseMessage.append("Work Email is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			employee.setWorkEmailAddress(email);
		}
		String designationName = getCellValue(row, getHeaderIndex(headerMap, "DESIGNATION_NAME"));
		log.debug("setRemainingFieldInEmployee customerId is : {}", commonUtils.getCustomerId());
		List<Designation> designationList = designationRepository.findByName(designationName, commonUtils.getCustomerId());
		log.debug("@method importEmployeeData Department is : {}", designationList);
		if (designationList == null) {
			log.error("Designation not found for Employee");
			responseMessage.append("Designation not found for Employee, ");
			updatedSuccessfully = false;

		} else {
			employee.setDesignation(designationList.get(0));
		}

		String contractType = getCellValue(row, getHeaderIndex(headerMap, "CONTRACT_TYPE"));
		if (contractType == null) {
			log.error("contractType cannot be null");
			responseMessage.append("contractType is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			employee.setContractType(contractType);
		}
		return updatedSuccessfully;
	}

	private boolean setDataInEmployeeForMandatoryFields(Map<Integer, String> headerMap, Row row,
			StringBuilder responseMessage, boolean updatedSuccessfully, Employee employee) {
		String firstName = getCellValue(row, getHeaderIndex(headerMap, "FIRST_NAME"));
		log.debug("First Name: {}", firstName);
		if (firstName == null) {
			log.error("First Name cannot be null");
			responseMessage.append("First Name is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("First Name: {}", firstName);
			employee.setFirstName(firstName);
		}

		String middleName = getCellValue(row, getHeaderIndex(headerMap, "MIDDLE_NAME"));
		log.debug("Middle Name: {}", middleName);
		if (null == middleName) {
			log.error("Middle Name cannot be null");
			responseMessage.append("Middle Name is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("Middle Name: {}", middleName);
			employee.setMiddleName(middleName);
		}

		String lastName = getCellValue(row, getHeaderIndex(headerMap, "LAST_NAME"));
		log.debug("Last Name: {}", lastName);
		if (lastName == null) {
			log.error("Last Name cannot be null");
			responseMessage.append("Last Name is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("Last Name: {}", lastName);
			employee.setLastName(lastName);
		}

		String fullName = getCellValue(row, getHeaderIndex(headerMap, "FULL_NAME"));
		log.debug("Full Name: {}", fullName);
		if (fullName == null) {
			log.error("Full Name cannot be null");
			responseMessage.append("Full Name is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("Full Name: {}", fullName);
			employee.setFullName(fullName);
		}

		String arabicFirstName = getCellValue(row, getHeaderIndex(headerMap, "ARABIC_FIRST_NAME"));
		log.debug("Arabic First Name: {}", arabicFirstName);
		if (arabicFirstName == null) {
			log.error("Arabic First Name cannot be null");
			responseMessage.append("Arabic First Name is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("Arabic First Name: {}", arabicFirstName);
			employee.setArabicFirstName(arabicFirstName);
		}

		String arabicMiddleName = getCellValue(row, getHeaderIndex(headerMap, "ARABIC_MIDDLE_NAME"));
		log.debug("Arabic Middle Name: {}", arabicMiddleName);
		if (arabicMiddleName == null) {
			log.error("Arabic Middle Name cannot be null");
			responseMessage.append("Arabic Middle Name is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("Arabic Middle Name: {}", arabicMiddleName);
			employee.setArabicMiddleName(arabicMiddleName);
		}

		String arabicLastName = getCellValue(row, getHeaderIndex(headerMap, "ARABIC_LAST_NAME"));
		log.debug("Arabic Last Name: {}", arabicLastName);
		if (arabicLastName == null) {
			log.error("Arabic Last Name cannot be null");
			responseMessage.append("Arabic Last Name is Mandatory/It cannot be Empty or Blank, ");
			updatedSuccessfully = false;

		} else {
			log.debug("Arabic Last Name: {}", arabicLastName);
			employee.setArabicLastName(arabicLastName);
		}

		String departmentName = getCellValue(row, getHeaderIndex(headerMap, "DEPARTMENT_NAME"));
		Department department = departmentRepository.findByName(departmentName);
		log.debug("@method importEmployeeData Department is : {}", department);
		if (department == null) {
			log.error("Department not found for Employee");
			responseMessage.append("Department not found for Employee , ");
			updatedSuccessfully = false;

		} else {
			employee.setDepartment(department);
		}
		return updatedSuccessfully;
	}

	private void uploadInputDocument(MultipartFile excelFile)
			throws IOException, JsonProcessingException, JsonMappingException {
		Integer documentIdofInputFile = documentIntegrationService
				.uploadProtectedAndUnProtectedFileOnDocument("importemployee_input.xlsx", excelFile);

		log.debug("Document id of DocumentId of InputFile is: {}", documentIdofInputFile);
	}

	private int getHeaderIndex(Map<Integer, String> headerMap, String headerName) {
		log.info("Inside @class EmployeeServiceImpl @method getHeaderIndex");
		for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
			log.info("Inside @class EmployeeServiceImpl @method getHeaderIndex ,entry : {} ,  headerName : {}", entry,
					headerName);
			if (entry.getValue().equalsIgnoreCase(headerName)) {
				return entry.getKey();
			}
		}
		log.error("Header not found with name : {} ", headerName);
		throw new BusinessException("Header '" + headerName + "' not found");
	}

	private String getCellValue(Row row, int columnIndex) {
		log.info("Inside @class EmployeeServiceImpl @method getCellValue");
		Cell cell = row.getCell(columnIndex);
		log.debug("@method getCellValue cell value is : {}", cell);

		if (cell == null) {
			return null;
		}

		String cellValue = null;

		switch (cell.getCellType()) {
		case STRING:
			cellValue = cell.getStringCellValue().trim();
			if (cellValue.isEmpty()) {
				return null; // Treat empty string as null
			}
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				// Handle date formatting
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				cellValue = dateFormat.format(cell.getDateCellValue());
			} else {
				// Handle large numbers like phone numbers
				BigDecimal bigDecimalValue = BigDecimal.valueOf(cell.getNumericCellValue());
				cellValue = bigDecimalValue.toPlainString(); // Avoid scientific notation
			}
			break;
		case BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case BLANK:
			cellValue = null;
			break;
		default:
			cellValue = null;
		}

		log.debug("@method getCellValue Parsed Value is : {}", cellValue);
		return cellValue;
	}

	private Date convertToDate(String dateStr) {
		log.info("Inside @class EmployeeServiceImpl @method convertToDate");
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error("Invalid date format");
			// throw new BusinessException("Invalid date format: " + dateStr);
		}
		return null;
	}

	@Override
	public Employee findEmployeeByFullName(String name) {
		log.info("Inside @class EmployeeServiceImpl @method findEmployeeByFullName");
		try {
			log.debug("Inside @class EmployeeServiceImpl @method findEmployeeByFullName customerId is : {}", commonUtils.getCustomerId());
			Employee employee = employeeRepository.findEmployeeByFullName(name, commonUtils.getCustomerId());
			return employee;
		} catch (BusinessException ex) {
			log.error("Error while finding Employee By Full Name");
		}
		return null;
	}

	private JSONObject createCompletionNotificationData() {
		log.info("Inside @class EmployeeServiceImpl @method createCompletionNotificationData");
		JSONObject object = new JSONObject();
		User contextUser = getUserContext();

		if (contextUser != null && contextUser.getFullName() != null) {
			log.debug("FullName of recipient is : {}", contextUser.getFullName());
			object.put("recipient", contextUser.getFullName());
		}

		return object;
	}

	@Override
	public Employee findByEmployeeId(String employeeId) {
		log.info("Inside @class EmployeeServiceImpl @method findByEmployeeId");
		try {
			log.debug("Inside @class EmployeeServiceImpl @method findByEmployeeId customerId is : {}", commonUtils.getCustomerId());
			Employee employee = employeeRepository.findByEmployeeId(employeeId, commonUtils.getCustomerId());
			log.debug("Inside @method findByEmployeeId Employee is : {}", employee);
			return employee;
		} catch (BusinessException ex) {
			log.error("Error while finding Employee By EmployeeId");
		}
		return null;
	}

	@Override
	public List<Employee> getEmployeeByEmploymentTypeList(String[] list) {
		log.info("Inside @class EmployeeServiceImpl @method getEmployeeByEmploymentTypeList ");
		try {
			log.debug("Inside @class EmployeeServiceImpl getEmployeeByEmploymentTypeList customerId is : {}", commonUtils.getCustomerId());
			List<Employee> employeeList = employeeRepository.findByEmploymentTypeList(list, commonUtils.getCustomerId());
			log.debug("Size of Employee list :{} ", employeeList.size());
			return employeeList;
		} catch (Exception e) {
			log.error("Error inside @class EmployeeServiceImpl @method getEmployeeByEmploymentTypeList :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public String deleteEmployeeCorrespondingData(Integer id) {
		log.debug("Inside deleteEmployeeCorrespondingData employeeId:{}",id);
		try {
		log.debug("deleteEmployeeCorrespondingData customerId is : {}", commonUtils.getCustomerId());
		log.info("Going to delete EmployeeCertifications");
		employeeCertificationRepository.deleteCertificationsByEmployeeId(id, commonUtils.getCustomerId());
		log.info("Completed deleting EmployeeCertifications");
		
		log.info("Going to delete employeeDependentDetails ");
		employeeDependentDetailsRepository.deleteDependentDetailsByEmployeeId(id, commonUtils.getCustomerId());
		log.info("Completed deleting employeeDependentDetails");
		
		log.info("Going to delete EmployeeLanguage ");
		employeeLanguageRepository.deleteLanguageByEmployeeId(id,commonUtils.getCustomerId());
		log.info("Completed deleting EmployeeLanguage ");
		
		log.info("Going to delete EmployeeNationalIdentification");
		employeeNationalIdentificationRepository.deleteNationalIdentificationByEmployeeId(id,commonUtils.getCustomerId());
		log.info("Completed deleting EmployeeNationalIdentification ");
		
		log.info("Going to delete EmployeeWorkExperience ");
		employeeWorkExperienceRepository.deleteWorkExperienceByEmployeeId(id,commonUtils.getCustomerId());
		log.info("Completed deleting EmployeeWorkExperience ");
		
		log.info("Going to delete Employeeeducationdetails ");
		employeeeducationdetailsRepository.deleteEducationByEmployeeId(id, commonUtils.getCustomerId());
		log.info("Completed deleting Employeeeducationdetails");
		
		employeeRepository.deleteEmployeeSkillMapping(id, commonUtils.getCustomerId());
		log.info("Completed deleting EmployeeeSkillMapping");
		employeeRepository.deleteById(id);
		log.info("Inside deleteEmployeeCorrespondingData employee deleted ");
		
		return APIConstants.SUCCESS_JSON;
		
		}catch(Exception e) {
			log.error("Error occured while deleting employee and its corresponding data:{}",e);
		}
		
		return APIConstants.FAILURE_JSON;
	}

	@Override
	public List<com.nouros.hrms.model.User> getUserFromName(List<String> userName) {
		log.debug("Inside getUserFromName userName:{}",userName.size());
		try {
	        String sql = "SELECT * FROM User u WHERE u.USERNAME IN :userName";
	        Query query = entityManager.createNativeQuery(sql, com.nouros.hrms.model.User.class);
	        query.setParameter("userName", userName);
	        @SuppressWarnings("unchecked")
	        List<com.nouros.hrms.model.User> userlist = (List<com.nouros.hrms.model.User>) query.getResultList();
	        log.debug("Inside getUserFromName  userlist:{}",userlist.size());
	        return userlist;
	    } catch (NoResultException e) {
	        log.warn("No user found with username: {}", userName);
	        return null;  
	    } catch (Exception e) {
	        log.error("Error occurred while fetching user by username: {}", Utils.getStackTrace(e));
	        return null; 
	    }
	}


	@Override
	public String getDesignationLevelByUserId(Integer userId) {
		log.info("Inside @Class EmployeeServiceImpl @method getDesignationLevelByUserId");
		log.info("Inside @Class EmployeeServiceImpl @method getDesignationLevelByUserId userId :{}",userId);
		Employee employee = employeeRepository.findByUserId(userId);
		if(employee !=null){
			return employee.getDesignation().getJobLevel();
		}
		else return "employee is null";
	}
	
	
	@Override
	public List<String> getDepartmentCodeForEmployees()
	{
		try
		{
		  log.info("Inside @class EmployeeServiceImpl @method getDepartmentCodeForEmployees ");
		   //log.debug(" Inside @getDepartmentCodeForEmployees  customerId is : {}");
		  List<String> departmentCodes = employeeRepository.getAllDepartmentCode();
		  return departmentCodes;
		}
		catch(Exception e)
		{
			log.error("Error In getting DepartmentCode from Employee :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	@Override
	 public List<Employee> getEmployeesByDepartmentCode(String departmentCode,Integer payrollRunId,String[] typeList)
	 {
		try {
		   log.debug("Inside @class EmployeeServiceImpl @method getEmployeesByDepartmentCode :{}",departmentCode);	
		 //  log.debug(" Inside @getEmployeesByDepartmentCode  customerId is : {}", commonUtils.getCustomerId());
		   List<Employee> employeeList = employeeRepository.getEmployeeByDepartmentCode(departmentCode,payrollRunId,typeList);
		   log.debug(" Size of Employee List :{} for Code :{} ",employeeList.size(),departmentCode);
		   return employeeList;
		}
		catch(Exception e)
		{
			log.error("Error @class EmployeeServiceImpl @method getEmployeesByDepartmentCode  :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	 }
	
	@Override
	public String addEmployeeInMilvus() {
		try {
			log.info("Inside @class EmployeeServiceImpl @method createEmployeeInMilvus ");
			//Employee employee = employeeRepository.fetchById(employeeId);
			List<Employee> employeeList = employeeRepository.findAll();
			List<Document> documents = new ArrayList<>();
			for (Employee employee : employeeList) {
				Map<String, Object> employeeMap = createEmployeeMap(employee);
				String employeeSkillsString = getSkillsInString(employee.getSkills());
				Document document = new Document(employeeSkillsString);
				document.getMetadata().putAll(employeeMap);
				documents.add(document);
			}
			VectorMetaData vectorMetaData = new VectorMetaData();
		 
			vectorMetaData.setCollectionName("HRMS_employee");
			vectorService.saveDocuments(vectorMetaData, documents);
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.error("Error @class EmployeeServiceImpl @method createEmployeeInMilvus  :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
 
	private Map<String,Object> createEmployeeMap(Employee employee)
	{
	 try
	 {
			log.debug("Employee is : {}", employee);
			Map<String,Object> employeeMap = new HashMap<>();
			addValueIfNotNull(employeeMap,"employee_id",employee.getId().longValue());
			 
			addValueIfNotNull(employeeMap,"job_grade",employee.getJobGrade());
			addValueIfNotNull(employeeMap,"performance_score",null);
			addValueIfNotNull(employeeMap,"department_name",employee.getDepartment().getName());
			addValueIfNotNull(employeeMap,"competencies_average",null);
			addValueIfNotNull(employeeMap,"experiance",null);
		    addValueIfNotNull(employeeMap,"current_position",employee.getDesignation().getName());
		    log.debug("employeeMap formed is : {} ", employeeMap);
		    return employeeMap;
	 }catch(Exception e)
	 {
			log.error("Error @class EmployeeServiceImpl @method createEmployeeJson  :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
	 }
	}
	
	private void addValueIfNotNull(Map<String,Object> employeeMap, String key, Object value) {
		if (value != null) {
			employeeMap.put(key, value);
		} else {
			employeeMap.put(key, " ");
		}
	}
	private String getSkillsInString(List<Skill> skills)
	{
		try
		{
			log.debug("Skills :{} ",skills);
			String stringSkill = "";
			for(Skill skill : skills)
			{
				stringSkill= stringSkill.concat(skill.getName());
			}
			if(stringSkill.equalsIgnoreCase(""))
			{
				stringSkill = "AI/ML";
			}
			log.debug("Inside @method getSkillsInString :{}",stringSkill);
			return stringSkill;
		}
		catch(Exception e)
		{
			log.error("Error inside @method getSkillsInString :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}	
	
}
