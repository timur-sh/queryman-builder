package org.queryman.builder.token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.queryman.builder.token.Expression.ExpressionType.*;

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
}