package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.AlertEvent;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.entity.ProjectAnalysis;

/**
 * Service Interface for managing AlertEvent.
 */
public interface AlertEventService {
	/**
	 * Save a alert event.
	 * 
	 * @param project the entity to save
	 * @return the persisted entity
	 */
	AlertEvent save(AlertEvent alertEvent);

	/**
	 * Get all the alert events.
	 * 
	 * @return the list of entities
	 */
	List<AlertEvent> findAll();

	/**
	 * Get all the alert events of current project.
	 * 
	 * @return the list of entities
	 */
	List<AlertEvent> findAllByProject(Project project);
	
	/**
	 * Get all the alert events of current project analysis.
	 * 
	 * @return the list of entities
	 */
	List<AlertEvent> findAllByProjectAnalysis(ProjectAnalysis projectAnalysis);

	/**
	 * Get the "id" alert event.
	 * 
	 * @param id the id of the entity
	 * @return the entity
	 */
	AlertEvent findOne(Long id);

	/**
	 * Delete the "id" alert event.
	 * 
	 * @param id the id of the entity
	 */
	void delete(Long id);



}
