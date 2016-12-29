package api;

import dao.LayIMDao;
import dao.operate.LayIMResultSetOperate;

import org.omg.PortableInterceptor.ACTIVE;

import pojo.BigGroup;
import pojo.FriendGroup;
import pojo.StatusUser;
import pojo.User;
import pojo.message.ToServerMessageMine;
import pojo.result.BaseDataResult;
import pojo.result.JsonResult;
import socket.manager.OnLineUserManager;
import util.LayIMFactory;
import util.cache.LayIMCache;
import util.log.LayIMLog;
import util.serializer.IJsonSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@WebServlet(name = "LayIMService",urlPatterns = "/api")
public class LayIMService extends HttpServlet {

    LayIMDao dao = new LayIMDao();
    IJsonSerializer serializer = LayIMFactory.createSerializer();

    //post请求
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    //get请求
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	response.setCharacterEncoding("UTF-8");

        //得到action类型，根据action来进行业务处理
        String action = request.getParameter("action");
        String userId = request.getParameter("id");

        int userIdInt = 0;
        if(userId != null){
            userIdInt = Integer.parseInt(userId);
        }
        JsonResult result = new JsonResult();
        switch (action) {
            case RequestAction.BASE:
//                result = dao.getBaseList(userIdInt);
//            	result = "";
//            	Map res = new OnLineUserManager().getOnLineUsers();
            	BaseDataResult res = new BaseDataResult();
            	
            	//查询我的信息begin
            	StatusUser me = new StatusUser();
            	me.setId(userIdInt);//我的ID
            	me.setAvatar("http://tp1.sinaimg.cn/1571889140/180/40030060651/1");//我的头像
            	me.setUsername("群员:"+userIdInt);//我的昵称
            	me.setSign("我曹,我是签名");//签名
            	me.setStatus("online");//在线状态
            	//查询我的信息end
            	res.setMine(me);
            	
            	//获取好友分组列表begin
            	List<FriendGroup> groupList = new ArrayList<FriendGroup>();
            	
            	FriendGroup group = new FriendGroup();
            	group.setId(9001);//分组ID
            	group.setGroupname("分组名称9001");//好友分组名
            	
            	List<User> friendList = new ArrayList<User>();
            	User friend = new User();
            	friend.setId(1001);
            	friend.setSign("我曹我是好友1001的签名");
            	friend.setUsername("1001好友");
            	friend.setAvatar("http://tp1.sinaimg.cn/1571889140/180/40030060651/1");
            	friendList.add(friend);
            	
            	group.setList(friendList);
            	
            	groupList.add(group);
            	
            	//获取好友分组列表end
            	res.setFriend(groupList);
            	
            	//群begin
            	List<BigGroup> g = new ArrayList<BigGroup>();
            	BigGroup gg = new BigGroup();
            	gg.setAvatar("http://tva2.sinaimg.cn/crop.0.0.199.199.180/005Zseqhjw1eplix1brxxj305k05kjrf.jpg");//分组图标
            	gg.setId(8001);//分组id
            	gg.setGroupname("我曹,这里群聊");//分组名称
            	g.add(gg);
            	res.setGroup(g);
            	//群end
            	
                result.setData(res);
                break;
            case RequestAction.MEMBER:
                int groupId =Integer.parseInt(request.getParameter("id"));
//                result = dao.getMemberList(groupId);
            	Map<String, Object> res1 = new HashMap<String, Object>();
            	
            	List<User> friendList1 = new ArrayList<User>();
            	User friend1 = new User();
            	friend1.setId(1001);
            	friend1.setSign("我曹我是好友1001的签名");
            	friend1.setUsername("1001好友");
            	friend1.setAvatar("http://tp1.sinaimg.cn/1571889140/180/40030060651/1");
            	friendList1.add(friend1);
            	
            	User friend2 = new User();
            	friend2.setId(1002);
            	friend2.setSign("我曹我是好友1002的签名");
            	friend2.setUsername("1002好友");
            	friend2.setAvatar("http://tp4.sinaimg.cn/2145291155/180/5601307179/1");
            	friendList1.add(friend2);
            	res1.put("list", friendList1);
            	result.setData(res1);
                break;
            default:
                break;
        }
        writeToClient(response,result);

    }

    private void writeToClient(HttpServletResponse response,JsonResult result) throws IOException {
        response.getWriter().write(serializer.toJSON(result));
    }
}
