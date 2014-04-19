<!DOCTYPE html>
<#import "/xblog/xblog.ftl" as xblog />
<html lang="zh_CN">
<@xblog.head title="${xconfig('xblog_title_index')}" description="XBlog" keywords="XHome, XBlog, 博客">
    <link href="${xblog.xblog_base_url}/css/message/message.css" rel="stylesheet" media="screen" />
</@xblog.head>
<body>
<#-- 导航菜单 开始 -->
<#assign nav_active = "message" />
<@include file = "header.ftl" />
<#-- 导航菜单 结束 -->
<div id="wrapper" class="container">
    <#-- 页面主内容 开始 -->
    <div class="col-lg-9">
        <#-- 留言表单 开始 -->
        <div id="leave_message_form_panel" class="well">
            <form id="leave_message_form" class="form-horizontal" role="form" action="${xblog.message_base_url}/add.json" method="POST">
                <div id="leave_message_error_msg" class="alert alert-danger" style="display: none;"></div>
                <div class="input-group">
                    <span class="input-group-btn input-group-addon">称呼</span>
                    <input class="form-control" id="message.userName" name="message.userName" placeholder="您的称呼（必填）" maxlength="20"> 
                </div>
                <br />
                <div class="input-group">
                    <span class="input-group-btn input-group-addon">邮箱</span>
                    <input class="form-control" id="message.userEmail" name="message.userEmail" placeholder="您的邮箱（必填，我们会为您保密）" maxlength="50"> 
                </div>
                <br />
                <div class="input-group">
                    <span class="input-group-btn input-group-addon">主页</span>
                    <input class="form-control" id="message.userWebsite" name="message.userWebsite" placeholder="您的个人主页" maxlength="100"> 
                </div>
                <br />
                <textarea class="form-control" rows="4" id="message.content" name="message.content" placeholder="说的什么吧~" maxlength="1000"></textarea>
                <br />
                <input type="submit" class="btn btn-primary nav-right" value="提交留言"></input> 
            </form>
        </div>
        <#-- 留言表单 结束 -->

        <#if commonResult?? && commonResult.status?? && commonResult.status == 0 && commonResult.data?? && commonResult.data.messages??>
            <#-- 留言列表 开始 -->
            <div id="leave_messages">
                <#list commonResult.data.messages.results as message>
                    <div id="leave_message_${message.id}" class="panel panel-default">
                        <div class="panel-heading">
                            <h1 class="panel-title">${message.userName} 发表于 ${message.created?string("yyyy-MM-dd HH:mm:ss")}</h1> 
                        </div>
                        <div class="panel-body">
                            ${message.content}
                            <#if message.reply??>
                                <hr/>${message.reply}
                            </#if>
                        </div> 
                    </div>
                </#list>
            </div>
            <#-- 留言列表 结束 -->
   
            <#-- 留言分页 开始 -->
            <#assign totalPage = commonResult.data.messages.totalPage />
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
                <#assign cpage = commonResult.data.messages.page />
                <ul class="pagination" style="margin-top: 0px; margin-bottom: 0px;">
                    <#-- 当前页大于1时显示上一页按钮 --> 
                    <#if (cpage > 1)>
                        <li><a href="${xblog.message_list_url}?page=${cpage - 1}${url_parameters}">&laquo;</a></li>
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
                                        <li><a href="${xblog.message_list_url}?page=${page}${url_parameters}">${page}</a></li>
                                    </#list>
                                    <li class="disabled"><a>...</a></li> 
                                </#if>
                            </#if>
                        </#if>
                        <#if (spage > 0)>
                            <#list spage..(cpage - 1) as page>
                                <li><a href="${xblog.message_list_url}?page=${page}${url_parameters}">${page}</a></li>
                            </#list>
                        </#if>
                    
                        <#-- 当前分页按钮 -->
                        <li class="active"><a href="${xblog.message_list_url}?page=${cpage}${url_parameters}">${cpage}</a></li>
                   
                        <#-- 当前分页按钮右边最多显示4个按钮 -->
                        <#assign epage = cpage + 4 />
                        <#if (epage > totalPage)>
                            <#assign epage = totalPage /> 
                        </#if>
                        <#if (cpage < totalPage)> 
                            <#list (cpage + 1)..epage as page>
                                <li><a href="${xblog.message_list_url}?page=${page}${url_parameters}">${page}</a></li>
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
                                    <li><a href="${xblog.message_list_url}?page=${page}${url_parameters}">${page}</a></li>
                                </#list>
                                <#if ((epage * 10) < totalPage)>
                                    <li class="disabled"><a>...</a></li>
                                </#if>
                            </#if>
                        </#if>
                    <#else> 
                        <#list 1..totalPage as page>
                            <li <#if page == cpage>class="active"</#if>><a href="${xblog.message_list_url}?page=${page}${url_parameters}">${page}</a></li>
                        </#list>
                    </#if>
                
                    <#if cpage < totalPage> 
                        <li><a href="${xblog.message_list_url}?page=${cpage + 1}${url_parameters}">&raquo;</a></li>
                    </#if>
                </ul>
            </#if>
            <#-- 留言分页 结束 -->
        </#if>
    </div>
    <#-- 页面主内容 结束 -->

    <#-- 导航菜单 开始 --> 
    <@include file = "nav.ftl" /> 
    <#-- 导航菜单 结束 --> 
</div>

<#-- 版权信息 开始 -->
<@include file = "footer.ftl" />
<#-- 版权信息 结束 -->
</body>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-validate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/jquery-xvalidate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xblog/js/message/validate.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xblog/js/message/list.js"></script>
</html>
