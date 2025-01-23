package com.nouros.hrms.model;

import java.util.Date;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.enttribe.product.pii.listners.PrePersistListener;
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
@Table(name = "APPLICANT_NATIONAL_IDENTIFICATION")
@EntityListeners(PrePersistListener.class)
public class ApplicantNationalIdentification extends BaseEntitySaaS{
	 @Size(max = 100)
	    @Basic
	    @Column(name = "BORDER_NUMBER", length = 100)
	    private String borderNumber;

	    @Basic
	    private boolean deleted;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "APPLICANT_FK", columnDefinition = "INT")
	    private Applicant applicant;

	    @Basic
	    @Column(name = "EXPIRY_DATE", length = 19)
	    private Date expiryDate;

	    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	    @Id
	    @Column(columnDefinition = "INT")
	    private Integer id;

	    @Size(max = 50)
	    @Basic
	    @Column(name = "IDENTIFICATION_NUMBER", length = 50)
	    private String identificationNumber;

	    @Size(max = 255)
	    @Basic
	    @Column(name = "SCANNED_IMAGE")
	    private String scannedImage;

	    @Size(max = 100)
	    @Basic
	    @Column(length = 100)
	    private String type;
	    
	    @Size(max = 255)
	    @Basic
	    @Column(name = "COUNTRY_OF_ISSUE")
	    private String countryOfIssue;

	    public ApplicantNationalIdentification() {
	    }

	    public ApplicantNationalIdentification(Integer id) {
	        this.id = id;
	    }

	    public String getBorderNumber() {
	        return borderNumber;
	    }

	    public void setBorderNumber(String borderNumber) {
	        this.borderNumber = borderNumber;
	    }

	    public boolean isDeleted() {
	        return deleted;
	    }

	    public void setDeleted(boolean deleted) {
	        this.deleted = deleted;
	    }

	    

	    public Applicant getApplicant() {
			return applicant;
		}

		public void setApplicant(Applicant applicant) {
			this.applicant = applicant;
		}

		public Date getExpiryDate() {
	        return expiryDate;
	    }

	    public void setExpiryDate(Date expiryDate) {
	        this.expiryDate = expiryDate;
	    }

	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public String getIdentificationNumber() {
	        return identificationNumber;
	    }

	    public void setIdentificationNumber(String identificationNumber) {
	        this.identificationNumber = identificationNumber;
	    }

	    public String getScannedImage() {
	        return scannedImage;
	    }

	    public void setScannedImage(String scannedImage) {
	        this.scannedImage = scannedImage;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

		public String getCountryOfIssue() {
			return countryOfIssue;
		}

		public void setCountryOfIssue(String countryOfIssue) {
			this.countryOfIssue = countryOfIssue;
		}
	    
	    

}
