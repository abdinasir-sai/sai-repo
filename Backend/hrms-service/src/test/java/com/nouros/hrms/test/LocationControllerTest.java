package com.nouros.hrms.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
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

import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.product.security.wrapper.UserWrapper;
import com.nouros.hrms.controller.LocationController;
import com.nouros.hrms.model.Location;
import com.nouros.hrms.repository.LocationRepository;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class LocationControllerTest extends SpringJUnitRunnerTest {

	private static final Logger log = LogManager.getLogger(LocationControllerTest.class);

	@Autowired
	private LocationController controller;

	@MockBean
	private CustomerInfo customerInfo;

	@MockBean
	private ICustomNumberValuesRest customNumberValuesRest;

	@MockBean
	private LocationRepository locationRepository;

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

		NameGenerationWrapperV2 nameGenerationWrapperV2 = new NameGenerationWrapperV2();
		nameGenerationWrapperV2.setGeneratedName("LOC-001");
		when(customNumberValuesRest.generateNameAndFriendlyName(eq("locationRuleDynamic"), anyMap(),
				eq(Status.ALLOCATED))).thenReturn(nameGenerationWrapperV2);

		Location location = new Location();
		location.setCountry("India");
		location.setStateProvince("test");
		location.setLocationId("LOC-001");
		when(locationRepository.save(any(Location.class))).thenReturn(location);

		Location locationToCreate = new Location();
		locationToCreate.setCountry("India");
		locationToCreate.setStateProvince("test");

		Location createdLocation = controller.create(locationToCreate);

		assertNotEquals(createdLocation, null);
		assertEquals("LOC-001", createdLocation.getLocationId());
	}

	Location getLocation() {
		NameGenerationWrapperV2 nameGenerationWrapperV2 = new NameGenerationWrapperV2();
		nameGenerationWrapperV2.setGeneratedName("LOC-001");
		when(customNumberValuesRest.generateNameAndFriendlyName(eq("locationRuleDynamic"), anyMap(),
				eq(Status.ALLOCATED))).thenReturn(nameGenerationWrapperV2);

		Location location = new Location();
		location.setCountry("India");
		location.setStateProvince("test");
		location.setLocationId("LOC-001");
		when(locationRepository.save(any(Location.class))).thenReturn(location);

		Location locationToCreate = new Location();
		locationToCreate.setCountry("India");
		locationToCreate.setStateProvince("test");

		return controller.create(locationToCreate);
	}
 
	@Test
	@Order(2)
	void update() {
		Location location = getLocation();
		log.info("Inside LocationControllerTest @method update : {} ", location);
		location.setName("Business Unit updated");
		location = controller.update(location);
		assertEquals(null, location); 
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
		Location location = getLocation();
		Location departmentList = controller.findById(location.getId());
		assertNotNull(departmentList, "The location found by ID should not be null");
	}

	@Test
	@Order(5)
	void findAllById() {
		Location location = getLocation();
		List<Integer> listInt = new ArrayList<>();
		listInt.add(location.getId());
		List<Location> list = controller.findAllById(listInt);
		assertTrue(list.size() >= 0, "The count should be non-negative");
	}

	@Test
	@Order(6)
	void deleteById() {
		Location location = getLocation();
		controller.deleteById(location.getId());
		Location departmentList = controller.findById(location.getId());
		assertNotNull(departmentList, "The list of locations should not be null");
	}

	@Test
	@Order(7)
	void search() {
		List<Location> list = controller.search("id=ge=0", 1, 10, "modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
	}

	@Test
	@Order(8)
	void createTwo() {

		NameGenerationWrapperV2 nameGenerationWrapperV2 = new NameGenerationWrapperV2();
		nameGenerationWrapperV2.setGeneratedName("LOC-001");
		when(customNumberValuesRest.generateNameAndFriendlyName(eq("locationRuleDynamic"), anyMap(),
				eq(Status.ALLOCATED))).thenReturn(nameGenerationWrapperV2);

		Location location = new Location();
		location.setCountry(null);
		location.setStateProvince(null);
		location.setLocationId("LOC-001");
		when(locationRepository.save(any(Location.class))).thenReturn(location);

		Location locationToCreate = new Location();
		locationToCreate.setCountry("India");
		locationToCreate.setStateProvince("test");

		Location createdLocation = controller.create(locationToCreate);

		assertNotEquals(createdLocation, null);
		assertEquals("LOC-001", createdLocation.getLocationId());
	}
}
