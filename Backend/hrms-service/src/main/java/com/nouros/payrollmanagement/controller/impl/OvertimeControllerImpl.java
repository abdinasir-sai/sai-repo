package com.nouros.payrollmanagement.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.payrollmanagement.controller.OvertimeController;
import com.nouros.payrollmanagement.model.Overtime;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.service.OvertimeService;
import com.nouros.payrollmanagement.wrapper.OvertimeDto;

import jakarta.validation.Valid;


@Primary
@RestController
@RequestMapping("/Overtime")
public class OvertimeControllerImpl implements OvertimeController{

	@Autowired
	private OvertimeService overtimeService;
	

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public Overtime create(@Valid Overtime overtime) {
		return overtimeService.create(overtime);
	}

	@Override
	public Overtime findById(@Valid Integer id) {
	
		return overtimeService.findById(id);
	}


	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(@Valid Integer id) {
		 
		overtimeService.deleteById(id);
	}

	@Override
	 @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public Overtime update(Overtime Overtime) {
		return overtimeService.update(Overtime);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(@Valid List<Integer> list) {
		overtimeService.softBulkDelete(list);
		
	}

	@Override
	public Long count(String filter) {
		return overtimeService.count(filter);
	}

	@Override
	public List<Overtime> findAllById(@Valid List<Integer> id) {
		
		return overtimeService.findAllById(id);
	}

	@Override
	public List<Overtime> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		return overtimeService.search(filter, offset, size, orderBy, orderType);
	}
	
	@Override 
    public ResponseEntity<OvertimeDto>  getOvertimeDetails(Integer empId, Integer overtimeId, Integer month, Integer year){
		
    	return ResponseEntity.ok(overtimeService.getOvertimeDetails(empId, overtimeId, month, year));
    }

	@Override
	public String deleteOvertimeLogsAndOvertimeById(@Valid Integer id) {
		
		return overtimeService.deleteOvertimeLogsAndOvertimeId(id);
	}
	
	


	
}
