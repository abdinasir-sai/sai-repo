package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.nouros.hrms.model.TodoAssignment;
import com.nouros.hrms.model.TodoBucket;
import com.nouros.hrms.repository.TodoAssignmentRepository;
import com.nouros.hrms.repository.TodoBucketRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.TodoAssignmentService;
import com.nouros.hrms.service.TodoBucketService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.ToDoBucketDto;
import com.enttribe.platform.viewbuilder.utils.constants.Constants;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TodoBucketServiceImpl extends AbstractService<Integer,TodoBucket> implements TodoBucketService{
	
	
	public TodoBucketServiceImpl(GenericRepository<TodoBucket> repository) {
		super(repository, TodoBucket.class);
	}
	
	@Autowired
	UserRest userRest;
	
	@Autowired
	CustomerInfo customerInfo;
	
	@Autowired
	  private CommonUtils commonUtils;
	
	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}
	
	@Autowired
	private TodoBucketRepository todoBucketRepository;

	private static final Logger log = LogManager.getLogger(TodoBucketServiceImpl.class);

	@Override
	public TodoBucket create(TodoBucket todoBucket) {
		log.info("inside @class TodoBucketServiceImpl @method create");
		return todoBucketRepository.save(todoBucket);
	}
	
	@Override
	public void softDelete(int id) {

		TodoBucket todoBucket = super.findById(id);

		if (todoBucket != null) {

			TodoBucket todoBucket1 = todoBucket;
			todoBucketRepository.save(todoBucket1);

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
	public String migrateAndDeleteBucket(ToDoBucketDto toDoBucketDto) {
		log.info("inside @class TodoBucketServiceImpl @method migrateAndDeleteBucket");
		TodoAssignmentService todoAssignmentService = ApplicationContextProvider.getApplicationContext()
				.getBean(TodoAssignmentService.class);
		List<TodoAssignment> todoAssignments = todoAssignmentService.getByTodoBucketId(toDoBucketDto.getBucketForDeletion().getId());
		log.debug("List of TodoAssignment Found are : {} " , todoAssignments);
		for(TodoAssignment todoAssignment : todoAssignments)
		{
			todoAssignment.setTodoBucket(toDoBucketDto.getBucketForMigration());
			todoAssignmentService.update(todoAssignment);
		}
		log.info("TodoAssignments Migrated from One Bucket to Another , now  going to Delete the Specific Bucket"
				+ " , with name : {}" , toDoBucketDto.getBucketForDeletion().getName());
		try {
			todoBucketRepository.deleteById(toDoBucketDto.getBucketForDeletion().getId());
			log.info("ToDoBucket  Deleted with Id : {} ", toDoBucketDto.getBucketForDeletion().getId());
			return APIConstants.SUCCESS_JSON;
			}catch(Exception e) {
				log.debug("SomeThing Went Wrong While Deleting Todo Bucket");
				return APIConstants.FAILURE_JSON;
			}
	}

	@Override
	public List<TodoBucket> findAllBuckets() {
		log.info("inside @class TodoBucketServiceImpl @method findAllBuckets");	
		try
		{
			User user =  getUserContext();
			log.debug(" Inside @findAllBuckets  customerId is : {}", commonUtils.getCustomerId());
			return todoBucketRepository.findAllBuckets(user.getUserid(), commonUtils.getCustomerId());
		}catch (EntityNotFoundException ex) {
			throw new BusinessException("No Buckets Found");
		} catch (BusinessException be) {
			throw new BusinessException(be.getMessage());
		} catch (Exception ex) {
			logger.error("Unexpected error occurred while finding Bucket cause:{}", ex);
			throw new BusinessException(Constants.SOMETHING_WENT_WRONG);
		}
	}
	
	
	@Override
	public String deleteTodoAssignmentAndBucketById(Integer id) {
		log.info("Inside method deleteTodoAssignmentAndBucketById With Id : {} ", id);
		try {
			TodoAssignmentRepository todoAssignmentRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(TodoAssignmentRepository.class);
			List<TodoAssignment> assignmentList = todoAssignmentRepository.getByTodoBucketId(id,commonUtils.getCustomerId()); 
			if(assignmentList !=null && !assignmentList.isEmpty())
			{
				for(TodoAssignment todoAssignment : assignmentList)
				{
					todoAssignmentRepository.deleteAllUserMappingsAssociatedWithTodoAssignment(todoAssignment.getId(),commonUtils.getCustomerId());	
				}
			}
			log.info("All TodoAssignment mappings of users Deleted with Buckets Id: {} ", id);
			todoAssignmentRepository.deleteAllTodoAssignmentsAssociatedWithBucket(id,commonUtils.getCustomerId());
			log.info("All TodoAssignment Deleted for Associated TodoBucket with Id : {} ", id);
			todoBucketRepository.deleteById(id);
			log.info("TodoBucket Deleted with Id : {} ", id);
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.debug("SomeThing Went Wrong While Deleting TodoAssignments And TodoBuckets ");
			return APIConstants.FAILURE_JSON;
		}
	}
	
	
	
	
}
