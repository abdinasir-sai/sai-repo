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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "RISK_TYPE")
@Filters(value = { @Filter(name = "riskTypeIdGtFilter", condition = "ID > :id"), @Filter(name = "riskTypeIdNInFilter", condition = "ID not in (:id)"), @Filter(name = "riskTypeIdLtEqFilter", condition = "ID <= :id"), @Filter(name = "riskTypeIdLtFilter", condition = "ID < :id"), @Filter(name = "riskTypeIdEqFilter", condition = "ID = :id"), @Filter(name = "riskTypeIdNEqFilter", condition = "ID != :id"), @Filter(name = "riskTypeIdInFilter", condition = "ID in (:id)"), @Filter(name = "riskTypeIdBwFilter", condition = "ID > :id_MIN  AND ID < :id_MAX"), @Filter(name = "riskTypeIdGtEqFilter", condition = "ID >= :id"), @Filter(name = "riskTypeIdEqFilter", condition = "ID = :id"), @Filter(name = "riskTypeTypeNameNInFilter", condition = "TYPE_NAME not in (:typeName)"), @Filter(name = "riskTypeTypeNameEqFilter", condition = "TYPE_NAME = :typeName"), @Filter(name = "riskTypeTypeNameNEqFilter", condition = "TYPE_NAME != :typeName"), @Filter(name = "riskTypeTypeNameInFilter", condition = "TYPE_NAME in (:typeName)") })
@FilterDefs(value = { @FilterDef(name = "riskTypeIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "riskTypeIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "riskTypeIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "riskTypeIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "riskTypeIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "riskTypeIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "riskTypeIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "riskTypeIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "riskTypeIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "riskTypeTypeNameNInFilter", parameters = { @ParamDef(name = "typeName", type = String.class) }), @FilterDef(name = "riskTypeTypeNameEqFilter", parameters = { @ParamDef(name = "typeName", type = String.class) }), @FilterDef(name = "riskTypeTypeNameNEqFilter", parameters = { @ParamDef(name = "typeName", type = String.class) }), @FilterDef(name = "riskTypeTypeNameInFilter", parameters = { @ParamDef(name = "typeName", type = String.class) }) })
public class RiskType extends BaseEntitySaaS{

    @Basic
    private boolean deleted;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

    @Size(max = 50)
    @Basic
    @Column(name = "TYPE_NAME", nullable = false, length = 50)
    private String typeName;

    public RiskType() {
    }

    public RiskType(Integer id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
