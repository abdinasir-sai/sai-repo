package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.AppraisalTemplatesController;
import com.nouros.hrms.model.AppraisalTemplates;
import com.nouros.hrms.service.AppraisalTemplatesService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This class represents the implementation of the AppraisalTemplatesController
 * interface. It is annotated with the Spring
 * annotations @RestController, @RequestMapping and @Primary to indicate that it
 * is a Spring-managed bean and should be used as the primary implementation of
 * the AppraisalTemplatesController. It is also annotated with @Api to provide
 * metadata for the Swagger documentation. The class also uses Lombok's @Slf4j
 * annotation to automatically generate a logger field named "log" that is used
 * to log method calls and results. The class fields include an instance of the
 * AppraisalTemplatesService bean, which is injected by Spring using
 * the @Autowired annotation. The class implements the following methods:
 * create(AppraisalTemplates AppraisalTemplates): creates an AppraisalTemplates
 * and returns the newly created AppraisalTemplates. count(String filter):
 * returns the number of AppraisalTemplates that match the specified filter.
 * search(String filter, Integer offset, Integer size, String orderBy, String
 * orderType): returns a list of AppraisalTemplates that match the specified
 * filter, sorted according to the specified orderBy and orderType.
 * update(AppraisalTemplates AppraisalTemplates): updates an AppraisalTemplates
 * and returns the updated AppraisalTemplates.
 * 
 * importData(MultipartFile excelFile): importing data from excel sheet
 * export(String filter, Integer offset, Integer size, String orderBy, String
 * orderType): export the data to excel sheet downloadTemplate(String fileName):
 * download excel sheet template auditHistory(int id, Integer limit, Integer
 * skip): return AuditHistory of AppraisalTemplates with id and limit and skip
 */
@Primary
@RestController
@RequestMapping("/AppraisalTemplates")

//@Tag(name="/AppraisalTemplates",tags="AppraisalTemplates",description="AppraisalTemplates")
public class AppraisalTemplatesControllerImpl implements AppraisalTemplatesController {

	private static final Logger log = LogManager.getLogger(AppraisalTemplatesControllerImpl.class );

	@Autowired
	private AppraisalTemplatesService appraisalTemplatesService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public AppraisalTemplates create(AppraisalTemplates appraisalTemplates) {
		log.info("inside @class AppraisalTemplatesControllerImpl @method create");
		return appraisalTemplatesService.create(appraisalTemplates);
	}

	@Override
	public Long count(String filter) {
		return appraisalTemplatesService.count(filter);
	}

	@Override
	public List<AppraisalTemplates> search(String filter, Integer offset, Integer size, String orderBy,
			String orderType) {
		return appraisalTemplatesService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public AppraisalTemplates update(AppraisalTemplates appraisalTemplates) {
		return appraisalTemplatesService.update(appraisalTemplates);
	}

	@Override
	public AppraisalTemplates findById(Integer id) {
		return appraisalTemplatesService.findById(id);
	}

	@Override
	public List<AppraisalTemplates> findAllById(List<Integer> id) {
		return appraisalTemplatesService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		appraisalTemplatesService.deleteById(id);
	}

	

}
