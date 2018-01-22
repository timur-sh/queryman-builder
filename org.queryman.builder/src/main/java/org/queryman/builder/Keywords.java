/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.token.Keyword;

import static org.queryman.builder.Tokens.keyword;

/**
 * @author Timur Shaidullin
 */
public final class Keywords {
    public static final Keyword SELECT   = keyword("select");
    public static final Keyword DISTINCT = keyword("distinct");
    public static final Keyword ON       = keyword("on");
    public static final Keyword FROM     = keyword("from");
    public static final Keyword ALL      = keyword("all");
    public static final Keyword WHERE    = keyword("where");
    public static final Keyword GROUP_BY = keyword("group by");
    public static final Keyword HAVING   = keyword("having");
}
