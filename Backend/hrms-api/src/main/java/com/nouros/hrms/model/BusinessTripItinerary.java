package com.nouros.hrms.model;

import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;



@Entity
@Table(name = "BUSINESS_TRIP_ITINERARY")
public class BusinessTripItinerary extends BaseEntitySaaS{
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(name="ID",columnDefinition = "INT")
    private Integer id;
	
	@Basic
    @Column(name = "TRAVEL_START_DATE", length = 19)
    private Date travelStartDate;
	
	@Basic
    @Column(name = "TRAVEL_END_DATE", length = 19)
    private Date travelEndDate;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_START_ARRIVAL_CITY", length = 255)
    private String travelStartArrivalCity;
	
	@Size(max = 100)
    @Basic
    @Column(name = "TRAVEL_FROM_COUNTRY", length = 100)
    private String travelFromCountry;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_END_DEPARTURE_CITY", length = 255)
    private String travelEndDepartureCity;
	
	@Size(max = 100)
    @Basic
    @Column(name = "TRAVEL_TO_COUNTRY", length = 100)
    private String travelToCountry;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_START_ARRIVAL_AIRPORT", length = 255)
    private String travelStartArrivalAirport;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_END_ARRIVAL_AIRPORT", length = 255)
    private String travelEndArrivalAirport;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_START_DEPARTURE_AIRPORT", length = 255)
    private String travelStartDepartureAirport;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_START_DEPARTURE_CITY", length = 255)
    private String travelStartDepartureCity;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TRAVEL_END_DEPARTURE_AIRPORT", length = 255)
    private String travelEndDepartureAirport;
	
	
	@Basic
    @Column(name = "TOTAL_BUSINESS_DAYS", columnDefinition = "INT")
    private Integer totalBusinessDays;
	
	@Basic
    @Column(name = "PAY_VALUE")
    private Double payValue;
	
	@Basic
    @Column(name = "TOTAL_LEAVES", columnDefinition = "INT")
    private Integer totalLeaves;
	
	@Basic
	@Column(name = "BUSINESS_DETAIL_JSON" ,  columnDefinition = "json")
	private String businessDetailJson;
	
	@Basic
    @Column(name = "PREFERRED_DEPARTURE_TIME")
	Date preferredDepartureTime;
	
	@Basic
    @Column(name = "CREATED_TIME", length = 19)
    private Date createdTime;
	

	@Basic
    @Column(name = "MODIFIED_TIME", length = 19)
    private Date modifiedTime;
	
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;
 
	@Column(name = "WORKFLOW_STAGE")
	private String workflowStage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTravelStartDate() {
		return travelStartDate;
	}

	public void setTravelStartDate(Date travelStartDate) {
		this.travelStartDate = travelStartDate;
	}

	public Date getTravelEndDate() {
		return travelEndDate;
	}

	public void setTravelEndDate(Date travelEndDate) {
		this.travelEndDate = travelEndDate;
	}

	public String getTravelStartArrivalCity() {
		return travelStartArrivalCity;
	}

	public void setTravelStartArrivalCity(String travelStartArrivalCity) {
		this.travelStartArrivalCity = travelStartArrivalCity;
	}

	public String getTravelFromCountry() {
		return travelFromCountry;
	}

	public void setTravelFromCountry(String travelFromCountry) {
		this.travelFromCountry = travelFromCountry;
	}

	public String getTravelEndDepartureCity() {
		return travelEndDepartureCity;
	}

	public void setTravelEndDepartureCity(String travelEndDepartureCity) {
		this.travelEndDepartureCity = travelEndDepartureCity;
	}

	public String getTravelToCountry() {
		return travelToCountry;
	}

	public void setTravelToCountry(String travelToCountry) {
		this.travelToCountry = travelToCountry;
	}

	public String getTravelStartArrivalAirport() {
		return travelStartArrivalAirport;
	}

	public void setTravelStartArrivalAirport(String travelStartArrivalAirport) {
		this.travelStartArrivalAirport = travelStartArrivalAirport;
	}

	public String getTravelEndArrivalAirport() {
		return travelEndArrivalAirport;
	}

	public void setTravelEndArrivalAirport(String travelEndArrivalAirport) {
		this.travelEndArrivalAirport = travelEndArrivalAirport;
	}

	public String getTravelStartDepartureAirport() {
		return travelStartDepartureAirport;
	}

	public void setTravelStartDepartureAirport(String travelStartDepartureAirport) {
		this.travelStartDepartureAirport = travelStartDepartureAirport;
	}

	public String getTravelEndDepartureAirport() {
		return travelEndDepartureAirport;
	}

	public void setTravelEndDepartureAirport(String travelEndDepartureAirport) {
		this.travelEndDepartureAirport = travelEndDepartureAirport;
	}

	public Integer getTotalBusinessDays() {
		return totalBusinessDays;
	}

	public void setTotalBusinessDays(Integer totalBusinessDays) {
		this.totalBusinessDays = totalBusinessDays;
	}

	public Double getPayValue() {
		return payValue;
	}

	public void setPayValue(Double payValue) {
		this.payValue = payValue;
	}

	public Integer getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(Integer totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	public String getBusinessDetailJson() {
		return businessDetailJson;
	}

	public void setBusinessDetailJson(String businessDetailJson) {
		this.businessDetailJson = businessDetailJson;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

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

	public String getTravelStartDepartureCity() {
		return travelStartDepartureCity;
	}

	public void setTravelStartDepartureCity(String travelStartDepartureCity) {
		this.travelStartDepartureCity = travelStartDepartureCity;
	}
 
	public Date getPreferredDepartureTime() {
		return preferredDepartureTime;
	}

	public void setPreferredDepartureTime(Date preferredDepartureTime) {
		this.preferredDepartureTime = preferredDepartureTime;
	}
	
	

}
