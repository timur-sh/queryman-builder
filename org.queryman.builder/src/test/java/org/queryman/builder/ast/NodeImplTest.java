package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;
import org.queryman.builder.PostgreSQL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.keyword;
import static org.queryman.builder.PostgreSQL.operator;
import static org.queryman.builder.ast.NodeUtil.node;

class NodeImplTest {
    @Test
    void simpleTree() {
        Node node = node(NodesMetadata.SELECT).setDelimiter(",");

        node.addLeaf(asConstant("id"))
           .addLeaf(asConstant("name"))
           .setDelimiter(", ")
           .addChildNode(
              node(keyword("from"))
                 .addLeaf(asConstant("table1"))
                 .addChildNode(
                    node(keyword("left join"))
                       .addLeaf(asConstant("table2"))
                       .addChildNode(node(keyword("on"))
                          .addLeaf(asConstant("id"))
                          .addLeaf(operator("="))
                          .addLeaf(asConstant("id"))
                          .setDelimiter("")
                       )
                 )
           )
           .addChildNode(node(keyword("condition"))
              .addLeaf(asConstant("id"))
              .addLeaf(operator("="))
              .addLeaf(asConstant("id"))
           );

        TreeFormatter formatter = new TreeFormatter();
        assertEquals("SELECT id, name from table1 left join table2 on id=id condition id = id", formatter.treeToString(node));
    }
}