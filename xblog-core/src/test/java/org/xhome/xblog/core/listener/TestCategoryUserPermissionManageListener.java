package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.CategoryUserPermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:58:52 PM
 * @describe
 */
public class TestCategoryUserPermissionManageListener implements
                CategoryUserPermissionManageListener {

    private Logger logger = LoggerFactory.getLogger(TestCategoryUserPermissionManageListener.class);

    @Override
    public boolean beforeCategoryUserPermissionManage(User oper, short action,
                    CategoryUserPermission categoryUserPermission,
                    Object... args) {
        logger.debug("TEST BEFORE CATEGORY USER PERMISSION MANAGE LISTENER {} {} {}",
                        oper.getName(),
                        action,
                        categoryUserPermission != null ? categoryUserPermission
                                        .getId() : "NULL");
        return true;
    }

    @Override
    public void afterCategoryUserPermissionManage(User oper, short action,
                    short result,
                    CategoryUserPermission categoryUserPermission,
                    Object... args) {
        logger.debug("TEST AFTER CATEGORY USER PERMISSION MANAGE LISTENER {} {} {}",
                        oper.getName(),
                        action,
                        categoryUserPermission != null ? categoryUserPermission
                                        .getId() : "NULL");
    }

}
