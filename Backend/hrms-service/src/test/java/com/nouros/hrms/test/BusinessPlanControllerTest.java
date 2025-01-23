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
import com.nouros.hrms.controller.BusinessPlanController;
import com.nouros.hrms.model.BusinessPlan;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class BusinessPlanControllerTest extends SpringJUnitRunnerTest{
	
	private static final Logger log = LogManager.getLogger(BusinessPlanControllerTest.class);

	@Autowired
	private BusinessPlanController controller;

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
	@Order(1)
	void create() {
		log.info("Inside BusinessPlanControllerTest @method create");
		BusinessPlan businessPlan = new BusinessPlan();
		businessPlan.setTitle("test");
		businessPlan = controller.create(businessPlan);
		assertNotEquals(businessPlan, null); 
	}

	@Test
	@Order(2)
	void update() {
		BusinessPlan businessPlan = new BusinessPlan();
		businessPlan.setTitle("test");
		businessPlan = controller.update(businessPlan);
		assertNotEquals(businessPlan, null);
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
		BusinessPlan businessPlan = new BusinessPlan();
		businessPlan.setTitle("test");
		businessPlan = controller.create(businessPlan);
		BusinessPlan businessPlanList = controller.findById(businessPlan.getId());
		assertNotNull(businessPlanList, "The businessPlan found by ID should not be null");
	}

	@Test
	@Order(5)
	void findAllById() {
		BusinessPlan businessPlan = new BusinessPlan();
		businessPlan.setTitle("test");
		businessPlan = controller.create(businessPlan);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(businessPlan.getId());
		List<BusinessPlan> businessPlans = controller.findAllById(listInt);
		System.out.println("List of businessPlans: " + businessPlans);
		assertNotNull(businessPlans, "The list of businessPlans should not be null");
		assertTrue(!businessPlans.isEmpty(), "The list of businessPlans should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		BusinessPlan businessPlan = new BusinessPlan();
		businessPlan.setTitle("test");
		businessPlan = controller.create(businessPlan);
		controller.deleteById(businessPlan.getId());
		BusinessPlan businessPlanList = controller.findById(businessPlan.getId());
		assertNotNull(businessPlanList, "The list of businessPlans should not be null");
	}

	@Test
	@Order(7)
	void search() {
		BusinessPlan businessPlan = new BusinessPlan();
		businessPlan.setTitle("test");
		 controller.create(businessPlan);
		List<BusinessPlan> list = controller.search("id=ge=0", 1, 10,"modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
    }
	
	
}
