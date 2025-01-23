package com.nouros.hrms.service.generic;



import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.enttribe.utils.Utils;
import com.nouros.hrms.repository.generic.GenericRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.enttribe.core.generic.dao.impl.CustomRsqlVisitor;
import com.enttribe.core.generic.dao.impl.HibernateGenericDao;
import com.enttribe.core.generic.exceptions.application.BusinessException;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import com.enttribe.core.generic.dao.impl.*;

/**
 * The AbstractService class is an abstract implementation of the GenericService
 * interface. This class provides a base implementation for common service
 * functionalities.
 *
 * @param <T> The type parameter representing the entity that this service
 *            manages.
 */
public abstract class AbstractService<Pk,T> implements GenericService<Pk,T> {

	protected final Logger logger = LoggerFactory.getLogger(AbstractService.class);

	/**
	 * Generic repository for the entity being audited.
	 */
	private GenericRepository<T> repository;
	/**
	 * EntityManager for interacting with the persistence layer.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Class type of the entity being audited.
	 */
	private Class<T> entityType;

	/**
	 * Constructor for AbstractService.
	 * 
	 * @param repository GenericRepository for the entity being managed by the
	 *                   service.
	 * @param entityType Class type of the entity being managed by the service.
	 */
	protected AbstractService(GenericRepository<T> repository, Class<T> entityType) {
		this.repository = repository;
		this.entityType = entityType;
	}

	/**
	 * Creates a new entity and saves it to the repository.
	 *
	 * @param entity The entity to be created and saved.
	 * @return The created entity, with any auto-generated fields (such as ID)
	 *         populated.
	 */

	@Override
	public T create(T entity) {
		return repository.saveAndFlush(entity);
	}

	/**
	 * Updates an existing entity and saves the changes to the repository.
	 * 
	 * @param entity The updated entity to be saved.
	 * @return The updated entity with any changes made by the repository (such as
	 *         auto-generated fields)
	 */
	@Override
	public T update(T entity) {
		return repository.saveAndFlush(entity);
	}

	/**
	 * Finds and retrieves an entity by its ID.
	 *
	 * @param id The ID of the entity to be retrieved.
	 * @return An Optional containing the found entity, or empty if no entity with
	 *         the given ID is found.
	 */
	@Override
	public T findById(Pk id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<T> cq = cb.createQuery(entityType);
	    Root<T> root = cq.from(entityType);
	    String primaryKeyName = getPrimaryKeyFieldName();
	    cq.select(root).where(cb.equal(root.get(primaryKeyName), id));
	    
	    T entityObject = null;
	    try {
	    entityObject = entityManager.createQuery(cq).getSingleResult();
	    
	    }catch(NoResultException e) {
	    	logger.debug("data not found for id :{} class :{} e :{}",id,entityType,e.getMessage());
	    	return  entityObject;
	    }catch(BusinessException ex) {
	    	logger.debug("data not found for id :{} class :{} ",id,entityType ,ex.getMessage());
	    	return entityObject;
	    }catch(Exception ex1) {
	    	logger.debug("data not found for id :{} class :{} ",id,entityType ,ex1.getMessage());
	    	return  entityObject;
	    }
		return entityObject;
	}

	/**
	 * Retrieves a list of entities by their IDs.
	 *
	 * @param id A List of IDs of entities to be retrieved.
	 * @return A List of found entities, empty list if no entities with the given
	 *         IDs are found.
	 */
	@Override
	public List<T> findAllById(List<Integer> id) {
		return repository.findAllById(id);
	}

	/**
	 * Deletes an entity by its ID.
	 *
	 * @param id The ID of the entity to be deleted.
	 */
	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	/**
	 * Deletes a list of entities.
	 *
	 * @param entities A List of entities to be deleted.
	 */
	@Override
	public void deleteAll(List<T> entities) {
		repository.deleteAll(entities);
	}

	/**
	 * Retrieves the audit history for a specific entity.
	 *
	 * @param id    The ID of the entity to retrieve the audit history for.
	 * @param limit The maximum number of history records to retrieve.
	 * @param skip  Number of records to skip.
	 * @return A JSON string containing the audit history of the entity
	 */

	@Override
	public String auditHistory(int id, Integer limit, Integer skip) {
		HibernateGenericDao hibernateGenericDao = new HibernateGenericDao(entityType, entityManager);
		try {
			List list = hibernateGenericDao.findAudit(id);
			return list.toString();
		} catch (Exception e) {

			logger.error("Error Inside @class: {} @Method: auditHistory() {}", this.getClass().getName(),
					e.getMessage());

			return null;
		}
	}

	/**
	 * Search for entities that match the given query.
	 *
	 * @param query     The RSQL query to use for searching entities.
	 * @param offset    The starting position for the search results.
	 * @param size      The maximum number of search results to return.
	 * @param orderby   The field to use for ordering the search results.
	 * @param orderType Order direction. Valid values are 'asc' and 'desc'.
	 *
	 * @return List of entities that match the given query.
	 */
	@Override
	public List<T> search(String query, Integer offset, Integer size, String orderby, String orderType) {
		logger.info("Inside Search Method");
		if (query == null) {
			return Collections.emptyList();
		}
		return searchByFilter(entityType, query, orderby, orderType, offset, size);
	}

	@SuppressWarnings("unchecked")
	private List<T> searchByFilter(Class<?> type, String query, String orderBy, String orderType, Integer offset,
			Integer size) {

		logger.debug("Inside searchByFilter Method  , with query : {}" ,query);
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<?> criteria = builder.createQuery(type);
		Root<?> root = criteria.from(type);
		System.out.print("Root information For Search:  " + root.toString());
		System.out.print("Root information class For Search : " + root.getClass());
		System.out.print("Root information java type For Search : "+ root.getJavaType());
		logger.debug("Root information For Search:  {}" ,root);
		logger.debug("Root information class For Search : {}" , root.getClass());
		logger.debug("Root information java type For Search : {}", root.getJavaType());
		try {
			Node rootNode = RsqlParserUtil.parse(query);
			CustomRsqlVisitor<T> visitor = new CustomRsqlVisitor<>();
			Specification<T> specification = rootNode.accept(visitor);
			Predicate predicate = specification.toPredicate((Root<T>) root, criteria, builder);

			criteria.where(predicate);
			if (orderBy != null && orderType != null) {
				if ("desc".equalsIgnoreCase(orderType)) {
					criteria.orderBy(entityManager.getCriteriaBuilder().desc(root.get(orderBy)));
				} else {
					criteria.orderBy(entityManager.getCriteriaBuilder().asc(root.get(orderBy)));
				}
			}
			TypedQuery<?> typedQuery = entityManager.createQuery(criteria);
			if (offset >= 0 && size >= 0) {
				typedQuery.setMaxResults(size);
				typedQuery.setFirstResult(offset);
			}
			return (List<T>) typedQuery.getResultList();
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));

		}
		return Collections.emptyList();
	}

	@Override
	public Long count(String query) {
		logger.info("Inside count Method ");
		if (query == null) {
			return null;
		}

		return countByFilter(entityType, query);
	}

	@SuppressWarnings("unchecked")
	private Long countByFilter(Class<?> t, String query) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<?> root = criteria.from(t);
		System.out.print("Root information For Count:  " + root.toString());
		System.out.print("Root information class For Count : " + root.getClass());
		System.out.print("Root information java type For Count : "+ root.getJavaType());
		logger.debug("Root information For Count:  {}" ,root);
		logger.debug("Root information class For Count : {}" , root.getClass());
		logger.debug("Root information java type For Count : {}", root.getJavaType());
		Node rootNode = new RSQLParser().parse(query);
		Specification<T> specification = rootNode.accept(new CustomRsqlVisitor<T>());
		Predicate predicate = specification.toPredicate((Root<T>) root, criteria, builder);
		criteria.where(predicate);
		criteria.select(entityManager.getCriteriaBuilder().countDistinct(root));
		return entityManager.createQuery(criteria).getSingleResult();
	}

	private String getPrimaryKeyFieldName() {
	    for (Field field : entityType.getDeclaredFields()) {
	      if (field.isAnnotationPresent(Id.class)) {
	        return field.getName();
	      }
	    }
	    throw new RuntimeException("No primary key field found in the entity class: " + entityType.getName());
	  }
}
