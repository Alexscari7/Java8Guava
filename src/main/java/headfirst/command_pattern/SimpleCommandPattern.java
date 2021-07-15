package headfirst.command_pattern;

/**
 * N
 *
 * @author wusd
 * @date : 2021/07/13 17:18
 */
public class SimpleCommandPattern {
    public static void main(String[] args) {
        ICommand command = new CommandA();
        Invoker invoker = new Invoker(command);
        invoker.call();
    }
}

interface ICommand {
    void execute();
}

class CommandA implements ICommand {
    // 可以方法入参代替成员变量
    private ReceiverA receiverA;

    CommandA() {
        receiverA = new ReceiverA();
    }
    @Override
    public void execute() {
        receiverA.action();
    }
}

class ReceiverA {
    public void action() {
        System.out.println("命令接收者A执行操作");
    }
}

class Invoker {
    // 可以方法入参代替成员变量
    private ICommand command;

    Invoker(ICommand command) {
        this.command = command;
    }
    public void call() {
        command.execute();
    }
}