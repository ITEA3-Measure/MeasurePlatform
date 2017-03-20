package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.MeasureView;

/**
 * Service Interface for managing MeasureView.
 */
public interface MeasureViewService {

	/**
	 * Save a measureView.
	 *
	 * @param measureView
	 *            the entity to save
	 * @return the persisted entity
	 */
	MeasureView save(MeasureView measureView);

	/**
	 * Get all the measureViews.
	 * 
	 * @return the list of entities
	 */
	List<MeasureView> findAll();

	/**
	 * Get the "id" measureView.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	MeasureView findOne(Long id);

	/**
	 * Delete the "id" measureView.
	 *
	 * @param id
	 *            the id of the entity
	 */
	void delete(Long id);

	List<MeasureView> findByProject(Long id);

	List<MeasureView> findByProjectOverview(Long id);

	List<MeasureView> findByPhase(Long id);

	List<MeasureView> findByPhaseOverview(Long id);

	List<MeasureView> findByDashboard(Long id);
}
