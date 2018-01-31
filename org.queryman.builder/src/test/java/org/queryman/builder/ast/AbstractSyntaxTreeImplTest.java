package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.queryman.builder.ast.NodesMetadata.*;

public class AbstractSyntaxTreeImplTest {
    @Test
    public void simpleTree() throws NoSuchFieldException, IllegalAccessException {
        AbstractSyntaxTree tree = new AbstractSyntaxTreeImpl();

        tree.startNode(SELECT, ", ");
        tree.addLeaf("id");
        tree.addLeaf("name");
        tree.addLeaf("phone");

        tree.addLeaves("date", "email");

        tree.startNode(FROM);
        tree.addLeaf("table1").addLeaf("table2").setDelimiter(" ");
        tree.endNode();

        tree.startNode(WHERE, "")
           .addLeaf("id")
           .addLeaf("=")
           .addLeaf("id")
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