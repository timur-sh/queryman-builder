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
import org.queryman.builder.Query;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.Conditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.Operators.GT;
import static org.queryman.builder.Operators.GTE;
import static org.queryman.builder.Operators.IN;
import static org.queryman.builder.Operators.LIKE;
import static org.queryman.builder.Operators.LT;
import static org.queryman.builder.Operators.LTE;
import static org.queryman.builder.Operators.NOT_IN;
import static org.queryman.builder.Operators.NOT_LIKE;
import static org.queryman.builder.PostgreSQL.asArray;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asFunc;
import static org.queryman.builder.PostgreSQL.asList;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asQuotedName;
import static org.queryman.builder.PostgreSQL.asSubQuery;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.conditionAll;
import static org.queryman.builder.PostgreSQL.conditionAny;
import static org.queryman.builder.PostgreSQL.conditionBetween;
import static org.queryman.builder.PostgreSQL.conditionExists;
import static org.queryman.builder.PostgreSQL.conditionSome;
import static org.queryman.builder.PostgreSQL.operator;
import static org.queryman.builder.PostgreSQL.select;

/**
 * @author Timur Shaidullin
 */
public class ConditionsImplTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = PostgreSQL.getTree();
    }

    void assembleAst(Conditions conditions) {
        ast.startNode(NodesMetadata.WHERE);
        conditions.assemble(ast);
        ast.endNode();
    }

    @Test
    void simple() {
        Conditions conditions = condition("id", "=", "3");
        assembleAst(conditions);
        assertEquals("WHERE id = 3", ast.toString());

        Conditions conditions1 = condition(asName("id"), EQUAL, asSubQuery(select("max(sum)")).as("sum"));
        assembleAst(conditions1);
        assertEquals("WHERE id = (SELECT max(sum)) AS sum", ast.toString());
    }

    //----
    // AND
    //----

    @Test
    void and() {
        Conditions conditions = condition("id", "=", "3");

        conditions.and("id2", "=", "2");
        assembleAst(conditions);
        assertEquals("WHERE id = 3 AND id2 = 2", ast.toString());

        conditions.and(asName("id4"), GT, asConstant(4));
        assembleAst(conditions);
        assertEquals("WHERE id = 3 AND id2 = 2 AND id4 > 4", ast.toString());

        conditions.and(condition(asName("id5"), GTE, asConstant(5)));
        assembleAst(conditions);
        assertEquals("WHERE id = 3 AND id2 = 2 AND id4 > 4 AND id5 >= 5", ast.toString());
    }

    @Test
    void andNot() {
        Conditions conditions = condition("id", "=", "3");

        conditions.andNot("id2", "=", "2");
        assembleAst(conditions);
        assertEquals("WHERE id = 3 AND NOT id2 = 2", ast.toString());

        conditions.andNot(asName("id4"), LT, asConstant(4));
        assembleAst(conditions);
        assertEquals("WHERE id = 3 AND NOT id2 = 2 AND NOT id4 < 4", ast.toString());

        conditions.andNot(condition(asName("id5"), LTE, asConstant(5)));
        assembleAst(conditions);
        assertEquals("WHERE id = 3 AND NOT id2 = 2 AND NOT id4 < 4 AND NOT id5 <= 5", ast.toString());
    }

    //----
    // OR
    //----

    @Test
    void or() {
        Conditions conditions = condition("id", "<>", "3");

        conditions.or("id2", "<>", "2");
        assembleAst(conditions);
        assertEquals("WHERE id <> 3 OR id2 <> 2", ast.toString());

        conditions.or(asName("id4"), GT, asConstant(4));
        assembleAst(conditions);
        assertEquals("WHERE id <> 3 OR id2 <> 2 OR id4 > 4", ast.toString());

        conditions.or(condition(asName("id5"), GTE, asConstant(5)));
        assembleAst(conditions);
        assertEquals("WHERE id <> 3 OR id2 <> 2 OR id4 > 4 OR id5 >= 5", ast.toString());
    }

    @Test
    void orNot() {
        Conditions conditions = condition("id", "=", "3");

        conditions.orNot("id2", "=", "2");
        assembleAst(conditions);
        assertEquals("WHERE id = 3 OR NOT id2 = 2", ast.toString());

        conditions.orNot(asName("id4"), LT, asConstant(4));
        assembleAst(conditions);
        assertEquals("WHERE id = 3 OR NOT id2 = 2 OR NOT id4 < 4", ast.toString());

        conditions.orNot(condition(asName("id5"), LTE, asConstant(5)));
        assembleAst(conditions);
        assertEquals("WHERE id = 3 OR NOT id2 = 2 OR NOT id4 < 4 OR NOT id5 <= 5", ast.toString());
    }

    @Test
    void orGroupWithNestedGroup() {
        Conditions conditions = condition("id", "=", "1");

        conditions.or("id2", "!=", "2")
           .and(condition("id3", "<", "3").andNot("id4", ">", "4"));

        assembleAst(conditions);
        assertEquals("WHERE id = 1 OR id2 != 2 AND (id3 < 3 AND NOT id4 > 4)", ast.toString());
    }

    @Test
    void between() {
        Conditions conditions = conditionBetween("id", "1", "3");
        assembleAst(conditions);
        assertEquals("WHERE id BETWEEN 1 AND 3", ast.toString());


        //todo add date constant
        conditions.andNot(conditionBetween(asQuotedName("date"), asConstant("2018-03-01"), asConstant("2018-03-11")));
        assembleAst(conditions);
        assertEquals("WHERE id BETWEEN 1 AND 3 AND NOT \"date\" BETWEEN '2018-03-01' AND '2018-03-11'", ast.toString());
    }

    @Test
    void orGroupWithNested2Group() {
        Conditions conditions = condition("id", "=", "1");

        conditions.or("id2", "!=", "2")
           .and(condition("id3", "<", "3")
              .andNot("id4", ">", "4")
              .andNot(condition("id5", ">", "4")
                 .orNot("id6", "<=", "6"))
           );

        assembleAst(conditions);
        assertEquals("WHERE id = 1 OR id2 != 2 AND (id3 < 3 AND NOT id4 > 4 AND NOT (id5 > 4 OR NOT id6 <= 6))", ast.toString());
    }

    @Test
    void like() {
        Conditions conditions = condition(asName("id"), LIKE, asConstant("abc"));
        assembleAst(conditions);
        assertEquals("WHERE id LIKE 'abc'", ast.toString());

        conditions.and(asQuotedName("str"), NOT_LIKE, asConstant("_d_"));
        assembleAst(conditions);
        assertEquals("WHERE id LIKE 'abc' AND \"str\" NOT LIKE '_d_'", ast.toString());
    }

    @Test
    void conditionList() {
        //todo add support for list of constants
        String[]   numbers    = { "one", "two", "three", "four", "five", "six" };
        Conditions conditions = condition(asName("number"), IN, asConstant(numbers));
        assembleAst(conditions);
        assertEquals("WHERE number IN ('one', 'two', 'three', 'four', 'five', 'six')", ast.toString());

        Conditions conditions2 = condition(asName("number"), IN, asList(numbers));
        assembleAst(conditions2);
        assertEquals("WHERE number IN (one, two, three, four, five, six)", ast.toString());

        Conditions conditions3 = condition(asName("number"), NOT_IN, asList(List.of(1, 2)));
        assembleAst(conditions3);
        assertEquals("WHERE number NOT IN (1, 2)", ast.toString());

    }

    @Test
    void conditionFunctionArray() {
        Conditions conditions4 = condition(asName("number"), EQUAL, asFunc("ALL", asArray(List.of(1, 2))));
        assembleAst(conditions4);
        assertEquals("WHERE number = ALL(ARRAY[1, 2])", ast.toString());

        Conditions conditions5 = condition(asName("number"), "!=", asFunc("SOME", asList(1, 2)));
        assembleAst(conditions5);
        assertEquals("WHERE number != SOME(1, 2)", ast.toString());
    }

    @Test
    void conditionExistsTest() {
        Conditions conditions = conditionExists(select("1"));
        assembleAst(conditions);
        assertEquals("WHERE EXISTS (SELECT 1)", ast.toString());

        conditions = conditionExists(select("1"));
        assembleAst(conditions);
        assertEquals("WHERE EXISTS (SELECT 1)", ast.toString());

        conditions.andExists(select("id", "name")
           .from("user")
           .where("id", "=", "1"));
        assembleAst(conditions);
        assertEquals("WHERE EXISTS (SELECT 1) AND EXISTS (SELECT id, name FROM user WHERE id = 1)", ast.toString());

        conditions = conditionExists(select("1"))
           .andNotExists(select("id", "name").from("user"));
        assembleAst(conditions);
        assertEquals("WHERE EXISTS (SELECT 1) AND NOT EXISTS (SELECT id, name FROM user)", ast.toString());

        conditions = conditionExists(select("1"))
           .orExists(select("id", "name").from("user"));
        assembleAst(conditions);
        assertEquals("WHERE EXISTS (SELECT 1) OR EXISTS (SELECT id, name FROM user)", ast.toString());

        conditions = conditionExists(select("1"))
           .orNotExists(select("id", "name").from("user"));
        assembleAst(conditions);
        assertEquals("WHERE EXISTS (SELECT 1) OR NOT EXISTS (SELECT id, name FROM user)", ast.toString());


        Query select = select("id", "name").from("user")
           .where("id", "=", "1")
           .and(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", "'timur@shaidullin.net'")
           )
           .and("id2", "=", "2");

        conditions = conditionExists(select);
        assembleAst(conditions);
        assertEquals("WHERE EXISTS (SELECT id, name FROM user WHERE id = 1 AND (name = timur AND phone is null OR email = 'timur@shaidullin.net') AND id2 = 2)", ast.toString());
    }

    @Test
    void conditionInSubquery() {
        Conditions conditions = condition("name", "IN", select("1", "2"));
        assembleAst(conditions);
        assertEquals("WHERE name IN (SELECT 1, 2)", ast.toString());

        conditions = conditions
           .and(asName("name"), IN, select("1", "2"))
           .andNot(asName("name"), IN, select("1", "2"))
           .or(asName("name"), IN, select("1", "2"))
           .orNot(asName("name"), NOT_IN, select("1", "2"));
        assembleAst(conditions);
        assertEquals("WHERE name IN (SELECT 1, 2) AND name IN (SELECT 1, 2) AND NOT name IN (SELECT 1, 2) OR name IN (SELECT 1, 2) OR NOT name NOT IN (SELECT 1, 2)", ast.toString());

        conditions = condition(asName("name"), NOT_IN, select("1", "2"));
        assembleAst(conditions);
        assertEquals("WHERE name NOT IN (SELECT 1, 2)", ast.toString());


        conditions = conditions
           .or(condition(asName("name"), NOT_IN, select("1", "2"))
              .and(condition(asName("name"), NOT_IN, select("1", "2"))
              )
           );

        assembleAst(conditions);
        assertEquals("WHERE name NOT IN (SELECT 1, 2) OR (name NOT IN (SELECT 1, 2) AND name NOT IN (SELECT 1, 2))", ast.toString());
    }

    @Test
    void conditionAnyTest() {
        Conditions conditions = conditionAny("id", "=", select( "1"));
        assembleAst(conditions);
        assertEquals("WHERE id = ANY (SELECT 1)", ast.toString());

        conditions = conditionAny("id", "=", select("1", "2"));
        assembleAst(conditions);
        assertEquals("WHERE id = ANY (SELECT 1, 2)", ast.toString());

        conditions.and(conditionAny(asName("id"), operator("="), select("id").from("user")));
        assembleAst(conditions);
        assertEquals("WHERE id = ANY (SELECT 1, 2) AND id = ANY (SELECT id FROM user)", ast.toString());
    }

    @Test
    void conditionSomeTest() {
        Conditions conditions = conditionSome("id", "=", select("1", "2"));
        assembleAst(conditions);
        assertEquals("WHERE id = SOME (SELECT 1, 2)", ast.toString());

        conditions.and(conditionSome(asName("id"), operator("="), select("id").from("user")));
        assembleAst(conditions);
        assertEquals("WHERE id = SOME (SELECT 1, 2) AND id = SOME (SELECT id FROM user)", ast.toString());
    }

    @Test
    void conditionAllTest() {
        Conditions conditions = conditionAll("id", "=", select("1", "2"));
        assembleAst(conditions);
        assertEquals("WHERE id = ALL (SELECT 1, 2)", ast.toString());

        conditions.and(conditionAll(asName("id"), operator("="), select("id").from("user")));
        assembleAst(conditions);
        assertEquals("WHERE id = ALL (SELECT 1, 2) AND id = ALL (SELECT id FROM user)", ast.toString());
    }
}
