package com.chaschev.reports.itext;

import com.chaschev.reports.billing.RowData;
import com.chaschev.reports.billing.Utils;
import com.google.common.base.Preconditions;

import java.util.Arrays;

import static com.chaschev.reports.itext.AdditionResultType.OK;
import static com.chaschev.reports.itext.AdditionResultType.OVERFLOW;

/**
 * User: chaschev
 * Date: 6/12/13
 */

// (code, patient table) row is this
public class CompositeColumnFall<DATA, T extends CompositeColumnFall> extends ColumnFall<DATA, T> {
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

    ColumnFall[] children;

    protected RowFiller rowFiller = new RowFiller();

    public static abstract class Projector<F>{
        public RowData project(Object obj){
            if (obj instanceof RowData) {
                return (RowData) obj;
            }

            return apply((F) obj);
        }

        protected abstract RowData apply(F obj);
    }

    Projector<DATA> childrenProjector;

    public CompositeColumnFall(String name) {
        super(name);
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


    @Override
    public void handlePageBreak() {

        if (parent != null) {
            parent.handlePageBreak();
            yLine = parent.getYLine();
        } else {
            document.newPage();
            yLine = ury;
        }

        calcChildrenPositions();

        onNewPage();
    }


    protected void onNewPage() {
        //print headers
    }

    public CompositeColumnFall<DATA, T> addChild(ColumnFall columnFall) {
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

    CompositeColumnFall<DATA, T> calcChildrenPositions() {
        calcChildrenWidthsFromRelatives();

        for (int i = 0; i < children.length; i++) {
            ColumnFall child = children[i];

            child.setRectangle(childrenXPositions[i], lly, childrenXPositions[i + 1], ury);
            child.setYLine(ury);

            if (child instanceof CompositeColumnFall) {
                ((CompositeColumnFall) child).calcChildrenPositions();
            }
        }

        Utils.print("[%s]: calcing positions, rect: [%.1f, %.1f]-[%.1f, %.1f], yLine: %.1f, children: %s%n", name,
            llx, lly, urx, ury, yLine, Arrays.toString(childrenXPositions)
        );


        return this;
    }

    public T setChildrenProjector(Projector<DATA> childrenProjector) {
        this.childrenProjector = childrenProjector;
        return (T) this;
    }
}
