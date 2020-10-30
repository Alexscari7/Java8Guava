package test;

import entity.Entity;
import entity.TreeNode;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wusd
 * @description diary test
 * @createtime 2019/07/19 21:44
 */
@Slf4j
public class forTest {

    @Test
    public void test() {
        Entity e1 = new Entity("王", "sz", "1");
        Entity e2 = new Entity("吴", "sz", "2");
        Entity e3 = new Entity("李", "wh", "1");
        Entity e4 = new Entity("张", "wh", "我");
        Entity e5 = new Entity("谭", "az", "indf");
        List<Entity> list = Lists.newArrayList(e1, e2, e3, e4, e5);
        /*Map<String, List<Entity>> result = Maps.newTreeMap();
        list.forEach(f -> {
            String date = f.getAddr();
            if ("sz".equals(f.getAddr())) {
                date = "今天";
            }
            List<Entity> innerList = result.computeIfAbsent(date, k -> new ArrayList<>());
            innerList.add(f);
        });*/
        TreeMap<String, List<Map<String, String>>> collect = list.stream().collect(Collectors.groupingBy(
                t -> {
                    if ("wh".equals(t.getAddr())) {
                        return "wuhhhh";
                    }
                    return t.getAddr();
                },
                () -> new TreeMap<>(),
                Collectors.mapping(t -> {
                    Map<String, String> innerMap = Maps.newHashMap();
                    innerMap.put("名字", t.getName());
                    innerMap.put("地址", t.getAddr());
                    innerMap.put("描述", t.getDesc());
                    return innerMap;
                }, Collectors.collectingAndThen(Collectors.toList(),
                        t -> t.stream().sorted((l, r) -> r.get("描述").compareTo(l.get("描述"))).collect(Collectors.toList())))
        ));


        System.out.println(collect);
    }

    @Test
    public void test1(){
        Entity e1 = new Entity("王", "2019", "1");
        Entity e2 = new Entity("吴", "2019", "2");
        Entity e7 = new Entity("李", "2017", "1");
        Entity e3 = new Entity("李", "2018", "1");
        Entity e4 = new Entity("张", "2017", "4");
        Entity e5 = new Entity("谭", "2018", "7");
        Entity e6 = new Entity("张", "2017", "8");
        List<Entity> list = Lists.newArrayList(e1, e2, e3, e4, e5, e6, e7);
        List<Entity> collect = list.stream().sorted(Comparator.comparing(Entity::getAddr,
                Comparator.reverseOrder()
        ).thenComparing(Entity::getDesc, Comparator.reverseOrder())).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void test2(){
        Entity source = new Entity("王", "2018", "2");
        Entity target = new Entity(null, "2019", "1");
        Class clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Method setMethod = pd.getWriteMethod();
                setMethod.invoke(target, getMethod.invoke(target) != null ? getMethod.invoke(target) :
                        getMethod.invoke(source));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        System.out.println(target);
    }

    @Test
    public void test3(){
        Entity e1 = new Entity("王", "2019", "1");
        Entity e2 = new Entity("吴", "2019", "2");
        Entity e7 = new Entity("李", "2017", "1");
        List<Entity> list = Lists.newArrayList(e1, e2, e7);
        Map<String, String> collect = list.stream().collect(Collectors.toMap(Entity::getAddr, Entity::getName, (v1,
                v2) -> v1.length() > v2.length() ? v1 : v2));
        System.out.println(collect);
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void test4(){
        // &&的优先级高于||
        if (false || false && true) {
            System.out.println(1);
        }
    }

    @Test
    public void test5(){
        // 布隆过滤器
        long createStart = System.nanoTime();
        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 10_000_000, 0.0001);
        IntStream.rangeClosed(1,10_000_000).boxed().map(String::valueOf).forEach(i -> bf.put(i));
        long middleTime = System.nanoTime();
        System.out.println("time for create filter :" + (middleTime - createStart)/1000_000_000.0 + "s");
        System.out.println(bf.mightContain("123"));
        System.out.println("time for get result :" + (System.nanoTime() - middleTime)/1000_000_000.0 + "s");
    }

    @Test
    public void test6(){
        // 对于从对象内部获取的属性或者对象，只要不改变它的地址，它就一直指向对象内部原属性，如果重新赋值则会改变地址，不会再执行对象内部，它的改变就不会影响对象内部了。
    }

    @Test
    public void test7(){
        String s= "{\"age\":\"17\",\"sex\":\"男\",\"hobby\":\"乒乓球\"}";
        Map<String, String> changeFieldMap = JSONObject.parseObject(s, Map.class);
        StringBuilder stringBuilder = new StringBuilder();
        changeFieldMap.forEach((field, value) -> stringBuilder.append(field).append("='").append(value).append(
                "',"));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        String updateSql = "update " + "kgrp" + "." + "R_561" + " set "
                + stringBuilder.toString() + " where period='" + "2019201" + "' and department_id='" + "2001" +
                "'";
        System.out.println(updateSql);
    }
}
