package com.nouros.hrms.service.impl;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.io.excel.Excel;
import com.enttribe.commons.io.excel.ExcelRow;
import com.enttribe.commons.io.excel.ExcelWriter;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.model.Candidate;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.repository.CandidateRepository;
import com.nouros.hrms.repository.DepartmentRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.CandidateService;
import com.nouros.hrms.service.DepartmentService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.ExcelUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * This is a class named "CandidateServiceImpl" which is located in the package " com.nouros.hrms.service.impl", It appears to be an implementation of the "CandidateService" interface and it extends the "AbstractService" class, which seems to be a generic class for handling CRUD operations for entities. This class is annotated with @Service, indicating that it is a Spring Service bean.
This class is using Lombok's @Slf4j annotation which will automatically generate an Slf4j based logger instance, so it is using the Slf4j API for logging.
The class has a constructor which takes a single parameter of GenericRepository Candidate and is used to call the superclass's constructor.
This class have one public method public byte[] export(List of Candidate  Candidate) for exporting the Candidate data into excel file by reading the template and mapping the Candidate details into it.
It's using Apache POI library for reading and writing excel files, and has methods for parsing the json files for column names and identities , and it also used 'ExcelUtils' for handling the excel operations.
It also uses 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and 'APIConstants' from 'com.nouros.hrms.util'
* */










@Service
public class CandidateServiceImpl extends AbstractService<Integer,Candidate> implements CandidateService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for Candidate entities.
	 */
	 
   private static final Logger log = LogManager.getLogger(CandidateServiceImpl.class);

	 private static final String ENTITY_NAME = "Candidate"; 
	 
	public CandidateServiceImpl(GenericRepository<Candidate> repository) {
		super(repository,Candidate.class);
	}
	@Autowired
	private CandidateRepository candidateRepository;
	
	/**
     * This method is used to export the given list of Candidate objects into an excel file.
     * It reads an excel template 'Candidate.xlsx' from the resource folder 'templates/reports'
     * and maps the Candidate data onto the template and returns the generated excel file in the form of a byte array.
     * param Candidate - List of Candidate objects to be exported
     * @return byte[] - The generated excel file in the form of a byte array
     * @throws IOException - When the template file is not found or there is an error reading the file
     */
	@Override
    public byte[] export(List<Candidate> candidate) throws IOException {		
		try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("templates/reports/Candidate.xlsx");
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resourceAsStream)) {
               int rowCount = 1;
               return setTableData(candidate, xssfWorkbook, rowCount);
           }
  }
  
  
    /**
 * This method is responsible for setting the data of an Excel document, using a template and a list of Candidate objects.
 * The data is written to the template starting at the specified row number.
 * 
 * @param Candidate a List of Candidate objects, representing the data that will be written to the Excel document
 * @param templatePath an XSSFWorkbook object, representing the template Excel document that the data will be written to
 * @param rowCount an int, representing the starting row number where data will be written in the Excel document
 * @return a byte array of the Excel document after the data has been written to it.
 * @throws IOException if there is an issue reading or writing to the Excel document
 */

  /**This method appears to take in three parameters:

A List of Candidate objects, representing the data that will be written to the Excel document.
An XSSFWorkbook object, representing the template Excel document that the data will be written to.
An int, representing the starting row number where data will be written in the Excel document.
The method has a return type of byte array, which is the Excel document after the data has been written to it. The method also throws an IOException, which would be thrown if there is an issue reading or writing to the Excel document.

The method starts by creating some maps to hold data that will be used later:

tableColumn: a map that will hold the columns of the Excel table.
identityColumnMapping: a map that will hold the mapping of columns
templateHeaders: a map that will hold the headers of the excel template
then it calls ExcelUtils.parseMappeddJson(tableColumn,identityColumnMapping,templateHeaders); to get the values for the maps created.

Then it creates an instance of ExcelWriter which will write the data to the workbook, it set the active sheet to the first sheet of the workbook and check if Candidate list is not empty.

It then iterates over the list of Candidate objects and for each Candidate, it creates a new row in the Excel document at the specified row number.

It also retrieves the list of columns for the "Candidate" entity from the tableColumn map, and iterates over the columns.

For each column, it attempts to retrieve the value for that column from the current Candidate object using the ExcelUtils.invokeGetter method, passing in the current Candidate object, the column name and the identityColumnMapping.

The value returned by this method is then set as the value of the cell in the current row and column.
If an introspection exception occur it will print the stacktrace of the exception

After all the data is written to the Excel document, the method returns the Excel document as a byte array using excelWriter.toByteArray() and log "going to return file"
* */
   private byte[] setTableData(List<Candidate>candidate,
  XSSFWorkbook templatePath,
  int rowCount)throws IOException
  {
    Map<String,List<String>> tableColumn=new HashMap<>();
      String entity=ENTITY_NAME;
        Map<String, String> identityColumnMapping =new HashMap<>();
        Map<String,List<String>> templateHeaders = new HashMap<>();
      ExcelUtils.parseMappeddJson(tableColumn,identityColumnMapping,templateHeaders);
      log.info("table column map is :{}",tableColumn);
      try (ExcelWriter excelWriter = new ExcelWriter(templatePath)) {
        excelWriter.getWorkbook().setActiveSheet(0);
        if (CollectionUtils.isNotEmpty(candidate)) {
          for (Candidate candidateDetails : candidate) {
            ExcelRow row = excelWriter.getOrCreateRow(0, rowCount);
            int index = 0;
            List<String> columns = tableColumn.get(entity);
            for(String column:columns) {
              if(column!=null) {   
              try {
              row.setCellValue(index, ExcelUtils.invokeGetter(candidateDetails,column,identityColumnMapping).toString());
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
 * This method is responsible for importing data from an Excel file, specifically data related to Candidate objects.
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
    List<Candidate> candidates =new ArrayList<>();
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
        candidates = saveData(workBook, columnsMapping, columnNames);
      }
    else {
      log.info("columns and headers invalide");
      }
    if (CollectionUtils.isNotEmpty(candidates)) {
      candidateRepository.saveAll(candidates);
      return APIConstants.SUCCESS_JSON;
    }
    return APIConstants.FAILURE_JSON;
  }

/**
 * This method is responsible for saving data to the database, specifically data related to Candidate objects.
 * The method takes in an Excel object, which represents the sheet containing the data, a mapping of columns from the excel sheet to the Candidate class, and a list of column names.
 * The method uses the iterator for the sheet to read data row by row, create new Candidate objects, and set the properties of the Candidate objects using the column mapping and column names.
 * The method returns a list of Candidate objects that have been saved to the database.
 *
 * @param sheet an Excel object representing the sheet containing the data
 * @param columnMapping a map representing the mapping of columns from the excel sheet to the Candidate class
 * @param columnNames a list of column names of the excel sheet
 * @return a list of Candidate objects that have been saved to the database
 */

    public List<Candidate> saveData(Excel sheet, Map<String, String> columnMapping,
      List<String> columnNames) {
    Iterator<ExcelRow> rowIterator = sheet.iterator();
    List<Candidate> candidates = new ArrayList<>();
    rowIterator.next();
    while (rowIterator.hasNext()) {
      ExcelRow excelRow = rowIterator.next();
          Candidate candidate = new Candidate();
              int index = -1;
      for (String columnName : columnNames) {
        try {
          ExcelUtils.invokeSetter(candidate, columnName, excelRow.getString(++index));
                  } catch (InstantiationException e) {
          log.error("failed while going to set the value :{}", excelRow.getString(++index));
          log.error("InstantiationException occurred: {}", e.getMessage());
          
        } catch (ClassNotFoundException e) {
          log.error("ClassNotFoundException occurred: {}", e.getMessage());
        }
      }
   CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
      Integer workspaceId=customerInfo.getActiveUserSpaceId();
      candidate.setWorkspaceId(workspaceId); //done done
        Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		Department departmentOne = new Department();
		DepartmentService departmentService = ApplicationContextProvider.getApplicationContext().getBean(DepartmentService.class);
		int departmentId = candidate.getDepartment().getId();
		Department department = departmentService.findById(departmentId);
		if (department != null) {
			departmentOne = department;	
		}
		if(departmentOne.getName() != null || candidate.getCity()!= null)
		{
			String departmentName =transformValue(departmentOne.getName());
			log.debug("department for rule is : {} ", departmentName);
			mp.put("department",departmentName);
			String city =transformCity(candidate.getCity());
			log.debug("City for rule is : {} ", city);
			mp.put("city",city);
		}
        nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("candidateRuleWithLocation", mp, Status.ALLOCATED);
	      log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		  generatedName = nameGenerationWrapperV2.getGeneratedName();
		  candidate.setCandidateId(generatedName);
          candidates.add(candidate);
    }
    return candidates;
  }
  
	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;
	
	private static String transformValue(String name) {
        String[] words = name.split(" ");
        StringBuilder transformedString = new StringBuilder();
        if (words.length > 1) {
        for (int i = 0; i < Math.min(words.length, 3); i++) {
            transformedString.append(words[i].charAt(0));
           }
        }
        else {
	        transformedString.append(name.substring(0, Math.min(name.length(), 3)));
	    }
        transformedString = new StringBuilder(transformedString.toString().toUpperCase());
        return transformedString.toString();
     }
	 
	private static String transformCity(String cityName) {
        String modifiedString = cityName.replaceAll("\\s", "").toUpperCase();
        if (modifiedString.length() > 10) {
            modifiedString = modifiedString.substring(0, 10);
        }
        return modifiedString;
    }
	
		/**
* This method is responsible for soft-deleting an Candidate  record in the database.
* The method takes in an int id which represents the id of the Candidate  that needs to be soft-deleted.
* It uses the id to find the Candidate by calling the CandidateRepository.findById method.
* If the Candidate  is found, it sets the "deleted" field to true, save the Candidate  in the repository, and saves it in the database
*
* @param id an int representing the id of the Candidate  that needs to be soft-deleted
*/
	@Override
	public void softDelete(int id) {

		Candidate candidate = super.findById(id);

		if (candidate != null) {

			Candidate candidate1 = candidate;

			
candidate1.setDeleted(true);
CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		    Integer workspaceId=customerInfo.getActiveUserSpaceId();

			candidate1.setWorkspaceId(workspaceId); //done done
		    candidateRepository.save(candidate1);
			
		}
	}
	
		/**
* This method is responsible for soft-deleting multiple Candidate records in the database in bulk.
* The method takes in a List of integers, each representing the id of an Candidate that needs to be soft-deleted.
* It iterates through the list, calling the softDelete method for each id passed in the list.
*
* @param list a List of integers representing the ids of the Candidate that need to be soft-deleted
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
	public Candidate createWithNaming(Candidate candidate) {
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		if(candidate.getDepartment().getName() != null || candidate.getCity()!= null)
		{
			String department =transformValue(candidate.getDepartment().getName());
			log.debug("Department for rule is : {} ", department);
			mp.put("department",department);
			String city = transformCity(candidate.getCity());
			log.debug("City for rule is : {} ", city);
			mp.put("city",city);
		}
		nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("candidateRuleWithLocation", mp, Status.ALLOCATED);
	      log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
		  generatedName = nameGenerationWrapperV2.getGeneratedName();
		  candidate.setCandidateId(generatedName);
		   CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		    Integer workspaceId=customerInfo.getActiveUserSpaceId();

		  candidate.setWorkspaceId(workspaceId); //done done
		return candidateRepository.save(candidate);
				
	}
	
 /**
 * Creates a new vendor.
 *
 * @param candidate The candidate  object to create.
 * @return The created vendor object.
 */
	@Override
	public Candidate create(Candidate candidate)
	{
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
	    Integer workspaceId=customerInfo.getActiveUserSpaceId();
			candidate.setWorkspaceId(workspaceId); //done done
			return candidateRepository.save(candidate);
	}
	
	
	

}
