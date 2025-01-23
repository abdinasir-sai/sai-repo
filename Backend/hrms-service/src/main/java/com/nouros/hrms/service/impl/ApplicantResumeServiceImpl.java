package com.nouros.hrms.service.impl;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.io.excel.Excel;
import com.enttribe.commons.io.excel.ExcelRow;
import com.enttribe.commons.io.excel.ExcelWriter;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.nouros.hrms.model.ApplicantResume;
import com.nouros.hrms.repository.ApplicantResumeRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantResumeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.util.report.ExcelUtils;

import jakarta.persistence.EntityNotFoundException;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service

public class ApplicantResumeServiceImpl  extends AbstractService<Integer,ApplicantResume> implements ApplicantResumeService {

	private static final String ENTITY_NAME = "ApplicantResume"; 

  private static final Logger log = LogManager.getLogger(ApplicantResumeServiceImpl.class);

	 
	public ApplicantResumeServiceImpl(GenericRepository<ApplicantResume> repository) {
		super(repository,ApplicantResume.class);
	}
	@Autowired
	private ApplicantResumeRepository applicantResumeRepository;
	
	@Autowired
	private CommonUtils commonUtils;
	
	@Override
    public byte[] export(List<ApplicantResume> applicantResume) throws IOException {		
		try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("templates/reports/ApplicantResume.xlsx");
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resourceAsStream)) {
               int rowCount = 1;
               return setTableData(applicantResume, xssfWorkbook, rowCount);
           }
  }
  
  
   private byte[] setTableData(List<ApplicantResume> applicantResumes,
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
        if (CollectionUtils.isNotEmpty(applicantResumes)) {
          for (ApplicantResume applicantResume : applicantResumes) {
            ExcelRow row = excelWriter.getOrCreateRow(0, rowCount);
            int index = 0;
            List<String> columns = tableColumn.get(entity);
            for(String column:columns) {
              if(column!=null) {   
              try {
              row.setCellValue(index, ExcelUtils.invokeGetter(applicantResume,column,identityColumnMapping).toString());
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
   
@Override
  public String importData(MultipartFile excelFile) throws IOException , InstantiationException, ClassNotFoundException {
    List<ApplicantResume> applicantResumes =new ArrayList<>();
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
        applicantResumes = saveData(workBook, columnNames);
      }
    else {
      log.info("columns and headers invalide");
      }
    if (CollectionUtils.isNotEmpty(applicantResumes)) {
    	applicantResumeRepository.saveAll(applicantResumes);
      return APIConstants.SUCCESS_JSON;
    }
    return APIConstants.FAILURE_JSON;
  }

    public List<ApplicantResume> saveData(Excel sheet,
      List<String> columnNames) {
    Iterator<ExcelRow> rowIterator = sheet.iterator();
    List<ApplicantResume> applicantResumeList = new ArrayList<>();
    rowIterator.next();
    while (rowIterator.hasNext()) {
      ExcelRow excelRow = rowIterator.next();
      ApplicantResume applicantResume = new ApplicantResume();
              int index = -1;
      for (String columnName : columnNames) {
        try {
          ExcelUtils.invokeSetter(applicantResume, columnName, excelRow.getString(++index));
                  } catch (InstantiationException e) {
          log.error("failed while going to set the value :{}", excelRow.getString(++index));
          log.error("InstantiationException occurred: {}", e.getMessage());
          
        } catch (ClassNotFoundException e) {
          log.error("ClassNotFoundException occurred: {}", e.getMessage());
        }
      }
      applicantResumeList.add(applicantResume);
    }
    return applicantResumeList;
  }
  
	
	
	@Override
    public void bulkDelete(List<Integer> list) {
	   if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				applicantResumeRepository.deleteById(list.get(i));
			
			}
		}
    }
	
	@Override
	public ApplicantResume create(ApplicantResume applicantResume)
	{
			return applicantResumeRepository.save(applicantResume);
	}


	@Override
	public List<ApplicantResume> findAllApplicantResumeByApplicantId(Integer applicantId) {
		 try {
		        log.info("Inside Method findAllApplicantResumeByApplicantId");
		        log.debug("Inside Method findAllApplicantResumeByApplicantId customerId is : {}", commonUtils.getCustomerId());
		        List<ApplicantResume> applicantResumesList =  applicantResumeRepository.findAllApplicantResumeByApplicantId(applicantId, commonUtils.getCustomerId());
		        if (applicantResumesList == null) {
		            throw new EntityNotFoundException("Appliant Resume with Applicant ID " + applicantId + " not found");
		        }
		        return applicantResumesList;
		    } catch (EntityNotFoundException e) {
		    	String contextInfo = "Entity not found while processing business trip"; 
		        throw new BusinessException("Entity not found. " + contextInfo, e);
		    } catch (Exception e) {
		        log.error("Error Occurred : {}  {} ",e.getMessage(),e.getStackTrace());
		        throw new BusinessException("Something Went Wrong");
		    }
	}
	
}
