package com.nouros.hrms.wrapper;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.nouros.hrms.model.Projects;

import lombok.Data;

@Data
@JsonFilter("propertyFilter")
public class TimeLogsWrapper {

	private Projects project;
	private String sunday;
	private String monday;
	private String tuesday;
	private String wednesday;
	private String thursday;
	private String friday;
	private String saturday;
	private String task;
	
}