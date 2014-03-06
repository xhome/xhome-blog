<!DOCTYPE html>
<#import "/xblog/xblog.ftl" as xblog />
<html lang="zh_CN">
<#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.article??>
    <#assign article = commonResult.data.article /> 
    <#assign page_title = xconfig('xblog_title_edit')?replace('$' + '{article.title}', article.title) />
<#else>
    <#assign page_title = xconfig('xblog_title_new') />
</#if>
<@xblog.head title="${page_title}" description="XBlog" keywords="XHome, XBlog, 博客">
    <link href="${xblog.xblog_base_url}/css/article/article.css" rel="stylesheet" media="screen" />
</@xblog.head>
<body>
<#-- 导航菜单 开始 -->
<#assign nav_active = "index" />
<@include file = "header.ftl" />
<#-- 导航菜单 结束 -->

<div id="wrapper" class="container">
    <#if article??>
        <form id="article_edit_form" role="form" action="${xblog.article_base_url}/update.htm" method="POST">
            <input id="article.id" name="article.id" type="hidden" value=${article.id} /> 
            <input id="article.version" name="article.version" type="hidden" value=${article.version} /> 
    <#else> 
        <form id="article_edit_form" role="form" action="${xblog.article_base_url}/add.htm" method="POST">
    </#if>
        <#-- 文章标题 -->
        <input id="article.title" name="article.title" type="text" class="form-control"
            placeholder="请输入文章标题" value="<#if article??>${article.title}</#if>" />

        <#-- 文章内容 -->
        <script id="editor" name="article.detail" type="text/plain">
            <#if article??>
                ${article.detail}
            </#if>
        </script>
        <div id="article_params" style="display:none"></div> 
    </form>
        
    <#-- 标签列表 -->
    <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.tags??>
        <#if article??>
            <#assign article_tags = "" /> 
            <#list article.tags as tag>
                <#assign article_tags = article_tags + (', "' + tag.id + '": "' + tag.name + '"') /> 
            </#list>
            <#assign article_tags = ('{' + article_tags?substring(1) + '}')?eval /> 
        </#if>
        <div id="article_tags" class="well">
            <#list commonResult.data.tags.results as tag>
                <label class="checkbox-inline">
                    <#if article_tags?? && article_tags['' + tag.id]??>
                        <input type="checkbox" id="article_tags_${tag.id}" value="${tag.name}" checked="on" />
                    <#else>
                        <input type="checkbox" id="article_tags_${tag.id}" value="${tag.name}" />
                    </#if>
                    ${tag.name}
                </label>
            </#list>
        </div>
    </#if>
    
    <#-- 分类列表 -->
    <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.categories??>
        <div class="row">
            <div class="col-md-4">
                <div class="input-group">
                    <span class="input-group-addon">分类</span> 
                    <select id="article_category" class="form-control">
                        <#list commonResult.data.categories.results as category>
                            <option value="${category.id}" <#if article?? && category.id == article.category.id>selected="on"</#if>>${category.name}</option> 
                        </#list>
                    </select>
                </div>
            </div>
            <div class="col-md-2 col-md-offset-6">
                <button type="button" class="btn btn-default btn-block" onclick="$('#article_edit_form').submit();">提交</button>
            </div>
        </div>
    </#if>
</div>

<#-- 版权信息 开始 -->
<@include file = "footer.ftl" />
<#-- 版权信息 结束 -->
</body>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-validate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-xvalidate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/ueditor/config.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="${xblog.xblog_base_url}/js/article/validate.js"></script>
<script type="text/javascript" src="${xblog.xblog_base_url}/js/article/edit.js"></script>
</html>
