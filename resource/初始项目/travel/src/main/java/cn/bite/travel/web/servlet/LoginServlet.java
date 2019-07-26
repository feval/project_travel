package cn.bite.travel.web.servlet;

import cn.bite.travel.domain.ResultInfo;
import cn.bite.travel.domain.User;
import cn.bite.travel.service.UserService;
import cn.bite.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/23 10:38
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码逻辑
        //接受前台参数:username,password
        Map<String, String[]> map = request.getParameterMap();
        User user=new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService=new UserServiceImpl();
        User u = userService.findByUsernaemAndPassword(user);
        ResultInfo info=new ResultInfo();
        if (u==null) {
            //  不存在
            info.setFlag(false);
            info.setErrorMsg("用户名和密码不匹配");
        }
        if (u!=null&& !"Y".equals(u.getStatus())) {
            //尚未激活
            info.setFlag(false);
            info.setErrorMsg("尚未激活");
        }
        if (u!=null&& "Y".equals(u.getStatus())) {
            request.getSession().setAttribute("user",u);
            info.setFlag(true);
        }
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(info);
        //mapper.writeValue(response.getOutputStream(),info);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
