package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.CategoryRolePermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:58:52 PM
 * @describe
 */
public class TestCategoryRolePermissionManageListener implements
                CategoryRolePermissionManageListener {

    private Logger logger = LoggerFactory.getLogger(TestCategoryRolePermissionManageListener.class);

    @Override
    public boolean beforeCategoryRolePermissionManage(User oper, short action,
                    CategoryRolePermission categoryRolePermission,
                    Object... args) {
        logger.debug("TEST BEFORE CATEGORY ROLE PERMISSION MANAGE LISTENER {} {} {}",
                        oper.getName(),
                        action,
                        categoryRolePermission != null ? categoryRolePermission
                                        .getId() : "NULL");
        return true;
    }

    @Override
    public void afterCategoryRolePermissionManage(User oper, short action,
                    short result,
                    CategoryRolePermission categoryRolePermission,
                    Object... args) {
        logger.debug("TEST AFTER CATEGORY ROLE PERMISSION MANAGE LISTENER {} {} {}",
                        oper.getName(),
                        action,
                        categoryRolePermission != null ? categoryRolePermission
                                        .getId() : "NULL");
    }

}
