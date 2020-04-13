package test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/09/26 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
    private String name;
    private String addr;
    private String desc;
}
