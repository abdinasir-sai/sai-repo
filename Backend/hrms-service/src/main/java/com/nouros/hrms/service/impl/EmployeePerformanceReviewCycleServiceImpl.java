package com.nouros.hrms.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.EmployeePerformanceReviewCycle;
import com.nouros.hrms.repository.EmployeePerformanceReviewCycleRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeePerformanceReviewCycleService;
import com.nouros.hrms.service.EmployeeReviewService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.wrapper.EmployeePerformanceReviewCycleWrapper;
import com.nouros.payrollmanagement.utils.PRConstant;


@Service
public class EmployeePerformanceReviewCycleServiceImpl extends AbstractService<Integer,EmployeePerformanceReviewCycle> implements EmployeePerformanceReviewCycleService {
	
	private static final Logger log = LogManager.getLogger(EmployeePerformanceReviewCycleServiceImpl.class);
 
	public EmployeePerformanceReviewCycleServiceImpl(GenericRepository<EmployeePerformanceReviewCycle> repository) {
		super(repository, EmployeePerformanceReviewCycle.class);
	}
	
	@Autowired
	private EmployeePerformanceReviewCycleRepository employeePerformanceReviewCycleRepository;

	@Override
	public void softDelete(int id) {
		log.info("EmployeePerformanceReviewCycle Id is : {}", id);
		EmployeePerformanceReviewCycle employeePerformanceReviewCycle = super.findById(id);
		if (employeePerformanceReviewCycle != null) {

			EmployeePerformanceReviewCycle employeePerformanceReviewCycle1 = employeePerformanceReviewCycle;
			employeePerformanceReviewCycle1.setDeleted(true);
			employeePerformanceReviewCycleRepository.save(employeePerformanceReviewCycle1);
		}
	}

	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {

				softDelete(list.get(i));

			}
		}

	}

	@Override
	public  EmployeePerformanceReviewCycle createEmployeePerformanceReviewCycleByWrapper(EmployeePerformanceReviewCycleWrapper employeePerformanceReviewCycleWrapper)
	{
		try {
		log.info("Inside class EmployeePerformanceReviewCycleServiceImpl method createEmployeePerformanceReviewCycleByWrapper ");	
		EmployeePerformanceReviewCycle employeePerformanceReviewCycle = new EmployeePerformanceReviewCycle();
		employeePerformanceReviewCycle.setStartDate(employeePerformanceReviewCycleWrapper.getStartDate());
		employeePerformanceReviewCycle.setEndDate(employeePerformanceReviewCycleWrapper.getEndDate());
		employeePerformanceReviewCycle.setStatus(PRConstant.STARTED);
		employeePerformanceReviewCycle = employeePerformanceReviewCycleRepository.save(employeePerformanceReviewCycle);
		log.info("employeePerformanceReviewCycle created");
		return employeePerformanceReviewCycle;
		}
		catch(Exception e)
		{
			log.debug("Error inside class EmployeePerformanceReviewCycleServiceImpl method createEmployeePerformanceReviewCycleByWrapper :{} :{} ",e.getMessage(),
					Utils.getStackTrace(e));
		throw new BusinessException();
		}
	}
	
	@Override
	public Boolean getValueforEmployeeReview()
	{
		try
		{
			log.info("Inside class EmployeePerformanceReviewCycleServiceImpl method getValueforEmployeeReview ");
			LocalDate todayDate = LocalDate.now();
			log.debug("Todays Date is :{} ",todayDate);
			List<EmployeePerformanceReviewCycle> employeePerformanceReviewCycles = employeePerformanceReviewCycleRepository.findAll();
			for(EmployeePerformanceReviewCycle employeePerformanceReviewCycle : employeePerformanceReviewCycles )
			{
			  Date startDate = employeePerformanceReviewCycle.getStartDate();
			  LocalDate localStartDate = convertDateToLocalDate(startDate);
			  if(localStartDate.getMonth() == todayDate.getMonth())
			  {
				  log.debug("Two Dates are equal :{} :{}",todayDate ,localStartDate);
				  EmployeeReviewService employeeReviewService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeReviewService.class);
				  employeeReviewService.createEmployeeReview(employeePerformanceReviewCycle);
				  return true;
			  }
			}
			log.debug("Two Dates are not equal :{} ",todayDate);
			return false;
		}
		catch(Exception e)
		{
			log.debug("Error inside class EmployeePerformanceReviewCycleServiceImpl method getValueforEmployeeReview :{} :{} ",e.getMessage(),
					Utils.getStackTrace(e));
		throw new BusinessException();
		}
		
	}
	

	public static LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}

}
