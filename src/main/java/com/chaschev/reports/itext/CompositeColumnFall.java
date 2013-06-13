package com.chaschev.reports.itext;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import java.util.List;

import static com.chaschev.reports.itext.AdditionResult.OK;
import static com.chaschev.reports.itext.AdditionResult.OVERFLOW;

/**
 * User: chaschev
 * Date: 6/12/13
 */
public class CompositeColumnFall<DATA> extends ColumnFall<DATA, CompositeColumnFall> {
    private transient float backup_yLine;

    // this is the min of all children
    // for row-mode it's equal to yLine of any of the children
    protected float yLine = Float.MAX_VALUE / 4;

    Filler filler = new Filler();

    ColumnFall[] children;

    int breakCount;

    Function<DATA, List> childrenProjector;

    public CompositeColumnFall() {

    }

    CompositeColumnFall(float llx, float lly, float urx, float ury, float... childrenWidths) {
        super(llx, lly, urx, ury, childrenWidths);
    }

    @Override
    public float getYLine() {
        return yLine;
    }

    @Override
    public AdditionResult growBy(float height) {
        yLine -= height;

        for (ColumnFall child : children) {
            child.growBy(height);
        }

        return yLine < lly ? OVERFLOW : OK;
    }

    @Override
    public AdditionResult applyAdder(boolean simulate, DATA data, boolean allowBreak) {
        List list = childrenProjector.apply(data);

        AdditionResult result = filler.apply(simulate, this, children, list);

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
        breakCount++;

        if (parent != null) {
            parent.handlePageBreak();
            yLine = parent.getYLine();
        } else {
            document.newPage();
            yLine = ury;
        }

        onNewPage();
    }

    @Override
    public void addContent(Iterable<DATA> datas, boolean allowBreak) {
        filler.allowBreak = allowBreak;

        super.addContent(datas, allowBreak);
    }

    protected void onNewPage() {
        //print headers
    }

    public CompositeColumnFall<DATA> addChild(ColumnFall columnFall) {
        Preconditions.checkNotNull(childrenWidths);

        if (children == null) {
            children = new ColumnFall[childrenWidths.length - 1];
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

    CompositeColumnFall<DATA> calcChildrenPositions() {
        for (int i = 0; i < children.length; i++) {
            ColumnFall child = children[i];
            child.setRectangle(childrenWidths[i], lly, childrenWidths[i + 1], ury);
            child.setRectangle(childrenWidths[i], lly, childrenWidths[i + 1], ury);
        }

        return this;
    }

    public void setChildrenProjector(Function<DATA, List> childrenProjector) {
        this.childrenProjector = childrenProjector;
    }
}
