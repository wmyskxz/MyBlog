package cn.wmyskxz.blog.service.impl;

import cn.wmyskxz.blog.dao.ArticleCommentMapper;
import cn.wmyskxz.blog.dao.CommentMapper;
import cn.wmyskxz.blog.dto.ArticleCommentDto;
import cn.wmyskxz.blog.entity.ArticleComment;
import cn.wmyskxz.blog.entity.ArticleCommentExample;
import cn.wmyskxz.blog.entity.Comment;
import cn.wmyskxz.blog.entity.CommentExample;
import cn.wmyskxz.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 留言/评论Service实现类
 *
 * @author:wmyskxz
 * @create:2018-06-17-上午 10:54
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ArticleCommentMapper articleCommentMapper;

    /**
     * 增加一条留言信息
     *
     * @param comment
     */
    @Override
    public void addComment(Comment comment) {
        commentMapper.insertSelective(comment);
    }

    /**
     * 增加一条文章评论信息
     * 说明：ArticleCommentDto中封装了tbl_comment和tbl_article_comment中的基础数据
     *
     * @param articleCommentDto
     */
    @Override
    public void addArticleComment(ArticleCommentDto articleCommentDto) {
        // 先增加Comment留言数据
        Comment comment = new Comment();
        comment.setIp(articleCommentDto.getIp());
        comment.setName(articleCommentDto.getName());
        comment.setEmail(articleCommentDto.getEmail());
        comment.setContent(articleCommentDto.getContent());
        addComment(comment);
        // 再更新tbl_article_comment作关联
        CommentExample example = new CommentExample();
        example.setOrderByClause("id desc");
        Long lastestCommentId = commentMapper.selectByExample(example).get(0).getId();
        ArticleComment articleComment = new ArticleComment();
        articleComment.setCommentId(lastestCommentId);
        articleComment.setArticleId(articleCommentDto.getArticleId());
        articleCommentMapper.insertSelective(articleComment);
    }

    /**
     * 通过留言ID删除一条数据
     * 说明：并不是直接删除数据库中的数据而是直接将isEffective字段置为false
     *
     * @param id
     */
    @Override
    public void deleteCommentById(Long id) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        comment.setIsEffective(false);
        commentMapper.updateByPrimaryKeySelective(comment);
    }

    /**
     * 删除文章评论信息
     * 说明：说明：并不是直接删除数据库中的数据而是直接将isEffective字段置为false
     * 注意：这里需要设置两个表的字段
     *
     * @param id tbl_article_comment表主键
     */
    @Override
    public void deleteArticleCommentById(Long id) {
        // 设置ArticleComment表中的字段为false
        ArticleComment articleComment = articleCommentMapper.selectByPrimaryKey(id);
        articleComment.setIsEffective(false);
        articleCommentMapper.updateByPrimaryKeySelective(articleComment);
        // 删除Comment表中对应的数据
        deleteCommentById(articleComment.getCommentId());
    }

    /**
     * 列举返回所有的留言信息
     *
     * @return
     */
    @Override
    public List<Comment> listAllComment() {
        // 无条件查询即返回所有
        CommentExample example = new CommentExample();
        return commentMapper.selectByExample(example);
    }

    /**
     * 获取对应文章下的所有评论信息
     * 说明：需要返回一个自己封装好的用来与前端交互的ArticleCommentDto集合
     *
     * @param id 文章ID
     * @return
     */
    @Override
    public List<ArticleCommentDto> listAllArticleCommentById(Long id) {
        List<ArticleCommentDto> comments = new ArrayList<>();
        ArticleCommentExample example = new ArticleCommentExample();
        example.or().andArticleIdEqualTo(id);
        List<ArticleComment> articleComments = articleCommentMapper.selectByExample(example);
        // 填充对应的评论信息
        for (ArticleComment articleComment : articleComments) {
            if (true == articleComment.getIsEffective()) {
                ArticleCommentDto articleCommentDto = new ArticleCommentDto();
                articleCommentDto.setArticleCommentId(articleComment.getId());
                articleCommentDto.setArticleId(articleComment.getArticleId());
                articleCommentDto.setCreateBy(articleComment.getCreateBy());
                // 查询对应的评论信息
                Comment comment = commentMapper.selectByPrimaryKey(articleComment.getCommentId());
                articleCommentDto.setId(comment.getId());
                articleCommentDto.setContent(comment.getContent());
                articleCommentDto.setEmail(comment.getEmail());
                articleCommentDto.setIp(comment.getIp());
                articleCommentDto.setName(comment.getName());
                comments.add(articleCommentDto);
            }
        }
        return comments;
    }

}
