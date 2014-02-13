<!DOCTYPE html>
<#import "/xauth/xauth.ftl" as xauth />
<html lang="zh_CN">
<@xauth.head title="博客" description="XBlog" keywords="XHome, XBlog, 博客">
<link href="xlibs/ext/resources/css/ext-all.css" rel="stylesheet" type="text/css"/>
<style>
#footer {
    background-color: #f5f5f5;
    text-align: center;
}
</style>
</@xauth.head>
<body>
<#-- 导航菜单 开始 -->
<#assign nav_active = "index" />
<@include file="header.ftl" />
<#-- 导航菜单 结束 -->

<div id="wrapper" class="container">
    <#-- 导航菜单 开始 --> 
    <div class="col-lg-2">
        <#-- 分类导航菜单 开始 -->
        <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.categories??>
        <div class="list-group">
            <a class="list-group-item active">
                <h4 class="list-group-item-heading">分类</h4>
            </a>
            <#list commonResult.data.categories.results as category>
            <a class="list-group-item" href="${xauth.base_url}xblog/article/index.htm?cid=${category.id}">${category.name}</a>
            </#list>
        </div>
        <#else>
        <div class="alert alert-danger">
            没有分类 
        </div>
        </#if>
        <#-- 分类导航菜单 结束 -->

        <#-- 标签导航菜单 开始 -->
        <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.tags??>
        <div class="list-group">
            <a class="list-group-item active">
                <h4 class="list-group-item-heading">标签</h4>
            </a>
            <#list commonResult.data.tags.results as tag>
            <a class="list-group-item" href="${xauth.base_url}xblog/article/index.htm?tid=${tag.id}">${tag.name}</a>
            </#list>
        </div>
        </#if>
        <#-- 标签导航菜单 结束 -->

        <a class="btn btn-primary btn-lg btn-block" href="${xauth.base_url}xauth/user/login.htm">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</a> 
    </div>
    <#-- 导航菜单 结束 --> 

    <#-- 页面主内容 开始 -->
    <div class="col-lg-10">
        <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.articles??>
        <#-- 文章列表 开始 -->
        <#list commonResult.data.articles.results as article>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title"><a href="${xauth.base_url}xblog/article/read.htm?id=${article.id}">${article.title}</a></h1> 
                </div>
                <#if article.content??>
                    <div class="panel-body">${article.content}</div>
                </#if>
                <div class="panel-footer">
                    <span class="glyphicon glyphicon-folder-open"></span>
                    <a href="${xauth.base_url}xblog/article/index.htm?cid=${article.category.id}">${article.category.name}</a>
                   
                    <#if article.tags?? && (article.tags?size > 0)>
                    <div class="pull-right">
                        <span class="glyphicon glyphicon-tags"></span>
                        <#list article.tags as tag>
                            <a href="${xauth.base_url}xblog/article/index.htm?tid=${tag.id}">${tag.name}</a><#if tag_has_next>,</#if>
                        </#list> 
                    </div>
                    </#if>
                </div> 
            </div>
        </#list>
        <#-- 文章列表 结束 -->
   
        <#-- 文章分页 开始 -->
        <#assign totalPage = commonResult.data.articles.totalPage />
        <#if (totalPage > 1)>
            <#assign cpage = commonResult.data.articles.page />
            <ul class="pagination">
                <#-- 当前页大于1时显示上一页按钮 --> 
                <#if (cpage > 1)>
                    <li><a href="${xauth.base_url}xblog/article/index.htm?page=${cpage - 1}">&laquo;</a></li>
                </#if>
               
                <#-- 分页条总共显示19个分页按钮，当总页数大于19时需要特殊处理 -->
                <#if (totalPage > 19)>
                    <#-- 当前分页按钮左边最多显示4个按钮 -->
                    <#assign spage = cpage - 4 />
                    <#if (spage > 4)>
                        <#-- 分页省略符按钮 -->
                        <li class="disabled"><a>...</a></li> 
                        <#if (spage > 10)>
                            <#assign npage = ((cpage / 10)?int) - 1 />
                            <#if (npage > 0)> 
                                <#list 1..npage as tpage>
                                    <#assign page = tpage * 10 /> 
                                    <li><a href="${xauth.base_url}xblog/article/index.htm?page=${page}">${page}</a></li>
                                </#list>
                                <li class="disabled"><a>...</a></li> 
                            </#if>
                        </#if>
                    </#if>
                    <#if (spage > 0)>
                        <#list spage..(cpage - 1) as page>
                            <li><a href="${xauth.base_url}xblog/article/index.htm?page=${page}">${page}</a></li>
                        </#list>
                    </#if>
                    
                    <#-- 当前分页按钮 -->
                    <li class="active"><a href="${xauth.base_url}xblog/article/index.htm?page=${cpage}">${cpage}</a></li>
                   
                    <#-- 当前分页按钮右边最多显示4个按钮 -->
                    <#assign epage = cpage + 4 />
                    <#if (epage > totalPage)>
                        <#assign epage = totalPage /> 
                    </#if>
                    <#if (cpage < totalPage)> 
                        <#list (cpage + 1)..epage as page>
                            <li><a href="${xauth.base_url}xblog/article/index.htm?page=${page}">${page}</a></li>
                        </#list>
                    </#if>
                    
                    <#assign dpage = totalPage - epage />
                    <#if (dpage > 0)>
                        <#-- 分页省略符按钮 -->
                        <li class="disabled"><a>...</a></li>
                        <#if (dpage > 10)>
                            <#assign spage = ((cpage / 10)?int + 1) />
                            <#assign epage = (totalPage / 10)?int />
                            <#list spage..epage as tpage>
                                <#assign page = tpage * 10 />
                                <li><a href="${xauth.base_url}xblog/article/index.htm?page=${page}">${page}</a></li>
                            </#list>
                            <#if ((epage * 10) < totalPage)>
                                <li class="disabled"><a>...</a></li>
                            </#if>
                        </#if>
                    </#if>
                <#else> 
                    <#list 1..totalPage as page>
                        <li <#if page == cpage>class="active"</#if>><a href="${xauth.base_url}xblog/article/index.htm?page=${page}">${page}</a></li>
                    </#list>
                </#if>
                
                <#if cpage < totalPage> 
                    <li><a href="${xauth.base_url}xblog/article/index.htm?page=${cpage + 1}">&raquo;</a></li>
                </#if>
            </ul>
        </#if>
        <#-- 文章分页 结束 -->
        <#else>
            <div class="alert alert-danger">
                博主太懒了，没有任何文章
            </div>
        </#if>
    </div>
    <#-- 页面主内容 结束 -->
</div>

<#-- 版权信息 开始 -->
<div id="footer" class="container">
    <@include file="copyright.ftl" />
</div>
<#-- 版权信息 结束 -->
</body>
</html>
