/* (C)2024 */
package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Relocation;
import com.nouros.hrms.service.generic.GenericService;

public interface RelocationService extends GenericService<Integer, Relocation> {
	
	Relocation create(Relocation relocation);

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
}
