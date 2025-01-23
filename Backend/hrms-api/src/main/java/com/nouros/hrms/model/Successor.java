package com.nouros.hrms.model;

import java.util.List;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Data
@Table(name = "SUCCESSOR")
public class Successor extends BaseEntitySaaS{

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(name="ID", columnDefinition = "INT")
    private Integer id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DESIGNATION_ID", columnDefinition = "INT")
    private Designation designation;
    
    @Column(name = "DEVELOPMENT_NEED")
    private String developmentNeed;
        
    @OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RECOMMENDED_CANDIDATE_ID", columnDefinition = "INT")
	private Employee employee;


    @Column(name = "PROCESS_INSTANCE_ID")
    private String processInstanceId;

    @Column(name = "WORKFLOW_STAGE")
    private String workflowStage;

    public List<WorkflowActions> getActions() {
        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "EmployeeReview");
    }
 

}
