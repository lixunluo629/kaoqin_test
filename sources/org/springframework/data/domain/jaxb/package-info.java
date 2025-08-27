
@XmlSchema(xmlns = {@XmlNs(prefix = "sd", namespaceURI = SpringDataJaxb.NAMESPACE)})
@XmlJavaTypeAdapters({@XmlJavaTypeAdapter(value = PageableAdapter.class, type = Pageable.class), @XmlJavaTypeAdapter(value = SortAdapter.class, type = Sort.class), @XmlJavaTypeAdapter(value = PageAdapter.class, type = Page.class)})
package org.springframework.data.domain.jaxb;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

