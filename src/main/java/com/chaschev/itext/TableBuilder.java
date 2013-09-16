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

import com.google.common.base.Predicate;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* User: chaschev
* Date: 6/21/13
*/
public class TableBuilder {
    public final ITextBuilder owner;

    PdfPTable table;

    protected final CellBuilder emptyCell = new CellBuilder(this).reuse(new PdfPCell());

    protected CellBuilder reusableCellBuilder = new CellBuilder(this);

    protected Predicate<CellBuilder> defaultCellState = new Predicate<CellBuilder>() {
        @Override
        public boolean apply(CellBuilder cellBuilder) {
            if(!owner.drawBorders){
                cellBuilder.setBorder(Rectangle.NO_BORDER);
            }
            return true;
        }
    };

    TableBuilder(ITextBuilder owner, float... relativeWidths) {
        this.owner = owner;

        this.table = new PdfPTable(relativeWidths);
        setDefaultCellState(defaultCellState);
    }

    public TableBuilder setDefaultCellState(Predicate<CellBuilder> defaultCellState) {
        this.defaultCellState = defaultCellState;
        defaultCellState.apply(emptyCell);
        defaultCellState.apply(cell(table.getDefaultCell()));
        return this;
    }

    public CellBuilder cell(PdfPCell cell) {
        return reusableCellBuilder.reuse(cell);
    }

    public TableBuilder splitRows(ITextBuilder.TableSplit tableSplit) {
        switch (tableSplit){
            case DEFAULT:
                table.setSplitRows(true);
                table.setSplitLate(true);

                break;
            case SPLIT_ALL:
                table.setSplitRows(true);
                table.setSplitLate(false);
                break;
            default:
                throw new UnsupportedOperationException("todo:" + tableSplit);
        }

        return this;
    }

    public CellBuilder reuseCellBuilder() {
        return prepareCellBuilder(reusableCellBuilder);
    }

    public CellBuilder prepareCellBuilder(CellBuilder cb){
        cb.reuse(new PdfPCell());
        defaultCellState.apply(cb);

        return cb;
    }


    public final CellBuilder reuseCellBuilder(Phrase elements){
        return cell(elements);
    }

    public CellBuilder cell(PdfPTable table) {
        reusableCellBuilder.reuse(new PdfPCell(table));
        defaultCellState.apply(reusableCellBuilder);

        return reusableCellBuilder;
    }

    public CellBuilder cell(Phrase elements) {
        reusableCellBuilder.reuse(new PdfPCell(elements));
        defaultCellState.apply(reusableCellBuilder);

        return reusableCellBuilder;
    }

    public CellBuilder cell(Image elements) {
        reusableCellBuilder.reuse(new PdfPCell(elements));
        defaultCellState.apply(reusableCellBuilder);

        return reusableCellBuilder;
    }

    public CellBuilder cell(Element element) {
        final PdfPCell cell = new PdfPCell();
        reusableCellBuilder
            .reuse(cell)
            .addElement(element);

        defaultCellState.apply(reusableCellBuilder);

        return reusableCellBuilder;
    }

    ////////////////////////// DELEGATES /////////////////////////////////

    public int getHeaderRows() {
        return table.getHeaderRows();
    }

    public void setSkipLastFooter(boolean skipLastFooter) {
        table.setSkipLastFooter(skipLastFooter);
    }

    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        return table.writeSelectedRows(rowStart, rowEnd, xPos, yPos, canvas);
    }

    public PdfPCell getDefaultCell() {
        return table.getDefaultCell();
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        table.setAccessibleAttribute(key, value);
    }

    public void setRunDirection(int runDirection) {
        table.setRunDirection(runDirection);
    }

    public PdfPRow getRow(int idx) {
        return table.getRow(idx);
    }

    public TableBuilder setLockedWidth(boolean lockedWidth) {
        table.setLockedWidth(lockedWidth);
        return this;
    }

    public static PdfContentByte[] beginWritingRows(PdfContentByte canvas) {
        return PdfPTable.beginWritingRows(canvas);
    }

    public void setExtendLastRow(boolean extendLastRows, boolean extendFinalRow) {
        table.setExtendLastRow(extendLastRows, extendFinalRow);
    }

    public boolean isExtendLastRow(boolean newPageFollows) {
        return table.isExtendLastRow(newPageFollows);
    }

    public void setExtendLastRow(boolean extendLastRows) {
        table.setExtendLastRow(extendLastRows);
    }

    public void setBreakPoints(int... breakPoints) {
        table.setBreakPoints(breakPoints);
    }

    public TableBuilder addCell(Image image) {
        table.addCell(image);
        return this;
    }

    public void setSkipFirstHeader(boolean skipFirstHeader) {
        table.setSkipFirstHeader(skipFirstHeader);
    }

    public TableBuilder addSeparatorCell(float height) {
        addCell(
            reuseCellBuilder()
                .setFixedHeight(height)
                .build());
        return this;
    }



    public boolean isNestable() {
        return table.isNestable();
    }

    public PdfPTableBody getBody() {
        return table.getBody();
    }

    public float getRowHeight(int idx) {
        return table.getRowHeight(idx);
    }

    public boolean isSplitRows() {
        return table.isSplitRows();
    }

    public float getFooterHeight() {
        return table.getFooterHeight();
    }

    public void setWidthPercentage(float widthPercentage) {
        table.setWidthPercentage(widthPercentage);
    }

    public void completeRow() {
        table.completeRow();
    }

    public void setTableEvent(PdfPTableEvent event) {
        table.setTableEvent(event);
    }

    public void keepRowsTogether(int start) {
        table.keepRowsTogether(start);
    }

    public void setSplitRows(boolean splitRows) {
        table.setSplitRows(splitRows);
    }

    public float getTotalWidth() {
        return table.getTotalWidth();
    }

    public float getTotalHeight() {
        return table.getTotalHeight();
    }

    public void setSpacingBefore(float spacing) {
        table.setSpacingBefore(spacing);
    }

    public void setLoopCheck(boolean loopCheck) {
        table.setLoopCheck(loopCheck);
    }

    public void keepRowsTogether(int start, int end) {
        table.keepRowsTogether(start, end);
    }

    public void setWidths(int[] relativeWidths) throws DocumentException {
        table.setWidths(relativeWidths);
    }

    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
        return table.writeSelectedRows(rowStart, rowEnd, xPos, yPos, canvases);
    }

    public int getFooterRows() {
        return table.getFooterRows();
    }

    public void setHeaderRows(int headerRows) {
        table.setHeaderRows(headerRows);
    }

    public PdfPTableFooter getFooter() {
        return table.getFooter();
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        table.setHorizontalAlignment(horizontalAlignment);
    }

    public void setWidths(float[] relativeWidths) throws DocumentException {
        table.setWidths(relativeWidths);
    }

    public int size() {
        return table.size();
    }

    public ArrayList<PdfPRow> getRows(int start, int end) {
        return table.getRows(start, end);
    }

    public float getSpacingBefore() {
        return table.getSpacingBefore();
    }

    public void setKeepTogether(boolean keepTogether) {
        table.setKeepTogether(keepTogether);
    }

    public boolean getKeepTogether() {
        return table.getKeepTogether();
    }

    public boolean deleteRow(int rowNumber) {
        return table.deleteRow(rowNumber);
    }

    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas, boolean reusable) {
        return table.writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvas, reusable);
    }

    public boolean process(ElementListener listener) {
        return table.process(listener);
    }

    public void setHeadersInEvent(boolean headersInEvent) {
        table.setHeadersInEvent(headersInEvent);
    }

    public float getSpacingAfter() {
        return table.getSpacingAfter();
    }

    public boolean isLockedWidth() {
        return table.isLockedWidth();
    }

    public boolean hasRowspan(int rowIdx) {
        return table.hasRowspan(rowIdx);
    }

    public float getWidthPercentage() {
        return table.getWidthPercentage();
    }

    public boolean isSkipLastFooter() {
        return table.isSkipLastFooter();
    }

    public List<Chunk> getChunks() {
        return table.getChunks();
    }

    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
        return table.writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases);
    }

    public boolean isComplete() {
        return table.isComplete();
    }

    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases, boolean reusable) {
        return table.writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases, reusable);
    }

    public boolean isHeadersInEvent() {
        return table.isHeadersInEvent();
    }

    public float calculateHeights() {
        return table.calculateHeights();
    }

    public ArrayList<PdfPRow> getRows() {
        return table.getRows();
    }

    public int getNumberOfColumns() {
        return table.getNumberOfColumns();
    }

    public void flushContent() {
        table.flushContent();
    }

    public void setTotalWidth(float[] columnWidth) throws DocumentException {
        table.setTotalWidth(columnWidth);
    }

    public PdfPTableHeader getHeader() {
        return table.getHeader();
    }

    public int type() {
        return table.type();
    }

    public void deleteBodyRows() {
        table.deleteBodyRows();
    }

    public void setWidthPercentage(float[] columnWidth, Rectangle pageSize) throws DocumentException {
        table.setWidthPercentage(columnWidth, pageSize);
    }

    public int getRunDirection() {
        return table.getRunDirection();
    }

    public void setSpacingAfter(float spacing) {
        table.setSpacingAfter(spacing);
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        return table.getAccessibleAttribute(key);
    }

    public void normalizeHeadersFooters() {
        table.normalizeHeadersFooters();
    }

    public int getLastCompletedRowIndex() {
        return table.getLastCompletedRowIndex();
    }

    public void setComplete(boolean complete) {
        table.setComplete(complete);
    }

    public boolean isLoopCheck() {
        return table.isLoopCheck();
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return table.getAccessibleAttributes();
    }

    public TableBuilder addCell(Phrase phrase) {
        return addCell(phrase, -1);
    }

    public TableBuilder addCell(Phrase phrase, float indent) {
        return addCell(phrase, indent, indent);
    }

    public TableBuilder addCell(Phrase phrase, float indent, float fwIndent) {
        CellBuilder cb = cell(phrase);

        if(indent >= 0){
            cb.setIndent(indent);
            cb.setFollowingIndent(fwIndent);
        }

        table.addCell(cb.build());
        return this;
    }

    public TableBuilder addCell(com.itextpdf.text.List list) {
        return addCell(list, -1,-1);
    }

    public TableBuilder addCell(com.itextpdf.text.List list, float indent, float fwIndent) {
        CellBuilder cb = cell(list);

        if(indent >= 0){
            cb.setIndent(indent);
            cb.setFollowingIndent(fwIndent);
        }

        table.addCell(cb.build());
        return this;
    }

    public int getHorizontalAlignment() {
        return table.getHorizontalAlignment();
    }

    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        return table.writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvas);
    }

    public float getRowspanHeight(int rowIndex, int cellIndex) {
        return table.getRowspanHeight(rowIndex, cellIndex);
    }

    public float spacingAfter() {
        return table.spacingAfter();
    }

    public boolean deleteLastRow() {
        return table.deleteLastRow();
    }

    public static void endWritingRows(PdfContentByte[] canvases) {
        PdfPTable.endWritingRows(canvases);
    }

    public void setSplitLate(boolean splitLate) {
        table.setSplitLate(splitLate);
    }

    public PdfName getRole() {
        return table.getRole();
    }

    public boolean isSkipFirstHeader() {
        return table.isSkipFirstHeader();
    }

    public float[] getAbsoluteWidths() {
        return table.getAbsoluteWidths();
    }

    public boolean isExtendLastRow() {
        return table.isExtendLastRow();
    }

    public boolean isSplitLate() {
        return table.isSplitLate();
    }

    public void setRole(PdfName role) {
        table.setRole(role);
    }

    public void resetColumnCount(int newColCount) {
        table.resetColumnCount(newColCount);
    }

    public TableBuilder addCell(String text) {
        table.addCell(text);
        return this;
    }

    public TableBuilder addCell(PdfPCell cell) {
        table.addCell(cell);
        return this;
    }

    public TableBuilder addEmptyCell() {
        table.addCell(emptyCell.cell);
        return this;
    }

    public void keepRowsTogether(int[] rows) {
        table.keepRowsTogether(rows);
    }

    public PdfPTableEvent getTableEvent() {
        return table.getTableEvent();
    }

    public void setFooterRows(int footerRows) {
        table.setFooterRows(footerRows);
    }

    public boolean isContent() {
        return table.isContent();
    }

    public TableBuilder addCell(PdfPTable table) {
        return addCell(table, -1);
    }

    public TableBuilder addCell(PdfPTable table, float indent) {
        CellBuilder cb = cell(table);

        if(indent >= 0){
            cb.setIndent(indent);
        }

        this.table.addCell(cb.build());
        return this;
    }

    public TableBuilder setTotalWidth(float totalWidth) {
        table.setTotalWidth(totalWidth);
        return this;
    }

    public float getHeaderHeight() {
        return table.getHeaderHeight();
    }

    public float spacingBefore() {
        return table.spacingBefore();
    }

    public PdfPCell emptyCell() {
        return emptyCell.cell;
    }


    public PdfPTable build() {
        return table;
    }
}
