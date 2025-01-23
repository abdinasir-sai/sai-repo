package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Designation;
import com.nouros.hrms.repository.generic.GenericRepository;

/**
 * 
 * The DesignationRepository interface is a extension of
 * {@link GenericRepository}. It's purpose is to provide additional methods that
 * are specific to the {@link Designation} entity. see GenericRepository see
 * JpaRepository
 */
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface DesignationRepository extends GenericRepository<Designation> {

	@Query(value = "SELECT * FROM DESIGNATION WHERE NAME = :designationName  AND DEPARTMENT_ID = :departmentId AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Designation findDesignationByNameAndDepartmentId(@Param("designationName") String designationName,
			@Param("departmentId") int departmentId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM DESIGNATION WHERE NAME = :designationName AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<Designation> findByName(@Param("designationName") String designationName, @Param("customerId") Integer customerId);
	
	@Query(value = "SELECT * FROM DESIGNATION WHERE NAME = :designationName AND WORKFLOW_STAGE = 'Approved' AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Designation findByNameAndApprovedWorkFlowStage(@Param("designationName") String designationName, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM DESIGNATION WHERE ID =:id AND CUSTOMER_ID = :customerId",nativeQuery = true)
	Designation findByDesignationId(@Param("id") Integer id, @Param("customerId") Integer customerId);
	
	@Query("SELECT d FROM Designation d WHERE d.workflowStage=:workflowStage ")
	List<Designation> getDesignationListByWorkflowStage(@Param("workflowStage")String workflowStage);
	
	@Query(value = "SELECT * FROM DESIGNATION WHERE NAME = :designationName AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Designation findByDesignationName(@Param("designationName") String designationName, @Param("customerId") Integer customerId);

}
