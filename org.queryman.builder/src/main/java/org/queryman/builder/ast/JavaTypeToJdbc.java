/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Expression;
import org.queryman.builder.token.PreparedExpression;
import org.queryman.builder.token.expression.ListExpression;
import org.queryman.builder.token.expression.prepared.ArrayExpression;
import org.queryman.builder.token.expression.prepared.BigDecimalExpression;
import org.queryman.builder.token.expression.prepared.BooleanExpression;
import org.queryman.builder.token.expression.prepared.ByteExpression;
import org.queryman.builder.token.expression.prepared.BytesExpression;
import org.queryman.builder.token.expression.prepared.DateExpression;
import org.queryman.builder.token.expression.prepared.DollarStringExpression;
import org.queryman.builder.token.expression.prepared.DoubleExpression;
import org.queryman.builder.token.expression.prepared.FloatExpression;
import org.queryman.builder.token.expression.prepared.IntegerExpression;
import org.queryman.builder.token.expression.prepared.LongExpression;
import org.queryman.builder.token.expression.prepared.NullExpression;
import org.queryman.builder.token.expression.prepared.ShortExpression;
import org.queryman.builder.token.expression.prepared.StringExpression;
import org.queryman.builder.token.expression.prepared.TimeExpression;
import org.queryman.builder.token.expression.prepared.TimestampExpression;
import org.queryman.builder.utils.ArraysUtils;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Timur Shaidullin
 */
class JavaTypeToJdbc {
    private final Connection connection;
    private final PreparedStatement statement;

    JavaTypeToJdbc(Connection connection, PreparedStatement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    PreparedStatement bind(Map<Integer, PreparedExpression> params) throws SQLException {
        for (Map.Entry<Integer, PreparedExpression> expr : params.entrySet()) {


            if (expr.getValue() instanceof NullExpression)
                statement.setNull(expr.getKey(), Types.NULL);

            if (expr.getValue() instanceof BooleanExpression)
                statement.setBoolean(expr.getKey(), ((BooleanExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof ShortExpression)
                statement.setShort(expr.getKey(), ((ShortExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof ByteExpression)
                statement.setByte(expr.getKey(), ((ByteExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof IntegerExpression)
                statement.setInt(expr.getKey(), ((IntegerExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof LongExpression)
                statement.setLong(expr.getKey(), ((LongExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof FloatExpression)
                statement.setFloat(expr.getKey(), ((FloatExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof DoubleExpression)
                statement.setDouble(expr.getKey(), ((DoubleExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof BigDecimalExpression)
                statement.setBigDecimal(expr.getKey(), ((BigDecimalExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof StringExpression)
                statement.setString(expr.getKey(), ((StringExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof DollarStringExpression)
                statement.setString(expr.getKey(), ((DollarStringExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof DateExpression)
                statement.setDate(expr.getKey(), ((DateExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof TimeExpression)
                statement.setTime(expr.getKey(), ((TimeExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof TimestampExpression)
                statement.setTimestamp(expr.getKey(), ((TimestampExpression) expr.getValue()).getValue());

            if (expr.getValue() instanceof ArrayExpression) {
                ArrayExpression arrayExpression = ((ArrayExpression) expr.getValue());

                Array arr = connection.createArrayOf(
                   String.valueOf(arrayExpression.hashCode()),
                   arrayExpression.getValue()
                );

                statement.setArray(expr.getKey(), arr);
            }

            if (expr.getValue() instanceof BytesExpression) {
                Byte[] bytes = ((BytesExpression) expr.getValue()).getValue();
                statement.setBytes(expr.getKey(), ArraysUtils.toWrapper(bytes));
            }

            if (expr.getValue() instanceof ListExpression) {
                throw new RuntimeException("It's needed to implement");
            }

        }
        // bind parameters
        /*
        setObject
        setRef
        setBlob
        setClob


        setRowId
        setSQLXML
         */

        return statement;
    }
}
