/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.jaxb.types.properties;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Timur Shaidullin
 */
public class JaxbProperty {
    @XmlAttribute
    public String name;

    @XmlAttribute
    public String value;
}
