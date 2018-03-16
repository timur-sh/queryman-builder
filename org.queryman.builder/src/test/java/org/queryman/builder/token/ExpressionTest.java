package org.queryman.builder.token;

import org.junit.jupiter.api.Test;
import org.queryman.builder.Query;
import org.queryman.builder.ast.TreeFormatterTestUtil;
import org.queryman.builder.token.expression.NullExpression;
import org.queryman.builder.token.expression.prepared.BigDecimalExpression;
import org.queryman.builder.token.expression.prepared.BooleanExpression;
import org.queryman.builder.token.expression.prepared.ByteExpression;
import org.queryman.builder.token.expression.prepared.BytesExpression;
import org.queryman.builder.token.expression.prepared.DateExpression;
import org.queryman.builder.token.expression.prepared.DoubleExpression;
import org.queryman.builder.token.expression.prepared.FloatExpression;
import org.queryman.builder.token.expression.prepared.IntegerExpression;
import org.queryman.builder.token.expression.prepared.LongExpression;
import org.queryman.builder.token.expression.prepared.ShortExpression;
import org.queryman.builder.token.expression.prepared.TimeExpression;
import org.queryman.builder.token.expression.prepared.TimestampExpression;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.queryman.builder.Queryman.all;
import static org.queryman.builder.Queryman.any;
import static org.queryman.builder.Queryman.asArray;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asDollarString;
import static org.queryman.builder.Queryman.asFunc;
import static org.queryman.builder.Queryman.asList;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.asQuotedName;
import static org.queryman.builder.Queryman.asSubQuery;
import static org.queryman.builder.Queryman.select;
import static org.queryman.builder.Queryman.some;
import static org.queryman.builder.Queryman.values;
import static org.queryman.builder.TestHelper.testBindParameters;

class ExpressionTest {
    @Test
    void constantExpression() {
        assertEquals("table_name", asName("table_name").getName());

        assertEquals("234.11", asConstant(234.11d).getName());
        assertEquals("?", ((PreparedExpression) asConstant(234.11d)).getPlaceholder());
        assertEquals(234.11, ((PreparedExpression) asConstant(234.11d)).getValue());
        assertTrue(asConstant(234.11d) instanceof DoubleExpression);

        assertEquals("0.5", asConstant(.50f).getName());
        assertEquals("?", ((PreparedExpression) asConstant(.50f)).getPlaceholder());
        assertEquals(0.5f, ((PreparedExpression) asConstant(.50f)).getValue());
        assertTrue(asConstant(.50f) instanceof FloatExpression);

        assertEquals("1", asConstant((byte) 1).getName());
        assertEquals("?", ((PreparedExpression) asConstant((byte) 1)).getPlaceholder());
        assertEquals((byte) 1, ((PreparedExpression) asConstant((byte) 1)).getValue());
        assertTrue(asConstant((byte) 1) instanceof ByteExpression);

        assertEquals("1", asConstant(1).getName());
        assertEquals("?", ((PreparedExpression) asConstant( 1)).getPlaceholder());
        assertEquals(1, ((PreparedExpression) asConstant(1)).getValue());
        assertTrue(asConstant(1) instanceof IntegerExpression);

        assertEquals("1", asConstant(1L).getName());
        assertEquals("?", ((PreparedExpression) asConstant(1L)).getPlaceholder());
        assertEquals(1L, ((PreparedExpression) asConstant(1L)).getValue());
        assertTrue(asConstant(1L) instanceof LongExpression);

        assertEquals("1", asConstant((short) 1).getName());
        assertEquals("?", ((PreparedExpression) asConstant((short) 1)).getPlaceholder());
        assertEquals((short) 1, ((PreparedExpression) asConstant((short) 1)).getValue());
        assertTrue(asConstant((short) 1) instanceof ShortExpression);

        assertEquals("true", asConstant(true).getName());
        assertEquals("?", ((PreparedExpression) asConstant(true)).getPlaceholder());
        assertEquals(true, ((PreparedExpression) asConstant(true)).getValue());
        assertTrue(asConstant(true) instanceof BooleanExpression);

        Date d1 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("Y-MM-dd");

        assertEquals("'" + format.format(d1) + "'", asConstant(d1).getName());
        assertEquals("?", ((PreparedExpression) asConstant(d1)).getPlaceholder());
        assertEquals(d1, ((PreparedExpression) asConstant(d1)).getValue());
        assertTrue(asConstant(d1) instanceof DateExpression);

        java.sql.Date d2 = new java.sql.Date(d1.getTime());
        assertEquals("'" + format.format(d2) + "'", asConstant(d2).getName());
        assertTrue(asConstant(d2) instanceof DateExpression);

        Time time = new Time(System.currentTimeMillis());
        assertEquals("?", ((PreparedExpression) asConstant(time)).getPlaceholder());
        assertEquals(time, ((PreparedExpression) asConstant(time)).getValue());
        assertTrue(asConstant(time) instanceof TimeExpression);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        assertEquals("?", ((PreparedExpression) asConstant(timestamp)).getPlaceholder());
        assertEquals(timestamp, ((PreparedExpression) asConstant(timestamp)).getValue());
        assertTrue(asConstant(timestamp) instanceof TimestampExpression);

        assertEquals("NULL", asConstant(null).getName());
        assertTrue(asConstant(null) instanceof NullExpression);

        BigDecimal bigDecimal = new BigDecimal(1);
        assertEquals("1", asConstant(bigDecimal).getName());
        assertEquals("?", ((PreparedExpression) asConstant(bigDecimal)).getPlaceholder());
        assertEquals(bigDecimal, ((PreparedExpression) asConstant(bigDecimal)).getValue());
        assertTrue(asConstant(bigDecimal) instanceof BigDecimalExpression);
    }

    @Test
    void constantArrayExpression() {
        byte[] b = { 1, 2, 3 };
        Byte[] b1 = { 1, 2, 3 };

        assertEquals("ARRAY[1, 2, 3]", asConstant(b).getName());
        assertEquals("?", ((PreparedExpression) asConstant(b)).getPlaceholder());
        assertTrue(asConstant(b) instanceof BytesExpression);

        assertEquals("ARRAY[1, 2, 3]", asConstant(b1).getName());
        assertArrayEquals(b1, ( Byte[])((PreparedExpression) asConstant(b1)).getValue());
        assertTrue(asConstant(b1) instanceof BytesExpression);
    }

    @Test
    void stringConstantExpression() {
        assertEquals("'Timur'", asConstant("Timur").getName());
        assertEquals("?", ((PreparedExpression) asConstant("Timur")).getPlaceholder());
        assertEquals("Timur", ((PreparedExpression) asConstant("Timur")).getValue());

        assertEquals("'I''m Timur'", asConstant("I'm Timur").getName());
        assertEquals("I'm Timur", ((PreparedExpression) asConstant("I'm Timur")).getValue());
    }

    @Test
    void dollarStringExpression() {
        assertEquals("$$Timur$$", asDollarString("Timur").getName());
        assertEquals("?", ((PreparedExpression) asDollarString("Timur")).getPlaceholder());
        assertEquals("Timur", ((PreparedExpression) asDollarString("Timur")).getValue());

        assertEquals("$$I'm Timur$$", asDollarString("I'm Timur").getName());
        assertEquals("?", ((PreparedExpression) asDollarString("I'm Timur")).getPlaceholder());
        assertEquals("I'm Timur", ((PreparedExpression) asDollarString("I'm Timur")).getValue());

        assertEquals("$name$I'm Timur$name$", asDollarString("I'm Timur", "name").getName());
        assertEquals("?", ((PreparedExpression) asDollarString("I'm Timur", "name")).getPlaceholder());
        assertEquals("I'm Timur", ((PreparedExpression) asDollarString("I'm Timur", "name")).getValue());
    }

    @Test
    void columnReference() {
        assertEquals("table.column", asName("table.column").getName());
        assertEquals("\"table\".\"column\"", asQuotedName("table.column").getName());
    }

    @Test
    void fieldSelection() {
        assertEquals("expression.fieldname", asName("expression.fieldname").getName());
        assertEquals(".fieldname", asName(".fieldname").getName());
        assertEquals("(tablename.compositecol).id", asName("(tablename.compositecol).id").getName());
    }

    @Test
    void asConstantListExpression() {
        assertEquals("()", asList().getName());

        Expression[] numbers = new Expression[]{ asConstant("one"), asConstant("two") };

        assertEquals("('one', 'two')", asList(numbers).getName());
        assertEquals("(?, ?)", ((PreparedExpression) asList(numbers)).getPlaceholder());
        testBindParameters(asList(numbers), map -> {
            assertTrue(map.size() == 2);
            assertEquals("one", map.get(1).getValue());
            assertEquals("two", map.get(2).getValue());
        });

        Expression strings = asList("one", "two", "three", "four", "five", "six");
        assertEquals("(one, two, three, four, five, six)", strings.getName());

        Expression chars = asList('1', '2', '3', '4', '5', '6');
        assertEquals("('1', '2', '3', '4', '5', '6')", chars.getName());

        Expression integers = asList(1, 0x1a);
        assertEquals("(1, 26)", integers.getName());

        Expression floats = asList(1f, 2e1);
        assertEquals("(1.0, 20.0)", floats.getName());

        Expression doubles = asList(1d, 2e-3);
        assertEquals("(1.0, 0.002)", doubles.getName());

        Expression longs = asList(1L, 2L, 0xFF_EC_DE_5E, 1_2_3L);
        assertEquals("(1, 2, -1253794, 123)", longs.getName());

        assertEquals("('one', 'two')", asList(asConstant("one"), asConstant("two")).getName());

        assertEquals("('1', '2')", asList(List.of(asConstant("1"), asConstant('2'))).getName());

        assertEquals("('1', '2')", asList(List.of('1', '2')).getName());

        assertEquals("(1, 2)", asList(List.of(1, 2)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1f, 2f)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1d, 2d)).getName());
    }

    @Test
    void asListForCollectionTest() {
        Expression e = asList(List.of(1, 2, 3));
        assertEquals("(1, 2, 3)", e.getName());

        e = asList(Set.of(1, 2, 3));
        assertEquals("(?, ?, ?)", ((PreparedExpression) e).getPlaceholder());
    }

    @Test
    void asListExpression() {
        assertEquals("()", asList().getName());

        Number[] numbers2 = { 1, 2 };
        assertEquals("(1, 2)", asList(numbers2).getName());

        Expression strings = asList("1", "2");
        assertEquals("(1, 2)", strings.getName());

        Expression chars = asList('1', '2', '3', '4', '5', '6');
        assertEquals("('1', '2', '3', '4', '5', '6')", chars.getName());

        Expression integers = asList(1, 0x1a);
        assertEquals("(1, 26)", integers.getName());

        Expression floats = asList(1f, 2e1);
        assertEquals("(1.0, 20.0)", floats.getName());

        Expression doubles = asList(1d, 2e-3);
        assertEquals("(1.0, 0.002)", doubles.getName());

        Expression longs = asList(1L, 2L, 0xFF_EC_DE_5E, 1_2_3L);
        assertEquals("(1, 2, -1253794, 123)", longs.getName());

        assertEquals("('one', 'two')", asList(asConstant("one"), asConstant("two")).getName());

        assertEquals("('1', '2')", asList(List.of('1', '2')).getName());

        assertEquals("(1, 2)", asList(List.of(1, 2)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1f, 2f)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1d, 2d)).getName());
    }

    @Test
    void arrayTest() {
        assertEquals("ARRAY[]", asArray().getName());

        Integer[] numbers = { 1, 2 };
        assertEquals("ARRAY[1, 2]", asArray(numbers).getName());
        assertEquals("?",((PreparedExpression)asArray(numbers)).getPlaceholder());
        assertEquals("ARRAY[1, 2]::bigint[]", asArray(numbers).cast("bigint[]").getName());
        assertEquals("?::bigint[]", ((PreparedExpression)asArray(numbers).cast("bigint[]")).getPlaceholder());
        assertEquals("ARRAY[1, 2]", asArray(List.of(numbers)).getName());
        assertEquals("ARRAY[1, 2]", asArray(List.of(1, 2)).getName());
        assertEquals("ARRAY[]", asArray().getName());

        assertEquals("ARRAY[]", asArray().getName());

        String[] numbers2 = { "1", "2" };
        assertEquals("ARRAY[1, 2]", asArray(numbers2).getName());
        assertEquals("ARRAY['1', '2']", asArray(List.of(asConstant("1"), asConstant("2"))).getName());
    }

    @Test
    void aliasTest() {
        assertEquals("book AS b", asName("book").as("b").getName());
    }


    //------
    // FUNCTIONS AND OPERATORS
    //------

    @Test
    void functionAll() {
        assertEquals("ALL(ARRAY[])", all(asArray()).getName());
        assertEquals("ALL(?)", ((PreparedExpression) all(asArray())).getPlaceholder());
        testBindParameters(all(asArray()), map -> {
            assertTrue(map.size() == 1);
        });

        assertEquals("ALL(ARRAY[1, 2])", all(asArray(1, 2)).getName());
        testBindParameters(all(asArray(1, 2)), map -> {
            assertTrue(map.size() == 1);
        });
        assertEquals("ALL()", all(asList()).getName());
        assertEquals("ALL(1, 2)", all(asList(1, 2)).getName());
        testBindParameters(all(asList(1, 2)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        assertEquals("ALL(NULL, 1)", all(null, 1).getName());
        assertEquals("ALL(1, 2)", all(1, 2).getName());
        assertEquals("ALL(?, ?)", ((PreparedExpression) all(1, 2)).getPlaceholder());
        testBindParameters(all(asList(1, 2)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        assertEquals("ALL(SELECT 1 FROM book WHERE 1 = id)", all(select(1).from("book").where(1, "=", "id")).getName());
        assertEquals("ALL(SELECT 1 FROM book WHERE ? = id)", ((PreparedExpression) all(select(1).from("book").where(3, "=", "id"))).getPlaceholder());
        testBindParameters(all(select(1).from("book").where(3, "=", "id")), map -> {
            assertTrue(map.size() == 1);
            assertEquals(3, map.get(1).getValue());
        });

        assertEquals("ALL(VALUES(1), (2))", all(values(1, 2)).getName());
        assertEquals("ALL(VALUES(?), (?))", ((PreparedExpression) all(values(1, 3))).getPlaceholder());
        testBindParameters(all(asList(1, 3)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(3, map.get(2).getValue());
        });
    }

    @Test
    void functionAny() {
        assertEquals("ANY(ARRAY[])", any(asArray()).getName());
        assertEquals("ANY(?)", ((PreparedExpression) any(asArray())).getPlaceholder());
        testBindParameters(any(asArray()), map -> {
            assertTrue(map.size() == 1);
        });

        assertEquals("ANY(ARRAY[1, 2])", any(asArray(1, 2)).getName());
        testBindParameters(any(asArray(1, 2)), map -> {
            assertTrue(map.size() == 1);
        });
        assertEquals("ANY()", any(asList()).getName());
        assertEquals("ANY(1, 2)", any(asList(1, 2)).getName());
        testBindParameters(any(asList(1, 2)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        assertEquals("ANY(NULL, 1)", any(null, 1).getName());
        assertEquals("ANY(1, 2)", any(1, 2).getName());
        assertEquals("ANY(?, ?)", ((PreparedExpression) any(1, 2)).getPlaceholder());
        testBindParameters(any(asList(1, 2)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        assertEquals("ANY(SELECT 1 FROM book WHERE 1 = id)", any(select(1).from("book").where(1, "=", "id")).getName());
        assertEquals("ANY(SELECT 1 FROM book WHERE ? = id)", ((PreparedExpression) any(select(1).from("book").where(3, "=", "id"))).getPlaceholder());
        testBindParameters(any(select(1).from("book").where(3, "=", "id")), map -> {
            assertTrue(map.size() == 1);
            assertEquals(3, map.get(1).getValue());
        });

        assertEquals("ANY(VALUES(1), (2))", any(values(1, 2)).getName());
        assertEquals("ANY(VALUES(?), (?))", ((PreparedExpression) any(values(1, 3))).getPlaceholder());
        testBindParameters(any(asList(1, 3)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(3, map.get(2).getValue());
        });
    }

    @Test
    void functionSome() {
        assertEquals("SOME(ARRAY[])", some(asArray()).getName());
        assertEquals("SOME(?)", ((PreparedExpression) some(asArray())).getPlaceholder());
        testBindParameters(some(asArray()), map -> {
            assertTrue(map.size() == 1);
        });

        assertEquals("SOME(ARRAY[1, 2])", some(asArray(1, 2)).getName());
        testBindParameters(some(asArray(1, 2)), map -> {
            assertTrue(map.size() == 1);
        });
        assertEquals("SOME()", some(asList()).getName());
        assertEquals("SOME(1, 2)", some(asList(1, 2)).getName());
        testBindParameters(some(asList(1, 2)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        assertEquals("SOME(NULL, 1)", some(null, 1).getName());
        assertEquals("SOME(1, 2)", some(1, 2).getName());
        assertEquals("SOME(?, ?)", ((PreparedExpression) some(1, 2)).getPlaceholder());
        testBindParameters(some(asList(1, 2)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
        });

        assertEquals("SOME(SELECT 1 FROM book WHERE 1 = id)", some(select(1).from("book").where(1, "=", "id")).getName());
        assertEquals("SOME(SELECT 1 FROM book WHERE ? = id)", ((PreparedExpression) some(select(1).from("book").where(3, "=", "id"))).getPlaceholder());
        testBindParameters(some(select(1).from("book").where(3, "=", "id")), map -> {
            assertTrue(map.size() == 1);
            assertEquals(3, map.get(1).getValue());
        });

        assertEquals("SOME(VALUES(1), (2))", some(values(1, 2)).getName());
        assertEquals("SOME(VALUES(?), (?))", ((PreparedExpression) some(values(1, 3))).getPlaceholder());
        testBindParameters(some(asList(1, 3)), map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(3, map.get(2).getValue());
        });
    }

    @Test
    void valuesTest() {
        Expression e1 = values(asList(1, 2), asList(3, 4));
        assertEquals("VALUES(1, 2), (3, 4)", e1.getName());
        assertEquals("VALUES(?, ?), (?, ?)", ((PreparedExpression) e1).getPlaceholder());
        testBindParameters(e1, map -> {
            assertTrue(map.size() == 4);
            assertEquals(1, map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
            assertEquals(3, map.get(3).getValue());
            assertEquals(4, map.get(4).getValue());
        });

        Expression e2 = values(asList(1, 2), asList(3, 4)).as("point", "x", "y");
        assertEquals("(VALUES(1, 2), (3, 4)) AS point(x, y)", e2.getName());
        assertEquals("(VALUES(?, ?), (?, ?)) AS point(x, y)", ((PreparedExpression) e2).getPlaceholder());


        assertEquals("VALUES(1), (2)", values(1, 2).getName());
        assertEquals("(VALUES(1), (2)) AS point(x, y)", values(1, 2).as("point", "x", "y").getName());
    }

    @Test
    void existsTest() throws NoSuchFieldException, IllegalAccessException {
        Query query = select(
           asFunc("EXISTS", asSubQuery(select("*").from("book").where("id", "=", 1))).as("exists")
        );
        PreparedExpression prepared = (PreparedExpression) asSubQuery(select("*").from("book").where("id", "=", 1));
        testBindParameters(prepared, map -> {
            assertTrue(map.size() == 1);
            assertEquals(1, map.get(1).getValue());
        });

        assertEquals("SELECT EXISTS(SELECT * FROM book WHERE id = 1) AS exists", query.sql());
        assertEquals("(SELECT * FROM book WHERE id = ?)", prepared.getPlaceholder());
        assertEquals("SELECT EXISTS(SELECT * FROM book WHERE id = ?) AS exists", TreeFormatterTestUtil.buildPreparedSQL(query));
    }

    @Test
    void commonTest() {
        Expression e = asFunc("concat", asConstant("price"), 2, asConstant("USD"));
        assertEquals("concat('price', 2, 'USD')", e.getName());
        testBindParameters(e, map -> {
            assertTrue(map.size() == 3);
            assertEquals("price", map.get(1).getValue());
            assertEquals(2, map.get(2).getValue());
            assertEquals("USD", map.get(3).getValue());
        });
    }

    @Test
    void subQueryTest() {
        Query query = select("*").from("book").where("id", "=", 1);

        PreparedExpression e = (PreparedExpression) asSubQuery(select(asFunc("EXISTS", query)).from("book").whereExists(query));
        assertEquals("(SELECT EXISTS(SELECT * FROM book WHERE id = 1) FROM book WHERE EXISTS (SELECT * FROM book WHERE id = 1))", e.getName());
        assertEquals("(SELECT EXISTS(SELECT * FROM book WHERE id = ?) FROM book WHERE EXISTS (SELECT * FROM book WHERE id = ?))", e.getPlaceholder());
        testBindParameters(e, map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(1, map.get(2).getValue());
        });

        PreparedExpression e2 = (PreparedExpression) asSubQuery(select(asSubQuery(query)).from("book").whereExists(query));
        assertEquals("(SELECT (SELECT * FROM book WHERE id = 1) FROM book WHERE EXISTS (SELECT * FROM book WHERE id = 1))", e2.getName());
        assertEquals("(SELECT (SELECT * FROM book WHERE id = ?) FROM book WHERE EXISTS (SELECT * FROM book WHERE id = ?))", e2.getPlaceholder());
        testBindParameters(e, map -> {
            assertTrue(map.size() == 2);
            assertEquals(1, map.get(1).getValue());
            assertEquals(1, map.get(2).getValue());
        });

    }
}