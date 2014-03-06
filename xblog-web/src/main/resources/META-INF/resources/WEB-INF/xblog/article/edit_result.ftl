<!DOCTYPE html>
<#import "/xblog/xblog.ftl" as xblog />
<html lang="zh_CN">
<#assign article = commonResult.data /> 
<#assign page_title = xconfig('xblog_title_edit')?replace('$' + '{article.title}', article.title) />
<@xblog.head title="${page_title}" description="XBlog" keywords="XHome, XBlog, 博客">
    <link href="${xblog.xblog_base_url}/css/article/article.css" rel="stylesheet" media="screen" />
</@xblog.head>
<body>
<#-- 导航菜单 开始 -->
<#assign nav_active = "index" />
<@include file = "header.ftl" />
<#-- 导航菜单 结束 -->

<div id="wrapper" class="container">
    <div class="alert alert-success">
        ${commonResult.message} 
    </div>
    <@include file="article.ftl" />
</div>

<#-- 版权信息 开始 -->
<@include file = "footer.ftl" />
<#-- 版权信息 结束 -->
</body>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-validate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-xvalidate.js"></script>
</html>
