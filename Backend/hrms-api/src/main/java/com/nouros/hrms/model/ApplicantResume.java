package com.nouros.hrms.model;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "APPLICANT_RESUME")
public class ApplicantResume extends BaseEntitySaaS{
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@Size(max = 255)
    @Basic
    @Column(name = "TITLE")
    private String title;
	
	@Size(max = 255)
    @Basic
    @Column(name = "COVER_LETTER")
    private String coverLetter;
	
	@Size(max = 255)
    @Basic
    @Column(name = "RESUME_ATTACHMENT")
    private String resumeAttachment;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TEXT1")
    private String text1;
	
	@Size(max = 255)
    @Basic
    @Column(name = "TEXT2")
    private String text2;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="APPLICANT_ID", columnDefinition="INT")
	private Applicant applicantId;
	
}
