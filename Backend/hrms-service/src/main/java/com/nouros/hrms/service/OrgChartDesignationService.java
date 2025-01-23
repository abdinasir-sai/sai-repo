package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.OrgChartDesignation;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.DesignationSummaryWrapper;

public interface OrgChartDesignationService extends GenericService<Integer,OrgChartDesignation> {

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public OrgChartDesignation create(OrgChartDesignation orgChartDesignation);

	public OrgChartDesignation update(OrgChartDesignation orgChartDesignation);

	String deleteOrgChartDesignationById(Integer id);

	String deleteOrgChartDesignationByDepartmentId(Integer departmentId);

	String generateJobTitleDescriptionByUserInput(DesignationSummaryWrapper designationSummaryWrapper);

	List<Employee> getEmployeeByApprovedDesignation(Integer id);

}
