package com.nouros.payrollmanagement.controller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.io.excel.ExcelWriter;
import com.enttribe.commons.spring.rest.ResponseBuilder;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.payrollmanagement.controller.PayrollRunController;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryService;
import com.nouros.payrollmanagement.service.PayrollRunService;
import com.nouros.payrollmanagement.wrapper.PayrollRunWrapper;

import jakarta.validation.constraints.NotEmpty;

/**
 * 
 * This class represents the implementation of the PayrollRunController
 * interface. It is annotated with the Spring
 * annotations @RestController, @RequestMapping and @Primary to indicate that it
 * is a Spring-managed bean and should be used as the primary implementation of
 * the PayrollRunController. It is also annotated with @Api to provide metadata
 * for the Swagger documentation. The class also uses Lombok's @Slf4j annotation
 * to automatically generate a logger field named "log" that is used to log
 * method calls and results. The class fields include an instance of the
 * PayrollRunService bean, which is injected by Spring using the @Autowired
 * annotation. The class implements the following methods: create(PayrollRun
 * PayrollRun): creates an PayrollRun and returns the newly created PayrollRun.
 * count(String filter): returns the number of PayrollRun that match the
 * specified filter. search(String filter, Integer offset, Integer size, String
 * orderBy, String orderType): returns a list of PayrollRun that match the
 * specified filter, sorted according to the specified orderBy and orderType.
 * update(PayrollRun PayrollRun): updates an PayrollRun and returns the updated
 * PayrollRun.
 * 
 * importData(MultipartFile excelFile): importing data from excel sheet
 * export(String filter, Integer offset, Integer size, String orderBy, String
 * orderType): export the data to excel sheet downloadTemplate(String fileName):
 * download excel sheet template auditHistory(int id, Integer limit, Integer
 * skip): return AuditHistory of PayrollRun with id and limit and skip
 */
@Primary
@RestController
@RequestMapping("/PayrollRun")
//@Tag(name="/PayrollRun",tags="PayrollRun",description="PayrollRun")
public class PayrollRunControllerImpl implements PayrollRunController {

	private static final Logger log = LogManager.getLogger(PayrollRunControllerImpl.class);

	@Autowired
	private PayrollRunService payrollRunService;

	@Autowired
	private EmployeeMonthlySalaryService employeeMonthlySalaryService;

	@Override
	@Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
	public PayrollRun create(PayrollRun payrollRun) {
		return payrollRunService.create(payrollRun);
	}

	@Override
	public Long count(String filter) {
		return payrollRunService.count(filter);
	}

	@Override
	public List<PayrollRun> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
		return payrollRunService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
	public PayrollRun update(PayrollRun payrollRun) {
		return payrollRunService.update(payrollRun);
	}

	@Override
	public PayrollRun findById(Integer id) {
		return payrollRunService.findById(id);
	}

	@Override
	public List<PayrollRun> findAllById(List<Integer> id) {
		return payrollRunService.findAllById(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void deleteById(Integer id) {
		payrollRunService.softDelete(id);
	}

	@Override
	@Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
	public void bulkDelete(List<Integer> list) {
		payrollRunService.softBulkDelete(list);
	}

	@Override
	public String importData(MultipartFile excelFile)
			throws IOException, InstantiationException, ClassNotFoundException {
		return payrollRunService.importData(excelFile);
	}

	@Override
	public ResponseEntity<byte[]> export(String filter, Integer offset, Integer size, String orderBy, String orderType)
			throws IOException {
		log.info("going to get list");
		List<PayrollRun> result = payrollRunService.search(filter, 0, 10000000, orderBy, orderType);
		log.info("size of the list is :{},", result.size());
		byte[] workBook = payrollRunService.export(result);
		String fileName = "PayrollRun.xlsx";
		return ResponseBuilder.toResponse(workBook, fileName);
	}

	@Override
	public ResponseEntity downloadTemplate(String fileName) throws IOException {
		InputStream resourceAsStream = this.getClass().getClassLoader()
				.getResourceAsStream("templates/reports/" + fileName);
		log.info("resourceAsStream :{}", resourceAsStream.available());
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resourceAsStream);
		try (ExcelWriter excelWriter = new ExcelWriter(xssfWorkbook)) {
			byte[] workBook = excelWriter.toByteArray();
			return ResponseBuilder.toResponse(workBook, fileName);
		}
	}

	@Override
	public String auditHistory(int id, Integer limit, Integer skip) {
		return payrollRunService.auditHistory(id, limit, skip);
	}

	@Override
	public List<WorkflowActions> getActions(@NotEmpty Integer id) {
		return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class)
				.getWorkflowActions(id, "PayrollRun");
	}

	@Override
	public ResponseEntity<byte[]> generateExcel(Integer payrollId) throws IOException {
		log.debug("inside generateExcelFile   payrollId  :{}", payrollId);
		return employeeMonthlySalaryService.generateExcel(payrollId);
	}

	@Override
	public PayrollRun identifyVarianceReasons(Integer id) {
		log.debug("Inside @class PayrollRunControllerImpl @method identifyVarianceReasons payrollId  :{} ", id);
		return payrollRunService.identifyVarianceReasons(id);
	}

	@Override
	public PayrollRun reExecutePayroll(Integer month, Integer year) {
		log.debug("Inside @class PayrollRunControllerImpl @method reExecutePayroll payrollId  :month :{} year :{} ",
				month, year);
		return payrollRunService.reExecutePayroll(month, year);
	}

	@Override
	public Object getPayrollEmployeeMonthlySalary() {
		log.debug("Inside @class PayrollRunControllerImpl @method getPayrollEmployeeMonthlySalary");

		return payrollRunService.getPayrollEmployeeMonthlySalary();
	}

	@Override
	public PayrollRun updatePayrollRunWorkflowStage(PayrollRunWrapper payrollRunWrapper) {
		return payrollRunService.updatePayrollRunWorkflowStage(payrollRunWrapper);
	}

}
