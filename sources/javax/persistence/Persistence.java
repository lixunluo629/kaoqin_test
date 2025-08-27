package javax.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.spi.PersistenceProvider;

/* loaded from: persistence-api-1.0.jar:javax/persistence/Persistence.class */
public class Persistence {
    public static String PERSISTENCE_PROVIDER = "javax.persistence.spi.PeristenceProvider";
    protected static final Set<PersistenceProvider> providers = new HashSet();
    private static final Pattern nonCommentPattern = Pattern.compile("^([^#]+)");

    public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        return createEntityManagerFactory(persistenceUnitName, null);
    }

    public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
        EntityManagerFactory emf = null;
        if (providers.size() == 0) {
            try {
                findAllProviders();
            } catch (IOException e) {
            }
        }
        for (PersistenceProvider provider : providers) {
            emf = provider.createEntityManagerFactory(persistenceUnitName, properties);
            if (emf != null) {
                break;
            }
        }
        if (emf == null) {
            throw new PersistenceException("No Persistence provider for EntityManager named " + persistenceUnitName);
        }
        return emf;
    }

    private static void findAllProviders() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = loader.getResources("META-INF/services/" + PersistenceProvider.class.getName());
        Set<String> names = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            InputStream is = url.openStream();
            try {
                names.addAll(providerNamesFromReader(new BufferedReader(new InputStreamReader(is))));
                is.close();
            } catch (Throwable th) {
                is.close();
                throw th;
            }
        }
        for (String s : names) {
            try {
                providers.add((PersistenceProvider) loader.loadClass(s).newInstance());
            } catch (ClassNotFoundException e) {
            } catch (IllegalAccessException e2) {
            } catch (InstantiationException e3) {
            }
        }
    }

    private static Set<String> providerNamesFromReader(BufferedReader reader) throws IOException {
        Set<String> names = new HashSet<>();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                Matcher m = nonCommentPattern.matcher(line.trim());
                if (m.find()) {
                    names.add(m.group().trim());
                }
            } else {
                return names;
            }
        }
    }
}
