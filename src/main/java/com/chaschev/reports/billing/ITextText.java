package com.chaschev.reports.billing;

import com.itextpdf.text.Phrase;

/**
 * User: chaschev
 * Date: 6/11/13
 */
public abstract class ITextText {
    int x;
    int y;

    int paddingLeft;
    int paddingRight;
    int paddingBottom;
    int paddingTop;

    int leadingOffset;

    ITextTextStyle style;

    public float width;
    public float height;


    public abstract Phrase getPhrase();
}
