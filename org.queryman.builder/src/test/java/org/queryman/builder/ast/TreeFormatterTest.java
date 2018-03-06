package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.ast.NodesMetadata.FROM;
import static org.queryman.builder.ast.NodesMetadata.SELECT;
import static org.queryman.builder.ast.NodesMetadata.WHERE;

class TreeFormatterTest {
    @Test
    void buildPreparedSQLTest() {
        AbstractSyntaxTree tree = new AbstractSyntaxTreeImpl();

        tree.startNode(SELECT, ", ");
        tree.addLeaf(asName("id"));
        tree.addLeaf(asName("name"));
        tree.addLeaf(asName("phone"));

        tree.addLeaves(asName("date"), asName("email"));

        tree.startNode(FROM);
        tree.addLeaf(asName("table1")).addLeaf(asName("table2")).setDelimiter(" ");
        tree.endNode();

        tree.startNode(WHERE, "")
           .addLeaf(asName("id"))
           .addLeaf(asName("="))
           .addLeaf(asConstant(1))
           .endNode();

        tree.endNode();

        TreeFormatter treeFormatter = new TreeFormatter();

        assertEquals("SELECT id, name, phone, date, email FROM table1 table2 WHERE id=?", treeFormatter.buildSQL(tree.getRootNode(), true));

    }
}