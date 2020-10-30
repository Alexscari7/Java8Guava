package zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * @author wusd
 * @description 空
 * @create 2020/09/18 13:09
 */
public class ReadWriteZKLock{
    private String lockPrefix;
    private ZooKeeper zooKeeper;
    private Thread currentThread;

    private static final String READ_LOCK_PATH = "/R";
    private static final String WRITE_LOCK_PATH = "/W";

    public ReadWriteZKLock(String lockPrefix, ZooKeeper zookeeper) {
        this.lockPrefix = lockPrefix;
        this.zooKeeper = zookeeper;
        currentThread = Thread.currentThread();
    }

    public void readLock() {
        if (!tryReadLock()) {
            LockSupport.park();
        }
    }

    public void writeLock() {
        if (!tryWriteLock()) {
            LockSupport.park();
        }
    }

    private boolean tryReadLock() {
        try {
            String lock = zooKeeper.create(lockPrefix + READ_LOCK_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> children = zooKeeper.getChildren(lockPrefix, false);
            // 将子节点按序号排序
            List<String> processedChildren = children.stream()
                    .sorted(Comparator.comparing(t -> t.substring(1)))
                    .map(t -> lockPrefix + "/" + t)
                    .collect(Collectors.toList());

            // 找到前面最近的写锁位置
            int wLockIndex = -1;
            for (int i = 0; i < processedChildren.indexOf(lock); i++) {
                if(processedChildren.get(i).matches(lockPrefix + WRITE_LOCK_PATH + "\\d*")){
                    wLockIndex = i;
                }
            }
            // 对于读锁来说，只有在全是读锁时才能加锁成功
            if(wLockIndex < 0){
                return true;
            }
            // 获取锁失败时，需要等待并监听前面最近的写节点的删除事件
            System.out.println("获取读锁失败，等待并监听前一个写锁的删除事件");
            zooKeeper.exists(processedChildren.get(wLockIndex), watchedEvent -> {
                if (Watcher.Event.EventType.NodeDeleted == watchedEvent.getType()) {
                    System.out.println("锁被释放，重新获取");
                    LockSupport.unpark(currentThread);
                }
            });
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean tryWriteLock() {
        try {
            String lock = zooKeeper.create(lockPrefix + WRITE_LOCK_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> children = zooKeeper.getChildren(lockPrefix, false);
            // 将子节点按序号排序
            List<String> processedChildren = children.stream()
                    .sorted(Comparator.comparing(t -> t.substring(1)))
                    .map(t -> lockPrefix + "/" + t)
                    .collect(Collectors.toList());

            // 对于写锁来说，只有在没有任何锁的情况下才能加锁
            // 如果当前节点序号最小，则获取锁成功
            if (processedChildren.indexOf(lock) == 0) {
                return true;
            }
            // 获取锁失败，设置前面节点的监听，被通知时直接获取成功
            System.out.println("获取写锁失败，等待并监听前一个锁节点的删除事件");
            String waitNode = processedChildren.get(processedChildren.indexOf(lock) - 1);
            zooKeeper.exists(waitNode, watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {
                    System.out.println("锁被释放，重新获取锁");
                    LockSupport.unpark(currentThread);
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
