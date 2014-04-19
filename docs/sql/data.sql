USE xblog;

INSERT INTO xhome_xblog_category VALUES(1, '默认分类', NULL, 1, 1, NOW(), NOW(), 1, 1);
INSERT INTO xhome_xblog_tag VALUES(1, '默认标签', 1, 1, NOW(), NOW(), 1, 1);

/* 博客管理配置项 */
INSERT INTO xhome_xauth_config (category, item, display, value, owner, modifier, created, modified, version, status) VALUES
    /* 1 开启， 0 关闭 */                                                           
    (2, 'xblog_allow_leave_message', '允许留言开关', '1', 1, 1, NOW(), NOW(), 0, 15),
    (2, 'xblog_allow_article_comment', '文章评论开关', '1', 1, 1, NOW(), NOW(), 0, 15),
    (2, 'xblog_article_content_length', '文章概要内容长度', '500', 1, 1, NOW(), NOW(), 0, 15),
    (2, 'xblog_title_index', '博客首页标题', 'XXXX的博客', 1, 1, NOW(), NOW(), 0, 15),
    (2, 'xblog_title_read', '博客阅读页标题', '${article.title} | XXXX的博客', 1, 1, NOW(), NOW(), 0, 15),
    (2, 'xblog_title_new', '博客新建文章页标题', '新建文章 | XXXX的博客', 1, 1, NOW(), NOW(), 0, 15),
    (2, 'xblog_title_edit', '博客修改文章页标题', '编辑文章 | ${article.title} | XXXX的博客', 1, 1, NOW(), NOW(), 0, 15);
