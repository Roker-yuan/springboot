package com.roker.springbootflowflowable.controller;

import lombok.extern.log4j.Log4j2;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @作者: Roker
 * @时间: 2022/9/27 23:24
 * @Copyright: Don`t be the same,be better!
 */
@RestController
@Log4j2
public class ProcessInstanceController {
    @Resource
    RuntimeService runtimeService;

    @PostMapping("/startProcess")
    void startProcessInstance() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("leaveTask", "100000");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ask_for_leave", map);
        runtimeService.setVariable(processInstance.getId(), "name", "javaboy");
        runtimeService.setVariable(processInstance.getId(), "reason", "休息一下");
        runtimeService.setVariable(processInstance.getId(), "days", 10);
        log.info("创建请假流程 processId：{}", processInstance.getId());
    }
}
