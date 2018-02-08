package org.queryman.builder.token;

import org.junit.jupiter.api.Test;
import org.queryman.builder.PostgreSQL;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asArray;
import static org.queryman.builder.PostgreSQL.asList;
import static org.queryman.builder.PostgreSQL.asStringArray;
import static org.queryman.builder.PostgreSQL.asStringList;
import static org.queryman.builder.PostgreSQL.func;
import static org.queryman.builder.token.Expression.ExpressionType.COLUMN_REFERENCE;
import static org.queryman.builder.token.Expression.ExpressionType.DEFAULT;
import static org.queryman.builder.token.Expression.ExpressionType.DOLLAR_STRING;
import static org.queryman.builder.token.Expression.ExpressionType.FIELD_SELECTION;
import static org.queryman.builder.token.Expression.ExpressionType.STRING_CONSTANT;

class ExpressionTest {
    @Test
    void defaultExpression() {
        assertEquals("table_name", new Expression("table_name", DEFAULT).getName());
        assertEquals("234.11", new Expression(234.11, DEFAULT).getName());
        assertEquals("0.5", new Expression(.50, DEFAULT).getName());
        assertEquals("0.51", new Expression(.51, DEFAULT).getName());
        assertEquals("5.1", new Expression(.51E+1, DEFAULT).getName());
    }

    @Test
    void stringConstantExpression() {
        assertEquals("'Timur'", new Expression("Timur", STRING_CONSTANT).getName());

        assertEquals("'I''m Timur'", new Expression("I'm Timur", STRING_CONSTANT).getName());
    }

    @Test
    void dollarStringExpression() {
        assertEquals("$$Timur$$", new Expression("Timur", DOLLAR_STRING).getName());

        assertEquals("$$I'm Timur$$", new Expression("I'm Timur", DOLLAR_STRING).getName());

        assertEquals("$name$I'm Timur$name$", new Expression("I'm Timur", DOLLAR_STRING).setTagName("name").getName());
    }

    @Test
    void columnReference() {
        assertEquals("table.column", new Expression("table.column", COLUMN_REFERENCE).getName());
        assertEquals("\"table\".\"column\"", new Expression("table.column", COLUMN_REFERENCE).setQuoted(true).getName());
    }

    @Test
    void fieldSelection() {
        assertEquals("expression.fieldname", new Expression("expression.fieldname", FIELD_SELECTION).getName());
        assertEquals(".fieldname", new Expression(".fieldname", FIELD_SELECTION).getName());
        assertEquals("(tablename.compositecol).id", new Expression("(tablename.compositecol).id", FIELD_SELECTION).getName());
    }

    @Test
    void asStringListExpression() {
        assertEquals("()", PostgreSQL.asStringList().getName());

        String[] numbers = { "one", "two" };
        assertEquals("('one', 'two')", PostgreSQL.asStringList(numbers).getName());

        Number[] numbers2 = { 1, 2 };
        assertEquals("('1', '2')", PostgreSQL.asStringList(numbers2).getName());

        Expression strings = PostgreSQL.asStringList("one", "two", "three", "four", "five", "six");
        assertEquals("('one', 'two', 'three', 'four', 'five', 'six')", strings.getName());

        Expression chars = PostgreSQL.asStringList('1', '2', '3', '4', '5', '6');
        assertEquals("('1', '2', '3', '4', '5', '6')", chars.getName());

        Expression integers = PostgreSQL.asStringList(1, 0x1a);
        assertEquals("('1', '26')", integers.getName());

        Expression floats = PostgreSQL.asStringList(1f, 2e1);
        assertEquals("('1.0', '20.0')", floats.getName());

        Expression doubles = PostgreSQL.asStringList(1d, 2e-3);
        assertEquals("('1.0', '0.002')", doubles.getName());

        Expression longs = PostgreSQL.asStringList(1L, 2L, 0xFF_EC_DE_5E, 1_2_3L);
        assertEquals("('1', '2', '-1253794', '123')", longs.getName());

        assertEquals("('one', 'two')", asStringList(List.of("one", "two")).getName());

        assertEquals("('1', '2')", asStringList(List.of('1', '2')).getName());

        assertEquals("('1', '2')", asStringList(List.of(1, 2)).getName());

        assertEquals("('1.0', '2.0')", asStringList(List.of(1f, 2f)).getName());

        assertEquals("('1.0', '2.0')", asStringList(List.of(1d, 2d)).getName());
    }

    @Test
    void asListExpression() {
        assertEquals("()", asList().getName());

        String[] numbers = { "one", "two" };
        assertEquals("(one, two)", asList(numbers).getName());

        Number[] numbers2 = { 1, 2 };
        assertEquals("(1, 2)", asList(numbers2).getName());

        Expression strings = asList("1", "2");
        assertEquals("(1, 2)", strings.getName());

        Expression chars = asList('1', '2', '3', '4', '5', '6');
        assertEquals("(1, 2, 3, 4, 5, 6)", chars.getName());

        Expression integers = asList(1, 0x1a);
        assertEquals("(1, 26)", integers.getName());

        Expression floats = asList(1f, 2e1);
        assertEquals("(1.0, 20.0)", floats.getName());

        Expression doubles = asList(1d, 2e-3);
        assertEquals("(1.0, 0.002)", doubles.getName());

        Expression longs = asList(1L, 2L, 0xFF_EC_DE_5E, 1_2_3L);
        assertEquals("(1, 2, -1253794, 123)", longs.getName());

        assertEquals("(one, two)", asList(List.of("one", "two")).getName());

        assertEquals("(1, 2)", asList(List.of('1', '2')).getName());

        assertEquals("(1, 2)", asList(List.of(1, 2)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1f, 2f)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1d, 2d)).getName());
    }

    @Test
    void arrayTest() {
        assertEquals("ARRAY[]", asArray().getName());

        String[] numbers = { "1", "2" };
        assertEquals("ARRAY[1, 2]", asArray(numbers).getName());
        assertEquals("ARRAY[1, 2]", asArray(List.of(numbers)).getName());
        assertEquals("ARRAY[1, 2]", asArray(List.of(1, 2)).getName());
        assertEquals("ARRAY[]", asArray().getName());

        assertEquals("ARRAY[]", asStringArray().getName());

        String[] numbers2 = { "1", "2" };
        assertEquals("ARRAY['1', '2']", asStringArray(numbers2).getName());
        assertEquals("ARRAY['1', '2']", asStringArray(List.of("1", "2")).getName());
        assertEquals("ARRAY['1', '2']", asStringArray(List.of(1, 2)).getName());
    }

    @Test
    void functionAll() {
        List<Integer> i = List.of(1, 2);

        assertEquals("ALL(ARRAY[])", func("ALL", asArray()).getName());
        assertEquals("ALL(ARRAY[1, 2])", func("ALL", asArray(1, 2)).getName());
        assertEquals("ALL()", func("ALL", asList()).getName());
        assertEquals("ALL(1, 2)", func("ALL", asList(1, 2)).getName());
    }
}