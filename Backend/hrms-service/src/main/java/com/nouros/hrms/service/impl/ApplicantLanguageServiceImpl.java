package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.nouros.hrms.model.ApplicantLanguage;
import com.nouros.hrms.repository.ApplicantLanguageRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantLanguageService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;


@Service
public class ApplicantLanguageServiceImpl extends AbstractService<Integer,ApplicantLanguage> implements ApplicantLanguageService {

	 private static final List<String> VALID_LANGUAGE_LEVELS = Arrays.asList(
		        "Beginner", "Intermediate", "Professional", "Native"
		    );
	
	public ApplicantLanguageServiceImpl(GenericRepository<ApplicantLanguage> repository) {
		super(repository, ApplicantLanguage.class);
	}
	
	@Autowired
	private ApplicantLanguageRepository applicantLanguageRepository;
	
	@Autowired
	private CommonUtils commonUtils;

	private static final Logger log = LogManager.getLogger(ApplicantLanguageServiceImpl.class);

	@Override
	public ApplicantLanguage create(ApplicantLanguage applicantLanguage) {
		log.info("inside @class ApplicantLanguageServiceImpl @method create");
		return applicantLanguageRepository.save(applicantLanguage);
	}
	
	@Override
	public void softDelete(int id) {

		ApplicantLanguage applicantLanguage = super.findById(id);

		if (applicantLanguage != null) {

			ApplicantLanguage applicantLanguage1 = applicantLanguage;
			applicantLanguageRepository.save(applicantLanguage1);

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
	public List<ApplicantLanguage> createApplicantLanguagefromPrompt(List<Map<String,String>> applicantLanguageList, Applicant applicant) {
	    log.info("Inside ApplicantLanguageServiceImpl @method createApplicantLanguagefromPropt");

	    List<ApplicantLanguage> savedApplicantLanguages = new ArrayList<>();
	    
	    try {
	        for (Map<String, String> languageMap : applicantLanguageList) {
	            ApplicantLanguage applicantLanguage = new ApplicantLanguage();

	            if (languageMap.containsKey("languageName")) {
	                applicantLanguage.setLanguageName(languageMap.get("languageName"));
	            }
	            
	            String languageLevel = languageMap.get("languageLevel");
                if (languageLevel != null && VALID_LANGUAGE_LEVELS.contains(languageLevel)) {
                    applicantLanguage.setLanguageLevel(languageLevel);
                }	            
	            if(applicant != null) {
	            	applicantLanguage.setApplicantId(applicant);   
		            }
	            
	            log.info("Inside @method createApplicantLanguagefromPropt ApplicantLanguage: {} ",convertObjectToJson(applicantLanguage));
	            savedApplicantLanguages.add(applicantLanguageRepository.save(applicantLanguage));
	        }
	    } catch (Exception e) {
	        log.error("Error while parsing response JSON", e);
	    }
	    
	    return savedApplicantLanguages;
	}

	@Override
	public List<ApplicantLanguage> updateApplicantLanguageAfterResumeDetailsSet(
			List<ApplicantLanguage> applicantLanguageList,Applicant updatedApplicant) {
		
		log.debug("Inside method updateApplicantLanguageAfterResumeDetailsSet applicantLanguageList is : {}",applicantLanguageList);
		
	try {	
List<ApplicantLanguage> savedApplicantLanguage = new ArrayList<>();
		
		if (applicantLanguageList != null && !applicantLanguageList.isEmpty()) {
            for (ApplicantLanguage language : applicantLanguageList) {
            	language.setApplicantId(updatedApplicant);
            	savedApplicantLanguage.add(applicantLanguageRepository.save(language));
            }
        }

        return savedApplicantLanguage;
	}catch (Exception e) {
		
		log.error("error while updating applicant language inside updateApplicantLanguageAfterResumeDetailsSet");
	throw new BusinessException("error while updating applicant language",e);
	}
	
	}

	@Override
	public List<ApplicantLanguage> getLanguagesForApplicant(Integer id) {
		log.info("Inside getLanguagesForApplicant");
		try {
			log.debug("Inside getLanguagesForApplicant customerId is : {}", commonUtils.getCustomerId());
			return applicantLanguageRepository.getLanguagesForApplicant(id, commonUtils.getCustomerId());
		} catch (Exception e) {
			log.error("An error occurred while getting ApplicantLanguage  by Applicant  Id : {}", e.getMessage());
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
