package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.Notification;
import org.measure.platform.core.entity.Project;

/**
 * Service Interface for managing notification.
 */
public interface NotificationService {

    /**
     * Save a notification.
     *
     * @param notification the entity to save
     * @return the persisted entity
     */
    Notification save(Notification notification);

    /**
     *  Get all the notifications.
     *  
     *  @return the list of entities
     */
    List<Notification> findAll();
    
    
    /**
     *  Get all the notifications by project.
     *  
     *   @param id the id of the project
     *  @return the list of entities
     */
    List<Notification> findAllByProject(Project project);
    

    /**
     *  Get the "id" notification.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Notification findOne(Long id);

    /**
     *  Delete the "id" notification.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    /**
     *  Get all the unvalidated notifications by project.
     *  
     *   @param id the id of the project
     *  @return the list of entities
     */
    
	List<Notification> findNewByProject(Project project);
}
