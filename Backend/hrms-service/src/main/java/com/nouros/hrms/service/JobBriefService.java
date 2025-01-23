package com.nouros.hrms.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.model.JobBrief;
import com.nouros.hrms.service.generic.GenericService;

public interface JobBriefService extends GenericService<Integer,JobBrief> {

	/**
	 * Imports job brief data from a Word file.
	 * 
	 * @param wordFile the Word file containing job brief data.
	 * @return a message indicating the result of the import operation.
	 * @throws IOException            if an I/O error occurs.
	 * @throws InstantiationException if there is an issue with instantiation.
	 * @throws ClassNotFoundException if the class is not found during
	 *                                instantiation.
	 * 
	 */
	Map<String, Object> importJobBriefData(MultipartFile wordFile);

	JobBrief searchByPostingTitle(String postingTitle);

	void bulkDelete(List<Integer> list);

	Map<String, Object> getDescriptionFromJobBrief(String postingTitle);

}
