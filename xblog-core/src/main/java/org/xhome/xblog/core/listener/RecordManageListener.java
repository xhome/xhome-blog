package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Record;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:39:07 PM
 * @describe 
 */
public interface RecordManageListener {
	
	/**
	 * 用户访问记录管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param record 待管理的用户访问记录信息
	 * @param args 除record之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeRecordManage(User oper, short action, Record record, Object ...args);
	
	/**
	 * 用户访问记录管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param record 待管理的用户访问记录信息
	 * @param args 除record之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterRecordManage(User oper, short action, short result, Record record, Object ...args);

}
