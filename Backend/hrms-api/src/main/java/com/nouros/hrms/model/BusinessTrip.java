package com.nouros.hrms.model;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
 
 
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Audited
@Table(name = "BUSINESS_TRIP")
@Filters(value = { @Filter(name = "businessTripEmployeeIdInFilter",condition = "EMPLOYEE_ID IN (select e.ID FROM EMPLOYEE e WHERE e.REPORTING_MANAGER_USERID_FK = (:id) OR e.USERID_PK = (:id)) "),
@Filter(name = "businessTripEmployeeDepartmentInFilter", condition = "EMPLOYEE_ID IN ( SELECT e.ID FROM EMPLOYEE e where e.DEPARTMENT_ID IN (SELECT d.ID FROM DEPARTMENT d WHERE d.NAME IN (:department)))")})
@FilterDefs(value = { @FilterDef(name = "businessTripEmployeeIdInFilter", parameters = {@ParamDef(name = "id", type = Integer.class)}),
@FilterDef(name = "businessTripEmployeeDepartmentInFilter", parameters = @ParamDef(name = "department", type = String.class))})

public class BusinessTrip extends BaseEntitySaaS{
	
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;
	
	
	@Size(max = 255)
    @Basic
    @Column(name = "REQUEST_ID", length = 255)
    private String requestId;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_START_ARRIVAL_CITY", length = 255)
    private String travelStartArrivalCity;
	
	@Basic
    @Column(name = "TRAVEL_END_DATE", length = 19)
    private Date travelEndDate;
	
	@Basic
    @Column(name = "TRAVEL_START_DATE", length = 19)
    private Date travelStartDate;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_START_DEPARTURE_AIRPORT", length = 255)
    private String travelStartDepartureAirport;
	
	@Size(max = 100)
    @Basic
    @Column(name = "TRAVEL_FROM_COUNTRY", length = 100)
    private String travelFromCountry;
	
	@Size(max = 100)
    @Basic
    @Column(name = "TRAVEL_TO_COUNTRY", length = 100)
    private String travelToCountry;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_START_DEPARTURE_CITY", length = 255)
    private String travelStartDepartureCity;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_START_ARRIVAL_AIRPORT", length = 255)
    private String travelStartArrivalAirport;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_END_DEPARTURE_AIRPORT", length = 255)
    private String travelEndDepartureAirport;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_END_DEPARTURE_CITY", length = 255)
    private String travelEndDepartureCity;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_END_ARRIVAL_AIRPORT", length = 255)
    private String travelEndArrivalAirport;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_END_ARRIVAL_CITY", length = 255)
    private String travelEndArrivalCity;
	
	@Basic
    @Column(name = "BUSINESS_START_DATE", length = 19)
    private Date businessStartDate;
	
	@Basic
    @Column(name = "BUSINESS_END_DATE", length = 19)
    private Date businessEndtDate;
	
	@Basic
    @Column(name = "TOTAL_BUSINESS_DAYS", columnDefinition = "INT")
    private Integer totalBusinessDays;
	
	@Basic
    @Column(name = "TOTAL_LEAVES", columnDefinition = "INT")
    private Integer totalLeaves;
	
	@Basic
    @Column(name = "PAY_VALUE")
    private Double payValue;
	
	@Size(max = 255)
    @Basic
    @Column(name = "ATTACHMENT", length = 255)
    private String attachment;
	
    @Basic
    @Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
    private String description;
	
	@Size(max = 255)
    @Basic
    @Column(name = "REMARK", length = 255)
    private String remark;
	
//	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@NotAudited
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinTable(name = "ITINERARY_BUSINESS_TRIP_MAPPING",joinColumns = { @JoinColumn(name = "BUSINESS_TRIP_ID") }, inverseJoinColumns = {
	@JoinColumn(name = "BUSINESS_TRIP_ITINERARY_ID", referencedColumnName = "ID") })
	private List<BusinessTripItinerary> businessTripItinerarys;
	
    @Basic
    @Column(name = "BUSINESS_TRAVEL_TYPE", columnDefinition = "ENUM('ONE_WAY','ROUND_TRIP','MULTI_CITY')")
    private String businessTravelType;
    
    @Basic
    @Column(name = "TRAVEL_SCOPE", columnDefinition = "ENUM('INTERNAL','EXTERNAL')")
    private String travelScope;
    
    @Basic
    @Column(name = "IS_ASSISTANCE_NEEDED")
    private boolean isAssistanceNeeded;
    
	@Column(name="OTHER_EXPENSE_BANK_REQUEST_ID")
	private Integer otherExpenseBankRequestId;
	
	public boolean isAssistanceNeeded() {
		return isAssistanceNeeded;
	}


	public void setAssistanceNeeded(boolean isAssistanceNeeded) {
		this.isAssistanceNeeded = isAssistanceNeeded;
	}


	public String getBusinessTravelType() {
		return businessTravelType;
	}


	public void setBusinessTravelType(String businessTravelType) {
		this.businessTravelType = businessTravelType;
	}


	public List<BusinessTripItinerary> getBusinessTripItinerarys() {
		return businessTripItinerarys;
	}


	public void setBusinessTripItinerarys(List<BusinessTripItinerary> businessTripItinerarys) {
		this.businessTripItinerarys = businessTripItinerarys;
	}


	public String getTravelFromCountry() {
		return travelFromCountry;
	}


	public void setTravelFromCountry(String travelFromCountry) {
		this.travelFromCountry = travelFromCountry;
	}


	public String getTravelToCountry() {
		return travelToCountry;
	}


	public void setTravelToCountry(String travelToCountry) {
		this.travelToCountry = travelToCountry;
	}


	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
    private Employee employee;
	

	@Basic
    @Column(name = "WORKSPACE_ID", columnDefinition = "INT")
    private Integer workspaceId;
 
    public BusinessTrip() {
		super();
	}
 
 
	public Integer getId() {
		return id;
	}
 
	public void setId(Integer id) {
		this.id = id;
	}
 

	public String getRequestId() {
		return requestId;
	}
 
 
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
 
 
	public String getTravelStartArrivalCity() {
		return travelStartArrivalCity;
	}
 
 
	public void setTravelStartArrivalCity(String travelStartArrivalCity) {
		this.travelStartArrivalCity = travelStartArrivalCity;
	}
 
 
	public Date getTravelEndDate() {
		return travelEndDate;
	}
 
 
	public void setTravelEndDate(Date travelEndDate) {
		this.travelEndDate = travelEndDate;
	}
 
 
	public String getTravelEndDepartureAirport() {
		return travelEndDepartureAirport;
	}
 
 
	public void setTravelEndDepartureAirport(String travelEndDepartureAirport) {
		this.travelEndDepartureAirport = travelEndDepartureAirport;
	}
  
	public String getTravelEndDepartureCity() {
		return travelEndDepartureCity;
	}
 
 
	public void setTravelEndDepartureCity(String travelEndDepartureCity) {
		this.travelEndDepartureCity = travelEndDepartureCity;
	}
 
 
	public String getTravelEndArrivalAirport() {
		return travelEndArrivalAirport;
	}
 
 
	public void setTravelEndArrivalAirport(String travelEndArrivalAirport) {
		this.travelEndArrivalAirport = travelEndArrivalAirport;
	}
 
 
	public String getTravelEndArrivalCity() {
		return travelEndArrivalCity;
	}
 
 
	public void setTravelEndArrivalCity(String travelEndArrivalCity) {
		this.travelEndArrivalCity = travelEndArrivalCity;
	}
 
 
	public Date getBusinessStartDate() {
		return businessStartDate;
	}
 
 
	public void setBusinessStartDate(Date businessStartDate) {
		this.businessStartDate = businessStartDate;
	}
 
 
	public Date getBusinessEndtDate() {
		return businessEndtDate;
	}
 
 
	public void setBusinessEndtDate(Date businessEndtDate) {
		this.businessEndtDate = businessEndtDate;
	}
 
 
	public Integer getTotalBusinessDays() {
		return totalBusinessDays;
	}
 
 
	public void setTotalBusinessDays(Integer totalBusinessDays) {
		this.totalBusinessDays = totalBusinessDays;
	}
 
 
	public Integer getTotalLeaves() {
		return totalLeaves;
	}
 
 
	public void setTotalLeaves(Integer totalLeaves) {
		this.totalLeaves = totalLeaves;
	}
 
 
	public Double getPayValue() {
		return payValue;
	}
 
 
	public void setPayValue(Double payValue) {
		this.payValue = payValue;
	}
 
 
	public String getAttachment() {
		return attachment;
	}
 
 
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
 
 
	public String getDescription() {
		return description;
	}
 
 
	public void setDescription(String description) {
		this.description = description;
	}
 
 
	public String getRemark() {
		return remark;
	}
 
 
	public void setRemark(String remark) {
		this.remark = remark;
	}
 

	public Employee getEmployee() {
		return employee;
	}


	public void setEmployee(Employee employee) {
		this.employee = employee;
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
 		
	public Date getTravelStartDate() {
		return travelStartDate;
	}


	public void setTravelStartDate(Date travelStartDate) {
		this.travelStartDate = travelStartDate;
	}


	public String getTravelStartDepartureAirport() {
		return travelStartDepartureAirport;
	}


	public void setTravelStartDepartureAirport(String travelStartDepartureAirport) {
		this.travelStartDepartureAirport = travelStartDepartureAirport;
	}


	public String getTravelStartDepartureCity() {
		return travelStartDepartureCity;
	}


	public void setTravelStartDepartureCity(String travelStartDepartureCity) {
		this.travelStartDepartureCity = travelStartDepartureCity;
	}


	public String getTravelStartArrivalAirport() {
		return travelStartArrivalAirport;
	}


	public void setTravelStartArrivalAirport(String travelStartArrivalAirport) {
		this.travelStartArrivalAirport = travelStartArrivalAirport;
	}
	


	public String getTravelScope() {
		return travelScope;
	}


	public void setTravelScope(String travelScope) {
		this.travelScope = travelScope;
	}


	public Integer getWorkspaceId() {
		return workspaceId;
	}
 
 
	public void setWorkspaceId(Integer workspaceId) {
		this.workspaceId = workspaceId;
	}
 
 
	
	
	public Integer getOtherExpenseBankRequestId() {
		return otherExpenseBankRequestId;
	}


	public void setOtherExpenseBankRequestId(Integer otherExpenseBankRequestId) {
		this.otherExpenseBankRequestId = otherExpenseBankRequestId;
	}


	public List<WorkflowActions> getActions() {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class)
				.getWorkflowActions(id, "BusinessTrip");
	}	
 
}
