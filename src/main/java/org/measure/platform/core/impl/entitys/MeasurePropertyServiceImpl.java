package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.MeasurePropertyService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;
import org.measure.platform.core.impl.repository.MeasurePropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MeasureProperty.
 */
@Service
@Transactional
public class MeasurePropertyServiceImpl implements MeasurePropertyService{

    private final Logger log = LoggerFactory.getLogger(MeasurePropertyServiceImpl.class);
    
    @Inject
    private MeasurePropertyRepository measurePropertyRepository;

    /**
     * Save a measureProperty.
     *
     * @param measureProperty the entity to save
     * @return the persisted entity
     */
    public MeasureProperty save(MeasureProperty measureProperty) {
        log.debug("Request to save MeasureProperty : {}", measureProperty);
        MeasureProperty result = measurePropertyRepository.save(measureProperty);
        return result;
    }

    /**
     *  Get all the measureProperties.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<MeasureProperty> findAll() {
        log.debug("Request to get all MeasureProperties");
        List<MeasureProperty> result = measurePropertyRepository.findAll();

        return result;
    }

    /**
     *  Get one measureProperty by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MeasureProperty findOne(Long id) {
        log.debug("Request to get MeasureProperty : {}", id);
        MeasureProperty measureProperty = measurePropertyRepository.findOne(id);
        return measureProperty;
    }

    /**
     *  Delete the  measureProperty by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeasureProperty : {}", id);
        measurePropertyRepository.delete(id);
    }

	@Override
	public List<MeasureProperty> findByInstance(MeasureInstance instance) {
		List<MeasureProperty> result = measurePropertyRepository.findByMeasure(instance);
        return result;
	}
}
