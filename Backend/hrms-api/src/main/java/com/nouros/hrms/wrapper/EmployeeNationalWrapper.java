package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class EmployeeNationalWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String identificationNumber;
	private String borderNumber;
	private String type;
	private Date expiryDate;
	private String scannedImage;
}
