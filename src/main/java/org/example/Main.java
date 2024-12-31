package org.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;

import java.util.List;
import java.io.IOException;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

public class Main {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 15000, null);

        // Create
        zooKeeper.create("/node", "data".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
        zooKeeper.create("/node/child", "child".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);

        // Read
        Stat stat = new Stat();
        var data = zooKeeper.getData("/node", false, stat);
        LOG.info(new String(data));
        LOG.info(String.valueOf("version = " + stat.getVersion()));

        List<String> children = zooKeeper.getChildren("/node", false);
        children.forEach(child -> LOG.info("child found = " + child));

        // Update
        zooKeeper.setData("/node", "new data".getBytes(), -1);
        // Version = -1 means it updates the latest version

        // Delete
        zooKeeper.delete("/node/child", -1);
        zooKeeper.delete("/node", -1);

        System.out.println("Hello world!");
    }
}