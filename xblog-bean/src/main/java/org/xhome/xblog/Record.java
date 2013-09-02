package org.xhome.xblog;

import org.xhome.common.Base;
import org.xhome.xauth.User;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20137:13:10 PM
 * @describe 文章访问记录
 */
public class Record extends Base {

	private static final long serialVersionUID = 4289536483960003628L;
	
	private Article article; // 访问文章
	private User user; // 访问用户
	private String address; // 访问地址
	private short agent; // 0:Other,1:Chrome, 2:Firefox, 3:IE, 4:Android
	private String  number;  // 设备编号
	
	public Record() {}
	
	public Record (Article article, User user, String address, short agent, String number) {
		this.setArticle(article);
		this.setUser(user);
		this.setAddress(address);
		this.setAgent(agent);
		this.setNumber(number);
	}
	
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public short getAgent() {
		return agent;
	}
	public void setAgent(short agent) {
		this.agent = agent;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

}
