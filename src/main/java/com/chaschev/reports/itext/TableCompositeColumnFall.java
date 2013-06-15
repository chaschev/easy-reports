package com.chaschev.reports.itext;

import com.itextpdf.text.Document;

/**
 * User: chaschev
 * Date: 6/15/13
 */
public class TableCompositeColumnFall<DATA> extends IterableCompositeColumnFall<Iterable<DATA>, DATA> {
    public TableCompositeColumnFall(String name, Document document) {
        super(name, document);
    }

    @Override
    public AdditionResult applyAdder(boolean simulate, Iterable<DATA> datas, boolean allowBreak, boolean afterPageBreak) {
        return super.addIterable(simulate, datas, allowBreak, afterPageBreak);
    }
}
