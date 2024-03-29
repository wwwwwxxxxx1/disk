<%@ page language="java" import="java.util.*, org.apache.hadoop.fs.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">

    <title>海康云盘系统</title>

    <link rel="stylesheet" href="css/poposlides.css">
    <link rel="stylesheet" href="css/layout.css">
    <style>


        #td-operator a{
            background-color: transparent; border: 1px solid #28B2FC;
            color: #28B2FC;font-weight: 900; font-size:13px; padding: 3px 10px;
            border-radius: 5px;margin-right:8px ;
            display:inline-block;
        }

        #td-operator img{
            width:18px;
        }

    </style>
    <script src="js/jquery-2.1.4.min.js" ></script>

    <script>


        function initTrEvent(){
            $("#center_table tr").mouseover(
                function(){
                    $(this).siblings().removeClass("highlightTd");
                    $(this).addClass("highlightTd");
                    $(this).find("div").show(); //显示出分享,下载,删除
                    $(this).siblings().find("div").hide();

                });
        }

        $(function(){
            initTrEvent();
        });

        function delFile(fileName){
            if(confirm('确定要删除吗')==true){
                window.location.href=" HiveServlet?flag=delete&fileName=" +encodeURI(fileName);
            }
        }
        ${refreshScript}



    </script>
</head>

<body>


<div class="frame-center">

    <div class="frame-title">
			        	   <span>
				        	    <a > HIVE-日志分析</a>
			              	</span>
        <span>
			            		  已全部加载，共  <label>${fn:length(hdfsFileList)} </label>个
			                </span>

    </div>

    <div class="tab">
        <table>
            <tr>
                <td id="td-operator">
                    <a href="javascript:$('#img1').show(); window.location.href='Hive_LogTableServlet' "><img src="images/upload.png"  />查看表格</a>
                    <a href="hive/log-group-result.jsp"><img src="images/download.png"/>访问量排序</a>
                    <a href="javascript:$('#img1').show(); window.location.href='Log_20_HiveServlet' "><img src="images/download.png" />访问量前20排序(10个)</a>
                    <a><img src="images/machine.png" />图表展示</a>
                </td>
                <td >

                </td>
                <td>大小</td>
                <td>修改日期</td>
            </tr>
        </table>

        <img id="img1" style="display:none" src="images/loading.gif" />


        <div class="datas">
            <table id="center_table">
                <c:forEach var="f" items="${hdfsFileList}">
                    <tr class="highlightTd">
                        <td>
                            &nbsp; <input type="checkbox"><img src="images/fileIcons/${f.icon}">
                            <label>
                                    ${f.name }
                            </label>
                        </td>
                        <td >
                            <div>
                                <a><img src="images/a.png" title="分享"  /></a>
                                <a onclick="return delFile('${f.path}')" ><img src="images/c.png"  title="删除"  /></a>
                            </div>
                        </td>
                        <td>${f.len}</td>
                        <td>${f.modificationTime}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
</body>
</html>