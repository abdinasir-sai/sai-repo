package com.nouros.hrms.service.generic;

import java.util.List;
import java.util.Optional;

/**
 * A generic service interface providing CRUD operations for entities of type T.
 * @param <T> The entity type.
 */
public interface GenericService<Pk,T> {
	
	/**
     * Searches for entities based on the provided query, offset, size, orderby, and orderType.
     * @param query The search query.
     * @param offset The offset for pagination.
     * @param size The maximum number of entities to retrieve.
     * @param orderby The field to order the results by.
     * @param orderType The type of order (e.g., "asc" or "desc").
     * @return A list of entities matching the search criteria.
     */
  List<T> search(String query, Integer offset, Integer size, String orderby, String orderType);
	
	/**
     * Counts the total number of entities matching the provided query.
     * @param query The search query.
     * @return The count of entities matching the query.
     */
  Long count(String query);

	/**
     * Creates a new entity.
     * @param entity The entity to create.
     * @return The created entity.
     */
  T create(T entity);

	/**
     * Updates an existing entity.
     * @param entity The entity to update.
     * @return The updated entity.
     */
  T update(T entity);

	/**
     * Finds an entity by its unique identifier.
     * @param id The identifier of the entity to find.
     * @return An Optional containing the entity if found, or empty if not found.
     */
  T findById(Pk id);	

	/**
     * Finds multiple entities by their unique identifiers.
     * @param id The list of identifiers of the entities to find.
     * @return A list of entities found.
     */
  List<T> findAllById(List<Integer> id);

	/**
     * Deletes an entity by its unique identifier.
     * @param id The identifier of the entity to delete.
     */
  void deleteById(Integer id);


	/**
     * Deletes a list of entities.
     * @param entities The list of entities to delete.
     */
  void deleteAll(List<T> entities);

	/**
     * Retrieves the audit history of an entity.
     * @param id The identifier of the entity.
     * @param limit The maximum number of audit entries to retrieve.
     * @param skip The number of audit entries to skip.
     * @return The audit history of the entity as a string.
     */
  String auditHistory(int id, Integer limit, Integer skip);
  
}
