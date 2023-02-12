package com.roker.springbootschedulesinglepoint.springTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简单演示
 *
 * @author liupengyuan
 * @date 2023/02/10
 * SpringTask:spring自主研发的定时任务工具，并且存在于Spring体系中，不需要添加任何依赖.
 * Spring Framework 自带定时任务，提供了 cron 表达式来实现丰富定时任务配置。
 * 新手推荐使用 https://cron.qqe2.com/ 这个网站来匹配你的 cron 表达式。
 * 使用步骤：
 * 1、添加SpringTask配置项
 * 只需在配置类中添加@EnableScheduling注解就可以使用SpringTask定时任务
 * 2、添加定时任务
 * 只需要定义一个 Spring Bean ，然后定义具体的定时任务逻辑方法并使用 @Scheduled 注解标记该方法即可。
 * 请注意：@Scheduled 注解中一定要声明定时任务的执行策略 cron 、fixedDelay、fixedRate 三选一。
 */
@Component
public class SimpleDemo {
    private final static Logger log = LoggerFactory.getLogger(SimpleDemo.class);
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * corn任务
     * cron是@Scheduled的一个参数，是一个字符串，以5个空格隔开，只允许6个域（注意不是7个，7个直接会报错），分别表示秒、分、时、日、月、周。
     */
//    @Scheduled(cron = "*/5 * * * * * ")
//    @Scheduled(cron = "${springTask.cornExpression} ")//也可以通过EL表达式从配置文件中获取
    public void cornTask(){
        log.info("SpringTask基于corn定时任务执行:{}",format.format(new Date()));
    }


    /**
     * 固定频率任务
     * fixedRate表示自上一次执行时间之后多长时间执行，以毫秒为单位。
     */
//    @Scheduled(fixedRate = 1000)
    public void fixedRateTask(){
        log.info("SpringTask基于fixedRate定时任务执行:{}",format.format(new Date()));
    }


    /**
     * fixedRateString任务
     * 有一个类似的参数叫fixedRateString，是字符串的形式，支持占位符。
     * 若在配置文件中有相应的属性，可以用占位符获取属性，如在application.properties中有
     */
//    @Scheduled(fixedRateString = "5000")
    @Scheduled(fixedRateString = "${springTask.fixedRateString}")
    public void fixedRateStringTask(){
        log.info("SpringTask基于fixedRateString定时任务执行:{}",format.format(new Date()));
    }

    /**
     * 固定延迟任务
     * fixedDelay与fixedRate有点类似，不过fixedRate是上一次开始之后计时，fixedDelay是上一次结束之后计时，
     * 也就是说，fixedDelay表示上一次执行完毕之后多长时间执行，单位也是毫秒。
     */
    @Scheduled(fixedDelay = 5000)
    public void fixedDelayTask(){
        log.info("SpringTask基于fixedDelay定时任务执行:{}",format.format(new Date()));
    }

    /**
     * 固定延迟字符串任务
     * 与fixedRateString类似，也是支持占位符
     */
    //    @Scheduled(fixedDelayString = "5000")
    @Scheduled(fixedDelayString = "${springTask.fixedDelayString}")
    public void fixedDelayStringTask(){
        log.info("SpringTask基于fixedDelayString定时任务执行:{}",format.format(new Date()));
    }

    /**
     * 初始延迟任务
     * initialDelay表示首次延迟多长时间后执行，单位毫秒，
     * 之后按照cron/fixedRate/fixedRateString/fixedDelay/fixedDelayString指定的规则执行，需要指定其中一个规则。
     */
    @Scheduled(initialDelay = 3000,fixedRate = 5000)
    public void initialDelayTask(){
        log.info("SpringTask基于initialDelay定时任务执行:{}",format.format(new Date()));
    }

    /**
     * 字符串初始延迟任务
     * 与initialDelay类似，不过是字符串，支持占位符。
     */
//    @Scheduled(initialDelayString = "3000",fixedRateString = "5000")
    @Scheduled(initialDelayString = "${springTask.initialDelayString}",fixedRateString = "${springTask.fixedDelayString}")
    public void initialDelayStringTask(){
        log.info("SpringTask基于initialDelayString定时任务执行:{}",format.format(new Date()));
    }
}
