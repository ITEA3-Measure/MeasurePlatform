package org.measure.platform.core.catalogue.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.measure.platform.core.catalogue.api.IMeasureCatalogueService;
import org.measure.smm.measure.api.IMeasure;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.service.MeasurePackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MeasureCatalogueService implements IMeasureCatalogueService {
    private final Logger log = LoggerFactory.getLogger(MeasureCatalogueService.class);

    @Value("${measureplatform.storage.measure}")
    private String measurePath;
    
    @Value("${measureplatform.storage.application}")
    private String applicationsPath;
    
    @Override
    public void storeMeasure(Path measure) {
        try {
            SMMMeasure measureInfos = MeasurePackager.getMeasureDataFromZip(measure);
            UnzipUtility unzip = new UnzipUtility();
            Path target = new File(measurePath).toPath().resolve(measureInfos.getName());
            Files.createDirectories(target);
            unzip.unzip(measure.toString(), target.toString());
            
        } catch (JAXBException | IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SMMMeasure> getAllMeasures() {
        List<SMMMeasure> result = new ArrayList<SMMMeasure>();
        try {
            File repository = new File(measurePath);
            for (File file : repository.listFiles()) {
                if (file.toPath().resolve(MeasurePackager.MEATADATAFILE).toFile().exists()) {
                    result.add(MeasurePackager.getMeasureData(file.toPath().resolve(MeasurePackager.MEATADATAFILE)));
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    public void deleteMeasure(String measureId) {
        try {
            File repository = new File(measurePath);
            for (File file : repository.listFiles()) {
                if (file.getName().equals(measureId)) {                           
                    FileUtils.deleteDirectory(file);
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public IMeasure getMeasureImplementation(String application,String measure) {
        
        Path measureImpl = null;
        
        if(application != null) {
        	measureImpl = new File(applicationsPath).toPath().resolve(application).resolve(measure);
        }else {
        	measureImpl =  new File(measurePath).toPath().resolve(measure);     	
        }
                
        if (measureImpl.toFile().exists()) {
            List<URL> jars;
            try {
                URL measureJar = getJars(measureImpl).get(0);
                jars = getJars(measureImpl.resolve("lib"));
                jars.add(measureJar);            
        
                try (URLClassLoader loader = new URLClassLoader(jars.toArray(new URL[jars.size()]),IMeasure.class.getClassLoader())) {
                    IMeasure result = null;
                    for (URL jar : jars) {
                        JarInputStream jarStream = new JarInputStream(new FileInputStream(new File(jar.getFile())));
                        for (JarEntry jarEntry = jarStream.getNextJarEntry(); jarEntry != null; jarEntry = jarStream.getNextJarEntry()) {
                            if (jarEntry.getName().endsWith(".class")) { //$NON-NLS-1$
								String metaclassNamespace = getNamespace(jarEntry.getName());
                                Class<?> metaclass = null;
                        
                     
                                try {
                                    metaclass = loader.loadClass(metaclassNamespace);
                                } catch (Throwable e) {
                                    log.error(e.getLocalizedMessage());
                                    System.out.println(metaclassNamespace);
                                }
                                if (metaclass != null && IMeasure.class.isAssignableFrom(metaclass)) {
                                    result = (IMeasure) metaclass.newInstance();
                                }
                            }
                        }
                    }
                    return result;
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage());
                }
            } catch (MalformedURLException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        return null;
    }

    private String getNamespace(String jarEntryName) {
        String namespace = jarEntryName;
        namespace = namespace.replaceAll("/", "\\.");
        String separator = "/";
        
        int index = namespace.lastIndexOf(separator);
        namespace = namespace.substring(index + 1);
        
        if (namespace.endsWith(".class")) {
            namespace = namespace.substring(0, namespace.length() - 6);
        }
        return namespace;
    }

    private List<URL> getJars(Path measureImpl) throws MalformedURLException {
        List<URL> jars = new ArrayList<>();
        if (measureImpl.toFile().exists() && measureImpl.toFile().isDirectory()) {
            for (File sub : measureImpl.toFile().listFiles()) {
                if (sub.getName().endsWith("jar")) {
                    jars.add(sub.toURI().toURL());
                }
            }
        }
        return jars;
    }

    @Override
    public SMMMeasure getMeasure(String applicationName,String measureName) {
    	
    	Path measureData = null;
        
        if(applicationName != null) {
        	measureData = new File(applicationsPath).toPath().resolve(applicationName).resolve(measureName).resolve(MeasurePackager.MEATADATAFILE);
        }else {
        	measureData =  new File(measurePath).toPath().resolve(measureName).resolve(MeasurePackager.MEATADATAFILE);     	
        }
                
        if (measureData.toFile().exists()) {
            try {
            	SMMMeasure measure = MeasurePackager.getMeasureData(measureData);
                return measure;
            } catch (JAXBException | IOException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        return null;
    }

}
