package com.nouros.hrms.service.impl;

import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.ai.chat.AiChatModel;
import com.enttribe.commons.io.excel.Excel;
import com.enttribe.commons.io.excel.ExcelRow;
import com.enttribe.commons.io.excel.ExcelWriter;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.document.model.Document;
import com.enttribe.document.rest.IDocumentRest;
import com.enttribe.document.rest.IDocumentStreamRest;
import com.enttribe.orchestrator.utility.annotation.TriggerBpmnAspect;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.platform.customannotation.annotation.GenericAnnotation;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.pii.filter.PropertyFilter;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantCertifications;
import com.nouros.hrms.model.ApplicantDependentDetails;
import com.nouros.hrms.model.ApplicantEducation;
import com.nouros.hrms.model.ApplicantExperience;
import com.nouros.hrms.model.ApplicantLanguage;
import com.nouros.hrms.model.ApplicantNationalIdentification;
import com.nouros.hrms.model.City;
import com.nouros.hrms.model.Country;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeCertification;
import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.model.EmployeeLanguage;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.model.EmployeeWorkExperience;
import com.nouros.hrms.model.Employeeeducationdetails;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.model.Location;
import com.nouros.hrms.model.Offers;
import com.nouros.hrms.model.Skill;
import com.nouros.hrms.repository.ApplicantCertificationsRepository;
import com.nouros.hrms.repository.ApplicantDependentDetailsRepository;
import com.nouros.hrms.repository.ApplicantEducationRepository;
import com.nouros.hrms.repository.ApplicantExperienceRepository;
import com.nouros.hrms.repository.ApplicantLanguageRepository;
import com.nouros.hrms.repository.ApplicantNationalIdentificationRepository;
import com.nouros.hrms.repository.ApplicantRepository;
import com.nouros.hrms.repository.CityRepository;
import com.nouros.hrms.repository.CountryRepository;
import com.nouros.hrms.repository.EmployeeCertificationRepository;
import com.nouros.hrms.repository.EmployeeDependentDetailsRepository;
import com.nouros.hrms.repository.EmployeeLanguageRepository;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.EmployeeWorkExperienceRepository;
import com.nouros.hrms.repository.EmployeeeducationdetailsRepository;
import com.nouros.hrms.repository.JobApplicationRepository;
import com.nouros.hrms.repository.JobOpeningRepository;
import com.nouros.hrms.repository.LocationRepository;
import com.nouros.hrms.repository.OffersRepository;
import com.nouros.hrms.repository.SkillRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantCertificationsService;
import com.nouros.hrms.service.ApplicantEducationService;
import com.nouros.hrms.service.ApplicantExperienceService;
import com.nouros.hrms.service.ApplicantLanguageService;
import com.nouros.hrms.service.ApplicantService;
import com.nouros.hrms.service.JobApplicationService;
import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.util.report.ExcelUtils;
import com.nouros.hrms.wrapper.ApplicantPromptWrapper;
import com.nouros.hrms.wrapper.ApplicantWrapper;
import com.nouros.hrms.wrapper.EmployeePopulateDto;
import com.nouros.hrms.wrapper.ProfessionalSummaryWrapper;
import com.nouros.hrms.wrapper.WorkExperienceSummaryWrapper;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

import jakarta.validation.Valid;

/**
 * This is a class named "ApplicantServiceImpl" which is located in the package
 * " com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "ApplicantService" interface and it extends the "AbstractService" class,
 * which seems to be a generic class for handling CRUD operations for entities.
 * This class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Applicant and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Applicant
 * Applicant) for exporting the Applicant data into excel file by reading the
 * template and mapping the Applicant details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class ApplicantServiceImpl extends AbstractService<Integer, Applicant> implements ApplicantService {

	private static final String SUMMARY = "summary";

	private static final String START_DATE = "startDate";

	private static final String DURATION = "duration";

	private static final String CURRENTLY_WORK_HERE = "currentlyWorkHere";

	private static final String CERTIFICATION_ID = "certificationId";

	private static final String ISSUING_INSTITUTION = "issuingInstitution";

	private static final String CERTIFICATION_NAME = "certificationName";

	private static final String LANGUAGE_SKILLS = "LanguageSkills";

	private static final String DEGREE_START_DATE = "degreeStartDate";

	private static final String DATEOF_COMPLETION = "DateofCompletion";

	private static final String COLLEGE_NAME = "collegeName";

	private static final String EDUCATION_DETAILS = "educationDetails";

	private static final String OCCUPATION = "occupation";

	private static final String END_DATE = "endDate";

	private static final String COMPANY = "company";

	private static final String ERROR_WHILE_CONVERTING_OBJECT_TO_JSON = "Error while converting object to JSON";

	private static final String APPLICANT_ALREADY_REGISTERED_BY_THIS_EMAIL_ID = "Applicant Already Registered By This Email Id";

	private static final String INVALID_FILE_FORMAT_PLEASE_PROVIDE_CORRECT_FILE_FORMAT_OF_RESUME = "Invalid File Format , Please Provide Correct File Format of Resume. ";

	private static final String RESUME_RESPONSE_SUMMARY_KEY_FROM_FULL_RESPONSEFOR_SUMMARY_IS = "resumeResponseSummary key from fullResponseforSummary is : {} ";

	private static final String RESUME_RESPONSE_SUMMARY_KEY_AS_RESPONSE_KEY = "resumeResponseSummary key from fullResponseforSummary as response key is : {} ";

	private static final String CAUGHT_ERROR_FOR_RESUME_SUMMARY_FROM_PROMPT = "Caught error for Resume Summary from Prompt : {} ";

	private static final String UNABLE_TO_PARSE_RESPONSE_FROM_PROMPT_AFTER_READING_CV_RESUME = "Unable To Parse Response from Prompt after Reading CV/Resume";

	private static final String STATE = "state";

	private static final String POSTAL_CODE = "postalCode";

	private static final String GENDER = "gender";

	private static final String CITIZENSHIP = "citizenship";

	private static final String REFERRED_BY_EMAIL_ID = "referredByEmailId";

	private static final String EMAIL_ID = "emailId";

	private static final String REFERRED_BY = "referredBy";

	private static final String SKILL_SET2 = "skillSet";

	private static final String APPLICANT_LANGUAGE = "applicantLanguage";

	private static final String APPLICANT_EDUCATION = "applicantEducation";

	private static final String CURRENT_EMPLOYER = "currentEmployer";

	private static final String CITIZENSHIP_STATUS = "citizenshipStatus";

	private static final String PREFERRED_LOCATION = "preferredLocation";

	private static final String FIELD_OF_STUDY = "fieldOfStudy";

	private static final String MARITAL_STATUS = "maritalStatus";

	private static final String REFERED_BY = "referedBy";

	private static final String REFERED_BY_EMAIL_ID = "referedByEmailId";

	private static final String COUNTRY = "country";

	private static final String RESPONSE_FROM_PROMPT_CURL_IS = "Response from PromptCurl is : {} ";

	private static final String SKILLS = "skills";

	private static final String HIGHEST_QUALIFICATION = "highestQualification";

	private static final String MOBILE = "mobile";

	private static final String EXPERIENCE_IN_YEARS = "experienceInYears";

	private static final String PROFILE_SUMMARY = "profileSummary";

	private static final String SKILL_SET = SKILL_SET2;

	private static final String LAST_NAME = "lastName";

	private static final String FIRST_NAME = "firstName";

	private static final String CURRENT_SALARY = "currentSalary";

	private static final String CURRENT_JOB_TITLE = "currentJobTitle";

	private static final String CAUGHT_ERROR_FOR_CREATING_AND_INSERTING_VECTOR = "Caught error for creating and inserting Vector : {} ";

	private static final Logger log = LogManager.getLogger(ApplicantServiceImpl.class);

	private static final String ROW_JSON_FORMED_IS2 = "ROW_JSON_FORMED_IS: {}";

	private static final String HEAD_HUNTED = "Head-Hunted";

	private static final List<String> VALID_MARITAL_STATUSES = Arrays.asList("Single", "Married", "Divorced", "Widowed",
			"Other");

	private static final List<String> CITIZENSHIP_STATUSES = Arrays.asList("Saudi citizen",
			"Saudi permanent resident/Iqama holder", "GCC", "Others");
	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Applicant entities.
	 */

	private static final String APPLICANT = "Applicant";
	public static final String INSIDE_METHOD = "Inside @method {}";
	public static final String INSIDE_METHOD_ONE_PARAMETER = "Inside @method {} {}";
	public static final String ROW_JSON_FORMED_IS = "rowJson formed is : ";
	public static final String SOMETHING_WENT_WRONG = "Something Went Wrong : {} ";
	public static final String RESULT = "result";
	public static final String RESPONSE = "response";
	public static final String EXPECTED_SALARY = "expectedSalary";
	private static final String EMPLOYMENT_STATUS = "employmentStatus";
	private static final String CONTRACT_TYPE = "contractType";
	private static final String LOCATION_NAME = "locationName";
	private static ObjectMapper objectMapper = null;

	SimpleDateFormat outputFormat = new SimpleDateFormat("d-MMM-yyyy", Locale.ENGLISH);

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;

	public ApplicantServiceImpl(GenericRepository<Applicant> repository) {
		super(repository, Applicant.class);
	}

	@Autowired
	private ApplicantRepository applicantRepository;

	@Autowired
	private JobOpeningRepository jobOpeningRepository;

	@Autowired
	private JobOpeningService jobOpeningService;

	@Autowired
	private IDocumentStreamRest documentStreamRest;

	@Autowired
	private VectorIntegrationService vectorIntegrationService;

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	private UserRest userRest;

	@Autowired
	private IDocumentRest documentRest;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ApplicantCertificationsRepository applicantCertificationsRepository;

	@Autowired
	private EmployeeCertificationRepository employeeCertificationRepository;

	@Autowired
	private ApplicantDependentDetailsRepository applicantDependentDetailsRepository;

	@Autowired
	private EmployeeDependentDetailsRepository employeeDependentDetailsRepository;

	@Autowired
	private ApplicantLanguageRepository applicantLanguageRepository;

	@Autowired
	private EmployeeLanguageRepository employeeLanguageRepository;

	@Autowired
	private ApplicantNationalIdentificationRepository applicantNationalIdentificationRepository;

	@Autowired
	private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;

	@Autowired
	private ApplicantExperienceRepository applicantExperienceRepository;

	@Autowired
	private EmployeeWorkExperienceRepository employeeWorkExperienceRepository;

	@Autowired
	private ApplicantEducationRepository applicantEducationRepository;

	@Autowired
	private EmployeeeducationdetailsRepository employeeeducationdetailsRepository;

	@Autowired
	private OffersRepository offersRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	AiChatModel aiChatModel;

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	/**
	 * This method is used to export the given list of Applicant objects into an
	 * excel file. It reads an excel template 'Applicant.xlsx' from the resource
	 * folder 'templates/reports' and maps the Applicant data onto the template and
	 * returns the generated excel file in the form of a byte array. param Applicant
	 * - List of Applicant objects to be exported
	 * 
	 * @return byte[] - The generated excel file in the form of a byte array
	 * @throws IOException - When the template file is not found or there is an
	 *                     error reading the file
	 */
	@Override
	public byte[] export(List<Applicant> applicant) throws IOException {
		try (InputStream resourceAsStream = this.getClass().getClassLoader()
				.getResourceAsStream("templates/reports/Applicant.xlsx");
				XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resourceAsStream)) {
			int rowCount = 1;
			return setTableData(applicant, xssfWorkbook, rowCount);
		}
	}

	/**
	 * This method is responsible for setting the data of an Excel document, using a
	 * template and a list of Applicant objects. The data is written to the template
	 * starting at the specified row number.
	 * 
	 * @param Applicant    a List of Applicant objects, representing the data that
	 *                     will be written to the Excel document
	 * @param templatePath an XSSFWorkbook object, representing the template Excel
	 *                     document that the data will be written to
	 * @param rowCount     an int, representing the starting row number where data
	 *                     will be written in the Excel document
	 * @return a byte array of the Excel document after the data has been written to
	 *         it.
	 * @throws IOException if there is an issue reading or writing to the Excel
	 *                     document
	 */

	/**
	 * This method appears to take in three parameters:
	 * 
	 * A List of Applicant objects, representing the data that will be written to
	 * the Excel document. An XSSFWorkbook object, representing the template Excel
	 * document that the data will be written to. An int, representing the starting
	 * row number where data will be written in the Excel document. The method has a
	 * return type of byte array, which is the Excel document after the data has
	 * been written to it. The method also throws an IOException, which would be
	 * thrown if there is an issue reading or writing to the Excel document.
	 * 
	 * The method starts by creating some maps to hold data that will be used later:
	 * 
	 * tableColumn: a map that will hold the columns of the Excel table.
	 * identityColumnMapping: a map that will hold the mapping of columns
	 * templateHeaders: a map that will hold the headers of the excel template then
	 * it calls
	 * ExcelUtils.parseMappeddJson(tableColumn,identityColumnMapping,templateHeaders);
	 * to get the values for the maps created.
	 * 
	 * Then it creates an instance of ExcelWriter which will write the data to the
	 * workbook, it set the active sheet to the first sheet of the workbook and
	 * check if Applicant list is not empty.
	 * 
	 * It then iterates over the list of Applicant objects and for each Applicant,
	 * it creates a new row in the Excel document at the specified row number.
	 * 
	 * It also retrieves the list of columns for the "Applicant" entity from the
	 * tableColumn map, and iterates over the columns.
	 * 
	 * For each column, it attempts to retrieve the value for that column from the
	 * current Applicant object using the ExcelUtils.invokeGetter method, passing in
	 * the current Applicant object, the column name and the identityColumnMapping.
	 * 
	 * The value returned by this method is then set as the value of the cell in the
	 * current row and column. If an introspection exception occur it will print the
	 * stacktrace of the exception
	 * 
	 * After all the data is written to the Excel document, the method returns the
	 * Excel document as a byte array using excelWriter.toByteArray() and log "going
	 * to return file"
	 */
	private byte[] setTableData(List<Applicant> applicant, XSSFWorkbook templatePath, int rowCount) throws IOException {
		Map<String, List<String>> tableColumn = new HashMap<>();
		String entity = APPLICANT;
		Map<String, String> identityColumnMapping = new HashMap<>();
		Map<String, List<String>> templateHeaders = new HashMap<>();
		ExcelUtils.parseMappeddJson(tableColumn, identityColumnMapping, templateHeaders);
		log.info("table column map is :{}", tableColumn);
		try (ExcelWriter excelWriter = new ExcelWriter(templatePath)) {
			excelWriter.getWorkbook().setActiveSheet(0);
			if (CollectionUtils.isNotEmpty(applicant)) {
				for (Applicant applicantDetails : applicant) {
					ExcelRow row = excelWriter.getOrCreateRow(0, rowCount);
					int index = 0;
					List<String> columns = tableColumn.get(entity);
					for (String column : columns) {
						if (column != null) {
							try {
								row.setCellValue(index, ExcelUtils
										.invokeGetter(applicantDetails, column, identityColumnMapping).toString());
							} catch (IntrospectionException e) {
								log.error("IntrospectionException occurred: {}", e.getMessage());
							}
						}
						++index;
					}
					rowCount++;
				}
			}
			log.info("going to return file");
			return excelWriter.toByteArray();
		}
	}

	/**
	 * This method is responsible for importing data from an Excel file,
	 * specifically data related to Applicant objects. The method takes in a
	 * MultipartFile object, which represents the Excel file containing the data.
	 * The method then validates the template headers in the Excel file and if they
	 * are valid, it saves the data to the database.
	 *
	 * @param excelFile a MultipartFile object representing the Excel file
	 *                  containing the data
	 * @return a string indicating whether the data import was successful or not.
	 * @throws IOException            if there is an issue reading from the Excel
	 *                                file
	 * @throws InstantiationException when there is issue with instantiation
	 * @throws ClassNotFoundException when the class not found
	 */
	@Override
	public String importData(MultipartFile excelFile)
			throws IOException, InstantiationException, ClassNotFoundException {
		List<Applicant> applicants = new ArrayList<>();
		Excel workBook = new Excel(excelFile.getInputStream());
		Map<String, List<String>> tableColumn = new HashMap<>(); // Table Name and list of Columns
		Map<String, String> columnsMapping = new HashMap<>(); // Json Mapping DispalyName and Name
		Map<String, List<String>> templateHeadres = new HashMap<>();
		List<String> displayNames = new ArrayList<>();
		ExcelUtils.parseMappeddJson(tableColumn, columnsMapping, templateHeadres);
		displayNames.addAll(templateHeadres.get(APPLICANT));
		List<String> columnNames = new ArrayList<>();
		columnNames.addAll(tableColumn.get(APPLICANT));
		boolean validateTableTemplateHeader = ExcelUtils.validateTableTemplateHeader(workBook, displayNames);// Validating
																												// Columns
																												// and
																												// Headers
		if (validateTableTemplateHeader) {
			log.info("columns and headers are validated");
			applicants = saveData(workBook, columnsMapping, columnNames);
		} else {
			log.info("columns and headers invalide");
		}
		if (CollectionUtils.isNotEmpty(applicants)) {
			List<Applicant> applicantsList = applicantRepository.saveAll(applicants);
			JSONArray rowJson = setApplicantDataArrayFromObjects(applicants);
			log.debug(ROW_JSON_FORMED_IS2, rowJson);
			try {
				String response = vectorIntegrationService.createVectorAndInsertForImport(rowJson, APPLICANT);
				log.debug("Response from createVectorAndInsert in case of Importing Applicants  is : {} ", response);
			} catch (BusinessException be) {
				log.error("Caught error for creating and inserting Vector at time of Import : {} ", be.getMessage());
			} catch (Exception e) {
				log.error(SOMETHING_WENT_WRONG, e);
			}
			String actionRequired = mapToJson(applicantsList);
			TriggerBpmnAspect triggerBpmn = ApplicationContextProvider.getApplicationContext()
					.getBean(TriggerBpmnAspect.class);
			log.info("calling trigger api ");
			triggerBpmn.triggerBpmnViaAPI(actionRequired, "HRMS_APP_NAME", APPLICANT);
			log.info("after calling trigger api ");
			return APIConstants.SUCCESS_JSON;
		}
		return APIConstants.FAILURE_JSON;
	}

	/**
	 * This method is responsible for saving data to the database, specifically data
	 * related to Applicant objects. The method takes in an Excel object, which
	 * represents the sheet containing the data, a mapping of columns from the excel
	 * sheet to the Applicant class, and a list of column names. The method uses the
	 * iterator for the sheet to read data row by row, create new Applicant objects,
	 * and set the properties of the Applicant objects using the column mapping and
	 * column names. The method returns a list of Applicant objects that have been
	 * saved to the database.
	 *
	 * @param sheet         an Excel object representing the sheet containing the
	 *                      data
	 * @param columnMapping a map representing the mapping of columns from the excel
	 *                      sheet to the Applicant class
	 * @param columnNames   a list of column names of the excel sheet
	 * @return a list of Applicant objects that have been saved to the database
	 */

	public List<Applicant> saveData(Excel sheet, Map<String, String> columnMapping, List<String> columnNames) {
		Iterator<ExcelRow> rowIterator = sheet.iterator();
		List<Applicant> applicants = new ArrayList<>();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			ExcelRow excelRow = rowIterator.next();
			Applicant applicant = new Applicant();
			int index = -1;
			for (String columnName : columnNames) {
				try {
					ExcelUtils.invokeSetter(applicant, columnName, excelRow.getString(++index));
				} catch (InstantiationException e) {
					log.error("failed while going to set the value :{}", excelRow.getString(++index));
					log.error("InstantiationException occurred: {}", e.getMessage());

				} catch (ClassNotFoundException e) {
					log.error("ClassNotFoundException occurred: {}", e.getMessage());
				}
			}
			generateIdByNaming(applicant);
			CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
			Integer workspaceId = customerInfo.getActiveUserSpaceId();
			applicant.setWorkspaceId(workspaceId); // done done
			JSONObject rowJson = setApplicantDataFromObjects(applicant);
			log.debug(ROW_JSON_FORMED_IS2, rowJson);
			try {
				String response = vectorIntegrationService.createVectorAndInsert(rowJson, APPLICANT);
				log.debug("Response from createVectorAndInsert is : {} ", response);
			} catch (BusinessException be) {
				log.error(CAUGHT_ERROR_FOR_CREATING_AND_INSERTING_VECTOR, be.getMessage());
			} catch (Exception e) {
				log.error(SOMETHING_WENT_WRONG, e);
			}
			applicants.add(applicant);
		}
		return applicants;
	}

	private void generateIdByNaming(Applicant applicant) {
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("applicantFixedRule", mp,
				Status.ALLOCATED);
		log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		generatedName = nameGenerationWrapperV2.getGeneratedName();
		applicant.setApplicantUniqueId(generatedName);
	}

	private String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * This method is responsible for soft-deleting an Applicant record in the
	 * database. The method takes in an int id which represents the id of the
	 * Applicant that needs to be soft-deleted. It uses the id to find the Applicant
	 * by calling the ApplicantRepository.findById method. If the Applicant is
	 * found, it sets the "deleted" field to true, save the Applicant in the
	 * repository, and saves it in the database
	 *
	 * @param id an int representing the id of the Applicant that needs to be
	 *           soft-deleted
	 */
	@Override
	public void softDelete(int id) {

		Applicant applicant = super.findById(id);

		if (applicant != null) {

			Applicant applicant1 = applicant;

			applicant1.setDeleted(true);
			CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
			Integer workspaceId = customerInfo.getActiveUserSpaceId();

			applicant1.setWorkspaceId(workspaceId); // done done
			applicantRepository.save(applicant1);

		}
	}

	/**
	 * This method is responsible for soft-deleting multiple Applicant records in
	 * the database in bulk. The method takes in a List of integers, each
	 * representing the id of an Applicant that needs to be soft-deleted. It
	 * iterates through the list, calling the softDelete method for each id passed
	 * in the list.
	 *
	 * @param list a List of integers representing the ids of the Applicant that
	 *             need to be soft-deleted
	 */
	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

	/**
	 * Creates a new vendor.
	 *
	 * @param applicant The applicant object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Applicant create(Applicant applicant) {
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("applicantFixedRule", mp,
				Status.ALLOCATED);
		log.info("Inside create NameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		generatedName = nameGenerationWrapperV2.getGeneratedName();
		applicant.setApplicantUniqueId(generatedName);
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		applicant.setWorkspaceId(workspaceId);
		User contextUser = getUserContext();
		log.debug("User id is", contextUser.getUserid());
		applicant.setUserId(contextUser.getUserid());

		Applicant applicantResponse = applicantRepository.save(applicant);
		JSONObject rowJson = setApplicantDataFromObjects(applicantResponse);
		log.debug(ROW_JSON_FORMED_IS2, rowJson);
		try {
			String response = vectorIntegrationService.createVectorAndInsert(rowJson, APPLICANT);
			log.debug("Response from createVectorAndInsert is : {} ", response);
		} catch (BusinessException be) {
			log.error(CAUGHT_ERROR_FOR_CREATING_AND_INSERTING_VECTOR, be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		return applicantResponse;
	}

	private JSONArray setApplicantDataArrayFromObjects(List<Applicant> applicants) {
		log.info(INSIDE_METHOD, "setApplicantDataArrayFromObjects");
		JSONArray dataArray = new JSONArray();
		for (Applicant applicant : applicants) {
			JSONObject rawJson = setApplicantDataFromObjects(applicant);
			dataArray.put(rawJson);
		}
		return dataArray;
	}

	@Override
	public List<JobOpening> findSuitableJobOpeningByApplicantId(@Valid Integer applicantId) {
		log.info(INSIDE_METHOD, "findSuitableJobOpeningByApplicantId");
		Applicant applicantOne = new Applicant();
		JSONArray arrayOfResult = null;
		List<JobOpening> jobOpeningList = new ArrayList<>();
		Applicant applicant = super.findById(applicantId);
		if (applicant != null) {
			applicantOne = applicant;
		}
		String searchString = createSearchString(applicantOne);
		try {
			String responseFromVectorSearch = vectorIntegrationService.searchByVector(searchString, "JobOpening");
			log.debug("Response from Searching By Vector is : {} ", responseFromVectorSearch);
			JSONObject fullResponse = new JSONObject(responseFromVectorSearch);
			arrayOfResult = fullResponse.getJSONArray(RESULT);
			log.debug("Result of Id's is : {} ", arrayOfResult);
			List<Integer> jobOpeningIdList = new ArrayList<>();
			for (int i = 0; i < arrayOfResult.length(); i++) {
				JSONObject jsonObject = arrayOfResult.getJSONObject(i);
				if (jsonObject.getDouble("distance") < 1) {
					int id = jsonObject.getInt("id");
					jobOpeningIdList.add(id);
				}
			}
			log.debug("JobOpening Id's are : {} ", jobOpeningIdList);
			jobOpeningList = jobOpeningRepository.findAllById(jobOpeningIdList);
			return jobOpeningList;
		} catch (BusinessException be) {
			log.error("Caught error Searching By Vector : {} ", be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		return jobOpeningList;
	}

	@Override
	public List<JobOpening> findSuitableJobOpeningByApplicantObject(ApplicantWrapper applicantWrapper) {
		log.info(INSIDE_METHOD, "findSuitableJobOpeningByApplicantObject");
		JSONArray arrayOfResult = null;
		List<JobOpening> jobOpeningList = new ArrayList<>();
		log.debug("Applicant Wrapper coming is : {} ", applicantWrapper);
		String searchString = createSearchStringFromWrapper(applicantWrapper);
		try {
			String responseFromVectorSearch = vectorIntegrationService.searchByVector(searchString, "JobOpening");
			log.debug("Response from Searching By Vector is : {} ", responseFromVectorSearch);
			JSONObject fullResponse = new JSONObject(responseFromVectorSearch);
			arrayOfResult = fullResponse.getJSONArray(RESULT);
			log.debug("Result of Id's fetched is : {} ", arrayOfResult);
			List<Integer> jobOpeningIdList = new ArrayList<>();
			for (int i = 0; i < arrayOfResult.length(); i++) {
				JSONObject jsonObject = arrayOfResult.getJSONObject(i);
				if (jsonObject.getDouble("distance") < 1) {
					int id = jsonObject.getInt("id");
					jobOpeningIdList.add(id);
				}
			}
			log.debug("JobOpening Id's are : {} ", jobOpeningIdList);
			jobOpeningList = jobOpeningRepository.findAllById(jobOpeningIdList);
			return jobOpeningList;
		} catch (BusinessException be) {
			log.error("Caught error Searching By Vector : {} ", be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		return jobOpeningList;
	}

	private JSONObject setApplicantDataFromObjects(Applicant applicant) {
		log.info(INSIDE_METHOD, "setApplicantDataFromObjects");

		log.debug("applicant is : {}", applicant);

		try {
			JSONObject jsonObject = new JSONObject();
			addValueIfNotNull(jsonObject, "id", applicant.getId());

			if (applicant.getCity() != null && applicant.getCity().getName() != null) {
				addValueIfNotNull(jsonObject, "city", applicant.getCity().getName());
			}
			if (applicant.getCountry() != null && applicant.getCountry().getName() != null) {
				addValueIfNotNull(jsonObject, COUNTRY, applicant.getCountry().getName());
			}
			addValueIfNotNull(jsonObject, CURRENT_JOB_TITLE, applicant.getCurrentJobTitle());
			addValueIfNotNull(jsonObject, "applicantSource", applicant.getApplicantSource());
			if (applicant.getCurrentSalary() != null) {
				addValueIfNotNull(jsonObject, CURRENT_SALARY, applicant.getCurrentSalary().toString());
			} else {
				addValueIfNotNull(jsonObject, CURRENT_SALARY, "0");
			}

			log.debug("jsonObject succesfully set p1");

			addValueIfNotNull(jsonObject, EXPECTED_SALARY, applicant.getExpectedSalary());
			addValueIfNotNull(jsonObject, FIRST_NAME, applicant.getFirstName());
			addValueIfNotNull(jsonObject, LAST_NAME, applicant.getLastName());
			addValueIfNotNull(jsonObject, SKILL_SET, applicant.getSkillSet());
			addValueIfNotNull(jsonObject, "applicantUniqueId", applicant.getApplicantUniqueId());
			addValueIfNotNull(jsonObject, "applicantStatus", applicant.getApplicantStatus());

			log.debug("jsonObject succesfully set p2");

			addValueIfNotNull(jsonObject, "fullName", applicant.getFullName());
			addValueIfNotNull(jsonObject, "arabicFirstName", applicant.getArabicFirstName());
			addValueIfNotNull(jsonObject, "arabicLastName", applicant.getArabicLastName());
			addValueIfNotNull(jsonObject, "arabicFullName", applicant.getArabicFullName());
			addValueIfNotNull(jsonObject, "countryOfResidence", applicant.getCountryOfResidence());
			addValueIfNotNull(jsonObject, MARITAL_STATUS, applicant.getMaritalStatus());

			log.debug("jsonObject succesfully set p3");

			addValueIfNotNull(jsonObject, FIELD_OF_STUDY, applicant.getFieldOfStudy());
			addValueIfNotNull(jsonObject, "university", applicant.getUniversity());
			addValueIfNotNull(jsonObject, "graduationYear", applicant.getGraduationYear());
			addValueIfNotNull(jsonObject, PREFERRED_LOCATION, applicant.getPreferredLocation());
			addValueIfNotNull(jsonObject, PROFILE_SUMMARY, applicant.getProfileSummary());
			addValueIfNotNull(jsonObject, CITIZENSHIP_STATUS, applicant.getCitizenshipStatus());

			log.debug("jsonObject succesfully set p4");

			String searchString = createSearchString(applicant);
			addValueIfNotNull(jsonObject, "searchstring", searchString);
			log.debug("JSONObject formed is : {} ", jsonObject);
			return jsonObject;
		} catch (Exception e) {
			throw new BusinessException("unable to set applicant data from objects");
		}
	}

	private void addValueIfNotNull(JSONObject jsonObject, String key, Object value) {
		if (value != null) {
			jsonObject.put(key, value);
		} else {
			jsonObject.put(key, "");
		}
	}

	private String createSearchString(Applicant applicant) {
		log.debug("Inside Method createSearchString");
		List<String> values = new ArrayList<>();
		values.add(String.valueOf(applicant.getId()));
		values.add(applicant.getCurrentJobTitle());
		values.add(applicant.getApplicantSource());
		values.add(String.valueOf(applicant.getCurrentSalary()));
		values.add(String.valueOf(applicant.getExpectedSalary()));

		log.debug("jsonObject succesfully set p2 inside createSearchString");

		values.add(applicant.getFirstName());
		values.add(applicant.getLastName());
		values.add(applicant.getSkillSet());
		values.add(applicant.getApplicantUniqueId());
		values.add(applicant.getApplicantStatus());

		if (applicant.getCity() != null && applicant.getCity().getName() != null) {
			values.add(applicant.getCity().getName());
		}
		if (applicant.getCountry() != null && applicant.getCountry().getName() != null) {
			values.add(applicant.getCountry().getName());
		}

		log.debug("jsonObject succesfully set p1 inside createSearchString");

		values.add(applicant.getFullName());
		values.add(applicant.getArabicFirstName());
		values.add(applicant.getArabicLastName());
		values.add(applicant.getArabicFullName());
		values.add(applicant.getCountryOfResidence());
		values.add(applicant.getMaritalStatus());
		values.add(applicant.getFieldOfStudy());
		values.add(applicant.getUniversity());
		values.add(applicant.getGraduationYear());
		values.add(applicant.getPreferredLocation());
		values.add(applicant.getProfileSummary());
		values.add(applicant.getCitizenshipStatus());

		log.debug("jsonObject succesfully set p2 inside createSearchString");
		return String.join(",", values);
	}

	private String createSearchStringFromWrapper(ApplicantWrapper applicantWrapper) {
		log.debug("Inside Method createSearchStringFromWrapper");
		List<String> values = new ArrayList<>();
		values.add(applicantWrapper.getCity() != null && applicantWrapper.getCity().getName() != null
				? applicantWrapper.getCity().getName()
				: "");
		values.add(applicantWrapper.getCountry() != null && applicantWrapper.getCountry().getName() != null
				? applicantWrapper.getCountry().getName()
				: "");
		values.add(applicantWrapper.getPresentAddress() != null ? applicantWrapper.getPresentAddress() : "");
		values.add(applicantWrapper.getCurrentEmployer() != null ? applicantWrapper.getCurrentEmployer() : "");
		values.add(applicantWrapper.getFirstName() != null ? applicantWrapper.getFirstName() : "");
		values.add(applicantWrapper.getLastName() != null ? applicantWrapper.getLastName() : "");
		values.add(applicantWrapper.getSkillSet() != null ? applicantWrapper.getSkillSet() : "");
		values.add(
				applicantWrapper.getHighestQualification() != null ? applicantWrapper.getHighestQualification() : "");
		values.add(applicantWrapper.getCurrentJobTitle() != null ? applicantWrapper.getCurrentJobTitle() : "");
		values.add(applicantWrapper.getEmailId() != null ? applicantWrapper.getEmailId() : "");
		values.add(applicantWrapper.getFullName() != null ? applicantWrapper.getFullName() : "");
		values.add(applicantWrapper.getPhone() != null ? applicantWrapper.getPhone() : "");
		if (applicantWrapper.getCurrentSalary() != null) {
			values.add(String.valueOf(applicantWrapper.getCurrentSalary()));
		}
		if (applicantWrapper.getExpectedSalary() != null) {
			values.add(String.valueOf(applicantWrapper.getExpectedSalary()));
		}
		return String.join(",", values);
	}

	@Override
	public String compareApplicantsForJobOpening(@Valid Integer applicant1Id, @Valid Integer applicant2Id,
			@Valid Integer jobOpeningId) {
		log.debug("Inside Method compareApplicantsForJobOpening");
		Applicant applicantOne = new Applicant();
		Applicant applicantTwo = new Applicant();
		JobOpening jobOpening1 = new JobOpening();
		String response = "";
		JSONObject result = new JSONObject();
		Applicant applicant1 = super.findById(applicant1Id);
		if (applicant1 != null) {
			applicantOne = applicant1;
		}
		log.debug("Applicant1 is : {} ", applicantOne);
		Applicant applicant2 = super.findById(applicant2Id);
		if (applicant2 != null) {
			applicantTwo = applicant2;
		}
		log.debug("Applicant2 is : {} ", applicantTwo);
		JobOpening jobOpening = jobOpeningService.findById(jobOpeningId);
		if (jobOpening != null) {
			jobOpening1 = jobOpening;
		}
		log.debug("jobOpening1 is : {} ", jobOpening1);
		String prompt = "";
		prompt = setDataForComparisonFromObjects(applicantOne, applicantTwo, jobOpening1);
		try {
			response = vectorIntegrationService.compareObjectsPrompt(prompt);
			log.debug("Response from compareObjectsPrompt is : {} ", response);
		} catch (BusinessException be) {
			log.error("Caught error for Compare Objects Prompt  : {} ", be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		JSONArray stringWithoutBrackets = null;
		JSONObject fullResponse = new JSONObject(response);

		if (null != fullResponse.getString(RESPONSE)) {
			String response1 = fullResponse.getString(RESPONSE);
			log.debug("Response key  from fullResponse is : {} ", response1);
		}
		stringWithoutBrackets = extractJsonArray(fullResponse.getString(RESPONSE));
		if (stringWithoutBrackets == null) {
			result.put(RESULT, "failure");
		} else {
			result.put(RESULT, "success");
			result.put(RESPONSE, stringWithoutBrackets);
		}
		return result.toString();
	}

	public JSONArray extractJsonArray(String input) {
		log.debug("Inside Method extractJsonArray");
		try {
			int startIndex = input.indexOf("[");
			int endIndex = input.indexOf("]", startIndex);
			String jsonArrayStr = input.substring(startIndex, endIndex + 1);
			return new JSONArray(jsonArrayStr);
		} catch (JSONException | StringIndexOutOfBoundsException e) {
			log.error("Inside @Class ApplicantServiceImpl @Method extractJsonArray generates the exception :{}",
					e.getMessage());
			return null;
		}
	}

	private String setDataForComparisonFromObjects(Applicant applicantOne, Applicant applicantTwo,
			JobOpening jobOpening1) {
		log.info(INSIDE_METHOD, "setDataForComparisonFromObjects");
		JSONObject applicant1Data = setDataForApplicant(applicantOne);
		log.debug("applicant1Data JSONObject is : {} ", applicant1Data);
		JSONObject applicant2Data = setDataForApplicant(applicantTwo);
		log.debug("applicant2Data JSONObject is : {} ", applicant2Data);
		JSONObject jobOpening1Data = setDataForJobOpening(jobOpening1);
		log.debug("jobOpening1Data JSONObject is : {} ", jobOpening1Data);
		String prompt = generatePrompt(jobOpening1Data.toString(), applicant1Data.toString(),
				applicant2Data.toString());
		log.debug("FinalObject on basis of 3 objects is : {} ", prompt);
		return prompt;
	}

	private JSONObject setDataForApplicant(Applicant applicant) {
		log.info(INSIDE_METHOD, "setDataForApplicant");
		JSONObject jsonObject = new JSONObject();
		addValueIfNotNull(jsonObject, "name", applicant.getFirstName());
		addValueIfNotNull(jsonObject, CURRENT_JOB_TITLE, applicant.getCurrentJobTitle());
		if (applicant.getExperienceInYears() != null) {
			addValueIfNotNull(jsonObject, EXPERIENCE_IN_YEARS, applicant.getExperienceInYears().toString());
		} else {
			addValueIfNotNull(jsonObject, EXPERIENCE_IN_YEARS, null);
		}
		addValueIfNotNull(jsonObject, HIGHEST_QUALIFICATION, applicant.getHighestQualification());
		addValueIfNotNull(jsonObject, SKILL_SET, applicant.getSkillSet());
		if (applicant.getExpectedSalary() != null) {
			addValueIfNotNull(jsonObject, EXPECTED_SALARY, applicant.getExpectedSalary().toString());
		} else {
			addValueIfNotNull(jsonObject, EXPECTED_SALARY, null);
		}
		return jsonObject;
	}

	private JSONObject setDataForJobOpening(JobOpening jobOpening) {
		log.info(INSIDE_METHOD, "setDataForJobOpening");
		JSONObject jsonObject = new JSONObject();
		addValueIfNotNull(jsonObject, "Title", jobOpening.getPostingTitle());
		addValueIfNotNull(jsonObject, "descriptionRequirements", jobOpening.getDescriptionRequirements());
		addValueIfNotNull(jsonObject, "jobType", jobOpening.getJobType());
		addValueIfNotNull(jsonObject, "salaryRange", jobOpening.getFromSalary());
		addValueIfNotNull(jsonObject, SKILLS, jobOpening.getSkills());
		addValueIfNotNull(jsonObject, "workExperience", jobOpening.getWorkExperience());
		addValueIfNotNull(jsonObject, "industry", jobOpening.getIndustry());
		return jsonObject;
	}

	public static String generatePrompt(String variable1, String variable2, String variable3) {
		log.info(INSIDE_METHOD, "generatePrompt");
		String prompt = "Given the following information:\n\n**Job Position:**\n%s\n\n**Candidates:**\n1. Candidate 1\n%s\n\n2. Candidate 2\n%s\n\nIdentify dimensions and evaluate candidates based on the information provided for the job position. Assign a score from 1 to 10 for each dimension for both candidates and calculate an overall score for the dimension \"Overall Skills Match\"\n\nPlease consider the job position details and assign scores based on the relevance and strength of each candidate's qualifications. Provide a brief rationale for each score.\n\n**Output Format:**\nGenerate a JSON object that reflects the evaluation table, like the example below:\n\n[{ \"dimension\":\"String: evaluation parameter corresponding to job position based for candidates\",\n\"candidate1Name\":\"String: name of candidate 1\",\n\"candidate2Name\":\"String: name of candidate 2\",\n\"candidate1Score\":\"String: score of candidate 1 for this dimension\",\n\"candidate2Score\":\"String: score of candidate 2 for this dimension\",\n\"Rationale\": \"String: Rationale behind scores for each candidate\"}]\nNOTE: AT LEAST 5 DIMENSIONS ARE NECESSARY. OVERALL SKILLS MATCH IS MANDATORY IN DIMENSIONS";

		return String.format(prompt, variable1, variable2, variable3);
	}

	@Override
	public String applicantRecommendationForJobOpening(@Valid Integer applicantId, @Valid Integer jobOpeningId) {
		log.debug("Inside Method ApplicantRecommendationForJobOpening");
		Applicant applicantOne = new Applicant();
		JobOpening jobOpening1 = new JobOpening();
		String response = "";
		JSONObject result = new JSONObject();
		Applicant applicant1 = super.findById(applicantId);
		if (applicant1 != null) {
			applicantOne = applicant1;
		}
		log.debug("Applicant1 is : {} ", applicantOne);
		JobOpening jobOpening = jobOpeningService.findById(jobOpeningId);
		if (jobOpening != null) {
			jobOpening1 = jobOpening;
		}
		log.debug("jobOpening1 is : {} ", jobOpening1);
		String prompt = "";
		prompt = setDataForRecommendationForObjects(applicantOne, jobOpening1);
		try {
			response = vectorIntegrationService.compareObjectsPrompt(prompt);
			log.debug(RESPONSE_FROM_PROMPT_CURL_IS, response);
		} catch (BusinessException be) {
			log.error("Caught error for Recommendation Objects Prompt  : {} ", be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		JSONArray stringWithoutBrackets = null;
		JSONObject fullResponse = new JSONObject(response);
		if (null != fullResponse.getString(RESPONSE)) {
			String response1 = fullResponse.getString(RESPONSE);
			log.debug("Response key  from fullResponse is : {} ", response1);
		}
		stringWithoutBrackets = extractJsonArray(fullResponse.getString(RESPONSE));
		if (stringWithoutBrackets == null) {
			result.put(RESULT, "failure");
		} else {
			result.put(RESULT, "success");
			result.put(RESPONSE, stringWithoutBrackets);
		}
		return result.toString();
	}

	private String setDataForRecommendationForObjects(Applicant applicantOne, JobOpening jobOpening1) {
		log.info(INSIDE_METHOD, "setDataForRecommendationForObjects");
		JSONObject applicant1Data = setDataForApplicant(applicantOne);
		log.debug("applicant1Data JSONObject is : {} ", applicant1Data);
		JSONObject jobOpening1Data = setDataForJobOpening(jobOpening1);
		log.debug("jobOpening1Data JSONObject is : {} ", jobOpening1Data);
		String prompt = generatePromptForRecommendation(jobOpening1Data.toString(), applicant1Data.toString());
		log.debug("FinalObject on basis of 2 objects is : {} ", prompt);
		return prompt;
	}

	public static String generatePromptForRecommendation(String variable1, String variable2) {
		log.info(INSIDE_METHOD, "generatePrompt");

		String prompt = "Given the following information:\n\n**Job Position:**\n%s\n\n**Candidates:**\n1. Candidate 1\n%s\n\nIdentify dimensions and evaluate candidates based on the information provided for the job position. Assign a score from 1 to 10 for each dimension of the candidate and calculate an overall score for a dimension \"Overall Skills Match\"\n\nPlease consider the job position details and assign scores based on the relevance and strength of the candidate's qualifications. Provide a brief rationale for each score.\n\n**Output Format:**\nGenerate a JSON object that reflects the evaluation table, like the example below:\n\n[{ \"dimension\":\"String: evaluation parameter corresponding to job position based for candidates\",\"score\":\"String: score of candidate for this dimension\",\"Rationale\": \"String: Rationale behind scores for each candidate\"}]\nNOTE: ATLEAST 5 DIMENSIONS ARE NECESSARY. OVERALL SKILLS MATCH IS MANDATORY IN DIMENSIONS";

		return String.format(prompt, variable1, variable2);
	}

	@Override
	public List<Object[]> reviewTheSalaryForJobOfferPositionAndCandidate() {
		log.debug("Inside reviewTheSalaryForJobOfferPositionAndCandidate customerId is : {}",
				commonUtils.getCustomerId());
		return applicantRepository.reviewTheSalaryForJobOfferPositionAndCandidate(commonUtils.getCustomerId());
	}

	@Override
	public List<Object[]> reviewJobOfferForPositionAndCandidate() {
		log.debug("Inside reviewJobOfferForPositionAndCandidate customerId is : {}", commonUtils.getCustomerId());
		return applicantRepository.reviewJobOfferForPositionAndCandidate(commonUtils.getCustomerId());
	}

	@Override
	public List<Object[]> findApplicantByCreatedDate() {
		log.debug("Inside findApplicantByCreatedDate customerId is : {}", commonUtils.getCustomerId());
		return applicantRepository.findApplicantIdByCreatedDate(commonUtils.getCustomerId());
	}

	@Override
	public Applicant createApplicant(ApplicantWrapper applicantWrapper) {

		log.info("Inside Applicant ServiceImpl @method createApplicant");

		Applicant applicant = new Applicant();

		applicant.setFirstName(applicantWrapper.getFirstName());
		applicant.setLastName(applicantWrapper.getLastName());
		if (applicantWrapper.getFullName() == null && applicantWrapper.getFirstName() != null
				&& applicantWrapper.getLastName() != null) {
			applicant.setFullName(applicantWrapper.getFirstName() + " " + applicantWrapper.getLastName());
		} else {
			applicant.setFullName(applicantWrapper.getFullName());
		}
		applicant.setPhone(applicantWrapper.getPhone());
		applicant.setEmailId(applicantWrapper.getEmailId());
		applicant.setCity(applicantWrapper.getCity());
		applicant.setCountry(applicantWrapper.getCountry());
		applicant.setCurrentEmployer(applicantWrapper.getCurrentEmployer());
		applicant.setCurrentJobTitle(applicantWrapper.getCurrentJobTitle());
		applicant.setHighestQualification(applicantWrapper.getHighestQualification());
		applicant.setMobile(applicantWrapper.getMobile());
		applicant.setSkillSet(applicantWrapper.getSkillSet());
		applicant.setStreet(applicantWrapper.getStreet());
		applicant.setPresentAddress(applicantWrapper.getPresentAddress());

		try {
			Applicant savedApplicant = applicantRepository.save(applicant);
			log.info("Applicant created successfully with ID: {}", savedApplicant.getId());
			return savedApplicant;
		} catch (Exception e) {
			log.error("Error creating applicant", e);
			throw new BusinessException("Error creating applicant", e);
		}
	}

	private static StringBuilder readPdfFile(File pdfFile) throws IOException {
		StringBuilder content = new StringBuilder();
		try (PDDocument document = Loader.loadPDF(pdfFile)) {
			if (!document.isEncrypted()) {
				PDFTextStripper pdfStripper = new PDFTextStripper();
				String text = pdfStripper.getText(document);
				content.append(text);
			}
			return content;
		} catch (IOException e) {
			log.error("Error while reading pdf document ", e);
		}
		// PdfReader reader = new PdfReader(Files.readAllBytes(pdfFile.toPath()));
		// int pages = reader.getNumberOfPages();
		// for (int i = 1; i <= pages; i++) {
		// content.append(PdfTextExtractor.getTextFromPage(reader, i));
		// }
		// reader.close();
		return content;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public ApplicantPromptWrapper updateApplicantFromResumeFolder() {

		ApplicantPromptWrapper applicantPromptWrapper = new ApplicantPromptWrapper();
		try {
			log.info("Inside ApplicantServiceImpl @method updateApplicantFromResumeFolder");
			String stringResponse = "";
			String response = "";
			String responseSummary = "";
			String prompt = "";
			String promptResumeSummary = "";
			StringBuilder resumeContent = new StringBuilder();
			List<Map<String, String>> applicantEducationList = new ArrayList<>();
			List<Map<String, String>> applicantLanguageList = new ArrayList<>();
			List<String> skillsList = new ArrayList<>();
			Map<String, Object> resultMap = new HashMap<>();
			List<Map<String, String>> applicantCertificationsList = new ArrayList<>();
			List<Map<String, String>> applicantExperienceList = new ArrayList<>();
			Applicant createdApplicant = new Applicant();
			StringBuilder textContent = new StringBuilder();
			log.info(
					"Inside ApplicantServiceImpl @method updateApplicantFromResumeFolder , going to fetch path for folder of resumes");
			final String RESUME_FOLDER_PATH = "/opt/visionwaves/hrms/resumes";
			log.debug("Fetched path for folder of resumes is : {} ", RESUME_FOLDER_PATH);
			File folder = new File(RESUME_FOLDER_PATH);
			if (folder.exists() && folder.isDirectory()) {
				log.debug("Folders Found : {} ", folder.getAbsolutePath());
				File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
				if (pdfFiles != null) {
					resumeContent = iterateForPdfFilesToReadPdfFiles(resumeContent, pdfFiles);
				} else {
					log.error("No PDF files found in the directory.");
				}
			} else {
				log.error("Invalid folder path or directory does not exist.");
			}
			log.debug(" resumeContent formed is : {} ", resumeContent);
			try {
				prompt = setDataForResumeDetails(resumeContent);
				response = vectorIntegrationService.executePrompt(prompt);
				log.debug(RESPONSE_FROM_PROMPT_CURL_IS, response);
			} catch (BusinessException be) {
				log.error("Caught error for summarise Data from Prompt : {} ", be.getMessage());
			} catch (Exception e) {
				log.error(SOMETHING_WENT_WRONG, e);
			}
			JSONObject fullResponse = new JSONObject(response);
			log.debug("JsonObject  fullResponse is : {} ", fullResponse);
			if (fullResponse.has(RESPONSE)) {
				try {
					JSONObject appliantDetails = fullResponse.getJSONObject(RESPONSE);
					stringResponse = appliantDetails.toString();
					log.debug("stringResponse converted  is : {} ", stringResponse);
				} catch (Exception e) {
					log.error(UNABLE_TO_PARSE_RESPONSE_FROM_PROMPT_AFTER_READING_CV_RESUME, e);
				}
			}
			try {
				promptResumeSummary = setDescriptionSummaryForResumeDetails(textContent);
				responseSummary = vectorIntegrationService.executePrompt(promptResumeSummary);
				log.debug(RESPONSE_FROM_PROMPT_CURL_IS, responseSummary);
			} catch (BusinessException be) {
				log.error(CAUGHT_ERROR_FOR_RESUME_SUMMARY_FROM_PROMPT, be.getMessage());
			} catch (Exception e) {
				log.error(SOMETHING_WENT_WRONG, e);
			}
			String resumeResponseSummary = "";
			JSONObject fullResponseforSummary = new JSONObject(responseSummary);
			if (fullResponseforSummary.has(RESPONSE)) {
				try {
					JSONObject responseSummaryObj = fullResponseforSummary.getJSONObject(RESPONSE);
					if (responseSummaryObj.get(PROFILE_SUMMARY) != null) {
						resumeResponseSummary = responseSummaryObj.getString(PROFILE_SUMMARY);
						log.debug(RESUME_RESPONSE_SUMMARY_KEY_FROM_FULL_RESPONSEFOR_SUMMARY_IS, resumeResponseSummary);
					}
				} catch (Exception e) {
					log.error(UNABLE_TO_PARSE_RESPONSE_FROM_PROMPT_AFTER_READING_CV_RESUME, e);
				}
			}
			extractResponseFromPrompt(stringResponse, applicantEducationList, applicantLanguageList,
					applicantCertificationsList, applicantExperienceList, skillsList, resultMap);

			Applicant applicant = getApplicantByUserId(customerInfo.getUserId());
			if (applicant == null) {
				applicant = new Applicant();
			}
			createdApplicant = setApplicantFromPromptDetails(applicant, stringResponse, resumeResponseSummary,
					applicantEducationList, applicantExperienceList, null);

			ApplicantEducationService applicantEducationService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantEducationService.class);
			List<ApplicantEducation> applicantEducationLists = applicantEducationService
					.createApplicationEducationfromPropt(applicantEducationList, createdApplicant);
			ApplicantLanguageService applicantLanguageService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantLanguageService.class);
			List<ApplicantLanguage> applicantLanguageLists = applicantLanguageService
					.createApplicantLanguagefromPrompt(applicantLanguageList, createdApplicant);

			ApplicantCertificationsService applicantCertificationsService = ApplicationContextProvider
					.getApplicationContext().getBean(ApplicantCertificationsService.class);

			List<ApplicantCertifications> applicantCertificationsLists = applicantCertificationsService
					.createApplicantCertificationsfromPropt(applicantCertificationsList, createdApplicant);

			ApplicantExperienceService applicantExperienceService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantExperienceService.class);

			List<ApplicantExperience> applicantExperienceLists = applicantExperienceService
					.createApplicantExperiencefromPrompt(applicantExperienceList, createdApplicant);

			// log.debug("going to insert data into milvus collection for applicant ");
			// JSONObject rowJson = setApplicantDataFromObjects(createdApplicant);
			// log.debug(ROW_JSON_FORMED_IS2, rowJson);
			// try {
			// String insertionResponse =
			// vectorIntegrationService.createVectorAndInsert(rowJson, APPLICANT);
			// log.debug("insertionResponse from createVectorAndInsert is : {} ",
			// insertionResponse);
			// } catch (BusinessException be) {
			// log.error(CAUGHT_ERROR_FOR_CREATING_AND_INSERTING_VECTOR, be.getMessage());
			// } catch (Exception e) {
			// log.error(SOMETHING_WENT_WRONG, e);
			// }
			applicantPromptWrapper.setApplicant(createdApplicant);
			applicantPromptWrapper.setApplicantEducation(applicantEducationLists);
			applicantPromptWrapper.setApplicantLanguage(applicantLanguageLists);
			applicantPromptWrapper.setApplicantCertifications(applicantCertificationsLists);
			applicantPromptWrapper.setApplicantExperience(applicantExperienceLists);
			applicantPromptWrapper.setJobApplicationUpdateNeeded(Boolean.FALSE);
			return applicantPromptWrapper;
		} catch (BusinessException be) {
			log.error("error message thrown is : {} ", be.getMessage());
			throw new BusinessException(be.getMessage());
		} catch (Exception e) {
			Applicant applicant = new Applicant();
			ApplicantEducation applicantEducation = new ApplicantEducation();
			ApplicantExperience applicantExperience = new ApplicantExperience();
			ApplicantLanguage applicantLanguage = new ApplicantLanguage();
			ApplicantCertifications applicantCertifications = new ApplicantCertifications();
			List<ApplicantEducation> applicantEducationList = new ArrayList<>();
			List<ApplicantExperience> applicantExperienceList = new ArrayList<>();
			List<ApplicantLanguage> applicantLanguageList = new ArrayList<>();
			List<ApplicantCertifications> applicantCertificationsList = new ArrayList<>();
			applicantEducationList.add(applicantEducation);
			applicantExperienceList.add(applicantExperience);
			applicantLanguageList.add(applicantLanguage);
			applicantCertificationsList.add(applicantCertifications);
			applicantPromptWrapper.setApplicant(applicant);
			applicantPromptWrapper.setApplicantEducation(applicantEducationList);
			applicantPromptWrapper.setApplicantLanguage(applicantLanguageList);
			applicantPromptWrapper.setApplicantCertifications(applicantCertificationsList);
			applicantPromptWrapper.setApplicantExperience(applicantExperienceList);
			return applicantPromptWrapper;
		}
	}

	private StringBuilder iterateForPdfFilesToReadPdfFiles(StringBuilder resumeContent, File[] pdfFiles) {
		for (File pdfFile : pdfFiles) {
			log.debug("file name is : {} ", pdfFile.getName());
			log.debug("file name of Pdf File  is : {} ", pdfFile.getPath());
			log.debug("file name is : {} ", pdfFile.getName());
			log.debug("Path of Pdf File is : {} ", pdfFile.getPath());
			try {
				resumeContent = readPdfFile(pdfFile);
			} catch (IOException e) {
				System.err.println("Failed to read PDF file: " + pdfFile.getName());
			}
		}
		return resumeContent;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public ApplicantPromptWrapper updateApplicantWithResume(Integer documentId, String jobId) {

		log.debug("Inside @method updateApplicantWithResume jobId is : {} ", jobId);

		ApplicantPromptWrapper applicantPromptWrapper = new ApplicantPromptWrapper();
		try {
			log.info("Inside ApplicantServiceImpl @method updateApplicantWithResume");
			String stringResponse = "";
			String response = "";
			List<Document> documentList = new ArrayList<>();
			List<Map<String, String>> applicantEducationList = new ArrayList<>();
			List<Map<String, String>> applicantLanguageList = new ArrayList<>();
			List<String> skillsList = new ArrayList<>();
			Map<String, Object> resultMap = new HashMap<>();
			List<Map<String, String>> applicantCertificationsList = new ArrayList<>();
			List<Map<String, String>> applicantExperienceList = new ArrayList<>();
			Applicant createdApplicant = new Applicant();
			StringBuilder textContent = new StringBuilder();
			byte[] input = null;
			try {
				documentList = documentRest.getDocumentAllVersions(documentId);
				input = documentStreamRest.fileDownloadByteArray(documentId);
			} catch (Exception e) {
				log.error("Document Failed to Download With This Given documentId : {} ", documentId);
			}

			readWordAndPdfResume(documentList, textContent, input);

			log.debug(" textContent formed is : {} ", textContent);
			String textContentWithoutSpaceAndBrackets = textContent.toString().replaceAll("\\s+", " ")
					.replaceAll("[(){}\\[\\]]", "");

			log.debug("String  textContentWithoutSpaceAndBrackets is : {} ", textContentWithoutSpaceAndBrackets);
			
			JSONObject fullResponse = null;
			log.debug("JsonObject  fullResponse is : {} ", fullResponse);
			try {
				Map<String, Object> inputMap = new HashMap<>();
				inputMap.put("resumeContent", textContentWithoutSpaceAndBrackets);
				response = aiChatModel
						.chatCompletion("HRMS_APP_NAME-applicant-Applicant_Data_Extraction_From_Resume-v-1", inputMap);
				log.debug("Response from ai chat model is : {} ", response);
				
				String formattedReponse = extractJsonObjectString(response);
				log.debug("formattedResponse after formating is : {}", formattedReponse);
				fullResponse = new JSONObject(formattedReponse);
				log.debug("JSONObject fullResponse for sdk is : {} ", fullResponse);

			} catch (Exception ex) {
				log.error("Error while Generating Response From Sdk for Resume Content : {} , Trace : {}", ex.getMessage(),Utils.getStackTrace(ex));
			}
			if (fullResponse.has(RESPONSE)) {
				try {
					JSONObject responseObj = fullResponse.getJSONObject(RESPONSE);
					log.debug("ResponseObj is : {} ", responseObj);
					// Check if the "response" key exists inside the first-level response object
					if (responseObj.has(RESPONSE)) {
						JSONObject nestedResponse = responseObj.getJSONObject(RESPONSE);
						log.debug("nestedResponse is : {} ", nestedResponse);
						stringResponse = nestedResponse.toString();
						log.debug("stringResponse coming in case of nested response is: {} ", stringResponse);
					} else {
						stringResponse = responseObj.toString();
						log.debug("stringResponse for single case response is: {} ", stringResponse);
					}
				} catch (Exception e) {
					log.error(UNABLE_TO_PARSE_RESPONSE_FROM_PROMPT_AFTER_READING_CV_RESUME, e);
				}
			}


			try {
				Map<String, Object> inputMap = new HashMap<>();
				inputMap.put("resumeContent", textContentWithoutSpaceAndBrackets);
				response = aiChatModel
						.chatCompletion("HRMS_APP_NAME-Applicant-Applicant_Professional_Summary-v-1", inputMap);
				log.debug("Response from ai in case of profilesummary chat model is : {} ", response);
				String formattedReponseSummary = extractJsonObjectString(response);
				log.debug("formattedReponseSummary after formating in case of profilesummary is : {}", formattedReponseSummary);
	//			String updatedformattedResponse = formattedReponseSummary.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
				log.debug("updatedformattedResponse after formating in case of profilesummary is : {}", formattedReponseSummary);
				fullResponse = new JSONObject(formattedReponseSummary);
				log.debug("JSONObject fullResponse for sdk In case of ProfileSummary is : {} ", fullResponse);

			} catch (Exception ex) {
				log.error("Error while Generating Response From Sdk for Resume Content : {} , trace: {}", ex.getMessage(), Utils.getStackTrace(ex));
			}

			String resumeResponseSummary = "";
			JSONObject fullResponseforSummary = new JSONObject(fullResponse);
			log.debug("fullResponseforSummary is : {}", fullResponse);
			resumeResponseSummary = extractResumeResponseSummary(fullResponse, resumeResponseSummary,
					fullResponseforSummary);

			extractResponseFromPrompt(stringResponse, applicantEducationList, applicantLanguageList,
					applicantCertificationsList, applicantExperienceList, skillsList, resultMap);

			Applicant applicant;
			if (hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME).equalsIgnoreCase("SAI")) {
				log.info("Create new applicant in Case Of Sai");
				applicant = null;
			} else {

				applicant = getApplicantByUserId(customerInfo.getUserId());
				log.debug("Applicant Found By UserID is : {}", applicant);
				if (applicant == null) {
					log.info("Create new applicant if not present ");
					applicant = new Applicant();
				}
			}

			createdApplicant = setApplicantFromPromptDetails(applicant, stringResponse, resumeResponseSummary,
					applicantEducationList, applicantExperienceList, documentId);

			ApplicantEducationService applicantEducationService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantEducationService.class);
			List<ApplicantEducation> applicantEducationLists = applicantEducationService
					.createApplicationEducationfromPropt(applicantEducationList, createdApplicant);
			ApplicantLanguageService applicantLanguageService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantLanguageService.class);
			List<ApplicantLanguage> applicantLanguageLists = applicantLanguageService
					.createApplicantLanguagefromPrompt(applicantLanguageList, createdApplicant);

			ApplicantCertificationsService applicantCertificationsService = ApplicationContextProvider
					.getApplicationContext().getBean(ApplicantCertificationsService.class);

			List<ApplicantCertifications> applicantCertificationsLists = applicantCertificationsService
					.createApplicantCertificationsfromPropt(applicantCertificationsList, createdApplicant);

			ApplicantExperienceService applicantExperienceService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantExperienceService.class);

			List<ApplicantExperience> applicantExperienceLists = applicantExperienceService
					.createApplicantExperiencefromPrompt(applicantExperienceList, createdApplicant);

			// log.debug("going to insert data into milvus collection for applicant ");
			// JSONObject rowJson = setApplicantDataFromObjects(createdApplicant);
			// log.debug(ROW_JSON_FORMED_IS2, rowJson);
			// try {
			// String insertionResponse =
			// vectorIntegrationService.createVectorAndInsert(rowJson, APPLICANT);
			// log.debug("insertionResponse from createVectorAndInsert is : {} ",
			// insertionResponse);
			// } catch (BusinessException be) {
			// log.error(CAUGHT_ERROR_FOR_CREATING_AND_INSERTING_VECTOR, be.getMessage());
			// } catch (Exception e) {
			// log.error(SOMETHING_WENT_WRONG, e);
			// }
			applicantPromptWrapper.setApplicant(createdApplicant);
			applicantPromptWrapper.setApplicantEducation(applicantEducationLists);
			applicantPromptWrapper.setApplicantLanguage(applicantLanguageLists);
			applicantPromptWrapper.setApplicantCertifications(applicantCertificationsLists);
			applicantPromptWrapper.setApplicantExperience(applicantExperienceLists);
			applicantPromptWrapper.setJobApplicationUpdateNeeded(Boolean.FALSE);

			log.debug("Server Value is : {} ", hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME));

//			if(jobId !=null && hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME).equalsIgnoreCase("SAI"))
//			{
//				JobApplicationService jobApplicationService = ApplicationContextProvider.getApplicationContext()
//						.getBean(JobApplicationService.class);
//				JobApplication jobApplicationsaved = jobApplicationService.createJobApplicationForApplicantWithoutPrioritization(applicant, jobId);
//				log.debug("job Application saved is : {} ", jobApplicationsaved);
//			}

			createJobApplicationForApplicantForSai(jobId, applicantPromptWrapper, applicant);

			return applicantPromptWrapper;
		} catch (BusinessException be) {
			log.error("error message thrown is : {} ", be.getMessage());
			if (be.getMessage().equalsIgnoreCase(APPLICANT_ALREADY_REGISTERED_BY_THIS_EMAIL_ID)) {
				throw new BusinessException(APPLICANT_ALREADY_REGISTERED_BY_THIS_EMAIL_ID);
			} else
				throw new BusinessException(be.getMessage());
		} catch (Exception e) {
			Applicant applicant = new Applicant();
			ApplicantEducation applicantEducation = new ApplicantEducation();
			ApplicantExperience applicantExperience = new ApplicantExperience();
			ApplicantLanguage applicantLanguage = new ApplicantLanguage();
			ApplicantCertifications applicantCertifications = new ApplicantCertifications();
			List<ApplicantEducation> applicantEducationList = new ArrayList<>();
			List<ApplicantExperience> applicantExperienceList = new ArrayList<>();
			List<ApplicantLanguage> applicantLanguageList = new ArrayList<>();
			List<ApplicantCertifications> applicantCertificationsList = new ArrayList<>();
			applicantEducationList.add(applicantEducation);
			applicantExperienceList.add(applicantExperience);
			applicantLanguageList.add(applicantLanguage);
			applicantCertificationsList.add(applicantCertifications);
			applicantPromptWrapper.setApplicant(applicant);
			applicantPromptWrapper.setApplicantEducation(applicantEducationList);
			applicantPromptWrapper.setApplicantLanguage(applicantLanguageList);
			applicantPromptWrapper.setApplicantCertifications(applicantCertificationsList);
			applicantPromptWrapper.setApplicantExperience(applicantExperienceList);
			return applicantPromptWrapper;
		}
	}

	private String extractResumeResponseSummary(JSONObject fullResponse, String resumeResponseSummary,
			JSONObject fullResponseforSummary) {
		if (fullResponse.has(RESPONSE)) {
			try {
				JSONObject responseSummaryObj = fullResponseforSummary.getJSONObject(RESPONSE);
				log.debug("responseSummaryObj is : {}", responseSummaryObj);
				// Check if the "response" key exists inside the first-level response object
				if (responseSummaryObj.has(RESPONSE)) {
					JSONObject nestedResponse = responseSummaryObj.getJSONObject(RESPONSE);
					log.debug("nestedResponse is : {}", nestedResponse);
					// Check if the "profileSummary" exists in the second-level "response"
					if (nestedResponse.has(PROFILE_SUMMARY)) {
						resumeResponseSummary = nestedResponse.getString(PROFILE_SUMMARY);
						log.debug("resumeResponseSummary is : {}", resumeResponseSummary);
					}
				}

				else if (responseSummaryObj.has(PROFILE_SUMMARY)) {
					resumeResponseSummary = responseSummaryObj.getString(PROFILE_SUMMARY);
					log.debug(RESUME_RESPONSE_SUMMARY_KEY_FROM_FULL_RESPONSEFOR_SUMMARY_IS, resumeResponseSummary);
				} else if (responseSummaryObj != null && responseSummaryObj.has(RESPONSE)) {
					resumeResponseSummary = responseSummaryObj.getString(RESPONSE);
					log.debug(RESUME_RESPONSE_SUMMARY_KEY_AS_RESPONSE_KEY, resumeResponseSummary);
				}

			} catch (Exception e) {
				log.error(UNABLE_TO_PARSE_RESPONSE_FROM_PROMPT_AFTER_READING_CV_RESUME, e);
			}
		}else if(fullResponseforSummary.has(PROFILE_SUMMARY)) {
			log.debug("fullResponseforSummary If contain key profilesummary is : {}", fullResponseforSummary);
			resumeResponseSummary = fullResponseforSummary.getString(PROFILE_SUMMARY);
			log.debug(RESUME_RESPONSE_SUMMARY_KEY_FROM_FULL_RESPONSEFOR_SUMMARY_IS, resumeResponseSummary);
		}
		return resumeResponseSummary;
	}

	private void createJobApplicationForApplicantForSai(String jobId, ApplicantPromptWrapper applicantPromptWrapper,
			Applicant applicant) {
		if (jobId != null
				&& hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME).equalsIgnoreCase("SAI")) {
			try {
				JobApplicationService jobApplicationService = ApplicationContextProvider.getApplicationContext()
						.getBean(JobApplicationService.class);

				log.info("Create/Update JobApplication With Overall Score In case Of Sai");
				if (applicant != null) {
					JobApplication jobApplication = jobApplicationService.findByApplicantId(applicant.getId());
					if (jobApplication != null) {
						log.debug("Update JobApplication : {}", jobApplication);
						jobApplication = jobApplicationService.updateJobApplicationForApplicant(applicant,
								jobApplication);
						applicantPromptWrapper.setJobApplication(jobApplication);
					} else {
						log.debug("Create JobApplication : {}", jobApplication);
						jobApplication = jobApplicationService.createJobApplicationForApplicant(applicant, jobId);
						applicantPromptWrapper.setJobApplication(jobApplication);
					}
					applicantPromptWrapper.setJobApplicationUpdateNeeded(Boolean.TRUE);
				}
			} catch (Exception e) {
				log.error(
						"Inside @Class ApplicantServiceImpl error while Creating/updating JobApplication for Applicant : {}",
						e.getMessage());
			}
		}
	}

	private void readWordAndPdfResume(List<Document> documentList, StringBuilder textContent, byte[] input) {
		if (documentList != null && documentList.get(0) != null && documentList.get(0).getExtension() != null
				&& documentList.get(0).getExtension().equalsIgnoreCase("pdf")) {
			log.debug("size of file Downloaded in case of pdf : {}", input.length);
			try (PDDocument document = Loader.loadPDF(input)) {
				if (!document.isEncrypted()) {
					PDFTextStripper pdfStripper = new PDFTextStripper();
					String text = pdfStripper.getText(document);
					textContent.append(text);
				}
			} catch (IOException ex) {
				throw new BusinessException(INVALID_FILE_FORMAT_PLEASE_PROVIDE_CORRECT_FILE_FORMAT_OF_RESUME);
			} catch (Exception e) {
				log.error("Error while downloading pdf document ", e);
			}
		} else if (documentList != null && documentList.get(0) != null && documentList.get(0).getExtension() != null
				&& documentList.get(0).getExtension().equalsIgnoreCase("docx")) {
			log.debug("size of file Downloaded in case of docx : {}", input.length);

			try {
				String text = readDocx(input);
				textContent.append(text);
			} catch (IOException ex) {
				throw new BusinessException(INVALID_FILE_FORMAT_PLEASE_PROVIDE_CORRECT_FILE_FORMAT_OF_RESUME);
			} catch (Exception e) {
				log.error("Error while reading docx document", e);
			}
		} else if (documentList != null && documentList.get(0) != null && documentList.get(0).getExtension() != null
				&& documentList.get(0).getExtension().equalsIgnoreCase("doc")) {
			log.debug("size of file Downloaded in case of doc is : {}", input.length);

			try {
				String text = readDoc(input);
				textContent.append(text);
			} catch (IOException ex) {
				throw new BusinessException(INVALID_FILE_FORMAT_PLEASE_PROVIDE_CORRECT_FILE_FORMAT_OF_RESUME);
			} catch (Exception e) {
				log.error("Error while reading doc type document", e);
			}
		}
	}

	private void setApplicantDetailsFromExperienceAndEducationList(Applicant applicant,
			List<Map<String, String>> experienceList, List<Map<String, String>> educationList) {
		log.info("inside method setApplicantDetailsFromExperienceAndEducationList");

		boolean foundAramco = false;
		String currentJobTitle = null;
		String currentEmployer = null;
		String highestQualification = null;
		LocalDate latestCompletionDate = null;

		if (experienceList != null && !experienceList.isEmpty()) {
			for (Map<String, String> experience : experienceList) {
				log.debug("Iterate for experienceList : {} ", experienceList);
				String company = experience.get(COMPANY);
				String endDate = experience.get(END_DATE);
				String occupation = experience.get(OCCUPATION);

				if (company != null && company.toLowerCase().contains("aramco")) {
					foundAramco = true;
				}
				log.debug("endDate found is  : {} ", endDate);
				if (endDate == null || endDate.equalsIgnoreCase("null")) {
					log.debug("currentJobTitle is : {}, currentEmployer : {} ", occupation, company);
					currentJobTitle = occupation;
					currentEmployer = company;
				}

				log.debug("Set previously worked with Aramco : {} ", foundAramco);
				applicant.setPreviouslyWorkedWithAramco(foundAramco);

				if (currentJobTitle != null) {
					log.debug("currentJobTitle is : {} ", currentJobTitle);
					applicant.setCurrentJobTitle(currentJobTitle);
				}
				if (currentEmployer != null) {
					log.debug("currentEmployer is : {} ", currentEmployer);
					applicant.setCurrentEmployer(currentEmployer);
					log.debug("going to break iteration loop , applicant now  is : {} ", applicant);
					// break ;
				}

			}

		}

		highestQualification = iterateEducationListToSetHighestQualification(educationList, highestQualification,
				latestCompletionDate);
		if (highestQualification != null) {
			log.debug("highestQualification is : {} ", highestQualification);
			applicant.setHighestQualification(highestQualification);

		}
	}

	private String iterateEducationListToSetHighestQualification(List<Map<String, String>> educationList,
			String highestQualification, LocalDate latestCompletionDate) {
		if (educationList != null && !educationList.isEmpty()) {
			for (Map<String, String> education : educationList) {
				log.debug("Iterate for educationList : {} ", education);
				String fieldOfStudy = education.get(FIELD_OF_STUDY);
				String dateOfCompletion = education.get("dateOfCompletion");

				if (dateOfCompletion != null) {

					LocalDate completionDate = LocalDate.parse(dateOfCompletion,
							DateTimeFormatter.ofPattern("MMMM yyyy"));

					if (latestCompletionDate == null || completionDate.isAfter(latestCompletionDate)) {
						latestCompletionDate = completionDate;
						highestQualification = fieldOfStudy;
					}
				} else {

					if (fieldOfStudy != null && !fieldOfStudy.trim().isEmpty()) {

						if (highestQualification == null) {
							highestQualification = fieldOfStudy;
						}
					}
				}

			}
		}
		return highestQualification;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@GenericAnnotation(actionType = "CREATE", uniqEntityId = "id", annotationName = {
			"GlobleSearch" }, appName = "HRMS_APP_NAME", entityName = "Applicant", globalSearchData = "firstName,lastName,emailId,arabicFirstName,arabicMiddleName,arabicLastName", searchTitle = "fullName")
	public Applicant setApplicantFromPromptDetails(Applicant applicant, String stringResponse,
			String resumeResponseSummary, List<Map<String, String>> applicantEducationList,
			List<Map<String, String>> applicantExperienceList, Integer documentId) {
		log.info("Inside ApplicantServiceImpl @method setApplicantFromPromptDetails and stringResponse is  : {}",
				stringResponse);
		User contextUser = getUserContext();
		Boolean applicantPresent = Boolean.FALSE;
		Map<String, Object> responseMap = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		Applicant applicantOld = null;
		try {
			responseMap = objectMapper.readValue(stringResponse, new TypeReference<Map<String, Object>>() {
			});
			log.debug("Inside ApplicantServiceImpl responseMap is : {}", responseMap);

			if ((hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME).equalsIgnoreCase("SAI"))) {

				String emailId = (String) responseMap.get(EMAIL_ID);
				log.debug("EmailId from resume is : {} ", emailId);
				applicant = fetchApplicantByEmailId(emailId);
				log.debug("Applicant Found for sai is: {} ", applicant);
				if (applicant == null) {
					log.info("create new Applicant In case Of Sai applicant : {}", applicant);
					applicant = new Applicant();

				}

				applicant.setApplicantSource("Internal-Referral");

			} else {
				if (applicant.getEmailId() != null && applicant.getUserId() != null
						&& !applicant.getUserId().equals(contextUser.getUserid())) {
					log.debug("fetch applicant email id if user not same   : {} , {} ", applicant.getUserId(),
							contextUser.getUserid());
					applicantOld = fetchApplicantByEmailId(applicant.getEmailId());
					if (applicantOld != null) {
						log.debug("applicantOld object found using  : {} ", convertObjectToJson(applicantOld));
						applicantPresent = true;
					}
				}

			}
			log.debug("Inside ApplicantServiceImpl after emailId verification , responseMap is : {}", responseMap);
			setApplicantOnValidString(applicant, responseMap);

			if (!(hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME).equalsIgnoreCase("SAI"))) {
				if (applicant.getEmailId() != null && applicant.getUserId() == null) {
					applicantOld = fetchApplicantByEmailId(applicant.getEmailId());
					if (applicantOld != null) {
						log.debug("applicantOld object found using  for new Applicant Formation : {} ",
								convertObjectToJson(applicantOld));
						applicantPresent = true;
					}
				}
			}
			log.debug("Inside ApplicantServiceImpl documentId Setted in ApplicantResume Field , document Id : {}",
					documentId);
			applicant.setApplicantResume(documentId.toString());
			setCityAndCountryInApplicant(applicant, responseMap);

			setKeysInApplicantFromPromptDetails(applicant, responseMap);

			setSkillInApplicant(applicant, responseMap);

			log.debug("resumeResponseSummary is : {}", resumeResponseSummary);

			if (resumeResponseSummary != null) {
				applicant.setProfileSummary(resumeResponseSummary);
			}

			log.debug("going to set , contextUser user Id is : {}", contextUser.getUserid());
			applicant.setUserId(contextUser.getUserid());

			setSkillSetForApplicant(applicant, responseMap);
			setApplicantBasedOnKeys(applicant, responseMap);
			setApplicantKeyFromResponseMap(applicant, responseMap);
			setApplicantDetailsFromExperienceAndEducationList(applicant, applicantExperienceList,
					applicantEducationList);

		} catch (Exception e) {
			log.error("Error while parsing response JSON", e);
		}
		log.debug("applicant object to be saved after resume iteration : {} ", convertObjectToJson(applicant));
		if (!applicantPresent) {
			log.info("In case Applicant Not Present");
			return applicantRepository.save(applicant);
		} else {
			throw new BusinessException(APPLICANT_ALREADY_REGISTERED_BY_THIS_EMAIL_ID);
		}

	}

	private void setKeysInApplicantFromPromptDetails(Applicant applicant, Map<String, Object> responseMap) {
		if (responseMap.containsKey(MOBILE)) {
			if (isValidString((String) responseMap.get(MOBILE))) {
				applicant.setMobile((String) responseMap.get(MOBILE));
			}
		}
		if (responseMap.containsKey("presentAddress")) {
			applicant.setPresentAddress((String) responseMap.get("presentAddress"));
		}
		if (responseMap.containsKey("street")) {
			applicant.setStreet((String) responseMap.get("street"));
		}
		if (responseMap.containsKey(REFERRED_BY)) {
			if (isValidString((String) responseMap.get(REFERRED_BY))) {
				applicant.setReferedBy((String) responseMap.get(REFERRED_BY));
			}
		}

		if (responseMap.containsKey("dateOfBirth")) {

			Date date = (Date) commonUtils.parseDateInDayMonthYearFormat((String) responseMap.get("dateOfBirth"));

			log.debug("DateOfBirth is : {}", date);

			if (date != null) {
				applicant.setDateOfBirth(date);
			}

		}
	}

	@Transactional(readOnly = true)
	public void setSkillInApplicant(Applicant applicant, Map<String, Object> responseMap) {

		log.debug("Inside method setSkillInApplicant");

		try {
			// Check if the responseMap contains the key for skills
			if (responseMap.containsKey(SKILL_SET2)) {
				Object skillSetValue = responseMap.get(SKILL_SET2);
				List<String> skillList = new ArrayList<>();

				// Case 1: If the value is a single string (comma-separated skills)
				if (skillSetValue instanceof String) {
					String skillSetString = (String) skillSetValue;
					log.debug("Skill set is a single string: {}", skillSetString);
					// Split the string by commas, trimming spaces around each skill
					String[] splitSkills = skillSetString.split("\\s*,\\s*");
					for (String skill : splitSkills) {
						skillList.add(skill);
					}
				}
				// Case 2: If the value is already a list of skills
				else if (skillSetValue instanceof List<?>) {
					@SuppressWarnings("unchecked")
					List<String> listFromResponse = (List<String>) skillSetValue;
					skillList.addAll(listFromResponse);
				}

				log.debug("Processed skill list: {}", skillList);

				List<Skill> skillsToAdd = new ArrayList<Skill>();

				// Loop through the skill list and add corresponding Skill objects
				if (skillList != null && !skillList.isEmpty()) {
					for (String skillName : skillList) {
						log.debug("Processing skill: {}", skillName);

						// List<Skill> skillObjList = skillRepository.findByName(skillName);

						log.debug("Inside method setSkillInApplicant customerId is : {}", commonUtils.getCustomerId());
						Skill skillFound = skillRepository.findByName(skillName, commonUtils.getCustomerId());

						if (skillFound != null) {
							log.debug("Add skill list: {}", skillFound);
							skillsToAdd.add(skillFound);
						}
					}
				}

				// If skills were found, set them in the applicant
				if (!skillsToAdd.isEmpty()) {
					log.debug("Setting skills in applicant: {}", skillsToAdd);
					applicant.setSkills(skillsToAdd);
				}
			}

		} catch (BusinessException ex) {
			log.error("Error inside method setSkillInApplicant", ex.getMessage());
		}
	}

	private void setCityAndCountryInApplicant(Applicant applicant, Map<String, Object> responseMap) {
		if (responseMap.containsKey("city")) {
			log.info("Going to set city");
			City city = null;
			if (responseMap.get("city") != null) {
				log.debug("Inside setCityAndCountryInApplicant customerId: {}", commonUtils.getCustomerId());
				city = cityRepository.findCityByName((String) responseMap.get("city"), commonUtils.getCustomerId());
			}
			if (city != null) {
				log.debug("Set city : {}", city.getId());
				applicant.setCity(city);
			}
		}

		if (responseMap.containsKey(COUNTRY)) {
			log.info("Going to set country");
			Country country = null;
			if (responseMap.get(COUNTRY) != null) {
				log.debug("Inside setCityAndCountryInApplicant customerId: {}", commonUtils.getCustomerId());
				country = countryRepository.findCountryByName((String) responseMap.get(COUNTRY),
						commonUtils.getCustomerId());
			}
			if (country != null) {
				log.debug("Set country : {}", country.getId());
				applicant.setCountry(country);
			}
		}
	}

	private void setSkillSetForApplicant(Applicant applicant, Map<String, Object> responseMap) {
		if (responseMap.containsKey(SKILL_SET)) {
			Object skillSetObj = responseMap.get(SKILL_SET);

			if (skillSetObj instanceof List) {
				@SuppressWarnings("unchecked")
				List<String> skillList = (List<String>) skillSetObj;
				String skillSetString = String.join(",", skillList);
				applicant.setSkillSet(skillSetString);
			} else if (skillSetObj instanceof String) {
				applicant.setSkillSet((String) skillSetObj);
			}
		}
	}

	@Transactional(readOnly = true)
	public Applicant fetchApplicantByEmailId(String emailId) {
		log.debug("Inside method fetchApplicantByEmailId , emailId is : {}", emailId);
		log.debug("Inside method fetchApplicantByEmailId customerId is : {}", commonUtils.getCustomerId());
		Applicant applicant = applicantRepository.findByEmailId(emailId, commonUtils.getCustomerId());
		log.debug("Inside method fetchApplicantByEmailId , applicant is : {}", applicant);
		return (applicant != null ? applicant : null);
	}

	private void setApplicantOnValidString(Applicant applicant, Map<String, Object> responseMap) {

		if (responseMap.containsKey(CURRENT_SALARY)) {
			applicant.setCurrentSalary(responseMap.get(CURRENT_SALARY) != null
					? Integer.parseInt(responseMap.get(CURRENT_SALARY).toString())
					: null);
		}

		User contextUser = getUserContext();

		if (hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME).equalsIgnoreCase("SAI")) {
			log.debug("Set EmailId By Resume");

			String email = (String) responseMap.get(EMAIL_ID);
			if (email != null) {
				String formattedEmail = email.replaceAll("\\s+", "");
				formattedEmail = formattedEmail.toLowerCase();
				if (isValidEmailFormat(formattedEmail)) {
					log.debug("Email Id set by Resume: {}", formattedEmail);
					applicant.setEmailId(formattedEmail);
				} else {
					throw new BusinessException("Applicant email is invalid In resume");
				}
			}
		} else {

			if (contextUser != null && contextUser.getEmail() != null) {
				log.debug("Set EmailId By UserLogin", contextUser.getEmail());

				String email = contextUser.getEmail();
				String formattedEmail = email.replaceAll("\\s+", "");
				formattedEmail = formattedEmail.toLowerCase();
				if (isValidEmailFormat(formattedEmail)) {
					log.debug("Email Id set by context user is : {}", formattedEmail);
					applicant.setEmailId(formattedEmail);
				} else {
					throw new BusinessException("Applicant email is invalid");
				}
			}

		}

		if (responseMap.containsKey(EXPECTED_SALARY)) {
			applicant.setExpectedSalary(responseMap.get(EXPECTED_SALARY) != null
					? Integer.parseInt(responseMap.get(EXPECTED_SALARY).toString())
					: null);
		}
		if (responseMap.containsKey(EXPERIENCE_IN_YEARS)) {
			applicant.setExperienceInYears(responseMap.get(EXPERIENCE_IN_YEARS) != null
					? Double.parseDouble(responseMap.get(EXPERIENCE_IN_YEARS).toString())
					: null);
		}
		setNameOfApplicant(applicant, responseMap);
	}

	private void setNameOfApplicant(Applicant applicant, Map<String, Object> responseMap) {

		String firstName = null;
		String lastName = null;
		if (responseMap.containsKey(FIRST_NAME)) {
			if (isValidString((String) responseMap.get(FIRST_NAME))) {
				firstName = (String) responseMap.get(FIRST_NAME);

				applicant.setFirstName(firstName);
			}
		}
		if (responseMap.containsKey(LAST_NAME)) {
			if (isValidString((String) responseMap.get(LAST_NAME))) {
				lastName = (String) responseMap.get(LAST_NAME);
				applicant.setLastName(lastName);
			}
		}

		if (firstName != null && lastName != null) {
			applicant.setFullName(firstName + " " + lastName);
		}
	}

	private void setApplicantBasedOnKeys(Applicant applicant, Map<String, Object> responseMap) {

		setApplicantPreferredDeatils(applicant, responseMap);

		setKeysInApplicantFromResponseMap(applicant, responseMap);

		Map<String, String> attributeMap = userRest.getAttributeMap(customerInfo.getUsername());
		log.debug("attributeMap fetched is : {}", attributeMap);
		String referedByKey = attributeMap.get(REFERED_BY);
		String referedByEmailId = attributeMap.get(REFERED_BY_EMAIL_ID);
		log.debug("referedByKey fetched from attributeMap is : {}", referedByKey);
		log.debug("referedByEmailId fetched from attributeMap is : {}", referedByEmailId);
		if (referedByKey != null && referedByKey.equalsIgnoreCase("referral")) {
			log.debug("applicant type in case of if , when it is referred ");
			if (referedByEmailId != null) {
				applicant.setReferedByEmailId(referedByEmailId);
			}
			applicant.setApplicantSource("Internal-Referral");
			applicant.setApplicantType("Referred");
		} else if (referedByKey != null && referedByKey.equalsIgnoreCase(HEAD_HUNTED)) {
			log.debug("applicant type in case of if , when it is Head-Hunted ");
			applicant.setApplicantSource(HEAD_HUNTED);
			applicant.setApplicantType(HEAD_HUNTED);
		} else {
			log.debug("applicant type in case of else ");
			applicant.setApplicantType("Regular");
		}

	}

	private void setKeysInApplicantFromResponseMap(Applicant applicant, Map<String, Object> responseMap) {
		if (responseMap.containsKey(CITIZENSHIP_STATUS)) {
			String citizenshipStatus = (String) responseMap.get(CITIZENSHIP_STATUS);
			if (citizenshipStatus != null && CITIZENSHIP_STATUSES.contains(citizenshipStatus)) {
				applicant.setCitizenshipStatus(citizenshipStatus);
			}
		}

		if (responseMap.containsKey("publication")) {
			applicant.setPublication((String) responseMap.get("publication"));
		}

		if (responseMap.containsKey(REFERRED_BY_EMAIL_ID)) {
			if (isValidString((String) responseMap.get(REFERRED_BY_EMAIL_ID))) {
				applicant.setReferedByEmailId(REFERRED_BY_EMAIL_ID);
			}
		}

		if (responseMap.containsKey("noticePeriod")) {
			applicant.setNoticePeriod((Integer) responseMap.get("noticePeriod"));
		}

		if (responseMap.containsKey(REFERRED_BY) && isValidString((String) responseMap.get(REFERRED_BY))) {
			applicant.setInternalReference(true);
		} else if (responseMap.containsKey("internalReference")) {
			Object internalReferenceValue = responseMap.get("internalReference");
			if (internalReferenceValue != null) {
				applicant.setInternalReference((boolean) internalReferenceValue);
			}
		}

		if (responseMap.containsKey("eligibleToWorkInSaudi")) {
			Object eligibleToWorkInSaudiValue = responseMap.get("eligibleToWorkInSaudi");
			if (eligibleToWorkInSaudiValue != null) {
				applicant.setEligibleToWorkInSaudi((boolean) eligibleToWorkInSaudiValue);
			}
		}

		if (responseMap.containsKey("firstJob")) {
			Object firstJobValue = responseMap.get("firstJob");
			if (firstJobValue != null) {
				applicant.setFirstJob((boolean) firstJobValue);
			}
		}
	}

	private void setApplicantPreferredDeatils(Applicant applicant, Map<String, Object> responseMap) {
		if (responseMap.containsKey("preferredName")) {
			applicant.setPreferredName((String) responseMap.get("preferredName"));
		}

		if (responseMap.containsKey("primaryShortAddress")) {
			applicant.setPrimaryShortAddress((String) responseMap.get("primaryShortAddress"));
		}

		if (responseMap.containsKey(MARITAL_STATUS)) {
			String maritalStatus = (String) responseMap.get(MARITAL_STATUS);
			if (maritalStatus != null && VALID_MARITAL_STATUSES.contains(maritalStatus)) {
				applicant.setMaritalStatus(maritalStatus);
			}
		}
		if (responseMap.containsKey("referenceRelation")) {
			applicant.setReferenceRelation((String) responseMap.get("referenceRelation"));
		}

		if (responseMap.containsKey(PREFERRED_LOCATION)) {
			String preferredLocation = (String) responseMap.get(PREFERRED_LOCATION);
			if (isValidString((String) responseMap.get(PREFERRED_LOCATION))
					&& preferredLocation.equalsIgnoreCase("Dammam Office")) {
				applicant.setPreferredLocation((String) responseMap.get(PREFERRED_LOCATION));
			}
		}
	}

	private void setApplicantKeyFromResponseMap(Applicant applicant, Map<String, Object> responseMap) {
		if (responseMap.containsKey("linkedinProfile")) {
			applicant.setLinkedinProfile((String) responseMap.get("linkedinProfile"));
		}
		if (responseMap.containsKey(CITIZENSHIP)) {
			if (isValidString((String) responseMap.get(CITIZENSHIP))) {
				applicant.setCitizenship((String) responseMap.get(CITIZENSHIP));
			}
		}

		if (responseMap.containsKey(GENDER)) {
			if (isValidString((String) responseMap.get(GENDER))) {
				applicant.setGender((String) responseMap.get(GENDER));
			}
		}

		if (responseMap.containsKey(POSTAL_CODE)) {
			if (isValidString((String) responseMap.get(POSTAL_CODE))) {
				applicant.setPostalCode((String) responseMap.get(POSTAL_CODE));
			}
		}

		if (responseMap.containsKey(STATE)) {
			if (isValidString((String) responseMap.get(STATE))) {
				applicant.setState((String) responseMap.get(STATE));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void extractResponseFromPrompt(String stringResponse, List<Map<String, String>> applicantEducationList,
			List<Map<String, String>> applicantLanguageList, List<Map<String, String>> applicantCertificationsList,
			List<Map<String, String>> applicantExperienceList, List<String> skillsList,
			Map<String, Object> resultMap1) {
		log.info(INSIDE_METHOD, "extractResponseFromPrompt");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode;
		try {
			rootNode = objectMapper.readTree(stringResponse);

			log.debug("rootNode formed is  : {} ", rootNode.toPrettyString());

			JsonNode applicantEducation = rootNode.get(EDUCATION_DETAILS);

			extractApplicantEducation(applicantEducationList, applicantEducation);

			JsonNode applicantLanguage = rootNode.get(LANGUAGE_SKILLS);

			if (applicantLanguage != null) {
				for (JsonNode applicantLang : applicantLanguage) {
					log.debug("rootNode applicantLang formed is  : {} ", applicantLang.toPrettyString());
					Map<String, String> langMap = objectMapper.convertValue(applicantLang,
							new TypeReference<Map<String, String>>() {
							});
					try {
						log.debug("langMap is  : {} ", langMap);
						applicantLanguageList.add(langMap);
						log.debug("Add Successfully in applicantLanguageList : {} ", applicantLanguageList);
						log.debug("Add Successfully in applicantLanguageList : {} ", applicantLang.toPrettyString());
					} catch (Exception e) {

						log.error("rootNode exception formed applicantLang : {} ", Utils.getStackTrace(e));

					}
				}
			}

			setApplicantCertificationsAndExperienceFromPrompt(applicantCertificationsList, applicantExperienceList,
					rootNode);

			try {
				log.info("Add to skillSet");

				JsonNode skillSet = rootNode.get(SKILL_SET);

				skillsList.addAll(Arrays.asList(skillSet.asText().split(",\\s*")));
				log.debug("Add Successfully in skillsList : {} ", skillsList);
			} catch (Exception e) {
				log.error("error occur while adding to skillset");
			}

			try {
				Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
				while (fields.hasNext()) {
					log.info("iterate for fields");

					Map.Entry<String, JsonNode> entry = fields.next();
					String key = entry.getKey();
					JsonNode value = entry.getValue();

					if (EDUCATION_DETAILS.equals(key) || LANGUAGE_SKILLS.equals(key) || SKILL_SET.equals(key)) {
						continue;
					}

					resultMap1.put(key, value.isNull() ? null : value.asText());
				}

			} catch (Exception e) {
				log.error("error while adding in resultmap1 : {} ", Utils.getStackTrace(e));
			}

			if (!applicantEducationList.isEmpty()) {
				resultMap1.put(EDUCATION_DETAILS, applicantEducationList);
			}
			if (!applicantLanguageList.isEmpty()) {
				resultMap1.put(LANGUAGE_SKILLS, applicantLanguageList);
			}
			if (!skillsList.isEmpty()) {
				resultMap1.put(SKILL_SET2, skillsList);
			}

			log.debug("inside method extractResponseFromPrompt size of resultMap1 : {} ", resultMap1.size());
		} catch (JsonProcessingException e) {
			log.error("inside method extractResponseFromPrompt : {} ", e.getMessage());
		}
	}

	private void extractApplicantEducation(List<Map<String, String>> applicantEducationList,
			JsonNode applicantEducation) {
		if (applicantEducation != null && applicantEducation.isArray()) {
			for (JsonNode applicantEdu : applicantEducation) {

				log.debug("rootNode applicantEdu formed applicantEdu : {} ", applicantEdu);

				log.debug("rootNode applicantEdu formed is  : {} ", applicantEdu.toPrettyString());

				Map<String, String> eduMap = new HashMap<>();

				try {
					log.debug("eduMap is  : {} ", eduMap);

					eduMap.put(COLLEGE_NAME,
							applicantEdu.get(COLLEGE_NAME) != null ? applicantEdu.get(COLLEGE_NAME).asText() : null);
					eduMap.put(DATEOF_COMPLETION,
							applicantEdu.get(DATEOF_COMPLETION) != null ? applicantEdu.get(DATEOF_COMPLETION).asText()
									: null);
					eduMap.put(DEGREE_START_DATE,
							applicantEdu.get(DEGREE_START_DATE) != null ? applicantEdu.get(DEGREE_START_DATE).asText()
									: null);
					eduMap.put(FIELD_OF_STUDY,
							applicantEdu.get(FIELD_OF_STUDY) != null ? applicantEdu.get(FIELD_OF_STUDY).asText()
									: null);

					log.debug("rootNode applicantEdu formed is  eduMap : {} ", eduMap);

					applicantEducationList.add(eduMap);
					log.debug("Added Successfully in applicantEdu : {} ", eduMap);
				} catch (Exception ex) {
					log.error("rootNode exception formed applicantEdu : {} ", Utils.getStackTrace(ex));
				}
			}
		}
	}

	private void setApplicantCertificationsAndExperienceFromPrompt(
			List<Map<String, String>> applicantCertificationsList, List<Map<String, String>> applicantExperienceList,
			JsonNode rootNode) {
		JsonNode applicantCertifications = rootNode.get("certification");

		extractApplicantCertificationsFromPrompt(applicantCertificationsList, applicantCertifications);

		JsonNode applicantExperience = rootNode.get("workExperience");

		if (applicantExperience != null && applicantExperience.isArray()) {
			for (JsonNode experience : applicantExperience) {
				log.debug("rootNode experience formed experience : {} ", experience);
				log.debug("rootNode experience formed is : {} ", experience.toPrettyString());

				Map<String, String> expMap = new HashMap<>();

				try {
					log.debug("expMap is : {} ", expMap);

					setInExperienceMapFromPrompt(experience, expMap);

					log.debug("rootNode experience formed is expMap : {} ", expMap);

					applicantExperienceList.add(expMap);
					log.debug("Added Successfully in experience : {} ", expMap);
				} catch (Exception ex) {
					log.error("rootNode exception formed experience : {} ", Utils.getStackTrace(ex));
				}
			}
		}
	}

	private void setInExperienceMapFromPrompt(JsonNode experience, Map<String, String> expMap) {
		expMap.put(COMPANY, experience.get(COMPANY) != null ? experience.get(COMPANY).asText() : null);
		expMap.put(CURRENTLY_WORK_HERE,
				experience.get(CURRENTLY_WORK_HERE) != null ? experience.get(CURRENTLY_WORK_HERE).asText() : null);
		expMap.put(DURATION, experience.get(DURATION) != null ? experience.get(DURATION).asText() : null);
		expMap.put(OCCUPATION, experience.get(OCCUPATION) != null ? experience.get(OCCUPATION).asText() : null);
		expMap.put(START_DATE, experience.get(START_DATE) != null ? experience.get(START_DATE).asText() : null);
		expMap.put(END_DATE, experience.get(END_DATE) != null ? experience.get(END_DATE).asText() : null);
		expMap.put(SUMMARY, experience.get(SUMMARY) != null ? experience.get(SUMMARY).asText() : null);
	}

	private void extractApplicantCertificationsFromPrompt(List<Map<String, String>> applicantCertificationsList,
			JsonNode applicantCertifications) {
		if (applicantCertifications != null && applicantCertifications.isArray()) {
			for (JsonNode certification : applicantCertifications) {
				log.debug("rootNode certification formed certification : {} ", certification);
				log.debug("rootNode certification formed is : {} ", certification.toPrettyString());

				Map<String, String> certMap = new HashMap<>();

				try {
					log.debug("certMap is : {} ", certMap);

					certMap.put(CERTIFICATION_NAME,
							certification.get(CERTIFICATION_NAME) != null
									? certification.get(CERTIFICATION_NAME).asText()
									: null);
					certMap.put(ISSUING_INSTITUTION,
							certification.get(ISSUING_INSTITUTION) != null
									? certification.get(ISSUING_INSTITUTION).asText()
									: null);
					certMap.put(CERTIFICATION_ID,
							certification.get(CERTIFICATION_ID) != null ? certification.get(CERTIFICATION_ID).asText()
									: null);

					log.debug("rootNode certification formed is certMap : {} ", certMap);

					applicantCertificationsList.add(certMap);
					log.debug("Added Successfully in certification : {} ", certMap);
				} catch (Exception ex) {
					log.error("rootNode exception formed certification : {} ", Utils.getStackTrace(ex));
				}
			}
		}
	}

	private String setDataForResumeDetails(StringBuilder textContent) {
		log.info(INSIDE_METHOD, "setDataForResumeDetails");

		String prompt = """
				\n<system>\nYOU ARE AN ADVANCED AI ASSISTANT HIGHLY SPECIALIZED IN PARSING RESUMES ACROSS A BROAD RANGE OF FORMATS,
				 WRITING STYLES, AND STRUCTURES. YOUR TASK IS TO EXTRACT KEY INFORMATION FROM RAW TEXT RESUMES AND RETURN IT IN A
				 STRICTLY FORMATTED JSON OUTPUT. YOU MUST HANDLE LINGUISTIC VARIATIONS, AMBIGUITIES, AND FORMATTING DIVERSITY WITH
				 HIGH ACCURACY AND ROBUSTNESS. IF INFORMATION IS UNCLEAR OR MISSING, THEN SET THE CORRESPONDING FIELD AS NULL TO YOUR
				 RESPONSE.\n\n### RESPONSE REQUIREMENTS:\n\n1. **DATA EXTRACTION**:\n   - Extract the following fields and map them
				  to the provided JSON schema. Handle abbreviations, typos, and formatting variations to ensure accurate extraction
				  . If a value cannot be confidently extracted, do not set its key to response.\n\n2. **DATE HANDLING**:\n
				  - Normalize all dates to `DD-MM-YYYY` format.\n   - If a day or month is missing, assume \"01\" for the missing
				   component (e.g., \"August 2023\" → `01-08-2023`).\n   - If only the year is available, set it as `01-01-YYYY`.\n
				      - For ambiguous or incomplete dates,  do not set its field to response.\n\n3. **STRUCTURED OUTPUT**:\n
				      - Generate the JSON strictly adhering to the schema below.\n   - Populate nested fields (e.g., `educationDetails`
				      , `LanguageSkills`, `workExperience`) accurately and handle multiple entries where applicable.\n\n4. **BOOLEAN AND
				       ENUM FIELDS**:\n   - Boolean fields (e.g., `eligibleToWorkInSaudi`, `firstJob`) should be `true`, `false`, or `null`
				       based on available data.\n   - Restrict enum values (e.g., `languageLevel`, `maritalStatus`) to predefined options.
				       If uncertain, use \"Other\" or `null`.\n\n5. **ERROR HANDLING**:\n   - If critical sections are missing or corrupted,
				        set all related fields to `null` and provide a note indicating incomplete input.\n   - Avoid assumptions or guesses.
				        Use context where possible but return `null` if information is ambiguous.\n\n6. **ADAPT TO VARIATIONS**:\n
				        - Recognize diverse linguistic expressions and variations in resume structure.\n   - Use context to resolve abbreviations,
				         typos, or non-standard terms without sacrificing accuracy.\n   - Handle missing or unclear fields by setting them to `null`
				          and ensure consistency across all extracted data.\n\n7. **STRICT VALIDATION**:\n   - Dates must strictly follow the
				          `DD-MM-YYYY` format. Validate all extracted dates for accuracy.\n   - Ensure all fields comply with the schema and are
				          suitable for real-world applications.\n\n8. **IMPORTANT NOTE**:\n   - Summary should not be null in any case , it must always generate
				          summary.\n - Date should not be null , it must always generate date .\n - It must strictly adhere to the Response Requirements
				          ,specifically date format.\n - If it is not possible to parse any field , then it must always set that filed value to null strictly.\n\n### RESPONSE JSON STRUCTURE:\n{{\n  \"city\": \"<extracted city>\",\n  \"country\": \"<extracted
				          country>\",\n  \"emailId\": \"<extracted email address>\",\n  \"firstName\": \"<extracted first name>\",\n  \"lastName\":
				          \"<extracted last name>\",\n  \"mobile\": \"<extracted phone number>\",\n  \"presentAddress\": \"<extracted full address>\",\n
				          \"skillSet\": \"<extracted skills>\",\n  \"street\": \"<extracted street>\",\n  \"citizenship\": \"<extracted citizenship>\",\n
				          \"gender\": \"<extracted gender>\",\n  \"postalCode\": \"<extracted postal code>\",\n  \"state\": \"<extracted state>\",\n
				          \"preferredName\": \"<extracted preferred name>\",\n  \"dateOfBirth\": \"<extracted date of birth>\",\n  \"primaryShortAddress\":
				          \"<extracted primary address>\",\n  \"maritalStatus\": \"<extracted marital status>\",\n  \"internalReference\": \"<boolean>\",\n
				          \"eligibleToWorkInSaudi\": \"<boolean>\",\n  \"firstJob\": \"<boolean>\",\n  \"preferredLocation\": \"<extracted preferred location>\"
				          ,\n  \"citizenshipStatus\": \"<extracted citizenship status>\",\n  \"linkedinProfile\": \"<extracted LinkedIn URL>\",\n  \"noticePeriod\":
				          \"<extracted notice period>\",\n  \"publication\": \"<extracted publications>\",\n  \"profileSummary\": \"<extracted summary>\",\n  \"experienceInYears\":
				          \"<extracted experience>\",\n  \"educationDetails\": [\n    {{\n      \"degreeStartDate\": \"<extracted start date>\",\n      \"dateOfCompletion\":
				          \"<extracted end date>\",\n      \"fieldOfStudy\": \"<extracted field of study>\",\n      \"collegeName\": \"<extracted college name>\"\n
				          }}\n  ],\n  \"LanguageSkills\": [\n    {{\n      \"languageName\": \"<extracted language>\",\n      \"languageLevel\": \"<proficiency level>\"\n
				           }}\n  ],\n  \"workExperience\": [\n    {{\n      \"company\": \"<extracted company>\",\n      \"currentlyWorkHere\": \"<boolean>\",\n
				           \"duration\": \"<extracted duration>\",\n      \"occupation\": \"<extracted job title>\",\n      \"startDate\": \"<start date>\",\n
				            \"endDate\": \"<end date>\",\n      \"summary\": \"<work summary>\"\n    }}\n  ],\n  \"certification\": [\n    {{\n
				            \"certificationName\": \"<extracted certification>\",\n      \"issuingInstitution\": \"<extracted institution>\",\n
				            \"certificationId\": \"<extracted ID>\"\n    }}\n  ]\n}}\n\n</system>\n<user>\nExtract all relevant details like name, email, phone,
				             skills, citizenship, job title, etc., from the following resume,
				            and return the structured JSON format. Provide lists for fields like education, certifications, and
				            work experience.\n\n### Resume Example:\nDocument:\n
				            %s.
				            \n
				            Please adhere to the strict schema and instructions
				            for best accuracy.\n\n

				            Please adhere to the strict schema and instructions for best accuracy.\n</user>\n

				            ### **Output Example**```json{{\"response\":\"RESPONSE JSON STRUCTURE\"}}```### **the output should be in exactly in json format which is surrounded by flower brackets**#### **Respond only with JSON data without any additional lines or explanations**


				""";

		return String.format(prompt, textContent);
	}

	private String setDescriptionSummaryForResumeDetails(StringBuilder textContent) {
		log.info(INSIDE_METHOD, "setDescriptionSummaryForResumeDetails");
		String prompt = "Interactive Resume Summary and Action Options\n" + " \n" + "Resume content:\n" + "%s\n" + " \n"
				+ "Output Requirements:\n"
				+ "Summarize the resume in under 500 words, highlighting key qualifications and achievements. Ensure the candidates name is included.\n"
				+ "Provide the summary in bullet-point format.\n"
				+ "The output must be returned as a JSON response with a key \"profileSummary\" and the value in string format.\n"
				+ "Your task is to output the JSON Response in well-formatted HTML based ,  while following a strict format .\n"
				+ "Prompt:\n" + "Resume Summary:\n"
				+ "Analyze the provided resume text to identify crucial elements such as qualifications, experiences, skills, and certifications.\n"
				+ "Compose a concise, two-line summary that highlights the key points from the resume, making sure to mention the candidates name prominently.\n"
				+ "Follow the summary with bullet points detailing the candidates major qualifications, experiences, and skills.\n"
				+ "Each bullet point must focus on a key aspect (e.g., work experience, skills, education, or certifications) from the resume.\n"
				+ "Formatting Rules:\n"
				+ "Always generate the response as a single string within a \"profileSummary\" key in the final JSON output.\n"
				+ "The summary and bullet points must follow proper grammar, spelling, and punctuation conventions.\n"
				+ "Your task is to output the JSON Response in well-formatted HTML based ,  while following a strict format\n"
				+ "has context menu";
		return String.format(prompt, textContent);
	}

	@Override
	public ApplicantPromptWrapper updateApplicantAfterResumeDetailsSet(ApplicantPromptWrapper applicantPromptWrapper) {

		log.debug("Inside method updateApplicantAfterResumeDetailsSet applicantPromptWrapper is : {}",
				applicantPromptWrapper);

		try {

			Applicant applicant = applicantPromptWrapper.getApplicant();
			log.debug("Inside method updateApplicantAfterResumeDetailsSet customerId is : {}",
					commonUtils.getCustomerId());

			Applicant applicantByEmail = applicantRepository
					.findByEmailId(applicantPromptWrapper.getApplicant().getEmailId(), commonUtils.getCustomerId());
			log.debug("Inside method applicantByEmail is : {}", convertObjectToJson(applicantByEmail));

			if (applicantByEmail != null && !applicantByEmail.getUserId().equals(customerInfo.getUserId())
					&& applicantByEmail.getEmailId().equals(applicantPromptWrapper.getApplicant().getEmailId())) {
				log.info("User ids are  : {} {}", applicantByEmail.getUserId(), customerInfo.getUserId());
				throw new BusinessException(APPLICANT_ALREADY_REGISTERED_BY_THIS_EMAIL_ID);
			}

			log.debug("Inside method updateApplicantAfterResumeDetailsSet applicant is : {}", applicant.toString());

			Applicant updatedApplicant = applicantRepository.save(applicant);

			List<ApplicantEducation> applicantEducationList = applicantPromptWrapper.getApplicantEducation();
			List<ApplicantLanguage> applicantLanguageList = applicantPromptWrapper.getApplicantLanguage();
			List<ApplicantCertifications> applicantCertificationsList = applicantPromptWrapper
					.getApplicantCertifications();
			List<ApplicantExperience> applicantExperienceList = applicantPromptWrapper.getApplicantExperience();

			ApplicantEducationService applicantEducationService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantEducationService.class);
			List<ApplicantEducation> applicantEducationLists = applicantEducationService
					.updateApplicantEducationAfterResumeDetailsSet(applicantEducationList, updatedApplicant);

			log.debug("Updated Applicant Education List: {}", applicantEducationLists);

			ApplicantLanguageService applicantLanguageService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantLanguageService.class);
			List<ApplicantLanguage> applicantLanguageLists = applicantLanguageService
					.updateApplicantLanguageAfterResumeDetailsSet(applicantLanguageList, updatedApplicant);

			log.debug("Updated Applicant Language List: {}", applicantLanguageLists);

			ApplicantCertificationsService applicantCertificationsService = ApplicationContextProvider
					.getApplicationContext().getBean(ApplicantCertificationsService.class);
			List<ApplicantCertifications> applicantCertificationsLists = applicantCertificationsService
					.updateApplicantCertificationsAfterResumeDetailsSet(applicantCertificationsList, updatedApplicant);

			log.debug("Updated Applicant Certifications List: {}", applicantCertificationsLists, updatedApplicant);

			ApplicantExperienceService applicantExperienceService = ApplicationContextProvider.getApplicationContext()
					.getBean(ApplicantExperienceService.class);
			List<ApplicantExperience> applicantExperienceLists = applicantExperienceService
					.updateApplicantExperienceAfterResumeDetailsSet(applicantExperienceList, updatedApplicant);

			log.debug("Updated Applicant Experience List: {}", applicantExperienceLists);

			applicantPromptWrapper.setApplicantCertifications(applicantCertificationsLists);
			applicantPromptWrapper.setApplicantEducation(applicantEducationLists);
			applicantPromptWrapper.setApplicantExperience(applicantExperienceLists);
			applicantPromptWrapper.setApplicantLanguage(applicantLanguageLists);

			log.debug("Updated applicantPromptWrapper is : {}", applicantPromptWrapper);
			log.debug("Updated applicantPromptWrapper is : {}", applicantPromptWrapper.toString());

			applicantRepository.save(applicant);

			log.debug("Updated applicant is : {}", applicant);
			log.debug("Updated applicant is : {}", applicant.toString());

			applicantPromptWrapper.setApplicant(applicant);

			log.debug("applicant updated successfully  applicant: {}", applicant);
			if (Boolean.TRUE.equals(applicantPromptWrapper.getJobApplicationUpdateNeeded())) {
				try {
					JobApplicationService jobApplicationService = ApplicationContextProvider.getApplicationContext()
							.getBean(JobApplicationService.class);
					JobApplication jobApplication = jobApplicationService.findByApplicantId(applicant.getId());
					if (jobApplication != null) {
						jobApplication = jobApplicationService.updateJobApplicationForApplicant(applicant,
								jobApplication);
						applicantPromptWrapper.setJobApplication(jobApplication);
					} else {
						jobApplication = jobApplicationService.createJobApplicationForApplicant(applicant, null);
						applicantPromptWrapper.setJobApplication(jobApplication);
					}
					applicantPromptWrapper.setJobApplicationUpdateNeeded(Boolean.TRUE);
				} catch (Exception e) {
					log.error(
							"Inside @Class ApplicantServiceImpl error while Creating/updating JobApplication for Applicant : {}",
							e.getMessage());
				}
			}
			return applicantPromptWrapper;
		} catch (BusinessException e) {

			log.error("inside method updateApplicantAfterResumeDetailsSet error while update applicant");
			throw new BusinessException("error while update applicant ", e.getMessage());
		}

	}

	@Override
	public Applicant getApplicantByUserId(Integer userId) {
		User contextUser = getUserContext();
		log.debug("contextUser user Id is : {}", contextUser.getUserid());
		log.debug("Inside getApplicantByUserId customerId is : {}", commonUtils.getCustomerId());
		Applicant applicant = applicantRepository.findByUserId(contextUser.getUserid(), commonUtils.getCustomerId());
		return (applicant != null ? applicant : null);

	}

	private Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		try {
			return (Date) sdf.parse(dateString);
		} catch (java.text.ParseException e) {
			log.error("Inside @Class ApplicantServiceImpl error while parse the date:{}", e.getMessage());
		}
		return null;
	}

	public static String readDocx(byte[] docxBytes) throws IOException {
		try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(docxBytes));
				XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
			return extractor.getText();
		}
	}

	public static String readDoc(byte[] docBytes) throws IOException {
		try (HWPFDocument doc = new HWPFDocument(new ByteArrayInputStream(docBytes));
				WordExtractor extractor = new WordExtractor(doc)) {
			return extractor.getText();
		}
	}

	private String convertObjectToJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error(ERROR_WHILE_CONVERTING_OBJECT_TO_JSON, e);
			return "Error converting object to JSON";
		}
	}

	private boolean isValidString(String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		if (input.contains(",")) {
			return false; // Return false if a comma is found
		}
		return true; // Return true if no comma is found
	}

	public String regenerateProfessionalSummary(ProfessionalSummaryWrapper professionalSummaryWrapper) {
		log.debug("Inside method regenerateProfessionalSummary professionalSummaryWrapper is : {}",
				professionalSummaryWrapper);
		try {
			return regenerateProfessionalSummaryUsingPrompt(professionalSummaryWrapper);
		} catch (Exception e) {
			log.error(ERROR_WHILE_CONVERTING_OBJECT_TO_JSON, e);
			return "Error While Regenerating Professional Summary";
		}
	}

	private String regenerateProfessionalSummaryUsingPrompt(ProfessionalSummaryWrapper professionalSummaryWrapper) {
		log.info("Inside method regenerateProfessionalSummaryUsingPrompt");
		String promptResumeSummary = "";
		String responseSummary = "";
		try {
			promptResumeSummary = getRegeneratedProfessionalResumeSummary(
					professionalSummaryWrapper.getExistingProfessionalSummary(),
					professionalSummaryWrapper.getUserRequirement());
			responseSummary = vectorIntegrationService.executePrompt(promptResumeSummary);
			log.debug(RESPONSE_FROM_PROMPT_CURL_IS, responseSummary);
		} catch (BusinessException be) {
			log.error(CAUGHT_ERROR_FOR_RESUME_SUMMARY_FROM_PROMPT, be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		String resumeResponseSummary = "";
		JSONObject fullResponseforSummary = new JSONObject(responseSummary);
		if (fullResponseforSummary.has(RESPONSE)) {
			try {
				JSONObject responseSummaryObj = fullResponseforSummary.getJSONObject(RESPONSE);
				if (responseSummaryObj.get(PROFILE_SUMMARY) != null) {
					resumeResponseSummary = responseSummaryObj.getString(PROFILE_SUMMARY);
					log.debug(RESUME_RESPONSE_SUMMARY_KEY_FROM_FULL_RESPONSEFOR_SUMMARY_IS, resumeResponseSummary);
					JSONObject jsonResponse = new JSONObject();
					jsonResponse.put("responseText", resumeResponseSummary);
					return jsonResponse.toString();
				}
			} catch (Exception e) {
				log.error("Unable To Parse Response from Prompt after Regenerating Professional Summary", e);
			}
		}
		return APIConstants.FAILURE_JSON;
	}

	private String getRegeneratedProfessionalResumeSummary(String existingProfessionalSummary, String userRequirement) {
		log.info("Inside method getRegeneratedProfessionalResumeSummary");
		String prompt = "You are a helpful AI Assistant which helps in extracting key information from a resume/CV of an applicant and generating an overall professional summary and individual work experience summary for them. You will be given profile summary content in text format below. Your task is to perform the user query asked below on the profile summary content provided to you.\n"
				+ " \n" + "User query: \n"
				+ "Regenerate the profile summary of the applicant using the profile summary data provided below and regenerate it according to the user input which will be provided separately below.\n"
				+ " \n" + "Profile summary content:\n" + "%s\n" + " \n" + "User input:\n" + "%s\n" + " \n"
				+ "Output Requirements:\n" + " \n"
				+ "Provide a professional summary of the applicant in a way such that when shown in the applications, it shows the applicants excitement and eagerness to work in future opportunities and what value will the applicant add in the organization in which he/she is applying for with their skills and experiences as provided in the resume text.\n"
				+ "Keep the summary under 800 characters.\n"
				+ "Always use the professional summary content as base and modify the content based on the input of the user in the same professional tone and in third person perspective.\n"
				+ "Always give output in JSON format for 'profileSummary' key with the regenerated text in the value"
				+ " **outputformat** {{###@ profileSummary : <string response> @###}}";
		return String.format(prompt, existingProfessionalSummary, userRequirement);
	}

	private boolean isValidEmailFormat(String email) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
		return email.matches(emailRegex);
	}

	@Override
	public String regenerateWorkExperienceSummary(WorkExperienceSummaryWrapper workExperienceSummaryWrapper) {
		log.debug("Inside method regenerateWorkExperienceSummary regenerateWorkExperienceSummary is : {}",
				workExperienceSummaryWrapper);
		try {
			return regenerateWorkExperienceSummaryUsingPrompt(workExperienceSummaryWrapper);
		} catch (Exception e) {
			log.error(ERROR_WHILE_CONVERTING_OBJECT_TO_JSON, e);
			return "Error While Regenerating WorkExperienceSummary Summary";
		}
	}

	private String regenerateWorkExperienceSummaryUsingPrompt(
			WorkExperienceSummaryWrapper workExperienceSummaryWrapper) {
		log.info("Inside method regenerateWorkExperienceSummaryUsingPrompt");
		String promptResumeSummary = "";
		String responseSummary = "";
		try {
			promptResumeSummary = getRegeneratedProfessionalResumeSummaryForWorkExperience(
					workExperienceSummaryWrapper.getExistingWorkExperienceSummary(),
					workExperienceSummaryWrapper.getUserRequirement());
			responseSummary = vectorIntegrationService.executePrompt(promptResumeSummary);
			log.debug(RESPONSE_FROM_PROMPT_CURL_IS, responseSummary);
		} catch (BusinessException be) {
			log.error(CAUGHT_ERROR_FOR_RESUME_SUMMARY_FROM_PROMPT, be.getMessage());
		} catch (Exception e) {
			log.error(SOMETHING_WENT_WRONG, e);
		}
		String resumeResponseSummary = "";
		JSONObject fullResponseforSummary = new JSONObject(responseSummary);
		if (fullResponseforSummary.has(RESPONSE)) {
			try {
				JSONObject responseSummaryObj = fullResponseforSummary.getJSONObject(RESPONSE);
				if (responseSummaryObj.get(PROFILE_SUMMARY) != null) {
					resumeResponseSummary = responseSummaryObj.getString(PROFILE_SUMMARY);
					log.debug(RESUME_RESPONSE_SUMMARY_KEY_FROM_FULL_RESPONSEFOR_SUMMARY_IS, resumeResponseSummary);
					JSONObject jsonResponse = new JSONObject();
					jsonResponse.put("responseText", resumeResponseSummary);
					return jsonResponse.toString();
				}
			} catch (Exception e) {
				log.error("Unable To Parse Response from Prompt after Regenerating WorkExperienceSummary", e);
			}
		}
		return APIConstants.FAILURE_JSON;
	}

	private String getRegeneratedProfessionalResumeSummaryForWorkExperience(String existingWorkExperienceSummary,
			String userRequirement) {
		log.info("Inside method getRegeneratedProfessionalResumeSummaryForWorkExperience");
		String prompt = "You are a helpful AI Assistant which helps in extracting key information from a resume/CV of an applicant and generating an overall professional summary and individual work experience summary for them. You will be given profile summary content in text format below. Your task is to perform the user query asked below on the profile summary content provided to you.\n"
				+ " \n" + "User query: \n"
				+ "Regenerate the profile summary of the applicant using the profile summary data provided below and regenerate it according to the user input which will be provided separately below.\n"
				+ " \n" + "Profile summary content:\n" + "%s\n" + " \n" + "User input:\n" + "%s\n" + " \n"
				+ "Output Requirements:\n" + " \n"
				+ "Provide a professional summary of the applicant in a way such that when shown in the applications, it shows the applicants excitement and eagerness to work in future opportunities and what value will the applicant add in the organization in which he/she is applying for with their skills and experiences as provided in the resume text.\n"
				+ "Keep the summary under 800 characters.\n"
				+ "Always use the professional summary content as base and modify the content based on the input of the user in the same professional tone and in third person perspective.\n"
				+ "Always give output in JSON format for 'profileSummary' key with the regenerated text in the value."
				+ " **outputformat** {{###@ profileSummary :<string response> @###}}";
		return String.format(prompt, existingWorkExperienceSummary, userRequirement);
	}

	public static ObjectMapper getObjectMapper() {
		log.info("Inside @Class ApplicantServiceImpl @Method getObjectMapper");
		if (!(objectMapper instanceof ObjectMapper)) {
			objectMapper = new ObjectMapper();
			SimpleFilterProvider filterProvider = new SimpleFilterProvider();
			filterProvider.setFailOnUnknownId(false);
			FilterProvider filters = filterProvider.addFilter("propertyFilter", new PropertyFilter());
			objectMapper.setFilterProvider(filters);
		}
		return objectMapper;
	}

	public void activateAction(Employee employee) {

		try {
			log.debug("Inside class Employeel method activateAction  :{} ", employee.getId());
			String processInstanceId = employee.getProcessInstanceId();
			log.debug("Starting workflowActionController  processInstanceId :{} ", processInstanceId);
			WorkflowActionsController workflowActionController = ApplicationContextProvider.getApplicationContext()
					.getBean(WorkflowActionsController.class);
			workflowActionController.notifyActions(processInstanceId);
			log.info("Starting workflowActionController completed ");
		} catch (Exception e) {
			log.error("Error in Triggering Action :{} :{} ", e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	@Override
	@Transactional
	public EmployeePopulateDto populateApplicantToEmployee(String processInstanceId, Integer applicantId) {
		log.debug("Inside method populateApplicantToEmployee processInstanceId:{},applicantId:{} ", processInstanceId,
				applicantId);
		Employee employee = null;
		EmployeePopulateDto employeePopulateDto = new EmployeePopulateDto();
		try {
			if (processInstanceId != null) {
				log.debug("Inside method populateApplicantToEmployee customerId is : {}", commonUtils.getCustomerId());
				Applicant applicantFound = applicantRepository.findApplicantByProcessInstanceId(processInstanceId,
						commonUtils.getCustomerId());
				log.debug("applicant Found from processInstanceId :{}", applicantFound);
				if (applicantFound != null) {
					employee = populateOtherApplicantData(applicantFound, employeePopulateDto);
					log.info("Inside first if populateOtherApplicantData completed");
					log.debug("Inside first if populateOtherApplicantData employee found is:{}", employee);
				}
			} else if (applicantId != null) {
				Applicant applicantFound = applicantRepository.findActiveApplicantById(applicantId,
						commonUtils.getCustomerId());
				if (applicantFound != null) {
					employee = populateOtherApplicantData(applicantFound, employeePopulateDto);
					log.debug("populateOtherApplicantData employee found is:{}", employee);
					log.info("populateOtherApplicantData completed");
				}
			}
			return employeePopulateDto;
		} catch (Exception e) {
			log.error("Error occured inside :{}", e);
		}
		return employeePopulateDto;
	}

	@Transactional
	public Employee populateOtherApplicantData(Applicant applicant, EmployeePopulateDto employeePopulateDto) {
		log.debug("Inside populateOtherApplicantData applicant:{} ", applicant.getId());
		Employee employee = null;
		if (applicant != null) {
			employee = createEmployeeFromApplicant(applicant);
			if (employee != null) {
				log.debug("Inside method populateApplicantToEmployee employee id:{}", employee.getId());
				log.debug("Inside populateOtherApplicantData applicant:{} , employee:{}", applicant.getId(),
						employee.getId());
				populateApplicantCertificationToEmployeeCertification(applicant, employee);
				log.info("populateApplicantCertificationToEmployeeCertification completed");
				populateApplicantDependentDetailsToEmployeeDependentDetails(applicant, employee);
				log.info("populateApplicantDependentDetailsToEmployeeDependentDetails completed");
				populateApplicantLanguageToEmployeeLanguage(applicant, employee);
				log.info("populateApplicantLanguageToEmployeeLanguage completed");
				populateApplicantNationalIdentificationToEmployeeNationalIdentification(applicant, employee,
						employeePopulateDto);
				log.info("populateApplicantNationalIdentificationToEmployeeNationalIdentification completed");
				populateApplicantExperienceToEmployeeExperience(applicant, employee);
				log.info("populateApplicantExperienceToEmployeeExperience completed");
				populateApplicantEducationToEmployeeEducationDetails(applicant, employee);
				log.info("populateApplicantEducationToEmployeeEducationDetails completed");

				if (employee != null) {
					log.debug("Inside method populateApplicantToEmployee employee id : {}", employee.getId());
					employeePopulateDto.setId(employee.getId());
					employeePopulateDto.setFullName(employee.getFullName());
					employeePopulateDto.setWorkEmailAddress(employee.getWorkEmailAddress());
					employeePopulateDto.setEmploymentStatus(employee.getEmploymentStatus());
					if (employee.getReportingManager() != null) {
						employeePopulateDto.setReportingManagerFullName(employee.getReportingManager().getFullName());
						employeePopulateDto.setReportingManagerWorkEmailAddress(
								employee.getReportingManager().getWorkEmailAddress());
					}
					log.debug("employeePopulateDto :{}", employeePopulateDto);
				}

				employee.setText2(employeePopulateDto.getType());
				employeeRepository.save(employee);

				try {
					TriggerBpmnAspect triggerBpmnAspect = ApplicationContextProvider.getApplicationContext()
							.getBean(TriggerBpmnAspect.class);
					String objEmployee = null;
					log.info("Going to Trigger BPMN for EmployeeId :{} ", employee.getId());
					try {
						objEmployee = getObjectMapper().writeValueAsString(employee);
						log.debug(" Obj employee :{} ", employee);
					} catch (JsonProcessingException e) {
						log.error("Error while getting object value as string :{}", Utils.getStackTrace(e));
					}
					try {
						triggerBpmnAspect.triggerBpmnViaAPI(objEmployee, "HRMS_APP_NAME", "Employee");
						log.info(" triggerBpmnAspect completed employee :{}  ", employee.getWorkflowStage());
						activateAction(employee);
					} catch (Exception e) {
						log.error("Error in Triggering Workflow :{} :{}", e.getMessage(), Utils.getStackTrace(e));

					}
				} catch (Exception e) {
					log.error("While triggering workflow action exception occured :{}", e);
				}
			}

		}
		return employee;

	}

	@Transactional
	public Employee createEmployeeFromApplicant(Applicant applicant) {
		log.debug("Inside method createEmployeeFromApplicant applicantId:{}", applicant.getId());
		try {
			Employee employee = new Employee();
			if (applicant != null) {
				log.debug("Inside method createEmployeeFromApplicant applicant found:{}", applicant.getId());
				employee.setFirstName(applicant.getFirstName());
				employee.setMiddleName(applicant.getMiddleName());
				employee.setLastName(applicant.getLastName());
				String firstName = applicant.getFirstName();
				String lastName = applicant.getLastName();
				String fullName = firstName + " " + lastName;
				log.debug("Inside method createEmployeeFromApplicant fullName:{}", fullName);
				employee.setFullName(fullName);
				employee.setArabicFirstName(applicant.getArabicFirstName());
				employee.setArabicFullName(applicant.getArabicFullName());
				employee.setArabicLastName(applicant.getArabicLastName());
				employee.setCountryOfResidence(applicant.getCountryOfResidence());
				employee.setDateOfBirth(applicant.getDateOfBirth());
				employee.setGender(applicant.getGender());
				employee.setMaritalStatus(applicant.getMaritalStatus());
				employee.setSourceHire(applicant.getApplicantSource());
				employee.setWorkEmailAddress(applicant.getEmailId());
				employee.setWorkPhoneNumber(applicant.getPhone());
				employee.setPersonalMobileNumber(applicant.getMobile());
				employee.setPersonalEmailAddress(applicant.getEmailId());
				employee.setUserId(applicant.getUserId());
				employee.setCitizenship(applicant.getCitizenship());
				employee.setPrimaryShortAddress(applicant.getPrimaryShortAddress());
				employee.setPrimaryAddressBuildingNumber(applicant.getPrimaryAddressBuildingNumber());
				employee.setPrimaryAddressPostalCode(applicant.getPrimaryAddressPostalCode());
				employee.setText1(applicant.getText1());
				employee.setText2(applicant.getText2());
				employee.setText3(applicant.getText3());
				employee.setText4(applicant.getText4());
				employee.setText5(applicant.getText5());
				employee.setText6(applicant.getText6());
				employee.setText7(applicant.getText7());
				employee.setText8(applicant.getText8());
				employee.setText9(applicant.getText9());
				employee.setText10(applicant.getText10());
				employee.setEmployeePhoto(applicant.getApplicantPhoto());
				employee.setArabicLastName(applicant.getArabicLastName());
				employee.setState(applicant.getState());

				List<Skill> skillList = new ArrayList<>(applicant.getSkills());
				log.debug("Skills are : {}", skillList);
				if (skillList != null && !skillList.isEmpty()) {
					log.debug("Skills fetched are : {}", skillList);
					employee.setSkills(skillList);
				}

				Map<String, String> mp = new HashMap<>();
				String generatedName = null;
				NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
				try {
					nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("employeeRule", mp,
							Status.ALLOCATED);
					log.info("Inside createEmployeeFromApplicant NameGenerationWrapperV2: {}", nameGenerationWrapperV2);
					generatedName = nameGenerationWrapperV2.getGeneratedName();
					log.debug("Inside createEmployeeFromApplicant generatedName: {} ", generatedName);
					employee.setEmployeeId(generatedName);
				} catch (Exception e) {
					logger.error("Failed to create/generate Naming Id For Employee");
				}
				if (applicant.getCity() != null) {
					employee.setPresentCity(applicant.getCity().getName());
				}
				if (applicant.getCountry() != null) {
					employee.setPresentCountry(applicant.getCountry().getName());
				}
				try {
					Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
					String employmentStatusMapped = hrmsSystemConfigMap.get(EMPLOYMENT_STATUS);
					String contractTypeMapped = hrmsSystemConfigMap.get(CONTRACT_TYPE);
					String locationNameMapped = hrmsSystemConfigMap.get(LOCATION_NAME);
					log.debug(
							"Inside createEmployeeFromApplicant employmentStatusMapped:{} ,contractTypeMapped:{}, locationNameMapped:{}",
							employmentStatusMapped, contractTypeMapped, locationNameMapped);

					employee.setEmploymentStatus(employmentStatusMapped);
					employee.setContractType(contractTypeMapped);
					log.debug("Inside createEmployeeFromApplicant customerId is : {}", commonUtils.getCustomerId());
					Location location = locationRepository.findByName(locationNameMapped, commonUtils.getCustomerId());
					if (location != null) {
						log.debug("Inside method createEmployeeFromApplicant locationId:{}", location.getId());
						employee.setLocation(location);
					}
				} catch (Exception e) {
					log.error("Exception occured while getting values from config : {}", e);
				}
				Offers offers = offersRepository.findByApplicantId(applicant.getId(), commonUtils.getCustomerId());
				if (offers != null) {
					log.debug("Inside method createEmployeeFromApplicant offers :{}", offers.getId());
					employee.setDepartment(offers.getDepartment());
					employee.setDesignation(offers.getDesignation());
					employee.setDateOfJoining(offers.getExpectedJoiningDate());
					Designation designation = offers.getDesignation();
					if (designation != null) {
						log.debug("In createEmployee Reporting manager:{}", designation.getEmployee());
						if (designation.getEmployee() != null) {
							log.debug("Reporting manager id:{}", designation.getEmployee().getId());
							employee.setReportingManager(designation.getEmployee());
						}
					}
				}
				setEmploymentTypeOfOnboardingEmployee(applicant, employee);
				log.debug("Inside method createEmployeeFromApplicant employee to be Saved is : {}", employee);
				Employee employeeSaved = employeeRepository.save(employee);
				log.debug("Inside method createEmployeeFromApplicant employeeSaved:{}", employeeSaved.getId());
			}

			return employee;
		} catch (Exception e) {
			log.error("Error occured while creating employee :{}", e);
		}
		return null;
	}

	public void setEmploymentTypeOfOnboardingEmployee(Applicant applicant, Employee employee) {
		log.debug("Inside method setEmploymentTypeOfOnboardingEmployee applicant:{} ", applicant.getId());
		String employmentType = "Direct Hire";
		JobApplication jobApplication = jobApplicationRepository.findByApplicantId(applicant.getId());
		log.debug("Inside method setEmploymentTypeOfOnboardingEmployee jobApplication found is : {} ", jobApplication);
		if (jobApplication != null) {
			JobOpening jobOpening = jobApplication.getJobOpening();
			log.debug("Inside method setEmploymentTypeOfOnboardingEmployee jobOpening found is : {} ", jobOpening);
			if (jobOpening != null && jobOpening.getJobType() != null) {
				log.debug("Inside setEmploymentTypeOfOnboardingEmployee jobType : {} ", jobOpening.getJobType());
				employmentType = jobOpening.getJobType();
			}
		}
		employee.setEmploymentType(employmentType);

		log.debug("Inside method setEmploymentTypeOfOnboardingEmployee EmploymentType: {}",
				employee.getEmploymentType());
	}

	@Transactional(readOnly = true)
	public void populateApplicantCertificationToEmployeeCertification(Applicant applicant, Employee employee) {
		log.debug("Inside method populateApplicantCertificationToEmployee applicantId:{} , employeeId:{}",
				applicant.getId(), employee.getId());
		log.debug("Inside populateApplicantCertificationToEmployeeCertification customerId is : {}",
				commonUtils.getCustomerId());
		try {
			if (applicant != null) {
				List<ApplicantCertifications> listOfCertifications = applicantCertificationsRepository
						.getCertificationsForApplicant(applicant.getId(), commonUtils.getCustomerId());
				log.debug("Inside method populateApplicantCertificationToEmployee listOfCertifications :{}",
						listOfCertifications.size());
				if (listOfCertifications != null && !listOfCertifications.isEmpty()) {
					for (ApplicantCertifications applicantCertifications : listOfCertifications) {
						if (applicantCertifications != null) {
							log.debug("Inside method populateApplicantCertificationToEmployee  certificationId:{}",
									applicantCertifications.getId());
							EmployeeCertification employeeCertification = new EmployeeCertification();
							employeeCertification.setEmployee(employee);
							employeeCertification.setCertificationName(applicantCertifications.getCertificationName());
							employeeCertification.setCertificationId(applicantCertifications.getCertificationId());
							employeeCertification.setIssuingInstitute(applicantCertifications.getIssuingInstitution());
							employeeCertification.setRenewalRequired(false);
							employeeCertificationRepository.save(employeeCertification);
							log.debug(
									"Inside method populateApplicantCertificationToEmployee savedEmployeeCertification:{}",
									employeeCertification.getId());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Inside method populateApplicantCertificationToEmployee error occured:{}", e);
		}
	}

	@Transactional(readOnly = true)
	public void populateApplicantDependentDetailsToEmployeeDependentDetails(Applicant applicant, Employee employee) {
		log.debug(
				"Inside method populateApplicantDependentDetailsToEmployeeDependenctDetails applicantId:{} , employeeId:{}",
				applicant.getId(), employee.getId());
		try {
			if (applicant != null) {
				log.debug("Inside method populateApplicantDependentDetailsToEmployeeDependenctDetails customerId: {}",
						commonUtils.getCustomerId());
				List<ApplicantDependentDetails> listOfDependentDetails = applicantDependentDetailsRepository
						.getDependentDetailsByApplicantId(applicant.getId(), commonUtils.getCustomerId());
				log.debug(
						"Inside method populateApplicantDependentDetailsToEmployeeDependenctDetails listOfDependentDetails :{}",
						listOfDependentDetails.size());
				if (listOfDependentDetails != null && !listOfDependentDetails.isEmpty()) {
					for (ApplicantDependentDetails applicantDependentDetails : listOfDependentDetails) {
						if (applicantDependentDetails != null) {
							log.debug(
									"Inside method populateApplicantDependentDetailsToEmployeeDependenctDetails ADD id:{}",
									applicantDependentDetails.getId());
							EmployeeDependentDetails employeeDependentDetails = new EmployeeDependentDetails();
							employeeDependentDetails.setFirstName(applicantDependentDetails.getFirstName());
							employeeDependentDetails.setMiddleName(applicantDependentDetails.getMiddleName());
							employeeDependentDetails.setLastName(applicantDependentDetails.getLastName());
							employeeDependentDetails.setFullName(applicantDependentDetails.getFullName());
							employeeDependentDetails.setContactNumber(applicantDependentDetails.getContactNumber());
							employeeDependentDetails.setDateOfBirth(applicantDependentDetails.getDateOfBirth());
							employeeDependentDetails
									.setDependentIdentification(applicantDependentDetails.getDependentIdentification());
							employeeDependentDetails.setRelationship(applicantDependentDetails.getRelationship());
							employeeDependentDetails.setGender(applicantDependentDetails.getGender());
							employeeDependentDetails.setEmployee(employee);
							employeeDependentDetails.setAccommodationRequirement(false);
							employeeDependentDetailsRepository.save(employeeDependentDetails);
							log.debug("Inside method populateApplicantDependentDetails EDD ids:{}",
									employeeDependentDetails.getId());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Inside method populateApplicantDependentDetailsToEmployeeDependenctDetails error occured:{}", e);
		}
	}

	@Transactional(readOnly = true)
	public void populateApplicantLanguageToEmployeeLanguage(Applicant applicant, Employee employee) {
		log.debug("Inside method populateApplicantLanguageToEmployeeLanguage applicantId:{} , employeeId:{}",
				applicant.getId(), employee.getId());
		try {
			if (applicant != null) {
				log.debug("Inside populateApplicantLanguageToEmployeeLanguage customerId: {}",
						commonUtils.getCustomerId());
				List<ApplicantLanguage> listOfApplicantLanguage = applicantLanguageRepository
						.getLanguagesForApplicant(applicant.getId(), commonUtils.getCustomerId());
				log.debug("Inside method populateApplicantLanguageToEmployeeLanguage listOfApplicantLanguage :{}",
						listOfApplicantLanguage.size());
				if (listOfApplicantLanguage != null && !listOfApplicantLanguage.isEmpty()) {
					for (ApplicantLanguage applicantLanguage : listOfApplicantLanguage) {
						if (applicantLanguage != null) {
							log.debug(
									"Inside method populateApplicantLanguageToEmployeeLanguage ApplicantLanguage id:{}",
									applicantLanguage.getId());
							EmployeeLanguage employeeLanguage = new EmployeeLanguage();
							employeeLanguage.setEmployee(employee);
							employeeLanguage.setLanguageName(applicantLanguage.getLanguageName());
							employeeLanguage.setLanguageLevel(applicantLanguage.getLanguageLevel());
							employeeLanguageRepository.save(employeeLanguage);
							log.debug(
									"Inside method populateApplicantLanguageToEmployeeLanguage employeeLanguage id:{}",
									employeeLanguage.getId());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Inside method populateApplicantLanguageToEmployeeLanguage error occured:{}", e);
		}

	}

	@Transactional(readOnly = true)
	public void populateApplicantNationalIdentificationToEmployeeNationalIdentification(Applicant applicant,
			Employee employee, EmployeePopulateDto employeePopulateDto) {
		log.debug(
				"Inside method populateApplicantNationalIdentificationToEmployeeNationalIdentification applicantId:{} , employeeId:{}",
				applicant.getId(), employee.getId());
		try {
			if (applicant != null) {
				log.debug(
						"Inside populateApplicantNationalIdentificationToEmployeeNationalIdentification customerId is : {}",
						commonUtils.getCustomerId());
				List<ApplicantNationalIdentification> listOfNationalIdentification = applicantNationalIdentificationRepository
						.getNationalIdentificationByApplicantId(applicant.getId(), commonUtils.getCustomerId());
				log.debug(
						"Inside method populateApplicantNationalIdentificationToEmployeeNationalIdentification listOfNationalIdentification :{}",
						listOfNationalIdentification.size());
				if (listOfNationalIdentification != null && !listOfNationalIdentification.isEmpty()) {
					for (ApplicantNationalIdentification applicantNationalIdentification : listOfNationalIdentification) {
						if (applicantNationalIdentification != null) {
							log.debug("Inside applicantNationalIdentification id:{}",
									applicantNationalIdentification.getId());
							EmployeeNationalIdentification employeeNationalIdentification = new EmployeeNationalIdentification();
							employeeNationalIdentification.setEmployee(employee);
							employeeNationalIdentification
									.setBorderNumber(applicantNationalIdentification.getBorderNumber());
							employeeNationalIdentification
									.setIdentificationNumber(applicantNationalIdentification.getIdentificationNumber());
							employeeNationalIdentification
									.setScannedImage(applicantNationalIdentification.getScannedImage());
							employeeNationalIdentification.setType(applicantNationalIdentification.getType());
							employeeNationalIdentification
									.setExpiryDate(applicantNationalIdentification.getExpiryDate());
							employeeNationalIdentification
									.setCountryOfIssue(applicantNationalIdentification.getCountryOfIssue());
							employeeNationalIdentificationRepository.save(employeeNationalIdentification);
							log.debug("Inside method  employeeNationalIdentification id:{}",
									employeeNationalIdentification.getId());
							employeePopulateDto.setNationalId(employeeNationalIdentification.getId());
							employeePopulateDto.setType(employeeNationalIdentification.getType());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(
					"Inside method populateApplicantNationalIdentificationToEmployeeNationalIdentification error ocuured:{}",
					e);
		}
	}

	@Transactional(readOnly = true)
	public void populateApplicantExperienceToEmployeeExperience(Applicant applicant, Employee employee) {
		log.debug("Inside method populateApplicantExperienceToEmployeeExperience applicantId:{} , employeeId:{}",
				applicant.getId(), employee.getId());
		try {
			if (applicant != null) {
				log.debug("Inside populateApplicantExperienceToEmployeeExperience customerId is : {}",
						commonUtils.getCustomerId());
				List<ApplicantExperience> listOfExperience = applicantExperienceRepository
						.getExperienceForApplicant(applicant.getId(), commonUtils.getCustomerId());
				log.debug("Inside method populateApplicantExperienceToEmployeeExperience listOfExperience :{}",
						listOfExperience.size());
				if (listOfExperience != null && !listOfExperience.isEmpty()) {
					for (ApplicantExperience applicantExperience : listOfExperience) {
						if (applicantExperience != null) {
							log.debug(
									"Inside method populateApplicantExperienceToEmployeeExperience applicantExperience:{}",
									applicantExperience.getId());
							EmployeeWorkExperience employeeWorkExperience = new EmployeeWorkExperience();
							employeeWorkExperience.setEmployee(employee);
							employeeWorkExperience.setCompanyName(applicantExperience.getCompany());
							employeeWorkExperience.setReasonForLeave(applicantExperience.getReasonForLeave());
							employeeWorkExperience.setEmployementStatus(applicantExperience.getEmploymentStatus());
							employeeWorkExperience
									.setEmpIdPreviousCompany(applicantExperience.getEmpIdPreviousCompany());
							employeeWorkExperience.setAddress(applicantExperience.getAddress());
							employeeWorkExperience.setAttachment1(applicantExperience.getAttachment1());
							employeeWorkExperience.setAttachment2(applicantExperience.getAttachment2());
							employeeWorkExperienceRepository.save(employeeWorkExperience);
							log.debug(
									"Inside method populateApplicantExperienceToEmployeeExperience employeeWorkExperience:{}",
									employeeWorkExperience.getId());

						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Inside method populateApplicantExperienceToEmployeeExperience error occured:{}", e);
		}

	}

	@Transactional(readOnly = true)
	public void populateApplicantEducationToEmployeeEducationDetails(Applicant applicant, Employee employee) {
		log.debug("Inside method populateApplicantEducationToEmployeeEducationDetails applicantId:{} , employeeId:{}",
				applicant.getId(), employee.getId());
		try {
			if (applicant != null) {
				log.debug("Inside populateApplicantEducationToEmployeeEducationDetails customerid: {}",
						commonUtils.getCustomerId());
				List<ApplicantEducation> listOfApplicantEducation = applicantEducationRepository
						.getEducationsForApplicant(applicant.getId(), commonUtils.getCustomerId());
				log.debug("Inside method populateApplicantEducationToEmployeeEducationDetails listOfExperience :{}",
						listOfApplicantEducation.size());
				if (listOfApplicantEducation != null && !listOfApplicantEducation.isEmpty()) {
					for (ApplicantEducation applicantEducation : listOfApplicantEducation) {
						if (applicantEducation != null) {
							log.debug(
									"Inside method populateApplicantEducationToEmployeeEducationDetails applicantEducation:{}",
									applicantEducation.getId());
							Employeeeducationdetails employeeeducationdetails = new Employeeeducationdetails();
							employeeeducationdetails.setEmployee(employee);
							employeeeducationdetails.setDegreeDiploma(applicantEducation.getDegreeDiploma());
							employeeeducationdetails.setDateOfCompletion(applicantEducation.getDateOfCompletion());
							employeeeducationdetails.setAdditionalNotes(applicantEducation.getAdditionalNotes());
							employeeeducationdetails.setCountryofStudy(applicantEducation.getCountryofStudy());
							employeeeducationdetails.setDegreeEndDate(applicantEducation.getDegreeEndDate());
							employeeeducationdetails.setDegreeStartDate(applicantEducation.getDegreeStartDate());
							employeeeducationdetails.setGrade("" + applicantEducation.getGrade());
							employeeeducationdetails.setStudentId(applicantEducation.getStudentId());
							employeeeducationdetails.setCity(applicantEducation.getCity());
							employeeeducationdetailsRepository.save(employeeeducationdetails);
							log.debug(
									"Inside method populateApplicantEducationToEmployeeEducationDetails employeeeducationdetails:{}",
									employeeeducationdetails.getId());

						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Inside method populateApplicantEducationToEmployeeEducationDetails error occured:{}", e);
		}
	}

	@Transactional
	@Override
	public String deleteApplicantCorrespondingData(Integer id) {
		log.debug("Inside deleteApplicantCorrespondingData employeeId:{}", id);
		log.debug(" Inside @deleteApplicantCorrespondingData customerId is : {}", commonUtils.getCustomerId());
		try {
			log.info("Going to delete ApplicantCertifications");
			applicantCertificationsRepository.deleteCertificationsByApplicantId(id, commonUtils.getCustomerId());
			log.info("Completed deleting ApplicantCertifications");

			log.info("Going to delete ApplicantDependentDetails ");
			applicantDependentDetailsRepository.deleteDependentDetailsByApplicantId(id, commonUtils.getCustomerId());
			log.info("Completed deleting ApplicantDependentDetails");

			log.info("Going to delete ApplicantLanguage ");
			applicantLanguageRepository.deleteLanguageByApplicantId(id, commonUtils.getCustomerId());
			log.info("Completed deleting ApplicantLanguage ");

			log.info("Going to delete ApplicantIdentification");
			applicantNationalIdentificationRepository.deleteNationalIdentificationByApplicantId(id,
					commonUtils.getCustomerId());
			log.info("Completed deleting ApplicantNationalIdentification ");

			log.info("Going to delete ApplicantWorkExperience ");
			applicantExperienceRepository.deleteExperienceByApplicantId(id, commonUtils.getCustomerId());
			log.info("Completed deleting ApplicantWorkExperience ");

			log.info("Going to delete Applicanteducationdetails ");
			applicantEducationRepository.deleteEducationByApplicantId(id, commonUtils.getCustomerId());
			log.info("Completed deleting Applicanteducationdetails");

			applicantRepository.deleteById(id);
			log.info("Completed deleting applicant");

			return APIConstants.SUCCESS_JSON;

		} catch (Exception e) {
			log.error("Error occured while deleting employee and its corresponding data:{}", e);
		}

		return APIConstants.FAILURE_JSON;
	}

	
	private static String extractJsonObjectString(String inputString) {
		log.debug("Inside extractJsonObjectString");
        int firstIndex = inputString.indexOf('{');
        int lastIndex = inputString.lastIndexOf('}');
 
 
        if (firstIndex != -1 && lastIndex != -1 && firstIndex < lastIndex) {
            inputString = inputString.substring(firstIndex, lastIndex + 1);
        }
 
        return inputString;
    }
	
}
