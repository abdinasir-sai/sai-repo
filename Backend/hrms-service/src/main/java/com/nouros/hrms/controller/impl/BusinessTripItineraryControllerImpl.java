package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.BusinessTripItineraryController;
import com.nouros.hrms.model.BusinessTripItinerary;
import com.nouros.hrms.service.BusinessTripItineraryService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/BusinessTripItinerary")
public class BusinessTripItineraryControllerImpl implements BusinessTripItineraryController {
	
	private static final Logger log = LogManager.getLogger(BusinessTripItineraryControllerImpl.class);

	  @Autowired
	  private BusinessTripItineraryService businessTripItineraryService;

	@Override
	public BusinessTripItinerary create(@Valid BusinessTripItinerary businessTripItinerary) {
		 log.info("inside @class BusinessTripItineraryControllerImpl @method create");
		return businessTripItineraryService.create(businessTripItinerary);
	}

	@Override
	public Long count(String filter) {
		
		return businessTripItineraryService.count(filter);
	}

	@Override
	public List<BusinessTripItinerary> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		
		return businessTripItineraryService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public BusinessTripItinerary update(@Valid BusinessTripItinerary businessTripItinerary) {
		
		return businessTripItineraryService.update(businessTripItinerary);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		
		businessTripItineraryService.deleteById(id);
	}

	@Override
	public void bulkDelete(@Valid List<Integer> list) {
		
		businessTripItineraryService.bulkDelete(list);		
	}

	@Override
	public BusinessTripItinerary findById(@Valid Integer id) {
		
		return businessTripItineraryService.findById(id);
	}

	@Override
	public List<BusinessTripItinerary> findAllById(@Valid List<Integer> id) {
		
		return businessTripItineraryService.findAllById(id);
	}

}
