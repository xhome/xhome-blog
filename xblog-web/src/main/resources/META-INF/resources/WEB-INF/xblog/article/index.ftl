<!DOCTYPE html>
<#import "/xblog/xblog.ftl" as xblog />
<html lang="zh_CN">
<@xblog.head title="${xconfig('xblog_title_index')}" description="XBlog" keywords="XHome, XBlog, 博客">
</@xblog.head>
<body>
<#if commonResult?? && commonResult.data?? && commonResult.data.search_word??>
    <#assign search_word = commonResult.data.search_word />
</#if>
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
        <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.articles??>
            <#-- 文章列表 开始 -->
            <#assign is_article_list = true /> 
            <#list commonResult.data.articles.results as article>
                <@include file = "article.ftl" /> 
            </#list>
            <#-- 文章列表 结束 -->
   
            <#-- 文章分页 开始 -->
            <#assign totalPage = commonResult.data.articles.totalPage />
            <#if (totalPage > 1)>
                <#-- URL原始参数 --> 
                <#assign url_parameters = "" /> 
                <#if RequestParameters??>
                    <#list RequestParameters?keys as key>
                        <#if key != 'page'>
                            <#assign url_parameters = url_parameters + '&' + key + '=' + RequestParameters[key] />
                        </#if>
                    </#list>
                </#if>
                <#assign cpage = commonResult.data.articles.page />
                <ul class="pagination" style="margin-top: 0px; margin-bottom: 0px;">
                    <#-- 当前页大于1时显示上一页按钮 --> 
                    <#if (cpage > 1)>
                        <li><a href="${xblog.article_index_url}?page=${cpage - 1}${url_parameters}">&laquo;</a></li>
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
                                        <li><a href="${xblog.article_index_url}?page=${page}${url_parameters}">${page}</a></li>
                                    </#list>
                                    <li class="disabled"><a>...</a></li> 
                                </#if>
                            </#if>
                        </#if>
                        <#if (spage > 0)>
                            <#list spage..(cpage - 1) as page>
                                <li><a href="${xblog.article_index_url}?page=${page}${url_parameters}">${page}</a></li>
                            </#list>
                        </#if>
                    
                        <#-- 当前分页按钮 -->
                        <li class="active"><a href="${xblog.article_index_url}?page=${cpage}${url_parameters}">${cpage}</a></li>
                   
                        <#-- 当前分页按钮右边最多显示4个按钮 -->
                        <#assign epage = cpage + 4 />
                        <#if (epage > totalPage)>
                            <#assign epage = totalPage /> 
                        </#if>
                        <#if (cpage < totalPage)> 
                            <#list (cpage + 1)..epage as page>
                                <li><a href="${xblog.article_index_url}?page=${page}${url_parameters}">${page}</a></li>
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
                                    <li><a href="${xblog.article_index_url}?page=${page}${url_parameters}">${page}</a></li>
                                </#list>
                                <#if ((epage * 10) < totalPage)>
                                    <li class="disabled"><a>...</a></li>
                                </#if>
                            </#if>
                        </#if>
                    <#else> 
                        <#list 1..totalPage as page>
                            <li <#if page == cpage>class="active"</#if>><a href="${xblog.article_index_url}?page=${page}${url_parameters}">${page}</a></li>
                        </#list>
                    </#if>
                
                    <#if cpage < totalPage> 
                        <li><a href="${xblog.article_index_url}?page=${cpage + 1}${url_parameters}">&raquo;</a></li>
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
<@include file = "footer.ftl" />
<#-- 版权信息 结束 -->

<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-validate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-xvalidate.js"></script>
</body>
</html>
