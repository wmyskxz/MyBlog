package cn.wmyskxz.blog.service.impl;

import cn.wmyskxz.blog.dao.*;
import cn.wmyskxz.blog.dto.ArticleDto;
import cn.wmyskxz.blog.dto.ArticleWithPictureDto;
import cn.wmyskxz.blog.entity.*;
import cn.wmyskxz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 文章Service实现类
 * 说明：ArticleInfo里面封装了picture/content/category等信息
 *
 * @author:wmyskxz
 * @create:2018-06-19-上午 9:29
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleInfoMapper articleInfoMapper;
	@Autowired
	ArticlePictureMapper articlePictureMapper;
	@Autowired
	ArticleCategoryMapper articleCategoryMapper;
	@Autowired
	ArticleContentMapper articleContentMapper;
	@Autowired
	CategoryInfoMapper categoryInfoMapper;

	private static byte MAX_LASTEST_ARTICLE_COUNT = 5;

	/**
	 * 增加一篇文章信息
	 * 说明：需要对应的写入tbl_article_picture/tbl_article_content/tbl_article_category表
	 * 注意：使用的是Article封装类
	 *
	 * @param articleDto 文章封装类
	 */
	@Override
	public void addArticle(ArticleDto articleDto) {
		// 增加文章信息表 - title/summary
		ArticleInfo articleInfo = new ArticleInfo();
		articleInfo.setTitle(articleDto.getTitle());
		articleInfo.setSummary(articleDto.getSummary());
		articleInfoMapper.insertSelective(articleInfo);
		// 获取刚才插入文章信息的ID
		Long articleId = getArticleLastestId();
		// 增加文章题图信息 - pictureUrl/articleId
		ArticlePicture articlePicture = new ArticlePicture();
		articlePicture.setPictureUrl(articleDto.getPictureUrl());
		articlePicture.setArticleId(articleId);
		articlePictureMapper.insertSelective(articlePicture);
		// 增加文章内容信息表 - content/articleId
		ArticleContent articleContent = new ArticleContent();
		articleContent.setContent(articleDto.getContent());
		articleContent.setArticleId(articleId);
		articleContentMapper.insertSelective(articleContent);
		// 增加文章分类信息表 - articleId/categoryId
		ArticleCategory articleCategory = new ArticleCategory();
		articleCategory.setArticleId(articleId);
		articleCategory.setCategoryId(articleDto.getCategoryId());
		articleCategoryMapper.insertSelective(articleCategory);
		// 对应文章的数量要加1
		CategoryInfo categoryInfo = categoryInfoMapper.selectByPrimaryKey(articleCategory.getCategoryId());
		categoryInfo.setNumber((byte) (categoryInfo.getNumber() + 1));
		categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
	}

	/**
	 * 删除一篇文章
	 * 说明：需要对应删除tbl_article_picture/tbl_article_content/tbl_article_category表中的内容
	 *
	 * @param id
	 */
	@Override
	public void deleteArticleById(Long id) {
		// 获取对应的文章信息
		ArticleDto articleDto = getOneById(id);
		// 删除文章信息中的数据
		articleInfoMapper.deleteByPrimaryKey(articleDto.getId());
		// 删除文章题图信息数据
		articlePictureMapper.deleteByPrimaryKey(articleDto.getArticlePictureId());
		// 删除文章内容信息表
		articleContentMapper.deleteByPrimaryKey(articleDto.getArticleContentId());
		// 删除文章分类信息表
		articleCategoryMapper.deleteByPrimaryKey(articleDto.getArticleCategoryId());
	}

	/**
	 * 更改文章的分类信息
	 *
	 * @param articleId
	 * @param categoryId
	 */
	@Override
	public void updateArticleCategory(Long articleId, Long categoryId) {
		ArticleCategoryExample example = new ArticleCategoryExample();
		example.or().andArticleIdEqualTo(articleId);
		ArticleCategory articleCategory = articleCategoryMapper.selectByExample(example).get(0);
		// 对应改变分类下的文章数目
		CategoryInfo categoryInfo = categoryInfoMapper.selectByPrimaryKey(articleCategory.getCategoryId());
		categoryInfo.setNumber((byte) (categoryInfo.getNumber() - 1));
		categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
		categoryInfo = categoryInfoMapper.selectByPrimaryKey(categoryId);
		categoryInfo.setNumber((byte) (categoryInfo.getNumber() + 1));
		categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
		// 更新tbl_article_category表字段
		articleCategory.setCategoryId(categoryId);
		articleCategoryMapper.updateByPrimaryKeySelective(articleCategory);
	}

	/**
	 * 更新文章信息
	 * 说明：需要对应更改tbl_article_picture/tbl_article_content/tbl_article_category表中的内容
	 * 注意：我们使用的是封装好的Article文章信息类
	 *
	 * @param articleDto 自己封装的Article信息类
	 */
	@Override
	public void updateArticle(ArticleDto articleDto) {
		// 更新文章信息中的数据
		ArticleInfo articleInfo = new ArticleInfo();
		articleInfo.setId(articleDto.getId());
		articleInfo.setTitle(articleDto.getTitle());
		articleInfo.setSummary(articleDto.getSummary());
		articleInfo.setIsTop(articleDto.getTop());
		articleInfo.setTraffic(articleDto.getTraffic());
		articleInfoMapper.updateByPrimaryKeySelective(articleInfo);
		// 更新文章题图信息数据
		ArticlePictureExample pictureExample = new ArticlePictureExample();
		pictureExample.or().andArticleIdEqualTo(articleDto.getId());
		ArticlePicture articlePicture = articlePictureMapper.selectByExample(pictureExample).get(0);
//        articlePicture.setId(articleDto.getArticlePictureId());
		articlePicture.setPictureUrl(articleDto.getPictureUrl());
		articlePictureMapper.updateByPrimaryKeySelective(articlePicture);
		// 更新文章内容信息数据
		ArticleContentExample contentExample = new ArticleContentExample();
		contentExample.or().andArticleIdEqualTo(articleDto.getId());
		ArticleContent articleContent = articleContentMapper.selectByExample(contentExample).get(0);
//		articleContent.setArticleId(articleDto.getId());
//		articleContent.setId(articleDto.getArticleContentId());
		articleContent.setContent(articleDto.getContent());
		articleContentMapper.updateByPrimaryKeySelective(articleContent);
		// 更新文章分类信息表
		ArticleCategoryExample categoryExample = new ArticleCategoryExample();
		categoryExample.or().andArticleIdEqualTo(articleDto.getId());
		ArticleCategory articleCategory = articleCategoryMapper.selectByExample(categoryExample).get(0);
//        articleCategory.setId(articleDto.getArticleCategoryId());
		articleCategory.setCategoryId(articleDto.getCategoryId());
		articleCategoryMapper.updateByPrimaryKeySelective(articleCategory);
	}

	/**
	 * 获取一篇文章内容
	 * 说明：需要对应从tbl_article_picture/tbl_article_content/tbl_article_category表中获取内容
	 * 并封装好
	 *
	 * @param id 文章ID
	 * @return 填充好数据的ArticleInfo
	 */
	@Override
	public ArticleDto getOneById(Long id) {
		ArticleDto articleDto = new ArticleDto();
		// 填充文章基础信息
		ArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(id);
		articleDto.setId(articleInfo.getId());
		articleDto.setTitle(articleInfo.getTitle());
		articleDto.setSummary(articleInfo.getSummary());
		articleDto.setTop(articleInfo.getIsTop());
		articleDto.setCreateBy(articleInfo.getCreateBy());
		// 文章访问量要加1
		articleInfo.setTraffic(articleInfo.getTraffic() + 1);
		articleDto.setTraffic(articleInfo.getTraffic() + 1);
		articleInfoMapper.updateByPrimaryKey(articleInfo);
		// 填充文章内容信息
		ArticleContentExample example = new ArticleContentExample();
		example.or().andArticleIdEqualTo(id);
		ArticleContent articleContent = articleContentMapper.selectByExample(example).get(0);
		articleDto.setContent(articleContent.getContent());
		articleDto.setArticleContentId(articleContent.getId());
		// 填充文章题图信息
		ArticlePictureExample example1 = new ArticlePictureExample();
		example1.or().andArticleIdEqualTo(id);
		ArticlePicture articlePicture = articlePictureMapper.selectByExample(example1).get(0);
		articleDto.setPictureUrl(articlePicture.getPictureUrl());
		articleDto.setArticlePictureId(articlePicture.getId());
		// 填充文章分类信息
		ArticleCategoryExample example2 = new ArticleCategoryExample();
		example2.or().andArticleIdEqualTo(id);
		ArticleCategory articleCategory = articleCategoryMapper.selectByExample(example2).get(0);
		articleDto.setArticleCategoryId(articleCategory.getId());
		// 填充文章分类基础信息
		CategoryInfoExample example3 = new CategoryInfoExample();
		example3.or().andIdEqualTo(articleCategory.getCategoryId());
		CategoryInfo categoryInfo = categoryInfoMapper.selectByExample(example3).get(0);
		articleDto.setCategoryId(categoryInfo.getId());
		articleDto.setCategoryName(categoryInfo.getName());
		articleDto.setCategoryNumber(categoryInfo.getNumber());
		return articleDto;
	}

	/**
	 * 获取所有的文章内容
	 *
	 * @return 封装好的Article集合
	 */
	@Override
	public List<ArticleWithPictureDto> listAll() {
		// 1.先获取所有的数据
		List<ArticleWithPictureDto> articles = listAllArticleWithPicture();
		// 2.然后再对集合进行重排，置顶的文章在前
		LinkedList<ArticleWithPictureDto> list = new LinkedList<>();
		for (int i = 0; i < articles.size(); i++) {
			if (true == articles.get(i).getTop()) {
				list.addFirst(articles.get(i));
			} else {
				list.addLast(articles.get(i));
			}
		}
		articles = new ArrayList<>(list);

		return articles;
	}

	/**
	 * 通过分类id返回该分类下的所有文章
	 *
	 * @param id 分类ID
	 * @return 对应分类ID下的所有文章(带题图)
	 */
	@Override
	public List<ArticleWithPictureDto> listByCategoryId(Long id) {
		ArticleCategoryExample example = new ArticleCategoryExample();
		example.or().andCategoryIdEqualTo(id);
		List<ArticleCategory> articleCategories = articleCategoryMapper.selectByExample(example);
		List<ArticleWithPictureDto> articles = new ArrayList<>();
		for (int i = 0; i < articleCategories.size(); i++) {
			Long articleId = articleCategories.get(i).getArticleId();
			ArticleWithPictureDto articleWithPictureDto = new ArticleWithPictureDto();
			// 填充文章基础信息
			ArticleInfoExample example1 = new ArticleInfoExample();
			example1.or().andIdEqualTo(articleId);
			ArticleInfo articleInfo = articleInfoMapper.selectByExample(example1).get(0);
			articleWithPictureDto.setId(articleInfo.getId());
			articleWithPictureDto.setTitle(articleInfo.getTitle());
			articleWithPictureDto.setSummary(articleInfo.getSummary());
			articleWithPictureDto.setTop(articleInfo.getIsTop());
			articleWithPictureDto.setTraffic(articleInfo.getTraffic());
			// 填充文章图片信息
			ArticlePictureExample example2 = new ArticlePictureExample();
			example2.or().andArticleIdEqualTo(articleInfo.getId());
			ArticlePicture articlePicture = articlePictureMapper.selectByExample(example2).get(0);
			articleWithPictureDto.setArticlePictureId(articlePicture.getId());
			articleWithPictureDto.setPictureUrl(articlePicture.getPictureUrl());
			articles.add(articleWithPictureDto);
		}


		// 对集合进行重排，置顶的文章在前
		LinkedList<ArticleWithPictureDto> list = new LinkedList<>();
		for (int i = 0; i < articles.size(); i++) {
			if (true == articles.get(i).getTop()) {
				list.add(articles.get(i));
			} else {
				list.addLast(articles.get(i));
			}
		}
		articles = new ArrayList<>(list);

		return articles;
	}

	/**
	 * 获取最新的文章信息，默认为5篇
	 * 说明：listAll默认已经按照id排序了，所以我们只需要返回前五篇就可以了
	 * 注意：需要判断当前的文章是否大于5篇
	 *
	 * @return 返回五篇最新的文章数据
	 */
	@Override
	public List<ArticleWithPictureDto> listLastest() {
		// 1.先获取所有的数据
		List<ArticleWithPictureDto> articles = listAllArticleWithPicture();
		// 2.判断是否满足5个的条件
		if (articles.size() >= MAX_LASTEST_ARTICLE_COUNT) {
			// 3.大于5个则返回前五个数据
			List<ArticleWithPictureDto> tempArticles = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				tempArticles.add(articles.get(i));
			}
			return tempArticles;
		}
		// 4.不大于五个则直接返回
		return articles;
	}

//    /**
//     * 为ArticleInfo填充对应的content/picture/category信息
//     * 说明：每一个ArticleInfo必有对应的这些信息，这是在增加时限制的
//     *
//     * @param articleInfo
//     */
//    private void fill(ArticleInfo articleInfo) {
//        Long articleId = articleInfo.getId();
//        // 填充picture信息
//        ArticlePictureExample example = new ArticlePictureExample();
//        example.or().andArticleIdEqualTo(articleId);
//        List<ArticlePicture> articlePictures = articlePictureMapper.selectByExample(example);
//        articleInfo.setArticlePicture(articlePictures.get(0));
//        // 填充content信息
//        ArticleContentExample example1 = new ArticleContentExample();
//        example1.or().andArticleIdEqualTo(articleId);
//        List<ArticleContent> articleContents = articleContentMapper.selectByExample(example1);
//        articleInfo.setArticleContent(articleContents.get(0));
//        // 填充category信息
//        ArticleCategoryExample example2 = new ArticleCategoryExample();
//        example2.or().andArticleIdEqualTo(articleId);
//        List<ArticleCategory> articleCategories = articleCategoryMapper.selectByExample(example2);
//        articleInfo.setArticleCategory(articleCategories.get(0));
//    }

	/**
	 * 返回最新插入一条数据的ID
	 *
	 * @return
	 */
	private Long getArticleLastestId() {
		ArticleInfoExample example = new ArticleInfoExample();
		example.setOrderByClause("id desc");
		return articleInfoMapper.selectByExample(example).get(0).getId();
	}

	/**
	 * 通过文章ID获取对应的文章题图信息
	 *
	 * @param id 文章ID
	 * @return 文章ID对应的文章题图信息
	 */
	@Override
	public ArticlePicture getPictureByArticleId(Long id) {
		ArticlePictureExample example = new ArticlePictureExample();
		example.or().andArticleIdEqualTo(id);
		return articlePictureMapper.selectByExample(example).get(0);
	}

	/**
	 * 获取所有的文章信息（带题图）
	 *
	 * @return
	 */
	private List<ArticleWithPictureDto> listAllArticleWithPicture() {
		ArticleInfoExample example = new ArticleInfoExample();
		example.setOrderByClause("id desc");
		// 无添加查询即返回所有
		List<ArticleInfo> articleInfos = articleInfoMapper.selectByExample(example);
		List<ArticleWithPictureDto> articles = new ArrayList<>();
		for (ArticleInfo articleInfo : articleInfos) {
			ArticleWithPictureDto articleWithPictureDto = new ArticleWithPictureDto();
			// 填充文章基础信息
			articleWithPictureDto.setId(articleInfo.getId());
			articleWithPictureDto.setTitle(articleInfo.getTitle());
			articleWithPictureDto.setSummary(articleInfo.getSummary());
			articleWithPictureDto.setTop(articleInfo.getIsTop());
			articleWithPictureDto.setTraffic(articleInfo.getTraffic());
			// 填充文章题图信息
			ArticlePictureExample example1 = new ArticlePictureExample();
			example1.or().andArticleIdEqualTo(articleInfo.getId());
			ArticlePicture articlePicture = articlePictureMapper.selectByExample(example1).get(0);
			articleWithPictureDto.setArticlePictureId(articlePicture.getId());
			articleWithPictureDto.setPictureUrl(articlePicture.getPictureUrl());
			articles.add(articleWithPictureDto);
		}
		return articles;
	}

}
