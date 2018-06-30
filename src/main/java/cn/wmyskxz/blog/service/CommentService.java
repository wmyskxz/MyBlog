package cn.wmyskxz.blog.service;

import cn.wmyskxz.blog.dto.ArticleCategoryDto;
import cn.wmyskxz.blog.dto.ArticleCommentDto;
import cn.wmyskxz.blog.entity.ArticleComment;
import cn.wmyskxz.blog.entity.Comment;

import java.util.List;

/**
 * 留言的Service
 */
public interface CommentService {
    void addComment(Comment comment);

    void addArticleComment(ArticleCommentDto articleCommentDto);

    void deleteCommentById(Long id);

    void deleteArticleCommentById(Long id);

    List<Comment> listAllComment();

    List<ArticleCommentDto> listAllArticleCommentById(Long id);
}
