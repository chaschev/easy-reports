package com.chaschev.itext;

/**
* User: chaschev
* Date: 9/10/13
*/
public class AtomicIncreaseResult {
    public float height;
    public ColumnTextBuilder.GrowthResultType type;

    public AtomicIncreaseResult(float height, ColumnTextBuilder.GrowthResultType type) {
        this.height = height;
        this.type = type;
    }
}
