/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.adapter;

import org.queryman.builder.Metadata;
import org.queryman.builder.boot.jaxb.JaxbCfg;

/**
 * @author Timur Shaidullin
 */
public class JaxbMetadataAdapter implements MetadataAdapter {
    public JaxbMetadataAdapter(JaxbCfg cfg) {

    }

    @Override
    public Metadata convert() {
        return null;
    }
}
