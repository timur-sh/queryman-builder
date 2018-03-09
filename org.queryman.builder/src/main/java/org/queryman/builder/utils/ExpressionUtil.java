/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.utils;

import org.queryman.builder.token.Expression;

import static org.queryman.builder.PostgreSQL.asName;

/**
 * @author Timur Shaidullin
 */
public class ExpressionUtil {
    public static <T>Expression getOrConvert(T field) {
        return field instanceof Expression ? (Expression) field : asName(String.valueOf(field));
    }
}
