package com.nouros.payrollmanagement.service.impl;

import java.beans.IntrospectionException;  
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.ai.chat.AiChatModel;
import com.enttribe.commons.io.excel.Excel;
import com.enttribe.commons.io.excel.ExcelRow;
import com.enttribe.commons.io.excel.ExcelWriter;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.execution.controller.IWorkorderController;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.service.impl.ChildEducationBenefitServiceImpl;
import com.nouros.hrms.service.impl.EducationalBenefitServiceImpl;
import com.nouros.hrms.service.impl.HealthClubBenefitServiceImpl;
import com.nouros.hrms.service.impl.NewHireBenefitServiceImpl;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.util.report.ExcelUtils;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructureHistory;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.model.OtherSalaryPayrollMapping;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.repository.PayrollRunRepository;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryService;
import com.nouros.payrollmanagement.service.EmployeeSalaryStructureHistoryService;
import com.nouros.payrollmanagement.service.EmployeeSalaryStructureService;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.service.OtherSalaryComponentService;
import com.nouros.payrollmanagement.service.OtherSalaryPayrollMappingService;
import com.nouros.payrollmanagement.service.PayrollRunService;
import com.nouros.payrollmanagement.utils.PRConstant;
import com.nouros.payrollmanagement.wrapper.PayrollRequestWrapper;
import com.nouros.payrollmanagement.wrapper.PayrollRunWrapper;



/**
 * This is a class named "PayrollRunServiceImpl" which is located in the package " com.enttribe.payrollmanagement.service.impl", It appears to be an implementation of the "PayrollRunService" interface and it extends the "AbstractService" class, which seems to be a generic class for handling CRUD operations for entities. This class is annotated with @Service, indicating that it is a Spring Service bean.
This class is using Lombok's @Slf4j annotation which will automatically generate an Slf4j based logger instance, so it is using the Slf4j API for logging.
The class has a constructor which takes a single parameter of GenericRepository PayrollRun and is used to call the superclass's constructor.
This class have one public method public byte[] export(List of PayrollRun  PayrollRun) for exporting the PayrollRun data into excel file by reading the template and mapping the PayrollRun details into it.
It's using Apache POI library for reading and writing excel files, and has methods for parsing the json files for column names and identities , and it also used 'ExcelUtils' for handling the excel operations.
It also uses 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and 'APIConstants' from 'com.enttribe.payrollmanagement.util'
* */










@Service
public class PayrollRunServiceImpl extends AbstractService<Integer,PayrollRun> implements PayrollRunService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for PayrollRun entities.
	 */
	 
   private static final Logger log = LogManager.getLogger(PayrollRunServiceImpl.class);

   private static final String ENTITY_NAME = "PayrollRun";
   public static final String RESPONSE = "response";
   public static final String VARIANCE_REASON = "varianceReason";
   private static final String UNABLE_TO_PARSE_RESPONSE_FROM_PROMPT_FOR_VARIANCE = "Unable To Parse Response from Prompt For Variance";
   
	public PayrollRunServiceImpl(GenericRepository<PayrollRun> repository) {
		super(repository,PayrollRun.class);
	}
	@Autowired
    private PayrollRunRepository payrollRunRepository;
	
    @Autowired
    private EmployeeMonthlySalaryService  employeeMonthlySalaryService;

	@Autowired
	private EmployeeSalaryStructureService employeeSalaryStructureService;
  

	@Autowired
	private EmployeeSalaryStructureHistoryService employeeSalaryStructureHistoryService;
	
	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;
	
	@Autowired
	WorkflowActionsController workflowActionsController;
	
	@Autowired
	IWorkorderController workorderController;
	
	@Autowired
	  private CommonUtils commonUtils;
 
	@Autowired
	AiChatModel aiChatModel;
	
	/**
     * This method is used to export the given list of PayrollRun objects into an excel file.
     * It reads an excel template 'PayrollRun.xlsx' from the resource folder 'templates/reports'
     * and maps the PayrollRun data onto the template and returns the generated excel file in the form of a byte array.
     * param PayrollRun - List of PayrollRun objects to be exported
     * @return byte[] - The generated excel file in the form of a byte array
     * @throws IOException - When the template file is not found or there is an error reading the file
     */
	@Override
    public byte[] export(List<PayrollRun> payrollRun) throws IOException {
 try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("templates/reports/PayrollRun.xlsx");
         XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resourceAsStream)) {
        int rowCount = 1;
        return setTableData(payrollRun, xssfWorkbook, rowCount);
    }    
  }
  
  
    /**
 * This method is responsible for setting the data of an Excel document, using a template and a list of PayrollRun objects.
 * The data is written to the template starting at the specified row number.
 * 
 * @param PayrollRun a List of PayrollRun objects, representing the data that will be written to the Excel document
 * @param templatePath an XSSFWorkbook object, representing the template Excel document that the data will be written to
 * @param rowCount an int, representing the starting row number where data will be written in the Excel document
 * @return a byte array of the Excel document after the data has been written to it.
 * @throws IOException if there is an issue reading or writing to the Excel document
 */

  /**This method appears to take in three parameters:

A List of PayrollRun objects, representing the data that will be written to the Excel document.
An XSSFWorkbook object, representing the template Excel document that the data will be written to.
An int, representing the starting row number where data will be written in the Excel document.
The method has a return type of byte array, which is the Excel document after the data has been written to it. The method also throws an IOException, which would be thrown if there is an issue reading or writing to the Excel document.

The method starts by creating some maps to hold data that will be used later:

tableColumn: a map that will hold the columns of the Excel table.
identityColumnMapping: a map that will hold the mapping of columns
templateHeaders: a map that will hold the headers of the excel template
then it calls ExcelUtils.parseMappeddJson(tableColumn,identityColumnMapping,templateHeaders); to get the values for the maps created.

Then it creates an instance of ExcelWriter which will write the data to the workbook, it set the active sheet to the first sheet of the workbook and check if PayrollRun list is not empty.

It then iterates over the list of PayrollRun objects and for each PayrollRun, it creates a new row in the Excel document at the specified row number.

It also retrieves the list of columns for the "PayrollRun" entity from the tableColumn map, and iterates over the columns.

For each column, it attempts to retrieve the value for that column from the current PayrollRun object using the ExcelUtils.invokeGetter method, passing in the current PayrollRun object, the column name and the identityColumnMapping.

The value returned by this method is then set as the value of the cell in the current row and column.
If an introspection exception occur it will print the stacktrace of the exception

After all the data is written to the Excel document, the method returns the Excel document as a byte array using excelWriter.toByteArray() and log "going to return file"
* */
   private byte[] setTableData(List<PayrollRun>payrollRun,
  XSSFWorkbook templatePath,
  int rowCount)throws IOException
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
        if (CollectionUtils.isNotEmpty(payrollRun)) {
          for (PayrollRun payrollRunDetails : payrollRun) {
            ExcelRow row = excelWriter.getOrCreateRow(0, rowCount);
            int index = 0;
            List<String> columns = tableColumn.get(entity);
            for(String column:columns) {
              if(column!=null) {   
              try {
              row.setCellValue(index, ExcelUtils.invokeGetter(payrollRunDetails,column,identityColumnMapping).toString());
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
 * This method is responsible for importing data from an Excel file, specifically data related to PayrollRun objects.
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
	log.debug(" Inside @importData  :{}  customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
    List<PayrollRun> payrollRuns =new ArrayList<>();
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
        payrollRuns = saveData(workBook, columnsMapping, columnNames);
      }
    else {
      log.info("columns and headers invalide");
      }
    if (CollectionUtils.isNotEmpty(payrollRuns)) {
      payrollRunRepository.saveAll(payrollRuns);
      return APIConstants.SUCCESS_JSON;
    }
    return APIConstants.FAILURE_JSON;
  }

/**
 * This method is responsible for saving data to the database, specifically data related to PayrollRun objects.
 * The method takes in an Excel object, which represents the sheet containing the data, a mapping of columns from the excel sheet to the PayrollRun class, and a list of column names.
 * The method uses the iterator for the sheet to read data row by row, create new PayrollRun objects, and set the properties of the PayrollRun objects using the column mapping and column names.
 * The method returns a list of PayrollRun objects that have been saved to the database.
 *
 * @param sheet an Excel object representing the sheet containing the data
 * @param columnMapping a map representing the mapping of columns from the excel sheet to the PayrollRun class
 * @param columnNames a list of column names of the excel sheet
 * @return a list of PayrollRun objects that have been saved to the database
 */

    public List<PayrollRun> saveData(Excel sheet, Map<String, String> columnMapping,
      List<String> columnNames) {
    	log.debug(" Inside @saveData :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
    Iterator<ExcelRow> rowIterator = sheet.iterator();
    List<PayrollRun> payrollRuns = new ArrayList<>();
    rowIterator.next();
    while (rowIterator.hasNext()) {
      ExcelRow excelRow = rowIterator.next();
          PayrollRun payrollRun = new PayrollRun();
              int index = -1;
      for (String columnName : columnNames) {
        try {
          ExcelUtils.invokeSetter(payrollRun, columnName, excelRow.getString(++index));
                  } catch (InstantiationException e) {
          log.error("failed while going to set the value :{}", excelRow.getString(++index));
          log.error("InstantiationException occurred: {}", e.getMessage());
          
        } catch (ClassNotFoundException e) {
          log.error("ClassNotFoundException occurred: {}", e.getMessage());
        }
      }
	    payrollRuns.add(payrollRun);
  		}
    return payrollRuns;
  }
  
	
	
		/**
* This method is responsible for soft-deleting an PayrollRun  record in the database.
* The method takes in an int id which represents the id of the PayrollRun  that needs to be soft-deleted.
* It uses the id to find the PayrollRun by calling the PayrollRunRepository.findById method.
* If the PayrollRun  is found, it sets the "deleted" field to true, save the PayrollRun  in the repository, and saves it in the database
*
* @param id an int representing the id of the PayrollRun  that needs to be soft-deleted
*/
	@Override
	public void softDelete(int id) {
    	log.debug(" Inside @softDelete :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
    log.info(" Inside @class PayrollRunServiceImpl @method softDelete ");
		try {
	PayrollRun payrollRun = super.findById(id);

	if (payrollRun != null) {
		PayrollRun payrollRun1 = payrollRun;
        
        cancelPayrollOtherAmount(payrollRun1);
        
//        EmployeeMonthlySalaryServiceImpl runDetailsServiceImpl = ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryServiceImpl.class);
//        List<EmployeeMonthlySalary> runDetails = runDetailsServiceImpl.getRunDetailsByPayrollId(id);           
//         
//          cancelBenefitByJson(runDetails);
//    
//        EmployeeMonthlySalaryService employeeMonthlySalaryService =  ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);  
//        employeeMonthlySalaryService.softBulkDeleteByPayrollId(Arrays.asList(id));
//      
        payrollRun1.setDeleted(true);
	    payrollRunRepository.save(payrollRun1);	
	}

}
catch(Exception e)
{
		log.error("Error Inside @class PayrollRunServiceImpl @method softDelete :{} :{}",
				 e.getMessage(),Utils.getStackTrace(e));	
}
	}
	
		private void cancelPayrollOtherAmount(PayrollRun payrollRun1) {
	    	log.debug(" Inside @cancelPayrollOtherAmount :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
			 log.debug("Inside PayrollRunServiceImpl cancelPayrollOtherAmount - payrollRun1: {}", payrollRun1.getId());
			 OtherSalaryPayrollMappingService otherSalaryPayrollMappingService = ApplicationContextProvider.getApplicationContext().getBean(OtherSalaryPayrollMappingService.class);
			 OtherSalaryComponentService otherSalaryComponentService = ApplicationContextProvider.getApplicationContext().getBean(OtherSalaryComponentService.class);
				
			List<OtherSalaryComponent> list1 =  otherSalaryPayrollMappingService.getComponentsByPayrollId(payrollRun1.getId(),OtherSalaryComponent.Type.EARNING.toString(),false);
			
			for(OtherSalaryComponent otherSalaryComponent :list1) {
				
				otherSalaryComponent.setWorkflowStage("APPROVED");
				otherSalaryComponentService.update(otherSalaryComponent);
			}
			
			List<OtherSalaryComponent> list2 =  otherSalaryPayrollMappingService.getComponentsByPayrollId(payrollRun1.getId(),OtherSalaryComponent.Type.DEDUCTION.toString(),false);
			
			for(OtherSalaryComponent otherSalaryComponent :list2) {
				if(otherSalaryComponent.getWorkflowStage()!=null && otherSalaryComponent.getWorkflowStage().equalsIgnoreCase( PRConstant.DEDUCTED)) {
					OtherSalaryPayrollMapping  otherSalaryPayrollMapping =    otherSalaryPayrollMappingService.getMappingAmount(otherSalaryComponent.getId(),false);
					if(otherSalaryPayrollMapping!=null && otherSalaryPayrollMapping.getAmount()!=null && !otherSalaryPayrollMapping.getAmount().equals(0)) {
						Double paidAmount = otherSalaryPayrollMapping.getAmount();
						if(paidAmount.equals(otherSalaryComponent.getAmount())){
							otherSalaryComponent.setWorkflowStage(PRConstant.APPROVED);
						}else if(otherSalaryComponent.getPaidAmount() !=null){
							 Double paidRevert  = otherSalaryComponent.getPaidAmount() - paidAmount;
							 Double amountRevert = otherSalaryComponent.getAmount() + paidAmount;
							 log.debug("Inside PayrollRunServiceImpl  getPaidAmount()  :{} getAmount() :{}   paidRevert {} - amountRevert: {}",otherSalaryComponent.getPaidAmount(), otherSalaryComponent.getAmount() ,paidRevert,amountRevert);
						     otherSalaryComponent.setAmount(amountRevert);
							 otherSalaryComponent.setPaidAmount(paidRevert);
							 otherSalaryComponent.setWorkflowStage(PRConstant.PENDING);
							 otherSalaryComponentService.update(otherSalaryComponent);
						}
					} 
				}else if(otherSalaryComponent.getWorkflowStage()!=null && otherSalaryComponent.getWorkflowStage().equalsIgnoreCase(PRConstant.PENDING)) {
					OtherSalaryPayrollMapping  otherSalaryPayrollMapping =    otherSalaryPayrollMappingService.getMappingAmount(otherSalaryComponent.getId(),false);
					if(otherSalaryPayrollMapping!=null && otherSalaryPayrollMapping.getAmount()!=null && !otherSalaryPayrollMapping.getAmount().equals(0)) {
						
						Double paidAmount = otherSalaryPayrollMapping.getAmount();
						Double paidRevert  = otherSalaryComponent.getPaidAmount() - paidAmount;
						Double amountRevert = otherSalaryComponent.getAmount() + paidAmount;
						log.debug("Inside PayrollRunServiceImpl  getPaidAmount()  :{} getAmount() :{}   paidRevert {} - amountRevert: {}",otherSalaryComponent.getPaidAmount(), otherSalaryComponent.getAmount() ,paidRevert,amountRevert);
					    otherSalaryComponent.setAmount(amountRevert);
						otherSalaryComponent.setPaidAmount(paidRevert);
						otherSalaryComponentService.update(otherSalaryComponent);
						 
					}
				}
			}		
		}

		/**
* This method is responsible for soft-deleting multiple PayrollRun records in the database in bulk.
* The method takes in a List of integers, each representing the id of an PayrollRun that needs to be soft-deleted.
* It iterates through the list, calling the softDelete method for each id passed in the list.
*
* @param list a List of integers representing the ids of the PayrollRun that need to be soft-deleted
*/
	@Override
	public void softBulkDelete(List<Integer> list) {
		log.info("Inside @class PayrollRunServiceImpl @method softBulkDelete ");
		log.debug(" Inside @softBulkDelete :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				
				softDelete(list.get(i));
				
			}
		}

	}
	
	
	
 /**
 * @param payrollRun The payrollRun  object to create.
 * @return The created vendor object.
 */
	@Override
	public PayrollRun create(PayrollRun payrollRun)
	{	
		log.debug(" Inside @create :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
			return payrollRunRepository.save(payrollRun);
	}


@Override
public PayrollRun createPayroll(PayrollRun payrollRun) {
	log.debug(" Inside @createPayroll :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	 log.info("inside payrollRun service impl @method createPayroll payrollRun:{} ",payrollRun );
	 payrollRun.setWorkflowStage(PRConstant.PROCESSING);
	 return payrollRunRepository.save(payrollRun);
}


@Override
public PayrollRun update(PayrollRun payrollRun) {
	log.info("inside payrollRunserviceimpl @method updatePayroll payrollRun:{} ",payrollRun);
	log.debug(" Inside @update :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	if(payrollRun!=null) {
		return  payrollRunRepository.save(payrollRun);
	}
	return null;
}

@Override
public PayrollRun identifyVarianceReasons(Integer payrollRunId) {
  log.debug("Inside PayrollRunServiceImpl identifyVarianceReasons - payrollRunId: {}", payrollRunId);
  log.debug(" Inside @identifyVarianceReasons :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
  PayrollRun payrollOpt = super.findById(payrollRunId);
  
  try {
  if (payrollOpt != null) {
    log.debug("Inside PayrollRunServiceImpl identifyVarianceReasons - payrollRunId: {}", payrollRunId);
    PayrollRun payrollrun = payrollOpt;
    List<EmployeeMonthlySalary> listEmployeeMonthlySalary = employeeMonthlySalaryService.getEmployeeMonthlySalaryByPayrollId(payrollRunId);
    if (listEmployeeMonthlySalary != null && !listEmployeeMonthlySalary.isEmpty()) {
      identifyEmployeeVariance(listEmployeeMonthlySalary, payrollrun);
      payrollrun =  setPayrollTop3Reasons(payrollrun);
    }	
    return payrollrun;
  }
  
   }catch(Exception ex) {
    log.error("ERInside PayrollRunServiceImpl identifyVarianceReasons: {}",Utils.getStackTrace(ex));
  }
  return null;
}

private PayrollRun setPayrollTop3Reasons( PayrollRun payrollrun) {
  log.debug("Inside PayrollRunServiceImpl setPayrollTop3Reasons - payrollRunId: {}", payrollrun.getId());
  log.debug(" Inside @setPayrollTop3Reasons :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
  String top3Reason = employeeMonthlySalaryService.getTop3ReasonByPayrollid(payrollrun.getId());
  
   String llmString = getLLMGeneratedReasonForPayroll(top3Reason);
   
   log.debug("Inside PayrollRunServiceImpl setPayrollTop3Reasons - llmString: {} top3Reason :{} ", llmString,top3Reason);
   
   payrollrun.setVarianceReason(llmString);
   
   payrollrun= update(payrollrun);
  
  return payrollrun;
}

private String getLLMGeneratedReasonForEmployeeVariance(String reason) {
	 log.debug("Inside PayrollRunServiceImpl getLLMGeneratedReasonForEmployeeVariance  - reason: {} ", reason);
	 log.debug(" Inside @getLLMGeneratedReason :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	 try {
			VectorIntegrationService vectorIntegrationService = ApplicationContextProvider.getApplicationContext().getBean(VectorIntegrationService.class);
			String response = "";
			String responseValue = "";
			JSONObject fullResponse = null;
			try {
				Map<String, Object> inputMap = new HashMap<>();
				inputMap.put("reason", reason);
				response = aiChatModel
						.chatCompletion("HRMS_APP_NAME-PayrollRun-Employee_Monthly_Salary_Variance-v-1", inputMap);
				log.debug("getLLMGeneratedReasonForEmployeeVariance Response from ai chat model is : {} ", response);
				
				log.debug("getLLMGeneratedReasonForEmployeeVariance JsonObject  fullResponse is : {} ", fullResponse);
				fullResponse = new JSONObject(response);
				log.debug("JSONObject fullResponse for sdk is : {} ", fullResponse);
			} catch (BusinessException ex) {
				log.error("Error while Generating Response From Sdk for Resume Content", ex.getMessage());
			}
		    if(fullResponse!=null) {
				if (fullResponse.has(RESPONSE)) {
					try {
						JSONObject responseObj = fullResponse.getJSONObject(RESPONSE);
						log.debug("getLLMGeneratedReasonForEmployeeVariance responseObj Of Variance Reason is : {} ", responseObj);
						// Check if the "response" key exists inside the first-level response object
						responseValue = responseObj.optString(VARIANCE_REASON);
						log.debug("getLLMGeneratedReasonForEmployeeVariance responseValue Of Variance Reason is : {} ", responseValue);
					} catch (Exception e) {
						log.error(UNABLE_TO_PARSE_RESPONSE_FROM_PROMPT_FOR_VARIANCE, e);
					}
					return responseValue;
			  }
	   }
		    return responseValue;
     }
	 catch(Exception ex) {
		 log.error("ERInside PayrollRunServiceImpl getLLMGeneratedReason : {}",Utils.getStackTrace(ex));
		 return null;
   }
}

private String getLLMGeneratedReasonForPayroll(String reason) {
	 log.debug("Inside PayrollRunServiceImpl getLLMGeneratedReasonForPayroll  - reason: {}", reason);
	 log.debug(" Inside @getLLMGeneratedReasonForPayroll :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	 try {
			VectorIntegrationService vectorIntegrationService = ApplicationContextProvider.getApplicationContext().getBean(VectorIntegrationService.class);
			String response = "";
			String responseValue = "";
			JSONObject fullResponse = null;
			try {
				Map<String, Object> inputMap = new HashMap<>();
				inputMap.put("reason", reason);
				response = aiChatModel
						.chatCompletion("HRMS_APP_NAME-PayrollRun-Payroll_Variance_Reason-v-1", inputMap);
				log.debug("Response from ai chat model getLLMGeneratedReasonForPayroll is : {} ", response);
				
				log.debug("JsonObject  fullResponse is : {} ", fullResponse);
				fullResponse = new JSONObject(response);
				log.debug("getLLMGeneratedReasonForPayroll JSONObject fullResponse for sdk is : {} ", fullResponse);
			} catch (BusinessException ex) {
				log.error("Error while Generating Response From Sdk for Resume Content", ex.getMessage());
			}
		    if(fullResponse!=null) {
				if (fullResponse.has(RESPONSE)) {
					try {
						JSONObject responseObj = fullResponse.getJSONObject(RESPONSE);
						log.debug("getLLMGeneratedReasonForPayroll responseObj Of Variance Reason is : {} ", responseObj);
						// Check if the "response" key exists inside the first-level response object
						responseValue = responseObj.optString(VARIANCE_REASON);
						log.debug("getLLMGeneratedReasonForPayroll responseValue Of Variance Reason is : {} ", responseValue);
					} catch (Exception e) {
						log.error(UNABLE_TO_PARSE_RESPONSE_FROM_PROMPT_FOR_VARIANCE, e);
					}
					return responseValue;
			  }
	   }
		    return responseValue;
    }
	 catch(Exception ex) {
		 log.error("ERInside PayrollRunServiceImpl getLLMGeneratedReason : {}",Utils.getStackTrace(ex));
		 return null;
  }
}


private void identifyEmployeeVariance(List<EmployeeMonthlySalary> listEmployeeMonthlySalary, PayrollRun payrollrun) {
  log.info("Inside PayrollRunServiceImpl identifyEmployeeVariance");
	 log.debug(" Inside @identifyEmployeeVariance :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
  try {
	  Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
	  String onboardMemberTypeList = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);
	  List<String> onboardMemberTypes = Arrays.asList(onboardMemberTypeList.split(","));
	  log.info("Inside PayrollRunServiceImpl identifyEmployeeVariance onboardMemberTypes: {}",onboardMemberTypes);
	   

  for (EmployeeMonthlySalary rundetails : listEmployeeMonthlySalary) {
    StringBuilder runDetailsReason = new StringBuilder("");
    Employee employee = rundetails.getEmployee();
    String employmentType=employee.getEmploymentType();
    log.info("Inside PayrollRunServiceImpl identifyEmployeeVariance employmentType: {} ",employmentType);
    boolean isBoardMember = onboardMemberTypes.contains(employmentType);
    log.info("Inside PayrollRunServiceImpl identifyEmployeeVariance isBoardMember: {}",isBoardMember);
   
    if (rundetails != null && employee != null && !isBoardMember) {
      Integer employeeId = employee.getId();
      Integer rundetailsId = rundetails.getId();
      log.debug(
          "Inside PayrollRunServiceImpl identifyEmployeeVariance - rundetailsId: {}  employeeId :{} ",
          rundetailsId, employeeId);
      EmployeeMonthlySalary lastrundetails = employeeMonthlySalaryService.getRecentEmployeeMonthlySalary(employeeId, rundetailsId);
      if (lastrundetails == null) {
          //first payroll for an employee
    	  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
          runDetailsReason.append(PRConstant.REASON_FOR_NEW_EMPLOYEE_FIRST_PAYROLL);
          log.debug(" inside @method identifyEmployeeVariance employeeId :{} VarianceReason :{} ",employee.getId(),runDetailsReason);
      }else {
          PayrollRun lastMonthPayroll = lastrundetails.getPayrollRun();
          
          EmployeeService employeeService=ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class); 

          Employee optEmployee = employeeService.findById(employeeId);
          
          
          if(optEmployee != null ) {
        	  employee = optEmployee;
        	  if (employee.getDateOfExit() != null && employee.getDateOfExit().before(payrollrun.getEndDate()) && employee.getDateOfExit().after(payrollrun.getStartDate())) {
                  //last payroll for an employee may be dateduration(employee notice period month dayscount) is less then month workingdays   
                	runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
                    runDetailsReason.append(PRConstant.REASON_FOR_RESIGNATION);
                    log.debug(" inside @method identifyEmployeeVariance employeeId :{} VarianceReason :{} ",employee.getId(),runDetailsReason);     
        	  }
        	  
          }
         
          Double currentMonthBasicSalary = identifyBasicSalaryDifference(rundetails, employeeId);
          Double lastMonthBasicSalary = identifyBasicSalaryDifference(lastrundetails, employeeId);
          log.debug("EmployeeID :{} currentMonthBasicSalary :{} lastMonthBasicSalary :{} ",employee.getId(),currentMonthBasicSalary,lastMonthBasicSalary);
          if (!compareDoubles(currentMonthBasicSalary, lastMonthBasicSalary)) {
          log.debug(
              "Inside PayrollRunServiceImpl identifyEmployeeVariance basic salary  currentMonthBasicSalary: {}  lastMonthBasicSalary :{} ",
              currentMonthBasicSalary, lastMonthBasicSalary);
	          runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
	          runDetailsReason.append(PRConstant.REASON_FOR_BASIC_SALARY);
	          log.debug(" inside @method identifyEmployeeVariance employeeId :{} VarianceReason :{} ",employee.getId(),runDetailsReason);     
	      }
        runDetailsReason = leaveCheck(payrollrun, runDetailsReason, employeeId,lastMonthPayroll );
        runDetailsReason = bonusCheck(rundetails, runDetailsReason, employeeId, lastrundetails);
        runDetailsReason = overtimeCheck(rundetails, runDetailsReason, employeeId, lastrundetails);
        runDetailsReason = otherEarningCheck(rundetails, runDetailsReason, employeeId, lastrundetails);
        runDetailsReason = otherDeductionCheck(rundetails, runDetailsReason, employeeId, lastrundetails);
        log.debug("Inside PayrollRun @method identifyEmployeeVariace runDetailsReason  employeeId :{} :{} ",runDetailsReason,employee.getId());
        
      }
      log.debug(
          "Inside PayrollRunServiceImpl runDetailsReason {}  ",
          runDetailsReason.toString());
	  String resultString = runDetailsReason.toString();
	     if(!resultString.equals("") &&  !resultString.isEmpty()){
	      rundetails.setVarianceReason(resultString);
           String llmReason=getLLMGeneratedReasonForEmployeeVariance(resultString);
	      log.debug("Inside PayrollRunServiceImpl identifyEmployeeVariance - employeeId :{} llmString: {} resultString :{}  ",employee.getId(), llmReason,resultString);
	      rundetails.setLlmVarianceReason(llmReason);  
		 }
	  rundetails = employeeMonthlySalaryService.update(rundetails);
     }
    }
  }catch(Exception ex) {
    log.error("ERInside PayrollRunServiceImpl identifyEmployeeVariance : {}",Utils.getStackTrace(ex));
  } 
}

private StringBuilder otherEarningCheck(EmployeeMonthlySalary rundetails, StringBuilder runDetailsReason, Integer employeeId,
		EmployeeMonthlySalary lastrundetails) {
	log.debug("Inside  PayrollRunServiceImpl  otherEarningCheck rundetails id :{}   employee id :{} runDetailsReason :{} lastrundetails :{}",
		      rundetails.getId(), employeeId, runDetailsReason, lastrundetails.getId());
	 log.debug(" Inside @otherEarningCheck :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	Double currentEarning = rundetails.getOtherEarning();
	Double lastEarning = lastrundetails.getOtherEarning();
	if (currentEarning != null && !currentEarning.equals(0.0) && (lastEarning == null ||  lastEarning.equals(0.0))) {
			  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
		    runDetailsReason.append(PRConstant.REASON_FOR_OTHER_EARNING);
		    log.debug(" inside @method otherEarningCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
		  }
	 else if(lastEarning!= null && !lastEarning.equals(0.0)  && (currentEarning == null ||  currentEarning.equals(0.0))) {
		  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
	      runDetailsReason.append(PRConstant.REASON_FOR_DECREASE_OTHER_EARNING);
	      log.debug(" inside @method otherEarningCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
	  }
	 else if(currentEarning != null && lastEarning !=null && !compareDoubles(currentEarning , lastEarning) ) {
		 if(currentEarning > lastEarning ) {
			 runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
			 runDetailsReason.append(PRConstant.REASON_FOR_OTHER_EARNING);
			    log.debug(" inside @method otherEarningCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
		 }
		 else {
			 runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
			 runDetailsReason.append(PRConstant.REASON_FOR_DECREASE_OTHER_EARNING);
			    log.debug(" inside @method otherEarningCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
		 }
	 }
	
	log.debug("Inside PayrollRunServiceImpl  otherEarningCheck runDetailsReason: {}",runDetailsReason );
	
     return runDetailsReason;
}

private StringBuilder otherDeductionCheck(EmployeeMonthlySalary rundetails , StringBuilder runDetailsReason, Integer employeeId,
		EmployeeMonthlySalary lastrundetails) {
	log.debug("Inside  PayrollRunServiceImpl  otherDeductionCheck rundetails id :{}   employee id :{} runDetailsReason :{} lastrundetails :{}",
		      rundetails.getId(), employeeId, runDetailsReason, lastrundetails.getId());
	 log.debug(" Inside @otherDeductionCheck :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	Double currentDeduction = rundetails.getOtherDeduction();
	Double lastDeduction = lastrundetails.getOtherDeduction();
	if (currentDeduction != null && !currentDeduction.equals(0.0) && (lastDeduction == null ||  lastDeduction.equals(0.0))) {
		log.info("Inside otherDeductionCheck got in current month ");
			  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
		    runDetailsReason.append(PRConstant.REASON_FOR_OTHER_DEDUCTION);
		    log.debug(" inside @method otherEarningCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
		  }
	 else if(lastDeduction != null && !lastDeduction.equals(0.0) && (currentDeduction == null ||  currentDeduction.equals(0.0))) {
		 log.info("Inside otherDeductionCheck got in last month ");
		  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
	      runDetailsReason.append(PRConstant.REASON_FOR_DECREASE_OTHER_DEDUCTION);
		    log.debug(" inside @method otherEarningCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
	  }
	 else if(currentDeduction != null && currentDeduction!=0 && lastDeduction!=0 && lastDeduction != null && ! compareDoubles(currentDeduction ,lastDeduction )) {
		 log.info("Inside otherDeductionCheck currentDeduction :{} lastDeduction:{} ",currentDeduction ,lastDeduction);
		 if(currentDeduction > lastDeduction ) {
			  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
		    runDetailsReason.append(PRConstant.REASON_FOR_OTHER_DEDUCTION); 
		    log.debug(" inside @method otherEarningCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
		 }
		 else if(currentDeduction < lastDeduction ){
			 runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
		     runDetailsReason.append(PRConstant.REASON_FOR_DECREASE_OTHER_DEDUCTION);
			    log.debug(" inside @method otherEarningCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
		 }
		 
	 }

	
	log.debug("Inside PayrollRunServiceImpl  otherDeductionCheck runDetailsReason: {}",runDetailsReason );
	
     return runDetailsReason;
}

private StringBuilder overtimeCheck(EmployeeMonthlySalary rundetails, StringBuilder runDetailsReason, Integer employeeId,
	    EmployeeMonthlySalary lastrundetails) {
	 log.debug(" Inside @overtimeCheck :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	log.debug("Inside  PayrollRunServiceImpl  overtimeCheck rundetails id :{}   employee id :{} runDetailsReason :{} lastrundetails :{}",
		      rundetails.getId(), employeeId, runDetailsReason, lastrundetails.getId());
	
	if((rundetails.getOvertime() !=null && !rundetails.getOvertime().equals(0.0) && (lastrundetails.getOvertime() == null ||  lastrundetails.getOvertime().equals(0.0)))
	 || (lastrundetails.getOvertime() !=null && !lastrundetails.getOvertime().equals(0.0) && (rundetails.getOvertime() == null || rundetails.getOvertime().equals(0.0)))) { 
		log.debug("Inside PayrollRunServiceImpl  overtimeCheck  currentRunDetailsOvertime : {} , lastRunDetailsOvertime: {}",
				rundetails.getOvertime(), lastrundetails.getOvertime());
		 runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
		 runDetailsReason.append(PRConstant.REASON_FOR_OVERTIME);	
		    log.debug(" inside @method identifyEmployeeVariance employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
	}
	else if(rundetails.getOvertime() !=null && lastrundetails.getOvertime() !=null && !compareDoubles(rundetails.getOvertime(), lastrundetails.getOvertime()) ) {
		log.debug("Inside PayrollRunServiceImpl  overtimeCheck elseif currentRunDetailsOvertime : {} , lastRunDetailsOvertime: {}",
				rundetails.getOvertime(), lastrundetails.getOvertime());
		runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
		runDetailsReason.append(PRConstant.REASON_FOR_OVERTIME);	
	    log.debug(" inside @method identifyEmployeeVariance employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
	}
	
	log.debug("Inside PayrollRunServiceImpl  overtimeCheck runDetailsReason: {}",runDetailsReason );
	
     return runDetailsReason;
}

private StringBuilder bonusCheck(EmployeeMonthlySalary rundetails, StringBuilder runDetailsReason, Integer employeeId,
    EmployeeMonthlySalary lastrundetails) {
	 log.debug(" Inside @bonusCheck :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
  log.debug(
      "Inside  PayrollRunServiceImpl  bonusCheck rundetails id :{}   employee id :{} runDetailsReason :{} lastrundetails :{}",
      rundetails.getId(), employeeId, runDetailsReason, lastrundetails.getId());
  if (rundetails.getRelocationAllowance() != null && !rundetails.getRelocationAllowance().equals(0.0)
      && (lastrundetails.getRelocationAllowance() == null ||  lastrundetails.getRelocationAllowance().equals(0.0))) {
	  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
    runDetailsReason.append(PRConstant.REASON_FOR_REALLOCATION_ALLOWANCE);
    log.debug(" inside @method bonusCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);    
  }
  else if(lastrundetails.getRelocationAllowance() != null && !lastrundetails.getRelocationAllowance().equals(0.0)
	      && (rundetails.getRelocationAllowance() == null ||  rundetails.getRelocationAllowance().equals(0.0))) {
	  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
      runDetailsReason.append(PRConstant.REASON_FOR_DECREASE_REALLOCATION_ALLOWANCE);
      log.debug(" inside @method bonusCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);    
  }
  if (rundetails.getSti() != null && !rundetails.getSti().equals(0.0) && (lastrundetails.getSti() == null ||lastrundetails.getSti().equals(0.0) )) {
	  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
    runDetailsReason.append(PRConstant.REASON_FOR_STI_ALLOWANCE);
    log.debug(" inside @method bonusCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
  }
  else if(lastrundetails.getSti() != null && !lastrundetails.getSti().equals(0.0) && (rundetails.getSti() == null ||rundetails.getSti().equals(0.0) )) {
	  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
	    runDetailsReason.append(PRConstant.REASON_FOR_DECREASE_STI_ALLOWANCE);
	    log.debug(" inside @method bonusCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
  }
  if (rundetails.getSignUpBonus() != null && !rundetails.getSignUpBonus().equals(0.0)
      && (lastrundetails.getSignUpBonus() == null || lastrundetails.getSignUpBonus().equals(0.0))) {
	  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
    runDetailsReason.append(PRConstant.REASON_FOR_SIGNUP_ALLOWANCE);
    log.debug(" inside @method bonusCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
  }
  else if(lastrundetails.getSignUpBonus() != null && !lastrundetails.getSignUpBonus().equals(0.0)
	      && (rundetails.getSignUpBonus() == null || rundetails.getSignUpBonus().equals(0.0))) {
	  runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
	  runDetailsReason.append(PRConstant.REASON_FOR_DECREASE_SIGNUP_ALLOWANCE);
	    log.debug(" inside @method bonusCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);
  }
  log.debug("Inside  PayrollRunServiceImpl  bonusCheck rundetails id :{} runDetailsReason:{}",rundetails.getId(),runDetailsReason);
  return runDetailsReason;
}

private StringBuilder leaveCheck(PayrollRun payrollrun, StringBuilder runDetailsReason, Integer employeeId,
    PayrollRun lastMonthPayroll) {
	 log.debug(" Inside @leaveCheck :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
  if (lastMonthPayroll != null) {
    log.debug(
        "Inside  PayrollRunServiceImpl  leaveCheck payrollrun id :{}   employee id :{} runDetailsReason :{} lastMonthPayroll :{}",
        payrollrun.getId(), employeeId, runDetailsReason, lastMonthPayroll.getId());
    Integer employeeWorkingDay = employeeSalaryStructureService
        .getWorkingDaysOfEmployee(payrollrun.getStartDate(), payrollrun.getEndDate(), employeeId);
 
    Integer leaveCount = payrollrun.getDurationDayCount() - employeeWorkingDay;
    log.debug("Inside PayrollRunServiceImpl @method leaveCheck EmployeeWorkingDay  :{} employeeID :{} ",employeeWorkingDay,employeeId,leaveCount);
    Integer employeeLastWorkingDay = employeeSalaryStructureService.getWorkingDaysOfEmployee(
        lastMonthPayroll.getStartDate(), lastMonthPayroll.getEndDate(), employeeId);
    Integer leaveLastCount = lastMonthPayroll.getDurationDayCount() - employeeLastWorkingDay;
    log.debug(
            "Inside  PayrollRunServiceImpl  leaveCount :{}   employee id :{} employeeWorkingDay :{} employeeLastWorkingDay :{} leaveLastCount :{} ",
            leaveCount, employeeId, employeeWorkingDay, employeeLastWorkingDay,leaveLastCount);
    if (!(leaveLastCount == leaveCount)) {
      runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
      runDetailsReason.append(PRConstant.REASON_FOR_LEAVES);
      runDetailsReason = appendWithCommaIfNeeded(runDetailsReason);
      runDetailsReason.append("Leave Lastcount :" + leaveLastCount + "Leave Currentcount :" + leaveCount);
      log.debug(" inside @method leaveCheck employeeId :{} VarianceReason :{} ",employeeId,runDetailsReason);   
    }
  }
  return runDetailsReason;
}

private Double identifyBasicSalaryDifference(EmployeeMonthlySalary rundetails, Integer employeeId) {
	 log.debug(" Inside @identifyBasicSalaryDifference :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
  Double basicSalary = 0.0;
  log.debug("Inside PayrollRunServiceImpl identifyBasicSalaryDifference rundetails id :{}   employee id :{}",
      rundetails.getId(), employeeId);
  EmployeeSalaryStructure employeeSalaryStructure = employeeSalaryStructureService
      .getEmployeeMappedSalaryStructure(employeeId);
  if (employeeSalaryStructure != null) {
    Date eSSStartDate = employeeSalaryStructure.getCreatedTime();
    Date runDetailDate = rundetails.getCreatedTime();
    log.debug("Inside PayrollRunServiceImpl identifyBasicSalaryDifference eSSStartDate :{}   runDetailDate:{}",
        eSSStartDate, runDetailDate);
    if (eSSStartDate.after(runDetailDate) ||  eSSStartDate.equals(runDetailDate)) { // after rundetails
      basicSalary = employeeSalaryStructure.getBasicSalary();
    } else {
      // may be rundetails createdTime more than 1month gap
      EmployeeSalaryStructureHistory employeeSalaryStructureHistory = employeeSalaryStructureHistoryService
          .getHistoryInGivenDate(runDetailDate, employeeId);
      if (employeeSalaryStructureHistory != null && employeeSalaryStructureHistory.getBasicSalary() != null
          && !employeeSalaryStructureHistory.getBasicSalary().equals(0.0)) {
        basicSalary = employeeSalaryStructureHistory.getBasicSalary();
      }
    }
  }
  log.debug(" Inside identifyBasicSalary Basic Salary :{} ",basicSalary);
  return basicSalary;
}

public boolean compareDoubles(Double a, Double b) {
  Long roundedA = Math.round(a);
  Long roundedB = Math.round(b);
  return roundedA == roundedB;
}
private  StringBuilder appendWithCommaIfNeeded(StringBuilder sb) {
    if (sb.length() > 0) {
        sb.append(",");
    }
    return sb;
}


@Override
public PayrollRun reExecutePayroll(Integer month, Integer year) {
	log.debug(" Inside @reExecutePayroll :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	 log.debug("Inside PayrollRunServiceImpl reExecutePayroll rundetails month :{}   year:{}",
			 month, year);
	 if(month!=null && year!=null&& month >= 1 && month <= 12) {
		  
		 try {
		 List<PayrollRun>list= payrollRunRepository.getPayrollRunByMonthAndYear(month,year);
		 List<Integer> payrollIds = new ArrayList<>();
	
		 EmployeeSalaryStructureService employeeSalaryStructureService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeSalaryStructureService.class);
		
		 for(PayrollRun payrollRun:list) {
			 payrollIds.add( payrollRun.getId());
		 }
		 
		 log.debug("Inside PayrollRunServiceImpl reExecutePayroll delete ids :{}  ",
				 payrollIds);
		
		 this.softBulkDelete(payrollIds);
		 
		 
		 PayrollRequestWrapper payrollRequestWrapper  = new PayrollRequestWrapper();
		 
		    YearMonth yearMonth = YearMonth.of(year, month);
	        LocalDate startDateLocal = yearMonth.minusMonths(1).atDay(21); // 21st of the previous month
	        LocalDate endDateLocal = yearMonth.atDay(20); // 20th of the current month

	        // Convert LocalDate to java.util.Date
	        Date startDate = Date.from(startDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
	        Date endDate = Date.from(endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
	       
	        log.debug("Inside PayrollRunServiceImpl reExecutePayroll rundetails startDate :{} endDate:{}",
	        		startDate, endDate);
	        
		    payrollRequestWrapper.setStartDate(startDate);
		    payrollRequestWrapper.setEndDate(endDate);
		 
		PayrollRun payrollRun =  employeeSalaryStructureService.executePayrollRun(payrollRequestWrapper);
		 
		return payrollRun;
		 }catch(Exception ex) {
			 log.error("Inside PayrollRunServiceImpl reExecutePayroll : {}", Utils.getStackTrace(ex), ex);
		 }
	 }
	return null;
}


@Override
 public Object getPayrollEmployeeMonthlySalary() {
 	log.debug("Inside PayrollRunServiceImpl getPayrollRunDetails rundetails");
	log.debug(" Inside @getPayrollEmployeeMonthlySalary :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
 	return payrollRunRepository.findEndMonthYearWithFlag();
}

 private List<EmployeeMonthlySalary> cancelBenefitByJson(List<EmployeeMonthlySalary> employeeMonthlySalary)
 {
	 log.info("Inside @class PayrollRunServiceImpl @method cancelBenefitByJson ");
		log.debug(" Inside @cancelBenefitByJson :{} customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	 ObjectMapper objectMapper = new ObjectMapper();   
     try {
    	 for(EmployeeMonthlySalary runDetail : employeeMonthlySalary)
         {
           String jsonString =runDetail.getBenefitObjectIdJson();
           
          try {
         	 HashMap<String, List<Integer>> deserializedMap = objectMapper.readValue(jsonString, new TypeReference<HashMap<String, List<Integer>>>() {});
 		    changingWorkFlowStage(deserializedMap);
         	 
          }
          catch (Exception e) {
      		log.error("Error while using json Inside @class PayrollRunServiceImpl @method cancelBenefitByJson :{} :{}",
   				 e.getMessage(),Utils.getStackTrace(e));
   	     throw new BusinessException();
 		    }
         }

	} 
     catch (Exception e) {
  		log.error("Error Inside @class PayrollRunServiceImpl @method cancelBenefitByJson :{} :{}",
				 e.getMessage(),Utils.getStackTrace(e));
	     throw new BusinessException();
	}	
     return employeeMonthlySalary;
 }
 
 
   private HashMap<String,List<Integer>> changingWorkFlowStage(HashMap<String,List<Integer>> deserializedMap)
   {
	   log.info("Inside @class PayrollRunServiceImpl @method changingWorkFlowStage ");
	   log.debug(" Inside @getWeightageConfigurationById  customerId is : {}", commonUtils.getCustomerId());
	   try {
		   for(Map.Entry<String, List<Integer>> entryMap: deserializedMap.entrySet())
	        {
	          List<Integer> listOfKeys = entryMap.getValue();
	          String key = entryMap.getKey();
	         
		   
		    if(key.equals(PRConstant.HEALTH_CLUB_BENEFIT) )
		    {
		    	HealthClubBenefitServiceImpl healthClubBenefitServiceImpl = ApplicationContextProvider.getApplicationContext().getBean(HealthClubBenefitServiceImpl.class);
		       healthClubBenefitServiceImpl.setWorkFlowStageForBulk(listOfKeys,PRConstant.APPROVED);
		    }
		    if(key.equals(PRConstant.EDUCATION_BENEFIT))
		    {
		    	 EducationalBenefitServiceImpl educationalBenefitServiceImpl = ApplicationContextProvider.getApplicationContext().getBean(EducationalBenefitServiceImpl.class); 
		    	 educationalBenefitServiceImpl.setWorkFlowStageForBulk(listOfKeys,PRConstant.APPROVED);
		    }
		    if(key.equals(PRConstant.CHILD_EDUCATION_BENEFIT))
		    {
		    	ChildEducationBenefitServiceImpl childEducationBenefitServiceImpl = ApplicationContextProvider.getApplicationContext().getBean(ChildEducationBenefitServiceImpl.class);
		    	childEducationBenefitServiceImpl.setWorkFlowStageForBulk(listOfKeys,PRConstant.APPROVED);  
		    }
		    if(key.equals(PRConstant.NEW_HIRE_BENEFIT))
		    {
		    	NewHireBenefitServiceImpl newHireBenefitServiceImpl = ApplicationContextProvider.getApplicationContext().getBean(NewHireBenefitServiceImpl.class);
		    	newHireBenefitServiceImpl.setWorkFlowStageForBulk(listOfKeys,PRConstant.APPROVED);
		    }
	        }
		   return deserializedMap;
	   }
	   catch(Exception e)
	   {
	  		log.error("Error Inside @class PayrollRunServiceImpl @method changingWorkFlowStage :{} :{}",
					 e.getMessage(),Utils.getStackTrace(e));
		     throw new BusinessException();
	   }
   }
   
   @Override
   public PayrollRun getPayrollByProcessInstanceId(String processInstanceId)
   {
	   try
	   {
		   log.debug(" Inside @getPayrollByProcessInstanceId :{}  customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
			if(processInstanceId!=null ) {
				Optional <PayrollRun> optionalPayrollRun = payrollRunRepository.findPayrollRunByProcessInstanceId(processInstanceId);
			     if(optionalPayrollRun.isPresent())
			     {
			    	 PayrollRun payrollRun = optionalPayrollRun.get();
			    	 return payrollRun;
			     }
			     else
			     {
			    		log.info("payrollRun not present ");
                           throw new BusinessException(" Payroll is soft deleted ");   
			     }
			
			}
			else
			{
				log.info("ProcessInstanceId not present ");
				throw new BusinessException(" ProcessInstanceId not present ");
			}

	   }
	   catch(Exception e)
	   {
	  		log.error("Error Inside @class PayrollRunServiceImpl @method getPayrollByProcessInstanceId :{} :{}",
					 e.getMessage(),Utils.getStackTrace(e));
		     throw new BusinessException();
	   }

   }

   @Override
   public PayrollRun getPayrollById(Integer payRollId)
   {
	   try
	   {
		   log.info("==============================================");
		   log.debug(" Inside @getPayrollById :{}  customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
			if(payRollId!=null ) {
				PayrollRun optionalPayrollRun = super.findById(payRollId);
			     if(optionalPayrollRun != null)
			     {
			    	 PayrollRun payrollRun = optionalPayrollRun;
			    	 return payrollRun;
			     }
			     else
			     {
			    		log.info("payrollRun not present ");
			    	 return null;
			     }
			
			}
			else
			{
				log.info("ProcessInstanceId not present ");
				return null;
			}

	   }
	   catch(Exception e)
	   {
	  		log.error("Error Inside @class PayrollRunServiceImpl @method getPayrollById :{} :{}",
					 e.getMessage(),Utils.getStackTrace(e));
		     throw new BusinessException();
	   }

   }


@Override
public PayrollRun updatePayrollRunWorkflowStage(PayrollRunWrapper payrollRunWrapper) {
	log.debug(" Inside @updatePayrollRunWorkflowStage :{}  customerId is : {}",ENTITY_NAME ,commonUtils.getCustomerId());
	log.debug("Inside method updatePayrollRunWorkflowStage Id : {}", payrollRunWrapper.getPayrollRunId());
	try {
		if (payrollRunWrapper.getPayrollRunId() != null) {
			PayrollRun optionalPayrollRun = super.findById(payrollRunWrapper.getPayrollRunId());
			if (optionalPayrollRun != null) {
				PayrollRun payrollRun = optionalPayrollRun;
				if (payrollRunWrapper.getWorkflowStage().equalsIgnoreCase("REJECTED") && payrollRun != null
						&& payrollRun.getProcessInstanceId() != null) {
					log.debug("Inside method updatePayrollRunWorkflowStage ProcessInstanceId found is  : {}",
							payrollRun.getProcessInstanceId());
					Workorder wordorder = workorderController
							.findByProcessInstanceId(payrollRun.getProcessInstanceId());
					log.debug("Inside method updatePayrollRunWorkflowStage wordorder found is  : {}", wordorder);
					if (wordorder != null && wordorder.getReferenceId() != null) {
						try {
							String response = workorderController.deleteWorkorder(wordorder.getReferenceId());
							log.debug(
									"Inside method updatePayrollRunWorkflowStage response from  deleteWorkorder api is : {}",
									response);
							 workflowActionsController.notifyActions(payrollRun.getProcessInstanceId()); 
							} catch (Exception e) {
							log.error("Facing error While deleting Workorder");
						}
					}
				}
				if(payrollRun!=null) {
				payrollRun.setWorkflowStage(payrollRunWrapper.getWorkflowStage());}
				return payrollRunRepository.save(payrollRun);
			} else {
				throw new BusinessException("PayrollRun with ID " + payrollRunWrapper.getPayrollRunId()+ " not found");
			}
		}
	} catch (Exception e) {
		throw new BusinessException("error while updating payrollRun work flow stage", e.getMessage());
	}
	return null;
}


}
