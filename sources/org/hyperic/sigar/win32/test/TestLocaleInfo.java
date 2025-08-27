package org.hyperic.sigar.win32.test;

import org.hyperic.sigar.test.SigarTestCase;
import org.hyperic.sigar.win32.LocaleInfo;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/test/TestLocaleInfo.class */
public class TestLocaleInfo extends SigarTestCase {
    public TestLocaleInfo(String name) {
        super(name);
    }

    private void checkInfo(LocaleInfo info, String match) throws Exception {
        assertGtZeroTrace("id", info.getId());
        assertGtZeroTrace("primary lang", info.getPrimaryLangId());
        assertGtEqZeroTrace("sub lang", info.getSubLangId());
        assertLengthTrace("perflib id", info.getPerflibLangId());
        assertIndexOfTrace(AbstractHtmlElementTag.LANG_ATTRIBUTE, info.toString(), match);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void testInfo() throws Exception {
        Object[] objArr = {new Object[]{new Integer(22), "Portuguese"}, new Object[]{new Integer(LocaleInfo.makeLangId(9, 5)), "New Zealand"}, new Object[]{new Integer(7), "German"}, new Object[]{new Integer(LocaleInfo.makeLangId(10, 20)), "Puerto Rico"}};
        for (int i = 0; i < objArr.length; i++) {
            Integer id = (Integer) objArr[i][0];
            String lang = (String) objArr[i][1];
            LocaleInfo info = new LocaleInfo(id);
            checkInfo(info, lang);
        }
        checkInfo(new LocaleInfo(), "");
    }
}
