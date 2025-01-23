package com.nouros.hrms.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.connectx.util.APIConstants;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Holiday;
import com.nouros.hrms.repository.HolidayRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.HolidayService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.payrollmanagement.utils.PRConstant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "HolidayServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "HolidayService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Holiday and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Holiday
 * Holiday) for exporting the Holiday data into excel file by reading the
 * template and mapping the Holiday details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class HolidayServiceImpl extends AbstractService<Integer,Holiday> implements HolidayService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Holiday entities.
	 */

	private static final Logger log = LogManager.getLogger(HolidayServiceImpl.class);

	public HolidayServiceImpl(GenericRepository<Holiday> repository) {
		super(repository, Holiday.class);
	}

	@Autowired
	private HolidayRepository holidayRepository;



	/**
	 * Creates a new vendor.
	 *
	 * @param holiday The holiday object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Holiday create(Holiday holiday) {
		log.info("inside @class HolidayServiceImpl @method create");
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		holiday.setWorkspaceId(workspaceId); // done done
		return holidayRepository.save(holiday);
	}

	@Override
	public List<Holiday> findAll() {
		return holidayRepository.findAll();
	}
	
	@Override
	public String createWeekendHolidayForYear()
	{
		try {
			int year = LocalDate.now().getYear();
	        
	         for (int month = 1; month <= 12; month++) {
	             LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
	            LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
	             for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
	                 if (date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                        Holiday holiday = new Holiday();
                        Date holidayDate = convertToDate(date);
                        holiday.setDate(holidayDate);
                       holiday.setName(PRConstant.WEEKEND);
                       holidayRepository.save(holiday);
	                }
	            }
	        }
           return APIConstants.SUCCESS_JSON;
		}
		catch(Exception e)
		{
			log.error("Error inside @class HolidayServiceImpl @method createWeekendHolidayForYear :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException("Problem while creating Weekend Holiday");
		}
	}
	 public Date convertToDate(LocalDate localDate) {
 	        LocalDateTime localDateTime = localDate.atStartOfDay();
 	        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	    }

}
