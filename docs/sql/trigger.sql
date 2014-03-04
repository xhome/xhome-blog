use xblog;

DELIMITER //

DROP TRIGGER IF EXISTS txblog_update_tag;//
CREATE TRIGGER txblog_update_tag AFTER UPDATE ON xhome_xblog_tag
FOR EACH ROW BEGIN
    IF old.id!=new.id OR old.status!=new.status THEN
        -- 更新对应的文章标签信息
        UPDATE xhome_xblog_article_tag AS at
            SET at.tag=new.id,
            at.modifier=new.modifier,
            at.modified=new.modified,
            at.status=new.status
        WHERE at.tag=old.id;
    END IF;
END;//

DROP TRIGGER IF EXISTS txblog_update_category;//
CREATE TRIGGER txblog_update_category AFTER UPDATE ON xhome_xblog_category
FOR EACH ROW BEGIN
    IF old.id!=new.id OR old.status!=new.status THEN
        -- 更新对应的文章标签信息
        UPDATE xhome_xblog_article AS a
            SET a.category=new.id,
            a.modifier=new.modifier,
            a.modified=new.modified,
            a.status=new.status
        WHERE a.category=old.id;
    END IF;
END;//
