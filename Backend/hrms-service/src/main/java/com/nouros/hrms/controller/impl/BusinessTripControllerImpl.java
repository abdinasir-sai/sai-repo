package com.nouros.hrms.controller.impl;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.controller.BusinessTripController;
import com.nouros.hrms.model.BusinessTrip;
import com.nouros.hrms.service.BusinessTripService;
import com.nouros.hrms.wrapper.BusinessTripDto;
import com.nouros.hrms.wrapper.BusinessTripWrapper;
import com.nouros.hrms.wrapper.CostingDayWrapper;
import com.nouros.hrms.wrapper.MultiCityBusinessTripWrapper;
import com.nouros.hrms.wrapper.OneWayBusinessTripWrapper;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
@Primary
@RestController
@RequestMapping("/BusinessTrip")

public class BusinessTripControllerImpl implements BusinessTripController {

	  private static final Logger log = LogManager.getLogger(BusinessTripControllerImpl.class);

	  @Autowired
	  private BusinessTripService businessTripService;

 
		
	  @Override
	  @TriggerBPMN(entityName = "BusinessTrip", appName = "HRMS_APP_NAME")
	  public BusinessTrip create(BusinessTrip businessTrip) {
		  log.info("inside @class BusinessTripControllerImpl @method create");
	       return businessTripService.create(businessTrip);
	   
	  }
 
	  
	  @Override
	  public Long count(String filter) {
	    return businessTripService.count(filter);
	  }
 
	  @Override
	  public List<BusinessTrip> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return businessTripService.search(filter, offset, size, orderBy, orderType);
	  }
 
	  @Override
	  public BusinessTrip update(BusinessTrip businessTrip) {
	    return businessTripService.update(businessTrip);
	  }
 
	  @Override
	  public BusinessTrip findById(Integer id) {
	    return businessTripService.findById(id);
	  }
 
	  @Override
	  public List<BusinessTrip> findAllById(List<Integer> id) {
	    return businessTripService.findAllById(id);
	  }
 
	  @Override
	  public void deleteById(Integer id) {
		  businessTripService.deleteById(id);
	  }
	  @Override
	  public void bulkDelete(List<Integer> list) {
		  businessTripService.bulkDelete(list);
	  }
 
	
	    
	    public List<Map<String, String>> getAirportDetails(String valueField) {
	        return businessTripService.getAirportDetails(valueField);
	    }
	    


		@Override
		public OneWayBusinessTripWrapper getCostingForSingleTrip(@Valid OneWayBusinessTripWrapper businessTripWrapper) {
			
			return businessTripService.getCostingForSingleTrip(businessTripWrapper);
		}



		@Override
		public MultiCityBusinessTripWrapper getCostingForMultiTrip(MultiCityBusinessTripWrapper businessTripWrapper) {
			
			return businessTripService.getCostingForMultiTrip(businessTripWrapper);
		}


		@Override
		public String deleteBusinessTripForEmployee(@Valid Integer id, Integer employeeId) {
			
			return businessTripService.deleteBusinessTripForEmployee(id, employeeId);
		}


		@Override
		public BusinessTrip updateBusinessTripWorkflowStage(BusinessTripDto businessTripDto) {
			
			return businessTripService.updateBusinessTripWorkflowStage(businessTripDto);
		}
		
		@Override
		 public ResponseEntity<byte[]> createCommonFileForTrips()
		   {
			   return businessTripService.createWpsTxtFileForAllTrips();
		   }
 
		 @Override
		  public ResponseEntity<byte[]> getAllTripsRecordsWps(Integer weekNum)
		   {
			   return businessTripService.downloadCommonWpsFile(weekNum);
		   }


		@Override
		public void updateWorkflowStageForEmployee() {
			
			businessTripService.updateWorkflowStageForEmployee();
		}
}