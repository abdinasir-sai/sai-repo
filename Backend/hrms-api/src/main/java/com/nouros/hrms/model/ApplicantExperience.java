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
@Table(name = "APPLICANT_EXPERIENCE")

public class ApplicantExperience extends BaseEntitySaaS{

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APPLICANT_ID", columnDefinition = "INT")
    private Applicant applicant;

    @Size(max = 100)
    @Basic
    @Column(length = 100)
    private String company;

    @Basic
    @Column(name = "CURRENTLY_WORK_HERE")
    private Boolean currentlyWorkHere;

    @Size(max = 50)
    @Basic
    @Column(length = 50)
    private String duration;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

    @Size(max = 200)
    @Basic
    @Column(length = 200)
    private String occupation;

    @Basic
    @Column(columnDefinition = "INT")
    private Integer rowid;

    @Basic
    @Column(columnDefinition = "LONGTEXT")
    private String summary;

    @Basic
    @Column(name = "WORKSPACE_ID", columnDefinition = "INT")
    private Integer workspaceId;
    
    @Basic
    @Column(name = "WORK_START_DATE", length = 19)
    private Date workStartDate;
    
    @Basic
    @Column(name = "WORK_END_DATE", length = 19)
    private Date workEndDate;
    
    @Size(max = 200)
   	@Basic
   	@Column(name = "ATTACHMENT1", length = 200)
   	private String attachment1;

   	@Size(max = 200)
   	@Basic
   	@Column(name = "ATTACHMENT2", length = 200)
   	private String attachment2;
   	
    @Basic
    @Column(name = "REASON_FOR_LEAVE",columnDefinition = "LONGTEXT")
    private String reasonForLeave;
   	
    @Basic
    @Column(name = "EMPLOYMENT_STATUS", columnDefinition = "ENUM('full-time','part-time','self-employed','contract')")
    private String employmentStatus;
    
    @Size(max = 255)
   	@Basic
   	@Column(name = "EMP_ID_PREVIOUS_COMPANY", length = 255)
   	private String empIdPreviousCompany;
    
    @Basic
    @Column(name = "ADDRESS",columnDefinition = "LONGTEXT")
    private String address;
    
    

    public Date getWorkStartDate() {
		return workStartDate;
	}

	public void setWorkStartDate(Date workStartDate) {
		this.workStartDate = workStartDate;
	}

	public Date getWorkEndDate() {
		return workEndDate;
	}

	public void setWorkEndDate(Date workEndDate) {
		this.workEndDate = workEndDate;
	}

	public Boolean getCurrentlyWorkHere() {
		return currentlyWorkHere;
	}

	public ApplicantExperience() {
    }

    public ApplicantExperience(Integer id) {
        this.id = id;
    }

  
    public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Boolean isCurrentlyWorkHere() {
        return currentlyWorkHere;
    }

    public void setCurrentlyWorkHere(Boolean currentlyWorkHere) {
        this.currentlyWorkHere = currentlyWorkHere;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Integer getRowid() {
        return rowid;
    }

    public void setRowid(Integer rowid) {
        this.rowid = rowid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
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

	public String getReasonForLeave() {
		return reasonForLeave;
	}

	public void setReasonForLeave(String reasonForLeave) {
		this.reasonForLeave = reasonForLeave;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employementStatus) {
		this.employmentStatus = employementStatus;
	}

	public String getEmpIdPreviousCompany() {
		return empIdPreviousCompany;
	}

	public void setEmpIdPreviousCompany(String empIdPreviousCompany) {
		this.empIdPreviousCompany = empIdPreviousCompany;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
    
    
}
