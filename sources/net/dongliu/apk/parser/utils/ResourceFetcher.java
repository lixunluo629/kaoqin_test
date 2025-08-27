package net.dongliu.apk.parser.utils;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/ResourceFetcher.class */
public class ResourceFetcher {
    private void fetchSystemAttrIds() throws ParserConfigurationException, SAXException, IOException {
        String html = getUrl("https://android.googlesource.com/platform/frameworks/base/+/master/core/res/res/values/public.xml");
        String xml = retrieveCode(html);
        if (xml != null) {
            parseAttributeXml(xml);
        }
    }

    private void parseAttributeXml(String xml) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        final List<Pair<Integer, String>> attrIds = new ArrayList<>();
        DefaultHandler dh = new DefaultHandler() { // from class: net.dongliu.apk.parser.utils.ResourceFetcher.1
            @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException, NumberFormatException {
                String type;
                if (qName.equals("public") && (type = attributes.getValue("type")) != null && type.equals("attr")) {
                    String idStr = attributes.getValue("id");
                    if (idStr == null) {
                        return;
                    }
                    String name = attributes.getValue("name");
                    if (idStr.startsWith("0x")) {
                        idStr = idStr.substring(2);
                    }
                    int id = Integer.parseInt(idStr, 16);
                    attrIds.add(new Pair(Integer.valueOf(id), name));
                }
            }
        };
        parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)), dh);
        for (Pair<Integer, String> pair : attrIds) {
            System.out.println(String.format("%s=%d", pair.getRight(), pair.getLeft()));
        }
    }

    private void fetchSystemStyle() throws IOException {
        String html = getUrl("https://android.googlesource.com/platform/frameworks/base/+/master/api/current.txt");
        String code = retrieveCode(html);
        if (code == null) {
            System.err.println("code area not found");
            return;
        }
        int begin = code.indexOf("R.style");
        int end = code.indexOf("}", begin);
        String styleCode = code.substring(begin, end);
        String[] lines = styleCode.split(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (String str : lines) {
            String line = str.trim();
            if (line.startsWith("field public static final")) {
                System.out.println(Strings.substringBefore(line, ScriptUtils.DEFAULT_STATEMENT_SEPARATOR).replace("deprecated ", "").substring("field public static final int ".length()).replace("_", "."));
            }
        }
    }

    private String getUrl(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        try {
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            byte[] bytes = Inputs.readAll(conn.getInputStream());
            String str = new String(bytes, StandardCharsets.UTF_8);
            conn.disconnect();
            return str;
        } catch (Throwable th) {
            conn.disconnect();
            throw th;
        }
    }

    private String retrieveCode(String html) {
        Matcher matcher = Pattern.compile("<ol class=\"prettyprint\">(.*?)</ol>").matcher(html);
        if (matcher.find()) {
            String codeHtml = matcher.group(1);
            return codeHtml.replace("</li>", ScriptUtils.FALLBACK_STATEMENT_SEPARATOR).replaceAll("<[^>]+>", "").replace("&lt;", "<").replace("&quot;", SymbolConstants.QUOTES_SYMBOL).replace("&gt;", ">");
        }
        return null;
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        ResourceFetcher fetcher = new ResourceFetcher();
        fetcher.fetchSystemAttrIds();
    }
}
