package org.measure.platform.core.data.api;

import java.util.List;

import org.measure.platform.core.data.entity.AlertEvent;
import org.measure.platform.core.data.entity.Project;
import org.measure.platform.core.data.entity.ProjectAnalysis;

/**
 * Service Interface for managing AlertEvent.
 */
public interface IAlertEventService {
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

	List<AlertEvent> findByProjectAndEventType(Project project, String analysisTool, String name);

	List<AlertEvent> findByProjectEventTypeAndProp(Project project, String analysisTool, String name, String property,String value);



}
