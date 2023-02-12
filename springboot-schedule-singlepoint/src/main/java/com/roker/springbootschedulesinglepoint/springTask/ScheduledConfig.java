package com.roker.springbootschedulesinglepoint.springTask;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 *
 * @author liupengyuan
 * @date 2023/02/10
 * 在方法前加注解@Scheduled即可使该任务为定时调度任务，但是在实际部署环境运行时，发现日志的打印十分卡顿，甚至出现滞后，定时任务没有按时执行，但是在自己的开发机上运行就没有出现卡顿；附上cron表达式网站：在线Cron表达式生成器
 * 因为定时调度任务默认是单线程的，如果有多个定时任务，则会单线程按顺序执行，在服务器的单核性能不足时很容易遇到瓶颈，遭遇卡顿，自己的开发机的单核性能较强，就不卡顿；
 * 单线程执行会导致后面的定时任务需要等待前面的任务执行完毕，就无法定时执行；
 * 假如最后一个任务是每个小时的00分00秒开始，每5分钟执行一次，但是由于单线程的性能问题，原计划于05、10、15等分执行的任务变为了06、11、16分执行，如果该任务中需要获取当前的时间，就会获取到不符合设计逻辑的滞后的时间，所以改为多线程执行是必须的；
 * 走弯路：
 * 使用注解@Async：
 * 在注解@Scheduled前再加一个注解@Async，启动多线程执行；单核性能问题得到了立竿见影的解决，但是同时发现bug，定时任务在写库时出现了重复写入的现象；分析完写入逻辑后，发现写入没有问题，唯一的可能就是线程太多了，同一个定时任务有多个线程在同时执行，写入时就会同时写入，出现重复；
 * 分析：@Scheduled是一个定时任务，并不会结束，而是定时触发，@Async结合@Scheduled之后就会变成了每次执行定时任务时，就会开启一个新线程，但是老线程还在运行该定时任务，新老线程都在执行一样的任务，线程数会不断增加，直到达到线程池配置的上限后开始报错；这里的错误原理类似于嵌套了循环；
 * 解决方案：将注解@Scheduled配置为多线程：
 * 虽然注解@Scheduled默认是单线程的，但是可以通过写配置类将其改为多线程执行，不需要借助@Async来开启多线程；
 */
@Component
public class ScheduledConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        //最大线程数
        taskScheduler.setPoolSize(30);
        return taskScheduler;
    }
}
