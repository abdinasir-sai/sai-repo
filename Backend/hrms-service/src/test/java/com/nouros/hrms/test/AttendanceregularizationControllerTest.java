package com.nouros.hrms.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.product.security.wrapper.UserWrapper;
import com.nouros.hrms.controller.AttendanceRegularizationController;
import com.nouros.hrms.controller.EmployeeController;
import com.nouros.hrms.model.AttendanceRegularization;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class AttendanceRegularizationControllerTest extends SpringJUnitRunnerTest{
	
	
	private static final Logger log = LogManager.getLogger(AttendanceRegularizationControllerTest.class);

	@Autowired
	private AttendanceRegularizationController controller;
	
	@Autowired
	private EmployeeController employeeController;

	@MockBean
	private CustomerInfo customerInfo;


	@BeforeEach
	void setup() {
		UserWrapper userWrapper = Mockito.mock(UserWrapper.class);
		userWrapper.setActiveUserSpaceId(1);
		when(customerInfo.getCustomerInfo()).thenReturn(userWrapper);
		when(userWrapper.getActiveUserSpaceId()).thenReturn(1);
	}


	@Test
	@Order(2)
	void update() {
		AttendanceRegularization attendanceRegularization = new AttendanceRegularization();
		attendanceRegularization.setApprovalName("test");
		attendanceRegularization = controller.update(attendanceRegularization);
		assertNotEquals(attendanceRegularization, null);
	}

	@Test
	@Order(3)
	void count() {
		long count = controller.count("id=ge=0");
		assertTrue(count >= 0, "The count should be non-negative");
	}


	
}
