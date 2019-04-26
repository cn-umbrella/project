<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String webPath = path + "/common/web";
    String pathWeb = basePath + "web";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="webPath" value="<%=webPath%>"></c:set>
<c:set var="pathWeb" value="<%=pathWeb%>"></c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>产品列表</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <link rel="stylesheet" type="text/css" href="${webPath}/css/jquery.mobile-1.4.2.min.css">
    <link rel="stylesheet" type="text/css" href="${webPath}/css/basic.css"/>
    <link rel="stylesheet" type="text/css" href="${webPath}/css/common.css"/>

    <script type="text/javascript" src="${webPath}/js/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="${webPath}/js/jquery.mobile-1.4.2.min.js"></script>
    <script type="text/javascript" src="${webPath}/js/base.js"></script>
    <script src="${webPath}/js/global-1.1.0.min.js"></script>
</head>
<body>
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
    <div class="header" style="position:fixed; width:100%;max-width: 750px; min-width: 320px;">
        <%--<a class="back_arr" href="${pathWeb}/web/gotoRegisterNew/1"></a>--%>
        <h1>贷款产品列表</h1>
    </div>
    <!-- main start -->
    <div class="wrapper" style="padding:0;">
        <div class="list" style="margin-top: 2.2rem;">
            <%--<li style="position: fixed;width: 100%;max-width: 750px;min-width: 320px;"><h6>你已注册成功，快来试试借款</h6></li>--%>
            <div class="list-n">
                <ul class="clearfix" id="Investors">
                </ul>
            </div>
        </div>
        <%-- <div id="cg" style="display:none;"><h2><img src="${webPath}/images/cg_icon.png">恭喜你，注册成功</h2></div>
           <script>
               $(document).ready(function(e) {
                     if("${from}"=="1"){
                         $('#cg').slideDown().delay(2000).slideUp(500);
                     }
               });
           </script>--%>
        <!-- main end -->
    </div>
</div>
</body>
<script type="text/javascript">
    var theHotIssue = {
        pageNum           : 1,
        totalPage         : 1,
        init              : function () {
            theHotIssue.theHotIssueInfo();
        }, theHotIssueInfo: function () {
            var data = {};
            data.numPerPage = 1200;
            data.pageNum = theHotIssue.pageNum;
            openAjax('${pathWeb}/productinfo/findProductInfoList', data, theHotIssue.theHotIssueHtml);
        }, theHotIssueHtml: function (data) {
            if (data.code == $_SUCCESS_CODE) {
                var jsonResult = data.result;
                theHotIssue.totalPage = data.result.pageInfo.totalPage;
                var transHtml = '';
                if (jsonResult.list.length > 0) {
                    $.each(jsonResult.list, function (i, account) {
                        var interestWay = "月";
                        if (account.interestWay == '0') {
                            interestWay = '日';
                        }
                        transHtml += '<li>'
                            + '<a href=\"${pathWeb}/web/gotoThirdProductDetail?productId=' + account.eid + '\" ' + 'rel="external"><img src="' + account.companyLogo + '">'
                            + '<dl>'
                            + '<dt>' + account.productName + '</dt>'
                            + '<dd><p>' + account.keyRifi + '</p></dd>'
                            + '<dd><span>' + interestWay + '费率：' + '<em>' + account.rate + '%</em></span><span>' + '申请成功：<em>' + account.applySuccessNum + '人</em></span></dd>'
                            + '</dl>'
                            + '</a></li>';

                    });
                    $("#Investors").append(transHtml);
                } else {
                    showLoader("已无推荐产品");
                }
            } else {
                showLoader(data.message);
            }
        }
    }

    $(function () {
        theHotIssue.init();
    });
</script>
</html>
