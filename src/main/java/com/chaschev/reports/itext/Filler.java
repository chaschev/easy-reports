package com.chaschev.reports.itext;

import com.google.common.base.Preconditions;

import java.util.List;

import static com.chaschev.reports.itext.AdditionResult.OK;

/**
* User: chaschev
* Date: 6/12/13
*/
public class Filler{
    /**
     * Grows as max of rows
     */
    boolean growSameHeight = true;

    float fixedRowHeight = -1;

    /**
     * Allows to break;
     */
    boolean allowBreak = true;

    public Filler() {
    }

    public Filler(boolean growSameHeight, boolean allowBreak) {
        this.growSameHeight = growSameHeight;
        this.allowBreak = allowBreak;
    }

    public AdditionResult applyBreakable(boolean simulate, CompositeColumnFall columnFall, ColumnFall[] children, List childrenData) {
        if(simulate){
            throw new IllegalArgumentException("simulation must be off when allowing breaks");
        }

        final float[] yBefore = newYBefore(children);

        AdditionResult result = OK;

        backup(columnFall, children);

        result = tryAddRow(true, children, childrenData, yBefore, result);

        rollback(columnFall, children);

        if (result != OK) {
            //this should start a new page
            columnFall.handlePageBreak();
            columnFall.calcChildrenPositions();
            result = applyBreakable(simulate, columnFall, children, childrenData);
        }else{
            result = tryAddRow(false, children, childrenData, yBefore, result);
        }

        Preconditions.checkArgument(result == OK, "breakable addition must be ok!");

        return result;
    }

    private static void rollback(ColumnFall columnFall, ColumnFall[] children) {
        for (ColumnFall child : children) {
            child.rollback();
        }

        columnFall.rollback();
    }

    private AdditionResult tryAddRow(boolean simulateRowAddition, ColumnFall[] children, List childrenData, float[] yBefore, AdditionResult result) {
        float maxGrowth = -1;

        for (int i = 0; i < children.length; i++) {
            ColumnFall child = children[i];

            if(growSameHeight) yBefore[i] = child.getYLine();

            //or-logic: if overflow ever occurred, leave it as an overflow
            result = result == OK ? child.applyAdder(simulateRowAddition, childrenData.get(i), allowBreak) : result;

            if(growSameHeight && result == OK) {
                maxGrowth = Math.max(yBefore[i] - child.getYLine(), maxGrowth);
            }
        }

        if(fixedRowHeight != -1){
            maxGrowth = Math.max(maxGrowth, fixedRowHeight);
        }

        //a table simultaneous growth
        if(growSameHeight) {
            if(result == OK){
                for (int i = 0; i < children.length; i++) {
                    ColumnFall child = children[i];
                    float height = maxGrowth - (yBefore[i] - child.getYLine());

                    if(height > 1e-2){
                        result = child.growBy(height);
                    }
                }
            }
        }

        return result;
    }

    public AdditionResult apply(boolean simulate, CompositeColumnFall columnFall, ColumnFall[] children, List childrenData) {
        if(allowBreak ){
            return applyBreakable(simulate, columnFall, children, childrenData);
        }

        final float[] yBefore = newYBefore(children);

        AdditionResult result = OK;

        backup(columnFall, children);

        result = tryAddRow(simulate, children, childrenData, yBefore, result);

        if (result != OK || simulate) {
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

    public Filler setGrowSameHeight(boolean growSameHeight) {
        this.growSameHeight = growSameHeight;
        return this;
    }

    public Filler setAllowBreak(boolean allowBreak) {
        this.allowBreak = allowBreak;
        return this;
    }
}
