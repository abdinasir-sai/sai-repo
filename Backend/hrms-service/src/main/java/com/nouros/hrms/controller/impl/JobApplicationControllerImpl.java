package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.JobApplicationController;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.service.JobApplicationService;
import com.nouros.hrms.wrapper.JobApplicationDto;
import com.nouros.hrms.wrapper.JobOpeningsDto;

import jakarta.validation.Valid;


/**

This class represents the implementation of the JobApplicationController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the JobApplicationController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the JobApplicationService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(JobApplication JobApplication): creates an JobApplication and returns the newly created JobApplication.
count(String filter): returns the number of JobApplication that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of JobApplication that match the specified filter, sorted according to the specified orderBy
and orderType.
update(JobApplication JobApplication): updates an JobApplication and returns the updated JobApplication.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of JobApplication with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/JobApplication")
//@Tag(name="/JobApplication",tags="JobApplication",description="JobApplication")
public class JobApplicationControllerImpl implements JobApplicationController {

  private static final Logger log = LogManager.getLogger(JobApplicationControllerImpl.class);
  
  @Autowired
  private JobApplicationService jobApplicationService;
  

	
  @Override
  @TriggerBPMN(entityName = "JobApplication", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public JobApplication create(JobApplication jobApplication) {
	  log.info("inside @class JobApplicationControllerImpl @method create");
    return jobApplicationService.create(jobApplication);
  }

  @Override
  public Long count(String filter) {
    return jobApplicationService.count(filter);
  }

  @Override
  public List<JobApplication> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return jobApplicationService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public JobApplication update(JobApplication jobApplication) {
    return jobApplicationService.update(jobApplication);
  }

  @Override
  public JobApplication findById(Integer id) {
    return jobApplicationService.findById(id);
  }

  @Override
  public List<JobApplication> findAllById(List<Integer> id) {
    return jobApplicationService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    jobApplicationService.deleteById(id);
  }
  


    @Override
    public String summariseResumeByJobOpeningForJobApplication(JobApplication jobApplication) {
      return jobApplicationService.summariseResumeByJobOpeningForJobApplication(jobApplication);
    }	
   
    @Override
    public String summariseResumeByJobOpeningWithInputFile(MultipartFile file,Integer jobApplicationId) {
      return jobApplicationService.summariseResumeByJobOpeningWithInputFile(jobApplicationId,file);
    }

	@Override
	public JobApplication findJobApplicantByUserContext() {
		return jobApplicationService.findJobApplicantByUserContext();
	}

	@Override
	public JobApplication calculateOverallScoreForJobApplication(JobApplication jobApplication) {
		return jobApplicationService.calculateOverallScoreForJobApplication(jobApplication);
	}

	@Override
	public JobOpeningsDto getApplicantCountByActiveJobOpening() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getTopRankedAndTopReferralApplicantByJobId(@Valid String jobId) {
		return jobApplicationService.getTopRankedAndTopReferralApplicantByJobId(jobId);
	}

	@Override
	public String deleteJobApplicationConfigurationScoreById(Integer id) {
		return jobApplicationService.deleteConfigurationScoreByJobApplicationId(id);
	}

	@Override
	public List<JobApplication> setRankingForJobAppicationsById(Integer id)
	{
		 return jobApplicationService.setRankingForJobAppicationsById(id);
		 
	}

	@Override
	public String setOverallScoresAndRankingForJobApplication() {
		return  jobApplicationService.setOverallScoresAndRankingForJobApplication();
	}

	@Override
	public Map<String, Object> setTopRankedAndTopReferralApplicantByJobId(String jobId , String jSon) {
		
		return jobApplicationService.setTopRankedAndTopReferralApplicantByJobId(jobId,jSon);
	}

	@Override
	public List<JobApplication> updateBatchForJobApplication(JobApplicationDto jobApplicationDto) {
		return jobApplicationService.updateBatchForJobApplication(jobApplicationDto);
	}

	@Override
	public List<JobApplication> updateJobApplicationStatus(JobApplicationDto jobApplicationDto) {
		return jobApplicationService.updateJobApplicationStatus(jobApplicationDto);
	}

	@Override
	public String setRankingForJobAppicationsOnRegularBasis() {
		return jobApplicationService.setRankingForJobAppicationsOnRegularBasis();
	}

	@Override
	public String setRankingForJobAppicationsPostTwoDays() {
		return jobApplicationService.setRankingForJobAppicationsPostTwoDays();
	}
	
   
   
}
