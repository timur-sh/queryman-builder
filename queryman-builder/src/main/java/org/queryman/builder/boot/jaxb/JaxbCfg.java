/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Timur Shaidullin
 */
@XmlRootElement(name = "configuration")
public class JaxbCfg {
    @XmlElement(name = "use-uppercase")
    public boolean useUppercase = false;
}
