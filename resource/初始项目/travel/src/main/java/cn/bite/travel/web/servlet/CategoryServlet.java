/*
package cn.bite.travel.web.servlet;

import cn.bite.travel.domain.Category;
import cn.bite.travel.service.CategoryService;
import cn.bite.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

*/
/**
 * Description:
 * Author:  llf
 * Created in 2019/7/24 9:12
 *//*

@WebServlet(name = "/category/*")
public class CategoryServlet extends BaseServlet {
    private CategoryService categoryService=new CategoryServiceImpl();
    protected void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> list = categoryService.findAll();
*/
/*        ObjectMapper mapper=new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),list);*//*

        writeValue(list,response);
    }
}
*/
