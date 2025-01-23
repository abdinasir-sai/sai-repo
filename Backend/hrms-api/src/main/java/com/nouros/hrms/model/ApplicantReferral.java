package com.nouros.hrms.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "APPLICANT_REFERRAL")
@Data
public class ApplicantReferral extends BaseEntitySaaS {

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@Size(max = 100)
	@Basic
	@Column(name = "FIRST_NAME", length = 100)
	private String firstName;

	@Size(max = 100)
	@Basic
	@Column(name = "LAST_NAME", length = 100)
	private String lastName;

	@Basic
	@Column(name = "JOB_ID", length = 100)
	private String jobId;

	@Basic
	@Column(name = "STATUS", columnDefinition = "ENUM('New','Applied','Applicant Register')")
	private String status;
	
	@Size(max = 100)
	@Basic
	@Column(name = "EMAIL_ID", length = 100)
	private String emailId;
	
	@Basic
	@Column(name = "APPLICANT_TYPE", columnDefinition = "ENUM('Regular','Referred','Head-Hunted')")
	private String applicantType;

}
