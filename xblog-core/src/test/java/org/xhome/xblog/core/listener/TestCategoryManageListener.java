package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.Category;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:58:52 PM
 * @describe
 */
public class TestCategoryManageListener implements CategoryManageListener {

    private Logger logger = LoggerFactory.getLogger(TestCategoryManageListener.class);

    @Override
    public boolean beforeCategoryManage(User oper, short action,
                    Category category, Object... args) {
        logger.debug("TEST BEFORE CATEGORY MANAGE LISTENER {} {} {}",
                        oper.getName(), action,
                        category != null ? category.getName() : "NULL");
        return true;
    }

    @Override
    public void afterCategoryManage(User oper, short action, short result,
                    Category category, Object... args) {
        logger.debug("TEST AFTER CATEGORY MANAGE LISTENER {} {} {}",
                        oper.getName(), action,
                        category != null ? category.getName() : "NULL");
    }

}
