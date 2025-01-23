package com.nouros.payrollmanagement.service.impl;



import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.commons.io.excel.Excel;
import com.enttribe.commons.io.excel.ExcelRow;
import com.enttribe.commons.io.excel.ExcelWriter;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.annotation.TriggerBpmnAspect;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.platform.utility.notification.mail.model.NotificationAttachment;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.pii.filter.PropertyFilter;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.nouros.hrms.integration.service.DocumentIntegrationService;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.LeavesService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.util.report.ExcelUtils;
import com.nouros.hrms.wrapper.UnpaidLeaveWrapper;
import com.nouros.payrollmanagement.integration.service.NamingIntegrationService;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructureHistory;
import com.nouros.payrollmanagement.model.GradeMetaInfo;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure.EmployeeMobileType;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure.FrequencyType;
import com.nouros.payrollmanagement.repository.EmployeeMonthlySalaryRepository;
import com.nouros.payrollmanagement.repository.EmployeeSalaryStructureRepository;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryService;
import com.nouros.payrollmanagement.service.EmployeeSalaryStructureHistoryService;
import com.nouros.payrollmanagement.service.EmployeeSalaryStructureService;
import com.nouros.payrollmanagement.service.GradeMetaInfoService;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.service.PayrollRunService;
import com.nouros.payrollmanagement.utils.CalculationUtils;
import com.nouros.payrollmanagement.utils.PRConstant;
import com.nouros.payrollmanagement.wrapper.PayrollRequestWrapper;
import com.nouros.payrollmanagement.wrapper.PayrollResponseWrapper;

import io.netty.handler.ssl.SslClientHelloHandler;

/**
 * This is a class named "EmployeeSalaryStructureServiceImpl" which is located
 * in the package " com.enttribe.payrollmanagement.service.impl", It appears to
 * be an implementation of the "EmployeeSalaryStructureService" interface and it
 * extends the "AbstractService" class, which seems to be a generic class for
 * handling CRUD operations for entities. This class is annotated with @Service,
 * indicating that it is a Spring Service bean. This class is using
 * Lombok's @Slf4j annotation which will automatically generate an Slf4j based
 * logger instance, so it is using the Slf4j API for logging. The class has a
 * constructor which takes a single parameter of GenericRepository
 * EmployeeSalaryStructure and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of
 * EmployeeSalaryStructure EmployeeSalaryStructure) for exporting the
 * EmployeeSalaryStructure data into excel file by reading the template and
 * mapping the EmployeeSalaryStructure details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.enttribe.payrollmanagement.util'
 */

@Service
public class EmployeeSalaryStructureServiceImpl extends AbstractService<Integer,EmployeeSalaryStructure>
		implements EmployeeSalaryStructureService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeSalaryStructure entities.
	 */

	private static final String ENTITY_NAME = "EmployeeSalaryStructure";
	private static final Logger log = LogManager.getLogger(EmployeeSalaryStructureServiceImpl.class);
	private static ObjectMapper objectMapper = null;

	public EmployeeSalaryStructureServiceImpl(GenericRepository<EmployeeSalaryStructure> repository) {
		super(repository, EmployeeSalaryStructure.class);
	}

	@Autowired
	private EmployeeSalaryStructureRepository employeeSalaryStructureRepository;

	@Autowired
	private CalculationUtils calculationUtils;

	@Autowired
	private HrmsSystemConfigService hrmsSystemConfigService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeSalaryStructureHistoryService employeeSalaryStructureHistoryService;

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	private GradeMetaInfoService gradeMetaInfoService;

	@Autowired
	LeavesService leavesService;

	@Autowired
	private UserRest userRest;

	@Autowired
	private EmployeeMonthlySalaryService employeeMonthlySalaryService;

	@Autowired
	private NotificationIntegration notificationIntegration;

	@Autowired
	private DocumentIntegrationService documentIntegrationService;
	
	@Autowired
	  private CommonUtils commonUtils;

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}


	/**
	 * This method is used to export the given list of EmployeeSalaryStructure
	 * objects into an excel file. It reads an excel template
	 * 'EmployeeSalaryStructure.xlsx' from the resource folder 'templates/reports'
	 * and maps the EmployeeSalaryStructure data onto the template and returns the
	 * generated excel file in the form of a byte array. param
	 * EmployeeSalaryStructure - List of EmployeeSalaryStructure objects to be
	 * exported
	 * 
	 * @return byte[] - The generated excel file in the form of a byte array
	 * @throws IOException - When the template file is not found or there is an
	 *                     error reading the file
	 */
	@Override
	public byte[] export(List<EmployeeSalaryStructure> employeeSalaryStructure) throws IOException {
		log.debug(" Inside @export :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		InputStream resourceAsStream = this.getClass().getClassLoader()
				.getResourceAsStream("templates/reports/EmployeeSalaryStructure.xlsx");
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resourceAsStream);
		int rowCount = 1;
		return setTableData(employeeSalaryStructure, xssfWorkbook, rowCount);
	}

	/**
	 * This method is responsible for setting the data of an Excel document, using a
	 * template and a list of EmployeeSalaryStructure objects. The data is written
	 * to the template starting at the specified row number.
	 * 
	 * @param EmployeeSalaryStructure a List of EmployeeSalaryStructure objects,
	 *                                representing the data that will be written to
	 *                                the Excel document
	 * @param templatePath            an XSSFWorkbook object, representing the
	 *                                template Excel document that the data will be
	 *                                written to
	 * @param rowCount                an int, representing the starting row number
	 *                                where data will be written in the Excel
	 *                                document
	 * @return a byte array of the Excel document after the data has been written to
	 *         it.
	 * @throws IOException if there is an issue reading or writing to the Excel
	 *                     document
	 */

	/**
	 * This method appears to take in three parameters:
	 * 
	 * A List of EmployeeSalaryStructure objects, representing the data that will be
	 * written to the Excel document. An XSSFWorkbook object, representing the
	 * template Excel document that the data will be written to. An int,
	 * representing the starting row number where data will be written in the Excel
	 * document. The method has a return type of byte array, which is the Excel
	 * document after the data has been written to it. The method also throws an
	 * IOException, which would be thrown if there is an issue reading or writing to
	 * the Excel document.
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
	 * check if EmployeeSalaryStructure list is not empty.
	 * 
	 * It then iterates over the list of EmployeeSalaryStructure objects and for
	 * each EmployeeSalaryStructure, it creates a new row in the Excel document at
	 * the specified row number.
	 * 
	 * It also retrieves the list of columns for the "EmployeeSalaryStructure"
	 * entity from the tableColumn map, and iterates over the columns.
	 * 
	 * For each column, it attempts to retrieve the value for that column from the
	 * current EmployeeSalaryStructure object using the ExcelUtils.invokeGetter
	 * method, passing in the current EmployeeSalaryStructure object, the column
	 * name and the identityColumnMapping.
	 * 
	 * The value returned by this method is then set as the value of the cell in the
	 * current row and column. If an introspection exception occur it will print the
	 * stacktrace of the exception
	 * 
	 * After all the data is written to the Excel document, the method returns the
	 * Excel document as a byte array using excelWriter.toByteArray() and log "going
	 * to return file"
	 */
	private byte[] setTableData(List<EmployeeSalaryStructure> employeeSalaryStructure, XSSFWorkbook templatePath,
			int rowCount) throws IOException {
		log.debug(" Inside @setTableData :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		Map<String, List<String>> tableColumn = new HashMap<>();
		String entity = ENTITY_NAME;
		Map<String, String> identityColumnMapping = new HashMap<>();
		Map<String, List<String>> templateHeaders = new HashMap<>();
		ExcelUtils.parseMappeddJson(tableColumn, identityColumnMapping, templateHeaders);
		log.info("table column map is :{}", tableColumn);
		try (ExcelWriter excelWriter = new ExcelWriter(templatePath)) {
			excelWriter.getWorkbook().setActiveSheet(0);
			if (CollectionUtils.isNotEmpty(employeeSalaryStructure)) {
				for (EmployeeSalaryStructure employeeSalaryStructureDetails : employeeSalaryStructure) {
					ExcelRow row = excelWriter.getOrCreateRow(0, rowCount);
					int index = 0;
					List<String> columns = tableColumn.get(entity);
					for (String column : columns) {
						if (column != null) {
							try {
								row.setCellValue(index, ExcelUtils
										.invokeGetter(employeeSalaryStructureDetails, column, identityColumnMapping)
										.toString());
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
	 * specifically data related to EmployeeSalaryStructure objects. The method
	 * takes in a MultipartFile object, which represents the Excel file containing
	 * the data. The method then validates the template headers in the Excel file
	 * and if they are valid, it saves the data to the database.
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
		log.debug(" Inside @importData :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		List<EmployeeSalaryStructure> employeeSalaryStructures = new ArrayList<>();
		Excel workBook = new Excel(excelFile.getInputStream());
		Map<String, List<String>> tableColumn = new HashMap<>(); // Table Name and list of Columns
		Map<String, String> columnsMapping = new HashMap<>(); // Json Mapping DispalyName and Name
		Map<String, List<String>> templateHeadres = new HashMap<>();
		List<String> displayNames = new ArrayList<>();
		ExcelUtils.parseMappeddJson(tableColumn, columnsMapping, templateHeadres);
		displayNames.addAll(templateHeadres.get(ENTITY_NAME));
		List<String> columnNames = new ArrayList<>();
		columnNames.addAll(tableColumn.get(ENTITY_NAME));
		boolean validateTableTemplateHeader = ExcelUtils.validateTableTemplateHeader(workBook, displayNames);// Validating
																												// Columns
																												// and
																												// Headers
		if (validateTableTemplateHeader) {
			log.info("columns and headers are validated");
			employeeSalaryStructures = saveData(workBook, columnsMapping, columnNames);
		} else {
			log.info("columns and headers invalide");
		}
		if (CollectionUtils.isNotEmpty(employeeSalaryStructures)) {
			employeeSalaryStructureRepository.saveAll(employeeSalaryStructures);
			return APIConstants.SUCCESS_JSON;
		}
		return APIConstants.FAILURE_JSON;
	}

	/**
	 * This method is responsible for saving data to the database, specifically data
	 * related to EmployeeSalaryStructure objects. The method takes in an Excel
	 * object, which represents the sheet containing the data, a mapping of columns
	 * from the excel sheet to the EmployeeSalaryStructure class, and a list of
	 * column names. The method uses the iterator for the sheet to read data row by
	 * row, create new EmployeeSalaryStructure objects, and set the properties of
	 * the EmployeeSalaryStructure objects using the column mapping and column
	 * names. The method returns a list of EmployeeSalaryStructure objects that have
	 * been saved to the database.
	 *
	 * @param sheet         an Excel object representing the sheet containing the
	 *                      data
	 * @param columnMapping a map representing the mapping of columns from the excel
	 *                      sheet to the EmployeeSalaryStructure class
	 * @param columnNames   a list of column names of the excel sheet
	 * @return a list of EmployeeSalaryStructure objects that have been saved to the
	 *         database
	 */

	public List<EmployeeSalaryStructure> saveData(Excel sheet, Map<String, String> columnMapping,
			List<String> columnNames) {
		log.debug(" Inside @saveData :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		Iterator<ExcelRow> rowIterator = sheet.iterator();
		List<EmployeeSalaryStructure> employeeSalaryStructures = new ArrayList<>();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			ExcelRow excelRow = rowIterator.next();
			EmployeeSalaryStructure employeeSalaryStructure = new EmployeeSalaryStructure();
			int index = -1;
			for (String columnName : columnNames) {
				try {
					ExcelUtils.invokeSetter(employeeSalaryStructure, columnName, excelRow.getString(++index));
				} catch (InstantiationException e) {
					log.error("failed while going to set the value :{}", excelRow.getString(++index));
					log.error("InstantiationException occurred: {}", e.getMessage());

				} catch (ClassNotFoundException e) {
					log.error("ClassNotFoundException occurred: {}", e.getMessage());
				}
			}
			employeeSalaryStructures.add(employeeSalaryStructure);
		}
		return employeeSalaryStructures;
	}

	/**
	 * This method is responsible for soft-deleting an EmployeeSalaryStructure
	 * record in the database. The method takes in an int id which represents the id
	 * of the EmployeeSalaryStructure that needs to be soft-deleted. It uses the id
	 * to find the EmployeeSalaryStructure by calling the
	 * EmployeeSalaryStructureRepository.findById method. If the
	 * EmployeeSalaryStructure is found, it sets the "deleted" field to true, save
	 * the EmployeeSalaryStructure in the repository, and saves it in the database
	 *
	 * @param id an int representing the id of the EmployeeSalaryStructure that
	 *           needs to be soft-deleted
	 */
	@Override
	public void softDelete(int id) {
		log.debug(" Inside @softDelete :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		EmployeeSalaryStructure employeeSalaryStructure = super.findById(id);

		if (employeeSalaryStructure != null) {

			EmployeeSalaryStructure employeeSalaryStructure1 = employeeSalaryStructure;

			employeeSalaryStructure1.setDeleted(true);
			employeeSalaryStructureRepository.save(employeeSalaryStructure1);

		}
	}

	/**
	 * This method is responsible for soft-deleting multiple EmployeeSalaryStructure
	 * records in the database in bulk. The method takes in a List of integers, each
	 * representing the id of an EmployeeSalaryStructure that needs to be
	 * soft-deleted. It iterates through the list, calling the softDelete method for
	 * each id passed in the list.
	 *
	 * @param list a List of integers representing the ids of the
	 *             EmployeeSalaryStructure that need to be soft-deleted
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
	 * @param employeeSalaryStructure The employeeSalaryStructure object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeSalaryStructure create(EmployeeSalaryStructure employeeSalaryStructure) {
		log.debug(" Inside @create :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		if (employeeSalaryStructure != null && employeeSalaryStructure.getEmployee() != null
				&& employeeSalaryStructure.getEmployee().getId() != null) {
			log.info("Inside EmployeeSalaryStructureService method create  employeeid  : {} ",
					employeeSalaryStructure.getEmployee().getId());

			Integer employeeId = employeeSalaryStructure.getEmployee().getId();

			EmployeeSalaryStructure existingSalaryMapping = getEmployeeMappedSalaryStructure(employeeId);

			if (existingSalaryMapping != null) {

				setEmployeeHistory(existingSalaryMapping);

				employeeSalaryStructureRepository.delete(existingSalaryMapping);

				setYearlyDetails(employeeSalaryStructure);
				return employeeSalaryStructureRepository.save(employeeSalaryStructure);
			}
			employeeSalaryStructure.setStartDate(new Date());
			setGradePercentageOfEmployee(employeeSalaryStructure);

			setYearlyDetails(employeeSalaryStructure);
			return employeeSalaryStructureRepository.save(employeeSalaryStructure);
		}
		return null;
	}

	private void setYearlyDetails(EmployeeSalaryStructure employeeSalaryStructure) {
		log.info("Inside EmployeeSalaryStructureService method setYearlyDetails");
		log.debug(" Inside @setYearlyDetails :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		if (employeeSalaryStructure != null) {
			Double grossSalary = 0.0;
			log.info("Inside EmployeeSalaryStructureService method setYearlyDetails ");
			grossSalary = setGrossSalaryeDetails(employeeSalaryStructure, grossSalary);

			log.info("Inside EmployeeSalaryStructureService method setYearlyDetails grossSalary2 :{} ", grossSalary);

			employeeSalaryStructure.setGrossSalary(grossSalary);

			if (employeeSalaryStructure.getGrossSalary() != null) {
				Double totalValue = 0.0;
				Double bonustotal = 0.0;

				totalValue = setGrossSalaryeDetails(employeeSalaryStructure, totalValue);

				totalValue = totalValue * 12.0;

				bonustotal = setGrossSalaryInsideSetYearlyDetails(employeeSalaryStructure, bonustotal);

				log.info(
						"Inside EmployeeSalaryStructureService method setYearlyDetails bonustotal :{}  totalValue :{}  ",
						bonustotal, totalValue);

				totalValue = totalValue + bonustotal;

				employeeSalaryStructure.setYearlyGrossSalary(totalValue);

			}
		}
	}

	private Double setGrossSalaryeDetails(EmployeeSalaryStructure employeeSalaryStructure, Double grossSalary) {
		if (employeeSalaryStructure.getBasicSalary() != null) {
			log.debug(" Inside @setGrossSalaryeDetails :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
			grossSalary = grossSalary + employeeSalaryStructure.getBasicSalary();
			log.info("Inside EmployeeSalaryStructureService method setGrossSalaryeDetails grossSalary :{} ",
					grossSalary);
			employeeSalaryStructure.setYearlyBasicSalary(employeeSalaryStructure.getBasicSalary() * 12.0);
		}
		if (employeeSalaryStructure.getOverbase() != null) {
			grossSalary = grossSalary + employeeSalaryStructure.getOverbase();
			employeeSalaryStructure.setYearlyOverbase(employeeSalaryStructure.getOverbase() * 12.0);
		}
		if (employeeSalaryStructure.getCriticalSkills() != null) {
			grossSalary = grossSalary + employeeSalaryStructure.getCriticalSkills();
			employeeSalaryStructure.setYearlyCriticalSkills(employeeSalaryStructure.getCriticalSkills() * 12.0);

		}

		if (employeeSalaryStructure.getMobileAllowance() != null) {
			grossSalary = grossSalary + employeeSalaryStructure.getMobileAllowance();
			employeeSalaryStructure.setYearlyMobileAllowance(employeeSalaryStructure.getMobileAllowance() * 12.0);
		}

		if (employeeSalaryStructure.getHra() != null) {
			grossSalary = grossSalary + employeeSalaryStructure.getHra();
			employeeSalaryStructure.setYearlyHra(employeeSalaryStructure.getHra() * 12.0);
		}

		if (employeeSalaryStructure.getTa() != null) {
			grossSalary = grossSalary + employeeSalaryStructure.getTa();
			employeeSalaryStructure.setYearlyTa(employeeSalaryStructure.getTa() * 12.0);
		}
		return grossSalary;
	}

	private Double setGrossSalaryInsideSetYearlyDetails(EmployeeSalaryStructure employeeSalaryStructure,
			Double grossSalaryYear) {
		log.debug(" Inside @setGrossSalaryInsideSetYearlyDetails :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		log.debug(
				"Inside EmployeeSalaryStructureService method setGrossSalaryInsideSetYearlyDetails grossSalaryYear:{}",
				grossSalaryYear);
		if (employeeSalaryStructure.getSignUpBonus() != null) {
			grossSalaryYear = grossSalaryYear + employeeSalaryStructure.getSignUpBonus();
		}
		if (employeeSalaryStructure.getRelocationAllowance() != null) {
			grossSalaryYear = grossSalaryYear + employeeSalaryStructure.getRelocationAllowance();

		}
		if (employeeSalaryStructure.getSti() != null) {
			grossSalaryYear = grossSalaryYear + employeeSalaryStructure.getSti();
		}
		log.debug(
				"Inside EmployeeSalaryStructureService method setGrossSalaryInsideSetYearlyDetails grossSalaryYear:{}",
				grossSalaryYear);
		return grossSalaryYear;
	}

	private void setEmployeeHistory(EmployeeSalaryStructure existingSalaryMapping) {
		log.info("Inside EmployeeSalaryStructureService method setEmployeeHistory");
		log.debug(" Inside @setEmployeeHistory :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		if (existingSalaryMapping != null) {

			EmployeeSalaryStructureHistory employeeSalaryStructureHistory = new EmployeeSalaryStructureHistory();

			employeeSalaryStructureHistory.setBasicSalary(existingSalaryMapping.getBasicSalary());

			employeeSalaryStructureHistory.setCriticalSkills(existingSalaryMapping.getCriticalSkills());

			employeeSalaryStructureHistory.setEmployee(existingSalaryMapping.getEmployee());

			employeeSalaryStructureHistory.setEmployeeMobileFlag(existingSalaryMapping.getEmployeeMobileFlag());
			employeeSalaryStructureHistory.setDeleted(false);

			employeeSalaryStructureHistory.setEndDate(new Date());
			employeeSalaryStructureHistory.setHra(existingSalaryMapping.getHra());
			employeeSalaryStructureHistory.setLocalCurrency(existingSalaryMapping.getLocalCurrency());

			employeeSalaryStructureHistory.setMobileAllowance(existingSalaryMapping.getMobileAllowance());
			employeeSalaryStructureHistory.setNegoPctCritical(existingSalaryMapping.getNegoPctCritical());

			employeeSalaryStructureHistory.setNegoPctOverbase(existingSalaryMapping.getNegoPctOverbase());
			employeeSalaryStructureHistory.setOverbase(existingSalaryMapping.getOverbase());
			employeeSalaryStructureHistory.setOvertime(existingSalaryMapping.getOvertime());
			employeeSalaryStructureHistory.setPayFrequency(existingSalaryMapping.getPayFrequency());

			employeeSalaryStructureHistory.setPayRate(existingSalaryMapping.getPayRate());

			employeeSalaryStructureHistory.setRelocationAllowance(existingSalaryMapping.getRelocationAllowance());

			employeeSalaryStructureHistory.setRelocationDate(existingSalaryMapping.getRelocationAllowanceDate());
			employeeSalaryStructureHistory.setSignUpBonus(existingSalaryMapping.getSignUpBonus());

			employeeSalaryStructureHistory.setSignUpBonusDate(existingSalaryMapping.getSignUpBonusDate());
			employeeSalaryStructureHistory.setStartDate(existingSalaryMapping.getCreatedTime());

			employeeSalaryStructureHistory.setSti(existingSalaryMapping.getSti());
			employeeSalaryStructureHistory.setStiDate(existingSalaryMapping.getStiDate());
			employeeSalaryStructureHistory.setTa(existingSalaryMapping.getTa());

			employeeSalaryStructureHistory.setGrossSalary(existingSalaryMapping.getGrossSalary());

			employeeSalaryStructureHistory.setYearlyBasicSalary(existingSalaryMapping.getYearlyBasicSalary());
			employeeSalaryStructureHistory.setYearlyCriticalSkills(existingSalaryMapping.getYearlyCriticalSkills());
			employeeSalaryStructureHistory.setYearlyGrossSalary(existingSalaryMapping.getYearlyGrossSalary());
			employeeSalaryStructureHistory.setYearlyHra(existingSalaryMapping.getYearlyHra());

			employeeSalaryStructureHistory.setYearlyMobileAllowance(existingSalaryMapping.getYearlyMobileAllowance());
			employeeSalaryStructureHistory.setYearlyOverbase(existingSalaryMapping.getYearlyOverbase());
			employeeSalaryStructureHistory.setYearlyTa(existingSalaryMapping.getYearlyTa());

			employeeSalaryStructureHistory.setBoardMemberAmount(existingSalaryMapping.getBoardMemberAmount());

			employeeSalaryStructureHistory.setPayFrequency(existingSalaryMapping.getPayFrequency());
			employeeSalaryStructureHistory.setPayRate(existingSalaryMapping.getPayRate());
			employeeSalaryStructureHistory.setLocalCurrency(existingSalaryMapping.getLocalCurrency());

			employeeSalaryStructureHistory = employeeSalaryStructureHistoryService
					.create(employeeSalaryStructureHistory);
			if (null != employeeSalaryStructureHistory.getId()) {
				log.info("Inside EmployeeSalaryStructureService created Successfully");

			}

		}

	}

	@Override
	public PayrollRun executePayrollRun(PayrollRequestWrapper payrollRequestWrapper) {
		log.info("Inside EmployeeSalaryStructureService method executePayrollRun :{} :{} ",payrollRequestWrapper.getStartDate(),payrollRequestWrapper.getEndDate());
		log.debug(" Inside @executePayrollRun :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		try {
			Integer sucessCount = 0;
			Integer failureCount = 0;

			PayrollResponseWrapper payrollResponseWrapper = new PayrollResponseWrapper();
			Map<Object, Object> execptionObject = new HashMap<>();

			payrollResponseWrapper.setSucessCount(sucessCount);
			payrollResponseWrapper.setFailureCount(failureCount);
			PayrollRun payrollRun = new PayrollRun();

			payrollRun = getDateRangeForPayroll(payrollRun, payrollRequestWrapper);

			PayrollRunService payrollRunService = ApplicationContextProvider.getApplicationContext()
					.getBean(PayrollRunService.class);

			payrollRun.setPayrollVariance(0.0);
			payrollRun = payrollRunService.createPayroll(payrollRun);

			List<Employee> listOfEmployee = employeeService.findAll();

			PayrollRun payrollRunAsyc = payrollRun;

			CompletableFuture<Void> processFuture = CompletableFuture.runAsync(() -> {
				log.info("Starting interateEmployee");
				interateEmployee(payrollResponseWrapper, execptionObject, payrollRunAsyc, listOfEmployee);
				log.info("Completed interateEmployee");
			});
			processFuture.get();
			processFuture.thenRunAsync(() -> {
				log.info("Starting setPayrollVarianceAndSum");
				try {

					setPayrollVarianceAndSum(payrollRunAsyc);
				} catch (Exception ex) {
					log.error("Inside EmployeeSalaryStructureService  setPayrollVarianceAndSum : {}",
							Utils.getStackTrace(ex), ex);
				}
				log.info("Completed setPayrollVarianceAndSum");
			}).thenRunAsync(() -> {
				log.info("Starting triggerBpmnAspect payrollRunAsyc :{} payrollRunAsyc ID :{} ",
						payrollRunAsyc.getWorkflowStage(), payrollRunAsyc.getPayrollVariance());

				TriggerBpmnAspect triggerBpmnAspect = ApplicationContextProvider.getApplicationContext()
						.getBean(TriggerBpmnAspect.class);
				String objpayroll = null;
				try {
					objpayroll = getObjectMapper().writeValueAsString(payrollRunAsyc);
				} catch (JsonProcessingException e) {
					log.error("Error while getting object value as string :{}", Utils.getStackTrace(e));
				}
				triggerBpmnAspect.triggerBpmnViaAPI(objpayroll, "HRMS_APP_NAME", "PayrollRun");
				log.info("Starting triggerBpmnAspect completed payrollRunAsyc :{} payrollRunAsyc ID :{} ",
						payrollRunAsyc.getWorkflowStage(), payrollRunAsyc.getPayrollVariance());

			}).thenRunAsync(() -> {
				log.info("Starting identifyRemarkForPayrollVariance");
				identifyRemarkForPayrollVariance(payrollRunAsyc);
				log.info("Completed identifyRemarkForPayrollVariance");
			}).thenRunAsync(() -> {

				PayrollRun payroll = payrollRunService.findById(payrollRunAsyc.getId());
				if (payroll != null) {
					PayrollRun payrollRunAction = payroll;
					ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
					service.schedule(() -> activateAction(payrollRunAction), 5, TimeUnit.SECONDS);
				}
			}).exceptionally(ex -> {
				log.error("Error occurred in runDetail method: {}", Utils.getStackTrace(ex), ex);
				PayrollRun payroll = payrollRunService.findById(payrollRunAsyc.getId());
				if (payroll != null ) {
					PayrollRun payrollRunAction = payroll;
					ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
					service.schedule(() -> activateAction(payrollRunAction), 5, TimeUnit.SECONDS);
				}
				return null;
			});

			log.debug("Inside EmployeeSalaryStructureService  payrollRun {}", payrollRunAsyc.toString());
			return payrollRun;

		} catch (Exception ex) {
			log.error("Inside EmployeeSalaryStructureService method ERROR : {}", Utils.getStackTrace(ex), ex);
			// return com.nouros.hrms.util.APIConstants.FAILURE_JSON;
			return null;
		}

	}

	public void activateAction(PayrollRun payroll3) {
		log.debug(" Inside @activateAction :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		PayrollRunService payrollRunService = ApplicationContextProvider.getApplicationContext()
				.getBean(PayrollRunService.class);
		PayrollRun payroll = payrollRunService.findById(payroll3.getId());
		if (payroll != null) {
			PayrollRun payrollRunAction = payroll;
			log.debug("Starting new payroll id :{} ", payrollRunAction.getId());
			String processInstanceId = payrollRunAction.getProcessInstanceId();
			log.debug("Starting workflowActionController  processInstanceId :{} ", processInstanceId);
			WorkflowActionsController workflowActionController = ApplicationContextProvider.getApplicationContext()
					.getBean(WorkflowActionsController.class);
			workflowActionController.notifyActions(processInstanceId);
			log.info("Starting workflowActionController completed ");
		}
	}

	private void interateEmployee(PayrollResponseWrapper payrollResponseWrapper, Map<Object, Object> execptionObject,
			PayrollRun payrollRun, List<Employee> listOfEmployee) {
		log.debug(" Inside @iterareEmployee :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		for (Employee employee : listOfEmployee) {
 
			Date dateOfExit = employee.getDateOfExit();
		if( dateOfExit==null || !dateOfExit.before(payrollRun.getStartDate()) )
		{

			if (employee.getDesignation() != null && employee.getDesignation().getName() != null
					&& !employee.getDesignation().getName().equalsIgnoreCase("CEO")) {

				log.debug("Inside EmployeeSalaryStructureService method interateEmployee employeeID : {} ",
						employee.getId());

				log.debug("Inside EmployeeSalaryStructureService method interateEmployee employee : {} ", employee);

				log.debug(
						"Inside EmployeeSalaryStructureService method interateEmployee  employee id :{}  employee joinindate: {} ",
						employee.getId(), employee.getDateOfJoining());

				EmployeeSalaryStructure employeeMapping = getEmployeeMappedSalaryStructure(employee.getId());

				if (employeeMapping != null) {

					Map<String, Object> employeeMap = new HashMap<>();

					Integer parsedIntworkingDays = getWorkingDaysOfEmployee(payrollRun.getStartDate(),
							payrollRun.getEndDate(), employee.getId());

					Double stiMultipler = getGradeByGradeNumber(employee.getJobGrade());

					log.debug(
							"Inside EmployeeSalaryStructureService method interateEmployee payroll employeestiMultipler : {} ",
							stiMultipler);

					employeeMap.put(PRConstant.EMPLOYEE_WORKING_DAYS, parsedIntworkingDays);
					employeeMap.put(PRConstant.GRADE_MULTIPLER, stiMultipler);
					employeeMap.put(PRConstant.EMPLOYEE_CITIZENSHIP, employee.getCitizenship());

					if (employee.getMaritalStatus() != null) {
						employeeMap.put(PRConstant.EMPLOYEE_MARITIAL_STATUS, employee.getMaritalStatus());
					}

					log.debug(
							"Inside EmployeeSalaryStructureService method interateEmployee employeeSalaryStructure : {} ",
							employeeMapping.getId());

					try {
						calculationUtils.employeeMonthlySalary(employeeMapping, employeeMap, payrollRun,
								payrollResponseWrapper, execptionObject);

					} catch (Exception ex) {
						log.error("Inside EmployeeSalaryStructureService  interateEmployee method ex : {}",
								Utils.getStackTrace(ex), ex);
					}

				}

			}

		}
		
		}
	}

	private void setPayrollVarianceAndSum(PayrollRun payrollRun) {
		log.debug("inside EmployeeSalaryStructureService @method setPayrollVarianceAndSum payrollRun:{}", payrollRun);
		log.debug(" Inside @setPayrollVarianceAndSum :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		Integer payrollRunId = payrollRun.getId();

		try {
			EmployeeMonthlySalaryRepository repository = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeMonthlySalaryRepository.class);
			Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
			String onboardMemberTypeList = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);
			log.debug("Inside @class EmployeeSalaryStructureService @method setPayrollVarianceAndSum :{}  ",
					onboardMemberTypeList);
			String[] listString = onboardMemberTypeList.split(",");
			log.debug("Inside @class EmployeeSalaryStructureService @method setPayrollVarianceAndSum Array :{}  ",
					listString);
			List<Object[]> resultArray = repository.getSumSalaryOfPayroll(payrollRunId, listString,commonUtils.getCustomerId());

			log.debug(
					"Inside EmployeeSalaryStructureService method executePayrollRun employeeSalaryStructure : resultArray{}",
					resultArray);

			if (resultArray != null && !resultArray.isEmpty()) {
				Object[] results = resultArray.get(0);

				try {
					Double sumNetAmount = getDoubleValue(results, 0);
					Double sumLastMonthNetAmount = getDoubleValue(results, 1);
					Double totalSti = getDoubleValue(results, 2);
					Double totalSignUpBonus = getDoubleValue(results, 3);
					Double totalOtherEarning = getDoubleValue(results, 4);
					Double totalDeduction = getDoubleValue(results, 5);
					Double totalRellocationAllowance = getDoubleValue(results, 6);
					Double totalBonus = totalSti + totalSignUpBonus;
					Double totalBasicSalary = getDoubleValue(results, 7);
					Double totalHra = getDoubleValue(results, 8);
					Double totalTa = getDoubleValue(results, 9);
					Double totalMobileAllowance = getDoubleValue(results, 10);
					Double totalOvertime = getDoubleValue(results,11);
					Double totalOverbase = getDoubleValue(results,12);
					Double totalCriticalSkills = getDoubleValue(results,13);
					
					
					
					Double totalAllowances = totalHra + totalTa + totalMobileAllowance + totalRellocationAllowance;
					
					Double totalAdditionalEarnings = totalOtherEarning + totalSti + totalSignUpBonus;
					
					Double totalGrossSalary = totalBasicSalary + totalAllowances + totalAdditionalEarnings + totalOvertime
							+ totalOverbase + totalCriticalSkills;
					
					Double totalNetSalary = totalGrossSalary - totalDeduction;
					

					totalSti = formatDouble(totalSti);
					totalBonus = formatDouble(totalBonus);
					totalOtherEarning = formatDouble(totalOtherEarning);
					totalDeduction = formatDouble(totalDeduction);
					totalGrossSalary = formatDouble(totalGrossSalary);
					totalBasicSalary= formatDouble(totalBasicSalary);
					totalAllowances = formatDouble(totalAllowances);
					totalAdditionalEarnings = formatDouble(totalAdditionalEarnings);
					totalNetSalary = formatDouble(totalNetSalary);

					log.debug(
							"Inside EmployeeSalaryStructureService method executePayrollRun sumNetAmount : {} sumLastMonthNetAmount :{} totalSti:{} totalBonus :{} totalRellocationAllowance :{} totalSignUpBonus :{} totalOtherEarning :{} totalDeduction :{} ",
							sumNetAmount, sumLastMonthNetAmount, totalSti, totalBonus, totalRellocationAllowance,
							totalSignUpBonus, totalOtherEarning, totalDeduction);
					
					log.debug("Inside EmployeeSalaryStructureService method executePayrollRun totalBasicSalary:{} ,totalAllowances:{} , totalAdditionalEarnings:{}, totalGrossSalary:{} , totalDeduction:{}, totalNetSalary:{} ",
							totalBasicSalary,totalAllowances, totalAdditionalEarnings, totalGrossSalary, totalDeduction, totalNetSalary );

					Double diffvariance = sumNetAmount - sumLastMonthNetAmount;
					Double variancePct = 0.0;
					if (diffvariance != 0.0 && sumNetAmount != 0.0 && sumLastMonthNetAmount != 0.0) {
						variancePct = (diffvariance / sumLastMonthNetAmount) * 100;
						variancePct = formatDouble(variancePct);
					}

					payrollRun.setPayrollVariance(variancePct);

					if (variancePct > 0.5) {
						log.debug(
								"Inside @class EmployeeSalaryStructureService @method variancePct setPayrollVarianceAndSum greter than 0.5 :{}  ",
								variancePct);
						payrollRun.setVarianceAction(PRConstant.IN_REVIEW_WITH_HR_SPECIALIST);
					} else {
						log.debug(
								"Inside @class EmployeeSalaryStructureService @method variancePct setPayrollVarianceAndSum less than than 0.5 :{}  ",
								variancePct);
						payrollRun.setVarianceAction(PRConstant.IN_REVIEW_WITH_PAYROLL_SPECIALIST);
					}

					sumLastMonthNetAmount = formatDouble(sumLastMonthNetAmount);
					sumNetAmount = formatDouble(sumNetAmount);

					payrollRun.setTotalLastMonthSalary(sumLastMonthNetAmount);
					payrollRun.setTotalBonus(totalBonus);
					payrollRun.setTotalSti(totalSti);
					
					payrollRun.setTotalBasicSalary(totalBasicSalary);
					payrollRun.setTotalAllowances(totalAllowances);
					payrollRun.setTotalOtherEarning(totalAdditionalEarnings);
					payrollRun.setTotalNetSalary(totalNetSalary);
					payrollRun.setTotalGrossSalary(totalGrossSalary);
					payrollRun.setTotalOtherDeduction(totalDeduction);
					
					
					
					
					payrollRun.setWorkflowStage(PRConstant.PROCESSED);
					PayrollRunService payrollRunService = ApplicationContextProvider.getApplicationContext()
							.getBean(PayrollRunService.class);
					payrollRunService.update(payrollRun);
					log.debug("Inside EmployeeSalaryStructureService payrollRun updated successfully {}  ",
							payrollRun.toString());
				} catch (Exception ex2) {
					log.error("Error occurred while calculating payroll details: {}", Utils.getStackTrace(ex2), ex2);
					throw new BusinessException("Error while calculating payroll details", ex2);
				}
			} else {
				log.warn("No results found for payrollRunId: {}", payrollRunId);
			}
		} catch (Exception ex1) {
			log.error("Error occurred while fetching payroll data: {}", Utils.getStackTrace(ex1), ex1);
			throw new BusinessException("Error while fetching payroll data", ex1);
		}
	}

	private void identifyRemarkForPayrollVariance(PayrollRun payrollRun) {
		log.debug("inside EmployeeSalaryStructureService @method identifyRemarkForPayrollVariance payrollRun:{}",
				payrollRun);
		log.debug(" Inside @identifyRemarkForPayrollVariance :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		try {
			if (payrollRun != null) {
				PayrollRunService payrollRunService = ApplicationContextProvider.getApplicationContext()
						.getBean(PayrollRunService.class);
				Integer payrollRunId = payrollRun.getId();
				payrollRun = payrollRunService.identifyVarianceReasons(payrollRunId);
				log.debug(
						"inside EmployeeSalaryStructureService @method identifyRemarkForPayrollVariance payrollRun:{}",
						payrollRun.getId());
			}
		} catch (Exception ex) {
			log.error("Inside EmployeeSalaryStructureService  identifyRemarkForPayrollVariance method ex : {}",
					Utils.getStackTrace(ex), ex);

		}
	}

	private void setGradePercentageOfEmployee(EmployeeSalaryStructure employeeSalaryStructure) {
		log.debug(" Inside @setGradePercentageOfEmployee :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		Employee employee = employeeSalaryStructure.getEmployee();
		if (employee.getJobGrade() != null) {
			String gradeNumber = employee.getJobGrade() + "";
			GradeMetaInfo grade = gradeMetaInfoService.getGradeByGradeNumber(gradeNumber);
			if (grade != null) {

				Double maxSalary = grade.getMaxBasic();
				log.debug("inside CalculationUtils @method setGradePercentageOfEmployee maxSalary :{}", maxSalary);
				Double basicSalary = employeeSalaryStructure.getBasicSalary();

				Double result = maxSalary - basicSalary;

				result = result / maxSalary;

				result = result * 100;
				log.debug("inside CalculationUtils @method setGradePercentageOfEmployee employee :{}", result);

				employee.setJobGradeRange(result);
				employeeService.update(employee);
			}
		}
	}

	public PayrollRun getDateRangeForPayroll(PayrollRun payrollRun, PayrollRequestWrapper payrollRequestWrapper) {
		log.debug(" Inside @getDateRangeForPayroll :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		log.debug("Inside getDateRangeForPayroll payrollRequestWrapper : {} ", payrollRequestWrapper);

		Integer durationDayCount = 0;

		Date currentDate = new Date();

		payrollRun.setRunDate(currentDate);

		if (payrollRequestWrapper == null || payrollRequestWrapper.getEndDate() == null
				|| payrollRequestWrapper.getStartDate() == null) {

			Integer dateLastMonth = Integer.parseInt(hrmsSystemConfigService.getValue(PRConstant.LAST_MONTH_DATE));

			Integer dateCurrentMonth = dateLastMonth - 1;

			log.info("getDateRangeForPayroll  hrmssystemconfig date  :dateLastMonth {}  dateCurrentMonth{} ",
					dateLastMonth, dateCurrentMonth);

			LocalDate currentDatelocal = LocalDate.now();

			YearMonth lastMonth = YearMonth.from(currentDatelocal).minusMonths(1);

			LocalDate startDatelocal = lastMonth.atDay(dateLastMonth);

			LocalDate endDatelocal = YearMonth.from(currentDatelocal).atDay(dateCurrentMonth);

			Date fromDate = Date.from(startDatelocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date toDate = Date.from(endDatelocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

			log.info("getDateRangeForPayroll  hrmssystemconfig date  :dateLastMonth {}  dateCurrentMonth{} ",
					dateLastMonth, dateCurrentMonth);

			payrollRun.setStartDate(fromDate);
			payrollRun.setEndDate(toDate);

			log.info("getDateRangeForPayroll payrollRun fromDate:{} ,toDate:{}", fromDate, toDate);

			String referenceId = generatePayrollReferenceId(toDate);
			log.info("getDateRangeForPayroll  hrmssystemconfig date  :referenceId {}   ", referenceId);
			payrollRun.setReferenceId(referenceId);

			durationDayCount = (int) ChronoUnit.DAYS.between(startDatelocal, endDatelocal);

			log.debug("getDateRangeForPayroll startDatelocal:{} endDatelocal:{} durationDayCount:{}", startDatelocal,
					endDatelocal, durationDayCount);

		} else {

			Date startDate = payrollRequestWrapper.getStartDate();
			Date timeZoneStartDate = setUserTimeZoneDate(startDate);

			Date endDate = payrollRequestWrapper.getEndDate();
			Date timeZoneEndDate = setUserTimeZoneDate(endDate);

			log.debug("Inside getDateRangeForPayroll timeZoneStartDate: {} ", timeZoneStartDate);
			log.debug("Inside getDateRangeForPayroll timeZoneEndDate: {}", timeZoneEndDate);

			if (startDate != null && endDate != null) {

				durationDayCount = getDayCountBetweenDates(startDate, endDate);
				payrollRun.setStartDate(timeZoneStartDate);
				payrollRun.setEndDate(timeZoneEndDate);

				log.info("Inside getDateRangeForPayroll durationDayCount:{} ", durationDayCount);

				String referenceId = generatePayrollReferenceId(endDate);
				log.info("getDateRangeForPayroll  hrmssystemconfig date  :referenceId {}   ", referenceId);
				payrollRun.setReferenceId(referenceId);
			}

		}
		payrollRun.setDurationDayCount(durationDayCount + 1);

		log.debug(" inside @class  EmployeeSalaryStructureServiceImpl @method  getDateRangeForPayroll payrollRun {} ",
				payrollRun);

		return payrollRun;
	}

	private Date setUserTimeZoneDate(Date date) {
		log.debug("Inside @class  EmployeeSalaryStructureServiceImpl @method setUserTimeZoneDate date: {}", date);
		try {

			if (ConfigUtils.getString(PRConstant.TIME_ZONE).equalsIgnoreCase("UTC")) {
				Instant startInstant = date.toInstant();
				Instant endInstant = startInstant.plus(5, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES);
				Date timeZoneStartDate = Date.from(endInstant);
				log.debug(
						"Inside @class  EmployeeSalaryStructureServiceImpl @method setUserTimeZoneDate timeZoneStartDate:{}",
						timeZoneStartDate);
				return timeZoneStartDate;
			}
		} catch (BusinessException e) {
			log.error("Error occur inside @class  EmployeeSalaryStructureServiceImpl setUserTimeZoneDate: {}",
					e.getMessage());
		}
		return date;
	}

	private String generatePayrollReferenceId(Date toDate) {
		log.debug(" inside @class  generatePayrollReferenceId @method  getDateRangeForPayroll toDate {} ", toDate);
		log.debug(" Inside @generatePayrollReferenceId :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		Map<String, String> mp = new HashMap<>();

		SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

		String monthString = monthFormat.format(toDate).toUpperCase();
		String yearString = yearFormat.format(toDate);
		mp.put("part2", monthString);
		mp.put("part3", yearString);
		String ruleName = ConfigUtils.getString(PRConstant.PAYROLL_RUN_RULE_NAME);
		log.debug(" inside @class  generatePayrollReferenceId @method  getDateRangeForPayroll mp {} ", mp);
		NamingIntegrationService namingService = ApplicationContextProvider.getApplicationContext()
				.getBean(NamingIntegrationService.class);
		String generatedReferenceId = namingService.generateNaming(ruleName, mp);
		log.debug(" inside @class  generatePayrollReferenceId @method  getDateRangeForPayroll generatedReferenceId {} ",
				generatedReferenceId);
		return generatedReferenceId;
	}

	@Override
	public Integer getWorkingDaysOfEmployee(Date fromDate, Date toDate, Integer employeeId) {
		log.debug("getWorkingDaysOfEmployee employeeId: {} ", employeeId);

		Employee optionalEmployee = employeeService.findById(employeeId);
		if (optionalEmployee != null ) {
			Employee employee = optionalEmployee;
			Date joiningDate = employee.getDateOfJoining();
			if (joiningDate != null && joiningDate.after(toDate)) {
				return 0;
			}
			if (joiningDate != null && joiningDate.after(fromDate) && joiningDate.before(toDate)) {
				log.debug("getWorkingDaysOfEmployee  joiningDate: {} fromDate:{}  toDate:{}", joiningDate, fromDate,
						toDate);
				fromDate = joiningDate;
			}

			List<UnpaidLeaveWrapper> wrapper = leavesService.getUnpaidLeaveCount(fromDate, toDate, employeeId);
			if (wrapper != null && !wrapper.isEmpty()) {
				UnpaidLeaveWrapper unpaidLeaveWrapper = wrapper.get(0);
				log.debug("getWorkingDaysOfEmployee  count of working days:  {}  employeeId :{} ",
						unpaidLeaveWrapper.getCountOfWorkingDays(), employeeId);
				Long workingDays = unpaidLeaveWrapper.getCountOfWorkingDays();

				Integer parsedIntworkingDays = parseLongToInteger(workingDays);
				log.debug(
						"Inside EmployeeSalaryStructureService method getWorkingDaysOfEmployee payroll employeeworkingDays : {}  parsedIntworkingDays : {}",
						workingDays, parsedIntworkingDays);
				return parsedIntworkingDays;
			}
		} else {
			log.warn("employee with employeeId {} not found", employeeId);
		}

		return 0;
	}

	private Double getGradeByGradeNumber(Integer jobGrade) {
		log.debug("Inside EmployeeSalaryStructureService method  getGradeByGradeNumber jobGrade: {} ", jobGrade);
		String jobNumber = jobGrade + "";
		GradeMetaInfo grade = gradeMetaInfoService.getGradeByGradeNumber(jobNumber);
		if (grade != null) {
			return grade.getStiMultiplier();
		}
		return 0.0;
	}

	public Integer getDayCountBetweenDates(Date startDate, Date endDate) {
		LocalDate startLocalDate = convertToLocalDate(startDate);
		LocalDate endLocalDate = convertToLocalDate(endDate);

		return (int) ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
	}

	private LocalDate convertToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Override
	public EmployeeSalaryStructure getEmployeeMappedSalaryStructure(Integer employeeId) {
		log.debug(" Inside @getEmployeeMappedSalaryStructure :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		log.debug("Inside EmployeeSalaryStructureService method getEmployeeMappedSalaryStructure   employeeId: {} ",
				employeeId);
		List<EmployeeSalaryStructure> listEmployeeSalaryStructure = employeeSalaryStructureRepository
				.findByEmployeeIdAndDeleted(employeeId, false);
		if (listEmployeeSalaryStructure != null && !listEmployeeSalaryStructure.isEmpty()) {
			return listEmployeeSalaryStructure.get(0);
		}
		return null;
	}

	public Integer parseLongToInteger(Long longValue) {
		if (longValue == null) {
			return null;
		}

		if (longValue < Integer.MIN_VALUE || longValue > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Long value " + longValue + " is out of range for an Integer.");
		}

		return longValue.intValue();
	}

	@Override
	public Object verifyOneTimeComponentCalculated(Integer employeeId) {
		log.debug(" Inside @verifyOneTimeComponentCalculated :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		log.debug("Inside EmployeeSalaryStructureService method verifyOneTimeComponentCalculated   employeeId: {} ",
				employeeId);
		Map<Object, Object> resultMap = new HashMap<>();
		if (employeeId == null)
			return null;
		Boolean checkForRellocation = employeeMonthlySalaryService.checkForRelocationAllowance(employeeId);

		resultMap.put("relocationCheck", checkForRellocation);
		resultMap.put("signUpCheck", checkForRellocation);

		log.debug("Inside EmployeeSalaryStructureService method verifyOneTimeComponentCalculated   resultMap: {} ",
				resultMap);

		return resultMap;
	}

	@Override
	public List<EmployeeSalaryStructure> getEmployeeSalaryStructureByUserId() {
		log.debug("Inside EmployeeSalaryStructureService method getEmployeeSalaryStructureByUserId");
		log.debug(" Inside @getEmployeeSalaryStructureByUserId :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		List<EmployeeSalaryStructure> resultList = new ArrayList<>();
		try {

			User contextUser = getUserContext();
			log.debug("contextUser user Id is : {}", contextUser.getUserid());
			Integer userId = contextUser.getUserid();

			Employee employee = employeeService.getEmployeeByUserId(userId);

			if (employee != null) {
				Integer employeeId = employee.getId();

				EmployeeSalaryStructure employeeSalaryStructure = getEmployeeMappedSalaryStructure(employeeId);

				if (employeeSalaryStructure == null) {
					log.error("No active salary structure found for employee ID: {} user id  {} ", employeeId, userId);
					return resultList;
				}
				resultList.add(employeeSalaryStructure);
				return resultList;
			} else {
				log.info("No employee found for user ID: {}", userId);
			}
		} catch (Exception e) {
			log.error("Error occurred while fetching employee salary structure", e);
			return resultList;
		}
		return resultList;
	}

	private Double getDoubleValue(Object[] results, int index) {
		return results[index] != null ? (Double) results[index] : 0.0;
	}

	private Double formatDouble(Object value) {
		log.debug("inside EmployeeSalaryStructureService @method formatDouble value :{} ", value);
		if (value != null && value instanceof BigDecimal) {
			return ((BigDecimal) value).doubleValue();
		} else if (value != null) {
			DecimalFormat df = new DecimalFormat("#.##");
			return Double.parseDouble(df.format(value));
		} else {
			return 0.0;
		}
	}

	public static ObjectMapper getObjectMapper() {
		log.info("Inside @Class EmployeeSalaryStructureService @Method getObjectMapper");
		if (!(objectMapper instanceof ObjectMapper)) {
			objectMapper = new ObjectMapper();
			SimpleFilterProvider filterProvider = new SimpleFilterProvider();
			filterProvider.setFailOnUnknownId(false);
			FilterProvider filters = filterProvider.addFilter("propertyFilter", new PropertyFilter());
			objectMapper.setFilterProvider(filters);
		}
		return objectMapper;
	}

	public List<EmployeeSalaryStructure> findByEmployeePk(Integer employeeId) {
		log.info("Inside @Class EmployeeSalaryStructureService @method findByEmployeePk ");
		log.debug(" Inside @findByEmployeePk :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		try {
			List<EmployeeSalaryStructure> employeeSalaryStructureList = employeeSalaryStructureRepository
					.findByEmployeeId(employeeId);
			log.info("Size of List in EmployeeSalaryStructure of employeeId :{} :{}", employeeId,
					employeeSalaryStructureList.size());
			return employeeSalaryStructureList;
		} catch (Exception e) {
			log.error("Error inside @class EmployeeSalaryStructureService @method findByEmployeePk :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	// Excel Upload :

	Map<String, String> failureMap = null;
	private Double doubleValue = null;
	private String stringValue = null;

	public List<EmployeeSalaryStructure> importEmployeeSalaryStructureData(MultipartFile excelFile) {
		log.debug(" Inside @importEmployeeSalaryStructureData :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		try {
			uploadInputFileInDocuments(excelFile);
		} catch (Exception e) {
			log.error("Error while Uploading Input File");
		}

		List<EmployeeSalaryStructure> list = new ArrayList<>();

		try (Workbook wb = new XSSFWorkbook(); Workbook workbook = new XSSFWorkbook(excelFile.getInputStream())) {

//			CompletableFuture<Void> processFuture = CompletableFuture.runAsync(() -> {
			log.info("Starting importEmployeeSalaryStructureData runAsync");

			Sheet sheet1 = wb.createSheet("Status");
			log.info("Workbook Object created");

			try {
				log.info("Inside importEmployeeSalaryStructureData ");

				Row rowCreated = sheet1.createRow(0);
				Cell cellCreated = rowCreated.createCell(0);

				cellCreated.setCellValue("EMPLOYEE");
				rowCreated.createCell(1).setCellValue("RESPONSE");

				log.info("Status Sheet Created");

				XSSFSheet spreadsheet = (XSSFSheet) workbook.getSheetAt(0);

				XSSFRow row;

				int columnCount = spreadsheet.getRow(0).getLastCellNum();
				int rowCount = spreadsheet.getLastRowNum() + 1;

				List<String> headerNamesList = getHeaderList(spreadsheet.getRow(0), columnCount);

				for (int i = 1; i < rowCount; i++) {

					failureMap = new HashMap<>();

					row = spreadsheet.getRow(i);

					rowCreated = sheet1.createRow(i);

					Employee e1 = findEmployee(row, headerNamesList, rowCreated.createCell(0));

					EmployeeSalaryStructure employeeSalaryStructure = null;

					if (e1 == null) {
						log.error("Unable to save this Employee Salary Structure id : {}", doubleValue.intValue());
						failureMap.put("EMPLOYEE_ID", "cannot find Id" + doubleValue.intValue());
						sheet1.createRow(i).createCell(1).setCellValue("" + failureMap);
					} else {
						List<EmployeeSalaryStructure> list1 = findByEmployeePk(e1.getId());
						log.debug("Inside if list1<EmployeeSalaryStructure> : {}", list1);
						if (list1 == null || list1.isEmpty()) {
							employeeSalaryStructure = new EmployeeSalaryStructure();
							log.info("new EmployeeSalaryStructure object created");
						} else {
							employeeSalaryStructure = list1.get(0);
							log.info("new EmployeeSalaryStructure object already existed so we need to update");
						}
						employeeSalaryStructure.setEmployee(e1);
						int k = 0;

						Boolean flag = true;

						for (int j = 0; j < columnCount; j++) {

							log.info("Inside for loop for column traversing");
							String headerName = spreadsheet.getRow(0).getCell(j).getStringCellValue().trim();

							if (k < 1) {
								k++;
								cellCreated = rowCreated.createCell(1);
							}

							log.debug("Inside if headerName : {}", headerName);

							doubleValue = null;
							stringValue = null;

							setStringAndDoubleValue(row, j, headerName);

							if ("BASIC_SALARY".equals(headerName)) {

								log.debug("Inside if basicSalary : {}", doubleValue);

								employeeSalaryStructure.setBasicSalary(doubleValue);

							} else if ("EMPLOYEE_ID".equals(headerName)) {

								log.debug("Inside if EMPLOYEE_ID : {}", doubleValue);

							} else if ("CRITICAL_SKILLS".equals(headerName)) {

								log.debug("Inside else if criticalSkills : {}", doubleValue);

								employeeSalaryStructure.setCriticalSkills(doubleValue);

							} else if ("END_DATE".equals(headerName)) {

								log.debug("Inside else if endDate : {}", stringValue);

								try {

									employeeSalaryStructure
											.setEndDate(convertToDate(stringValue, headerName, failureMap));
								} catch (Exception e) {
									log.error("Inside catch block of endDate");
								}

							} else if ("HOUSING_ALLOWANCE".equals(headerName)) {

								log.debug("Inside else if hra : {}", doubleValue);

								employeeSalaryStructure.setHra(doubleValue);

							} else if ("LOCAL_CURRENCY".equals(headerName)) {

								log.debug("Inside else if localCurrency : {}", stringValue);

								employeeSalaryStructure.setLocalCurrency(stringValue);

							} else if ("MOBILE_ALLOWANCE".equals(headerName)) {

								log.debug("Inside else if mobileAllowance : {}", doubleValue);

								employeeSalaryStructure.setMobileAllowance(doubleValue);

							} else if ("OVERBASE".equals(headerName)) {

								log.debug("Inside else if overbase : {}", doubleValue);

								employeeSalaryStructure.setOverbase(doubleValue);

							} else if ("OVERTIME".equals(headerName)) {

								log.debug("Inside else if overtime : {}", doubleValue);

								employeeSalaryStructure.setOvertime(doubleValue);
							} else if ("GROSS_SALARY".equals(headerName)) {

								log.debug("Inside else if grossSalary : {}", doubleValue);

								employeeSalaryStructure.setGrossSalary(doubleValue);

							} else if ("PAY_FREQUENCY".equals(headerName)) {

								log.debug("Inside else if payFrequency : {}", stringValue);

								if (stringValue.equalsIgnoreCase("HOURLY"))
									employeeSalaryStructure.setPayFrequency(FrequencyType.HOURLY);
								else if (stringValue.equalsIgnoreCase("WEEKLY"))
									employeeSalaryStructure.setPayFrequency(FrequencyType.WEEKLY);
								else if (stringValue.equalsIgnoreCase("MONTHLY"))
									employeeSalaryStructure.setPayFrequency(FrequencyType.MONTHLY);
								else {
									log.error("Unable to find PAY_FREQUENCY : {}", stringValue);
									failureMap.put(headerName, "Wrong " + stringValue);

								}
							} else if ("EMPLOYEE_MOBILE_FLAG".equals(headerName)) {

								stringValue = stringValue.trim();
								log.debug("Inside else if employeeMobileFlag : {}", stringValue);

								if (stringValue.equalsIgnoreCase("EMPLOYEE_MOBILE_PLAN"))
									employeeSalaryStructure
											.setEmployeeMobileFlag(EmployeeMobileType.EMPLOYEE_MOBILE_PLAN);
								else if (stringValue.equalsIgnoreCase("COMPANY_MOBILE_PLAN"))
									employeeSalaryStructure
											.setEmployeeMobileFlag(EmployeeMobileType.COMPANY_MOBILE_PLAN);
								else if (stringValue.equalsIgnoreCase("COMPANY_MOBILE"))
									employeeSalaryStructure.setEmployeeMobileFlag(EmployeeMobileType.COMPANY_MOBILE);
								else {
									log.error("Unable to fetch employeeMobileFlag : {}", stringValue);
									failureMap.put(headerName, "Wrong " + stringValue);
								}
							} else if ("PAY_RATE".equals(headerName)) {

								log.debug("Inside else if payRate : {}", doubleValue);

								employeeSalaryStructure.setPayRate(doubleValue);

							} else if ("RELOCATION_ALLOWANCE".equals(headerName)) {

								log.debug("Inside else if relocationAllowance : {}", doubleValue);

								employeeSalaryStructure.setRelocationAllowance(doubleValue);

							} else if ("RELOCATION_ALLOWANCE_DATE".equals(headerName)) {

								log.debug("Inside else if relocationAllowanceDate : {}", stringValue);
								try {

									employeeSalaryStructure.setRelocationAllowanceDate(
											convertToDate(stringValue, headerName, failureMap));
								} catch (Exception e) {
									log.error("Inside catch block of endDate");
								}

							} else if ("SIGN_UP_BONUS".equals(headerName)) {

								log.debug("Inside else if signUpBonus : {}", doubleValue);

								employeeSalaryStructure.setSignUpBonus(doubleValue);

							} else if ("SIGN_UP_BONUS_DATE".equals(headerName)) {

								log.debug("Inside else if signUpBonusDate : {}", stringValue);
								try {

									employeeSalaryStructure
											.setSignUpBonusDate(convertToDate(stringValue, headerName, failureMap));
								} catch (Exception e) {
									log.error("Inside catch block of endDate");
								}

							} else if ("START_DATE".equals(headerName)) {

								log.debug("Inside else if startDate : {}", stringValue);
								try {

									employeeSalaryStructure
											.setStartDate(convertToDate(stringValue, headerName, failureMap));
								} catch (Exception e) {
									log.error("Inside catch block of endDate");
								}

							} else if ("STI".equals(headerName)) {

								log.debug("Inside else if sti : {}", doubleValue);

								employeeSalaryStructure.setSti(doubleValue);

							} else if ("STI_DATE".equals(headerName)) {

								log.debug("Inside else if stiDate : {}", stringValue);

								Date date = convertToDate(stringValue, headerName, failureMap);
								if (date != null)
									employeeSalaryStructure.setStiDate(date);
								else {
									break;
								}

							} else if ("TRANSPORTATION_ALLOWANCE".equals(headerName)) {

								log.debug("Inside else if ta : {}", doubleValue);
								if (doubleValue != null)
									employeeSalaryStructure.setTa(doubleValue);
								else {
									failureMap.put(headerName, "either blank or wrong");
									flag = false;
								}

							} else if ("BOARD_MEMBER_AMOUNT".equals(headerName)) {

								log.debug("Inside else if boardMemberAmount : {}", doubleValue);

								employeeSalaryStructure.setBoardMemberAmount(doubleValue);
								;

							} else {

								failureMap.put(headerName, "Either Header_Name mismatch or wrong value inserted "
										+ stringValue + " " + doubleValue);

							}

						}

						if (flag == true) {
							list.add(employeeSalaryStructureRepository.save(employeeSalaryStructure));
							cellCreated.setCellValue("Success");
							log.debug("EmployeeSalaryStructure created : {}", e1);
						}

						else {
							cellCreated.setCellValue("Failure : " + failureMap);
							log.error("unable to save data due to blank employeeId or Travel allowance");
						}

					}

				}

//				workbook.close();

			} catch (Exception e) {
				String s1 = "" + e.getMessage();
				log.error("Unable to read/create sheet : {}", s1);
				sheet1.createRow(0).createCell(0).setCellValue("Unable to read Excel File : " + s1);

			}
			log.info("Completed importEmployeeSalaryStructureData runAsync");
			try {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				wb.write(byteArrayOutputStream);

				log.info("sendExcelMial method called");
				sendExcelMail(byteArrayOutputStream, excelFile);

				wb.close();

			} catch (Exception e) {
				log.error(" @importEmployeeSalaryStructureData Unable to convert to byteArrayOutputStream",
						e.getMessage());
			}

		} catch (Exception e) {
			log.error(" @importEmployeeSalaryStructureData Unable to read/create excel file", e.getMessage());
		}

		return list;
	}

	private List<String> getHeaderList(Row row, Integer columnCount) {
		log.info("Inside @class EmployeeSalaryStructureService @method getHeaderList");

		List<String> headerList = new ArrayList<>();

		for (int i = 0; i < columnCount; i++) {
			headerList.add(row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue());
		}
		log.debug("headerList : {}", headerList);
		return headerList;

	}

	Employee findEmployee(Row row, List<String> headerList, Cell cellCreated) {

		log.info("Inside @class EmployeeSalaryStructureService @method findEmployee");
		try {

			for (int i = 0; i < headerList.size(); i++) {
				String headerName = headerList.get(i);

				setStringAndDoubleValue(row, i, headerName);

				if ("EMPLOYEE_ID".equals(headerName) && doubleValue != null) {
					cellCreated.setCellValue(doubleValue.intValue());
					Employee e1 = employeeService.findByEmployeeId("" + doubleValue.intValue());
					log.debug("Employee found e1 = {}", e1);
					return e1;
				}

			}
		} catch (Exception e) {
			log.error("findEmployee Exception {}", e.getMessage());
		}
		return null;
	}

	private void setStringAndDoubleValue(Row row, Integer columnIndex, String headerName) {
		try {
			log.debug(" Inside @setStringAndDoubleValue :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
			Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
			log.debug("@method setStringAndDoubleValue getCell cell value is : {}", cell);

			if (cell == null) {
				log.debug("@method setStringAndDoubleValue getCell cell value is null", cell);

			} else {

				switch (cell.getCellType()) {
				case STRING:
					log.debug("@method setStringAndDoubleValue Inside Switch STRING headerName{} : {} ", headerName,
							doubleValue);
					stringValue = cell.getStringCellValue().trim();
					break;
				case NUMERIC:

					if (DateUtil.isCellDateFormatted(cell)) {

						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						stringValue = dateFormat.format(cell.getDateCellValue());
						log.debug("@method setStringAndDoubleValue Inside Switch NUMERIC headerName{} : {} ",
								headerName, stringValue);
					} else {

						doubleValue = cell.getNumericCellValue();
						log.debug("@method setStringAndDoubleValue Inside Switch NUMERIC headerName{} : {} ",
								headerName, doubleValue);
					}
					break;
				case BLANK:
					failureMap.put(headerName, "Blank Cell found ");
					log.debug("@method setStringAndDoubleValue Inside Switch BLANK headerName{} : {} ", headerName,
							doubleValue);
					break;
				default:
					log.debug("@method setStringAndDoubleValue Inside Switch DEFAULT headerName{} : {} ", headerName,
							doubleValue);
					failureMap.put(headerName, "CellType not found ");

				}
			}
		} catch (Exception e) {
			log.error("setStringAndDoubleValue Exception {}", e.getMessage());
		}
	}

	private Date convertToDate(String dateStr, String headerName, Map<String, String> failureMap) {
		log.info("Inside @class EmployeeSalaryStructureService @method convertToDate");
		if (dateStr == null || dateStr.isEmpty()) {
			failureMap.put(headerName, "date cannot be blank or : " + dateStr);
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			failureMap.put(headerName, "Wrong date format : " + dateStr);
			return null;
			// throw new BusinessException("Invalid date format: " + dateStr);
		}
	}

	private JSONObject createCompletionNotificationData() {
		log.info("Inside @class EmployeeSalaryStructureService @method createCompletionNotificationData");
		log.debug(" Inside @createCompletionNotificationData :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
		JSONObject object = new JSONObject();
		User contextUser = getUserContext();

		if (contextUser != null && contextUser.getFullName() != null) {
			log.debug("FullName of recipient is : {}", contextUser.getFullName());
			object.put("recipient", contextUser.getFullName());
		}

		return object;
	}

	private void sendExcelMail(ByteArrayOutputStream byteArrayOutputStream, MultipartFile excelFile) {
		try {
			log.debug(" Inside @sendExcelMail :{}  customerId is : {}",ENTITY_NAME, commonUtils.getCustomerId());
			// ByteArrayOutputStream to ByteArrayInputStream
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

			// Create MultipartFile using MockMultipartFile
			MultipartFile multipartFile = new MockMultipartFile("EmployeeSalaryStructureData.xlsx",
					byteArrayInputStream);

			log.info("Going to upload file in document");
			Integer documentId = documentIntegrationService.uploadProtectedAndUnProtectedFileOnDocument(
					"EmployeeSalaryStructureSuccessRate.xlsx", multipartFile);
			log.debug("EmployeeSalaryStructureSuccesRate Document id is : {}", documentId);

			User contextUser = getUserContext();

			JSONObject object = createCompletionNotificationData();
			if (documentId != null && contextUser != null && contextUser.getEmail() != null) {
				String attachmentFilePath = ConfigUtils.getPlainString("ATTACHMENT_FILE_PATH") + documentId;
				List<NotificationAttachment> notificationAttachmentList = new ArrayList<>();
				NotificationAttachment notificationAttachment = new NotificationAttachment();
				notificationAttachment.setIsPublic(true);
				notificationAttachment.setPath(attachmentFilePath);
				notificationAttachment.setName("EmployeeSalaryStructureResponse.xlsx");
				notificationAttachmentList.add(notificationAttachment);
				log.debug("Notification Attachment List is : {}", notificationAttachmentList);
				NotificationTemplate template = notificationIntegration.getTemplte("Employee_Salary_Structure_Import");
				notificationIntegration.sendEmail(template, object, contextUser.getEmail(), null,
						notificationAttachmentList);

			}
		} catch (Exception e) {
			log.error(" @sendExcelMail Unable to convert .xls file to multipart file", e.getMessage());
		}

	}

	private void uploadInputFileInDocuments(MultipartFile excelFile)
			throws IOException, JsonProcessingException, JsonMappingException {
		Integer documentId1 = documentIntegrationService
				.uploadProtectedAndUnProtectedFileOnDocument("EmployeeSalaryStructureInputFile.xlsx", excelFile);
		log.debug("EmployeeSalaryStructureUploadedByUser Document id is : {}", documentId1);
	}

}
