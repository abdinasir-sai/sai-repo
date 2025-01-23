package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.EmployeeSuccessor;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.EmployeeSuccessorWrapper;
 
 
public interface EmployeeSuccessorService extends GenericService<Integer , EmployeeSuccessor>{
	List<EmployeeSuccessorWrapper> getPotentialSuccessorList(Integer designationId);
	 //List<Document> searchVector(String embeddingCollection, String query, Integer topRecord, Double threshold, Filter.Expression expression) ;
}
