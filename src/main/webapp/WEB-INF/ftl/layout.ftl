<!--
  Created by IntelliJ IDEA.
  User: junyalu
  Date: 2019/4/21
  Time: 8:45
  To change this template use File | Settings | File Templates.
-->
<#macro layout submitAction filter>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="../../favicon.ico">

        <title>Dashboard for RSUN</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="/static/plugin/bootstrap-3.3.7-dist/css/bootstrap.css">
        <link rel="stylesheet" href="/static/plugin/bootstrap-3.3.7-dist/css/bootstrap-theme.css">
        <link rel="stylesheet" href="/static/plugin/bootstrap-select-1.13.10/css/bootstrap-select.min.css">

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link href="/static/assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="/static/css/dashboard.css" rel="stylesheet">

        <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
        <!--[if lt IE 9]>
        <script src="/static/assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
        <script src="/static/assets/js/ie-emulation-modes-warning.js"></script>

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Bootstrap core JavaScript
        ================================================== -->
        <script src="/static/assets/js/vendor/jquery.min.js"></script>

        <script src="/static/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
        <script src="/static/plugin/bootstrap-select-1.13.10/js/bootstrap-select.min.js"></script>
        <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
        <script src="/static/assets/js/vendor/holder.min.js"></script>
        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="/static/assets/js/ie10-viewport-bug-workaround.js"></script>
    </head>

    <body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">RSUN HOUSE ANALYSIS</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <form id="searchForm" method="post" action="${submitAction}">
                    <input type="hidden" id="page" name="page"/>
                    <input type="hidden" id="refreshCache" name="refreshCache" value="false"/>
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                               aria-haspopup="true"
                               aria-expanded="false">Settings <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a><input type="checkbox" onchange="document.getElementById('refreshCache').value=this.checked" />
                                        强制刷新缓存</a></li>
                                <li role="separator" class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                            </ul>
                        </li>
                    </ul>
                    <div class="navbar-form navbar-right">
                        <input id="key" name="key" type="text" class="form-control" placeholder="Search...">
                        <button type="submit" class="btn btn-primary">查询</button>
                    </div>
                    <#if filter=="cert">
                        <select id="region" name="region" class="selectpicker dashboard-nav-select navbar-right">
                            <option value="" selected="selected">不限</option>
                            <option value="1">梁溪区(崇安)</option>
                            <option value="2">梁溪区(南长)</option>
                            <option value="3">梁溪区(北塘)</option>
                            <option value="4">新吴区</option>
                            <option value="5">滨湖区</option>
                            <option value="6">锡山区</option>
                            <option value="7">惠山区</option>
                        </select>
                    <#elseif filter=="house">
                        <select name="lstusage" id="lstusage" class="selectpicker dashboard-nav-select navbar-right">
                            <option value="-1" selected="selected">不限</option>
                            <option value="1">住宅</option>
                            <option value="101">低层住宅</option>
                            <option value="102">多层住宅</option>
                            <option value="103">小高层住宅</option>
                            <option value="104">高层住宅</option>
                            <option value="105">别墅</option>
                            <option value="2">商业</option>
                            <option value="3">办公</option>
                            <option value="4">车库</option>
                            <option value="5">厂房仓库</option>
                            <option value="6">其它</option>
                            <option value="7">酒店式公寓</option>
                        </select>
                    </#if>
                </form>
            </div>
        </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-3 col-md-2 sidebar">
                <ul id="sider_nav" class="nav nav-sidebar">
                    <li id="li_overview"><a href="#">总览</a></li>

                    <li id="li_analysis"><a href="#">分析</a></li>
                    <li id="li_export"><a href="#">导出excel</a></li>
                </ul>
                <ul class="nav nav-sidebar">
                    <li><a href="#">查看缓存</a></li>
                </ul>
            </div>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <#nested />
            </div>
        </div>
    </div>


    <script type="text/javascript">
    </script>
    </body>
    </html>


</#macro>