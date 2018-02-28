/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.ast.TreeFactory;
import org.queryman.builder.boot.ServiceRegister;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.command.delete.DeleteAsStep;
import org.queryman.builder.command.from.FromFirstStep;
import org.queryman.builder.command.impl.ConditionsImpl;
import org.queryman.builder.command.impl.DeleteImpl;
import org.queryman.builder.command.impl.FromImpl;
import org.queryman.builder.command.impl.InsertImpl;
import org.queryman.builder.command.impl.SelectImpl;
import org.queryman.builder.command.impl.SequenceImpl;
import org.queryman.builder.command.impl.UpdateImpl;
import org.queryman.builder.command.insert.InsertAsStep;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.command.create.sequence.SequenceAsStep;
import org.queryman.builder.command.update.UpdateAsStep;
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
import org.queryman.builder.token.expression.SubQueryExpression;

import java.util.Arrays;
import java.util.List;

import static org.queryman.builder.ast.NodesMetadata.ALL;
import static org.queryman.builder.ast.NodesMetadata.ANY;
import static org.queryman.builder.ast.NodesMetadata.EXISTS;
import static org.queryman.builder.ast.NodesMetadata.SOME;

/**
 * Entry point of all parts of PostgreSQL. This contains a full collection of
 * SQL statements like the SELECT, UPDATE, DELETE.
 * Also other parts of SQL clauses are encapsulated here, such as any conditions clause
 * (AND, OR, OR NOT, etc), FROM clause, any expressions,
 *
 * @author Timur Shaidullin
 */
public class PostgreSQL {
    private static TreeFactory treeFactory;

    static {
        treeFactory = new ServiceRegister()
           .makeDefaults()
           .treeFactory();
    }

    public static void setTreeFactory(TreeFactory factory) {
        treeFactory = factory;
    }

    /**
     * @return tree
     */
    public static AbstractSyntaxTree getTree() {
        return treeFactory.getTree();
    }

    //----
    // SELECT API
    //----

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     select("id", "name", 3); // SELECT id, name, 3
     * </code>
     *
     * @param columns output columns
     *
     * @return select instance
     */
    @SafeVarargs
    public static <T> SelectFromStep select(T... columns) {
        return select(Arrays.stream(columns)
           .map(v -> PostgreSQL.asName(String.valueOf(v)))
           .toArray(Expression[]::new)
        );
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     select(asName("id"), asName("name")); // SELECT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select instance
     */
    public static SelectFromStep select(Expression... columns) {
        return new SelectImpl(getTree(), columns);
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     selectAll("id", "name"); // SELECT ALL id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select instance
     */
    public static SelectFromStep selectAll(String... columns) {
        return selectAll(Arrays.stream(columns).map(PostgreSQL::asName).toArray(Expression[]::new));
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     select(asName("id"), asName("name")); // SELECT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select instance
     */
    public static SelectFromStep selectAll(Expression... columns) {
        return new SelectImpl(getTree(), columns).all();
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     selectDistinct("id", "name"); // SELECT DISTINCT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select instance
     */
    public static SelectFromStep selectDistinct(String... columns) {
        return selectDistinct(Arrays.stream(columns).map(PostgreSQL::asName).toArray(Expression[]::new));
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     selectDistinct(asName("id"), asName("name")); // SELECT DISTINCT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select instance
     */
    public static SelectFromStep selectDistinct(Expression... columns) {
        return new SelectImpl(getTree(), columns).distinct();
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     String[] distinct = {"id"};
     *     selectDistinctOn(distinct, "id", "name"); // SELECT DISTINCT ON (id) id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select instance
     */
    public static SelectFromStep selectDistinctOn(String[] distinct, String... columns) {
        return selectDistinctOn(
           Arrays.stream(distinct).map(PostgreSQL::asName).toArray(Expression[]::new),
           Arrays.stream(columns).map(PostgreSQL::asName).toArray(Expression[]::new)
        );
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     Expression[] distinct = {"id"};
     *     selectDistinctOn(distinct, asName("id"), asName("name")); // SELECT DISTINCT ON (id) id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select instance
     */
    public static SelectFromStep selectDistinctOn(Expression[] distinct, Expression... columns) {
        return new SelectImpl(getTree(), columns).distinctOn(distinct);
    }

    //----
    // SEQUENCE
    //----

    /**
     * Create a sequence.
     * Example:
     * <code>
     *     createSequence("book_seq")
     *      .as("smallint")
     *      .incrementBy(1)
     *      .minvalue(0)
     *      .noMaxvalue()
     *      .startWith(0)
     *      .cache(5)
     *      .cycle()
     *      .ownedByNone()
     *      .sql();
     * </code>
     *
     * @param name sequence name
     * @return sequence AS step
     */
    public static SequenceAsStep createSequence(String name) {
        return createSequence(asName(name));
    }

    /**
     * Create a sequence.
     * Example:
     * <code>
     *     createSequence("book_seq")
     *      .as("smallint")
     *      .incrementBy(1)
     *      .minvalue(0)
     *      .noMaxvalue()
     *      .startWith(0)
     *      .cache(5)
     *      .cycle()
     *      .ownedByNone()
     *      .sql();
     * </code>
     *
     * @param name sequence name
     * @return sequence AS step
     */
    public static SequenceAsStep createSequence(Expression name) {
        return new SequenceImpl(getTree(), name);
    }

    /**
     * Create a sequence.
     * Example:
     * <code>
     *     createTempSequence("book_seq")
     *      .as("smallint")
     *      .incrementBy(1)
     *      .minvalue(0)
     *      .noMaxvalue()
     *      .startWith(0)
     *      .cache(5)
     *      .cycle()
     *      .ownedByNone()
     *      .sql();
     * </code>
     *
     * @param name sequence name
     * @return sequence AS step
     */
    public static SequenceAsStep createTempSequence(String name) {
        return createTempSequence(asName(name));
    }

    /**
     * Create a sequence.
     * Example:
     * <code>
     *     createTempSequence("book_seq")
     *      .as("smallint")
     *      .incrementBy(1)
     *      .minvalue(0)
     *      .noMaxvalue()
     *      .startWith(0)
     *      .cache(5)
     *      .cycle()
     *      .ownedByNone()
     *      .sql();
     * </code>
     *
     * @param name sequence name
     * @return sequence AS step
     */
    public static SequenceAsStep createTempSequence(Expression name) {
        return new SequenceImpl(getTree(), name, true);
    }

    /**
     * Create a sequence.
     * Example:
     * <code>
     *     createTempSequenceIfNotExists("book_seq")
     *      .as("smallint")
     *      .incrementBy(1)
     *      .minvalue(0)
     *      .noMaxvalue()
     *      .startWith(0)
     *      .cache(5)
     *      .cycle()
     *      .ownedByNone()
     *      .sql();
     * </code>
     *
     * @param name sequence name
     * @return sequence AS step
     */
    public static SequenceAsStep createTempSequenceIfNotExists(String name) {
        return createTempSequenceIfNotExists(asName(name));
    }

    /**
     * Create a sequence.
     * Example:
     * <code>
     *     createTempSequenceIfNotExists("book_seq")
     *      .as("smallint")
     *      .incrementBy(1)
     *      .minvalue(0)
     *      .noMaxvalue()
     *      .startWith(0)
     *      .cache(5)
     *      .cycle()
     *      .ownedByNone()
     *      .sql();
     * </code>
     *
     * @param name sequence name
     * @return sequence AS step
     */
    public static SequenceAsStep createTempSequenceIfNotExists(Expression name) {
        return new SequenceImpl(getTree(), name, true, true);
    }

    /**
     * Create a sequence.
     * Example:
     * <code>
     *     createSequenceIfNotExists("book_seq")
     *      .as("smallint")
     *      .incrementBy(1)
     *      .minvalue(0)
     *      .noMaxvalue()
     *      .startWith(0)
     *      .cache(5)
     *      .cycle()
     *      .ownedByNone()
     *      .sql();
     * </code>
     *
     * @param name sequence name
     * @return sequence AS step
     */
    public static SequenceAsStep createSequenceIfNotExists(String name) {
        return createSequenceIfNotExists(asName(name));
    }

    /**
     * Create a sequence.
     * Example:
     * <code>
     *     createSequenceIfNotExists("book_seq")
     *      .as("smallint")
     *      .incrementBy(1)
     *      .minvalue(0)
     *      .noMaxvalue()
     *      .startWith(0)
     *      .cache(5)
     *      .cycle()
     *      .ownedByNone()
     *      .sql();
     * </code>
     *
     * @param name sequence name
     * @return sequence AS step
     */
    public static SequenceAsStep createSequenceIfNotExists(Expression name) {
        return new SequenceImpl(getTree(), name, false, true);
    }

    //----
    // DECLARE
    //----

    public static void declare(String name) {

    }

    //----
    // DELETE
    //----

    public static DeleteAsStep deleteFrom(String name) {
        return deleteFrom(asName(name));
    }

    public static DeleteAsStep deleteFrom(Expression name) {
        return new DeleteImpl(getTree(), name);
    }

    public static DeleteAsStep deleteFromOnly(String name) {
        return deleteFromOnly(asName(name));
    }

    public static DeleteAsStep deleteFromOnly(Expression name) {
        return new DeleteImpl(getTree(), name, true);
    }

    //----
    // UPDATE
    //----

    /**
     * UPDATE statement.
     *
     * update("book")
     *  .as("b")
     *  .set("author", asQuotedName("Andrew"))
     *  .where("b.id", "=", "1")
     *  .returning(asName("max(price)").as("price"))
     *  .sql();
     *
     * @param name name of the table ot update
     * @return update as step
     */
    public static UpdateAsStep update(String name) {
        return update(asName(name));
    }


    /**
     * UPDATE statement.
     *
     * update("book")
     *  .as("b")
     *  .set("author", asQuotedName("Andrew"))
     *  .where("b.id", "=", "1")
     *  .returning(asName("max(price)").as("price"))
     *  .sql();
     *
     * @param name name of the table ot update
     * @return update as step
     */
    public static UpdateAsStep update(Expression name) {
        return new UpdateImpl(getTree(), name);
    }

    /**
     * UPDATE statement.
     *
     * updateOnly("book")
     *  .as("b")
     *  .set("author", asQuotedName("Andrew"))
     *  .where("b.id", "=", "1")
     *  .returning(asName("max(price)").as("price"))
     *  .sql();
     *
     * @param name name of the table ot update
     * @return update as step
     */
    public static UpdateAsStep updateOnly(String name) {
        return updateOnly(asName(name));
    }

    /**
     * UPDATE statement.
     *
     * updateOnly("book")
     *  .as("b")
     *  .set("author", asQuotedName("Andrew"))
     *  .where("b.id", "=", "1")
     *  .returning(asName("max(price)").as("price"))
     *  .sql();
     *
     * @param name name of the table ot update
     * @return update as step
     */
    public static UpdateAsStep updateOnly(Expression name) {
        return new UpdateImpl(getTree(), name, true);
    }

    //----
    // UPDATE
    //----

    public static InsertAsStep insertInto(String table) {
        return insertInto(asName(table));
    }

    public static InsertAsStep insertInto(Expression table) {
        return new InsertImpl(getTree(), table);
    }

    //----
    // COMMON CONDITIONS
    //----

    /**
     * Create a condition
     *
     * @param leftValue  left operand
     * @param operator   operator
     * @param rightValue right operand
     * @return {@link Conditions}
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
     *
     * @param leftValue  left operand
     * @param operator   operator
     * @param rightValue right operand
     * @return {@link Conditions}
     * @see #operator(String)
     * @see #condition(Expression, Operator, Expression)
     */
    public static Conditions condition(Expression leftValue, String operator, Expression rightValue) {
        return condition(leftValue, Operators.map(operator), rightValue);
    }

    /**
     * Create a condition
     *
     * @param leftValue  left operand
     * @param operator   operator
     * @param rightValue right operand
     * @return {@link Conditions}
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
     * @param field  seeking operand
     * @param value1 operand before AND
     * @param value2 operand after AND
     * @return {@link Conditions}
     * @see #conditionBetween(String, String, String)
     */
    public static Conditions conditionBetween(Expression field, Expression value1, Expression value2) {
        return new ConditionsImpl(NodesMetadata.BETWEEN, field, condition(value1, operator("AND"), value2));
    }

    /**
     * Create a condition: field BETWEEN value1 AND value2.
     *
     * @param field  seeking operand
     * @param value1 operand before AND
     * @param value2 operand after AND
     * @return {@link Conditions}
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
     * <p>
     * Example:
     * <code>
     * condition(asName("name"), IN, select("name").from("authors")); // name IN (select name from authors)
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    right operand
     * @return {@link Conditions}

     * @see Operators#IN
     */
    public static Conditions condition(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(field, new NodeMetadata(operator), query);
    }

    /**
     * Subquery condition. Primarily it is used by {@code IN} expression:
     * <p>
     * Example:
     * <code>
     * condition("name", "IN", select("name").from("authors")); // name IN (select name from authors)
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    right operand
     * @return {@link Conditions}
     * @see #select(Object[])
     */
    public static Conditions condition(String field, String operator, Query query) {
        return condition(asName(field), Operators.map(operator), query);
    }

    /**
     * Creates a condition.
     * Example:
     * <code>
     * conditionExists(select(1, 2)); // EXISTS (SELECT 1, 2);
     * </code>
     *
     * @param query subquery
     * @return {@link Conditions}
     */
    public static Conditions conditionExists(Query query) {
        return new ConditionsImpl(EXISTS, query);
    }

    /**
     * Creates a condition.
     * Example:
     * <code>
     * conditionSome(asName("id"), operator("="), select(1)); // id = SOME (SELECT 1);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery right operand
     * @return {@link Conditions}
     */
    public static Conditions conditionSome(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(SOME, query));
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     * conditionSome("id", "=", select(1, 2)); // id = SOME (SELECT 1, 2);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery right operand
     * @return {@link Conditions}
     * @see #conditionSome(Expression, Operator, Query)
     */
    public static Conditions conditionSome(String field, String operator, Query query) {
        return conditionSome(asName(field), operator(operator), query);
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     * conditionAny("id", "=", select(1, 2)); // id = ANY (SELECT 1, 2);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery right operand
     * @return {@link Conditions}
     */
    public static Conditions conditionAny(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(ANY, query));
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     * conditionAny("id", "=", select(1, 2)); // id = ANY (SELECT 1, 2);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery right operand
     * @return {@link Conditions}
     * @see #conditionAny(Expression, Operator, Query)
     */
    public static Conditions conditionAny(String field, String operator, Query query) {
        return conditionAny(asName(field), operator(operator), query);
    }

    /**
     * Creates a condition.
     * Example:
     * <code>
     * conditionAll("id", "=", select(1, 2)); // id = ALL (SELECT 1, 2);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery -right operand
     * @return {@link Conditions}
     * @see #conditionAny(Expression, Operator, Query)
     */
    public static Conditions conditionAll(Expression field, Operator operator, Query query) {
        return new ConditionsImpl(new NodeMetadata(operator), field, new ConditionsImpl(ALL, query));
    }

    /**
     * Creates a condition.
     * Example:
     * <code>
     * conditionAll("id", "=", select(1, 2)); // id = ALL (SELECT 1, 2);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery -right operand
     * @return {@link Conditions}
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
     * @return first step of FROM clause.
     * @see #from(Expression)
     */
    public static FromFirstStep from(String tableName) {
        return from(asName(tableName));
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName - is a table name
     * @return first step of FROM clause.
     * @see #from(String)
     */
    public static FromFirstStep from(Expression tableName) {
        return new FromImpl(tableName);
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName is a table name
     * @return first step of FROM clause.
     * @see #fromOnly(Expression)
     */
    public static FromFirstStep fromOnly(String tableName) {
        return fromOnly(asName(tableName));
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * @param tableName is a table name
     * @return first step of FROM clause.
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
     * <p>
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
     * Prepared constant expression:
     * <p>
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
    public static Expression asPreparedConstant(String constant) {
        return new ConstantExpression(constant).setPrepared(true);
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
     * Prepared string expression:
     * <code>
     * 'any string is here'
     * </code>
     *
     * @param constant string constant
     * @return a string surrounded by single quote string. e.g. 'string'
     */
    public static Expression asPreparedString(String constant) {
        return new StringExpression(constant).setPrepared(true);
    }

    /**
     * Dollar string expression:
     * <code>
     * $$any string is here$$
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
     * $$any string is here$$
     * $tag$any string is here$tag$
     * </code>
     *
     * @param constant dollar string
     * @param tagName  tag name
     * @return a string surrounded by dollar singes string. e.g. $tag$ string $tag$
     */
    public static Expression asDollarString(String constant, String tagName) {
        return new DollarStringExpression(constant, tagName);
    }

    /**
     * Prepared dollar string expression:
     * <code>
     * $$any string is here$$
     * </code>
     *
     * @param constant dollar string
     * @return a string surrounded by dollar singes string. e.g. $$string$$
     */
    public static Expression asPreparedDollarString(String constant) {
        return new DollarStringExpression(constant, "").setPrepared(true);
    }

    /**
     * Prepared dollar string expression:
     * <code>
     * $$any string is here$$
     * $tag$any string is here$tag$
     * </code>
     *
     * @param constant dollar string
     * @param tagName  tag name
     * @return a string surrounded by dollar singes string. e.g. $tag$ string $tag$
     */
    public static Expression asPreparedDollarString(String constant, String tagName) {
        return new DollarStringExpression(constant, tagName).setPrepared(true);
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
     * It is a synonym of {@link #asConstant(String)}
     *
     * @param constant numeric constant
     * @return number.
     */
    public static Expression asPreparedNumber(Number constant) {
        return new ConstantExpression(constant).setPrepared(true);
    }

    /**
     * Column reference expression:
     * <code>
     * table.column
     * (complex_type).field
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
     * "table"."column"
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
     * List of string expression.
     * Example:
     * <code>
     * asStringList(1, 2, 3); // ('1', '2', '3')
     * </code>
     *
     * @param constants values
     * @return (...), where {@code ...} are values of string concatenated by comma.
     */
    @SafeVarargs
    public static <T> Expression asStringList(T... constants) {
        return new ListStringExpression<>(constants);
    }

    /**
     * List of string expression.
     * Example:
     * <code>
     * asString(List.of(1, 2, 3,)); // ('1', '2', '3')
     * </code>
     *
     * @param constants list of values
     * @return (...), where {@code ...} are values of string concatenated by comma.
     * @see #asStringList(T...)
     */
    public static <T> Expression asStringList(List<T> constants) {
        return asStringList(constants.toArray());
    }

    /**
     * List expression.
     * Example:
     * <code>
     * asString(1, 2, 3); // (1, 2, 3)
     * </code>
     *
     * @param constants values
     * @return (...), where {@code ...} are values concatenated by comma.
     */
    @SafeVarargs
    public static <T> Expression asList(T... constants) {
        return new ListExpression<>(constants);
    }

    /**
     * List expression.
     * Example:
     * <code>
     * asString(List.of(1, 2, 3)); // (1, 2, 3)
     * </code>
     *
     * @param constants values
     * @return (...), where {@code ...} are values concatenated by comma.
     */
    public static <T> Expression asList(List<T> constants) {
        return asList(constants.toArray());
    }

    //----
    // ARRAY EXPRESSIONS
    //----

    /**
     * Array expression:
     * <code>
     * asArray(1, 2); // ARRAY[1, 2]
     * </code>
     *
     * @param arr - values of array
     * @return ARRAY[...], where {@code ...} are values concatenated by comma.
     */
    @SafeVarargs
    public static <T> Expression asArray(T... arr) {
        return new ArrayExpression<>(arr);
    }

    /**
     * Array expression:
     * <code>
     * asArray(List.of(1,2)); // ARRAY[1, 2]
     * </code>
     *
     * @param arr - values of array
     * @return ARRAY[...], where {@code ...} are values concatenated by comma.
     */
    public static <T> Expression asArray(List<T> arr) {
        return asArray(arr.toArray());
    }

    /**
     * Array of string expression:
     * <code>
     * asArray(1, 2); // ARRAY[1, 2]
     * </code>
     *
     * @param arr - string values of array
     * @return ARRAY[...], where {@code ...} are values concatenated by comma.
     */
    @SafeVarargs
    public static <T> Expression asStringArray(T... arr) {
        return new ArrayStringExpression<>(arr);
    }

    /**
     * Array of string expression:
     * <code>
     * asArray(List.of(1, 2)); // ARRAY['1', '2']
     * </code>
     *
     * @param arr - string values of array
     * @return ARRAY[...], where {@code ...} are values concatenated by comma.
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
     * asFunc("ALL", asArray(List.of(1, 2)));  // ALL(ARRAY[1, 2])
     * asFunc("SOME", asList(1, 2)); // SOME(1, 2)
     * </code>
     *
     * @param name       - function or operator name, examples: <code>ALL, ANY ...</code>
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
    /**
     * Examples:
     * <code>
     * max("price");  // MAX("price")
     * </code>
     *
     * @param field field
     * @return an aggregate functions
     *
     */
    public static Expression max(String field) {
        return new FuncExpression("MAX", asName(field));
    }

    /**
     * @param name of function
     * @param arguments arguments
     * @return a function with list of arguments

     * @see #asFunc(String, Expression)
     */
    @SafeVarargs
    public static <T> Expression asFunc(String name, T... arguments) {
        String[] args = Arrays.stream(arguments).map(String::valueOf).toArray(String[]::new);
        return asFunc(name, asList(args));
    }

    /**
     * This is a synonym of {@link #asFunc(String, Expression)}.
     *
     * Examples:
     * <code>
     * asOperator("ALL", asArray(List.of(1, 2)));  // ALL(ARRAY[1, 2])
     * asOperator("SOME", asList(1, 2)); // SOME(1, 2)
     * </code>
     *
     * @param name       - function or operator name, examples: <code>ALL, ANY ...</code>
     * @param expression - list or array expression
     * @return a combine of {@code name} and {@code expression} objects
     *
     * @see #asArray(Object[])
     * @see #asStringArray(Object[])
     * @see #asList(Object[])
     * @see #asStringList(List)
     *
     * @see #asFunc(String, Expression)
     */
    public static Expression asOperator(String name, Expression expression) {
        return new FuncExpression(name, expression);
    }

    /**
     * This is a synonym of {@link #asFunc(String, Object[])}.
     *
     * @param name of function
     * @param arguments arguments
     * @return a function with list of arguments

     * @see #asOperator(String, Expression)
     */
    @SafeVarargs
    public static <T> Expression asOperator(String name, T... arguments) {
        String[] args = Arrays.stream(arguments).map(String::valueOf).toArray(String[]::new);
        return asFunc(name, asList(args));
    }

    /**
     * VALUES list.
     * Examples:
     * <code>
     *     values(asList(1, 2), asList(3, 4)); // VALUES(1, 2), (3, 4)
     *
     *     // (VALUES(1, 2), (3, 4)) AS point(x, y)
     *     values(asList(1, 2), asList(3, 4)).as("point", "x", "y");
     * </code>
     *
     *
     * @param expressions list of expressions
     * @return VALUES expression
     *
     * @see #asList(Object[])
     * @see #asStringList(Object[])
     */
    public static Expression values(Expression... expressions) {
       return new FuncExpression("VALUES", expressions);
    }

    /**
     * @param query subquery
     * @return a subquery expression
     */
    public static Expression asSubQuery(Query query) {
        return new SubQueryExpression(query);
    }

    //----
    // COMMON
    //----

    /**
     * Create an operator which ordinarily is used by condition.
     *
     * @param operator LIKE, ILIKE, =, !=, @> etc.
     * @return instance of {@link Operator}.
     * <p>
     * Most useful operators are collected there:
     * @see Operators
     */
    public static Operator operator(String operator) {
        return new Operator(operator);
    }

    /**
     * Create a keyword which ordinarily is used to build SQL query.
     *
     * @param keyword SELECT, UPDATE, FROM, JOIN etc.
     * @return instance of {@link Keyword}
     * <p>
     * Most useful keywords are collected there:
     * @see Operators
     */
    public static Keyword keyword(String keyword) {
        return new Keyword(keyword);
    }

    /**
     * NodeMetadata contains a metadata for tree node.
     *
     * @param keyword NodeMetadata is composed from keyword.
     * @return node metadata
     */
    public static NodeMetadata nodeMetadata(Keyword keyword) {
        return new NodeMetadata(keyword);
    }

    public static ConflictTarget conflictTargetExpression(String indexName) {
        return conflictTargetExpression(indexName, null, null);
    }

    public static ConflictTarget conflictTargetExpression(String indexName, String collation) {
        return conflictTargetExpression(indexName, collation, null);
    }

    public static ConflictTarget conflictTargetExpression(String indexName, String collation, String opclass) {
        return new ConflictTarget(indexName, collation, opclass).markAsExpression();
    }

    public static ConflictTarget conflictTargetColumn(String indexName) {
        return conflictTargetColumn(indexName, null, null);
    }

    public static ConflictTarget conflictTargetColumn(String indexName, String collation) {
        return conflictTargetColumn(indexName, collation, null);
    }

    public static ConflictTarget conflictTargetColumn(String indexName, String collation, String opclass) {
        return new ConflictTarget(indexName, collation, opclass).markAsColumn();
    }
}
