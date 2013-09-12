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

package com.chaschev.itext.columns;

import com.chaschev.itext.*;
import com.google.common.base.Preconditions;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

/**
 * todo: test page breaks
 * todo: table support
 * todo: list support
 * todo: test spaces
 */

/**
 * User: chaschev
 * Date: 9/8/13
 */
public class BalancedColumnsBuilder {
    public static final Logger logger = LoggerFactory.getLogger(BalancedColumnsBuilder.class);

    private static final double MINIMAL_HEIGHT_COEFFICIENT = 0.70;

    private int startAtElement = 0;
    private ColumnTextBuilder initialLeftCTB;

    private ElementSequence sequence = new ElementSequence();

    RectangleBuilder origRectangle;

    ITextBuilder b;

    private static final int MAX_PAGE_COUNT = 20;

    float hPadding = 5;

    private RectangleBuilder singleColumnRect = new RectangleBuilder().reuse(new Rectangle(0,0,0,0));
    private float additionalSpaceForColumn = 10f;

    private transient double minimalLeftColumnHeight;
    private double referenceHeight;
    private final SplitResult currentLeftResult = new SplitResult();
    private final SplitResult currentRightResult = new SplitResult();
    private ColumnTextBuilder leftCTB;
    private ColumnTextBuilder rightCTB;
    final SplitResult bestResult = new SplitResult().worstResult();


    public BalancedColumnsBuilder(Rectangle rectangle, ITextBuilder b) {
        this.origRectangle = new RectangleBuilder().reuse(rectangle);
        this.b = b;
    }

    public BalancedColumnsBuilder(
        ColumnTextBuilder initialLeftCTB,
        int startAtElement,
        Rectangle rectangle,
        ITextBuilder b) {
        this.initialLeftCTB = initialLeftCTB.freeze();
        this.startAtElement = startAtElement;

        this.origRectangle = new RectangleBuilder().reuse(rectangle);
        this.b = b;
    }

    public BalancedColumnsBuilder add(Element... element) {
        for (Element el : element) {
            sequence.add(el);
        }
        return this;
    }

    public double heightPenalty(double columnHeight){
        double v = columnHeight - referenceHeight;

        if(v < 0) return v = 0;

        return v * 1.0;
    }

    public void go() {
        _go();
    }

    /**
     *
     */
    private class DirectContentAdder {
        ColumnTextBuilder startWith;
        @Nonnull final ColumnTextBuilder dest;
        int startAtIndex;
        boolean simulate = true;
        double quickHeight;
        @Nullable
        private ColumnTextBuilder rightCTB;
        private boolean setHeights;
        private float startContentHeight = -10000;

        private DirectContentAdder(@Nonnull ColumnTextBuilder dest) {
            this.dest = dest;
        }


        public DirectContentAdder setStartWith(ColumnTextBuilder startWith) {
            this.startWith = startWith;
            return this;
        }

        public DirectContentAdder setStartAtIndex(int startAtIndex) {
            this.startAtIndex = startAtIndex;
            return this;
        }

        public DirectContentAdder setSwitchToRightCTB(ColumnTextBuilder rightCTB) {
            this.rightCTB = rightCTB;
            return this;
        }

        public DirectContentAdder setSimulate(boolean simulate) {
            this.simulate = simulate;
            return this;
        }

        public DirectContentAdder setQuickHeight(double quickHeight) {
            this.quickHeight = quickHeight;
            return this;
        }

        public DirectContentAdder setHeights(boolean setHeights) {
            this.setHeights = setHeights;
            return this;
        }

        public boolean isHeights() {
            return setHeights;
        }

        public class Result{
            int status;
            int index;
            ColumnTextBuilder contentLeft;

            public Result(int status, int index, ColumnTextBuilder contentLeft) {
                this.status = status;
                this.index = index;
                this.contentLeft = contentLeft;
            }

            public boolean hasContentLeft(int totalElementCount){
                return !(contentLeft == null && totalElementCount == index);
            }
        }

        public Result go() {
            if(quickHeight != 0){
                if(rightCTB != null){
                    throw new IllegalStateException("if quickHeight !=0 then rightCTB == null!");
                }
            }

            Preconditions.checkNotNull(dest);

            ColumnTextBuilder currentCtb = dest;
            List<Element> elements = sequence.getElements();

            int i;

            if(quickHeight != 0){
                currentCtb.adjustBottom((float) quickHeight);
            }

            if(startWith != null){
                currentCtb.copyContentFrom(startWith);

                float yBefore = currentCtb.getYLine();

                int status = currentCtb.go(simulate);

                if(setHeights){
                    startContentHeight = yBefore - currentCtb.getYLine();
                }

                if (ColumnText.hasMoreText(status)){
                    if (rightCTB != null) { // => quickHeight == 0
                        rightCTB.copyContentFrom(currentCtb);

                        currentCtb = rightCTB;

                        yBefore = currentCtb.getYLine();

                        status = currentCtb.go(simulate);

                        if(setHeights){
                            startContentHeight += yBefore - currentCtb.getYLine();
                        }

                        if (ColumnText.hasMoreText(status)) {
                            return new Result(ColumnText.NO_MORE_COLUMN, startAtIndex, currentCtb);
                        }
                    } else{
                        return new Result(ColumnText.NO_MORE_COLUMN, startAtIndex, currentCtb);
                    }
                }
            }

            //optimisation mode
            if(quickHeight !=0){
                for (i = startAtIndex; i < elements.size(); i++) {
                    Element el = elements.get(i);

                    if(currentCtb.fits(el)){
                        if (el instanceof SpaceElement) {
                            SpaceElement spaceElement = (SpaceElement) el;
                            spaceElement.add(currentCtb, simulate);
                            currentCtb.go(simulate);
                        }else{
                            currentCtb
                                .addElement(el)
                                .go(simulate);
                        }
                    }else{
                        break;
                    }
                }

                return new Result(ColumnText.NO_MORE_TEXT, i, null);
            }

            for (i = startAtElement; i < elements.size(); i++) {
                Element el = elements.get(i);

                float yBefore = currentCtb.getYLine();

                if (el instanceof SpaceElement) {
                    SpaceElement space = (SpaceElement) el;

                    if(space.fits(currentCtb, currentCtb.getBottom())){
                        space.add(currentCtb, simulate);
                    }else{
                        if(currentCtb == dest){
                            currentCtb = rightCTB;
                        }else if(currentCtb == rightCTB){
                            return new Result(ColumnText.NO_MORE_COLUMN, i + 1, currentCtb.clearContent());
                        }
                    }
                } else {
                    currentCtb.addElement(el);

                    if (ColumnText.hasMoreText(currentCtb.go(simulate))) {
                        if(currentCtb == dest){
                            if (rightCTB == null) {
                                return new Result(ColumnText.NO_MORE_COLUMN, i + 1, currentCtb);
                            } else {
                                currentCtb = rightCTB;
                                rightCTB.copyContentFrom(leftCTB);

                                final int status = currentCtb.go(simulate);
                                if (ColumnText.hasMoreText(status)) {
                                    return new Result(ColumnText.NO_MORE_COLUMN, i + 1, currentCtb);
                                }
                            }
                        }else{
                            return new Result(ColumnText.NO_MORE_COLUMN, i + 1, currentCtb);
                        }
                    }
                }

                if(setHeights){
                    sequence.setHeight(i, yBefore - currentCtb.getYLine());
                }
            }

            return new Result(ColumnText.NO_MORE_TEXT, elements.size(), null);
        }
    }

    private void _go(){
        if(b.drawBorders){
            b.getCanvasBuilder().drawGrayRectangle(origRectangle.get(), BaseColor.LIGHT_GRAY);
        }

        currentLeftResult.totalElementCount =
            currentRightResult.totalElementCount =
             bestResult.totalElementCount = sequence.size();

        //try adding into a single infinite column to calc height
        final float hCenter = horCenter();

        referenceHeight = calcReferenceHeight(hCenter);

        currentLeftResult.referenceHeight =
            currentRightResult.referenceHeight =
                bestResult.referenceHeight = referenceHeight;

        leftCTB = setColumn((float) referenceHeight, hCenter, true, true, singleColumnRect, b.newColumnTextBuilder());
        rightCTB = setColumn((float) referenceHeight, hCenter, false, true, singleColumnRect, b.newColumnTextBuilder());

        minimalLeftColumnHeight = MINIMAL_HEIGHT_COEFFICIENT * referenceHeight / 2;

        int i;

        List<Element> elements = sequence.getElements();

        final DirectContentAdder.Result quickResult = new DirectContentAdder(leftCTB)
            .setStartWith(initialLeftCTB)
            .setStartAtIndex(startAtElement)
            .setQuickHeight(minimalLeftColumnHeight)
            .setSimulate(true)
            .go();

        i = quickResult.index;

        bestResult.assignIfWorseThan(
            currentLeftResult
                .setElementsAddedCount(i)
                .setLeftElementSplitHeight(0, 0)
                .setLeftColumnHeight(leftCTB.getCurrentHeight())
                .setPageSplit(i, quickResult.hasContentLeft(elements.size()))
        );

        boolean pageOverFlow = false;

        //the only situation possible is the content left from initialContent
        if(quickResult.contentLeft != null){
            leftCTB.copyContentFrom(quickResult.contentLeft);
            pageOverFlow = iterateOnLeft(leftCTB.newAtomicIteratorFor(), i - 1,
                currentLeftResult, leftCTB.getTop(), sequence.initialContentHeight);
        }

        if(i == elements.size()){
            bestResult
                .assignIfWorseThan(currentLeftResult
                    .setLeftElementSplitHeight(0, 0)
                    .setLeftColumnHeight(leftCTB.getCurrentHeight())
                    .setElementsAddedCount(i)
                    .setPageSplit(i, leftCTB.hasMoreText()));
        }

        if(pageOverFlow) return;

        elementsCycle:
        for (; i < elements.size(); i++) {
            Element el = elements.get(i);
            final SplitResult currentResult = currentLeftResult;

            currentResult
                .setLeftElementSplitHeight(0, 0)
                .setElementsAddedCount(i)
                .setLeftColumnHeight(leftCTB.getCurrentHeight());

            if(i > 0){          //this is 99.9% true
                considerAddingToRight(i, false);
            }

            if (el instanceof SpaceElement) {
                SpaceElement space = (SpaceElement) el;

                currentResult.setElementsAddedCount(i);

                considerAddingToRight(i + 1, false);

                if (space.fits(leftCTB, origRectangle.getBottom())) {
                    space.add(leftCTB, true);
                } else {
                    if (leftCTB.getSimpleColumnRectangle().getBottom() - space.getHeight() < b.getDocument().bottom()) {
                        break;
                    }

                    leftCTB
                        .growBottom(space.getHeight())
                        .setYLine(leftCTB.getYLine() - space.getHeight());
                }
            } else {
                float elementTop = leftCTB.getYLine();

                final Iterator<AtomicIncreaseResult> iterator = leftCTB.newAtomicIteratorFor(el);

                if (iterateOnLeft(iterator, i, currentResult, elementTop, sequence.getHeight(i))) {
                    break elementsCycle;
                }

//                leftCTB.restoreState();
            }

            currentResult
                .setLeftColumnHeight(leftCTB.getCurrentHeight())
                .setElementsAddedCount(i + 1);
        }


        //here we have bestResult, so let's add our content with no simulation!

//        setColumn(bestResult, b.newColumnTextBuilder())
        setColumn((float) bestResult.leftColumnHeight, hCenter, true, true, singleColumnRect, leftCTB);
        setColumn((float) bestResult.rightColumnHeight, hCenter, false, false, singleColumnRect, rightCTB);

        if(b.drawBorders){
            b.getCanvasBuilder().drawGrayRectangle(leftCTB.getSimpleColumnRectangle(), BaseColor.RED);
            b.getCanvasBuilder().drawGrayRectangle(rightCTB.getSimpleColumnRectangle(), BaseColor.GREEN);
        }

        leftCTB.clearContent();
        rightCTB.clearContent();

        final DirectContentAdder.Result addResult = new DirectContentAdder(leftCTB)
            .setStartWith(initialLeftCTB)
            .setStartAtIndex(startAtElement)
            .setSwitchToRightCTB(rightCTB)
            .setSimulate(false)
            .go();

        if(addResult.hasContentLeft(elements.size())){
            final ColumnTextBuilder contentCopy = addResult.contentLeft == null ? null :
                b.newColumnTextBuilder().setACopy(addResult.contentLeft);


            startWithANewPage(contentCopy, addResult.index);
        }
    }

    private boolean iterateOnLeft(Iterator<AtomicIncreaseResult> iterator, int currentIndex, SplitResult currentResult, float elementTop, float elementHeight) {
        currentResult
            .setLeftColumnHeight(leftCTB.getCurrentHeight())
            .setLeftElementSplitHeight(elementTop - leftCTB.getYLine(), elementHeight);

        if(currentResult.leftElementSplitHeight > 0){
            considerAddingToRight(currentIndex + 1, true);
        }

        while (iterator.hasNext()) {
            AtomicIncreaseResult r = iterator.next();

            currentResult
                .setLeftColumnHeight(leftCTB.getCurrentHeight())
                .setLeftElementSplitHeight(elementTop - leftCTB.getYLine(), elementHeight);

            switch (r.type) {
                case PAGE_OVERFLOW:
                    return true;

                case NORMAL:
                    considerAddingToRight(currentIndex + 1, true);
                    break;

                case NO_MORE_CONTENT:
                    considerAddingToRight(currentIndex + 1, false);
                    break;
            }
        }
        return false;
    }

    private void startWithANewPage(ColumnTextBuilder content, int startAt) {
        //noinspection SimplifiableConditionalExpression
        logger.debug("starting with a new page, content: {}, startAt: {}", content == null ? false : content.hasMoreText(), startAt);

        final RectangleBuilder rect = b.reuseRectangleBuilder(origRectangle.get())
            .setTop(b.getDocument().top())
            .setBottom(b.getDocument().bottom());

        b.getDocument().newPage();

        new BalancedColumnsBuilder(content, startAt, rect.get(), b)
            .setSequence(sequence)
            .go();
    }

    private float calcReferenceHeight(float hCenter) {
        float yBefore = origRectangle.getTop();

        ColumnTextBuilder tempCtb = b.newColumnTextBuilder();

        singleColumnRect.copyPositionsFrom(origRectangle);

        singleColumnRect.setBottom(-100000f);
        singleColumnRect.setRight(hCenter);

        applyPadding(singleColumnRect, true);

        tempCtb.setSimpleColumn(singleColumnRect.get());

        final DirectContentAdder adder = new DirectContentAdder(tempCtb);

        adder
            .setStartWith(initialLeftCTB)
            .setStartAtIndex(startAtElement)
            .setSimulate(true)
            .setHeights(true)
            .go();

        sequence.initialContentHeight = adder.startContentHeight;

        float yAfter = tempCtb.getYLine();

        return (yBefore - yAfter) ;
    }

    private void considerAddingToRight(int startAt, boolean hasNotFlushedText) {
        logger.trace("adding to right, i: {}, leftHeight: {}, leftSplit: {}", startAt, currentLeftResult.leftColumnHeight,
            currentLeftResult.leftElementSplitHeight);


        //copy content from the left
        rightCTB
            .setACopy(leftCTB)
            .setYLine(leftCTB.getTop());

        if(!hasNotFlushedText && sequence.isSpace(startAt)){
            startAt++;
        }

        //we can flush what's left from the previous column
        if(rightCTB.hasMoreText()){
            rightCTB.go(true);
        }

        currentRightResult.copyFrom(currentLeftResult);

        //copied from left method
        int i;

        List<Element> elements = sequence.getElements();

        final DirectContentAdder.Result quickResult = new DirectContentAdder(rightCTB)
            .setStartWith(leftCTB)
            .setStartAtIndex(startAt)
            .setQuickHeight(leftCTB.getCurrentHeight())
            .setSimulate(true)
            .go();

        i = quickResult.index;

        bestResult.assignIfWorseThan(
            currentRightResult
                .setElementsAddedCount(i)
                .setRightElementSplitHeight(0, 0)
                .setRightColumnHeight(rightCTB.getCurrentHeight())
                .setPageSplit(i, quickResult.hasContentLeft(elements.size()))
        );

        boolean pageOverFlow = false;

        if(quickResult.contentLeft != null){
            rightCTB.copyContentFrom(quickResult.contentLeft);
            AtomicIncreaseResult lastResult = iterateOnRight(i - 1, currentRightResult, rightCTB.getTop(), rightCTB.newAtomicIteratorFor());
            if(lastResult.type == ColumnTextBuilder.GrowthResultType.PAGE_OVERFLOW){
                pageOverFlow = true;
            }
        }

        if(i == elements.size()){
            bestResult
                .assignIfWorseThan(currentRightResult
                    .setRightElementSplitHeight(0, 0)
                    .setRightColumnHeight(rightCTB.getCurrentHeight())
                    .setElementsAddedCount(i)
                    .setPageSplit(i, rightCTB.hasMoreText()));
        }

        if(pageOverFlow) return;

        elementsCycle:
        for (; i < elements.size(); i++) {
            Element el = elements.get(i);

            final SplitResult currentResult = currentRightResult;

            setFullAddedElementsStateRight(currentResult, i, rightCTB.getCurrentHeight());

            currentResult.setPageSplit(true);                    //temporary pessimism

            if (el instanceof SpaceElement) {
                //todo extract method
                SpaceElement space = (SpaceElement) el;

                bestResult.assignIfWorseThan(
                    currentResult
                        .setElementsAddedCount(i)
                        .setPageSplit(i, false));


                if (space.fits(rightCTB, origRectangle.getBottom())) {
                    space.add(rightCTB, true);
                } else {
                    if (rightCTB.getSimpleColumnRectangle().getBottom() - space.getHeight() < b.getDocument().bottom()) {
                        break;
                    }

                    rightCTB
                        .growBottom(space.getHeight())
                        .setYLine(rightCTB.getYLine() - space.getHeight());
                }
            } else {
                float elementTop = rightCTB.getYLine();

                final Iterator<AtomicIncreaseResult> iterator = rightCTB.newAtomicIteratorFor(el);

                AtomicIncreaseResult lastResult = iterateOnRight(i, currentResult, elementTop, iterator);

                if(lastResult.type == ColumnTextBuilder.GrowthResultType.PAGE_OVERFLOW){
                    break;
                }
            }

            //element is fully added here
            currentResult
                .setRightColumnHeight(rightCTB.getCurrentHeight())
                .setRightElementSplitHeight(0, 0);

            bestResult.assignIfWorseThan(currentResult);
        }
    }

    private AtomicIncreaseResult iterateOnRight(int currentIndex, SplitResult currentResult, float elementTop, Iterator<AtomicIncreaseResult> iterator) {
        AtomicIncreaseResult lastIncreaseResult = null;

        currentResult
            .setRightColumnHeight(rightCTB.getCurrentHeight())
            .setRightElementSplitHeight(elementTop - rightCTB.getYLine(), sequence.getHeight(currentIndex));

        bestResult.assignIfWorseThan(currentResult);

        cycle: while (iterator.hasNext()) {
            AtomicIncreaseResult r = iterator.next();
            currentResult.setRightColumnHeight(rightCTB.getCurrentHeight());

            lastIncreaseResult = r;

            switch (r.type) {
                case PAGE_OVERFLOW:
                    break cycle;
                case NORMAL:
                    currentResult
                        .setRightElementSplitHeight(elementTop - rightCTB.getYLine(), sequence.getHeight(currentIndex));
                    bestResult.assignIfWorseThan(currentResult);
                    break;
                case NO_MORE_CONTENT:
                    setFullAddedElementsStateRight(currentResult, currentIndex + 1, rightCTB.getCurrentHeight());

                    break cycle;
            }
        }
        return lastIncreaseResult == null ? new AtomicIncreaseResult(0, ColumnTextBuilder.GrowthResultType.NO_MORE_CONTENT) : lastIncreaseResult;
    }

    private void setFullAddedElementsStateRight(SplitResult result, int elementCount, double height) {
        result
            .setRightElementSplitHeight(0, 0)
            .setRightColumnHeight(height)
            .setElementsAddedCount(elementCount)
            .setPageSplit(elementCount, false);

        bestResult.assignIfWorseThan(result);
    }

    private ColumnTextBuilder setColumn(float height, float horCenter, boolean isLeft, boolean simulate, RectangleBuilder singleColumnRect, ColumnTextBuilder ctb) {
//        new RectangleBuilder().
        final RectangleBuilder rect = b
            .newCopyRectangleBuilder(origRectangle.get())
            .setBottom(singleColumnRect.getTop() - height);

        if (isLeft) {
            rect.setRight(horCenter);
        } else {
            rect.setLeft(horCenter);
        }

        applyPadding(rect, isLeft);

        logger.debug("setting column to: {}", rect);

        ctb.setSimpleColumn(rect.get());

        return ctb;
    }

    private void applyPadding(RectangleBuilder r, boolean isLeft) {
        if (isLeft) {
            r.setRight(r.getRight() - hPadding);
        } else {
            r.setLeft(r.getLeft() + hPadding);
        }
    }

    private float horCenter() {
        return (origRectangle.getLeft() + origRectangle.getRight()) / 2;
    }

    private BalancedColumnsBuilder setSequence(ElementSequence sequence) {
        this.sequence = sequence;
        return this;
    }
}
