package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/09/26 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity implements Cloneable{
    private String name;
    private String addr;
    private String desc;

    public String isA() {
        return "原来的";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Entity entitya = new Entity();
        Entity en = (Entity) entitya.clone();
        System.out.println(entitya.hashCode() + "" + en.hashCode());
    }
}
