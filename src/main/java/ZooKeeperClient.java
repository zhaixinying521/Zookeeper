import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ZooKeeperClient {
    private static String connectString = "192.168.88.120:2181,192.168.88.100:2181,192.168.88.130:2181";
    private static int sessionTimeout = 4000;
    private ZooKeeper zkClient = null;
    //private static String znode = "/zk2";

    @Before
    public void init() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType() + "---" + watchedEvent.getPath());
                try {
                    ZooKeeperClient.this.zkClient.getChildren("/", true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @After
    public void close() throws InterruptedException {
        zkClient.close();
    }

    @Test
    public void create() throws KeeperException, InterruptedException {
        String path = "/zk2";
        String data = "huashi";
//        String nodeCreated = zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//        String nodeCreated = zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        String nodeCreated = zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(nodeCreated + " done!");
    }

    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        String path = "/";
        List<String> children = zkClient.getChildren( path, true);
        for (String child : children) {
            System.out.println(child);
        }

//        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void exist() throws KeeperException, InterruptedException {
        String path = "/tod";
        Stat stat = zkClient.exists(path, false);
        System.out.println(stat == null ? path + " no exit!" : path + " exit!");
    }

    @Test
    public void delete() throws KeeperException, InterruptedException {
//        String path = "/tod";
        String path = "/tod0000000002";
        zkClient.delete(path, zkClient.exists(path, false).getVersion());
    }

    @Test
    public void set() throws KeeperException, InterruptedException {
        String path = "/tod";
        String data = "shenda";
        Stat stat = zkClient.setData(path, data.getBytes(), zkClient.exists(path, false).getVersion());
        System.out.println(stat);
    }

    @Test
    public void get() throws KeeperException, InterruptedException {
        String path = "/tod";
        byte[] data = zkClient.getData(path, false, zkClient.exists(path, false));
        System.out.println(new String(data));
    }
}