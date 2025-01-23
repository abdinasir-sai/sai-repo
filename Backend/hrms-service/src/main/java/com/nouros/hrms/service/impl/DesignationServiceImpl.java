package com.nouros.hrms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.commons.ai.chat.AiChatModel;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.repository.DesignationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.DesignationService;
import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

/**
 * This is a class named "DesignationServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "DesignationService" interface and it extends the "AbstractService"
 * class, which seems to be a generic class for handling CRUD operations for
 * entities. This class is annotated with @Service, indicating that it is a
 * Spring Service bean. This class is using Lombok's @Slf4j annotation which
 * will automatically generate an Slf4j based logger instance, so it is using
 * the Slf4j API for logging. The class has a constructor which takes a single
 * parameter of GenericRepository Designation and is used to call the
 * superclass's constructor. This class have one public method public byte[]
 * export(List of Designation Designation) for exporting the Designation data
 * into excel file by reading the template and mapping the Designation details
 * into it. It's using Apache POI library for reading and writing excel files,
 * and has methods for parsing the json files for column names and identities ,
 * and it also used 'ExcelUtils' for handling the excel operations. It also uses
 * 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and
 * 'APIConstants' from 'com.nouros.hrms.util'
 */

@Service
public class DesignationServiceImpl extends AbstractService<Integer,Designation> implements DesignationService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Designation entities.
	 */

	@Autowired
	  private CommonUtils commonUtils;

	
	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;
	
	@Autowired
	AiChatModel aiChatModel;


	

	private static final Logger log = LogManager.getLogger(DesignationServiceImpl.class);

	public DesignationServiceImpl(GenericRepository<Designation> repository) {
		super(repository, Designation.class);
	}

	@Autowired
	private DesignationRepository designationRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param designation The designation object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Designation create(Designation designation) {
		log.info("inside @class DesignationServiceImpl @method create");
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		try {
			nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("designationRule", mp,
					Status.ALLOCATED);
			log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
			generatedName = nameGenerationWrapperV2.getGeneratedName();
			designation.setDesignationCode(generatedName);
		} catch (Exception e) {
			logger.error("Failed to create/generate Naming Id For Designation");
		}

		return designationRepository.save(designation);
	}
	
	
	@Override
	public List<Designation> getDesignationForSuccession()
	{
		try
		{
			log.info("Inside @class DesignationServiceImpl @method getDesignationForSuccession");
	        HrmsSystemConfigService hrmsSystemConfigService = ApplicationContextProvider.getApplicationContext().getBean(HrmsSystemConfigService.class);
	        String designationFinalWorkflowStage = hrmsSystemConfigService.getValue(PRConstant.DESIGNATION_FINAL_STAGE);
	        log.debug("Value of Designation Final WorkflowStage :{} ",designationFinalWorkflowStage);
			List<Designation> designationList = designationRepository.getDesignationListByWorkflowStage(designationFinalWorkflowStage);
			log.debug(" Size of Designation List :{} ",designationList);
			return designationList;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class DesignationServiceImpl @method getDesignationForSuccession :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		 throw new BusinessException();
		}
	}
	
	 

	@Override
	public Designation setJobLevelForDesignationRecursively(String designationName) {
		log.info("Inside @class DesignationServiceImpl @method setJobLevelForDesignationRecursively");
		
//		Designation designation;
//		
//		if(designationName != null) {
//			log.debug("designationName is : {} ,and customerid is : {} ",designationName,commonUtils.getCustomerId());
//			designation = designationRepository.findByDesignationName(designationName, commonUtils.getCustomerId());
//			log.debug("@method setJobLevelForDesignationRecursively designation : {}",designation);
//		}else {
//			log.debug("No Designation Found By Given Name");
//			return null;
//		}
		
		return null;
		
	}
	
	@Override
	public JSONObject getJsonObjectForDesignation(String designationName)
	{
 	 JobOpeningService jobOpeningService = ApplicationContextProvider.getApplicationContext().getBean(JobOpeningService.class);
		try
		{
			log.debug("Inside @class DesignationServiceImpl @method getJsonObjectForDesignation DesingationName :{} ",designationName);;
			List<Designation> designations = designationRepository.findByName(designationName, commonUtils.getCustomerId());
			Designation designation = designations.get(0);
			log.debug("Desigantion Id recevied :{} ",designation);
			if(designation != null && designation.getJobDescription()!=null )
			{
				Map<String, Object> inputMap = new HashMap<>();
				inputMap.put("input", designation.getJobDescription());
				String response = aiChatModel.chatCompletion("HRMS_APP_NAME-Prompt-Designation_value_prompt-v-1",inputMap);
				log.debug("Reponse is :{} ",response);
				JSONObject jsonObject = new JSONObject(response) ;
			log.debug("Value of jsonObject :{} ",jsonObject);
			String skills = jsonObject.getString("Skills");
			String coreCapability = jsonObject.getString("Core Capability");
			JSONArray responsibilityArray = jsonObject.getJSONArray("Responsibility");
			String ReponsibilityString =getReponsibility(responsibilityArray);
			String experienceAndEducation = jsonObject.getString("experienceAndEducation");
			List<JobOpening> jobOpenings = jobOpeningService.getJobOpeningByDesignation(designation.getId());
	          for(JobOpening jobOpening : jobOpenings)
	          {
	        	  jobOpening.setSkills(skills);
	        	  jobOpening.setCoreCapabilities(coreCapability);
	        	  jobOpening.setExperienceAndEducation(experienceAndEducation);
	        	  jobOpening.setResponsibilities(ReponsibilityString);
	        	  jobOpeningService.create(jobOpening);
	          }
			return jsonObject;
			}
		return null;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class DesignationServiceImpl @method getJsonObjectForDesignation :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
            return null;
		}
	}
  
	private String getReponsibility(JSONArray responsibilityArray)
	{
		try {
			log.debug("Inside @class DesingationServiceImpl @method getReponsibility responsibilityArray:{} ",responsibilityArray);
			 StringBuilder responsibilityString = new StringBuilder();
		        
		        for (int i = 0; i < responsibilityArray.length(); i++) {
		            JSONObject responsibility = responsibilityArray.getJSONObject(i);
		            String title = responsibility.getString("title");
		            String description = responsibility.getString("description");
		            
		            // Concatenate the title and description into the string
		            responsibilityString.append("Title: ").append(title)
		                                .append("\nDescription: ").append(description)
		                                .append("\n\n");
		        }

		        // Convert the StringBuilder to String and print it
		        String responsibilities = responsibilityString.toString();
		       
		        log.debug("Repsonsibilty :{} ",responsibilities);
		        return responsibilities;
		}
		catch(Exception e)
		{
		log.error("Error inside @DesigantionServiceImpl @method getReponsibility :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		throw new BusinessException();
		}
	}

 }
