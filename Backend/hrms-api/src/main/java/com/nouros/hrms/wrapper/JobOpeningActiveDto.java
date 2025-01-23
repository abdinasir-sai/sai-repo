package com.nouros.hrms.wrapper;

import java.util.Date;

import lombok.Data;

@Data
public class JobOpeningActiveDto {

	private String title;
	private String status;
	private Date createdTime;
	private String creatorFullName;
	private Integer jobApplicationCount;
	private Integer jobApplicationStatus;
	private Integer jobApplicationCountForToday;
	private String departmentName;
	private Boolean isCritical;
	private String jobId;
	private String nLevel;
}
