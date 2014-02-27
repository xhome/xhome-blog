package org.xhome.xblog.core.service;

import org.xhome.xauth.core.service.ConfigService;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Feb 27, 2014
 * @describe 博客管理参数
 */
public interface BlogConfigService extends ConfigService {

	String ITEM_ALLOW_ARTICLE_COMMENT = "xblog_allow_article_comment"; // 评论开关

	/**
	 * 查询是否允许文章评论
	 * 
	 * @return
	 */
	public boolean allowArticleComment();

}
