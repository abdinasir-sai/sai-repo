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
import jakarta.validation.constraints.Size;

/**
* Auto-generated by:
* org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
*/
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name="EMPLOYEE_DAILY_ATTENDANCE")
public class EmployeeDailyAttendance extends BaseEntitySaaS{
@Size(max = 40)
@Basic
@Column(length=40)
private String comments;

@Basic
@Column(name="DEVIATION_TIME", length=19)
private Date deviationTime;

@Basic
@Column(name="EARLY_ENTRY", length=19)
private Date earlyEntry;

@Basic
@Column(name="EARLY_EXIT", length=19)
private Date earlyExit;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name="EMPLOYEEID", columnDefinition="INT")
private Employee employee;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name="EMPLOYEE_LEAVE_TYPE_FK", columnDefinition="INT")
private EmployeeLeaveType employeeLeaveType;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name="LEAVES_FK", columnDefinition="INT")
private Leaves leaves;

@Basic
@Column(name="EXPECTED_FROM_TIME", length=19)
private Date expectedFromTime;

@Basic
@Column(name="EXPECTED_TO_TIME", length=19)
private Date expectedToTime;

@Basic
@Column(name="FIRST_CHECK_IN", length=19)
private Date firstCheckIn;

@Size(max = 100)
@Basic
@Column(name="FIRST_CHECK_IN_LOCATION", length=100)
private String firstCheckInLocation;

@Basic
@Column(name="FIRST_CHECK_OUT", length=19)
private Date firstCheckOut;

@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@Id
@Column(columnDefinition="INT")
private Integer id;

@Size(max = 100)
@Basic
@Column(name="LAST_CHECK_OUT_LOCATION", length=100)
private String lastCheckOutLocation;

@Basic
@Column(name="LATE_ENTRY", length=19)
private Date lateEntry;

@Basic
@Column(name="LATE_EXIT", length=19)
private Date lateExit;

@Basic
@Column(name="LATE_NIGHT_OVERTIME_HOURS")
private Double lateNightOvertimeHours;

@Basic
@Column(name="LOGIN_DATE", length=19)
private Date loginDate;

@Basic
@Column(length=19)
private Date overtime;

@Basic
@Column(name="PAYABLE_HOURS")
private Double payableHours;

@Basic
@Column(name = "STATUS", columnDefinition = "ENUM('PRESENT', 'ABSENT', 'LEAVE', 'LEAVE_CANCELLED')")
private String status;

@Basic
@Column(name = "SHIFTS")
private Boolean shifts;

@Size(max = 20)
@Basic
@Column(name="SHIFT_ALLOWANCE", length=20)
private String shiftAllowance;

@Basic
@Column(name="TIME_ZONE", length=19)
private Date timeZone;

@Basic
@Column(name="TOTAL_HOURS")
private Double totalHours;

@Basic
@Column(name="WORKSPACE_ID", columnDefinition="INT")
private Integer workspaceId;

 
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name="LEAVE_TYPE_FK", columnDefinition="INT")
private LeaveType leaveType;


public EmployeeDailyAttendance() {
}

public EmployeeDailyAttendance(Integer id) {
this.id = id;
}

public String getComments() {
return comments;
}

public void setComments(String comments) {
this.comments = comments;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public Date getDeviationTime() {
return deviationTime;
}

public void setDeviationTime(Date deviationTime) {
this.deviationTime = deviationTime;
}

public Date getEarlyEntry() {
return earlyEntry;
}

public void setEarlyEntry(Date earlyEntry) {
this.earlyEntry = earlyEntry;
}

public Date getEarlyExit() {
return earlyExit;
}

public void setEarlyExit(Date earlyExit) {
this.earlyExit = earlyExit;
}

public Employee getEmployee() {
return employee;
}

public void setEmployee(Employee employee) {
this.employee = employee;
}

public Date getExpectedFromTime() {
return expectedFromTime;
}

public void setExpectedFromTime(Date expectedFromTime) {
this.expectedFromTime = expectedFromTime;
}

public Date getExpectedToTime() {
return expectedToTime;
}

public void setExpectedToTime(Date expectedToTime) {
this.expectedToTime = expectedToTime;
}

public Date getFirstCheckIn() {
return firstCheckIn;
}

public void setFirstCheckIn(Date firstCheckIn) {
this.firstCheckIn = firstCheckIn;
}

public String getFirstCheckInLocation() {
return firstCheckInLocation;
}

public void setFirstCheckInLocation(String firstCheckInLocation) {
this.firstCheckInLocation = firstCheckInLocation;
}

public Date getFirstCheckOut() {
return firstCheckOut;
}

public void setFirstCheckOut(Date firstCheckOut) {
this.firstCheckOut = firstCheckOut;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getLastCheckOutLocation() {
return lastCheckOutLocation;
}

public void setLastCheckOutLocation(String lastCheckOutLocation) {
this.lastCheckOutLocation = lastCheckOutLocation;
}

public Date getLateEntry() {
return lateEntry;
}

public void setLateEntry(Date lateEntry) {
this.lateEntry = lateEntry;
}

public Date getLateExit() {
return lateExit;
}

public void setLateExit(Date lateExit) {
this.lateExit = lateExit;
}

public Double getLateNightOvertimeHours() {
return lateNightOvertimeHours;
}

public void setLateNightOvertimeHours(Double lateNightOvertimeHours) {
this.lateNightOvertimeHours = lateNightOvertimeHours;
}

public EmployeeLeaveType getEmployeeLeaveType() {
	return employeeLeaveType;
}

public void setEmployeeLeaveType(EmployeeLeaveType employeeLeaveType) {
	this.employeeLeaveType = employeeLeaveType;
}

public Leaves getLeaves() {
	return leaves;
}

public void setLeaves(Leaves leaves) {
	this.leaves = leaves;
}

public Date getLoginDate() {
return loginDate;
}

public void setLoginDate(Date loginDate) {
this.loginDate = loginDate;
}

public Boolean getShifts() {
	return shifts;
}

public void setShifts(Boolean shifts) {
	this.shifts = shifts;
}

public Date getOvertime() {
return overtime;
}

public void setOvertime(Date overtime) {
this.overtime = overtime;
}

public Double getPayableHours() {
return payableHours;
}

public void setPayableHours(Double payableHours) {
this.payableHours = payableHours;
}

public String getShiftAllowance() {
return shiftAllowance;
}

public void setShiftAllowance(String shiftAllowance) {
this.shiftAllowance = shiftAllowance;
}

public Date getTimeZone() {
return timeZone;
}

public void setTimeZone(Date timeZone) {
this.timeZone = timeZone;
}

public Double getTotalHours() {
return totalHours;
}

public void setTotalHours(Double totalHours) {
this.totalHours = totalHours;
}

public Integer getWorkspaceId() {
return workspaceId;
}

public void setWorkspaceId(Integer workspaceId) {
this.workspaceId = workspaceId;
}

public LeaveType getLeaveType() {
	return leaveType;
}

public void setLeaveType(LeaveType leaveType) {
	this.leaveType = leaveType;
}

}
