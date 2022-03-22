package com.liangzhicheng;

import com.google.common.collect.*;
import com.liangzhicheng.modules.entity.Dept;
import com.liangzhicheng.modules.entity.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.*;

public class GuavaMapTest {

    /**
     * Table - 双键Map
     */
    @Test
    public void testTable(){
        //普通map
        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
        Map<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("AA", 1);
        paramsMap.put("BB", 2);
        resultMap.put("CC", paramsMap);
        Integer num = resultMap.get("CC").get("AA");
        System.out.println("[普通map] num：" + num);

        //guava中table map
        Table<String, String, Integer> tableMap = HashBasedTable.create();
        tableMap.put("CC", "AA", 1);
        tableMap.put("CC", "BB", 2);
        tableMap.put("DD", "AA", 2);
        tableMap.put("DD", "BB", 3);
        num = tableMap.get("DD", "AA");
        System.out.println("[guava中map] num：" + num);

        //获取key或value的集合
        getList(tableMap);

        //计算key对应的所有value的和
        calcSum(tableMap);

        //转换rowKey和columnKey
        convert(tableMap);

        //转换嵌套的map
        convertMap(tableMap);
    }
    private void getList(Table<String, String, Integer> tableMap){
        //key不重复
        Set<String> rowKeys = tableMap.rowKeySet();
        Set<String> columnKeys = tableMap.columnKeySet();
        //value重复
        Collection<Integer> values = tableMap.values();
        System.out.println("[第一个键] rowKeys：" + rowKeys);
        System.out.println("[第二个键] columnKeys：" + columnKeys);
        System.out.println("[最终值] values：" + values);
    }
    private void calcSum(Table<String, String, Integer> tableMap) {
        for(String key : tableMap.rowKeySet()){
            Set<Map.Entry<String, Integer>> rows = tableMap.row(key).entrySet();
            int total = 0;
            for(Map.Entry<String, Integer> row : rows){
                total += row.getValue();
            }
            System.out.println("[计算和] " + key + "：" + total);
        }
    }
    private void convert(Table<String, String, Integer> tableMap) {
        tableMap = Tables.transpose(tableMap);
        Set<Table.Cell<String, String, Integer>> cells = tableMap.cellSet();
        cells.forEach(cell -> System.out.println("[转换键值] "
                + cell.getRowKey()
                + ", "
                + cell.getColumnKey()
                + "："
                + cell.getValue()));
    }
    private void convertMap(Table<String, String, Integer> tableMap) {
        Map<String, Map<String, Integer>> rowMap = tableMap.rowMap();
        Map<String, Map<String, Integer>> columnMap = tableMap.columnMap();
        System.out.println("[转换map] rowMap：" + rowMap);
        System.out.println("[转换map] columnMap：" + columnMap);
    }

    /**
     * BiMap - 双向Map
     */
    @Test
    public void testBiMap(){
        //普通map中，如果想要根据value查找对应的key，无论是使用for循环还是迭代器，都需要遍历整个map，以循环keySet的方式
        String value = "天空";
        Map<String, Object> map = new HashMap<>();
        map.put("AA", value);
        map.put("BB", "地表");
        List<String> keys = new ArrayList<>();
        for(String key : map.keySet()){
            if(map.get(key).equals(value)){
                keys.add(key);
            }
        }
        System.out.println("[普通map] keys：" + keys);

        //guava中BiMap
        HashBiMap<@Nullable String, @Nullable String> biMap = HashBiMap.create();
        biMap.put("AA", "aa");
        biMap.put("BB", "bb");
        biMap.put("CC", "cc");
        //使用key获取value
        System.out.println("[guava中BiMap] " + biMap.get("CC"));
        //使用value获取key
        BiMap<@Nullable String, @Nullable String> inverse = biMap.inverse();
        System.out.println("[guava中BiMap] " + inverse.get("aa"));

        //反转后原先值为bb对应的键是BB，现在键变成了DD
        inverse.put("bb", "DD");
        System.out.println(biMap);

        //把新的key映射到已有的value上，可以使用forcePut方法强制替换掉原有的key
        biMap.forcePut("ZZ", "aa");
        System.out.println(biMap);
    }

    /**
     * Multimap - 多值Map
     */
    @Test
    public void testMultiMap(){
        //普通map
        Map<String, List<Integer>> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        map.put("num", list);
        System.out.println("[普通map] map：" + map);

        //guava中Multimap
        ArrayListMultimap<@Nullable String, @Nullable Integer> multimap = ArrayListMultimap.create();
        multimap.put("num", 1);
        multimap.put("num", 2);
        multimap.put("num", 6);
        multimap.put("total", 8);
        System.out.println("[guava中Multimap] multimap：" + multimap);

        //获取值的集合
        List<@Nullable Integer> nums = multimap.get("num");
        System.out.println("[值的集合] nums：" + nums);
        //移除元素
        nums.remove(0);
        //添加元素
        List<@Nullable Integer> totals = multimap.get("total");
        totals.add(9);
        System.out.println(multimap);

        //转换为map
        convertMap(multimap);

        //数量
        size(multimap);
    }
    private void convertMap(ArrayListMultimap<String, Integer> multimap) {
        Map<String, Collection<Integer>> asMap = multimap.asMap();
        for(String key : asMap.keySet()){
            System.out.println("[转换map] " + key + "：" + asMap.get(key));
        }
        asMap.get("num").add(5);
        System.out.println(multimap);
    }
    private void size(ArrayListMultimap<String, Integer> multimap) {
        System.out.println("[multimap中数量] size：" + multimap.size());
        System.out.println("[multimap中数量] entries size：" + multimap.entries().size());
        for (Map.Entry<String, Integer> entry : multimap.entries()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
        //keySet中保存的是不同的key的个数
        System.out.println(multimap.keySet().size());

        //转换map后个数
        Set<Map.Entry<String, Collection<Integer>>> entries = multimap.asMap().entrySet();
        System.out.println(entries.size());
    }

    /**
     * RangeMap - 范围Map
     */
    @Test
    public void testRangeMap(){
        //普通if else
        String result = general(91);
        System.out.println("[普通if else] result：" + result);
        //guava中RangeMap
        rangeMap();
    }
    private String general(int score) {
        if(score >= 0 && score < 60){
            return "不及格";
        }else if(score >= 60 && score <= 90){
            return "良好";
        }else if(score > 90 && score <= 100){
            return "优秀";
        }
        return "";
    }
    private void rangeMap() {
        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closedOpen(0,60),"不及格");
        rangeMap.put(Range.closed(60,90),"良好");
        rangeMap.put(Range.openClosed(90,100),"优秀");
        System.out.println("[guava中RangeMap] " + rangeMap.get(59));
        System.out.println("[guava中RangeMap] " + rangeMap.get(86));
        System.out.println("[guava中RangeMap] " + rangeMap.get(95));
    }

    /**
     * ClassToInstanceMap - 实例Map
     *
     * 特殊的Map，它的键是class，而值是这个class对应的实例对象
     */
    @Test
    public void testClassToInstanceMap(){
        //普通map
        Map<Class, Object> map = new HashMap<>();
        User user1 = new User("AA",18);
        Dept dept1 = new Dept("develop",100);
        map.put(User.class, user1);
        map.put(Dept.class, dept1);
        User user2 = (User) map.get(User.class);
        System.out.println(user1 == user2);

        //guava中ClassToInstanceMap，省去强制类型转换
        ClassToInstanceMap<Object> instanceMap = MutableClassToInstanceMap.create();
        User user3 = new User("BB", 18);
        Dept dept2 = new Dept("beta", 100);
        instanceMap.putInstance(User.class, user3);
        instanceMap.putInstance(Dept.class, dept2);
        User user4 = instanceMap.getInstance(User.class);
        System.out.println(user3 == user4);

        //同类型使用
        ClassToInstanceMap<Map> instanceMap2 = MutableClassToInstanceMap.create();
        HashMap<String, Object> hashMap = new HashMap<>();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        ArrayList<Object> arrayList = new ArrayList<>();

        instanceMap2.putInstance(HashMap.class, hashMap);
        instanceMap2.putInstance(TreeMap.class, treeMap);
        //因为HashMap和TreeMap都集成了Map父类，放入其他类型编译报错
//        instanceMap2.putInstance(ArrayList.class, arrayList);
    }

}
