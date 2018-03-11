/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.utils;

import org.queryman.builder.Query;
import org.queryman.builder.Queryman;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.expression.SubQueryExpression;

import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asName;

/**
 * @author Timur Shaidullin
 */
public class ExpressionUtil {
    /**
     * Returns an expression. Checking scenario is:
     * <ul>
     *     <li>if field is an {@link Expression} object return itself </li>
     *     <li>if field is a {@link Query} object, returns a {@link SubQueryExpression}</li>
     *     <li>if field is not a {@link String} object,
     *      the {@link Queryman#asConstant(Object)} is applicable
     *     </li>
     *     <li>returns a {@link org.queryman.builder.token.expression.ColumnReferenceExpression}</li>
     * </ul>
     *
     * @param field
     * @return expression
     */
    public static <T>Expression toExpression(T field) {
        if (field instanceof Expression)
            return (Expression) field;

        if (field instanceof Query)
            return new SubQueryExpression((Query) field);

        if (!(field instanceof String))
            return asConstant(field);

        return asName(String.valueOf(field));
    }
}
