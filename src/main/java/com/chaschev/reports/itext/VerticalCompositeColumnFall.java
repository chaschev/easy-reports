package com.chaschev.reports.itext;

import com.itextpdf.text.Document;

import java.util.Arrays;

/**
 * User: chaschev
 * Date: 6/15/13
 */
public class VerticalCompositeColumnFall<DATA> extends IterableCompositeColumnFall<DATA, Object> {
    protected Projector<DATA> childrenProjector;

    VerticalCompositeColumnFall(String name, Document document) {
        super(name, document);
    }

    @Override
    public AdditionResult applyAdder(boolean simulate, DATA data, boolean allowBreak, boolean afterPageBreak) {
        RowData rowData = childrenProjector.project(data);
        return addIterable(simulate, Arrays.asList(rowData.cellValues), allowBreak, afterPageBreak);
    }

    @Override
    protected ColumnFall<Object, ? extends ColumnFall> currentRowFall() {
        ColumnFall r;
        if(rowIndex == -1) {
            r = children[0];
        }else{
            r = children[rowIndex];
        }

        r.setRectangle(llx, lly, urx, ury);
        r.initPositions();
        r.setYLine(getYLine());

        return r;
    }

    public void setChildrenProjector(Projector<DATA> childrenProjector) {
        this.childrenProjector = childrenProjector;
    }

    public void setChildren(ColumnFall... columnFall) {
        children = columnFall;
    }
}
