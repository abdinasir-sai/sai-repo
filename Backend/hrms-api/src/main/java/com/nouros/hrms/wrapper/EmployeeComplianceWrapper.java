package com.nouros.hrms.wrapper;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmployeeComplianceWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String codeOfConduct;
	private String companyAssetAgreement;
	private String conflictOfInterest;
	private String cyberCompliance;
	private String employmentContract;
	private String nationalAddressCertificate;
	private String ibanCertificate;
	private String nationalIdentificationNumber;
	private String nationalIdentificationNumberPassport;
}
