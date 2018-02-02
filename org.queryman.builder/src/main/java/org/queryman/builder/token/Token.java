/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

import org.queryman.builder.ast.AstVisitor;

/**
 * PostgreSQL token can be a key word, an identifier(that is splitted on unqualifiedName
 * and qualifiedName), a constant, an operator. Any of above tokens must be based
 * on this interface.
 *
 * @see Field
 * @see Keyword
 * @see Operator
 * @see UnquialifiedName
 * @see QualifiedName
 *
 * @author Timur Shaidullin
 */
public interface Token extends AstVisitor {

}

/*
token
    field
        unqualifiedName
        qualifiedName
        constant
    identifier
    operator
 */