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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Rectangle;

import java.util.List;

/**
 * User: chaschev
 * Date: 6/21/13
 */
public class RectangleBuilder<T extends RectangleBuilder> {
    protected Rectangle rectangle;

    public RectangleBuilder reuse(Rectangle rectangle){
        this.rectangle = rectangle;
        return this;
    }

    public RectangleBuilder copyPositionsFrom(Rectangle other){
        rectangle.setLeft(other.getLeft());
        rectangle.setRight(other.getRight());
        rectangle.setTop(other.getTop());
        rectangle.setBottom(other.getBottom());

        return this;
    }

    public RectangleBuilder copyPositionsFrom(RectangleBuilder other){
        rectangle.setLeft(other.getLeft());
        rectangle.setRight(other.getRight());
        rectangle.setTop(other.getTop());
        rectangle.setBottom(other.getBottom());

        return this;
    }

    public boolean process(ElementListener listener) {
        return rectangle.process(listener);
    }

    public float getLeft() {
        return rectangle.getLeft();
    }

    public float getBorderWidthLeft() {
        return rectangle.getBorderWidthLeft();
    }

    public float getBottom() {
        return rectangle.getBottom();
    }

    public boolean isContent() {
        return rectangle.isContent();
    }

    public float getRight(float margin) {
        return rectangle.getRight(margin);
    }

    public void cloneNonPositionParameters(Rectangle rect) {
        rectangle.cloneNonPositionParameters(rect);
    }

    public Rectangle rotate() {
        return rectangle.rotate();
    }

    public BaseColor getBorderColor() {
        return rectangle.getBorderColor();
    }

    public BaseColor getBorderColorRight() {
        return rectangle.getBorderColorRight();
    }

    public void setBorderWidthRight(float borderWidthRight) {
        rectangle.setBorderWidthRight(borderWidthRight);
    }

    public float getBorderWidthTop() {
        return rectangle.getBorderWidthTop();
    }

    public List<Chunk> getChunks() {
        return rectangle.getChunks();
    }

    public void setRight(float urx) {
        rectangle.setRight(urx);
    }

    public BaseColor getBorderColorLeft() {
        return rectangle.getBorderColorLeft();
    }

    public boolean isNestable() {
        return rectangle.isNestable();
    }

    public T setBorderColorBottom(BaseColor borderColorBottom) {
        rectangle.setBorderColorBottom(borderColorBottom);
        return (T) this;
    }

    public boolean isUseVariableBorders() {
        return rectangle.isUseVariableBorders();
    }

    public void disableBorderSide(int side) {
        rectangle.disableBorderSide(side);
    }

    public T setBackgroundColor(BaseColor backgroundColor) {
        rectangle.setBackgroundColor(backgroundColor);
        return (T) this;
    }

    public BaseColor getBorderColorTop() {
        return rectangle.getBorderColorTop();
    }

    public void setBorderWidth(float borderWidth) {
        rectangle.setBorderWidth(borderWidth);
    }

    public float getRight() {
        return rectangle.getRight();
    }

    public float getHeight() {
        return rectangle.getHeight();
    }

    public float getBorderWidth() {
        return rectangle.getBorderWidth();
    }

    public int type() {
        return rectangle.type();
    }

    public BaseColor getBackgroundColor() {
        return rectangle.getBackgroundColor();
    }

    public float getBorderWidthBottom() {
        return rectangle.getBorderWidthBottom();
    }

    public void setGrayFill(float value) {
        rectangle.setGrayFill(value);
    }

    public boolean hasBorders() {
        return rectangle.hasBorders();
    }

    public BaseColor getBorderColorBottom() {
        return rectangle.getBorderColorBottom();
    }

    public void setLeft(float llx) {
        rectangle.setLeft(llx);
    }

    public int getBorder() {
        return rectangle.getBorder();
    }

    public void setBorderWidthLeft(float borderWidthLeft) {
        rectangle.setBorderWidthLeft(borderWidthLeft);
    }

    public Rectangle rectangle(float top, float bottom) {
        return rectangle.rectangle(top, bottom);
    }

    public T setBorderWidthTop(float borderWidthTop) {
        rectangle.setBorderWidthTop(borderWidthTop);
        return (T) this;
    }

    public float getBottom(float margin) {
        return rectangle.getBottom(margin);
    }

    public RectangleBuilder setBorder(int border) {
        rectangle.setBorder(border);
        return this;
    }

    public RectangleBuilder setBorderColorTop(BaseColor borderColorTop) {
        rectangle.setBorderColorTop(borderColorTop);
        return this;
    }

    public boolean hasBorder(int type) {
        return rectangle.hasBorder(type);
    }

    public void setRotation(int rotation) {
        rectangle.setRotation(rotation);
    }

    public float getWidth() {
        return rectangle.getWidth();
    }

    public RectangleBuilder<T> setBottom(float lly) {
        rectangle.setBottom(lly);
        return this;
    }

    public T setBorderWidthBottom(float borderWidthBottom) {
        rectangle.setBorderWidthBottom(borderWidthBottom);
        return (T) this;
    }

    public float getTop(float margin) {
        return rectangle.getTop(margin);
    }

    public RectangleBuilder<T> setTop(float ury) {
        rectangle.setTop(ury);
        return this;
    }

    public void softCloneNonPositionParameters(Rectangle rect) {
        rectangle.softCloneNonPositionParameters(rect);
    }

    public void setBorderColorLeft(BaseColor borderColorLeft) {
        rectangle.setBorderColorLeft(borderColorLeft);
    }

    public float getTop() {
        return rectangle.getTop();
    }

    public void normalize() {
        rectangle.normalize();
    }

    public void enableBorderSide(int side) {
        rectangle.enableBorderSide(side);
    }

    public void setBorderColorRight(BaseColor borderColorRight) {
        rectangle.setBorderColorRight(borderColorRight);
    }

    public int getRotation() {
        return rectangle.getRotation();
    }

    public float getBorderWidthRight() {
        return rectangle.getBorderWidthRight();
    }

    public float getGrayFill() {
        return rectangle.getGrayFill();
    }

    public float getLeft(float margin) {
        return rectangle.getLeft(margin);
    }

    public void setUseVariableBorders(boolean useVariableBorders) {
        rectangle.setUseVariableBorders(useVariableBorders);
    }

    public RectangleBuilder setBorderColor(BaseColor borderColor) {
        rectangle.setBorderColor(borderColor);
        return this;
    }

    public Rectangle get() {
        return rectangle;
    }

    public RectangleBuilder<T> growBottom(float growBy) {
        rectangle.setBottom(rectangle.getBottom() - growBy);
        return this;
    }

    public RectangleBuilder<T> adjustBottom(float newRectHeight) {
        rectangle.setBottom(rectangle.getTop() - newRectHeight);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("Rectangle{");
        sb.append(rectangle.getLeft());
        sb.append(", ");
        sb.append(rectangle.getBottom());
        sb.append(" -> ");
        sb.append(rectangle.getRight());
        sb.append(", ");
        sb.append(rectangle.getTop());
        sb.append("}");

        return sb.toString();
    }
}
