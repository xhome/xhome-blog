/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2013-8-8 23:09:44                            */
/*==============================================================*/


/*==============================================================*/
/* Table: xhome_blog_catagory                                   */
/*==============================================================*/
CREATE TABLE xhome_blog_catagory
(
   id                   INTEGER UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID',
   name                 VARCHAR(20) NOT NULL COMMENT '栏目名称',
   parent               INTEGER NOT NULL DEFAULT NULL COMMENT 'NULL表示顶级栏目',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id),
   CONSTRAINT FK_CATAGORY_PARENT FOREIGN KEY (parent) REFERENCES xhome_blog_catagory (id) ON DELETE restrict ON UPDATE cascade
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

/*==============================================================*/
/* Table: xhome_blog_article                                    */
/*==============================================================*/
CREATE TABLE xhome_blog_article
(
   id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文章ID',
   title                VARCHAR(50) NOT NULL COMMENT '文章标题',
   content              TEXT NOT NULL COMMENT '文章内容',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:完全公开,1:仅可见,2:不可见评论,3:仅自己可见,4:指定权限',
   catagory             INTEGER NOT NULL COMMENT '所属分类',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id),
   CONSTRAINT FK_ARTICLE_CATAGORY FOREIGN KEY (catagory) REFERENCES xhome_blog_catagory (id) ON DELETE restrict ON UPDATE cascade
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

/*==============================================================*/
/* Table: xhome_blog_article_role_permission                    */
/*==============================================================*/
CREATE TABLE xhome_blog_article_role_permission
(
   id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   article              BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
   role                 INTEGER UNSIGNED NOT NULL COMMENT 'NULL表示针对所有角色',
   permission           TINYINT UNSIGNED NOT NULL COMMENT '0:不允许访问,1:允许访问,2,:允许查看评论,3:允许评论,4:允许修改,5:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_article_role_permission COMMENT '文章角色访问权限';

/*==============================================================*/
/* Table: xhome_blog_tag                                        */
/*==============================================================*/
CREATE TABLE xhome_blog_tag
(
   id                   INTEGER UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签ID',
   name                 VARCHAR NOT NULL COMMENT '标签名称',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

/*==============================================================*/
/* Table: xhome_blog_article_tag                                */
/*==============================================================*/
CREATE TABLE xhome_blog_article_tag
(
   id                   INTEGER UNSIGNED NOT NULL,
   article              BIGINT NOT NULL COMMENT '文章ID',
   tag                  INTEGER NOT NULL COMMENT '标签ID',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id),
   CONSTRAINT FK_AT_TAG FOREIGN KEY (tag) REFERENCES xhome_blog_tag (id) ON DELETE restrict ON UPDATE cascade,
   CONSTRAINT FK_AT_ARTICLE FOREIGN KEY (article) REFERENCES xhome_blog_article (id) ON DELETE cascade ON UPDATE cascade
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

/*==============================================================*/
/* Table: xhome_blog_article_user_permission                    */
/*==============================================================*/
CREATE TABLE xhome_blog_article_user_permission
(
   id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   article              BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
   user                 BIGINT UNSIGNED NOT NULL COMMENT 'NULL表示针对所有用户',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许访问,1:允许访问,2,:允许查看评论,3:允许评论,4:允许修改,5:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_article_user_permission COMMENT '文章用户访问权限';

/*==============================================================*/
/* Table: xhome_blog_article_visited                            */
/*==============================================================*/
CREATE TABLE xhome_blog_article_visited
(
   id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   article              BIGINT NOT NULL COMMENT '文章',
   user                 BIGINT NOT NULL COMMENT '用户',
   address              VARCHAR NOT NULL COMMENT '访问地址(IPv4/IPv6)',
   agent                TINYINT NOT NULL DEFAULT 0 COMMENT '0:Other,1:Chrome, 2:Firefox, 3:IE, 4:Android',
   number               VARCHAR(100) NOT NULL COMMENT '设备编号',
   created              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:允许访问,1:文章不存在,2:用户不存在:3:权限不够',
   PRIMARY KEY (id)
)
ENGINE = MYISAM
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_article_visited COMMENT '文章访问记录';

/*==============================================================*/
/* Table: xhome_blog_catagory_role_manage                       */
/*==============================================================*/
CREATE TABLE xhome_blog_catagory_role_manage
(
   id                   INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
   catagory             INTEGER UNSIGNED NOT NULL COMMENT 'NULL表示针对所有分类，否则针对分类本身或其子分类',
   role                 INTEGER UNSIGNED NOT NULL COMMENT '角色ID',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许查看,1:允许查看,2:允许添加,3,:允许修改,4:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_catagory_role_manage COMMENT '分类角色管理权限';

/*==============================================================*/
/* Table: xhome_blog_catagory_role_permission                   */
/*==============================================================*/
CREATE TABLE xhome_blog_catagory_role_permission
(
   id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   catagory             INTEGER UNSIGNED NOT NULL COMMENT '分类ID',
   role                 INTEGER UNSIGNED NOT NULL COMMENT 'NULL表示针对所有角色',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许访问,1:允许访问,2,:允许查看评论,3:允许评论,4:允许修改,5:允许删除,6:允许添加',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_catagory_role_permission COMMENT '分类角色访问权限';

/*==============================================================*/
/* Table: xhome_blog_catagory_user_manage                       */
/*==============================================================*/
CREATE TABLE xhome_blog_catagory_user_manage
(
   id                   INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
   catagory             INTEGER UNSIGNED NOT NULL COMMENT 'NULL表示针对所有分类，否则针对分类本身或其子分类',
   user                 BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许查看,1:允许查看,2:允许添加,3,:允许修改,4:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_catagory_user_manage COMMENT '分类用户管理权限';

/*==============================================================*/
/* Table: xhome_blog_catagory_user_permission                   */
/*==============================================================*/
CREATE TABLE xhome_blog_catagory_user_permission
(
   id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   catagory             INTEGER UNSIGNED NOT NULL COMMENT '分类ID',
   user                 BIGINT UNSIGNED NOT NULL COMMENT 'NULL表示针对所有用户',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许访问,1:允许访问,2,:允许查看评论,3:允许评论,4:允许修改,5:允许删除,6:允许添加',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_catagory_user_permission COMMENT '分类用户访问权限';

/*==============================================================*/
/* Table: xhome_blog_comment                                    */
/*==============================================================*/
CREATE TABLE xhome_blog_comment
(
   id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论ID',
   article              BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
   type                 TINYINT UNSIGNED NOT NULL COMMENT '0:评论,1:回复,2:引用',
   target               BIGINT UNSIGNED NOT NULL COMMENT '普通评论为文章ID，回复或引用则为评论ID',
   content              TEXT NOT NULL COMMENT '评论内容',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id),
   CONSTRAINT FK_COMMENT_ARTICLE FOREIGN KEY (article) REFERENCES xhome_blog_article (id) ON DELETE cascade ON UPDATE cascade
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_comment COMMENT '文章评论';

/*==============================================================*/
/* Table: xhome_blog_tag_role_manage                            */
/*==============================================================*/
CREATE TABLE xhome_blog_tag_role_manage
(
   id                   INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
   tag                  INTEGER UNSIGNED NOT NULL COMMENT 'NULL表示针对所有标签',
   role                 INTEGER UNSIGNED NOT NULL COMMENT '角色ID',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许查看,1:允许查看,2:允许添加,3,:允许修改,4:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_tag_role_manage COMMENT '标签角色管理权限';

/*==============================================================*/
/* Table: xhome_blog_tag_user_manage                            */
/*==============================================================*/
CREATE TABLE xhome_blog_tag_user_manage
(
   id                   INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
   tag                  INTEGER UNSIGNED NOT NULL COMMENT 'NULL表示针对所有标签',
   user                 BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
   permission           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:不允许查看,1:允许查看,2:允许添加,3,:允许修改,4:允许删除',
   owner                BIGINT NOT NULL COMMENT '创建者',
   modifier             BIGINT NOT NULL COMMENT '修改者',
   created              TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
   modified             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   version              TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
   status               TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态0:标记删除,1:正常,2:不允许删除,3:不允许修改,4:锁定',
   PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
AUTO_INCREMENT = 1;

ALTER TABLE xhome_blog_tag_user_manage COMMENT '标签用户管理权限';

