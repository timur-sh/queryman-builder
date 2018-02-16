/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.command.from.FromFirstStep;
import org.queryman.builder.command.impl.ConditionsImpl;
import org.queryman.builder.command.impl.FromImpl;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Keyword;
import org.queryman.builder.token.Operator;
import org.queryman.builder.token.expression.ArrayExpression;
import org.queryman.builder.token.expression.ArrayStringExpression;
import org.queryman.builder.token.expression.ColumnReferenceExpression;
import org.queryman.builder.token.expression.ConstantExpression;
import org.queryman.builder.token.expression.DollarStringExpression;
import org.queryman.builder.token.expression.FuncExpression;
import org.queryman.builder.token.expression.ListExpression;
import org.queryman.builder.token.expression.ListStringExpression;
import org.queryman.builder.token.expression.StringExpression;

import java.util.List;

import static org.queryman.builder.ast.NodesMetadata.ALL;
import static org.queryman.builder.ast.NodesMetadata.ANY;
import static org.queryman.builder.ast.NodesMetadata.EXISTS;
import static org.queryman.builder.ast.NodesMetadata.SOME;

/**
 * @author Timur Shaidullin
 */
public class PostgreSQL {
    public static Operator operator(String operator) {
        return new Operator(operator);
    }

    //----
    // COMMON CONDITIONS
    //----

    public static Conditions condition(String leftValue, String operator, String rightValue) {
        return condition(asName(leftValue), Operators.map(operator), asConstant(rightValue));
    }

    public static Conditions condition(Expression leftValue, String operator, Expression rightValue) {
        return condition(leftValue, Operators.map(operator), rightValue);
    }

    public static Conditions condition(Expression leftValue, Operator operator, Expression rightValue) {
        return new ConditionsImpl(leftValue, new NodeMetadata(operator), rightValue);
    }

    //----
    // BETWEEN CONDITIONS
    //----

    /**
     * WHERE .. BETWEEN .. AND .. condition.
     */
    public static Conditions between(Expression field, Expression value1, Expression value2) {
        return new ConditionsImpl(NodesMetadata.BETWEEN, field, condition(value1, operator("AND"), value2));
    }

    /**
     * WHERE .. BETWEEN .. AND .. condition.
     */
    public static Conditions between(String field, String value1, String value2) {
        return between(asName(field), asName(value1), asName(value2));
    }

    public static Keyword keyword(String keyword) {
        return new Keyword(keyword);
    }

    //----
    // SUBQUERY CONDITIONS
    //----

    public static Conditions conditionExists(Query query) {
        return new ConditionsImpl(EXISTS, query);
    }

    public static Conditions conditionSome(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(SOME, query));
    }

    public static Conditions conditionSome(String field, String operator, Query query) {
        return conditionSome(asName(field), operator(operator), query);
    }

    public static Conditions conditionAny(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(ANY, query));
    }

    public static Conditions conditionAny(String field, String operator, Query query) {
        return conditionAny(asName(field), operator(operator), query);
    }

    public static Conditions conditionAll(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(ALL, query));
    }

    public static Conditions conditionAll(String field, String operator, Query query) {
        return conditionAll(asName(field), operator(operator), query);
    }

    /**
     * Subquery condition. It is used primarily by {@code IN} expression:
     *
     * Example:
     * name IN (select name from authors)
     * condition(asName("name"), Operators.IN, select("name").from("authors"))
     */
    public static Conditions condition(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(field, new NodeMetadata(operator), query);
    }

    /**
     * Subquery condition. It is used primarily by {@code IN} expression:
     *
     * Example:
     * name IN (select name from authors)
     * condition("name", "IN", select("name").from("authors"))
     */
    public static Conditions condition(String field, String operator, Query query) {
        return condition(asName(field), Operators.map(operator), query);
    }

    //----
    // FROM
    //----

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName - is a table name.
     *
     * @see #from(Expression)
     */
    public static FromFirstStep from(String tableName) {
        return from(asName(tableName));
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName - is a table name
     *
     * @see #from(String)
     */
    public static FromFirstStep from(Expression tableName) {
        return new FromImpl(tableName);
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName - is a table name
     *
     * @see #fromOnly(Expression)
     */
    public static FromFirstStep fromOnly(String tableName) {
        return fromOnly(asName(tableName));
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName - is a table name
     *
     * @see #fromOnly(String)
     */
    public static FromFirstStep fromOnly(Expression tableName) {
        return new FromImpl(tableName, true);
    }

    //----
    // COMMON EXPRESSIONS
    //----

    /**
     * @return a constant. e.g. 1, id, ARRAY[1] ...
     */
    public static Expression asConstant(String constant) {
        return new ConstantExpression(constant);
    }

    /**
     * @return a string surrounded by single quote string. e.g. 'string'
     */
    public static Expression asString(String constant) {
        return new StringExpression(constant);
    }

    /**
     * @return a string surrounded by dollar singes string. e.g. $$string$$
     */
    public static Expression asDollarString(String constant) {
        return new DollarStringExpression(constant, "");
    }


    /**
     * @return a string surrounded by dollar singes string. e.g. $tag$string$tag$
     *
     * where {@code tag} is {@code tagName}
     */
    public static Expression asDollarString(String constant, String tagName) {
        return new DollarStringExpression(constant, tagName);
    }

    /**
     * It is synonym of {@link #asConstant(String)}
     *
     * @return number.
     */
    public static Expression asNumber(Number constant) {
        return new ConstantExpression(constant);
    }

    /**
     * @return a quoted name. e.g. id; table.phone
     */
    public static Expression asName(String constant) {
        return new ColumnReferenceExpression(constant);
    }

    /**
     * @return a quoted name. e.g. "id"; "table"."phone"
     */
    public static Expression asQuotedName(String constant) {
        return new ColumnReferenceExpression(constant, true);
    }

    //----
    // LIST EXPRESSIONS
    //----

    /**
     * @param constants - values of list
     * @return (...), where {@code ...} is concatenated string of values by comma.
     */
    @SafeVarargs
    public static <T> Expression asStringList(T... constants) {
        return new ListStringExpression<T>(constants);
    }

    /**
     * @return (...), where {@code ...} is concatenated string of values by comma.
     *
     * @see #asStringList(T...)
     */
    public static <T> Expression asStringList(List<T> constants) {
        return asStringList(constants.toArray());
    }

    /**
     * @param constants - values of list
     * @return (...), where {@code ...} is concatenated string of values by comma.
     */
    @SafeVarargs
    public static <T> Expression asList(T... constants) {
        return new ListExpression<T>(constants);
    }

    /**
     * @return (...), where {@code ...} is concatenated string of values by comma.
     *
     * @see #asList(T...)
     */
    public static <T> Expression asList(List<T> constants) {
        return asList(constants.toArray());
    }

    //----
    // ARRAY EXPRESSIONS
    //----

    /**
     * @param arr - values of array
     * @return ARRAY[...], where {@code ...} is concatenated string of values by comma.
     */
    @SafeVarargs
    public static <T> Expression asArray(T... arr) {
        return new ArrayExpression<T>(arr);
    }

    /**
     * @return ARRAY[...], where {@code ...} is concatenated string of values by comma.
     *
     * @see #asArray(T...)
     */
    public static <T> Expression asArray(List<T> arr) {
        return asArray(arr.toArray());
    }

    /**
     * @param arr - string values of array
     * @return ARRAY[...], where {@code ...} is concatenated string of values by comma.
     */
    @SafeVarargs
    public static <T> Expression asStringArray(T... arr) {
        return new ArrayStringExpression<T>(arr);
    }

    /**
     * @return ARRAY[...], where {@code ...} is concatenated string of values by comma.
     *
     * @see #asStringArray(T...)
     */
    public static <T> Expression asStringArray(List<T> arr) {
        return asStringArray(arr.toArray());
    }

    //----
    // FUNCTION EXPRESSIONS
    //----

    /**
     * Examples:
     * <code>
     *     asFunc("ALL", asArray(List.of(1, 2)));  // ALL(ARRAY[1, 2])
     *     asFunc("SOME", asList(1, 2)); // SOME(1, 2)
     * </code>
     *
     * @param name - function or operator name, examples: <code>ALL, ANY ...</code>
     * @param expression - list or array expression
     * @return a combine of {@code name} and {@code expression} objects
     *
     * @see #asArray(Object[])
     * @see #asStringArray(Object[])
     * @see #asList(Object[])
     * @see #asStringList(List)
     */
    public static Expression asFunc(String name, Expression expression) {
        return new FuncExpression(name, expression);
    }
}
