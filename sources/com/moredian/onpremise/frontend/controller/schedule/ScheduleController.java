package com.moredian.onpremise.frontend.controller.schedule;

import com.moredian.onpremise.frontend.controller.base.BaseController;
import com.moredian.onpremise.task.api.CustomSchedule;
import com.moredian.onpremise.task.factory.RefreshCacheScheduleFactory;
import com.moredian.onpremise.task.factory.ScheduleFactory;
import io.swagger.annotations.Api;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-schedule", description = "on-premise定时任务相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/schedule/ScheduleController.class */
public class ScheduleController extends BaseController {
    @RequestMapping(value = {"/schedule/changeCorn"}, method = {RequestMethod.POST})
    public String changeCorn(@RequestParam("corn") String corn, String key) {
        ScheduleFactory factory = new RefreshCacheScheduleFactory();
        CustomSchedule schedule = factory.getSchedule();
        return "" + schedule.saveScheduled(corn);
    }

    @RequestMapping(value = {"/schedule/stopAllSchedule"}, method = {RequestMethod.POST})
    public String stopAllSchedule() {
        ScheduleFactory factory = new RefreshCacheScheduleFactory();
        CustomSchedule schedule = factory.getSchedule();
        return "" + schedule.stopAllSchedule();
    }

    @RequestMapping(value = {"/schedule/listSchedules"}, method = {RequestMethod.POST})
    public List listSchedules() {
        return new ArrayList();
    }
}
