package com.nouros.payrollmanagement.wrapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.nouros.payrollmanagement.model.OvertimeLogs;

import lombok.Data;


@Data
@JsonFilter("propertyFilter")
public class OvertimeDto {
	
	private List<OvertimeLogs> overtimeLogs;
	private String overtimeStatus;
	private Integer year;
	private Integer month;

}
