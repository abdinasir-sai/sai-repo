package com.nouros.hrms.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "PROJECTS")
@Filters(value = { @Filter(name = "projectsApprovalStatusNInFilter", condition = "APPROVAL_STATUS not in (:approvalStatus)"), @Filter(name = "projectsApprovalStatusEqFilter", condition = "APPROVAL_STATUS = :approvalStatus"), @Filter(name = "projectsApprovalStatusNEqFilter", condition = "APPROVAL_STATUS != :approvalStatus"), @Filter(name = "projectsApprovalStatusInFilter", condition = "APPROVAL_STATUS in (:approvalStatus)"), @Filter(name = "projectsClientNameNInFilter", condition = "CLIENT_NAME not in (:clientName)"), @Filter(name = "projectsClientNameEqFilter", condition = "CLIENT_NAME = :clientName"), @Filter(name = "projectsClientNameNEqFilter", condition = "CLIENT_NAME != :clientName"), @Filter(name = "projectsClientNameInFilter", condition = "CLIENT_NAME in (:clientName)"), @Filter(name = "projectsDescriptionNInFilter", condition = "DESCRIPTION not in (:description)"), @Filter(name = "projectsDescriptionEqFilter", condition = "DESCRIPTION = :description"), @Filter(name = "projectsDescriptionNEqFilter", condition = "DESCRIPTION != :description"), @Filter(name = "projectsDescriptionInFilter", condition = "DESCRIPTION in (:description)"), @Filter(name = "projectsIdGtFilter", condition = "ID > :id"), @Filter(name = "projectsIdNInFilter", condition = "ID not in (:id)"), @Filter(name = "projectsIdLtEqFilter", condition = "ID <= :id"), @Filter(name = "projectsIdLtFilter", condition = "ID < :id"), @Filter(name = "projectsIdEqFilter", condition = "ID = :id"), @Filter(name = "projectsIdNEqFilter", condition = "ID != :id"), @Filter(name = "projectsIdInFilter", condition = "ID in (:id)"), @Filter(name = "projectsIdBwFilter", condition = "ID > :id_MIN  AND ID < :id_MAX"), @Filter(name = "projectsIdGtEqFilter", condition = "ID >= :id"), @Filter(name = "projectsIdEqFilter", condition = "ID = :id"), @Filter(name = "projectsProjectCostGtFilter", condition = "PROJECT_COST > :projectCost"), @Filter(name = "projectsProjectCostNInFilter", condition = "PROJECT_COST not in (:projectCost)"), @Filter(name = "projectsProjectCostLtEqFilter", condition = "PROJECT_COST <= :projectCost"), @Filter(name = "projectsProjectCostLtFilter", condition = "PROJECT_COST < :projectCost"), @Filter(name = "projectsProjectCostEqFilter", condition = "PROJECT_COST = :projectCost"), @Filter(name = "projectsProjectCostNEqFilter", condition = "PROJECT_COST != :projectCost"), @Filter(name = "projectsProjectCostInFilter", condition = "PROJECT_COST in (:projectCost)"), @Filter(name = "projectsProjectCostBwFilter", condition = "PROJECT_COST > :projectCost_MIN  AND PROJECT_COST < :projectCost_MAX"), @Filter(name = "projectsProjectCostGtEqFilter", condition = "PROJECT_COST >= :projectCost"), @Filter(name = "projectsProjectCostEqFilter", condition = "PROJECT_COST = :projectCost"), @Filter(name = "projectsProjectHeadNInFilter", condition = "PROJECT_HEAD not in (:projectHead)"), @Filter(name = "projectsProjectHeadEqFilter", condition = "PROJECT_HEAD = :projectHead"), @Filter(name = "projectsProjectHeadNEqFilter", condition = "PROJECT_HEAD != :projectHead"), @Filter(name = "projectsProjectHeadInFilter", condition = "PROJECT_HEAD in (:projectHead)"), @Filter(name = "projectsProjectManagerNInFilter", condition = "PROJECT_MANAGER not in (:projectManager)"), @Filter(name = "projectsProjectManagerEqFilter", condition = "PROJECT_MANAGER = :projectManager"), @Filter(name = "projectsProjectManagerNEqFilter", condition = "PROJECT_MANAGER != :projectManager"), @Filter(name = "projectsProjectManagerInFilter", condition = "PROJECT_MANAGER in (:projectManager)"), @Filter(name = "projectsProjectNameNInFilter", condition = "PROJECT_NAME not in (:projectName)"), @Filter(name = "projectsProjectNameEqFilter", condition = "PROJECT_NAME = :projectName"), @Filter(name = "projectsProjectNameNEqFilter", condition = "PROJECT_NAME != :projectName"), @Filter(name = "projectsProjectNameInFilter", condition = "PROJECT_NAME in (:projectName)"), @Filter(name = "projectsStatusNInFilter", condition = "STATUS not in (:status)"), @Filter(name = "projectsStatusEqFilter", condition = "STATUS = :status"), @Filter(name = "projectsStatusNEqFilter", condition = "STATUS != :status"), @Filter(name = "projectsStatusInFilter", condition = "STATUS in (:status)"), @Filter(name = "projectsWorkspaceIdGtFilter", condition = "WORKSPACE_ID > :workspaceId"), @Filter(name = "projectsWorkspaceIdNInFilter", condition = "WORKSPACE_ID not in (:workspaceId)"), @Filter(name = "projectsWorkspaceIdLtEqFilter", condition = "WORKSPACE_ID <= :workspaceId"), @Filter(name = "projectsWorkspaceIdLtFilter", condition = "WORKSPACE_ID < :workspaceId"), @Filter(name = "projectsWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId"), @Filter(name = "projectsWorkspaceIdNEqFilter", condition = "WORKSPACE_ID != :workspaceId"), @Filter(name = "projectsWorkspaceIdInFilter", condition = "WORKSPACE_ID in (:workspaceId)"), @Filter(name = "projectsWorkspaceIdBwFilter", condition = "WORKSPACE_ID > :workspaceId_MIN  AND WORKSPACE_ID < :workspaceId_MAX"), @Filter(name = "projectsWorkspaceIdGtEqFilter", condition = "WORKSPACE_ID >= :workspaceId"), @Filter(name = "projectsWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId") })
@FilterDefs(value = { @FilterDef(name = "projectsApprovalStatusNInFilter", parameters = { @ParamDef(name = "approvalStatus", type = String.class) }), @FilterDef(name = "projectsApprovalStatusEqFilter", parameters = { @ParamDef(name = "approvalStatus", type = String.class) }), @FilterDef(name = "projectsApprovalStatusNEqFilter", parameters = { @ParamDef(name = "approvalStatus", type = String.class) }), @FilterDef(name = "projectsApprovalStatusInFilter", parameters = { @ParamDef(name = "approvalStatus", type = String.class) }), @FilterDef(name = "projectsClientNameNInFilter", parameters = { @ParamDef(name = "clientName", type = String.class) }), @FilterDef(name = "projectsClientNameEqFilter", parameters = { @ParamDef(name = "clientName", type = String.class) }), @FilterDef(name = "projectsClientNameNEqFilter", parameters = { @ParamDef(name = "clientName", type = String.class) }), @FilterDef(name = "projectsClientNameInFilter", parameters = { @ParamDef(name = "clientName", type = String.class) }), @FilterDef(name = "projectsDescriptionNInFilter", parameters = { @ParamDef(name = "description", type = String.class) }), @FilterDef(name = "projectsDescriptionEqFilter", parameters = { @ParamDef(name = "description", type = String.class) }), @FilterDef(name = "projectsDescriptionNEqFilter", parameters = { @ParamDef(name = "description", type = String.class) }), @FilterDef(name = "projectsDescriptionInFilter", parameters = { @ParamDef(name = "description", type = String.class) }), @FilterDef(name = "projectsIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "projectsIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "projectsIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "projectsIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "projectsIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "projectsIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "projectsIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "projectsIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "projectsIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "projectsProjectCostGtFilter", parameters = { @ParamDef(name = "projectCost", type = Double.class) }), @FilterDef(name = "projectsProjectCostNInFilter", parameters = { @ParamDef(name = "projectCost", type = Double.class) }), @FilterDef(name = "projectsProjectCostLtEqFilter", parameters = { @ParamDef(name = "projectCost", type = Double.class) }), @FilterDef(name = "projectsProjectCostLtFilter", parameters = { @ParamDef(name = "projectCost", type = Double.class) }), @FilterDef(name = "projectsProjectCostEqFilter", parameters = { @ParamDef(name = "projectCost", type = Double.class) }), @FilterDef(name = "projectsProjectCostNEqFilter", parameters = { @ParamDef(name = "projectCost", type = Double.class) }), @FilterDef(name = "projectsProjectCostInFilter", parameters = { @ParamDef(name = "projectCost", type = Double.class) }), @FilterDef(name = "projectsProjectCostBwFilter", parameters = { @ParamDef(name = "projectCost_MIN", type = Double.class), @ParamDef(name = "projectCost_MAX", type = Double.class) }), @FilterDef(name = "projectsProjectCostGtEqFilter", parameters = { @ParamDef(name = "projectCost", type = Double.class) }), @FilterDef(name = "projectsProjectHeadNInFilter", parameters = { @ParamDef(name = "projectHead", type = String.class) }), @FilterDef(name = "projectsProjectHeadEqFilter", parameters = { @ParamDef(name = "projectHead", type = String.class) }), @FilterDef(name = "projectsProjectHeadNEqFilter", parameters = { @ParamDef(name = "projectHead", type = String.class) }), @FilterDef(name = "projectsProjectHeadInFilter", parameters = { @ParamDef(name = "projectHead", type = String.class) }), @FilterDef(name = "projectsProjectManagerNInFilter", parameters = { @ParamDef(name = "projectManager", type = String.class) }), @FilterDef(name = "projectsProjectManagerEqFilter", parameters = { @ParamDef(name = "projectManager", type = String.class) }), @FilterDef(name = "projectsProjectManagerNEqFilter", parameters = { @ParamDef(name = "projectManager", type = String.class) }), @FilterDef(name = "projectsProjectManagerInFilter", parameters = { @ParamDef(name = "projectManager", type = String.class) }), @FilterDef(name = "projectsProjectNameNInFilter", parameters = { @ParamDef(name = "projectName", type = String.class) }), @FilterDef(name = "projectsProjectNameEqFilter", parameters = { @ParamDef(name = "projectName", type = String.class) }), @FilterDef(name = "projectsProjectNameNEqFilter", parameters = { @ParamDef(name = "projectName", type = String.class) }), @FilterDef(name = "projectsProjectNameInFilter", parameters = { @ParamDef(name = "projectName", type = String.class) }), @FilterDef(name = "projectsStatusNInFilter", parameters = { @ParamDef(name = "status", type = String.class) }), @FilterDef(name = "projectsStatusEqFilter", parameters = { @ParamDef(name = "status", type = String.class) }), @FilterDef(name = "projectsStatusNEqFilter", parameters = { @ParamDef(name = "status", type = String.class) }), @FilterDef(name = "projectsStatusInFilter", parameters = { @ParamDef(name = "status", type = String.class) }), @FilterDef(name = "projectsWorkspaceIdGtFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "projectsWorkspaceIdNInFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "projectsWorkspaceIdLtEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "projectsWorkspaceIdLtFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "projectsWorkspaceIdEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "projectsWorkspaceIdNEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "projectsWorkspaceIdInFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "projectsWorkspaceIdBwFilter", parameters = { @ParamDef(name = "workspaceId_MIN", type = Integer.class), @ParamDef(name = "workspaceId_MAX", type = Integer.class) }), @FilterDef(name = "projectsWorkspaceIdGtEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }) })
public class Projects extends BaseEntitySaaS{

    @Basic
    @Column(name = "APPROVAL_STATUS", columnDefinition = "ENUM", length = 12)
    private String approvalStatus;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CLIENT_ID", columnDefinition = "INT")
    private Clients clientId;

    @Size(max = 100)
    @Basic
    @Column(length = 100)
    private String description;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

    @Basic
    @Column(name = "PROJECT_COST")
    private Double projectCost;
    
    @Basic
    @Column(name = "RATE_PER_HOUR")
    private Double ratePerHour;

    @Size(max = 50)
    @Basic
    @Column(name = "PROJECT_HEAD", length = 50)
    private String projectHead;

    @Size(max = 50)
    @Basic
    @Column(name = "PROJECT_MANAGER", length = 50)
    private String projectManager;

    @Size(max = 100)
    @Basic
    @Column(name = "PROJECT_NAME", length = 100)
    private String projectName;

    @Basic
    @Column(columnDefinition = "ENUM", length = 4)
    private String status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(targetEntity = com.nouros.hrms.model.TimeLogs.class, mappedBy = "projects", cascade = CascadeType.MERGE)
    private Set<TimeLogs> timeLogs = new HashSet<>();

    @Basic
    @Column(name = "WORKSPACE_ID", columnDefinition = "INT")
    private Integer workspaceId;

    public Projects() {
    }

    public Double getRatePerHour() {
		return ratePerHour;
	}

	public void setRatePerHour(Double ratePerHour) {
		this.ratePerHour = ratePerHour;
	}

	public Projects(Integer id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Clients getClientId() {
		return clientId;
	}

	public void setClientId(Clients clientId) {
		this.clientId = clientId;
	}
	

	 public String getDescription() {
	        return description;
	    }

	public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getProjectCost() {
        return projectCost;
    }

    public void setProjectCost(Double projectCost) {
        this.projectCost = projectCost;
    }

    public String getProjectHead() {
        return projectHead;
    }

    public void setProjectHead(String projectHead) {
        this.projectHead = projectHead;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<TimeLogs> getTimeLogs() {
        return timeLogs;
    }

    public void setTimeLogs(Set<TimeLogs> timeLogs) {
        this.timeLogs = timeLogs;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }
    
    @Column(name = "PROCESS_INSTANCE_ID")
    private String processInstanceId;

    @Column(name = "WORKFLOW_STAGE")
    private String workflowStage;

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

    public List<WorkflowActions> getActions() {
        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "Projects");
    }
}
