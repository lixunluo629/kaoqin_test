package org.springframework.web.servlet.view.feed;

import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/feed/AbstractAtomFeedView.class */
public abstract class AbstractAtomFeedView extends AbstractFeedView<Feed> {
    public static final String DEFAULT_FEED_TYPE = "atom_1.0";
    private String feedType = DEFAULT_FEED_TYPE;

    protected abstract List<Entry> buildFeedEntries(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    @Override // org.springframework.web.servlet.view.feed.AbstractFeedView
    protected /* bridge */ /* synthetic */ void buildFeedEntries(Map map, WireFeed wireFeed, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        buildFeedEntries((Map<String, Object>) map, (Feed) wireFeed, httpServletRequest, httpServletResponse);
    }

    public AbstractAtomFeedView() {
        setContentType("application/atom+xml");
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.web.servlet.view.feed.AbstractFeedView
    /* renamed from: newFeed, reason: merged with bridge method [inline-methods] */
    public Feed mo8210newFeed() {
        return new Feed(this.feedType);
    }

    protected final void buildFeedEntries(Map<String, Object> model, Feed feed, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Entry> entries = buildFeedEntries(model, request, response);
        feed.setEntries(entries);
    }
}
