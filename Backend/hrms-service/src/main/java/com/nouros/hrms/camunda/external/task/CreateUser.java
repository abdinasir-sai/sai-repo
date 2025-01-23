package com.nouros.hrms.camunda.external.task;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.service.EmployeeService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
@ExternalTaskSubscription("CreateUser")
public class CreateUser implements ExternalTaskHandler{

	private static final Logger log = LogManager.getLogger(CreateUser.class);

	private static final long ONE_MINUTE = 1000L * 60;
	private static final int MAX_RETRIES = 4;

	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		try { 
			log.info("inside @class CreateUser @method execute");
			String processInstanceId=externalTask.getProcessInstanceId();
			EmployeeService employeeService= ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
		 	Employee employee= employeeService.findByProcessInstanceId(processInstanceId);
		 	employee.setWorkflowStage("Completed");
		 	employeeService.update(employee);
		 	externalTaskService.complete(externalTask);
		} catch (Exception e) {
			Integer retries = this.getRetries(externalTask);
			Long timeout = this.getNextTimeout(retries);
			log.error("inside @class CreateUser @method execute exception :{}",Utils.getStackTrace(e) , e);
			externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), retries, timeout);
		}
	}
	
	private Integer getRetries(ExternalTask task) {
		Integer retries = task.getRetries();
		if (retries == null) {
			retries = MAX_RETRIES;
		} else {
			retries = retries - 1;
		}
		return retries;
	}

	private Long getNextTimeout(Integer retries) {
		return ONE_MINUTE * (MAX_RETRIES - retries);
	}
		
}
