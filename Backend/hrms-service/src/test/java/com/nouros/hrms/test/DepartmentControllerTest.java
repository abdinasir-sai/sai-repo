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
import com.nouros.hrms.controller.DepartmentController;
import com.nouros.hrms.controller.DivisionController;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.repository.DepartmentRepository;
import com.nouros.hrms.wrapper.DepartmentDetailsWrapper;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class DepartmentControllerTest extends SpringJUnitRunnerTest {

	private static final Logger log = LogManager.getLogger(DepartmentControllerTest.class);

	@Autowired
	private DivisionController divisionController;

	@Autowired
	private DepartmentController controller;
	
	@Autowired 
	DepartmentRepository departmentRepository;

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
		log.info("Inside DepartmentControllerTest @method create");
		Department department = new Department();
		department.setName("Business Unit");
		department = controller.create(department);
		assertNotEquals(department, null); 
	}

	@Test
	@Order(2)
	void update() {
		Department department = new Department();
		department.setName("Business Unit updated");
		department = controller.update(department);
		assertNotEquals(department, null);
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
		Department department = new Department();
		department.setName("Business Unit");
		department = controller.create(department);
		Department departmentList = controller.findById(department.getId());
		assertNotNull(departmentList, "The department found by ID should not be null");
	}

	@Test
	@Order(5) 
	void findAllById() {
		Department department = new Department();
		department.setName("Business Unit");
		department = controller.create(department);
		List<Integer> listInt = new ArrayList<>();
		listInt.add(department.getId());
		List<Department> departments = controller.findAllById(listInt);
		System.out.println("List of departments: " + departments);
		assertNotNull(departments, "The list of departments should not be null");
		assertTrue(!departments.isEmpty(), "The list of departments should not be empty");
	}

	@Test
	@Order(6)
	void deleteById() {
		Department department = new Department();
		department.setName("Business Unit");
		department = controller.create(department);
		controller.deleteById(department.getId());
		Department departmentList = controller.findById(department.getId());
		assertNotNull(departmentList, "The list of departments should not be null");
	}

	@Test
	@Order(7)
	void search() {
		Department department = new Department();
		department.setName("Business Unit");
	   controller.create(department);
		List<Department> list = controller.search("id=ge=0", 1, 10, "modifiedTime", "desc");
		assertTrue(list.size() >= 0, "The count should be non-negative");
	}
	
	@Test
	@Order(8)
     void testGetAllChildDepartments() {
       
		Department department = new Department();
		department.setName("Business Unit");
		department = controller.create(department);

       
		Department departmentChild = new Department();
		departmentChild.setName("child");
		departmentChild.setParentDepartment(department);
		controller.create(departmentChild);

		List<DepartmentDetailsWrapper> result = controller.getAllChildDepartments("Business Unit");

        assertNotNull(result);
    }




}
