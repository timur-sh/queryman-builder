/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.PostgreSQL;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.command.Conditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Operators.GT;
import static org.queryman.builder.Operators.GTE;
import static org.queryman.builder.Operators.LT;
import static org.queryman.builder.Operators.LTE;
import static org.queryman.builder.PostgreSQL.asConstant;
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
        Conditions conditions = PostgreSQL.condition("id", "=", "3");
        conditions.assemble(ast);
        assertEquals("id = 3", ast.toString());
    }

    //----
    // AND
    //----

    @Test
    void and() {
        Conditions conditions = PostgreSQL.condition("id", "=", "3");

        conditions.and("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 AND id2 = 2)", ast.toString());

        conditions.and(asName("id3"), "!=", asConstant("3"));
        conditions.assemble(ast);
        assertEquals("(id = 3 AND id2 = 2 AND id3 != 3)", ast.toString());

        conditions.and(asName("id4"), GT, asConstant("4"));
        conditions.assemble(ast);
        assertEquals("(id = 3 AND id2 = 2 AND id3 != 3 AND id4 > 4)", ast.toString());

        conditions.and(condition(asName("id5"), GTE, asConstant("5")));
        conditions.assemble(ast);
        assertEquals("(id = 3 AND id2 = 2 AND id3 != 3 AND id4 > 4 AND id5 >= 5)", ast.toString());
    }

    @Test
    void andNot() {
        Conditions conditions = PostgreSQL.condition("id", "=", "3");

        conditions.andNot("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 AND NOT id2 = 2)", ast.toString());

        conditions.andNot(asName("id3"), "!=", asConstant("3"));
        conditions.assemble(ast);
        assertEquals("(id = 3 AND NOT id2 = 2 AND NOT id3 != 3)", ast.toString());

        conditions.andNot(asName("id4"), LT, asConstant("4"));
        conditions.assemble(ast);
        assertEquals("(id = 3 AND NOT id2 = 2 AND NOT id3 != 3 AND NOT id4 < 4)", ast.toString());

        conditions.andNot(condition(asName("id5"), LTE, asConstant("5")));
        conditions.assemble(ast);
        assertEquals("(id = 3 AND NOT id2 = 2 AND NOT id3 != 3 AND NOT id4 < 4 AND NOT id5 <= 5)", ast.toString());
    }

    @Test
    void andGroup() {
        Conditions conditions = PostgreSQL.condition("id", "=", "3");

        conditions.and("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 AND id2 = 2)", ast.toString());
    }

    @Test
    void andGroupExpression() {
        Conditions conditions = PostgreSQL.condition(asQuotedName("id"), operator("="), asNumber(3));

        conditions.and(asName("id2"), "=", asString("2"));
        conditions.assemble(ast);
        assertEquals("(\"id\" = 3 AND id2 = '2')", ast.toString());
    }

    @Test
    void andNotGroupExpression() {
        Conditions conditions = PostgreSQL.condition(asQuotedName("id"), operator("="), asNumber(3));

        conditions.andNot(asName("id2"), "=", asString("2"));
        conditions.assemble(ast);
        assertEquals("(\"id\" = 3 AND NOT id2 = '2')", ast.toString());
    }

    //----
    // OR
    //----

    @Test
    void or() {
        Conditions conditions = PostgreSQL.condition("id", "<>", "3");

        conditions.or("id2", "<>", "2");
        conditions.assemble(ast);
        assertEquals("(id <> 3 OR id2 <> 2)", ast.toString());

        conditions.or(asName("id3"), "!=", asConstant("3"));
        conditions.assemble(ast);
        assertEquals("(id <> 3 OR id2 <> 2 OR id3 != 3)", ast.toString());

        conditions.or(asName("id4"), GT, asConstant("4"));
        conditions.assemble(ast);
        assertEquals("(id <> 3 OR id2 <> 2 OR id3 != 3 OR id4 > 4)", ast.toString());

        conditions.or(condition(asName("id5"), GTE, asConstant("5")));
        conditions.assemble(ast);
        assertEquals("(id <> 3 OR id2 <> 2 OR id3 != 3 OR id4 > 4 OR id5 >= 5)", ast.toString());
    }

    @Test
    void orNot() {
        Conditions conditions = PostgreSQL.condition("id", "=", "3");

        conditions.orNot("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 OR NOT id2 = 2)", ast.toString());

        conditions.orNot(asName("id3"), "!=", asConstant("3"));
        conditions.assemble(ast);
        assertEquals("(id = 3 OR NOT id2 = 2 OR NOT id3 != 3)", ast.toString());

        conditions.orNot(asName("id4"), LT, asConstant("4"));
        conditions.assemble(ast);
        assertEquals("(id = 3 OR NOT id2 = 2 OR NOT id3 != 3 OR NOT id4 < 4)", ast.toString());

        conditions.orNot(condition(asName("id5"), LTE, asConstant("5")));
        conditions.assemble(ast);
        assertEquals("(id = 3 OR NOT id2 = 2 OR NOT id3 != 3 OR NOT id4 < 4 OR NOT id5 <= 5)", ast.toString());
    }

    @Test
    void orGroup() {
        Conditions conditions = PostgreSQL.condition("id", "=", "3");

        conditions.or("id2", "=", "2");
        conditions.assemble(ast);
        assertEquals("(id = 3 OR id2 = 2)", ast.toString());
    }

    @Test
    void orGroupExpression() {
        Conditions conditions = PostgreSQL.condition(asQuotedName("id"), operator("="), asNumber(3));

        conditions.or(asName("id2"), "=", asString("2"));
        conditions.assemble(ast);
        assertEquals("(\"id\" = 3 OR id2 = '2')", ast.toString());
    }

    @Test
    void orNotGroupExpression() {
        Conditions conditions = PostgreSQL.condition(asQuotedName("id"), operator("="), asNumber(3));

        conditions.orNot(asName("id2"), "=", asString("2"));
        conditions.assemble(ast);
        assertEquals("(\"id\" = 3 OR NOT id2 = '2')", ast.toString());
    }

    @Test
    void orGroupWithNestedGroup() {
        Conditions conditions = PostgreSQL.condition("id", "=", "1");

        conditions.or("id2", "=", "2")
           .or(condition("id3", "=", "3")
              .or("id4", "=", "4")
           );
        conditions.assemble(ast);
        assertEquals("(id = 1 OR id2 = 2 OR (id3 = 3 OR id4 = 4))", ast.toString());
    }
}
