package com.chaschev.reports.itext;

/**
* User: chaschev
* Date: 6/12/13
*/
public interface CellContentAdder<T extends ColumnFall, DATA>{
    AdditionResult add(boolean simulate, T columnFall, RowData<DATA> data);
}
