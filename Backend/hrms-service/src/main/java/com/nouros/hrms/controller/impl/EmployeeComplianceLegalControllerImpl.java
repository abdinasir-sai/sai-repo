package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeComplianceLegalController;
import com.nouros.hrms.model.EmployeeComplianceLegal;
import com.nouros.hrms.service.EmployeeComplianceLegalService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This class represents the implementation of the
 * EmployeeComplianceLegalController interface. It is annotated with the Spring
 * annotations @RestController, @RequestMapping and @Primary to indicate that it
 * is a Spring-managed bean and should be used as the primary implementation of
 * the EmployeeComplianceLegalController. It is also annotated with @Api to
 * provide metadata for the Swagger documentation. The class also uses
 * Lombok's @Slf4j annotation to automatically generate a logger field named
 * "log" that is used to log method calls and results. The class fields include
 * an instance of the EmployeeComplianceLegalService bean, which is injected by
 * Spring using the @Autowired annotation. The class implements the following
 * methods: create(EmployeeComplianceLegal EmployeeComplianceLegal): creates an
 * EmployeeComplianceLegal and returns the newly created
 * EmployeeComplianceLegal. count(String filter): returns the number of
 * EmployeeComplianceLegal that match the specified filter. search(String
 * filter, Integer offset, Integer size, String orderBy, String orderType):
 * returns a list of EmployeeComplianceLegal that match the specified filter,
 * sorted according to the specified orderBy and orderType.
 * update(EmployeeComplianceLegal EmployeeComplianceLegal): updates an
 * EmployeeComplianceLegal and returns the updated EmployeeComplianceLegal.
 * 
 * importData(MultipartFile excelFile): importing data from excel sheet
 * export(String filter, Integer offset, Integer size, String orderBy, String
 * orderType): export the data to excel sheet downloadTemplate(String fileName):
 * download excel sheet template auditHistory(int id, Integer limit, Integer
 * skip): return AuditHistory of EmployeeComplianceLegal with id and limit and
 * skip
 */
@Primary
@RestController
@RequestMapping("/EmployeeComplianceLegal")
//@Tag(name="/EmployeeComplianceLegal",tags="EmployeeComplianceLegal",description="EmployeeComplianceLegal")
public class EmployeeComplianceLegalControllerImpl implements EmployeeComplianceLegalController {

	private static final Logger log = LogManager.getLogger(EmployeeComplianceLegalControllerImpl.class);

	@Autowired
	private EmployeeComplianceLegalService employeeComplianceLegalService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE EMPLOYEE COMPLIANCE LEGAL")
	public EmployeeComplianceLegal create(EmployeeComplianceLegal employeeComplianceLegal) {
		log.info("inside @class EmployeeComplianceLegalControllerImpl @method create");
		return employeeComplianceLegalService.create(employeeComplianceLegal);
	}

	@Override
	public Long count(String filter) {
		return employeeComplianceLegalService.count(filter);
	}

	@Override
	public List<EmployeeComplianceLegal> search(String filter, Integer offset, Integer size, String orderBy,
			String orderType) {
		return employeeComplianceLegalService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE COMPLIANCE LEGAL")
	public EmployeeComplianceLegal update(EmployeeComplianceLegal employeeComplianceLegal) {
		return employeeComplianceLegalService.update(employeeComplianceLegal);
	}

	@Override
	public EmployeeComplianceLegal findById(Integer id) {
		return employeeComplianceLegalService.findById(id);
	}

	@Override
	public List<EmployeeComplianceLegal> findAllById(List<Integer> id) {
		return employeeComplianceLegalService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE EMPLOYEE COMPLIANCE LEGAL BY ID")
	public void deleteById(Integer id) {
		employeeComplianceLegalService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "BULK DELETE EMPLOYEE COMPLIANCE LEGAL")
	public void bulkDelete(List<Integer> list) {
		employeeComplianceLegalService.softBulkDelete(list);
	}

	@Override
	public EmployeeComplianceLegal getSelfEmployeeComplianceLegal(Integer id, Integer userId) {
		return employeeComplianceLegalService.getSelfEmployeeComplianceLegal(id, userId);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE COMPLIANCE LEGAL")
	public EmployeeComplianceLegal updateEmployeeComplianceLegal(EmployeeComplianceLegal employeeComplianceLegal) {
		return employeeComplianceLegalService.updateEmployeeComplianceLegal(employeeComplianceLegal);
	}
}
