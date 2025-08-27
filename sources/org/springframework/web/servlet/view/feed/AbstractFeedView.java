package org.springframework.web.servlet.view.feed;

import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.io.WireFeedOutput;
import java.io.OutputStreamWriter;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/feed/AbstractFeedView.class */
public abstract class AbstractFeedView<T extends WireFeed> extends AbstractView {
    /* renamed from: newFeed */
    protected abstract T mo8210newFeed();

    protected abstract void buildFeedEntries(Map<String, Object> map, T t, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    @Override // org.springframework.web.servlet.view.AbstractView
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WireFeed wireFeedMo8210newFeed = mo8210newFeed();
        buildFeedMetadata(model, wireFeedMo8210newFeed, request);
        buildFeedEntries(model, wireFeedMo8210newFeed, request, response);
        setResponseContentType(request, response);
        if (!StringUtils.hasText(wireFeedMo8210newFeed.getEncoding())) {
            wireFeedMo8210newFeed.setEncoding("UTF-8");
        }
        WireFeedOutput feedOutput = new WireFeedOutput();
        ServletOutputStream out = response.getOutputStream();
        feedOutput.output(wireFeedMo8210newFeed, new OutputStreamWriter(out, wireFeedMo8210newFeed.getEncoding()));
        out.flush();
    }

    protected void buildFeedMetadata(Map<String, Object> model, T feed, HttpServletRequest request) {
    }
}
