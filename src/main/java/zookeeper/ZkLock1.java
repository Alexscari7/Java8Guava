package zookeeper;

import com.google.common.collect.Lists;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.proto.WatcherEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author wusd
 * @description 互斥锁 非公平锁实现，直接创建znode，创建成功即为获取成功，抛出异常则为获取失败并等待
 * 每个等待线程被唤醒的几率相同，无论到来的顺序
 * @create 2020/09/17 9:49
 */
public class ZkLock1 {

    static ZooKeeper zooKeeper;
    static Object o = new Object();

    @BeforeEach
    void getZookeeper() throws IOException {
        zooKeeper = new ZooKeeper("47.111.103.138:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("接收到watcher事件" + watchedEvent.getType().name());
                System.out.println("zk状态" + watchedEvent.getState().name());
                System.out.println("监控path:" + watchedEvent.getPath());
            }
        }, 0, new byte[]{});
    }

    @Test
    void test1() throws InterruptedException, KeeperException {
        while(!tryLock()){
            synchronized (o){
                o.wait();
            }
        }
        System.out.println("获取锁成功");
        Thread.sleep(100000);
    }

    public boolean tryLock() throws InterruptedException, KeeperException {
        Exception ex = null;
        try {
            // 获取锁成功，直接返回true
            String lock = zooKeeper.create("/exclusive_lock_unfair", null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL);
        } catch (KeeperException.NodeExistsException e) {
            // 获取锁失败，返回false并等待，设置监听，当锁可用时重新获取
            ex = e;
            System.out.println("获取锁失败,等待并监听此节点的删除事件");
            zooKeeper.exists("/mynode", watchedEvent -> {
                if (watchedEvent.getType().equals(Watcher.Event.EventType.NodeDeleted)) {
                    System.out.println("锁被释放，重新获取锁");
                }
                synchronized (o){
                    o.notifyAll();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !(ex instanceof KeeperException.NodeExistsException);
    }

}
