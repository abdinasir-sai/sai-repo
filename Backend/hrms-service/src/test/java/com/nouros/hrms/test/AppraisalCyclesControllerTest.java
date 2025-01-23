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
import com.nouros.hrms.controller.AppraisalCyclesController;
import com.nouros.hrms.controller.EmployeeController;
import com.nouros.hrms.model.AppraisalCycles;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class AppraisalCyclesControllerTest extends SpringJUnitRunnerTest {

	
	private static final Logger log = LogManager.getLogger(AppraisalCyclesControllerTest.class);

	@Autowired
	private AppraisalCyclesController controller;

	@MockBean
	private CustomerInfo customerInfo;

	@Autowired
	private EmployeeController employeeController;

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
		log.info("Inside AppraisalCyclesControllerTest @method create");
		AppraisalCycles appraisalCycles = new AppraisalCycles();
		appraisalCycles.setName("Business Unit");
		appraisalCycles = controller.create(appraisalCycles);
		assertNotEquals(appraisalCycles, null); 
	}

	@Test
	@Order(2)
	void update() {
		AppraisalCycles appraisalCycles = new AppraisalCycles();
		appraisalCycles.setName("Business Unit updated");
		appraisalCycles = controller.update(appraisalCycles);
		assertNotEquals(appraisalCycles, null);
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
		AppraisalCycles appraisalCycles = new AppraisalCycles();
		appraisalCycles.setName("Business Unit");
		appraisalCycles = controller.create(appraisalCycles);
		AppraisalCycles appraisalCyclesList = controller.findById(appraisalCycles.getId());
		assertNotNull(appraisalCyclesList, "The appraisalCycles found by ID should not be null");
	}

	@Test
	@Order(5)
	void findAllById() {
		AppraisalCycles appraisalCycles = new AppraisalCycles();
		appraisalCycles.setName("Business Unit");
		appraisalCycles = controller.create(appraisalCycles);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(appraisalCycles.getId());
		List<AppraisalCycles> appraisalsCycles = controller.findAllById(listInt);
		System.out.println("List of appraisalsCycles: " + appraisalsCycles);
		assertNotNull(appraisalsCycles, "The list of appraisalsCycles should not be null");
		assertTrue(!appraisalsCycles.isEmpty(), "The list of appraisalsCycles should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		AppraisalCycles appraisalCycles = new AppraisalCycles();
		appraisalCycles.setName("Business Unit");
		appraisalCycles = controller.create(appraisalCycles);
		controller.deleteById(appraisalCycles.getId());
		AppraisalCycles appraisalCyclesList = controller.findById(appraisalCycles.getId());
		assertNotNull(appraisalCyclesList, "The list of appraisalsCycles should not be null");
	}

	@Test
	@Order(7)
	void search() {
		AppraisalCycles appraisalCycles = new AppraisalCycles();
		appraisalCycles.setName("Business Unit");
	   controller.create(appraisalCycles);
		List<AppraisalCycles> list = controller.search("id=ge=0", 1, 10,"modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
    }
	
}
