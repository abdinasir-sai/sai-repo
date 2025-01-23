package com.nouros.payrollmanagement.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.OvertimeLogs;
import com.nouros.payrollmanagement.wrapper.OvertimeDto;


public interface OvertimeLogsService extends GenericService<Integer, OvertimeLogs>{

	void softDelete(int id);
	
	void softBulkDelete(List<Integer> list);
	
	//@Transactional(propagation=Propagation.REQUIRES_NEW)
	public OvertimeLogs create(OvertimeLogs overtimeLogs);
	
	public List<OvertimeLogs> getOvertimeLogsbyOvertimeIdAndPayrollDate(Integer overtimeId , Date payrollRunStartDate , Date payrollRunEndDate);

	public OvertimeDto createOrUpdateOvertimeLogs(OvertimeDto overtimeDto, Integer month,Integer year);
	 List<OvertimeLogs> getOvertimeLogsForEmployeeByWorkflowStageAndDate(Integer employeeId ,String workflowStage,Date payrollRunStartDate);
}
