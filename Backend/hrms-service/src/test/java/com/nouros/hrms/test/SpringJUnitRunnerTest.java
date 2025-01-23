package com.nouros.hrms.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.nouros.hrms.Application;
@SpringBootTest(classes = Application.class)
@SqlConfig
//@Sql(value = "classpath:ApplicationData.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
  class SpringJUnitRunnerTest {

	
	@Autowired
	  protected MockMvc mvc;
	
	@Test
	void contextLoads() {
		 assertTrue(true);
	}

}
