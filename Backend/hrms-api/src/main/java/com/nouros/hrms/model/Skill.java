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
@Table(name = "SKILL")
@Filters(value = { @Filter(name = "skillDescriptionNInFilter", condition = "DESCRIPTION not in (:description)"),
		@Filter(name = "skillDescriptionEqFilter", condition = "DESCRIPTION = :description"),
		@Filter(name = "skillDescriptionNEqFilter", condition = "DESCRIPTION != :description"),
		@Filter(name = "skillDescriptionInFilter", condition = "DESCRIPTION in (:description)"),
		@Filter(name = "skillIdGtFilter", condition = "ID > :id"),
		@Filter(name = "skillIdNInFilter", condition = "ID not in (:id)"),
		@Filter(name = "skillIdLtEqFilter", condition = "ID <= :id"),
		@Filter(name = "skillIdLtFilter", condition = "ID < :id"),
		@Filter(name = "skillIdEqFilter", condition = "ID = :id"),
		@Filter(name = "skillIdNEqFilter", condition = "ID != :id"),
		@Filter(name = "skillIdInFilter", condition = "ID in (:id)"),
		@Filter(name = "skillIdBwFilter", condition = "ID > :id_MIN  AND ID < :id_MAX"),
		@Filter(name = "skillIdGtEqFilter", condition = "ID >= :id"),
		@Filter(name = "skillIdEqFilter", condition = "ID = :id"),
		@Filter(name = "skillNameNInFilter", condition = "NAME not in (:name)"),
		@Filter(name = "skillNameEqFilter", condition = "NAME = :name"),
		@Filter(name = "skillNameNEqFilter", condition = "NAME != :name"),
		@Filter(name = "skillNameInFilter", condition = "NAME in (:name)"),
		@Filter(name = "skillWorkspaceIdGtFilter", condition = "WORKSPACE_ID > :workspaceId"),
		@Filter(name = "skillWorkspaceIdNInFilter", condition = "WORKSPACE_ID not in (:workspaceId)"),
		@Filter(name = "skillWorkspaceIdLtEqFilter", condition = "WORKSPACE_ID <= :workspaceId"),
		@Filter(name = "skillWorkspaceIdLtFilter", condition = "WORKSPACE_ID < :workspaceId"),
		@Filter(name = "skillWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId"),
		@Filter(name = "skillWorkspaceIdNEqFilter", condition = "WORKSPACE_ID != :workspaceId"),
		@Filter(name = "skillWorkspaceIdInFilter", condition = "WORKSPACE_ID in (:workspaceId)"),
		@Filter(name = "skillWorkspaceIdBwFilter", condition = "WORKSPACE_ID > :workspaceId_MIN  AND WORKSPACE_ID < :workspaceId_MAX"),
		@Filter(name = "skillWorkspaceIdGtEqFilter", condition = "WORKSPACE_ID >= :workspaceId"),
		@Filter(name = "skillWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId") })
@FilterDefs(value = {
		@FilterDef(name = "skillDescriptionNInFilter", parameters = {
				@ParamDef(name = "description", type = String.class) }),
		@FilterDef(name = "skillDescriptionEqFilter", parameters = {
				@ParamDef(name = "description", type = String.class) }),
		@FilterDef(name = "skillDescriptionNEqFilter", parameters = {
				@ParamDef(name = "description", type = String.class) }),
		@FilterDef(name = "skillDescriptionInFilter", parameters = {
				@ParamDef(name = "description", type = String.class) }),
		@FilterDef(name = "skillIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }),
		@FilterDef(name = "skillIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }),
		@FilterDef(name = "skillIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }),
		@FilterDef(name = "skillIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }),
		@FilterDef(name = "skillIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }),
		@FilterDef(name = "skillIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }),
		@FilterDef(name = "skillIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }),
		@FilterDef(name = "skillIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class),
				@ParamDef(name = "id_MAX", type = Integer.class) }),
		@FilterDef(name = "skillIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }),
		@FilterDef(name = "skillNameNInFilter", parameters = { @ParamDef(name = "name", type = String.class) }),
		@FilterDef(name = "skillNameEqFilter", parameters = { @ParamDef(name = "name", type = String.class) }),
		@FilterDef(name = "skillNameNEqFilter", parameters = { @ParamDef(name = "name", type = String.class) }),
		@FilterDef(name = "skillNameInFilter", parameters = { @ParamDef(name = "name", type = String.class) }),
		@FilterDef(name = "skillWorkspaceIdGtFilter", parameters = {
				@ParamDef(name = "workspaceId", type = Integer.class) }),
		@FilterDef(name = "skillWorkspaceIdNInFilter", parameters = {
				@ParamDef(name = "workspaceId", type = Integer.class) }),
		@FilterDef(name = "skillWorkspaceIdLtEqFilter", parameters = {
				@ParamDef(name = "workspaceId", type = Integer.class) }),
		@FilterDef(name = "skillWorkspaceIdLtFilter", parameters = {
				@ParamDef(name = "workspaceId", type = Integer.class) }),
		@FilterDef(name = "skillWorkspaceIdEqFilter", parameters = {
				@ParamDef(name = "workspaceId", type = Integer.class) }),
		@FilterDef(name = "skillWorkspaceIdNEqFilter", parameters = {
				@ParamDef(name = "workspaceId", type = Integer.class) }),
		@FilterDef(name = "skillWorkspaceIdInFilter", parameters = {
				@ParamDef(name = "workspaceId", type = Integer.class) }),
		@FilterDef(name = "skillWorkspaceIdBwFilter", parameters = {
				@ParamDef(name = "workspaceId_MIN", type = Integer.class),
				@ParamDef(name = "workspaceId_MAX", type = Integer.class) }),
		@FilterDef(name = "skillWorkspaceIdGtEqFilter", parameters = {
				@ParamDef(name = "workspaceId", type = Integer.class) }) })
public class Skill extends BaseEntitySaaS {

	@Size(max = 100)
	@Basic
	@Column(length = 100)
	private String description;

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(name = "ID", columnDefinition = "INT")
	private Integer id;

	@Size(max = 100)
	@Basic
	@Column(length = 100)
	private String name;

	@Basic
	@Column(name = "WORKSPACE_ID", columnDefinition = "INT")
	private Integer workspaceId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_SKILL", columnDefinition = "INT")
	private Skill parentSkill;

	public Skill getParentSkill() {
		return parentSkill;
	}

	public void setParentSkill(Skill parentSkill) {
		this.parentSkill = parentSkill;
	}

	public Skill() {
	}

	public Skill(Integer id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(Integer workspaceId) {
		this.workspaceId = workspaceId;
	}
}
