/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

import org.queryman.builder.utils.Tools;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.queryman.builder.utils.Tools.EMPTY;

/**
 * Represents qualified name.
 * Examples:
 * <p>
 * <code>PostgreSQL.qualifiedName("public.books");</code> equals "public"."books"
 * <p>
 * <code>PostgreSQL.qualifiedName("books");</code> equals "books"
 * </p>
 *
 * @author Timur Shaidullin
 */
public class QualifiedName extends AbstractToken implements Name {
    public QualifiedName(String name) {
        super(name);
    }

    @Override
    public String getName() {
        if (isEmpty()) {
            return null;
        }

        List<String> collect = Arrays.stream(name.split("\\."))
           .map(part -> "\"" + part + "\"")
           .collect(Collectors.toList());

        return String.join(".", collect.toArray(EMPTY));
    }
}
