<#-- 显示文章内容 -->
<div class="panel panel-primary">
    <div class="panel-heading">
        <h1 class="panel-title"><a href="${xblog.article_read_url}?id=${article.id}">${article.title}</a></h1> 
    </div>
    <#if article.content??>
        <div class="panel-body">${article.content}</div>
    </#if>
    <div class="panel-footer">
        <span class="glyphicon glyphicon-folder-open"></span>
        <a href="${xblog.article_index_url}?cid=${article.category.id}">${article.category.name}</a>
       
        <#if article.tags?? && (article.tags?size > 0)>
        <div class="pull-right">
            <span class="glyphicon glyphicon-tags"></span>
            <#list article.tags as tag>
                <a href="${xblog.article_index_url}?tid=${tag.id}">${tag.name}</a><#if tag_has_next>,</#if>
            </#list> 
        </div>
        </#if>
    </div> 
</div>
