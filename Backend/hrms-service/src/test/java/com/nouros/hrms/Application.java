package com.nouros.hrms;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.product.pii.filter.PropertyFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.nouros.hrms.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * The Application class is the entry point of the application.
 * It contains the main method that starts the application.
 */
@EnableAsync
@SpringBootApplication(exclude = CassandraAutoConfiguration.class)
@ServletComponentScan({ "com.enttribe" })
@EnableScheduling
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.nouros.hrms","com.enttribe.payrollmanagement", "com.enttribe", "com.enttribe.orchestrator.utility"})
@EntityScan({ "com.enttribe.platform.vbhelper","com.nouros.hrms","com.enttribe.payrollmanagement","com.enttribe.orchestrator.utility"})
@EnableJpaRepositories(basePackages = { "com.enttribe.platform.vbhelper","com.enttribe.payrollmanagement", "com.nouros.hrms", "com.enttribe.orchestrator.utility" })
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@ComponentScan(basePackages = { "com.enttribe" , "com.enttribe.commons.spring.boot.autoconfigure.storage"})
@EnableFeignClients(basePackages = {"com.nouros.hrms", "com.enttribe.workflow" ,"com.enttribe.payrollmanagement","com.enttribe.orchestrator","com.enttribe"})
public class Application {
	
	private Logger logger = LogManager.getLogger(Application.class);
	
	/**
     * The main entry point of the application.
     * This method sets up the properties file paths, starts the Spring application, and registers the application node in JavaMelody
     * if the JavaMelody status is enabled in the configuration.
     *
     * @param args The command-line arguments.
     */
  public static void main(String[] args) {
    ConfigUtils.setPropertiesFilePath("application.properties", "config.properties");
    SpringApplication.run(Application.class, args);
    
  }
  
    
  @Bean
  public ObjectMapper objectMapper() {
	  logger.info("Custom Object Mapper initialised ");
	    ObjectMapper objectMapper = new ObjectMapper();
	    // Create a SimpleFilterProvider
	    objectMapper.registerModule(new Jdk8Module());
	    SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	    // Add your custom filter to the filter provider
	    filterProvider.setFailOnUnknownId(false);
	    FilterProvider filters = filterProvider.addFilter("propertyFilter", new PropertyFilter());
	    objectMapper.setFilterProvider(filters);
	    return objectMapper;
	}
  
  /**
   * Creates an instance of the AuditorAware interface with the SpringAuditorAware implementation.
   * This bean is used for auditing purposes in Spring Data JPA.
   *
   * @return An instance of the AuditorAware interface.
   */
  @Bean
  public AuditorAware<User> auditorAware(){
      return new SpringAuditorAware();
  }
  
  @Bean
  public OpenAPI customOpenAPI() {
      return new OpenAPI().info(new Info().title("hrms").version("4.0.0"))
              .components(new Components().addSecuritySchemes("default", createOAuthSecurityScheme()));

  }
  
  private List<String> packagesToScan = List.of("com.nouros.hrms","com.enttribe.payrollmanagement");

  SecurityScheme createOAuthSecurityScheme() {
      Scopes scopesArray = new Scopes();
      OAuthFlow oAuthFlow = new OAuthFlow().authorizationUrl("http://localhost/auth");
      try {
          logger.debug("Permission SCopes Size is {}", scopesArray.size());
          oAuthFlow.scopes(scopesArray);
          Scopes scopes = readAuthorizationScopes(packagesToScan, new Scopes());
          logger.info("Authorization Scopes  is {}", scopes.values());
          oAuthFlow.scopes(scopes);

      } catch (Exception e) {
          logger.error("error while getting permission {}", e.getMessage(), e);
      }
      return new SecurityScheme()
              .type(SecurityScheme.Type.OAUTH2)
              .description("Oauth2 flow")
              .flows(new OAuthFlows().implicit(oAuthFlow));
  }

  private static Scopes readAuthorizationScopes(List<String> packageNames, Scopes scopesArray) {
      List<String> scopes = new ArrayList<>();

      for (String packageName : packageNames) {
          // Create a new instance of Reflections for the specified package
          Reflections reflections = new Reflections(packageName);

          // Get all classes with the @FeignClient annotation
          Set<Class<?>> classes = reflections.getTypesAnnotatedWith(FeignClient.class);

          // Process each class
          for (Class<?> clazz : classes) {

              // Process each method in the class
              for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                  // Check for @Operation annotation
                  if (method.isAnnotationPresent(Operation.class)) {
                      Operation apiOperation = method.getAnnotation(Operation.class);

                      // Check for @Authorization annotation
                      if (apiOperation.security().length > 0) {
                          io.swagger.v3.oas.annotations.security.SecurityRequirement[] authorizations = apiOperation.security();

                          // Process each authorization
                          for (io.swagger.v3.oas.annotations.security.SecurityRequirement authorization : authorizations) {
                              // Check for @AuthorizationScope annotation
                              if (authorization.scopes().length > 0) {
                                  String[] authorizationScopes = authorization.scopes();

                                  // Process each authorization scope
                                  for (String scope : authorizationScopes) {
                                      scopesArray.addString(scope, scope);
                                      scopes.add(scope);
                                  }
                              }
                          }
                      }
                  }
              }
          }
      }
      return scopesArray;
  }

}
