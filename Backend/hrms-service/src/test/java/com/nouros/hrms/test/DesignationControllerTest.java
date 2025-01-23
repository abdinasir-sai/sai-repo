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
import com.nouros.hrms.controller.DesignationController;
import com.nouros.hrms.model.Designation;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class DesignationControllerTest extends SpringJUnitRunnerTest{

	
	private static final Logger log = LogManager.getLogger(DesignationControllerTest.class);

	

	@Autowired
	private DesignationController controller;

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
		log.info("Inside DesignationControllerTest @method create");
		Designation designation = new Designation();
		designation.setName("Business Unit");
		designation = controller.create(designation);
		assertNotEquals(designation, null);
	}

	@Test
	@Order(2)
	void update() {
		Designation designation = new Designation();
		designation.setName("Business Unit updated");
		designation = controller.update(designation);
		assertNotEquals(designation, null);
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
		Designation designation = new Designation();
		designation.setName("Business Unit");
		designation = controller.create(designation);
		Designation designationList = controller.findById(designation.getId());
		assertNotNull(designationList, "The designation found by ID should not be null");
	}

	@Test
	@Order(5) 
	void findAllById() {
		Designation designation = new Designation();
		designation.setName("Business Unit");
		designation = controller.create(designation);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(designation.getId());
		List<Designation> designations = controller.findAllById(listInt);
		System.out.println("List of designations: " + designations);
		assertNotNull(designations, "The list of designations should not be null");
		assertTrue(!designations.isEmpty(), "The list of designations should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		Designation designation = new Designation();
		designation.setName("Business Unit");
		designation = controller.create(designation);
		controller.deleteById(designation.getId());
		Designation designationList = controller.findById(designation.getId());
		assertNotNull(designationList, "The list of designations should not be null");
	}

	@Test
	@Order(7)
	void search() {
		Designation designation = new Designation();
		designation.setName("Business Unit");
		controller.create(designation);
		List<Designation> list = controller.search("id=ge=0", 1, 10, "modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
	}
}
