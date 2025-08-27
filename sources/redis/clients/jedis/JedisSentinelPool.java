package redis.clients.jedis;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisSentinelPool.class */
public class JedisSentinelPool extends Pool<Jedis> {
    protected GenericObjectPoolConfig poolConfig;
    protected int connectionTimeout;
    protected int soTimeout;
    protected String password;
    protected int database;
    protected String clientName;
    protected Set<MasterListener> masterListeners;
    protected Logger log;
    private volatile JedisFactory factory;
    private volatile HostAndPort currentHostMaster;
    private final Object initPoolLock;

    public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig) {
        this(masterName, sentinels, poolConfig, 2000, null, 0);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels) {
        this(masterName, sentinels, new GenericObjectPoolConfig(), 2000, null, 0);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels, String password) {
        this(masterName, sentinels, new GenericObjectPoolConfig(), 2000, password);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int timeout, String password) {
        this(masterName, sentinels, poolConfig, timeout, password, 0);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int timeout) {
        this(masterName, sentinels, poolConfig, timeout, null, 0);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, String password) {
        this(masterName, sentinels, poolConfig, 2000, password);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int timeout, String password, int database) {
        this(masterName, sentinels, poolConfig, timeout, timeout, password, database);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int timeout, String password, int database, String clientName) {
        this(masterName, sentinels, poolConfig, timeout, timeout, password, database, clientName);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int timeout, int soTimeout, String password, int database) {
        this(masterName, sentinels, poolConfig, timeout, soTimeout, password, database, null);
    }

    public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, int database, String clientName) throws NumberFormatException {
        this.connectionTimeout = 2000;
        this.soTimeout = 2000;
        this.database = 0;
        this.masterListeners = new HashSet();
        this.log = Logger.getLogger(getClass().getName());
        this.initPoolLock = new Object();
        this.poolConfig = poolConfig;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.password = password;
        this.database = database;
        this.clientName = clientName;
        HostAndPort master = initSentinels(sentinels, masterName);
        initPool(master);
    }

    @Override // redis.clients.util.Pool
    public void destroy() {
        for (MasterListener m : this.masterListeners) {
            m.shutdown();
        }
        super.destroy();
    }

    public HostAndPort getCurrentHostMaster() {
        return this.currentHostMaster;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPool(HostAndPort master) {
        synchronized (this.initPoolLock) {
            if (!master.equals(this.currentHostMaster)) {
                this.currentHostMaster = master;
                if (this.factory == null) {
                    this.factory = new JedisFactory(master.getHost(), master.getPort(), this.connectionTimeout, this.soTimeout, this.password, this.database, this.clientName, false, null, null, null);
                    initPool(this.poolConfig, this.factory);
                } else {
                    this.factory.setHostAndPort(this.currentHostMaster);
                    this.internalPool.clear();
                }
                this.log.info("Created JedisPool to master at " + master);
            }
        }
    }

    private HostAndPort initSentinels(Set<String> sentinels, String masterName) throws NumberFormatException {
        List<String> masterAddr;
        HostAndPort master = null;
        boolean sentinelAvailable = false;
        this.log.info("Trying to find master from available Sentinels...");
        Iterator<String> it = sentinels.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String sentinel = it.next();
            HostAndPort hap = HostAndPort.parseString(sentinel);
            this.log.fine("Connecting to Sentinel " + hap);
            Jedis jedis = null;
            try {
                try {
                    jedis = new Jedis(hap.getHost(), hap.getPort());
                    masterAddr = jedis.sentinelGetMasterAddrByName(masterName);
                    sentinelAvailable = true;
                } catch (JedisException e) {
                    this.log.warning("Cannot get master address from sentinel running @ " + hap + ". Reason: " + e + ". Trying next one.");
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                if (masterAddr == null || masterAddr.size() != 2) {
                    this.log.warning("Can not get master addr, master name: " + masterName + ". Sentinel: " + hap + ".");
                    if (jedis != null) {
                        jedis.close();
                    }
                } else {
                    master = toHostAndPort(masterAddr);
                    this.log.fine("Found Redis master at " + master);
                    if (jedis != null) {
                        jedis.close();
                    }
                }
            } catch (Throwable th) {
                if (jedis != null) {
                    jedis.close();
                }
                throw th;
            }
        }
        if (master == null) {
            if (sentinelAvailable) {
                throw new JedisException("Can connect to sentinel, but " + masterName + " seems to be not monitored...");
            }
            throw new JedisConnectionException("All sentinels down, cannot determine where is " + masterName + " master is running...");
        }
        this.log.info("Redis master running at " + master + ", starting Sentinel listeners...");
        for (String sentinel2 : sentinels) {
            HostAndPort hap2 = HostAndPort.parseString(sentinel2);
            MasterListener masterListener = new MasterListener(masterName, hap2.getHost(), hap2.getPort());
            masterListener.setDaemon(true);
            this.masterListeners.add(masterListener);
            masterListener.start();
        }
        return master;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HostAndPort toHostAndPort(List<String> getMasterAddrByNameResult) throws NumberFormatException {
        String host = getMasterAddrByNameResult.get(0);
        int port = Integer.parseInt(getMasterAddrByNameResult.get(1));
        return new HostAndPort(host, port);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // redis.clients.util.Pool
    public Jedis getResource() {
        while (true) {
            Jedis jedis = (Jedis) super.getResource();
            jedis.setDataSource(this);
            HostAndPort master = this.currentHostMaster;
            HostAndPort connection = new HostAndPort(jedis.getClient().getHost(), jedis.getClient().getPort());
            if (master.equals(connection)) {
                return jedis;
            }
            returnBrokenResource(jedis);
        }
    }

    @Override // redis.clients.util.Pool
    @Deprecated
    public void returnBrokenResource(Jedis resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    @Override // redis.clients.util.Pool
    @Deprecated
    public void returnResource(Jedis resource) {
        if (resource != null) {
            resource.resetState();
            returnResourceObject(resource);
        }
    }

    /* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisSentinelPool$MasterListener.class */
    protected class MasterListener extends Thread {
        protected String masterName;
        protected String host;
        protected int port;
        protected long subscribeRetryWaitTimeMillis;
        protected volatile Jedis j;
        protected AtomicBoolean running;

        protected MasterListener() {
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.running = new AtomicBoolean(false);
        }

        public MasterListener(String masterName, String host, int port) {
            super(String.format("MasterListener-%s-[%s:%d]", masterName, host, Integer.valueOf(port)));
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.running = new AtomicBoolean(false);
            this.masterName = masterName;
            this.host = host;
            this.port = port;
        }

        public MasterListener(JedisSentinelPool this$0, String masterName, String host, int port, long subscribeRetryWaitTimeMillis) {
            this(masterName, host, port);
            this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.running.set(true);
            while (this.running.get()) {
                this.j = new Jedis(this.host, this.port);
                try {
                    try {
                    } catch (JedisException e) {
                        if (this.running.get()) {
                            JedisSentinelPool.this.log.log(Level.SEVERE, "Lost connection to Sentinel at " + this.host + ":" + this.port + ". Sleeping 5000ms and retrying.", (Throwable) e);
                            try {
                                Thread.sleep(this.subscribeRetryWaitTimeMillis);
                            } catch (InterruptedException e1) {
                                JedisSentinelPool.this.log.log(Level.SEVERE, "Sleep interrupted: ", (Throwable) e1);
                            }
                        } else {
                            JedisSentinelPool.this.log.fine("Unsubscribing from Sentinel at " + this.host + ":" + this.port);
                        }
                        this.j.close();
                    }
                    if (!this.running.get()) {
                        this.j.close();
                        return;
                    }
                    List<String> masterAddr = this.j.sentinelGetMasterAddrByName(this.masterName);
                    if (masterAddr == null || masterAddr.size() != 2) {
                        JedisSentinelPool.this.log.warning("Can not get master addr, master name: " + this.masterName + ". Sentinel: " + this.host + "：" + this.port + ".");
                    } else {
                        JedisSentinelPool.this.initPool(JedisSentinelPool.this.toHostAndPort(masterAddr));
                    }
                    this.j.subscribe(new JedisPubSub() { // from class: redis.clients.jedis.JedisSentinelPool.MasterListener.1
                        @Override // redis.clients.jedis.JedisPubSub
                        public void onMessage(String channel, String message) {
                            JedisSentinelPool.this.log.fine("Sentinel " + MasterListener.this.host + ":" + MasterListener.this.port + " published: " + message + ".");
                            String[] switchMasterMsg = message.split(SymbolConstants.SPACE_SYMBOL);
                            if (switchMasterMsg.length > 3) {
                                if (MasterListener.this.masterName.equals(switchMasterMsg[0])) {
                                    JedisSentinelPool.this.initPool(JedisSentinelPool.this.toHostAndPort(Arrays.asList(switchMasterMsg[3], switchMasterMsg[4])));
                                    return;
                                } else {
                                    JedisSentinelPool.this.log.fine("Ignoring message on +switch-master for master name " + switchMasterMsg[0] + ", our master name is " + MasterListener.this.masterName);
                                    return;
                                }
                            }
                            JedisSentinelPool.this.log.severe("Invalid message received on Sentinel " + MasterListener.this.host + ":" + MasterListener.this.port + " on channel +switch-master: " + message);
                        }
                    }, "+switch-master");
                    this.j.close();
                } catch (Throwable th) {
                    this.j.close();
                    throw th;
                }
            }
        }

        public void shutdown() {
            try {
                JedisSentinelPool.this.log.fine("Shutting down listener on " + this.host + ":" + this.port);
                this.running.set(false);
                if (this.j != null) {
                    this.j.disconnect();
                }
            } catch (Exception e) {
                JedisSentinelPool.this.log.log(Level.SEVERE, "Caught exception while shutting down: ", (Throwable) e);
            }
        }
    }
}
