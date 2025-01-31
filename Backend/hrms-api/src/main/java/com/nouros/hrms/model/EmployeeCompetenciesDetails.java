package com.nouros.hrms.model;

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
@Table(name = "EMPLOYEE_COMPETENCIES_DETAILS")
@Filters(value = { @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageGtFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE > :weightage)"), @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageNInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE not in (:weightage)"), @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageLtEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE <= :weightage)"), @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageLtFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE < :weightage)"), @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE = :weightage)"), @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageNEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE != :weightage)"), @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE in (:weightage)"), @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageBwFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE > :weightage_MIN  AND COMPETENCIES.WEIGHTAGE < :weightage_MAX )"), @Filter(name = "employeeCompetenciesMappingCompetenciesWeightageGtEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.WEIGHTAGE >= :weightage)"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelGtFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL > :actualLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelNInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL not in (:actualLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelLtEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL <= :actualLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelLtFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL < :actualLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL = :actualLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelNEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL != :actualLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL in (:actualLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelBwFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL > :actualLevel_MIN  AND COMPETENCIES.ACTUAL_LEVEL < :actualLevel_MAX )"), @Filter(name = "employeeCompetenciesMappingCompetenciesActualLevelGtEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ACTUAL_LEVEL >= :actualLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelGtFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL > :requiredLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelNInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL not in (:requiredLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelLtEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL <= :requiredLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelLtFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL < :requiredLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL = :requiredLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelNEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL != :requiredLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL in (:requiredLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelBwFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL > :requiredLevel_MIN  AND COMPETENCIES.REQUIRED_LEVEL < :requiredLevel_MAX )"), @Filter(name = "employeeCompetenciesMappingCompetenciesRequiredLevelGtEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.REQUIRED_LEVEL >= :requiredLevel)"), @Filter(name = "employeeCompetenciesMappingCompetenciesDescriptionNInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.DESCRIPTION not in (:description)"), @Filter(name = "employeeCompetenciesMappingCompetenciesDescriptionEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.DESCRIPTION = :description)"), @Filter(name = "employeeCompetenciesMappingCompetenciesDescriptionNEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.DESCRIPTION != :description)"), @Filter(name = "employeeCompetenciesMappingCompetenciesDescriptionInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.DESCRIPTION in (:description)"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdGtFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID > :id)"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdNInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID not in (:id)"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdLtEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID <= :id)"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdLtFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID < :id)"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID = :id)"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdNEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID != :id)"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID in (:id)"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdBwFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID > :id_MIN  AND COMPETENCIES.ID < :id_MAX )"), @Filter(name = "employeeCompetenciesMappingCompetenciesIdGtEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.ID >= :id)"), @Filter(name = "employeeCompetenciesMappingCompetenciesCompetencyNameNInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.COMPETENCY_NAME not in (:competencyName)"), @Filter(name = "employeeCompetenciesMappingCompetenciesCompetencyNameEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.COMPETENCY_NAME = :competencyName)"), @Filter(name = "employeeCompetenciesMappingCompetenciesCompetencyNameNEqFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.COMPETENCY_NAME != :competencyName)"), @Filter(name = "employeeCompetenciesMappingCompetenciesCompetencyNameInFilter", condition = "COMPETENCIES_ID in (select COMPETENCIES.id from COMPETENCIES where COMPETENCIES.COMPETENCY_NAME in (:competencyName)"), @Filter(name = "employeeCompetenciesMappingEmployeeIdGtFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID > :id)"), @Filter(name = "employeeCompetenciesMappingEmployeeIdNInFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID not in (:id)"), @Filter(name = "employeeCompetenciesMappingEmployeeIdLtEqFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID <= :id)"), @Filter(name = "employeeCompetenciesMappingEmployeeIdLtFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID < :id)"), @Filter(name = "employeeCompetenciesMappingEmployeeIdEqFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID = :id)"), @Filter(name = "employeeCompetenciesMappingEmployeeIdNEqFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID != :id)"), @Filter(name = "employeeCompetenciesMappingEmployeeIdInFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID in (:id)"), @Filter(name = "employeeCompetenciesMappingEmployeeIdBwFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID > :id_MIN  AND EMPLOYEE.ID < :id_MAX )"), @Filter(name = "employeeCompetenciesMappingEmployeeIdGtEqFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID >= :id)"), @Filter(name = "employeeCompetenciesMappingEmployeeName2NInFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 not in (:name2)"), @Filter(name = "employeeCompetenciesMappingEmployeeName2EqFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 = :name2)"), @Filter(name = "employeeCompetenciesMappingEmployeeName2NEqFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 != :name2)"), @Filter(name = "employeeCompetenciesMappingEmployeeName2InFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 in (:name2)"), @Filter(name = "employeeCompetenciesMappingEmployeeName1NInFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 not in (:name1)"), @Filter(name = "employeeCompetenciesMappingEmployeeName1EqFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 = :name1)"), @Filter(name = "employeeCompetenciesMappingEmployeeName1NEqFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 != :name1)"), @Filter(name = "employeeCompetenciesMappingEmployeeName1InFilter", condition = "EMPLOYEE_ID in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 in (:name1)"), @Filter(name = "employeeCompetenciesMappingEmployee2IdGtFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID > :id)"), @Filter(name = "employeeCompetenciesMappingEmployee2IdNInFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID not in (:id)"), @Filter(name = "employeeCompetenciesMappingEmployee2IdLtEqFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID <= :id)"), @Filter(name = "employeeCompetenciesMappingEmployee2IdLtFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID < :id)"), @Filter(name = "employeeCompetenciesMappingEmployee2IdEqFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID = :id)"), @Filter(name = "employeeCompetenciesMappingEmployee2IdNEqFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID != :id)"), @Filter(name = "employeeCompetenciesMappingEmployee2IdInFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID in (:id)"), @Filter(name = "employeeCompetenciesMappingEmployee2IdBwFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID > :id_MIN  AND EMPLOYEE2.ID < :id_MAX )"), @Filter(name = "employeeCompetenciesMappingEmployee2IdGtEqFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.ID >= :id)"), @Filter(name = "employeeCompetenciesMappingEmployee2Name2NInFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.NAME2 not in (:name2)"), @Filter(name = "employeeCompetenciesMappingEmployee2Name2EqFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.NAME2 = :name2)"), @Filter(name = "employeeCompetenciesMappingEmployee2Name2NEqFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.NAME2 != :name2)"), @Filter(name = "employeeCompetenciesMappingEmployee2Name2InFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.NAME2 in (:name2)"), @Filter(name = "employeeCompetenciesMappingEmployee2Name1NInFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.NAME1 not in (:name1)"), @Filter(name = "employeeCompetenciesMappingEmployee2Name1EqFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.NAME1 = :name1)"), @Filter(name = "employeeCompetenciesMappingEmployee2Name1NEqFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.NAME1 != :name1)"), @Filter(name = "employeeCompetenciesMappingEmployee2Name1InFilter", condition = "REVIEWER_ID in (select EMPLOYEE2.id from EMPLOYEE2 where EMPLOYEE2.NAME1 in (:name1)"), @Filter(name = "employeeCompetenciesMappingIdGtFilter", condition = "ID > :id"), @Filter(name = "employeeCompetenciesMappingIdNInFilter", condition = "ID not in (:id)"), @Filter(name = "employeeCompetenciesMappingIdLtEqFilter", condition = "ID <= :id"), @Filter(name = "employeeCompetenciesMappingIdLtFilter", condition = "ID < :id"), @Filter(name = "employeeCompetenciesMappingIdEqFilter", condition = "ID = :id"), @Filter(name = "employeeCompetenciesMappingIdNEqFilter", condition = "ID != :id"), @Filter(name = "employeeCompetenciesMappingIdInFilter", condition = "ID in (:id)"), @Filter(name = "employeeCompetenciesMappingIdBwFilter", condition = "ID > :id_MIN  AND ID < :id_MAX"), @Filter(name = "employeeCompetenciesMappingIdGtEqFilter", condition = "ID >= :id"), @Filter(name = "employeeCompetenciesMappingIdEqFilter", condition = "ID = :id"), @Filter(name = "employeeCompetenciesMappingText1NInFilter", condition = "TEXT1 not in (:text1)"), @Filter(name = "employeeCompetenciesMappingText1EqFilter", condition = "TEXT1 = :text1"), @Filter(name = "employeeCompetenciesMappingText1NEqFilter", condition = "TEXT1 != :text1"), @Filter(name = "employeeCompetenciesMappingText1InFilter", condition = "TEXT1 in (:text1)"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdGtFilter", condition = "WORKSPACE_ID > :workspaceId"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdNInFilter", condition = "WORKSPACE_ID not in (:workspaceId)"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdLtEqFilter", condition = "WORKSPACE_ID <= :workspaceId"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdLtFilter", condition = "WORKSPACE_ID < :workspaceId"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdNEqFilter", condition = "WORKSPACE_ID != :workspaceId"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdInFilter", condition = "WORKSPACE_ID in (:workspaceId)"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdBwFilter", condition = "WORKSPACE_ID > :workspaceId_MIN  AND WORKSPACE_ID < :workspaceId_MAX"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdGtEqFilter", condition = "WORKSPACE_ID >= :workspaceId"), @Filter(name = "employeeCompetenciesMappingWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId") })
@FilterDefs(value = { @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageGtFilter", parameters = { @ParamDef(name = "weightage", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageNInFilter", parameters = { @ParamDef(name = "weightage", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageLtEqFilter", parameters = { @ParamDef(name = "weightage", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageLtFilter", parameters = { @ParamDef(name = "weightage", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageEqFilter", parameters = { @ParamDef(name = "weightage", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageNEqFilter", parameters = { @ParamDef(name = "weightage", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageInFilter", parameters = { @ParamDef(name = "weightage", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageBwFilter", parameters = { @ParamDef(name = "weightage_MIN", type = Double.class), @ParamDef(name = "weightage_MAX", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesWeightageGtEqFilter", parameters = { @ParamDef(name = "weightage", type = Double.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelGtFilter", parameters = { @ParamDef(name = "actualLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelNInFilter", parameters = { @ParamDef(name = "actualLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelLtEqFilter", parameters = { @ParamDef(name = "actualLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelLtFilter", parameters = { @ParamDef(name = "actualLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelEqFilter", parameters = { @ParamDef(name = "actualLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelNEqFilter", parameters = { @ParamDef(name = "actualLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelInFilter", parameters = { @ParamDef(name = "actualLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelBwFilter", parameters = { @ParamDef(name = "actualLevel_MIN", type = Integer.class), @ParamDef(name = "actualLevel_MAX", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesActualLevelGtEqFilter", parameters = { @ParamDef(name = "actualLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelGtFilter", parameters = { @ParamDef(name = "requiredLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelNInFilter", parameters = { @ParamDef(name = "requiredLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelLtEqFilter", parameters = { @ParamDef(name = "requiredLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelLtFilter", parameters = { @ParamDef(name = "requiredLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelEqFilter", parameters = { @ParamDef(name = "requiredLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelNEqFilter", parameters = { @ParamDef(name = "requiredLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelInFilter", parameters = { @ParamDef(name = "requiredLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelBwFilter", parameters = { @ParamDef(name = "requiredLevel_MIN", type = Integer.class), @ParamDef(name = "requiredLevel_MAX", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesRequiredLevelGtEqFilter", parameters = { @ParamDef(name = "requiredLevel", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesDescriptionNInFilter", parameters = { @ParamDef(name = "description", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesDescriptionEqFilter", parameters = { @ParamDef(name = "description", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesDescriptionNEqFilter", parameters = { @ParamDef(name = "description", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesDescriptionInFilter", parameters = { @ParamDef(name = "description", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesCompetencyNameNInFilter", parameters = { @ParamDef(name = "competencyName", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesCompetencyNameEqFilter", parameters = { @ParamDef(name = "competencyName", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesCompetencyNameNEqFilter", parameters = { @ParamDef(name = "competencyName", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingCompetenciesCompetencyNameInFilter", parameters = { @ParamDef(name = "competencyName", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeName2NInFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeName2EqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeName2NEqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeName2InFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeName1NInFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeName1EqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeName1NEqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployeeName1InFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2IdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2Name2NInFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2Name2EqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2Name2NEqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2Name2InFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2Name1NInFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2Name1EqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2Name1NEqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingEmployee2Name1InFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingText1NInFilter", parameters = { @ParamDef(name = "text1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingText1EqFilter", parameters = { @ParamDef(name = "text1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingText1NEqFilter", parameters = { @ParamDef(name = "text1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingText1InFilter", parameters = { @ParamDef(name = "text1", type = String.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdGtFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdNInFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdLtEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdLtFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdNEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdInFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdBwFilter", parameters = { @ParamDef(name = "workspaceId_MIN", type = Integer.class), @ParamDef(name = "workspaceId_MAX", type = Integer.class) }), @FilterDef(name = "employeeCompetenciesMappingWorkspaceIdGtEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }) })
public class EmployeeCompetenciesDetails extends BaseEntitySaaS{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMPETENCIES_ID", columnDefinition = "INT")
    private Competencies competencies;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REVIEWER_ID", columnDefinition = "INT")
    private Employee employee2;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;
    
    @Size(max = 255)
    @Basic
    @Column(name = "LABEL")
    private String label;

    @Size(max = 255)
    @Basic
    private String text1;

    @Basic
    @Column(name = "WORKSPACE_ID", columnDefinition = "INT")
    private Integer workspaceId;
    
    @Column(name="EMPLOYEE_RATING" )
    private Double employeeRating;
    
    @Column(name = "MANAGER_RATING",columnDefinition = "ENUM('Not Started','On Track','At Risk','Deferred','Completed','Off Track')")
    private Double managerRating;
    
    @Column(name ="EMPLOYEE_FEEDBACK",columnDefinition = "TEXT" )
    private String employeeFeedback;
    
    @Column(name ="MANAGER_FEEDBACK",columnDefinition = "TEXT")
    private String managerFeedback;

    @ManyToOne
    @JoinColumn(name="EMPLOYEE_REVIEW_FK",nullable=false,columnDefinition = "INT")
    private EmployeeReview employeeReview;
    
    public EmployeeCompetenciesDetails() {
    }

    public EmployeeCompetenciesDetails(Integer id) {
        this.id = id;
    }

    public Competencies getCompetencies() {
        return competencies;
    }

    public void setCompetencies(Competencies competencies) {
        this.competencies = competencies;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee2() {
        return employee2;
    }

    public void setEmployee2(Employee employee2) {
        this.employee2 = employee2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getEmployeeRating() {
		return employeeRating;
	}

	public void setEmployeeRating(Double employeeRating) {
		this.employeeRating = employeeRating;
	}

	public Double getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(Double managerRating) {
		this.managerRating = managerRating;
	}

	public String getEmployeeFeedback() {
		return employeeFeedback;
	}

	public void setEmployeeFeedback(String employeeFeedback) {
		this.employeeFeedback = employeeFeedback;
	}

	public String getManagerFeedback() {
		return managerFeedback;
	}

	public void setManagerFeedback(String managerFeedback) {
		this.managerFeedback = managerFeedback;
	}

	public EmployeeReview getEmployeeReview() {
		return employeeReview;
	}

	public void setEmployeeReview(EmployeeReview employeeReview) {
		this.employeeReview = employeeReview;
	}
    
}
