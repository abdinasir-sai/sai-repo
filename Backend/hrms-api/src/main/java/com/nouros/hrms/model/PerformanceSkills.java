package com.nouros.hrms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "PERFORMANCE_SKILLS")
public class PerformanceSkills extends BaseEntitySaaS{

	@Basic
	@Column(columnDefinition = "LONGTEXT")
    private String description;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

    @Size(max = 100)
    @Basic
    @Column(name="NAME",length = 100)
    private String name;
    
    @Basic
    @Column(name="WEIGHTAGE")
    private Double weightage;
    
    @Basic
    @Column(name="EMPLOYEE_PROGRESS")
    private Double employeeProgress;
	
    @Basic
    @Column(name="REVIEWED_PROGRESS")
    private Double reviewedProgress;
}
