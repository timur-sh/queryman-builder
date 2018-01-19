/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.loader;

import java.util.Objects;

/**
 * @author Timur Shaidullin
 */
public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || Objects.equals(string, "");
    }
}
