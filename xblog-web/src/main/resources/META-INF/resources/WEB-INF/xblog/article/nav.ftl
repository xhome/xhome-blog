<#-- 导航菜单 -->
<div id="nav_article" class="col-lg-2">
    <#-- 分类导航菜单 开始 -->
    <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.categories??>
        <div class="list-group">
            <a class="list-group-item active">
                <h4 class="list-group-item-heading">分类</h4>
            </a>
            <#list commonResult.data.categories.results as category>
                <a class="list-group-item" href="${xblog.article_index_url}?cid=${category.id}">${category.name}</a>
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
                <a class="list-group-item" href="${xblog.article_index_url}?tid=${tag.id}">${tag.name}</a>
            </#list>
        </div>
    </#if>
    <#-- 标签导航菜单 结束 -->

    <#-- 登录按钮 开始 -->
    <#if !xblog.xauth.user??> 
        <a class="btn btn-primary btn-lg btn-block" href="${xblog.xauth.user_login_url}">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</a> 
    </#if>
    <#-- 登录按钮 结束 --> 
</div>
