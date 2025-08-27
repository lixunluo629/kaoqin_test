package redis.clients.jedis;

import java.util.List;
import redis.clients.jedis.JedisCluster;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ClusterCommands.class */
public interface ClusterCommands {
    String clusterNodes();

    String clusterMeet(String str, int i);

    String clusterAddSlots(int... iArr);

    String clusterDelSlots(int... iArr);

    String clusterInfo();

    List<String> clusterGetKeysInSlot(int i, int i2);

    String clusterSetSlotNode(int i, String str);

    String clusterSetSlotMigrating(int i, String str);

    String clusterSetSlotImporting(int i, String str);

    String clusterSetSlotStable(int i);

    String clusterForget(String str);

    String clusterFlushSlots();

    Long clusterKeySlot(String str);

    Long clusterCountKeysInSlot(int i);

    String clusterSaveConfig();

    String clusterReplicate(String str);

    List<String> clusterSlaves(String str);

    String clusterFailover();

    List<Object> clusterSlots();

    String clusterReset(JedisCluster.Reset reset);

    String readonly();
}
