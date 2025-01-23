package com.nouros.hrms.wrapper;

import lombok.Data;

@Data
public class UnpaidLeaveWrapper {

	private Long countOfLeave;
	private Integer employeeId;
	
	private Long countOfWorkingDays;
	
	
}
