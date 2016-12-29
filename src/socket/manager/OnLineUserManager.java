package socket.manager;

import util.cache.LayIMCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class OnLineUserManager {
    static final String cacheName = "LAYIM";
    static final String cacheKey = "ONLINE_USER";

    public void addUser(String userId){
        Map map = LayIMCache.getInstance().get(cacheName,cacheKey);
        if(map == null){
            map = new ConcurrentHashMap();
        }
        map.put(userId,"online");
        LayIMCache.getInstance().set(cacheName,cacheKey,map);
    }

    public void removeUser(String userId){
        Map map = LayIMCache.getInstance().get(cacheName,cacheKey);

        if (map == null){ return; }

        map.remove(userId);
        LayIMCache.getInstance().set(cacheName,cacheKey,map);
    }

    public Map getOnLineUsers(){
        Map map = LayIMCache.getInstance().get(cacheName,cacheKey);
        return map;
    }
    //
}
