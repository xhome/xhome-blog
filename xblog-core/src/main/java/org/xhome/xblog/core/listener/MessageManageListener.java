package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Message;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:05 PM
 * @description 留言管理监听器接口
 */
public interface MessageManageListener {

    /**
     * 留言管理前监听器接口
     * 
     * @param oper
     *            执行该操作的用户
     * @param action
     *            操作类型
     * @param message
     *            待管理的留言信息
     * @param args
     *            除message之外的方法调用参数
     * @return 是否允许执行该操作：true/false（允许/不允许）
     */
    public boolean beforeMessageManage(User oper, short action,
                    Message message, Object... args);

    /**
     * 留言管理后监听器接口
     * 
     * @param oper
     *            执行该操作的用户
     * @param action
     *            操作类型
     * @param result
     *            操作结果
     * @param message
     *            待管理的留言信息
     * @param args
     *            除message之外的方法调用参数
     * @return 是否允许执行该操作：true/false（允许/不允许）
     */
    public void afterMessageManage(User oper, short action, short result,
                    Message message, Object... args);

}
