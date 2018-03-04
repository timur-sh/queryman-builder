/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.PostgreSQL;
import org.queryman.builder.Query;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.command.clause.Join;
import org.queryman.builder.command.clause.OrderBy;
import org.queryman.builder.command.from.From;
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.command.select.SelectGroupByStep;
import org.queryman.builder.command.select.SelectHavingFirstStep;
import org.queryman.builder.command.select.SelectHavingStep;
import org.queryman.builder.command.select.SelectJoinOnStep;
import org.queryman.builder.command.select.SelectJoinOnStepsStep;
import org.queryman.builder.command.select.SelectJoinStep;
import org.queryman.builder.command.select.SelectLimitStep;
import org.queryman.builder.command.select.SelectOffsetStep;
import org.queryman.builder.command.select.SelectOrderByStep;
import org.queryman.builder.command.select.SelectWhereStep;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;
import org.queryman.builder.token.Token;
import org.queryman.builder.utils.Tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.queryman.builder.Keywords.EXCEPT;
import static org.queryman.builder.Keywords.EXCEPT_ALL;
import static org.queryman.builder.Keywords.EXCEPT_DISTINCT;
import static org.queryman.builder.Keywords.INTERSECT;
import static org.queryman.builder.Keywords.INTERSECT_ALL;
import static org.queryman.builder.Keywords.INTERSECT_DISTINCT;
import static org.queryman.builder.Keywords.UNION;
import static org.queryman.builder.Keywords.UNION_ALL;
import static org.queryman.builder.Keywords.UNION_DISTINCT;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asList;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.conditionExists;
import static org.queryman.builder.ast.NodesMetadata.EMPTY;
import static org.queryman.builder.ast.NodesMetadata.ON;
import static org.queryman.builder.ast.NodesMetadata.SELECT;
import static org.queryman.builder.ast.NodesMetadata.SELECT_ALL;
import static org.queryman.builder.ast.NodesMetadata.SELECT_DISTINCT;

/**
 * @author Timur Shaidullin
 */
public class SelectImpl extends AbstractQuery implements
   SelectFromStep,
   SelectJoinStep,
   SelectJoinOnStep,
   SelectJoinOnStepsStep,
   SelectWhereStep,
   SelectGroupByStep,
   SelectHavingFirstStep,
   SelectHavingStep,
   SelectOrderByStep,
   SelectLimitStep,
   SelectOffsetStep,
   SelectFinalStep {

    private final List<From>    FROM     = new LinkedList<>();
    private final List<Token>   GROUP_BY = new LinkedList<>();
    private final List<OrderBy> ORDER_BY = new LinkedList<>();

    private final List<CombiningQuery> COMBINING_QUERY = new LinkedList<>();

    private final Token[] COLUMNS_SELECTED;
    private       Token[] DISTINCT_COLUMNS;

    private Conditions wheres;
    private Conditions havings;
    private Stack<Join> joins = new Stack<>();

    private boolean join   = true;
    private boolean where  = false;
    private boolean having = false;

    private boolean selectAll      = false;
    private boolean selectDistinct = false;

    private Expression limit;
    private Expression offset;

    public SelectImpl(AbstractSyntaxTree ast, String... columnsSelected) {
        this(
           ast,
           Arrays.stream(columnsSelected)
              .map(PostgreSQL::asName)
              .collect(Collectors.toList())
        );
    }

    public SelectImpl(AbstractSyntaxTree ast, List<Expression> names) {
        this(ast, names.toArray(Tools.EMPTY_EXPRESSIONS));
    }

    public SelectImpl(AbstractSyntaxTree ast, Expression... columnsSelected) {
        super(ast);
        this.COLUMNS_SELECTED = columnsSelected;
    }

    /**
     * Clause SELECT ALL ...
     */
    public final SelectImpl all() {
        selectAll = true;
        return this;
    }

    /**
     * Clause SELECT DISTINCT ...
     */
    public final SelectImpl distinct() {
        selectDistinct = true;
        return this;
    }

    /**
     * Clause SELECT DISTINCT ON ( .. ) ...
     */
    public final SelectImpl distinctOn(String... columns) {
        distinctOn(Arrays.stream(columns).map(PostgreSQL::asName).toArray(Expression[]::new));
        return this;
    }

    /**
     * Clause SELECT DISTINCT ON ( .. ) ...
     */
    public final SelectImpl distinctOn(Expression... columns) {
        selectDistinct = true;
        DISTINCT_COLUMNS = columns;
        return this;
    }

    @Override
    public final void assemble(AbstractSyntaxTree tree) {
        if (selectAll)
            tree.startNode(SELECT_ALL, ", ");
        else if (selectDistinct) {
            tree.startNode(SELECT_DISTINCT, ", ");
            if (DISTINCT_COLUMNS != null && DISTINCT_COLUMNS.length > 0)
                tree.startNode(ON, "")
                   .addLeaves(asList(DISTINCT_COLUMNS))
                   .endNode();
        } else
            tree.startNode(SELECT);

        tree.startNode(EMPTY, ", ").addLeaves(COLUMNS_SELECTED).endNode();

        if (!FROM.isEmpty()) {
            tree.startNode(NodesMetadata.FROM.setJoinNodes(true), ", ");

            for (From from : FROM)
                tree.peek(from);

            tree.endNode();
        }

        if (joins.size() > 0)
            for (Join join1 : joins)
                tree.peek(join1);

        if (wheres != null)
            tree.startNode(NodesMetadata.WHERE)
               .peek(wheres)
               .endNode();

        if (!GROUP_BY.isEmpty())
            tree.startNode(NodesMetadata.GROUP_BY, ", ")
               .addLeaves(GROUP_BY)
               .endNode();

        if (havings != null)
            tree.startNode(NodesMetadata.HAVING)
               .peek(havings)
               .endNode();

        //todo window

        if (!COMBINING_QUERY.isEmpty())
            for (CombiningQuery q : COMBINING_QUERY)
                tree.peek(q);

        if (!ORDER_BY.isEmpty()) {
            tree.startNode(NodesMetadata.ORDER_BY);

            for (OrderBy orderBy : ORDER_BY)
                tree.peek(orderBy);

            tree.endNode();
        }

        if (limit != null)
            tree.startNode(NodesMetadata.LIMIT)
               .addLeaf(limit)
               .endNode();

        if (offset != null)
            tree.startNode(NodesMetadata.OFFSET)
               .addLeaf(offset)
               .endNode();

        tree.endNode();
    }

    private void resetToWhere() {
        where = true;
        join = false;
        having = false;
    }

    private void resetToJoin() {
        where = false;
        join = true;
        having = false;
    }

    private void resetToHaving() {
        where = false;
        join = false;
        having = true;
    }

    //--
    // FROM API
    //--

    @Override
    public final SelectImpl from(String... tables) {
        from(Arrays.stream(tables)
           .map(PostgreSQL::asName)
           .toArray(Expression[]::new)
        );
        return this;
    }

    @Override
    public final SelectImpl from(Expression... tables) {
        from(Arrays.stream(tables).map(FromImpl::new).toArray(From[]::new));
        return this;
    }

    @Override
    public final SelectImpl from(From... tables) {
        FROM.clear();
        FROM.addAll(List.of(tables));
        return this;
    }

    //--
    // WHERE API
    //--

    @Override
    public final SelectImpl where(String left, String operator, String right) {
        where(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl where(Expression left, Operator operator, Expression right) {
        where(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl where(Expression field, Operator operator, Query query) {
        where(condition(field, operator, query));
        return this;
    }

    @Override
    public final SelectImpl where(Conditions conditions) {
        resetToWhere();
        this.wheres = new ConditionsImpl(conditions);

        return this;
    }

    @Override
    public final SelectImpl whereExists(Query query) {
        where(conditionExists(query));

        return this;
    }

    @Override
    public final SelectImpl and(String left, String operator, String right) {
        and(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl and(Expression left, Operator operator, Expression right) {
        and(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl and(Expression field, Operator operator, Query query) {
        and(condition(field, operator, query));
        return this;
    }

    @Override
    public final SelectImpl and(Conditions conditions) {
        if (where)
            this.wheres.and(conditions);
        else if (join)
            joins.peek().getConditions().and(conditions);
        else if (having)
            havings.and(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final SelectImpl andExists(Query query) {
        and(conditionExists(query));
        return this;
    }

    @Override
    public final SelectImpl andNot(String left, String operator, String right) {
        andNot(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl andNot(Expression left, Operator operator, Expression right) {
        andNot(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl andNot(Expression field, Operator operator, Query query) {
        andNot(condition(field, operator, query));
        return this;
    }

    @Override
    public final SelectImpl andNot(Conditions conditions) {
        if (where)
            this.wheres.andNot(conditions);
        else if (join)
            joins.peek().getConditions().andNot(conditions);
        else if (having)
            havings.andNot(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final SelectImpl andNotExists(Query query) {
        andNot(conditionExists(query));
        return this;
    }

    @Override
    public final SelectImpl or(String left, String operator, String right) {
        or(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl or(Expression left, Operator operator, Expression right) {
        or(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl or(Expression field, Operator operator, Query query) {
        or(condition(field, operator, query));
        return this;
    }

    @Override
    public final SelectImpl or(Conditions conditions) {
        if (where)
            this.wheres.or(conditions);
        else if (join)
            joins.peek().getConditions().or(conditions);
        else if (having)
            havings.or(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final SelectImpl orExists(Query query) {
        or(conditionExists(query));
        return this;
    }

    @Override
    public final SelectImpl orNot(String left, String operator, String right) {
        orNot(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl orNot(Conditions conditions) {
        if (where)
            this.wheres.orNot(conditions);
        else if (join)
            joins.peek().getConditions().orNot(conditions);
        else if (having)
            havings.orNot(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final SelectImpl orNotExists(Query query) {
        orNot(conditionExists(query));
        return this;
    }

    @Override
    public final SelectImpl orNot(Expression left, Operator operator, Expression right) {
        orNot(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl orNot(Expression field, Operator operator, Query query) {
        orNot(condition(field, operator, query));
        return this;
    }

    //----
    // GROUP BY API
    //----

    @Override
    public final SelectImpl groupBy(String... expressions) {
        Expression[] expr = Arrays.stream(expressions)
           .map(PostgreSQL::asName)
           .toArray(Expression[]::new);

        return groupBy(expr);
    }

    @Override
    public final SelectImpl groupBy(Expression... expressions) {
        GROUP_BY.clear();
        GROUP_BY.addAll(List.of(expressions));
        return this;
    }

    //----
    // ORDER BY API
    //----

    @Override
    public final SelectImpl orderBy(String column) {
        orderBy(column, null, null);
        return this;
    }

    @Override
    public final SelectImpl orderBy(String column, String sorting) {
        orderBy(column, sorting, null);
        return this;
    }

    @Override
    public final SelectImpl orderBy(String column, String sorting, String nulls) {
        ORDER_BY.clear();
        ORDER_BY.add(new OrderBy(column, sorting, nulls));
        return this;
    }

    @Override
    public final SelectImpl limit(long limit) {
        this.limit = asConstant(limit);
        return this;
    }

    @Override
    public final SelectImpl offset(long offset) {
        this.offset = asConstant(offset);
        return this;
    }

    //----
    // JOIN API
    //----

    @Override
    public final SelectImpl join(String name) {
        join(asName(name));
        return this;
    }

    @Override
    public final SelectImpl join(Expression name) {
        resetToJoin();
        joins.add(new Join(name, NodesMetadata.JOIN));

        return this;
    }

    @Override
    public final SelectImpl innerJoin(String name) {
        innerJoin(asName(name));
        return this;
    }

    @Override
    public final SelectImpl innerJoin(Expression name) {
        resetToJoin();
        joins.add(new Join(name, NodesMetadata.INNER_JOIN));

        return this;
    }

    @Override
    public final SelectImpl leftJoin(String name) {
        return leftJoin(asName(name));
    }

    @Override
    public final SelectImpl leftJoin(Expression name) {
        resetToJoin();
        joins.add(new Join(name, NodesMetadata.LEFT_JOIN));

        return this;
    }

    @Override
    public final SelectImpl rightJoin(String name) {
        return rightJoin(asName(name));
    }

    @Override
    public final SelectImpl rightJoin(Expression name) {
        resetToJoin();
        joins.add(new Join(name, NodesMetadata.RIGHT_JOIN));

        return this;
    }

    @Override
    public final SelectImpl fullJoin(String name) {
        return fullJoin(asName(name));
    }

    @Override
    public final SelectImpl fullJoin(Expression name) {
        resetToJoin();
        joins.add(new Join(name, NodesMetadata.FULL_JOIN));

        return this;
    }

    @Override
    public final SelectImpl crossJoin(String name) {
        return crossJoin(asName(name));
    }

    @Override
    public final SelectImpl crossJoin(Expression name) {
        resetToJoin();
        joins.add(new Join(name, NodesMetadata.CROSS_JOIN));

        return this;
    }

    @Override
    public final SelectImpl naturalJoin(String name) {
        return naturalJoin(asName(name));
    }

    @Override
    public final SelectImpl naturalJoin(Expression name) {
        resetToJoin();
        joins.add(new Join(name, NodesMetadata.NATURAL_JOIN));

        return this;
    }

    @Override
    public final SelectImpl using(String... name) {
        return using(Arrays.stream(name).map(PostgreSQL::asName).toArray(Expression[]::new));
    }

    @Override
    public final SelectImpl using(Expression... columns) {
        joins.peek().using(columns);
        return this;
    }

    @Override
    public final SelectImpl on(boolean conditions) {
        resetToJoin();
        joins.peek().setConditions(conditions);
        return this;
    }

    @Override
    public final SelectImpl on(String left, String operator, String right) {
        return on(condition(left, operator, right));
    }

    @Override
    public final SelectImpl on(Expression left, Operator operator, Expression right) {
        return on(condition(left, operator, right));
    }

    @Override
    public final SelectImpl on(Expression field, Operator operator, Query query) {
        return on(condition(field, operator, query));
    }

    @Override
    public final SelectImpl on(Conditions conditions) {
        joins.peek().setConditions(conditions);
        return this;
    }

    @Override
    public final SelectImpl onExists(Query query) {
        resetToJoin();
        joins.peek().setConditions(conditionExists(query));
        return this;
    }

    @Override
    public final SelectImpl union(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(UNION, select));
        return this;
    }

    @Override
    public final SelectImpl unionAll(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(UNION_ALL, select));
        return this;
    }

    @Override
    public final SelectImpl unionDistinct(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(UNION_DISTINCT, select));
        return this;
    }

    @Override
    public final SelectImpl intersect(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(INTERSECT, select));
        return this;
    }

    @Override
    public final SelectImpl intersectAll(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(INTERSECT_ALL, select));
        return this;
    }

    @Override
    public final SelectImpl intersectDistinct(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(INTERSECT_DISTINCT, select));
        return this;
    }

    @Override
    public final SelectImpl except(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(EXCEPT, select));
        return this;
    }

    @Override
    public final SelectImpl exceptAll(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(EXCEPT_ALL, select));
        return this;
    }

    @Override
    public final SelectImpl exceptDistinct(SelectFinalStep select) {
        COMBINING_QUERY.add(new CombiningQuery(EXCEPT_DISTINCT, select));
        return this;
    }

    //----
    // HAVING API
    //----

    @Override
    public final SelectImpl having(String left, String operator, String right) {
        having(condition(left, operator, right));
        return this;
    }

    @Override
    public final SelectImpl having(Expression left, Operator operator, Expression right) {
        having(condition(left, operator, right));
        return this;
    }

    @Override
    public final SelectImpl having(Expression field, Operator operator, Query query) {
        having(condition(field, operator, query));
        return this;
    }

    @Override
    public final SelectImpl having(Conditions conditions) {
        resetToHaving();
        this.havings = new ConditionsImpl(conditions);
        return this;
    }

    @Override
    public final SelectImpl havingExists(Query query) {
        having(conditionExists(query));
        return this;
    }
}
