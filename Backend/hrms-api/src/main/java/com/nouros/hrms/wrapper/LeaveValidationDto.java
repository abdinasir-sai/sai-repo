package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class LeaveValidationDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer empId;
	private String leaveTypeId;
	private Date fromDate;
	private Date toDate;
	private Integer duration;

}
