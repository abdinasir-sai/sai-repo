package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.TodoBucketController;
import com.nouros.hrms.model.TodoBucket;
import com.nouros.hrms.service.TodoBucketService;
import com.nouros.hrms.wrapper.ToDoBucketDto;

@Primary
@RestController
@RequestMapping("/TodoBucket")
public class TodoBucketControllerImpl implements TodoBucketController {

	private static final Logger log = LogManager.getLogger(TodoBucketControllerImpl.class);

	@Autowired
	private TodoBucketService todoBucketService;

	@Override
	// @TriggerBPMN(entityName = "TodoBucket", appName = "HRMS_APP_NAME")
	public TodoBucket create(TodoBucket todoBucket) {
		log.info("inside @class TodoBucketControllerImpl @method create");
		return todoBucketService.create(todoBucket);
	}

	@Override
	public Long count(String filter) {
		return todoBucketService.count(filter);
	}

	@Override
	public List<TodoBucket> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return todoBucketService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public TodoBucket update(TodoBucket todoBucket) {
		return todoBucketService.update(todoBucket);
	}

	@Override
	public TodoBucket findById(Integer id) {
		return todoBucketService.findById(id);
	}

	@Override
	public List<TodoBucket> findAllById(List<Integer> id) {
		return todoBucketService.findAllById(id);
	}

	@Override
	public List<TodoBucket> findAllBuckets() {
		return todoBucketService.findAllBuckets();
	}

	@Override
	public void deleteById(Integer id) {
		todoBucketService.deleteById(id);
	}

	@Override
	public String migrateAndDeleteBucket(ToDoBucketDto toDoBucketDto) {
		return todoBucketService.migrateAndDeleteBucket(toDoBucketDto);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE TODO BUCKET  & TODO ASSIGNMENT BY BUCKET ID")
	public String deleteTodoAssignmentAndBucketById(Integer id) {
		return todoBucketService.deleteTodoAssignmentAndBucketById(id);
	}

}
