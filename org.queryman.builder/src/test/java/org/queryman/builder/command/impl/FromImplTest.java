package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.PostgreSQL;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.from.From;
import org.queryman.builder.command.from.FromFirstStep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.from;
import static org.queryman.builder.PostgreSQL.fromOnly;

class FromImplTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = PostgreSQL.getTree();
    }

    void assembleAst(From from) {
        ast.startNode(NodesMetadata.FROM, ", ");
        from.assemble(ast);
        ast.endNode();
    }

    @Test
    void fromTest() {
        FromFirstStep from = from("books");
        assembleAst(from);
        assertEquals("FROM books", ast.toString());
    }

    @Test
    void fromTablesample() {
        FromFirstStep from = from("books");
        from.tablesample("SYSTEM", "30");
        assembleAst(from);
        assertEquals("FROM books TABLESAMPLE SYSTEM(30)", ast.toString());
    }

    @Test
    void fromTablesampleRepeatable() {
        FromFirstStep from = from("books");
        from.tablesample("BERNOULLI", "1")
           .repeatable(1);

        assembleAst(from);
        assertEquals("FROM books TABLESAMPLE BERNOULLI(1) REPEATABLE(1)", ast.toString());
    }

    @Test
    void fromOnlyTest() {
        FromFirstStep from = fromOnly("books");
        assembleAst(from);
        assertEquals("FROM ONLY books", ast.toString());
    }

    @Test
    void fromAs() {
        FromFirstStep from = from("books");
        from.as("b");
        assembleAst(from);
        assertEquals("FROM books AS b", ast.toString());
    }
}