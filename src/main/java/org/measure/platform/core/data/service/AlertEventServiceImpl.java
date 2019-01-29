package org.measure.platform.core.data.service;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.data.api.IAlertEventPropertyService;
import org.measure.platform.core.data.api.IAlertEventService;
import org.measure.platform.core.data.entity.AlertEvent;
import org.measure.platform.core.data.entity.AlertEventProperty;
import org.measure.platform.core.data.entity.Project;
import org.measure.platform.core.data.entity.ProjectAnalysis;
import org.measure.platform.core.data.querys.AlertEventRepository;
import org.measure.platform.service.analysis.api.IAlertEngineService;
import org.measure.platform.service.analysis.api.IAlertSubscriptionManager;
import org.measure.platform.service.analysis.api.IAnalysisCatalogueService;
import org.measure.platform.service.analysis.data.alert.AlertSubscription;
import org.measure.platform.service.analysis.data.alert.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class AlertEventServiceImpl implements IAlertEventService {
	private final Logger log = LoggerFactory.getLogger(AlertEventServiceImpl.class);

	@Inject
	private AlertEventRepository alertEventRepository;

	@Inject
	private IAlertEventPropertyService alertEventPropertyService;
	
    @Inject
    private IAlertEngineService subscriptionManager;


	/**
	 * Save a Alert Event.
	 * 
	 * @param AlertEvent
	 *            the entity to save
	 * @return the persisted entity
	 */
	public AlertEvent save(AlertEvent alertEvent) {
		log.debug("Request to save AlertEvent : {}", alertEvent);
		AlertEvent result = alertEventRepository.save(alertEvent);
		return result;
	}

	/**
	 * Get all the AlertEvents.
	 * 
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<AlertEvent> findAll() {
		log.debug("Request to get all Projects");
		List<AlertEvent> result = alertEventRepository.findAll();
		return result;
	}

	/**
	 * Get one AlertEvent by id.
	 * 
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public AlertEvent findOne(Long id) {
		log.debug("Request to get Project : {}", id);
		AlertEvent result = alertEventRepository.findOne(id);
		return result;
	}

	/**
	 * Delete the AlertEvent result by id.
	 * 
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		AlertEvent alertEvent = alertEventRepository.findOne(id);
		for (AlertEventProperty instance : alertEventPropertyService.findAllByAlertEvent(alertEvent)) {
			alertEventPropertyService.delete(instance.getId());
		}
				
		alertEventRepository.delete(id);
	}

	@Override
	public List<AlertEvent> findAllByProject(Project project) {
		return alertEventRepository.findByProject(project);
	}


	@Override
	public List<AlertEvent> findByProjectAndEventType(Project project, String analysisTool, String eventType) {
		return alertEventRepository.findByProjectAndEventType(project, analysisTool, eventType);
	}

	@Override
	public List<AlertEvent> findByProjectEventTypeAndProp(Project project, String analysisTool, String eventType,
			String property, String value) {
		return alertEventRepository.findByProjectEventTypeAndProp(project, analysisTool, eventType, property, value);
	}

}
