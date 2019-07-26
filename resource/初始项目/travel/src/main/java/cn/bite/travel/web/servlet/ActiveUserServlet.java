package cn.bite.travel.web.servlet;

import cn.bite.travel.service.UserService;
import cn.bite.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:激活用户
 * Author:  llf
 * Created in 2019/7/16 19:40
 */
@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收激活码
       /* String code = request.getParameter("code");
        //判断激活码是否为空
        if (code != null) {
            UserService userService = new UserServiceImpl();
            boolean flag = userService.active(code);
            //提示信息(向浏览器响应内容)
            String msg="";
            if (flag) {
                //激活成功
                msg="您已经激活成功了.请<a href='login.html'>登陆</a>";
            } else {
                //激活失败
                msg="激活失败";
            }
            //设置中文乱码
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }*/
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
