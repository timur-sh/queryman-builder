/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * The expression is a main part of query. Queryman Builder provides expressions
 * showing in the list below:
 * <ul>
 *     <li>constant expression</li>
 *     <li>function call expression</li>
 *     <li>list expression</li>
 *     <li>subquery expression</li>
 * </ul>
 *
 *
 * @author Timur Shaidullin
 */
public abstract class Expression extends AbstractToken
   implements
   ExpressionAsStep,
   ExpressionCastStep {

    /**
     * Alias of expression.
     */
    protected String outputName;

    /**
     * Specified type for explicitly casting.
     */
    private String castType;

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

        if (castType != null)
            builder.append("::").append(castType);

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
    public final Expression cast(String type) {
        this.castType = type;
        return this;
    }

    @Override
    public final Expression as(String alias) {
        this.outputName = alias;
        return this;
    }

    @Override
    public final Expression as(String alias, String... columns) {
        as(alias);

        this.columns = columns;
        return this;
    }

    /**
     * Wrap the {@code name} into single quotes
     */
    protected String toPostgreSQLString(String name) {
        if (isEmpty())
            return "";

        return "'" + name.replaceAll("'", "''") + "'";
    }


    protected abstract String prepareName();
}