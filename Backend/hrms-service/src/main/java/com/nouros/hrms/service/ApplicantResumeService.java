package com.nouros.hrms.service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.model.ApplicantResume;
import com.nouros.hrms.service.generic.GenericService;

public interface ApplicantResumeService  extends GenericService<Integer, ApplicantResume> {

    String auditHistory(int id, Integer limit, Integer skip);
	
	String importData(MultipartFile excelFile) throws IOException, InstantiationException, ClassNotFoundException;
	
	 byte[] export(List<ApplicantResume> applicantResume) throws IOException;
	  
	 void bulkDelete(List<Integer> list);

	List<ApplicantResume> findAllApplicantResumeByApplicantId(Integer applicantId);
	 
}
