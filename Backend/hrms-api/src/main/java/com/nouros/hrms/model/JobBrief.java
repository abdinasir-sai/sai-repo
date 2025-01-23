package com.nouros.hrms.model;

import org.hibernate.annotations.Comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "JOB_BRIEF")
@Data
@EqualsAndHashCode(callSuper=false)
public class JobBrief extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(name = "ID", columnDefinition = "INT")
	@Comment("Primary key for the JobBrief table")
	private Integer id;

	@Lob
	@Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
	@Comment("Detailed description of the job")
	private String description;

	@Column(name = "POSTING_TITLE", length = 100)
	@Comment("Posting Title is used to store the job title with deparment name ")
	private String postingTitle;

}
