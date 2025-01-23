package com.nouros.hrms.wrapper;

import java.util.List;

import lombok.Data;

@Data
public class JobOpeningsDto {
	
	List<JobOpeningActiveDto> jobOpenings;
	Integer JobOpeningCount;

}
