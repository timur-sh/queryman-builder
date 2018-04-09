/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.utils;

import org.queryman.builder.token.Token;

import java.util.Objects;

/**
 * @author Timur Shaidullin
 */
public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || Objects.equals(string, "");
    }

    public static boolean isEmpty(Token token) {
        return token == null || isEmpty(token.getName());
    }
}
