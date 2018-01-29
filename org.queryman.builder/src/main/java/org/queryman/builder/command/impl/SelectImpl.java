/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.Select;
import org.queryman.builder.Statements;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.command.select.SelectFromManySteps;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.command.select.SelectGroupByStep;
import org.queryman.builder.command.select.SelectWhereStep;
import org.queryman.builder.command.where.Conditions;

import java.util.LinkedList;
import java.util.List;

import static org.queryman.builder.Statements.condition;
import static org.queryman.builder.ast.NodesMetadata.AND;
import static org.queryman.builder.ast.NodesMetadata.OR;
import static org.queryman.builder.ast.NodesMetadata.SELECT;

/**
 * @author Timur Shaidullin
 */
public class SelectImpl extends AbstractQuery implements
   SelectFromStep,
   SelectFromManySteps,
   SelectWhereStep,
   SelectGroupByStep,
   SelectFinalStep,
   Select {

    private final String[] COLUMNS_SELECTED;
    private final List<String>     FROM     = new LinkedList<>();
    private final List<String>     GROUP_BY = new LinkedList<>();
    private final List<Conditions> WHERE    = new LinkedList<>();
//    private final Map<ConditionItem, Condition> WHERE_GROUP = new HashMap<>();


    public SelectImpl(
       AbstractSyntaxTree ast,
       String... columnsSelected
    ) {
        super(ast);
        this.COLUMNS_SELECTED = columnsSelected;
    }

    @Override
    public final void assemble(AbstractSyntaxTree tree) {
        tree.startNode(SELECT, ", ")
           .addLeaves(COLUMNS_SELECTED);

        if (!FROM.isEmpty()) {
            tree.startNode(NodesMetadata.FROM, ", ")
               .addLeaves(FROM)
               .endNode();
        }

        if (!WHERE.isEmpty()) {
            tree.startNode(NodesMetadata.WHERE);

            for (Conditions condition : WHERE) {
                tree.peek(condition);
            }

            tree.endNode();
        }

        if (!GROUP_BY.isEmpty()) {
            tree.startNode(NodesMetadata.GROUP_BY, ", ")
               .addLeaves(GROUP_BY)
               .endNode();
        }

        tree.endNode();
    }

    //--
    // FROM API
    //--

    @Override
    public final SelectImpl from(String... tables) {
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
    public SelectImpl where(Conditions conditions) {
        WHERE.clear();
        WHERE.add(conditions);

        return this;
    }

    @Override
    public final SelectImpl and(String left, String operator, String right) {
        and(condition(left, operator, right));

        return this;
    }

    @Override
    public SelectWhereStep and(Conditions conditions) {
        WHERE.add(new ConditionsImpl(AND, conditions));

        return this;
    }

    @Override
    public final SelectImpl or(String left, String operator, String right) {
        or(condition(left, operator, right));

        return this;
    }

    @Override
    public SelectWhereStep or(Conditions conditions) {
        WHERE.add(new ConditionsImpl(OR, conditions));

        return this;
    }

    //--
    // GROUP BY API
    //--

    @Override
    public SelectFinalStep groupBy(String... expressions) {
        GROUP_BY.addAll(List.of(expressions));
        return this;
    }
}
