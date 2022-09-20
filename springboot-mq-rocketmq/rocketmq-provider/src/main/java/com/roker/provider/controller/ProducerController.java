package com.roker.provider.controller;

import com.roker.provider.service.IRocketMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者: Roker
 * @时间: 2022/9/15 16:40
 * @Copyright: Don`t be the same,be better!
 */
@RestController
public class ProducerController {
    @Autowired
    private IRocketMQService rocketMQService;


}
