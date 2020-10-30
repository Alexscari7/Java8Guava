package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * @author wusd
 * @description 空
 * @create 2020/10/08 15:02
 */
public class MyJobListener implements JobListener {
    private static final String LISTENER_NAME = "myjoblistener";

    @Override
    public String getName() {
        return LISTENER_NAME;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        System.out.println("任务将要被执行");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        System.out.println("任务被否决");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        System.out.println("任务已被执行");
    }
}
