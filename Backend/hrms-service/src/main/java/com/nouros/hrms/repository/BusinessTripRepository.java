package com.nouros.hrms.repository;
 
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

import com.nouros.hrms.model.BusinessTrip;
import com.nouros.hrms.model.BusinessTripItinerary;
import com.nouros.hrms.repository.generic.GenericRepository;
 
 
@Repository
@Transactional(readOnly = true)
public interface BusinessTripRepository extends GenericRepository<BusinessTrip>{
	
	
	@Query(value="SELECT * FROM BUSINESS_TRIP_ITINERARY WHERE ID IN (select BUSINESS_TRIP_ITINERARY_ID FROM ITINERARY_BUSINESS_TRIP_MAPPING WHERE BUSINESS_TRIP_ID = :id AND CUSTOMER_ID = :customerId) AND CUSTOMER_ID = :customerId ;", nativeQuery=true)
	List<BusinessTripItinerary> getByBusinessTripId(@Param("id")int id, @Param("customerId") Integer customerId
			 );
	
	@Query(value="delete from ITINERARY_BUSINESS_TRIP_MAPPING where BUSINESS_TRIP_ITINERARY_ID = :id AND CUSTOMER_ID = :customerId ",nativeQuery=true)
	void deleteAllItineraryMapping(@Param("id") Integer id, @Param("customerId") Integer customerId
			 );
	
	@Query(value="select * from BUSINESS_TRIP where EMPLOYEE_ID = :employeeId AND CUSTOMER_ID = :customerId",nativeQuery=true)
	List<BusinessTrip> getBusinessTripsForEmployee(@Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId
			 );
	
	@Query(value = "SELECT * FROM BUSINESS_TRIP WHERE TRAVEL_START_DATE>=:beforeDate AND TRAVEL_END_DATE <=:curDate AND WORKFLOW_STAGE =:workflowStage AND CUSTOMER_ID = :customerId",nativeQuery = true)
    List<BusinessTrip> getBusinessTripByDates(@Param("beforeDate")LocalDate beforeDate,@Param("curDate")LocalDate curDate,@Param("workflowStage")String  workflowStage, @Param("customerId") Integer customerId
    		 );

	@Query(value="select * from BUSINESS_TRIP where ID=(SELECT MAX(ID) from BUSINESS_TRIP where EMPLOYEE_ID = :employeeId AND CUSTOMER_ID = :customerId)",nativeQuery=true)
	BusinessTrip getEmployeeLastBusinessTrip(@Param("employeeId") Integer employeeId, @Param("customerId") Integer customerId
			 );
	
	@Query(value ="SELECT * FROM BUSINESS_TRIP WHERE OTHER_EXPENSE_BANK_REQUEST_ID=:otherExpenseBankRequestId AND CUSTOMER_ID = :customerId",nativeQuery=true)
	List<BusinessTrip> getBusinessTripsByBankRequestId(@Param("otherExpenseBankRequestId")Integer otherExpenseBankRequestId, @Param("customerId") Integer customerId
			 );


	@Query(value ="SELECT e.TEXT1 , SUM(bt.PAY_VALUE) FROM BUSINESS_TRIP bt INNER JOIN EMPLOYEE e ON bt.EMPLOYEE_ID = e.ID WHERE  bt.ID IN (:businessTripList) AND bt.TRAVEL_SCOPE =:type  AND e.TEXT1 IS NOT NULL AND bt.CUSTOMER_ID = :customerId AND e.CUSTOMER_ID = :customerId GROUP BY e.TEXT1 ",nativeQuery =true)
	List<Object []> getBusinessTripForExpense(@Param("businessTripList")List<Integer> businessTripList,@Param("type")String type, @Param("customerId") Integer customerId
			 );
	
	@Query(value ="SELECT  SUM(bt.PAY_VALUE) FROM BUSINESS_TRIP bt INNER JOIN EMPLOYEE e ON bt.EMPLOYEE_ID = e.ID WHERE  bt.ID IN (:businessTripList) AND bt.TRAVEL_SCOPE =:type AND e.TEXT1 IS NOT NULL AND  bt.CUSTOMER_ID = :customerId AND e.CUSTOMER_ID = :customerId ",nativeQuery =true)
	Object  getBusinessTripForAccural(@Param("businessTripList")List<Integer> businessTripList,@Param("type")String type, @Param("customerId") Integer customerId);

	
}
