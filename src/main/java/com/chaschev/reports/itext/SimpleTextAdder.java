package com.chaschev.reports.itext;

/**
* User: chaschev
* Date: 6/12/13
*/
public class SimpleTextAdder implements CellContentAdder {
    @Override
    public AdditionResult add(boolean simulate, ColumnFall columnFall, RowData data) {
        SingleColumnFall single = (SingleColumnFall) columnFall;
        return single.addTextObjects(simulate, (Object[]) data.cellValues[0]);
    }
}
