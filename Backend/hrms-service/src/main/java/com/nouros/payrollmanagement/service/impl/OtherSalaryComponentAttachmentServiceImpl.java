package com.nouros.payrollmanagement.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.core.generic.dao.GenericRepository;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.product.storagesystem.rest.StorageRest;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.model.OtherSalaryComponentAttachment;
import com.nouros.payrollmanagement.repository.OtherSalaryComponentAttachmentRepository;
import com.nouros.payrollmanagement.repository.OtherSalaryComponentRepository;
import com.nouros.payrollmanagement.service.OtherSalaryComponentAttachmentService;
import com.nouros.payrollmanagement.service.OtherSalaryComponentService;

@Service
public class OtherSalaryComponentAttachmentServiceImpl implements OtherSalaryComponentAttachmentService{

	private static final Logger log = LogManager.getLogger(EmployeeMonthlySalaryServiceImpl.class);

	    @Autowired
	    private OtherSalaryComponentAttachmentRepository otherSalaryComponentAttachmentRepository;
	    
	    @Autowired
	    private OtherSalaryComponentRepository otherSalaryComponentRepository;
	    
	    @Autowired
	    private OtherSalaryComponentService otherSalaryComponentService;

		@Autowired
	    private StorageRest storageRest;
		
//		public OtherSalaryComponentAttachmentServiceImpl(GenericRepository<OtherSalaryComponentAttachment> repository) {
//			super(repository,OtherSalaryComponentAttachment.class);
//		}

		@Override
public OtherSalaryComponent upload(MultipartFile file, Integer id) {
    String fileName = "Ocm_" + id + "_" + System.currentTimeMillis() + ".xlsx";
    String rootDir = "hrms";
    String filePath = "hrmsocm/" + fileName;

    try (InputStream inputStream = file.getInputStream()) {
        uploadFileInStorage(inputStream, fileName, filePath, rootDir);
		log.debug("Inside inputStream :: {}", inputStream);
        OtherSalaryComponent optionalOtherSalaryComponent = otherSalaryComponentService.findById(id);
        if (optionalOtherSalaryComponent != null) {
            OtherSalaryComponent otherSalaryComponent = optionalOtherSalaryComponent;
            log.debug("Inside otherSalaryComponent :: {}", otherSalaryComponent);
            OtherSalaryComponentAttachment attachment = new OtherSalaryComponentAttachment();
			log.debug("Inside attachment :: {}", attachment);
            attachment.setOtherSalaryComponent(otherSalaryComponent);
            attachment.setFilePath(filePath);
			
            otherSalaryComponentAttachmentRepository.save(attachment);
			log.debug("Inside attachment :: {}", attachment);
            return otherSalaryComponent;
        } else {
            throw new BusinessException("OtherSalaryComponent not found with ID: " + id);
        }
    } catch (IOException e) {
        e.printStackTrace();
        throw new BusinessException("Error while converting file to InputStream", e);
    }
}

private void uploadFileInStorage(InputStream in, String fileName, String filePath, String rootDir) throws IOException {
    log.debug("Inside uploadFileInStorage :: {}", fileName);
    log.debug("fileName: {} filePath: {}", fileName, filePath); 

    try (InputStream inputStream = new ByteArrayInputStream(in.readAllBytes())) {
        storageRest.createFile(inputStream, rootDir, filePath);
    }
}
		

}
