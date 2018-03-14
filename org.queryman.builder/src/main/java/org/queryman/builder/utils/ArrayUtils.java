/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.utils;

import org.queryman.builder.token.Expression;

import java.util.Arrays;
import java.util.function.Function;

import static org.queryman.builder.Queryman.asName;

/**
 * @author Timur Shaidullin
 */
public class ArrayUtils {
    public static <T> boolean inArray(T needle, T[] arr) {
        for (T t : arr)
            if (needle == t)
                return true;

        return false;
    }

    /**
     * Wrapping a Byte array to bytes[].
     */
    public static byte[] toPrimitive(Byte[] bytes) {
        byte[] b = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++)
            b[i] = bytes[i];

        return b;
    }

    /**
     * Wrapping a bytes[] to Byte array.
     */
    public static Byte[] toWrapper(byte[] bytes) {
        Byte[] b = new Byte[bytes.length];

        for (int i = 0; i < bytes.length; i++)
            b[i] = bytes[i];

        return b;
    }

    /**
     * Wrapping a float[] to Float array.
     */
    public static Float[] toWrapper(float[] arr) {
        Float[] b = new Float[arr.length];

        for (int i = 0; i < arr.length; i++)
            b[i] = arr[i];

        return b;
    }

    /**
     * Wrapping a short[] to Short array.
     */
    public static Short[] toWrapper(short[] arr) {
        Short[] b = new Short[arr.length];

        for (int i = 0; i < arr.length; i++)
            b[i] = arr[i];

        return b;
    }

    /**
     * Wrapping a integer[] to Integer array.
     */
    public static Integer[] toWrapper(int[] arr) {
        return Arrays.stream(arr).boxed().toArray(Integer[]::new);
    }

    /**
     * Wrapping a long[] to Long array.
     */
    public static Long[] toWrapper(long[] arr) {
        return Arrays.stream(arr).boxed().toArray(Long[]::new);
    }

    /**
     * Wrapping a double[] to Double array.
     */
    public static Double[] toWrapper(double[] arr) {
        return Arrays.stream(arr).boxed().toArray(Double[]::new);
    }

    /**
     * Convert list of object to list of {@link org.queryman.builder.token.expression.ColumnReferenceExpression}.
     *
     * @param values list of names
     * @return array of column reference expressions
     */
    @SafeVarargs
    public static <T>Expression[] toExpressions(T... values) {
        return toExpressions(ExpressionUtil::toExpression, values);
    }

    /**
     * @param func functional interface to convert {@code values} to
     *             appropriate expression.
     * @param values list of values
     *
     * @return array of expressions
     */
    @SafeVarargs
    public static <T> Expression[] toExpressions(Function<T, Expression> func, T... values) {
        return Arrays.stream(values)
           .map(func)
           .toArray(Expression[]::new);
    }
}
