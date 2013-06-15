package com.chaschev.reports.itext;

/**
 * User: chaschev
 * Date: 6/12/13
 */

/**
 * Problems: inlining an image
 * Several columns
 * try-adding (try-and-real-factory or object stack or)
 * Adding as row (all added or none)
 * Adding on a next [page, column]
 * Page X of Y
 * Footer, Header
 * Handling next-page event
 *
 * adding as a row (add to several)
 * | |  | |
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.ColumnText;

class SavePoint{
    float llx;
    float lly;
    float urx;
    float ury;

    ColumnText columnText;

    SavePoint(float llx, float lly, float urx, float ury, ColumnText columnText) {
        reuse(llx, lly, urx, ury, columnText);
    }

    public SavePoint reuse(float llx, float lly, float urx, float ury, ColumnText columnText) {
        this.llx = llx;
        this.lly = lly;
        this.urx = urx;
        this.ury = ury;
        this.columnText = columnText;
        return this;
    }
}

public abstract class ColumnFall<DATA, T extends ColumnFall> {
    private static final float NOT_SET_VALUE = -999999919;

    //todo hide
    public transient Document document;

    private transient float backup_llx;
    private transient float backup_lly;
    private transient float backup_urx;
    private transient float backup_ury;

    protected String name;

    protected ColumnFall(String name) {
        this.name = name;
    }

    float llx = NOT_SET_VALUE;
    float lly;
    float urx;
    float ury;

    protected float[] childrenRelativeWidths;
    protected transient float[] childrenXPositions;

    protected transient ColumnFall parent;


    protected transient CellContentAdder<T, DATA> adder = new SimpleTextAdder();

    //TODO move width to composite



    public abstract float getYLine();

    public abstract AdditionResultType growBy(float height) ;

    public abstract void setYLine(float y) ;

    public abstract AdditionResult applyAdder(boolean simulate, DATA data, boolean allowBreak, boolean afterPageBreak);


    abstract protected void backupMe();
    abstract protected void rollbackMe();

    SingleColumnFall backup(){
        this.backup_llx = llx;
        this.backup_lly = lly;
        this.backup_urx = urx;
        this.backup_ury = ury;

        backupMe();

        return null;
    }

    void rollback() {
        llx = backup_llx;
        lly = backup_lly;
        urx = backup_urx;
        ury = backup_ury;

        rollbackMe();
    }

    public abstract void handlePageBreak();

    public T setRectangle(float llx, float lly, float urx, float ury){
        this.llx = llx;
        this.lly = lly;
        this.urx = urx;
        this.ury = ury;

        return (T) this;
    }

    public T setRelativeWidths(float... values){
        this.childrenRelativeWidths = values;
        return (T) this;
//        return calcChildrenWidthsFromRelatives();
    }

    protected T calcChildrenWidthsFromRelatives() {
        double sum = 0;

        for (float value : childrenRelativeWidths) {
            sum += value;
        }

        double totalWidth = urx - llx;

        childrenXPositions = new float[childrenRelativeWidths.length + 1];

        childrenXPositions[0] = llx;

        for (int i = 0; i < childrenRelativeWidths.length; i++) {
            childrenXPositions[i+1] = childrenXPositions[i] + (float) (totalWidth * childrenRelativeWidths[i] / sum);
        }

        return (T) this;
    }

    public boolean isRectangleSet(){
        return llx != NOT_SET_VALUE;
    }
}
