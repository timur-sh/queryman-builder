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
import org.queryman.builder.command.ConflictTarget;
import org.queryman.builder.command.clause.OrderBy;
import org.queryman.builder.command.create_sequence.SequenceAsStep;
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
import org.queryman.builder.command.update.UpdateAsStep;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Keyword;
import org.queryman.builder.token.Operator;
import org.queryman.builder.token.expression.ColumnReferenceExpression;
import org.queryman.builder.token.expression.FuncExpression;
import org.queryman.builder.token.expression.ListExpression;
import org.queryman.builder.token.expression.SubQueryExpression;
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
import org.queryman.builder.token.expression.prepared.UUIDExpression;
import org.queryman.builder.utils.ArrayUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.queryman.builder.ast.NodesMetadata.EXISTS;
import static org.queryman.builder.utils.ArrayUtils.toExpressions;
import static org.queryman.builder.utils.ExpressionUtil.toExpression;

/**
 * The entry point to all parts of PostgreSQL clauses. This contains a full
 * collection of SQL statements like the SELECT, UPDATE, DELETE etc.
 *
 * Also other parts of SQL clauses are encapsulated here, such as conditions clause
 * (AND, OR, OR NOT, etc), FROM clause, etc.
 *
 * @author Timur Shaidullin
 */
public class Queryman {
    private static TreeFactory treeFactory;

    static {
        TreeFactory t = new ServiceRegister()
           .makeDefaults()
           .treeFactory();

        setTreeFactory(t);
    }

    /**
     * Sets a {@link TreeFactory} explicitly, otherwise it is set implicitly
     * via the static block above.
     *
     * @param factory tree factory
     */
    public static void setTreeFactory(TreeFactory factory) {
        treeFactory = factory;
    }

    /**
     * @return an abstract syntax three
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
     * @return select from step
     */
    @SafeVarargs
    public static <T> SelectFromStep select(T... columns) {
        return select(Arrays.stream(columns)
           .map(v -> Queryman.asName(String.valueOf(v)))
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
     * @return select from step
     */
    public static SelectFromStep select(Expression... columns) {
        return new SelectImpl(getTree(), columns);
    }

    /**
     * SELECT ALL statement.
     * Example:
     * <code>
     *     selectAll("id", "name"); // SELECT ALL id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    @SafeVarargs
    public static <T> SelectFromStep selectAll(T... columns) {
        return selectAll(Arrays.stream(columns)
           .map(v -> asName(String.valueOf(v)))
           .toArray(Expression[]::new));
    }

    /**
     * SELECT ALL statement.
     * Example:
     * <code>
     *     select(asName("id"), asName("name")); // SELECT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    public static SelectFromStep selectAll(Expression... columns) {
        return new SelectImpl(getTree(), columns).all();
    }

    /**
     * SELECT DISTINCT statement.
     * Example:
     * <code>
     *     selectDistinct("id", "name"); // SELECT DISTINCT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    @SafeVarargs
    public static <T> SelectFromStep selectDistinct(T... columns) {
        return selectDistinct(Arrays.stream(columns)
           .map(v -> asName(String.valueOf(v)))
           .toArray(Expression[]::new));
    }

    /**
     * SELECT DISTINCT statement.
     * Example:
     * <code>
     *     selectDistinct(asName("id"), asName("name")); // SELECT DISTINCT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    public static SelectFromStep selectDistinct(Expression... columns) {
        return new SelectImpl(getTree(), columns).distinct();
    }

    /**
     * SELECT DISTINCT ON (..) .. statement.
     * Example:
     * <code>
     *     String[] distinct = {"id"};
     *     selectDistinctOn(distinct, "id", "name"); // SELECT DISTINCT ON (id) id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    @SafeVarargs
    public static <T> SelectFromStep selectDistinctOn(String[] distinct, T... columns) {
        return selectDistinctOn(
           Arrays.stream(distinct).map(Queryman::asName).toArray(Expression[]::new),
           Arrays.stream(columns)
              .map(v -> asName(String.valueOf(v)))
              .toArray(Expression[]::new)
        );
    }

    /**
     * SELECT DISTINCT ON (..) .. statement.
     * Example:
     * <code>
     *     Expression[] distinct = {"id"};
     *     selectDistinctOn(distinct, asName("id"), asName("name")); // SELECT DISTINCT ON (id) id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    public static SelectFromStep selectDistinctOn(Expression[] distinct, Expression... columns) {
        return new SelectImpl(getTree(), columns).distinctOn(distinct);
    }

    //----
    // SEQUENCE
    //----

    /**
     * Creates a sequence.
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
     * Creates a sequence.
     * Example:
     * <code>
     *     createSequence(asName("book_seq"))
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
     * Creates a create_sequence.
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
     * Creates a sequence.
     * Example:
     * <code>
     *     createTempSequence(asName("book_seq"))
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
     * Creates a sequence.
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
     * Creates a sequence.
     * Example:
     * <code>
     *     createTempSequenceIfNotExists(asName("book_seq"))
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
     * Creates a sequence.
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
     * Creates a sequence.
     * Example:
     * <code>
     *     createSequenceIfNotExists(asName("book_seq"))
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

    /**
     * DELETE statement.
     *
     * <code>
     *     // DELETE FROM book AS b USING author, order WHERE b.id = 1 RETURNING id
     *     deleteFrom("book")
     *      .as("b")
     *      .using("author", "order")
     *      .where("b.id", "=", "1")
     *      .returning(asName("id")
     *      .sql()
     * </code>
     *
     * @param name target table name
     * @return delete AS step
     */
    public static DeleteAsStep deleteFrom(String name) {
        return deleteFrom(asName(name));
    }

    /**
     * DELETE statement.
     *
     * <code>
     *     // DELETE FROM book AS b USING author, order WHERE b.id = 1 RETURNING id
     *     deleteFrom(asName("book"))
     *      .as("b")
     *      .using("author", "order")
     *      .where("b.id", "=", "1")
     *      .returning(asName("id")
     *      .sql()
     * </code>
     *
     * @param name target table name
     * @return delete AS step
     */
    public static DeleteAsStep deleteFrom(Expression name) {
        return new DeleteImpl(getTree(), name);
    }

    /**
     * DELETE statement.
     *
     * <code>
     *     // DELETE FROM ONLY book AS b USING author, order WHERE b.id = 1 RETURNING id
     *     deleteFromOnly("book")
     *      .as("b")
     *      .using("author", "order")
     *      .where("b.id", "=", "1")
     *      .returning(asName("id")
     *      .sql()
     * </code>
     *
     * @param name target table name
     * @return delete AS step
     */
    public static DeleteAsStep deleteFromOnly(String name) {
        return deleteFromOnly(asName(name));
    }

    /**
     * DELETE statement.
     *
     * <code>
     *     // DELETE FROM ONLY book AS b USING author, order WHERE b.id = 1 RETURNING id
     *     deleteFromOnly(asName("book"))
     *      .as("b")
     *      .using("author", "order")
     *      .where("b.id", "=", "1")
     *      .returning(asName("id")
     *      .sql()
     * </code>
     *
     * @param name target table name
     * @return delete AS step
     */
    public static DeleteAsStep deleteFromOnly(Expression name) {
        return new DeleteImpl(getTree(), name, true);
    }

    //----
    // UPDATE
    //----

    /**
     * UPDATE statement.
     *
     * <code>
     *     // UPDATE book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price
     *     update("book")
     *      .as("b")
     *      .set("author", asConstant("Andrew"))
     *      .where("b.id", "=", 1)
     *      .returning(asName("max(price)").as("price"))
     *      .sql();
     * </code>
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
     * <code>
     *     // UPDATE book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price
     *     update(asName("book"))
     *      .as("b")
     *      .set("author", asConstant("Andrew"))
     *      .where("b.id", "=", 1)
     *      .returning(asName("max(price)").as("price"))
     *      .sql();
     * </code>
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
     * <code>
     *     // UPDATE ONLY book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price
     *     updateOnly("book")
     *      .as("b")
     *      .set("author", asConstant("Andrew"))
     *      .where("b.id", "=", 1)
     *      .returning(asName("max(price)").as("price"))
     *      .sql();
     * </code>
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
     * <code>
     *     // UPDATE ONLY book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price
     *     updateOnly(asName("book"))
     *      .as("b")
     *      .set("author", asConstant("Andrew"))
     *      .where("b.id", "=", 1)
     *      .returning(asName("max(price)").as("price"))
     *      .sql();
     * </code>
     *
     * @param name name of the table ot update
     * @return update as step
     */
    public static UpdateAsStep updateOnly(Expression name) {
        return new UpdateImpl(getTree(), name, true);
    }

    //----
    // INSERT
    //----

    /**
     * INSERT INTO statement.
     * <code>
     *     insertInto("book")
     *      .as("b")
     *      .columns("id", "name")
     *      .overridingSystemValue()
     *      .values(1, "test")
     *      .onConflict()
     *      .onConstraint("index_name")
     *      .doNothing()
     *      .sql()
     * </code>
     *
     * @param table target table name
     * @return insert AS step
     */
    public static InsertAsStep insertInto(String table) {
        return insertInto(asName(table));
    }

    /**
     * INSERT INTO statement.
     * <code>
     *     insertInto(asName("book"))
     *      .as("b")
     *      .columns("id", "name")
     *      .overridingSystemValue()
     *      .values(1, "test")
     *      .onConflict()
     *      .onConstraint("index_name")
     *      .doNothing()
     *      .sql()
     * </code>
     *
     * @param table target table name
     * @return insert AS step
     */
    public static InsertAsStep insertInto(Expression table) {
        return new InsertImpl(getTree(), table);
    }

    //----
    // COMMON CONDITIONS
    //----

    /**
     * Creates a condition
     * <code>
     *     ...
     *     .where(condition("id", "=", "author_id"))
     *     ...
     * </code>
     *
     * @param leftValue  left operand
     * @param operator   operator
     * @param rightValue right operand
     * @param <T> String, Expression, Operator or Query object
     *
     * @return {@link Conditions}
     *
     * @see #condition(Expression, Operator, Expression)
     */
    public static <T> Conditions condition(T leftValue, T operator, T rightValue) {
        return condition(toExpression(leftValue), operator(operator), toExpression(rightValue));
    }

    /**
     * Creates a condition
     * <code>
     *     ...
     *     .where(condition(asName("id"), "=", asConstant(1)))
     *     ...
     * </code>
     *
     * @param leftValue  left operand
     * @param operator   operator
     * @param rightValue right operand
     *
     * @return {@link Conditions}
     *
     * @see #operator(Object)
     * @see #condition(Expression, Operator, Expression)
     */
    public static Conditions condition(Expression leftValue, String operator, Expression rightValue) {
        return condition(leftValue, operator(operator), rightValue);
    }

    /**
     * Creates a condition
     * <code>
     *     ...
     *     .where(condition(asName("id"), operator("="), asConstant(1)))
     *     ...
     * </code>
     *
     * @param leftValue  left operand
     * @param operator   operator
     * @param rightValue right operand
     *
     * @return {@link Conditions}
     *
     * @see #operator(Object)
     */
    public static Conditions condition(Expression leftValue, Operator operator, Expression rightValue) {
        return new ConditionsImpl(leftValue, new NodeMetadata(operator), rightValue);
    }

    //----
    // BETWEEN CONDITIONS
    //----

    /**
     * Creates a BETWEEN condition
     * <code>
     *     ...
     *     .where(conditionBetween(asName("id"), asConstant(2), asConstant(10)))
     *     ...
     * </code>
     *
     * @param field  field
     * @param value1 left value of AND
     * @param value2 right value of AND
     *
     * @return {@link Conditions}
     *
     * @see #operator(Object)
     *
     * @see #conditionBetween(String, String, String)
     */
    public static Conditions conditionBetween(Expression field, Expression value1, Expression value2) {
        return new ConditionsImpl(NodesMetadata.BETWEEN, field, condition(value1, operator("AND"), value2));
    }

    /**
     * Creates a BETWEEN condition
     * <code>
     *     ...
     *     .where(conditionBetween("id", "2", "10"))
     *     ...
     * </code>
     *
     * @param field  field
     * @param value1 left value of AND
     * @param value2 right value of AND
     *
     * @return {@link Conditions}
     *
     * @see #conditionBetween(String, String, String)
     */
    public static Conditions conditionBetween(String field, String value1, String value2) {
        return conditionBetween(asName(field), asName(value1), asName(value2));
    }

    //----
    // SUBQUERY CONDITIONS
    //----

    /**
     * Creates a condition.
     *
     * <code>
     *      conditionExists(select(1, 2)); // EXISTS (SELECT 1, 2);
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
     *      conditionSome("id", "=", select(1)); // id = SOME (SELECT 1);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery right operand
     * @return {@link Conditions}
     */
    public static <T> Conditions conditionSome(T field, T operator, Query query) {
        return condition(toExpression(field), operator, some(query));
    }

    /**
     * Create a condition.
     * Example:
     * <code>
     *      conditionAny("id", "=", select(1, 2)); // id = ANY (SELECT 1, 2);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery right operand
     * @return {@link Conditions}
     */
    public static <T> Conditions conditionAny(T field, T operator, Query query) {
        return condition(toExpression(field), operator, any(query));
    }

    /**
     * Creates a condition.
     * Example:
     * <code>
     *      conditionAll("id", "=", select(1, 2)); // id = ALL (SELECT 1, 2);
     * </code>
     *
     * @param field    left operand
     * @param operator operator
     * @param query    subquery -right operand
     * @return {@link Conditions}
     * @see #conditionAny(Expression, Operator, Query)
     */
    public static <T> Conditions conditionAll(T field, T operator, Query query) {
        return condition(toExpression(field), operator, all(query));
    }

    //----
    // FROM
    //----

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * <code>
     *     from("book")
     *      .as(b)
     *      .tablesample("BERNOULLI", "1")
     *      .repeatable(1)
     * </code>
     *
     * @param tableName is a table name.
     * @return first step of FROM clause.
     *
     * @see #from(Expression)
     */
    public static FromFirstStep from(String tableName) {
        return from(asName(tableName));
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * <code>
     *     from(asName("book"))
     *      .as(b)
     *      .tablesample("BERNOULLI", "1")
     *      .repeatable(1)
     * </code>
     *
     * @param tableName is a table name.
     * @return first step of FROM clause.
     *
     * @see #from(Expression)
     */
    public static FromFirstStep from(Expression tableName) {
        return new FromImpl(tableName);
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * <code>
     *     fromOnly("book")
     *      .as(b)
     *      .tablesample("BERNOULLI", "1")
     *      .repeatable(1)
     * </code>
     *
     * @param tableName is a table name.
     * @return first step of FROM clause.
     *
     * @see #fromOnly(Expression)
     */
    public static FromFirstStep fromOnly(String tableName) {
        return fromOnly(asName(tableName));
    }

    /**
     * FROM clause can be used as a part of other SQL statements.
     *
     * <code>
     *     from(asName("book"))
     *      .as(b)
     *      .tablesample("BERNOULLI", "1")
     *      .repeatable(1)
     * </code>
     *
     * @param tableName is a table name.
     * @return first step of FROM clause.
     *
     */
    public static FromFirstStep fromOnly(Expression tableName) {
        return new FromImpl(tableName, true);
    }

    //----
    // COMMON EXPRESSIONS
    //----

    /**
     * Using a class name of constant it creates an appropriate Expression object.
     *
     * {@link java.util.Date} is could not be convert to particular expression,
     * you should use ad hoc methods to convert it to any expression of DATE type:
     * * {@link #asDate(Object)}
     * * {@link #asTime(Object)}
     * * {@link #asTimestamp(Object)}
     *
     * @param constant any constant
     * @return a constant of appropriate type.
     * @throws IllegalArgumentException if constant is not supported
     *
     * @see <a href=" https://www.postgresql.org/docs/9.2/static/sql-syntax-lexical.html#SQL-SYNTAX-CONSTANTS">PostgreSQL constants</a>
     */
    public static <T> Expression asConstant(T constant) {
        if (constant == null)
            return new NullExpression(null);
        Class<?> cl = ((Object)constant).getClass();

        switch (cl.getCanonicalName()) {
            case "java.lang.String":
            case "java.lang.Character":
                return new StringExpression(String.valueOf(constant));
            case "java.lang.Boolean":
                return new BooleanExpression((Boolean) constant);
            case "java.lang.Integer":
                return new IntegerExpression((Integer) constant);
            case "java.lang.Byte":
                return new ByteExpression((Byte) constant);
            case "java.lang.Short":
                return new ShortExpression((Short) constant);
            case "java.lang.Long":
                return new LongExpression((Long) constant);
            case "java.lang.Double":
                return new DoubleExpression((Double) constant);
            case "java.lang.Float":
                return new FloatExpression((Float) constant);
            case "java.util.Date":
                java.util.Date d = (java.util.Date) constant;
                return asDate(new Date(d.getTime()));
            case "java.sql.Date":
                return asDate(constant);
            case "java.sql.Time":
                return asTime(constant);
            case "java.sql.Timestamp":
                return asTimestamp(constant);
            case "java.math.BigDecimal":
                return new BigDecimalExpression((BigDecimal) constant);
            case "java.util.UUID":
                return new UUIDExpression((UUID) constant);
            case "byte[]":
                return asConstant(ArrayUtils.toWrapper((byte[]) constant));
            case "int[]":
                return asConstant(ArrayUtils.toWrapper((int[]) constant));
            case "short[]":
                return asConstant(ArrayUtils.toWrapper((short[]) constant));
            case "long[]":
                return asConstant(ArrayUtils.toWrapper((long[]) constant));
            case "float[]":
                return asConstant(ArrayUtils.toWrapper((float[]) constant));
        }

        if (constant instanceof Expression)
            return (Expression) constant;

        throw new IllegalArgumentException("Unsupported type " + ((Object)constant).getClass().getCanonicalName());
    }

    /**
     * Creates an array expression. If constant if Byte[] type, the ByteExpression
     * is created.
     *
     * @param constants array constant
     * @return array expression
     *
     * @see #asConstant(Object)
     */
    public static <T> Expression asConstant(T[] constants) {
        if (constants == null)
            return new NullExpression(null);

        switch (constants.getClass().getCanonicalName()) {
            case "java.lang.Byte[]":
                return new BytesExpression((Byte[])constants);
        }

        return asArray(constants);
    }

    /**
     * Creates a Date expression.
     *
     * @param date date
     * @return {@link DateExpression}
     */
    public static <T> Expression asDate(T date) {
        return new DateExpression((Date) date);
    }

    /**
     * Creates a Time expression.
     *
     * @param time time
     * @return {@link TimeExpression}
     */
    public static <T> Expression asTime(T time) {
        return new TimeExpression((Time) time);
    }

    /**
     * Creates a Timestamp expression.
     *
     * @param timestamp time
     * @return {@link TimestampExpression}
     */
    public static <T> Expression asTimestamp(T timestamp) {
        return new TimestampExpression((Timestamp) timestamp);
    }

    /**
     * Dollar string expression:
     * <code>
     *      $$any string is here$$
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
     *      $$any string is here$$
     *      $tag$any string is here$tag$
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
     * Column reference expression:
     * <code>
     *      table.column
     *      (complex_type).field
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
     *      "table"."column"
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
     * @see #asList(Object[])
     */
    public static Expression asFunc(String name, Expression expression) {
        return new FuncExpression(name, expression);
    }

    /**
     * @param name of function
     * @param arguments arguments
     * @return a function with list of arguments

     * @see #asFunc(String, Expression)
     */
    @SafeVarargs
    public static <T> Expression asFunc(String name, T... arguments) {
        return new FuncExpression(name, toExpressions(arguments));
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
        return asFunc("MAX", asName(field));
    }

    /**
     * Creates an ALL expression that contains one argument
     *
     * @param argument argument of any
     * @param <T> String ot Expression object
     * @return an ALL expression
     */
    public static <T> Expression all(T argument) {
        return asFunc("ALL", toExpression(argument));
    }

    /**
     * Creates an ALL expression that contains one argument
     *
     * @param arguments argument of
     * @param <T> String ot Expression objects
     * @return an ALL expression
     */
    @SafeVarargs
    public static <T> Expression all(T... arguments) {
        return asFunc("ALL", toExpressions(arguments));
    }

    /**
     * Creates an ANY expression that contains one argument
     *
     * @param argument argument of any
     * @param <T> String ot Expression object
     * @return an ANY expression
     */
    public static <T> Expression any(T argument) {
        return asFunc("ANY", toExpression(argument));
    }

    /**
     * Creates an ANY expression that contains one argument
     *
     * @param arguments argument of
     * @param <T> String ot Expression objects
     * @return an ANY expression
     */
    @SafeVarargs
    public static <T> Expression any(T... arguments) {
        return asFunc("ANY", toExpressions(arguments));
    }

    /**
     * Creates an SOME expression that contains one argument
     *
     * @param argument argument of any
     * @param <T> String ot Expression object
     * @return an SOME expression
     */
    public static <T> Expression some(T argument) {
        return asFunc("SOME", toExpression(argument));
    }

    /**
     * Creates an SOME expression that contains one argument
     *
     * @param arguments argument of
     * @param <T> String ot Expression objects
     * @return an SOME expression
     */
    @SafeVarargs
    public static <T> Expression some(T... arguments) {
        return asFunc("SOME", toExpressions(arguments));
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
     * @see #asList(Object[])
     *
     * @see #asFunc(String, Expression)
     */
    public static Expression asOperator(String name, Expression expression) {
        return asFunc(name, expression);
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
        return asFunc(name, arguments);
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
     */
    @SafeVarargs
    public static <T> Expression values(T... expressions) {
        Function<T, Expression> func = v -> {
            if (v instanceof ListExpression)
                return (Expression) v;
            return asList(v);
        };

       return asFunc("VALUES", toExpressions(func, expressions));
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
     * Creates an operator which ordinarily is used by condition.
     *
     * @param operator LIKE, ILIKE, =, !=, @> etc.
     * @return instance of {@link Operator}.
     * <p>
     * Most useful operators are collected there:
     * @see Operators
     */
    public static <T> Operator operator(T operator) {
        if (operator instanceof Operator)
            return (Operator) operator;

        return new Operator(String.valueOf(operator));
    }

    /**
     * Creates a keyword which ordinarily is used to build SQL query.
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

    /**
     * The type represents an index expression.
     * {@link ConflictTarget} type is a part of INSERT .. ON CONFLICT clause
     *
     * @param expression index expression
     * @return conflict target type
     */
    public static ConflictTarget targetExpression(String expression) {
        return targetExpression(expression, null, null);
    }

    /**
     * The type represents an index expression.
     * {@link ConflictTarget} type is a part of INSERT .. ON CONFLICT clause
     *
     * @param expression index expression
     * @param collation collation
     * @return conflict target type
     */
    public static ConflictTarget targetExpression(String expression, String collation) {
        return targetExpression(expression, collation, null);
    }

    /**
     * The type represents an index expression.
     * {@link ConflictTarget} type is a part of INSERT .. ON CONFLICT clause
     *
     * @param expression index expression
     * @param collation collation
     * @param opclass operator class
     * @return conflict target type
     */
    public static ConflictTarget targetExpression(String expression, String collation, String opclass) {
        return new ConflictTarget(expression, collation, opclass).markAsExpression();
    }

    /**
     * The type represents an index column.
     * {@link ConflictTarget} type is a part of INSERT .. ON CONFLICT clause
     *
     * @param column index expression
     * @return conflict target type
     */
    public static ConflictTarget targetColumn(String column) {
        return targetColumn(column, null, null);
    }

    /**
     * The type represents an index column.
     * {@link ConflictTarget} type is a part of INSERT .. ON CONFLICT clause
     *
     * @param column index expression
     * @param collation collation
     * @return conflict target type
     */
    public static ConflictTarget targetColumn(String column, String collation) {
        return targetColumn(column, collation, null);
    }

    /**
     * The type represents an index column.
     * {@link ConflictTarget} type is a part of INSERT .. ON CONFLICT clause
     *
     * @param column index expression
     * @param collation collation
     * @param opclass operator class
     * @return conflict target type
     */
    public static ConflictTarget targetColumn(String column, String collation, String opclass) {
        return new ConflictTarget(column, collation, opclass).markAsColumn();
    }

    /**
     * Creates an {@link OrderBy} object. It's used in SELECT .. ORDER BY clause.
     *
     * @param name column name
     * @return OrderBy type
     */
    public static OrderBy orderBy(String name) {
        return orderBy(name, null, null);
    }

    /**
     * Creates an {@link OrderBy} object. It's used in SELECT .. ORDER BY clause.
     *
     * @param name column name or expression
     * @param sorting sorting constant: ASC | DESC | USING constant
     * @return OrderBy type
     */
    public static OrderBy orderBy(String name, String sorting) {
        return orderBy(name, sorting, null);
    }

    /**
     * Creates an {@link OrderBy} object. It's used in SELECT .. ORDER BY clause.
     *
     * @param name column name or expression
     * @param sorting sorting constant: ASC | DESC | USING constant
     * @param nulls sets a position a row with null values for {@code name} column.
     *              It may be FIRST | LAST
     * @return OrderBy type
     */
    public static OrderBy orderBy(String name, String sorting, String nulls) {
        return new OrderBy(name, sorting, nulls);
    }
}
