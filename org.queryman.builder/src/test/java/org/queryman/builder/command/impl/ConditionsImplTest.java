/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.command.Conditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asNumber;
import static org.queryman.builder.PostgreSQL.asQuotedName;
import static org.queryman.builder.PostgreSQL.asString;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.operator;

/**
 * @author Timur Shaidullin
 */
public class ConditionsImplTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = new AbstractSyntaxTreeImpl();
    }

    @Test
    void simple() {
        Conditions conditions = new ConditionsImpl("id", "=", "3");
        conditions.assemble(ast);
        assertEquals("id = 3", ast.toString());
    }

    //----
    // AND
    //----

    @Test
    void and() {
        Conditions conditions = new ConditionsImpl("id", "=", "3");

        conditions.and("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 AND id2 = 2)", ast.toString());
    }

    @Test
    void andGroup() {
        Conditions conditions = new ConditionsImpl("id", "=", "3");

        conditions.and("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 AND id2 = 2)", ast.toString());
    }

    @Test
    void andGroupExpression() {
        Conditions conditions = new ConditionsImpl(asQuotedName("id"), operator("="), asNumber(3));

        conditions.and(asName("id2"), "=", asString("2"));
        conditions.assemble(ast);
        assertEquals("(\"id\" = 3 AND id2 = '2')", ast.toString());
    }

    @Test
    void andNotGroupExpression() {
        Conditions conditions = new ConditionsImpl(asQuotedName("id"), operator("="), asNumber(3));

        conditions.andNot(asName("id2"), "=", asString("2"));
        conditions.assemble(ast);
        assertEquals("(\"id\" = 3 AND NOT id2 = '2')", ast.toString());
    }

    //----
    // OR
    //----

    @Test
    void or() {
        Conditions conditions = new ConditionsImpl("id", "=", "3");

        conditions.or("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 OR id2 = 2)", ast.toString());
    }

    @Test
    void orGroup() {
        Conditions conditions = new ConditionsImpl("id", "=", "3");

        conditions.or("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 OR id2 = 2)", ast.toString());
    }

    @Test
    void orGroupExpression() {
        Conditions conditions = new ConditionsImpl(asQuotedName("id"), operator("="), asNumber(3));

        conditions.or(asName("id2"), "=", asString("2"));
        conditions.assemble(ast);
        assertEquals("(\"id\" = 3 OR id2 = '2')", ast.toString());
    }

    @Test
    void orNotGroupExpression() {
        Conditions conditions = new ConditionsImpl(asQuotedName("id"), operator("="), asNumber(3));

        conditions.orNot(asName("id2"), "=", asString("2"));
        conditions.assemble(ast);
        assertEquals("(\"id\" = 3 OR NOT id2 = '2')", ast.toString());
    }

    @Test
    void orGroupWithNestedGroup() {
        Conditions conditions = new ConditionsImpl("id", "=", "1");

        conditions.or("id2", "=", "2")
           .or(condition("id3", "=", "3")
              .or("id4", "=", "4")
           );
        conditions.assemble(ast);
        assertEquals("(id = 1 OR id2 = 2 OR (id3 = 3 OR id4 = 4))", ast.toString());
    }
}
