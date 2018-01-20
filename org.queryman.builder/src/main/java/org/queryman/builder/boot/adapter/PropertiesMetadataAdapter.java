/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.adapter;

import org.queryman.builder.Metadata;

import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class PropertiesMetadataAdapter implements MetadataAdapter {
    public PropertiesMetadataAdapter(Properties properties) {

    }

    @Override
    public Metadata convert() {
        return null;
    }
}
