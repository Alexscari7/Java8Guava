package java8start.optional;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/07/29 21:46
 */
public class OptionalTest {
    @Test
    public void test(){
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDateTime localDateTime = date.atTime(time);
        System.out.println(localDateTime);
    }

}
