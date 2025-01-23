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
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "INTERVIEW_FEEDBACK")
@Data
@EqualsAndHashCode(callSuper=false)
public class InterviewFeedback extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(name = "ID", columnDefinition = "INT")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INTERVIEW", columnDefinition = "INT")
    private Interview interview;
	
	@Basic
	@Column(name = "QUESTION", columnDefinition = "LONGTEXT")
	private String question;

	@Basic
	@Column(name = "COMMENTS", length = 350)
	private String comments;
	
	@Basic
	@Column(name = "RATING", length = 10)
	private String rating;

	@Basic
	@Column(name = "TEXT1", length = 200)
	private String text1;
	
	@Basic
	@Column(name = "OVERALL_AVERAGE_SCORE")
	private Double overallAverageScore;
}
