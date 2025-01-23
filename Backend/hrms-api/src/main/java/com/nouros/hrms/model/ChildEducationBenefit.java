package com.nouros.hrms.model;

import java.util.Date;
import java.util.List;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonFilter;
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

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@JsonFilter("propertyFilter")
@Table(name = "CHILD_EDUCATION_BENEFIT")
public class ChildEducationBenefit extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@Basic
	@Column(name = "NUMBER_OF_CHILDREN", columnDefinition = "ENUM('1','2','3')")
	private String numberOfChildren;

	@Basic
	@Column(name = "AMOUNT_REQUIRED")
	private Double amountRequired;

	@Column(name = "IS_DELETED", columnDefinition = "bit(1) default 0")
	private Boolean isDeleted;

	@Size(max = 50)
	@Basic
	@Column(name = "FIRST_CHILD_NAME", length = 50)
	private String firstChildName;

	@Basic
	@Column(name = "FIRST_CHILD_DOB", length = 19)
	private Date firstChildDob;

	@Size(max = 50)
	@Basic
	@Column(name = "SECOND_CHILD_NAME", length = 50)
	private String secondChildName;

	@Basic
	@Column(name = "SECOND_CHILD_DOB", length = 19)
	private Date secondChildDob;

	@Size(max = 50)
	@Basic
	@Column(name = "THIRD_CHILD_NAME", length = 50)
	private String thirdChildName;

	@Basic
	@Column(name = "THIRD_CHILD_DOB", length = 19)
	private Date thirdChildDob;

	@Basic
	@Column(name = "SCHOOL_YEAR", length = 19)
	private Date schoolYear;

	@Basic
	@Column(name = "EDUCATION_GRADE", columnDefinition = "INT")
	private Integer educationGrade;

	@Size(max = 200)
	@Basic
	@Column(name = "ATTACHMENT", length = 200)
	private String attachment;

	@Basic
	@Column(name = "STATUS", columnDefinition = "ENUM('Submitted','Approved','Rejected')")
	private String status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
	private Employee employee;

	@Basic
	@Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
	private String description;

	@Size(max = 100)
	@Basic
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	 @Size(max = 255)
	 @Basic
	@Column(name = "WORKFLOW_STAGE")
	private String workflowStage;
	
	   public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

  @Size(max = 250)
  @Column(name = "TEXT1", length = 250)
   private String text1;

  
  @Column(name="OUTPUT_STRING",length = 200)
  private String outputString;
    
    public List<WorkflowActions> getActions() {
        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "ChildEducationBenefit");
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(String numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	public Double getAmountRequired() {
		return amountRequired;
	}

	public void setAmountRequired(Double amountRequired) {
		this.amountRequired = amountRequired;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getFirstChildName() {
		return firstChildName;
	}

	public void setFirstChildName(String firstChildName) {
		this.firstChildName = firstChildName;
	}

	public Date getFirstChildDob() {
		return firstChildDob;
	}

	public void setFirstChildDob(Date firstChildDob) {
		this.firstChildDob = firstChildDob;
	}

	
	public String getThirdChildName() {
		return thirdChildName;
	}

	public void setThirdChildName(String thirdChildName) {
		this.thirdChildName = thirdChildName;
	}

	
	public String getSecondChildName() {
		return secondChildName;
	}

	public void setSecondChildName(String secondChildName) {
		this.secondChildName = secondChildName;
	}

	public Date getSecondChildDob() {
		return secondChildDob;
	}

	public void setSecondChildDob(Date secondChildDob) {
		this.secondChildDob = secondChildDob;
	}

	public Date getThirdChildDob() {
		return thirdChildDob;
	}

	public void setThirdChildDob(Date thirdChildDob) {
		this.thirdChildDob = thirdChildDob;
	}

	public Date getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(Date schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Integer getEducationGrade() {
		return educationGrade;
	}

	public void setEducationGrade(Integer educationGrade) {
		this.educationGrade = educationGrade;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getOutputString() {
		return outputString;
	}

	public void setOutputString(String outputString) {
		this.outputString = outputString;
	}


    
}
