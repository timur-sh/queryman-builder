/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import static org.queryman.builder.ast.NodeUtil.node;

/**
 * @author Timur Shaidullin
 */
public final class Nodes {
    public static final String SELECT             = "select";
    public static final String SELECT_DISTINCT    = "select distinct";
    public static final String SELECT_DISTINCT_ON = "select distinct on";
}
