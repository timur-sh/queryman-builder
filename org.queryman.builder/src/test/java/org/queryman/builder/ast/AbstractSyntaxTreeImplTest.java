package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.queryman.builder.ast.Nodes.*;

public class AbstractSyntaxTreeImplTest {
    @Test
    public void select() {
        AbstractSyntaxTree tree = new AbstractSyntaxTreeImpl();

        tree.startNode(SELECT)
           .endNode(SELECT);

//        assertEquals(tree.toString(), "select id, name");
    }
}