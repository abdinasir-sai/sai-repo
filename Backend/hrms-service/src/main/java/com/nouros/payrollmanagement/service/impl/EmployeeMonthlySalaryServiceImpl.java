package com.nouros.payrollmanagement.service.impl;

import java.beans.IntrospectionException;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.io.excel.Excel;
import com.enttribe.commons.io.excel.ExcelRow;
import com.enttribe.commons.io.excel.ExcelWriter;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.product.storagesystem.rest.StorageRest;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.CheckService;
import com.nouros.hrms.service.EmployeeNationalIdentificationService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.util.report.ExcelUtils;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.repository.EmployeeMonthlySalaryRepository;
import com.nouros.payrollmanagement.repository.PayrollRunRepository;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryService;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.service.PayrollRunService;
import com.nouros.payrollmanagement.utils.CalculationUtils;
import com.nouros.payrollmanagement.utils.PRConstant;
import com.nouros.payrollmanagement.wrapper.PayrollTotals;

import jakarta.persistence.EntityNotFoundException;



/**
 * This is a class named "EmployeeMonthlySalaryServiceImpl" which is located in the package " com.enttribe.payrollmanagement.service.impl", It appears to be an implementation of the "EmployeeMonthlySalaryService" interface and it extends the "AbstractService" class, which seems to be a generic class for handling CRUD operations for entities. This class is annotated with @Service, indicating that it is a Spring Service bean.
This class is using Lombok's @Slf4j annotation which will automatically generate an Slf4j based logger instance, so it is using the Slf4j API for logging.
The class has a constructor which takes a single parameter of GenericRepository EmployeeMonthlySalary and is used to call the superclass's constructor.
This class have one public method public byte[] export(List of EmployeeMonthlySalary  EmployeeMonthlySalary) for exporting the EmployeeMonthlySalary data into excel file by reading the template and mapping the EmployeeMonthlySalary details into it.
It's using Apache POI library for reading and writing excel files, and has methods for parsing the json files for column names and identities , and it also used 'ExcelUtils' for handling the excel operations.
It also uses 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and 'APIConstants' from 'com.enttribe.payrollmanagement.util'
* */










@Service
public class EmployeeMonthlySalaryServiceImpl extends AbstractService<Integer,EmployeeMonthlySalary> implements EmployeeMonthlySalaryService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for EmployeeMonthlySalary entities.
	 */
	
	@Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
	private String rootDirBucketName;
	 
   private static final Logger log = LogManager.getLogger(EmployeeMonthlySalaryServiceImpl.class);

	 private static final String ENTITY_NAME = "EmployeeMonthlySalary"; 
	 private static final String PAYROLLRUN_NOT_FOUND =" PayrollRun not found for id";
	 private static final String INSIDE_CLASS_LOG = "Inside @Class EmployeeMonthlySalaryServiceImpl @Method";
	 private static final String ATTACHMENT = "attachment";
	 
	public EmployeeMonthlySalaryServiceImpl(GenericRepository<EmployeeMonthlySalary> repository) {
		super(repository,EmployeeMonthlySalary.class);
	}
	@Autowired
	private EmployeeMonthlySalaryRepository employeeMonthlySalaryRepository;
	
	@Autowired
	private PayrollRunRepository payrollRunRepository;
	
	@Autowired
	private StorageRest storageRest;
	

	@Autowired
	CustomerInfo customerInfo;
	
	@Autowired
	private UserRest userRest;
	
	@Autowired
	  private CommonUtils commonUtils;
	

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	@Autowired
	private HrmsSystemConfigService hrmsSystemConfigService;
	
	@Autowired
	private AccountDetailsService accountDetailsService;

  @Autowired
  private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;
  
 


	
	/**
     * This method is used to export the given list of EmployeeMonthlySalary objects into an excel file.
     * It reads an excel template 'EmployeeMonthlySalary.xlsx' from the resource folder 'templates/reports'
     * and maps the EmployeeMonthlySalary data onto the template and returns the generated excel file in the form of a byte array.
     * param EmployeeMonthlySalary - List of EmployeeMonthlySalary objects to be exported
     * @return byte[] - The generated excel file in the form of a byte array
     * @throws IOException - When the template file is not found or there is an error reading the file
     */
	@Override
    public byte[] export(List<EmployeeMonthlySalary> employeeMonthlySalary) throws IOException {
   try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("templates/reports/EmployeeMonthlySalary.xlsx");
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resourceAsStream)) {
           int rowCount = 1;
           return setTableData(employeeMonthlySalary, xssfWorkbook, rowCount);
       }
           
  }
	
  
    /**
 * This method is responsible for setting the data of an Excel document, using a template and a list of EmployeeMonthlySalary objects.
 * The data is written to the template starting at the specified row number.
 * 
 * @param EmployeeMonthlySalary a List of EmployeeMonthlySalary objects, representing the data that will be written to the Excel document
 * @param templatePath an XSSFWorkbook object, representing the template Excel document that the data will be written to
 * @param rowCount an int, representing the starting row number where data will be written in the Excel document
 * @return a byte array of the Excel document after the data has been written to it.
 * @throws IOException if there is an issue reading or writing to the Excel document
 */

  /**This method appears to take in three parameters:

A List of EmployeeMonthlySalary objects, representing the data that will be written to the Excel document.
An XSSFWorkbook object, representing the template Excel document that the data will be written to.
An int, representing the starting row number where data will be written in the Excel document.
The method has a return type of byte array, which is the Excel document after the data has been written to it. The method also throws an IOException, which would be thrown if there is an issue reading or writing to the Excel document.

The method starts by creating some maps to hold data that will be used later:

tableColumn: a map that will hold the columns of the Excel table.
identityColumnMapping: a map that will hold the mapping of columns
templateHeaders: a map that will hold the headers of the excel template
then it calls ExcelUtils.parseMappeddJson(tableColumn,identityColumnMapping,templateHeaders); to get the values for the maps created.

Then it creates an instance of ExcelWriter which will write the data to the workbook, it set the active sheet to the first sheet of the workbook and check if EmployeeMonthlySalary list is not empty.

It then iterates over the list of EmployeeMonthlySalary objects and for each EmployeeMonthlySalary, it creates a new row in the Excel document at the specified row number.

It also retrieves the list of columns for the "EmployeeMonthlySalary" entity from the tableColumn map, and iterates over the columns.

For each column, it attempts to retrieve the value for that column from the current EmployeeMonthlySalary object using the ExcelUtils.invokeGetter method, passing in the current EmployeeMonthlySalary object, the column name and the identityColumnMapping.

The value returned by this method is then set as the value of the cell in the current row and column.
If an introspection exception occur it will print the stacktrace of the exception

After all the data is written to the Excel document, the method returns the Excel document as a byte array using excelWriter.toByteArray() and log "going to return file"
* */
   private byte[] setTableData(List<EmployeeMonthlySalary>employeeMonthlySalary,XSSFWorkbook templatePath,int rowCount)throws IOException
  {
	   log.debug(" Inside @setTableData :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    Map<String,List<String>> tableColumn=new HashMap<>();
      String entity=ENTITY_NAME;
        Map<String, String> identityColumnMapping =new HashMap<>();
        Map<String,List<String>> templateHeaders = new HashMap<>();
      ExcelUtils.parseMappeddJson(tableColumn,identityColumnMapping,templateHeaders);
      log.info("table column map is :{}",tableColumn);
      try (ExcelWriter excelWriter = new ExcelWriter(templatePath)) {
        excelWriter.getWorkbook().setActiveSheet(0);
        if (CollectionUtils.isNotEmpty(employeeMonthlySalary)) {
          for (EmployeeMonthlySalary employeeMonthlySalaryDetails : employeeMonthlySalary) {
            ExcelRow row = excelWriter.getOrCreateRow(0, rowCount);
            int index = 0;
            List<String> columns = tableColumn.get(entity);
            for(String column:columns) {
              if(column!=null) {   
              try {
              row.setCellValue(index, ExcelUtils.invokeGetter(employeeMonthlySalaryDetails,column,identityColumnMapping).toString());
             }
        catch (IntrospectionException e) {
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
 * This method is responsible for importing data from an Excel file, specifically data related to EmployeeMonthlySalary objects.
 * The method takes in a MultipartFile object, which represents the Excel file containing the data.
 * The method then validates the template headers in the Excel file and if they are valid, it saves the data to the database.
 *
 * @param excelFile a MultipartFile object representing the Excel file containing the data
 * @return a string indicating whether the data import was successful or not.
 * @throws IOException if there is an issue reading from the Excel file
 * @throws InstantiationException when there is issue with instantiation
 * @throws ClassNotFoundException when the class not found
 */
@Override
  public String importData(MultipartFile excelFile) throws IOException , InstantiationException, ClassNotFoundException {
	   log.debug(" Inside @importData :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    List<EmployeeMonthlySalary> employeeMonthlySalarys =new ArrayList<>();
    Excel workBook = new Excel(excelFile.getInputStream());
      Map<String, List<String>> tableColumn = new HashMap<>(); // Table Name and list of Columns
      Map<String, String> columnsMapping = new HashMap<>(); // Json Mapping DispalyName and Name
       Map<String, List<String>> templateHeadres = new HashMap<>();
          List<String> displayNames = new ArrayList<>();
      ExcelUtils.parseMappeddJson(tableColumn, columnsMapping,templateHeadres);
          displayNames.addAll(templateHeadres.get(ENTITY_NAME));
      List<String> columnNames = new ArrayList<>();
         columnNames.addAll(tableColumn.get(ENTITY_NAME));
      boolean validateTableTemplateHeader =
          ExcelUtils.validateTableTemplateHeader(workBook, displayNames);// Validating Columns and Headers
           if (validateTableTemplateHeader) {
        log.info("columns and headers are validated");
        employeeMonthlySalarys = saveData(workBook, columnsMapping, columnNames);
      }
    else {
      log.info("columns and headers invalide");
      }
    if (CollectionUtils.isNotEmpty(employeeMonthlySalarys)) {
      //employeeMonthlySalaryRepository.saveAll(runDetailss);
    	employeeMonthlySalaryRepository.saveAll(null);
    	
      return APIConstants.SUCCESS_JSON;
    }
    return APIConstants.FAILURE_JSON;
  }

/**
 * This method is responsible for saving data to the database, specifically data related to EmployeeMonthlySalary objects.
 * The method takes in an Excel object, which represents the sheet containing the data, a mapping of columns from the excel sheet to the EmployeeMonthlySalary class, and a list of column names.
 * The method uses the iterator for the sheet to read data row by row, create new EmployeeMonthlySalary objects, and set the properties of the EmployeeMonthlySalary objects using the column mapping and column names.
 * The method returns a list of EmployeeMonthlySalary objects that have been saved to the database.
 *
 * @param sheet an Excel object representing the sheet containing the data
 * @param columnMapping a map representing the mapping of columns from the excel sheet to the EmployeeMonthlySalary class
 * @param columnNames a list of column names of the excel sheet
 * @return a list of EmployeeMonthlySalary objects that have been saved to the database
 */

    public List<EmployeeMonthlySalary> saveData(Excel sheet, Map<String, String> columnMapping,
      List<String> columnNames) {
    	log.debug(" Inside @saveData :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    Iterator<ExcelRow> rowIterator = sheet.iterator();
    List<EmployeeMonthlySalary> employeeMonthlySalarys = new ArrayList<>();
    rowIterator.next();
    while (rowIterator.hasNext()) {
      ExcelRow excelRow = rowIterator.next();
          EmployeeMonthlySalary employeeMonthlySalary = new EmployeeMonthlySalary();
              int index = -1;
      for (String columnName : columnNames) {
        try {
          ExcelUtils.invokeSetter(employeeMonthlySalary, columnName, excelRow.getString(++index));
                  } catch (InstantiationException e) {
          log.error("failed while going to set the value :{}", excelRow.getString(++index));
          log.error("InstantiationException occurred: {}", e.getMessage());
          
        } catch (ClassNotFoundException e) {
          log.error("ClassNotFoundException occurred: {}", e.getMessage());
        }
      }
	    employeeMonthlySalarys.add(employeeMonthlySalary);
  		}
    return employeeMonthlySalarys;
  }
  
	
	
		/**
* This method is responsible for soft-deleting an EmployeeMonthlySalary  record in the database.
* The method takes in an int id which represents the id of the EmployeeMonthlySalary  that needs to be soft-deleted.
* It uses the id to find the EmployeeMonthlySalary by calling the EmployeeMonthlySalaryRepository.findById method.
* If the EmployeeMonthlySalary  is found, it sets the "deleted" field to true, save the EmployeeMonthlySalary  in the repository, and saves it in the database
*
* @param id an int representing the id of the EmployeeMonthlySalary  that needs to be soft-deleted
*/
	@Override
	public void softDelete(int id) {
		log.debug(" Inside @softDelete :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		EmployeeMonthlySalary employeeMonthlySalary = super.findById(id);
		try
		{
		Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
		String listofFullTimeEmployee = hrmsSystemConfigMap.get(PRConstant.FULL_TIME_EMPLOYEE_EMPLOYEMENT_TYPE_LIST);
		String onboardMemberTypeList = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);

		if (employeeMonthlySalary != null) {

			EmployeeMonthlySalary employeeMonthlySalary1 = employeeMonthlySalary;
			log.info("Inside softDelete runDetails id:{}",employeeMonthlySalary1.getId());
			PayrollRun payrollRun=employeeMonthlySalary1.getPayrollRun();
			Employee employee=employeeMonthlySalary1.getEmployee();
			String employmentType = employee.getEmploymentType();
			CalculationUtils calculationUtils = ApplicationContextProvider.getApplicationContext().getBean(CalculationUtils.class);
			boolean check = calculationUtils.checkIfExists(employmentType, listofFullTimeEmployee);
			boolean checkOnBoardMember = calculationUtils.checkIfExists(employmentType, onboardMemberTypeList); 
			log.info("EmploymentType check:{}",check);
			
			
			if (listofFullTimeEmployee != null && !listofFullTimeEmployee.isEmpty() && check)
			{
				log.info("fullTimeEmployee calculating payroll update");
				Double netAmount=employeeMonthlySalary1.getNetAmount();
				
				Double sti=employeeMonthlySalary1.getSti();
				Double totalSti=payrollRun.getTotalSti();
				totalSti-=sti;
				
				Double reallocationAllowance=employeeMonthlySalary1.getRelocationAllowance();
			    Double signUpBonus=employeeMonthlySalary1.getSignUpBonus();
			    Double bonus=signUpBonus+sti;
			    Double totalBonus=payrollRun.getTotalBonus();
			    totalBonus-=bonus;
			    
				
				Double otherEarning=employeeMonthlySalary1.getOtherEarning();
				// Double totalOtherEarning=payrollRun.getTotalOtherEarning();
				// totalOtherEarning-=otherEarning;
			
				
				Double basicSalary= employeeMonthlySalary1.getBasicSalary();
				Double hra= employeeMonthlySalary1.getHra();
				Double overtime= employeeMonthlySalary1.getOvertime();
				Double overbase= employeeMonthlySalary1.getOverbase();
				Double criticalSkills= employeeMonthlySalary1.getCriticalSkills();				
				Double totalDeduction=employeeMonthlySalary1.getTotalDeductionAmount();
				Double totalOtherDeduction=payrollRun.getTotalOtherDeduction();
				totalOtherDeduction-=totalDeduction;
				
				Double totalBasicSalary=payrollRun.getTotalBasicSalary();
				totalBasicSalary-=basicSalary;
				
				Double totalAllowances=payrollRun.getTotalAllowances();
				Double mobileAllowance= employeeMonthlySalary1.getMobileAllowance();
				Double tA= employeeMonthlySalary1.getTa();
				Double allowances = hra+mobileAllowance+tA+reallocationAllowance;
				totalAllowances-=allowances;
				
				Double totalAdditionalEarnings=payrollRun.getTotalOtherEarning();
				Double additionalEarning= otherEarning+sti+signUpBonus;
				totalAdditionalEarnings-=additionalEarning;
				
				Double totalGrossSalary=payrollRun.getTotalGrossSalary();
				Double grossSalary= basicSalary+allowances+additionalEarning+overtime+overbase+criticalSkills;
				totalGrossSalary-=grossSalary;
				
				Double totalNetSalary=payrollRun.getTotalNetSalary();
				Double netSalary= grossSalary-totalDeduction;
				totalNetSalary-=netSalary;
				
				
				Double totalLastMonthSalary=payrollRun.getTotalLastMonthSalary();
				Double diffvariance = totalNetSalary -  totalLastMonthSalary;
	            Double variancePct = 0.0;
	            if (diffvariance != 0.0 && totalNetSalary != 0.0 && totalLastMonthSalary != 0.0) {
	                variancePct = (diffvariance / totalLastMonthSalary) * 100;
	            }
	            
	            log.debug("Inside softDelete totalBasicsalary:{},totalAllowances:{},totalAdditionalEarnings:{},totalGrossSalary:{},"
	            		+ "totalOtherDeduction:{}, totalNetSalary:{}, variance:{}",totalBasicSalary,totalAllowances,totalAdditionalEarnings,
	            		totalGrossSalary,totalOtherDeduction, totalNetSalary,variancePct);
	            
	            payrollRun.setPayrollVariance(variancePct);
				
				payrollRun.setTotalBonus(totalBonus);
				payrollRun.setTotalSti(totalSti);
				
				payrollRun.setTotalBasicSalary(totalBasicSalary);
				payrollRun.setTotalAllowances(totalAllowances);
				payrollRun.setTotalOtherEarning(totalAdditionalEarnings);
				payrollRun.setTotalGrossSalary(totalGrossSalary);
				payrollRun.setTotalOtherDeduction(totalOtherDeduction);
				payrollRun.setTotalNetSalary(totalNetSalary);
				
				
			}
			payrollRunRepository.save(payrollRun);
             employeeMonthlySalary1.setDeleted(true);
		    employeeMonthlySalaryRepository.save(employeeMonthlySalary1);
		}
		
		}
		catch(Exception ex)
		{
			log.error("Inside softDelete  : {}", Utils.getStackTrace(ex), ex);
		}
		
		
		
		
	}
	
		/**
* This method is responsible for soft-deleting multiple EmployeeMonthlySalary records in the database in bulk.
* The method takes in a List of integers, each representing the id of an EmployeeMonthlySalary that needs to be soft-deleted.
* It iterates through the list, calling the softDelete method for each id passed in the list.
*
* @param list a List of integers representing the ids of the EmployeeMonthlySalary that need to be soft-deleted
*/
	@Override
	public void softBulkDelete(List<Integer> list) {
		log.debug(" Inside @softBulkDelete :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		log.debug(INSIDE_CLASS_LOG + " softBulkDelete list :{} ", list);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}
	
	
	
 /**
 * @param employeeMonthlySalary The runDetails  object to create.
 * @return The created vendor object.
 */
	@Override
	public EmployeeMonthlySalary create(EmployeeMonthlySalary employeeMonthlySalary)
	{	
			return employeeMonthlySalaryRepository.save(employeeMonthlySalary);
	}


@Override
public BigDecimal getRecentNetworthForEmployee(Integer employeeId) {
	 log.info("inside rundetailserviceimpl getRecentNetworthForEmployee employeeId:{}",employeeId);
	 log.debug(" Inside @getRecentNetworthForEmployee :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	 BigDecimal netAmount = employeeMonthlySalaryRepository.getRecentNetForEmployee(employeeId,commonUtils.getCustomerId());
	 
		 return (BigDecimal) netAmount;
}


@Override
public List<EmployeeMonthlySalary> getEmployeeMonthlySalaryByPayrollId(Integer payrollId) {
	List<EmployeeMonthlySalary> employeeMonthlySalary=employeeMonthlySalaryRepository.employeeMonthlySalaryByPayrollId(payrollId);
	log.info("list size {}",employeeMonthlySalary.size());
	log.info("list  {}",employeeMonthlySalary);
	return employeeMonthlySalary;
}




public byte[] generateExcelReport(List<EmployeeMonthlySalary> employeeMonthlySalaryList) {
    if (employeeMonthlySalaryList == null) {
        log.error("Run details list is null");
        throw new IllegalArgumentException("Run details list cannot be null");
    }
	 log.debug(" Inside @generateExcelReport :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());

    try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        XSSFSheet sheet = workbook.createSheet("Run Details");
        log.debug("Inside generateExcelReport");

        createHeaderRow(sheet);

        int rowCount = 1;
        for (EmployeeMonthlySalary employeeMonthlySalary : employeeMonthlySalaryList) {
            if (employeeMonthlySalary == null || employeeMonthlySalary.getEmployee() == null) {
                log.info("Run details or Employee is null, skipping...");
                continue;
            }
            createDataRow(sheet, employeeMonthlySalary, rowCount++);
        }

        log.debug("Values inserted in Excel");
        workbook.write(bos);
        return bos.toByteArray();
    } catch (IOException e) {
        log.error("Error while generating Excel report", e);
        log.error("Error occurred during Excel report generation: {}", e.getMessage());
        return new byte[0];
    }
}


private void createHeaderRow(XSSFSheet sheet) {
    Row headerRow = sheet.createRow(0);
    String[] headers = {
        "Employee Number", "Full Name", "Nationality", "National ID", "Hire Date", "Department", "Job", "Position", "Grade",
         "Basic Salary","Over base", "Critical Talent", "Housing Allowance", "Transportation Allowance", "Mobile Allowance",
        "Signing Bonus", "Over Time", "Relocation Package","Other Earning","Other Deductions", "Short Term Incentive" ,"GOSI Employee","GOSI Employer",
        "Total Earnings", "Total Deductions", "Last Month Total Net", "Total Net", "Working Days","Variance","Variance Amount" ,"Variance Reason","Remark"
    };
    for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
    }
}

private void createDataRow(XSSFSheet sheet, EmployeeMonthlySalary employeeMonthlySalary, int rowCount) {
	 log.debug(" Inside @createDataRow :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    Employee employee = employeeMonthlySalary.getEmployee();
    Department department = employee.getDepartment();
    Designation designation = employee.getDesignation();
  
    Row row = sheet.createRow(rowCount);
    row.createCell(0).setCellValue(defaultIfNull(employee.getEmployeeId(), ""));
    row.createCell(1).setCellValue(defaultIfNull(employee.getFullName(), ""));
    row.createCell(2).setCellValue(defaultIfNull(employee.getCitizenship(), ""));
    row.createCell(3).setCellValue("-");
    row.createCell(4).setCellValue(defaultIfNull(employee.getDateOfJoining(), ""));
    row.createCell(5).setCellValue(defaultIfNull(department != null ? department.getName() : null, ""));
    row.createCell(6).setCellValue("-");
    row.createCell(7).setCellValue(defaultIfNull(designation != null ? designation.getName() : null, ""));
    row.createCell(8).setCellValue(defaultIfNull(employee.getJobGrade(), 0));
    row.createCell(9).setCellValue(defaultIfNull(employeeMonthlySalary.getBasicSalary(), 0));
    row.createCell(10).setCellValue(defaultIfNull(employeeMonthlySalary.getOverbase(), 0));
    row.createCell(11).setCellValue(defaultIfNull(employeeMonthlySalary.getCriticalSkills(), 0));
    row.createCell(12).setCellValue(defaultIfNull(employeeMonthlySalary.getHra(), 0));
    row.createCell(13).setCellValue(defaultIfNull(employeeMonthlySalary.getTa(), 0));
    row.createCell(14).setCellValue(defaultIfNull(employeeMonthlySalary.getMobileAllowance(), 0));
    row.createCell(15).setCellValue(defaultIfNull(employeeMonthlySalary.getSignUpBonus(), 0));
    row.createCell(16).setCellValue(defaultIfNull(employeeMonthlySalary.getOvertime(), 0));
    row.createCell(17).setCellValue(defaultIfNull(employeeMonthlySalary.getRelocationAllowance(), 0));
    row.createCell(18).setCellValue(defaultIfNull(employeeMonthlySalary.getOtherEarning(), 0));
    row.createCell(19).setCellValue(defaultIfNull(employeeMonthlySalary.getOtherDeduction(), 0));  
    row.createCell(20).setCellValue(defaultIfNull(employeeMonthlySalary.getSti(),0));
    row.createCell(21).setCellValue(defaultIfNull(employeeMonthlySalary.getGosiEmployee(), 0));
    row.createCell(22).setCellValue(defaultIfNull(employeeMonthlySalary.getGosiEmployer(), 0));
    row.createCell(23).setCellValue(defaultIfNull(employeeMonthlySalary.getTotalEarningAmount(), 0));
    row.createCell(24).setCellValue(defaultIfNull(employeeMonthlySalary.getTotalDeductionAmount(), 0));
    row.createCell(25).setCellValue(defaultIfNull(employeeMonthlySalary.getLastMonthNetAmount(), 0));
    row.createCell(26).setCellValue(defaultIfNull(employeeMonthlySalary.getNetAmount(), 0));
    row.createCell(27).setCellValue(defaultIfNull(employeeMonthlySalary.getWorkingDays(), 0));
    row.createCell(28).setCellValue(defaultIfNull(employeeMonthlySalary.getVariance(), 0));
    row.createCell(29).setCellValue(defaultIfNull(employeeMonthlySalary.getVarianceAmount(),0));
    row.createCell(30).setCellValue(defaultIfNull(employeeMonthlySalary.getVarianceReason(),""));
    row.createCell(31).setCellValue(defaultIfNull(employeeMonthlySalary.getRemark(),""));     
}

private String defaultIfNull(Object value, String defaultValue) {
    return value != null ? value.toString() : defaultValue;
}

private double defaultIfNull(Number value, double defaultValue) {
    return value != null ? value.doubleValue() : defaultValue;
}



@Override
public ResponseEntity<byte[]> generateExcelForUpload(Integer payrollId) throws IOException {
	log.debug(" Inside @generateExcelForUpload :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    List<EmployeeMonthlySalary> employeeMonthlySalaryList = getEmployeeMonthlySalaryByPayrollId(payrollId);
    byte[] excelBytes = generateExcelReport(employeeMonthlySalaryList);
    PayrollRun payrollRun = payrollRunRepository.findById(payrollId)
            .orElseThrow(() -> new EntityNotFoundException(PAYROLLRUN_NOT_FOUND + payrollId));
    String fileName = payrollRun.getReferenceId() + ".xlsx";
    String filePath = "hrms/hrms/" + fileName;
    InputStream inputStream = new ByteArrayInputStream(excelBytes);
    		
    log.info("Inside Rundetails generateExcelForUpload  rootDirBucketName :{}",rootDirBucketName);
    uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName ); 
        payrollRun.setBasePathForExcel(filePath);
        payrollRun.setExcelFileGenerated(true);
        payrollRunRepository.save(payrollRun);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData(ATTACHMENT, "EmployeeMonthlySalary.xlsx");
    return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
}

@Override
public ResponseEntity<byte[]> generateExcel(Integer payrollId) throws IOException {
	PayrollRun payrollRun = payrollRunRepository.findById(payrollId)
            .orElseThrow(() -> new EntityNotFoundException(PAYROLLRUN_NOT_FOUND + payrollId));
	boolean isGenerated = payrollRun.isExcelFileGenerated();
	if(!isGenerated) {
		return generateExcelForUpload( payrollId);
		
	}
	else {
		return downloadExcel(payrollId);
	}
}

@Async
@Override
public void generateExcelForPayroll(Integer payrollId) throws IOException {
	log.debug(" Inside @generateExcelForPayroll :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	PayrollRun payrollRun = payrollRunRepository.findById(payrollId)
            .orElseThrow(() -> new EntityNotFoundException(PAYROLLRUN_NOT_FOUND + payrollId));
	boolean isGenerated = payrollRun.isExcelFileGenerated();
	if(!isGenerated) {
		 generateExcelForUpload( payrollId);
	}
	else {
		 downloadExcel(payrollId);
    }
}



public ResponseEntity<byte[]> downloadExcel(Integer payrollId) throws IOException {
	log.debug(" Inside @downloadExcel :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    PayrollRun payrollRun = payrollRunRepository.findById(payrollId)
            .orElseThrow(() -> new EntityNotFoundException(PAYROLLRUN_NOT_FOUND + payrollId));

    String filePath = payrollRun.getBasePathForExcel();
    		
    log.info("Inside Rundetails downloadExcel rootDirBucketName :{} ",rootDirBucketName);
    try {
        log.debug("Inside @method: downloadExcel with params @payrollId:: {}", payrollId);
        
        InputStream inputStream = storageRest.read(rootDirBucketName, filePath);
        log.debug("InputStream available bytes: {}", inputStream.available());

        InputStreamResource resource = new InputStreamResource(inputStream);
        byte[] fileData = resource.getContentAsByteArray();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData(ATTACHMENT, "RunDetails_" + payrollId + ".xlsx");

        return new ResponseEntity<>(fileData, responseHeaders, HttpStatus.OK);
    } catch (Exception e) {
        throw new BusinessException("Something went wrong", e);
    }
}


private void uploadFileInStorage(InputStream in, String fileName, String filePath ,  String rootDir)
	      throws IOException {
	    log.debug("Inside uploadFileInStorage :: {}", fileName);
	    log.debug("fileName: {} filePath: {}",fileName, filePath); 
	    try (InputStream inputStream = new ByteArrayInputStream(in.readAllBytes())) {
	    
	  	  storageRest.createFile(inputStream, rootDir, filePath);
	    }
	}


@Override
public Boolean checkForRelocationAllowance(Integer employeeId) {
	log.debug(" Inside @checkForRelocationAllowance :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	 log.debug("Inside EmployeeMonthlySalaryServiceImpl checkForRelocationAllowance :employeeId: {}", employeeId);
	 
	  List<EmployeeMonthlySalary> list = employeeMonthlySalaryRepository.checkEmployeeMonthlySalaryforRellocationAllowance(employeeId);
	  
	  if(list!=null && !list.isEmpty()) {
		  log.debug("Inside EmployeeMonthlySalaryServiceImpl checkForRelocationAllowance list is not empty");
		  return false;
	  }
	return true;
}


@Override
public Boolean checkForSignUpBonus(Integer employeeId) {
	log.debug(" Inside @checkForSignUpBonus :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	 log.debug("Inside EmployeeMonthlySalaryServiceImpl checkForRelocationAllowance :employeeId: {}", employeeId);
	
	 
	  List<EmployeeMonthlySalary> list =  employeeMonthlySalaryRepository.checkEmployeeMonthlySalaryforSignUpBonus(employeeId);
	  
	  if(list!=null && !list.isEmpty()) {
		  log.debug("Inside EmployeeMonthlySalaryServiceImpl checkForSignUpBonus list is not empty");
		  return false;
	  }
	return true;
}


@Override
public String getEmployeeMonthlySalary(Integer employeeId) {
	log.debug(" Inside @getEmployeeMonthlySalary :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	log.info("Inside class EmployeeMonthlySalaryServiceImpl method getEmployeeRundetails employeeId :{} ",employeeId);
	JSONArray recordsArray = new JSONArray();
  Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
	String onboardMemberTypeList = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);
	CalculationUtils calculationUtils = ApplicationContextProvider.getApplicationContext().getBean(CalculationUtils.class);
	boolean checkOnBoardMember=false;
	try {
		
	if(employeeId==null) {
		User contextUser = getUserContext();
		log.debug("Inside class EmployeeMonthlySalaryServiceImpl  contextUser user Id is : {}", contextUser.getUserid());
		Integer userId = contextUser.getUserid();
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
		
		Employee employee = employeeService.getEmployeeByUserId(userId);
    if(employee!=null){
		employeeId = employee.getId();
		log.debug("Inside class EmployeeMonthlySalaryServiceImpl  userContextemployeeId: {}", employeeId);
    String employmentType = employee.getEmploymentType();
		 checkOnBoardMember = calculationUtils.checkIfExists(employmentType, onboardMemberTypeList);
		 if(checkOnBoardMember) {
		  log.error("Inside @getEmployeeRundetails For employmentType: {} ",employmentType);
		  throw new BusinessException("Payslips for Board of Directors are not generated. "); 
		 }		
    }	
	}
	String fiql = String.format("deleted==false;id=ge=0;employee.id==%d;(payrollRun.workflowStage==PAYROLL_BANK_APPROVAL,payrollRun.workflowStage==BOARD_MEMBER_BANK_APPROVAL,payrollRun.workflowStage==COMPLETED);payrollRun.deleted==false", employeeId);
	
	log.debug("Inside class EmployeeMonthlySalaryServiceImpl  fiql: {}", fiql);	
	
	
	EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
	
	
	List<EmployeeMonthlySalary> payHistoryRecords = employeeMonthlySalaryService.search(fiql, 0, 2000,"id" ,"asc");


     for (EmployeeMonthlySalary record : payHistoryRecords) {
         JSONObject recordJson = new JSONObject();
         recordJson.put("id", record.getId());
         
         PayrollRun payrollRun= record.getPayrollRun();
         if(payrollRun!=null) {
        	 Date endDate =payrollRun.getEndDate();
        	 recordJson.put("date", endDate); // Assuming the date is in a format that can be converted to string
             recordsArray.put(recordJson);
         }
    
     }
     log.debug("Generated JSON: {}", recordsArray);
     if(recordsArray!=null)
     log.debug("Generated JSON: {}", recordsArray.toString());
     return recordsArray.toString();
	}catch(Exception ex) {
		
		log.error("inside EmployeeMonthlySalaryServiceImpl @method getEmployeeRundetails error :{} ", Utils.getStackTrace(ex), ex);
	}
	return recordsArray.toString();
}







public ResponseEntity<byte[]> createWpsTxtFile(String payrollRunProcessInstanceId) {
	try {
		log.debug(" Inside @createWpsTxTFile :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		log.info(INSIDE_CLASS_LOG+"createWpsTxtFile");
		if(payrollRunProcessInstanceId!=null) {
		Optional <PayrollRun> payrollRun = payrollRunRepository.findPayrollRunByProcessInstanceId(payrollRunProcessInstanceId);
		if(!payrollRun.isEmpty()) {
		PayrollRun payrollRunObject = payrollRun.get();
		Integer payrollId = payrollRunObject.getId();
		log.debug(INSIDE_CLASS_LOG+"createWpsTxtFile payrollRunId :{}",payrollId);
    if(payrollRunObject.getBasePathForWPS()==null && ((payrollRunObject.getWorkflowStage().equalsIgnoreCase("APPROVED_BY_FINANCE_CONTROLLER") || payrollRunObject.getWorkflowStage().equalsIgnoreCase("PAYROLL_BANK_APPROVAL"))) ){
		List<String[]> data = fetchData(payrollId,true);
		ByteArrayOutputStream out = generateTxt(data);
		byte[] txtBytes = out.toByteArray();
		String fileReference = getSystemTimeWithTimeStamp();
		String fileName = "Wps" + fileReference+ ".txt";
		String filePath = "hrmswps/" + fileName;
		
				
		
		log.info("Inside Rundetails createWpsTxtFile rootDirBucketName :{}" , rootDirBucketName);
		
		InputStream inputStream = new ByteArrayInputStream(txtBytes);
		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
      payrollRunObject.setBasePathForWPS(filePath);
      payrollRunRepository.save(payrollRunObject);
//      CheckService checkService = ApplicationContextProvider.getApplicationContext().getBean(CheckService.class);
//      checkService.checkBODInRunDetail(payrollRunProcessInstanceId);
      HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(ATTACHMENT, fileName);
        return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);
    }else{
      return downloadWpsFile(payrollRunObject);
    }
    }
		}
		return null;	
	} catch (Exception e) {
		log.error(INSIDE_CLASS_LOG+"createWpsTxtFile exception occurs :{}",e.getMessage());
		log.error(INSIDE_CLASS_LOG+"  createWpsTxtFile method ex : {}", Utils.getStackTrace(e), e);
		throw new BusinessException("Exception occurs inside fetchData");
		
	}	
}

public ResponseEntity<byte[]> downloadWpsFile(PayrollRun payrollRunObject) throws IOException {
	log.debug(" Inside @downloadWpsFile :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    String filePath = payrollRunObject.getBasePathForWPS();
    
    		
    log.info("Inside Rundetails downloadWpsFile rootDirBucketName :{} ",rootDirBucketName);
	
   if(payrollRunObject.getBasePathForWPS()!=null)
   {
	   String currentTime = getSystemTimeWithTimeStamp();
	   try {
	       log.debug("Inside @method: downloadWpsFile with params @payrollRunObject:: {}", payrollRunObject);
	       InputStream inputStream = storageRest.read(rootDirBucketName, filePath);
	       log.debug("InputStream available bytes: {}", inputStream.available());
	       InputStreamResource resource = new InputStreamResource(inputStream);
	       byte[] fileData = resource.getContentAsByteArray();
	       HttpHeaders responseHeaders = new HttpHeaders();
	       responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	       responseHeaders.setContentDispositionFormData(ATTACHMENT, "Wps" + currentTime + ".txt");
	       return new ResponseEntity<>(fileData, responseHeaders, HttpStatus.OK);
	     }
	    catch (Exception e) {
	       throw new BusinessException("Something went wrong", e);
	   }
   }
   else
   {
	   return null;
   }
}

 
public  List<String[]> fetchData(Integer payrollRunId,Boolean forGeneralEmployee) {
	List<String[]> data = null;
	try {
		log.debug(" Inside @fetchData :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		log.info(INSIDE_CLASS_LOG+"fetchData");
	     Map<String,String> mapHrmsSystemConfigMap  =    hrmsSystemConfigService.getHrmsKeyValue();   
		  String ifh =  mapHrmsSystemConfigMap.get("ifh");
	      String ifile = mapHrmsSystemConfigMap.get("ifile");
	      String csv =  mapHrmsSystemConfigMap.get("csv");
	      String connectId =  mapHrmsSystemConfigMap.get("connectId");
	      String customerId =  mapHrmsSystemConfigMap.get("customerId");
	      String p =  mapHrmsSystemConfigMap.get("p");
	      String one =  mapHrmsSystemConfigMap.get("one");
	      String bathdr =  mapHrmsSystemConfigMap.get("bathdr");
	      String achcr =  mapHrmsSystemConfigMap.get("achcr");
	      String first =  mapHrmsSystemConfigMap.get("@1st@");
	      String sar =  mapHrmsSystemConfigMap.get("sar");
	      String secpty =  mapHrmsSystemConfigMap.get("secpty");
	      String n =  mapHrmsSystemConfigMap.get("n");
	      String sach =  mapHrmsSystemConfigMap.get("@sach@");
	      String companyName =  mapHrmsSystemConfigMap.get("companyName");
	      String paymentPurposeCodeS = mapHrmsSystemConfigMap.get("paymentPurposeCodeS");
	      String paymentNarrationCompany =  mapHrmsSystemConfigMap.get("paymentNarrationCompany");
	      String debitAccountNumber =  mapHrmsSystemConfigMap.get("debitAccountNumber");
	      String molEstablisedId = mapHrmsSystemConfigMap.get("molEstablisedId");
	      String employerId = mapHrmsSystemConfigMap.get("employerId");
		
	      log.debug(INSIDE_CLASS_LOG+"fetchData after all the data fetch from Hrms_System_Config mapHrmsSystemConfigMap :{} ",mapHrmsSystemConfigMap);
	
	    String currentDate = getCurrentDate();
		String currentTime = getCurrentTime();
		String valueDate = getDateAfterDays(2);
		String fileReference = getSystemTimeWithTimeStamp();
		String batchReference = getSystemTimeWithTimeStamp();
		
		 log.info(INSIDE_CLASS_LOG+"fetchData after set date and time");
		 List<EmployeeMonthlySalary> employeeMonthlySalary=null;
		if(forGeneralEmployee)
		{
			Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
			String onboardMemberTypeList = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);
			String[] list = onboardMemberTypeList.split(",");
			log.debug("The array of String is :{} ",list);
			employeeMonthlySalary = employeeMonthlySalaryRepository.employeeMonthlySalaryByPayrollIdForWps(payrollRunId,list);
		}
		else 
		employeeMonthlySalary = employeeMonthlySalaryRepository.employeeMonthlySalaryByPayrollIdForOnBoarded(payrollRunId,PRConstant.BOARD_OF_DIRECTORS);
		  
	
		if(employeeMonthlySalary !=null && !employeeMonthlySalary.isEmpty()) {
		Integer count =  employeeMonthlySalary.size();
		log.debug(INSIDE_CLASS_LOG+"fetch count :{}",count);	
		String totalCount = count.toString();
		log.debug(INSIDE_CLASS_LOG+"fetch totalCount :{}",totalCount);	
		Integer totalRecordCount = Integer.parseInt(totalCount)+2;
		String totalRecordCountString = totalRecordCount.toString();
		String netWorth=null;
	    PayrollRunService payrollRunService =ApplicationContextProvider.getApplicationContext().getBean(PayrollRunService.class);

		PayrollRun result = payrollRunService.findById(payrollRunId);
		if(result != null){
		netWorth = result.getTotalNetSalary().toString();
		log.info(INSIDE_CLASS_LOG+"fetch netWorth :{}",netWorth);	
		}
		
		data = new ArrayList<>();
		data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one, totalRecordCountString});
		data.add(new String[] {bathdr, achcr, totalCount, "", "", "", paymentPurposeCodeS, paymentNarrationCompany, "", first, valueDate, debitAccountNumber, sar, netWorth, "", "", "", "", "", "", companyName, molEstablisedId, employerId, "", "", "", batchReference});
		iterateOnEmployeeMonthlySalary(secpty, n, sach, employeeMonthlySalary, data);
		
		log.info(INSIDE_CLASS_LOG+"fetchData EmployeeMonthlySalary Successfully Iterate");
		}
		return data;
	} catch (Exception e) {
		log.error("Exception occurs"+INSIDE_CLASS_LOG+" fetchData :{}",e.getMessage());
		throw new BusinessException("Exception occurs inside fetchData");
	}
}



private void iterateOnEmployeeMonthlySalary(String secpty, String n, String sach, List<EmployeeMonthlySalary> employeeMonthlySalary,
		List<String[]> data) {
	log.debug(" Inside @iterateOnEmployeeMonthlySalary :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	log.info(INSIDE_CLASS_LOG+" iterateOnRunDetails");
	for (EmployeeMonthlySalary runDetail : employeeMonthlySalary) {
		log.debug(INSIDE_CLASS_LOG+"iterateOnRunDetails runDetail :{}",runDetail);
        Integer employeeId = runDetail.getEmployee().getId();
        String employeeReferenceId = runDetail.getEmployee().getEmployeeId();
        String nationalIdentification = "";
        EmployeeNationalIdentification employeeNationalIdentification =  employeeNationalIdentificationRepository.findNationalIdentificationNumberByEmployeeId(employeeId,commonUtils.getCustomerId());
        if(employeeNationalIdentification!=null){
          nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
        }
        List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employeeId);

        for (AccountDetails accountDetail : accountDetails) {
        	log.debug(INSIDE_CLASS_LOG+"iterateOnRunDetails accountDetail :{}",accountDetail);
            StringBuilder rowBuilder = new StringBuilder();
            rowBuilder.append(secpty).append(",");rowBuilder.append(accountDetail.getIban()).append(",");rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");rowBuilder.append(employeeReferenceId).append(",");
            rowBuilder.append(accountDetail.getBankId()).append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append("");rowBuilder.append(runDetail.getNetAmount()).append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append(n).append(",");rowBuilder.append(n).append(",");
            rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append("").append(",");rowBuilder.append(sach).append(",");rowBuilder.append(nationalIdentification).append(",");rowBuilder.append(runDetail.getBasicSalary()).append(",");rowBuilder.append(runDetail.getHra()).append(",");rowBuilder.append(runDetail.getOtherEarning()).append(",");
            rowBuilder.append(runDetail.getTotalDeductionAmount()) ;  
             
            data.add(rowBuilder.toString().split(","));
            log.debug(INSIDE_CLASS_LOG+" iterateOnRunDetails data :{}",data);
        }
    }
}

private ByteArrayOutputStream generateTxt(List<String[]> data) {
		log.debug(INSIDE_CLASS_LOG + " generateTxt data :{}", data);
		log.debug(" Inside @generateTxt :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
			if(data!=null) {
			for (String[] line : data) {
				writer.write(String.join(",", line));
				writer.newLine();
			}
			log.info(INSIDE_CLASS_LOG + " generateTxt data");
			writer.flush();
		}
		} catch (IOException e) {
			log.error("Error generating TXT content: {}", e.getMessage());
		}
		return out;
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

private String getSystemTimeWithTimeStamp() {
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return now.format(formatter);
}

@Override
public EmployeeMonthlySalary getRecentEmployeeMonthlySalary(Integer employeeId, Integer rundetailsId) {
  log.debug(INSIDE_CLASS_LOG + " getRecentRundetails employeeId :{} rundetailsId :{}", employeeId, rundetailsId);
	log.debug(" Inside @getRecentEmployeeMonthlySalary :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
  try{
    List<Long> idopt = employeeMonthlySalaryRepository.getRecentEmployeeMonthlySalary(employeeId, rundetailsId,commonUtils.getCustomerId());
    if(idopt!=null && !idopt.isEmpty()){
      
    	Long id =  idopt.get(0);
    Integer rdId =   id.intValue();
    log.debug(" Inside @class EmployeeMonthlySalaryServiceImpl @method getRecentEmployeeMonthlySalary id of recent EmployeeMonthlySalary :{} ",
    		rdId);
    EmployeeMonthlySalary runDetailsopt  = super.findById(rdId);
     if(runDetailsopt != null){
       EmployeeMonthlySalary employeeMonthlySalary =runDetailsopt;
       return employeeMonthlySalary;
      }
    }
  }catch(Exception ex){
    log.error("InsideError RunDetailsServiceImplerror getRecentRundetails : {}", Utils.getStackTrace(ex));
  }
  return null;
}


@Override
public String getTop3ReasonByPayrollid(Integer payrollId) {
	log.debug(" Inside @getTop3ReasonByPayrollid :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
  log.debug(INSIDE_CLASS_LOG + " getTop3ReasonByPayrollid payrollId :{} ", payrollId);
  StringBuilder strb = new StringBuilder("");
  try {
  List<Object[]> listObj = employeeMonthlySalaryRepository.getTop3ReasonsByPayrollId(payrollId,commonUtils.getCustomerId());
  boolean flag =false;
  for(Object[] obj :listObj) {
    
  	if(flag)
  	strb.append(",");
    
  	String str = (String) obj[0];
  	
  	Long count = (Long) obj[1];
  	
  	if(str.equalsIgnoreCase(PRConstant.REASON_FOR_REALLOCATION_ALLOWANCE) || str.equalsIgnoreCase(PRConstant.REASON_FOR_DECREASE_REALLOCATION_ALLOWANCE)|| str.equalsIgnoreCase(PRConstant.REASON_FOR_SIGNUP_ALLOWANCE)|| str.equalsIgnoreCase(PRConstant.REASON_FOR_DECREASE_SIGNUP_ALLOWANCE)|| str.equalsIgnoreCase(PRConstant.REASON_FOR_DECREASE_STI_ALLOWANCE)|| str.equalsIgnoreCase(PRConstant.REASON_FOR_STI_ALLOWANCE)) {
  		 log.debug(INSIDE_CLASS_LOG + " getTop3ReasonByPayrollid str :{} ", str);
  		log.debug(INSIDE_CLASS_LOG + " getTop3ReasonByPayrollid count :{} ", count);
  		str = str+" for " +count+" employees ";
  		log.debug(INSIDE_CLASS_LOG + " getTop3ReasonByPayrollid str :{} ", str);
  	}
  	 log.debug(INSIDE_CLASS_LOG + " getTop3ReasonByPayrollid str :{} ", str);
  	strb.append(str);
  	log.debug(INSIDE_CLASS_LOG + " getTop3ReasonByPayrollid strb :{} ", strb.toString());
  	flag=true;
    
  }
  log.debug(INSIDE_CLASS_LOG + " getTop3ReasonByPayrollid stringbuilder :{} ", strb.toString());
   
  }catch(Exception ex) {
    log.error("ERROR"+INSIDE_CLASS_LOG +"getTop3ReasonByPayrollid :{} ",ex.getMessage());
  }
  return strb.toString();
}


@Override
public void softBulkDeleteByPayrollId(List<Integer> listPayrollRunId) {
	log.debug(" Inside @softBulkDeleteByPayrollId :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	log.debug("{} softBulkDeleteByPayrollId list: {}", INSIDE_CLASS_LOG, listPayrollRunId);
    
	 List<Integer> rundetailsIds = new ArrayList<>();
	 try {
	 for(Integer payrollId :listPayrollRunId) {
		 
		 log.debug("{} softBulkDeleteByPayrollId payrollId: {}", INSIDE_CLASS_LOG, payrollId);
	        
		 List<EmployeeMonthlySalary> listOfRundetails =  employeeMonthlySalaryRepository.employeeMonthlySalaryByPayrollId(payrollId);
		 for(EmployeeMonthlySalary employeeMonthlySalary :listOfRundetails) {
			 log.debug("{} softBulkDeleteByPayrollId runDetails: {} id :{} ", INSIDE_CLASS_LOG, employeeMonthlySalary,employeeMonthlySalary.getId());
			 rundetailsIds.add(employeeMonthlySalary.getId()); 
		 } 
		 
	 }
	 softBulkDelete(rundetailsIds);	
	 }catch(Exception ex) {
		 log.error("ERROR"+INSIDE_CLASS_LOG +"softBulkDeleteByPayrollId :{} ",ex.getMessage());
	 }
	 
  }

  @Override
  public ResponseEntity<byte[]> createGOSIReportFile(Integer payrollRunId) {
		log.debug(" Inside @createGOSIReportFile :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    log.info(INSIDE_CLASS_LOG+" createGOSIReportFile");
    log.debug(INSIDE_CLASS_LOG+"createGOSIReportFile payrollRunId :{}", payrollRunId);
    List<EmployeeMonthlySalary> employeeMonthlySalary = employeeMonthlySalaryRepository.employeeMonthlySalaryByPayrollId(payrollRunId);
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Payroll Data");
      String[] headers = getHeaders(sheet);
      setEmployeeAndEmployeeMonthlySalaryInExcelFile(employeeMonthlySalary, sheet, headers);
      log.info(INSIDE_CLASS_LOG+"file data inserted.");
      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        workbook.write(out);
        byte[] bytes = out.toByteArray();
   PayrollRunService payrollRunService =ApplicationContextProvider.getApplicationContext().getBean(PayrollRunService.class);
   PayrollRun payrollRun = payrollRunService.getPayrollById(payrollRunId);     
   LocalDate localDate = convertDateToLocalDate(payrollRun.getEndDate());
        String month = dateFormated();
        String year = String.valueOf(localDate.getYear());
        String fileName = "GOSI_report_"+month+"_"+year+".xlsx";
        log.debug("THe name of GOSI report is :{} ",fileName);
        HttpHeaders headersResp = new HttpHeaders();
        headersResp.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersResp.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, headersResp, HttpStatus.OK);
      } catch (IOException e) {
        log.error("Exception occurs @Method createGOSIReportFile :{} :{}", e.getMessage(), Utils.getStackTrace(e));
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (IOException e) {
      log.error("Exception occurs @Method createGOSIReportFile :{} :{}", e.getMessage(), Utils.getStackTrace(e));
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  private void setEmployeeAndEmployeeMonthlySalaryInExcelFile(List<EmployeeMonthlySalary> employeeMonthlySalary, Sheet sheet, String[] headers) {
    int rowNum = 1;
	log.debug(" Inside @setEmployeeAndEmployeeMonthlySalaryInExcelFile :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    for (EmployeeMonthlySalary runDetail : employeeMonthlySalary) {
      Row row = sheet.createRow(rowNum++);
      Employee employee = runDetail.getEmployee();
      row.createCell(0).setCellValue(employee.getEmployeeId());
      row.createCell(1).setCellValue(employee.getFullName());
      row.createCell(2).setCellValue(employee.getCitizenship());
      if (runDetail.getBasicSalary() != null && runDetail.getHra() != null) {
    	  
        double salaryInGOSI = runDetail.getBasicSalary() + runDetail.getHra();
        if(salaryInGOSI>45000)
        {
        	salaryInGOSI=45000;
        }
   
        row.createCell(3).setCellValue(runDetail.getBasicSalary() + runDetail.getHra());
        row.createCell(4).setCellValue(salaryInGOSI);
        row.createCell(5).setCellValue(runDetail.getBasicSalary() + runDetail.getHra() - salaryInGOSI);
        row.createCell(6).setCellValue(0.02 * salaryInGOSI);
        if(employee.getCitizenship().equalsIgnoreCase(PRConstant.SAUDI))

        	{row.createCell(7).setCellValue(0.09 * salaryInGOSI);
        row.createCell(8).setCellValue(0.09 * salaryInGOSI);
        row.createCell(9).setCellValue(0.0075 * salaryInGOSI);
        row.createCell(10).setCellValue(0.0075 * salaryInGOSI);
        	}
        else {
        	
        	row.createCell(7).setCellValue(0.0);
            row.createCell(8).setCellValue(0.0);
            row.createCell(9).setCellValue(0.0);
            row.createCell(10).setCellValue(0.0);
        }
      }
    }
    for (int i = 0; i < headers.length; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  private String[] getHeaders(Sheet sheet) {
    log.info(INSIDE_CLASS_LOG+" getHeaders");
    Row headerRow = sheet.createRow(0);
    String[] headers = { "Employee No", "Name", "National", "Basic Salary + Housing", "Salary in GOSI", "Diff",
        "Company Cost Accident 2%",
        "Company Cost GOSI 9%", "Employee Cost %9", "Company Sanid Cost %0.75", "Employee Sanid Cost %0.75" };
    for (int i = 0; i < headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
    }
    return headers;
  }
  
  @Override
  public List<Object[]> getSumOfValue(Integer payrollId,String[] parts)
  {
         log.info("Inside @class EmployeeMonthlySalaryServiceImpl @method getSumOfValue ");
         try
         {
        	 log.debug(" Inside @getSumOfValue :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
        	 List<Object[]> values = employeeMonthlySalaryRepository.getSumOfValue(payrollId,parts,commonUtils.getCustomerId());  
               return values;
         }
         catch(Exception e)
         {
                 log.error("Error inside @class EmployeeMonthlySalaryServiceImpl @method getSumOfValue  :{} :{}",e.getMessage(),Utils.getStackTrace(e));
            throw new BusinessException();
         }
  }
  
  @Override
  public List<Object[]> getSumOfValueOfAccural(Integer payrollId,String[] parts)
  {
         
         log.info("Inside @class EmployeeMonthlySalaryServiceImpl @method getSumOfValueOfAccural ");
         try
         {
        	 log.debug(" Inside @getSumOfValueOfAccural :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
          List<Object[]> values = employeeMonthlySalaryRepository.getSumOfValueForAccural(payrollId,parts,commonUtils.getCustomerId());
          return values;
         }
         
         catch(Exception e)
         {
                 log.error("Error inside @class EmployeeMonthlySalaryServiceImpl @method getSumOfValueOfAccural :{} :{}",e.getMessage(),Utils.getStackTrace(e));
                    throw new BusinessException();
         }
  }
  
  public ResponseEntity<byte[]> createWpsTxtFileForOnBoarded(String payrollRunProcessInstanceId) {
    	 log.debug(" Inside @createWpsTxtFileForOnBoarded :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		try {
	    String filePath ="";
      LocalDate localDate = LocalDate.now();
			log.info(INSIDE_CLASS_LOG+"createWpsTxtFileForOnBoarded");
			if(payrollRunProcessInstanceId!=null ) {
			Optional <PayrollRun> payrollRun = payrollRunRepository.findPayrollRunByProcessInstanceId(payrollRunProcessInstanceId);
			if(!payrollRun.isEmpty()) {
			PayrollRun payrollRunObject = payrollRun.get();
			Integer payrollId = payrollRunObject.getId();
			log.debug(INSIDE_CLASS_LOG+"createWpsTxtFileForOnBoarded payrollRunId :{}",payrollId);
  	    if(payrollRunObject.getWorkflowStage()!=null  && ((payrollRunObject.getWorkflowStage().equalsIgnoreCase("APPROVED_BY_FINANCE_CONTROLLER") || payrollRunObject.getWorkflowStage().equalsIgnoreCase("PAYROLL_BANK_APPROVAL"))) ){
 
 
			List<String[]> data = fetchData(payrollId,false);
			ByteArrayOutputStream out = generateTxt(data);
			byte[] txtBytes = out.toByteArray();
			Date payrollDate = payrollRunObject.getEndDate();
			LocalDate payrollLocalDate = convertDateToLocalDate(payrollDate);
			Integer month = payrollLocalDate.getMonthValue();
			String monthString = String.valueOf(month);
			String fileReference = monthString + payrollLocalDate.getYear();
			log.debug("The month of BOD :{} ",monthString);
			String fileName = "BODWps" + fileReference+ ".txt";
	        filePath += "hrmswps/" + fileName;
	        log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}",fileName,fileReference,filePath);
    		
    		log.info("Inside createWpsTxtFileForAllEducationalBenefit rootDirBucketName :{}" , rootDirBucketName);
    		InputStream inputStream = new ByteArrayInputStream(txtBytes);
    		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
    		HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData(ATTACHMENT, fileName);
            saveData(filePath,month ,payrollLocalDate.getYear(),localDate);
            log.debug("The Path of File Dir to store is :{} ",filePath);
	        return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);
	    }else if(payrollRunObject.getBasePathForWPS()==null) {
	    	return null;
	    }
	     
	    }
			}
			
			return null;	
		} catch (Exception e) {
			log.error(INSIDE_CLASS_LOG+"createWpsTxtFileForOnBoarded exception occurs :{}",e.getMessage());
			log.error(INSIDE_CLASS_LOG+"  createWpsTxtFileForOnBoarded method ex : {}", Utils.getStackTrace(e), e);
			throw new BusinessException("Exception occurs inside fetchData");
			
		}	
	}

  private String getYearForBoardedMember() {
	  LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
      String formattedDate = currentDate.format(formatter);
	    return formattedDate;
	}

  private String getMonthForBoardedMember() {
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
    String formattedMonth = currentDate.format(formatter);
    return formattedMonth;
}

  
  public ResponseEntity<byte[]> downloadWpsFileForBoardedMember(String year , String month)  {
 	 log.debug(" Inside @downloadWpsFileForBoardedMember :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
    String filePath =   employeeMonthlySalaryRepository.getFilePath(Integer.parseInt(month),Integer.parseInt(year),commonUtils.getCustomerId());
	    
	    		
	    log.info("Inside Rundetails downloadWpsFile rootDirBucketName :{} ",rootDirBucketName);
		
	    String currentTime = getSystemTimeWithTimeStampForBoardedMember();
	  try {
	      log.debug("Inside @method: downloadWpsFile the path is :{}",filePath );
	      log.debug("final path is :{} ",rootDirBucketName+filePath);
	      InputStream inputStream = storageRest.read(rootDirBucketName, filePath);
	      log.debug("InputStream available bytes: {}", inputStream.available());
	      InputStreamResource resource = new InputStreamResource(inputStream);
	      byte[] fileData = resource.getContentAsByteArray();
	      HttpHeaders responseHeaders = new HttpHeaders();
	      responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	      responseHeaders.setContentDispositionFormData(ATTACHMENT, "Wps" + currentTime + ".txt");
	      return new ResponseEntity<>(fileData, responseHeaders, HttpStatus.OK);
	    }
	   catch (Exception e) {
	      throw new BusinessException("Something went wrong", e);
	  }
	}
  
  void saveData(String path , Integer month ,Integer year ,LocalDate date)
  {
	 	 log.debug(" Inside @saveData :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
		log.info(INSIDE_CLASS_LOG+"saveData");
		try
		{
			log.debug("The values are :{} :{} :{} :{} ",path,month,year,date);
			employeeMonthlySalaryRepository.saveData(path, date, month, year,PRConstant.BOARD_OF_DIRECTORS,commonUtils.getCustomerId());
			log.info("The data is inserted ");
		}
		catch(Exception e)
		{
			log.error(INSIDE_CLASS_LOG+"  saveData method ex : {}", Utils.getStackTrace(e), e);
			throw new BusinessException();
	
		}
  }
  
  private String getSystemTimeWithTimeStampForBoardedMember() {
	  LocalDate currentDate = LocalDate.now();
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
      String formattedDate = currentDate.format(formatter);
	    return formattedDate;
	}
  
  @Override
  public List<Object[]> getExpenseForBOD(Integer payrollId ,String employeeType)
  {
	 	 log.debug(" Inside @getExpenseForBOD :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	  try
	  {
		  log.info(INSIDE_CLASS_LOG+" getExpenseForBOD ");
		  List<Object[]> listOfBodExpense = employeeMonthlySalaryRepository.getExpenseForBOD(payrollId, employeeType,commonUtils.getCustomerId());
		  log.debug("Size of list of BOD :{} ",listOfBodExpense.size());
		  return listOfBodExpense;
	  }
	  catch(Exception e)
	  {
			log.error(INSIDE_CLASS_LOG+"  getExpenseForBOD : {}", Utils.getStackTrace(e), e);
			throw new BusinessException();
  
	  }
  }
  
  @Override
  public  Object[] getAccuralsForBOD(Integer payrollId ,String employeeType)
  {
	 	 log.debug(" Inside @getAccuralsForBOD :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	  try
	  {
		  log.info(INSIDE_CLASS_LOG+" getAccuralsForBOD ");
		   Object[]  listOfBodExpense = employeeMonthlySalaryRepository.getAccuralForBOD(payrollId, employeeType,commonUtils.getCustomerId());
		  log.debug("Size of list of BOD :{} ",listOfBodExpense);
		  return listOfBodExpense;
	  }
	  catch(Exception e)
	  {
			log.error(INSIDE_CLASS_LOG+"  getAccuralsForBOD : {}", Utils.getStackTrace(e), e);
			throw new BusinessException();
  
	  }
  }
    
  
	public LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}
	private String dateFormated() {
	    LocalDate date = LocalDate.now();
	    int monthValue = date.getMonthValue();

	    // Array with month abbreviations
	    String[] monthAbbreviations = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

	    // Get the abbreviated month name
	    String month = monthAbbreviations[monthValue - 1];

	    // Return just the month abbreviation
	    return month; // No year included here
	}


	@Override
	public PayrollTotals getPayrollSum(Integer payrollRunId) {
	 	 log.debug(" Inside @getPayrollSum :{}  customerId is : {}", ENTITY_NAME,commonUtils.getCustomerId());
	log.debug("Inside getPayrollSum payrollRunId:{}",payrollRunId );
	log.debug(" Inside @getWeightageConfigurationById  customerId is : {}", commonUtils.getCustomerId());
	PayrollTotals payrollTotals = new PayrollTotals();
	Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
	String onboardMemberTypeList = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);
	log.debug("Inside @method getPayrollSum :{}  ",onboardMemberTypeList);
	String[] listString = onboardMemberTypeList.split(",");
	log.debug("Inside @method getPayrollSum : Array :{}  " ,listString);
	
	List<Object[]> resultArray = employeeMonthlySalaryRepository.getSumOfEmployeeMonthlySalaryComponents(payrollRunId, listString, commonUtils.getCustomerId());

	log.debug(
			"Inside @method getPayrollSum : resultArray{}",
			resultArray);

	if (resultArray != null && !resultArray.isEmpty()) {
		Object[] results = resultArray.get(0);

		try {
			Double totalBasicSalary = getDoubleValue(results, 0);
			Double totalOverbase = getDoubleValue(results, 1);
			Double totalCriticalSkills = getDoubleValue(results, 2);
			Double totalHra = getDoubleValue(results, 3);
			Double totalTa = getDoubleValue(results, 4);
			Double totalMobileAllowance = getDoubleValue(results, 5);
			Double totalSTI = getDoubleValue(results, 6);
			Double totalSignUpBonus = getDoubleValue(results, 7);
			Double totalOvertime = getDoubleValue(results, 8);
			Double totalRelocationAllowance = getDoubleValue(results, 9);
			Double totalGosiEmployee = getDoubleValue(results, 10);
			Double totalGosiEmployer = getDoubleValue(results, 11);
			Double totalOtherEarning = getDoubleValue(results, 12);
			Double totalOtherDeduction = getDoubleValue(results, 13);
			Double totalEarningAmount = getDoubleValue(results, 14);
			Double totalDeductionAmount = getDoubleValue(results, 15);
			Double totalAmount = getDoubleValue(results, 16);
			Double totalVarianceAmount = getDoubleValue(results, 17);
			
			

			totalBasicSalary = formatDouble(totalBasicSalary);
			totalOverbase = formatDouble(totalOverbase);
			totalCriticalSkills = formatDouble(totalCriticalSkills);
			totalHra = formatDouble(totalHra);
			totalTa = formatDouble(totalTa);
			totalMobileAllowance=formatDouble(totalMobileAllowance);
			totalSTI = formatDouble(totalSTI);
			totalSignUpBonus = formatDouble(totalSignUpBonus);
			totalOvertime = formatDouble(totalOvertime);
			totalRelocationAllowance = formatDouble(totalRelocationAllowance);
			totalGosiEmployee = formatDouble(totalGosiEmployee);
			totalGosiEmployer = formatDouble(totalGosiEmployer);
			totalOtherEarning = formatDouble(totalOtherEarning);
			totalOtherDeduction = formatDouble(totalOtherDeduction);
			totalEarningAmount = formatDouble(totalEarningAmount);
			totalDeductionAmount = formatDouble(totalDeductionAmount);
			totalAmount = formatDouble(totalAmount);
			totalVarianceAmount = formatDouble(totalVarianceAmount);
            
			payrollTotals.setTotalBasicSalary(totalBasicSalary);
			payrollTotals.setTotalOverbase(totalOverbase);
			payrollTotals.setTotalCriticalSkills(totalCriticalSkills);
			payrollTotals.setTotalHra(totalHra);
			payrollTotals.setTotalTa(totalTa);
			payrollTotals.setTotalMobileAllowance(totalMobileAllowance);
			payrollTotals.setTotalSTI(totalSTI);
			payrollTotals.setTotalSignUpBonus(totalSignUpBonus);
			payrollTotals.setTotalOvertime(totalOvertime);
			payrollTotals.setTotalRelocationAllowance(totalRelocationAllowance);
			payrollTotals.setTotalGosiEmployee(totalGosiEmployee);
			payrollTotals.setTotalGosiEmployer(totalGosiEmployer);
			payrollTotals.setTotalOtherEarning(totalOtherEarning);
			payrollTotals.setTotalOtherDeduction(totalOtherDeduction);
			payrollTotals.setTotalEarningAmount(totalEarningAmount);
			payrollTotals.setTotalDeductionAmount(totalDeductionAmount);
			payrollTotals.setTotalAmount(totalAmount);
			payrollTotals.setTotalVarianceAmount(totalVarianceAmount);
			
			log.debug("Inside @method getPayrollSum  payrollTotals:{}", payrollTotals);
			
			
		}
		catch(Exception e) {
			throw new BusinessException("Error while calculating  details", e);
		}
		}

		return payrollTotals;
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
	
	@Override
	public EmployeeMonthlySalary getEmployeeMonthlySalaryByEmployeeIdAndPayrollRunId(Integer employeeId,Integer payrollRunId)
	{
        try
        {
    		log.debug("Inside @class EmployeeMonthlySalaryServiceImpl @method getEmployeeMonthlySalaryByEmployeeIdAndPayrollRunId EmployeeId payrollRunId :{} :{} "
    				,employeeId,payrollRunId);
    		EmployeeMonthlySalary employeeMonthlySalary = employeeMonthlySalaryRepository.findByEmployeeIdAndPayrollRunIdAndCustomerId(employeeId, payrollRunId, commonUtils.getCustomerId());
    	 log.debug(" Employee Monthly Salary Recevied Id :{} ",employeeMonthlySalary.getId());
    	 return employeeMonthlySalary;	
        }
        catch(Exception e)
        {
        	log.error("Error Inside @class EmployeeMonthlySalaryServiceImpl @method getEmployeeMonthlySalaryByEmployeeIdAndPayrollRunId :{} :{} ",
        			e.getMessage(),Utils.getStackTrace(e));
        	throw new BusinessException();
        }
	}
}
	


