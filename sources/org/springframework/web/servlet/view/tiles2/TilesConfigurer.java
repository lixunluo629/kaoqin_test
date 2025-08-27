package org.springframework.web.servlet.view.tiles2;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesException;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.Refreshable;
import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.el.ELAttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.extras.complete.CompleteAutoloadTilesContainerFactory;
import org.apache.tiles.extras.complete.CompleteAutoloadTilesInitializer;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.startup.AbstractTilesInitializer;
import org.apache.tiles.startup.TilesInitializer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.DispatcherServlet;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/TilesConfigurer.class */
public class TilesConfigurer implements ServletContextAware, InitializingBean, DisposableBean {
    private static final boolean tilesElPresent = ClassUtils.isPresent("org.apache.tiles.el.ELAttributeEvaluator", TilesConfigurer.class.getClassLoader());
    private TilesInitializer tilesInitializer;
    private String[] definitions;
    private Class<? extends DefinitionsFactory> definitionsFactoryClass;
    private Class<? extends PreparerFactory> preparerFactoryClass;
    private ServletContext servletContext;
    protected final Log logger = LogFactory.getLog(getClass());
    private boolean checkRefresh = false;
    private boolean validateDefinitions = true;
    private boolean useMutableTilesContainer = false;

    public void setTilesInitializer(TilesInitializer tilesInitializer) {
        this.tilesInitializer = tilesInitializer;
    }

    public void setCompleteAutoload(boolean completeAutoload) {
        if (completeAutoload) {
            try {
                this.tilesInitializer = new SpringCompleteAutoloadTilesInitializer();
                return;
            } catch (Throwable ex) {
                throw new IllegalStateException("Tiles-Extras 2.2 not available", ex);
            }
        }
        this.tilesInitializer = null;
    }

    public void setDefinitions(String... definitions) {
        this.definitions = definitions;
    }

    public void setCheckRefresh(boolean checkRefresh) {
        this.checkRefresh = checkRefresh;
    }

    public void setValidateDefinitions(boolean validateDefinitions) {
        this.validateDefinitions = validateDefinitions;
    }

    public void setDefinitionsFactoryClass(Class<? extends DefinitionsFactory> definitionsFactoryClass) {
        this.definitionsFactoryClass = definitionsFactoryClass;
    }

    public void setPreparerFactoryClass(Class<? extends PreparerFactory> preparerFactoryClass) {
        this.preparerFactoryClass = preparerFactoryClass;
    }

    public void setUseMutableTilesContainer(boolean useMutableTilesContainer) {
        this.useMutableTilesContainer = useMutableTilesContainer;
    }

    @Override // org.springframework.web.context.ServletContextAware
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws TilesException {
        SpringWildcardServletTilesApplicationContext springWildcardServletTilesApplicationContext = new SpringWildcardServletTilesApplicationContext(this.servletContext);
        if (this.tilesInitializer == null) {
            this.tilesInitializer = createTilesInitializer();
        }
        this.tilesInitializer.initialize(springWildcardServletTilesApplicationContext);
    }

    protected TilesInitializer createTilesInitializer() {
        return new SpringTilesInitializer();
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws TilesException {
        this.tilesInitializer.destroy();
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/TilesConfigurer$SpringTilesInitializer.class */
    private class SpringTilesInitializer extends AbstractTilesInitializer {
        private SpringTilesInitializer() {
        }

        protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext context) {
            return new SpringTilesContainerFactory();
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/TilesConfigurer$SpringTilesContainerFactory.class */
    private class SpringTilesContainerFactory extends BasicTilesContainerFactory {
        private SpringTilesContainerFactory() {
        }

        protected BasicTilesContainer instantiateContainer(TilesApplicationContext context) {
            return TilesConfigurer.this.useMutableTilesContainer ? new CachingTilesContainer() : new BasicTilesContainer();
        }

        protected void registerRequestContextFactory(String className, List<TilesRequestContextFactory> factories, TilesRequestContextFactory parent) {
            if (ClassUtils.isPresent(className, TilesConfigurer.class.getClassLoader())) {
                super.registerRequestContextFactory(className, factories, parent);
            }
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: org.apache.tiles.definition.DefinitionsFactoryException */
        protected List<URL> getSourceURLs(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory) throws DefinitionsFactoryException {
            if (TilesConfigurer.this.definitions != null) {
                try {
                    List<URL> result = new LinkedList<>();
                    for (String definition : TilesConfigurer.this.definitions) {
                        Set<URL> resources = applicationContext.getResources(definition);
                        if (resources != null) {
                            result.addAll(resources);
                        }
                    }
                    return result;
                } catch (IOException ex) {
                    throw new DefinitionsFactoryException("Cannot load definition URLs", ex);
                }
            }
            return super.getSourceURLs(applicationContext, contextFactory);
        }

        protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
            CachingLocaleUrlDefinitionDAO cachingLocaleUrlDefinitionDAOInstantiateLocaleDefinitionDao = super.instantiateLocaleDefinitionDao(applicationContext, contextFactory, resolver);
            if (TilesConfigurer.this.checkRefresh && (cachingLocaleUrlDefinitionDAOInstantiateLocaleDefinitionDao instanceof CachingLocaleUrlDefinitionDAO)) {
                cachingLocaleUrlDefinitionDAOInstantiateLocaleDefinitionDao.setCheckRefresh(true);
            }
            return cachingLocaleUrlDefinitionDAOInstantiateLocaleDefinitionDao;
        }

        protected DefinitionsReader createDefinitionsReader(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory) {
            DigesterDefinitionsReader reader = new DigesterDefinitionsReader();
            if (!TilesConfigurer.this.validateDefinitions) {
                Map<String, String> map = new HashMap<>();
                map.put("org.apache.tiles.definition.digester.DigesterDefinitionsReader.PARSER_VALIDATE", Boolean.FALSE.toString());
                reader.init(map);
            }
            return reader;
        }

        protected DefinitionsFactory createDefinitionsFactory(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
            if (TilesConfigurer.this.definitionsFactoryClass != null) {
                TilesApplicationContextAware tilesApplicationContextAware = (DefinitionsFactory) BeanUtils.instantiate(TilesConfigurer.this.definitionsFactoryClass);
                if (tilesApplicationContextAware instanceof TilesApplicationContextAware) {
                    tilesApplicationContextAware.setApplicationContext(applicationContext);
                }
                BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(tilesApplicationContextAware);
                if (bw.isWritableProperty(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)) {
                    bw.setPropertyValue(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME, resolver);
                }
                if (bw.isWritableProperty("definitionDAO")) {
                    bw.setPropertyValue("definitionDAO", createLocaleDefinitionDao(applicationContext, contextFactory, resolver));
                }
                if (tilesApplicationContextAware instanceof Refreshable) {
                    ((Refreshable) tilesApplicationContextAware).refresh();
                }
                return tilesApplicationContextAware;
            }
            return super.createDefinitionsFactory(applicationContext, contextFactory, resolver);
        }

        protected PreparerFactory createPreparerFactory(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory) {
            if (TilesConfigurer.this.preparerFactoryClass != null) {
                return (PreparerFactory) BeanUtils.instantiate(TilesConfigurer.this.preparerFactoryClass);
            }
            return super.createPreparerFactory(applicationContext, contextFactory);
        }

        protected LocaleResolver createLocaleResolver(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory) {
            return new SpringLocaleResolver();
        }

        protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
            AttributeEvaluator evaluator;
            if (TilesConfigurer.tilesElPresent && JspFactory.getDefaultFactory() != null) {
                evaluator = TilesElActivator.createEvaluator(applicationContext);
            } else {
                evaluator = new DirectAttributeEvaluator();
            }
            return new BasicAttributeEvaluatorFactory(evaluator);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/TilesConfigurer$SpringCompleteAutoloadTilesInitializer.class */
    private static class SpringCompleteAutoloadTilesInitializer extends CompleteAutoloadTilesInitializer {
        private SpringCompleteAutoloadTilesInitializer() {
        }

        protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext context) {
            return new SpringCompleteAutoloadTilesContainerFactory();
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/TilesConfigurer$SpringCompleteAutoloadTilesContainerFactory.class */
    private static class SpringCompleteAutoloadTilesContainerFactory extends CompleteAutoloadTilesContainerFactory {
        private SpringCompleteAutoloadTilesContainerFactory() {
        }

        protected LocaleResolver createLocaleResolver(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory) {
            return new SpringLocaleResolver();
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/TilesConfigurer$TilesElActivator.class */
    private static class TilesElActivator {
        private TilesElActivator() {
        }

        public static AttributeEvaluator createEvaluator(TilesApplicationContext applicationContext) {
            ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
            evaluator.setApplicationContext(applicationContext);
            evaluator.init(Collections.emptyMap());
            return evaluator;
        }
    }
}
