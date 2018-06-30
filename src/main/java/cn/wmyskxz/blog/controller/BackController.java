package cn.wmyskxz.blog.controller;

import cn.wmyskxz.blog.dto.ArticleDto;
import cn.wmyskxz.blog.entity.*;
import cn.wmyskxz.blog.service.CommentService;
import cn.wmyskxz.blog.service.SysService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 后台控制器
 *
 * @author:wmyskxz
 * @create:2018-06-16-下午 15:08
 */
@RestController
@RequestMapping("/admin")
public class BackController extends BaseController {

    /* 后台登录账号密码 */
    private static String username = "wmyskxz";
    private static String password = "123456";

    /**
     * 后台登录操作
     *
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/login")
    public String login(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 对用户账号进行验证,是否正确
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/admin/index.html");
        } else {
            response.sendRedirect(request.getContextPath() + "/toLogin");
        }
        return null;
    }

    /**
     * 增加一篇文章
     *
     * @return
     */
    @ApiOperation("增加一篇文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "summary", value = "文章简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isTop", value = "文章是否置顶", required = true, dataType = "Boolean"),
            @ApiImplicitParam(name = "categoryId", value = "文章分类对应ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "content", value = "文章md源码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pictureUrl", value = "文章题图url", required = true, dataType = "String")
    })
    @PostMapping("article/")
    public String addArticle(@RequestBody ArticleDto articleDto) {
        articleService.addArticle(articleDto);
        return null;
    }


    /**
     * 删除一篇文章
     *
     * @param id
     * @return
     */
    @ApiOperation("删除一篇文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long")
    @DeleteMapping("article/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteArticleById(id);
        return null;
    }

    /**
     * 编辑/更新一篇文章
     *
     * @return
     */
    @ApiOperation("编辑/更新一篇文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "summary", value = "文章简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isTop", value = "文章是否置顶", required = true, dataType = "Boolean"),
            @ApiImplicitParam(name = "categoryId", value = "文章分类对应ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "content", value = "文章md源码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pictureUrl", value = "文章题图url", required = true, dataType = "String")
    })
    @PutMapping("article/{id}")
    public String updateArticle(@PathVariable Long id, @RequestBody ArticleDto articleDto) {
        articleDto.setId(id);
        articleService.updateArticle(articleDto);
//        System.out.println(articleDto.getTop());
        return null;
    }

    /**
     * 改变某一篇文章的分类
     *
     * @param id
     * @return
     */
    @ApiOperation("改变文章分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "Long"),
    })
    @PutMapping("article/sort/{id}")
    public String changeArticleCategory(@PathVariable Long id, Long categoryId) {
        articleService.updateArticleCategory(id, categoryId);
        return null;
    }

    /**
     * 通过题图的id更改题图信息
     *
     * @param id
     * @return
     */
    @ApiOperation("更改文章题图信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "pictureUrl", value = "题图URL", required = true, dataType = "String")
    })
    @PutMapping("article/picture/{id}")
    public String updateArticlePicture(@PathVariable Long id, String pictureUrl) {
        ArticleDto articleDto = articleService.getOneById(id);
        articleDto.setPictureUrl(pictureUrl);
        articleService.updateArticle(articleDto);
        return null;
    }

//    /**
//     * 插入一条题图信息
//     *
//     * @param id
//     * @return
//     */
//    @ApiOperation("给文章插入一条题图信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long"),
//            @ApiImplicitParam(name = "picture_url", value = "题图url", required = true, dataType = "String"),
//    })
//    @PostMapping("article/picture/{id}")
//    public String addArticlePicture(@PathVariable Long id, @RequestBody String picture_url) {
//        return null;
//    }


    /**
     * 增加一条分类信息数据
     *
     * @return
     */
    @ApiOperation("增加分类信息")
    @ApiImplicitParam(name = "name", value = "分类名称", required = true, dataType = "String")
    @PostMapping("category")
    public String addCategoryInfo(@RequestBody CategoryInfo categoryInfo) {
        categoryService.addCategory(categoryInfo);
        return null;
    }

    /**
     * 更新/编辑一条分类信息
     *
     * @param id
     * @return
     */
    @ApiOperation("更新/编辑分类信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "name", value = "分类名称", required = true, dataType = "String")
    })
    @PutMapping("category/{id}")
    public String updateCategoryInfo(@PathVariable Long id, @RequestBody CategoryInfo categoryInfo) {
        categoryService.updateCategory(categoryInfo);
        return null;
    }

    /**
     * 根据ID删除分类信息
     *
     * @param id
     * @return
     */
    @ApiOperation("删除分类信息")
    @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "Long")
    @DeleteMapping("category/{id}")
    public String deleteCategoryInfo(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return null;
    }

    /**
     * 通过id获取一条分类信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取某一条分类信息")
    @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "Long")
    @GetMapping("category/{id}")
    public CategoryInfo getCategoryInfo(@PathVariable Long id) {
        return categoryService.getOneById(id);
    }

    /**
     * 通过评论ID删除文章评论信息
     *
     * @param id
     * @return
     */
    @ApiOperation("删除文章评论信息")
    @ApiImplicitParam(name = "id", value = "评论ID", required = true, dataType = "Long")
    @DeleteMapping("comment/article/{id}")
    public String deleteArticleComment(@PathVariable Long id) {
        commentService.deleteArticleCommentById(id);
        return null;
    }

    /**
     * 通过id删除某一条留言
     *
     * @param id
     * @return
     */
    @ApiOperation("删除一条留言")
    @ApiImplicitParam(name = "id", value = "评论/留言ID", required = true, dataType = "Long")
    @DeleteMapping("comment/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteCommentById(id);
        return null;
    }

    /**
     * 回复留言/评论，通过id去找到对应的Email
     *
     * @param id
     * @return
     */
    @ApiOperation("回复留言/评论")
    @ApiImplicitParam(name = "id", value = "评论/留言ID", required = true, dataType = "Long")
    @GetMapping("comment/reply/{id}")
    public String replyComment(@PathVariable Long id) {
        return null;
    }

    /**
     * 通过id获取某一条评论/留言
     *
     * @param id
     * @return
     */
    @ApiOperation("获取某一条评论/留言")
    @ApiImplicitParam(name = "id", value = "评论/留言ID", required = true, dataType = "Long")
    @GetMapping("comment/{id}")
    public Comment getCommentById(@PathVariable Long id) {
        return null;
    }


    /**
     * 通过ID获取一篇文章，内容为MD源码格式
     *
     * @param id
     * @return
     */
    @ApiOperation("获取一篇文章，内容为md源码格式")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long")
    @GetMapping("article/{id}")
    public ArticleDto getArticleDtoById(@PathVariable Long id) {
        return articleService.getOneById(id);
    }
}
