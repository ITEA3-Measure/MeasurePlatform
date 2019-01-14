package org.measure.platform.core.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.measure.platform.core.api.IApplicationCatalogueService;
import org.measure.smm.measurementapplication.model.SMMApplication;
import org.measure.smm.service.ApplicationPackager;
import org.measure.smm.service.MeasurePackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ApplicationCatalogueService implements IApplicationCatalogueService {

    private final Logger log = LoggerFactory.getLogger(MeasureCatalogueService.class);

    @Value("${measurementapplication.repository.path}")
    private String applicationsPath;

    
	@Override
    @Transactional(readOnly = true)
	public List<SMMApplication> getAllApplications() {
        List<SMMApplication> result = new ArrayList<SMMApplication>();
        try {
            File repository = new File(applicationsPath);
            for (File file : repository.listFiles()) {
                if (file.toPath().resolve(ApplicationPackager.MEATADATAFILE).toFile().exists()) {
                    result.add(ApplicationPackager.getApplicationData(file.toPath().resolve(ApplicationPackager.MEATADATAFILE)));
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return result;
	}


	
}