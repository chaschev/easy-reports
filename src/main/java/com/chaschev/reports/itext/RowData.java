package com.chaschev.reports.itext;

/**
 * User: chaschev
 * Date: 6/15/13
 */

import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * This is projected data.
 */
public class RowData<DATA> {
    public Object[] cellValues;

    public RowData(Object... cellValues) {
        Preconditions.checkNotNull(cellValues);
        this.cellValues = cellValues;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RowData{");
        sb.append("cellValues=").append(Arrays.deepToString(cellValues));
        sb.append('}');
        return sb.toString();
    }
}
