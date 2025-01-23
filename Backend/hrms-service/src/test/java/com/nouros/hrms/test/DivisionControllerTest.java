package com.nouros.hrms.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.nouros.hrms.controller.DivisionController;
import com.nouros.hrms.model.Division;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
 class DivisionControllerTest extends SpringJUnitRunnerTest {

	private static final Logger log = LogManager.getLogger(DivisionControllerTest.class);

	@Autowired
	private DivisionController controller;

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
		log.info("Inside DivisionControllerTest @method create");
		Division division = new Division();
		division.setName("Business Unit");
		division = controller.create(division);
		assertNotEquals(division, null); 
	}

	@Test
	@Order(2)
	void update() {
		Division division = new Division();
		division.setName("Business Unit updated");
		division = controller.update(division);
		assertNotEquals(division, null);
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
		Division division = new Division();
		division.setName("Business Unit");
		division = controller.create(division);
		Division divisionList = controller.findById(division.getId());
		assertNotNull(divisionList, "The division found by ID should not be null");
	}

	@Test
	@Order(5)
	void findAllById() {
		Division division = new Division();
		division.setName("Business Unit");
		division = controller.create(division);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(division.getId());
		List<Division> divisions = controller.findAllById(listInt);
		System.out.println("List of divisions: " + divisions);
		assertNotNull(divisions, "The list of divisions should not be null");
		assertTrue(!divisions.isEmpty(), "The list of divisions should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		Division division = new Division();
		division.setName("Business Unit");
		division = controller.create(division);
		controller.deleteById(division.getId());
		Division divisionList = controller.findById(division.getId());
		assertNotNull(divisionList, "The list of divisions should not be null");
	}

	@Test
	@Order(7)
	void search() {
		Division division = new Division();
		division.setName("Business Unit");
	  controller.create(division);
		List<Division> list = controller.search("id=ge=0", 1, 10,"modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
    }
	
	@Test
	@Order(8)
	void  getAirportDetails()
	{
		List<Map<String, String>> list = controller.getAirportDetails("test");
	    assertTrue(list.isEmpty()); 
	}
}
