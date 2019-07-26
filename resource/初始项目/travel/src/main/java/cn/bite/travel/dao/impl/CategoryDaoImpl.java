package cn.bite.travel.dao.impl;

import cn.bite.travel.dao.CategoryDao;
import cn.bite.travel.domain.Category;
import cn.bite.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/24 9:15
 */
public class CategoryDaoImpl implements CategoryDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public List<Category> findAll() {
        String sql="select * from  tab_category";
        return template.query(sql,new BeanPropertyRowMapper<Category>(Category.class));
    }
}
