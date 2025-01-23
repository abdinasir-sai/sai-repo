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
import com.nouros.hrms.controller.ApplicantResumeController;
import com.nouros.hrms.model.ApplicantResume;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class ApplicantResumeControllerTest extends SpringJUnitRunnerTest {
	
	private static final Logger log = LogManager.getLogger(ApplicantResumeControllerTest.class);

	@Autowired
	private ApplicantResumeController controller;

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
		log.info("Inside ApplicantResumeControllerTest @method create");
		ApplicantResume applicantResume = new ApplicantResume();
		applicantResume.setText1("test");
		applicantResume = controller.create(applicantResume);
		assertNotEquals(applicantResume, null); 
	}

	@Test
	@Order(2)
	void update() {
		ApplicantResume applicantResume = new ApplicantResume();
		applicantResume.setText1("test");
		applicantResume = controller.update(applicantResume);
		assertNotEquals(applicantResume, null);
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
		ApplicantResume applicantResume = new ApplicantResume();
		applicantResume.setText1("test");
		applicantResume = controller.create(applicantResume);
		ApplicantResume applicantResumeList = controller.findById(applicantResume.getId());
		assertNotNull(applicantResumeList, "The applicantResume found by ID should not be null");
	}

	@Test
	@Order(5)
	void findAllById() {
		ApplicantResume applicantResume = new ApplicantResume();
		applicantResume.setText1("test");
		applicantResume = controller.create(applicantResume);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(applicantResume.getId());
		List<ApplicantResume> applicantsResume = controller.findAllById(listInt);
		System.out.println("List of applicantsResume: " + applicantsResume);
		assertNotNull(applicantsResume, "The list of applicantsResume should not be null");
		assertTrue(!applicantsResume.isEmpty(), "The list of applicantsResume should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		ApplicantResume applicantResume = new ApplicantResume();
		applicantResume.setText1("test");
		applicantResume = controller.create(applicantResume);
		controller.deleteById(applicantResume.getId());
		ApplicantResume applicantResumeList = controller.findById(applicantResume.getId());
		assertNotNull(applicantResumeList, "The list of applicantsResume should not be null");
	}

	@Test
	@Order(7)
	void search() {
		ApplicantResume applicantResume = new ApplicantResume();
		applicantResume.setText1("test");
		 controller.create(applicantResume);
		List<ApplicantResume> list = controller.search("id=ge=0", 1, 10,"modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
    }
	

}
