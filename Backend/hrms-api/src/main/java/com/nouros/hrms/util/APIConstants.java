package com.nouros.hrms.util;

/**

The APIConstants class defines a collection of constants used throughout the project.
This class is defined as a final and contains only static variables, so it cannot be extended or instantiated.
@author Bootnext team
@version 1.0
@since 2022-07
*/
public class APIConstants {

  private APIConstants() {
    super();
  }

/**
The SUCCESS_CODE property is a constant int value that represents the HTTP status code of a successful request.
*/
  public static final String SUCCESS_CODE = "200";
/**

The SUCCESS_CODE_MSG property is a constant String value that represents the message of a successful request.
*/
  public static final String SUCCESS_CODE_MSG = "SUCCESS";
/**

The DEFAULT_SCHEME property is a constant String value that represents the default scheme for the API.
*/
  public static final String DEFAULT_SCHEME = "default";
/**

The QUERY property is a constant String value that represents the filter parameter for RSQL queries.
*/
  public static final String QUERY = "filter";
/**

The OFFSET property is a constant String value that represents the offset parameter for pagination.
*/
  public static final String OFFSET = "offset";
/**

The SIZE property is a constant String value that represents the size parameter for pagination.
*/
  public static final String SIZE = "size";
/**

The ORDER_BY property is a constant String value that represents the order by parameter for sorting.
*/
  public static final String ORDER_BY = "orderBy";
/**

The ORDER_TYPE property is a constant String value that represents the order type parameter for sorting.
*/
  public static final String ORDER_TYPE = "orderType";
/**
 * This variable is used to store the unique identifier for an object.
 * The identifier is a string and is used to uniquely identify
 * the object across multiple instances of the application.
 * */
  public static final String ID = "id";
  
   /**
 * This variable is used to limit the number of records that are returned from a query.
 * It's used in conjunction with the 'skip' variable to provide pagination functionality.
 * */
  public static final String LIMIT = "limit";
  
  /**
 * This variable is used to specify the number of records to skip
 * when retrieving data from a database or API.
 * It's used in conjunction with the 'limit' variable to provide pagination functionality.
 * */
  public static final String SKIP = "skip";

/**
 * This variable is used to hold a JSON string that represents a successful operation.
 * It's typically used to return a JSON response from a REST API.
 * */
  public static final String SUCCESS_JSON="{\"result\":\"success\"}";

/**
 * This variable is used to hold a JSON string that represents a failed operation.
 * It's typically used to return a JSON response from a REST API.
 * */
  public static final String FAILURE_JSON = "{\"result\":\"failure\"}";

  public static final String INSIDE_METHOD = "Inside method {}";
  
  public static final String  DYNAMIC_VALUE1 = "\"$";
	   public static final String  DYNAMIC_VALUE2 ="{\"";
	   
    
  
}
