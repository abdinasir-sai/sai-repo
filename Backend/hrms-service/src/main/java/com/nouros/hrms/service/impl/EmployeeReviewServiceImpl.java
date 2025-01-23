package com.nouros.hrms.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeCompetenciesDetails;
import com.nouros.hrms.model.EmployeeGoalsDetails;
import com.nouros.hrms.model.EmployeePerformanceReviewCycle;
import com.nouros.hrms.model.EmployeeReview;
import com.nouros.hrms.repository.EmployeeReviewRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeCompetenciesDetailsService;
import com.nouros.hrms.service.EmployeeGoalsDetailsService;
import com.nouros.hrms.service.EmployeePerformanceReviewCycleService;
import com.nouros.hrms.service.EmployeeReviewService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.EmployeePerformanceReviewCycleWrapper;
import com.nouros.hrms.wrapper.SelfAssessmentWrapper;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.service.PayrollRunService;
import com.nouros.payrollmanagement.utils.PRConstant;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.annotation.TriggerBpmnAspect;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.product.pii.filter.PropertyFilter;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;

/**
 * This is a class named "EmployeeReviewServiceImpl" which is located in the
 * package " com.nouros.hrms.service.impl", It appears to be an implementation
 * of the "EmployeeReviewService" interface and it extends the "AbstractService"
 * class, which seems to be a generic class for handling CRUD operations for
 * entities. This class is annotated with @Service, indicating that it is a
 * Spring Service bean. This class is using Lombok's @Slf4j annotation which
 * will automatically generate an Slf4j based logger instance, so it is using
 * the Slf4j API for logging. The class has a constructor which takes a single
 * parameter of GenericRepository EmployeeReview and is used to call the
 * superclass's constructor. This class have one public method public byte[]
 * export(List of EmployeeReview EmployeeReview) for exporting the
 * EmployeeReview data into excel file by reading the template and mapping the
 * EmployeeReview details into it. It's using Apache POI library for reading and
 * writing excel files, and has methods for parsing the json files for column
 * names and identities , and it also used 'ExcelUtils' for handling the excel
 * operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class EmployeeReviewServiceImpl extends AbstractService<Integer,EmployeeReview> implements EmployeeReviewService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   EmployeeReview entities.
	 */

	private static final Logger log = LogManager.getLogger(EmployeeReviewServiceImpl.class);

	public EmployeeReviewServiceImpl(GenericRepository<EmployeeReview> repository) {
		super(repository, EmployeeReview.class);
	}

	private static ObjectMapper objectMapper = null;

	@Autowired
	private EmployeeReviewRepository employeeReviewRepository;

	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;
	
	@Autowired
	CustomerInfo customerInfo;
	
	@Autowired
	private CommonUtils commonUtils;

	/**
	 * Creates a new vendor.
	 *
	 * @param employeeReview The employeeReview object to create.
	 * @return The created vendor object.
	 */
	@Override
	public EmployeeReview create(EmployeeReview employeeReview) {
		log.info("inside @class EmployeeReviewServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		employeeReview.setWorkspaceId(workspaceId); // done done
		return employeeReviewRepository.save(employeeReview);
	}

	@Override
	public EmployeePerformanceReviewCycle createEmployeeReview(
			EmployeePerformanceReviewCycle employeePerformanceReviewCycle) {
		try {
			log.info("Inside @class  EmployeeReviewServiceImpl method createEmployeeReview  ");
			EmployeePerformanceReviewCycleService employeePerformanceReviewCycleService = ApplicationContextProvider
					.getApplicationContext().getBean(EmployeePerformanceReviewCycleService.class);
		 			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);
			Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
			String EmployeeFilter = hrmsSystemConfigMap.get(PRConstant.PERFORMANCE_LIST_EMPLOYEE);
			String[] listOfEmploymentType = EmployeeFilter.split(",");
			List<Employee> employeeList = employeeService.getEmployeeByEmploymentTypeList(listOfEmploymentType);
            List<EmployeeReview>  employeeReviewList  = new ArrayList<>(); 
        	CompletableFuture<Void> processFuture = CompletableFuture.runAsync(() -> {
            
            for (Employee employee : employeeList) {
				EmployeeReview employeeReview = new EmployeeReview();
				employeeReview.setEmployee(employee);
				employeeReview.setEmployeePerformanceReviewCycle(employeePerformanceReviewCycle);
				employeeReview.setStartDate(employeePerformanceReviewCycle.getStartDate());
				employeeReview.setEndDate(employeePerformanceReviewCycle.getEndDate());
				employeeReview.setEmployee2(employee.getReportingManager());
				 employeeReview = create(employeeReview);
				employeeReviewList.add(employeeReview);
				 TriggerBpmnAspect triggerBpmnAspect = ApplicationContextProvider.getApplicationContext()
						.getBean(TriggerBpmnAspect.class);
				String objEmployeeReview = null;
				log.info("Going to Trigger BPMN for EmployeeId :{} , EmployeeReview :{} ", employee.getId(),
						employeeReview.getId());
				try {
					objEmployeeReview = getObjectMapper().writeValueAsString(employeeReview);
				} catch (JsonProcessingException e) {
					log.error("Error while getting object value as string :{}", Utils.getStackTrace(e));
				}
				try {
					triggerBpmnAspect.triggerBpmnViaAPI(objEmployeeReview, "HRMS_APP_NAME", "EmployeeReview");
					log.info(" triggerBpmnAspect completed employeeReview :{}  ", employeeReview.getWorkflowStage());
					activateAction(employeeReview);
				} catch (Exception e) {
					log.error("Error in Triggering Workflow :{} :{}",e.getMessage(),Utils.getStackTrace(e));
				
				}
            }
            }).thenRunAsync(() -> {
            	for(EmployeeReview employeeReview : employeeReviewList)
            	{
            		Employee employee = employeeReview.getEmployee();
				 EmployeeCompetenciesDetailsService employeeCompetenciesDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeCompetenciesDetailsService.class); 
				 employeeCompetenciesDetailsService.createCompetenciesForEmployee(employee, employeeReview);
            	}
            	}).exceptionally(ex -> {
    				log.error("Error occurred in Employee Review : {}", Utils.getStackTrace(ex), ex);
    				 
    				return null;
    			});
			 
//            ExecutorService threadPool = Executors.newFixedThreadPool(employeeList.size());
//                
//            for (Employee employee :  employeeList) {
//               threadPool.submit(new Runnable() {
//                    public void run() {
//                    	log.debug("Inside Runnable method for EmployeeID :{} ",employee.getId());
//                    	log.debug("Active Threads :{}",Thread.activeCount());
//                    Boolean value =	createEmployeeReviewDemo(employee, employeePerformanceReviewCycle,employeeReviewList);
//                    log.debug("Employee Review For Employee id :{}  :{} ",employee.getId(),value);
//                     }
//                });
//            }
//             threadPool.shutdown();
// 
//            //threadPool.awaitTermination(60, TimeUnit.MILLISECONDS);
//
             return employeePerformanceReviewCycle;
		} catch (Exception e) {
			log.debug("Error inside class EmployeeReviewServiceImpl method createEmployeeReview :{} :{} ",
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

	public void activateAction(EmployeeReview employeeReview) {
       
		try {
			 log.debug("Inside class EmployeeReviewServiceImpl method activateAction  :{} ",employeeReview.getId());
			String processInstanceId = employeeReview.getProcessInstanceId();
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
	public EmployeeReview getEmployeeReviewByEmployeeAndDate(Integer employeeId , LocalDate currentDate)
	{
		try {
			log.debug("Inside getEmployeeReivewByEmployeeAndDate employeeID :{} Date :{} ",employeeId , currentDate);
			log.debug("Inside getEmployeeReivewByEmployeeAndDate customerId is : {}", commonUtils.getCustomerId());
			EmployeeReview employeeReview = employeeReviewRepository.getEmployeeReviewByEmployeeIdAndDate(employeeId, currentDate, commonUtils.getCustomerId());
		   log.debug("Inside method getEmployeeReviewByEmployeeAndDate employeeReview Id :{} ",employeeReview.getId());
		   return employeeReview;
		}
		catch(Exception e)
		{
			  log.error("Error inside getEmployeeReviewByEmployeeAndDate :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		        throw new BusinessException();  
		}
	}
	
	@Override
	public SelfAssessmentWrapper getEmployeeSelfAssessmentFormData(Integer employeeReviewId)
	{
		try {
			log.info("Inside @class EmployeeReviewServiceImpl @method getEmployeeSelfAssessmentFormData :{} ",employeeReviewId);
			EmployeeGoalsDetailsService employeeGoalsDetailsService =  ApplicationContextProvider.getApplicationContext().getBean(EmployeeGoalsDetailsService.class);
			List<EmployeeGoalsDetails> employeeGoalsList = employeeGoalsDetailsService.getEmployeeGoalsDetailsByEmployeeReviewId(employeeReviewId);
			EmployeeCompetenciesDetailsService employeeCompetenciesDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeCompetenciesDetailsService.class);
			List<EmployeeCompetenciesDetails> employeeCompetenciesList = employeeCompetenciesDetailsService.getAllEmployeeCompetencies(employeeReviewId);
			SelfAssessmentWrapper selfAssessmentWrapper = new SelfAssessmentWrapper();
			selfAssessmentWrapper.setEmployeeGoalDetailsList(employeeGoalsList);
			selfAssessmentWrapper.setEmployeeCompetenciesDetailsList(employeeCompetenciesList);
			selfAssessmentWrapper.setEmployeeReviewId(employeeReviewId);
			return selfAssessmentWrapper;
		}
		catch(Exception e)
		{
			  log.error("Error inside getEmployeeSelfAssessmentFormData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		        throw new BusinessException();  
		}
	}
	
	@Override
	public String updateEmployeePerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper)
	{
		try
		{
			log.info("Inside @class EmployeeReviewServiceImpl @method updateEmployeePerfomanceData employee Review Id :{} ",selfAssessmentWrapper.getEmployeeReviewId());
			EmployeeReview optionalEmployeeReview = super.findById(selfAssessmentWrapper.getEmployeeReviewId());
			if(optionalEmployeeReview != null)
			{
				EmployeeReview employeeReview = optionalEmployeeReview;
				List<EmployeeGoalsDetails> listOfGoals = selfAssessmentWrapper.getEmployeeGoalDetailsList();
				EmployeeGoalsDetailsService employeeGoalsDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeGoalsDetailsService.class);
				for(EmployeeGoalsDetails employeeGoalDetail : listOfGoals)
				{
					employeeGoalsDetailsService.update(employeeGoalDetail);
				}
				log.info("Inside @class EmployeeReviewServiceImpl @method updateEmployeePerfomanceData Employee Goals Updated ");
				List<EmployeeCompetenciesDetails> employeeCompetencyList = selfAssessmentWrapper.getEmployeeCompetenciesDetailsList();
				EmployeeCompetenciesDetailsService employeeCompeteciesDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeCompetenciesDetailsService.class);
				Double ratingSum = 0.0;
				for(EmployeeCompetenciesDetails employeeCompetenciesDetails : employeeCompetencyList)
				{
					log.debug(" The Rating for EmployeeReview :{} :{} ",employeeReview.getId(),employeeCompetenciesDetails.getEmployeeRating());
					ratingSum = ratingSum + employeeCompetenciesDetails.getEmployeeRating();
					employeeCompeteciesDetailsService.update(employeeCompetenciesDetails);
				}
				log.debug("The Competency sum :{} ",ratingSum);
				ratingSum = ratingSum/5;
				log.debug("The Competency avg :{} ",ratingSum);			
				employeeReview.setCompetenciesAverageEmployee(ratingSum);
				employeeReviewRepository.save(employeeReview);
				return APIConstants.SUCCESS_JSON;	
			}
			else
			   {
				   log.error("Inside @class EmployeeReviewServiceImpl @method updateEmployeePerfomanceData Employee Review not Found ");
				   throw new BusinessException("Inside @class EmployeeReviewServiceImpl @method updateEmployeePerfomanceData  Exception Employee Review not found ");
			   }
			
		}
		catch(Exception e)
		{
			  log.error("Error inside updateEmployeePerfomanceData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		        throw new BusinessException(); 
		}
		
	}
	
	// Code optimization
//	@Override
//	public String updateEmployeePerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper) {
//	    try {
//	        log.info("Inside @class EmployeeReviewServiceImpl @method updateEmployeePerfomanceData employee Review Id :{}", selfAssessmentWrapper.getEmployeeReviewId());
//
//	        // Fetch the employee review and check if it exists
//	        EmployeeReview employeeReview = employeeReviewRepository.findById(selfAssessmentWrapper.getEmployeeReviewId())
//	                .orElseThrow(() -> new BusinessException("Employee Review not found"));
//
//	        // Retrieve the services only once
//	        EmployeeGoalsDetailsService employeeGoalsDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeGoalsDetailsService.class);
//	        EmployeeCompetenciesDetailsService employeeCompetenciesDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeCompetenciesDetailsService.class);
//
//	        // Update employee goals
//	        List<EmployeeGoalsDetails> listOfGoals = selfAssessmentWrapper.getEmployeeGoalDetailsList();
//	        listOfGoals.forEach(employeeGoalsDetailsService::update);
//	        log.info("Employee Goals Updated");
//
//	        // Calculate the competencies average rating
//	        List<EmployeeCompetenciesDetails> employeeCompetencyList = selfAssessmentWrapper.getEmployeeCompetenciesDetailsList();
//	        double ratingSum = employeeCompetencyList.stream()
//	                .mapToDouble(EmployeeCompetenciesDetails::getEmployeeRating)
//	                .sum();
//	        double ratingAvg = ratingSum / 5;
//	        log.debug("Competency sum: {} | Competency avg: {}", ratingSum, ratingAvg);
//
//	        // Update employee review with the new average rating
//	        employeeReview.setCompetenciesAverageEmployee(ratingAvg);
//	        for(EmployeeCompetenciesDetails employeeCompetenciesDetails : employeeCompetencyList)
//				{
//					log.debug(" The Rating for EmployeeReview :{} :{} ",employeeReview.getId(),employeeCompetenciesDetails.getEmployeeRating());
//					employeeCompetenciesDetailsService.update(employeeCompetenciesDetails);
//				}
//	        employeeReviewRepository.save(employeeReview);
//
//	        return APIConstants.SUCCESS_JSON;
//	    } catch (BusinessException e) {
//	        log.error("EmployeeReview not found: {}", e.getMessage());
//	        throw e; // Propagate the business exception
//	    } catch (Exception e) {
//	        log.error("Error inside updateEmployeePerfomanceData: {}", Utils.getStackTrace(e));
//	        throw new BusinessException("Error updating employee performance data", e);
//	    }
//	}
//
//	
	@Override
	public String updateManagerPerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper)
	{
		try
		{
			log.info("Inside @class EmployeeReviewServiceImpl @method updateEmployeePerfomanceData employee Review Id :{} ",selfAssessmentWrapper.getEmployeeReviewId());
			 
			EmployeeReview optionalEmployeeReview =  super.findById(selfAssessmentWrapper.getEmployeeReviewId());
		   if(optionalEmployeeReview != null)
		   {
			   EmployeeReview employeeReview = optionalEmployeeReview;
			   List<EmployeeGoalsDetails> listOfGoals = selfAssessmentWrapper.getEmployeeGoalDetailsList();
				EmployeeGoalsDetailsService employeeGoalsDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeGoalsDetailsService.class);
				for(EmployeeGoalsDetails employeeGoalDetail : listOfGoals)
				{
					employeeGoalsDetailsService.update(employeeGoalDetail);
				}
				log.info("Inside @class EmployeeReviewServiceImpl @method updateEmployeePerfomanceData Employee Goals Updated ");
				List<EmployeeCompetenciesDetails> employeeCompetencyList = selfAssessmentWrapper.getEmployeeCompetenciesDetailsList();
				EmployeeCompetenciesDetailsService employeeCompeteciesDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeCompetenciesDetailsService.class);
				Double ratingSum = 0.0;
				for(EmployeeCompetenciesDetails employeeCompetenciesDetails : employeeCompetencyList)
				{
					ratingSum = ratingSum + employeeCompetenciesDetails.getManagerRating();
					employeeCompeteciesDetailsService.update(employeeCompetenciesDetails);
				}
				log.debug("The Competency sum :{} ",ratingSum);
				ratingSum = ratingSum/5;
				log.debug("The Competency avg :{} ",ratingSum);			
				employeeReview.setCompetenciesAverageReviewer(ratingSum);
				employeeReviewRepository.save(employeeReview);				   
				return APIConstants.SUCCESS_JSON;
		   }
		   else
		   {
			   log.error("Inside @class EmployeeReviewServiceImpl @method updateEmployeePerfomanceData Employee Review not Found ");
			   throw new BusinessException("Inside @class EmployeeReviewServiceImpl Exception Employee Review not found ");
		   }
		}
		catch(Exception e)
		{
			  log.error("Error inside updateEmployeePerfomanceData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		        throw new BusinessException(); 
		}
		
	}
	
	
	@Override
	public String optionalUpdatePerfomanceData(SelfAssessmentWrapper selfAssessmentWrapper)
	{
		try
		{
			log.debug("Inside @class EmployeeReviewServiceImpl @method optionalUpdatePerformanceData employeeReview Id :{} ",selfAssessmentWrapper.getEmployeeReviewId());
			EmployeeReview optionalEmployeeReview =  super.findById(selfAssessmentWrapper.getEmployeeReviewId());
			   if(optionalEmployeeReview != null)
			   {
				   List<EmployeeGoalsDetails> listOfGoals = selfAssessmentWrapper.getEmployeeGoalDetailsList();
					EmployeeGoalsDetailsService employeeGoalsDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeGoalsDetailsService.class);
					for(EmployeeGoalsDetails employeeGoalDetail : listOfGoals)
					{
						employeeGoalsDetailsService.update(employeeGoalDetail);
					}
					log.info("Inside @class EmployeeReviewServiceImpl @method optionalUpdatePerformanceData Employee Goals Updated ");
					List<EmployeeCompetenciesDetails> employeeCompetencyList = selfAssessmentWrapper.getEmployeeCompetenciesDetailsList();
					EmployeeCompetenciesDetailsService employeeCompeteciesDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeCompetenciesDetailsService.class);
					for(EmployeeCompetenciesDetails employeeCompetenciesDetails : employeeCompetencyList)
					{
						employeeCompeteciesDetailsService.update(employeeCompetenciesDetails);
					}	   
					log.info("Competencies Updated ");
					return APIConstants.SUCCESS_JSON;
			   }
			   else
			   {
				   log.error("Inside @class EmployeeReviewServiceImpl @method optionalUpdatePerformanceData Employee Review not Found ");
				   throw new BusinessException("Exception Employee Review not found ");
			   }		
		}
		catch(Exception e)
		{
			  log.error("Error inside optionalUpdatePerfomanceData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		        throw new BusinessException(); 
		}
		
	}
	
	
	public Boolean createEmployeeReviewDemo(Employee employee,EmployeePerformanceReviewCycle employeePerformanceReviewCycle,List<EmployeeReview> employeeReviewList)
	{
		try
		{
			log.debug("Inside @class EmployeeReviewServiceImp @method createEmployeeReview employeeId :{}  ",employee.getId());
			EmployeeReview employeeReview = new EmployeeReview();
			employeeReview.setEmployee(employee);
			employeeReview.setEmployeePerformanceReviewCycle(employeePerformanceReviewCycle);
			employeeReview.setStartDate(employeePerformanceReviewCycle.getStartDate());
			employeeReview.setEndDate(employeePerformanceReviewCycle.getEndDate());
			employeeReview.setEmployee2(employee.getReportingManager());
			 employeeReview = create(employeeReview);
			employeeReviewList.add(employeeReview);
 			 EmployeeCompetenciesDetailsService employeeCompetenciesDetailsService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeCompetenciesDetailsService.class); 
			 employeeCompetenciesDetailsService.createCompetenciesForEmployee(employee, employeeReview);
			 TriggerBpmnAspect triggerBpmnAspect = ApplicationContextProvider.getApplicationContext()
					.getBean(TriggerBpmnAspect.class);
			String objEmployeeReview = null;
			log.info("Going to Trigger BPMN for EmployeeId :{} , EmployeeReview :{} ", employee.getId(),
					employeeReview.getId());
			try {
				objEmployeeReview = getObjectMapper().writeValueAsString(employeeReview);
			} catch (JsonProcessingException e) {
				log.error("Error while getting object value as string :{}", Utils.getStackTrace(e));
			}
			try {
				triggerBpmnAspect.triggerBpmnViaAPI(objEmployeeReview, "HRMS_APP_NAME", "EmployeeReview");
				log.info(" triggerBpmnAspect completed employeeReview :{}  ", employeeReview.getWorkflowStage());
				activateAction(employeeReview);
			} catch (Exception e) {
				log.error("Error in Triggering Workflow :{} :{}",e.getMessage(),Utils.getStackTrace(e));
			
			}
			return true;
		}
		catch(Exception e)
		{
			  log.error("Error inside createEmployeeReview :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		        throw new BusinessException(); 
	
		}
	}
	
	@Override
	public List<EmployeeReview> getTopEmployeeReviewByPerformanceScore()
	{
		try
		{
			log.info("Inside @class EmployeeReviewServiceImpl @method getTopEmployeeReviewByPerformanceScore ");
			List<EmployeeReview> employeeReviewList = employeeReviewRepository.getEmployeeReviewListByPerformanceScore(commonUtils.getCustomerId());
			log.debug(" Size of EmployeeReview List :{} ",employeeReviewList.size());
			return employeeReviewList;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class EmployeeReviewServiceImpl @method getTopEmployeeReviewByPerformanceScore :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
}



