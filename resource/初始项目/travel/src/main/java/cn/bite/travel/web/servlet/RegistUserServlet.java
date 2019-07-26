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
 * Created in 2019/7/16 16:56
 */
@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*        //加入验证码操作
        //获取前台页面中的验证码
        String check = request.getParameter("check");
        //从session域中获取服务器随机生成的验证码
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        //一次性验证,防止验证码被复用
        request.getSession().removeAttribute("CHECKCODE_SERVER");
        //如果不一致,
        //防止空指针
        if (checkcode_server==null||!checkcode_server.equalsIgnoreCase(check)) {
            //验证码存在问题
            ResultInfo resultInfo=new ResultInfo();
            //设置响应数据
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码不正确");
            //服务器将数据响应到前台
            //创建json的解析对象(jacksonde jar包核心类)
            ObjectMapper mapper=new ObjectMapper();
            String json = mapper.writeValueAsString(resultInfo);
            //设置服务器响应格式
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //获取参数数据
        Map<String, String[]> map = request.getParameterMap();
        //封装user对象
        User user=new User();
        //使用commons-beanUtils工具类:封装javabean
        //BeanUtils.populate(user,map);
        //调用Service,查询是否存在该用户
        UserService userService=new UserServiceImpl();
        boolean flag = userService.regist(user);
        //创建响应结果对象:ResultInfo
        ResultInfo resultInfo=new ResultInfo();
        //判断当前是否注册成功
        if (flag) {
            //注册成功
            resultInfo.setFlag(true);
        }else{
            //注册失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败!!!");
        }

        //服务器将数据响应到前台
        //创建json的解析对象(jacksonde jar包核心类)
        ObjectMapper mapper=new ObjectMapper();
        String json = mapper.writeValueAsString(resultInfo);
        //设置服务器响应格式
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);*/

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
