package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.AlertEventPropertyService;
import org.measure.platform.core.entity.AlertEvent;
import org.measure.platform.core.entity.AlertEventProperty;
import org.measure.platform.core.impl.repository.AlertEventPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing AlertEventProperty.
 */
@Service
@Transactional
public class AlertEventPropertyServiceImpl implements AlertEventPropertyService {
    private final Logger log = LoggerFactory.getLogger(AlertEventPropertyServiceImpl.class);

    @Inject
    private AlertEventPropertyRepository alertEventPropertyRepository;


    /**
     * Save a AlertEventProperty.
     * @param project the entity to save
     * @return the persisted entity
     */
    public AlertEventProperty save(AlertEventProperty alertEventProperty) {
        log.debug("Request to save AlertEventProperty : {}", alertEventProperty);
        AlertEventProperty result = alertEventPropertyRepository.save(alertEventProperty);
        return result;
    }

    /**
     * Get all the AlertEventProperty
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AlertEventProperty> findAll() {
        log.debug("Request to get all AlertEventPropertys");
        List<AlertEventProperty> result = alertEventPropertyRepository.findAll();
        return result;
    }


    /**
     * Get one AlertEventProperty by id.
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AlertEventProperty findOne(Long id) {
        log.debug("Request to get AlertEventProperty : {}", id);
        AlertEventProperty project = alertEventPropertyRepository.findOne(id);
        return project;
    }

    /**
     * Delete the  AlertEventProperty by id.
     * @param id the id of the entity
     */
    public void delete(Long id) {
        alertEventPropertyRepository.delete(id);
    }

	@Override
	public List<AlertEventProperty> findAllByAlertEvent(AlertEvent alertEvent) {
		// TODO Auto-generated method stub
		return alertEventPropertyRepository.findByAlertEvent(alertEvent);
	}

}
