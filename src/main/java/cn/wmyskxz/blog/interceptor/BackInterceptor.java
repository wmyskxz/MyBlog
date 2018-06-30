package cn.wmyskxz.blog.interceptor;

import cn.wmyskxz.blog.entity.User;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台拦截器
 * 说明：对/admin开头的地址进行拦截，必须经过验证之后才能够访问
 *
 * @author:wmyskxz
 * @create:2018-06-20-下午 18:47
 */
public class BackInterceptor implements HandlerInterceptor {

    private static String username = "wmyskxz";
    private static String password = "123456";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = true;
//        System.out.println("进入成功！");
        User user = (User) request.getSession().getAttribute("user");
        if (null == user) {
            // 如果用户为空则跳转到error页面
            // 2018年6月29日20:23:05：修改sendRedirect方法为getRequestDispatcher，
            // 目的是保证地址栏不改变（与前台错误页面响应一致），这样用户就不知道后台页面的地址
            request.getRequestDispatcher(request.getContextPath() + "/error.html").forward(request, response);
//            response.sendRedirect(request.getContextPath() + "/error.html");
            flag = false;
        } else {
            // 对用户账号进行验证,是否正确
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
//                System.out.println("访问后台API");
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }
}
