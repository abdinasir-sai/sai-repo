package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.AppraisalCyclesController;
import com.nouros.hrms.model.AppraisalCycles;
import com.nouros.hrms.service.AppraisalCyclesService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This class represents the implementation of the AppraisalCyclesController
 * interface. It is annotated with the Spring
 * annotations @RestController, @RequestMapping and @Primary to indicate that it
 * is a Spring-managed bean and should be used as the primary implementation of
 * the AppraisalCyclesController. It is also annotated with @Api to provide
 * metadata for the Swagger documentation. The class also uses Lombok's @Slf4j
 * annotation to automatically generate a logger field named "log" that is used
 * to log method calls and results. The class fields include an instance of the
 * AppraisalCyclesService bean, which is injected by Spring using the @Autowired
 * annotation. The class implements the following methods:
 * create(AppraisalCycles AppraisalCycles): creates an AppraisalCycles and
 * returns the newly created AppraisalCycles. count(String filter): returns the
 * number of AppraisalCycles that match the specified filter. search(String
 * filter, Integer offset, Integer size, String orderBy, String orderType):
 * returns a list of AppraisalCycles that match the specified filter, sorted
 * according to the specified orderBy and orderType. update(AppraisalCycles
 * AppraisalCycles): updates an AppraisalCycles and returns the updated
 * AppraisalCycles.
 * 
 * importData(MultipartFile excelFile): importing data from excel sheet
 * export(String filter, Integer offset, Integer size, String orderBy, String
 * orderType): export the data to excel sheet downloadTemplate(String fileName):
 * download excel sheet template auditHistory(int id, Integer limit, Integer
 * skip): return AuditHistory of AppraisalCycles with id and limit and skip
 */
@Primary
@RestController
@RequestMapping("/AppraisalCycles")

//@Tag(name="/AppraisalCycles",tags="AppraisalCycles",description="AppraisalCycles")
public class AppraisalCyclesControllerImpl implements AppraisalCyclesController {

	private static final Logger log = LogManager.getLogger(AppraisalCyclesControllerImpl.class);

	
	@Autowired
	private AppraisalCyclesService appraisalCyclesService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public AppraisalCycles create(AppraisalCycles appraisalCycles) {
		log.info("inside @class AppraisalCyclesControllerImpl @method create");
		return appraisalCyclesService.create(appraisalCycles);
	}

	@Override
	public Long count(String filter) {
		return appraisalCyclesService.count(filter);
	}

	@Override
	public List<AppraisalCycles> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return appraisalCyclesService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public AppraisalCycles update(AppraisalCycles appraisalCycles) {
		return appraisalCyclesService.update(appraisalCycles);
	}

	@Override
	public AppraisalCycles findById(Integer id) {
		return appraisalCyclesService.findById(id);
	}

	@Override
	public List<AppraisalCycles> findAllById(List<Integer> id) {
		return appraisalCyclesService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		appraisalCyclesService.deleteById(id);
	}

}
