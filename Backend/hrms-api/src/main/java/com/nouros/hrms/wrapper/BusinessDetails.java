package com.nouros.hrms.wrapper;

import java.util.Date;
import lombok.Data;

@Data
public class BusinessDetails {

	String businessLabel;
	Date startDate;
	Date endDate;
	Integer days;
}
