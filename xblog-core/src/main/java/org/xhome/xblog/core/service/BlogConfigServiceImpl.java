package org.xhome.xblog.core.service;

import org.xhome.xauth.Config;
import org.xhome.xauth.core.service.ConfigServiceImpl;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Feb 27, 2014
 * @describe
 */
public class BlogConfigServiceImpl extends ConfigServiceImpl implements
		BlogConfigService {

	/**
	 * @see org.xhome.xblog.core.service.BlogConfigService#allowArticleComment()
	 */
	@Override
	public boolean allowArticleComment() {
		Config config = configDAO.queryConfigByItem(ITEM_ALLOW_ARTICLE_COMMENT);
		return config != null ? !"0".equals(config.getValue()) : false;
	}

}
