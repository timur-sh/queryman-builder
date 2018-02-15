/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.queryman.builder.utils.Tools.EMPTY_STRING;

/**
 * Represent a qualified name. Usual it is an either column or table reference.
 * <p>
 * <code>
 * public.books
 * "public"."books"
 * </code>
 *
 *
 *
 * @author Timur Shaidullin
 */
public class ColumnReferenceExpression extends Expression {
    /**
     * Whether use quote or not.
     */
    private boolean quoted = false;

    public ColumnReferenceExpression(String constant) {
        super(constant);
    }

    public ColumnReferenceExpression(String constant, boolean quoted) {
        super(constant);
        this.quoted = quoted;
    }

    /**
     * @return a qualified name. e.g. table.column
     */
    @Override
    protected String prepareName() {
        if (isEmpty()) {
            return null;
        }

        String[] parts = name.split("\\.");

        if (quoted) {
            List<String> collect = Arrays.stream(parts)
               .map(part -> "\"" + part + "\"")
               .collect(Collectors.toList());

            return String.join(".", collect.toArray(EMPTY_STRING));
        }

        return String.join(".", parts);
    }
}
