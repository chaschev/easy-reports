package com.chaschev.reports.itext;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.itextpdf.text.Document;

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
public abstract class IterableCompositeColumnFall<DATA, ROW_DATA> extends ColumnFall<DATA, IterableCompositeColumnFall<DATA, ROW_DATA>> {
    protected int breakCount;

    protected ColumnFall<ROW_DATA, ? extends ColumnFall> rowColumnFall;

    //mode 1
//    protected Function<DATA, Iterable> dataToRows;

    //mode 2
    //vertical composite
//    protected Projector<DATA> childrenProjector;

    protected float yLine = Float.MAX_VALUE / 4;

    int backup_rowIndex;
    int rowIndex = -1;

    Object rowValue;
    Object backup_rowValue ;

    IterableCompositeColumnFall(String name, Document document) {
        super(name, document);
    }

    protected AdditionResult addIterableUntilPageBreak(boolean simulate, Iterable<ROW_DATA> datas, boolean allowBreak, boolean afterPageBreak){
        if(allowBreak){
            for (Iterator<ROW_DATA> iterator = datas.iterator(); iterator.hasNext(); ) {
                ROW_DATA data = iterator.next();

                rowValue = data;
                rowIndex++;

                ColumnFall<ROW_DATA, ? extends ColumnFall> currentRowFall = currentRowFall();

                AdditionResult result = currentRowFall.applyAdder(simulate, data, true, afterPageBreak);

                yLine = currentRowFall.getYLine();

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
            for (Object data : datas) {
                backup();

                AdditionResult result = rowColumnFall.applyAdder(true, (ROW_DATA) data, false, afterPageBreak);

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
                    result = currentRowFall().applyAdder(false, (ROW_DATA) data, false, afterPageBreak);
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

    protected ColumnFall<ROW_DATA, ? extends ColumnFall> currentRowFall() {
        return rowColumnFall;
    }

    protected AdditionResult addIterable(boolean simulate, Iterable<ROW_DATA> data, boolean allowBreak, boolean afterPageBreak) {
//        rowFiller.allowBreak = allowBreak;

        AdditionResult result = addIterableUntilPageBreak(simulate, data, allowBreak, afterPageBreak);

        while (parent == null && result.type == BREAK_NOW){
            handlePageBreak();
            result = addIterableUntilPageBreak(simulate, result.thisIterableDataLeft, allowBreak, true);
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
        yLine = y;
        currentRowFall().setYLine(y);
    }

    @Override
    public float getYLine() {
        return yLine;
    }

    @Override
    protected void backupMe() {
        currentRowFall().backup();
        backup_rowIndex = rowIndex;
        backup_rowValue = rowValue;
    }

    @Override
    protected void rollbackMe() {
        currentRowFall().rollback();
        rowIndex = backup_rowIndex;
        rowValue = backup_rowValue;
    }

    public IterableCompositeColumnFall<ROW_DATA, DATA> setRowFall(ColumnFall<ROW_DATA, ? extends ColumnFall> rowColumnFall) {
//        setChildrenProjector(CompositeColumnFall.IDENTITY_PROJECTOR);
        this.rowColumnFall = rowColumnFall;
        this.children = new ColumnFall[]{rowColumnFall};
//        addChild(rowColumnFall);
        return (IterableCompositeColumnFall<ROW_DATA, DATA>) this;
    }
}
