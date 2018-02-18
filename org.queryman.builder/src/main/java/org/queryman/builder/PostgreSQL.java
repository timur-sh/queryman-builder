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
import org.queryman.builder.command.select.SelectFromStep;
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
 * Entry point of all parts of PostgreSQL. This contains a full collection of
 * SQL statements like the SELECT, UPDATE, DELETE.
 * Also other parts of SQL clauses are incapsulate here - conditions clause
 * (AND, OR, OR NOT, etc), FROM clause, any expressions,
 *
 * @author Timur Shaidullin
 */
public class PostgreSQL {
    //----
    // SELECT API
    //----

    public static SelectFromStep select(String... columns) {
        return null;
    }

    /**
     * Create an operator which ordinarily is used by condition.
     * @param operator LIKE, ILIKE, =, !=, @> etc.
     *
     * Most useful operators are collected there:
     * @see Operators
     */
    public static Operator operator(String operator) {
        return new Operator(operator);
    }

    /**
     * Create a keyword which ordinarily is used to build SQL query.
     * @param keyword SELECT, UPDATE, FROM, JOIN etc.
     *
     * Most useful keywords are collected there:
     * @see Operators
     */
    public static Keyword keyword(String keyword) {
        return new Keyword(keyword);
    }

    //----
    // COMMON CONDITIONS
    //----

    /**
     * Create a condition
     * @param leftValue left operand
     * @param operator operator
     * @param rightValue right operand
     *
     * @see #operator(String)
     * @see #asName(String)
     * @see #asName(String)
     * @see #condition(Expression, Operator, Expression)
     */
    public static Conditions condition(String leftValue, String operator, String rightValue) {
        return condition(asName(leftValue), Operators.map(operator), asName(rightValue));
    }

    /**
     * Create a condition

     * @param leftValue left operand
     * @param operator operator
     * @param rightValue right operand
     *
     * @see #operator(String)
     * @see #condition(Expression, Operator, Expression)
     */
    public static Conditions condition(Expression leftValue, String operator, Expression rightValue) {
        return condition(leftValue, Operators.map(operator), rightValue);
    }

    /**
     * Create a condition
     *
     * @param leftValue left operand
     * @param operator operator
     * @param rightValue right operand
     *
     * @see #operator(String)
     */
    public static Conditions condition(Expression leftValue, Operator operator, Expression rightValue) {
        return new ConditionsImpl(leftValue, new NodeMetadata(operator), rightValue);
    }

    //----
    // BETWEEN CONDITIONS
    //----

    /**
     * Create a condition: field BETWEEN value1 AND value2.
     *
     * @param field
     * @param value1
     * @param value2
     *
     * @see #conditionBetween(String, String, String)
     */
    public static Conditions conditionBetween(Expression field, Expression value1, Expression value2) {
        return new ConditionsImpl(NodesMetadata.BETWEEN, field, condition(value1, operator("AND"), value2));
    }

    /**
     * Create a condition: field BETWEEN value1 AND value2.
     *
     * @param field
     * @param value1
     * @param value2
     *
     * @see #conditionBetween(Expression, Expression, Expression)
     */
    public static Conditions conditionBetween(String field, String value1, String value2) {
        return conditionBetween(asName(field), asName(value1), asName(value2));
    }

    //----
    // SUBQUERY CONDITIONS
    //----

    /**
     * Subquery condition. Primarily it is used by {@code IN} expression:
     *
     * Example:
     * <code>
     *  condition(asName("name"), IN, select("name").from("authors")); // name IN (select name from authors)
     * </code>
     *
     * @param field
     * @param operator
     * @param query
     *
     * @see #select(String...)
     * @see Operators#IN
     */
    public static Conditions condition(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(field, new NodeMetadata(operator), query);
    }

    /**
     * Subquery condition. Primarily it is used by {@code IN} expression:
     *
     * Example:
     * <code>
     *  condition("name", "IN", select("name").from("authors")); // name IN (select name from authors)
     * </code>
     *
     * @param field
     * @param operator
     * @param query
     *
     * @see #select(String...)
     */
    public static Conditions condition(String field, String operator, Query query) {
        return condition(asName(field), Operators.map(operator), query);
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     *     conditionExists(select(1, 2)); // EXISTS (SELECT 1, 2);
     * </code>
     *
     * @param query subquery
     */
    public static Conditions conditionExists(Query query) {
        return new ConditionsImpl(EXISTS, query);
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     *     conditionSome(asName("id"), operator("="), select(1, 2)); // id = SOME (SELECT 1, 2);
     * </code>
     *
     * @param field
     * @param operator
     * @param query subquery
     */
    public static Conditions conditionSome(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(SOME, query));
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     *     conditionSome("id", "=", select(1, 2)); // id = SOME (SELECT 1, 2);
     * </code>
     *
     * @param field
     * @param operator
     * @param query subquery
     *
     * @see #conditionSome(Expression, Operator, Query)
     */
    public static Conditions conditionSome(String field, String operator, Query query) {
        return conditionSome(asName(field), operator(operator), query);
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     *     conditionAny("id", "=", select(1, 2)); // id = ANY (SELECT 1, 2);
     * </code>
     *
     * @param field
     * @param operator
     * @param query subquery
     *
     */
    public static Conditions conditionAny(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(ANY, query));
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     *     conditionAny("id", "=", select(1, 2)); // id = ANY (SELECT 1, 2);
     * </code>
     *
     * @param field
     * @param operator
     * @param query subquery
     *
     * @see #conditionAny(Expression, Operator, Query)
     */
    public static Conditions conditionAny(String field, String operator, Query query) {
        return conditionAny(asName(field), operator(operator), query);
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     *     conditionAll("id", "=", select(1, 2)); // id = ALL (SELECT 1, 2);
     * </code>
     *
     * @param field
     * @param operator
     * @param query subquery
     *
     * @see #conditionAny(Expression, Operator, Query)
     */
    public static Conditions conditionAll(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(ALL, query));
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     *     conditionAll("id", "=", select(1, 2)); // id = ALL (SELECT 1, 2);
     * </code>
     *
     * @param field
     * @param operator
     * @param query subquery
     *
     * @see #conditionAll(Expression, Operator, Query)
     */
    public static Conditions conditionAll(String field, String operator, Query query) {
        return conditionAll(asName(field), operator(operator), query);
    }

    //----
    // FROM
    //----

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName is a table name.
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
     * @param tableName is a table name
     *
     * @see #fromOnly(Expression)
     */
    public static FromFirstStep fromOnly(String tableName) {
        return fromOnly(asName(tableName));
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName is a table name
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
     * Constant expression:
     *
     * <code>
     * table_name
     * $n1
     * LIST[1]
     * 234.11
     * .50
     * .2E+1
     * </code>
     *
     * @param constant any constant
     * @return a constant. e.g. 1, id, ARRAY[1] ...
     */
    public static Expression asConstant(String constant) {
        return new ConstantExpression(constant);
    }

    /**
     * String expression:
     * <code>
     * 'any string is here'
     * </code>
     *
     * @param constant string constant
     * @return a string surrounded by single quote string. e.g. 'string'
     */
    public static Expression asString(String constant) {
        return new StringExpression(constant);
    }

    /**
     * Dollar string expression:
     * <code>
     *     $$any string is here$$
     * </code>
     *
     * @param constant dollar string
     * @return a string surrounded by dollar singes string. e.g. $$string$$
     */
    public static Expression asDollarString(String constant) {
        return new DollarStringExpression(constant, "");
    }

    /**
     * Dollar string expression:
     * <code>
     *     $$any string is here$$
     *     $tag$any string is here$tag$
     * </code>
     *
     * @param constant dollar string
     * @param tagName tag name
     * @return a string surrounded by dollar singes string. e.g. $tag$ string $tag$
     */
    public static Expression asDollarString(String constant, String tagName) {
        return new DollarStringExpression(constant, tagName);
    }

    /**
     * It is a synonym of {@link #asConstant(String)}
     *
     * @param constant numeric constant
     * @return number.
     */
    public static Expression asNumber(Number constant) {
        return new ConstantExpression(constant);
    }

    /**
     * Column reference expression:
     * <code>
     *     table.column
     *     (complex_type).field
     * </code>
     *
     * @param constant column reference string
     * @return a quoted name. e.g. id; table.phone
     */
    public static Expression asName(String constant) {
        return new ColumnReferenceExpression(constant);
    }

    /**
     * Quoted column reference expression:
     * <code>
     *     "table"."column"
     * </code>
     *
     * @param constant column reference string
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
