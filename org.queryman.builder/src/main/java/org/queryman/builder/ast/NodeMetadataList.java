/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * @author Timur Shaidullin
 */
public final class NodeMetadataList {
    public static final NodeMetadata SELECT_DISTINCT    = new NodeMetadata("select distinct");
    public static final NodeMetadata SELECT_DISTINCT_ON = new NodeMetadata("select distinct on");
}
