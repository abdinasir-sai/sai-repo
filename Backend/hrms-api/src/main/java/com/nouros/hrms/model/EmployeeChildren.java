package com.nouros.hrms.model;

import java.util.Date;
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

@Entity
@Table(name = "EMPLOYEE_CHILDREN")
public class EmployeeChildren extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@Size(max = 100)
	@Basic
	@Column(name = "NAME", length = 100)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
	private Employee employee;

	@Basic
	@Column(name = "DATE_OF_BIRTH", length = 19)
	private Date dateOfBirth;

	@Size(max = 100)
	@Basic
	@Column(name = "NATIONAL_ID_TYPE")
	private String nationalIdType;

	@Size(max = 100)
	@Basic
	@Column(name = "NATIONAL_ID_NUMBER")
	private String nationalIdNumber;

	@Size(max = 250)
	@Column(name = "TEXT1", length = 250)
	private String text1;

	@Basic
	@Column(name = "AMOUNT")
	private Double amount;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "HR_BENEFITS_ID", columnDefinition = "INT")
	private HrBenefits hrBenefits;

	@Size(max = 100)
	@Basic
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	@Size(max = 100)
	@Basic
	@Column(name = "WORKFLOW_STAGE")
	private String workflowStage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getNationalIdType() {
		return nationalIdType;
	}

	public void setNationalIdType(String nationalIdType) {
		this.nationalIdType = nationalIdType;
	}

	public String getNationalIdNumber() {
		return nationalIdNumber;
	}

	public void setNationalIdNumber(String nationalIdNumber) {
		this.nationalIdNumber = nationalIdNumber;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public HrBenefits getHrBenefits() {
		return hrBenefits;
	}

	public void setHrBenefits(HrBenefits hrBenefits) {
		this.hrBenefits = hrBenefits;
	}

	public List<WorkflowActions> getActions() {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class)
				.getWorkflowActions(id, "EmployeeChildren");
	}

}
