package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;
import org.queryman.builder.PostgreSQL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.keyword;
import static org.queryman.builder.PostgreSQL.operator;
import static org.queryman.builder.PostgreSQL.qualifiedName;
import static org.queryman.builder.ast.NodeUtil.node;

class NodeImplTest {
    @Test
    void simpleTree() {
        Node node = node(NodesMetadata.SELECT).setDelimiter(",");

        node.addLeaf(qualifiedName("id"))
           .addLeaf(qualifiedName("name"))
           .setDelimiter(", ")
           .addChildNode(
              node(keyword("from"))
                 .addLeaf(qualifiedName("table1"))
                 .addChildNode(
                    node(keyword("left join"))
                       .addLeaf(qualifiedName("table2"))
                       .addChildNode(node(keyword("on"))
                          .addLeaf(qualifiedName("id"))
                          .addLeaf(operator("="))
                          .addLeaf(qualifiedName("id"))
                          .setDelimiter("")
                       )
                 )
           )
           .addChildNode(node(keyword("condition"))
              .addLeaf(qualifiedName("id"))
              .addLeaf(operator("="))
              .addLeaf(qualifiedName("id"))
           );

        TreeFormatter formatter = new TreeFormatter();
        assertEquals("SELECT id, name from table1 left join table2 on id=id condition id = id", formatter.treeToString(node));
    }
}