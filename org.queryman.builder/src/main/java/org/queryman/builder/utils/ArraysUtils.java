/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.utils;

import java.util.Arrays;

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
}
