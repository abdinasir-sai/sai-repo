package com.nouros.hrms.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "JOB_INTERVIEW_QUESTION")
@Data
public class JobInterviewQuestion {

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(name = "ID",columnDefinition = "INT")
    private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOB_OPENING_FK", columnDefinition = "INT")
    private JobOpening jobOpening;
    
    @Column(name = "QUESTION")
    private String question;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
	
}
