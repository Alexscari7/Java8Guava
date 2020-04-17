package java8start;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/09/26 16:44
 */

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Apple{
    private String color;
    private Double weight;
    public static boolean isRedApple(Apple apple){
        return "red".equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 10;
    }
}
