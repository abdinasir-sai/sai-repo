package com.nouros.hrms.model;

import java.util.Date;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

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

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "APPRAISAL_TEMPLATES")
@Filters(value = { @Filter(name = "appraisalTemplatesApplicableFromDateGtFilter", condition = "APPLICABLE_FROM_DATE > (SELECT DATE_FORMAT(:applicableFromDate, '%Y-%m-%d'))"), @Filter(name = "appraisalTemplatesApplicableFromDateLtFilter", condition = "APPLICABLE_FROM_DATE < (SELECT DATE_FORMAT(:applicableFromDate, '%Y-%m-%d'))"), @Filter(name = "appraisalTemplatesApplicableFromDateBwFilter", condition = "APPLICABLE_FROM_DATE >(SELECT DATE_FORMAT(:applicableFromDate_MIN, '%Y-%m-%d')) AND APPLICABLE_FROM_DATE <(SELECT DATE_FORMAT(:applicableFromDate_MAX, '%Y-%m-%d'))"), @Filter(name = "appraisalTemplatesDepartmentIdGtFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID > :id)"), @Filter(name = "appraisalTemplatesDepartmentIdNInFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID not in (:id)"), @Filter(name = "appraisalTemplatesDepartmentIdLtEqFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID <= :id)"), @Filter(name = "appraisalTemplatesDepartmentIdLtFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID < :id)"), @Filter(name = "appraisalTemplatesDepartmentIdEqFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID = :id)"), @Filter(name = "appraisalTemplatesDepartmentIdNEqFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID != :id)"), @Filter(name = "appraisalTemplatesDepartmentIdInFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID in (:id)"), @Filter(name = "appraisalTemplatesDepartmentIdBwFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID > :id_MIN  AND DEPARTMENT.ID < :id_MAX )"), @Filter(name = "appraisalTemplatesDepartmentIdGtEqFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.ID >= :id)"), @Filter(name = "appraisalTemplatesDepartmentName2NInFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.NAME2 not in (:name2)"), @Filter(name = "appraisalTemplatesDepartmentName2EqFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.NAME2 = :name2)"), @Filter(name = "appraisalTemplatesDepartmentName2NEqFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.NAME2 != :name2)"), @Filter(name = "appraisalTemplatesDepartmentName2InFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.NAME2 in (:name2)"), @Filter(name = "appraisalTemplatesDepartmentName1NInFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.NAME1 not in (:name1)"), @Filter(name = "appraisalTemplatesDepartmentName1EqFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.NAME1 = :name1)"), @Filter(name = "appraisalTemplatesDepartmentName1NEqFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.NAME1 != :name1)"), @Filter(name = "appraisalTemplatesDepartmentName1InFilter", condition = "DEPARTMENT_FK in (select DEPARTMENT.id from DEPARTMENT where DEPARTMENT.NAME1 in (:name1)"), @Filter(name = "appraisalTemplatesIdGtFilter", condition = "ID > :id"), @Filter(name = "appraisalTemplatesIdNInFilter", condition = "ID not in (:id)"), @Filter(name = "appraisalTemplatesIdLtEqFilter", condition = "ID <= :id"), @Filter(name = "appraisalTemplatesIdLtFilter", condition = "ID < :id"), @Filter(name = "appraisalTemplatesIdEqFilter", condition = "ID = :id"), @Filter(name = "appraisalTemplatesIdNEqFilter", condition = "ID != :id"), @Filter(name = "appraisalTemplatesIdInFilter", condition = "ID in (:id)"), @Filter(name = "appraisalTemplatesIdBwFilter", condition = "ID > :id_MIN  AND ID < :id_MAX"), @Filter(name = "appraisalTemplatesIdGtEqFilter", condition = "ID >= :id"), @Filter(name = "appraisalTemplatesIdEqFilter", condition = "ID = :id"), @Filter(name = "appraisalTemplatesRoleIdNInFilter", condition = "ROLE_ID not in (:roleId)"), @Filter(name = "appraisalTemplatesRoleIdEqFilter", condition = "ROLE_ID = :roleId"), @Filter(name = "appraisalTemplatesRoleIdNEqFilter", condition = "ROLE_ID != :roleId"), @Filter(name = "appraisalTemplatesRoleIdInFilter", condition = "ROLE_ID in (:roleId)"), @Filter(name = "appraisalTemplatesWorkspaceIdGtFilter", condition = "WORKSPACE_ID > :workspaceId"), @Filter(name = "appraisalTemplatesWorkspaceIdNInFilter", condition = "WORKSPACE_ID not in (:workspaceId)"), @Filter(name = "appraisalTemplatesWorkspaceIdLtEqFilter", condition = "WORKSPACE_ID <= :workspaceId"), @Filter(name = "appraisalTemplatesWorkspaceIdLtFilter", condition = "WORKSPACE_ID < :workspaceId"), @Filter(name = "appraisalTemplatesWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId"), @Filter(name = "appraisalTemplatesWorkspaceIdNEqFilter", condition = "WORKSPACE_ID != :workspaceId"), @Filter(name = "appraisalTemplatesWorkspaceIdInFilter", condition = "WORKSPACE_ID in (:workspaceId)"), @Filter(name = "appraisalTemplatesWorkspaceIdBwFilter", condition = "WORKSPACE_ID > :workspaceId_MIN  AND WORKSPACE_ID < :workspaceId_MAX"), @Filter(name = "appraisalTemplatesWorkspaceIdGtEqFilter", condition = "WORKSPACE_ID >= :workspaceId"), @Filter(name = "appraisalTemplatesWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId") })
@FilterDefs(value = { @FilterDef(name = "appraisalTemplatesApplicableFromDateGtFilter", parameters = { @ParamDef(name = "applicableFromDate", type = String.class) }), @FilterDef(name = "appraisalTemplatesApplicableFromDateLtFilter", parameters = { @ParamDef(name = "applicableFromDate", type = String.class) }), @FilterDef(name = "appraisalTemplatesApplicableFromDateBwFilter", parameters = { @ParamDef(name = "applicableFromDate_MIN", type = String.class), @ParamDef(name = "applicableFromDate_MAX", type = String.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesDepartmentName2NInFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "appraisalTemplatesDepartmentName2EqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "appraisalTemplatesDepartmentName2NEqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "appraisalTemplatesDepartmentName2InFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "appraisalTemplatesDepartmentName1NInFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "appraisalTemplatesDepartmentName1EqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "appraisalTemplatesDepartmentName1NEqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "appraisalTemplatesDepartmentName1InFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "appraisalTemplatesIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesRoleIdNInFilter", parameters = { @ParamDef(name = "roleId", type = String.class) }), @FilterDef(name = "appraisalTemplatesRoleIdEqFilter", parameters = { @ParamDef(name = "roleId", type = String.class) }), @FilterDef(name = "appraisalTemplatesRoleIdNEqFilter", parameters = { @ParamDef(name = "roleId", type = String.class) }), @FilterDef(name = "appraisalTemplatesRoleIdInFilter", parameters = { @ParamDef(name = "roleId", type = String.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdGtFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdNInFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdLtEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdLtFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdNEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdInFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdBwFilter", parameters = { @ParamDef(name = "workspaceId_MIN", type = Integer.class), @ParamDef(name = "workspaceId_MAX", type = Integer.class) }), @FilterDef(name = "appraisalTemplatesWorkspaceIdGtEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }) })
public class AppraisalTemplates extends BaseEntitySaaS{

    @Basic
    @Column(name = "APPLICABLE_FROM_DATE", length = 19)
    private Date applicableFromDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPARTMENT_FK", columnDefinition = "INT")
    private Department department;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

    @Size(max = 100)
    @Basic
    @Column(name = "ROLE_ID", length = 100)
    private String roleId;

    @Basic
    @Column(name = "WORKSPACE_ID", columnDefinition = "INT")
    private Integer workspaceId;

    public AppraisalTemplates() {
    }

    public AppraisalTemplates(Integer id) {
        this.id = id;
    }

    public Date getApplicableFromDate() {
        return applicableFromDate;
    }

    public void setApplicableFromDate(Date applicableFromDate) {
        this.applicableFromDate = applicableFromDate;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }
}
