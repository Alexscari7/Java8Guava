package headfirst.statemachine_pattern;

/**
 * @author wusd
 */
public class MockCpuClient {
    public static void main(String[] args) {
        ThreadContext context = new ThreadContext();
        context.start();
        context.getCPU();
        context.suspend();
        context.resume();
        context.getCPU();
        context.stop();
    }
}
// 环境类
class ThreadContext {
    private ThreadState state;
    ThreadContext() {
        state = new New();
    }
    public void setState(ThreadState state) {
        this.state = state;
    }
    public ThreadState getState() {
        return state;
    }
    public void start() {
        state.start(this);
    }
    public void getCPU() {
        state.getCPU(this);
    }
    public void suspend() {
        state.suspend(this);
    }
    public void stop() {
        state.stop(this);
    }
    public void resume() {
        state.resume(this);
    }
}
// 抽象状态类：线程状态
abstract class ThreadState {
    protected String stateName;

    protected void start(ThreadContext hj) {
        throw new UnsupportedOperationException(stateName + "不支持start操作");
    }
    protected void getCPU(ThreadContext hj) {
        throw new UnsupportedOperationException(stateName + "不支持getCPU操作");
    }
    protected void suspend(ThreadContext hj) {
        throw new UnsupportedOperationException(stateName + "不支持suspend操作");
    }
    protected void stop(ThreadContext hj) {
        throw new UnsupportedOperationException(stateName + "不支持stop操作");
    }
    protected void resume(ThreadContext hj) {
        throw new UnsupportedOperationException(stateName + "不支持resume操作");
    }

}
// 具体状态类：新建状态
class New extends ThreadState {
    public New() {
        stateName = "新建状态";
        System.out.println("当前线程处于：新建状态");
    }
    public void start(ThreadContext hj) {
        System.out.print("调用start()方法-->");
        hj.setState(new Runnable());
    }
}
// 具体状态类：就绪状态
class Runnable extends ThreadState {
    public Runnable() {
        stateName = "就绪状态";
        System.out.println("当前线程处于：就绪状态");
    }
    public void getCPU(ThreadContext hj) {
        System.out.print("调用getCPU()方法-->");
        hj.setState(new Running());
    }
}
// 具体状态类：运行状态
class Running extends ThreadState {
    public Running() {
        stateName = "运行状态";
        System.out.println("当前线程处于：运行状态");
    }
    public void suspend(ThreadContext hj) {
        System.out.print("调用suspend()方法-->");
        hj.setState(new Blocked());
    }
    public void stop(ThreadContext hj) {
        System.out.print("调用stop()方法-->");
        hj.setState(new Dead());
    }
}
// 具体状态类：阻塞状态
class Blocked extends ThreadState {
    public Blocked() {
        stateName = "阻塞状态";
        System.out.println("当前线程处于：阻塞状态");
    }
    public void resume(ThreadContext hj) {
        System.out.print("调用resume()方法-->");
        hj.setState(new Runnable());

    }
}
// 具体状态类：死亡状态
class Dead extends ThreadState {
    public Dead() {
        stateName = "死亡状态";
        System.out.println("当前线程处于：死亡状态");
    }
}