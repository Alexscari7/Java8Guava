package junittest;

import com.google.common.collect.Lists;
import enums.Color;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author wusd
 * @description junit annotation学习
 * @create 2020/04/18 11:27
 */
@Slf4j
class FirstStart {
    private static String name;

    // @Test类和Test所有junit API方法都不需要加方法修饰符，因为在编译期就指定位public了
    @Test
    void test10() {
        System.out.println("This is test for @Test");
    }

    // @ParameterizedTest 允许test方法使用不同的参数运行多次，至少使用一个source来提供参数
    // 参数提供可以由indexed argument，aggregator，或ParameterResolver依次提供
    // 1.@ValueSource 提供一个数组参数，参数类型可以是byte、short、int、long、float、double、char、boolean、String、Class
    // 2.@NullSource、@EmptySource、@NullAndEmptySource 可以提供null对象或空字符串对象
    // 3.@EnumSource 使用枚举类提供参数，使用names时可以指定使用的枚举对象，省略时使用该枚举类的所有枚举对象,还可以使用正则表达式匹配枚举对象
    //   默认mode=INCLUDE，也可以使用@EXCLUDE排除特定的枚举对象
    // 4.@MethodSource 使用本类或者外部类定义的工厂方法，方法必须由static修饰，并且返回一个stream(包括原生IntStream等)或list或array
    //   可以指定工厂方法，外部方法需要使用完整包名，不提供方法名时，会默认寻找本类下同名的工厂方法
    // 5.@CsvSource 使用String数组定义简单的多参数，使用','分割，还可以自定义null和空字符串。同时@CsvFileSource使用外部文件
    @Tag("fail-test")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"22", "2", "3"})
    void test20(String s) {
        assertTrue(s.length() > 1, "参数长度必须大于1");
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"YELLOW"})
    void test21(Color color) {
        assertTrue(EnumSet.of(Color.RED, Color.GREEN).contains(color));
    }

    @ParameterizedTest
    @MethodSource
    void test22(String s, int i, List<String> list) {
        assertEquals(5, s.length());
        assertTrue(i > 1 && i < 4);
        assertEquals(2, list.size());
    }

    static List<Arguments> test22() {
        // Arguments.of()和arguments()等同
        /*return Stream.of(
                arguments("apple", 2, Arrays.asList("a", "b")),
                arguments("lemon", 3, Arrays.asList("x", "y"))
        );*/
        return Lists.newArrayList(arguments("apple", 2, Arrays.asList("a", "b")),arguments("lemon", 3,
                Arrays.asList("x", "y")));
    }

    @ParameterizedTest
    @CsvSource({"apple, 1", "banana, 2", "'lemon ,is great', 3"})
    void test23(String s, int i) {
        System.out.println(s + i);
        assertTrue(s.length() > 4);
        assertTrue(i > 0);
    }

    // @RepeatedTest 等同于使用@Test运行多次，提供value指定运行次数，name默认为repetition {currentRepetition} of {totalRepetitions}
    // 可以在测试入参注入TestInfo和RepetitionInfo。同时RepetitionInfo还可以注入在@BeforeEach和@AfterEach中
    @RepeatedTest(value = 3)
    void test30(TestInfo testInfo, RepetitionInfo repetitionInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @Test
    @Timeout(3)
    void testtemp() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // @TestFactory 允许在运行期使用工厂方法生成动态测试
    // @TestTemplate @RepeatedTest和@ParameterizedTest都是基于此注解运行的

    // @TestMethodOrder 类级注释，可以让所有测试方法以指定顺序@Order(value)运行

    // junit中Test实例的生命周期默认是PER_METHOD，即每次只需测试方法时，实例化这个测试类，以便于隔离执行单个测试方法。
    // 它提供了@TestInstance(Lifecycle.PER_CLASS)，每个测试类只会被实例化一次，需要需要使用实例变量，需要在@BeforeEach和@AfterEach中重置变量状态
    // 使用此模式后，@BeforeAll和@AfterAll不在需要被声明为static了。还可以使用配置文件指定所有测试类默认模式

    // @DisplayName指定测试类或测试方法的名字

    // @BeforeEach 表示此方法在每次测试方法(@Test, @RepeatedTest, @ParameterizedTest, @TestFactory)之前执行一次
    // @BeforeAll 表示此方法在所有测试方法之前执行一次，并且此方法必须是static
    // @AfterEach @AfterAll同理

    // @Nested 允许存在测试类存在内部类，在内部类上使用@Nested即可，但内部类中不允许使用非静态方法，即(@BeforeAll和@AfterAll方法)，因为这是Java规定
    // 因为使用了@TestInstance(Lifecycle.PER_CLASS)后，这两个方法不需要声明为静态，所以此时可以在@Nested内部测试类中使用

    // @Tag可以对测试类或测试方法打标签，在gradle中配置useJUnitPlatform方法时，可以指定执行特定标签的方法

    // @Disabled表示此测试类或测试方法被禁用，推荐写上说明

    // @Timeout 如果测试方法在指定时间没完成，则失败
}
