package com.nouros.hrms.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.execution.controller.IWorkorderController;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.product.storagesystem.rest.StorageRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeChildren;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.model.NewHireBenefit;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.repository.EmployeeChildrenRepository;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.HrBenefitsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.HrBenefitsService;
import com.nouros.hrms.service.NewHireBenefitService;
import com.nouros.hrms.service.OtherExpenseBankRequestService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.BenefitWrapper;
import com.nouros.hrms.wrapper.EmployeeChildrenWrapper;
import com.nouros.hrms.wrapper.HrBenefitsDto;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

@Service
public class HrBenefitsServiceImpl extends AbstractService<Integer,HrBenefits> implements HrBenefitsService {

	public HrBenefitsServiceImpl(GenericRepository<HrBenefits> repository) {
		super(repository, HrBenefits.class);
	}

	@Autowired
	private HrBenefitsRepository hrBenefitsRepository;

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	IWorkorderController workorderController;

	@Autowired
	private StorageRest storageRest;

	@Autowired
	private HrmsSystemConfigService hrmsSystemConfigService;

	@Autowired
	private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;

	@Autowired
	WorkflowActionsController workflowActionsController;
	
	@Autowired
	private EmployeeChildrenRepository employeeChildrenRepository;
	
	@Autowired
	private CommonUtils commonUtils;


	@Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
	private String rootDirBucketName;

	private static final String ATTACHMENT = "attachment";
	private static final String ERROR_MSG = "errorMsg";
	private static final String INSIDE_CLASS_LOG = "Inside @Class HrBenefitsServiceImpl @Method";
	private static final String CHILD_MUST_BE_BETWEEN_5_TO_18 = "Child must be 5 to 18 year old for applying the benefit";
	private static final String ONLY_DIRECT_HIRE_EMPLOYEES_ARE_ELIGIBLE = "Only Direct Hire Employees are eligible";
	private static final String THE_EMPLOYMENT_TYPE_OF_EMPLOYEE_IS = "the employment type of employee is :{} ";
	private static final String THE_CHILD_AGE_IS = "The child Age is :{}";
	private static final String DATE_OF_BIRTH = "dateOfBirth";
	private static final String EMPLOYEE_CHILDREN = "EmployeeChildren";
	private static final String SCHOOL_YEAR = "schoolYear";
	private static final String AMOUNT = "amount";
    private static final String STATUS = "status";
 
	
	private static final Logger log = LogManager.getLogger(HrBenefitsServiceImpl.class);

	@Override
	public HrBenefits create(HrBenefits hrBenefits) {
		log.info("inside @class HrBenefitsServiceImpl @method create");
		return hrBenefitsRepository.save(hrBenefits);
	}

	@Override
	public void softDelete(int id) {

		HrBenefits hrBenefits = super.findById(id);

		if (hrBenefits != null) {

			HrBenefits hrBenefits1 = hrBenefits;
			hrBenefitsRepository.save(hrBenefits1);

		}
	}

	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

	@Override
	public List<Object[]> getForHRBenefitsForExpense(String status, String type, List<Integer> listOfHrBenefitsId) {
		log.info("Inside @class getForHRBenefits @method getForHRBenefitsForExpense ");
		try {
			log.debug("Inside @class getForHRBenefits getForHRBenefitsForExpense customerId is : {}", commonUtils.getCustomerId());
			List<Object[]> values = hrBenefitsRepository.getHRBenefitsForExpense(status, type, listOfHrBenefitsId, commonUtils.getCustomerId());
			return values;
		} catch (Exception e) {
			log.error("Error inside @class getForHRBenefits @method getForHRBenefitsForExpense  :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public Object[] getForHRBenefitsForAccural(String status, String type, List<Integer> listOfHrBenefitsId) {
		log.info("Inside @class getForHRBenefits @method getForHRBenefitsForAccural ");
		try {
			log.debug("Inside @class getForHRBenefits customerId is : {}", commonUtils.getCustomerId());
			Object[] values = hrBenefitsRepository.getHRBenefitsForAccural(status, type, listOfHrBenefitsId, commonUtils.getCustomerId());
			return values;
		} catch (Exception e) {
			log.error("Error inside @class getForHRBenefits @method getForHRBenefitsForAccural  :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public void updateGLStatus(String newStatus, String oldStatus, String workFlowStage) {
		log.info("Inside @class getForHRBenefits @method getForHRBenefitsForAccural ");
		try {
			log.debug("Inside @class getForHRBenefits updateGLStatus customerId is : {}", commonUtils.getCustomerId());
			Integer rows = hrBenefitsRepository.updateGLStatus(newStatus, oldStatus, workFlowStage, commonUtils.getCustomerId());
			log.debug("the columns updated are :{}", rows);
			log.info("Updated the status ");
		} catch (Exception e) {
			log.error("Error inside @class getForHRBenefits @method getForHRBenefitsForAccural  :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public String benefitValidateString(Object benefit) {
		JSONObject obj = new JSONObject();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(benefit);
			Map<String, Object> benefitData = objectMapper.readValue(jsonString, Map.class);
			String benefitType = (String) benefitData.get("benefitType");
			Integer userId = (Integer) benefitData.get("userId");

			log.info("Inside @class HrBenefitsServiceImpl @method benefitValidate ");
			Integer id = userId;
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);
			Employee employee = employeeService.getEmployeeByUserId(id);
			if (employee != null) {

				checkEligibilty(benefitType, employee, benefitData);
				 obj.put(STATUS,"success");
			}
			return obj.toJSONString();

		} catch (BusinessException e) {
			log.error("Error occured inside HrBenefitTypeService @Method benefitValidate {},{}", Utils.getStackTrace(e),
					e);
			obj.put(ERROR_MSG, e.getMessage());
			return obj.toJSONString();

		} catch (Exception e) {
			log.error("Error occured inside HrBenefitTypeService @Method benefitValidate {},{}", Utils.getStackTrace(e),
					e);
			throw new BusinessException("Problem While Processing");
		}

	}

	void checkEligibilty(String type, Employee employee, Map<String, Object> benefitData) {
		switch (type) {

		case "New Hire Benefit":
			isNewHireBenefitEligible(employee, benefitData);
			break;

		case "Educational Benefit":
			isEducationalBenefitEligible(employee, benefitData);
			break;

		case "Health Club Benefit":
			isHealthClubBenefitEligible(employee, benefitData);
			break;

		case "Child Education Benefit":
			isChildEducationBenefitEligible(employee, benefitData);
			break;

		default:
			break;

		}
	}

	public void isNewHireBenefitEligible(Employee employee, Map<String, Object> benefitData) {
		log.info("Inside @class HrBenefitsService @method isNewHireBenefitEligible ");
		log.debug(THE_EMPLOYMENT_TYPE_OF_EMPLOYEE_IS, employee.getEmploymentType());
		
		 String employmentTypeList = hrmsSystemConfigService.getValue(PRConstant.HR_BENEFIT_EMPLOYMENT_LIST);
		 log.debug("Inside@method isNewHireBenefitEligible value of Employment Type list :{} ",employmentTypeList);
		 String[] employmentTypes = employmentTypeList.split(",");
		 Boolean check = checkValue(employmentTypes,employee.getEmploymentType()); 
		 log.debug("Inside @class HrBenefitsService Value of Comparison of :{}",check);
		if (!check) {
			throw new BusinessException("You are not eligible for New Hire Benefit");
		}
		String finalHrBenefitApproval = hrmsSystemConfigService.getValue(PRConstant.FINAL_HR_BENEFIT_APPROVAL_STAGE);
		log.debug(" @method isNewHireBenefitEligible Value of Final Hr Benefit Approval Stage :{} ",finalHrBenefitApproval);
		log.debug("Inside @class HrBenefitsService isNewHireBenefitEligible customerId is : {}", commonUtils.getCustomerId());
		List<HrBenefits> hrBenefitList = hrBenefitsRepository.getBenefitList(employee.getId(),
				PRConstant.NEW_HIRE_BENEFIT_NEW, finalHrBenefitApproval, commonUtils.getCustomerId());
		if (hrBenefitList.size() >= 1) {
			log.debug("the size of New Hire Benefit is :{} ", hrBenefitList.size());
			throw new BusinessException("New Hire Benefit Can be taken only once");
		}
	}

	public void isEducationalBenefitEligible(Employee employee, Map<String, Object> benefitData) {
		log.info("Inside @class HrBenefitsService @method isEducationalBenefitEligible ");
		log.debug(THE_EMPLOYMENT_TYPE_OF_EMPLOYEE_IS, employee.getEmploymentType());
		String employmentTypeList = hrmsSystemConfigService.getValue(PRConstant.HR_BENEFIT_EMPLOYMENT_LIST);
		 log.debug("@method isEducationalBenefitEligible value of Employment Type list :{} ",employmentTypeList);
		 String[] employmentTypes = employmentTypeList.split(",");
		 Boolean check = checkValue(employmentTypes,employee.getEmploymentType()); 
		 log.debug("@method isEducationalBenefitEligible Value of Comparison of :{}",check);
		if (!check) {
			throw new BusinessException( "You are not eligible for Educational Benefit");
		}
		String finalHrBenefitApproval = hrmsSystemConfigService.getValue(PRConstant.FINAL_HR_BENEFIT_APPROVAL_STAGE);
		log.debug("@method isEducationalBenefitEligible Value of Final Hr Benefit Approval Stage :{} ",finalHrBenefitApproval);
		log.debug("Inside @class HrBenefitsService isEducationalBenefitEligible customerId is : {}", commonUtils.getCustomerId());
		List<HrBenefits> hrBenefitList = hrBenefitsRepository.getBenefitList(employee.getId(),
				PRConstant.NEW_HIRE_BENEFIT_NEW, finalHrBenefitApproval, commonUtils.getCustomerId());
		String amountString = (String) benefitData.get(AMOUNT);
		if (!amountString.isEmpty()) {
			Double amount = parseAmount((String) benefitData.get(AMOUNT));
			Double sum = getSum(hrBenefitList) + amount;
			if (sum > 7000) {
				log.debug("the size of Education Benefit is :{} ", sum);
				throw new BusinessException("You cannot take Educational Benefit more than 7000 ");
			}
		}
	}

	public void isHealthClubBenefitEligible(Employee employee, Map<String, Object> benefitData) {
		log.info("Inside @class HrBenefitsService @method isHealthClubBenefitEligible ");
		log.debug(THE_EMPLOYMENT_TYPE_OF_EMPLOYEE_IS, employee.getEmploymentType());
		String employmentTypeList = hrmsSystemConfigService.getValue(PRConstant.HR_BENEFIT_EMPLOYMENT_LIST);
		 log.debug("@method isHealthClubBenefitEligible value of Employment Type list :{} ",employmentTypeList);
		 String[] employmentTypes = employmentTypeList.split(",");
		 Boolean check = checkValue(employmentTypes,employee.getEmploymentType());
		 log.debug("@method isHealthClubBenefitEligible Value of Comparison of :{}",check);
		if (!check) {
			throw new BusinessException("You are not eligible for Health Club Benefit");
		}
		LocalDate localDate = LocalDate.now();
		if (employee.getDateOfJoining() != null) {
			LocalDate dateOfJoining = dateToLocalDate(employee.getDateOfJoining());
			if (dateOfJoining.getYear() < localDate.getYear()) {
				log.info("Next year of joining ");
				String finalHrBenefitApproval = hrmsSystemConfigService.getValue(PRConstant.FINAL_HR_BENEFIT_APPROVAL_STAGE);
				log.debug("@method isHealthClubBenefitEligible Value of Final Hr Benefit Approval Stage :{} ",finalHrBenefitApproval);
				log.debug("Inside @class HrBenefitsService isHealthClubBenefitEligible customerId is : {}", commonUtils.getCustomerId());
				List<HrBenefits> hrBenefitList = hrBenefitsRepository.getBenefitListForCurrentYear(employee.getId(),
						PRConstant.HEALTH_CLUB_BENEFIT_NEW,finalHrBenefitApproval, commonUtils.getCustomerId());
				String amountString = (String) benefitData.get(AMOUNT);
				if (!amountString.isEmpty()) {
					Double amount = parseAmount((String) benefitData.get(AMOUNT));
					Double sum = getSum(hrBenefitList) + amount;

					if (sum > 4200) {
						log.debug("the size of Health Club Benefit is :{} ", sum);
						throw new BusinessException("You cannot take Health Club Benefit more than 4200 ");
					}
				}
			}
		} else
			throw new BusinessException("Date of Joining not present");
	}

	void isChildEducationBenefitEligible(Employee employee, Map<String, Object> benefitData) {
		log.info("Inside @class HrBenefitsService @method isChildEducationBenefitEligible ");
		log.debug(THE_EMPLOYMENT_TYPE_OF_EMPLOYEE_IS, employee.getEmploymentType());
		String employmentTypeList = hrmsSystemConfigService.getValue(PRConstant.HR_BENEFIT_EMPLOYMENT_LIST);
		 log.debug("@method isChildEducationBenefitEligible value of Employment Type list :{} ",employmentTypeList);
		 String[] employmentTypes = employmentTypeList.split(",");
		 Boolean check = checkValue(employmentTypes,employee.getEmploymentType()); 
		 log.debug("@method isChildEducationBenefitEligible Value of Comparison of :{}",check);
		if (!check) {
			throw new BusinessException("You are not eligible for Child Education Benefit");
		}
		List<Map<String, Object>> childObj = (List<Map<String, Object>>) benefitData.get("childObj");
		if (childObj != null) {
			getBenefitLimitForJobGrade(employee, benefitData, childObj);
		} else {
			log.info("Child Object not present ");
		}
	}

	private void getBenefitLimitForJobGrade(Employee employee, Map<String, Object> benefitData,
			List<Map<String, Object>> childObj) {
		for (int i = 0; i < childObj.size(); i++) {
			Map<String, Object> employeeChildren = (Map<String, Object>) childObj.get(i).get(EMPLOYEE_CHILDREN);
			LocalDate dOB = StringToLocalDate((String) employeeChildren.get(DATE_OF_BIRTH));
			int age = calculateAge(dOB);
			if (age < 5 || age > 18) {
				log.info(THE_CHILD_AGE_IS, age);
				throw new BusinessException(CHILD_MUST_BE_BETWEEN_5_TO_18);
			}
			Integer gradeAmount = getGradeAmount(employee.getJobGrade());
			if (employeeChildren.get(AMOUNT) != null) {
				String amountString = (String) employeeChildren.get(AMOUNT);
				if (!amountString.isEmpty()) {
					Double amount = parseAmount((String) employeeChildren.get(AMOUNT));
					if ((String) employeeChildren.get("name") != null && amount != null) {
						Long schoolYearLong = (Long) benefitData.get(SCHOOL_YEAR);
						Date schoolYearDate = new Date(schoolYearLong);
						log.debug(
								"Inside @class HrBenefitsService @method isChildEducationBenefitEligible Job grade between 1 and 2:{} ",
								(String) employeeChildren.get("name"));
						String finalHrBenefitApproval = hrmsSystemConfigService.getValue(PRConstant.FINAL_HR_BENEFIT_APPROVAL_STAGE);
						log.debug(" getBenefitLimitForJobGrade Value of Final Hr Benefit Approval Stage :{} ",finalHrBenefitApproval);
						log.debug("Inside @class HrBenefitsService getBenefitLimitForJobGrade customerId is : {}", commonUtils.getCustomerId());
						List<HrBenefits> hrBenefitsList1 = hrBenefitsRepository.getBenefitListForChildEducation(
								employee.getId(), PRConstant.CHILD_EDUCATION_BENEFIT_NEW, finalHrBenefitApproval,
								(String) employeeChildren.get("name"), schoolYearDate, commonUtils.getCustomerId());
						Double sum1 = getSum(hrBenefitsList1) + amount;
						if (sum1 > gradeAmount) {
							throw new BusinessException("Cannot take Child Education Benefit More than " + gradeAmount
									+ " for " + (String) employeeChildren.get("name"));
						}
					}
				}
			}
		}
	}

	
	public Double getSum(List<HrBenefits> hrBenefitList) {
		try {
			Double sum = 0.0;
			for (HrBenefits hrBenefit : hrBenefitList) {
				sum = sum + hrBenefit.getAmount();
			}
			log.debug("The sum is :{} ", sum);
			return sum;
		} catch (Exception e) {
			log.error("Error while Calculationg sum for  :{} :{} ", e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	LocalDate dateToLocalDate(Date dateOfJoining) {

		return dateOfJoining.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Override
	public HrBenefits updateHrBenefitsWorkflowStage(HrBenefitsDto hrBenefitsDto) {
		log.debug("Inside method updateHrBenefitsWorkflowStage HrBenefitsId : {}", hrBenefitsDto.getHrBenefitsId());
		try {
			if (hrBenefitsDto.getHrBenefitsId() != null) {
				HrBenefits optionalHrBenefits = super
						.findById(hrBenefitsDto.getHrBenefitsId());
				if (optionalHrBenefits != null) {
					HrBenefits hrBenefits = optionalHrBenefits;
					if (hrBenefitsDto.getWorkflowStage().equalsIgnoreCase("CANCELLED") && hrBenefits != null
							&& hrBenefits.getProcessInstanceId() != null) {
						log.debug("Inside method updateHrBenefitsWorkflowStage ProcessInstanceId found is  : {}",
								hrBenefits.getProcessInstanceId());
						Workorder wordorder = workorderController
								.findByProcessInstanceId(hrBenefits.getProcessInstanceId());
						log.debug("Inside method updateHrBenefitsWorkflowStage wordorder found is  : {}", wordorder);
						if (wordorder != null && wordorder.getReferenceId() != null) {
							try {
								String response = workorderController.deleteWorkorder(wordorder.getReferenceId());
								log.debug(
										"Inside method updateHrBenefitsWorkflowStage response from  deleteWorkorder api is : {}",
										response);
								workflowActionsController.notifyActions(hrBenefits.getProcessInstanceId());
							} catch (Exception e) {
								log.error("Facing error While deleting Workorder");
							}
						}
					}
					if (hrBenefits != null) {
						hrBenefits.setWorkflowStage(hrBenefitsDto.getWorkflowStage());
					}
					return hrBenefitsRepository.save(hrBenefits);
				} else {
					throw new BusinessException("HrBenefits with ID " + hrBenefitsDto.getHrBenefitsId() + " not found");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("error while updating hrBenefits work flow stage", e.getMessage());
		}

		return null;
	}

	public ResponseEntity<byte[]> createWpsTxtFileForAllBenefits(String benefitName) {
		log.info("inside @class HrBenefitsServiceImpl @method createWpsTxtFileForAllBenefits ");
		try {
			String benefit = "";
			LocalDate currDate = LocalDate.now();
			LocalDate nextDate = currDate.plusDays(1);
			Integer year = currDate.getYear();
			LocalDate sixDaysAgo = currDate.minusDays(6);
			DayOfWeek dayOfWeek = DayOfWeek.from(currDate);
			log.debug("the current day is :{} ", dayOfWeek.name());
			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			int weekNumber = currDate.get(weekFields.weekOfWeekBasedYear());
			log.debug("Week num is :{} ", weekNumber);
			log.debug("The Benefit is :{} ",benefitName);
			String benefitNameSpace= getBenefit(benefitName);
			log.debug("Benefit Name :{} ",benefitNameSpace);
			String finalHrBenefitApproval = hrmsSystemConfigService.getValue(PRConstant.FINAL_HR_BENEFIT_APPROVAL_STAGE);
			log.debug(" createWpsTxtFileForAllBenefits Value of Final Hr Benefit Approval Stage :{} ",finalHrBenefitApproval);
			log.debug("Inside @class HrBenefitsService createWpsTxtFileForAllBenefits customerId is : {}", commonUtils.getCustomerId());	
			List<HrBenefits> hrBenefits = hrBenefitsRepository.getBenefitsByWorkflowStageAndDate(finalHrBenefitApproval,
      					sixDaysAgo, currDate,benefitNameSpace, commonUtils.getCustomerId());
      			log.debug("size of educational Benefits :{} ", hrBenefits.size());
      			List<String[]> data = getData(hrBenefits);
      			ByteArrayOutputStream out = generateTxt(data);
      			byte[] txtBytes = out.toByteArray();
      			String fileReference = getSystemTimeWithTimeStamp();
      		//	String type = benefitName.replaceAll("\\s","");
      			String fileName = benefitName + "Wps" + fileReference + ".txt";
      			String filePath = "hrmswps/" + fileName;

      			log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}", fileName, fileReference, filePath);

      			log.info("Inside createWpsTxtFileForAllEducationalBenefit rootDirBucketName :{}", rootDirBucketName);
      			InputStream inputStream = new ByteArrayInputStream(txtBytes);
      			uploadFileInStorage(inputStream, fileName, filePath, rootDirBucketName);
      			HttpHeaders headers = new HttpHeaders();
      			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      			headers.setContentDispositionFormData(ATTACHMENT, fileName);
      			// String fileDir = rootDirBucketName + filePath;
      			log.debug("The Path of File Dir to store is :{} ", filePath);
      			List<Integer> employeeIdList = getEmployeeIdList(hrBenefits);
      			ObjectMapper objectMapper = new ObjectMapper();
      			String json = objectMapper.writeValueAsString(employeeIdList);
      			OtherExpenseBankRequest otherExpenseBankRequest = savePath(benefitName, filePath, currDate, year, weekNumber, json);
			   saveValueInBenefits(otherExpenseBankRequest , hrBenefits);
      			return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);

		} catch (Exception e) {
			log.error(
					"Error inside @class EducationBenefitServiceImpl @method createWpsTxtFileForAllEducationalBenefit :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException("Something wrong in Creating wps for all Education Benefit ");
		}

	}

	private ByteArrayOutputStream generateTxt(List<String[]> data) {
		log.debug("Inside @class HrBenefitsServiceImpl @method generateTxt data :{}", data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
			if (data != null) {
				for (String[] line : data) {
					writer.write(String.join(",", line));
					writer.newLine();
				}
				log.info("Inside @class HrBenefitsServiceImpl generateTxt data");
				writer.flush();
			}
		} catch (IOException e) {
			log.error("Error generating TXT content: {}", e.getMessage());
		}
		return out;
	}

	private String getSystemTimeWithTimeStamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return now.format(formatter);
	}

	private void uploadFileInStorage(InputStream in, String fileName, String filePath, String rootDir)
			throws IOException {
		log.debug("Inside uploadFileInStorage :: {}", fileName);
		log.debug("fileName: {} filePath: {}", fileName, filePath);
		try (InputStream inputStream = new ByteArrayInputStream(in.readAllBytes())) {

			storageRest.createFile(inputStream, rootDir, filePath);
		}
	}

	List<String[]> getData(List<HrBenefits> hrBenefits) {
		List<String[]> data = new ArrayList<>();
		try {
			log.info(INSIDE_CLASS_LOG + "getData");
			Map<String, String> mapHrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
			String ifh = mapHrmsSystemConfigMap.get("ifh");
			String ifile = mapHrmsSystemConfigMap.get("ifile");
			String csv = mapHrmsSystemConfigMap.get("csv");
			String connectId = mapHrmsSystemConfigMap.get("connectId");
			String customerId = mapHrmsSystemConfigMap.get("customerId");
			String sach = mapHrmsSystemConfigMap.get("@sach@");
			String bathdr = mapHrmsSystemConfigMap.get("bathdr");
			String achcr = mapHrmsSystemConfigMap.get("achcr");
			String debitAccountNumber = mapHrmsSystemConfigMap.get("debitAccountNumber");
			String paymentPurposeCodeO = mapHrmsSystemConfigMap.get("paymentPurposeCodeO");
			String paymentNarrationCompany = mapHrmsSystemConfigMap.get("wpsPopTest");
			String pcmHsbcnetTest = mapHrmsSystemConfigMap.get("PcmHsbcnetTest");
			String first = mapHrmsSystemConfigMap.get("@1st@");
			String sar = mapHrmsSystemConfigMap.get("sar");
			String molEstablisedId = mapHrmsSystemConfigMap.get("molEstablisedId");
			String employerId = mapHrmsSystemConfigMap.get("employerId");
			String p = mapHrmsSystemConfigMap.get("p");
			String one = mapHrmsSystemConfigMap.get("one");
			String secpty = mapHrmsSystemConfigMap.get("secpty");
			String n = mapHrmsSystemConfigMap.get("n");
			String alw = mapHrmsSystemConfigMap.get("ALW");
			log.debug(
					INSIDE_CLASS_LOG
							+ "getData after all the data fetch from Hrms_System_Config mapHrmsSystemConfigMap :{} ",
					mapHrmsSystemConfigMap);
			String fileReference = "ILWPSOPOPVATEST" + getSystemTimeWithTimeStamp();
			String batchReference = "IWPSOPOP" + getSystemTimeWithTimeStampNew();
			String currentDate = getCurrentDate();
			String currentTime = getCurrentTime();
			String valueDate = getDateAfterDays(1);

			log.info(INSIDE_CLASS_LOG + "getData after set date and time");

 
				Integer count = hrBenefits.size();
				log.debug(INSIDE_CLASS_LOG + "fetch count :{}", count);
				String totalCount = count.toString();
				log.debug(INSIDE_CLASS_LOG + "fetch totalCount :{}", totalCount);
				Integer totalRecordCount = Integer.parseInt(totalCount) + 2;
				String totalRecordCountString = totalRecordCount.toString();
				String netWorth = getTotalAmount(hrBenefits);

 
				data.add(new String[] { ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime,
						p, one, totalRecordCountString });
				data.add(new String[] { bathdr, achcr, totalCount, "", "", "", paymentPurposeCodeO,
						paymentNarrationCompany, "", first, valueDate, debitAccountNumber, sar, netWorth, "", "", "",
						"", "", "", molEstablisedId, employerId, "", "", "", batchReference });
				iterateBenefits(secpty, n, sach, hrBenefits, data, alw, paymentNarrationCompany);

				log.info(INSIDE_CLASS_LOG + "fetchData Educational Benefit Successfully Iterate");
 
			return data;

		} catch (Exception e) {
			log.error("Error Inside @class EducationalBenefitServiceImpl @method getData :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private String getSystemTimeWithTimeStampNew() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		return now.format(formatter);
	}

	private String getCurrentTime() {
		LocalTime time = LocalTime.now();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(timeFormatter);
	}

	private String getCurrentDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return date.format(dateFormatter);
	}

	private String getDateAfterDays(int days) {
		LocalDate date = LocalDate.now();
		LocalDate newDate = date.plusDays(days);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return newDate.format(dateFormatter);
	}

	private String getTotalAmount(List<HrBenefits> hrBenefits) {
		log.debug("Inside Method getTotalAmount hrBenefits size :{} ", hrBenefits.size());
		Double amount = 0.0;
		for (HrBenefits hrBenefit : hrBenefits) {
			amount = amount + hrBenefit.getAmount();
		}
		log.debug("Inside Method getTotalAmount educationalBenefits amount :{} ", amount);
		return String.valueOf(amount);

	}

	private void iterateBenefits(String secpty, String n, String sach, List<HrBenefits> hrBenefits, List<String[]> data,
			String alw, String paymentNarrationCompany) {
		String one = "1";
		log.info(INSIDE_CLASS_LOG + " iterateOnRunDetails");
        if(hrBenefits != null) {
		for (HrBenefits hrBenefit : hrBenefits) {
			log.debug(INSIDE_CLASS_LOG + "iterateOnRunDetails runDetail :{}", hrBenefit);
			Integer employeeId = hrBenefit.getEmployee().getId();
			String employeeReferenceId = hrBenefit.getEmployee().getEmployeeId();
			Double amount =0.0;
			if(hrBenefit.getBenefitType().equalsIgnoreCase(PRConstant.HEALTH_CLUB_BENEFIT_NEW))
            {
            	amount = getAmountForHealthClub(hrBenefit.getEmployee(),hrBenefit);
            }
            else
            {
            	amount = hrBenefit.getAmount();
            }
            	String nationalIdentification = "";
            	log.debug("Inside @class HrBenefitsService iterateBenefits customerId is : {}", commonUtils.getCustomerId());
			EmployeeNationalIdentification employeeNationalIdentification = employeeNationalIdentificationRepository
					.findNationalIdentificationNumberByEmployeeId(employeeId, commonUtils.getCustomerId());
			if (employeeNationalIdentification != null) {
				nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
			}
			AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext()
					.getBean(AccountDetailsService.class);
			List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employeeId);

			for (AccountDetails accountDetail : accountDetails) {
				log.debug(INSIDE_CLASS_LOG + "iterateEducationalBenefit accountDetail :{}", accountDetail);
				StringBuilder rowBuilder = new StringBuilder();

				rowBuilder.append(secpty).append(",");
				rowBuilder.append(accountDetail.getIban()).append(",");
				rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
				rowBuilder.append(employeeId).append(",");
				rowBuilder.append(accountDetail.getBankId()).append(",").append(",").append(",");

				rowBuilder.append(amount);

				rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n)
						.append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
				rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()).append(",");
				rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one)
						.append(",");
				rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",")
						.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",")
						.append(",");
				rowBuilder.append(alw);
				data.add(rowBuilder.toString().split(","));
				log.debug(INSIDE_CLASS_LOG + " iterateBenefit data :{}", data);

			}
		}
        }
	}

	OtherExpenseBankRequest savePath(String type, String path, LocalDate date, Integer year, Integer weekNumber, String json) {
		log.info("Inside @class HrBenefitsServiceImpl @method savePath ");
		try {
			OtherExpenseBankRequestService otherExpenseBankRequestService = ApplicationContextProvider
					.getApplicationContext().getBean(OtherExpenseBankRequestService.class);
		OtherExpenseBankRequest otherExpenseBankRequest =	otherExpenseBankRequestService.savePath(type, path, date, year, weekNumber, PRConstant.WPS_CREATED, json);
			log.debug("Inside @class HrBenefitsServiceImpl @method savePath date :{}  path:{} ", date, path);
	     return otherExpenseBankRequest;
		} catch (Exception e) {
			log.error("Error inside @class HrBenefitsServiceImpl @method savePath :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum,String benefitName) {

		log.info("Inside @class HrBenefitServiceImpl @method downloadCommonWpsFile");
		LocalDate currDate = LocalDate.now();
		Integer year = currDate.getYear();
		OtherExpenseBankRequestService otherExpenseBankRequestService = ApplicationContextProvider
				.getApplicationContext().getBean(OtherExpenseBankRequestService.class);
		String filePath = otherExpenseBankRequestService.getPathForWps(benefitName, weekNum, year);

		log.info("Inside HrBenefitServiceImpl downloadWpsFile rootDirBucketName :{} ", rootDirBucketName);

		String currentTime = getSystemTimeWithTimeStamp();
		try {
			log.debug("Inside BusinessExpenseServiceImpl @method downloadWpsFile :{} ", filePath);
			InputStream inputStream = storageRest.read(rootDirBucketName, filePath);
			log.debug("InputStream available bytes: {}", inputStream.available());
			InputStreamResource resource = new InputStreamResource(inputStream);
			byte[] fileData = resource.getContentAsByteArray();
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			responseHeaders.setContentDispositionFormData(ATTACHMENT, "Wps" + currentTime + ".txt");
			return new ResponseEntity<>(fileData, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error Inside @class HrBenefitServiceImpl @method downloadWpsFile :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException("Something went wrong inside downloadWpsFile ");
		}
	}

	private List<Integer> getEmployeeIdList(List<HrBenefits> hrBenefits) {
		try {
			log.info("Inside @class HrBenefitServiceImpl @method getEmployeeIdList ");
			List<Integer> employeeIdList = new ArrayList<>();
			for (HrBenefits hrBenefit : hrBenefits) {
				employeeIdList.add(hrBenefit.getEmployee().getId());
			}
			log.debug("The Size of List :{} ", employeeIdList.size());
			return employeeIdList;
		} catch (Exception e) {
			log.error("Error Inside @class HrBenefitServiceImpl @method getEmployeeIdList :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private int calculateAge(LocalDate dateOfBirth) {
		LocalDate currentDate = LocalDate.now();
		return Period.between(dateOfBirth, currentDate).getYears();
	}

	public LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}

	public LocalDate StringToLocalDate(String childDateOfBirth) {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(childDateOfBirth);
			return convertDateToLocalDate(date);

		} catch (Exception e) {
			log.error("Error Inside @class HrBenefitServiceImpl @method StringToLocalDate :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	public boolean isDoubleNumber(String str) {
		try {
			double v = Double.parseDouble(str);
			return true;
		} catch (NumberFormatException nfe) {
		}
		return false;
	}

	private Double parseAmount(String amountString) {

		if (isDoubleNumber(amountString)) {
			return Double.valueOf(amountString);
		} else {
			try {
				Integer amountInt = Integer.parseInt(amountString);
				return (double) amountInt;
			} catch (NumberFormatException e) {
				log.error("Error Inside @class HrBenefitServiceImpl @method parseAmount :{} :{} ", e.getMessage(),
						Utils.getStackTrace(e));
				throw new BusinessException();
			}
		}
	}

	private Integer getGradeAmount(Integer jobGrade) {
		if (jobGrade >= 8 && jobGrade <= 13)
			return 35000;
		if (jobGrade >= 3 && jobGrade <= 7)
			return 30000;
		if (jobGrade >= 1 && jobGrade <= 2)
			return 20000;
		return 0;
	}

	@Override
	public String deleteHrBenefitsAndItsReferences(Integer id) {
		log.info("Inside method deleteHrBenefitAndReferences");
		
		try {
			log.debug("Inside @class HrBenefitsService deleteHrBenefitsAndItsReferences customerId is : {}", commonUtils.getCustomerId());
			List<EmployeeChildren> employeeChildrenList = employeeChildrenRepository.findByHrBenefitsId(id, commonUtils.getCustomerId());
			
			log.debug("employeeChildrenList are  : {}", employeeChildrenList);
			if (!employeeChildrenList.isEmpty() && employeeChildrenList != null) {
				employeeChildrenRepository.deleteAll(employeeChildrenList);
			}
			
			hrBenefitsRepository.deleteById(id);
			return APIConstants.SUCCESS_JSON;
			
		}catch (BusinessException ex) {
			throw new BusinessException("Error while deleting Hrbenefits and its references ", ex.getMessage());
		}	
	}
	
	public void saveValueInBenefits(OtherExpenseBankRequest otherExpenseBankRequest ,List<HrBenefits> hrBenefits)
	{
		try
		{
			log.debug(" Inside @class HrBenefitServiceImpl @method saveValueInBenefits oTEBR id :{}  hrbenefit size :{}  ",otherExpenseBankRequest.getId(),hrBenefits.size());
		  for(HrBenefits hrBenefit : hrBenefits)
		  {
			  hrBenefit.setOtherExpenseBankRequestId(otherExpenseBankRequest.getId());
			  hrBenefitsRepository.save(hrBenefit);
		  }
		  log.info("Id of Other Expense Bank Request Saved in HR Benefit ");
		}
		catch(Exception e)
		{
			log.error("Error Inside @class HrBenefitServiceImpl @method saveValueInBenefits :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	

	
	@Override
	public List<HrBenefits> getListOfHrBenefitsByOtherExpenseBankRequestId(OtherExpenseBankRequest otherExpenseBankRequest)
	{
		try
		{
			log.debug("Inside class HrBenefitsServiceImpl method getListOfHrBenefitsByOtherExpenseBankRequestId  ID :{} ",otherExpenseBankRequest.getId());
			log.debug("Inside @class HrBenefitsService getListOfHrBenefitsByOtherExpenseBankRequestId customerId is : {}", commonUtils.getCustomerId());
			List<HrBenefits> hrBenefitsList = hrBenefitsRepository.getListOfHrBenefitsByOtherExpenseBankRequestId(otherExpenseBankRequest.getId(), commonUtils.getCustomerId());
		  return hrBenefitsList;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class HrBenefitServiceImpl @method getListOfHrBenefitsByOtherExpenseBankRequestId :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
  
	public String getBenefit(String benefitName)
	{
		HashMap<String,String> map = new HashMap<>();
		map.put("ChildEducationBenefit", "Child Education Benefit");
		map.put("EducationalBenefit","Educational Benefit");
		map.put("HealthClubBenefit","Health Club Benefit");
		map.put("NewHireBenefit","New Hire Benefit");
		return map.get(benefitName);
	}
	
	public Double getAmountForHealthClub(Employee employee , HrBenefits hrBenefit)
	{
		try
		{
			log.debug("Inside @class HrBenefitsService @method getAmountForHealthClub employeeId :{} hrBenefitId :{}",employee.getId(),hrBenefit.getId());
			LocalDate localDate = LocalDate.now();
			Integer year = localDate.getYear();
			log.debug(" Current Year is :{} ",year);
			log.debug("  Current LocalDate :{}  year :{} ",localDate,year);
			Date joiningDate = employee.getDateOfJoining();
			LocalDate localJoiningDate = convertDateToLocalDate(joiningDate);
		    Integer joiningYear = localJoiningDate.getYear();
			log.debug(" Joining Date :{} LocalDate :{}  year :{} ",joiningDate,localJoiningDate,joiningYear);
			String healthClubFinalWorkflowStage = hrmsSystemConfigService.getValue(PRConstant.HEALTH_CLUB_BENEFITS_FINAL_STAGE);
			log.debug("Inside @method getAmountForHealthClub FinalWorkflowStage of Health-Club-Benefit :{} ",healthClubFinalWorkflowStage);
			log.debug("Inside @class HrBenefitsService customerId is : {}", commonUtils.getCustomerId());
			Double healthClubBenefitEarlyAmount = hrBenefitsRepository.getSumOfAmountOfBenefit(PRConstant.HEALTH_CLUB_BENEFIT_NEW
					,employee.getId(), healthClubFinalWorkflowStage, commonUtils.getCustomerId());
		   log.debug("Total Health Club Benefit Amount earlier is :{} ",healthClubBenefitEarlyAmount);
			String healthClubAmountString = PRConstant.HEALTH_CLUB_AMOUNT_PER_AMOUNT;
		String amountString =	hrmsSystemConfigService.getValue(healthClubAmountString);
		log.debug(" healthClubAmountString:{} amountString :{} ",healthClubAmountString,amountString);
		Double amount = Double.valueOf(amountString);
//		  Double totalAmount = amount*12;
//		   Double remainingAmount = totalAmount-healthClubBenefitEarlyAmount;
		if(year.equals(joiningYear))
			{
			 Integer currentMonth = localDate.getMonthValue();
			 Integer joiningMonth = localJoiningDate.getMonthValue();
			 Integer monthDifference = currentMonth-joiningMonth;
				log.info(" Joining Year And Current Year same ");
				log.debug(" Current month :{} and Joining Month :{} difference :{}",currentMonth,joiningMonth,monthDifference);
		     Double remainingAmount =amount*monthDifference-healthClubBenefitEarlyAmount;
		   log.debug(" Remaining Amount of Health Club Amount :{} and amout claimed ",remainingAmount,hrBenefit.getAmount());
              if(remainingAmount<=hrBenefit.getAmount())	
              {
            	  log.debug("Remaining Amount:{} is less than Claimed amount :{}",remainingAmount,hrBenefit.getAmount());
            	  return remainingAmount;
              }
              else
              {
            	  log.debug(" Remaining Amount :{} is Greater than Claimed Amount :{} ",remainingAmount,hrBenefit.getAmount());
            	  return hrBenefit.getAmount();
              }
			}
			else
			{
				  Double totalAmount = amount*12;
				   Double remainingAmount = totalAmount-healthClubBenefitEarlyAmount;
				log.info(" Joining Year And Current Year is not same ");
				   log.debug(" Remaining Amount of Health Club Amount :{} and amout claimed ",remainingAmount,hrBenefit.getAmount());
		              if(remainingAmount<=hrBenefit.getAmount())	
		              {
		            	  log.debug("Remaining Amount:{} is less than Claimed amount :{}",remainingAmount,hrBenefit.getAmount());
		            	  return remainingAmount;
		              }
		              else
		              {
		            	  log.debug(" Remaining Amount :{} is Greater than Claimed Amount :{} ",remainingAmount,hrBenefit.getAmount());
		            	  return hrBenefit.getAmount();
		              }
			}
		}
		catch(Exception e)
		{
			  log.error("Error Inside @class HrBenefitsServiceImpl @method getAmountforHealthClub :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			  throw new BusinessException();
		}
	}
	
	public Boolean checkValue(String[] checkList , String checkValue)
	{
		try
		{
		  log.debug("Value of checklist :{} checkValue :{}",checkList,checkValue);	
		  for(String checkData : checkList)
		  {
			  log.debug(" Values checkData :{} checkvalue:{}",checkData,checkValue);
			  if(checkData.equalsIgnoreCase(checkValue))
			  {
				  log.info("Values are Equal ");
				  return true;
			  }
		  }
		  return false;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class HrBenefitsServiceImpl @method checkValue :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		return false;
		}
	}
}
