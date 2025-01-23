package com.nouros.hrms.model;

import java.util.List;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

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
import lombok.Data;

@Data
@Entity
@Audited
@Table(name = "ORG_CHART_DESIGNATION")
public class OrgChartDesignation extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@Basic
	@Column(name = "DELETED")
	private boolean deleted;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORGCHART_ID", columnDefinition = "INT")
	private PlannedOrgChart orgChartId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DESIGNATION_ID", columnDefinition = "INT")
	private Designation designation;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DEPARTMENT_ID", columnDefinition = "INT")
	private Department department;

	@Size(max = 255)
	@Basic
	@Column(name = "NAME")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_DESIGNATION", columnDefinition = "INT")
	private OrgChartDesignation parentDesignation;

	@Basic
	@Column(name = "JOB_GRADE")
	private Integer jobGrade;

	@Basic
	@Column(name = "JOB_DESCRIPTION", columnDefinition = "LONGTEXT")
	private String jobDescription;

	@Size(max = 255)
	@Basic
	@Column(name = "JOB_LEVEL")
	private String jobLevel;

	@Basic
	@Column(name = "CORE_CAPABILITIES", columnDefinition = "LONGTEXT")
	private String coreCapabilities;

	@Basic
	@Column(name = "EXPERIENCE_AND_EDUCATION", columnDefinition = "LONGTEXT")
	private String experienceAndEducation;

	@Basic
	@Column(name = "RESPONSIBILITIES", columnDefinition = "LONGTEXT")
	private String responsibilities;

	@Basic
	@Column(name = "KPI", columnDefinition = "LONGTEXT")
	private String kpi;

	@Size(max = 100)
	@Basic
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	@Size(max = 100)
	@Basic
	@Column(name = "WORKFLOW_STAGE")
	private String workflowStage;

	@Basic
	@Column(name = "START_COMPENSATION_RANGE", columnDefinition = "INT")
	private Integer startCompensationRange;

	@Basic
	@Column(name = "END_COMPENSATION_RANGE", columnDefinition = "INT")
	private Integer endCompensationRange;

	@Basic
	@Column(name = "REQUISITION_TYPE", columnDefinition = "ENUM('New Job Requisition','Internal Placement','Internal Job Postion')")
	private String requisitionType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "HIRING_MANAGER", columnDefinition = "INT")
	private Employee hiringManager;

	@Column(name = "CURRENT_EMPLOYEE", columnDefinition = "json")
	private String currentEmployee;
	
	@Basic
	@Column(name = "INTERNAL_EMPLOYEE_JUSTIFICATION", columnDefinition = "LONGTEXT")
	private String internalEmployeeJustification;
	
	@Basic
	@Column(name = "IS_CRITICAL")
	private Boolean isCritical;

	public List<WorkflowActions> getActions() {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class)
				.getWorkflowActions(id, "HealthClubBenefit");
	}

}
