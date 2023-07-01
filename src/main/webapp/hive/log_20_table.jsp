<%@ page language="java" import="java.util.*,com.beans.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">

    <title></title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">

    <style>
        table{
            border-collapse: collapse;
            border:1px solid #EAEAEA;
            width:90%;
        }

        td,th{
            border:1px solid #EAEAEA;
            height:25px;
            font-size: 12px; font-family: 微软雅黑;
            padding-left:10px;
        }

        th{
            background:#F4FBFF;
        }

        .highlightTd{
            background:#F4FBFF;
            font-weight:bold;
        }
    </style>

    <script>
        $(function(){
            $("#center_table tr").mouseover(
                function(){
                    $(this).siblings().removeClass("highlightTd");
                    $(this).addClass("highlightTd");

                });
        });

    </script>
</head>

<body>
<div>

    <cite style="color:#A30014;font-weight:bold;font-family: 'Montserrat', 'sans-serif' " >
        <a href="javascript:window.history.back()"  >
            <img  width="25" src="images/fileIcons/back.png"  ></a>
        &nbsp;	&nbsp;	&nbsp;	&nbsp; HIVE-表格示例
    </cite>
    <hr />


    <table id="center_table">
        <tr>

            <th>IP地址</th>
            <th>访问次数</th>

        </tr>


        <c:forEach var="log" items="${logList }">
            <tr>
                <td>${log.ip } </td>
                <td>${log.count }</td>
            </tr>
        </c:forEach>
    </table>

</div>
</body>
</html>