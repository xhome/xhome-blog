package org.xhome.validator.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 28, 20139:17:04 PM
 * @describe 
 */
public class BlogValidatorConfig implements Config {
	
	public final static String	TAG_NAME_EMPTY_MESSAGE			= "xblog_tag_name_empty_message";
	public final static String	TAG_NAME_SIZE_MIN				= "xblog_tag_name_size_min";
	public final static String	TAG_NAME_SIZE_MAX				= "xblog_tag_name_size_max";
	public final static String	TAG_NAME_SIZE_MESSAGE			= "xblog_tag_name_size_message";
	
	public final static String	CATEGORY_NAME_EMPTY_MESSAGE		= "xblog_category_name_empty_message";
	public final static String	CATEGORY_NAME_SIZE_MIN			= "xblog_category_name_size_min";
	public final static String	CATEGORY_NAME_SIZE_MAX			= "xblog_category_name_size_max";
	public final static String	CATEGORY_NAME_SIZE_MESSAGE		= "xblog_category_name_size_message";
	
	public final static String	ARTICLE_TITLE_EMPTY_MESSAGE		= "xblog_article_title_empty_message";
	public final static String	ARTICLE_TITLE_SIZE_MIN			= "xblog_article_title_size_min";
	public final static String	ARTICLE_TITLE_SIZE_MAX			= "xblog_article_title_size_max";
	public final static String	ARTICLE_TITLE_SIZE_MESSAGE		= "xblog_article_title_size_message";
	public final static String	ARTICLE_CONTENT_EMPTY_MESSAGE	= "xblog_article_content_empty_message";
	public final static String	ARTICLE_CONTENT_SIZE_MIN		= "xblog_article_content_size_min";
	public final static String	ARTICLE_CONTENT_SIZE_MAX		= "xblog_article_content_size_max";
	public final static String	ARTICLE_CONTENT_SIZE_MESSAGE	= "xblog_article_content_size_message";
	public final static String	ARTICLE_CATEGORY_EMPTY_MESSAGE	= "xblog_article_category_empty_message";
	
	public final static String	COMMENT_CONTENT_EMPTY_MESSAGE	= "xblog_comment_content_empty_message";
	public final static String	COMMENT_CONTENT_SIZE_MIN		= "xblog_comment_content_size_min";
	public final static String	COMMENT_CONTENT_SIZE_MAX		= "xblog_comment_content_size_max";
	public final static String	COMMENT_CONTENT_SIZE_MESSAGE	= "xblog_comment_content_size_message";
	public final static String	COMMENT_ARTICLE_EMPTY_MESSAGE	= "xblog_comment_article_empty_message";
	public final static String	COMMENT_TARGET_EMPTY_MESSAGE	= "xblog_comment_target_empty_message";
	
	public final static String	TAG_PERMISSION_TAG_EMPTY_MESSAGE	= "xblog_tag_permission_tag_empty_message";
	public final static String	TAG_PERMISSION_ROLE_EMPTY_MESSAGE	= "xblog_tag_permission_role_empty_message";
	public final static String	TAG_PERMISSION_USER_EMPTY_MESSAGE	= "xblog_tag_permission_user_empty_message";

	public final static String	CATEGORY_PERMISSION_CATEGORY_EMPTY_MESSAGE	= "xblog_category_permission_category_empty_message";
	public final static String	CATEGORY_PERMISSION_ROLE_EMPTY_MESSAGE		= "xblog_category_permission_role_empty_message";
	public final static String	CATEGORY_PERMISSION_USER_EMPTY_MESSAGE		= "xblog_category_permission_user_empty_message";

	public final static String	ARTICLE_PERMISSION_ARTICLE_EMPTY_MESSAGE	= "xblog_article_permission_article_empty_message";
	public final static String	ARTICLE_PERMISSION_ROLE_EMPTY_MESSAGE		= "xblog_article_permission_role_empty_message";
	public final static String	ARTICLE_PERMISSION_USER_EMPTY_MESSAGE		= "xblog_article_permission_user_empty_message";
	
	public Map<String, String> validatorConfigs() {
		Map<String, String> configs = new HashMap<String, String>();
		
		configs.put(TAG_NAME_EMPTY_MESSAGE, "标签名不能为空");
		configs.put(TAG_NAME_SIZE_MIN, "0");
		configs.put(TAG_NAME_SIZE_MAX, "30");
		configs.put(TAG_NAME_SIZE_MESSAGE, "标签名不能超过30个字符");
		
		configs.put(CATEGORY_NAME_EMPTY_MESSAGE, "栏目名不能为空");
		configs.put(CATEGORY_NAME_SIZE_MIN, "0");
		configs.put(CATEGORY_NAME_SIZE_MAX, "30");
		configs.put(CATEGORY_NAME_SIZE_MESSAGE, "栏目名不能超过30个字符");
		
		configs.put(ARTICLE_TITLE_EMPTY_MESSAGE, "文章标题不能为空");
		configs.put(ARTICLE_TITLE_SIZE_MIN, "0");
		configs.put(ARTICLE_TITLE_SIZE_MAX, "50");
		configs.put(ARTICLE_TITLE_SIZE_MESSAGE, "文章标题不能超过50个字符");
		
		configs.put(ARTICLE_CONTENT_EMPTY_MESSAGE, "文章内容不能为空");
		configs.put(ARTICLE_CONTENT_SIZE_MIN, "0");
		configs.put(ARTICLE_CONTENT_SIZE_MAX, "60000");
		configs.put(ARTICLE_CONTENT_SIZE_MESSAGE, "文章内容不能超过60000个字符");
		configs.put(ARTICLE_CATEGORY_EMPTY_MESSAGE, "文章分类不能为空");
		
		configs.put(COMMENT_CONTENT_EMPTY_MESSAGE, "评论内容不能为空");
		configs.put(COMMENT_CONTENT_SIZE_MIN, "0");
		configs.put(COMMENT_CONTENT_SIZE_MAX, "60000");
		configs.put(COMMENT_CONTENT_SIZE_MESSAGE, "评论内容不能超过60000个字符");
		configs.put(COMMENT_ARTICLE_EMPTY_MESSAGE, "评论文章不能为空");
		configs.put(COMMENT_TARGET_EMPTY_MESSAGE, "评论目标不能为空");

		configs.put(TAG_PERMISSION_TAG_EMPTY_MESSAGE, "标签信息不能为空");
		configs.put(TAG_PERMISSION_ROLE_EMPTY_MESSAGE, "角色信息不能为空");
		configs.put(TAG_PERMISSION_USER_EMPTY_MESSAGE, "用户信息不能为空");

		configs.put(CATEGORY_PERMISSION_CATEGORY_EMPTY_MESSAGE, "分类信息不能为空");
		configs.put(CATEGORY_PERMISSION_ROLE_EMPTY_MESSAGE, "角色信息不能为空");
		configs.put(CATEGORY_PERMISSION_USER_EMPTY_MESSAGE, "用户信息不能为空");
		
		configs.put(ARTICLE_PERMISSION_ARTICLE_EMPTY_MESSAGE, "文章信息不能为空");
		configs.put(ARTICLE_PERMISSION_ROLE_EMPTY_MESSAGE, "角色信息不能为空");
		configs.put(ARTICLE_PERMISSION_USER_EMPTY_MESSAGE, "用户信息不能为空");
		
		return configs;
	}

}
