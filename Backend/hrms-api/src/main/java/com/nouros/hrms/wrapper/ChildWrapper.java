package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ChildWrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	 String name;
	 Double amount;
	 Date dateOfBirth;
	 
		
}
