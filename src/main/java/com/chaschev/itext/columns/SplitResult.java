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

    double getScore(){
        return score + 20 * elementsAddedCount;
    }

    double getTotalScore(){
        return getScore() - getPenalty();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SplitResult{");

        sb.append("totalScore=").append(getTotalScore());
        sb.append(", penalty=").append(getPenalty());
        sb.append(", score=").append(getScore());
        sb.append(", leftColumnHeight=").append(leftColumnHeight);
        sb.append(", rightColumnHeight=").append(rightColumnHeight);
        sb.append(", leftElementSplitHeight=").append(leftElementSplitHeight);
        sb.append(", rightElementSplitHeight=").append(rightElementSplitHeight);
        sb.append(", referenceHeight=").append(referenceHeight);
        sb.append(", rectHeight=").append(rectHeight);
        sb.append(", pageSplit=").append(pageSplit);
        sb.append(", score=").append(score);
        sb.append(", elementsAddedCount=").append(elementsAddedCount);
        sb.append('}');

        return sb.toString();
    }

    public double getPenalty() {
        return
            rightNotUsedPenalty() +
                tooBigColumnPenalty(leftColumnHeight) + tooBigColumnPenalty(rightColumnHeight)
                + columnBalancePenalty()
                + leftSplitPenalty()
                + rightSplitPenalty()
                + pageSplitPenalty();
    }

    private double columnBalancePenalty() {
        final double v = Math.abs(rightColumnHeight - leftColumnHeight);

        if(leftColumnHeight > rightColumnHeight){
            return v * 0.8;
        }else{
            return v;
        }
    }

    private double rightSplitPenalty() {
        return rightElementSplitHeight;
    }

    private double leftSplitPenalty() {
        return leftElementSplitHeight;
    }

    private double rightNotUsedPenalty() {
        if(rightColumnHeight <= 0.001 && elementsAddedCount < totalElementCount){
            return referenceHeight/4;
        }

        return 0;
    }

    private double tooBigColumnPenalty(double columnHeight) {
        double v = columnHeight - referenceHeight;

        if(v < 0) v = 0;

        return v * 6.0;
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

    public SplitResult setLeftElementSplitHeight(double leftElementSplitHeight, float elementHeight) {
        if(Math.abs(leftElementSplitHeight) < 0.001){
            this.leftElementSplitHeight = 0;
        } else {
            this.leftElementSplitHeight =
                Math.min(leftElementSplitHeight, elementHeight - leftElementSplitHeight);

            pageSplit = true;
        }
        return this;
    }

    public SplitResult setRightElementSplitHeight(double rightElementSplitHeight, float elementHeight) {
        if(Math.abs(rightElementSplitHeight) < 0.001){
            this.rightElementSplitHeight = 0;
        }else{
            this.rightElementSplitHeight =
                Math.min(rightElementSplitHeight, elementHeight - rightElementSplitHeight);

            pageSplit = true;
        }
        return this;
    }

    public SplitResult setPageSplit(int fullyRenderedItems, boolean hasMoreContent) {
        return setPageSplit(hasMoreContent || fullyRenderedItems != totalElementCount);
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
            BalancedColumnsBuilder.logger.trace("better score: {}", result);
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
