package com.nouros.hrms.wrapper;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OneWayBusinessTripWrapper {
	String businessPurpose;
	AirportWrapper fromCity;
	AirportWrapper toCity;
	Date beginTravelDate;
	Date endTravelDate;
	Boolean isAssistanceNeeded;
	String additionalInstructions;
	TripAssistanceDetails assistanceDetails;
	Integer gapDays;
	Integer totalBusinessDays;
	Double payValue;
	List<BusinessDetails> business;
	String attachment;
	String travelScope;
}
