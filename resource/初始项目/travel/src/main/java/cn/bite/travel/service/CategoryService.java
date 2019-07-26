package cn.bite.travel.service;

import cn.bite.travel.domain.Category;

import java.util.List;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/24 9:13
 */
public interface CategoryService {
    List<Category> findAll();
}
