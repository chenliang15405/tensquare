package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensquare.user.pojo.Admin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
    private RedisTemplate redisTemplate;
	@Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;

    /**
     * 更新粉丝数和关注数，这个方法是用来给feign客户端使用，所以不用返回值也可以
     */
    @RequestMapping(value = "/{userid}/{friendid}/{x}",method = RequestMethod.PUT)
    public void updateFansAndFollowCount(@PathVariable String userid,@PathVariable String friendid,@PathVariable int x){
        userService.updateFansAndFollowCount(x,userid,friendid);
    }


    /**
     * 登录
     * @param user
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody User user){
        User loginUser = userService.login(user);
        if(loginUser == null){
            return new Result(true,StatusCode.LONGINERROR,"登录失败");
        }
        //使得前后端可以通话的操作，使用jwt实现

        //生成token令牌
        String token = jwtUtil.createJWT(loginUser.getId(), user.getMobile(), "user");
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        map.put("role","user");
        return new Result(true,StatusCode.OK,"登录成功",map);
    }


	/**
	 * 发送短信验证码
	 * @return
	 */
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendSms(@PathVariable String mobile){
        userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"验证码发送成功");
	}


    /**
     * 用户注册
     * @param code
     * @param user
     * @return
     */
    @RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result register(@PathVariable String code,@RequestBody User user){
	    //得到缓存中的验证码
        String checkcodeRedis = (String) redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
        if(checkcodeRedis.isEmpty()){
            return new Result(true,StatusCode.ERROR,"请先获取手机验证码");
        }
        if(!checkcodeRedis.equals(code)){
            return new Result(true,StatusCode.ERROR,"请先获取手机验证码");
        }
        userService.add(user);
        return new Result(true,StatusCode.OK,"注册成功");
    }



	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
        String header = request.getHeader("Authorization");
        System.out.println("user中通过网关获取到头信息 " + header);
        return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
