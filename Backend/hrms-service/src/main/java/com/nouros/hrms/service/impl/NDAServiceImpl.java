package com.nouros.hrms.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nouros.hrms.model.NDA;
import com.nouros.hrms.repository.NDARepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.NDAService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.wrapper.ClausesDto;
import com.nouros.hrms.wrapper.NDARequestDto;
import com.nouros.hrms.wrapper.NDAResponseDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class NDAServiceImpl extends AbstractService<Integer,NDA> implements NDAService {

	private static final String CLOUSES = "clouses";

	public NDAServiceImpl(GenericRepository<NDA> repository) {
		super(repository, NDA.class);
	}

	private static final Logger log = LogManager.getLogger(NDAServiceImpl.class);

	@Autowired
	private NDARepository ndaRepository;

	@Autowired
	private EntityManager entityManager;
 
	@Override
	public NDAResponseDto createNDA(NDARequestDto ndaRequestDto) {
		return ndaResponse(ndaRequestDto);
	}

	private NDAResponseDto ndaResponse(NDARequestDto ndaRequestDto) {
		NDA nda = new NDA();
		NDAResponseDto ndaResponseDto = new NDAResponseDto();
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String todayDate = currentDate.format(formatter);
		getNDAHeaders(ndaRequestDto, ndaResponseDto, todayDate);
		List<ClausesDto> list = new ArrayList<>();
		getClauses(ndaRequestDto, list);
		ndaResponseDto.setClouses(list);
		getFooters(ndaResponseDto, todayDate, ndaRequestDto);
		ndaResponseDto.setRequestParam(setRequestParam(ndaRequestDto));
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = objectMapper.writeValueAsString(ndaResponseDto);
			nda.setJson(jsonString);
			nda = ndaRepository.save(nda);
			ndaResponseDto.setId(nda.getId());
			nda.setJson(objectMapper.writeValueAsString(ndaResponseDto));
			ndaRepository.save(nda);
		} catch (JsonProcessingException e) {
			log.error("Error inside @class NDAServiceimpl @Method ndaResponse {}", Utils.getStackTrace(e));
		}
		return ndaResponseDto;
	}

	private void getFooters(NDAResponseDto ndaResponseDto, String todayDate, NDARequestDto ndaRequestDto) {
		ndaResponseDto.setFooter1(
				"IN WITNESS WHEREOF, the Parties have executed this Non-Disclosure Agreement as of the Effective Date first above written.\n"
						+ " \n" + "Aramco Digital Company\n" + "By: Robert Horny\n" + "Name: Robert\n"
						+ "Title: Director \n" + "Date: " + todayDate + "\n" + " \n" + ""
						+ ndaRequestDto.getCompanyName() + "\n" + "By: John Carter \n" + "Name: John \n"
						+ "Title: Senior Director\n" + "Date: " + todayDate + "\n" + " \n"
						+ "Please bear in mind the importance of engaging a qualified legal professional to review this Non-Disclosure Agreement, ensuring its alignment with your unique requirements and compliance with applicable legal standards.");
		ndaResponseDto.setFooter2("");
	}

	private void getNDAHeaders(NDARequestDto ndaRequestDto, NDAResponseDto ndaResponseDto, String todayDate) {
		ndaResponseDto.setHeader1("NON-DISCLOSURE AGREEMENT");
		ndaResponseDto.setHeader2("This Non-Disclosure Agreement (the 'Agreement') is made as of " + todayDate
				+ ", by and between Aramco Digital Company, with an office at " + ndaRequestDto.getCompanyAddress()
				+ ", ('Disclosing Party'), and " + ndaRequestDto.getCompanyName() + ", with an office at "
				+ ndaRequestDto.getCompanyAddress()
				+ ", ('Receiving Party'). WHEREAS, Aramco Digital Company proposes to engage "
				+ ndaRequestDto.getCompanyName()
				+ " to [Our project is basically hrms] (the 'Purpose'); WHEREAS, Aramco Digital Company has a total budget "
				+ ndaRequestDto.getProjectBudget() + " " + ndaRequestDto.getScopeOfProject() + " "
				+ ndaRequestDto.getCompanyAddress()
				+ " (the 'Project'); WHEREAS, the execution of the Project will require "
				+ ndaRequestDto.getCompanyName() + " to " + ndaRequestDto.getScopeOfProject() + "; WHEREAS, "
				+ ndaRequestDto.getCompanyName()
				+ " will receive access to confidential and proprietary data which is vital to the Project and must be protected; Now, therefore, in consideration of the mutual covenants and agreements herein contained, the parties hereto agree as follows:");
	}

	private void getClauses(NDARequestDto ndaRequestDto, List<ClausesDto> list) {
		ClausesDto clausesDto1 = new ClausesDto();
		ClausesDto clausesDto2 = new ClausesDto();
		ClausesDto clausesDto3 = new ClausesDto();
		ClausesDto clausesDto4 = new ClausesDto();
		ClausesDto clausesDto5 = new ClausesDto();
		ClausesDto clausesDto6 = new ClausesDto();
		ClausesDto clausesDto7 = new ClausesDto();

		clausesDto1.setName("Confidential Information");
		clausesDto1.setDescription(
				"Confidential Information shall mean all data, materials, products, technology, computer programs, specifications, manuals, business plans, financial information, software, marketing plans, including without limitation the data, methodologies, and information "
						+ ndaRequestDto.getScopeOfProject()
						+ ", operational efficiencies, budget utilizations, strategic project details, and all other information disclosed or submitted by Aramco Digital Company to "
						+ ndaRequestDto.getCompanyName() + ", except as excluded below");
		clausesDto2.setName("Exclusions from Confidential Information");
		clausesDto2.setDescription(
				"Confidential Information does not include any information that: (a) is or becomes publicly known without breach of this Agreement; (b) was known to the Receiving Party prior to its disclosure by the Disclosing Party without breach of any obligation owed to the Disclosing Party; (c) is received from a third party without breach of any obligation owed to the Disclosing Party; or (d) was independently developed by the Receiving Party.");
		clausesDto3.setName("Obligations of Receiving Party");
		clausesDto3.setDescription(
				"The Receiving Party agrees to maintain the Confidential Information in strict confidence, not to disclose it to any third parties, and not to use it for any purpose except to carry out the discussed Purpose. All employees or subcontractors with access to Confidential Information must agree in writing to comply with the terms and conditions of this Agreement.");
		clausesDto4.setName("Duration of Confidentiality");
		clausesDto4.setDescription(
				"The Receiving Party’s duty to hold the Confidential Information in confidence shall remain in effect for a period of "
						+ ndaRequestDto.getDurationOfConfidentiality() + " after the termination of this Agreement.");
		clausesDto5.setName("Return or Destruction of Confidential Information");
		clausesDto5.setDescription(
				"Upon the termination of this Agreement, the Receiving Party will return or destroy all copies of the Disclosing Party's Confidential Information, as directed by the Disclosing Party.");
		clausesDto6.setName("Non-Solicitation");
		clausesDto6.setDescription("For a period of " + ndaRequestDto.getDurationOfConfidentiality()
				+ " after the termination of this Agreement, the Receiving Party will not directly or indirectly solicit, induce or attempt to induce any employee of the Disclosing Party to terminate his or her employment with the Disclosing Party.");
		clausesDto7.setName("No Warranty");
		clausesDto7.setDescription(
				"The Confidential Information is provided \"as is.\" The Disclosing Party makes no warranties, express, implied or otherwise, regarding its accuracy, completeness, or performance");
		list.add(clausesDto1);
		list.add(clausesDto2);
		list.add(clausesDto3);
		list.add(clausesDto4);
		list.add(clausesDto5);
		list.add(clausesDto6);
		list.add(clausesDto7);
	}

	private String setRequestParam(NDARequestDto ndaRequestDto) {
		try {
			ObjectMapper object = new ObjectMapper();
			return object.writeValueAsString(ndaRequestDto);
		} catch (Exception e) {
			log.error("Error inside @class NDAServiceimpl @Method setRequestParam {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public NDAResponseDto addClouse(NDARequestDto ndaRequestDto) {
		NDA ndaOptional = super.findById(ndaRequestDto.getId());
		if (ndaOptional != null) {
			NDA nda = ndaOptional;
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(nda.getJson());
				String description = fetchDescriptionByClouseName(ndaRequestDto);
				if (description != null && !description.isEmpty() && !description.trim().isEmpty()) {
					addClouse(ndaRequestDto, description, objectMapper, jsonNode);
					nda.setJson(objectMapper.writeValueAsString(jsonNode));
				}
				ndaRepository.save(nda);
				return objectMapper.readValue(objectMapper.writeValueAsString(jsonNode), NDAResponseDto.class);
			} catch (IOException e) {
				log.error("Error inside @class NDAServiceimpl @Method addClouse {}", Utils.getStackTrace(e));
				throw new BusinessException("Unable to add clouse");
			}
		}
		throw new BusinessException("Unable to add clouse");
	}

	@Override
	public NDAResponseDto removeClouse(NDARequestDto ndaRequestDto) {
		NDA ndaOptional = super.findById(ndaRequestDto.getId());
		if (ndaOptional != null) {
			NDA nda = ndaOptional;

			try {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(nda.getJson());
				removeClause(jsonNode, ndaRequestDto.getExistingClouseName());
				String modifiedJsonString = objectMapper.writeValueAsString(jsonNode);
				log.info("Modified JSON String:\n {} " , modifiedJsonString);
				nda.setJson(objectMapper.writeValueAsString(jsonNode));
				ndaRepository.save(nda);
				return objectMapper.readValue(objectMapper.writeValueAsString(jsonNode), NDAResponseDto.class);
			} catch (IOException e) {
				log.error("Error inside @class NDAServiceimpl @Method removeClouse {}", Utils.getStackTrace(e));
			}
		}
		return ndaResponse(ndaRequestDto);
	}

	private void addClouse(NDARequestDto ndaRequestDto, String description, ObjectMapper objectMapper,
			JsonNode jsonNode) throws JsonProcessingException {
		if (jsonNode.has(CLOUSES)) {
			ArrayNode clausesNode = (ArrayNode) jsonNode.get(CLOUSES);
			ObjectNode newClause = objectMapper.createObjectNode();
			newClause.put("name", ndaRequestDto.getClouseNameToBeAdded());
			newClause.put("description", description);
			int indexToAdd = getIndexToAdd(clausesNode, ndaRequestDto.getExistingClouseName(),
					ndaRequestDto.getClousePosition());
			if (indexToAdd != -1) {
				clausesNode.insert(indexToAdd, newClause);
				String modifiedJsonString = objectMapper.writeValueAsString(jsonNode);
				log.info("clausesNode is {}", clausesNode);
				log.info("Modified JSON String: {} \n" , modifiedJsonString);

			} else {
				throw new BusinessException("The provided clause name was not found in the 'clauses' array.");
			}
		} else {
			throw new BusinessException("The 'clouses' key is not present in the JSON data.");
		}
	}

	@SuppressWarnings("unchecked")
	private String fetchDescriptionByClouseName(NDARequestDto ndaRequestDto) {
		String nameString = ndaRequestDto.getClouseNameToBeAdded();
		String sqlQuery = "SELECT DESCRIPTION FROM NDA_CLOUSES_NAME WHERE NAME = :nameString";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("nameString", nameString);
		List<String> descriptions = query.getResultList();
		if (!descriptions.isEmpty()) {
			return descriptions.get(0);
		}
		return null;
	}

	private static int getIndexToAdd(ArrayNode clausesNode, String targetClauseName, String addPosition) {
		Iterator<JsonNode> iterator = clausesNode.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			JsonNode clauseNode = iterator.next();
			String name = clauseNode.get("name").asText();
			if (addPosition.equalsIgnoreCase("after") && name.equals(targetClauseName)) {
				return index + 1;
			} else if (addPosition.equalsIgnoreCase("before") && name.equals(targetClauseName)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	private static int getIndexToRemove(ArrayNode clausesNode, String targetClauseName) {
		Iterator<JsonNode> iterator = clausesNode.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			JsonNode clauseNode = iterator.next();
			String name = clauseNode.get("name").asText();
			if (name.equals(targetClauseName)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	private static void removeClause(JsonNode jsonNode, String targetClauseName) {
		if (jsonNode.has(CLOUSES)) {
			ArrayNode clausesNode = (ArrayNode) jsonNode.get(CLOUSES);
			int indexToRemove = getIndexToRemove(clausesNode, targetClauseName);
			if (indexToRemove != -1) {
				clausesNode.remove(indexToRemove);
			}
		} else {
			throw new BusinessException("The 'clauses' key is not present in the JSON data.");
		}
	}

	@Override
	public NDAResponseDto findNDAById(Integer id) {
		NDAResponseDto ndaDto = new NDAResponseDto();
	
      NDA optionalNDA = super.findById(id);
	    if (optionalNDA == null) {
	        log.error("NDA not found with id: {}", id);
	        return ndaDto;
	    }
	    
	    NDA nda = optionalNDA;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(nda.getJson());
			return objectMapper.readValue(objectMapper.writeValueAsString(jsonNode), NDAResponseDto.class);
		} catch (Exception e) {
			log.error("Error inside @class NDAServiceimpl @Method findNDAById {}", Utils.getStackTrace(e));
		}
		return ndaDto;

	}

}
