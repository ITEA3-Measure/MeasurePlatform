package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.NotificationService;
import org.measure.platform.core.entity.Notification;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.impl.repository.NotificationRepository;
import org.measure.platform.core.impl.repository.PhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private PhaseRepository phaseRepository;

    /**
     * Save a project.
     * @param project
     * the entity to save
     * @return the persisted entity
     */
    public Notification save(Notification notification) {
        log.debug("Request to save Project : {}", notification);
        Notification result = notificationRepository.save(notification);
        return result;
    }

    /**
     * Get all the projects.
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Notification> findAll() {
        log.debug("Request to get all Projects");
        List<Notification> result = notificationRepository.findAll();
        return result;
    }

    /**
     * Get one project by id.
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Notification findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        Notification notification = notificationRepository.findOne(id);
        return notification;
    }

    /**
     * Delete the  project by id.
     * @param id the id of the entity
     */
    public void delete(Long id) {
        notificationRepository.delete(id);
    }

    @Override
    public List<Notification> findAllByProject(Project project) {
        List<Notification> result = notificationRepository.findByProject(project);
        return result;
    }

    @Override
    public List<Notification> findNewByProject(Project project) {
        List<Notification> result = notificationRepository.findNewByProject(project);
        return result;
    }

}
