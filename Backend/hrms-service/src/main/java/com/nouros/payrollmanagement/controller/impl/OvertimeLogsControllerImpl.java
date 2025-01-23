package com.nouros.payrollmanagement.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.payrollmanagement.controller.OvertimeLogsController;
import com.nouros.payrollmanagement.model.OvertimeLogs;
import com.nouros.payrollmanagement.service.OvertimeLogsService;
import com.nouros.payrollmanagement.wrapper.OvertimeDto;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/OvertimeLogs")
public class OvertimeLogsControllerImpl implements OvertimeLogsController{

	@Autowired
	private OvertimeLogsService overtimeLogsService;
	
	private static final Logger log = LogManager.getLogger(OvertimeLogsControllerImpl.class);

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public OvertimeLogs create(@Valid OvertimeLogs overtimeLogs) {
		
		return overtimeLogsService.create(overtimeLogs);
	}

	@Override
	public OvertimeLogs findById(@Valid Integer id) {
	
		return overtimeLogsService.findById(id);
	}

	@Override
	public List<OvertimeLogs> findAllById(@Valid List<Integer> id) {
		
		return overtimeLogsService.findAllById(id);
	}
	

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(@Valid Integer id) {
		  
		overtimeLogsService.deleteById(id);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public OvertimeLogs update (OvertimeLogs overtimeLogs) {
		return overtimeLogsService.update(overtimeLogs);
	}
	
	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(@Valid List<Integer> list) {
		overtimeLogsService.softBulkDelete(list);
		
	}

	@Override
	public Long count(String filter) {
		return overtimeLogsService.count(filter);
	}

	@Override
	public List<OvertimeLogs> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		 
		return overtimeLogsService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public ResponseEntity<OvertimeDto> createOrUpdateOvertimeLogs(@Valid OvertimeDto overtimeDto) {
		
		return ResponseEntity.ok(overtimeLogsService.createOrUpdateOvertimeLogs(overtimeDto,overtimeDto.getMonth(),overtimeDto.getYear()));
		
	}

  
	
}
	

