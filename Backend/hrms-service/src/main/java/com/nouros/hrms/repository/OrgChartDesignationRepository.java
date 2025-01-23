package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.OrgChartDesignation;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface OrgChartDesignationRepository extends GenericRepository<OrgChartDesignation> {

	@Query(value = "SELECT * FROM ORG_CHART_DESIGNATION WHERE PARENT_DESIGNATION = :parentId AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<OrgChartDesignation> findAllByParentDesignation(@Param("parentId") Integer parentId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM ORG_CHART_DESIGNATION WHERE DEPARTMENT_ID = :departmentId AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<OrgChartDesignation> findByDepartmentId(@Param("departmentId") Integer departmentId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM ORG_CHART_DESIGNATION WHERE ORGCHART_ID = :id AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<OrgChartDesignation> findByPlannedOrgChartId(@Param("id") Integer id, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM ORG_CHART_DESIGNATION WHERE PARENT_DESIGNATION = :id AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Designation findOrgChartDesignationByParentDesignationId(@Param("id") Integer id, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM ORG_CHART_DESIGNATION WHERE DEPARTMENT_ID = :departmentId AND  CREATOR = :userId AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<OrgChartDesignation> findOrgCharDesignationByDepartmentAndUserId(@Param("departmentId") Integer departmentId,
			@Param("userId") Integer userId, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM ORG_CHART_DESIGNATION WHERE ORGCHART_ID = :id AND CREATOR = :userId AND DELETED = 0 AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<OrgChartDesignation> findByPlannedOrgChartIdAndUserId(@Param("id") Integer id,@Param("userId") Integer userId, @Param("customerId") Integer customerId);

}
