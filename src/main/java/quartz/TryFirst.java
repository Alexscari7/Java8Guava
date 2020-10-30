package quartz;

import org.junit.jupiter.api.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import java.util.Date;

/**
 * @author wusd
 * @description 空
 * @create 2020/10/08 11:37
 */
public class TryFirst {


    @Test
    void test1() {

        try {
            // 调度器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 任务
            JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("job1", "group1")
                    .usingJobData("count", 0)
                    .build();

            // 触发器
            Trigger simpleTrigger = getSimpleTrigger();
            Trigger cronTrigger = getCronTrigger();



            // 设置Job局部监听器
            scheduler.getListenerManager().addJobListener(new MyJobListener(), KeyMatcher.keyEquals(new JobKey("job1", "group1")));

            // 设置Trigger监听器
            scheduler.getListenerManager().addTriggerListener(new MyTriggerListener(), KeyMatcher.keyEquals(new TriggerKey("cron-trigger", "group1")));

            // 设置调度器监听器
            scheduler.getListenerManager().addSchedulerListener(new MySchedulerListener());

            // 使用调度器将任务与触发器关联，即部署任务
            scheduler.scheduleJob(jobDetail, cronTrigger);

            // 启动调度器
            scheduler.start();
            Thread.sleep(30000);
            scheduler.shutdown();


        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Trigger getCronTrigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("cron-trigger", "group1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
        return trigger;
    }

    private Trigger getSimpleTrigger() {
        Date now = new Date();
        Date endDate = new Date(now.getTime() + 10000);
        // SimpleTrigger 定义开始时间和结束时间，和调度计划。结束时间优于调度计划，即到达结束时间后强制结束任务
        // 调度计划：使用Builder构造，可以简单设置间隔多少秒forever执行，也可以分开写间隔多少秒,执行多少次，当不确定执行次数时使用
        // SimpleTrigger.REPEAT_INDEFINITELY，到达结束时间后自动结束
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("simple-trigger", "group1")
                .startNow()
                //.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(5))
                .endAt(endDate)
                .build();
        return trigger;
    }
}
