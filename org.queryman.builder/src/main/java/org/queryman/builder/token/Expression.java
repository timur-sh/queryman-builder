/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

import org.queryman.builder.utils.StringUtils;

/**
 * PostgreSQL expressions:
 *
 * <ul>
 *     <li>String</li>
 *     <li>Dollar string</li>
 *     <li>Constant</li>
 *     <li>List</li>
 *     <li>Array</li>
 * </ul>
 *
 * @see org.queryman.builder.token.expression.ArrayExpression
 * @see org.queryman.builder.token.expression.ArrayStringExpression
 * @see org.queryman.builder.token.expression.ColumnReferenceExpression
 * @see org.queryman.builder.token.expression.ConstantExpression
 * @see org.queryman.builder.token.expression.DollarStringExpression
 * @see org.queryman.builder.token.expression.FuncExpression
 * @see org.queryman.builder.token.expression.ListExpression
 * @see org.queryman.builder.token.expression.ListStringExpression
 * @see org.queryman.builder.token.expression.StringExpression
 *
 * @author Timur Shaidullin
 */
public abstract class Expression extends AbstractToken {

    /**
     * Alias of expression.
     */
    protected String outputName;

    /**
     * Column aliases of derived table.
     */
    private String[] columns;

    public Expression(String constant) {
        super(constant);
    }


    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append(prepareName());

        if (outputName != null)
            builder
               .append(" ")
               .append("AS")
               .append(" ")
               .append(outputName)
               ;

        if (columns != null) {
            if (columns.length == 0)
                builder.append("()");
            else {
                String[] result = new String[columns.length];

                for (int i = 0; i < columns.length; i++) {
                    result[i] = String.valueOf(columns[i]);
                }

                builder.append("(" + String.join(", ", result) + ")");
            }
        }

        return builder.toString();
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(name);
    }

    public final Expression as(String alias) {
        this.outputName = alias;
        return this;
    }

    public final Expression as(String alias, String... columns) {
        as(alias);

        this.columns = columns;
        return this;
    }

    /**
     * Wrap the {@code name} into single quotes
     */
    protected String toPostgresqlString(String name) {
        if (StringUtils.isEmpty(name))
            return "";

        return "'" + name.replaceAll("'", "''") + "'";
    }


    protected abstract String prepareName();
}