<#-- 导航菜单 -->
<div id="nav_article" class="col-lg-3">
    <#-- 分类导航菜单 开始 -->
    <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.categories??>
        <div class="list-group">
            <a class="list-group-item active">
                <h4 class="list-group-item-heading">分类</h4>
            </a>
            <#list commonResult.data.categories.results?sort_by("articleCount")?reverse?chunk(2) as row>
                <div class="list-group-item">
                    <div class="row">
                        <#list row as category>
                            <div class="col-xs-6" style="padding-right: 5px; padding-left: 10px;">
                                <a href="${xblog.article_index_url}?cid=${category.id}">
                                    ${category.name}
                                    <span class="badge" style="float: right;">${category.articleCount}</span>
                                </a>
                            </div>
                        </#list>
                    </div>
                </div>
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
            <div class="list-group-item">
                <#assign tagMapStr = "{" />
                <#assign fistTag = true />
                <#assign font_size = 35 />
                <#list commonResult.data.tags.results?sort_by("articleCount")?reverse as tag>
                    <#if fistTag == true>
                        <#assign fistTag = false />
                    <#else>
                        <#assign tagMapStr = tagMapStr + "," />
                    </#if>
                    <#assign tagMapStr = tagMapStr + "\"" + tag.name + "\":" + font_size />
                    <#if (font_size > 10)>
                        <#assign font_size = font_size - 1 />
                    </#if>
                </#list>
                <#assign tagMapStr = tagMapStr + "}" />
                <#assign tagMap = tagMapStr?eval />
            
                <#list commonResult.data.tags.results?sort_by("name") as tag>
                    <a href="${xblog.article_index_url}?tid=${tag.id}" style="font-size: ${tagMap[tag.name]}px;">${tag.name}</a>
                </#list>
            </div>
        </div>
    </#if>
    <#-- 标签导航菜单 结束 -->

    <#-- 登录按钮 开始 -->
    <#if !xblog.xauth.user??> 
        <a class="btn btn-primary btn-lg btn-block" href="${xblog.xauth.user_login_url}">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</a> 
    </#if>
    <#-- 登录按钮 结束 --> 
</div>
