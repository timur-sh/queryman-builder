/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import org.queryman.builder.token.Operator;

import java.util.Map;

import static java.util.Map.entry;
import static org.queryman.builder.PostgreSQL.operator;

/**
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

    public static final Operator IS_DISTINCT_FROM     = operator("IS DISTINCT FROM");
    public static final Operator IS_NOT_DISTINCT_FROM = operator("IS NOT DISTINCT FROM");

    public static final Operator BETWEEN_SYMMETRIC     = operator("BETWEEN SYMMETRIC");
    public static final Operator NOT_BETWEEN_SYMMETRIC = operator("NOT BETWEEN SYMMETRIC");

    public static final Operator IS     = operator("IS");
    public static final Operator IS_NOT = operator("IS NOT");

    public static final Operator ISNULL     = operator("ISNULL");
    public static final Operator NOTNULL = operator("NOTNULL");

    public static final Operator LT    = operator("<");
    public static final Operator LTE   = operator("<=");
    public static final Operator GT    = operator(">");
    public static final Operator GTE   = operator(">=");
    public static final Operator EQUAL = operator("=");
    public static final Operator NE    = operator("!=");
    public static final Operator NE2   = operator("<>");

    public static final Operator map(String operator) {
        Map<String, Operator> operators = Map.ofEntries(
           entry(AND.getName(), AND),
           entry(NOT.getName(), NOT),
           entry(AND_NOT.getName(), AND_NOT),
           entry(OR.getName(), OR),
           entry(OR_NOT.getName(), OR_NOT),

           entry(LIKE.getName(), LIKE),
           entry(NOT_LIKE.getName(), NOT_LIKE),
           entry(ILIKE.getName(), ILIKE),
           entry(NOT_ILIKE.getName(), NOT_ILIKE),

           entry(IS.getName(), IS),
           entry(IS_NOT.getName(), IS_NOT),
           entry(ISNULL.getName(), ISNULL),
           entry(NOTNULL.getName(), NOTNULL),

           entry(BETWEEN.getName(), BETWEEN),
           entry(NOT_BETWEEN.getName(), NOT_BETWEEN),
           entry(IS_DISTINCT_FROM.getName(), IS_DISTINCT_FROM),
           entry(IS_NOT_DISTINCT_FROM.getName(), IS_NOT_DISTINCT_FROM),
           entry(BETWEEN_SYMMETRIC.getName(), BETWEEN_SYMMETRIC),
           entry(NOT_BETWEEN_SYMMETRIC.getName(), NOT_BETWEEN_SYMMETRIC),

           entry(LT.getName(), LT),
           entry(LTE.getName(), LTE),
           entry(GT.getName(), GT),
           entry(EQUAL.getName(), EQUAL),
           entry(NE.getName(), NE),
           entry(NE2.getName(), NE2)
        );

        return operators.getOrDefault(operator.toUpperCase(), operator(operator));
    }
}
