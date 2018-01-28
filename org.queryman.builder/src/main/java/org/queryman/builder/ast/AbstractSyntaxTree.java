/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.util.List;

/**
 * This class provides API to manipulate abstract syntax tree of SQL. Each node must
 * start with {@link #startNode(NodeMetadata)} and end with {@link #endNode()}
 *
 * Each leaf added at the end of parent node.
 *
 * @author Timur Shaidullin
 */
public interface AbstractSyntaxTree {
    /**
     * Each node must have a {@code metadata} object.
     */
    AbstractSyntaxTree startNode(NodeMetadata metadata);

    /**
     * Each node must have a {@code metadata}. Set custom delimiter.
     * See {@link #setDelimiter(String)}.
     */
    AbstractSyntaxTree startNode(NodeMetadata metadata, String delimiter);

    /**
     * Each leaves must separated by {@code delimiter} token. If it is not
     * specified, space {code " "} is used as default delimiter.
     */
    AbstractSyntaxTree setDelimiter(String delimiter);

    /**
     * Each node must closed.
     */
    AbstractSyntaxTree endNode();

    /**
     * Add {@code leaf} into current node. Kinds of leaf are SQL key words,
     * identifiers, constants, expressions etc.
     */
    AbstractSyntaxTree addLeaf(String leaf);

    /**
     * Add {@code leaves} into current node. Kinds of leaves are SQL key words,
     * identifiers, constants, expressions etc.
     */
    AbstractSyntaxTree addLeaves(String... leaves);

    /**
     * Add {@code leaves} into current node. Kinds of leaves are SQL key words,
     * identifiers, constants, expressions etc.
     */
    AbstractSyntaxTree addLeaves(List<String> leaves);

    /**
     * Insert child node into the parent node.
     */
    AbstractSyntaxTree addChildNode(Node node);

    /**
     * Assembled tree is destroyed then new one is initialized.
     */
    AbstractSyntaxTree reinitialize();

    /**
     * It look into {@code node} object, then the method {@link ASTBuilder#assemble(AbstractSyntaxTree)}
     * is called.
     */
    AbstractSyntaxTree peek(ASTBuilder node);
}
