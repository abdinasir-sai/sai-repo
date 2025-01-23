package com.nouros.payrollmanagement.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nouros.hrms.model.BaseEntitySaaS;
import com.nouros.hrms.model.Employee;

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
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "OTHER_SALARY_COMPONENT")
@JsonFilter("propertyFilter")
@Filters(value = {@Filter(name = "otherSalaryComponentEmployeeDepartmentInFilter", condition = "EMPLOYEE_FK IN ( SELECT e.ID FROM EMPLOYEE e where e.DEPARTMENT_ID IN (SELECT d.ID FROM DEPARTMENT d WHERE d.NAME IN (:department)))")})
@FilterDefs(value = {@FilterDef(name = "otherSalaryComponentEmployeeDepartmentInFilter", parameters = @ParamDef(name = "department", type = String.class))})
public class OtherSalaryComponent extends BaseEntitySaaS{
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
	@Column(columnDefinition = "INT")
	private Integer id;
	
	@Basic
    private boolean deleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_FK", columnDefinition = "INT")
    private Employee employee;
	
	@Size(max = 50)
    @Column(name = "REFERENCE_ID")
    private String referenceId;

	@Basic
    @Column(name = "EXPECTED_DATE", length = 19)
    private Date expectedDate;
	
	@Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
	
	@Basic
	@Column(name = "AMOUNT")
	private Double amount;
	
	
    public enum Type{
    	EARNING,DEDUCTION
    }
	
	
	@Basic
    @Column(name = "TYPE", columnDefinition = "ENUM", length = 8)
    private String type;
	
    @Column(name = "PROCESS_INSTANCE_ID")
    private String processInstanceId;
	
    @Column(name = "WORKFLOW_STAGE")
    private String workflowStage;
	
    @Basic
	@Column(name = "PAID_AMOUNT")
	private Double paidAmount;
    
    @Size(max = 255)
    @Column(name = "COMMENT")
    private String comment;

      public List<WorkflowActions> getActions() {
        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "OtherSalaryComponent");
    }
    
}
