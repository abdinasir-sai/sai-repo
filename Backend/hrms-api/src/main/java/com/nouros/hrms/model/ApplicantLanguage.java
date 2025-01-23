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
@Table(name = "APPLICANT_LANGUAGE")
public class ApplicantLanguage extends BaseEntitySaaS{
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APPLICANT_ID", columnDefinition = "INT")
	private Applicant applicantId;
    
    @Size(max = 100)
    @Basic
    @Column(name = "LANGUAGE_NAME", length = 100)
    private String languageName;
    
    @Basic
    @Column(name = "LANGUAGE_LEVEL", columnDefinition = "ENUM('Beginner','Intermediate','Professional','Native')")
    private String languageLevel;

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

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getLanguageLevel() {
		return languageLevel;
	}

	public void setLanguageLevel(String languageLevel) {
		this.languageLevel = languageLevel;
	}
    
    
    

}
