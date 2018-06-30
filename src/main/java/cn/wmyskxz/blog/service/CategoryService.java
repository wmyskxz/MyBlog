package cn.wmyskxz.blog.service;

import cn.wmyskxz.blog.dto.ArticleCategoryDto;
import cn.wmyskxz.blog.entity.ArticleCategory;
import cn.wmyskxz.blog.entity.CategoryInfo;

import java.util.List;

/**
 * 分类Service
 */
public interface CategoryService {
    void addCategory(CategoryInfo categoryInfo);

    void deleteCategoryById(Long id);

    void updateCategory(CategoryInfo categoryInfo);

    void updateArticleCategory(ArticleCategory articleCategory);

    CategoryInfo getOneById(Long id);

    List<CategoryInfo> listAllCategory();

    ArticleCategoryDto getCategoryByArticleId(Long id);
}
