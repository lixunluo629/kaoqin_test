package org.springframework.boot.context.embedded.undertow;

import io.undertow.servlet.UndertowServletLogger;
import io.undertow.servlet.api.SessionPersistenceManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.ConfigurableObjectInputStream;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/FileSessionPersistence.class */
public class FileSessionPersistence implements SessionPersistenceManager {
    private final File dir;

    public FileSessionPersistence(File dir) {
        this.dir = dir;
    }

    public void persistSessions(String deploymentName, Map<String, SessionPersistenceManager.PersistentSession> sessionData) {
        try {
            save(sessionData, getSessionFile(deploymentName));
        } catch (Exception ex) {
            UndertowServletLogger.ROOT_LOGGER.failedToPersistSessions(ex);
        }
    }

    private void save(Map<String, SessionPersistenceManager.PersistentSession> sessionData, File file) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
        try {
            save(sessionData, stream);
            stream.close();
        } catch (Throwable th) {
            stream.close();
            throw th;
        }
    }

    private void save(Map<String, SessionPersistenceManager.PersistentSession> sessionData, ObjectOutputStream stream) throws IOException {
        Map<String, Serializable> session = new LinkedHashMap<>();
        for (Map.Entry<String, SessionPersistenceManager.PersistentSession> entry : sessionData.entrySet()) {
            session.put(entry.getKey(), new SerializablePersistentSession(entry.getValue()));
        }
        stream.writeObject(session);
    }

    public Map<String, SessionPersistenceManager.PersistentSession> loadSessionAttributes(String deploymentName, ClassLoader classLoader) {
        try {
            File file = getSessionFile(deploymentName);
            if (file.exists()) {
                return load(file, classLoader);
            }
            return null;
        } catch (Exception ex) {
            UndertowServletLogger.ROOT_LOGGER.failedtoLoadPersistentSessions(ex);
            return null;
        }
    }

    private Map<String, SessionPersistenceManager.PersistentSession> load(File file, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ConfigurableObjectInputStream(new FileInputStream(file), classLoader);
        try {
            Map<String, SessionPersistenceManager.PersistentSession> mapLoad = load(stream);
            stream.close();
            return mapLoad;
        } catch (Throwable th) {
            stream.close();
            throw th;
        }
    }

    private Map<String, SessionPersistenceManager.PersistentSession> load(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        Map<String, SerializablePersistentSession> session = readSession(stream);
        long time = System.currentTimeMillis();
        Map<String, SessionPersistenceManager.PersistentSession> result = new LinkedHashMap<>();
        for (Map.Entry<String, SerializablePersistentSession> entry : session.entrySet()) {
            SessionPersistenceManager.PersistentSession entrySession = entry.getValue().getPersistentSession();
            if (entrySession.getExpiration().getTime() > time) {
                result.put(entry.getKey(), entrySession);
            }
        }
        return result;
    }

    private Map<String, SerializablePersistentSession> readSession(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        return (Map) stream.readObject();
    }

    private File getSessionFile(String deploymentName) {
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
        return new File(this.dir, deploymentName + ".session");
    }

    public void clear(String deploymentName) {
        getSessionFile(deploymentName).delete();
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/FileSessionPersistence$SerializablePersistentSession.class */
    static class SerializablePersistentSession implements Serializable {
        private static final long serialVersionUID = 0;
        private final Date expiration;
        private final Map<String, Object> sessionData;

        SerializablePersistentSession(SessionPersistenceManager.PersistentSession session) {
            this.expiration = session.getExpiration();
            this.sessionData = new LinkedHashMap(session.getSessionData());
        }

        public SessionPersistenceManager.PersistentSession getPersistentSession() {
            return new SessionPersistenceManager.PersistentSession(this.expiration, this.sessionData);
        }
    }
}
