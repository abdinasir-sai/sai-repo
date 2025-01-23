package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Holiday;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * HolidayService interface is a service layer interface which handles all the
 * business logic related to Holiday model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Holiday
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface HolidayService extends GenericService<Integer,Holiday> {

	

	List<Holiday> findAll();
	String createWeekendHolidayForYear();

}
