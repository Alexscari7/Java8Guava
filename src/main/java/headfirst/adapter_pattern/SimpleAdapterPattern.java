package headfirst.adapter_pattern;

import com.sun.xml.internal.ws.api.server.Adapter;

/**
 * N
 *
 * @author wusd
 * @date : 2021/08/06 10:58
 */
public class SimpleAdapterPattern {
    public static void main(String[] args) {
        Adaptee adaptee = new Adaptee();
        Target extendsAdapter = new ExtendsAdapter();
        extendsAdapter.request();
        Target combinedAdapter = new CombinedAdapter(adaptee);
        combinedAdapter.request();
    }
}

class Adaptee {
    public void specificRequest() {
        System.out.println("适配者处理请求");
    }
}

interface Target {
    void request();
}

class CombinedAdapter implements Target {
    private Adaptee adaptee;

    public CombinedAdapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        adaptee.specificRequest();
    }
}

class ExtendsAdapter extends Adaptee implements Target {
    @Override
    public void request() {
        specificRequest();
    }
}