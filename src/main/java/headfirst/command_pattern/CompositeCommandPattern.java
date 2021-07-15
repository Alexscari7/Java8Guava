package headfirst.command_pattern;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * N
 *
 * @author wusd
 * @date : 2021/07/14 11:43
 */
public class CompositeCommandPattern {
    public static void main(String[] args) {
        CompositeInvoker invoker = new CompositeInvoker();
        invoker.executeInOrder();
        invoker.executeA();
        invoker.executeB();
        invoker.undo();
    }
}

abstract class AbstractCommand {
    public abstract void execute();
}

class ConcreteCommandA extends AbstractCommand {
    public CompositeReceiver receiver = new CompositeReceiver();
    @Override
    public void execute() {
        receiver.actionA();
    }
}

class ConcreteCommandB extends AbstractCommand {
    public CompositeReceiver receiver = new CompositeReceiver();
    @Override
    public void execute() {
        receiver.actionB();
    }
}

class CompositeReceiver {
    public void actionA() {
        System.out.println("执行命令A");
    }
    public void actionB() {
        System.out.println("执行命令B");
    }
}

class CompositeInvoker {
    public List<AbstractCommand> commandList;

    public LinkedList<AbstractCommand> undoList;

    public List<AbstractCommand> redoList;

    CompositeInvoker() {
        commandList = new ArrayList<>();
        addCommand(new ConcreteCommandA());
        addCommand(new ConcreteCommandB());
        undoList = new LinkedList<>();
        redoList = new ArrayList<>();
    }

    public void addCommand(AbstractCommand command) {
        commandList.add(command);
    }
    public void removeCommand(AbstractCommand command) {
        commandList.remove(command);
    }

    public void log(AbstractCommand command) {
        redoList.add(command);
        undoList.add(command);
    }

    // 针对不同的业务场景执行不同的命令组合
    public void executeInOrder() {
        for (AbstractCommand command : commandList) {
            command.execute();
            log(command);
        }
    }

    public void executeA() {
        commandList.get(0).execute();
        log(commandList.get(0));
    }

    public void executeB() {
        commandList.get(1).execute();
        log(commandList.get(1));
    }

    public void undo() {
        ListIterator<AbstractCommand> iterator = undoList.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        while (iterator.hasPrevious()) {
            iterator.previous().execute();
            iterator.remove();
        }
    }

    public void recover() {
        for (AbstractCommand command : redoList) {
            command.execute();
        }
    }
}


