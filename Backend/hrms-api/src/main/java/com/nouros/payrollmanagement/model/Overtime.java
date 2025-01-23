package com.nouros.payrollmanagement.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nouros.hrms.model.BaseEntity;
import com.nouros.hrms.model.BaseEntitySaaS;
import com.nouros.hrms.model.Employee;

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
import lombok.Data;

@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Audited
@Table(name = "OVERTIME")
@JsonFilter("propertyFilter")
@Filters(value = { @Filter(name = "overtimeEmployeeIdInFilter",condition = "EMPLOYEE_ID IN (select e.ID FROM EMPLOYEE e WHERE e.REPORTING_MANAGER_USERID_FK = (:id) OR e.USERID_PK = (:id)) "),
@Filter(name = "overtimeEmployeeDepartmentInFilter", condition = "EMPLOYEE_ID IN ( SELECT e.ID FROM EMPLOYEE e where e.DEPARTMENT_ID IN (SELECT d.ID FROM DEPARTMENT d WHERE d.NAME IN (:department)))")})
@FilterDefs(value = { @FilterDef(name = "overtimeEmployeeIdInFilter", parameters = {@ParamDef(name = "id", type = Integer.class)}),
@FilterDef(name = "overtimeEmployeeDepartmentInFilter", parameters = @ParamDef(name = "department", type = String.class))})
public class Overtime extends BaseEntitySaaS{

	    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	    @Id
	    @Column(name="ID" )
	    private Integer id;
	    
	    @Column(name="DELETED",columnDefinition = "TINYINT(1) DEFAULT 0")
	    private boolean deleted = false;

	    @Basic
	    @Column(name = "OVERTIME_YEAR")
	    private Integer overtimeYear;

	    @Basic
	    @Column(name = "OVERTIME_MONTH")
	    private Integer overtimeMonth;

		@Column(name = "OVERTIME_PERIOD_START")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date overtimePeriodStart;

	    @Column(name = "OVERTIME_PERIOD_END")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date overtimePeriodEnd;

	    @Column(name = "SUBMISSION_DATE", nullable=false)
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date submissionDate;

	    @Basic
	    @Column(name = "SUBMITTED_HOURS")
	    private Double submittedHours;

	    @ManyToOne
	    @JoinColumn(name="EMPLOYEE_ID",nullable=false,columnDefinition = "INT")
	    private Employee employee;
	    
	    @Basic
	    @Column(name = "APPROVAL_STATUS", columnDefinition = "ENUM", length = 12)
	    private String approvalStatus;

	    @Column(name = "PROCESS_INSTANCE_ID",length = 100)
	    private String processInstanceId;

	    @Column(name = "WORKFLOW_STAGE",length = 100)
	    private String workflowStage;
	
	    public List<WorkflowActions> getActions() {
	        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "Overtime");
	    }
}
