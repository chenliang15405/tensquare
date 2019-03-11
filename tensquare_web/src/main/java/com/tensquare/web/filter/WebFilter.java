package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 网关过滤器，因为请求经过网关之后，没有将头信息转发给微服务，所以需要在过滤器中
 * 进行处理，然后重新传递头信息 否则token无法验证
 */
@Component
public class WebFilter extends ZuulFilter {

    /**
     * 表示在请求前：pre后者请求后 post执行
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 多个过滤器执行的顺序，数字越小，表示越先执行
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 表示过滤器是否开启，true开启，false不开启
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作，return 任何object类型的值都表示继续执行
     * setsendzuulResponse(false) 就表示不在执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("经过web网关过滤器");
        //得到request上下文
        RequestContext context = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = context.getRequest();
        //得到头信息
        String header = request.getHeader("Authorization");
        //判读是否有token的头信息
        if(header != null && !"".equals(header)){
            //把头信息继续向下传递
            context.addZuulRequestHeader("Authorization",header);
        }
        //放行
        return null;
    }
}
