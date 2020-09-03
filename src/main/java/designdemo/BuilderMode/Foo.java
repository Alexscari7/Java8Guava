package designdemo.BuilderMode;

/**
 * @author wusd
 * @description 空
 * @create 2020/08/31 17:57
 */
public class Foo{
    private int id;
    private String name;
    private Foo(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
    }
    private final Object finalizerGuardian = new Object(){
        @Override
        protected void finalize() throws Throwable {
            // what to do for out object's release operation
        }
    };
    private static class Builder{
        private int id;
        private String name;
        public Builder setId(int id){
            this.id = id;
            return this;
        }
        public Builder setName(String name){
            this.name = name;
            this.id = id;
            return this;
        }
        public Foo build(){
            return new Foo(this);
        }
    }

    public static void main(String[] args) {

    }
}
