package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nouros.hrms.wrapper.EmployeeChildrenWrapper;

import lombok.Data;
  
@Data
public class BenefitWrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	Integer userId;
	String benefitType;
	Double amount;
	Date schoolYear;
	List<EmployeeChildrenWrapper> childObj;
	public List<EmployeeChildrenWrapper> getChildObj() {
		return childObj;
	}
	public void setChildObj(List<EmployeeChildrenWrapper> childObj) {
		this.childObj = childObj;
	}
	
	 
	
}
