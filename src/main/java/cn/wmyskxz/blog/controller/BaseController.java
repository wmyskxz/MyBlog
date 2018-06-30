package cn.wmyskxz.blog.controller;

import cn.wmyskxz.blog.service.ArticleService;
import cn.wmyskxz.blog.service.CategoryService;
import cn.wmyskxz.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础控制器，包含了Controller层中共有的一些Service
 *
 * @author:wmyskxz
 * @create:2018-06-19-上午 11:25
 */
public class BaseController {

    @Autowired
    ArticleService articleService;
    @Autowired
    CommentService commentService;
    @Autowired
    CategoryService categoryService;

}
