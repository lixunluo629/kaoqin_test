package org.hyperic.sigar.pager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/Pager.class */
public class Pager {
    public static final String DEFAULT_PROCESSOR_CLASSNAME = "org.hyperic.sigar.pager.DefaultPagerProcessor";
    private static Map PagerProcessorMap = Collections.synchronizedMap(new HashMap());
    private PagerProcessor processor;
    private boolean skipNulls;
    private PagerEventHandler eventHandler;

    private Pager(PagerProcessor processor) {
        this.processor = null;
        this.skipNulls = false;
        this.eventHandler = null;
        this.processor = processor;
        this.skipNulls = false;
        this.eventHandler = null;
        if (this.processor instanceof PagerProcessorExt) {
            this.skipNulls = ((PagerProcessorExt) this.processor).skipNulls();
            this.eventHandler = ((PagerProcessorExt) this.processor).getEventHandler();
        }
    }

    public static Pager getDefaultPager() {
        try {
            return getPager(DEFAULT_PROCESSOR_CLASSNAME);
        } catch (Exception e) {
            throw new IllegalStateException(new StringBuffer().append("This should never happen: ").append(e).toString());
        }
    }

    public static Pager getPager(String pageProcessorClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Pager p = (Pager) PagerProcessorMap.get(pageProcessorClassName);
        if (p == null) {
            PagerProcessor processor = (PagerProcessor) Class.forName(pageProcessorClassName).newInstance();
            p = new Pager(processor);
            PagerProcessorMap.put(pageProcessorClassName, p);
        }
        return p;
    }

    public PageList seek(Collection source, int pagenum, int pagesize) {
        return seek(source, pagenum, pagesize, (Object) null);
    }

    public PageList seek(Collection source, PageControl pc) {
        if (pc == null) {
            pc = new PageControl();
        }
        return seek(source, pc.getPagenum(), pc.getPagesize(), (Object) null);
    }

    public PageList seek(Collection source, int pagenum, int pagesize, Object procData) {
        PageList dest = new PageList();
        seek(source, dest, pagenum, pagesize, procData);
        dest.setTotalSize(source.size());
        return dest;
    }

    public void seek(Collection source, Collection dest, int pagenum, int pagesize) {
        seek(source, dest, pagenum, pagesize, null);
    }

    public void seek(Collection source, Collection dest, int pagenum, int pagesize, Object procData) {
        Object elt;
        Iterator iter = source.iterator();
        if (pagesize == -1 || pagenum == -1) {
            pagenum = 0;
            pagesize = Integer.MAX_VALUE;
        }
        int i = 0;
        int i2 = 0;
        while (true) {
            int currentPage = i2;
            if (!iter.hasNext() || currentPage >= pagenum) {
                break;
            }
            iter.next();
            i++;
            i2 = currentPage + (i % pagesize == 0 ? 1 : 0);
        }
        if (this.eventHandler != null) {
            this.eventHandler.init();
        }
        if (this.skipNulls) {
            while (iter.hasNext() && dest.size() < pagesize) {
                if (this.processor instanceof PagerProcessorExt) {
                    elt = ((PagerProcessorExt) this.processor).processElement(iter.next(), procData);
                } else {
                    elt = this.processor.processElement(iter.next());
                }
                if (elt != null) {
                    dest.add(elt);
                }
            }
        } else {
            while (iter.hasNext() && dest.size() < pagesize) {
                dest.add(this.processor.processElement(iter.next()));
            }
        }
        if (this.eventHandler != null) {
            this.eventHandler.cleanup();
        }
    }

    public PageList seekAll(Collection source, int pagenum, int pagesize, Object procData) {
        PageList dest = new PageList();
        seekAll(source, dest, pagenum, pagesize, procData);
        dest.setTotalSize(source.size());
        return dest;
    }

    public void seekAll(Collection source, Collection dest, int pagenum, int pagesize, Object procData) {
        Object elt;
        Iterator iter = source.iterator();
        if (pagesize == -1 || pagenum == -1) {
            pagenum = 0;
            pagesize = Integer.MAX_VALUE;
        }
        int i = 0;
        int i2 = 0;
        while (true) {
            int currentPage = i2;
            if (!iter.hasNext() || currentPage >= pagenum) {
                break;
            }
            if (this.processor instanceof PagerProcessorExt) {
                Object ret = ((PagerProcessorExt) this.processor).processElement(iter.next(), procData);
                if (ret != null) {
                    i++;
                }
            } else {
                this.processor.processElement(iter.next());
                i++;
            }
            i2 = currentPage + ((i == 0 || i % pagesize != 0) ? 0 : 1);
        }
        if (this.eventHandler != null) {
            this.eventHandler.init();
        }
        if (this.skipNulls) {
            while (iter.hasNext()) {
                if (this.processor instanceof PagerProcessorExt) {
                    elt = ((PagerProcessorExt) this.processor).processElement(iter.next(), procData);
                } else {
                    elt = this.processor.processElement(iter.next());
                }
                if (elt != null && dest.size() < pagesize) {
                    dest.add(elt);
                }
            }
        } else {
            while (iter.hasNext()) {
                Object elt2 = this.processor.processElement(iter.next());
                if (dest.size() < pagesize) {
                    dest.add(elt2);
                }
            }
        }
        if (this.eventHandler != null) {
            this.eventHandler.cleanup();
        }
    }

    public PageList processAll(PageList source) {
        PageList dest = new PageList();
        Iterator it = source.iterator();
        while (it.hasNext()) {
            Object elt = this.processor.processElement(it.next());
            if (elt != null) {
                dest.add(elt);
            }
        }
        dest.setTotalSize(source.getTotalSize());
        return dest;
    }
}
