package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.ApplicantCertifications;
import com.nouros.hrms.model.ApplicantEducation;
import com.nouros.hrms.repository.ApplicantCertificationsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantCertificationsService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;



@Service
public class ApplicantCertificationsServiceImpl extends AbstractService<Integer,ApplicantCertifications> implements ApplicantCertificationsService {
	
	public ApplicantCertificationsServiceImpl(GenericRepository<ApplicantCertifications> repository) {
		super(repository, ApplicantCertifications.class);
	}
	
	@Autowired
	private ApplicantCertificationsRepository applicantCertificationsRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	private static final Logger log = LogManager.getLogger(ApplicantCertificationsServiceImpl.class);

	@Override
	public ApplicantCertifications create(ApplicantCertifications applicantCertifications) {
		log.info("inside @class ApplicantCertificationsServiceImpl @method create");
		return applicantCertificationsRepository.save(applicantCertifications);
	}
	
	@Override
	public void softDelete(int id) {

		ApplicantCertifications applicantCertifications = super.findById(id);

		if (applicantCertifications != null) {

			ApplicantCertifications applicantCertifications1 = applicantCertifications;
			applicantCertificationsRepository.save(applicantCertifications1);

		}
	}
	
	
	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}
	
	
	@Override
	public List<ApplicantCertifications> createApplicantCertificationsfromPropt(List<Map<String, String>> applicantCertificationsList, Applicant applicant) {
	    log.info("Inside ApplicantCertificationsServiceImpl @method createApplicantCertificationsfromPropt");

	    log.debug("Applicant Certification List applicantCertificationsList : {}",applicantCertificationsList);
	    
	    List<ApplicantCertifications> savedApplicantCertifications = new ArrayList<>();

	    try {
	        for (Map<String, String> certificationMap : applicantCertificationsList) {
	        	
	        	log.info("iterate for applicantCertificationsList");
	        	
	        	
	            ApplicantCertifications applicantCertification = new ApplicantCertifications();

	            if (certificationMap.containsKey("certificationName")) {
	                applicantCertification.setCertificationName(certificationMap.get("certificationName"));
	            }
	            if (certificationMap.containsKey("issuingInstitution")) {
	                applicantCertification.setIssuingInstitution(certificationMap.get("issuingInstitution"));
	            }
	            if (certificationMap.containsKey("certificationId")) {
	                applicantCertification.setCertificationId(certificationMap.get("certificationId"));
	            }

	            if (applicant != null) {
	                applicantCertification.setApplicantId(applicant);
	            }
	            log.debug("Applicant Certification is : {}",convertObjectToJson(applicantCertification));
	            savedApplicantCertifications.add(applicantCertificationsRepository.save(applicantCertification));
	            
	            log.debug("Applicant Certification set successfully savedApplicantCertifications : {}",savedApplicantCertifications);
	            
	        }
	    } catch (Exception e) {
	        log.error("Error while parsing response JSON for certifications", e);
	    }

	    return savedApplicantCertifications;
	}

	@Override
	public List<ApplicantCertifications> updateApplicantCertificationsAfterResumeDetailsSet(
			List<ApplicantCertifications> applicantCertificationsList,Applicant updatedApplicant) {
		
		log.debug("Inside method updateApplicantCertificationsAfterResumeDetailsSet applicantCertificationsList is : {}",applicantCertificationsList);
		
		try {
List<ApplicantCertifications> savedApplicantCertifications = new ArrayList<>();
		
		if (applicantCertificationsList != null && !applicantCertificationsList.isEmpty()) {
            for (ApplicantCertifications certifications : applicantCertificationsList) {
            	certifications.setApplicantId(updatedApplicant);
                savedApplicantCertifications.add(applicantCertificationsRepository.save(certifications));
            }
        }

        return savedApplicantCertifications;
	}catch (Exception e) {
		
	log.error("unable to update applicant certification inside method  updateApplicantCertificationsAfterResumeDetailsSet");
	throw new BusinessException("unable to update applicant certification",e);
	}
		
	}

	@Override
	public List<ApplicantCertifications> getCertificationsForApplicant(Integer id) {
		log.info("Inside getCertificationsForApplicant");
		try {
			log.debug("Inside getCertificationsForApplicant customerId is : {}", commonUtils.getCustomerId());
			return applicantCertificationsRepository.getCertificationsForApplicant(id, commonUtils.getCustomerId());
		} catch (Exception e) {
			log.error("An error occurred while getting ApplicantCertifications  by Applicant  Id : {}", e.getMessage());
			return null;
		}
	}

	private String convertObjectToJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Error while converting object to JSON", e);
			return "Error converting object to JSON";
		}
	}

}
