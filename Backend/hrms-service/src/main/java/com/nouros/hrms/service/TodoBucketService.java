package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.TodoBucket;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.ToDoBucketDto;

public interface TodoBucketService extends GenericService<Integer,TodoBucket>{
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public TodoBucket create(TodoBucket todoBucket);

	String migrateAndDeleteBucket(ToDoBucketDto toDoBucketDto);

	List<TodoBucket> findAllBuckets();

	String deleteTodoAssignmentAndBucketById(Integer id);

}
