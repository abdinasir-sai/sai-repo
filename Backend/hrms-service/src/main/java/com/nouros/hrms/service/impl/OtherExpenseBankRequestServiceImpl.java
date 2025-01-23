package com.nouros.hrms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.orchestrator.utility.annotation.TriggerBpmnAspect;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.product.pii.filter.PropertyFilter;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.repository.OtherExpenseBankRequestRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.OtherExpenseBankRequestService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;

@Service
public class OtherExpenseBankRequestServiceImpl extends AbstractService<Integer,OtherExpenseBankRequest>
		implements OtherExpenseBankRequestService {

	public OtherExpenseBankRequestServiceImpl(GenericRepository<OtherExpenseBankRequest> repository) {
		super(repository, OtherExpenseBankRequest.class);
	}

	private static ObjectMapper objectMapper = null;
	
	@Autowired
	private OtherExpenseBankRequestRepository otherExpenseBankRequestRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	private static final Logger log = LogManager.getLogger(OtherExpenseBankRequestServiceImpl.class);

	@Override
	public void softDelete(int id) {

		OtherExpenseBankRequest otherExpenseBankRequest = super.findById(id);

		if (otherExpenseBankRequest != null) {

			OtherExpenseBankRequest otherExpenseBankRequest1 = otherExpenseBankRequest;
			otherExpenseBankRequestRepository.save(otherExpenseBankRequest1);

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
	public OtherExpenseBankRequest create(OtherExpenseBankRequest otherExpenseBankRequest) {
		log.info("inside @class OtherExpenseBankRequestServiceImpl @method create");
		return otherExpenseBankRequestRepository.save(otherExpenseBankRequest);
	}

	@Override
	public OtherExpenseBankRequest savePath(String type, String path, LocalDate localDate, Integer year,
			Integer weekNumber, String stage,String json) {
		log.info("inside @class OtherExpenseBankRequestServiceImpl @method savePath");
		try {
			OtherExpenseBankRequest otherExpenseBankRequest = new OtherExpenseBankRequest();
			otherExpenseBankRequest.setType(type);
			otherExpenseBankRequest.setWpsFilePath(path);
			Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			otherExpenseBankRequest.setDate(date);
			otherExpenseBankRequest.setYear(year);
			otherExpenseBankRequest.setWeekNum(weekNumber);
			otherExpenseBankRequest.setEmployeeIdList(json);
			otherExpenseBankRequest = otherExpenseBankRequestRepository.save(otherExpenseBankRequest);
		
			log.info("Data saved in the table ");
			 TriggerBpmnAspect triggerBpmnAspect = ApplicationContextProvider.getApplicationContext()
						.getBean(TriggerBpmnAspect.class);
				String objEmployeeReview = null;
				log.info("Going to Trigger BPMN for  otherExpenseBankRequest :{} ", 
						otherExpenseBankRequest.getId());
				try {
					objEmployeeReview = getObjectMapper().writeValueAsString(otherExpenseBankRequest);
				} catch (JsonProcessingException e) {
					log.error("Error while getting object value as string :{}", Utils.getStackTrace(e));
				}
				try {
					triggerBpmnAspect.triggerBpmnViaAPI(objEmployeeReview, "HRMS_APP_NAME", "OtherExpenseBankRequest");
					log.info(" triggerBpmnAspect completed otherExpenseBankRequest :{}  ", otherExpenseBankRequest.getWorkflowStage());
					activateAction(otherExpenseBankRequest);
				} catch (Exception e) {
					log.error("Error in Triggering Workflow :{} :{}",e.getMessage(),Utils.getStackTrace(e));
				
				}
				return otherExpenseBankRequest;
		} catch (Exception e) {
			log.error("Error inside @class OtherExpenseBankRequestServiceImpl @method savePath :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public String getPathForWps(String type, Integer weeknum, Integer year) {
		log.info("inside @Class OtherExpenseBankRequestServiceImpl @method getPathForWps ");
		try {
			log.debug(" Inside @getPathForWps  customerId is : {}", commonUtils.getCustomerId());
			String path = otherExpenseBankRequestRepository.getPath(type, weeknum, year,commonUtils.getCustomerId());
			return path;
		} catch (Exception e) {
			log.error("Error inside @class OtherExpenseBankRequestServiceImpl @method getPathForWps :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	public void addRequestIdForOtherExpenseRequest(String requestId, String workFlowStage) {
		log.info("inside @Class OtherExpenseBankRequestServiceImpl @method changeWorkFlowStage ");
		try {
			log.debug(" Inside @addRequestIdForOtherExpenseRequest  customerId is : {}", commonUtils.getCustomerId());
			Integer count = otherExpenseBankRequestRepository.addRequestIdFromWorkFlowStage(requestId, workFlowStage,commonUtils.getCustomerId());
			log.info("The Request ID is Updated in ", count);
		} catch (Exception e) {
			log.error("Error inside @class OtherExpenseBankRequestServiceImpl @method changeWorkFlowStage :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public List<Integer> getEmployeeIdList(String workFlowStage) {
		log.info("inside @Class OtherExpenseBankRequestServiceImpl @method getEmployeeIdList ");
		try {
			log.debug(" Inside @getEmployeeIdList  customerId is : {}", commonUtils.getCustomerId());
			List<String> employeeList = otherExpenseBankRequestRepository
					.getEmployeeListFromWorkFlowStage(workFlowStage,commonUtils.getCustomerId());
			if (employeeList != null && !employeeList.isEmpty()) {
				List<Integer> employeeIdList = getListFromStrings(employeeList);
				return employeeIdList;
			}
			return null;
		} catch (Exception e) {
			log.error("Error inside @class OtherExpenseBankRequestServiceImpl @method getEmployeeIdList :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private List<Integer> getListFromStrings(List<String> employeeList) {
		log.info("Inside @class OtherExpenseBankRequestServiceImpl @method getListFromStrings ");
		List<Integer> employeeIdList = new ArrayList<>();
		try {
			log.debug("the size of List is :{} ", employeeList.size());
			ObjectMapper objectMapper = new ObjectMapper();
			if (employeeList != null && !employeeList.isEmpty()) {
				for (String list : employeeList) {
					List<Integer> convertedList = objectMapper.readValue(list, new TypeReference<List<Integer>>() {});
					log.debug("the converted List is :{} ", convertedList);
					for (Integer id : convertedList) {
						employeeIdList.add(id);
					}
				}
			}
			employeeIdList = removeDuplicate(employeeIdList);
			return employeeIdList;
		} catch (Exception e) {
			log.error("Error inside @class OtherExpenseBankRequestServiceImpl @method getListFromStrings :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private List<Integer> removeDuplicate(List<Integer> employeeIdList) {
		try {
			log.info("Inside @class OtherExpenseBankRequestServiceImpl @method removeDuplicate ");
			HashMap<Integer, Integer> hashMap = new HashMap<>();
			for (Integer id : employeeIdList) {
				if (!hashMap.containsKey(id)) {
					hashMap.put(id, 1);
				}
			}
			List<Integer> employeeIdListnew = new ArrayList<>();
			for (Map.Entry<Integer, Integer> entryMap : hashMap.entrySet()) {
				employeeIdListnew.add(entryMap.getKey());
			}
			log.debug("The List is :{} ", employeeIdListnew);
			return employeeIdListnew;
		} catch (Exception e) {
			log.error("Error inside @class OtherExpenseBankRequestServiceImpl @method removeDuplicate :{} :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
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

	public void activateAction(OtherExpenseBankRequest otherExpenseBankRequest) {
       
		try {
			 log.debug("Inside class EmployeeReviewServiceImpl method activateAction  :{} ",otherExpenseBankRequest.getId());
			String processInstanceId = otherExpenseBankRequest.getProcessInstanceId();
			log.debug("Starting workflowActionController  processInstanceId :{} ", processInstanceId);
			WorkflowActionsController workflowActionController = ApplicationContextProvider.getApplicationContext()
					.getBean(WorkflowActionsController.class);
			workflowActionController.notifyActions(processInstanceId);
			log.info("Starting workflowActionController completed ");
		} catch (Exception e) {
          log.error("Error in Triggering Action :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
        throw new BusinessException();  
		}

	}

	@Override
	public List<OtherExpenseBankRequest> getListOfOtherExpenseBankRequestByStage(String workflowStage,LocalDate localEndDate,String[] benefitsName)
	{
		try
		{
			log.debug(" Inside @class OtherExpenseBankRequest @method getListOfOtherExpenseBankRequestByStage :{} :{} :{} ",workflowStage,localEndDate,benefitsName);
 

			int weekOfYear = localEndDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
			log.debug("The Week No. is :{} ",weekOfYear);
			log.debug(" Inside @getListOfOtherExpenseBankRequestByStage  customerId is : {}", commonUtils.getCustomerId());
      List<OtherExpenseBankRequest> listOfOtherExpenseBankRequest =  otherExpenseBankRequestRepository.getListOfOtherExpenseBankRequestByWorkflowStageAndWeekNum(workflowStage, weekOfYear,benefitsName,commonUtils.getCustomerId());
	log.debug(" Size of list :{} ",listOfOtherExpenseBankRequest.size());	
      return listOfOtherExpenseBankRequest;
		}
		catch(Exception e)
		{
			log.error("Error in getListOfOtherExpenseBankRequestByStage :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	        throw new BusinessException(); 
		}
	}
	
	public static LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}
	
	@Override
	public List<OtherExpenseBankRequest> getListOfOtherExpenseBankRequestByStageForEntity(String workflowStage,LocalDate localEndDate,String entityName)
	{
		try
		{
			log.debug(" Inside @class OtherExpenseBankRequest @method getListOfOtherExpenseBankRequestByStage :{} :{} :{} ",workflowStage,localEndDate,entityName);
 

			int weekOfYear = localEndDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
			log.debug("The Week No. is :{} ",weekOfYear);
			log.debug(" Inside @getListOfOtherExpenseBankRequestByStageForEntity  customerId is : {}", commonUtils.getCustomerId());
      List<OtherExpenseBankRequest> listOfOtherExpenseBankRequest =  otherExpenseBankRequestRepository.getListOfOtherExpenseBankRequestByWorkflowStageAndWeekNumAndEntity(workflowStage, weekOfYear,entityName,commonUtils.getCustomerId());
	log.debug(" Size of list :{} ",listOfOtherExpenseBankRequest.size());	
      return listOfOtherExpenseBankRequest;
		}
		catch(Exception e)
		{
			log.error("Error in getListOfOtherExpenseBankRequestByStage :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	        throw new BusinessException(); 
		}
	}

	
}
