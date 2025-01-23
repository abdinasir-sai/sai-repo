package com.nouros.hrms.service;

import java.util.List;

import org.json.JSONObject;

import com.nouros.hrms.model.Designation;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * DesignationService interface is a service layer interface which handles all
 * the business logic related to Designation model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Designation
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface DesignationService extends GenericService<Integer, Designation> {

	public List<Designation> getDesignationForSuccession();

	JSONObject getJsonObjectForDesignation(String designationName);

	public Designation setJobLevelForDesignationRecursively(String designationName);
}
