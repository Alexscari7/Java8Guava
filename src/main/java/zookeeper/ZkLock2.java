package zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * @author wusd
 * @description 互斥锁，公平锁实现，在母节点下创建有序节点，如果只有一个子节点或子节点序号最小，说明获取锁成功，
 * 否则等待并监听前一个节点的删除事件，被删除时当前节点直接获取锁成功，不需要重新竞争，且是按等待顺序获取的
 * @create 2020/09/18 9:37
 */
public class ZkLock2 {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    static ZooKeeper zooKeeper;
    static Thread mian;
    static String lockPrefix = "/exclusive_lock_fair";
    static String lockPath = "/seq";

    @BeforeEach
    void getZookeeper() throws IOException {
        zooKeeper = new ZooKeeper("47.111.103.138:2181", 60000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("接收到watcher事件" + watchedEvent.getType().name());
                System.out.println("zk状态" + watchedEvent.getState().name());
                System.out.println("监控path:" + watchedEvent.getPath());
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
            }
        });
    }

    @Test
    void test1() throws InterruptedException, KeeperException {
        countDownLatch.await();
        System.out.println("zookeeper同步状态连接成功，开始执行操作");

        if(!tryLock()){
            mian = Thread.currentThread();
            LockSupport.park();
        }
        System.out.println("获取锁成功");
        Thread.sleep(100000);
    }

    public boolean tryLock() {
        try {
            String lock = zooKeeper.create(lockPrefix + lockPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> orginChildren = zooKeeper.getChildren("/exclusive_lock_fair", false);
            List<String> sortedChildren = orginChildren.stream()
                    .sorted(Comparator.naturalOrder())
                    .map(t -> lockPrefix + "/" + t)
                    .collect(Collectors.toList());
            // 获取锁成功
            if (sortedChildren.indexOf(lock) == 0) {
                return true;
            }
            // 获取锁失败，设置监听，被通知时直接获取成功
            System.out.println("获取锁失败，等待并监听前一个节点的删除事件");
            String waitNode = sortedChildren.get(sortedChildren.indexOf(lock) - 1);
            zooKeeper.exists(waitNode, watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {
                    System.out.println("锁被释放，重新获取锁");
                    LockSupport.unpark(mian);
                }
            });
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
