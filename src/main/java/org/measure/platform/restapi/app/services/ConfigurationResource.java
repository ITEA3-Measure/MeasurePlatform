package org.measure.platform.restapi.app.services;

import org.measure.platform.restapi.app.services.dto.PlatformConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping(value = "api/platform")
public class ConfigurationResource {

	@Value("${measure.kibana.adress}")
	private String kibanaAdress;
	
	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	public  PlatformConfiguration getPlatformConfiguration() {		
		System.out.println("getPlatformConfiguration() ");
		PlatformConfiguration conf = new PlatformConfiguration();
		conf.setKibanaAdress(kibanaAdress);
		return conf;
	}

}
