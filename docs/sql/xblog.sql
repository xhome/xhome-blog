/**
 *  所有TABLE均包含如下字段(ID)作为主键：
 *  id           INTEGER NOT NULL AUTO_INCREMENT COMMENT 'ID',
 *  owner        BIGINT NOT NULL COMMENT '创建者',
 *  modifier     BIGINT NOT NULL COMMENT '修改者',
 *  created      TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
 *  modified     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
 *  version      TINYINT NOT NULL DEFAULT 0 COMMENT '数据版本',
 *  status       TINYINT NOT NULL DEFAULT 1 COMMENT '状态标记'
 */

/*创建数据库*/
DROP database IF EXISTS xblog;
CREATE database xblog;
USE xblog;

/*创建用户*/
GRANT SELECT,INSERT,UPDATE,DELETE,EXECUTE ON xblog.* TO 'xblog'@'localhost' IDENTIFIED BY 'xblog';

/*==============================================================*/
/* Table: xhome_xblog_category                                  */
/*==============================================================*/
CREATE TABLE xhome_xblog_category
(
   id                   INTEGER NOT NULL AUTO_INCREMENT COMMENT '分类ID',
   name                 VARCHAR(30) NOT NULL COMMENT '分类名称',
   articleCount         BIGINT NOT NULL DEFAULT 0 COMMENT '分类文章总数',
   parent               INTEGER DEFAULT NULL COMMENT 'NULL表示顶级分类',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id),
   CONSTRAINT FK_CATEGORY_PARENT FOREIGN KEY (parent) REFERENCES xhome_xblog_category (id) ON DELETE RESTRICT ON UPDATE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_category COMMENT '分类';

/*==============================================================*/
/* Table: xhome_xblog_article                                   */
/*==============================================================*/
CREATE TABLE xhome_xblog_article
(
   id                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
   title                VARCHAR(50) NOT NULL COMMENT '文章标题',
   attribute            TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '文章属性',
   content              TEXT NOT NULL COMMENT '文章内容',
   category             INTEGER NOT NULL COMMENT '所属分类',
   readCount            BIGINT NOT NULL DEFAULT 0 COMMENT '文章阅读总数',
   commentCount         BIGINT NOT NULL DEFAULT 0 COMMENT '文章评论总数', 
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id),
   CONSTRAINT FK_ARTICLE_category FOREIGN KEY (category) REFERENCES xhome_xblog_category (id) ON DELETE RESTRICT ON UPDATE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_article COMMENT '文章';

/*==============================================================*/
/* Table: xhome_xblog_tag                                       */
/*==============================================================*/
CREATE TABLE xhome_xblog_tag
(
   id                   INTEGER NOT NULL AUTO_INCREMENT COMMENT '标签ID',
   name                 VARCHAR(30) NOT NULL COMMENT '标签名称',
   articleCount         BIGINT NOT NULL DEFAULT 0 COMMENT '标签文章总数',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_tag COMMENT '标签';

/*==============================================================*/
/* Table: xhome_xblog_article_tag                               */
/*==============================================================*/
CREATE TABLE xhome_xblog_article_tag
(
   id                   INTEGER NOT NULL AUTO_INCREMENT COMMENT '文章标签ID',
   article              BIGINT NOT NULL COMMENT '文章ID',
   tag                  INTEGER NOT NULL COMMENT '标签ID',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id),
   CONSTRAINT FK_AT_TAG FOREIGN KEY (tag) REFERENCES xhome_xblog_tag (id) ON DELETE RESTRICT ON UPDATE CASCADE,
   CONSTRAINT FK_AT_ARTICLE FOREIGN KEY (article) REFERENCES xhome_xblog_article (id) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_article_tag COMMENT '文章标签';

/*==============================================================*/
/* Table: xhome_xblog_comment                                   */
/*==============================================================*/
CREATE TABLE xhome_xblog_comment
(
   id                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
   article              BIGINT NOT NULL COMMENT '文章ID',
   type                 TINYINT UNSIGNED NOT NULL COMMENT '0:评论,1:回复,2:引用',
   target               BIGINT UNSIGNED NOT NULL COMMENT '普通评论为文章ID，回复或引用则为评论ID',
   user_name            VARCHAR(20) NOT NULL COMMENT '评论用户的名称',
   user_email           VARCHAR(50) NOT NULL COMMENT '评论用户的邮箱',
   user_website         VARCHAR(100) DEFAULT NULL COMMENT '评论用户的个人主页', 
   content              TEXT NOT NULL COMMENT '评论内容',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id),
   CONSTRAINT FK_COMMENT_ARTICLE FOREIGN KEY (article) REFERENCES xhome_xblog_article (id) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_comment COMMENT '文章评论';

/*==============================================================*/
/* Table: xhome_xblog_record                           */
/*==============================================================*/
CREATE TABLE xhome_xblog_record
(
   id                   BIGINT NOT NULL AUTO_INCREMENT,
   article              BIGINT NOT NULL COMMENT '文章',
   user                 BIGINT NOT NULL COMMENT '用户',
   address              VARCHAR(16) NOT NULL COMMENT '访问地址(IPv4/IPv6)',
   agent                TINYINT NOT NULL DEFAULT 0 COMMENT '0:Other,1:Chrome, 2:Firefox, 3:IE, 4:Android',
   number               VARCHAR(150) NOT NULL COMMENT '设备编号',
   created              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:允许访问,1:文章不存在,2:用户不存在:3:权限不够',
   PRIMARY KEY (id)
)
ENGINE = MYISAM
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_record COMMENT '文章访问记录';

/*==============================================================*/
/* Table: xhome_xblog_category_role_permission                  */
/*==============================================================*/
CREATE TABLE xhome_xblog_category_role_permission
(
   id                   BIGINT NOT NULL AUTO_INCREMENT,
   category             INTEGER DEFAULT NULL COMMENT 'NULL表示针对所有分类',
   role                 INTEGER DEFAULT NULL COMMENT 'NULL表示针对所有角色',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许访问,1:允许访问,2:允许查看评论,3:允许评论,4:允许修改,5:允许删除,6:允许添加',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_category_role_permission comment '分类角色访问权限';

/*==============================================================*/
/* Table: xhome_xblog_category_user_permission                  */
/*==============================================================*/
CREATE TABLE xhome_xblog_category_user_permission
(
   id                   BIGINT NOT NULL AUTO_INCREMENT,
   category             INTEGER DEFAULT NULL COMMENT 'NULL表示针对所有分类',
   user                 BIGINT DEFAULT NULL COMMENT 'NULL表示针对所有用户',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许访问,1:允许访问,2:允许查看评论,3:允许评论,4:允许修改,5:允许删除,6:允许添加',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_category_user_permission COMMENT '分类用户访问权限';

/*==============================================================*/
/* Table: xhome_xblog_article_role_permission                   */
/*==============================================================*/
CREATE TABLE xhome_xblog_article_role_permission
(
   id                   BIGINT NOT NULL AUTO_INCREMENT,
   article              BIGINT DEFAULT NULL COMMENT 'NULL表示针对所有文章',
   role                 INTEGER DEFAULT NULL COMMENT 'NULL表示针对所有角色',
   permission           TINYINT UNSIGNED NOT NULL COMMENT '0:不允许访问,1:允许访问,2:允许查看评论,3:允许评论,4:允许修改,5:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_article_role_permission COMMENT '文章角色访问权限';

/*==============================================================*/
/* Table: xhome_xblog_article_user_permission                   */
/*==============================================================*/
CREATE TABLE xhome_xblog_article_user_permission
(
   id                   BIGINT NOT NULL AUTO_INCREMENT,
   article              BIGINT DEFAULT NULL COMMENT 'NULL表示针对所有文章',
   user                 BIGINT DEFAULT NULL COMMENT 'NULL表示针对所有用户',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许访问,1:允许访问,2:允许查看评论,3:允许评论,4:允许修改,5:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_article_user_permission COMMENT '文章用户访问权限';

/*==============================================================*/
/* Table: xhome_xblog_tag_role_permission                       */
/*==============================================================*/
CREATE TABLE xhome_xblog_tag_role_permission
(
   id                   BIGINT NOT NULL AUTO_INCREMENT,
   tag                  INTEGER DEFAULT NULL COMMENT 'NULL表示针对所有标签',
   role                 INTEGER DEFAULT NULL COMMENT 'NULL表示针对所有角色',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许查看,1:允许查看,2:允许添加,3,:允许修改,4:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_tag_role_permission COMMENT '标签角色访问权限';

/*==============================================================*/
/* Table: xhome_xblog_tag_user_permission                       */
/*==============================================================*/
CREATE TABLE xhome_xblog_tag_user_permission
(
   id                   BIGINT NOT NULL AUTO_INCREMENT,
   tag                  INTEGER DEFAULT NULL COMMENT 'NULL表示针对所有标签',
   user                 BIGINT DEFAULT NULL COMMENT 'NULL表示针对所有用户',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许查看,1:允许查看,2:允许添加,3,:允许修改,4:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_tag_user_permission COMMENT '标签用户访问权限';

/*==============================================================*/
/* Table: xhome_xblog_manage_log                                */
/*==============================================================*/
CREATE TABLE xhome_xblog_manage_log
(
   id                   BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
   content              VARCHAR(50) COMMENT '内容描述',
   action               TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:Add, 1:Update, 2: Remove, 3: Search...',
   type                 TINYINT UNSIGNED NOT NULL DEFAULT 2 COMMENT '1: 分类, 2: 文章, 3: 标签, 4: 文章标签, 5: 评论, 6: 文章访问记录..',
   obj                  BIGINT COMMENT '对象ID,NULL表示未知',
   owner                BIGINT COMMENT '创建者,NULL表示匿名用户',
   created              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作结果,0:成功,其它：错误状态码',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_xblog_manage_log COMMENT '管理日志';
