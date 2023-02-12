package com.roker.springbootschedulesinglepoint.jdk;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 实战案例
 * 假设你正在开发一个电商网站，该网站需要每天晚上12点定时清除所有过期的购物车信息。您可以使用ScheduledExecutorService来实现该功能。
 * 需求背景：电商网站需要每天晚上12点定时清除所有过期的购物车信息。
 * 选型思路：因为需要定时执行任务，因此我们可以使用ScheduledExecutorService来实现该功能。
 *
 * 通过计算从当前时间到每天晚上12点的时间差，并以该时差为延迟启动任务。任务每24小时执行一次，清除所有过期的购物车信息。
 * @author liupengyuan
 * @date 2023/02/10
 */
public class PracticalCase1 {
    private final static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    public static void main(String[] args) {

        Runnable task = () -> {
            // 清除所有过期的购物车信息
            System.out.println("Clearing all expired shopping cart information...");
        };

        // 计算从当前时间到每天晚上12点的时间差，并以该时差为延迟启动任务
        long delay = getDelayToMidnight();

        executor.scheduleAtFixedRate(task, delay, 24, TimeUnit.HOURS);
    }

    private static long getDelayToMidnight() {
        // 获取当前时间
        long now = System.currentTimeMillis();

        // 获取当天午夜的时间
        long midnight = getMidnightTime(now);

        // 返回从当前时间到午夜的时间差
        return midnight - now;
    }

    private static long getMidnightTime(long timeInMillis) {
        // 获取今天的日期
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(timeInMillis);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND,0);
        today.set(Calendar.AM_PM, Calendar.AM);
        today.add(Calendar.DAY_OF_MONTH, 1);

        return today.getTimeInMillis();
    }
}
