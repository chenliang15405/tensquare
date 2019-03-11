package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.apache.http.protocol.RequestContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 网关过滤器，因为请求经过网关之后，没有将头信息转发给微服务，所以需要在过滤器中
 * 进行处理，然后重新传递头信息 否则token无法验证
 */
@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 表示在请求前：pre后者请求后 post执行
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 多个过滤器执行的顺序，数字越小，表示越先执行
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 表示过滤器是否开启，true开启，false不开启
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作，return 任何object类型的值都表示继续执行
     * context.setSendZuulResponse(false); 就表示不在执行
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("经过Manager网关过滤器");
        //得到request上下文
        RequestContext context = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = context.getRequest();

        //网关内部还有一个分发的请求，还没有向微服务跳转的时候，所以这个请求直接可以放行
        if(request.getMethod().equals("OPTIONS")){
            //放行
            return null;
        }
        //如果是登录请求，则直接放行
        if(request.getRequestURI().indexOf("login")>0){
            System.out.println("admin登录页面url : " + request.getRequestURL().toString());
            return null;
        }

        //得到头信息
        String header = request.getHeader("Authorization");
        //判读是否有token的头信息
        if (header != null && !"".equals(header)) {
            if (header.startsWith("Bearer ")) {
                String token = header.substring(7);

                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    //后台的网关只能admin用户登录，所以只转换角色是admin的请求
                    if(roles.equals("admin")){
                        //转发头信息下去，并且放行
                        context.addZuulRequestHeader("Authorization",header);
                        return null;
                    }
                } catch (Exception e) {
                    //网关过滤器不进行转发 终止运行
                    context.setSendZuulResponse(false);
                }
            }
        }
        //终止运行
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(403);
        context.setResponseBody("权限不足");
        context.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
