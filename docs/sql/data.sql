USE xblog;

INSERT INTO xhome_xblog_tag VALUES(1, 'TestTag', 1, 1, NOW(), NOW(), 1, 1);

INSERT INTO xhome_xblog_category VALUES(1, 'TestCategory', NULL, 1, 1, NOW(), NOW(), 1, 1);

/* 博客管理配置项 */
/* 1 开启， 0 关闭 */                                                           
INSERT INTO xhome_xauth_config VALUES(7, 2, 'xblog_allow_article_comment', '文章评论开关', '1', 1, 1, NOW(), NOW(), 0, 2);
