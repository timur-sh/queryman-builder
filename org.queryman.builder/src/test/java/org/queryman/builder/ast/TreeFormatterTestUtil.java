/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.Query;
import org.queryman.builder.Queryman;
import org.queryman.builder.command.Conditions;

/**
 * @author Timur Shaidullin
 */
public class TreeFormatterTestUtil {
    private static AbstractSyntaxTree tree(Query query) {
        AbstractSyntaxTree tree = Queryman.getTree();
        query.assemble(tree);

        return tree;
    }

    public static String buildPreparedSQL(Query query)  {
        return new TreeFormatter().buildSQL(tree(query).getRootNode(), true);
    }

    public static String buildPreparedSQL(Conditions conditions) {
        AbstractSyntaxTree tree = Queryman.getTree();

        tree.startNode(NodesMetadata.EMPTY);
        conditions.assemble(tree);
        tree.endNode();
        return new TreeFormatter().buildSQL(tree.getRootNode(), true);
    }

    public static String buildSQL(Conditions conditions) {
        AbstractSyntaxTree tree = Queryman.getTree();

        tree.startNode(NodesMetadata.EMPTY);
        conditions.assemble(tree);
        tree.endNode();
        return new TreeFormatter().buildSQL(tree.getRootNode(), false);
    }

}
