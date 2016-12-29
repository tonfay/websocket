package pojo;

import java.util.List;

/**
 */
public class FriendGroup extends Group {
    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    private int online;
    private List<User> list;
}
