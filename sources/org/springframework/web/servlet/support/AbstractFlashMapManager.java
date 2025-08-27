package org.springframework.web.servlet.support;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.util.UrlPathHelper;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/support/AbstractFlashMapManager.class */
public abstract class AbstractFlashMapManager implements FlashMapManager {
    private static final Object DEFAULT_FLASH_MAPS_MUTEX = new Object();
    protected final Log logger = LogFactory.getLog(getClass());
    private int flashMapTimeout = 180;
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    protected abstract List<FlashMap> retrieveFlashMaps(HttpServletRequest httpServletRequest);

    protected abstract void updateFlashMaps(List<FlashMap> list, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    public void setFlashMapTimeout(int flashMapTimeout) {
        this.flashMapTimeout = flashMapTimeout;
    }

    public int getFlashMapTimeout() {
        return this.flashMapTimeout;
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
        this.urlPathHelper = urlPathHelper;
    }

    public UrlPathHelper getUrlPathHelper() {
        return this.urlPathHelper;
    }

    @Override // org.springframework.web.servlet.FlashMapManager
    public final FlashMap retrieveAndUpdate(HttpServletRequest request, HttpServletResponse response) {
        List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
        if (CollectionUtils.isEmpty(allFlashMaps)) {
            return null;
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Retrieved FlashMap(s): " + allFlashMaps);
        }
        List<FlashMap> mapsToRemove = getExpiredFlashMaps(allFlashMaps);
        FlashMap match = getMatchingFlashMap(allFlashMaps, request);
        if (match != null) {
            mapsToRemove.add(match);
        }
        if (!mapsToRemove.isEmpty()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Removing FlashMap(s): " + mapsToRemove);
            }
            Object mutex = getFlashMapsMutex(request);
            if (mutex != null) {
                synchronized (mutex) {
                    List<FlashMap> allFlashMaps2 = retrieveFlashMaps(request);
                    if (allFlashMaps2 != null) {
                        allFlashMaps2.removeAll(mapsToRemove);
                        updateFlashMaps(allFlashMaps2, request, response);
                    }
                }
            } else {
                allFlashMaps.removeAll(mapsToRemove);
                updateFlashMaps(allFlashMaps, request, response);
            }
        }
        return match;
    }

    private List<FlashMap> getExpiredFlashMaps(List<FlashMap> allMaps) {
        List<FlashMap> result = new LinkedList<>();
        for (FlashMap map : allMaps) {
            if (map.isExpired()) {
                result.add(map);
            }
        }
        return result;
    }

    private FlashMap getMatchingFlashMap(List<FlashMap> allMaps, HttpServletRequest request) {
        List<FlashMap> result = new LinkedList<>();
        for (FlashMap flashMap : allMaps) {
            if (isFlashMapForRequest(flashMap, request)) {
                result.add(flashMap);
            }
        }
        if (!result.isEmpty()) {
            Collections.sort(result);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Found matching FlashMap(s): " + result);
            }
            return result.get(0);
        }
        return null;
    }

    protected boolean isFlashMapForRequest(FlashMap flashMap, HttpServletRequest request) {
        String expectedPath = flashMap.getTargetRequestPath();
        if (expectedPath != null) {
            String requestUri = getUrlPathHelper().getOriginatingRequestUri(request);
            if (!requestUri.equals(expectedPath) && !requestUri.equals(expectedPath + "/")) {
                return false;
            }
        }
        MultiValueMap<String, String> actualParams = getOriginatingRequestParams(request);
        MultiValueMap<String, String> expectedParams = flashMap.getTargetRequestParams();
        for (String expectedName : expectedParams.keySet()) {
            List<String> actualValues = (List) actualParams.get(expectedName);
            if (actualValues == null) {
                return false;
            }
            for (String expectedValue : (List) expectedParams.get(expectedName)) {
                if (!actualValues.contains(expectedValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    private MultiValueMap<String, String> getOriginatingRequestParams(HttpServletRequest request) {
        String query = getUrlPathHelper().getOriginatingQueryString(request);
        return ServletUriComponentsBuilder.fromPath("/").query(query).build().getQueryParams();
    }

    @Override // org.springframework.web.servlet.FlashMapManager
    public final void saveOutputFlashMap(FlashMap flashMap, HttpServletRequest request, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(flashMap)) {
            return;
        }
        String path = decodeAndNormalizePath(flashMap.getTargetRequestPath(), request);
        flashMap.setTargetRequestPath(path);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Saving FlashMap=" + flashMap);
        }
        flashMap.startExpirationPeriod(getFlashMapTimeout());
        Object mutex = getFlashMapsMutex(request);
        if (mutex != null) {
            synchronized (mutex) {
                List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
                List<FlashMap> allFlashMaps2 = allFlashMaps != null ? allFlashMaps : new CopyOnWriteArrayList<>();
                allFlashMaps2.add(flashMap);
                updateFlashMaps(allFlashMaps2, request, response);
            }
            return;
        }
        List<FlashMap> allFlashMaps3 = retrieveFlashMaps(request);
        List<FlashMap> allFlashMaps4 = allFlashMaps3 != null ? allFlashMaps3 : new LinkedList<>();
        allFlashMaps4.add(flashMap);
        updateFlashMaps(allFlashMaps4, request, response);
    }

    private String decodeAndNormalizePath(String path, HttpServletRequest request) {
        if (path != null && !path.isEmpty()) {
            path = getUrlPathHelper().decodeRequestString(request, path);
            if (path.charAt(0) != '/') {
                String requestUri = getUrlPathHelper().getRequestUri(request);
                path = StringUtils.cleanPath(requestUri.substring(0, requestUri.lastIndexOf(47) + 1) + path);
            }
        }
        return path;
    }

    protected Object getFlashMapsMutex(HttpServletRequest request) {
        return DEFAULT_FLASH_MAPS_MUTEX;
    }
}
