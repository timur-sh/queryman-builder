package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.command.select.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectImplTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = new AbstractSyntaxTreeImpl();
    }

    @Test
    void select() {
        Select select = new SelectImpl(ast, "id", "name", "min(price) as min");
        assertEquals("select id, name, min(price) as min", select.sql());
    }

}