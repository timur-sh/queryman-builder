package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.ast.NodesMetadata.FROM;
import static org.queryman.builder.ast.NodesMetadata.SELECT;
import static org.queryman.builder.ast.NodesMetadata.WHERE;

public class AbstractSyntaxTreeImplTest {
    @Test
    public void simpleTree() throws NoSuchFieldException, IllegalAccessException {
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
           .addLeaf(asName("id"))
           .endNode();

        assertThrows(BrokenTreeException.class, () -> tree.toString());

        tree.endNode();

        assertEquals("SELECT id, name, phone, date, email FROM table1 table2 WHERE id=id", tree.toString());

        Field field = tree.getClass().getDeclaredField("NODES");
        field.setAccessible(true);
        Stack<Node> nodes = (Stack<Node>) field.get(tree);
        assertEquals(nodes.size(), 0);
    }
}