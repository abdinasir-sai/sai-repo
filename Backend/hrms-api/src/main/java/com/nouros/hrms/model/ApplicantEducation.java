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
@Table(name = "APPLICANT_EDUCATION")
public class ApplicantEducation extends BaseEntitySaaS{

    @Basic
    @Column(name = "ADDITIONAL_NOTES", columnDefinition = "LONGTEXT")
    private String additionalNotes;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APPLICANT_ID", columnDefinition = "INT")
    private Applicant applicant;
   
    @Basic
    @Column(name = "DATE_OF_COMPLETION", length = 19)
    private Date dateOfCompletion;

    @Size(max = 100)
    @Basic
    @Column(name = "DEGREE_DIPLOMA", length = 100)
    private String degreeDiploma;

    @Size(max = 100)
    @Basic
    @Column(name = "FIELD_OF_STUDY", length = 100)
    private String fieldOfStudy;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

    @Size(max = 100)
    @Basic
    @Column(name = "SCHOOL_NAME", length = 100)
    private String schoolName;

    @Basic
    @Column(name = "WORKSPACE_ID", columnDefinition = "INT")
    private Integer workspaceId;
    
    @Size(max = 100)
    @Basic
    @Column(name = "COUNTRY_OF_STUDY", length = 100)
    private String countryofStudy;
    
    @Basic
    @Column(name = "DEGREE_START_DATE", length = 19)
    private Date degreeStartDate;
    
    @Basic
    @Column(name = "DEGREE_END_DATE", length = 19)
    private Date degreeEndDate;
    
    @Basic
    @Column(name = "APPLICANT_ENROLLED_STATUS")
    private boolean applicantEnrolledStatus;
    
    @Size(max = 200)
	@Basic
	@Column(name = "ATTACHMENT1", length = 200)
	private String attachment1;

	@Size(max = 200)
	@Basic
	@Column(name = "ATTACHMENT2", length = 200)
	private String attachment2;
	
	@Basic
    @Column(name = "GRADE", columnDefinition = "INT")
    private Integer grade;
	
	@Size(max = 255)
	@Basic
	@Column(name = "STUDENT_ID", length = 255)
	private String studentId;
	
	@Size(max = 255)
	@Basic
	@Column(name = "CITY", length = 255)
	private String city;

    public String getCountryofStudy() {
		return countryofStudy;
	}

	public void setCountryofStudy(String countryofStudy) {
		this.countryofStudy = countryofStudy;
	}

	public Date getDegreeStartDate() {
		return degreeStartDate;
	}

	public void setDegreeStartDate(Date degreeStartDate) {
		this.degreeStartDate = degreeStartDate;
	}

	public Date getDegreeEndDate() {
		return degreeEndDate;
	}

	public void setDegreeEndDate(Date degreeEndDate) {
		this.degreeEndDate = degreeEndDate;
	}


	public boolean isApplicantEnrolledStatus() {
		return applicantEnrolledStatus;
	}

	public void setApplicantEnrolledStatus(boolean applicantEnrolledStatus) {
		this.applicantEnrolledStatus = applicantEnrolledStatus;
	}

	public ApplicantEducation() {
    }

    public ApplicantEducation(Integer id) {
        this.id = id;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

 
    public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	public Date getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(Date dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public String getDegreeDiploma() {
        return degreeDiploma;
    }

    public void setDegreeDiploma(String degreeDiploma) {
        this.degreeDiploma = degreeDiploma;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
	
    
}
