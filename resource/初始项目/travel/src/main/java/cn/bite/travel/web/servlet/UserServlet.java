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


@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    protected void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("");

        String check = request.getParameter("check");
        String checkcode_server = (String)request.getSession().getAttribute("CHECKCODE_SERVER");
        request.getSession().removeAttribute("CHECKCODE_SERVER");
        if(checkcode_server==null || !check.equalsIgnoreCase(checkcode_server)) {
            ResultInfo resultInfo=new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码存在问题");
            ObjectMapper mapper=new ObjectMapper();
            String json = mapper.writeValueAsString(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

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
        boolean flag=userService.regist(user);
        ResultInfo resultInfo=new ResultInfo();
        if (flag) {
            resultInfo.setFlag(true);
        }else{
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resultInfo);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("");
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
    protected void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("");
        //从session中获取
        User user=(User)request.getSession().getAttribute("user");
        //判断是否为空
        if (user!=null) {
            //服务器将user对象响应到前台页面:header.html
            ObjectMapper mapper=new ObjectMapper();
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),user);
        }
    }
    protected void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("");
        String code = request.getParameter("code");
        if (code != null) {
            UserService userService = new UserServiceImpl();
            boolean flag = userService.active(code);
            String msg ="";
            if (flag) {
                msg="您的账户激活成功,请<a href='login.html'>登陆</a>";
            }else{
                msg="激活失败,请联系管理员";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

}
