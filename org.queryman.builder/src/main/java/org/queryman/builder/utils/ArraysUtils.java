/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.utils;

/**
 * @author Timur Shaidullin
 */
public class ArraysUtils {
    public static <T> boolean inArray(T needle, T[] arr) {
        for (T t : arr)
            if (needle == t)
                return true;

        return false;
    }
}
