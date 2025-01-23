package com.nouros.hrms.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.execution.controller.IWorkorderController;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.product.storagesystem.rest.StorageRest;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.model.BusinessExpense;
import com.nouros.hrms.model.BusinessTrip;
import com.nouros.hrms.model.BusinessTripItinerary;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.repository.BusinessTripItineraryRepository;
import com.nouros.hrms.repository.BusinessTripRepository;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.BusinessTripService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.OtherExpenseBankRequestService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.AirportWrapper;
import com.nouros.hrms.wrapper.BusinessDetails;
import com.nouros.hrms.wrapper.BusinessTripDto;
import com.nouros.hrms.wrapper.BusinessTripWrapper;
import com.nouros.hrms.wrapper.CostingDayWrapper;
import com.nouros.hrms.wrapper.MultiCityBusinessTripWrapper;
import com.nouros.hrms.wrapper.OneWayBusinessTripWrapper;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class BusinessTripServiceImpl extends AbstractService<Integer,BusinessTrip> implements BusinessTripService {

	private static final Logger log = LogManager.getLogger(BusinessTripServiceImpl.class);


	protected BusinessTripServiceImpl(
	        com.nouros.hrms.repository.generic.GenericRepository<BusinessTrip> repository
	    ) {
	        super(repository, BusinessTrip.class);
	    }

	private static final String MEALS_TOTAL = "mealsTotal";

	private static final String COST_FOR_MONTH = "costForMonth";

	private static final String MAXIMUM_DIEM_COST = "maximumDiemCost";

	private static final String INSIDE_METHOD_GET_COSTING_AND_DAYS_BY_COUNTRY_AND_CITY = "Inside Method getCostingAndDaysByCountryAndCity";

	private static final String COUNTRY = "country";
	
	private static final String COUNTY = "county";
	
	private static final String ATTACHMENT = "attachment";
	
	private static final String INSIDE_CLASS_LOG = "Inside @Class BusinessTripServiceImpl @Method";


	@Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
	private String rootDirBucketName;
	
	@Autowired
	private StorageRest storageRest;
	
 
	@Autowired
	private BusinessTripRepository businessTripRepository;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private HrmsSystemConfigService hrmsSystemConfigService;
	
	@Autowired
	UserRest userRest;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	CustomerInfo customerInfo;
	
	@Autowired
	 private CommonUtils commonUtils;
	
	@Autowired
	private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}
	
	@Autowired
	IWorkorderController workorderController;
	
	@Autowired
	WorkflowActionsController workflowActionsController;
  

	@Override
	public void bulkDelete(List<Integer> list) { 
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				businessTripRepository.deleteById(list.get(i));
			}
		}
	}


	@Override
	public BusinessTrip create(BusinessTrip businessTrip) {
//		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
//		Integer workspaceId = customerInfo.getActiveUserSpaceId();
//		businessTrip.setWorkspaceId(workspaceId); 
		if (businessTrip != null && businessTrip.getEmployee() == null) {
			log.debug("Inside if Condition when Employee is null");
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);

			User contextUser = getUserContext();
			log.debug("contextUser is : {}", contextUser);
			if (contextUser != null && contextUser.getUserid() != null) {
				try {
					Employee employee = employeeService.getEmployeeByUserId(contextUser.getUserid());
					log.debug("Employee Found is : {}", employee);
					businessTrip.setEmployee(employee);
					log.debug("Inside create businessTrip:{}", businessTrip);
					log.debug("Inside create businessTrip cretedTime:{} , modifiedTime:{}",
							businessTrip.getCreatedTime(), businessTrip.getModifiedTime());
				} catch (BusinessException be) {
					throw new BusinessException(be.getMessage());
				}
			}

		}
		return businessTripRepository.save(businessTrip);
	}
	
	public List<Map<String, String>> getAirportDetails(String valueField) {
		log.info("Inside Method getAirportDetails");
		try {
			String sql = "SELECT a.AIRPORT_NAME, a.ITA_CODE, a.COUNTRY, a.CITY FROM AIRPORT_DETAILS a WHERE a.COUNTRY LIKE CONCAT('%', :valueField, '%') OR a.CITY LIKE CONCAT('%', :valueField, '%') Or a.AIRPORT_NAME LIKE CONCAT('%', :valueField, '%') OR a.ITA_CODE LIKE CONCAT('%', :valueField, '%')";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("valueField", valueField);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = query.getResultList();
			return resultList.stream().map(result -> {
				Map<String, String> map = new HashMap<>();
				map.put("airportName", (String) result[0]);
				map.put("itaCode", (String) result[1]);
				map.put(COUNTRY, (String) result[2]);
				map.put("city", (String) result[3]);
				return map;
			}).toList();
		} catch (Exception e) {
			log.error("Error Caught while Fetching  AirPort Details : {} ", Utils.getStackTrace(e));
			return List.of();
		}
	}


	private String iterateForNonUsaBasedMealsAndLodgeCost(String maximumDiemCost,
			List<Map<String, String>> nonUsaBasedMealsAndLodgeCost) {
		for (Map<String, String> map : nonUsaBasedMealsAndLodgeCost) {
		    if (map != null && map.containsKey(MAXIMUM_DIEM_COST)) {
		        maximumDiemCost = map.get(MAXIMUM_DIEM_COST);
		        if (maximumDiemCost == null || maximumDiemCost.isEmpty()) {
		            log.error("maximumDiemCost is null or empty");
		        }
		    }
		}
		return maximumDiemCost;
	}

	private String iterateForUsaBasedLodgingCost(String lodgingTotalCost,
			List<Map<String, String>> usaBasedLodgingCost) {
		for (Map<String, String> map : usaBasedLodgingCost) {
		    if (map != null && map.containsKey(COST_FOR_MONTH)) {
		        lodgingTotalCost = map.get(COST_FOR_MONTH);
		        if (lodgingTotalCost == null || lodgingTotalCost.isEmpty()) {
		            log.error("lodgingTotalCost is null or empty");
		        }
		    }
		}
		return lodgingTotalCost;
	}

	private String iterateForUsaBasedMealsCostInsideCheckAndSetCostings(String mealsTotalCost,
			List<Map<String, String>> usaBasedMealsCost) {
		for (Map<String, String> map : usaBasedMealsCost) {
		    if (map != null && map.containsKey(MEALS_TOTAL)) {
		        mealsTotalCost = map.get(MEALS_TOTAL);
		        if (mealsTotalCost == null || mealsTotalCost.isEmpty()) {
		            log.error("mealsTotalCost is null or empty");
		        }
		    }
		}
		return mealsTotalCost;
	}
	
	private String iterateForNonUsaBasedMealsCostInsideCheckAndSetCostings(String mealsTotalCost,
			List<Map<String, String>> usaBasedMealsCost) {
		for (Map<String, String> map : usaBasedMealsCost) {
		    if (map != null && map.containsKey(MEALS_TOTAL)) {
		        mealsTotalCost = map.get(MEALS_TOTAL);
		        if (mealsTotalCost == null || mealsTotalCost.isEmpty()) {
		            log.error("mealsTotalCost is null or empty");
		        }
		    }
		}
		return mealsTotalCost;
	}

	private List<Map<String, String>> findLodgingCostByUSACountryAndCity(String travelEndArrivalCity, Date businessEndDate) {
		log.info("Inside Method findLodgingCostByUSACountryAndCity");
		try {
			String monthName = getMonthName(businessEndDate);
			log.debug("Month Name is : {} ", monthName);
			Integer year = Integer.parseInt(getYear(businessEndDate));
			log.debug("year is : {} ", year);

			String sql = "SELECT l." + monthName.toUpperCase()
					+ ",l.YEAR , l.CITY , l.COUNTY FROM USA_BASED_LODGING_COSTS l WHERE l.CITY = :city AND l.YEAR = :year ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("city", travelEndArrivalCity);
			query.setParameter("year", year);
			List<Object[]> resultList = query.getResultList();
       	return resultList.stream().map(result -> {
	            Map<String, String> map = new HashMap<>();
	            map.put(COST_FOR_MONTH, (String) result[0]);
	            map.put("year", (String) result[1]);
	            map.put("city", (String) result[2]);
	            map.put(COUNTY, (String) result[3]);
	            return map;
	        }).toList(); 
		} catch (Exception e) {
			log.error("Error Caught while Fetching  Lodging Cost Details for  USA Based Cities : {} ", e.getMessage());
			return List.of();
		}
	}

	public static String getMonthName(Date date) {
		SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
		return monthFormat.format(date);
	}

	public static String getYear(Date date) {
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		return yearFormat.format(date);
	}

	private List<Map<String, String>> findMealsAndLodgeCostByNonUSACountryAndCity(String travelToCountry,String travelEndArrivalCity) {
		log.info(INSIDE_METHOD_GET_COSTING_AND_DAYS_BY_COUNTRY_AND_CITY);
		try {
			String sql = "SELECT n.MAXIMUM_LODGING, n.MIE_COST , n.MAXIMUM__PER_DIEM_RATE ,n.CITY ,n.COUNTRY  FROM NON_USA_PERDIEM n WHERE n.COUNTRY = :country AND n.CITY = :city";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(COUNTRY, travelToCountry);
			query.setParameter("city", travelEndArrivalCity);
			List<Object[]> resultList = query.getResultList();
			return resultList.stream().map(result -> {
				Map<String, String> map = new HashMap<>();
				map.put("maximumLodging", (String) result[0]);
				map.put("mealsCost", (String) result[1]);
				map.put(MAXIMUM_DIEM_COST, (String) result[2]);
				map.put("city", (String) result[3]);
				map.put(COUNTRY, (String) result[4]);
				return map;
			}).toList();
		} catch (Exception e) {
			log.error("Error Caught while Fetching  Meals Details for NON - USA Based Cities : {} ", e.getMessage());
			return List.of();
		}
	}

	private List<Map<String, String>> findMealsCostByUSACountryAndCity(String travelEndArrivalCity) {
		log.info(INSIDE_METHOD_GET_COSTING_AND_DAYS_BY_COUNTRY_AND_CITY);
		try {
			String sql = "SELECT m.MIE__TOTAL , m.COUNTY ,m.CITY  FROM USA_BASED_MEALS_COSTS m WHERE m.CITY = :city";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("city", travelEndArrivalCity);
			List<Object[]> resultList = query.getResultList();
			return resultList.stream().map(result -> {
				Map<String, String> map = new HashMap<>();
				map.put(MEALS_TOTAL, (String) result[0]);
				map.put(COUNTY, (String) result[1]);
				map.put("city", (String) result[2]);
				return map;
			}).toList();
		} catch (Exception e) {
			log.error("Error Caught while Fetching  Meals Details for USA Based Cities : {} ", e.getMessage());
			return List.of();
		}
	}
	
	private List<Map<String, String>> findMealsCostByNonUSACountryAndCity(String travelEndArrivalCity) {
		log.info(INSIDE_METHOD_GET_COSTING_AND_DAYS_BY_COUNTRY_AND_CITY);
		try {
			String sql = "SELECT m.MIE_COST , m.COUNTRY ,m.CITY  FROM NON_USA_PERDIEM m WHERE m.CITY = :city";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("city", travelEndArrivalCity);
			List<Object[]> resultList = query.getResultList();
			return resultList.stream().map(result -> {
				Map<String, String> map = new HashMap<>();
				map.put(MEALS_TOTAL, (String) result[0]);
				map.put(COUNTY, (String) result[1]);
				map.put("city", (String) result[2]);
				return map;
			}).toList();
		} catch (Exception e) {
			log.error("Error Caught while Fetching  Meals Details for USA Based Cities : {} ", e.getMessage());
			return List.of();
		}
	}

	
	public int calculateDaysBetween(Date businessStartDate,Date businessEndDate) {
		long differenceInMillis = businessEndDate.getTime() - businessStartDate.getTime();
		return (int) (differenceInMillis / (1000 * 60 * 60 * 24));
	}

	
	@Override
	public OneWayBusinessTripWrapper getCostingForSingleTrip(OneWayBusinessTripWrapper businessTripWrapper) {
		
		
		  log.debug("Inside getCostingForSingleTrip businessTripWrapper:{}",businessTripWrapper); 
		  try { 
			  if (businessTripWrapper == null) {
		  log.error("BusinessTripWrapper is null"); throw new
		  IllegalArgumentException("BusinessTripWrapper cannot be null"); 
		  }
		  AirportWrapper fromAirport = businessTripWrapper.getFromCity();
		  AirportWrapper toAirport = businessTripWrapper.getToCity();
		  
		  if (toAirport.getCountry() == null || toAirport.getCountry().isEmpty()) 
		  {
		  log.error("TravelToCountry is null or empty"); 
		  throw new IllegalArgumentException("TravelToCountry cannot be null or empty"); 
		  } 
		  if(toAirport.getCity() == null || toAirport.getCity().isEmpty()) {
		  log.error("TravelEndArrivalCity is null or empty"); 
		  throw new IllegalArgumentException("TravelEndArrivalCity cannot be null or empty"); 
		  }
		  
		  if (fromAirport.getCountry() != null && toAirport.getCountry() != null) {
			  log.debug("Inside getCostingForSingleTrip fromCountry:{}, toCountry:{}", fromAirport.getCountry(), toAirport.getCountry());
			  if (fromAirport.getCountry().equalsIgnoreCase(toAirport.getCountry())) {
		            businessTripWrapper.setTravelScope("INTERNAL"); 
		        } else {
		            businessTripWrapper.setTravelScope("EXTERNAL");  
		        }
		  }
		  log.debug("Inside getCostingForSingleTrip travelScope:{}", businessTripWrapper.getTravelScope());
		  
		  String maximumDiemCost = "0"; 
		  String mealsTotalCost = "0"; 
		  String lodgingTotalCost = "0";
		  
		  checkAndSetCostingsForSingle(businessTripWrapper, maximumDiemCost, mealsTotalCost, lodgingTotalCost );
		  log.debug("Inside getCostingForSingleTrip payValue:{}", businessTripWrapper.getPayValue());
		  
		  
		  } catch (NumberFormatException e) 
		  { throw new IllegalArgumentException("Invalid cost values, unable to parse to integer",
		  e); } 
		  catch (Exception e) { 
			  throw new BusinessException("Unexpected error occurred in getCostingForSingleTrip ",
		  e); }
		 
		return businessTripWrapper;
	}
	
	
	private void checkAndSetCostingsForSingle(OneWayBusinessTripWrapper businessTripWrapper, String maximumDiemCost, 
			                         String mealsTotalCost,String lodgingCost ) {
		
		log.info("Inside Method checkAndSetCostingsForSingle");
		AirportWrapper toAirport = businessTripWrapper.getToCity();
		Integer businessDays= businessTripWrapper.getTotalBusinessDays();
		Double payValue=0.0;
		
		if (businessTripWrapper!=null && toAirport.getCity() !=null && toAirport.getCountry().equalsIgnoreCase("United States")) {
		    
		    log.info("Inside @checkAndSetCostingsForSingle going to calculate costings for USA  ");
		    handleUSACostings( businessTripWrapper,  toAirport, mealsTotalCost,  lodgingCost,  businessDays);
		    log.info("Inside @checkAndSetCostingsForSingle completed costings for USA    ");
		    }
		  else {
			 
		   log.info("Inside @checkAndSetCostingsForSingle  NON-USA  ");    
		   if(businessTripWrapper != null) {
			log.info("Inside @checkAndSetCostingsForSingle going to calculate costings for NON-USA  ");     
		    handleNonUSACostings( businessTripWrapper, toAirport,  maximumDiemCost,  mealsTotalCost,  businessDays);
		    log.info("Inside @checkAndSetCostingsForSingle completed costings for NON-USA  ");
		}   
	}
		log.info("checkAndSetCostingsForSingle travel completed");
	}
	
	private void handleUSACostings(OneWayBusinessTripWrapper businessTripWrapper, AirportWrapper toAirport, 
            String mealsTotalCost, String lodgingCost, Integer businessDays) 
	{
		Double usaPayValue=0.0;
		log.info("Inside handleUSACostings ");
		List<Map<String, String>> usaBasedMealsCost = findMealsCostByUSACountryAndCity(toAirport.getCity());
	    log.debug("Inside @checkAndSetCostingsForSingle usaBasedMealsCost is : {} ", usaBasedMealsCost);
	    if (usaBasedMealsCost == null || usaBasedMealsCost.isEmpty()) {
	        log.error("USA based meals cost data is null or empty");
	        throw new IllegalStateException("No data found for USA based meals cost");
	    }
	    mealsTotalCost = iterateForUsaBasedMealsCostInsideCheckAndSetCostings(mealsTotalCost, usaBasedMealsCost);
	    
	    List<BusinessDetails> businessList= businessTripWrapper.getBusiness();
	    Integer totalLodgingCost=0;
	    if(businessList !=null && !businessList.isEmpty()){
	     log.debug("Inside  Method checkAndSetCostingsForSingle businessList:{} ",businessList);
	     
	    for(BusinessDetails businessCase :businessList ) {
	    	
	    List<Map<String, String>> usaBasedLodgingCost = findLodgingCostByUSACountryAndCity(toAirport.getCity(),
	    		businessCase.getEndDate());
	    log.debug("usaBasedLodgingCost is : {} ", usaBasedLodgingCost);
	    if (usaBasedLodgingCost == null || usaBasedLodgingCost.isEmpty()) {
	        log.error("USA based lodging cost data is null or empty");
	       }
	      lodgingCost = iterateForUsaBasedLodgingCost(lodgingCost, usaBasedLodgingCost);
	      totalLodgingCost=totalLodgingCost+(Integer.parseInt(lodgingCost) * businessCase.getDays());
	       }
	     }
	    
	    if(businessTripWrapper.getEndTravelDate()== null) {
	    businessTripWrapper.setPayValue((double)(businessTripWrapper.getTotalBusinessDays()*(Integer.parseInt(mealsTotalCost)))+totalLodgingCost);
	    }  
	    else {
	    	
	    	usaPayValue= (double)((businessDays-1)*(Integer.parseInt(mealsTotalCost)+totalLodgingCost))+ 1*Integer.parseInt(mealsTotalCost);
	    	businessTripWrapper.setPayValue(usaPayValue);
	    }
	    
		
	}
	
	private void handleNonUSACostings(OneWayBusinessTripWrapper businessTripWrapper, AirportWrapper toAirport, 
            String maximumDiemCost, String mealsTotalCost, Integer businessDays ) {
		log.info("Inside handle NON-USA Costings ");
		Double nonUSAPayValue=0.0;
		String mealsNonUSACost ="0";
		
		List<Map<String, String>> nonUsaBasedMealsAndLodgeCost = findMealsAndLodgeCostByNonUSACountryAndCity(toAirport.getCountry(),
			      toAirport.getCity());
			    log.debug("nonUsaBasedMealsAndLodgeCost is : {} ", nonUsaBasedMealsAndLodgeCost);
			    if (nonUsaBasedMealsAndLodgeCost == null || nonUsaBasedMealsAndLodgeCost.isEmpty()) {
			        log.error("Non-USA based meals and lodge cost data is null or empty");
			        throw new IllegalStateException("No data found for non-USA based meals and lodge cost");
			    }
			    maximumDiemCost = iterateForNonUsaBasedMealsAndLodgeCost(maximumDiemCost, nonUsaBasedMealsAndLodgeCost);
			    log.debug("Inside checkAndSetCostingsForSingle maximumDiemCost :{}",maximumDiemCost);

			    List<Map<String, String>> nonUsaBasedMealsCost= findMealsCostByNonUSACountryAndCity(toAirport.getCity());
			    log.debug("Inside @checkAndSetCostingsForSingle nonUsaBasedMealsCost is : {} ", nonUsaBasedMealsCost);
			    if (nonUsaBasedMealsCost == null || nonUsaBasedMealsCost.isEmpty()) {
			        log.error("NON USA based meals cost data is null or empty");
			        throw new IllegalStateException("No data found for NON USA based meals cost");
			    }
			    mealsNonUSACost=iterateForNonUsaBasedMealsCostInsideCheckAndSetCostings(mealsNonUSACost,nonUsaBasedMealsCost);
			    log.debug("Inside checkAndSetCostingsForSingle mealsNonUSACost :{}",mealsNonUSACost);

			    
			    if(businessTripWrapper.getEndTravelDate()== null) {
			    log.info("Inside checkAndSetCostingsForSingle travel end date is null");
			    	businessTripWrapper.setPayValue((double) (businessTripWrapper.getTotalBusinessDays() * Integer.parseInt(maximumDiemCost) ));
	             }  
			    else {
			    	log.info("Inside checkAndSetCostingsForSingle travel end date is not null");
			    	nonUSAPayValue= (double)((businessDays-1)*(Integer.parseInt(maximumDiemCost)))+ 1*Integer.parseInt(mealsNonUSACost);
			    	businessTripWrapper.setPayValue(nonUSAPayValue);	
			    }
	}
   
	@Override
	public BusinessTrip update(BusinessTrip businessTrip)
	{
		log.debug("Inside BusinessTripServiceImpl update : {}",businessTrip); 
		return businessTripRepository.save(businessTrip);
	}
	
	
	@Override
	public MultiCityBusinessTripWrapper getCostingForMultiTrip(MultiCityBusinessTripWrapper businessTripWrapper) {
		log.debug("Inside getCostingForMultiTrip businessTripWrapper:{}",businessTripWrapper); 
		  try { 
			  if (businessTripWrapper == null) {
		  log.error("BusinessTripWrapper is null"); throw new
		  IllegalArgumentException("BusinessTripWrapper cannot be null"); 
		  }
		      
			  Double totalPayValue=0.0;
			  
			  List<OneWayBusinessTripWrapper> multiCityList = businessTripWrapper.getMulticityData();
			  if(multiCityList!=null && !multiCityList.isEmpty()) {
			  
				  OneWayBusinessTripWrapper firstTrip= multiCityList.get(0);
				  String firstCity = firstTrip.getFromCity().getCity();
				  
				  log.debug("Inside getCostingForMultiTrip firstCity: {} ",firstCity);
			  
			  businessTripWrapper=setTravelScope(businessTripWrapper);
			  log.debug("Inside getCostingForMultiTrip travelScope: {} ",businessTripWrapper.getTravelScope());
			  
			  for(OneWayBusinessTripWrapper singleTrip: multiCityList ) {
				  String lastCity = singleTrip.getToCity().getCity();
				  if(!firstCity.equals(lastCity)) {
				 log.debug("Inside getCostingForMultiTrip  when firstCity: {} not equals lastCity:{} ",firstCity, lastCity); 
				  String maximumDiemCostSingle = "0"; 
				  String mealsTotalCostSingle = "0"; 
				  String lodgingTotalCostSingle = "0";
				  checkAndSetCostingsForSingle(singleTrip, maximumDiemCostSingle, mealsTotalCostSingle, lodgingTotalCostSingle );
				  log.debug("Inside getCostingForMultiTrip  totalPayValue:{}",totalPayValue);
				  totalPayValue+=singleTrip.getPayValue();
				  log.debug("Inside getCostingForMultiTrip After iterating totalPayValue:{}",totalPayValue);
				  }
				  
			  }
			  }
			  
			  businessTripWrapper.setPayValue(totalPayValue);
			  log.debug("Inside getCostingForMultiTrip totalPayValue:{}",businessTripWrapper.getPayValue());
			  
		 }

			     
		  catch (NumberFormatException e) {
		    throw new IllegalArgumentException("Invalid cost values, unable to parse to integer",e); } 
		  catch (Exception e) { 
			  throw new BusinessException("Unexpected error occurred in getCostingForSingleTrip ",e); }
		  
		  return businessTripWrapper;
}
	
	private MultiCityBusinessTripWrapper setTravelScope(MultiCityBusinessTripWrapper businessTripWrapper) {
		
		for(OneWayBusinessTripWrapper singleTrip: businessTripWrapper.getMulticityData() ) {
			 String fromCountry = singleTrip.getFromCity().getCountry();
		     String toCountry = singleTrip.getToCity().getCountry();
		     log.debug("Inside setTravelScope fromCountry:{} , toCountry:{}",fromCountry,toCountry);
		        if (fromCountry != null && toCountry != null && !fromCountry.equals(toCountry)) {
		        	
		            businessTripWrapper.setTravelScope("EXTERNAL");
		            log.debug("Inside setTravelScope : {}",businessTripWrapper.getTravelScope());
		            break;
		        } 
		        else {
		            businessTripWrapper.setTravelScope("INTERNAL");
		        }
		}
		
	    
		return businessTripWrapper;
	}

	@Override
	public String deleteBusinessTripForEmployee(Integer id, Integer employeeId) {
		log.debug("Inside BusinessTripServiceImpl deleteBusinessTripForEmployee id:{},employeeId:{}",id,employeeId);
		try
		{
			BusinessTripRepository businessTripRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(BusinessTripRepository.class);
			
			if(id!=null) {
			
			deleteBusinessTripForEmployee(id);
			log.info("Completed deleteBusinessTripForEmployee");
			businessTripRepository.deleteById(id);
			log.info("BusinessTrip deleted with id:{}",id);
			return APIConstants.SUCCESS_JSON;
			}
			
			if(employeeId == null) {
			User contextUser = getUserContext();
			log.debug("contextUser user Id is : {}", contextUser.getUserid());
			Employee employee = employeeRepository.findByUserId(contextUser.getUserid());
			if(employee!=null) {
				log.debug("Inside deleteBusinessTripForEmployee employee found with id:{}", employee.getId());
				employeeId=employee.getId();
			} else {
				log.error("Employee not found for context user id:{}",contextUser.getUserid());
				throw new BusinessException("Employee not found");
			       }		
	        }
			log.debug("Inside BusinessTripServiceImpl deleteBusinessTripForEmployee customerId is : {}", commonUtils.getCustomerId());

			List<BusinessTrip> businessTripList =businessTripRepository.getBusinessTripsForEmployee(employeeId, commonUtils.getCustomerId());
			if( businessTripList!=null && !businessTripList.isEmpty()) {
				log.info("businessTripList is not null and empty :{}", businessTripList);
				for(BusinessTrip businessTrip : businessTripList){
					log.info("Going to deleting businessTrip and its corresponding mappings with id:{}",businessTrip.getId());
					deleteBusinessTripForEmployee(businessTrip.getId());
					log.info("Going to delete businessTrip id:{}",businessTrip.getId());
					businessTripRepository.deleteById(businessTrip.getId());
					log.info("Deleted businessTrip id:{}",businessTrip.getId());
				}
			log.info("Deleted all business trips for employee :{}", employeeId);
			}
		 	
			return APIConstants.SUCCESS_JSON;
			
			
		}
		catch (Exception e) {
			log.debug("SomeThing Went Wrong While Deleting BusinessTrip ");
			log.error("Error occured while deleting businessTrip :{}",e);
			return APIConstants.FAILURE_JSON;
		}
		
		
	}
	
	private String deleteBusinessTripForEmployee(Integer businessTripId)
	{
		log.info("Inside deleteBusinessTripForEmployee businessTripId :{}",businessTripId);
		BusinessTripRepository businessTripRepository = ApplicationContextProvider.getApplicationContext()
				.getBean(BusinessTripRepository.class);
		BusinessTripItineraryRepository businessTripItineraryRepository = ApplicationContextProvider.getApplicationContext()
				.getBean(BusinessTripItineraryRepository.class);
		try {
			log.debug("Inside deleteBusinessTripForEmployee customerId is : {}", commonUtils.getCustomerId());
		List<BusinessTripItinerary> itineraryList =	businessTripRepository.getByBusinessTripId(businessTripId, commonUtils.getCustomerId());
		log.info("Inside deleteBusinessTripForEmployee itineraryList :{}",itineraryList);
		if(itineraryList !=null && !itineraryList.isEmpty()) {
			log.info("not empty itineraryList :{}",itineraryList);
			for(BusinessTripItinerary businessTripItinerary: itineraryList ) {
				
				businessTripRepository.deleteAllItineraryMapping(businessTripItinerary.getId(), commonUtils.getCustomerId());
				businessTripItineraryRepository.deleteById(businessTripItinerary.getId());
			}
			log.info("All BusinessTripItinerary mapppings for businessTrip deleted with id:{}",businessTripId);
	}
		log.info("All BusinessTripItinerary mapppings for businessTrip deleted with id:{}",businessTripId);
		return APIConstants.SUCCESS_JSON;
		}
		catch(Exception e) {
			log.debug("SomeThing Went Wrong While Deleting BusinessTrip And BusinessTripItinerary ");
			return APIConstants.FAILURE_JSON;
		}
}

	@Override
	public BusinessTrip updateBusinessTripWorkflowStage(BusinessTripDto businessTripDto) {
		log.debug("Inside method updateBusinessTripWorkflowStage Id : {} workflowStage:{} ", businessTripDto.getBusinessTripId(), businessTripDto.getWorkflowStage());
		try {
			if (businessTripDto.getBusinessTripId() != null) {
				BusinessTrip optionalBusinessTrip = super.findById(businessTripDto.getBusinessTripId());
				if (optionalBusinessTrip != null) {
					BusinessTrip businessTrip = optionalBusinessTrip;
					if (businessTripDto.getWorkflowStage().equalsIgnoreCase("CANCELLED") && businessTrip != null
							&& businessTrip.getProcessInstanceId() != null) {
						log.debug("Inside method updateBusinessTripWorkflowStage ProcessInstanceId found is  : {}",
								businessTrip.getProcessInstanceId());
						Workorder wordorder = workorderController
								.findByProcessInstanceId(businessTrip.getProcessInstanceId());
						log.debug("Inside method updateBusinessTripWorkflowStage wordorder found is  : {}", wordorder);
						if (wordorder != null && wordorder.getReferenceId() != null) {
							try {
								String response = workorderController.deleteWorkorder(wordorder.getReferenceId());
								log.debug(
										"Inside method updateBusinessTripWorkflowStage response from  deleteWorkorder api is : {}",
										response);
								 workflowActionsController.notifyActions(businessTrip.getProcessInstanceId()); 
 							} catch (Exception e) {
								log.error("Facing error While deleting Workorder");
							}
						}
					}
					if(businessTrip!=null) {
						businessTrip.setWorkflowStage(businessTripDto.getWorkflowStage());}
					return businessTripRepository.save(businessTrip);
				} else {
					throw new BusinessException("BusinessTrip with ID " + businessTripDto.getBusinessTripId() + " not found");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("error while updating BusinessTrip work flow stage", e.getMessage());
		}
		return null;
	}
	
	
	
	@Override
	public ResponseEntity<byte[]> createWpsTxtFileForAllTrips() {
		log.info("inside @class BusinessTripServiceImpl @method createWpsTxtFileForAllBenefits ");
		try {
			LocalDate currDate = LocalDate.now();
			LocalDate nextDate = currDate.plusDays(1);
			Integer year = currDate.getYear();
			LocalDate sixDaysAgo = currDate.minusDays(6);
			DayOfWeek dayOfWeek = DayOfWeek.from(currDate);
			log.debug("the current day is :{} ", dayOfWeek.name());
			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			int weekNumber = currDate.get(weekFields.weekOfWeekBasedYear());
			log.debug("Week num is :{} ", weekNumber);
			log.debug("inside @class BusinessTripServiceImpl @method createWpsTxtFileForAllBenefitcustomerId is : {}", commonUtils.getCustomerId());
          
      			List<BusinessTrip> businessTrips = businessTripRepository.getBusinessTripByDates(
      					sixDaysAgo, currDate,PRConstant.APPROVED_BY_FINANCE_CONTROLLER, commonUtils.getCustomerId());
      			log.debug("size of Trips :{} ", businessTrips.size());
      			List<String[]> data = getData(businessTrips);
      			ByteArrayOutputStream out = generateTxt(data);
      			byte[] txtBytes = out.toByteArray();
      			String fileReference = getSystemTimeWithTimeStamp();
      			String fileName = "BT" + "Wps" + fileReference + ".txt";
      			String filePath = "hrmswps/" + fileName;

      			log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}", fileName, fileReference, filePath);

      			log.info("Inside createWpsTxtFileForAllTrip rootDirBucketName :{}", rootDirBucketName);
      			InputStream inputStream = new ByteArrayInputStream(txtBytes);
      			uploadFileInStorage(inputStream, fileName, filePath, rootDirBucketName);
      			HttpHeaders headers = new HttpHeaders();
      			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      			headers.setContentDispositionFormData(ATTACHMENT, fileName);
      			// String fileDir = rootDirBucketName + filePath;
      			log.debug("The Path of File Dir to store is :{} ", filePath);
      			List<Integer> employeeIdList = getEmployeeIdList(businessTrips);
      			ObjectMapper objectMapper = new ObjectMapper();
      			String json = objectMapper.writeValueAsString(employeeIdList);
      		OtherExpenseBankRequest otherExpenseBankRequest = savePath(PRConstant.BUSINESS_TRIP_SM, filePath, currDate, year, weekNumber, json);
      		saveValueInBenefits(otherExpenseBankRequest,businessTrips);
			return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);

		} catch (Exception e) {
			log.error(
					"Error inside @class EducationBenefitServiceImpl @method createWpsTxtFileForAllEducationalBenefit :{} :{} ",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException("Something wrong in Creating wps for all Education Benefit ");
		}

	}

	private ByteArrayOutputStream generateTxt(List<String[]> data) {
		log.debug("Inside @class BusinessTripServiceImpl @method generateTxt data :{}", data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
			if (data != null) {
				for (String[] line : data) {
					writer.write(String.join(",", line));
					writer.newLine();
				}
				log.info("Inside @class BusinessTripServiceImpl generateTxt data");
				writer.flush();
			}
		} catch (IOException e) {
			log.error("Error generating TXT content: {}", e.getMessage());
		}
		return out;
	}

	private String getSystemTimeWithTimeStamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return now.format(formatter);
	}

	private void uploadFileInStorage(InputStream in, String fileName, String filePath, String rootDir)
			throws IOException {
		log.debug("Inside uploadFileInStorage :: {}", fileName);
		log.debug("fileName: {} filePath: {}", fileName, filePath);
		try (InputStream inputStream = new ByteArrayInputStream(in.readAllBytes())) {

			storageRest.createFile(inputStream, rootDir, filePath);
		}
	}

	List<String[]> getData(List<BusinessTrip> businessTrips) {
		List<String[]> data = new ArrayList<>();
		try {
			log.info(INSIDE_CLASS_LOG + "getData");
			Map<String, String> mapHrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
			String ifh = mapHrmsSystemConfigMap.get("ifh");
			String ifile = mapHrmsSystemConfigMap.get("ifile");
			String csv = mapHrmsSystemConfigMap.get("csv");
			String connectId = mapHrmsSystemConfigMap.get("connectId");
			String customerId = mapHrmsSystemConfigMap.get("customerId");
			String sach = mapHrmsSystemConfigMap.get("@sach@");
			String bathdr = mapHrmsSystemConfigMap.get("bathdr");
			String achcr = mapHrmsSystemConfigMap.get("achcr");
			String debitAccountNumber = mapHrmsSystemConfigMap.get("debitAccountNumber");
			String paymentPurposeCodeO = mapHrmsSystemConfigMap.get("paymentPurposeCodeO");
			String paymentNarrationCompany = mapHrmsSystemConfigMap.get("wpsPopTest");
			String pcmHsbcnetTest = mapHrmsSystemConfigMap.get("PcmHsbcnetTest");
			String first = mapHrmsSystemConfigMap.get("@1st@");
			String sar = mapHrmsSystemConfigMap.get("sar");
			String molEstablisedId = mapHrmsSystemConfigMap.get("molEstablisedId");
			String employerId = mapHrmsSystemConfigMap.get("employerId");
			String p = mapHrmsSystemConfigMap.get("p");
			String one = mapHrmsSystemConfigMap.get("one");
			String secpty = mapHrmsSystemConfigMap.get("secpty");
			String n = mapHrmsSystemConfigMap.get("n");
			String alw = mapHrmsSystemConfigMap.get("ALW");
			log.debug(
					INSIDE_CLASS_LOG
							+ "getData after all the data fetch from Hrms_System_Config mapHrmsSystemConfigMap :{} ",
					mapHrmsSystemConfigMap);
			String fileReference = "ILWPSOPOPVATEST" + getSystemTimeWithTimeStamp();
			String batchReference = "IWPSOPOP" + getSystemTimeWithTimeStampNew();
			String currentDate = getCurrentDate();
			String currentTime = getCurrentTime();
			String valueDate = getDateAfterDays(1);

			log.info(INSIDE_CLASS_LOG + "getData after set date and time");

 
				Integer count = businessTrips.size();
				log.debug(INSIDE_CLASS_LOG + "fetch count :{}", count);
				String totalCount = count.toString();
				log.debug(INSIDE_CLASS_LOG + "fetch totalCount :{}", totalCount);
				Integer totalRecordCount = Integer.parseInt(totalCount) + 2;
				String totalRecordCountString = totalRecordCount.toString();
				String netWorth = getTotalAmount(businessTrips);

 
				data.add(new String[] { ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime,
						p, one, totalRecordCountString });
				data.add(new String[] { bathdr, achcr, totalCount, "", "", "", paymentPurposeCodeO,
						paymentNarrationCompany, "", first, valueDate, debitAccountNumber, sar, netWorth, "", "", "",
						"", "", "", molEstablisedId, employerId, "", "", "", batchReference });
				iterateTrips(secpty, n, sach, businessTrips, data, alw, paymentNarrationCompany);

				log.info(INSIDE_CLASS_LOG + "fetchData Educational Benefit Successfully Iterate");
 
			return data;

		} catch (Exception e) {
			log.error("Error Inside @class businessTripServiceImpl @method getData :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private String getSystemTimeWithTimeStampNew() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		return now.format(formatter);
	}

	private String getCurrentTime() {
		LocalTime time = LocalTime.now();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(timeFormatter);
	}

	private String getCurrentDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return date.format(dateFormatter);
	}

	private String getDateAfterDays(int days) {
		LocalDate date = LocalDate.now();
		LocalDate newDate = date.plusDays(days);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return newDate.format(dateFormatter);
	}

	private String getTotalAmount(List<BusinessTrip> businessTrips) {
		log.debug("Inside Method getTotalAmount businessTrips size :{} ", businessTrips.size());
		Double amount = 0.0;
		for (BusinessTrip businessTrip : businessTrips) {
			amount = amount + businessTrip.getPayValue();
		}
		log.debug("Inside Method getTotalAmount businessTrips amount :{} ", amount);
		return String.valueOf(amount);

	}

	private void iterateTrips(String secpty, String n, String sach, List<BusinessTrip> businessTrips, List<String[]> data,
			String alw, String paymentNarrationCompany) {
		String one = "1";
		log.info(INSIDE_CLASS_LOG + " iterateOnRunDetails");
        if(businessTrips != null) {
		for (BusinessTrip businessTrip  : businessTrips) {
			log.debug(INSIDE_CLASS_LOG + "iterateOnbusinessTrip businessTrip :{}", businessTrip);
			Integer employeeId = businessTrip.getEmployee().getId();
			String employeeReferenceId = businessTrip.getEmployee().getEmployeeId();

			String nationalIdentification = "";
			log.debug("Inside iterateTrips customerId: {}", commonUtils.getCustomerId());
			EmployeeNationalIdentification employeeNationalIdentification = employeeNationalIdentificationRepository
					.findNationalIdentificationNumberByEmployeeId(employeeId, commonUtils.getCustomerId());
			if (employeeNationalIdentification != null) {
				nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
			}
			AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext()
					.getBean(AccountDetailsService.class);
			List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employeeId);

			for (AccountDetails accountDetail : accountDetails) {
				log.debug(INSIDE_CLASS_LOG + "iterateEducationalBenefit accountDetail :{}", accountDetail);
				StringBuilder rowBuilder = new StringBuilder();

				rowBuilder.append(secpty).append(",");
				rowBuilder.append(accountDetail.getIban()).append(",");
				rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
				rowBuilder.append(employeeId).append(",");
				rowBuilder.append(accountDetail.getBankId()).append(",").append(",").append(",");

				rowBuilder.append(businessTrip.getPayValue());

				rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n)
						.append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
				rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()).append(",");
				rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one)
						.append(",");
				rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",")
						.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",")
						.append(",");
				rowBuilder.append(alw);
				data.add(rowBuilder.toString().split(","));
				log.debug(INSIDE_CLASS_LOG + " iterateBenefit data :{}", data);

			}
		}
        }
	}

	OtherExpenseBankRequest savePath(String type, String path, LocalDate date, Integer year, Integer weekNumber, String json) {
		log.info("Inside @class BusinessTripServiceImpl @method savePath ");
		try {
			OtherExpenseBankRequestService otherExpenseBankRequestService = ApplicationContextProvider
					.getApplicationContext().getBean(OtherExpenseBankRequestService.class);
			OtherExpenseBankRequest otherExpenseBankRequest = otherExpenseBankRequestService.savePath(type, path, date, year, weekNumber, PRConstant.WPS_CREATED, json);
			log.debug("Inside @class BusinessTripServiceImpl @method savePath date :{}  path:{} ", date, path);
		 return otherExpenseBankRequest;
		} catch (Exception e) {
			log.error("Error inside @class BusinessTripServiceImpl @method savePath :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum) {

		log.info("Inside @class BusinessTripServiceImpl @method downloadCommonWpsFile");
		LocalDate currDate = LocalDate.now();
		Integer year = currDate.getYear();
		OtherExpenseBankRequestService otherExpenseBankRequestService = ApplicationContextProvider
				.getApplicationContext().getBean(OtherExpenseBankRequestService.class);
		String filePath = otherExpenseBankRequestService.getPathForWps(PRConstant.BUSINESS_TRIP_SM, weekNum, year);

		log.info("Inside BusinessTripServiceImpl downloadWpsFile rootDirBucketName :{} ", rootDirBucketName);

		String currentTime = getSystemTimeWithTimeStamp();
		try {
			log.debug("Inside BusinessExpenseServiceImpl @method downloadWpsFile :{} ", filePath);
			InputStream inputStream = storageRest.read(rootDirBucketName, filePath);
			log.debug("InputStream available bytes: {}", inputStream.available());
			InputStreamResource resource = new InputStreamResource(inputStream);
			byte[] fileData = resource.getContentAsByteArray();
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			responseHeaders.setContentDispositionFormData(ATTACHMENT, "Wps" + currentTime + ".txt");
			return new ResponseEntity<>(fileData, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error Inside @class BusinessTripServiceImpl @method downloadWpsFile :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException("Something went wrong inside downloadWpsFile ");
		}
	}

	private List<Integer> getEmployeeIdList(List<BusinessTrip> businessTrips) {
		try {
			log.info("Inside @class BusinessTripServiceImpl @method getEmployeeIdList ");
			List<Integer> employeeIdList = new ArrayList<>();
			for (BusinessTrip businessTrip : businessTrips) {
				employeeIdList.add(businessTrip.getEmployee().getId());
			}
			log.debug("The Size of List :{} ", employeeIdList.size());
			return employeeIdList;
		} catch (Exception e) {
			log.error("Error Inside @class BusinessTripServiceImpl @method getEmployeeIdList :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public void updateWorkflowStageForEmployee() {
		User contextUser = getUserContext();
		log.debug("contextUser user Id is : {}", contextUser.getUserid());
		EmployeeRepository employeeRepository = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeRepository.class);
		Employee employee = employeeRepository.findByUserId(contextUser.getUserid());
		BusinessTrip updatedBusinessTrip = null;
		if(employee!=null) {
			log.debug("Inside updateWorkflowStageForEmployee employee:{}",employee.getId());
			BusinessTripRepository businessTripRepository= ApplicationContextProvider.getApplicationContext()
	    			.getBean(BusinessTripRepository.class);
			log.debug("Inside updateWorkflowStageForEmployee customerId is : {}", commonUtils.getCustomerId());
	    	BusinessTrip businessTrip = businessTripRepository.getEmployeeLastBusinessTrip(employee.getId(),commonUtils.getCustomerId());
	    	log.debug("Inside updateWorkflowStageForEmployee businessTrip id id:{}, workStage:{}",
	    			businessTrip.getId(),businessTrip.getWorkflowStage());
	    	businessTrip.setWorkflowStage("Approved");
	    	updatedBusinessTrip = businessTripRepository.save(businessTrip);
	    	log.info("Inside getJobLevelForEmployee updatedBusinessTrip saved id:{}, updatedWorkflow:{}" ,updatedBusinessTrip.getId(),updatedBusinessTrip.getWorkflowStage());
	    	
	  }
		}
	
	
	public void saveValueInBenefits(OtherExpenseBankRequest otherExpenseBankRequest ,List<BusinessTrip> businessTrips)
	{
		try
		{
			log.debug(" Inside @class HrBenefitServiceImpl @method saveValueInBenefits oTEBR id :{}  hrbenefit size :{}  ",otherExpenseBankRequest.getId(),businessTrips.size());
		  for(BusinessTrip businessTrip : businessTrips)
		  {
			  businessTrip.setOtherExpenseBankRequestId(otherExpenseBankRequest.getId());
			  businessTripRepository.save(businessTrip);
		}
		  log.info("Id of Other Expense Bank Request Saved in BusinessExpense ");
		}
		catch(Exception e)
		{
			log.error("Error Inside @class HrBenefitServiceImpl @method saveValueInBenefits :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	@Override
	public List<BusinessTrip> getBusinessTripByOtherExpenseBankRequestId(OtherExpenseBankRequest otherExpenseBankRequest)
	{
		try
		{
			log.debug("Inside @class BusinessTripServiceImpl @method getBusinessTripByOtherExpenseBankRequestId OEBR id :{} ",otherExpenseBankRequest.getId());
			log.debug("Inside @class BusinessTripServiceImpl @method getBusinessTripByOtherExpenseBankRequestId customerId is : {}", commonUtils.getCustomerId());
		 List<BusinessTrip> businessTrips = businessTripRepository.getBusinessTripsByBankRequestId(otherExpenseBankRequest.getId(), commonUtils.getCustomerId());
		 log.debug("Size of BusinessTrips :{} for OEBR id :{} ",businessTrips.size(),otherExpenseBankRequest.getId());
		 return businessTrips;	
		}
		catch(Exception e)
		{
			log.error("Error Inside @class HrBenefitServiceImpl @method getBusinessTripByOtherExpenseBankRequestId :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	@Override
	public List<Object[]> getBusinessTripForExpense(List<Integer> businessTripIds,String type)
	{
		try
		{
			log.debug("Inside @class BusinessTripServiceImpl @Method getBusinessTripForExpense size of list :{} ",businessTripIds.size());
			log.debug("Inside @class BusinessTripServiceImpl @Method getBusinessTripForExpense customerId is : {}", commonUtils.getCustomerId());
			List<Object[]> businessTripsList = businessTripRepository.getBusinessTripForExpense(businessTripIds,type, commonUtils.getCustomerId());
			log.debug(" Size of Business Trip Expense for Type :{} size :{} ",type,businessTripsList.size());
			return businessTripsList;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class HrBenefitServiceImpl @method getBusinessTripForExpense :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();	
		}
	}
	
	@Override
	public Object  getBusinessTripForAccural(List<Integer> businessTripIds,String type)
	{
		try
		{
			log.debug("Inside @class BusinessTripServiceImpl @Method getBusinessTripForAccural size of list :{} ",businessTripIds.size());
			log.debug("Inside @class BusinessTripServiceImpl @Method getBusinessTripForAccural customerId is : {}", commonUtils.getCustomerId());
			Object businessTripsList = businessTripRepository.getBusinessTripForAccural(businessTripIds,type, commonUtils.getCustomerId());
			log.debug(" Size of Business Trip Accural for Type :{} size :{} ",type,businessTripsList);
			return businessTripsList;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class HrBenefitServiceImpl @method getBusinessTripForAccural :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();	
		}
	}
}

	
