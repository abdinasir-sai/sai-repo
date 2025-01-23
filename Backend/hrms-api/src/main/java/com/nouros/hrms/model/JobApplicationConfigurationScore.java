package com.nouros.hrms.model;

import java.util.List;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name ="JOB_APPLICATION_CONFIGURATION_SCORE")
public class JobApplicationConfigurationScore {
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "JOB_APPLICATION_ID", columnDefinition = "INT")
	private JobApplication jobApplication;
	
	@Basic
    @Column(name = "INDIVIDUAL_SCORE")
    private Double individualScore;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="CONFIGURATION_ID")
	private WeightageConfiguration configurationId;
	
	@Size(max = 100)
	@Basic
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	@Size(max = 100)
	@Basic
	@Column(name = "WORKFLOW_STAGE")
	private String workflowStage;
	
	@Size(max = 1000)
	@Basic
	@Column(name = "REASON")
	private String reason;
	
	public List<WorkflowActions> getActions() {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class)
				.getWorkflowActions(id, "JobApplicationConfigurationScore");
	}

}
