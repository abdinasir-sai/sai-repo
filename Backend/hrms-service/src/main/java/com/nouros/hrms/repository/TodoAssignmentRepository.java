package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.TodoAssignment;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface TodoAssignmentRepository extends GenericRepository<TodoAssignment>{

    @Query(value="select * from TO_DO_ASSIGNMENT where BUCKET_ID = :bucketId AND CUSTOMER_ID=:customerId",nativeQuery=true)
	List<TodoAssignment> getByTodoBucketId(@Param("bucketId") Integer bucketId, @Param("customerId") Integer customerId
			 );

    @Modifying
    @Query(value="delete from TO_DO_ASSIGNMENT where BUCKET_ID = :id AND CUSTOMER_ID=:customerId",nativeQuery=true)
	void deleteAllTodoAssignmentsAssociatedWithBucket(@Param("id") Integer id, @Param("customerId") Integer customerId
			 );

    @Modifying
    @Query(value="delete from USER_TO_DO_ASSIGNMENT_MAPPING where TO_DO_ASSIGNMENT_ID = :id AND CUSTOMER_ID = :customerId",nativeQuery=true)
	void deleteAllUserMappingsAssociatedWithTodoAssignment(@Param("id") Integer id, @Param("customerId") Integer customerId
			);

}
