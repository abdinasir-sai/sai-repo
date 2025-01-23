package com.nouros.payrollmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nouros.hrms.model.BaseEntity;
import com.nouros.hrms.model.BaseEntitySaaS;
import com.nouros.hrms.model.Employee;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "OTHER_SALARY_PAYROLL_MAPPING")
public class OtherSalaryPayrollMapping extends BaseEntitySaaS{
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
	@Column(columnDefinition = "INT")
	private Integer id;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_FK", columnDefinition = "INT")
    private Employee employee;
	
	
	@Column(name = "PAYROLL_RUN_ID")
	private Integer payrollRunId;
	
	@Basic
	@Column(name = "AMOUNT")
	private Double amount;
	
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OTHER_SALARY_COMPONENT_FK", columnDefinition = "INT")
    private OtherSalaryComponent otherSalaryComponent;
	
	
	
}
