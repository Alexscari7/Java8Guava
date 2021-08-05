package headfirst.chain_of_responsibility_pattern;

import java.util.LinkedList;

/**
 * N
 *
 * @author wusd
 * @date : 2021/08/04 19:41
 */
public class SimpleChainPattern {
    public static void main(String[] args) {
        /*Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        Handler handlerC = new HandlerC();
        handlerA.setNextHandler(handlerB);
        handlerB.setNextHandler(handlerC);
        handlerC.handleRequest("测试请求");*/

        Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        Handler handlerC = new HandlerC();
        Handler handlerEntrance = new Handler.Builder()
                .addHandler(handlerA)
                .addHandler(handlerB)
                .addHandler(handlerC)
                .build();
        handlerEntrance.handleRequest("测试请求");
    }
}

abstract class Handler {
    private Handler nextHandler;
    abstract void handleRequest(String request);
    void setNextHandler(Handler handler) {
        this.nextHandler = handler;
    }
    Handler getNextHandler() {
        return nextHandler;
    }

    public static class Builder {
        private LinkedList<Handler> handlerList = new LinkedList<>();

        public Builder addHandler(Handler handler) {
            if (!handlerList.isEmpty()) {
                handlerList.getLast().setNextHandler(handler);
            }
            handlerList.add(handler);
            return this;
        }

        public Handler build() {
            return handlerList.getFirst();
        }

    }
}

class HandlerA extends Handler {
    @Override
    public void handleRequest(String request) {
        System.out.println("HandlerA开始处理请求：" + request);
        if (getNextHandler() != null) {
            getNextHandler().handleRequest(request);
        }
    }
}
class HandlerB extends Handler {
    @Override
    public void handleRequest(String request) {
        System.out.println("HandlerB开始处理请求：" + request);
        if (getNextHandler() != null) {
            getNextHandler().handleRequest(request);
        }
    }
}
class HandlerC extends Handler {
    @Override
    public void handleRequest(String request) {
        System.out.println("HandlerC开始处理请求：" + request);
        if (getNextHandler() != null) {
            getNextHandler().handleRequest(request);
        }
    }
}