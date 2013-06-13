package com.chaschev.reports.itext;

/**
* User: chaschev
* Date: 6/12/13
*/
public class SimpleTextAdder implements CellContentAdder {
    @Override
    public AdditionResult add(boolean simulate, ColumnFall columnFall) {
        throw new UnsupportedOperationException("todo SimpleTextAdder.add");
    }

    @Override
    public AdditionResult add(boolean simulate, ColumnFall columnFall, Object o) {
        SingleColumnFall single = (SingleColumnFall) columnFall;
        return single.addTextObjects(simulate, (Object[]) o);
    }
}
