package socket;

import pojo.SocketUser;
import pojo.message.ToServerTextMessage;
import socket.sender.MessageSender;
import socket.manager.GroupUserManager;
import socket.manager.OnLineUserManager;
import util.LayIMFactory;
import util.cache.LayIMCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket_layim/{uid}")
public class LayIMServer {

	@OnOpen
	public void open(Session session,@PathParam("uid") int uid) {
		SocketUser user = new SocketUser();
		user.setSession(session);
		user.setUserId(uid);
		// 保存在线列表
		LayIMFactory.createManager().addUser(user);
		print("当前在线用户：" + LayIMFactory.createManager().getOnlineCount());
		print("缓存中的用户个数：" + new OnLineUserManager().getOnLineUsers().size());
		
		//新接入的用户,都默认扔到8001群里
		String key = GroupUserManager.getCacheKey(8001);
		List<String> memebers = LayIMCache.getInstance().getListCache(GroupUserManager.cacheName,key);
		if(memebers == null){
			memebers = new ArrayList<String>();
			memebers.add(uid+"");
			System.out.println(uid+"加入");
		}else{
			boolean s = true;
			for (String string : memebers) {
				if(Integer.parseInt(string) == uid){
					s = false;
					break;
				}
			}
			if(s){
				memebers.add(uid+"");
				System.out.println(uid+"加入");
			}
		}
		GroupUserManager save = new GroupUserManager();
		save.saveGroupMemeberIds(8001, memebers);//默认分组8001
	}

	@OnMessage
	public void receiveMessage(String message, Session session) {
		// try {

		ToServerTextMessage toServerTextMessage = LayIMFactory
				.createSerializer()
				.toObject(message, ToServerTextMessage.class);

		// 得到接收的对象
		MessageSender sender = new MessageSender();

		sender.sendMessage(toServerTextMessage);

		// }catch (Exception e){
		// e.printStackTrace();
		// }
	}

	@OnError
	public void error(Throwable t) {
		print(t.getMessage());
	}

	@OnClose
	public void close(Session session,@PathParam("uid") int uid) {

		SocketUser user = new SocketUser();
		user.setSession(session);
		user.setUserId(uid);
		print("用户掉线");
		// 移除该用户
		LayIMFactory.createManager().removeUser(user);
		// print("当前在线用户："+LayIMFactory.createManager().getOnlineCount());

	}

	private void print(String msg) {
		System.out.println(msg);
	}
}
