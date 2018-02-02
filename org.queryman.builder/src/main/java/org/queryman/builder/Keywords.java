/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import org.queryman.builder.token.Keyword;

/**
 * @author Timur Shaidullin
 */
public class Keywords {
    public static final Keyword SELECT = new Keyword("SELECT");

    public static final Keyword FROM = new Keyword("FROM");

    public static final Keyword GROUP_BY = new Keyword("GROUP BY");

    public static final Keyword ORDER_BY = new Keyword("ORDER BY");

    public static final Keyword WHERE = new Keyword("WHERE");

    public static final Keyword LIMIT  = new Keyword("LIMIT");
    public static final Keyword OFFSET = new Keyword("OFFSET");

    public static final Keyword AND     = new Keyword("AND");
    public static final Keyword AND_NOT = new Keyword("AND NOT");
    public static final Keyword OR      = new Keyword("OR");
    public static final Keyword OR_NOT  = new Keyword("OR NOT");
}
