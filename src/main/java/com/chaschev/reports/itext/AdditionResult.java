package com.chaschev.reports.itext;

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

public enum AdditionResult{
    OK, OVERFLOW
}
