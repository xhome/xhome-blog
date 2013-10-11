package org.xhome.xblog.web.action;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.http.response.Result;
import org.xhome.validator.CommonValidator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.core.service.RecordService;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 11, 201310:00:52 PM
 * @describe 
 */
@Controller
public class RecordAction {

	@Autowired(required = false)
	private RecordService recordService;
	private Logger logger = LoggerFactory.getLogger(RecordAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_RECORD_QUERY	= "xauth/record/query.do";
	public final static String	RM_RECORD_COUNT	= "xauth/record/count.do";
	
	@InitBinder
	public void initBinder(HttpServletRequest request, WebDataBinder binder) {
		String uri = request.getRequestURI();
		commonValidator.setValidators(validatorMapping.getValidatorByUri(uri));
		binder.setValidator(commonValidator);
		if (logger.isDebugEnabled()) {
			logger.debug("init binder for " + uri);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = RM_RECORD_QUERY, method = RequestMethod.GET)
	public Object getRecords(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "查询文章访问日志");
			} else {
				query = new QueryBase();
				logger.info("用户" + uname + "查询文章访问日志");
			}
		}
		recordService.getRecords(user, query);
		
		String msg = "条件查询文章访问日志";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_RECORD_COUNT, method = RequestMethod.GET)
	public Object countRecords(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "统计文章访问日志");
			} else {
				logger.info("用户" + uname + "统计文章访问日志");
			}
		}
		long count = recordService.countRecords(user, query);
		
		String msg = "条件统计文章访问日志，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}

	public void setRecordService(RecordService recordService) {
		this.recordService = recordService;
	}

	public RecordService getRecordService() {
		return recordService;
	}
	
}
