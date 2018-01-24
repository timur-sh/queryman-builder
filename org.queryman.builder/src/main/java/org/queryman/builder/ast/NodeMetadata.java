/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * This class contains node names. Usually node name is sequence of keywords.
 *
 * @author Timur Shaidullin
 */
public final class NodeMetadata {
    public static final String SELECT             = "select";
    public static final String SELECT_DISTINCT    = "select distinct";
    public static final String SELECT_DISTINCT_ON = "select distinct on";

    public static final String FROM = "from";

    public static final String WHERE = "where";
}
