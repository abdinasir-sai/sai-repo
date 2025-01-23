package com.nouros.hrms.model;

import java.util.Date;
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
import lombok.Data;

@Data
@Entity
@Table(name = "EMPLOYEE_PERFORMANCE_REVIEW_CYCLE")
public class EmployeePerformanceReviewCycle extends BaseEntitySaaS{
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;
	
	@Column(name="DELETED",columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean deleted = false;
	
	@Basic
    @Column(name = "START_DATE", length = 19)
    private Date startDate;
	
	@Basic
    @Column(name = "END_DATE", length = 19)
    private Date endDate;
	
	@Basic
	@Column(name = "TYPE", columnDefinition = "ENUM('HALF YEARLY')")
	private String type;
	
	@Size(max = 255)
    @Basic
    @Column(name = "STATUS", length = 255)
    private String status;
	
	@Column(name = "PROCESS_INSTANCE_ID",length = 100)
    private String processInstanceId;

    @Column(name = "WORKFLOW_STAGE",length = 100)
    private String workflowStage;

    public List<WorkflowActions> getActions() {
        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "EmployeePerformanceReviewCycle");
    }
	

}
