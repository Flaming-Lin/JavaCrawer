<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html><html>
<head>
<base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>ECharts</title>
    <!-- 引入 echarts.js -->
    <script src="js/echarts.js"></script>
    <script src="js/echarts-wordcloud.js"></script>
</head>
<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width:1800px;height:800px;"></div>
    <script type="text/javascript">
        var chart = echarts.init(document.getElementById('main'));

            var option = {
                tooltip: {},
                series: [ {
                    type: 'wordCloud',
                    gridSize: 2,
                    sizeRange: [24, 100],
                    rotationRange: [-90, 90],
                    shape: 'pentagon',
                    width: 800,
                    height: 800,
                    drawOutOfBound: true,
                    textStyle: {
                        normal: {
                            color: function () {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    data: [{name:"新闻",value:376},{name:"科技",value:320},{name:"标题",value:262},{name:"财经",value:232},{name:"报道",value:182},{name:"军事",value:175},{name:"美国",value:150},{name:"记者",value:134},{name:"24日",value:128},{name:"美元",value:121},{name:"来源",value:121},{name:"一个",value:116},{name:"日本",value:112},{name:"25日",value:111},{name:"男子",value:110},{name:"北京",value:108},{name:"公司",value:106},{name:"市场",value:102},{name:"游戏",value:96},{name:"消息",value:94}
                    ]
                } ]
            };

            chart.setOption(option);

            window.onresize = chart.resize;
    </script>
</body>
</html>
