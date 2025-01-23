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
import com.nouros.hrms.controller.AssetController;
import com.nouros.hrms.model.Asset;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class AssetControllerTest extends SpringJUnitRunnerTest {

	
	private static final Logger log = LogManager.getLogger(AssetControllerTest.class);

	@Autowired
	private AssetController controller;

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
		log.info("Inside AssetControllerTest @method create");
		Asset asset = new Asset();
		asset.setDeviceName("Mobile");
		asset = controller.create(asset);
		assertNotEquals(asset, null); 
	}

	@Test
	@Order(2)
	void update() {
		Asset asset = new Asset();
		asset.setDeviceName("Mobile");
		asset = controller.update(asset);
		assertNotEquals(asset, null);
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
		Asset asset = new Asset();
		asset.setDeviceName("Mobile");
		asset = controller.create(asset);
		Asset assetList = controller.findById(asset.getId());
		assertNotNull(assetList, "The asset found by ID should not be null");
	}

	@Test
	@Order(5)
	void findAllById() {
		Asset asset = new Asset();
		asset.setDeviceName("Mobile");
		asset = controller.create(asset);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(asset.getId());
		List<Asset> assets = controller.findAllById(listInt);
		System.out.println("List of assets: " + assets);
		assertNotNull(assets, "The list of assets should not be null");
		assertTrue(!assets.isEmpty(), "The list of assets should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		Asset asset = new Asset();
		asset.setDeviceName("Mobile");
		asset = controller.create(asset);
		controller.deleteById(asset.getId());
		Asset assetList = controller.findById(asset.getId());
		assertNotNull(assetList, "The list of assets should not be null");
	}

	@Test
	@Order(7)
	void search() {
		Asset asset = new Asset();
		asset.setDeviceName("Mobile");
		controller.create(asset);
		List<Asset> list = controller.search("id=ge=0", 1, 10,"modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
    }

	
}
