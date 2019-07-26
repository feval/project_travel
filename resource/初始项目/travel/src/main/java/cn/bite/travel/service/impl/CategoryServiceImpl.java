package cn.bite.travel.service.impl;

import cn.bite.travel.dao.CategoryDao;
import cn.bite.travel.dao.impl.CategoryDaoImpl;
import cn.bite.travel.domain.Category;
import cn.bite.travel.service.CategoryService;
import cn.bite.travel.util.JedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/24 9:14
 */
public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        Jedis jedis = JedisUtil.getJedis();
        //查询key中记录的值
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 10, -1);
        List<Category> list = null;
        //判断
        if (categorys == null || categorys.size() == 0) {
            System.out.println("从数据库中获取分类信息...");
            list = categoryDao.findAll();
            //遍历集合  将信息存储到redis中
            for (int i = 0; i < list.size(); i++) {
                //将当前的cid和cname存储到redis中

                jedis.zadd("category", list.get(i).getCid(), list.get(i).getCname());
            }
        } else {
            //创建一个list集合
            list = new ArrayList<>();
            System.out.println("从redis数据库中获取分类信息");
            Category category = new Category();
            for (Tuple tuple : categorys) {
                category.setCid((int) tuple.getScore());
                category.setCname(tuple.getElement());
                list.add(category);
            }
        }


        //  return categoryDao.findAll();
        return null;
    }
}
