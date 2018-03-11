package org.queryman.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Operators.ILIKE;
import static org.queryman.builder.Operators.LIKE;
import static org.queryman.builder.Operators.NOT_ILIKE;
import static org.queryman.builder.Operators.NOT_LIKE;
import static org.queryman.builder.Operators.NOT_SIMILAR_TO;
import static org.queryman.builder.Operators.SIMILAR_TO;
import static org.queryman.builder.Queryman.operator;

class OperatorsTest {
    @Test
    void like() {
        assertEquals(NOT_LIKE.getName(), "NOT LIKE");
        assertEquals(LIKE.getName(), "LIKE");
        assertEquals("LIKE", operator("LIKE").getName());
    }

    @Test
    void ilike() {
        assertEquals(NOT_ILIKE.getName(), "NOT ILIKE");
        assertEquals(ILIKE.getName(), "ILIKE");
        assertEquals("ILIKE", operator("ILIKE").getName());
    }

    @Test
    void similarTo() {
        assertEquals(NOT_SIMILAR_TO.getName(), "NOT SIMILAR TO");
        assertEquals(SIMILAR_TO.getName(), "SIMILAR TO");
        assertEquals("SIMILAR TO", operator("SIMILAR TO").getName());
    }

}