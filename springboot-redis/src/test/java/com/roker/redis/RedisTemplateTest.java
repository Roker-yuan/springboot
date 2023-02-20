package com.roker.redis;

import com.roker.redis.model.UserVo;
import com.roker.redis.util.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootRedisApplication.class)
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * obj api测试
     *
     * @throws Exception 异常
     */
    @Test
    public void objApiTest() throws Exception {
        // 设置对象值，并且2秒自动过期
        ValueOperations<String, UserVo> operations = redisTemplate.opsForValue();
        UserVo user = new UserVo("aa@126.com", "张三");
        operations.set("user", user, 2, TimeUnit.SECONDS);

        //获取对象值
        UserVo userVo = operations.get("user");
        System.out.println(userVo.toString());
        System.out.println("获取user过期时间（单位秒）：" + redisTemplate.getExpire("user"));


        //删除key
        Boolean deleteValue = redisTemplate.delete("user");
        System.out.println("删除userName结果：" +  deleteValue);
    }

    /**
     * 列表api测试
     *
     * @throws Exception 异常
     */
    @Test
    public void listApiTest() throws Exception {
        // 向列表中添加数据
        ListOperations<String, UserVo> operations = redisTemplate.opsForList();
        // 往List左侧插入一个元素
        operations.leftPush("userList", new UserVo("aa@126.com", "张三"));
        operations.leftPush("userList", new UserVo("bb@126.com", "里斯"));
        //往 List 右侧插入一个元素
        operations.rightPush("userList", new UserVo("cc@126.com", "王五"));
        operations.rightPush("userList", new UserVo("dd@126.com", "赵六"));
        // 获取List 大小
        Long size = operations.size("userList");
        System.out.println("获取列表总数：" + size);
        //遍历整个List
        List<UserVo> allUserVo1 = operations.range("userList", 0, size);
        System.out.println("遍历列表所有数据：" + JacksonUtils.objToStr(allUserVo1));
        //遍历整个List，-1表示倒数第一个即最后一个
        List<UserVo> allUserVo2 = operations.range("userList", 0, -1);
        System.out.println("遍历列表所有数据：" + JacksonUtils.objToStr(allUserVo2));
        //从 List 左侧取出第一个元素，并移除
        Object userVo1 = operations.leftPop("userList", 200, TimeUnit.MILLISECONDS);
        System.out.println("从左侧取出第一个元素并移除：" + userVo1.toString());
        System.out.println("遍历列表所有数据：" + JacksonUtils.objToStr(allUserVo1));
        //从 List 右侧取出第一个元素，并移除
        Object userVo2 = operations.rightPop("userList", 200, TimeUnit.MILLISECONDS);
        System.out.println("从右侧取出第一个元素并移除：" + userVo2.toString());
        System.out.println("遍历列表所有数据：" + JacksonUtils.objToStr(allUserVo1));

    }

    /**
     * 哈希api测试
     *
     * @throws Exception 异常
     */
    @Test
    public void hashApiTest() throws Exception {
        // 向hash中添加数据
        HashOperations<String, String, Integer> operations = redisTemplate.opsForHash();
        //Hash 中新增元素。
        operations.put("score", "张三", 2);
        operations.put("score", "里斯", 1);
        operations.put("score", "王五", 3);
        operations.put("score", "赵六", 4);

        Boolean hasKey = operations.hasKey("score", "张三");
        System.out.println("检查是否存在【score】【张三】：" + hasKey);
        Integer value = operations.get("score", "张三");
        System.out.println("获取【score】【张三】的值：" + value);
        Set<String> keys = operations.keys("score");
        System.out.println("获取hash表【score】所有的key集合：" + JacksonUtils.objToStr(keys));
        List<Integer> values = operations.values("score");
        System.out.println("获取hash表【score】所有的value集合：" + JacksonUtils.objToStr(values));
        Map<String,Integer> map = operations.entries("score");
        System.out.println("获取hash表【score】下的map数据：" + JacksonUtils.objToStr(map));
        Long delete = operations.delete("score", "里斯");
        System.out.println("删除【score】中key为【里斯】的数据：" + delete);
        Boolean result = redisTemplate.delete("score");
        System.out.println("删除整个key：" + result);

    }

    /**
     * 集合api测试
     *
     * @throws Exception 异常
     */
    @Test
    public void setApiTest() throws Exception {
        // 向集合中添加数据
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        //向集合中添加元素,set元素具有唯一性
        operations.add("city", "北京","上海", "广州", "深圳", "武汉");
        Long size = operations.size("city");
        System.out.println("获取集合总数：" + size);
        //判断是否是集合中的元素
        Boolean isMember = operations.isMember("city", "广州");
        System.out.println("检查集合中是否存在指定元素：" + isMember);
        Set<String> cityNames = operations.members("city");
        System.out.println("获取集合所有元素：" + JacksonUtils.objToStr(cityNames));
        Long remove = operations.remove("city", "广州");
        System.out.println("删除指定元素结果：" + remove);
        //移除并返回集合中的一个随机元素
        String cityName = operations.pop("city");
        System.out.println("移除并返回集合中的一个随机元素：" + cityName);
    }

    /**
     * z组api测试
     *
     * @throws Exception 异常
     */
    @Test
    public void zSetApiTest() throws Exception {
        // 向有序集合中添加数据
        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
        //向有序集合中添加元素,set元素具有唯一性
        operations.add("cityName", "北京", 100);
        operations.add("cityName", "上海", 95);
        operations.add("cityName", "广州", 75);
        operations.add("cityName", "深圳", 85);
        operations.add("cityName", "武汉", 70);

        //获取变量指定区间的元素。0, -1表示全部
        Set<String> ranges = operations.range("cityName", 0, -1);
        System.out.println("获取有序集合所有元素：" + JacksonUtils.objToStr(ranges));
        Set<String> byScores = operations.rangeByScore("cityName", 85, 100);
        System.out.println("获取有序集合所有元素（按分数从小到大）："+ JacksonUtils.objToStr(byScores));
        Long zCard = operations.zCard("cityName");
        System.out.println("获取有序集合成员数: " + zCard);
        Long remove = operations.remove("cityName", "武汉");
        System.out.println("删除某个成员数结果: " + remove);

    }

}
