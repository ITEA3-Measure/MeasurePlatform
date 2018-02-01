package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.AnalysisCard;
import org.measure.platform.core.entity.ProjectAnalysis;

/**
 * Service Interface for managing AnalysisCard.
 */
public interface AnalysisCardService {
    /**
     * Save a an AnalysisCard.
     * @param card the entity to save
     * @return the persisted entity
     */
	AnalysisCard save(AnalysisCard card);

    /**
     * Get all the AnalysisCard.
     * @return the list of entities
     */
    List<AnalysisCard> findAll();

    /**
     * Get all the Project Analysis of current owner.
     * @return the list of entities
     */
    List<AnalysisCard> findAllByProjectAnalysis(ProjectAnalysis projectAnalysis);

    /**
     * Get the "id" AnalysisCard.
     * @param id the id of the entity
     * @return the entity
     */
    AnalysisCard findOne(Long id);

    /**
     * Delete the "id" project.
     * @param id the id of the entity
     */
    void delete(Long id);


}
