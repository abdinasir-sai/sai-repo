package com.nouros.hrms.service;
 
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.nouros.hrms.model.BusinessTrip;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.BusinessTripDto;
import com.nouros.hrms.wrapper.MultiCityBusinessTripWrapper;
import com.nouros.hrms.wrapper.OneWayBusinessTripWrapper;


 
public interface BusinessTripService extends GenericService<Integer, BusinessTrip>{


		/**
 
	This method is used to soft delete a list of BusinessTrip identified by their ids.
	@param list The list of ids of the BusinessTrip to be soft deleted.
	*/
		void bulkDelete(List<Integer> list);
		
		List<Map<String, String>> getAirportDetails(String valueField);
		
		BusinessTrip create(BusinessTrip businessTrip);
		BusinessTrip update(BusinessTrip businessTrip);
		BusinessTrip updateBusinessTripWorkflowStage(BusinessTripDto businessTripDto);
		
		OneWayBusinessTripWrapper getCostingForSingleTrip(OneWayBusinessTripWrapper businessTripWrapper );
		MultiCityBusinessTripWrapper getCostingForMultiTrip(MultiCityBusinessTripWrapper businessTripWrapper );
		
		String deleteBusinessTripForEmployee(Integer id, Integer employeeId);
		
		public ResponseEntity<byte[]> createWpsTxtFileForAllTrips();
		public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum);
		
		public void updateWorkflowStageForEmployee();
		public  List<BusinessTrip> getBusinessTripByOtherExpenseBankRequestId(OtherExpenseBankRequest otherExpenseBankRequest); 
		public List<Object[]> getBusinessTripForExpense(List<Integer> businessTripIds,String type);
		public Object  getBusinessTripForAccural(List<Integer> businessTripIds,String type);

}