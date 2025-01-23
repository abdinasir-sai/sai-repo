package com.nouros.hrms.model;

import java.sql.Date;
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
@Table(name = "NEW_HIRE_BENEFIT")
public class NewHireBenefit extends BaseEntitySaaS{
	

		@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	    @Id
	    @Column(columnDefinition = "INT")
	    private Integer id;
	   
	    @Basic
	    @Column(name = "DELETED")
	    private boolean deleted;
	    
	    @Basic
	    @Column(name = "DATE_OF_CLAIM", length = 19)
	    private Date dateOfClaim;
	    
	    @Basic
	    @Column(name = "AMOUNT")
	    private Double amount;
	    
	    @Basic
	    @Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
	    private String description;
	    
	    @Size(max = 255)
	    @Basic
	    @Column(name = "ATTACHMENT")
	    private String attachment;
	    
	    @Basic
	    @Column(name = "STATUS", columnDefinition = "ENUM('Submitted','Approved','Rejected')")
	    private String status;
	    
	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
	    private Employee employee;
	    
	    @Size(max = 100)
	    @Basic
	    @Column(name = "PROCESS_INSTANCE_ID")
	    private String processInstanceId;
	    
	    @Size(max = 100)
	    @Basic
	    @Column(name = "WORKFLOW_STAGE")
	    private String workflowStage;
	    
	    @Size(max = 250)
	    @Column(name = "TEXT1", length = 250)
	    private String text1;
	    
	    @Column(name="OUTPUT_STRING",length = 200)
	    private String outputString;
	    
	    public List<WorkflowActions> getActions() {
	        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "NewHireBenefit");
	    }

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public boolean isDeleted() {
			return deleted;
		}

		public void setDeleted(boolean deleted) {
			this.deleted = deleted;
		}

		public Date getDateOfClaim() {
			return dateOfClaim;
		}

		public void setDateOfClaim(Date dateOfClaim) {
			this.dateOfClaim = dateOfClaim;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
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
	    
	    public String getText1() {
			return text1;
		}

		public void setText1(String text1) {
			this.text1 = text1;
		}

		public String getOutputString() {
			return outputString;
		}

		public void setOutputString(String outputString) {
			this.outputString = outputString;
		}

		
}
