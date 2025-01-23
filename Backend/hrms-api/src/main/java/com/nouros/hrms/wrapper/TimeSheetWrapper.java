package com.nouros.hrms.wrapper;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter("propertyFilter")
public class TimeSheetWrapper {
	Map<String, List<TimeLogsWrapper>> weeklyTimeLogs;
	Integer year;
	Integer month;
	Integer weekNumber;
	String timeSheetStatus;
	String timeSheetDescription;
	Integer empId;
}