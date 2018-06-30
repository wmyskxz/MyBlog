package cn.wmyskxz.blog.controller;

import cn.wmyskxz.blog.dto.ArticleCommentDto;
import cn.wmyskxz.blog.dto.ArticleDto;
import cn.wmyskxz.blog.dto.ArticleWithPictureDto;
import cn.wmyskxz.blog.entity.*;
import cn.wmyskxz.blog.util.Markdown2HtmlUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 前台控制器
 *
 * @author:wmyskxz
 * @create:2018-06-16-下午 15:35
 */
@RestController
@RequestMapping("/api")
public class ForeController extends BaseController {

    /**
     * 获取所有文章列表
     *
     * @return
     */
    @ApiOperation("获取所有文章")
    @GetMapping("article/list")
    public List<ArticleWithPictureDto> listAllArticleInfo() {
        return articleService.listAll();
    }

    /**
     * 获取某一个分类下的所有文章
     *
     * @param id
     * @return
     */
    @ApiOperation("获取某一个分类下的所有文章")
    @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "Long")
    @GetMapping("article/list/sort/{id}")
    public List<ArticleWithPictureDto> listArticleInfo(@PathVariable Long id) {
        return articleService.listByCategoryId(id);
    }

    /**
     * 获取最新的文章
     *
     * @return
     */
    @ApiOperation("获取最新的几篇文章")
    @GetMapping("article/list/lastest")
    public List<ArticleWithPictureDto> listLastestArticle() {
        return articleService.listLastest();
    }

    /**
     * 通过文章的ID获取对应的文章信息
     *
     * @param id
     * @return 自己封装好的文章信息类
     */
    @ApiOperation("通过文章ID获取文章信息")
    @GetMapping("article/{id}")
    public ArticleDto getArticleById(@PathVariable Long id) {
        ArticleDto articleDto = articleService.getOneById(id);
        articleDto.setContent(Markdown2HtmlUtil.markdown2html(articleDto.getContent()));
        return articleDto;
    }

//    /**
//     * 通过文章id获取一篇文章
//     * 说明：ArticleInfo中封装了picture/content/category信息
//     *
//     * @param id
//     * @return
//     */
//    @ApiOperation("通过文章id获取文章内容")
//    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long")
//    @GetMapping("article/{id}")
//    public ArticleInfo getArticleContent(@PathVariable Long id) {
//        return articleService.getOneById(id);
//    }

//    /**
//     * 通过文章ID获取它对应的题图信息
//     *
//     * @param id
//     * @return
//     */
//    @ApiOperation("获取文章题图信息")
//    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long")
//    @GetMapping("article/picture/{id}")
//    public ArticlePicture getArticlePicture(@PathVariable Long id) {
//        return null;
//    }

    /**
     * 获取所有分类信息
     *
     * @return
     */
    @ApiOperation("获取所有分类信息")
    @GetMapping("category/list")
    public List<CategoryInfo> listAllCategoryInfo() {
        return categoryService.listAllCategory();
    }

    /**
     * 获取所有的留言信息
     *
     * @return
     */
    @ApiOperation("获取所有的留言信息")
    @GetMapping("comment/list")
    public List<Comment> listAllComment() {
        return commentService.listAllComment();
    }

    /**
     * 通过文章ID获取某一篇文章的评论信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取某一篇文章的评论信息")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long")
    @GetMapping("comment/article/{id}")
    public List<ArticleCommentDto> listMessageByArticleId(@PathVariable Long id) {
        return commentService.listAllArticleCommentById(id);
    }

    /**
     * 给某一篇文章增加一条评论信息
     *
     * @return
     */
    @ApiOperation("给文章中增加一条评论信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "content", value = "评论信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "Email地址，用于回复", required = false, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "用户自定义的名称", required = true, dataType = "String")
    })
    @PostMapping("comment/article/{id}")
    public String addArticleComment(@PathVariable Long id, @RequestBody ArticleCommentDto articleCommentDto, HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        articleCommentDto.setIp(ip);
        articleCommentDto.setArticleId(id);
        commentService.addArticleComment(articleCommentDto);

        return null;
    }

    /**
     * 增加一条留言
     *
     * @return
     */
    @ApiOperation("增加一条留言")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "评论信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "Email地址，用于回复", required = false, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "用户自定义的名称", required = true, dataType = "String")
    })
    @PostMapping("comment")
    public String addMessage(@RequestBody Comment comment, HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        comment.setIp(ip);
        commentService.addComment(comment);

        return null;
    }
}
