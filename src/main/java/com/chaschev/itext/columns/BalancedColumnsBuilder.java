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
 * User: chaschev
 * Date: 9/8/13
 */
public class BalancedColumnsBuilder {
    private static final Logger logger = LoggerFactory.getLogger(BalancedColumnsBuilder.class);

    private static final double MINIMAL_HEIGHT_COEFFICIENT = 0.70;

    private int startAtElement = 0;
    private ColumnTextBuilder initialLeftCTB;

    ElementSequence sequence = new ElementSequence();

    RectangleBuilder origRectangle;
    RectangleBuilder workingRectangle;

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
    private final SplitResult bestResult = new SplitResult().worstResult();

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

    public BalancedColumnsBuilder add(Element element) {
        sequence.add(element);
        return this;
    }

    public double heightPenalty(double columnHeight){
        double v = columnHeight - referenceHeight;

        if(v < 0) return v = 0;

        return v * 1.0;
    }

    public void go() {
        if(initialLeftCTB == null){
            go(b.newColumnTextBuilder());
        }else{
            go(b.newColumnTextBuilder().setACopy(initialLeftCTB));
        }
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

        public class Result{
            int status;
            int index;
            ColumnTextBuilder content;

            public Result(int status, int index, ColumnTextBuilder content) {
                this.status = status;
                this.index = index;
                this.content = content;
            }

            public boolean noContentLeft(int elementCount){
                return content == null && elementCount == index;
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

            if(initialLeftCTB != null){
                currentCtb.copyContentFrom(initialLeftCTB);

                if(quickHeight != 0){
                    currentCtb.adjustBottom((float) quickHeight);
                }

                if (ColumnText.hasMoreText(leftCTB.go(simulate))){
                    // quickHeight == 0
                    if (rightCTB != null) {
                        rightCTB.copyContentFrom(currentCtb);

                        currentCtb = rightCTB;

                        if (ColumnText.hasMoreText(currentCtb.go(simulate))) {
                            return new Result(ColumnText.NO_MORE_COLUMN, startAtIndex, currentCtb);
                        }
                    } else{
                        return new Result(ColumnText.NO_MORE_COLUMN, startAtIndex, currentCtb);
                    }
                }
            }

            //optimisation mode
            if(quickHeight !=0){
                for (i = startAtElement; i < elements.size(); i++) {
                    Element el = elements.get(i);

                    if(currentCtb.fits(el)){
                        currentCtb.go(simulate);
                    }else{
                        break;
                    }
                }

                return new Result(ColumnText.NO_MORE_TEXT, i, null);
            }

            for (i = startAtElement; i < elements.size(); i++) {
                Element el = elements.get(i);

                if (el instanceof SpaceElement) {
                    SpaceElement space = (SpaceElement) el;

                    if(space.fits(currentCtb, currentCtb.getBottom())){
                        space.add(currentCtb, simulate);
                    }else{
                        if(currentCtb == dest){
                            currentCtb = rightCTB;
                        }else if(currentCtb == rightCTB){
                            return new Result(ColumnText.NO_MORE_COLUMN, i + 1, currentCtb.reset());
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

                                if (ColumnText.hasMoreText(currentCtb.go(simulate))) {
                                    return new Result(ColumnText.NO_MORE_COLUMN, i + 1, currentCtb);
                                }
                            }
                        }else{
                            return new Result(ColumnText.NO_MORE_COLUMN, i + 1, currentCtb);
                        }
                    }
                }
            }

            return new Result(ColumnText.NO_MORE_TEXT, elements.size(), null);
        }
    }

    private void addContent(){

    }

    private void go(ColumnTextBuilder ctbForLeftColumn){
        //try adding into a single infinite column to calc height
        final float hCenter = horCenter();

        referenceHeight = calcReferenceHeight(hCenter);

        leftCTB = setColumn((float) referenceHeight, hCenter, true, true, singleColumnRect, b.newColumnTextBuilder());
        rightCTB = setColumn((float) referenceHeight, hCenter, false, true, singleColumnRect, b.newColumnTextBuilder());

        minimalLeftColumnHeight = MINIMAL_HEIGHT_COEFFICIENT * referenceHeight;

        //todo quickly add elements for minimalLeftColumnHeight

        int i = startAtElement;

        List<Element> elements = sequence.getElements();

        new DirectContentAdder(leftCTB)
            .setStartWith(initialLeftCTB)
            .setStartAtIndex(startAtElement)
            .setQuickHeight(minimalLeftColumnHeight)
            .setSimulate(true)
            .go();

        elementsCycle:
        for (; i < elements.size(); i++) {
            Element el = elements.get(i);
            final SplitResult currentResult = currentLeftResult;

            currentResult
                .setLeftElementSplitHeight(0)
                .setElementsAddedCount(i)
                .setLeftColumnHeight(leftCTB.getCurrentHeight());

            if(i > 0){          //this is 99.9% true
                considerAddingToRight(i, false);
            }

            if (el instanceof SpaceElement) {
                SpaceElement space = (SpaceElement) el;

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

                final Iterator<ColumnTextBuilder.AtomicIncreaseResult> iterator = leftCTB.newAtomicIteratorFor(el, workingRectangle);

                currentResult
                    .setLeftColumnHeight(leftCTB.getCurrentHeight())
                    .setLeftElementSplitHeight(elementTop - rightCTB.getYLine());

                while (iterator.hasNext()) {
                    ColumnTextBuilder.AtomicIncreaseResult r = iterator.next();

                    currentResult.setLeftElementSplitHeight(rightCTB.getCurrentHeight());

                    switch (r.type) {
                        case PAGE_OVERFLOW:
                            break elementsCycle;

                        case NORMAL:
                            currentResult.setLeftElementSplitHeight(elementTop - leftCTB.getYLine());
                            considerAddingToRight(i + 1, true);
                            break;

                        case NO_MORE_CONTENT:
                            break;
                    }
                }
            }

            currentResult
                .setLeftColumnHeight(leftCTB.getCurrentHeight())
                .setElementsAddedCount(i + 1);

            //check height penalty
            if (currentResult.getTotalScore() < bestResult.getTotalScore()) {
                break;
            }
        }


        //here we have bestResult, so let's add our content with no simulation!

//        setColumn(bestResult, b.newColumnTextBuilder())
        setColumn((float) bestResult.leftColumnHeight, hCenter, true, true, singleColumnRect, leftCTB);
        setColumn((float) bestResult.rightColumnHeight, hCenter, false, false, singleColumnRect, rightCTB);

        final DirectContentAdder.Result addResult = new DirectContentAdder(leftCTB)
            .setStartWith(initialLeftCTB)
            .setStartAtIndex(startAtElement)
            .setSwitchToRightCTB(rightCTB)
            .setSimulate(false)
            .go();

        if(!addResult.noContentLeft(elements.size())){
            startWithANewPage(addResult.content, addResult.index);
        }
    }

    private void startWithANewPage(ColumnTextBuilder ctb, int startAt) {

    }

    private float calcReferenceHeight(float hCenter) {
        float yBefore = origRectangle.getTop();

        ColumnTextBuilder tempCtb = b.newColumnTextBuilder();

        singleColumnRect.copyPositionsFrom(origRectangle);

        singleColumnRect.setBottom(-100000f);
        singleColumnRect.setRight(hCenter);

        //todo applyPadding(tempR, true);

        tempCtb.setSimpleColumn(singleColumnRect.get());

        addSequence(tempCtb, true);

        float yAfter = tempCtb.getYLine();

        return yBefore - yAfter;
    }

    private void considerAddingToRight(int startAt, boolean hasNotFlushedText) {
        currentRightResult.copyFrom(currentLeftResult);

        rightCTB.setACopy(leftCTB);

        if(!hasNotFlushedText && sequence.isSpace(startAt)){
            startAt++;
        }

        List<Element> elements = sequence.getElements();

        elementsCycle:
        for (int i = startAt; i < elements.size(); i++) {
            Element el = elements.get(i);

            final SplitResult currentResult = currentRightResult;

            currentResult
                .setRightElementSplitHeight(0)
                .setRightColumnHeight(rightCTB.getCurrentHeight())
                .setElementsAddedCount(i)
                .setPageSplit(true);                    //temporary pessimism

            bestResult.assignIfWorseThan(currentResult);

            if (el instanceof SpaceElement) {
                //todo extract method
                SpaceElement space = (SpaceElement) el;

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

                final Iterator<ColumnTextBuilder.AtomicIncreaseResult> iterator = rightCTB.newAtomicIteratorFor(el, workingRectangle);

                //
                currentResult
                    .setRightColumnHeight(rightCTB.getCurrentHeight())
                    .setRightElementSplitHeight(elementTop - rightCTB.getYLine());

                bestResult.assignIfWorseThan(currentResult);

                cycle: while (iterator.hasNext()) {
                    ColumnTextBuilder.AtomicIncreaseResult r = iterator.next();
                    currentResult.setRightColumnHeight(rightCTB.getCurrentHeight());

                    switch (r.type) {
                        case PAGE_OVERFLOW:
                            break elementsCycle;
                        case NORMAL:
                            currentResult.setRightElementSplitHeight(elementTop - rightCTB.getYLine());
                            bestResult.assignIfWorseThan(currentResult);
                            break;
                        case NO_MORE_CONTENT:
                            bestResult.assignIfWorseThan(currentResult);
                            break cycle;
                    }
                }
            }

            //element is fully added here
            currentResult
                .setRightColumnHeight(rightCTB.getCurrentHeight())
                .setRightElementSplitHeight(0);

            bestResult.assignIfWorseThan(currentResult);

            //check height penalty
            if (currentResult.getTotalScore() < bestResult.getTotalScore()) {
                break;
            }
        }
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

    private void addSequence(ColumnTextBuilder ctb, boolean simulate) {
        for (Element el : sequence.getElements()) {
            if (el instanceof SpaceElement) {
                SpaceElement space = (SpaceElement) el;

                space.add(ctb, simulate);
            }else{
                ctb.addElement(el);
                if(ColumnText.hasMoreText(ctb.go(simulate))){
                    throw new IllegalStateException("element " + el + " does not fit infinite column");
                }
            }
        }
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
    
    

}
