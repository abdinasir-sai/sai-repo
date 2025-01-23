package com.nouros.hrms.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.usermanagement.user.wrapper.UserProviderWrapper;
import com.nouros.hrms.controller.DepartmentController;
import com.nouros.hrms.controller.DesignationController;
import com.nouros.hrms.controller.DivisionController;
import com.nouros.hrms.controller.EmployeeComplianceLegalController;
import com.nouros.hrms.controller.EmployeeController;
import com.nouros.hrms.controller.EmployeeDependentDetailsController;
import com.nouros.hrms.controller.EmployeeEmergencyContactController;
import com.nouros.hrms.controller.EmployeeNationalIdentificationController;
import com.nouros.hrms.controller.LocationController;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.Division;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeComplianceLegal;
import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.model.EmployeeEmergencyContact;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.Location;
import com.nouros.hrms.repository.LocationRepository;
import com.nouros.hrms.service.DivisionService;
import com.nouros.hrms.wrapper.EmployeeCompleteDetailsDto;
import com.nouros.hrms.wrapper.EmployeeComplianceWrapper;
import com.nouros.hrms.wrapper.EmployeeDependentWrapper;
import com.nouros.hrms.wrapper.EmployeeDetailsDto;
import com.nouros.hrms.wrapper.EmployeeEmergencyWrapper;
import com.nouros.hrms.wrapper.EmployeeNationalWrapper;
import com.nouros.hrms.wrapper.EmployeeOrgChartDto;
import com.nouros.hrms.wrapper.OrgnisationDto;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.product.security.wrapper.UserWrapper;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class EmployeeControllerTest extends SpringJUnitRunnerTest {

	private static final Logger log = LogManager.getLogger(EmployeeControllerTest.class);

	@Autowired
	private EmployeeController controller;

	@Autowired
	private EmployeeComplianceLegalController employeeComplianceLegalController;

	@Autowired
	private EmployeeDependentDetailsController employeeDependentDetailsController;

	@Autowired
	private EmployeeEmergencyContactController employeeEmergencyContactController;

	@Autowired
	private DivisionController divisionController;

	@Autowired
	private DepartmentController departmentController;

	@Autowired
	private DesignationController designationController;

	@Autowired
	private LocationController loactionController;

	@MockBean
	private ICustomNumberValuesRest customNumberValuesRest;

	@MockBean
	private LocationRepository locationRepository;

	@MockBean
	private CustomerInfo customerInfo;

	private Employee employee;

	@MockBean
	private UserRest userRestService;

	@MockBean
	private DivisionService divisionService;

	@Autowired
	private EmployeeNationalIdentificationController employeeNationalIdentificationController;



	@BeforeEach
	void setup() {
		UserWrapper userWrapper = Mockito.mock(UserWrapper.class);
		userWrapper.setActiveUserSpaceId(1);
		when(customerInfo.getCustomerInfo()).thenReturn(userWrapper);
		when(userWrapper.getActiveUserSpaceId()).thenReturn(1);

		employee = new Employee();
		employee.setFirstName("Employee");
		employee.setLastName("Test");
		employee.setEmploymentType("FULL_TIME");
		employee.setWorkEmailAddress("test@example.com");
		employee.setDateOfBirth(new Date());
	}

	@Test
	@Order(0)
	void testCreateWithNaming() {
		Map<String, String> userData = new HashMap<>();
		userData.put("key", "value");
		when(userRestService.createFromIDP(any())).thenReturn(userData);
		User user = new User();
		user.setUserid(1);
		when(userRestService.byUserName(anyString())).thenReturn(user);
		Employee createdEmployee = controller.create(employee);
		assertNotNull(createdEmployee);
		assertEquals("EmployeeTest", createdEmployee.getFullName());
	}

	Employee getEmployee() {
		Map<String, String> userData = new HashMap<>();
		userData.put("key", "value");
		when(userRestService.createFromIDP(any())).thenReturn(userData);
		User user = new User();
		user.setUserid(1);
		when(userRestService.byUserName(anyString())).thenReturn(user);
		return controller.create(employee);
	}

	@Test
	@Order(2)
	void testCreateWithNaming_UserCreationSuccess() {
		Map<String, String> userData = new HashMap<>();
		userData.put("key", "value");
		UserProviderWrapper userProviderWrapper = new UserProviderWrapper();
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
		when(userRestService.createFromIDP(userProviderWrapper)).thenReturn(userData);
		User user = new User();
		user.setUserid(1);
		when(userRestService.byUserName(anyString())).thenReturn(user);
		Department department = new Department();
		department.setName("Business Unit");
		department = departmentController.create(department);
		employee.setDepartment(department);
		Designation designation = new Designation();
		designation.setName("IT");
		designation = designationController.create(designation);
		employee.setDesignation(designation);
		NameGenerationWrapperV2 nameGenerationWrapperV2 = new NameGenerationWrapperV2();
		nameGenerationWrapperV2.setGeneratedName("LOC-001");
		when(customNumberValuesRest.generateNameAndFriendlyName(eq("locationRuleDynamic"), anyMap(),
				eq(Status.ALLOCATED))).thenReturn(nameGenerationWrapperV2);
		Location location = new Location();
		location.setCountry("India");
		location.setStateProvince("test");
		location.setLocationId("LOC-001");
		location.setName("Test");
		when(locationRepository.save(any(Location.class))).thenReturn(location);
		Location locationToCreate = new Location();
		locationToCreate.setCountry("India");
		locationToCreate.setStateProvince("test");
		Location createdLocation = loactionController.create(locationToCreate);
		employee.setLocation(createdLocation);
		Employee createdEmployee = controller.create(employee);
		assertNotNull(createdEmployee);
		assertEquals("EmployeeTest", createdEmployee.getFullName());
		assertEquals(1, createdEmployee.getUserId());

	}

	@Test
	@Order(2)
	void update() {
		Employee employee1 = new Employee();
		employee1.setFirstName("Employee Test updated");
		employee1 = controller.update(employee1);
		assertNotEquals(null, employee1);
	}

	@Test
	@Order(3)
	void count() {
		long count = controller.count("id=ge=0");
		assertTrue(count >= 0, "The count should be non-negative");
	}

	@Test
	@Order(4)
	void findById() {
		Employee employee2 = getEmployee();
		Employee employeeList = controller.findById(employee2.getId());
		assertNotNull(employeeList, "The employee found by ID should not be null");
	}

	@Test
	@Order(5)
	void deleteById() {
		Employee employee3 = getEmployee();
		controller.deleteById(employee3.getId());
		Employee employeeList = controller.findById(employee3.getId());
		assertNotNull(employeeList, "The list of divisions should not be null");

	}

	@Test
	@Order(6)
	void search() {
		getEmployee();
		List<Employee> list = controller.search("id=ge=0", 1, 10, "modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
	}

	


	@Test
	@Order(7)
	void testGetOrgChart() {
		Division division = new Division();
		Division divisionNew = new Division();
		division.setName("Business Unit");
		division = divisionController.create(division);
		divisionNew.setName("divisionNew");
		divisionController.create(divisionNew);
		Department ceoDepartment = new Department();
		ceoDepartment.setName("CEO");
		ceoDepartment.setDivision(division);
		ceoDepartment = departmentController.create(ceoDepartment);
		Map<String, String> userData = new HashMap<>();
		userData.put("key", "value");
		when(userRestService.createFromIDP(any())).thenReturn(userData);
		User user = new User();
		user.setUserid(1);
		when(userRestService.byUserName(anyString())).thenReturn(user);
		employee.setDepartment(ceoDepartment);
		employee.setCountryOfResidence("India");
		 controller.create(employee);
		Department hrDepartment = new Department();
		hrDepartment.setName("HR");
		hrDepartment.setDivision(division);
	   departmentController.create(hrDepartment);
		controller.getOrgChart("HR");
		OrgnisationDto orgChartCEO = controller.getOrgChart("CEO");
		assertNotNull(orgChartCEO);
	}

	@Test
	@Order(8)
	void getEmployeeOrgChart() {
		Division division = new Division();
		Division divisionNew = new Division();
		division.setName("Business Unit");
		division = divisionController.create(division);
		divisionNew.setName("divisionNew");
		divisionController.create(divisionNew);
		Department ceoDepartment = new Department();
		ceoDepartment.setName("CEO");
		ceoDepartment.setDivision(division);
		ceoDepartment = departmentController.create(ceoDepartment);
		Map<String, String> userData = new HashMap<>();
		userData.put("key", "value");
		when(userRestService.createFromIDP(any())).thenReturn(userData);
		User user = new User();
		user.setUserid(1);
		when(userRestService.byUserName(anyString())).thenReturn(user);
		employee.setDepartment(ceoDepartment);
		employee.setCountryOfResidence("India");
		 controller.create(employee);
		Department hrDepartment = new Department();
		hrDepartment.setName("HR");
		hrDepartment.setDivision(division);
		departmentController.create(hrDepartment);
		EmployeeOrgChartDto org = controller.getEmployeeOrgChart("EmployeeTest",140294);
		assertNotNull(org);

	}

	@Test
	@Order(9)
	void getSelfEmployeeDetails() {
		Division division = new Division();
		Division divisionNew = new Division();
		division.setName("Business Unit");
		division = divisionController.create(division);
		divisionNew.setName("divisionNew");
		divisionController.create(divisionNew);
		Department ceoDepartment = new Department();
		ceoDepartment.setName("CEO");
		ceoDepartment.setDivision(division);
		ceoDepartment = departmentController.create(ceoDepartment);
		Map<String, String> userData = new HashMap<>();
		userData.put("key", "value");
		when(userRestService.createFromIDP(any())).thenReturn(userData);
		User user = new User();
		user.setUserid(1);
		when(userRestService.byUserName(anyString())).thenReturn(user);
		employee.setDepartment(ceoDepartment);
		employee.setCountryOfResidence("India");
		employee.setUserId(1);
		Employee emp = controller.create(employee);
		Department hrDepartment = new Department();
		hrDepartment.setName("HR");
		hrDepartment.setDivision(division);
		 departmentController.create(hrDepartment);
		controller.getSelfEmployeeDetails(emp.getId(), null);
		EmployeeDetailsDto assertNotNull = controller.getSelfEmployeeDetails(null, emp.getUserId());
		assertNotNull(assertNotNull);
	}


	@Test
	@Order(10)
	void testUpdateSelfEmployeeDetails() {
		Employee emp = getEmployee();
		EmployeeDetailsDto employeeDetailsDto = new EmployeeDetailsDto();
		employeeDetailsDto.setId(emp.getId());
		employeeDetailsDto.setUserId(1);
		employeeDetailsDto.setFirstName("John");
		employeeDetailsDto.setMiddleName("Doe");
		employeeDetailsDto.setLastName("Doe");
		employeeDetailsDto.setGivenName("John Doe");
		employeeDetailsDto.setPersonalEmailAddress("john.doe@example.com");
		employeeDetailsDto.setPersonalMobileNumber("1234567890");
		employeeDetailsDto.setMaritalStatus("Single");
		employeeDetailsDto.setCountryOfResidence("USA");
		employeeDetailsDto.setReligion("None");
		employeeDetailsDto.setPrimaryAddressBuildingNumber("123");
		employeeDetailsDto.setPrimaryAddressCity("New York");
		employeeDetailsDto.setPrimaryAddressCountry("USA");
		employeeDetailsDto.setPrimaryAddressPostalCode("10001");
		employeeDetailsDto.setPrimaryShortAddress("123 Main St, New York, NY 10001");
		employeeDetailsDto.setReportingManagersUserId(3);
		employeeDetailsDto.setReportingManagersFullName("Jane Smith");

		EmployeeNationalIdentification eni = new EmployeeNationalIdentification();
		eni.setIdentificationNumber("123456789");
		eni.setBorderNumber("0000");
		eni.setType("type");
		eni.setExpiryDate(null);
		eni.setScannedImage("Image");
		eni = employeeNationalIdentificationController.create(eni);
		EmployeeNationalWrapper nationalWrapper = new EmployeeNationalWrapper();
		nationalWrapper.setIdentificationNumber("123456789");
		nationalWrapper.setId(eni.getId());
		nationalWrapper.setBorderNumber("0000");
		nationalWrapper.setType("type");
		nationalWrapper.setExpiryDate(null);
		nationalWrapper.setScannedImage("Image");
		employeeDetailsDto.setEmployeeNational(nationalWrapper);

		EmployeeEmergencyContact ec = new EmployeeEmergencyContact();
		ec.setEmergencyContactNumber("1111");
		ec.setEmergencyContactEmailAddress("");
		ec.setEmergencyContactMiddleName("");
		ec.setRelationship("");
		ec.setEmergencyContactFirstName("Jane");
		ec.setEmergencyContactLastName("Doe");
		ec = employeeEmergencyContactController.create(ec);
		EmployeeEmergencyWrapper emergencyWrapper = new EmployeeEmergencyWrapper();
		emergencyWrapper.setId(ec.getId());
		emergencyWrapper.setEmergencyContactNumber("1111");
		emergencyWrapper.setEmergencyContactEmailAddress("");
		emergencyWrapper.setEmergencyContactMiddleName("");
		emergencyWrapper.setRelationship("");
		emergencyWrapper.setEmergencyContactFirstName("Jane");
		emergencyWrapper.setEmergencyContactLastName("Doe");
		employeeDetailsDto.setEmergencyDetails(emergencyWrapper);
		employeeDetailsDto.setEmployeeNational(nationalWrapper);

		EmployeeComplianceLegal eec = new EmployeeComplianceLegal();
		eec.setCodeOfConduct("Agreed");
		eec.setCompanyAssetAgreement("");
		eec.setConflictOfInterest("");
		eec.setCyberCompliance("");
		eec.setEmploymentContract("");
		eec.setIbanCertificate("");
		eec.setNationalAddressCertificate("");
		eec.setNationalIdentificationNumber("");
		eec.setNationalIdentificationNumberPassport("");
		employeeComplianceLegalController.create(eec);
		EmployeeComplianceWrapper complianceWrapper = new EmployeeComplianceWrapper();
		complianceWrapper.setId(eec.getId());
		complianceWrapper.setCodeOfConduct("Agreed");
		complianceWrapper.setCompanyAssetAgreement("");
		complianceWrapper.setConflictOfInterest("");
		complianceWrapper.setCyberCompliance("");
		complianceWrapper.setEmploymentContract("");
		complianceWrapper.setIbanCertificate("");
		complianceWrapper.setNationalAddressCertificate("");
		complianceWrapper.setNationalIdentificationNumber("");
		complianceWrapper.setNationalIdentificationNumberPassport("");
		employeeDetailsDto.setEmployeeCompliance(complianceWrapper);

		EmployeeDependentDetails edd = new EmployeeDependentDetails();
		edd.setFullName("Child Doe");
		edd.setContactNumber("");
		edd.setDependentIdentification("");
		edd.setFirstName("");
		edd.setLastName("");
		edd.setRelationship("Father");
		edd = employeeDependentDetailsController.create(edd);
		EmployeeDependentWrapper dependentWrapper = new EmployeeDependentWrapper();
		dependentWrapper.setId(edd.getId());
		dependentWrapper.setFullName("Child Doe");
		dependentWrapper.setContactNumber("");
		dependentWrapper.setDependentIdentification("");
		dependentWrapper.setFirstName("");
		dependentWrapper.setLastName("");
		dependentWrapper.setRelationship("Father");
		List<EmployeeDependentWrapper> dependents = new ArrayList<>();
		dependents.add(dependentWrapper);
		employeeDetailsDto.setDependentDetails(dependents);

		EmployeeDetailsDto updatedEmployee = controller.updateSelfEmployeeDetails(employeeDetailsDto);
		assertNotNull(updatedEmployee);

	}

	@Test
	@Order(11)
	void testGetEmployeeDetailsForAdmin() {
		Employee emp = getEmployee();
		List<EmployeeCompleteDetailsDto> updatedEmployee = controller.getEmployeeDetailsForAdmin(emp.getId(),
				emp.getFullName());
		assertNotNull(updatedEmployee);
	}

	@Test
	@Order(11)
	void testUpdateEmployeeDetailsForAdmin() {
		EmployeeCompleteDetailsDto employeeCompleteDetailsDto = new EmployeeCompleteDetailsDto();
		Employee emp = getEmployee();
		EmployeeNationalIdentification eni = new EmployeeNationalIdentification();
		eni.setIdentificationNumber("123456789");
		eni.setBorderNumber("0000");
		eni.setType("type");
		eni.setExpiryDate(null);
		eni.setScannedImage("Image");
		eni = employeeNationalIdentificationController.create(eni);
	
		EmployeeEmergencyContact ec = new EmployeeEmergencyContact();
		ec.setEmergencyContactNumber("1111");
		ec.setEmergencyContactEmailAddress("");
		ec.setEmergencyContactMiddleName("");
		ec.setRelationship("");
		ec.setEmergencyContactFirstName("Jane");
		ec.setEmergencyContactLastName("Doe");
		ec = employeeEmergencyContactController.create(ec);

		EmployeeComplianceLegal eec = new EmployeeComplianceLegal();
		eec.setCodeOfConduct("Agreed");
		eec.setCompanyAssetAgreement("");
		eec.setConflictOfInterest("");
		eec.setCyberCompliance("");
		eec.setEmploymentContract("");
		eec.setIbanCertificate("");
		eec.setNationalAddressCertificate("");
		eec.setNationalIdentificationNumber("");
		eec.setNationalIdentificationNumberPassport("");
		eec = employeeComplianceLegalController.create(eec);

		EmployeeDependentDetails edd = new EmployeeDependentDetails();
		edd.setFullName("Child Doe");
		edd.setContactNumber("");
		edd.setDependentIdentification("");
		edd.setFirstName("");
		edd.setLastName("");
		edd.setRelationship("Father");
		edd = employeeDependentDetailsController.create(edd);
		List<EmployeeDependentDetails> listEdd = new ArrayList<>();
		listEdd.add(edd);

		employeeCompleteDetailsDto.setDependentDetails(listEdd);
		employeeCompleteDetailsDto.setComplianceDetails(eec);
		employeeCompleteDetailsDto.setEmergencyDetails(ec);
		employeeCompleteDetailsDto.setNationalIdentificationDetails(eni);
		employeeCompleteDetailsDto.setEmployee(emp);

		List<EmployeeCompleteDetailsDto> updatedEmployee = controller
				.updateEmployeeDetailsForAdmin(employeeCompleteDetailsDto);
		assertNotNull(updatedEmployee);
	}
	

	

}
