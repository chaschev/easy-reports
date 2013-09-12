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

    private float leftElementSplitTotalHeight;
    private float rightElementSplitTotalHeight;

    boolean pageSplit;

    double score;

    public int elementsAddedCount;

    public int totalElementCount;


    public SplitResult() {
    }

    double getScore(){
        return score + 20 * elementsAddedCount
            + (leftColumnHeight + rightColumnHeight) * 0.8;
    }

    double getTotalScore(){
        return getScore() - getPenalty();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SplitResult{");

        sb.append("totalScore=").append(format(getTotalScore(), 1));
        sb.append(", penalty=").append(format(getPenalty(), 1));
        sb.append(", score=").append(format(getScore(), 1));
        sb.append(", leftColumnHeight=").append(leftColumnHeight);
        sb.append(", rightColumnHeight=").append(rightColumnHeight);
        sb.append(", leftElementSplitHeight=").append(format(leftElementSplitHeight, 1));
        sb.append(", rightElementSplitHeight=").append(format(rightElementSplitHeight, 1));
        sb.append(", referenceHeight=").append(format(referenceHeight, 1));
        sb.append(", rectHeight=").append(rectHeight);
        sb.append(", pageSplit=").append(pageSplit);
        sb.append(", score=").append(score);
        sb.append(", elementsAddedCount=").append(elementsAddedCount);
        sb.append(",\n              penalties=").append(debugPenalties());
        sb.append('}');

        return sb.toString();
    }


    public static final long[] pows10;

    static {
        pows10 = new long[17];
        pows10[0] = 1;
        for (int i = 1; i < pows10.length; i++) {
            pows10[i] = 10 * pows10[i-1];
        }
    }


    //todo move to chutis
    private static String format(double totalScore, int precision) {
        final long l = (long) totalScore;
        return l + "." + Math.abs((long) ((totalScore - l) * pows10[precision]));
    }

    public String debugPenalties(){
        return String.format(
            "rightNotUsed=%s, " +
            "bigLeft=%s, " +
            "bigRight=%s, " +
            "imbalance=%s, " +
            "smallBalance=%s, " +
            "leftSplit=%s, " +
            "rightSplit=%s, " +
            "pageSplit=%s",
            format(rightNotUsedPenalty(), 1),
            format(tooBigColumnPenalty(leftColumnHeight), 1),
            format(tooBigColumnPenalty(rightColumnHeight), 1),
            format(columnBalancePenalty(), 1),
            format(smallBalancePenalty(), 1),
            format(leftSplitPenalty(), 1),
            format(rightSplitPenalty(), 1),
            format(pageSplitPenalty(), 1)
        );
    }

    public double getPenalty() {
        return
            rightNotUsedPenalty() +
                tooBigColumnPenalty(leftColumnHeight) + tooBigColumnPenalty(rightColumnHeight)
                + columnBalancePenalty()
                + smallBalancePenalty()
                + leftSplitPenalty()
                + rightSplitPenalty()
                + pageSplitPenalty();
    }

    /**
     * Small items should not be balanced;
     */
    private double smallBalancePenalty() {
        double v = (rightColumnHeight + leftColumnHeight) -
            Math.abs(rightColumnHeight - leftColumnHeight);

        if(referenceHeight < 11.0 * 3){
            v *= 1.5;
        }else
        if(referenceHeight < 11.0 * 5){
            v *= 1.2;
        }else
        if(referenceHeight < 11.0 * 8){
            v *= 0.6;
        }else
        if(referenceHeight < 11.0 * 12){
            v *= 0.3;
        }else{
            v = 0;
        }

        return v;
    }

    private double columnBalancePenalty() {
        double v = Math.abs(rightColumnHeight - leftColumnHeight);

        if(leftColumnHeight > rightColumnHeight){
            v *= 0.8;
        }else{
            v *= 1.6;
        }

        if(referenceHeight < 11.0 * 3){
            v *= 0.1;
        }else
        if(referenceHeight < 11.0 * 5){
            v *= 0.3;
        }else
        if(referenceHeight < 11.0 * 8){
            v *= 0.6;
        }else
        if(referenceHeight < 11.0 * 12){
            v *= 0.8;
        }

        return v;
    }

    private double leftSplitPenalty() {
        return splitPenalty(leftElementSplitHeight, leftColumnHeight);
    }

    private double rightSplitPenalty() {
        return splitPenalty(rightElementSplitHeight, rightColumnHeight);
    }

    private static double splitPenalty(double splitH, double columnH) {
//        return Math.abs(splitH) < 0.01 ? 0 :
//            Math.abs(columnH /2 - splitH) * 2;

        double v = Math.abs(splitH) < 0.01 ? 0 :
            (columnH / splitH * 5);

        if (splitH <= 11.0 * 1.5) {
            v *= 3.0;
        } else if (splitH <= 11.0 * 2.5) {
            v *= 2.0;
        }
        if (splitH <= 11.0 * 3.5) {
            v *= 1.5;
        }

        return v;
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
        this.leftElementSplitHeight = getSplitHeight(leftElementSplitHeight, elementHeight);
        this.leftElementSplitTotalHeight = elementHeight;

        return this;
    }

    public SplitResult setRightElementSplitHeight(double rightElementSplitHeight, float elementHeight) {
        this.rightElementSplitHeight =  getSplitHeight(rightElementSplitHeight, elementHeight);
        this.rightElementSplitTotalHeight = elementHeight;
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
        if(BalancedColumnsBuilder.logger.isTraceEnabled()){
            if(!result.pageSplit){
                BalancedColumnsBuilder.logger.trace("comparing to: \n  {}", result);
            }
        }
        if(getTotalScore() < result.getTotalScore()){
            BalancedColumnsBuilder.logger.trace("better score: \n  {}", result);
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


        leftElementSplitTotalHeight = r.leftElementSplitTotalHeight;
        rightElementSplitTotalHeight = r.rightElementSplitTotalHeight;

        pageSplit = r.pageSplit;

        score = r.score;

        elementsAddedCount = r.elementsAddedCount;

        return this;
    }

    private double getSplitHeight(double leftElementSplitHeight, float elementHeight) {
        final double height ;
        if(Math.abs(leftElementSplitHeight) < 0.001){
            height = 0;
        } else {
            height = Math.min(leftElementSplitHeight, elementHeight - leftElementSplitHeight);

            pageSplit = true;
        }
        return height;
    }


}
