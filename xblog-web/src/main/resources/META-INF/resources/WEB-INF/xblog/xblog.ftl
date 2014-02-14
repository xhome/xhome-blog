<#import "/xauth/xauth.ftl" as xauth />
<#assign head = xauth.head />

<#assign base_url = xauth.base_url />
<#assign xblog_base_url = base_url + "xblog">

<#assign article_base_url = xblog_base_url + "/article" />
<#assign article_index_url = article_base_url + "/index.htm" />
<#assign article_read_url = article_base_url + "/read.htm" />
<#assign article_search_url = article_base_url + "/search.htm" />

<#assign comment_base_url = xblog_base_url + "/comment" />
<#assign comment_add_url = comment_base_url + "/add.htm" />
