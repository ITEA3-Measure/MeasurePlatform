package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;
import org.measure.platform.core.entity.MeasureReference;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.impl.repository.MeasureInstanceRepository;
import org.measure.platform.core.impl.repository.MeasurePropertyRepository;
import org.measure.platform.core.impl.repository.MeasureReferenceRepository;
import org.measure.platform.core.impl.repository.MeasureViewRepository;
import org.measure.platform.core.impl.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MeasureInstance.
 */
@Service
@Transactional
public class MeasureInstanceServiceImpl implements MeasureInstanceService {

	private final Logger log = LoggerFactory.getLogger(MeasureInstanceServiceImpl.class);

	@Inject
	private MeasureInstanceRepository measureInstanceRepository;

	@Inject
	private ProjectRepository projectRepository;

	@Inject
	private MeasurePropertyRepository propertyRepository;
	
	@Inject
	private MeasureViewRepository viewRepository;
	
	
	@Inject
	private MeasureReferenceRepository referenceRepository;

	/**
	 * Save a measureInstance.
	 *
	 * @param measureInstance
	 *            the entity to save
	 * @return the persisted entity
	 */
	public MeasureInstance save(MeasureInstance measureInstance) {
		log.debug("Request to save MeasureInstance : {}", measureInstance);	
		MeasureInstance result = measureInstanceRepository.save(measureInstance);	
		return result;
	}

	/**
	 * Get all the measureInstances.
	 * 
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<MeasureInstance> findAll() {
		log.debug("Request to get all MeasureInstances");
		List<MeasureInstance> result = measureInstanceRepository.findAll();
		return result;
	}

	@Override
	public List<MeasureInstance> findMeasureInstancesByProject(Long projectId) {
		Project project = projectRepository.getOne(projectId);
		List<MeasureInstance> result = measureInstanceRepository.findByProject(project);
		return result;
	}
	
	
	@Override
	public List<MeasureInstance> findMeasureInstanceByReference(String measureid) {
		List<MeasureInstance> result = measureInstanceRepository.findByMeasure(measureid);
		return result;
	}

	/**
	 * Get one measureInstance by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public MeasureInstance findOne(Long id) {
		log.debug("Request to get MeasureInstance : {}", id);
		MeasureInstance measureInstance = measureInstanceRepository.findOne(id);
		return measureInstance;
	}

	/**
	 * Delete the measureInstance by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		
		MeasureInstance minstance = measureInstanceRepository.findOne(id);
		for(MeasureProperty prop : propertyRepository.findByMeasure(minstance)){
			propertyRepository.delete(prop);
		}
		
		for(MeasureReference prop : referenceRepository.findByMeasure(minstance)){
			referenceRepository.delete(prop);
		}
		
		for(MeasureView prop : viewRepository.findByMeasure(minstance)){
			viewRepository.delete(prop);
		}
		
		measureInstanceRepository.delete(id);
	}

	

}
