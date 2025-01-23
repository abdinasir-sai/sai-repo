package com.nouros.hrms.wrapper;

import java.util.List;

import lombok.Data;

@Data
public class JobOpeningSchedulerDto {
	List<JobOpeningsWrapper> jobOpenings;
}
