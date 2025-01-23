package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.TodoBucket;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface TodoBucketRepository extends GenericRepository<TodoBucket> {
	
	@Query(value = " SELECT tdb.* FROM TO_DO_BUCKET tdb WHERE tdb.CREATOR =?1 AND tdb.CUSTOMER_ID=?2 UNION  SELECT tdb.* FROM TO_DO_BUCKET tdb JOIN TO_DO_ASSIGNMENT tda ON tdb.ID = tda.BUCKET_ID JOIN USER_TO_DO_ASSIGNMENT_MAPPING utam ON tda.ID = utam.TO_DO_ASSIGNMENT_ID WHERE utam.USER_ID_PK =?1 OR tda.CREATOR =?1 AND tda.CUSTOMER_ID=?2 ", nativeQuery = true)
	List<TodoBucket> findAllBuckets(@Param("userId")Integer userId,  @Param("customerId") Integer customerId);

}
