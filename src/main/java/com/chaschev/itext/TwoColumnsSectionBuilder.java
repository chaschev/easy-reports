/*
 * Copyright (C) 2013 Afoundria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chaschev.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: ACHASCHEV
 * Date: 7/12/13
 * Time: 1:29 AM
 */
public class TwoColumnsSectionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(TwoColumnsSectionBuilder.class);

    private static final int MAX_PAGE_COUNT = 5;
    Rectangle rectangle;

    ITextBuilder b;

    ContentAdder contentAdder;

    float hPadding = 5;
    private Rectangle tempR;
    private float yLine;
    private float additionalSpaceForColumn = 10f;

    public TwoColumnsSectionBuilder(Rectangle rectangle, ITextBuilder b, ContentAdder contentAdder) {
        this.rectangle = rectangle;
        this.b = b;
        this.contentAdder = contentAdder;

        tempR = new Rectangle(rectangle);
    }

    public TwoColumnsSectionBuilder newPage(float pageTop, float pageBottom) {
        rectangle.setTop(pageTop);
        rectangle.setBottom(pageBottom);
        return this;
    }

    public TwoColumnsSectionBuilder addToDocument(float pageTop, float pageBottom) {
        Document document = b.getDocument();
        int pageCount = 0;

        for(;!tryAdding() && pageCount < MAX_PAGE_COUNT;pageCount++){
            document.newPage();
            newPage(pageTop, pageBottom)
                .tryAdding();
        }

        if(pageCount == MAX_PAGE_COUNT){
            throw new IllegalStateException("two columns took more than " + MAX_PAGE_COUNT + " pages");
        }

        return this;
    }

    public static interface ContentAdder{
        int add(ColumnTextBuilder ctb, boolean simulate);
    }

    public boolean fits(){
        final boolean simulate = true;

        //try adding into a single infinite column to calc height
        ColumnTextBuilder ctb = b
            .reuseColumnTextBuilder();

        b.reuseRectangleBuilder(tempR).copyPositionsFrom(rectangle);

        tempR.setBottom(-10000f);
        tempR.setRight(horCenter());

        applyPadding(tempR, true);

        ctb.setSimpleColumn(tempR);

        float yBefore = ctb.getYLine();

        contentAdder.add(ctb, simulate);

        int status = ctb.go(simulate);

        if(ColumnText.hasMoreText(status)){
            return false;
        }

        float yAfter = ctb.getYLine();

        final float height = yBefore - yAfter;

        logger.trace("height: {}", height);

//        if(height > rectangle.getHeight()) {
//            return false;
//        }

        //now try adding into two actual columns
        final float columnHeight = height / 2 + additionalSpaceForColumn;

        if(!addToTwoColumns(simulate, columnHeight)){
            return false;
        }

        addToTwoColumns(false, columnHeight);

        return true;
    }

    public boolean tryAdding(){
        return fits();
    }

    private boolean addToTwoColumns(boolean simulate, float height) {
        ColumnTextBuilder ctb;
        int status;
        ctb = b.reuseColumnTextBuilder();

        b.reuseRectangleBuilder(tempR).copyPositionsFrom(rectangle);

        final float horCenter = horCenter();

        setColumn(height, horCenter, true, ctb, simulate);

        contentAdder.add(ctb, simulate);

        status = ctb.go(simulate);

        yLine = ctb.getYLine();

        if(!ColumnText.hasMoreText(status) && simulate){
            return true;
        }

        setColumn(height, horCenter, false, ctb, simulate);

        status = ctb.go(simulate);

        yLine = Math.min(yLine, ctb.getYLine());

        //right column may not fit the rect, check if does fit the page
        if(simulate){
            if(yLine < rectangle.getBottom()){
                return false;
            }
        }

        return !ColumnText.hasMoreText(status);
    }

    private void setColumn(float height, float horCenter, boolean left, ColumnTextBuilder ctb, boolean simulate) {
        b.reuseRectangleBuilder(tempR).copyPositionsFrom(rectangle);

        tempR.setBottom(tempR.getTop() - height);

        if (left) {
            tempR.setRight(horCenter);
        } else {
            tempR.setLeft(horCenter);
            tempR.setBottom(-10000f);
        }

        applyPadding(tempR, left);

        logger.debug("setting column to: {}", tempR);

        ctb.setSimpleColumn(tempR);
    }

    private float horCenter() {
        return (rectangle.getLeft() + rectangle.getRight()) / 2;
    }

    private void applyPadding(Rectangle r, boolean isLeft) {
        if (isLeft) {
            r.setRight(r.getRight() - hPadding);
        } else {
            r.setLeft(r.getLeft() + hPadding);
        }
    }

    public TwoColumnsSectionBuilder setHPadding(float hPadding) {
        this.hPadding = hPadding;
        return this;
    }

    public float getYLine() {
        return yLine;
    }

    public TwoColumnsSectionBuilder setAdditionalSpaceForColumn(float additionalSpaceForColumn) {
        this.additionalSpaceForColumn = additionalSpaceForColumn;
        return this;
    }
}
