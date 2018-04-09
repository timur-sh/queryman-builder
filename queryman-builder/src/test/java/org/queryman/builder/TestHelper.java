/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.TreeFormatterTestUtil;
import org.queryman.builder.ast.TreeFormatterUtil;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.PreparedExpression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Timur Shaidullin
 */
public class TestHelper {
    public static void testBindParameters(Expression expression, Procedure<Map<Integer, PreparedExpression>> executable) {
        if (!(expression instanceof PreparedExpression)) {
            throw new IllegalArgumentException("Must be tested only prepared expressions");
        }

        Map<Integer, PreparedExpression> map = new HashMap<>();
        ((PreparedExpression) expression).bind(map);


        executable.execute(map);
    }

    public static void testBindParameters(Conditions conditions, Procedure<Map<Integer, PreparedExpression>> executable) {
        Map<Integer, PreparedExpression> map = TreeFormatterTestUtil.buildPreparedParameters(conditions);

        executable.execute(map);
    }

    public static void testBindParameters(Query query, Procedure<Map<Integer, PreparedExpression>> executable) {
        Map<Integer, PreparedExpression> map = TreeFormatterUtil.buildPreparedParameters(query);

        executable.execute(map);
    }
}
