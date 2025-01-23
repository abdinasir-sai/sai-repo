/* (C)2024 */
package com.nouros.hrms.controller.impl;

import com.enttribe.commons.spring.rest.ResponseBuilder;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.RelocationController;
import com.nouros.hrms.model.Relocation;
import com.nouros.hrms.service.RelocationService;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Primary
@RestController
@RequestMapping("/Relocation")

public class RelocationControllerImpl implements RelocationController {

	@Autowired
	private RelocationService relocationService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public Relocation create(Relocation relocation) {
		return relocationService.create(relocation);
	}

	@Override
	public Long count(String filter) {
		return relocationService.count(filter);
	}

	@Override
	public List<Relocation> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return relocationService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public Relocation update(Relocation relocation) {
		return relocationService.update(relocation);
	}

	@Override
	public Relocation findById(Integer id) {
		return relocationService.findById(id);
	}

	@Override
	public List<Relocation> findAllById(List<Integer> id) {
		return relocationService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		relocationService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		relocationService.softBulkDelete(list);
	}

}
