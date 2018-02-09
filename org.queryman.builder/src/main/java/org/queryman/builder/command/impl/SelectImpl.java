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
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.command.select.SelectFromManySteps;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.command.select.SelectGroupByStep;
import org.queryman.builder.command.select.SelectLimitStep;
import org.queryman.builder.command.select.SelectOffsetStep;
import org.queryman.builder.command.select.SelectOrderByStep;
import org.queryman.builder.command.select.SelectWhereManySteps;
import org.queryman.builder.command.select.SelectWhereStep;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;
import org.queryman.builder.token.Token;
import org.queryman.builder.utils.Tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.queryman.builder.PostgreSQL.asNumber;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.conditionExists;
import static org.queryman.builder.ast.NodesMetadata.SELECT;

/**
 * @author Timur Shaidullin
 */
public class SelectImpl extends AbstractQuery implements
   SelectFromStep,
   SelectFromManySteps,
   SelectWhereStep,
   SelectWhereManySteps,
   SelectGroupByStep,
   SelectOrderByStep,
   SelectLimitStep,
   SelectOffsetStep,
   SelectFinalStep {

    private final List<Token>   FROM     = new LinkedList<>();
    private final List<Token>   GROUP_BY = new LinkedList<>();
    private final List<OrderBy> ORDER_BY = new LinkedList<>();

    private final Token[] COLUMNS_SELECTED;

    private Conditions conditions;
    private Expression limit;
    private Expression   offset;

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

    @Override
    public final void assemble(AbstractSyntaxTree tree) {
        tree.startNode(SELECT, ", ")
           .addLeaves(COLUMNS_SELECTED);

        if (!FROM.isEmpty())
            tree.startNode(NodesMetadata.FROM, ", ")
               .addLeaves(FROM)
               .endNode();

        if (conditions != null)
            tree.startNode(NodesMetadata.WHERE)
               .peek(conditions)
               .endNode();

        if (!GROUP_BY.isEmpty())
            tree.startNode(NodesMetadata.GROUP_BY, ", ")
               .addLeaves(GROUP_BY)
               .endNode();

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

    //--
    // FROM API
    //--

    @Override
    public final SelectImpl from(String... tables) {
        FROM.clear();
        FROM.addAll(
           Arrays.stream(tables)
              .map(PostgreSQL::asName)
              .collect(Collectors.toList())
        );
        return this;
    }

    @Override
    public final SelectImpl from(Expression... tables) {
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
        this.conditions = condition(field, operator, query);
        return this;
    }

    @Override
    public final SelectImpl where(Conditions conditions) {
        this.conditions = new ConditionsImpl(conditions);

        return this;
    }

    @Override
    public final SelectImpl whereExists(Query query) {
        this.conditions = conditionExists(query);

        return this;
    }

    @Override
    public final SelectImpl whereBetween(String field, String value1, String value2) {
        whereBetween(PostgreSQL.between(field, value1, value2));
        return this;
    }

    @Override
    public final SelectImpl whereBetween(Expression field, Expression value1, Expression value2) {
        whereBetween(PostgreSQL.between(field, value1, value2));
        return this;
    }

    @Override
    public final SelectImpl whereBetween(Conditions conditions) {
        where(conditions);

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
        conditions.and(field, operator, query);
        return this;
    }

    @Override
    public final SelectImpl and(Conditions conditions) {
        this.conditions.and(conditions);

        return this;
    }

    @Override
    public final SelectImpl andExists(Query query) {
        conditions.andExists(query);
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
    public SelectWhereStep andNot(Expression field, Operator operator, Query query) {
        conditions.andNot(field, operator, query);
        return this;
    }

    @Override
    public final SelectImpl andNot(Conditions conditions) {
        this.conditions.andNot(conditions);

        return this;
    }

    @Override
    public SelectWhereStep andNotExists(Query query) {
        conditions.andNotExists(query);
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
    public SelectWhereStep or(Expression field, Operator operator, Query query) {
        conditions.or(field, operator, query);
        return this;
    }

    @Override
    public final SelectImpl or(Conditions conditions) {
        this.conditions.or(conditions);

        return this;
    }

    @Override
    public SelectWhereStep orExists(Query query) {
        conditions.orExists(query);
        return this;
    }

    @Override
    public final SelectImpl orNot(String left, String operator, String right) {
        orNot(condition(left, operator, right));

        return this;
    }

    @Override
    public final SelectImpl orNot(Conditions conditions) {
        this.conditions.orNot(conditions);
        return this;
    }

    @Override
    public SelectWhereStep orNotExists(Query query) {
        conditions.orNotExists(query);
        return this;
    }

    @Override
    public final SelectImpl orNot(Expression left, Operator operator, Expression right) {
        orNot(condition(left, operator, right));

        return this;
    }

    @Override
    public SelectWhereStep orNot(Expression field, Operator operator, Query query) {
        conditions.orNot(field, operator, query);
        return this;
    }

    //--
    // GROUP BY API
    //--

    @Override
    public final SelectImpl groupBy(String... expressions) {
        GROUP_BY.addAll(
           Arrays.stream(expressions)
              .map(PostgreSQL::asName)
              .collect(Collectors.toList())
        );
        return this;
    }

    //--
    // ORDER BY API
    //--

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
        this.limit = asNumber(limit);
        return this;
    }

    @Override
    public final SelectImpl offset(long offset) {
        this.offset = asNumber(offset);
        return this;
    }
}
