package quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 * @author wusd
 * @description 空
 * @create 2020/10/08 11:49
 */
@PersistJobDataAfterExecution
public class HelloJob implements Job {
    private Integer count;

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("say hello, count:" + (++count));
        context.getJobDetail().getJobDataMap().put("count", count);
    }
}
