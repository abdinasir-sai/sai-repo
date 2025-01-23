package com.nouros.hrms.controller.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nouros.hrms.controller.HibernateController;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
@Primary
@RestController
@RequestMapping("/HibernateEntry")
public class HibernateControllerImpl implements HibernateController 
{

	private static final String POJO_KEY = "pojo_key";
	private static final String TABLE_NAME = "table_name";
	private static final String COLUMN_NAME = "column_name";

	@Override
	public List<ObjectNode> getEntityDetailsFromPackageAsJson() {
		String packageName = "com.nouros.hrms.model"; 
		 return getEntityDetailsFromPackageAsJson(packageName);
	}
	
	public static List<ObjectNode> getEntityDetailsFromPackageAsJson(String packageName) {
        List<ObjectNode> jsonList = new ArrayList<>();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

        for (Class<?> entityClass : entityClasses) {
            jsonList.addAll(getEntityDetailsAsJson(entityClass));
        }

        return jsonList;
    }

    public static List<ObjectNode> getEntityDetailsAsJson(Class<?> clazz) {
        List<ObjectNode> jsonList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        // Check if the class is annotated with @Entity
        if (clazz.isAnnotationPresent(Entity.class)) {
            // Get the table name
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            String tableName = tableAnnotation != null ? tableAnnotation.name() : clazz.getSimpleName();

            // Get the fields and their column names
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();

                    ObjectNode jsonNode = mapper.createObjectNode();
                    jsonNode.put(COLUMN_NAME, columnName);
                    jsonNode.put(TABLE_NAME, tableName);
                    jsonNode.put(POJO_KEY, field.getName());
                    jsonList.add(jsonNode);
                } else if (field.isAnnotationPresent(JoinColumn.class)) {
                    JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
                    String columnName = joinColumnAnnotation.name();

                    ObjectNode jsonNode = mapper.createObjectNode();
                    jsonNode.put(COLUMN_NAME, columnName);
                    jsonNode.put(TABLE_NAME, tableName);
                    jsonNode.put(POJO_KEY, field.getName());
                    jsonList.add(jsonNode);

                    jsonList.addAll(getForeignKeyDetailsAsJson(field));
                }
            }
        }
        return jsonList;
    }

    public static List<ObjectNode> getForeignKeyDetailsAsJson(Field field) {
        List<ObjectNode> jsonList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Class<?> fieldType = field.getType();

        if (fieldType.isAnnotationPresent(Entity.class)) {
            // Get the table name
            Table tableAnnotation = fieldType.getAnnotation(Table.class);
            String tableName = tableAnnotation != null ? tableAnnotation.name() : fieldType.getSimpleName();

            Field[] foreignFields = fieldType.getDeclaredFields();
            for (Field foreignField : foreignFields) {
                if (foreignField.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = foreignField.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();

                    ObjectNode jsonNode = mapper.createObjectNode();
                    jsonNode.put(COLUMN_NAME, columnName);
                    jsonNode.put(TABLE_NAME, tableName);
                    jsonNode.put(POJO_KEY, foreignField.getName());
                    jsonList.add(jsonNode);
                }
            }
        }
        return jsonList;
    }

}
