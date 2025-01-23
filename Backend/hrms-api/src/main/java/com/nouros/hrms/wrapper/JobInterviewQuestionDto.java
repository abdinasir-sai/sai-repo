package com.nouros.hrms.wrapper;

import java.io.Serializable;

import lombok.Data;

@Data
public class JobInterviewQuestionDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String information;
	private String department;
	private String domain;
	private String skill;
	private String jobDescription;
	private String note;
	private String exmapleJson;
	
}
