/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.Query;
import org.queryman.builder.Queryman;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.Conditions;

import java.sql.SQLException;
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
import static org.queryman.builder.Queryman.any;
import static org.queryman.builder.Queryman.asArray;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asFunc;
import static org.queryman.builder.Queryman.asList;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.asQuotedName;
import static org.queryman.builder.Queryman.asSubQuery;
import static org.queryman.builder.Queryman.condition;
import static org.queryman.builder.Queryman.conditionAll;
import static org.queryman.builder.Queryman.conditionAny;
import static org.queryman.builder.Queryman.conditionBetween;
import static org.queryman.builder.Queryman.conditionExists;
import static org.queryman.builder.Queryman.conditionSome;
import static org.queryman.builder.Queryman.operator;
import static org.queryman.builder.Queryman.select;
import static org.queryman.builder.Queryman.some;
import static org.queryman.builder.Queryman.values;
import static org.queryman.builder.ast.TreeFormatterTestUtil.buildPreparedSQL;
import static org.queryman.builder.ast.TreeFormatterTestUtil.buildSQL;

/**
 * @author Timur Shaidullin
 */
public class ConditionsImplTest extends BaseTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = Queryman.getTree();
    }

    void assembleAst(Conditions conditions) {
        ast.startNode(NodesMetadata.WHERE);
        conditions.assemble(ast);
        ast.endNode();
    }

    @Test
    void simple() throws NoSuchFieldException, IllegalAccessException {
        Conditions conditions = condition("id", "=", "3");
        assertEquals("id = 3", buildPreparedSQL(conditions));
        assertEquals("id = 3", buildSQL(conditions));

        conditions = condition(1, "=", 2);
        assertEquals("1 = 2", buildSQL(conditions));
        assertEquals("? = ?", buildPreparedSQL(conditions));

        conditions = condition(asName("id"), EQUAL, asSubQuery(select("max(sum)")).as("sum"));
        assertEquals("id = (SELECT max(sum)) AS sum", buildSQL(conditions));
        assertEquals("id = (SELECT max(sum)) AS sum", buildPreparedSQL(conditions));

        conditions = condition(
           select("id").from("book").limit(1),
           EQUAL,
           select("max(sum)")
        );
        assertEquals("(SELECT id FROM book LIMIT 1) = (SELECT max(sum))", buildSQL(conditions));
        assertEquals("(SELECT id FROM book LIMIT 1) = (SELECT max(sum))", buildPreparedSQL(conditions));
    }

    //----
    // AND
    //----

    @Test
    void and() {
        Conditions conditions = condition("id", "=", "3")
           .and("id", "=", "2")
           .and(1, "=", 2)
           .and(condition("id", "=", "2"))
           .and(condition(1, "=", 2))
           .and(conditionBetween(1, 2, 3))
           .and(conditionAll("id", "=", select(1)))
           .and(conditionExists(select("*").from("book").where("id", "=", 1)));

        assertEquals("id = 3 AND id = 2 AND 1 = 2 AND id = 2 AND 1 = 2 AND 1 BETWEEN 2 AND 3 AND id = ALL(SELECT 1) AND EXISTS (SELECT * FROM book WHERE id = 1)", buildSQL(conditions));
        assertEquals("id = 3 AND id = 2 AND ? = ? AND id = 2 AND ? = ? AND 1 BETWEEN ? AND ? AND id = ALL(SELECT 1) AND EXISTS (SELECT * FROM book WHERE id = ?)", buildPreparedSQL(conditions));

    }

    @Test
    void andNot() {
        Conditions conditions = condition("id", "=", "3")
           .andNot("id", "=", "2")
           .andNot(1, "=", 2)
           .andNot(condition("id", "=", "2"))
           .andNot(condition(1, "=", 2))
           .andNot(conditionBetween(1, 2, 3))
           .andNot(conditionAll("id", "=", select(1)))
           .andNot(conditionExists(select("*").from("book").where("id", "=", 1)));

        assertEquals("id = 3 AND NOT id = 2 AND NOT 1 = 2 AND NOT id = 2 AND NOT 1 = 2 AND NOT 1 BETWEEN 2 AND 3 AND NOT id = ALL(SELECT 1) AND NOT EXISTS (SELECT * FROM book WHERE id = 1)", buildSQL(conditions));
        assertEquals("id = 3 AND NOT id = 2 AND NOT ? = ? AND NOT id = 2 AND NOT ? = ? AND NOT 1 BETWEEN ? AND ? AND NOT id = ALL(SELECT 1) AND NOT EXISTS (SELECT * FROM book WHERE id = ?)", buildPreparedSQL(conditions));

    }

    //----
    // OR
    //----

    @Test
    void or() {
        Conditions conditions = condition("id", "=", "3")
           .or("id", "=", "2")
           .or(1, "=", 2)
           .or(condition("id", "=", "2"))
           .or(condition(1, "=", 2))
           .or(conditionBetween(1, 2, 3))
           .or(conditionAll("id", "=", select(1)))
           .or(conditionExists(select("*").from("book").where("id", "=", 1)));

        assertEquals("id = 3 OR id = 2 OR 1 = 2 OR id = 2 OR 1 = 2 OR 1 BETWEEN 2 AND 3 OR id = ALL(SELECT 1) OR EXISTS (SELECT * FROM book WHERE id = 1)", buildSQL(conditions));
        assertEquals("id = 3 OR id = 2 OR ? = ? OR id = 2 OR ? = ? OR 1 BETWEEN ? AND ? OR id = ALL(SELECT 1) OR EXISTS (SELECT * FROM book WHERE id = ?)", buildPreparedSQL(conditions));

    }

    @Test
    void orNot() {
        Conditions conditions = condition("id", "=", "3")
           .orNot("id", "=", "2")
           .orNot(1, "=", 2)
           .orNot(condition("id", "=", "2"))
           .orNot(condition(1, "=", 2))
           .orNot(conditionBetween(1, 2, 3))
           .orNot(conditionAll("id", "=", select(1)))
           .orNot(conditionExists(select("*").from("book").where("id", "=", 1)));

        assertEquals("id = 3 OR NOT id = 2 OR NOT 1 = 2 OR NOT id = 2 OR NOT 1 = 2 OR NOT 1 BETWEEN 2 AND 3 OR NOT id = ALL(SELECT 1) OR NOT EXISTS (SELECT * FROM book WHERE id = 1)", buildSQL(conditions));
        assertEquals("id = 3 OR NOT id = 2 OR NOT ? = ? OR NOT id = 2 OR NOT ? = ? OR NOT 1 BETWEEN ? AND ? OR NOT id = ALL(SELECT 1) OR NOT EXISTS (SELECT * FROM book WHERE id = ?)", buildPreparedSQL(conditions));

    }

    @Test
    void orGroupWithNestedGroup() {
        Conditions conditions = condition("id", "=", 9)
           .and(condition("id", "=", "3")
              .orNot("id", "=", "2")
              .orNot(1, "=", 2)
              .orNot(condition("id", "=", "2"))
              .orNot(condition(1, "=", 2))
              .orNot(conditionBetween(1, 2, 3))
              .orNot(conditionAll("id", "=", select(1)))
              .orNot(conditionExists(select("*").from("book").where("id", "=", 1)))
           );

        assertEquals("id = 9 AND (id = 3 OR NOT id = 2 OR NOT 1 = 2 OR NOT id = 2 OR NOT 1 = 2 OR NOT 1 BETWEEN 2 AND 3 OR NOT id = ALL(SELECT 1) OR NOT EXISTS (SELECT * FROM book WHERE id = 1))", buildSQL(conditions));
        assertEquals("id = ? AND (id = 3 OR NOT id = 2 OR NOT ? = ? OR NOT id = 2 OR NOT ? = ? OR NOT 1 BETWEEN ? AND ? OR NOT id = ALL(SELECT 1) OR NOT EXISTS (SELECT * FROM book WHERE id = ?))", buildPreparedSQL(conditions));
    }

    @Test
    void between() {
        Conditions conditions = conditionBetween("id", "1", 3)
           .and(conditionBetween(1, select(1), select(3)))
           .and(conditionBetween("date", asConstant("2018-03-01"), asConstant("2018-03-01")));
        assertEquals("id BETWEEN 1 AND 3 AND 1 BETWEEN (SELECT 1) AND (SELECT 3) AND date BETWEEN '2018-03-01' AND '2018-03-01'", buildSQL(conditions));
        assertEquals("id BETWEEN 1 AND ? AND 1 BETWEEN (SELECT 1) AND (SELECT 3) AND date BETWEEN ? AND ?", buildPreparedSQL(conditions));
    }

    @Test
    void orGroupWithNested2Group() {
        Conditions conditions = condition("id", "=", "1")
           .or("id2", "!=", "2")
           .and(condition("id3", "<", "3")
              .andNot("id4", ">", "4")
              .andNot(condition("id5", ">", "4")
                 .orNot("id6", "<=", "6"))
           );

        assertEquals("id = 1 OR id2 != 2 AND (id3 < 3 AND NOT id4 > 4 AND NOT (id5 > 4 OR NOT id6 <= 6))", buildSQL(conditions));
        assertEquals("id = 1 OR id2 != 2 AND (id3 < 3 AND NOT id4 > 4 AND NOT (id5 > 4 OR NOT id6 <= 6))", buildPreparedSQL(conditions));
    }

    @Test
    void like() throws NoSuchFieldException, IllegalAccessException, SQLException {
        Conditions conditions = condition(asName("id"), LIKE, asConstant("abc"))
           .and(asQuotedName("str"), NOT_LIKE, asConstant("_d_"));
        assertEquals("id LIKE 'abc' AND \"str\" NOT LIKE '_d_'", buildSQL(conditions));
        assertEquals("id LIKE ? AND \"str\" NOT LIKE ?", buildPreparedSQL(conditions));

        Query query = select("*").from("book").where(condition(asName("id"), EQUAL, 1)
           .and(asQuotedName("name"), NOT_LIKE, asConstant("_d_")));

        assertEquals("SELECT * FROM book WHERE (id = 1 AND \"name\" NOT LIKE '_d_')", query.sql());
        assertEquals("SELECT * FROM book WHERE (id = ? AND \"name\" NOT LIKE ?)", buildPreparedSQL(query));
        inBothStatement(query, rs -> {});
    }

    @Test
    void conditionList() {
        Number[]   numbers    = { 1, 2, 3, 4 };
        Conditions conditions = condition(asName("number"), IN, asList(numbers));

        assertEquals("number IN (1, 2, 3, 4)", buildSQL(conditions));
        assertEquals("number IN (?, ?, ?, ?)", buildPreparedSQL(conditions));

        Conditions conditions3 = condition(asName("number"), NOT_IN, asList(List.of(1, 2)));
        assertEquals("number NOT IN (1, 2)", buildSQL(conditions3));

    }

    @Test
    void conditionFunctionArray() {
        Conditions conditions = condition(asName("number"), EQUAL, asFunc("ALL", asArray(1, 2)));
        assertEquals("number = ALL(ARRAY[1, 2])", buildSQL(conditions));
        assertEquals("number = ALL(ARRAY[?, ?])", buildPreparedSQL(conditions));

        conditions = condition(asName("number"), "!=", some(asList(1, 2)));
        assertEquals("number != SOME(1, 2)", buildSQL(conditions));
        assertEquals("number != SOME(?, ?)", buildPreparedSQL(conditions));
    }

    @Test
    void conditionExistsTest() {
        Conditions conditions = conditionExists(select("1"));
        assembleAst(conditions);
        assertEquals("EXISTS (SELECT 1)", ast.toString());

        conditions = conditionExists(select("1"));
        assembleAst(conditions);
        assertEquals("EXISTS (SELECT 1)", ast.toString());

        conditions.andExists(select("id", "name")
           .from("user")
           .where("id", "=", "1"));
        assembleAst(conditions);
        assertEquals("EXISTS (SELECT 1) AND EXISTS (SELECT id, name FROM user WHERE id = 1)", ast.toString());

        conditions = conditionExists(select("1"))
           .andNotExists(select("id", "name").from("user"));
        assembleAst(conditions);
        assertEquals("EXISTS (SELECT 1) AND NOT EXISTS (SELECT id, name FROM user)", ast.toString());

        conditions = conditionExists(select("1"))
           .orExists(select("id", "name").from("user"));
        assembleAst(conditions);
        assertEquals("EXISTS (SELECT 1) OR EXISTS (SELECT id, name FROM user)", ast.toString());

        conditions = conditionExists(select("1"))
           .orNotExists(select("id", "name").from("user"));
        assembleAst(conditions);
        assertEquals("EXISTS (SELECT 1) OR NOT EXISTS (SELECT id, name FROM user)", ast.toString());


        Query select = select("id", "name").from("user")
           .where("id", "=", "1")
           .and(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", "'timur@shaidullin.net'")
           )
           .and("id2", "=", "2");

        conditions = conditionExists(select);
        assembleAst(conditions);
        assertEquals("EXISTS (SELECT id, name FROM user WHERE id = 1 AND (name = timur AND phone is null OR email = 'timur@shaidullin.net') AND id2 = 2)", ast.toString());
    }

    @Test
    void conditionInSubquery() {
        Conditions conditions = condition("name", "IN", select("1", "2"));
        assembleAst(conditions);
        assertEquals("name IN (SELECT 1, 2)", ast.toString());

        conditions = conditions
           .and(asName("name"), IN, select("1", "2"))
           .andNot(asName("name"), IN, select("1", "2"))
           .or(asName("name"), IN, select("1", "2"))
           .orNot(asName("name"), NOT_IN, select("1", "2"));
        assembleAst(conditions);
        assertEquals("name IN (SELECT 1, 2) AND name IN (SELECT 1, 2) AND NOT name IN (SELECT 1, 2) OR name IN (SELECT 1, 2) OR NOT name NOT IN (SELECT 1, 2)", ast.toString());

        conditions = condition(asName("name"), NOT_IN, select("1", "2"));
        assembleAst(conditions);
        assertEquals("name NOT IN (SELECT 1, 2)", ast.toString());


        conditions = conditions
           .or(condition(asName("name"), NOT_IN, select("1", "2"))
              .and(condition(asName("name"), NOT_IN, select("1", "2"))
              )
           );

        assembleAst(conditions);
        assertEquals("name NOT IN (SELECT 1, 2) OR (name NOT IN (SELECT 1, 2) AND name NOT IN (SELECT 1, 2))", ast.toString());
    }

    @Test
    void conditionAnyTest() {
        Conditions conditions = condition("id", "=", any(select("1")));
        assembleAst(conditions);
        assertEquals("id = ANY(SELECT 1)", ast.toString());

        conditions = condition("id", "=", any(values(1, 2)));
        assembleAst(conditions);
        assertEquals("id = ANY(VALUES(1), (2))", ast.toString());

        conditions = conditionAny("id", "=", select("1", "2"));
        assembleAst(conditions);
        assertEquals("id = ANY(SELECT 1, 2)", ast.toString());

        conditions.and(conditionAny(asName("id"), operator("="), select("id").from("user")));
        assembleAst(conditions);
        assertEquals("id = ANY(SELECT 1, 2) AND id = ANY(SELECT id FROM user)", ast.toString());
    }

    @Test
    void conditionSomeTest() {
        Conditions conditions = conditionSome("id", "=", select("1", "2"));
        assembleAst(conditions);
        assertEquals("id = SOME(SELECT 1, 2)", ast.toString());

        conditions.and(conditionSome(asName("id"), operator("="), select("id").from("user")));
        assembleAst(conditions);
        assertEquals("id = SOME(SELECT 1, 2) AND id = SOME(SELECT id FROM user)", ast.toString());
    }

    @Test
    void conditionAllTest() {
        Conditions conditions = conditionAll("id", "=", select("1", "2"));
        assembleAst(conditions);
        assertEquals("id = ALL(SELECT 1, 2)", ast.toString());

        conditions.and(conditionAll(asName("id"), operator("="), select("id").from("user")));
        assembleAst(conditions);
        assertEquals("id = ALL(SELECT 1, 2) AND id = ALL(SELECT id FROM user)", ast.toString());
    }
}
