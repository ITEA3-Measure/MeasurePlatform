package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.MeasureReferenceService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureReference;
import org.measure.platform.core.impl.repository.MeasureReferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MeasureReference.
 */
@Service
@Transactional
public class MeasureReferenceServiceImpl implements MeasureReferenceService{

    private final Logger log = LoggerFactory.getLogger(MeasureReferenceServiceImpl.class);
    
    @Inject
    private MeasureReferenceRepository measureReferenceRepository;

    /**
     * Save a MeasureReference.
     *
     * @param MeasureReference the entity to save
     * @return the persisted entity
     */
    public MeasureReference save(MeasureReference MeasureReference) {
        log.debug("Request to save MeasureReference : {}", MeasureReference);
        MeasureReference result = measureReferenceRepository.save(MeasureReference);
        return result;
    }

    /**
     *  Get all the measureProperties.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<MeasureReference> findAll() {
        log.debug("Request to get all MeasureProperties");
        List<MeasureReference> result = measureReferenceRepository.findAll();

        return result;
    }

    /**
     *  Get one MeasureReference by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MeasureReference findOne(Long id) {
        log.debug("Request to get MeasureReference : {}", id);
        MeasureReference MeasureReference = measureReferenceRepository.findOne(id);
        return MeasureReference;
    }

    /**
     *  Delete the  MeasureReference by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeasureReference : {}", id);
        measureReferenceRepository.delete(id);
    }

	@Override
	public List<MeasureReference> findByInstance(MeasureInstance instance) {
		List<MeasureReference> result = measureReferenceRepository.findByMeasure(instance);
        return result;
	}
}
