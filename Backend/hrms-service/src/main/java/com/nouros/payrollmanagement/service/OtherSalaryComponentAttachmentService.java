package com.nouros.payrollmanagement.service;

import org.springframework.web.multipart.MultipartFile;

import com.nouros.payrollmanagement.model.OtherSalaryComponent;

public interface OtherSalaryComponentAttachmentService {

	OtherSalaryComponent upload(MultipartFile file, Integer id);

}
