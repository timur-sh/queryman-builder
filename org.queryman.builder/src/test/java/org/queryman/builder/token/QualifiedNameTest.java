package org.queryman.builder.token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.qualifiedName;

class QualifiedNameTest {
    @Test
    void nameWithSchema() {
        Name name = qualifiedName("public.books");
        assertEquals("\"public\".\"books\"", name.getName());
    }

    @Test
    void nameWithoutSchema() {
        Name name = qualifiedName("books");
        assertEquals("\"books\"", name.getName());
    }

    @Test
    void nameWithEmptySchema() {
        Name name = qualifiedName(".books");
        assertEquals("\"\".\"books\"", name.getName());
    }
}