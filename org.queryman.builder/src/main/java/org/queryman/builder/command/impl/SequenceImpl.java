/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.Keywords;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.command.create.sequence.SequenceCacheStep;
import org.queryman.builder.command.create.sequence.SequenceCycleStep;
import org.queryman.builder.command.create.sequence.SequenceFinalStep;
import org.queryman.builder.command.create.sequence.SequenceFirstStep;
import org.queryman.builder.command.create.sequence.SequenceIncrementStep;
import org.queryman.builder.command.create.sequence.SequenceMaxValueStep;
import org.queryman.builder.command.create.sequence.SequenceMinValueStep;
import org.queryman.builder.command.create.sequence.SequenceOwnerStep;
import org.queryman.builder.command.create.sequence.SequenceStartStep;
import org.queryman.builder.token.Expression;

import static org.queryman.builder.Keywords.CACHE;
import static org.queryman.builder.Keywords.CREATE_SEQUENCE;
import static org.queryman.builder.Keywords.CREATE_SEQUENCE_IF_NOT_EXISTS;
import static org.queryman.builder.Keywords.CREATE_TEMP_SEQUENCE;
import static org.queryman.builder.Keywords.CREATE_TEMP_SEQUENCE_IF_NOT_EXISTS;
import static org.queryman.builder.Keywords.CYCLE;
import static org.queryman.builder.Keywords.INCREMENT;
import static org.queryman.builder.Keywords.INCREMENT_BY;
import static org.queryman.builder.Keywords.MAXVALUE;
import static org.queryman.builder.Keywords.MINVALUE;
import static org.queryman.builder.Keywords.NO_CYCLE;
import static org.queryman.builder.Keywords.OWNED_BY;
import static org.queryman.builder.Keywords.START;
import static org.queryman.builder.Keywords.START_WITH;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asNumber;
import static org.queryman.builder.PostgreSQL.nodeMetadata;

/**
 * Implementation of SEQUENCE statement.
 *
 * @author Timur Shaidullin
 */
public class SequenceImpl extends AbstractQuery implements
   SequenceFirstStep,
   SequenceIncrementStep,
   SequenceMinValueStep,
   SequenceMaxValueStep,
   SequenceStartStep,
   SequenceCacheStep,
   SequenceCycleStep,
   SequenceOwnerStep,
   SequenceFinalStep {

    private final Expression name;
    private final boolean    temp;
    private final boolean    notExists;
    private       String     dataType;

    private Expression increment;
    private boolean    incrementBy;

    private Expression minvalue;
    private Expression maxvalue;
    private Expression cache;
    private Expression    cycle;
    private Expression ownedBy;

    private Expression start;
    private boolean    startWith;

    public SequenceImpl(AbstractSyntaxTree tree, Expression name) {
        this(tree, name, false);
    }

    public SequenceImpl(AbstractSyntaxTree tree, Expression name, boolean temp) {
        this(tree, name, temp, false);
    }

    public SequenceImpl(AbstractSyntaxTree tree, Expression name, boolean temp, boolean notExists) {
        super(tree);
        this.name = name;
        this.temp = temp;
        this.notExists = notExists;
    }

    @Override
    public final SequenceImpl as(String dataType) {
        this.dataType = dataType;
        return this;
    }

    @Override
    public final SequenceImpl increment(long increment) {
        this.increment = asNumber(increment);
        return this;
    }

    @Override
    public final SequenceImpl incrementBy(long increment) {
        increment(increment);
        this.incrementBy = true;
        return this;
    }

    @Override
    public final SequenceImpl minvalue(long minvalue) {
        this.minvalue = asNumber(minvalue);
        return this;
    }

    @Override
    public final SequenceImpl noMinvalue() {
        minvalue = asConstant("NO MINVALUE");
        return this;
    }

    @Override
    public final SequenceImpl maxvalue(long maxvalue) {
        this.maxvalue = asNumber(maxvalue);
        return this;
    }

    @Override
    public final SequenceImpl noMaxvalue() {
        maxvalue = asConstant("NO MAXVALUE");
        return this;
    }

    @Override
    public final SequenceImpl start(long start) {
        this.start = asNumber(start);
        return this;
    }

    @Override
    public final SequenceImpl startWith(long start) {
        start(start);
        this.startWith = true;
        return this;
    }

    @Override
    public final SequenceImpl cache(long cache) {
        this.cache = asNumber(cache);
        return this;
    }

    @Override
    public final SequenceImpl cycle() {
        cycle = asConstant(true);
        return this;
    }

    @Override
    public final SequenceImpl noCycle() {
        cycle = asConstant(false);
        return this;
    }

    @Override
    public final SequenceImpl ownedBy(String name) {
        return ownedBy(asName(name));
    }

    @Override
    public final SequenceImpl ownedBy(Expression name) {
        ownedBy = name;
        return this;
    }

    @Override
    public final SequenceImpl ownedByNone() {
        return ownedBy(asName("NONE"));
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        if (temp && notExists)
            tree.startNode(nodeMetadata(CREATE_TEMP_SEQUENCE_IF_NOT_EXISTS));
        else if (temp)
            tree.startNode(nodeMetadata(CREATE_TEMP_SEQUENCE));
        else if (notExists)
            tree.startNode(nodeMetadata(CREATE_SEQUENCE_IF_NOT_EXISTS));
        else
            tree.startNode(nodeMetadata(CREATE_SEQUENCE));

        tree.addLeaf(name);

        if (dataType != null)
            tree.startNode(nodeMetadata(Keywords.AS))
               .addLeaf(asName(dataType))
               .endNode();

        if (increment != null)
            tree.startNode(nodeMetadata(incrementBy ? INCREMENT_BY :INCREMENT))
               .addLeaf(increment)
               .endNode();

        if (minvalue != null)
            tree.startNode(nodeMetadata(MINVALUE))
               .addLeaf(minvalue)
               .endNode();

        if (maxvalue != null)
            tree.startNode(nodeMetadata(MAXVALUE))
               .addLeaf(maxvalue)
               .endNode();

        if (start != null)
            tree.startNode(nodeMetadata(startWith ? START_WITH : START))
               .addLeaf(start)
               .endNode();

        if (cache != null)
            tree.startNode(nodeMetadata(CACHE))
               .addLeaf(cache)
               .endNode();

        if (cycle != null)
            tree.startNode(nodeMetadata(Boolean.valueOf(cycle.toString()) ? CYCLE : NO_CYCLE))
               .endNode();

        if (ownedBy != null)
            tree.startNode(nodeMetadata(OWNED_BY))
               .addLeaf(ownedBy)
               .endNode();

        tree.endNode();
    }
}
