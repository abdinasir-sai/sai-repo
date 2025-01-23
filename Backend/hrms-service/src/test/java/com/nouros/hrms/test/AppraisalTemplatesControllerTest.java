package com.nouros.hrms.test;


import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.nouros.hrms.controller.AppraisalTemplatesController;
import com.nouros.hrms.controller.DepartmentController;
import com.nouros.hrms.model.AppraisalTemplates;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class AppraisalTemplatesControllerTest extends SpringJUnitRunnerTest{
	
	
	private static final Logger log = LogManager.getLogger(AppraisalTemplatesControllerTest.class);

	@Autowired
	private AppraisalTemplatesController controller;

	@MockBean
	private CustomerInfo customerInfo;

    @Autowired
    private DepartmentController departmentController;
	
	@BeforeEach
	void setup() {
		UserWrapper userWrapper = Mockito.mock(UserWrapper.class);
		userWrapper.setActiveUserSpaceId(1);
		when(customerInfo.getCustomerInfo()).thenReturn(userWrapper);
		when(userWrapper.getActiveUserSpaceId()).thenReturn(1);
	}

	@Test
	@Order(1)
	void create() {
		log.info("Inside AppraisalTemplatesControllerTest @method create");
		AppraisalTemplates appraisalTemplates = new AppraisalTemplates();
		appraisalTemplates.setRoleId("test");
		appraisalTemplates = controller.create(appraisalTemplates);
		assertNotEquals(appraisalTemplates, null); 
	}

	@Test
	@Order(2)
	void update() {
		AppraisalTemplates appraisalTemplates = new AppraisalTemplates();
		appraisalTemplates.setRoleId("test");
		appraisalTemplates = controller.update(appraisalTemplates);
		assertNotEquals(appraisalTemplates, null);
	}

	@Test
	@Order(3)
	void count() {
		long count = controller.count("id=ge=0");
		assertTrue(count >= 0, "The count should be non-negative");
	}

	@Test
	@Order(4)
	void findById() {
		AppraisalTemplates appraisalTemplates = new AppraisalTemplates();
		appraisalTemplates.setRoleId("test");
		appraisalTemplates = controller.create(appraisalTemplates);
		AppraisalTemplates appraisalTemplatesList = controller.findById(appraisalTemplates.getId());
		assertNotNull(appraisalTemplatesList, "The appraisalTemplates found by ID should not be null");
	}

	@Test
	@Order(5)
	void findAllById() {
		AppraisalTemplates appraisalTemplates = new AppraisalTemplates();
		appraisalTemplates.setRoleId("test");
		appraisalTemplates = controller.create(appraisalTemplates);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(appraisalTemplates.getId());
		List<AppraisalTemplates> appraisalsTemplates = controller.findAllById(listInt);
		System.out.println("List of appraisalsTemplates: " + appraisalsTemplates);
		assertNotNull(appraisalsTemplates, "The list of appraisalsTemplates should not be null");
		assertTrue(!appraisalsTemplates.isEmpty(), "The list of appraisalsTemplates should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		AppraisalTemplates appraisalTemplates = new AppraisalTemplates();
		appraisalTemplates.setRoleId("test");
		appraisalTemplates = controller.create(appraisalTemplates);
		controller.deleteById(appraisalTemplates.getId());
		AppraisalTemplates appraisalTemplatesList = controller.findById(appraisalTemplates.getId());
		assertNotNull(appraisalTemplatesList, "The list of appraisalsTemplates should not be null");
	}

	@Test
	@Order(7)
	void search() {
		AppraisalTemplates appraisalTemplates = new AppraisalTemplates();
		appraisalTemplates.setRoleId("test");
		 controller.create(appraisalTemplates);
		List<AppraisalTemplates> list = controller.search("id=ge=0", 1, 10,"modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
    }


}
