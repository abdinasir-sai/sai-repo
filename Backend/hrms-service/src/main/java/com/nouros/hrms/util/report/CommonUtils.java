package com.nouros.hrms.util.report;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;

@Service
public class CommonUtils {

	public CommonUtils() {

		log.info("Inside @CommonUtils constructor");
	}

	private static final Logger log = LogManager.getLogger(ExcelUtils.class);
	
	public Integer getCustomerId()
	  {
		  try {
		  Integer customerId = null;
			CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
			if (customerInfo != null && customerInfo.getCustomerWrapper()!= null && customerInfo.getCustomerWrapper().getId()!=null) {
				log.debug(" Inside @getCustomerId CustomerWrapper is :{}", customerInfo.getCustomerWrapper());
				 customerId = customerInfo.getCustomerWrapper().getId();
			}
			log.debug(" Inside @getCustomerId customerId is :{}", customerId);
		 return customerId;
		  }
		  catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	  }




	public static Date parseDateInDayMonthYearFormat(String inputDate) {
		log.debug("inputDate is : {}", inputDate);

		if (inputDate != null) {
			// Normalize the input date by removing trailing periods and trimming
			inputDate = inputDate.trim().replaceAll("\\.$", "");
			log.debug("inputDate after normalization is : {}", inputDate);

			inputDate = toTitleCase(inputDate); // Convert date string to Title Case
			log.debug("inputDate after calling toTitleCase is : {}", inputDate);

			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// Define a list of date patterns to handle different formats
			List<String> datePatterns = new ArrayList<>();
			datePatterns.add("dd-MM-yy");
			datePatterns.add("dd-MM-yyyy");
			datePatterns.add("dd-MMM-yy");
			datePatterns.add("dd-MMMM-yy");
			datePatterns.add("dd-MMM-yyyy");
			datePatterns.add("dd-MMMM-yyyy");
			datePatterns.add("MMM-yyyy");
			datePatterns.add("MMMM-yyyy");
			datePatterns.add("yyyy.MM");

			// Patterns for single-digit day with various formats
			datePatterns.add("d-MM-yy");
			datePatterns.add("d-MM-yyyy");
			datePatterns.add("d-MMM-yy");
			datePatterns.add("d-MMMM-yy");
			datePatterns.add("d-MMM-yyyy");
			datePatterns.add("d-MMMM-yyyy");
			datePatterns.add("dd-MMM yyyy");

			// Handle year-only format (e.g., 2018)
			if (inputDate.matches("^\\d{4}$")) {
				inputDate = "01-01-" + inputDate;
				log.debug("Updated year-only inputDate is : {}", inputDate);
			}

			// Handle month and year (e.g., May 2022 or Aug. 2018)
			if (inputDate.matches("^[a-zA-Z]{3,}(\\.)? \\d{4}$")) {
				inputDate = "01-" + inputDate.replace(".", "");
				log.debug("Updated month and year inputDate is : {}", inputDate);
			}

			// Handle year and month in dotted format (e.g., 2022.9)
			if (inputDate.matches("^\\d{4}\\.\\d{1,2}$")) {
				String[] parts = inputDate.split("\\.");
				inputDate = "01-" + monthNumberToName(parts[1]) + "-" + parts[0];
				log.debug("Updated year and month dotted inputDate is : {}", inputDate);
			}

			for (String pattern : datePatterns) {
				String formattedDate = parseAndFormatDate(inputDate, pattern, outputFormatter);
				if (formattedDate != null) {
					log.debug("formattedDate is : {} , pattern is : {}", formattedDate, pattern);
					return java.sql.Date.valueOf(formattedDate);
				}
			}
		}

		log.error("Invalid Date Format or Out of Range: {}", inputDate);
		return null;
	}

	private static String parseAndFormatDate(String date, String pattern, DateTimeFormatter outputFormatter) {
		try {
			log.debug("Inside method @parseAndFormatDate");
			// Attempt to parse using the provided pattern
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
			LocalDate parsedDate = LocalDate.parse(date, inputFormatter);

			// Adjust year if it exceeds the current year by more than 5 years
			LocalDate currentDate = LocalDate.now();
			int currentYear = currentDate.getYear();

			if (parsedDate.getYear() - currentYear > 5) {
				parsedDate = parsedDate.withYear(parsedDate.getYear() - 100); // Set year to previous decade

			}
			log.debug("parsedDate is : {}", parsedDate);

			return parsedDate.format(outputFormatter);
		} catch (DateTimeParseException e) {
			// Pattern did not match, return null and move to the next pattern
			return null;
		}
	}

	// Convert input date string to title case to match "MMM" or "MMMM" patterns
	private static String toTitleCase(String input) {
		log.debug("Inside method @toTitleCase");
		String[] parts = input.split("-");
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].length() > 1) {
				parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1).toLowerCase();
			}
		}
		return String.join("-", parts);
	}

	

	private static String monthNumberToName(String monthNumber) {
		log.debug("Inside method @monthNumberToName");
		try {
			int month = Integer.parseInt(monthNumber);
			return Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		} catch (Exception e) {
			log.error("Invalid month number: {}", monthNumber, e);
			return null;
		}
	}
	
	public Double getAverageInterviewScore(List<Integer> scoreList) {
		log.debug("Inside method getAverageInterviewScore : {}",scoreList);
		Double averageScore=0.0;
		try {
			if(scoreList!=null && !scoreList.isEmpty()) {
				log.debug("getAverageInterviewScore list size: {}",scoreList.size());
				averageScore= scoreList.stream().mapToInt(Integer::intValue).average().orElse(0.0);
				log.debug("getAverageInterviewScore averageScore: {}",averageScore);
				return averageScore;
			}
		}
		catch(Exception e){
			log.error("Exception occured while calaculating average : {}",e);
		}
		return averageScore;
	}

}
