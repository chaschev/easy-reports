package com.chaschev.reports.itext;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;

import static com.chaschev.reports.itext.AdditionResultType.*;

/**
 * User: chaschev
 * Date: 6/14/13
 */

/**
 * This is a minimalistic decorator around iterator. It's purpose is to
 *
 * 1) implement applyAdder
 * 2) add rows
 *
 * Does it need rollbacks?
 *
 * @param <DATA>
 *
 * Example is:
 *
 * patient table,
 * table of (code, patient table)
 */
public class IterableCompositeColumnFall<DATA> extends ColumnFall<Iterable<DATA>, IterableCompositeColumnFall<DATA>> {
    protected int breakCount;

    protected ColumnFall<DATA, ? extends ColumnFall> rowColumnFall;

    //mode 1
//    protected Function<DATA, Iterable> dataToRows;

    //mode 2
    //vertical composite
//    protected Projector<DATA> childrenProjector;

    protected float yLine = Float.MAX_VALUE / 4;

    public IterableCompositeColumnFall(String name) {
        super(name);

//        this.childrenProjector = CompositeColumnFall.IDENTITY_PROJECTOR;
    }

    @Override
    public AdditionResult applyAdder(boolean simulate, Iterable<DATA> datas, boolean allowBreak, boolean afterPageBreak) {
        return addIterable(simulate, datas, allowBreak);
    }

    protected AdditionResult addIterable(boolean simulate, Iterable<DATA> datas, boolean allowBreak, boolean afterPageBreak){
        if(allowBreak){
            for (Iterator<DATA> iterator = datas.iterator(); iterator.hasNext(); ) {
                DATA data = iterator.next();

                AdditionResult result = rowColumnFall.applyAdder(simulate, data, true, afterPageBreak);

                //can't be overflow here, ok?
                if (result.type == BREAK_NOW || result.type == OVERFLOW) {
                    //add what's left as a new row
                    ArrayList thisData;
                    if(result.type == OVERFLOW){
                        //completely doesn't fit
                        thisData = Lists.newArrayList(data);
                    }else{
                        //save what's left
                        thisData = Lists.newArrayList(result.rowDataLeft);
                    }

                    Iterators.addAll(thisData, iterator);

                    Utils.print("[%s]: break during table row iteration, afterPageBreak=%s, dataLeft: %d items, %s%n", name, afterPageBreak, thisData.size(), thisData);

                    return AdditionResult.newThisIterableDataLeft(thisData);
                }
            }

            return AdditionResult.OK;
        }else{
            for (DATA data : datas) {
                backup();

                AdditionResult result = rowColumnFall.applyAdder(true, data, false, afterPageBreak);

                if(result == AdditionResult.OVERFLOW){
                    //todo this is wrong. Any break must be escalated.
                    if(true) {
                        throw new UnsupportedOperationException("redo this");
                    }
                    parent.handlePageBreak();
                    handlePageBreak();
                }

                rollback();

                if(!simulate){
                    result = rowColumnFall.applyAdder(false, data, false, afterPageBreak);
                }

                if(result.type == BREAK_NOW){
                    throw new IllegalStateException("how?");
                }

                if(result == AdditionResult.OVERFLOW){
                    throw new IllegalStateException("data cannot be fit into page: " + data);
                }
            }

            return AdditionResult.OK;
        }

    }

    private AdditionResult addIterable(boolean simulate, Iterable<DATA> data, boolean allowBreak) {
//        rowFiller.allowBreak = allowBreak;

        AdditionResult result = addIterable(simulate, data, allowBreak, false);

        while (parent == null && result.type == BREAK_NOW){
            handlePageBreak();
            result = addIterable(simulate, result.thisIterableDataLeft, allowBreak, true);
        }

        return result;
    }

    @Override
    public AdditionResultType growBy(float height) {
        yLine -= height;

        return yLine < lly ? OVERFLOW : OK;
    }

    @Override
    public void setYLine(float y) {
        rowColumnFall.setYLine(y);
    }

    @Override
    public float getYLine() {
        return rowColumnFall.getYLine();
    }

    @Override
    protected void backupMe() {
        rowColumnFall.backup();
    }

    @Override
    protected void rollbackMe() {
        rowColumnFall.rollback();
    }

    public IterableCompositeColumnFall<DATA> setRowFall(ColumnFall<DATA, ? extends ColumnFall> rowColumnFall) {
//        setChildrenProjector(CompositeColumnFall.IDENTITY_PROJECTOR);
        this.rowColumnFall = rowColumnFall;
        this.children = new ColumnFall[]{rowColumnFall};
//        addChild(rowColumnFall);
        return this;
    }
}
