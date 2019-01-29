package org.measure.platform.core.catalogue.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.measure.platform.core.catalogue.api.IApplicationCatalogueService;
import org.measure.smm.application.model.SMMApplication;
import org.measure.smm.measure.model.SMMMeasure;
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

    @Value("${measureplatform.storage.application}")
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
	
	 
    @Override
    public void storeApplication(Path application) {
        try {
            SMMApplication measureInfos = ApplicationPackager.getApplicationDataFromZip(application);
            UnzipUtility unzip = new UnzipUtility();
            Path target = new File(applicationsPath).toPath().resolve(measureInfos.getName());
            Files.createDirectories(target);
            unzip.unzip(application.toString(), target.toString());
            
        } catch (JAXBException | IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }
    
    @Override
    public void deleteApplication(String applicationName) {
        try {
            File repository = new File(applicationsPath);
            for (File file : repository.listFiles()) {
                if (file.getName().equals(applicationName)) {                           
                    FileUtils.deleteDirectory(file);
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }
    
    
    @Override
    public SMMApplication getApplication(String applicationName) {
        Path repository = new File(applicationsPath).toPath();
        Path measureData = repository.resolve(applicationName).resolve(ApplicationPackager.MEATADATAFILE);
        if (measureData.toFile().exists()) {
            try {
            	return ApplicationPackager.getApplicationData(measureData);
            } catch (JAXBException | IOException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        return null;
    }
    
    


	
}