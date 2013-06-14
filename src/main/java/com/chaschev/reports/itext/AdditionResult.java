package com.chaschev.reports.itext;

import com.chaschev.reports.billing.RowData;

import java.util.List;

/**
 * Proposed solution:
 *
 * Wrapper operating basically in two modes: multi-column and a single column mode
 * Multi-column is like a single-column, but the page is narrower and continue on the top
 *
 * There is an action on reaching the bottom:
 *
 * a) Continue in ColumnFallX
 * b) Continue on the new Page (obvious example is obvious, weird example: page end in a center cell)
 * c) can be added
 *
 * Two splitting modes
 *
 * a) split-as-a-table (creates 3 columns with 'Continue on the new Page' actions)
 * b) split-as-columns (creates 3 columns with 'Continue in ColumnFallX' actions)
 * c) can be added
 *
 * Add-as-row action (split mode a, columns > 1)
 *
 * a) Add in simulation mode. Add all or none ()
 *
 */

public class AdditionResult{
    public static final AdditionResult OK = new AdditionResult(null, AdditionResultType.OK);
    public static final AdditionResult OVERFLOW = new AdditionResult(null, AdditionResultType.OVERFLOW);

    public RowData rowDataLeft;
    public List thisIterableDataLeft;
    public AdditionResultType type;

    public AdditionResult(AdditionResultType type) {
        this.type = type;
    }

    public static AdditionResult newRowDataLeft(RowData rowDataLeft){
        return new AdditionResult(rowDataLeft, AdditionResultType.BREAK_NOW);
    }

    public static AdditionResult newThisIterableDataLeft(List thisIterableDataLeft){
        AdditionResult result = new AdditionResult(AdditionResultType.BREAK_NOW);

        result.thisIterableDataLeft = thisIterableDataLeft;

        return result;
    }

    private AdditionResult(RowData rowDataLeft, AdditionResultType type) {
        this.rowDataLeft = rowDataLeft;
        this.type = type;
    }

    public boolean isForTable(){
        return thisIterableDataLeft != null;
    }
}
