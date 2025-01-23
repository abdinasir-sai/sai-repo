package com.nouros.payrollmanagement.model;

import java.util.Date;
import java.util.List;

import org.hibernate.envers.Audited;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nouros.hrms.model.BaseEntity;
import com.nouros.hrms.model.BaseEntitySaaS;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Audited
@Table(name = "OVERTIME_LOGS")
public class OvertimeLogs extends BaseEntitySaaS{

	    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	    @Id
	    @Column(name="ID")
	    private Integer id;
	    
	    @Column(name="DELETED",columnDefinition = "TINYINT(1) DEFAULT 0")
	    private boolean deleted = false;

	    @ManyToOne
	    @JoinColumn(name="OVERTIME_ID",nullable=false,columnDefinition = "INT")
	    private Overtime overtime;
	    
	    @Column(name = "OVERTIME_DATE")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date overtimeDate;
	    
	    @Size(max = 255)
	    @Basic
	    @Column(name = "OVERTIME_DAY" )
	    private String OvertimeDay;

	    @Column(name = "START_TIME")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date startTime;

	    @Column(name = "END_TIME")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date endTime;
	    
	    @Basic
	    @Column(name = "OVERTIME_HOURS")
	    private Double overtimeHours;
	    
	    @Basic
	    @Column(name = "OVERTIME_MINUTES",columnDefinition = "INT")
	    private Integer overtimeMinutes;

	    @Basic
	    @Column(name="DESCRIPTION",columnDefinition = "LONGTEXT")
	    private String description;
	    
	    @Basic
	    @Column(name = "PROCESS_INSTANCE_ID",length = 100)
	    private String processInstanceId;

	    @Basic
	    @Column(name = "WORKFLOW_STAGE",length = 100 )
	    private String workflowStage;
	
	    public List<WorkflowActions> getActions() {
	        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "OvertimeLogs");
	    }
		
}
