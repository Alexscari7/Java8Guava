package headfirst.template_pattern;

/**
 * @author wusd
 */
public class SimpleTemplatePattern {

    public static void main(String[] args) {
        AbstractTemplate template = new ConcreteClass();
        template.templateMethod();
    }

}

abstract class AbstractTemplate {
    final void templateMethod() {
        commonMethod();
        abstractMethod1();
        abstractMethod2();
        hookMethod();
    }

    private void commonMethod() {
        System.out.println("执行公有方法");
    }


    protected abstract void abstractMethod1();

    protected abstract void abstractMethod2();

    protected void hookMethod() {
    }

}

class ConcreteClass extends AbstractTemplate {
    @Override
    protected void abstractMethod1() {
        System.out.println("执行分化方法1");
    }

    @Override
    protected void abstractMethod2() {
        System.out.println("执行分化方法2");
    }

    @Override
    protected void hookMethod() {
        System.out.println("执行特有方法");
    }
}
