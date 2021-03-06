package org.xhome.xblog;

import org.xhome.common.Base;

/**
 * @project xblog-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 31, 20136:03:42 PM
 * @describe 评论
 */
public class Comment extends Base {

	private static final long serialVersionUID = -8034039899889499530L;

	private Article article; // 所评论文章
	private int type = TYPE_COMMENT; // 0:评论,1:回复,2:引用
	private String userName; // 评论用户的名称
	private String userEmail; // 评论用户的邮箱
	private String userWebsite; // 评论用户的个人主页
	private String content; // 评论内容
	private Comment target; // 回复或引用的评论

	public final static int TYPE_COMMENT = 0; // 普通评论
	public final static int TYPE_REPLY = 1; // 针对评论的回复
	public final static int TYPE_QUOTE = 2; // 引用评论

	public Comment() {
	}

	public Comment(String content, Article article) {
		this(content, article, null);
	}

	public Comment(String content, Article article, Comment target) {
		this.setContent(content);
		this.setArticle(article);
		this.setTarget(target);
		this.setType(target != null ? TYPE_REPLY : TYPE_COMMENT);
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail
	 *            the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the userWebsite
	 */
	public String getUserWebsite() {
		return userWebsite;
	}

	/**
	 * @param userWebsite
	 *            the userWebsite to set
	 */
	public void setUserWebsite(String userWebsite) {
		this.userWebsite = userWebsite;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Comment getTarget() {
		return target;
	}

	public void setTarget(Comment target) {
		this.target = target;
	}

}
