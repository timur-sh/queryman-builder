/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.expression.prepared.ArrayExpression;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.Operators.IN;
import static org.queryman.builder.Operators.LIKE;
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
import static org.queryman.builder.TestHelper.testBindParameters;
import static org.queryman.builder.ast.TreeFormatterTestUtil.buildPreparedSQL;
import static org.queryman.builder.ast.TreeFormatterTestUtil.buildSQL;

/**
 * @author Timur Shaidullin
 */
public class ConditionsImplTest extends BaseTest {
    @Test
    void simple() throws NoSuchFieldException, IllegalAccessException {
        Conditions conditions = condition("id", "=", "3");
        assertEquals("id = 3", buildPreparedSQL(conditions));
        assertEquals("id = 3", buildSQL(conditions));

        conditions = condition(1, "=", 2);
        assertEquals("1 = 2", buildSQL(conditions));
        assertEquals("? = ?", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        conditions = condition(asName("id"), EQUAL, asSubQuery(select("max(sum)")).as("sum"));
        assertEquals("id = (SELECT max(sum)) AS sum", buildSQL(conditions));
        assertEquals("id = (SELECT max(sum)) AS sum", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertTrue(map.size() == 0);
        });

        conditions = condition(
           select("id").from("book").where("id", "=", 55).limit(1),
           EQUAL,
           select("max(sum)")
        );
        assertEquals("(SELECT id FROM book WHERE id = 55 LIMIT 1) = (SELECT max(sum))", buildSQL(conditions));
        assertEquals("(SELECT id FROM book WHERE id = ? LIMIT 1) = (SELECT max(sum))", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertTrue(map.size() == 1);
            assertEquals(55, map.get(1).getValue());
        });
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
        testBindParameters(conditions, map -> {
            assertEquals(7, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
            assertEquals(1, map.get(3).getValue());
            assertEquals(2, map.get(4).getValue());
            assertEquals(2, map.get(5).getValue());
            assertEquals(3, map.get(6).getValue());
            assertEquals(1, map.get(7).getValue());
        });
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
           .andNot(conditionExists(select("*").from("book").where("id", "=", 2)));

        assertEquals("id = 3 AND NOT id = 2 AND NOT 1 = 2 AND NOT id = 2 AND NOT 1 = 2 AND NOT 1 BETWEEN 2 AND 3 AND NOT id = ALL(SELECT 1) AND NOT EXISTS (SELECT * FROM book WHERE id = 2)", buildSQL(conditions));
        assertEquals("id = 3 AND NOT id = 2 AND NOT ? = ? AND NOT id = 2 AND NOT ? = ? AND NOT 1 BETWEEN ? AND ? AND NOT id = ALL(SELECT 1) AND NOT EXISTS (SELECT * FROM book WHERE id = ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(7, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
            assertEquals(1, map.get(3).getValue());
            assertEquals(2, map.get(4).getValue());
            assertEquals(2, map.get(5).getValue());
            assertEquals(3, map.get(6).getValue());
            assertEquals(2, map.get(7).getValue());
        });
    }

    //----
    // OR
    //----

    @Test
    void or() {
        Conditions conditions = condition("id", "=", "3")
           .or("id", "=", "2")
           .or(11, "=", 2)
           .or(condition("id", "=", "2"))
           .or(condition(1, "=", 2))
           .or(conditionBetween(1, 2, 3))
           .or(conditionAll("id", "=", select(1)))
           .or(conditionExists(select("*").from("book").where("id", "=", 1)));

        assertEquals("id = 3 OR id = 2 OR 11 = 2 OR id = 2 OR 1 = 2 OR 1 BETWEEN 2 AND 3 OR id = ALL(SELECT 1) OR EXISTS (SELECT * FROM book WHERE id = 1)", buildSQL(conditions));
        assertEquals("id = 3 OR id = 2 OR ? = ? OR id = 2 OR ? = ? OR 1 BETWEEN ? AND ? OR id = ALL(SELECT 1) OR EXISTS (SELECT * FROM book WHERE id = ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(7, map.size());
            assertEquals(11, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
            assertEquals(1, map.get(3).getValue());
            assertEquals(2, map.get(4).getValue());
            assertEquals(2, map.get(5).getValue());
            assertEquals(3, map.get(6).getValue());
            assertEquals(1, map.get(7).getValue());
        });
    }

    @Test
    void orNot() {
        Conditions conditions = condition("id", "=", "3")
           .orNot("id", "=", "2")
           .orNot(1, "=", 22)
           .orNot(condition("id", "=", "2"))
           .orNot(condition(1, "=", 2))
           .orNot(conditionBetween(1, 2, 3))
           .orNot(conditionAll("id", "=", select(1)))
           .orNot(conditionExists(select("*").from("book").where("id", "=", 1)));

        assertEquals("id = 3 OR NOT id = 2 OR NOT 1 = 22 OR NOT id = 2 OR NOT 1 = 2 OR NOT 1 BETWEEN 2 AND 3 OR NOT id = ALL(SELECT 1) OR NOT EXISTS (SELECT * FROM book WHERE id = 1)", buildSQL(conditions));
        assertEquals("id = 3 OR NOT id = 2 OR NOT ? = ? OR NOT id = 2 OR NOT ? = ? OR NOT 1 BETWEEN ? AND ? OR NOT id = ALL(SELECT 1) OR NOT EXISTS (SELECT * FROM book WHERE id = ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(7, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(22, map.get(2).getValue());
            assertEquals(1, map.get(3).getValue());
            assertEquals(2, map.get(4).getValue());
            assertEquals(2, map.get(5).getValue());
            assertEquals(3, map.get(6).getValue());
            assertEquals(1, map.get(7).getValue());
        });
    }

    @Test
    void orGroupWithNestedGroup() {
        Conditions conditions = condition("id", "=", 9)
           .and(condition("id", "=", "3")
              .orNot("id", "=", "2")
              .orNot(1, "=", 2)
              .orNot(condition("id", "=", "2"))
              .orNot(condition(99, "=", 2))
              .orNot(conditionBetween(1, 2, 3))
              .orNot(conditionAll("id", "=", select(1)))
              .orNot(conditionExists(select("*").from("book").where("id", "=", 1)))
           );

        assertEquals("id = 9 AND (id = 3 OR NOT id = 2 OR NOT 1 = 2 OR NOT id = 2 OR NOT 99 = 2 OR NOT 1 BETWEEN 2 AND 3 OR NOT id = ALL(SELECT 1) OR NOT EXISTS (SELECT * FROM book WHERE id = 1))", buildSQL(conditions));
        assertEquals("id = ? AND (id = 3 OR NOT id = 2 OR NOT ? = ? OR NOT id = 2 OR NOT ? = ? OR NOT 1 BETWEEN ? AND ? OR NOT id = ALL(SELECT 1) OR NOT EXISTS (SELECT * FROM book WHERE id = ?))", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(8, map.size());
            assertEquals(9, map.get(1).getValue());
            assertEquals(1, map.get(2).getValue());
            assertEquals(2, map.get(3).getValue());
            assertEquals(99, map.get(4).getValue());
            assertEquals(2, map.get(5).getValue());
            assertEquals(2, map.get(6).getValue());
            assertEquals(3, map.get(7).getValue());
            assertEquals(1, map.get(8).getValue());
        });
    }

    @Test
    void between() {
        Conditions conditions = conditionBetween("id", "1", 3)
           .and(conditionBetween(1, select(1), select(3)))
           .and(conditionBetween("date", asConstant("2018-03-01"), asConstant("2018-03-01")));
        assertEquals("id BETWEEN 1 AND 3 AND 1 BETWEEN (SELECT 1) AND (SELECT 3) AND date BETWEEN '2018-03-01' AND '2018-03-01'", buildSQL(conditions));
        assertEquals("id BETWEEN 1 AND ? AND 1 BETWEEN (SELECT 1) AND (SELECT 3) AND date BETWEEN ? AND ?", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(3, map.size());
            assertEquals(3, map.get(1).getValue());
            assertEquals("2018-03-01", map.get(2).getValue());
            assertEquals("2018-03-01", map.get(3).getValue());
        });
    }

    @Test
    void orGroupWithNested2Group() {
        Conditions conditions = condition("id", "=", "1")
           .or("id2", "!=", "2")
           .and(condition("id3", "<", "3")
              .andNot(4, ">", "4")
              .andNot(condition("id5", ">", "4")
                 .orNot("id6", "<=", 6))
           );

        assertEquals("id = 1 OR id2 != 2 AND (id3 < 3 AND NOT 4 > 4 AND NOT (id5 > 4 OR NOT id6 <= 6))", buildSQL(conditions));
        assertEquals("id = 1 OR id2 != 2 AND (id3 < 3 AND NOT ? > 4 AND NOT (id5 > 4 OR NOT id6 <= ?))", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(2, map.size());
            assertEquals(4, map.get(1).getValue());
            assertEquals(6, map.get(2).getValue());
        });
    }

    @Test
    void like() throws NoSuchFieldException, IllegalAccessException, SQLException {
        Conditions conditions = condition(asName("id"), LIKE, asConstant("abc"))
           .and(asQuotedName("str"), NOT_LIKE, asConstant("_d_"));
        assertEquals("id LIKE 'abc' AND \"str\" NOT LIKE '_d_'", buildSQL(conditions));
        assertEquals("id LIKE ? AND \"str\" NOT LIKE ?", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(2, map.size());
            assertEquals("abc", map.get(1).getValue());
            assertEquals("_d_", map.get(2).getValue());
        });

        Query query = select("*").from("book").where(condition(asName("id"), EQUAL, 1)
           .and(asQuotedName("name"), NOT_LIKE, asConstant("_d_")));

        assertEquals("SELECT * FROM book WHERE (id = 1 AND \"name\" NOT LIKE '_d_')", query.sql());
        assertEquals("SELECT * FROM book WHERE (id = ? AND \"name\" NOT LIKE ?)", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(2, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals("_d_", map.get(2).getValue());
        });
        inBothStatement(query, rs -> {});
    }

    @Test
    void conditionList() {
        Number[]   numbers    = { 1, 2, 3, 4 };
        Conditions conditions = condition(asName("number"), IN, asList(numbers));

        assertEquals("number IN (1, 2, 3, 4)", buildSQL(conditions));
        assertEquals("number IN (?, ?, ?, ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(4, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
            assertEquals(3, map.get(3).getValue());
            assertEquals(4, map.get(4).getValue());
        });

        conditions = condition(asName("number"), NOT_IN, asList(List.of(1, 2)));
        assertEquals("number NOT IN (1, 2)", buildSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(2, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

    }

    @Test
    void conditionFunctionArray() {
        Conditions conditions = condition(asName("number"), EQUAL, asFunc("ALL", asArray(1, 2)));
        assertEquals("number = ALL(ARRAY[1, 2])", buildSQL(conditions));
        assertEquals("number = ALL(?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(1, map.size());
            assertTrue(map.get(1) instanceof ArrayExpression);
        });

        conditions = condition(asName("number"), "!=", some(asList(1, 2)));
        assertEquals("number != SOME(1, 2)", buildSQL(conditions));
        assertEquals("number != SOME(?, ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(2, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });
    }

    @Test
    void conditionExistsTest() {
        Conditions conditions = conditionExists(select("1"));
        assertEquals("EXISTS (SELECT 1)", buildSQL(conditions));
        assertEquals("EXISTS (SELECT 1)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(0, map.size());
        });

        conditions = conditionExists(select("1"));
        assertEquals("EXISTS (SELECT 1)", buildSQL(conditions));

        conditions.andExists(select("id", "name")
           .from("user")
           .where("id", "=", 1));
        assertEquals("EXISTS (SELECT 1) AND EXISTS (SELECT id, name FROM user WHERE id = 1)", buildSQL(conditions));
        assertEquals("EXISTS (SELECT 1) AND EXISTS (SELECT id, name FROM user WHERE id = ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(1, map.size());
            assertEquals(1, map.get(1).getValue());
        });

        conditions = conditionExists(select("1"))
           .andNotExists(select("id", "name").from("user").where(1, "=", 1));
        assertEquals("EXISTS (SELECT 1) AND NOT EXISTS (SELECT id, name FROM user WHERE 1 = 1)", buildSQL(conditions));
        assertEquals("EXISTS (SELECT 1) AND NOT EXISTS (SELECT id, name FROM user WHERE ? = ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(2, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(1, map.get(2).getValue());
        });

        conditions = conditionExists(select("1"))
           .orExists(select("id", "name").from("user").where(1, "=", 2));
        assertEquals("EXISTS (SELECT 1) OR EXISTS (SELECT id, name FROM user WHERE 1 = 2)", buildSQL(conditions));
        assertEquals("EXISTS (SELECT 1) OR EXISTS (SELECT id, name FROM user WHERE ? = ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(2, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        conditions = conditionExists(select("1"))
           .orNotExists(select("id", "name").from("user").where(1, "=", 3));
        assertEquals("EXISTS (SELECT 1) OR NOT EXISTS (SELECT id, name FROM user WHERE 1 = 3)", buildSQL(conditions));
        assertEquals("EXISTS (SELECT 1) OR NOT EXISTS (SELECT id, name FROM user WHERE ? = ?)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(2, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals(3, map.get(2).getValue());
        });

        Query select = select("id", "name").from("user")
           .where("id", "=", 1)
           .and(condition("name", "=", asConstant("timur"))
              .and("phone", "is", null)
              .or("email", "=", asConstant("timur@shaidullin.net"))
           )
           .and(2, "=", "2");

        conditions = conditionExists(select);
        assertEquals("EXISTS (SELECT id, name FROM user WHERE id = 1 AND (name = 'timur' AND phone is NULL OR email = 'timur@shaidullin.net') AND 2 = 2)", buildSQL(conditions));
        assertEquals("EXISTS (SELECT id, name FROM user WHERE id = ? AND (name = ? AND phone is NULL OR email = ?) AND ? = 2)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(4, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals("timur", map.get(2).getValue());
            assertEquals("timur@shaidullin.net", map.get(3).getValue());
            assertEquals(2, map.get(4).getValue());
        });
    }

    @Test
    void conditionInSubquery() {
        Conditions conditions = condition("name", "IN", select("1", "2"));
        assertEquals("name IN (SELECT 1, 2)", buildSQL(conditions));

        conditions = conditions
           .and(asName("name"), IN, select("1", "2"))
           .andNot(asName("name"), IN, select("1", "2"))
           .or(asName("name"), IN, select("1", "2"))
           .orNot(asName("name"), NOT_IN, select("1", "2"));
        assertEquals("name IN (SELECT 1, 2) AND name IN (SELECT 1, 2) AND NOT name IN (SELECT 1, 2) OR name IN (SELECT 1, 2) OR NOT name NOT IN (SELECT 1, 2)", buildSQL(conditions));
        assertEquals("name IN (SELECT 1, 2) AND name IN (SELECT 1, 2) AND NOT name IN (SELECT 1, 2) OR name IN (SELECT 1, 2) OR NOT name NOT IN (SELECT 1, 2)", buildPreparedSQL(conditions));

        conditions = condition(asName("name"), NOT_IN, select("1", "2"));
        assertEquals("name NOT IN (SELECT 1, 2)", buildSQL(conditions));
        assertEquals("name NOT IN (SELECT 1, 2)", buildPreparedSQL(conditions));


        conditions = conditions
           .or(condition(asName("name"), NOT_IN, select("1", "2"))
              .and(condition(asName("name"), NOT_IN, select("1", "2"))
              )
           );

        assertEquals("name NOT IN (SELECT 1, 2) OR (name NOT IN (SELECT 1, 2) AND name NOT IN (SELECT 1, 2))", buildSQL(conditions));
        assertEquals("name NOT IN (SELECT 1, 2) OR (name NOT IN (SELECT 1, 2) AND name NOT IN (SELECT 1, 2))", buildPreparedSQL(conditions));
    }

    @Test
    void conditionAnyTest() {
        Conditions conditions = condition("id", "=", any(select("1")));
        assertEquals("id = ANY(SELECT 1)", buildSQL(conditions));
        assertEquals("id = ANY(SELECT 1)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(0, map.size());
        });

        conditions = condition("id", "=", any(values(3, 2)));
        assertEquals("id = ANY(VALUES(3), (2))", buildSQL(conditions));
        assertEquals("id = ANY(VALUES(?), (?))", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(2, map.size());
            assertEquals(3, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        conditions = conditionAny("id", "=", select("1", "2"));
        assertEquals("id = ANY(SELECT 1, 2)", buildSQL(conditions));
        assertEquals("id = ANY(SELECT 1, 2)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(0, map.size());
        });

        conditions.and(conditionAny(asName("id"), operator("="), select("id").from("user")));
        assertEquals("id = ANY(SELECT 1, 2) AND id = ANY(SELECT id FROM user)", buildSQL(conditions));
        assertEquals("id = ANY(SELECT 1, 2) AND id = ANY(SELECT id FROM user)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(0, map.size());
        });
    }

    @Test
    void conditionSomeTest() {
        Conditions conditions = conditionSome("id", "=", select("1", "2"));
        assertEquals("id = SOME(SELECT 1, 2)", buildSQL(conditions));
        assertEquals("id = SOME(SELECT 1, 2)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(0, map.size());
        });

        conditions.and(conditionSome(asName("id"), operator("="), select("id").from("user")));
        assertEquals("id = SOME(SELECT 1, 2) AND id = SOME(SELECT id FROM user)", buildSQL(conditions));
        assertEquals("id = SOME(SELECT 1, 2) AND id = SOME(SELECT id FROM user)", buildPreparedSQL(conditions));
        testBindParameters(conditions, map -> {
            assertEquals(0, map.size());
        });
    }

    @Test
    void conditionAllTest() {
        Conditions conditions = conditionAll("id", "=", select("1", "2"));
        assertEquals("id = ALL(SELECT 1, 2)", buildSQL(conditions));
        assertEquals("id = ALL(SELECT 1, 2)", buildPreparedSQL(conditions));

        conditions.and(conditionAll(asName("id"), operator("="), select("id").from("user")));
        assertEquals("id = ALL(SELECT 1, 2) AND id = ALL(SELECT id FROM user)", buildSQL(conditions));
        assertEquals("id = ALL(SELECT 1, 2) AND id = ALL(SELECT id FROM user)", buildPreparedSQL(conditions));
    }
}
