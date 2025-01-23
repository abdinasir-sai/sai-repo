package com.nouros.hrms.util.report;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.json.JSONArray;
import org.json.JSONObject;

import com.enttribe.commons.Symbol;
import com.enttribe.commons.io.excel.Excel;
import com.enttribe.commons.io.excel.ExcelRow;
import com.enttribe.commons.lang.NumberUtils;
import com.enttribe.commons.lang.StringUtils;
import com.google.common.io.Resources;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for handling Excel-related operations, such as parsing JSON, validating table templates, and invoking getters and setters.
 */
public class ExcelUtils {
	
    private static final Logger log = LogManager.getLogger(ExcelUtils.class);

		private static final String DISPLAY_NAME = "displayName";
		private static final String EXCEPTION_MESS = "An error occurred while performing an operation: {}";
	/**
	   * Retrieves the URL of a resource given its path.
	   *
	   * @param path The path to the resource.
	   * @return The URL of the resource.
	   * @throws IOException If an I/O error occurs while retrieving the resource.
	   */
  public URL getResource(String path) throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    log.info("path is :{}", path);
    return classLoader.getResource(path);
  	
   }
  
    /**
   * Parses the mapped JSON data to populate the provided maps.
   *
   * @param tableColumn           The map to store table and column mappings.
   * @param identityColumnMapping The map to store identity column mappings.
   * @param templateHeaders       The map to store template and header mappings.
   * @throws IOException If an I/O error occurs while parsing the JSON data.
   */
   @SuppressWarnings("deprecation")
   public static void parseMappeddJson(Map<String, List<String>> tableColumn,
      Map<String, String> identityColumnMapping,Map<String ,List<String>> templateHeaders) throws IOException {
    URL url  = new ExcelUtils().getResource("MappedJson.json");
    String json = Resources.toString(url, Charsets.UTF_8);
    log.info("parseMappeddJson json is :{}", json);
    JSONObject jsonObject = new JSONObject(json);
    JSONArray jsonArray = jsonObject.getJSONArray("tables");
    log.info("jsonArray   is :{}", jsonArray);
   for(int i=0;i<jsonArray.length();i++)
	{
		Object tableObj=jsonArray.get(i);
   
      JSONObject table = (JSONObject) tableObj;
      String jsontableName = table.getString("name");
      String tableName = table.getString("name");
      if (StringUtils.isNotEmpty(jsontableName)) {
        JSONArray columnArray = table.getJSONArray("fields");
        List<String> columnNames = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        parseNestedJson(identityColumnMapping, jsontableName, columnArray, columnNames, headers);
        tableColumn.put(jsontableName, columnNames);//tableName and columns
        templateHeaders.put(tableName, headers);//template and Headers
      }
    }
  }

	/**
	 * @param identityColumnMapping
	 * @param jsontableName
	 * @param columnArray
	 * @param columnNames
	 * @param headers
	 */
	private static void parseNestedJson(Map<String, String> identityColumnMapping, String jsontableName,
			JSONArray columnArray, List<String> columnNames, List<String> headers) {
		for(int j=0;j<columnArray.length();j++)
    	{
    		Object column=columnArray.get(j);
      
          JSONObject columns = (JSONObject) column;
          JSONObject properties = null;
          if (columns.has("identityColumn") && Boolean.TRUE.equals(columns.get("identityColumn"))) {
            identityColumnMapping.put(jsontableName, columns.getString("name"));
            columnNames.add(columns.getString("name"));
            headers.add(columns.getString(DISPLAY_NAME));
          } else if (columns.has("properties")) {
            properties = columns.getJSONObject("properties");
            if (!(columns.getString(DISPLAY_NAME).equalsIgnoreCase("id")
                || properties.has("suppress"))) {
              columnNames.add(columns.getString("name")); 
              headers.add(columns.getString(DISPLAY_NAME));
            }
          } else if (!(columns.getString(DISPLAY_NAME).equalsIgnoreCase("id"))) {
            columnNames.add(columns.getString("name"));
            headers.add(columns.getString(DISPLAY_NAME));
            if (log.isInfoEnabled()) {
            log.info("Column Names   is :{}", columns.getString("name"));
            }
          }
        }
	}

 /**
    * Validates if the table template headers match the provided column names.
    *
    * @param sheet       The Excel sheet to validate.
    * @param columnNames The list of column names to validate against.
    * @return {@code true} if the headers match, {@code false} otherwise.
    */
  public static boolean validateTableTemplateHeader(Excel sheet, List<String> columnNames) {
    Iterator<ExcelRow> rowIterator = sheet.iterator();
    boolean flag = true;
    while (rowIterator.hasNext()) {
      ExcelRow excelRow = rowIterator.next();
      int index = -1;
      for (String header : columnNames) {
        if (!header.equalsIgnoreCase(excelRow.getString(++index))) {
          flag = false;
        }
      }
      return flag;
    }
    return false;
  }
  
   /**
   * Invokes the getter method of an object given its variable name and identity column mapping.
   *
   * @param obj                 The object to invoke the getter method on.
   * @param variableName        The variable name of the property to retrieve.
   * @param identityColumnMapping The map to store identity column mappings.
   * @return The result of invoking the getter method, or "Object Not Found" if not found.
   * @throws IntrospectionException If an introspection error occurs while invoking the getter method.
   */
   public static Object invokeGetter(Object obj, String variableName,
      Map<String, String> identityColumnMapping) throws IntrospectionException {
    PropertyDescriptor pd = new PropertyDescriptor(variableName, obj.getClass());
    Method getter = pd.getReadMethod();
    Class<?> returnType = getter.getReturnType();
    String name = returnType.getName();
    if (name.contains("com.nouros.hrms.model")) {
      try {
        String identityColumnName = identityColumnMapping.get(substringAfterLast(name, "."));
        Object newInstance = getter.invoke(obj);
        PropertyDescriptor psd = new PropertyDescriptor(identityColumnName, newInstance.getClass());
        Method declaredMethod = psd.getReadMethod();
        Object f = declaredMethod.invoke(newInstance);   
      if (f == null) {
          f = Symbol.HYPHEN;
        }

        return f;
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NullPointerException
          | SecurityException e) {
        log.error(EXCEPTION_MESS, e.getMessage(), e);
      }
    } else {
      try {
        Method getters = pd.getReadMethod();
        Object f = getters.invoke(obj);
        if (f == null) {
          f = Symbol.HYPHEN;
        }
        return f;
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        log.error(EXCEPTION_MESS, e.getMessage(), e);
      }
    }
    return "Object Not Found";
  }

  /**
    * Invokes the setter method of an object given its property name and variable value.
    *
    * @param objects       The object to invoke the setter method on.
    * @param propertyName  The name of the property to set.
    * @param variableValue The value to set for the property.
    * @throws InstantiationException If an instantiation error occurs while invoking the setter method.
    * @throws ClassNotFoundException If the specified class is not found while invoking the setter method.
    */
  public static void invokeSetter(Object objects, String propertyName, String variableValue)
    throws InstantiationException, ClassNotFoundException {
    try {
      PropertyDescriptor pd = new PropertyDescriptor(propertyName, objects.getClass());
      Method setter = pd.getWriteMethod();
      castParameters(objects, setter, variableValue);
    } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      log.error(EXCEPTION_MESS, e.getMessage(), e);
    }
  }

 /**
   * Casts and sets the parameters of an object for a setter method given its variable value.
   *
   * @param obj           The object to set the parameter for.
   * @param setter        The setter method to invoke.
   * @param variableValue The value to set for the parameter.
   * @throws IllegalAccessException    If an illegal access occurs while invoking the setter method.
   * @throws IllegalArgumentException  If an illegal argument occurs while invoking the setter method.
   * @throws InvocationTargetException If an invocation target error occurs while invoking the setter method.
   * @throws InstantiationException    If an instantiation error occurs while invoking the setter method.
   * @throws ClassNotFoundException  If the specified class is not found while invoking the setter method.
   */
  public static void castParameters(Object obj, Method setter, String variableValue)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      InstantiationException, ClassNotFoundException {
    Class<?>[] parameterTypes = setter.getParameterTypes();
    String name = parameterTypes[0].getName();
    log.info("name is: {}", name);
    if (name.contains("Long")) {
      Long value = NumberUtils.toLong(variableValue);
      setter.invoke(obj, value);
    } else if (name.contains("Date")) {
      Timestamp ts = new Timestamp(Long.parseLong(variableValue));
      Date date = new Date(ts.getTime());
      setter.invoke(obj, date);
    } else if (name.contains("Integer")) {
      Integer value = Integer.parseInt(variableValue);
      setter.invoke(obj, value);
    } else if (name.contains("Boolean")) {
      Boolean value = Boolean.parseBoolean(variableValue);
      setter.invoke(obj, value);
    } else if (name.contains("Double")) {
      Double value = Double.valueOf(variableValue);
      setter.invoke(obj, value);
    } else if (name.contains("String")) {
      String value = String.valueOf(variableValue);
      setter.invoke(obj, value);
    } else if (name.contains("Float")) {
      Float value = Float.valueOf(variableValue);
      setter.invoke(obj, value);
    } else if (name.contains("com.nouros.hrms.model")) {
      Class<?> cls = Class.forName(name);
      try {
        Method declaredMethod = cls.getDeclaredMethod("setId", Integer.class);
        Object newInstance = cls.getConstructor().newInstance();
        declaredMethod.invoke(newInstance, Integer.parseInt(variableValue));
        setter.invoke(obj, newInstance);
      } catch (NoSuchMethodException e) {
        log.error(EXCEPTION_MESS, e.getMessage(), e);
        log.error("NoSuchMethodException occurred: {}", e.getMessage());
      } catch (SecurityException e) {
         log.error("SecurityException occurred: {}", e.getMessage());
      }
    }
  }

}
