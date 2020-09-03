package test;

import Entitys.Entity;
import Entitys.TreeNode;
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
import java.util.concurrent.locks.ReentrantLock;
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
        if (false || returnFalse() && returnTrue()) {
            System.out.println(1);
        }
    }

    public static boolean returnTrue() {
        System.out.println("真");
        return true;
    }

    public static boolean returnFalse() {
        System.out.println("假");
        return false;
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

    @Test
    public void test8() throws IOException {

        List<String> columns = Lists.newArrayList("DEPARTMENT_ID", "PERIOD", "CUSTOMER_NO", "SORT_INDEX");
        List<String> result = Lists.newArrayList();
        for (int i = 0; i < 9; i++) {
            String insertSql = "insert into KGRP.R_789(DEPARTMENT_ID,PERIOD,CUSTOMER_NO,SORT_INDEX,A2,B2) values" +
                    "('$DEPARTMENT_ID$','$PERIOD$','$CUSTOMER_NO$','$SORT_INDEX$','$A2$','$B2$')";
            for (String column : columns) {
                String replacement = "$" + column + "$";
                if ("$SORT_INDEX$".equals(replacement)) {
                    insertSql = StringUtils.replace(insertSql, replacement, String.valueOf(i));
                } else if ("$DEPARTMENT_ID$".equals(replacement)) {
                    insertSql = StringUtils.replace(insertSql, replacement, "100100");
                } else if ("$PERIOD$".equals(replacement)) {
                    insertSql = StringUtils.replace(insertSql, replacement, "20191129");
                } else if ("$CUSTOMER_NO$".equals(replacement)) {
                    insertSql = StringUtils.replace(insertSql, replacement, "79023");
                } else {
                    insertSql = StringUtils.replace(insertSql, replacement, "");
                }
            }
            result.add(insertSql);
        }
        result.stream().forEach(System.out::println);
    }

    @Test
    public void speedTest() throws ClassNotFoundException, NoSuchFieldException, InterruptedException {
        Object lock = new Object();
        Thread a = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    for (int i = 0; i < 10; i++) {
                        System.out.println(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread b = new Thread(() -> {
            synchronized (lock) {
                lock.notify();
            }
        });
        a.start();
        Thread.sleep(1000);
        b.start();

    }

    @Test
    void rebuildTreeBySerl() throws Exception {
        // 终于
        int[] preorder = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] inorder = {4, 7, 2, 1, 5, 3, 8, 6};
        // 根据先序和中序序列重建二叉树
        TreeNode root =  rebuildTree(preorder, 0, preorder.length-1,
                inorder, 0, inorder.length-1);

        // 获取每一层中的节点 宽度优先？
        List<HashMap<Integer, Object>> list = Lists.newArrayList();
        traveTree(0, root, list);
        Map<Integer, List<Object>> collect = list.stream().collect(Collectors.groupingBy(t -> t.keySet().iterator().next(),
                Collectors.mapping(t -> t.values().iterator().next(), Collectors.toList())));
        System.out.println(collect);

        Integer i = Optional.of(1).orElse(0);

    }

    private void traveTree(int i, TreeNode root, List<HashMap<Integer, Object>> list) {
        if (root != null) {
            int n = i + 1;
            List<Object> innerList = Lists.newArrayList();
            HashMap<Integer, Object> map = Maps.newHashMap();
            map.put(n, root.value);
            innerList.add(map);
            list.add(map);

            traveTree(n, root.left, list);
            traveTree(n, root.right, list);
        }
    }

    private TreeNode rebuildTree(int[] preorder, int startPreorder, int endPreorder, int[] inorder, int startInorder,
                                 int endInorder) {
        TreeNode root = new TreeNode();
        // 获取root元素在中序中的相对位置
        int breakLength = getBreakLength(preorder, startPreorder, inorder, startInorder, endInorder);

        int lStartInorder = startInorder;
        int lEndInorder = startInorder + breakLength - 1;
        int lStartPreorder = startPreorder + 1;
        int lEndPreorder = startPreorder + breakLength;

        int rStartInorder = startInorder + breakLength + 1;
        int rEndInorder = endInorder;
        int rStartPreorder = startPreorder + breakLength + 1;
        int rEndPreorder = endPreorder;

        root.value = preorder[startPreorder];
        root.left = breakLength == 0 ? null :
                rebuildTree(preorder, lStartPreorder, lEndPreorder, inorder, lStartInorder, lEndInorder);
        root.right = (breakLength == endInorder - startInorder) ? null :
                rebuildTree(preorder, rStartPreorder, rEndPreorder, inorder, rStartInorder, rEndInorder);
        return root;
    }

    private int getBreakLength(int[] preorder, int startPreorder, int[] inorder, int startInorder, int endInorder) {
        for (int i = startInorder; i < endInorder + 1; i++) {
            if(inorder[i] == preorder[startPreorder])
                return i - startInorder;
        }
        return -1;
    }

    @Test
    void test12() throws IOException {
        System.out.println("开始监听");
        System.in.read();
    }

}
