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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
* User: chaschev
* Date: 6/21/13
*/
public class CellBuilder extends RectangleBuilder<CellBuilder>{
    public TableBuilder owner;
    protected PdfPCell cell;

    CellBuilder(TableBuilder owner) {
        this.owner = owner;
    }

    public CellBuilder reuse(PdfPCell cell) {
        this.cell = cell;
        super.reuse(cell);

        return this;
    }

    public CellBuilder addElement(Element element) {
        cell.addElement(element);
        return this;
    }

    public Phrase getPhrase() {
        return cell.getPhrase();
    }

    public void setPhrase(Phrase phrase) {
        cell.setPhrase(phrase);
    }

    public int getHorizontalAlignment() {
        return cell.getHorizontalAlignment();
    }

    public CellBuilder setHorizontalAlignment(int horizontalAlignment) {
        cell.setHorizontalAlignment(horizontalAlignment);
        return this;
    }

    public int getVerticalAlignment() {
        return cell.getVerticalAlignment();
    }

    public void setVerticalAlignment(int verticalAlignment) {
        cell.setVerticalAlignment(verticalAlignment);
    }

    public float getEffectivePaddingLeft() {
        return cell.getEffectivePaddingLeft();
    }

    public float getPaddingLeft() {
        return cell.getPaddingLeft();
    }

    public void setPaddingLeft(float paddingLeft) {
        cell.setPaddingLeft(paddingLeft);
    }

    public float getEffectivePaddingRight() {
        return cell.getEffectivePaddingRight();
    }

    public float getPaddingRight() {
        return cell.getPaddingRight();
    }

    public void setPaddingRight(float paddingRight) {
        cell.setPaddingRight(paddingRight);
    }

    public float getEffectivePaddingTop() {
        return cell.getEffectivePaddingTop();
    }

    public float getPaddingTop() {
        return cell.getPaddingTop();
    }

    public CellBuilder setPaddingTop(float paddingTop) {
        cell.setPaddingTop(paddingTop);
        return this;
    }

    public float getEffectivePaddingBottom() {
        return cell.getEffectivePaddingBottom();
    }

    public float getPaddingBottom() {
        return cell.getPaddingBottom();
    }

    public void setPaddingBottom(float paddingBottom) {
        cell.setPaddingBottom(paddingBottom);
    }

    public void setPadding(float padding) {
        cell.setPadding(padding);
    }

    public boolean isUseBorderPadding() {
        return cell.isUseBorderPadding();
    }

    public void setUseBorderPadding(boolean use) {
        cell.setUseBorderPadding(use);
    }

    public void setLeading(float fixedLeading, float multipliedLeading) {
        cell.setLeading(fixedLeading, multipliedLeading);
    }

    public float getLeading() {
        return cell.getLeading();
    }

    public float getMultipliedLeading() {
        return cell.getMultipliedLeading();
    }

    public CellBuilder setIndent(float indent) {
        cell.setIndent(indent);
        return this;
    }

    public float getIndent() {
        return cell.getIndent();
    }

    public float getExtraParagraphSpace() {
        return cell.getExtraParagraphSpace();
    }

    public void setExtraParagraphSpace(float extraParagraphSpace) {
        cell.setExtraParagraphSpace(extraParagraphSpace);
    }

    public CellBuilder setFixedHeight(float fixedHeight) {
        cell.setFixedHeight(fixedHeight);
        return this;
    }

    public float getFixedHeight() {
        return cell.getFixedHeight();
    }

    public boolean hasFixedHeight() {
        return cell.hasFixedHeight();
    }

    public void setMinimumHeight(float minimumHeight) {
        cell.setMinimumHeight(minimumHeight);
    }

    public float getMinimumHeight() {
        return cell.getMinimumHeight();
    }

    public boolean hasMinimumHeight() {
        return cell.hasMinimumHeight();
    }

    public boolean isNoWrap() {
        return cell.isNoWrap();
    }

    public void setNoWrap(boolean noWrap) {
        cell.setNoWrap(noWrap);
    }

    public PdfPTable getTable() {
        return cell.getTable();
    }

    public int getColspan() {
        return cell.getColspan();
    }

    public CellBuilder setColspan(int colspan) {
        cell.setColspan(colspan);
        return this;
    }

    public int getRowspan() {
        return cell.getRowspan();
    }

    public void setRowspan(int rowspan) {
        cell.setRowspan(rowspan);
    }

    public CellBuilder setFollowingIndent(float indent) {
        cell.setFollowingIndent(indent);
        return this;
    }

    public float getFollowingIndent() {
        return cell.getFollowingIndent();
    }

    public void setRightIndent(float indent) {
        cell.setRightIndent(indent);
    }

    public float getRightIndent() {
        return cell.getRightIndent();
    }

    public float getSpaceCharRatio() {
        return cell.getSpaceCharRatio();
    }

    public void setSpaceCharRatio(float spaceCharRatio) {
        cell.setSpaceCharRatio(spaceCharRatio);
    }

    public void setRunDirection(int runDirection) {
        cell.setRunDirection(runDirection);
    }

    public int getRunDirection() {
        return cell.getRunDirection();
    }

    public Image getImage() {
        return cell.getImage();
    }

    public void setImage(Image image) {
        cell.setImage(image);
    }

    public PdfPCellEvent getCellEvent() {
        return cell.getCellEvent();
    }

    public void setCellEvent(PdfPCellEvent cellEvent) {
        cell.setCellEvent(cellEvent);
    }

    public int getArabicOptions() {
        return cell.getArabicOptions();
    }

    public void setArabicOptions(int arabicOptions) {
        cell.setArabicOptions(arabicOptions);
    }

    public boolean isUseAscender() {
        return cell.isUseAscender();
    }

    public void setUseAscender(boolean useAscender) {
        cell.setUseAscender(useAscender);
    }

    public boolean isUseDescender() {
        return cell.isUseDescender();
    }

    public void setUseDescender(boolean useDescender) {
        cell.setUseDescender(useDescender);
    }

    public ColumnText getColumn() {
        return cell.getColumn();
    }

    public List<Element> getCompositeElements() {
        return cell.getCompositeElements();
    }

    public void setColumn(ColumnText column) {
        cell.setColumn(column);
    }

    public int getRotation() {
        return cell.getRotation();
    }

    public CellBuilder setBorderWidthTop(float borderWidthTop) {
        cell.setBorderWidthTop(borderWidthTop);
        return this;
    }

    public CellBuilder setBorder(int border) {
        cell.setBorder(border);
        return this;
    }

    public CellBuilder setBorderColorTop(BaseColor borderColorTop) {
        cell.setBorderColorTop(borderColorTop);
        return this;
    }

    public BaseColor getBorderColorBottom() {
        return cell.getBorderColorBottom();
    }

    public CellBuilder setBorderColorBottom(BaseColor borderColorBottom) {
        cell.setBorderColorBottom(borderColorBottom);
        return this;
    }

    public BaseColor getBorderColor() {
        return cell.getBorderColor();
    }

    public CellBuilder setBorderColor(BaseColor borderColor) {
        cell.setBorderColor(borderColor);
        return this;
    }

    public BaseColor getBorderColorLeft() {
        return cell.getBorderColorLeft();
    }

    public void setBorderColorLeft(BaseColor borderColorLeft) {
        cell.setBorderColorLeft(borderColorLeft);
    }

    public BaseColor getBorderColorRight() {
        return cell.getBorderColorRight();
    }

    public void setBorderColorRight(BaseColor borderColorRight) {
        cell.setBorderColorRight(borderColorRight);
    }

    public BaseColor getBorderColorTop() {
        return cell.getBorderColorTop();
    }

    public void setRotation(int rotation) {
        cell.setRotation(rotation);
    }

    public float getMaxHeight() {
        return cell.getMaxHeight();
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        return cell.getAccessibleAttribute(key);
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        cell.setAccessibleAttribute(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return cell.getAccessibleAttributes();
    }

    public PdfName getRole() {
        return cell.getRole();
    }

    public void setRole(PdfName role) {
        cell.setRole(role);
    }

    public UUID getId() {
        return cell.getId();
    }

    public void setId(UUID id) {
        cell.setId(id);
    }

    public void addHeader(PdfPHeaderCell header) {
        cell.addHeader(header);
    }

    public ArrayList<PdfPHeaderCell> getHeaders() {
        return cell.getHeaders();
    }

    public CellBuilder alignRight() {
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return this;
    }

    public CellBuilder alignBottom() {
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        return this;
    }

    public PdfPCell build() {
        return cell;
    }

    public CellBuilder topLine(float height, BaseColor color) {
        return this.setBorder(Rectangle.TOP)
            .setBorderColorTop(color)
            .setBorderWidthTop(0.2f)
            .setPaddingTop(height);
    }
}
