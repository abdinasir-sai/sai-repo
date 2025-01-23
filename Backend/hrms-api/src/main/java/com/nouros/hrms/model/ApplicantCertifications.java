package com.nouros.hrms.model;

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

@Entity
@Table(name = "APPLICANT_CERTIFICATIONS")
public class ApplicantCertifications extends BaseEntitySaaS{
	
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APPLICANT_ID", columnDefinition = "INT")
	private Applicant applicantId;
    
    @Size(max = 100)
    @Basic
    @Column(name = "CERTIFICATION_NAME", length = 100)
    private String certificationName;
    
    @Size(max = 100)
    @Basic
    @Column(name = "ISSUING_INSTITUTION", length = 100)
    private String issuingInstitution;
    
    @Size(max = 100)
    @Basic
    @Column(name = "CERTIFICATION_ID", length = 100)
    private String certificationId;
    
    @Size(max = 200)
   	@Basic
   	@Column(name = "ATTACHMENT1", length = 200)
   	private String attachment1;

   	@Size(max = 200)
   	@Basic
   	@Column(name = "ATTACHMENT2", length = 200)
   	private String attachment2;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Applicant getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(Applicant applicantId) {
		this.applicantId = applicantId;
	}

	public String getCertificationName() {
		return certificationName;
	}

	public void setCertificationName(String certificationName) {
		this.certificationName = certificationName;
	}

	public String getIssuingInstitution() {
		return issuingInstitution;
	}

	public void setIssuingInstitution(String issuingInstitution) {
		this.issuingInstitution = issuingInstitution;
	}

	public String getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(String certificationId) {
		this.certificationId = certificationId;
	}

	public String getAttachment1() {
		return attachment1;
	}

	public void setAttachment1(String attachment1) {
		this.attachment1 = attachment1;
	}

	public String getAttachment2() {
		return attachment2;
	}

	public void setAttachment2(String attachment2) {
		this.attachment2 = attachment2;
	}
	
    
    

}
