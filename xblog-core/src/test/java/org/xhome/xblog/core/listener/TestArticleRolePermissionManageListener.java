package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.ArticleRolePermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:58:52 PM
 * @describe
 */
public class TestArticleRolePermissionManageListener implements
                ArticleRolePermissionManageListener {

    private Logger logger = LoggerFactory.getLogger(TestArticleRolePermissionManageListener.class);

    @Override
    public boolean beforeArticleRolePermissionManage(User oper, short action,
                    ArticleRolePermission articleRolePermission, Object... args) {
        logger.debug("TEST BEFORE ARTICLE ROLE PERMISSION MANAGE LISTENER {} {} {}",
                        oper.getName(),
                        action,
                        articleRolePermission != null ? articleRolePermission
                                        .getId() : "NULL");
        return true;
    }

    @Override
    public void afterArticleRolePermissionManage(User oper, short action,
                    short result, ArticleRolePermission articleRolePermission,
                    Object... args) {
        logger.debug("TEST AFTER ARTICLE ROLE PERMISSION MANAGE LISTENER {} {} {}",
                        oper.getName(),
                        action,
                        articleRolePermission != null ? articleRolePermission
                                        .getId() : "NULL");
    }

}
