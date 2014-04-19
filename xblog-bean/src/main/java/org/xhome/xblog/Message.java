package org.xhome.xblog;

import org.xhome.common.Base;

/**
 * @project xblog-bean
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Apr 18, 2014
 * @describe 留言
 */
public class Message extends Base {

    private static final long serialVersionUID = 7309110862265020748L;
    private String            userName;                               // 留言用户的名称
    private String            userEmail;                              // 留言用户的邮箱
    private String            userWebsite;                            // 留言用户的个人主页
    private String            content;                                // 留言内容
    private String            reply;                                  // 留言回复

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

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the reply
     */
    public String getReply() {
        return reply;
    }

    /**
     * @param reply
     *            the reply to set
     */
    public void setReply(String reply) {
        this.reply = reply;
    }
}
