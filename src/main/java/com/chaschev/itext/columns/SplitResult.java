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

/**
 * User: chaschev
 * Date: 9/8/13
 */
public class SplitResult {
    double referenceHeight;

    double rectHeight;
    double leftColumnHeight;
    double rightColumnHeight;

    double leftElementSplitHeight;
    double rightElementSplitHeight;

    boolean pageSplit;

    double score;

    public int elementsAddedCount;

    public int totalElementCount;

    public SplitResult() {
    }

    double getTotalScore(){
        return score - getPenalty();
    }

    public double getPenalty() {
        return
            rightNotUsedPenalty() +
            columnPenalty(leftColumnHeight) + columnPenalty(rightColumnHeight)
            + rightElementSplitHeight +
            pageSplitPenalty();
    }

    private double rightNotUsedPenalty() {
        if(rightColumnHeight <= 0.001 && elementsAddedCount < totalElementCount){
            return 10;
        }

        return 0;
    }

    private double columnPenalty(double columnHeight) {
        double v = columnHeight - referenceHeight;

        if(v < 0) v = 0;

        return v * 1.0;
    }

    protected double pageSplitPenalty() {
        return pageSplit ? 100 : 0;
    }

    public SplitResult worstResult(){
        leftElementSplitHeight = Double.MAX_VALUE / 20;
        rightElementSplitHeight = Double.MAX_VALUE / 20;
        pageSplit = true;

        return this;
    }

    public double getRectHeight() {
        return this.rectHeight;
    }

    public SplitResult setRectHeight(double rectHeight) {
        this.rectHeight = rectHeight;
        return this;
    }

    public double getLeftColumnHeight() {
        return this.leftColumnHeight;
    }

    public SplitResult setLeftColumnHeight(double leftColumnHeight) {
        this.leftColumnHeight = leftColumnHeight;
        return this;
    }

    public double getRightColumnHeight() {
        return this.rightColumnHeight;
    }

    public SplitResult setRightColumnHeight(double rightColumnHeight) {
        this.rightColumnHeight = rightColumnHeight;
        return this;
    }

    public SplitResult setLeftElementSplitHeight(double leftElementSplitHeight) {
        this.leftElementSplitHeight = leftElementSplitHeight;
        return this;
    }

    public SplitResult setRightElementSplitHeight(double rightElementSplitHeight) {
        this.rightElementSplitHeight = rightElementSplitHeight;
        return this;
    }

    public SplitResult setPageSplit(int fullyRenderedItems, boolean hasNotFlushedText) {
        return setPageSplit(!hasNotFlushedText && fullyRenderedItems == totalElementCount);
    }

    public SplitResult setPageSplit(boolean pageSplit) {
        this.pageSplit = pageSplit;
        return this;
    }

    public SplitResult setScore(double score) {
        this.score = score;
        return this;
    }

    public void assignIfWorseThan(SplitResult result) {
        if(getTotalScore() < result.getTotalScore()){
            copyFrom(result);
        }
    }

    public SplitResult setElementsAddedCount(int elementsAddedCount) {
        this.elementsAddedCount = elementsAddedCount;
        return this;
    }

    public SplitResult copyFrom(SplitResult r){
        referenceHeight = r.referenceHeight;

        rectHeight = r.rectHeight;
        leftColumnHeight = r.leftColumnHeight;
        rightColumnHeight = r.rightColumnHeight;

        leftElementSplitHeight = r.leftElementSplitHeight;
        rightElementSplitHeight = r.rightElementSplitHeight;

        pageSplit = r.pageSplit;

        score = r.score;

        elementsAddedCount = r.elementsAddedCount;

        return this;
    }
}
