package org.xhome.xblog.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xhome.xblog.Article;
import org.xhome.xblog.Category;
import org.xhome.xblog.Comment;
import org.xhome.xblog.ManageLog;
import org.xhome.xblog.Record;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:46 PM
 * @describe 
 */
public abstract class AbstractTest {
	
	protected ApplicationContext	context	= null;
	protected Logger				logger	= LoggerFactory.getLogger(this.getClass());
	protected Tag oper;
	
	public AbstractTest() {
		context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		oper = new Tag("jhat");
	}
	
	protected void printTag(List<Tag> tags) {
		if (tags != null) {
			for (Tag tag : tags) {
				printTag(tag);
			}
		}
	}
	
	protected void printTag(Tag tag) {
		if (tag == null) {
			return;
		}
		logger.debug("Id:" + tag.getId() + "\tName:" + tag.getName()
				+ "\tStatus:" + tag.getStatus());
	}
	
	protected void printCategory(List<Category> categorys) {
		if (categorys != null) {
			for (Category category : categorys) {
				printCategory(category);
			}
		}
	}
	
	protected void printCategory(Category category) {
		if (category == null) {
			return;
		}
		logger.debug("Id:" + category.getId() + "\tName:" + category.getName()
				+ "\tStatus:" + category.getStatus());
		
		Category parent = category.getParent();
		if (parent != null) {
			logger.debug("\tParent:\tId:" + category.getId() + "\tName:" + category.getName()
					+ "\tStatus:" + category.getStatus());
		}
	}
	
	protected void printArticle(List<Article> articles) {
		if (articles != null) {
			for (Article article: articles) {
				printArticle(article);
			}
		}
	}
	
	protected void printArticle(Article article) {
		logger.debug("Id:" + article.getId() + "\tTitle:" + article.getTitle() + "\tAttribute:" + article.getAttribute()
				+ "\tContent:" + article.getContent() + "\tVersion:" + article.getVersion() + "\tStatus:" + article.getStatus());
		printCategory(article.getCategory());
		printTag(article.getTags());
	}
	
	protected void printComment(List<Comment> comments) {
		if (comments != null) {
			for (Comment comment : comments) {
				printComment(comment);
			}
		}
	}
	
	protected void printComment(Comment comment) {
		if (comment == null) {
			return;
		}
		Article article = comment.getArticle();
		logger.debug("Id:" + comment.getId() + "\tArticle:" + (article != null ? article.getId() : null)
				+ "\tType:" + comment.getType() + "\tCotent:" + comment.getContent() + "\tStatus:" + comment.getStatus());
		
		Comment target = comment.getTarget();
		if (target != null) {
			article = target.getArticle();
			logger.debug("\tTarget:\tId:" + target.getId() + "\tArticle:" + (article != null ? article.getId() : null)
					+ "\tType:" + target.getType() + "\tCotent:" + target.getContent() + "\tStatus:" + target.getStatus());
		}
	}
	
	protected void printRecord(List<Record> records) {
		if (records != null) {
			for (Record record : records) {
				printRecord(record);
			}
		}
	}
	
	protected void printRecord(Record record) {
		if (record== null) {
			return;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.debug("Id:" + record.getId()
				+ "\tUser:" + record.getUser().getId()
				+ "\tArticle:" + record.getArticle().getId()
				+ "\tTime:" + format.format(record.getCreated())
				+ "\tAddress:" + record.getAddress()
				+ "\tAgent:" + record.getAgent()
				+ "\tNumber:" + record.getNumber()
				+ "\tStatus:" + record.getStatus());
	}
	
	protected void printManageLog(List<ManageLog> manageLogs) {
		if (manageLogs != null) {
			for (ManageLog manageLog : manageLogs) {
				printManageLog(manageLog);
			}
		}
	}
	
	protected void printManageLog(ManageLog manageLog) {
		if (manageLog == null) {
			return;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.debug("Id:" + manageLog.getId()
				+ "\tAction:" + manageLog.getAction()
				+ "\tType:" + manageLog.getType()
				+ "\tObj:" + manageLog.getObj()
				+ "\tContent:" + manageLog.getContent()
				+ "\tTime:" + format.format(manageLog.getCreated())
				+ "\tStatus:" + manageLog.getStatus());
	}
	
}
