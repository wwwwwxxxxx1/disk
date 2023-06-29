<%--
  Created by IntelliJ IDEA.
  User: wuxin
  Date: 2023/6/29
  Time: 09:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>

<head>
    <base href="<%=basePath%>">
    <title></title>
    <script src="js/jquery-2.1.4.min.js" ></script>

    <script>

        $(function() {
            $.ajax({
                url : "MR_RemoveRepeatServlet",
                dataType : "json",
                data:{filePath:'${param.filePath}'},  //要注意,它是从上个页面选的文件
                success : function(wordList) {
                    for(var i=0;i<wordList.length;i++){
                        var li= "<li>"+wordList[i]+"</li>"
                        $("#ul1").append(li);
                    }
                }
            })
        });

    </script>

</head>
body>
<div>

    <cite style="color:#A30014;font-weight:bold;font-family: 'Montserrat', sans-serif" >
        <a href="javascript:window.history.back()"   >
            <img width="25" src="images/fileIcons/back.png" >
        </a>
        &nbsp;	&nbsp;	&nbsp;	&nbsp; MR-去重复示例     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;>${param.filePath} 文件内共有如下单词: </cite>
    <hr />
    <ul id="ul1">
    </ul>



</div>

</body>
</html>
