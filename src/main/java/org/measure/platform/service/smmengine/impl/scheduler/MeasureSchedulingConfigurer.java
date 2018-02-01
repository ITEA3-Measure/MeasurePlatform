package org.measure.platform.service.smmengine.impl.scheduler;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.service.smmengine.api.ISchedulingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class MeasureSchedulingConfigurer implements SchedulingConfigurer {
    @Inject
    private ISchedulingService sheduleService;

    @Inject
    private MeasureInstanceService measureInstanceService;

    @Bean()
    public ThreadPoolTaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());    
        for(MeasureInstance measure : measureInstanceService.findAll()){
            if(measure.isIsShedule()!= null && measure.isIsShedule()){
                sheduleService.scheduleMeasure(measure);
            }
        }
    }

}
