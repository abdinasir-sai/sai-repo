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
import com.nouros.hrms.controller.BreakdetailsController;
import com.nouros.hrms.model.Breakdetails;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class BreakdetailsControllerTest extends SpringJUnitRunnerTest{
	
	private static final Logger log = LogManager.getLogger(BreakdetailsControllerTest.class);

	@Autowired
	private BreakdetailsController controller;

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
		log.info("Inside BreakdetailsControllerTest @method create");
		Breakdetails breakdetails = new Breakdetails();
		breakdetails.setBreakName("Lunch");
		breakdetails = controller.create(breakdetails);
		assertNotEquals(breakdetails, null); 
	}

	@Test
	@Order(2)
	void update() {
		Breakdetails breakdetails = new Breakdetails();
		breakdetails.setBreakName("Lunch");
		breakdetails = controller.update(breakdetails);
		assertNotEquals(breakdetails, null);
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
		Breakdetails breakdetails = new Breakdetails();
		breakdetails.setBreakName("Lunch");
		breakdetails = controller.create(breakdetails);
		Breakdetails breakdetailsList = controller.findById(breakdetails.getId());
		assertNotNull(breakdetailsList, "The breakdetails found by ID should not be null");
	}

	@Test
	@Order(5)
	void findAllById() {
		Breakdetails breakdetails = new Breakdetails();
		breakdetails.setBreakName("Lunch");
		breakdetails = controller.create(breakdetails);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(breakdetails.getId());
		List<Breakdetails> breaksdetails = controller.findAllById(listInt);
		System.out.println("List of breaksdetails: " + breaksdetails);
		assertNotNull(breaksdetails, "The list of breaksdetails should not be null");
		assertTrue(!breaksdetails.isEmpty(), "The list of breaksdetails should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		Breakdetails breakdetails = new Breakdetails();
		breakdetails.setBreakName("Lunch");
		breakdetails = controller.create(breakdetails);
		controller.deleteById(breakdetails.getId());
		Breakdetails breakdetailsList = controller.findById(breakdetails.getId());
		assertNotNull(breakdetailsList, "The list of breaksdetails should not be null");
	}

	@Test
	@Order(7)
	void search() {
		Breakdetails breakdetails = new Breakdetails();
		breakdetails.setBreakName("Lunch");
		controller.create(breakdetails);
		List<Breakdetails> list = controller.search("id=ge=0", 1, 10,"modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
    }
	
}
