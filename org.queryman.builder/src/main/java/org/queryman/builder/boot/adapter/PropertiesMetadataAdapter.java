/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.adapter;

import org.queryman.builder.cfg.Settings;

import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public final class PropertiesMetadataAdapter {
    public static Properties convert(Properties properties) {
        Properties properties1 = new Properties();

        if (properties.containsKey(Settings.USE_UPPERCASE))
            properties1.setProperty(Settings.USE_UPPERCASE, properties.getProperty(Settings.USE_UPPERCASE));

        return properties1;
    }
}
