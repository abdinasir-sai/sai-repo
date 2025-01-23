package com.nouros.hrms.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.enttribe.product.pii.listners.PrePersistListener;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Audited
@Table(name = "EMPLOYEE_EMERGENCY_CONTACT")
@JsonFilter("propertyFilter")
@EntityListeners(PrePersistListener.class)
@Filters(value = { @Filter(name = "employeeEmergencyContactEmployeeIdInFilter",condition = "EMPLOYEE_ID IN (select e.ID FROM EMPLOYEE e WHERE e.REPORTING_MANAGER_USERID_FK = (:id) OR e.USERID_PK = (:id)) ")})
@FilterDefs(value = { @FilterDef(name = "employeeEmergencyContactEmployeeIdInFilter", parameters = {@ParamDef(name = "id", type = Integer.class)})})
public class EmployeeEmergencyContact extends BaseEntitySaaS{
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
    private Employee employeeId;
	
	@Size(max = 50)
    @Basic
    @Column(name = "EMERGENCY_CONTACT_FIRST_NAME", length = 50)
    private String emergencyContactFirstName;
	
	@Size(max = 50)
    @Basic
    @Column(name = "EMERGENCY_CONTACT_MIDDLE_NAME", length = 50)
    private String emergencyContactMiddleName;
	
	@Size(max = 50)
    @Basic
    @Column(name = "EMERGENCY_CONTACT_LAST_NAME", length = 50)
    private String emergencyContactLastName;
	
	
	@Size(max = 100)
    @Basic
    @Column(name = "EMERGENCY_CONTACT_FULL_NAME", length = 100)
    private String emergencyContactFullName;
	
	@Size(max = 50)
    @Basic
    @Column(name = "EMERGENCY_CONTACT_NUMBER", length = 50)
    private String emergencyContactNumber;
	
	@Size(max = 80)
    @Basic
    @Column(name = "EMERGENCY_CONTACT_EMAIL_ADDRESS", length = 80)
    private String emergencyContactEmailAddress;
	
	
	@Size(max = 50)
    @Basic
    @Column(name = "RELATIONSHIP", length = 50)
    private String relationship;

	@Basic
	@Column(name = "ADDRESS", length = 255)
	private String address;
	
	@Basic
	@Column(name = "CITY", length = 255)
	private String city;
	
	@Basic
	@Column(name = "COUNTRY", length = 255)
	private String country;
	
	@Basic
	@Column(name = "POSTAL_CODE", length = 255)
	private String postalCode;

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getEmergencyContactFirstName() {
		return emergencyContactFirstName;
	}


	public void setEmergencyContactFirstName(String emergencyContactFirstName) {
		this.emergencyContactFirstName = emergencyContactFirstName;
	}


	public String getEmergencyContactMiddleName() {
		return emergencyContactMiddleName;
	}


	public void setEmergencyContactMiddleName(String emergencyContactMiddleName) {
		this.emergencyContactMiddleName = emergencyContactMiddleName;
	}


	public String getEmergencyContactLastName() {
		return emergencyContactLastName;
	}


	public void setEmergencyContactLastName(String emergencyContactLastName) {
		this.emergencyContactLastName = emergencyContactLastName;
	}


	public String getEmergencyContactFullName() {
		return emergencyContactFullName;
	}


	public void setEmergencyContactFullName(String emergencyContactFullName) {
		this.emergencyContactFullName = emergencyContactFullName;
	}


	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}


	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}


	public String getEmergencyContactEmailAddress() {
		return emergencyContactEmailAddress;
	}


	public void setEmergencyContactEmailAddress(String emergencyContactEmailAddress) {
		this.emergencyContactEmailAddress = emergencyContactEmailAddress;
	}

	public Employee getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(Employee employeeId) {
		this.employeeId = employeeId;
	}


	public String getRelationship() {
		return relationship;
	}


	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getPostalCode() {
		return postalCode;
	}


	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	
	

}
