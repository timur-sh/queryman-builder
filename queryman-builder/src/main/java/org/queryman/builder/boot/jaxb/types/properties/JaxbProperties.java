/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.jaxb.types.properties;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Timur Shaidullin
 */
public class JaxbProperties {
    @XmlElement(name = "property")
    public JaxbProperty[] properties;
}
