/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.Select;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.command.select.SelectFromManySteps;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.command.select.SelectGroupByStep;
import org.queryman.builder.command.select.SelectWhereStep;
import org.queryman.builder.command.where.WhereGroup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private final List<String>           FROM        = new LinkedList<>();
    private final List<String>           GROUP_BY    = new LinkedList<>();
    private final List<Where>            WHERE       = new LinkedList<>();
    private final Map<Where, WhereGroup> WHERE_GROUP = new HashMap<>();


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

            for (Where where : WHERE) {
                if (WHERE_GROUP.containsKey(where)) {
                    if (where.getToken() == null) {
                        tree.peek(WHERE_GROUP.get(where));

                    } else {
                        tree.startNode(where.getToken())
                           .peek(WHERE_GROUP.get(where))
                           .endNode();
                    }

                } else if (where.getToken() == null) {
                    tree.addLeaves(where.getLeftValue(), where.getOperator(), where.getRightValue());

                } else {
                    tree.startNode(where.getToken())
                       .addLeaves(where.getLeftValue(), where.getOperator(), where.getRightValue())
                       .endNode();
                }
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
        WHERE.clear();
        WHERE.add(CommandUtils.where(left, operator, right));
        return this;
    }

    @Override
    public SelectWhereStep where(WhereGroup whereGroup) {
        WHERE.clear();
        Where where = CommandUtils.stubWhere(null);
        WHERE.add(where);
        WHERE_GROUP.put(where, whereGroup);
        return this;
    }

    @Override
    public final SelectImpl andWhere(String left, String operator, String right) {
        WHERE.add(CommandUtils.andWhere(left, operator, right));
        return this;
    }

    @Override
    public final SelectImpl orWhere(String left, String operator, String right) {
        WHERE.add(CommandUtils.orWhere(left, operator, right));
        return this;
    }

    @Override
    public SelectWhereStep andWhere(WhereGroup whereGroup) {
        Where where = CommandUtils.stubWhere(AND);
        WHERE.add(where);
        WHERE_GROUP.put(where, whereGroup);
        return this;
    }

    @Override
    public SelectWhereStep orWhere(WhereGroup whereGroup) {
        Where where = CommandUtils.stubWhere(OR);
        WHERE.add(where);
        WHERE_GROUP.put(where, whereGroup);
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
