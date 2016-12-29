package socket.manager;

import pojo.SocketUser;

import javax.websocket.Session;

/**
 */
public interface IUserManager {

    boolean addUser(SocketUser user);

    boolean removeUser(SocketUser user);

    int getOnlineCount();

    SocketUser getUser(int userId);

}
