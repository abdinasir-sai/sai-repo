package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.nouros.hrms.model.InterviewFeedback;
import com.nouros.hrms.repository.InterviewFeedbackRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.InterviewFeedbackService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.payrollmanagement.utils.CalculationUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class InterviewFeedbackServiceImpl extends AbstractService<Integer,InterviewFeedback>
		implements InterviewFeedbackService {

	private static final Logger log = LogManager.getLogger(InterviewFeedbackServiceImpl.class);

	public static final String INSIDE_METHOD = "Inside @method {}";
	public static final String INSIDE_METHOD_ONE_PARAMETER = "Inside @method {} {}";

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;
	
	@Autowired
	private CommonUtils commonUtils;
	
	@Autowired
	private CalculationUtils calculationUtils;

	public InterviewFeedbackServiceImpl(GenericRepository<InterviewFeedback> repository) {
		super(repository, InterviewFeedback.class);
	}

	@Autowired
	private InterviewFeedbackRepository interviewFeedbackRepository;

	@Override
	public void softDelete(int id) {
		interviewFeedbackRepository.deleteById(id);
	}

	@Override
	public void softBulkDelete(List<Integer> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				interviewFeedbackRepository.deleteById(list.get(i));
			}
		}
	}

	@Override
	public InterviewFeedback create(InterviewFeedback interviewFeedback) {
		log.debug(INSIDE_METHOD, "create");
		return interviewFeedbackRepository.save(interviewFeedback);
	}

	@Override
	public List<InterviewFeedback> findAllByInterviewId(Integer interviewId) {
		log.debug(INSIDE_METHOD, "findAllByInterviewId");
		List<InterviewFeedback> interviewFeedbackLists = new ArrayList<>();
		try {
			log.debug(" Inside @findAllByInterviewId  customerId is : {}", commonUtils.getCustomerId());
			interviewFeedbackLists = interviewFeedbackRepository.findAllByInterviewId(interviewId);
			log.debug("interviewFeedbackLists fetched on basis of interviewId are {} ", interviewFeedbackLists);
		} catch (Exception e) {
			log.error(" findAllByInterviewId : {}", e.getMessage());
		}
		return interviewFeedbackLists;
	}

	@Override
	public InterviewFeedback setAverageInterviewScore(Integer interviewFeedbackId, List<Integer> scoreList) {
		log.debug(INSIDE_METHOD, "setAverageInterviewScore interviewId: {} ,scoreList: {} ",interviewFeedbackId,scoreList);
		
		InterviewFeedback interviewFeedback = null;
		Double averageScore = 0.0;
		try {
			averageScore =commonUtils.getAverageInterviewScore(scoreList);
			log.debug(INSIDE_METHOD, "setAverageInterviewScore averageScore: {} ",averageScore);
			Double formatAverageScore = calculationUtils.formatDouble(averageScore);
			log.debug(INSIDE_METHOD, "setAverageInterviewScore formatAverageScore: {} ",formatAverageScore);
			Optional<InterviewFeedback> optionalInterviewFeedback = interviewFeedbackRepository.findById(interviewFeedbackId);
	        if (optionalInterviewFeedback.isPresent()) {
	             interviewFeedback = optionalInterviewFeedback.get();
	            log.debug("setAverageInterviewScore fetched InterviewFeedback: {}", interviewFeedback);
	            interviewFeedback.setOverallAverageScore(formatAverageScore);
	            interviewFeedbackRepository.save(interviewFeedback);
	            log.debug("setAverageInterviewScore saved InterviewFeedback: {}", interviewFeedback);

	            return interviewFeedback;
	           }
		}
		catch(Exception e) {
			log.error("Error occured while setting average score : {} ",e);
		}
		
		return interviewFeedback;
	}


	
}
