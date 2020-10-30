package zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.proto.WatcherEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author wusd
 * @description 通过exist、getData、getChildren设置监听
 * @create 2020/09/17 18:58
 */
public class WatcherTest {
    static ZooKeeper zooKeeper;
    static Watcher watcher;

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
        watcher = watchedEvent -> {
            System.out.println("接收到watcher事件" + watchedEvent.getType().name());
            System.out.println("zk状态" + watchedEvent.getState().name());
            System.out.println("监控path:" + watchedEvent.getPath());
        };
    }

    @Test
    void testExistWatcher() throws KeeperException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + ":使用exist设置watcher");
        zooKeeper.exists("/jnode", true);
        // 只对当前节点的创建和删除发送事件通知
        Thread.sleep(1000000000);
    }

    @Test
    void testGetWatcher() throws KeeperException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + ":使用getData设置watcher");
        zooKeeper.getData("/jnode", watcher, new Stat());
        // 只对当前节点的修改有效
        Thread.sleep(1000000000);
    }

    @Test
    void testGetChildWatcher() throws KeeperException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + ":使用getChildrenData设置watcher");
        List<String> children = zooKeeper.getChildren("/jnode", watcher, new Stat());
        System.out.println(children);
        // 只对当前节点的邻级子节点的创建和删除发送事件
        Thread.sleep(1000000000);
    }


}
