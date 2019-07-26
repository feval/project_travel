package cn.bite.travel.dao;

import cn.bite.travel.domain.Category;

import java.util.List;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/24 9:15
 */
public interface CategoryDao {
    List<Category> findAll();
}
