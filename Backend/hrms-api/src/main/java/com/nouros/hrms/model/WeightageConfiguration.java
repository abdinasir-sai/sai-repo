package com.nouros.hrms.model;

import java.util.List;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "WEIGHTAGE_CONFIGURATION")
public class WeightageConfiguration extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@Size(max = 200)
	@Basic
	@Column(name = "CONFIGURATION_NAME")
	private String configurationName;

	@Column(name = "CONFIGURATION_JSON", columnDefinition = "json")
	private String configurationJson;

	@Size(max = 100)
	@Basic
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	@Size(max = 100)
	@Basic
	@Column(name = "WORKFLOW_STAGE")
	private String workflowStage;
	
	@Basic
	@Column(name = "CONFIGURATION_QUESTIONS", columnDefinition = "TEXT")
	private String configurationQuestions;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	public String getConfigurationJson() {
		return configurationJson;
	}

	public void setConfigurationJson(String configurationJson) {
		this.configurationJson = configurationJson;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getWorkflowStage() {
		return workflowStage;
	}

	public void setWorkflowStage(String workflowStage) {
		this.workflowStage = workflowStage;
	}

	public List<WorkflowActions> getActions() {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class)
				.getWorkflowActions(id, "WeightageConfiguration");
	}

	public String getConfigurationQuestions() {
		return configurationQuestions;
	}

	public void setConfigurationQuestions(String configurationQuestions) {
		this.configurationQuestions = configurationQuestions;
	}

	
}
