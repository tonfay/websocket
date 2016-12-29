
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String t = request.getServerName()+":"+request.getServerPort();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>tf_layim</title>
<link rel="stylesheet" href="js/layui/css/layui.css"/>
 <script type="text/javascript" src="js/layui/layui.js"></script>
  <style>
    html{background-color: #D9D9D9;}
  </style>
</head>
<body>

<div style="margin: 300px auto; text-align: center; font-size: 20px;">
  
</div>

<script>
	function GetRandomNum(Min,Max)
	{
		var Range = Max - Min;   
		var Rand = Math.random();   
		return(Min + Math.round(Rand * Range));   
	}
	var socket = null;
	var dd = GetRandomNum(1,10000000);
	var im = {
		getUid : function() {
			//var uid = '${param.uid}';
			var i = '${param.uid}';
			var uid = dd;
			if(i == ""){
				uid = dd;
			}else{
				uid = i;
			}
			return uid;
		},
		init : function() {
			if ('WebSocket' in window) {
				var uid = im.getUid();
				var socketUrl = 'ws://<%=t%>/dd/websocket_layim/'+uid;
				socket = new WebSocket(socketUrl);
				im.startListener();
			} else {
				alert('当前浏览器不支持WebSocket功能，请更换浏览器访问。');
			}
		},
		startListener : function() {
			if (socket) {
				// 连接发生错误的回调方法
				socket.onerror = function() {
					console.log("连接失败!");
					window.location.href = window.location.href;
				};
				// 连接成功建立的回调方法
				socket.onopen = function(event) {
					console.log("连接成功");
				}
				// 接收到消息的回调方法
				socket.onmessage = function(event) {
					console.log("接收到消息");
					im.handleMessage(event.data);
				}
				// 连接关闭的回调方法
				socket.onclose = function() {
					console.log("关闭连接！!");
					window.location.href = window.location.href;
				}
			}
		},
		handleMessage : function(msg) {
			var msg = JSON.parse(event.data);
			console.log(msg);
			switch (msg.type) {
			case 'TYPE_TEXT_MESSAGE':
				layim.getMessage(msg.msg);
				break;
			default:
				break;
			}
		}
	};

	im.init();
	var layim;
	layui.use('layim', function(layim) {
		layim = layim;
		//基础配置
		layim.config({

			/* init : {
				url: '' //接口地址（返回的数据格式见下文）
				,type: 'get' //默认get，一般可不填
				,data: {}
				,mine: {
					"username": "纸飞机" //我的昵称
					,"id": "100000" //我的ID
					,"status": "online" //在线状态 online：在线、hide：隐身
					,"sign": "在深邃的编码世界，做一枚轻盈的纸飞机" //我的签名
					,"avatar": "a.jpg" //我的头像
				}
				,friend : [{
					"groupname": "前端码屌" //好友分组名
					,"id": 1 //分组ID
					,"list": [{ //分组下的好友列表
						"username": "贤心" //好友昵称
						,"id": "100000" //好友ID
						,"avatar": "a.jpg" //好友头像
						,"sign": "这些都是测试数据，实际使用请严格按照该格式返回" //好友签名
						,"status": "online" //若值为offline代表离线，online或者不填为在线
					}]
				  }]
			} *///获取主面板列表信息，下文会做进一步介绍
			init : {
				url : '/dd/api?action=base',
				data : {
					id : im.getUid()
				}
			},
			uploadImage : {
				url : '' //接口地址
				,
				type : 'post' //默认post
			},
			members : {
				url : '/dd/api?action=member' //接口地址（返回的数据格式见下文）
				,
				type : 'get' //默认get，一般可不填
				,
				data : {}
			//额外参数
			},
			tool : [ {
				alias : 'code' //工具别名
				,
				title : '代码' //工具名称
				,
				icon : '&#xe64e;' //工具图标，参考图标文档
			} ],
			initSkin: '3.jpg'
			,voice: "default.wav"
			,title: "聊天工具"

			,
			msgbox : layui.cache.dir + 'css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
			,
			find : layui.cache.dir + 'css/modules/layim/html/find.html' //发现页面地址，若不开启，剔除该项即可
			,
			chatLog : layui.cache.dir + 'css/modules/layim/html/chatLog.html' //聊天记录页面地址，若不开启，剔除该项即可
		});
		layim.on('sendMessage', function(res) {
			var mine = res.mine; //包含我发送的消息及我的信息
			var to = res.to; //对方的信息
			//监听到上述消息后，就可以轻松地发送socket了，如：
			/* socket.send(JSON.stringify({
				type : 'chatMessage' //随便定义，用于在服务端区分消息类型
				,
				data : res
			})); */
			socket.send(JSON.stringify(res));
		});

		//监听自定义工具栏点击，以添加代码为例
		layim.on('tool(code)', function(insert, send, obj) { //事件中的tool为固定字符，而code则为过滤器，对应的是工具别名（alias）
			layer.prompt({
				title : '插入代码',
				formType : 2,
				shade : 0
			}, function(text, index) {
				layer.close(index);
				insert('[pre class=layui-code]' + text + '[/pre]'); //将内容插入到编辑器，主要由insert完成
				//send(); //自动发送
			});
			console.log(this); //获取当前工具的DOM对象
			console.log(obj); //获得当前会话窗口的DOM对象、基础信息
		});
		layim.on('members', function(data){
			console.log("群里都有哪些人:"+data);
		});

		//监听收到的聊天消息，假设你服务端emit的事件名为：chatMessage
		socket.onmessage = function(res) {
			res = JSON.parse(res.data);
			if (res.type == 'TYPE_TEXT_MESSAGE') {
				layim.getMessage(res.msg); //res.data即你发送消息传递的数据（阅读：监听发送的消息）
			}
		};
		/* var user = {
			type: 'friend'
			,id: 1234560
			,username: '李彦宏' //好友昵称，若申请加群，参数为：groupname
			,avatar: 'http://tva4.sinaimg.cn/crop.0.0.996.996.180/8b2b4e23jw8f14vkwwrmjj20ro0rpjsq.jpg' //头像
			,sign: '全球最大的中文搜索引擎'
		}
		layim.setFriendGroup({
			type: user.type
			,username: user.username
			,avatar: user.avatar
			,group: layim.cache().friend //获取好友列表数据
			,submit: function(group, index){
				//一般在此执行Ajax和WS，以通知对方已经同意申请
				//……
				
				//同意后，将好友追加到主面板
				layim.addList({
					type: user.type
					,username: user.username
					,avatar: user.avatar
					,groupid: 9001 //所在的分组id
					,id: user.id //好友ID
					,sign: user.sign //好友签名
				});
				
				layer.close(index);
			}
		}); */
		
		//监听layim建立就绪
		layim.on('ready', function(res){
			//console.log(res.mine);
			layim.msgbox(1); //模拟消息盒子有新消息，实际使用时，一般是动态获得
			layer.msg('连接成功', {
				icon: 1
			})
		});
		
		//监听聊天窗口的切换
		layim.on('chatChange', function(res){
			var type = res.data.type;
			console.log(res.data.id)
			if(type === 'friend'){
				//模拟标注好友状态
				//layim.setChatStatus('<span style="color:#FF5722;">在线</span>');
			}else if(type === 'group'){
				//模拟系统消息
				layim.getMessage({
					system: true
					,id: res.data.id
					,type: "group"
					,content: '群员'+(im.getUid()) + '加入群聊'
				});
			}
		});
		
		
		//模拟系统消息
		layim.getMessage({
			groupname: "我曹,这里群聊"
			,avatar: "http://tva2.sinaimg.cn/crop.0.0.199.199.180/005Zseqhjw1eplix1brxxj305k05kjrf.jpg"
			,id: 8001
			,type: "group"
		});
		
	});
</script>
</body>
</html>

