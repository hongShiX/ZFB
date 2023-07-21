package com.hh.micrTask;

import com.hh.api.service.IncomeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("taskManager")
public class TaskManager {
    @DubboReference(interfaceClass = IncomeService.class, version = "1.0-SNAPSHOT")
    private IncomeService incomeService;

    /*生成收益计划*/
    @Scheduled(cron = "0 0 1 * * ?")
    public void invokeGenerateIncomePlan() {
        incomeService.generateIncomePlan();
    }


    /*生成收益返还*/
    @Scheduled(cron = "0 0 2 * * ?")
    public void invokeGenerateIncomeBack() {
        incomeService.generateIncomeBack();
    }
}
