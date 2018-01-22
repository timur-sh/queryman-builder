/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.Nodes;
import org.queryman.builder.token.Keyword;

import javax.xml.soap.Node;

/**
 * This class provides API to build abstract syntax tree of SQL.
 *
 * @author Timur Shaidullin
 */
public interface AbstractSyntaxTreeSQL {
    /**
     * Node represents a
     */
    AbstractSyntaxTreeSQL addNode(Node node);
}
