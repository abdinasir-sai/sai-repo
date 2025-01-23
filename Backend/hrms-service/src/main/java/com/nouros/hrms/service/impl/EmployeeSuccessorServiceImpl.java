package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enttribe.commons.ai.model.rag.VectorMetaData;
import com.enttribe.commons.ai.rag.VectorService;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeSuccessor;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.model.Successor;
import com.nouros.hrms.repository.EmployeeSuccessorRepository;
import com.nouros.hrms.service.DesignationService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.EmployeeSuccessorService;

import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.service.SuccessorService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.wrapper.EmployeeSuccessorWrapper;
 
 

@Service
public class EmployeeSuccessorServiceImpl extends AbstractService<Integer,EmployeeSuccessor> implements EmployeeSuccessorService{

 

	@Autowired
	private VectorService vectorService; 

	
	@Autowired
	private EmployeeSuccessorRepository employeeSuccessorRepository;
	     
	
	    public static final String MULTI_AGENT_PLAN_COLLECTION_NAME = "HOW_TO_PLANS_FOR_MULTI_AGENT";
	    
	    public static final String DOCUMENT_SECTIONS_COLLECTION_NAME = "DOCUMENT_SECTIONS";
	    
	private static final Logger log = LogManager.getLogger(EmployeeSuccessorServiceImpl.class);
	
	protected EmployeeSuccessorServiceImpl(com.nouros.hrms.repository.generic.GenericRepository<EmployeeSuccessor> repository) {
	        super(repository, EmployeeSuccessor.class);
	    }

	@Override
  public List<EmployeeSuccessorWrapper> getPotentialSuccessorList(Integer designationId)
	{
		SuccessorService successorService = ApplicationContextProvider.getApplicationContext().getBean(SuccessorService.class); 
		EmployeeService employeeService =  ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
		try {
			log.info("Inside @class EmployeeSuccessorServiceImpl @method getPotentialSuccessorList ");
             DesignationService designationService = ApplicationContextProvider.getApplicationContext().getBean(DesignationService.class);
             Designation designation =  designationService.findById(designationId);
             String designationString = createDesignationJson(designation);
              log.debug(" Json Object :{} ",designationString);
 			VectorMetaData vectorMetaData = new VectorMetaData();
 			vectorMetaData.setCollectionName("HRMS_employee");
 	        SearchRequest searchRequest =SearchRequest.defaults().withQuery(designationString).withTopK(10);
           List<EmployeeSuccessorWrapper > employeeSuccessorWrapperList = new ArrayList<>();
 	        List<Document> documents = vectorService.similaritySearch(vectorMetaData, searchRequest);
        if(!documents.isEmpty())
        {
        	Successor successor = new Successor();
        	successor.setDesignation(designation);
        	successor = successorService.create(successor);
              for(Document document : documents)
              {
            	 Map<String,Object > metaDataMap =  document.getMetadata();
            	 log.debug("Metdata map :{} ",metaDataMap.toString()) ;
                 EmployeeSuccessorWrapper employeeSuccessorWrapper = new EmployeeSuccessorWrapper();
                  Integer employeeId = null;
                  Object employeeIdObj = metaDataMap.get("employee_id");
                  if (employeeIdObj != null) {
                      try 
                      {employeeId = Integer.parseInt(employeeIdObj.toString());} 
                      
                      catch (NumberFormatException e) 
                      {log.warn("Invalid employee_id format: {}. Using default value -1", employeeIdObj, e); }
                  }  
                  
                  log.debug("Employee id for document :{} :{} ", document.getId(), employeeId);
                   Double percentage = null;
                  Object distanceObj = metaDataMap.get("distance");
                  if (distanceObj != null) {
                      try {
                          percentage = Double.parseDouble(distanceObj.toString()) * 100;
                      } catch (NumberFormatException e) {
                          log.warn("Invalid distance format: {}. Using default percentage 0.0", distanceObj, e);
                          percentage = 0.0; // Default value for percentage
                      }
                  } else {
                      log.warn("distance is missing. Using default percentage 0.0");
                      percentage = 0.0; // Default value for percentage
                  }
                  log.debug("Percentage is :{} ", percentage);;
            	 Employee employee = employeeService.findById(employeeId);
            	 EmployeeSuccessor employeeSuccessor = new EmployeeSuccessor();
           	  employeeSuccessor.setSuccessor(successor);
           	  employeeSuccessor.setEmployee(employee);
           	  //employeeSuccessor.setEmployeeScore(score);
           	employeeSuccessor = employeeSuccessorRepository.save(employeeSuccessor);
           	employeeSuccessorWrapper.setEmployeeSuccessor(employeeSuccessor);
           	employeeSuccessorWrapper.setPercentage(percentage);
           	employeeSuccessorWrapperList.add(employeeSuccessorWrapper);
              }
        	
        }
        else
        {
        	log.info("Document Size :{} ",documents.size() );
        }
     
 		log.debug("List :{} ",documents);
             return employeeSuccessorWrapperList;
		}
		catch(Exception e)
		{
			log.error("Error inside @class EmployeeSuccessorServiceImpl @method getPotentialSuccessorList :{} :{}",e.getMessage(),Utils.getStackTrace(e));
            throw new BusinessException();
		}
	}
	private String createDesignationJson(Designation designation)
	{
	 try
	 {
			log.debug("Designation is : {}", designation);
			List<String> values = new ArrayList<>();
//			 addValueIfNotNull(designationMap,"core_capability",designation.getCoreCapabilities());
//			addValueIfNotNull(designationMap,"job_grade",designation.getJobGrade());
//			addValueIfNotNull(designationMap,"experience_education",designation.getExperienceAndEducation());
//			addValueIfNotNull(designationMap,"job_description",designation.getJobDescription());
			values.add(designation.getCoreCapabilities());
			values.add(String.valueOf(designation.getJobGrade()));
			values.add(designation.getExperienceAndEducation());
			values.add(designation.getJobDescription());
			log.debug("values formed is : {} ", values);
			return String.join(",", values);
	 }catch(Exception e)
	 {
			log.error("Error @class EmployeeServiceImpl @method createEmployeeJson  :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
	 }
	}
	
	
		 
//    public List<Document> searchVector(String embeddingCollection, String query, Integer topRecord, Double threshold, Filter.Expression expression) {
//        log.info("Going to search {} collection for query {} with thresold {} for metadata {}",embeddingCollection,query,threshold, expression);
//        
//        long timestamp=System.currentTimeMillis();
//        List<Document> documents=new ArrayList<>();
//       // VectorStore vectorStore = getVectorStore(embeddingCollection);
//
//        SearchRequest searchRequest =SearchRequest.defaults().withQuery(query).withTopK(topRecord);
//              log.debug(" Search request Query :{} ",searchRequest.getQuery());
//        if (threshold != null){
//            searchRequest=searchRequest.withSimilarityThreshold(threshold);
//        }else{
//            searchRequest=searchRequest.withSimilarityThreshold(SearchRequest.SIMILARITY_THRESHOLD_ACCEPT_ALL);
//        }
//
//        if (expression != null){
//            searchRequest=searchRequest.withFilterExpression(expression);
//        }
//
//        log.debug("Search Request  :{} ",searchRequest);
//        documents=knowledgeGraphVectorStore1.similaritySearch(searchRequest);
//        log.info("Sematic search done in {} ms",(System.currentTimeMillis()-timestamp));
//         log.debug("Value of Document :{} ",documents);
//           return documents;
//    }
	
//	public VectorStore  getVectorStore(String collection) {
// 
//            return this.knowledgeGraphVectorStore;
//      }

}
