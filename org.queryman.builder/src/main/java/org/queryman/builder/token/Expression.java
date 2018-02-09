/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

import org.queryman.builder.utils.ArraysUtils;
import org.queryman.builder.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.queryman.builder.utils.Tools.EMPTY_STRING;

/**
 * Expressions are:
 * <ul>
 * <li>{@link ExpressionType#DEFAULT} is used for constants(except string asConstant),
 * function calls operator invocations, subsctipts </li>
 * <li>{@link ExpressionType#STRING_CONSTANT} represents a string asConstant</li>
 * <li>{@link ExpressionType#DOLLAR_STRING} represents a dollar string asConstant</li>
 * <li>{@link ExpressionType#COLUMN_REFERENCE} represents a simple column references</li>
 * <li>{@link ExpressionType#FIELD_SELECTION} represents a field selection. </li>
 *
 * <li>{@link ExpressionType#LIST} represents a list of values expression. </li>
 * <li>{@link ExpressionType#STRING_LIST} represents a list of strings expression. </li>
 *
 * <li>{@link ExpressionType#ARRAY} represents an array of values expression. </li>
 * <li>{@link ExpressionType#STRING_ARRAY} represents an array of strings expression. </li>
 * </ul>
 *
 * Function expression
 * <ul>
 *  <li>{@link ExpressionType#FUNC} </li>
 * </ul>
 *
 * The above two {@link ExpressionType#COLUMN_REFERENCE} and {@link ExpressionType#FIELD_SELECTION}
 * may be surrounded by quotes:
 * Example
 * <code>
 *
 * </code>
 *
 * @author Timur Shaidullin
 */
public class Expression<T> extends AbstractToken {
    private final ExpressionType type;
    private String tagName = "";
    private boolean quoted = false;

    private Expression expression;
    private T[] arr;

    public Expression(String constant, ExpressionType type) {
        super(constant);
        this.type = type;
    }

    public Expression(Number constant, ExpressionType type) {
        this(constant.toString(), type);
    }

    @SafeVarargs
    public Expression(ExpressionType type, T... constants) {
        this("", type);
        arr = constants;
    }

    public Expression(String name, ExpressionType type, Expression expression) {
        this(name, type);
        Objects.requireNonNull(expression);
        this.expression = expression;
    }

    /**
     * Tag name is used to surround dollar string. If no tag is given, the empty
     * {@code ""} string will be used.
     *
     * Example:
     * With tag name {@code type}: $type$ it contains any text $type$
     * Without tag name: $$ it contains any text $$
     */
    public Expression setTagName(String tagName) {
        this.tagName = tagName;

        return this;
    }

    public Expression setQuoted(boolean quoted) {
        this.quoted = quoted;

        return this;
    }

    @Override
    public String getName() {
        switch (type) {
            case DEFAULT:
                return name;
            case STRING_CONSTANT:
                return stringName();
            case DOLLAR_STRING:
                return dollarStringName();
            case COLUMN_REFERENCE:
                return columnReferenceName();
            case FIELD_SELECTION:
                return fieldSelectionName();
            case LIST:
                return listNames();
            case STRING_LIST:
                return listStringNames();
            case ARRAY:
                return arrayNames();
            case STRING_ARRAY:
                return stringArrayNames();
            case FUNC:
                return func();
        }

        throw new RuntimeException("The type is not handled");
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(name) && arr == null;
    }

    public String func() {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        String result = expression.getName();
        ExpressionType[] expressionTypes = {ExpressionType.LIST, ExpressionType.STRING_LIST};

        if (ArraysUtils.inArray(expression.type, expressionTypes))
            return String.join("", name, result);

        return String.join("", name, "(", result ,")");
    }

    /**
     * @return an array of values e.g. ARRAY[1, 2 [,...]]
     */
    public String arrayNames() {
        if (arr == null)
            return "ARRAY[]";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = String.valueOf(arr[i]);
        }

        return "ARRAY[" + String.join(", ", result) + "]";
    }

    /**
     * @return an array of strings. e.g. ARRAY['1', '2' [,...]]
     */
    private String stringArrayNames() {
        if (arr == null)
            return "ARRAY[]";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = toPostgresqlString(String.valueOf(arr[i]));
        }

        return "ARRAY[" + String.join(", ", result) + "]";
    }

    /**
     * Wrap the {@code name} into single quotes
     */
    private String toPostgresqlString(String name) {
        if (StringUtils.isEmpty(name))
            return "";

        return "'" + name.replaceAll("'", "''") + "'";
    }

    /**
     * @return a list of names e.g. (1, 2 [,...])
     */
    private String listNames() {
        if (arr == null)
            return "()";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = String.valueOf(arr[i]);
        }

        return "(" + String.join(", ", result) + ")";
    }

    /**
     * @return a list of string names. e.g. ('1', '2' [,...])
     */
    private String listStringNames() {
        if (arr == null)
            return "()";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = toPostgresqlString(String.valueOf(arr[i]));
        }

        return "(" + String.join(", ", result) + ")";
    }

    /**
     * @return a string surrounded by single quote string. e.g. 'string'
     */
    private String stringName() {
        if (isEmpty()) {
            return null;
        }

        return toPostgresqlString(name);
    }

    /**
     * @return a string surrounded by dollar singes string. e.g. $$string$$ or
     * $tag$string$tag$
     */
    private String dollarStringName() {
        if (isEmpty()) {
            return null;
        }

        String tag = "$" + tagName +"$";

        return tag + name + tag;
    }

    /**
     * {@code true} is returned if name is valid qualified name, i.e
     * <code>tablename</code>
     * <code>public.tablename</code>
     * <code>(tablename.compositecol).id</code>
     */
    private boolean checkQualifiedName(String[] parts) {
        for (String part : parts) {
            part = part.replaceAll("[()]", "");

            if (StringUtils.isEmpty(part))
                return false;
        }

        return true;
    }

    /**
     * @return a qualified name. e.g. table.column
     */
    private String columnReferenceName() {
        if (isEmpty()) {
            return null;
        }

        String[] parts = name.split("\\.");

        if (!checkQualifiedName(parts)) {
            //todo log it
            System.err.println("WARNING: '" + name + "' is not valid qualified name");
        }

        if (quoted) {
            List<String> collect = Arrays.stream(parts)
               .map(part -> "\"" + part + "\"")
               .collect(Collectors.toList());

            return String.join(".", collect.toArray(EMPTY_STRING));
        }

        return String.join(".", parts);

    }

    /**
     * @return field selection name.
     */
    private String fieldSelectionName() {
        if (isEmpty()) {
            return null;
        }

        String[] parts = name.split("\\.");

        if (!checkQualifiedName(parts)) {
            //todo log it
            System.err.println("WARNING: '" + name + "' is not valid qualified name");
        }

        return String.join(".", parts);
    }

    public enum ExpressionType {
        /**
         * Represent a default expression. It is not surrounded by quotes. It
         * is printed as is.
         * <p>
         * <code>
         * table_name
         * $n1
         * LIST[1]
         * 234.11
         * .50
         * .2E+1
         * </code>
         */
        DEFAULT,

        /**
         * Represents string asConstant. Variable is surrounded by single quotes:
         * Example:
         * <code>
         * 'string variable is here'
         * 'string variable''s here'
         * </code>
         */
        STRING_CONSTANT,

        /**
         * Represents dollar string asConstant. Variable is surrounded by single quotes:
         * Example:
         * <code>
         * $$string variable is here$$
         * </code>
         */
        DOLLAR_STRING,

        /**
         * Represent a qualified name. Usual it is an either column or table reference.
         * <p>
         * <code>
         * public.books
         * </code>
         */
        COLUMN_REFERENCE,

        /**
         * Represent a qualified quoted name. Usual it is an either column or table reference.
         * <p>
         * <code>
         * (compositecol).somefield
         * </code>
         */
        FIELD_SELECTION,

        /**
         * Represent a list of value expressions.
         * <p>
         * <code>
         * (1, 2, 3 [,...])
         * </code>
         */
        LIST,

        /**
         * Represent a list of string expressions.
         * <p>
         * <code>
         * ('one', 'two', 'three', [,...])
         * </code>
         */
        STRING_LIST,

        /**
         * Represent a list of value expressions.
         * <p>
         * <code>
         * ARRAY[1, 2, 3 [,...]]
         * </code>
         */
        ARRAY,

        /**
         * Represent a list of string expressions.
         * <p>
         * <code>
         * ARRAY['one', 'two', 'three', [,...]]
         * </code>
         */
        STRING_ARRAY,

        /**
         * Functions with an argument array
         */
        FUNC,

    }
}