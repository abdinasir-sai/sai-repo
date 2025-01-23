package com.nouros.hrms.wrapper;

import java.util.Date;

import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.model.JobOpening;

import lombok.Data;

@Data
public class OfferDto {
	
	private int applicantId;
	private int jobOpeningId;
	private String jobTitle;
	private Department department;
	private JobApplication jobApplication;
	private Date dateOpened;
	private Applicant applicant;
	private String salary;
	private String conditionEmployment;
	private Date deadlineDate;
	private JobOpening jobOpening;
	private Double compensationStructuresInsideAmount;
}
