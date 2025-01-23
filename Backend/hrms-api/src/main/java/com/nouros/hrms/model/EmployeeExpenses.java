package com.nouros.hrms.model;

import java.util.Date;
import java.util.List;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "EMPLOYEE_EXPENSES")
public class EmployeeExpenses extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@Size(max = 200)
	@Basic
	@Column(name = "EXPENSE_CATEGORY")
	private String expenseCategory;

	@Size(max = 200)
	@Basic
	@Column(name = "CURRENCY")
	private String currency;

	@Basic
	@Column(name = "DATE_OF_EXPENSE", length = 19)
	private Date dateOfExpense;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
	private Employee employee;

	@Basic
	@Column(name = "AMOUNT_SPENT")
	private Double amountSpent;
	
	@Basic
	@Column(name = "TAX_AMOUNT")
	private Double taxAmount;

	@Basic
	@Column(name = "TOTAL_AMOUNT_REIMBURSED")
	private Double totalAmountReimbursed;

	@Size(max = 200)
	@Basic
	@Column(name = "PAYMENT_METHOD")
	private String paymentMethod;
	
	@Size(max = 200)
	@Basic
	@Column(name = "PAYMENT_STATUS")
	private String paymentStatus;

	@Size(max = 200)
	@Basic
	@Column(name = "RECIEPTS")
	private String reciepts;

	@Size(max = 200)
	@Column(name = "APPROVAL_STATUS")
	private String approvalStatus;
	
	@Size(max = 200)
	@Basic
	@Column(name = "REIMBURSEMENT_PAYMENT_METHOD")
	private String reimbursementPaymentMethod;
	
	@Basic
	@Column(name = "REIMBURSEMENT_DATE", length = 19)
	private Date reimbursementDate;
	
	@Basic
    @Column(name = "DESCRIPTION_OF_EXPENSE", columnDefinition = "LONGTEXT")
    private String descriptionOfExpense;
		
	@Size(max = 255)
    @Column(name = "PROCESS_INSTANCE_ID")
    private String processInstanceId;

	@Size(max = 255)
    @Column(name = "WORKFLOW_STAGE")
    private String workflowStage;

    public List<WorkflowActions> getActions() {
        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "EmployeeExpenses");
    }
}
