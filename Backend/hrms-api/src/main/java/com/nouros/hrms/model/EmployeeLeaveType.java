package com.nouros.hrms.model;

import java.util.Date;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "EMPLOYEE_LEAVE_TYPE")
public class EmployeeLeaveType  extends BaseEntitySaaS{
	
	 @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
     @Id
	 @Column(columnDefinition = "INT")
	 private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LEAVE_TYPE_FK", columnDefinition = "INT")
    private LeaveType leaveType;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID_FK", columnDefinition = "INT")
    private Employee employeeId;
	
	@Basic
    @Column(name = "BALANCE")
    private Double balance;
	
	@Basic
    @Column(name = "TOTAL_BALANCE")
    private Double totalBalance;
	
	@Basic
	@Column(name="YEAR_START_DATE", length=19)
	private Date yearStartDate;
	
	@Basic
	@Column(name="YEAR_END_DATE", length=19)
	private Date yearEndDate;
	
	@Basic
	@Column(name="WORKSPACE_ID", columnDefinition="INT")
	private Integer workspaceId;
	
	
}
