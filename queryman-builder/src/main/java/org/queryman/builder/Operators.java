/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import org.queryman.builder.token.Operator;

import static org.queryman.builder.Queryman.operator;

/**
 * Collection of operators which can be used in different conditions.
 *
 * @see org.queryman.builder.command.Conditions
 *
 * @author Timur Shaidullin
 */
public class Operators {
    public static final Operator AND     = operator("AND");
    public static final Operator NOT     = operator("NOT");
    public static final Operator AND_NOT = operator("AND NOT");
    public static final Operator OR      = operator("OR");
    public static final Operator OR_NOT  = operator("OR NOT");

    public static final Operator BETWEEN     = operator("BETWEEN");
    public static final Operator NOT_BETWEEN = operator("NOT BETWEEN");

    public static final Operator LIKE     = operator("LIKE");
    public static final Operator NOT_LIKE = operator("NOT LIKE");

    public static final Operator ILIKE     = operator("ILIKE");
    public static final Operator NOT_ILIKE = operator("NOT ILIKE");

    public static final Operator SIMILAR_TO = operator("SIMILAR TO");
    public static final Operator NOT_SIMILAR_TO     = operator("NOT SIMILAR TO");

    public static final Operator IS_DISTINCT_FROM     = operator("IS DISTINCT FROM");
    public static final Operator IS_NOT_DISTINCT_FROM = operator("IS NOT DISTINCT FROM");

    public static final Operator BETWEEN_SYMMETRIC     = operator("BETWEEN SYMMETRIC");
    public static final Operator NOT_BETWEEN_SYMMETRIC = operator("NOT BETWEEN SYMMETRIC");

    public static final Operator IS     = operator("IS");
    public static final Operator IS_NOT = operator("IS NOT");

    public static final Operator IN     = operator("IN");
    public static final Operator NOT_IN = operator("NOT IN");

    public static final Operator ISNULL     = operator("ISNULL");
    public static final Operator NOTNULL = operator("NOTNULL");

    public static final Operator LT    = operator("<");
    public static final Operator LTE   = operator("<=");
    public static final Operator GT    = operator(">");
    public static final Operator GTE   = operator(">=");
    public static final Operator EQUAL = operator("=");
    public static final Operator NE    = operator("!=");
    public static final Operator NE2   = operator("<>");
}
