package com.nouros.hrms.controller.impl;

import com.enttribe.commons.spring.rest.ResponseBuilder;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeCertificationController;
import com.nouros.hrms.model.EmployeeCertification;
import com.nouros.hrms.service.EmployeeCertificationService;

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
@RequestMapping("/EmployeeCertification")
public class EmployeeCertificationControllerImpl implements EmployeeCertificationController {

    @Autowired private EmployeeCertificationService employeeCertificationService;

    @Override
    @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
    public EmployeeCertification create(EmployeeCertification employeeCertification) {
        return employeeCertificationService.create(employeeCertification);
    }

    @Override
    public Long count(String filter) {
        return employeeCertificationService.count(filter);
    }

    @Override
    public List<EmployeeCertification> search(
            String filter, Integer offset, Integer size, String orderBy, String orderType) {
        return employeeCertificationService.search(filter, offset, size, orderBy, orderType);
    }

    @Override
    @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
    public EmployeeCertification update(EmployeeCertification employeeCertification) {
        return employeeCertificationService.update(employeeCertification);
    }

    @Override
    public EmployeeCertification findById(Integer id) {
        return employeeCertificationService.findById(id);
    }

    @Override
    public List<EmployeeCertification> findAllById(List<Integer> id) {
        return employeeCertificationService.findAllById(id);
    }

    @Override
    @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
    public void deleteById(Integer id) {
        employeeCertificationService.softDelete(id);
    }

    @Override
    @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
    public void bulkDelete(List<Integer> list) {
        employeeCertificationService.softBulkDelete(list);
    }

}
