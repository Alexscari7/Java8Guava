package enums;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/04/11 9:41
 */

public enum Color {
    RED("红色", "1"), GREEN("绿色", "2"),YELLOW;
    private String name;
    private String index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    Color(String name, String index) {
        this.name = name;
        this.index = index;
        System.out.println("args constructor, this is " + name);
    }
    Color(){
        System.out.println("no args");
    }

    @Override
    public String toString() {
        return "Color{" +
                "name='" + name + '\'' +
                ", index='" + index + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Color red = Color.RED;
        Color yellow = Color.YELLOW;
        System.out.println(yellow.toString());
    }
}