package com.nouros.hrms.wrapper;

import java.util.List;

import lombok.Data;

@Data
public class MultiCityBusinessTripWrapper {

	List<OneWayBusinessTripWrapper> multicityData;
	String businessPurpose;
	Boolean isAssistanceNeeded;
	String additionalInstructions;
	String attachment;
	Double payValue;
	Integer gapDays;
	Integer totalBusinessDays;
	String travelScope;
}
