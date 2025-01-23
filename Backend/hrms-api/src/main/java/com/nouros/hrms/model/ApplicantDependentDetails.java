package com.nouros.hrms.model;

import java.util.Date;

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
@Table(name = "APPLICANT_DEPENDENT_DETAILS")
@JsonFilter("propertyFilter")
@EntityListeners(PrePersistListener.class)
public class ApplicantDependentDetails extends BaseEntitySaaS{
	
	 @Size(max = 100)
	    @Basic
	    @Column(name = "CONTACT_NUMBER", length = 100)
	    private String contactNumber;

	    @Basic
	    @Column(name = "DATE_OF_BIRTH", length = 19)
	    private Date dateOfBirth;

	    @Basic
	    private boolean deleted;

	    @Size(max = 255)
	    @Basic
	    @Column(name = "DEPENDENT_IDENTIFICATION")
	    private String dependentIdentification;
	    
	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "APPLICANT_FK", columnDefinition = "INT")
	    private Applicant applicant;

	    @Size(max = 100)
	    @Basic
	    @Column(name = "FIRST_NAME", length = 100)
	    private String firstName;

	    @Size(max = 200)
	    @Basic
	    @Column(name = "FULL_NAME", length = 200)
	    private String fullName;

	    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	    @Id
	    @Column(columnDefinition = "INT")
	    private Integer id;

	    @Size(max = 100)
	    @Basic
	    @Column(name = "LAST_NAME", length = 100)
	    private String lastName;

	    @Size(max = 100)
	    @Basic
	    @Column(name = "MIDDLE_NAME", length = 100)
	    private String middleName;

	    @Basic
	    @Column(name = "RELATIONSHIP", columnDefinition = "ENUM('Mother','Father','Brother','Sister','Wife','Child','Husband')")
	    private String relationship;
	    
	    @Basic
	    @Column(name = "GENDER", columnDefinition = "ENUM('Male','Female','Other')")
	    private String gender;

	    public ApplicantDependentDetails() {
	    }

	    public ApplicantDependentDetails(Integer id) {
	        this.id = id;
	    }

	    public String getContactNumber() {
	        return contactNumber;
	    }

	    public void setContactNumber(String contactNumber) {
	        this.contactNumber = contactNumber;
	    }

	    public Date getDateOfBirth() {
	        return dateOfBirth;
	    }

	    public void setDateOfBirth(Date dateOfBirth) {
	        this.dateOfBirth = dateOfBirth;
	    }

	    public boolean isDeleted() {
	        return deleted;
	    }

	    public void setDeleted(boolean deleted) {
	        this.deleted = deleted;
	    }

	    public String getDependentIdentification() {
	        return dependentIdentification;
	    }

	    public void setDependentIdentification(String dependentIdentification) {
	        this.dependentIdentification = dependentIdentification;
	    }


	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public String getFullName() {
	        return fullName;
	    }

	    public void setFullName(String fullName) {
	        this.fullName = fullName;
	    }

	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public String getMiddleName() {
	        return middleName;
	    }

	    public void setMiddleName(String middleName) {
	        this.middleName = middleName;
	    }

	    public String getRelationship() {
	        return relationship;
	    }

	    public void setRelationship(String relationship) {
	        this.relationship = relationship;
	    }

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public Applicant getApplicant() {
			return applicant;
		}

		public void setApplicant(Applicant applicant) {
			this.applicant = applicant;
		}
		
		
	    

}
