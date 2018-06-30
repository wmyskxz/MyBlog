package cn.wmyskxz.blog.controller;

import cn.wmyskxz.blog.entity.SysLog;
import cn.wmyskxz.blog.entity.SysView;
import cn.wmyskxz.blog.service.SysService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统Controller
 *
 * @author:wmyskxz
 * @create:2018-06-21-上午 10:23
 */
@RestController
@RequestMapping("/admin")
public class SysController {


    @Autowired
    SysService sysService;

    /**
     * 返回所有的系统日志记录信息
     *
     * @return
     */
    @ApiOperation("返回所有的SysLog信息")
    @GetMapping("/sys/log")
    public List<SysLog> listAllLog() {
        return sysService.listAllLog();
    }

    /**
     * 返回所有的系统浏览记录信息
     *
     * @return
     */
    @ApiOperation("返回所有的SysView信息")
    @GetMapping("/sys/view")
    public List<SysView> listAllView() {
        return sysService.listAllView();
    }

}
