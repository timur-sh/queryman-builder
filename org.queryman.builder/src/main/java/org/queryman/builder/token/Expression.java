/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

import org.queryman.builder.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.queryman.builder.utils.Tools.EMPTY;

/**
 * Expressions are:
 * <ul>
 * <li>{@link ExpressionType#DEFAULT} is used for constants(except string asConstant),
 * function calls operator invocations, subsctipts </li>
 * <li>{@link ExpressionType#STRING_CONSTANT} represents a string asConstant</li>
 * <li>{@link ExpressionType#DOLLAR_STRING} represents a dollar string asConstant</li>
 * <li>{@link ExpressionType#COLUMN_REFERENCE} represents a simple column references</li>
 * <li>{@link ExpressionType#FIELD_SELECTION} represents a field selection. </li>
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
public class Expression extends AbstractToken implements Field {
    private final ExpressionType type;
    private String tagName = "";
    private boolean quoted = false;

    public Expression(String constant, ExpressionType type) {
        super(constant);
        this.type = type;
    }

    public Expression(Number constant, ExpressionType type) {
        this(constant.toString(), type);
    }

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
        if (isEmpty()) {
            return null;
        }

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
        }

        throw new RuntimeException("The type is not handled");
    }

    private String stringName() {
        return "'" + name.replaceAll("'", "''") + "'";
    }

    private String dollarStringName() {
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

    private String columnReferenceName() {
        String[] parts = name.split("\\.");

        if (!checkQualifiedName(parts)) {
            //todo log it
            System.err.println("WARNING: '" + name + "' is not valid qualified name");
        }

        if (quoted) {
            List<String> collect = Arrays.stream(parts)
               .map(part -> "\"" + part + "\"")
               .collect(Collectors.toList());

            return String.join(".", collect.toArray(EMPTY));
        }

        return String.join(".", parts);

    }

    private String fieldSelectionName() {
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
         * ARRAY[1]
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

    }
}