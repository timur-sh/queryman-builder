/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.Query;

import java.lang.reflect.Field;

/**
 * @author Timur Shaidullin
 */
public class TreeFormatterTestUtil {
    private static AbstractSyntaxTree tree(Query query) throws NoSuchFieldException, IllegalAccessException {
        Field field = query.getClass().getSuperclass().getDeclaredField("tree");
        field.setAccessible(true);
        AbstractSyntaxTree tree = (AbstractSyntaxTree) field.get(query);
        query.assemble(tree);

        return tree;
    }

    public static String buildPreparedSQL(Query query) throws NoSuchFieldException, IllegalAccessException {
        return new TreeFormatter().buildSQL(tree(query).getRootNode(), true);
    }
}
