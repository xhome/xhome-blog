<#-- 显示文章内容 -->
<div class="panel panel-primary">
    <div class="panel-heading">
        <#assign article_detail = article.detail />
        <#if search_word??>
            <#assign article_title = article.title?replace(search_word, '<font color="red">' + search_word + '</font>')>
        <#else>
            <#assign article_title = article.title />
        </#if>
        <h1 class="panel-title">
            <a href="${xblog.article_read_url}?id=${article.id}">${article_title}</a>
            <#if xblog.xauth.user?? && article.owner ?? && (xblog.xauth.user.id == article.owner)>
                <a href="${xblog.article_edit_url}?id=${article.id}" style="float:right;">编辑</a> 
            </#if>
        </h1> 
    </div>
        <div class="well" style="padding: 2px; margin: 0px; border-radius: 0px;">
            <span class="glyphicon glyphicon-calendar"></span> ${article.modifiedStr}
            <div style="float: right">
                <span>${article.readCount}次阅读</span>
                &nbsp;&nbsp;
                <span>${article.commentCount}条评论</span> 
            </div>
        </div>
    <div class="panel-body">
        ${article_detail}
        <#if is_article_list??>
            <a href="${xblog.article_read_url}?id=${article.id}">阅读全文...</a>
        </#if>
    </div>
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
