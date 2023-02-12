package com.roker.springbootschedulesinglepoint.jdk;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 一些计划执行者
 *
 * @author liupengyuan
 * @date 2023/02/10
 * 自从 JDK1.5 之后，提供了 ScheduledExecutorService 代替 TimerTask 来执行定时任务，提供了不错的可靠性。
 * ScheduledExecutorService是Java中用于调度任务执行的一种并发工具类。它可以在后台执行定时任务或周期性任务，是非常实用的。
 * 一些常见的场景如下：
 * 定时任务：如果您需要在某个特定时间执行任务，您可以使用ScheduledExecutorService。例如，您可以创建一个定时器，每天定时运行数据备份任务。
 * 周期性任务：如果您需要在某个周期内执行任务，您可以使用ScheduledExecutorService。例如，您可以创建一个定时器，每隔5分钟执行检查应用程序状态的任务。
 * 实时数据处理：如果您需要定期处理大量数据，ScheduledExecutorService可以提供帮助。例如，您可以创建一个定时器，每天凌晨执行分析用户数据的任务。
 */

@Slf4j
public class SimpleDemo {
    private final static ScheduledExecutorService scheduleExecutor = Executors.newScheduledThreadPool(10);
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static void main(String[] args) {
        //延迟不循环任务schedule方法
//        delayTaskWithoutLoop();
        //延迟且循环scheduleAtFixedRate方法
        delayAndLoopTask();
        //严格按照一定时间间隔执行
        scheduleWithFixedDelay();
    }

    /**
     * 固定延迟任务执行
     * scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);
     * 参数1：任务
     * 参数2：初始化完成后延迟多长时间执行第一次任务
     * 参数3：任务执行时间间隔
     * 参数4：单位
     * 解释：以上一次任务执行结束时间为准，加上任务时间间隔作为下一次任务开始时间，由此可以得出，任务可以严格按照时间间隔执行
     */
    private static void scheduleWithFixedDelay() {
        scheduleExecutor.scheduleWithFixedDelay(()->{
            try {
                log.info("开始执行...time {}", format.format(new Date()));
                Thread.sleep(5000);
                log.info("执行结束...time {}", format.format(new Date()));
            } catch (Exception e) {
                log.error("定时任务执行出错");
            }
        },5,5,TimeUnit.SECONDS);
    }

    /**
     * 延迟且循环任务
     * scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
     * 参数1：任务
     * 参数2：初始化完成后延迟多长时间执行第一次任务
     * 参数3：任务时间间隔
     * 参数4：单位
     * 方法解释：是以上一个任务开始的时间计时，比如period为5，那5秒后，检测上一个任务是否执行完毕，如果上一个任务执行完毕，则当前任务立即执行，
     *         如果上一个任务没有执行完毕，则需要等上一个任务执行完毕后立即执行，如果你的任务执行时间超过5秒，那么任务时间间隔参数将无效，任务会不停地循环执行，
     *         由此可得出该方法不能严格保证任务按一定时间间隔执行
     */
    private static void delayAndLoopTask() {
        scheduleExecutor.scheduleAtFixedRate(() -> {
            try {
                log.info("开始执行...time {}", format.format(new Date()));
                Thread.sleep(1000);
//                Thread.sleep(6000);//错误的设置，任务执行时间要长于定时间隔，导致任务无法按照设置的间隔去执行，而是等待上一个任务执行完毕后开始当前任务的执行
                log.info("执行结束...time {}", format.format(new Date()));
            } catch (Exception e) {
                log.error("定时任务执行出错");
            }
        },5,5,TimeUnit.SECONDS);
        log.info("任务初始化成功 {}", format.format(new Date()));
    }

    /**
     * 延迟任务没有循环
     * schedule(Runnable command, long delay, TimeUnit unit)
     * 任务执行时间为初始化完成后5s才开始执行，且只执行一次
     */
    private static void delayTaskWithoutLoop() {
        scheduleExecutor.schedule(() -> {
            try {
                log.info("开始执行...time {}", format.format(new Date()));
                Thread.sleep(1000);
                log.info("执行结束...time {}", format.format(new Date()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },5,TimeUnit.SECONDS);
        log.info("任务初始化成功 {}", format.format(new Date()));
    }


}
