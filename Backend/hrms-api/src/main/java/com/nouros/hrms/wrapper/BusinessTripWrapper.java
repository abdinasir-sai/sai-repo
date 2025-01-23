package com.nouros.hrms.wrapper;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BusinessTripWrapper {
	
	
	String travelStartArrivalCity;
	 String travelEndArrivalCity;
	String travelFromCountry;
	String travelToCountry;
	Date businessStartDate;
	Date businessEndDate;
	Date travelEndDate;
	Date travelStartDate;

}
