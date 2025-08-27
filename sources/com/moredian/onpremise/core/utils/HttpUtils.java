package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.hyperic.sigar.NetFlags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/HttpUtils.class */
public class HttpUtils {
    public static final String URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";
    public static final String URL_PARAM_DECODECHARSET_GBK = "GBK";
    private static final String URL_PARAM_CONNECT_FLAG = "&";
    private static final String EMPTY = "";
    private static MultiThreadedHttpConnectionManager connectionManager;
    private static HttpClient client;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) HttpUtils.class);
    private static int connectionTimeOut = 25000;
    private static int socketTimeOut = 25000;
    private static int maxConnectionPerHost = 20;
    private static int maxTotalConnections = 20;

    static {
        connectionManager = null;
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
    }

    public static String sendURLPost(String url) {
        return sendURLPost(url, null);
    }

    public static String sendURLPost(String url, String params) {
        return sendURLPost(url, params, null);
    }

    public static String absoluteURLPost(String url, Map<String, Object> params, String enc) {
        String response = "";
        PostMethod postMethod = null;
        try {
            try {
                PostMethod postMethod2 = new PostMethod(url);
                if (enc == null || "".equals(enc)) {
                    enc = "UTF-8";
                }
                postMethod2.setRequestHeader("Content-Type", "application/json;charset=" + enc);
                if (params != null) {
                    Set<String> keySet = params.keySet();
                    for (String key : keySet) {
                        Object value = params.get(key);
                        if (value != null) {
                            postMethod2.addParameter(key, value.toString());
                        }
                    }
                }
                int statusCode = client.executeMethod(postMethod2);
                if (statusCode == 200) {
                    response = postMethod2.getResponseBodyAsString();
                } else {
                    logger.info("url:{} 响应状态码:{}", url, Integer.valueOf(postMethod2.getStatusCode()));
                }
                if (postMethod2 != null) {
                    postMethod2.releaseConnection();
                }
            } catch (HttpException e) {
                logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", (Throwable) e);
                if (0 != 0) {
                    postMethod.releaseConnection();
                }
            } catch (IOException e2) {
                logger.error("发生网络异常:{}", (Throwable) e2);
                if (0 != 0) {
                    postMethod.releaseConnection();
                }
            }
            return response;
        } catch (Throwable th) {
            if (0 != 0) {
                postMethod.releaseConnection();
            }
            throw th;
        }
    }

    public static String sendURLPost(String url, String params, String enc) {
        logger.info(url);
        String response = "";
        PostMethod postMethod = null;
        try {
            try {
                PostMethod postMethod2 = new PostMethod(url);
                if (enc == null || "".equals(enc)) {
                    enc = "UTF-8";
                }
                postMethod2.setRequestHeader("Content-Type", "application/json;charset=" + enc);
                RequestEntity requestEntity = new ByteArrayRequestEntity(params.getBytes("UTF-8"));
                postMethod2.setRequestEntity(requestEntity);
                int statusCode = client.executeMethod(postMethod2);
                if (statusCode == 200) {
                    InputStream inputStream = postMethod2.getResponseBodyAsStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer stringBuffer = new StringBuffer();
                    while (true) {
                        String str = br.readLine();
                        if (str == null) {
                            break;
                        }
                        stringBuffer.append(str);
                    }
                    response = stringBuffer.toString();
                } else {
                    logger.info("url:{} 响应状态码:{}", url, Integer.valueOf(postMethod2.getStatusCode()));
                }
                if (postMethod2 != null) {
                    postMethod2.releaseConnection();
                }
            } catch (HttpException e) {
                logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", (Throwable) e);
                if (0 != 0) {
                    postMethod.releaseConnection();
                }
            } catch (IOException e2) {
                logger.error("发生网络异常:{}", (Throwable) e2);
                if (0 != 0) {
                    postMethod.releaseConnection();
                }
            }
            return response;
        } catch (Throwable th) {
            if (0 != 0) {
                postMethod.releaseConnection();
            }
            throw th;
        }
    }

    public static String sendURLGet(String url, Map<String, Object> params, String enc) {
        String url2 = "" + url;
        String response = "";
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer("");
        if (enc == null || "".equals(enc)) {
            enc = "UTF-8";
        }
        if (strtTotalURL.indexOf("?") == -1) {
            strtTotalURL.append(url2).append("?").append(getUrl(params, enc));
        } else {
            strtTotalURL.append(url2).append("&").append(getUrl(params, enc));
        }
        logger.debug("GET请求URL = \n" + strtTotalURL.toString());
        try {
            try {
                try {
                    GetMethod getMethod2 = new GetMethod(strtTotalURL.toString());
                    getMethod2.setRequestHeader("Content-Type", "application/json;charset=" + enc);
                    int statusCode = client.executeMethod(getMethod2);
                    if (statusCode == 200) {
                        InputStream inputStream = getMethod2.getResponseBodyAsStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer stringBuffer = new StringBuffer();
                        while (true) {
                            String str = br.readLine();
                            if (str == null) {
                                break;
                            }
                            stringBuffer.append(str);
                        }
                        response = stringBuffer.toString();
                    } else {
                        logger.debug("响应状态码 = " + getMethod2.getStatusCode());
                    }
                    if (getMethod2 != null) {
                        getMethod2.releaseConnection();
                    }
                } catch (IOException e) {
                    logger.error("发生网络异常:{}", (Throwable) e);
                    if (0 != 0) {
                        getMethod.releaseConnection();
                    }
                }
            } catch (HttpException e2) {
                logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", (Throwable) e2);
                if (0 != 0) {
                    getMethod.releaseConnection();
                }
            }
            return response;
        } catch (Throwable th) {
            if (0 != 0) {
                getMethod.releaseConnection();
            }
            throw th;
        }
    }

    public static String sendURLGet(String url, Map<String, String> headers, Map<String, Object> params, String enc) {
        String url2 = "" + url;
        String response = "";
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer("");
        if (enc == null || "".equals(enc)) {
            enc = "UTF-8";
        }
        if (strtTotalURL.indexOf("?") == -1) {
            strtTotalURL.append(url2).append("?").append(getUrl(params, enc));
        } else {
            strtTotalURL.append(url2).append("&").append(getUrl(params, enc));
        }
        logger.debug("GET请求URL = \n" + strtTotalURL.toString());
        try {
            try {
                GetMethod getMethod2 = new GetMethod(strtTotalURL.toString());
                getMethod2.setRequestHeader("Content-Type", "application/json;charset=" + enc);
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    getMethod2.setRequestHeader(e.getKey(), e.getValue());
                }
                int statusCode = client.executeMethod(getMethod2);
                if (statusCode == 200) {
                    response = getMethod2.getResponseBodyAsString();
                } else {
                    logger.info("url:{} 响应状态码:{}", url2, Integer.valueOf(getMethod2.getStatusCode()));
                }
                if (getMethod2 != null) {
                    getMethod2.releaseConnection();
                }
            } catch (HttpException e2) {
                logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题 {}", (Throwable) e2);
                if (0 != 0) {
                    getMethod.releaseConnection();
                }
            } catch (IOException e3) {
                logger.error("发生网络异常:{}", (Throwable) e3);
                if (0 != 0) {
                    getMethod.releaseConnection();
                }
            }
            return response;
        } catch (Throwable th) {
            if (0 != 0) {
                getMethod.releaseConnection();
            }
            throw th;
        }
    }

    private static String getUrl(Map<String, Object> map, String valueEnc) throws UnsupportedEncodingException {
        if ("".equals(valueEnc) || valueEnc == null) {
            valueEnc = "UTF-8";
        }
        if (null == map || map.keySet().size() == 0) {
            return "";
        }
        StringBuffer url = new StringBuffer();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (map.containsKey(key)) {
                Object val = map.get(key);
                Object str = val != null ? val : "";
                try {
                    str = URLEncoder.encode(str.toString(), valueEnc);
                } catch (UnsupportedEncodingException e) {
                    logger.error("error:{}", (Throwable) e);
                }
                url.append(key).append(SymbolConstants.EQUAL_SYMBOL).append(str).append("&");
            }
        }
        String strURL = url.toString();
        if ("&".equals("" + strURL.charAt(strURL.length() - 1))) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }
        return strURL;
    }

    public static String getCilentIp(HttpServletRequest request) throws UnknownHostException {
        if (request == null) {
            return null;
        }
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP-CLIENT-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP-X_FORWARDED-FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (NetFlags.LOOPBACK_ADDRESS.equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    if (inet != null) {
                        ipAddress = inet.getHostAddress();
                    }
                } catch (UnknownHostException e) {
                    logger.error("error:{}", (Throwable) e);
                }
            }
        }
        if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }
}
