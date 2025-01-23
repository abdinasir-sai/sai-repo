package com.nouros.hrms.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.BusinessTripItinerary;
import com.nouros.hrms.repository.BusinessTripItineraryRepository;
import com.nouros.hrms.service.BusinessTripItineraryService;
import com.nouros.hrms.service.generic.AbstractService;

@Service
public class BusinessTripItineraryServiceImpl extends AbstractService<Integer,BusinessTripItinerary> implements BusinessTripItineraryService {
	
	private static final Logger log = LogManager.getLogger(BusinessTripItineraryServiceImpl.class);
	
	@Autowired 
	BusinessTripItineraryRepository businessTripItineraryRepository;


	protected BusinessTripItineraryServiceImpl(
	        com.nouros.hrms.repository.generic.GenericRepository<BusinessTripItinerary> repository
	    ) {
	        super(repository, BusinessTripItinerary.class);
	    }


	@Override
	public void bulkDelete(List<Integer> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				businessTripItineraryRepository.deleteById(list.get(i));
			}
		}
		
	}
 

}
