package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.JobBriefController;
import com.nouros.hrms.model.JobBrief;
import com.nouros.hrms.service.JobBriefService;

@Primary
@RestController
@RequestMapping("/JobBrief")
public class JobBriefControllerImpl implements JobBriefController {

	@Autowired
	private JobBriefService jobBriefService;

	@Override
	@TriggerBPMN(entityName = "JobApplication", appName = "HRMS_APP_NAME")
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public JobBrief create(JobBrief jobBrief) {
		return jobBriefService.create(jobBrief);
	}

	@Override
	public Long count(String filter) {
		return jobBriefService.count(filter);
	}

	@Override
	public List<JobBrief> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return jobBriefService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public JobBrief findById(Integer id) {
		return jobBriefService.findById(id);
	}

	@Override
	public List<JobBrief> findAllById(List<Integer> id) {
		return jobBriefService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		jobBriefService.deleteById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		jobBriefService.bulkDelete(list);
	}

	

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public JobBrief update(JobBrief jobBrief) {
		return jobBriefService.update(jobBrief);
	}

	

	@Override
	public Map<String, Object> searchByPostingTitle(String postingTitle) {
		return jobBriefService.getDescriptionFromJobBrief(postingTitle);

	}
}
