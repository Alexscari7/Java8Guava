package test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 10_000_000, 0.0001);
        IntStream.rangeClosed(1,10_000_000).boxed().map(String::valueOf).forEach(i -> bf.put(i));
        long start = System.nanoTime();
        System.out.println(bf.mightContain("123"));
        System.out.println((System.nanoTime() - start)/1000_000.0);
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
    public void speedTest(){
        String modelFieldMapJson = "{\"41\":{\"0\":\"中信证券股份有限公司\",\"1\":\"管理类别一\"},\"1\":{\"0\":\"中信信托有限责任公司\"," +
                "\"1\":\"管理类别一\"},\"2\":{\"0\":\"中信保诚人寿保险有限公司\",\"1\":\"管理类别一\"},\n" +
                "\"3\":{\"0\":\"中信资产管理有限公司\",\"1\":\"管理类别二\"},\"4\":{\"0\":\"中信财务有限公司\",\"1\":\"管理类别二\"},\"5\":{\"0\":\"中信重工机械股份有限公司\",\"1\":\"管理类别二\"},\n" +
                "\"6\":{\"0\":\"中信戴卡股份有限公司\",\"1\":\"管理类别二\"},\"7\":{\"0\":\"中信泰富特钢集团有限公司\",\"1\":\"管理类别二\"},\"8\":{\"0\":\"中信建设有限责任公司\",\"1\":\"管理类别三\"},\n" +
                "\"9\":{\"0\":\"中信工程设计建设有限公司\",\"1\":\"管理类别三\"},\"10\":{\"0\":\"中信出版集团股份有限公司\",\"1\":\"管理类别三\"},\"11\":{\"0\":\"中信医疗健康产业集团有限公司\",\n" +
                "\"1\":\"管理类别三\"},\"12\":{\"0\":\"中信农业科技股份有限公司\",\"1\":\"管理类别三\"},\"13\":{\"0\":\"中信金属集团有限公司\",\"1\":\"管理类别三\"},\"14\":{\"0\":\"中信和业投资有限公司\",\n" +
                "\"1\":\"管理类别四\"},\"15\":{\"0\":\"中信银行股份有限公司\",\"1\":\"管理类别一\"},\"16\":{\"0\":\"华夏基金管理有限公司\",\"1\":\"管理类别一\"},\"17\":{\"0\":\"中信期货有限公司\",\n" +
                "\"1\":\"管理类别一\"},\"18\":{\"0\":\"中信建投证券股份有限公司\",\"1\":\"管理类别一\"},\"19\":{\"0\":\"中信建投基金管理有限公司\",\"1\":\"管理类别一\"},\"20\":{\"0\":\"中信建投期货有限公司\",\n" +
                "\"1\":\"管理类别一\"},\"21\":{\"0\":\"中信保诚基金管理有限公司\",\"1\":\"管理类别一\"},\"22\":{\"0\":\"中信国际电讯集团有限公司\",\"1\":\"管理类别二\"},\"23\":{\"0\":\"中信数字媒体网络有限公司\",\n" +
                "\"1\":\"管理类别二\"},\"24\":{\"0\":\"中信云网有限公司\",\"1\":\"管理类别二\"},\"25\":{\"0\":\"中信控股有限责任公司\",\"1\":\"管理类别二\"},\"26\":{\"0\":\"中信海洋直升机股份有限公司\",\"1\":\n" +
                "\"管理类别三\"},\"27\":{\"0\":\"中信投资控股有限公司\",\"1\":\"管理类别三\"},\"28\":{\"0\":\"中信环境投资集团有限公司\",\"1\":\"管理类别三\"},\"29\":{\"0\":\"中信国安葡萄酒业股份有限公司\",\n" +
                "\"1\":\"管理类别三\"},\"30\":{\"0\":\"大昌行集团有限公司\",\"1\":\"管理类别三\"},\"31\":{\"0\":\"中信机电制造公司\",\"1\":\"管理类别四\"},\"32\":{\"0\":\"中信矿业科技发展有限公司\",\n" +
                "\"1\":\"管理类别四\"},\"33\":{\"0\":\"中信兴业投资集团有限公司\",\"1\":\"管理类别四\"},\"34\":{\"0\":\"中信资产运营有限公司\",\"1\":\"管理类别四\"},\"35\":{\"0\":\"中信置业有限公司\",\n" +
                "\"1\":\"管理类别四\"},\"36\":{\"0\":\"中信城市开发运营有限责任公司\",\"1\":\"管理类别四\"},\"37\":{\"0\":\"中信泰富有限公司\",\"1\":\"管理类别四\"},\"38\":{\"0\":\"中信国安集团有限公司\",\n" +
                "\"1\":\"管理类别四\"},\"39\":{\"0\":\"中信建筑设计研究总院有限公司\",\"1\":\"管理类别三\"},\"40\":{\"0\":\"中国市政工程中南设计研究总院有限公司\",\"1\":\"管理类别三\"}}";
        Map<String, List<String>> modelFieldMap = JSONObject.parseObject(modelFieldMapJson, Map.class);
        List<String> sortIndexList = Lists.newArrayList(modelFieldMap.keySet());
        int maxCountOfSingleThread = (int) Math.ceil(Math.sqrt(sortIndexList.size()));
        List<Map<String, List<String>>> childModelFieldList = new ArrayList<>();
        int taskCount = (int) Math.ceil(sortIndexList.size() * 1.0 / maxCountOfSingleThread);
        System.out.println(taskCount);
        for (int i = 0; i < taskCount; i++) {
            Map<String, List<String>> childModelFieldMap = Maps.newHashMap();
            for (int j = i * maxCountOfSingleThread; j < (i + 1) * maxCountOfSingleThread; j++) {
                if (j < sortIndexList.size()) {
                    String sortIndex = sortIndexList.get(j);
                    childModelFieldMap.put(sortIndex, modelFieldMap.get(sortIndex));
                }
            }
            childModelFieldList.add(childModelFieldMap);
        }

        childModelFieldList.forEach(System.out::println);
    }

    @Test
    public void test9() throws Exception {
        String s = "[{\"departmentId\":\"100100\",\"parentDepartmentId\":\"-1\"},{\"departmentId\":\"wusdep4\"," +
                "\"parentDepartmentId\":\"100100\"},{\"departmentId\":\"wusdep5\",\"parentDepartmentId\":\"100100\"}]";
        List<TaskNodeDepartmentInfoInstEntity> taskNodeDepartmentInfoInstEntities = JSONArray.parseArray(s, TaskNodeDepartmentInfoInstEntity.class);
        List<TaskNodeDepartmentInfoInstEntity> nodeList = Lists.newArrayList();
        getAllParentNodes(taskNodeDepartmentInfoInstEntities, nodeList, "wusdep4");
        //getRejectTaskNodeDepartmentInfoInstEntityList(taskNodeDepartmentInfoInstEntities, nodeList, "wusdep4","wusdep4");
        nodeList.forEach(t -> System.out.println(t.getDepartmentId()));


    }

    private static void getBetweenNodes(
            List<TaskNodeDepartmentInfoInstEntity> taskNodeDepartmentInfoInstEntities,
            List<TaskNodeDepartmentInfoInstEntity> parentNodeList, String start, String end) throws Exception {
        if (start.equals(end)) {
            return;
        }
        TaskNodeDepartmentInfoInstEntity node =
                taskNodeDepartmentInfoInstEntities.stream().filter(t -> start.equals(t.getDepartmentId())).findAny().orElseThrow(() -> new Exception("顶级节点不存在"));
        parentNodeList.add(node);
        String newStart = node.getParentDepartmentId();
        getBetweenNodes(taskNodeDepartmentInfoInstEntities, parentNodeList, newStart, end);

    }

    public static void getAllParentNodes(List<TaskNodeDepartmentInfoInstEntity> allNodeList,
                                         List<TaskNodeDepartmentInfoInstEntity> parentNodeList, String start) throws Exception {
        getBetweenNodes(allNodeList, parentNodeList, start, "-1");
        parentNodeList.removeIf(t -> start.equals(t.getDepartmentId()));
    }

    @Test
    public void test10() throws Exception {
        Map<String, String> map = Collections.EMPTY_MAP;
        System.out.println(map.isEmpty());
        System.out.println(map.size());
        System.out.println(map == null);
    }

}