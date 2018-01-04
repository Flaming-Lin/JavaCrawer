<%@ page language="java" import="java.util.*,java.sql.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	Connection conn = null;
	try 
	{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=jidong", "Flaming","nuofeng123456");
		System.out.println("---------连接数据库----------");
	} 
	catch(ClassNotFoundException e)
	{
		e.printStackTrace();
	}
	catch (SQLException e)
	{
		e.printStackTrace();
	}
	String sql = "SELECT keywords,counts FROM jidong_data where counts >= 1 and counts <=20 ORDER BY counts DESC";
	PreparedStatement pst = conn.prepareStatement(sql);
	ResultSet rs = pst.executeQuery();
	StringBuilder sb1 = new StringBuilder();
	StringBuilder sb2 = new StringBuilder();
	while(rs.next()) {
		sb1.append("\"");
		sb1.append(rs.getString(1));
		sb1.append("\",");
		
		sb2.append(rs.getInt(2));
		sb2.append(",");
	}
	
	rs.close();
	pst.close();
	conn.close();
	String s1 = sb1.substring(0,sb1.length() -1);
	String s2 = sb2.substring(0,sb2.length() - 1);
	System.out.println(s1);
	System.out.println(s2);
%>

<!DOCTYPE html><html>
<head>
<base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>ECharts</title>
    <!-- 引入 echarts.js -->
    <script src="js/echarts.js"></script>
</head>
<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 1400px;height:600px;"></div>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        // 指定图表的配置项和数据
        var option = {
            title: {
                text: '新浪新闻热词分析'
            },
            tooltip: {},
            legend: {
                data:['词频']
            },
            xAxis: {
                data: [<%=s1%>]
            },
            yAxis: {},
            series: [{
                name: '销量',
                type: 'bar',
                data: [<%=s2%>]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>
