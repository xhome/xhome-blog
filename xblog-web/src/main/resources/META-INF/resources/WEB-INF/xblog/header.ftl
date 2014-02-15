<div id="header">
<nav class="navbar navbar-inverse navbar-static-top" role="navigation">
    <div class="container">
        <div class="col-lg-2">
            <a class="navbar-brand" href="${xblog.article_index_url}">XHome XBlog</a>
        </div>

        <div class="col-lg-7">
        <ul class="nav navbar-nav navbar-left">
            <li <#if nav_active == 'index'>class="active"</#if>><a href="${xblog.article_index_url}">首页</a></li>
            <li <#if nav_active == 'about'>class="active"</#if>><a href="${xblog.base_url}about.htm">关于</a></li>
            <#if xblog.xauth.user??>
                <li class="dropdown">
                    <a data-toggle="dropdown">管理<b class="caret"></b></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a role="button" data-toggle="modal" data-target="#change_password_modal">修改密码</a></li>
                        <li><a href="${xblog.base_url}dashboard.htm">后台管理</a></li>
                        <li><a href="${xblog.xauth.user_logout_url}">退出登录</a></li>
                    </ul>
                </li> 
            </#if>
        </ul>
        </div>
        
        <div class="col-lg-3 navbar-right" style="padding-right: 0px;">
            <form class="navbar-form" role="search" action="${xblog.article_search_url}">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="搜索" name="search"> 
                <span class="input-group-btn">
                    <button type="submit" class="btn btn-default" title="搜索">
                        <span class="glyphicon glyphicon-search"></span> 
                    </button>
                </span> 
            </div>
            </form>
        </div>
    </div>
</nav>
</div>
<#-- 修改密码弹窗 开始 -->
<#if xblog.xauth.user??>
<div class="modal fade" id="change_password_modal" role="dialog" aria-labelledby="change_password_modal_label" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="change_password_modal_label" class="modal-title">修改密码</h4>
			</div>
            <div class="modal-body">
                <form id="user_change_password_form" class="form-horizontal" role="form" action="${xblog.xauth.user_base_url}/chpasswd.json" method="POST">
                    <div id="user_change_password_error_msg" class="alert alert-danger" style="display: none;"></div>
                    <div class="input-group">
                        <span class="input-group-btn input-group-addon"> 旧密码 </span>
                        <input type="password" class="form-control" id="password_old" name="password_old" placeholder="请输入旧密码" maxlength="20"> 
                    </div>
                    <br />
                    <div class="input-group">
                        <span class="input-group-btn input-group-addon"> 新密码 </span>
                        <input type="password" class="form-control" id="password_new" name="password_new" placeholder="请输入新密码" maxlength="20"> 
                    </div>
                    <br />
                    <div class="input-group">
                        <span class="input-group-btn input-group-addon">确认密码</span>
                        <input type="password" class="form-control" id="password_confirm" name="password_confirm" placeholder="请确认新密码" maxlength="20"> 
                    </div>
                </form>
            </div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button type="submit" class="btn btn-primary" onclick="$('#user_change_password_form').submit();">确认修改</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="xauth/js/user/validate.js"></script>
<script type="text/javascript" src="xblog/js/header.js"></script>
</#if>
<#-- 修改密码弹窗 结束 -->
