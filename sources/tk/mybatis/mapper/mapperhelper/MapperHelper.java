package tk.mybatis.mapper.mapperhelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.provider.EmptyProvider;
import tk.mybatis.mapper.util.StringUtil;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/mapperhelper/MapperHelper.class */
public class MapperHelper {
    private final Map<String, Boolean> msIdSkip;
    private List<Class<?>> registerClass;
    private Map<Class<?>, MapperTemplate> registerMapper;
    private Map<String, MapperTemplate> msIdCache;
    private Config config;

    public MapperHelper() {
        this.msIdSkip = new HashMap();
        this.registerClass = new ArrayList();
        this.registerMapper = new ConcurrentHashMap();
        this.msIdCache = new HashMap();
        this.config = new Config();
    }

    public MapperHelper(Properties properties) {
        this();
        setProperties(properties);
    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    private MapperTemplate fromMapperClass(Class<?> mapperClass) throws SecurityException {
        Method[] methods = mapperClass.getDeclaredMethods();
        Class<?> templateClass = null;
        Class<?> tempClass = null;
        Set<String> methodSet = new HashSet<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(SelectProvider.class)) {
                SelectProvider provider = (SelectProvider) method.getAnnotation(SelectProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(InsertProvider.class)) {
                InsertProvider provider2 = (InsertProvider) method.getAnnotation(InsertProvider.class);
                tempClass = provider2.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(DeleteProvider.class)) {
                DeleteProvider provider3 = (DeleteProvider) method.getAnnotation(DeleteProvider.class);
                tempClass = provider3.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(UpdateProvider.class)) {
                UpdateProvider provider4 = (UpdateProvider) method.getAnnotation(UpdateProvider.class);
                tempClass = provider4.type();
                methodSet.add(method.getName());
            }
            if (templateClass == null) {
                templateClass = tempClass;
            } else if (templateClass != tempClass) {
                throw new RuntimeException("一个通用Mapper中只允许存在一个MapperTemplate子类!");
            }
        }
        if (templateClass == null || !MapperTemplate.class.isAssignableFrom(templateClass)) {
            templateClass = EmptyProvider.class;
        }
        try {
            MapperTemplate mapperTemplate = (MapperTemplate) templateClass.getConstructor(Class.class, MapperHelper.class).newInstance(mapperClass, this);
            for (String methodName : methodSet) {
                try {
                    mapperTemplate.addMethodMap(methodName, templateClass.getMethod(methodName, MappedStatement.class));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(templateClass.getCanonicalName() + "中缺少" + methodName + "方法!");
                }
            }
            return mapperTemplate;
        } catch (Exception e2) {
            throw new RuntimeException("实例化MapperTemplate对象失败:" + e2.getMessage());
        }
    }

    public void registerMapper(Class<?> mapperClass) {
        if (!this.registerMapper.containsKey(mapperClass)) {
            this.registerClass.add(mapperClass);
            this.registerMapper.put(mapperClass, fromMapperClass(mapperClass));
        }
        Class<?>[] interfaces = mapperClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                registerMapper(anInterface);
            }
        }
    }

    public void registerMapper(String mapperClass) {
        try {
            registerMapper(Class.forName(mapperClass));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("注册通用Mapper[" + mapperClass + "]失败，找不到该通用Mapper!");
        }
    }

    public boolean isMapperMethod(String msId) {
        if (this.msIdSkip.get(msId) != null) {
            return this.msIdSkip.get(msId).booleanValue();
        }
        for (Map.Entry<Class<?>, MapperTemplate> entry : this.registerMapper.entrySet()) {
            if (entry.getValue().supportMethod(msId)) {
                this.msIdSkip.put(msId, true);
                this.msIdCache.put(msId, entry.getValue());
                return true;
            }
        }
        this.msIdSkip.put(msId, false);
        return false;
    }

    public boolean isExtendCommonMapper(Class<?> mapperInterface) {
        for (Class<?> mapperClass : this.registerClass) {
            if (mapperClass.isAssignableFrom(mapperInterface)) {
                return true;
            }
        }
        return false;
    }

    public void setSqlSource(MappedStatement ms) {
        MapperTemplate mapperTemplate = this.msIdCache.get(ms.getId());
        if (mapperTemplate != null) {
            try {
                mapperTemplate.setSqlSource(ms);
            } catch (Exception e) {
                throw new RuntimeException("调用方法异常:" + e.getMessage());
            }
        }
    }

    public void setProperties(Properties properties) {
        this.config.setProperties(properties);
        String mapper = null;
        if (properties != null) {
            mapper = properties.getProperty("mappers");
        }
        if (StringUtil.isNotEmpty(mapper)) {
            String[] mappers = mapper.split(",");
            for (String mapperClass : mappers) {
                if (mapperClass.length() > 0) {
                    registerMapper(mapperClass);
                }
            }
        }
    }

    public void ifEmptyRegisterDefaultInterface() {
        if (this.registerClass.size() == 0) {
            registerMapper("tk.mybatis.mapper.common.Mapper");
        }
    }

    public void processConfiguration(Configuration configuration) {
        processConfiguration(configuration, null);
    }

    public void processConfiguration(Configuration configuration, Class<?> mapperInterface) {
        String prefix;
        if (mapperInterface != null) {
            prefix = mapperInterface.getCanonicalName();
        } else {
            prefix = "";
        }
        Iterator it = new ArrayList(configuration.getMappedStatements()).iterator();
        while (it.hasNext()) {
            Object object = it.next();
            if (object instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) object;
                if (ms.getId().startsWith(prefix) && isMapperMethod(ms.getId()) && (ms.getSqlSource() instanceof ProviderSqlSource)) {
                    setSqlSource(ms);
                }
            }
        }
    }
}
