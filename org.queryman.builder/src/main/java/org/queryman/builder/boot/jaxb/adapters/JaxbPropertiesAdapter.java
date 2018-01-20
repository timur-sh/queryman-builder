/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.jaxb.adapters;

import org.queryman.builder.boot.jaxb.types.properties.JaxbProperties;
import org.queryman.builder.boot.jaxb.types.properties.JaxbProperty;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class JaxbPropertiesAdapter extends XmlAdapter<JaxbProperties, Properties> {
    @Override
    public Properties unmarshal(JaxbProperties v) throws Exception {
        Properties properties = new Properties();

        for (JaxbProperty property : v.properties)
            properties.put(property.name, property.value);

        return properties;
    }

    @Override
    public JaxbProperties marshal(Properties v) throws Exception {
        return null;
    }
}
