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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author wusd
 * @description 读写分离锁，加读锁时要求之前没锁或者全是读锁，加写锁时要求之前没锁
 * @create 2020/09/18 10:55
 */
public class ZkLock3 {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    static ZooKeeper zooKeeper;
    static String lockPrefix = "/shared_lock";

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

        ReadWriteZKLock readWriteZKLock = new ReadWriteZKLock(lockPrefix, zooKeeper);
        readWriteZKLock.writeLock();
        System.out.println("获取锁成功");
        Thread.sleep(100000);
    }
}

