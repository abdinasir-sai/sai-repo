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
@Table(name = "OTHER_EXPENSE_BANK_REQUEST")
public class OtherExpenseBankRequest extends BaseEntitySaaS{
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id; 
	
	@Column(name = "DELETED")
	private boolean deleted;
	
	@Size(max = 100)
	@Basic
	@Column(name = "WPS_FILE_PATH")
	private String wpsFilePath;
	
	@Basic
	@Column(name = "TYPE")
	private String type;
	
	@Size(max = 100)
	@Basic
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;
	
	@Size(max = 100)
	@Basic
	@Column(name = "WORKFLOW_STAGE")
	private String workflowStage;
	
	@Basic
	@Column(name = "DATE", length = 19)
	private Date Date;

 	
	@Size(max = 100)
	@Basic
	@Column(name = "REQUEST_ID")
	private String requestId;
	
	@Basic
	@Column(name = "WEEK_NUM", length = 19)
	private Integer weekNum;

	
	@Basic
	@Column(name = "YEAR", length = 19)
	private Integer year;

	@Column(name = "EMPLOYEE_ID_LIST")
	private String employeeIdList;
	
	public List<WorkflowActions> getActions() {
        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "OtherExpenseBankRequest");
    }
	

}
