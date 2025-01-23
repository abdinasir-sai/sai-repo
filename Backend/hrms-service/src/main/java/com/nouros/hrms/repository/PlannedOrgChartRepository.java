package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.PlannedOrgChart;
import com.nouros.hrms.repository.generic.GenericRepository;

import jakarta.validation.Valid;

@Repository
@Transactional(readOnly = true)
public interface PlannedOrgChartRepository extends GenericRepository<PlannedOrgChart> {

	@Query(value = "SELECT * FROM PLANNED_ORG_CHART WHERE DEPARTMENT_ID = :departmentId AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<PlannedOrgChart> findAllByDepartmentId(@Param("departmentId") Integer departmentId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT COUNT(*) FROM PLANNED_ORG_CHART " + "WHERE DEPARTMENT_ID = :departmentId AND CUSTOMER_ID = :customerId"
			+ "AND WORKFLOW_STAGE != 'Approved' " + "AND DELETED = 0", nativeQuery = true)
	int countByDepartmentIdAndNotApproved(@Param("departmentId") int departmentId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT COUNT(*) FROM PLANNED_ORG_CHART WHERE DEPARTMENT_ID = :departmentId AND CUSTOMER_ID = :customerId", nativeQuery = true)
	int countPlannedOrgChartDepartmentId(@Param("departmentId") int departmentId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM PLANNED_ORG_CHART WHERE ID = :plannedOrgChartId AND CUSTOMER_ID = :customerId", nativeQuery = true)
	PlannedOrgChart findPlannedOrgChartById(@Param("plannedOrgChartId") Integer plannedOrgChartId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM PLANNED_ORG_CHART WHERE PROCESS_INSTANCE_ID = :processInstanceId AND CUSTOMER_ID = :customerId", nativeQuery = true)
	PlannedOrgChart findPlannedOrgChartByProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT COUNT(*) FROM PLANNED_ORG_CHART WHERE DEPARTMENT_ID = :departmentId AND CREATOR = :userId AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	int countPlannedOrgChartDepartmentIdAndUser(@Param("departmentId") Integer departmentId,
			@Param("userId") Integer userId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM PLANNED_ORG_CHART WHERE DEPARTMENT_ID = :departmentId AND CREATOR = :userId AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	PlannedOrgChart findByDepartmentIdAndUserId(@Param("departmentId") Integer departmentId,
			@Param("userId") Integer userId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM PLANNED_ORG_CHART WHERE ID = :plannedOrgChartId AND CREATOR = :userId AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	PlannedOrgChart findPlannedOrgChartByIdAndUser(@Param("plannedOrgChartId") Integer plannedOrgChartId,
			@Param("userId") Integer userId, @Param("customerId") Integer customerId);
}
