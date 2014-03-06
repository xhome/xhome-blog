<!DOCTYPE html>
<#import "../xblog.ftl" as xblog />
<html lang="zh_CN">
<#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.article??>
    <#assign article = commonResult.data.article /> 
</#if>
<@xblog.head title="${xconfig('xblog_title_read')?replace('$' + '{article.title}', article.title)}" description="XBlog" keywords="XHome, XBlog, 博客">
    <link href="${xblog.xblog_base_url}/css/article/article.css" rel="stylesheet" media="screen" />
</@xblog.head>
<body>
<#-- 导航菜单 开始 -->
<#assign nav_active = "index" />
<@include file = "header.ftl" />
<#-- 导航菜单 结束 -->

<div id="wrapper" class="container">
    <#-- 导航菜单 开始 --> 
    <@include file = "nav.ftl" /> 
    <#-- 导航菜单 结束 --> 

    <#-- 页面主内容 开始 -->
    <div class="col-lg-10">
        <#if article??>
            <#-- 文章内容 开始 -->
            <@include file = "article.ftl" /> 
            <#-- 文章内容 结束 -->
            
            <#-- 文章评论列表 开始 -->
            <div id="article_comments">
                <#if article.comments?? && (article.comments?size > 0)>
                    <#--
                    <button class="btn btn-primary" onclick="$(window).scrollTop($('#article_comment_form').offset().top);">发表评论</button>
                    -->
                    <#list article.comments as comment>
                        <div id="article_comment_${comment.id}" class="panel panel-default">
                            <div class="panel-heading">
                                <h1 class="panel-title">${comment.userName} 发表于 ${comment.created?string("yyyy-MM-dd HH:mm:ss")}</h1> 
                            </div>
                            <div class="panel-body">${comment.content}</div> 
                        </div>
                    </#list>
                </#if>
            </div>
            <#-- 文章评论列表 结束 -->
            
            <#-- 发表文章评论 开始 -->
            <div class="well">
                <form id="article_comment_form" class="form-horizontal" role="form" action="${xblog.comment_base_url}/add.json" method="POST">
                    <div id="article_comment_error_msg" class="alert alert-danger" style="display: none;"></div>
                    <input type="hidden" id="comment.article.id" name="comment.article.id" value="${article.id}" />
                    <input type="hidden" id="comment.target.id" name="comment.target.id" value="${article.id}" />
                    <input type="hidden" id="comment.article.title" name="comment.article.title" value="${article.title}" />
                    <div class="input-group">
                        <span class="input-group-btn input-group-addon">称呼</span>
                        <input class="form-control" id="comment.userName" name="comment.userName" placeholder="您的称呼（必填）" maxlength="20"> 
                    </div>
                    <br />
                    <div class="input-group">
                        <span class="input-group-btn input-group-addon">邮箱</span>
                        <input class="form-control" id="comment.userEmail" name="comment.userEmail" placeholder="您的邮箱（必填，我们会为您保密）" maxlength="50"> 
                    </div>
                    <br />
                    <div class="input-group">
                        <span class="input-group-btn input-group-addon">主页</span>
                        <input class="form-control" id="comment.userWebsite" name="comment.userWebsite" placeholder="您的个人主页" maxlength="100"> 
                    </div>
                    <br />
                    <textarea class="form-control" rows="4" id="comment.content" name="comment.content" placeholder="说的什么吧~" maxlength="1000"></textarea>
                    <br />
                    <input type="submit" class="btn btn-primary nav-right" value="提交评论"></input> 
                </form>
            </div>
            <#-- 发表文章评论 结束 -->
        <#else>
            <div class="alert alert-danger">
                文章不存在~
            </div>
        </#if>
    </div>
    <#-- 页面主内容 结束 -->
</div>

<#-- 版权信息 开始 -->
<@include file = "footer.ftl" />
<#-- 版权信息 结束 -->

<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-validate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-xvalidate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xblog/js/article/validate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xblog/js/article/read.js"></script>
</body>
</html>
