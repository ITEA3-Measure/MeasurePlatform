package org.measure.platform.restapi.measure;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.service.smmengine.api.ILoggerService;
import org.measure.smm.log.MeasureLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/measure-logger")
public class MeasureLoggerResource {
    @Inject
    private ILoggerService logger;

    @Timed
    @RequestMapping(value = "/measure-execution", method = RequestMethod.GET)
    public List<MeasureLog> executeMeasure() {
        return logger.getMeasureExecutionLogs();
    }

}
