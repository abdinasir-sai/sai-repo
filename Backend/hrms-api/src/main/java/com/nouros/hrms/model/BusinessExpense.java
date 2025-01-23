package com.nouros.hrms.model;

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
@Entity
@Audited
@Table(name = "BUSINESS_EXPENSE")
@Filters(value = { @Filter(name = "businessExpenseEmployeeIdInFilter",condition = "EMPLOYEE_ID IN (select e.ID FROM EMPLOYEE e WHERE e.REPORTING_MANAGER_USERID_FK = (:id) OR e.USERID_PK = (:id)) "),
@Filter(name = "businessExpenseEmployeeDepartmentInFilter", condition = "EMPLOYEE_ID IN ( SELECT e.ID FROM EMPLOYEE e where e.DEPARTMENT_ID IN (SELECT d.ID FROM DEPARTMENT d WHERE d.NAME IN (:department)))")})
@FilterDefs(value = { @FilterDef(name = "businessExpenseEmployeeIdInFilter", parameters = {@ParamDef(name = "id", type = Integer.class)}),
@FilterDef(name = "businessExpenseEmployeeDepartmentInFilter", parameters = @ParamDef(name = "department", type = String.class))})
public class BusinessExpense extends BaseEntitySaaS{


	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;
	
	@Column(name="DELETED",columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean deleted = false;
	
	@Column(name = "CLAIM_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date claimDate;
	
	 @Basic
	 @Column(name = "CLAIM_AMOUNT")
	 private Double claimAmount;
	 
	 @Basic
	 @Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
     private String description;
	 
	 @Size(max = 255)
	 @Basic
	 @Column(name = "ATTACHMENT")
	 private String attachment;
	 
	 @ManyToOne
	 @JoinColumn(name="EMPLOYEE_ID",nullable=false,columnDefinition = "INT")
	 private Employee employee;
	 
	 @ManyToOne
	 @JoinColumn(name="BUSINESS_TRIP_ID",nullable=false,columnDefinition = "INT")
	 private BusinessTrip businessTrip;

	 @Column(name = "PROCESS_INSTANCE_ID",length = 100)
	 private String processInstanceId;
	 
	 @Column(name = "WORKFLOW_STAGE",length = 100)
	 private String workflowStage;
	 
	    @Column(name="FILE_PATH",length = 200)
	    private String filePath;

		@Column(name="OTHER_EXPENSE_BANK_REQUEST_ID")
		private Integer otherExpenseBankRequestId;	 
		
		@Basic
	    @Column(name = "REMARK", columnDefinition = "LONGTEXT")
	    private String remark;
		
		@Basic
	    @Column(name = "EXPENSE_CATEGORY", columnDefinition = "ENUM('Individual','Business Trip')")
	    private String expenseCategory;
	    
		
		

	public List<WorkflowActions> getActions() {
	        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "BusinessExpense");
	 }
	 
}

