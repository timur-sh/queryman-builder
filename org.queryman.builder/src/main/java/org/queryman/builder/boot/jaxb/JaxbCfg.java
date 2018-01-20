/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.jaxb;

import org.queryman.builder.boot.jaxb.adapters.JaxbPropertiesAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
@XmlRootElement(name = "configuration")
public class JaxbCfg {
    @XmlElement(name = "properties")
    @XmlJavaTypeAdapter(value = JaxbPropertiesAdapter.class)
    public Properties properties;
}
