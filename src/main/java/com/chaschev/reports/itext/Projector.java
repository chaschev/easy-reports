package com.chaschev.reports.itext;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Phrase;

/**
* User: chaschev
* Date: 6/15/13
*/
public abstract class Projector<F>{
    protected static Object[] chunks(Object... o) {
        for (int i = 0; i < o.length; i++) {
            o[i] = chunk(o[i]);
        }

        return o;
    }
    protected static Chunk chunk(Object o) {
        return new Chunk(String.valueOf(o));
    }

    protected static Phrase phrase(Object o) {
        return new Phrase(String.valueOf(o));
    }

    public RowData project(Object obj){
        if (obj instanceof RowData) {
            return (RowData) obj;
        }

        return apply((F) obj);
    }

    protected abstract RowData apply(F obj);
}
