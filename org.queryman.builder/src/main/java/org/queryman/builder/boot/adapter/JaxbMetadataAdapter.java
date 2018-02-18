/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.adapter;

import org.queryman.builder.boot.jaxb.JaxbCfg;
import org.queryman.builder.cfg.Settings;

import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public final class JaxbMetadataAdapter {
    public static Properties convert(JaxbCfg jaxbCfg) {
        Properties properties = new Properties();
        properties.setProperty(Settings.USE_UPPERCASE, String.valueOf(jaxbCfg.useUppercase));

        return properties;
    }
}
