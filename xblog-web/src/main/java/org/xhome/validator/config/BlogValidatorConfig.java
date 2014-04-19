package org.xhome.validator.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @project xblog-web
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 28, 20139:17:04 PM
 * @describe
 */
public class BlogValidatorConfig implements Config {

    public final static String TAG_NAME_EMPTY_MESSAGE                     = "xblog_tag_name_empty_message";
    public final static String TAG_NAME_SIZE_MIN                          = "xblog_tag_name_size_min";
    public final static String TAG_NAME_SIZE_MAX                          = "xblog_tag_name_size_max";
    public final static String TAG_NAME_SIZE_MESSAGE                      = "xblog_tag_name_size_message";

    public final static String CATEGORY_NAME_EMPTY_MESSAGE                = "xblog_category_name_empty_message";
    public final static String CATEGORY_NAME_SIZE_MIN                     = "xblog_category_name_size_min";
    public final static String CATEGORY_NAME_SIZE_MAX                     = "xblog_category_name_size_max";
    public final static String CATEGORY_NAME_SIZE_MESSAGE                 = "xblog_category_name_size_message";

    public final static String ARTICLE_TITLE_EMPTY_MESSAGE                = "xblog_article_title_empty_message";
    public final static String ARTICLE_TITLE_SIZE_MIN                     = "xblog_article_title_size_min";
    public final static String ARTICLE_TITLE_SIZE_MAX                     = "xblog_article_title_size_max";
    public final static String ARTICLE_TITLE_SIZE_MESSAGE                 = "xblog_article_title_size_message";
    public final static String ARTICLE_DETAIL_EMPTY_MESSAGE               = "xblog_article_detail_empty_message";
    public final static String ARTICLE_DETAIL_SIZE_MIN                    = "xblog_article_detail_size_min";
    public final static String ARTICLE_DETAIL_SIZE_MAX                    = "xblog_article_detail_size_max";
    public final static String ARTICLE_DETAIL_SIZE_MESSAGE                = "xblog_article_detail_size_message";
    public final static String ARTICLE_CATEGORY_EMPTY_MESSAGE             = "xblog_article_category_empty_message";

    public final static String COMMENT_USER_NAME_EMPTY_MESSAGE            = "xblog_comment_user_name_empty_message";
    public final static String COMMENT_USER_NAME_SIZE_MIN                 = "xblog_comment_user_name_size_min";
    public final static String COMMENT_USER_NAME_SIZE_MAX                 = "xblog_comment_user_name_size_max";
    public final static String COMMENT_USER_NAME_SIZE_MESSAGE             = "xblog_comment_user_name_size_message";

    public final static String COMMENT_USER_EMAIL_EMPTY_MESSAGE           = "xblog_comment_user_email_empty_message";
    public final static String COMMENT_USER_EMAIL_SIZE_MIN                = "xblog_comment_user_email_size_min";
    public final static String COMMENT_USER_EMAIL_SIZE_MAX                = "xblog_comment_user_email_size_max";
    public final static String COMMENT_USER_EMAIL_SIZE_MESSAGE            = "xblog_comment_user_email_size_message";
    public final static String COMMENT_USER_EMAIL_PATTERN_REGEXP          = "xblog_comment_user_email_pattern_regexp";
    public final static String COMMENT_USER_EMAIL_PATTERN_MESSAGE         = "xblog_comment_user_email_pattern_message";

    public final static String COMMENT_USER_WEBSITE_EMPTY_MESSAGE         = "xblog_comment_user_website_empty_message";
    public final static String COMMENT_USER_WEBSITE_SIZE_MIN              = "xblog_comment_user_website_size_min";
    public final static String COMMENT_USER_WEBSITE_SIZE_MAX              = "xblog_comment_user_website_size_max";
    public final static String COMMENT_USER_WEBSITE_SIZE_MESSAGE          = "xblog_comment_user_website_size_message";
    public final static String COMMENT_USER_WEBSITE_PATTERN_REGEXP        = "xblog_comment_user_website_pattern_regexp";
    public final static String COMMENT_USER_WEBSITE_PATTERN_MESSAGE       = "xblog_comment_user_website_pattern_message";

    public final static String COMMENT_CONTENT_EMPTY_MESSAGE              = "xblog_comment_content_empty_message";
    public final static String COMMENT_CONTENT_SIZE_MIN                   = "xblog_comment_content_size_min";
    public final static String COMMENT_CONTENT_SIZE_MAX                   = "xblog_comment_content_size_max";
    public final static String COMMENT_CONTENT_SIZE_MESSAGE               = "xblog_comment_content_size_message";
    public final static String COMMENT_ARTICLE_EMPTY_MESSAGE              = "xblog_comment_article_empty_message";
    public final static String COMMENT_TARGET_EMPTY_MESSAGE               = "xblog_comment_target_empty_message";

    public final static String TAG_PERMISSION_TAG_EMPTY_MESSAGE           = "xblog_tag_permission_tag_empty_message";
    public final static String TAG_PERMISSION_ROLE_EMPTY_MESSAGE          = "xblog_tag_permission_role_empty_message";
    public final static String TAG_PERMISSION_USER_EMPTY_MESSAGE          = "xblog_tag_permission_user_empty_message";

    public final static String CATEGORY_PERMISSION_CATEGORY_EMPTY_MESSAGE = "xblog_category_permission_category_empty_message";
    public final static String CATEGORY_PERMISSION_ROLE_EMPTY_MESSAGE     = "xblog_category_permission_role_empty_message";
    public final static String CATEGORY_PERMISSION_USER_EMPTY_MESSAGE     = "xblog_category_permission_user_empty_message";

    public final static String ARTICLE_PERMISSION_ARTICLE_EMPTY_MESSAGE   = "xblog_article_permission_article_empty_message";
    public final static String ARTICLE_PERMISSION_ROLE_EMPTY_MESSAGE      = "xblog_article_permission_role_empty_message";
    public final static String ARTICLE_PERMISSION_USER_EMPTY_MESSAGE      = "xblog_article_permission_user_empty_message";

    public final static String MESSAGE_USER_NAME_EMPTY_MESSAGE            = "xblog_message_user_name_empty_message";
    public final static String MESSAGE_USER_NAME_SIZE_MIN                 = "xblog_message_user_name_size_min";
    public final static String MESSAGE_USER_NAME_SIZE_MAX                 = "xblog_message_user_name_size_max";
    public final static String MESSAGE_USER_NAME_SIZE_MESSAGE             = "xblog_message_user_name_size_message";

    public final static String MESSAGE_USER_EMAIL_EMPTY_MESSAGE           = "xblog_message_user_email_empty_message";
    public final static String MESSAGE_USER_EMAIL_SIZE_MIN                = "xblog_message_user_email_size_min";
    public final static String MESSAGE_USER_EMAIL_SIZE_MAX                = "xblog_message_user_email_size_max";
    public final static String MESSAGE_USER_EMAIL_SIZE_MESSAGE            = "xblog_message_user_email_size_message";
    public final static String MESSAGE_USER_EMAIL_PATTERN_REGEXP          = "xblog_message_user_email_pattern_regexp";
    public final static String MESSAGE_USER_EMAIL_PATTERN_MESSAGE         = "xblog_message_user_email_pattern_message";

    public final static String MESSAGE_USER_WEBSITE_EMPTY_MESSAGE         = "xblog_message_user_website_empty_message";
    public final static String MESSAGE_USER_WEBSITE_SIZE_MIN              = "xblog_message_user_website_size_min";
    public final static String MESSAGE_USER_WEBSITE_SIZE_MAX              = "xblog_message_user_website_size_max";
    public final static String MESSAGE_USER_WEBSITE_SIZE_MESSAGE          = "xblog_message_user_website_size_message";
    public final static String MESSAGE_USER_WEBSITE_PATTERN_REGEXP        = "xblog_message_user_website_pattern_regexp";
    public final static String MESSAGE_USER_WEBSITE_PATTERN_MESSAGE       = "xblog_message_user_website_pattern_message";

    public final static String MESSAGE_CONTENT_EMPTY_MESSAGE              = "xblog_message_content_empty_message";
    public final static String MESSAGE_CONTENT_SIZE_MIN                   = "xblog_message_content_size_min";
    public final static String MESSAGE_CONTENT_SIZE_MAX                   = "xblog_message_content_size_max";
    public final static String MESSAGE_CONTENT_SIZE_MESSAGE               = "xblog_message_content_size_message";

    public final static String MESSAGE_REPLY_EMPTY_MESSAGE                = "xblog_message_reply_empty_message";
    public final static String MESSAGE_REPLY_SIZE_MIN                     = "xblog_message_reply_size_min";
    public final static String MESSAGE_REPLY_SIZE_MAX                     = "xblog_message_reply_size_max";
    public final static String MESSAGE_REPLY_SIZE_MESSAGE                 = "xblog_message_reply_size_message";

    public Map<String, String> validatorConfigs() {
        Map<String, String> configs = new HashMap<String, String>();

        configs.put(TAG_NAME_EMPTY_MESSAGE, "标签名不能为空");
        configs.put(TAG_NAME_SIZE_MIN, "0");
        configs.put(TAG_NAME_SIZE_MAX, "30");
        configs.put(TAG_NAME_SIZE_MESSAGE, "标签名不能超过30个字符");

        configs.put(CATEGORY_NAME_EMPTY_MESSAGE, "分类名不能为空");
        configs.put(CATEGORY_NAME_SIZE_MIN, "0");
        configs.put(CATEGORY_NAME_SIZE_MAX, "30");
        configs.put(CATEGORY_NAME_SIZE_MESSAGE, "分类名不能超过30个字符");

        configs.put(ARTICLE_TITLE_EMPTY_MESSAGE, "文章标题不能为空");
        configs.put(ARTICLE_TITLE_SIZE_MIN, "0");
        configs.put(ARTICLE_TITLE_SIZE_MAX, "50");
        configs.put(ARTICLE_TITLE_SIZE_MESSAGE, "文章标题不能超过50个字符");

        configs.put(ARTICLE_DETAIL_EMPTY_MESSAGE, "文章内容不能为空");
        configs.put(ARTICLE_DETAIL_SIZE_MIN, "0");
        configs.put(ARTICLE_DETAIL_SIZE_MAX, "60000");
        configs.put(ARTICLE_DETAIL_SIZE_MESSAGE, "文章内容不能超过60000个字符");
        configs.put(ARTICLE_CATEGORY_EMPTY_MESSAGE, "文章分类不能为空");

        configs.put(COMMENT_USER_NAME_EMPTY_MESSAGE, "评论用户名称不能为空");
        configs.put(COMMENT_USER_NAME_SIZE_MIN, "0");
        configs.put(COMMENT_USER_NAME_SIZE_MAX, "20");
        configs.put(COMMENT_USER_NAME_SIZE_MESSAGE, "评论用户名称不能超过20个字符");

        configs.put(COMMENT_USER_EMAIL_EMPTY_MESSAGE, "评论用户邮箱不能为空");
        configs.put(COMMENT_USER_EMAIL_SIZE_MIN, "0");
        configs.put(COMMENT_USER_EMAIL_SIZE_MAX, "50");
        configs.put(COMMENT_USER_EMAIL_SIZE_MESSAGE, "评论用户邮箱不能超过50个字符");
        configs.put(COMMENT_USER_EMAIL_PATTERN_REGEXP,
                        "\\b(^['\\w-_]+(\\.['\\w-_]+)*@([\\w-])+(\\.[\\w-]+)*((\\.[\\w]{2,})|(\\.[\\w]{2,}\\.[\\w]{2,}))$)\\b");
        configs.put(COMMENT_USER_EMAIL_PATTERN_MESSAGE, "评论用户邮箱格式不正确");

        configs.put(COMMENT_USER_WEBSITE_EMPTY_MESSAGE, "评论用户个人主页不能为空");
        configs.put(COMMENT_USER_WEBSITE_SIZE_MIN, "0");
        configs.put(COMMENT_USER_WEBSITE_SIZE_MAX, "100");
        configs.put(COMMENT_USER_WEBSITE_SIZE_MESSAGE, "评论用户个人主页不能超过100个字符");
        configs.put(COMMENT_USER_WEBSITE_PATTERN_REGEXP,
                        "((([A-Za-z]{3,9}:(?://)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:/[\\+~%/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)");
        configs.put(COMMENT_USER_WEBSITE_PATTERN_MESSAGE, "评论用户个人主页格式不正确");

        configs.put(COMMENT_CONTENT_EMPTY_MESSAGE, "评论内容不能为空");
        configs.put(COMMENT_CONTENT_SIZE_MIN, "1");
        configs.put(COMMENT_CONTENT_SIZE_MAX, "60000");
        configs.put(COMMENT_CONTENT_SIZE_MESSAGE, "评论内容必须为1~60000个字符");
        configs.put(COMMENT_ARTICLE_EMPTY_MESSAGE, "评论文章不能为空");
        configs.put(COMMENT_TARGET_EMPTY_MESSAGE, "评论目标不能为空");

        configs.put(TAG_PERMISSION_TAG_EMPTY_MESSAGE, "标签信息不能为空");
        configs.put(TAG_PERMISSION_ROLE_EMPTY_MESSAGE, "角色信息不能为空");
        configs.put(TAG_PERMISSION_USER_EMPTY_MESSAGE, "用户信息不能为空");

        configs.put(CATEGORY_PERMISSION_CATEGORY_EMPTY_MESSAGE, "分类信息不能为空");
        configs.put(CATEGORY_PERMISSION_ROLE_EMPTY_MESSAGE, "角色信息不能为空");
        configs.put(CATEGORY_PERMISSION_USER_EMPTY_MESSAGE, "用户信息不能为空");

        configs.put(ARTICLE_PERMISSION_ARTICLE_EMPTY_MESSAGE, "文章信息不能为空");
        configs.put(ARTICLE_PERMISSION_ROLE_EMPTY_MESSAGE, "角色信息不能为空");
        configs.put(ARTICLE_PERMISSION_USER_EMPTY_MESSAGE, "用户信息不能为空");

        configs.put(MESSAGE_USER_NAME_EMPTY_MESSAGE, "留言用户名称不能为空");
        configs.put(MESSAGE_USER_NAME_SIZE_MIN, "0");
        configs.put(MESSAGE_USER_NAME_SIZE_MAX, "20");
        configs.put(MESSAGE_USER_NAME_SIZE_MESSAGE, "留言用户名称不能超过20个字符");

        configs.put(MESSAGE_USER_EMAIL_EMPTY_MESSAGE, "留言用户邮箱不能为空");
        configs.put(MESSAGE_USER_EMAIL_SIZE_MIN, "0");
        configs.put(MESSAGE_USER_EMAIL_SIZE_MAX, "50");
        configs.put(MESSAGE_USER_EMAIL_SIZE_MESSAGE, "留言用户邮箱不能超过50个字符");
        configs.put(MESSAGE_USER_EMAIL_PATTERN_REGEXP,
                        "\\b(^['\\w-_]+(\\.['\\w-_]+)*@([\\w-])+(\\.[\\w-]+)*((\\.[\\w]{2,})|(\\.[\\w]{2,}\\.[\\w]{2,}))$)\\b");
        configs.put(MESSAGE_USER_EMAIL_PATTERN_MESSAGE, "留言用户邮箱格式不正确");

        configs.put(MESSAGE_USER_WEBSITE_EMPTY_MESSAGE, "留言用户个人主页不能为空");
        configs.put(MESSAGE_USER_WEBSITE_SIZE_MIN, "0");
        configs.put(MESSAGE_USER_WEBSITE_SIZE_MAX, "100");
        configs.put(MESSAGE_USER_WEBSITE_SIZE_MESSAGE, "留言用户个人主页不能超过100个字符");
        configs.put(MESSAGE_USER_WEBSITE_PATTERN_REGEXP,
                        "((([A-Za-z]{3,9}:(?://)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:/[\\+~%/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)");
        configs.put(MESSAGE_USER_WEBSITE_PATTERN_MESSAGE, "留言用户个人主页格式不正确");

        configs.put(MESSAGE_CONTENT_EMPTY_MESSAGE, "留言内容不能为空");
        configs.put(MESSAGE_CONTENT_SIZE_MIN, "1");
        configs.put(MESSAGE_CONTENT_SIZE_MAX, "60000");
        configs.put(MESSAGE_CONTENT_SIZE_MESSAGE, "留言内容必须为1~60000个字符");

        configs.put(MESSAGE_REPLY_EMPTY_MESSAGE, "留言回复不能为空");
        configs.put(MESSAGE_REPLY_SIZE_MIN, "1");
        configs.put(MESSAGE_REPLY_SIZE_MAX, "60000");
        configs.put(MESSAGE_REPLY_SIZE_MESSAGE, "留言回复必须为1~60000个字符");

        return configs;
    }
}
