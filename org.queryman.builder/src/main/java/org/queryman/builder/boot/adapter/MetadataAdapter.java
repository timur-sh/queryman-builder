/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.adapter;

import org.queryman.builder.Metadata;

/**
 * @author Timur Shaidullin
 */
public interface MetadataAdapter {
    public Metadata convert();
}
