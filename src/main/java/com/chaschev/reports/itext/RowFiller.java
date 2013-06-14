package com.chaschev.reports.itext;

import com.chaschev.reports.billing.RowData;

import static com.chaschev.reports.itext.AdditionResultType.*;

/**
* User: chaschev
* Date: 6/12/13
*/
public class RowFiller {
    /**
     * Grows as max of rows
     */
    boolean growSameHeight = true;

    float fixedRowHeight = -1;

    /**
     * Allows to break;
     */
    boolean allowBreak = true;

    public RowFiller() {
    }

    public RowFiller(boolean growSameHeight, boolean allowBreak) {
        this.growSameHeight = growSameHeight;
        this.allowBreak = allowBreak;
    }

    private static void rollback(ColumnFall columnFall, ColumnFall[] children) {
        for (ColumnFall child : children) {
            child.rollback();
        }

        columnFall.rollback();
    }

    private AdditionResult tryAddRow(boolean simulateRowAddition, ColumnFall[] children, RowData childrenData, float[] yBefore, boolean afterPageBreak) {
        float maxGrowth = -1;

        Object[] childrenValues = childrenData.cellValues;

        AdditionResultType resultType = OK;

        Object[] dataLeft = null;

        for (int i = 0; i < children.length; i++) {
            ColumnFall child = children[i];

            if(growSameHeight) {
                yBefore[i] = child.getYLine();
            }

            //or-logic: if overflow ever occurred, leave it as an overflow
            AdditionResult r = child.applyAdder(simulateRowAddition, childrenValues[i], allowBreak, afterPageBreak);
            //filler should save data left result
            if(r.type == BREAK_NOW){
                if (dataLeft == null) {
                    dataLeft = new Object[children.length];
                }


                dataLeft[i] = r.thisIterableDataLeft;
            }

            if(r.type == OVERFLOW){
                resultType = OVERFLOW;
            }else
            if(r.type == BREAK_NOW){
                resultType = BREAK_NOW;
            }

            //can we break now? i think, yes...
            if(growSameHeight && (resultType == OK || resultType == BREAK_NOW)) {
                maxGrowth = Math.max(yBefore[i] - child.getYLine(), maxGrowth);
            }
        }

        if(resultType == BREAK_NOW){
            //now we need to capture data in other columns if it's possible
            for (int i = 0; i < children.length; i++) {
//                ColumnFall child = children[i];

                // skip BREAK_NOW columns, they are already filled
                if(dataLeft[i] != null) continue;

                Object o = childrenValues[i];

                if(!(o instanceof Iterable)){
                    dataLeft[i] = o;
                }
            }
        }

        if(fixedRowHeight != -1){
            maxGrowth = Math.max(maxGrowth, fixedRowHeight);
        }

        //a table simultaneous growth
        if(growSameHeight) {
            if(resultType == OK || resultType == BREAK_NOW){
                for (int i = 0; i < children.length; i++) {
                    ColumnFall child = children[i];
                    float height = maxGrowth - (yBefore[i] - child.getYLine());

                    if(height > 1e-2){
                        AdditionResultType r = child.growBy(height);
                        if(resultType == OK) resultType = r;
                    }
                }
            }
        }

        if(resultType == OK) return AdditionResult.OK;

        if(resultType == BREAK_NOW){
            return AdditionResult.newRowDataLeft(new RowData(dataLeft));
        }

        return AdditionResult.OVERFLOW;
    }

    public AdditionResult apply(boolean simulate, CompositeColumnFall columnFall, ColumnFall[] children, RowData childrenData, boolean afterPageBreak) {
//        if(allowBreak ){
//            return applyBreakable(simulate, columnFall, children, childrenData);
//        }

        final float[] yBefore = newYBefore(children);

        backup(columnFall, children);

        AdditionResult result = tryAddRow(simulate, children, childrenData, yBefore, afterPageBreak);

        if ((result != AdditionResult.OK && result.type != BREAK_NOW) || simulate) {
            rollback(columnFall, children);
        }

        return result;
    }

    private float[] newYBefore(ColumnFall... children) {
        final float[] yBefore;

        if(growSameHeight){
            yBefore = new float[children.length];
        }else{
            yBefore = null;
        }
        return yBefore;
    }

    private static void backup(ColumnFall columnFall, ColumnFall[] children) {
        columnFall.backup();

        for (ColumnFall child : children) {
            child.backup();
        }
    }

    public RowFiller setGrowSameHeight(boolean growSameHeight) {
        this.growSameHeight = growSameHeight;
        return this;
    }

    public RowFiller setAllowBreak(boolean allowBreak) {
        this.allowBreak = allowBreak;
        return this;
    }
}
