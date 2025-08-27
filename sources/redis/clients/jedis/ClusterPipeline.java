package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ClusterPipeline.class */
public interface ClusterPipeline {
    Response<String> clusterNodes();

    Response<String> clusterMeet(String str, int i);

    Response<String> clusterAddSlots(int... iArr);

    Response<String> clusterDelSlots(int... iArr);

    Response<String> clusterInfo();

    Response<List<String>> clusterGetKeysInSlot(int i, int i2);

    Response<String> clusterSetSlotNode(int i, String str);

    Response<String> clusterSetSlotMigrating(int i, String str);

    Response<String> clusterSetSlotImporting(int i, String str);
}
