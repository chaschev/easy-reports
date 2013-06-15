package com.chaschev.reports.itext;

import com.google.common.base.Preconditions;
import com.itextpdf.text.Document;

import java.util.Arrays;

import static com.chaschev.reports.itext.AdditionResultType.OK;
import static com.chaschev.reports.itext.AdditionResultType.OVERFLOW;

/**
 * User: chaschev
 * Date: 6/12/13
 */

// (code, patient table) row is this
public class HCompositeColumnFall<DATA, T extends HCompositeColumnFall> extends ColumnFall<DATA, T> {
    public static final Projector IDENTITY_PROJECTOR = new Projector() {
        @Override
        protected RowData apply(Object o) {
            return (RowData) o;
        }
    };
    private transient float backup_yLine;

    // this is the min of all children
    // for row-mode it's equal to yLine of any of the children
    protected float yLine = Float.MAX_VALUE / 4;

    protected float[] childrenRelativeWidths;
    protected transient float[] childrenXPositions;

    protected RowFiller rowFiller = new RowFiller();

    Projector<DATA> childrenProjector;

    public HCompositeColumnFall(String name, Document document) {
        super(name, document);
    }

    @Override
    public float getYLine() {
        return yLine;
    }

    @Override
    public AdditionResultType growBy(float height) {
        yLine -= height;

        for (ColumnFall child : children) {
            child.growBy(height);
        }

        return yLine < lly ? OVERFLOW : OK;
    }

    @Override
    public void setYLine(float y) {
        this.yLine = y;
        for (ColumnFall child : children) {
            child.setYLine(y);
        }
    }

    @Override
    public AdditionResult applyAdder(boolean simulate, DATA data, boolean allowBreak, boolean afterPageBreak) {
        RowData rowData = childrenProjector.project(data);

        AdditionResult result = rowFiller.apply(simulate, this, children, rowData, afterPageBreak);

        for (ColumnFall child : children) {
            yLine = Math.min(yLine, child.getYLine());
        }

        return result;
    }


    @Override
    protected void backupMe() {
        backup_yLine = yLine;
    }

    @Override
    protected void rollbackMe() {
        yLine = backup_yLine;
    }


    //todo hide
    public T initPositions() {
        calcChildrenWidthsFromRelatives();

        for (int i = 0; i < children.length; i++) {
            ColumnFall child = children[i];

            child.setRectangle(childrenXPositions[i], lly, childrenXPositions[i + 1], ury);
            child.setYLine(ury);
            child.initPositions();
        }

        Utils.print("[%s]: calcing positions, rect: [%.1f, %.1f]-[%.1f, %.1f], yLine: %.1f, children: %s%n", name,
            llx, lly, urx, ury, yLine, Arrays.toString(childrenXPositions)
        );


        return (T) this;
    }

    public T setChildrenProjector(Projector<DATA> childrenProjector) {
        this.childrenProjector = childrenProjector;
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

    public HCompositeColumnFall<DATA, T> addChild(ColumnFall columnFall) {
        Preconditions.checkNotNull(childrenRelativeWidths);

        if (children == null) {
            children = new ColumnFall[childrenRelativeWidths.length];
        }

        int childIndex = 0;

        for (; childIndex < children.length; childIndex++) {
            if (children[childIndex] == null) break;
        }

        children[childIndex] = columnFall;

        columnFall.parent = this;
        columnFall.document = document;

        return this;
    }
}
