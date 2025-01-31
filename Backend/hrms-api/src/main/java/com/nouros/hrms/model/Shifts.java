package com.nouros.hrms.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
* Auto-generated by:
* org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
*/
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name="SHIFTS")
public class Shifts extends BaseEntitySaaS{
@Size(max = 100)
@Basic
@Column(name = "DEPARTMENTS" ,length=100)
private String departments;

@Size(max = 100)
@Basic
@Column(name ="DIVISION" ,length=100)
private String division;

@Size(max = 100)
@Basic
@Column(name = "EMPLOYEE" , length=100)
private String employee;

@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
@OneToMany(targetEntity=com.nouros.hrms.model.EmployeeShift.class, mappedBy="shifts", cascade=CascadeType.MERGE)
private Set<EmployeeShift> employeeShifts = new HashSet<>();

@Basic
@Column(name="END_TIME", length=19)
private Date endTime;

@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@Id
@Column(name = "ID" , columnDefinition="INT")
private Integer id;

@Size(max = 100)
@Basic
@Column(name = "LOCATIONS" , length=100)
private String locations;


@Basic
@Column(name = "SHIFT_ALLOWANCE", columnDefinition = "ENUM", length = 2)
private String shiftAllowance;

@Basic
@Column(name = "SHIFT_MARGIN", columnDefinition = "ENUM", length = 2)
private String shiftMargin;

@Size(max = 20)
@Basic
@Column(name="SHIFT_HOURS", length=20)
private String shiftHours;

@Size(max = 30)
@Basic
@Column(name="SHIFT_NAME", length=30)
private String shiftName;

@Basic
@Column(name="START_TIME", length=19)
private Date startTime;

@Basic
@Column(name="HOURS_BEFORE_SHIFT_STARTS", length=19)
private Date hoursBeforeShiftStarts;

@Basic
@Column(name="HOURS_BEFORE_SHIFT_ENDS", length=19)
private Date hoursBeforeShiftEnds;

@Basic
@Column(name = "WEEKEND", columnDefinition = "ENUM", length = 2)
private String weekend;

@Basic
@Column(name="WORKSPACE_ID", columnDefinition="INT")
private Integer workspaceId;

@Basic
@Column(name="RATE_PER_DAY")
private Double ratePerDay;

public Shifts() {
}

public Shifts(Integer id) {
this.id = id;
}

public String getDepartments() {
return departments;
}

public void setDepartments(String departments) {
this.departments = departments;
}

public String getDivision() {
return division;
}

public void setDivision(String division) {
this.division = division;
}

public String getEmployee() {
return employee;
}

public void setEmployee(String employee) {
this.employee = employee;
}

public Set<EmployeeShift> getEmployeeShifts() {
return employeeShifts;
}

public void setEmployeeShifts(Set<EmployeeShift> employeeShifts) {
this.employeeShifts = employeeShifts;
}

public Date getEndTime() {
return endTime;
}

public void setEndTime(Date endTime) {
this.endTime = endTime;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getShiftMargin() {
	return shiftMargin;
}

public void setShiftMargin(String shiftMargin) {
	this.shiftMargin = shiftMargin;
}

public String getLocations() {
return locations;
}

public void setLocations(String locations) {
this.locations = locations;
}

public String getShiftAllowance() {
return shiftAllowance;
}

public void setShiftAllowance(String shiftAllowance) {
this.shiftAllowance = shiftAllowance;
}

public String getShiftHours() {
return shiftHours;
}

public void setShiftHours(String shiftHours) {
this.shiftHours = shiftHours;
}

public String getShiftName() {
return shiftName;
}

public void setShiftName(String shiftName) {
this.shiftName = shiftName;
}

public Date getStartTime() {
return startTime;
}

public void setStartTime(Date startTime) {
this.startTime = startTime;
}

public String getWeekend() {
	return weekend;
}

public Double getRatePerDay() {
	return ratePerDay;
}

public void setRatePerDay(Double ratePerDay) {
	this.ratePerDay = ratePerDay;
}

public void setWeekend(String weekend) {
	this.weekend = weekend;
}

public Integer getWorkspaceId() {
return workspaceId;
}

public Date getHoursBeforeShiftStarts() {
	return hoursBeforeShiftStarts;
}

public void setHoursBeforeShiftStarts(Date hoursBeforeShiftStarts) {
	this.hoursBeforeShiftStarts = hoursBeforeShiftStarts;
}

public Date getHoursBeforeShiftEnds() {
	return hoursBeforeShiftEnds;
}

public void setHoursBeforeShiftEnds(Date hoursBeforeShiftEnds) {
	this.hoursBeforeShiftEnds = hoursBeforeShiftEnds;
}

public void setWorkspaceId(Integer workspaceId) {
this.workspaceId = workspaceId;
}
}
