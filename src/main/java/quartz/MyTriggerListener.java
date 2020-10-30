package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

/**
 * @author wusd
 * @description 空
 * @create 2020/10/08 15:19
 */
public class MyTriggerListener implements TriggerListener {
    private static final String LISTENER_NAME = "mytriggerlistener";
    @Override
    public String getName() {
        return LISTENER_NAME;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        System.out.println("触发器将要被执行");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        System.out.println("触发器未被执行");
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        System.out.println("触发器已被执行");
    }
}
