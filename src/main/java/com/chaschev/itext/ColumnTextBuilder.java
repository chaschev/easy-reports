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

import com.chaschev.chutils.util.Exceptions;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: ACHASCHEV
 * Date: 7/11/13
 * Time: 3:05 PM
 */
public class ColumnTextBuilder {
    public static final SimpleGrowStrategy DEFAULT_GROW_STRATEGY = new SimpleGrowStrategy(10.0);

    private final ITextBuilder iTextBuilder;
    ColumnText columnText;
    private Rectangle simpleColumnRectangle;

    protected float pageBottom;
    protected float pageTop;
    private boolean frozen;

    private ColumnTextBuilder storedState;

    public ColumnTextBuilder(ITextBuilder iTextBuilder) {
        this.iTextBuilder = iTextBuilder;
    }

    public ColumnTextBuilder newColumnText(PdfContentByte canvas) {
        columnText = new ColumnText(canvas);
        return this;
    }


    public int getLinesWritten() {
        return columnText.getLinesWritten();
    }

    public void setCanvases(PdfContentByte[] canvases) {
        columnText.setCanvases(canvases);
    }

    public PdfContentByte getCanvas() {
        return columnText.getCanvas();
    }

    public boolean hasMoreText(int status) {
        return ColumnText.hasMoreText(status);
    }

    /**
     * Avoid using this non-API method.
     */
    public boolean hasMoreText() {
        try {
            final Object compositeElements = compositeElementsField.get(columnText);

            return getVersion() != 0 || (compositeElements!=null && !((List)compositeElements).isEmpty());
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public boolean zeroHeightElement() {
        return columnText.zeroHeightElement();
    }

    public static float fitText(Font font, String text, Rectangle rect, float maxFontSize, int runDirection) {
        return ColumnText.fitText(font, text, rect, maxFontSize, runDirection);
    }

    public void setCanvas(PdfContentByte canvas) {
        columnText.setCanvas(canvas);
    }

    public ColumnTextBuilder addElement(Element element) {
        columnText.addElement(element);
        return this;
    }

    public int getRowsDrawn() {
        return columnText.getRowsDrawn();
    }

    public ColumnTextBuilder setACopy(ColumnTextBuilder source) {
        clearContent();    //content is saved after setACopy

        setSimpleColumn(new Rectangle(source.simpleColumnRectangle));

        columnText.setACopy(source.columnText);

        this.pageBottom = source.pageBottom;
        this.pageTop = source.pageTop;

        return this;
    }

    public ColumnTextBuilder copyContentFrom(ColumnTextBuilder source) {
        if(source == this){
            return this;
        }

        final Rectangle temp = simpleColumnRectangle;

        setACopy(source);

        setSimpleColumn(temp);

        return this;
    }

    public boolean getInheritGraphicState() {
        return columnText.getInheritGraphicState();
    }

    public void clearChunks() {
        columnText.clearChunks();
    }

    public float getLeading() {
        return columnText.getLeading();
    }

    public ColumnTextBuilder setColumns(float[] leftLine, float[] rightLine) {
        columnText.setColumns(leftLine, rightLine);
        simpleColumnRectangle = null;
        return this;
    }

    public float getFilledWidth() {
        return columnText.getFilledWidth();
    }

    public ColumnTextBuilder setLeading(float leading) {
        columnText.setLeading(leading);
        return this;
    }

    public float getSpaceCharRatio() {
        return columnText.getSpaceCharRatio();
    }

    public int getRunDirection() {
        return columnText.getRunDirection();
    }

    public PdfContentByte[] getCanvases() {
        return columnText.getCanvases();
    }

    public int getArabicOptions() {
        return columnText.getArabicOptions();
    }

    public ColumnTextBuilder setSpaceCharRatio(float spaceCharRatio) {
        columnText.setSpaceCharRatio(spaceCharRatio);
        return this;
    }

    public ColumnTextBuilder setSimpleColumn(float llx, float lly, float urx, float ury) {
        return setSimpleColumn(new Rectangle(llx, lly, urx, ury));
    }


    public int go(boolean simulate, IAccessibleElement elementToGo) throws DocumentException {
        Preconditions.checkArgument(!frozen, "can't go when frozen");
        return columnText.go(simulate, elementToGo);
    }

    public ColumnTextBuilder addText(Chunk chunk) {
        columnText.addText(chunk);
        return this;
    }

    public static boolean isAllowedElement(Element element) {
        return ColumnText.isAllowedElement(element);
    }

    public ColumnTextBuilder setText(Phrase phrase) {
        columnText.setText(phrase);
        return this;
    }

    public ColumnTextBuilder clearContent(){
        return setText(new Phrase(""));
    }

    public boolean isAdjustFirstLine() {
        return columnText.isAdjustFirstLine();
    }

    public float getExtraParagraphSpace() {
        return columnText.getExtraParagraphSpace();
    }

    public float getYLine() {
        return columnText.getYLine();
    }

    public void setFilledWidth(float filledWidth) {
        columnText.setFilledWidth(filledWidth);
    }

    public void setIndent(float indent) {
        columnText.setIndent(indent);
    }

    public void setAdjustFirstLine(boolean adjustFirstLine) {
        columnText.setAdjustFirstLine(adjustFirstLine);
    }

    public float getMultipliedLeading() {
        return columnText.getMultipliedLeading();
    }

    public float getLastX() {
        return columnText.getLastX();
    }

    public float getIndent() {
        return columnText.getIndent();
    }

    public ColumnTextBuilder goGo() throws DocumentException {
        while(ColumnText.hasMoreText(go())){
            iTextBuilder.document.newPage();

            simpleColumnRectangle = new Rectangle(simpleColumnRectangle);

            simpleColumnRectangle.setBottom(pageBottom);
            simpleColumnRectangle.setTop(pageTop);

            columnText.setSimpleColumn(simpleColumnRectangle);

            iTextBuilder.defaultColumnTextSettings.apply(this);
        }

        return this;
    }

    public int go() {
        try {
            Preconditions.checkArgument(!frozen, "can't go when frozen");

            return columnText.go();
        } catch (DocumentException e) {
            throw Exceptions.runtime(e);
        }
    }

    public void setExtraParagraphSpace(float extraParagraphSpace) {
        columnText.setExtraParagraphSpace(extraParagraphSpace);
    }

    public float getCurrentLeading() {
        return columnText.getCurrentLeading();
    }

    public int go(boolean simulate)  {
        try {
            Preconditions.checkArgument(!frozen, "can't go when frozen");
            return columnText.go(simulate);
        } catch (DocumentException e) {
            throw Exceptions.runtime(e);
        }
    }

    public void setInheritGraphicState(boolean inheritGraphicState) {
        columnText.setInheritGraphicState(inheritGraphicState);
    }

    public float getRightIndent() {
        return columnText.getRightIndent();
    }

    public void setIndent(float indent, boolean repeatFirstLineIndent) {
        columnText.setIndent(indent, repeatFirstLineIndent);
    }

    public boolean isUseAscender() {
        return columnText.isUseAscender();
    }

    public void setAlignment(int alignment) {
        columnText.setAlignment(alignment);
    }

    public float getDescender() {
        return columnText.getDescender();
    }

    public ColumnTextBuilder setSimpleColumn(float llx, float lly, float urx, float ury, float leading, int alignment) {
        this.simpleColumnRectangle = new Rectangle(llx, lly, urx, ury);
        columnText.setSimpleColumn(llx, lly, urx, ury, leading, alignment);
        return this;
    }

    public void setFollowingIndent(float indent) {
        columnText.setFollowingIndent(indent);
    }

    public static float getWidth(Phrase phrase) {
        return ColumnText.getWidth(phrase);
    }

    public void updateFilledWidth(float w) {
        columnText.updateFilledWidth(w);
    }

    public void setRightIndent(float indent) {
        columnText.setRightIndent(indent);
    }

    public void setArabicOptions(int arabicOptions) {
        columnText.setArabicOptions(arabicOptions);
    }

    public void setRunDirection(int runDirection) {
        columnText.setRunDirection(runDirection);
    }

    public float getFollowingIndent() {
        return columnText.getFollowingIndent();
    }

    public void setLeading(float fixedLeading, float multipliedLeading) {
        columnText.setLeading(fixedLeading, multipliedLeading);
    }

    public ColumnTextBuilder setYLine(float yLine) {
        columnText.setYLine(yLine);
        return this;
    }

    public ColumnTextBuilder setSimpleColumn(Rectangle rect) {
        return setSimpleColumn(rect, false);
    }

    public ColumnTextBuilder setSimpleColumn(Rectangle rect, boolean saveYLine) {
        float yLine = 0;

        if(saveYLine){
            yLine = getYLine();
        }

        Preconditions.checkArgument(!frozen, "can't change column when frozen");

        columnText.setSimpleColumn(rect);
        this.simpleColumnRectangle = rect;

        this.pageBottom = iTextBuilder.document.bottom();
        this.pageTop = iTextBuilder.document.top();

        if(saveYLine){
            setYLine(yLine);
        }

        return this;
    }

    public static float getWidth(Phrase phrase, int runDirection, int arabicOptions) {
        return ColumnText.getWidth(phrase, runDirection, arabicOptions);
    }

    public void setSimpleColumn(Phrase phrase, float llx, float lly, float urx, float ury, float leading, int alignment) {
        columnText.setSimpleColumn(phrase, llx, lly, urx, ury, leading, alignment);
    }

    public void setUseAscender(boolean useAscender) {
        columnText.setUseAscender(useAscender);
    }

    public ColumnTextBuilder addText(Phrase phrase) {
        columnText.addText(phrase);
        return this;
    }

    public int getAlignment() {
        return columnText.getAlignment();
    }

    public List<Element> getCompositeElements() {
        return columnText.getCompositeElements();
    }

    public ColumnTextBuilder verticalSeparator(float h) {
        columnText.setYLine(columnText.getYLine() - h);

        return this;
    }

    public ColumnTextBuilder newLine() {
        columnText.addText(Chunk.NEWLINE);
        return this;
    }

    public void setPageBottom(float pageBottom) {
        this.pageBottom = pageBottom;
    }

    public void setPageTop(float pageTop) {
        this.pageTop = pageTop;
    }

    public ColumnTextBuilder dup(){
        final ColumnTextBuilder b = new ColumnTextBuilder(iTextBuilder);

        b.simpleColumnRectangle = new Rectangle(simpleColumnRectangle);
        b.columnText = ColumnText.duplicate(columnText);
        b.pageBottom = pageBottom;
        b.pageTop = pageTop;

        return b;
    }

    public RectangleBuilder getCurrentRectangle() {
        return iTextBuilder
            .newRectangleBuilder(new Rectangle(simpleColumnRectangle))
            .setTop(getYLine())
            ;
    }

    public ColumnTextBuilder addTruncatedLine(Chunk chunk, boolean addEllipsis) {
        final float pixelsForEllipsis = 6;

        try {
            ColumnText dup = ColumnText.duplicate(columnText);

            final Rectangle oneLineRectangle = new Rectangle(simpleColumnRectangle);

            oneLineRectangle.setTop(dup.getYLine());

            final float fontHeight = calcApproximateFontHeight(chunk.getFont()) * 1.6f;

            oneLineRectangle.setBottom(dup.getYLine() - fontHeight);

            if(addEllipsis){
                oneLineRectangle.setRight(oneLineRectangle.getRight() - pixelsForEllipsis);
            }

            dup.setSimpleColumn(oneLineRectangle);
            dup.addText(chunk);

            final int status = dup.go();

            float yLine;

            if(addEllipsis && ColumnText.hasMoreText(status)){
                oneLineRectangle.setLeft(dup.getLastX() + 2);
                oneLineRectangle.setRight(oneLineRectangle.getRight() + pixelsForEllipsis * 2);

                dup = ColumnText.duplicate(dup);


                dup.setSimpleColumn(oneLineRectangle);

                final Chunk ellipses = new Chunk("...\n", chunk.getFont());

                dup.setText(new Phrase(ellipses));
                dup.go();
                yLine = dup.getYLine();
            }else{
                yLine = dup.getYLine();
            }

            setYLine(yLine);

            return this;

        } catch (DocumentException e) {
            throw Exceptions.runtime(e);
        }
    }

    private static float calcApproximateFontHeight(Font font) {
        final BaseFont baseFont = font.getBaseFont();

        return baseFont.getFontDescriptor(BaseFont.ASCENT, font.getSize()) -
        baseFont.getFontDescriptor(BaseFont.DESCENT, font.getSize());
    }

    public ColumnTextBuilder adjustBottom(float newRectHeight) {
        new RectangleBuilder().reuse(simpleColumnRectangle).adjustBottom(newRectHeight);

        setSimpleColumn(simpleColumnRectangle);

        return this;
    }

    public ColumnTextBuilder growBottom(float growBy) {
        iTextBuilder.reusableRectangleBuilder.reuse(simpleColumnRectangle).growBottom(growBy);

        float yLine = getYLine();

        setSimpleColumn(simpleColumnRectangle);

        setYLine(yLine);

        return this;
    }

    public double getCurrentHeight() {
        return simpleColumnRectangle.getTop() - getYLine();
    }

    public float getBottom() {
        return simpleColumnRectangle.getBottom();
    }

    public float getRight() {
        return simpleColumnRectangle.getRight();
    }

    public float getLeft() {
        return simpleColumnRectangle.getLeft();
    }

    public float getTop() {
        return simpleColumnRectangle.getTop();
    }

    public ColumnTextBuilder freeze() {
        frozen = true;
        return this;
    }



    public static enum GrowthResultType{
        PAGE_OVERFLOW,
        NORMAL,
        NO_MORE_CONTENT
    }

    public Iterator<AtomicIncreaseResult> newAtomicIteratorFor(){
        return newAtomicIteratorFor(null);
    }

    public Iterator<AtomicIncreaseResult> newAtomicIteratorFor(@Nullable Element element){
        final Rectangle rect = getSimpleColumnRectangle();

        final RectangleBuilder original = iTextBuilder.newRectangleBuilder(rect);

        rect.setBottom(rect.getTop());

        setSimpleColumn(rect, true);

        return newAtomicIteratorFor(element, iTextBuilder.newRectangleBuilder(rect), original);
    }

    public Iterator<AtomicIncreaseResult> newAtomicIteratorFor(@Nullable Element element, final RectangleBuilder modifiableRectangle, RectangleBuilder originalRectangle){
        if(element != null){
            addElement(element);
        }

        final GrowStrategy growStrategy;

        if(element == null){
            growStrategy = DEFAULT_GROW_STRATEGY;
        }else
        if (element instanceof Phrase) {
            Phrase phrase = (Phrase) element;
            final float approxHeight = phrase.getTotalLeading();

            growStrategy = new SimpleGrowStrategy(approxHeight);
        }else
        if (element instanceof com.itextpdf.text.List) {
            com.itextpdf.text.List list = (com.itextpdf.text.List) element;

            float lineHeight = 10f;

            final ListItem item = list.getFirstItem();

            if(item != null){
                lineHeight = item.getTotalLeading();
            }

            growStrategy = new SimpleGrowStrategy(lineHeight);
        }else
        if (element instanceof PdfPTable) {
            PdfPTable table = (PdfPTable) element;

            float lineHeight = Float.MAX_VALUE;

            ArrayList<PdfPRow> rows = table.getRows();
            for (int i = 0; i < rows.size(); i++) {
                lineHeight = Math.min(lineHeight, table.getRowHeight(i));
            }

            if(lineHeight == Float.MAX_VALUE){
                lineHeight = 10f;
            }

            growStrategy = new SimpleGrowStrategy(lineHeight);
        }else
        {
            throw new UnsupportedOperationException("todo: support element: " + element.getClass().getSimpleName());
        }

        return newAtomicIteratorFor(modifiableRectangle, growStrategy, originalRectangle);
    }

    public static interface GrowStrategy{
        void apply(int step, RectangleBuilder modifiableRect);
    }

    public static final class SimpleGrowStrategy implements GrowStrategy{
        final double approxHeight;
        float smallStep;
        float bigStep;

        public SimpleGrowStrategy(double approxMinLineHeight) {
            this.approxHeight = approxMinLineHeight;
            smallStep = (float) (approxMinLineHeight / 5);
            bigStep = (float) (approxMinLineHeight * 0.8);
        }

        @Override
        public final void apply(int step, RectangleBuilder modifiableRect) {
            if(step == 1){
                modifiableRect.growBottom(bigStep);
            }else{
                modifiableRect.growBottom(smallStep);
            }
        }
    }


    public Iterator<AtomicIncreaseResult> newAtomicIteratorFor(final RectangleBuilder modifiableRectangle, final GrowStrategy growStrategy, RectangleBuilder originalRectangle) {
        final int status = go(true);

        if(!ColumnText.hasMoreText(status)){
            return Iterators.emptyIterator();
        }

        return new AtomicIterator(modifiableRectangle, growStrategy, originalRectangle);
    }

    static final Field bidiLineField;
    static final Field compositeElementsField;
    static final Field totalTextLength;

    static {
        try {
            bidiLineField = ColumnText.class.getDeclaredField("bidiLine");
            bidiLineField.setAccessible(true);

            compositeElementsField = ColumnText.class.getDeclaredField("compositeElements");
            compositeElementsField.setAccessible(true);

            totalTextLength = BidiLine.class.getDeclaredField("totalTextLength");
            totalTextLength.setAccessible(true);
        } catch (NoSuchFieldException e) {
            ITextBuilder.logger.info(e.toString());
            throw Exceptions.runtime(e);
        }
    }

    /**
     * @deprecated fix: add version for composite mode
     */
    @Deprecated
    public int getVersion(){
        try {
            final Object bidiLine = bidiLineField.get(columnText);

            if(bidiLine == null){
                return 0;
            }

            return (Integer)totalTextLength.get(bidiLine);
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public Rectangle getSimpleColumnRectangle() {
        return simpleColumnRectangle;
    }

    public boolean fits(Element... element) {
        return fits(iTextBuilder.reuseColumnTextBuilder(), element);
    }

    public boolean fits(ColumnTextBuilder reusableCtb, Element... element) {
        reusableCtb.clearContent().setSimpleColumn(getCurrentRectangle().get());

        for (Element el : element) {
            if (el instanceof SpaceElement) {
                final SpaceElement space = (SpaceElement) el;
                if(space.fits(reusableCtb)){
                    space.add(reusableCtb, true);
                }else{
                    return false;
                }
            } else {
                reusableCtb.addElement(el);

                if (ColumnText.hasMoreText(reusableCtb.go(true))) {
                    return false;
                }
            }
        }

        return true;
    }

    public ColumnTextBuilder saveState(){
        if(storedState == null){
            storedState = iTextBuilder.newColumnTextBuilder();
        }
        storedState.setACopy(this);

        return this;
    }

    public ColumnTextBuilder restoreState(){
        setACopy(storedState);

        return this;
    }

    private class AtomicIterator extends AbstractIterator<AtomicIncreaseResult> {
        private final RectangleBuilder modifiableRectangle;
        private final GrowStrategy growStrategy;
        boolean finishOnNext;

        private final RectangleBuilder originalRectangle;

        public AtomicIterator(RectangleBuilder modifiableRectangle, GrowStrategy growStrategy, RectangleBuilder originalRectangle) {
            this.modifiableRectangle = modifiableRectangle;
            this.growStrategy = growStrategy;
            this.originalRectangle = originalRectangle;
        }

        public void restore(){
            setSimpleColumn(originalRectangle.get(), true);
        }

        @Override
        protected AtomicIncreaseResult computeNext() {
            if(finishOnNext){
                endOfData();
                return null;
            }

            int stepCount = 0;

            final AtomicIncreaseResult r;

            int status = go(true);

            if(!ColumnText.hasMoreText(status)){
                return new AtomicIncreaseResult(0, GrowthResultType.NORMAL);
            }

            float bottomBefore = modifiableRectangle.getBottom();

            while(true){
                stepCount++;

                growStrategy.apply(stepCount, modifiableRectangle);

                if(modifiableRectangle.getBottom() < pageBottom){
                    r = new AtomicIncreaseResult(bottomBefore - modifiableRectangle.getBottom(), GrowthResultType.PAGE_OVERFLOW);
                    finishOnNext();
                    break;
                }

                setSimpleColumn(modifiableRectangle.get(), true);

                final float v1 = getYLine();

                // iText has a bug here: it can't handle keepTogether()
                // because it presumes that items would never be rolled back
                // so it adds line by line, adding line heights to yLine.
                // So on the iteration, when I have 16px rect, one line fits,
                // I increase the height by 3px, it false-positively adds the second line
                // which fits the 16+3-height rectangle.
                // So force it to rollback to the previous state by invoking restoreState(),
                // so that it would add items from scratch.
                saveState();

                status = go(true);

                if(!ColumnText.hasMoreText(status)){
                    r = new AtomicIncreaseResult(bottomBefore - modifiableRectangle.getBottom(), GrowthResultType.NO_MORE_CONTENT);
                    finishOnNext();
                    break;
                }

                final float v2 = getYLine();

                if(v1 != v2){
                    r = new AtomicIncreaseResult(bottomBefore - modifiableRectangle.getBottom(),
                        GrowthResultType.NORMAL);
                    break;
                }

                restoreState();
            }

            return r;
        }

        private void finishOnNext() {
            finishOnNext = true;
        }
    }

    public ColumnTextBuilder flipPageForSpace(float height){
        if(getYLine() - height < iTextBuilder.document.bottom()){
            iTextBuilder.document.newPage();
            onPageFlip();
        }

        return this;
    }

    public void onPageFlip() {
        final RectangleBuilder rect = getCurrentRectangle()
            .setTop(iTextBuilder.document.top())
            .setBottom(iTextBuilder.document.bottom());

        setSimpleColumn(rect.get(), false);
    }
}
