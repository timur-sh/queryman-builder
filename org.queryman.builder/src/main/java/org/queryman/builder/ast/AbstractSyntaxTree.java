/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Token;

import java.util.List;

/**
 * This class provides API to manipulate abstract syntax tree of SQL tokens. Each node must
 * start with {@link #startNode(NodeMetadata)} and end with {@link #endNode()}.
 * Each node may have or may not have {@code leaves}.
 * <p>
 * Example:
 *
 * AbstractSyntaxTree tree = new AbstractSyntaxTree();
 * tree.startNode(SELECT, ", ");
 *    .addLeaf("id");
 *    .addLeaf("name");
 *    .addLeaf("phone");
 * tree.addLeaves("date", "email");
 *
 * tree.startNode(FROM);
 * tree.addLeaf("table1").addLeaf("table2").setDelimiter(" ");
 * tree.endNode();
 *
 * tree.startNode(WHERE, "")
 *    .addLeaf("id")
 *    .addLeaf("=")
 *    .addLeaf("id")
 *    .endNode();
 *
 * tree.endNode();
 * </p>
 *
 * @author Timur Shaidullin
 */
public interface AbstractSyntaxTree {
    /**
     * Each node must have a {@code metadata} object.
     */
    AbstractSyntaxTree startNode(NodeMetadata metadata);

    /**
     * Each node must have a {@code metadata}. And override a default delimiter
     * by provides a new one.
     *
     * @see #setDelimiter(String).
     * @see #startNode(NodeMetadata).
     */
    AbstractSyntaxTree startNode(NodeMetadata metadata, String delimiter);

    /**
     * Each leaves must separated by {@code delimiter} token. If it is not
     * specified, space {@code " "} is used as default delimiter.
     */
    AbstractSyntaxTree setDelimiter(String delimiter);

    /**
     * Each node must be closed.
     */
    AbstractSyntaxTree endNode();

    /**
     * Add {@code leaf} into current node. Kinds of leaf are SQL tokens.
     *
     * @see #addLeaves(Token...)
     * @see Token
     */
    AbstractSyntaxTree addLeaf(Token token);

    /**
     * Add {@code leaves} into current node. Kinds of leaves are SQL tokens.
     *
     * @see #addLeaf(Token)
     * @see #addLeaves(List) (Token)
     * @see Token
     */
    AbstractSyntaxTree addLeaves(Token... tokens);

    /**
     * Add {@code leaves} into current node. Kinds of leaves are SQL tokens.
     *
     * @see #addLeaf(Token)
     * @see #addLeaves(Token...)
     * @see Token
     */
    AbstractSyntaxTree addLeaves(List<Token> tokens);

    /**
     * Insert child node into the parent node.
     */
    AbstractSyntaxTree addChildNode(Node node);

    /**
     * Destroy an assembled tree then new one is initialized.
     */
    AbstractSyntaxTree reinitialize();

    /**
     * It look into {@code node} object, then the method {@link AstVisitor#assemble(AbstractSyntaxTree)}
     * is called.
     */
    AbstractSyntaxTree peek(AstVisitor node);
}
