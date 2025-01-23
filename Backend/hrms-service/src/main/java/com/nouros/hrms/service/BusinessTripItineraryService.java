package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.BusinessTripItinerary;
import com.nouros.hrms.service.generic.GenericService;

public interface BusinessTripItineraryService extends GenericService<Integer, BusinessTripItinerary> {

	void bulkDelete(List<Integer> list);
}
