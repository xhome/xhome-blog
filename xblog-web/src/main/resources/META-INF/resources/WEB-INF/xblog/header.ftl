<div id="header">
<nav class="navbar navbar-inverse navbar-static-top" role="navigation">
    <div class="container">
        <div class="col-lg-2">
            <a class="navbar-brand" href="${xblog.article_index_url}">XHome XBlog</a>
        </div>

        <div class="col-lg-7">
        <ul class="nav navbar-nav navbar-left">
            <li <#if nav_active == 'index'>class="active"</#if>><a href="${xblog.article_index_url}">首页</a></li>
            <li <#if nav_active == 'about'>class="active"</#if>><a href="${xblog.base_url}about.htm">关于</a></li>
        </ul>
        </div>
        
        <div class="col-lg-3 navbar-right" style="padding-right: 0px;">
            <form class="navbar-form" role="search" action="${xblog.article_search_url}">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="搜索" name="search"> 
                <span class="input-group-btn">
                    <button type="submit" class="btn btn-default" title="搜索">
                        <span class="glyphicon glyphicon-search"></span> 
                    </button>
                </span> 
            </div>
            </form>
        </div>
    </div>
</nav>
</div>
