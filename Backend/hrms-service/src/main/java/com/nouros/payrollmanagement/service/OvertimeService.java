package com.nouros.payrollmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Employee;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.Overtime;
import com.nouros.payrollmanagement.model.OvertimeLogs;
import com.nouros.payrollmanagement.wrapper.OvertimeDto;

public interface OvertimeService extends GenericService<Integer, Overtime> {

	void softDelete(int id);
	
	void softBulkDelete(List<Integer> list);
	
	//@Transactional(propagation=Propagation.REQUIRES_NEW)
	Overtime create(Overtime overtime);
	 
	List<Overtime> getListOfOvertimeByEmployeeId(Integer employeeId);
	
	OvertimeDto getOvertimeDetails(Integer empId,Integer overtimeId, Integer month , Integer year);
	 
	Overtime createOvertime(Employee employee, String overtimeStatus, Integer month , Integer year , List<OvertimeLogs> overtimeLogsList);
    
	Overtime updateOvertimeDetails(Overtime overtime,String overtimeStatus, Integer month , Integer year, List<OvertimeLogs> overtimeLogsList);
    
	Overtime updateWithBpmn(Overtime overtime);
	
	String deleteOvertimeLogsAndOvertimeId(Integer id);
	
	

}
