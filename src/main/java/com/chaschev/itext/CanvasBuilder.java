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

import com.itextpdf.awt.FontMapper;
import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.awt.*;
import java.awt.print.PrinterJob;
import java.util.ArrayList;

/**
 * User: chaschev
 * Date: 9/9/13
 */
public class CanvasBuilder {
    PdfContentByte canvas;

    public CanvasBuilder(PdfContentByte canvas) {
        this.canvas = canvas;
    }

    public CanvasBuilder drawGrayRectangle(Rectangle rectangle, BaseColor color){
        return saveState()
            .rectangle(rectangle.getLeft(), rectangle.getBottom(), rectangle.getWidth(), rectangle.getHeight())
            .setColorStroke(color)
            .setLineWidth(0.1f)
            .stroke()
            .restoreState();
    }

    public void fill() {
        canvas.fill();
    }

    public void addTemplate(PdfTemplate template, AffineTransform transform) {
        canvas.addTemplate(template, transform);
    }

    public void setTextMatrix(java.awt.geom.AffineTransform transform) {
        canvas.setTextMatrix(transform);
    }

    public void addTemplate(PdfTemplate template, float x, float y) {
        canvas.addTemplate(template, x, y);
    }

    public void setRGBColorFill(int red, int green, int blue) {
        canvas.setRGBColorFill(red, green, blue);
    }

    public void addTemplate(PdfTemplate template, java.awt.geom.AffineTransform transform) {
        canvas.addTemplate(template, transform);
    }

    public void concatCTM(float a, float b, float c, float d, float e, float f) {
        canvas.concatCTM(a, b, c, d, e, f);
    }

    public void remoteGoto(String filename, int page, float llx, float lly, float urx, float ury) {
        canvas.remoteGoto(filename, page, llx, lly, urx, ury);
    }

    public void circle(float x, float y, float r) {
        canvas.circle(x, y, r);
    }

    public void setPatternFill(PdfPatternPainter p, BaseColor color, float tint) {
        canvas.setPatternFill(p, color, tint);
    }

    public PdfTemplate createTemplate(float width, float height) {
        return canvas.createTemplate(width, height);
    }

    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f, boolean tagContent) {
        canvas.addTemplate(template, a, b, c, d, e, f, tagContent);
    }

    public void addPSXObject(PdfPSXObject psobject) {
        canvas.addPSXObject(psobject);
    }

    public Graphics2D createGraphics(float width, float height, FontMapper fontMapper) {
        return canvas.createGraphics(width, height, fontMapper);
    }

    public void setPatternFill(PdfPatternPainter p) {
        canvas.setPatternFill(p);
    }

    public void newlineShowText(float wordSpacing, float charSpacing, String text) {
        canvas.newlineShowText(wordSpacing, charSpacing, text);
    }

    public boolean localDestination(String name, PdfDestination destination) {
        return canvas.localDestination(name, destination);
    }

    public void setLineDash(float phase) {
        canvas.setLineDash(phase);
    }

    public void ellipse(float x1, float y1, float x2, float y2) {
        canvas.ellipse(x1, y1, x2, y2);
    }

    public void closePath() {
        canvas.closePath();
    }

    public void setRGBColorStroke(int red, int green, int blue) {
        canvas.setRGBColorStroke(red, green, blue);
    }

    public CanvasBuilder restoreState() {
        canvas.restoreState();
        return this;
    }

    public void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
        canvas.setCMYKColorFill(cyan, magenta, yellow, black);
    }

    public float getWordSpacing() {
        return canvas.getWordSpacing();
    }

    public void setPatternFill(PdfPatternPainter p, BaseColor color) {
        canvas.setPatternFill(p, color);
    }

    public void rectangle(Rectangle rectangle) {
        canvas.rectangle(rectangle);
    }

    public void resetCMYKColorStroke() {
        canvas.resetCMYKColorStroke();
    }

    public Graphics2D createGraphicsShapes(float width, float height) {
        return canvas.createGraphicsShapes(width, height);
    }

    public float getXTLM() {
        return canvas.getXTLM();
    }

    public void setLineDash(float unitsOn, float unitsOff, float phase) {
        canvas.setLineDash(unitsOn, unitsOff, phase);
    }

    public ByteBuffer getInternalBuffer() {
        return canvas.getInternalBuffer();
    }

    public void roundRectangle(float x, float y, float w, float h, float r) {
        canvas.roundRectangle(x, y, w, h, r);
    }

    public void curveTo(float x2, float y2, float x3, float y3) {
        canvas.curveTo(x2, y2, x3, y3);
    }

    public void moveTextWithLeading(float x, float y) {
        canvas.moveTextWithLeading(x, y);
    }

    public void add(PdfContentByte other) {
        canvas.add(other);
    }

    public void clip() {
        canvas.clip();
    }

    public void endMarkedContentSequence() {
        canvas.endMarkedContentSequence();
    }

    public byte[] toPdf(PdfWriter writer) {
        return canvas.toPdf(writer);
    }

    public void setColorFill(PdfSpotColor sp, float tint) {
        canvas.setColorFill(sp, tint);
    }

    public void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
        canvas.setCMYKColorStroke(cyan, magenta, yellow, black);
    }

    public void addTemplate(PdfTemplate template, AffineTransform transform, boolean tagContent) {
        canvas.addTemplate(template, transform, tagContent);
    }

    public CanvasBuilder setGrayStroke(float gray) {
        canvas.setGrayStroke(gray);
        return this;
    }

    public void setLiteral(String s) {
        canvas.setLiteral(s);
    }

    public Graphics2D createGraphicsShapes(float width, float height, boolean convertImagesToJPEG, float quality) {
        return canvas.createGraphicsShapes(width, height, convertImagesToJPEG, quality);
    }

    public PdfContentByte getDuplicate(boolean inheritGraphicState) {
        return canvas.getDuplicate(inheritGraphicState);
    }

    public void showTextKerned(String text) {
        canvas.showTextKerned(text);
    }

    public void reset(boolean validateContent) {
        canvas.reset(validateContent);
    }

    public void addImage(com.itextpdf.text.Image image, float a, float b, float c, float d, float e, float f, boolean inlineImage) throws DocumentException {
        canvas.addImage(image, a, b, c, d, e, f, inlineImage);
    }

    public void showTextAlignedKerned(int alignment, String text, float x, float y, float rotation) {
        canvas.showTextAlignedKerned(alignment, text, x, y, rotation);
    }

    public void setRGBColorStrokeF(float red, float green, float blue) {
        canvas.setRGBColorStrokeF(red, green, blue);
    }

    public void moveTo(float x, float y) {
        canvas.moveTo(x, y);
    }

    public void resetRGBColorFill() {
        canvas.resetRGBColorFill();
    }

    public void closePathStroke() {
        canvas.closePathStroke();
    }

    public void setTextMatrix(float a, float b, float c, float d, float x, float y) {
        canvas.setTextMatrix(a, b, c, d, x, y);
    }

    public PdfPatternPainter createPattern(float width, float height) {
        return canvas.createPattern(width, height);
    }

    public void setColorStroke(PdfSpotColor sp, float tint) {
        canvas.setColorStroke(sp, tint);
    }

    public void showText(String text) {
        canvas.showText(text);
    }

    public void eoClip() {
        canvas.eoClip();
    }

    public void addTemplate(PdfTemplate template, float x, float y, boolean tagContent) {
        canvas.addTemplate(template, x, y, tagContent);
    }

    public void beginText() {
        canvas.beginText();
    }

    public void beginLayer(PdfOCG layer) {
        canvas.beginLayer(layer);
    }

    public void localGoto(String name, float llx, float lly, float urx, float ury) {
        canvas.localGoto(name, llx, lly, urx, ury);
    }

    public void setMiterLimit(float miterLimit) {
        canvas.setMiterLimit(miterLimit);
    }

    public void eoFillStroke() {
        canvas.eoFillStroke();
    }

    public void setTextMatrix(AffineTransform transform) {
        canvas.setTextMatrix(transform);
    }

    public void closeMCBlock(IAccessibleElement element) {
        canvas.closeMCBlock(element);
    }

    public void setTextRenderingMode(int rendering) {
        canvas.setTextRenderingMode(rendering);
    }

    public void newlineShowText(String text) {
        canvas.newlineShowText(text);
    }

    public Graphics2D createPrinterGraphicsShapes(float width, float height, boolean convertImagesToJPEG, float quality, PrinterJob printerJob) {
        return canvas.createPrinterGraphicsShapes(width, height, convertImagesToJPEG, quality, printerJob);
    }

    public Graphics2D createPrinterGraphics(float width, float height, PrinterJob printerJob) {
        return canvas.createPrinterGraphics(width, height, printerJob);
    }

    public void setTextRise(float rise) {
        canvas.setTextRise(rise);
    }

    public void setHorizontalScaling(float scale) {
        canvas.setHorizontalScaling(scale);
    }

    public void setWordSpacing(float wordSpace) {
        canvas.setWordSpacing(wordSpace);
    }

    public void setColorFill(BaseColor color) {
        canvas.setColorFill(color);
    }

    public void drawButton(float llx, float lly, float urx, float ury, String text, BaseFont bf, float size) {
        canvas.drawButton(llx, lly, urx, ury, text, bf, size);
    }

    public void showText(PdfTextArray text) {
        canvas.showText(text);
    }

    public PdfPatternPainter createPattern(float width, float height, float xstep, float ystep) {
        return canvas.createPattern(width, height, xstep, ystep);
    }

    public float getHorizontalScaling() {
        return canvas.getHorizontalScaling();
    }

    public void variableRectangle(Rectangle rect) {
        canvas.variableRectangle(rect);
    }

    public Graphics2D createGraphics(float width, float height, boolean convertImagesToJPEG, float quality) {
        return canvas.createGraphics(width, height, convertImagesToJPEG, quality);
    }

    public void curveFromTo(float x1, float y1, float x3, float y3) {
        canvas.curveFromTo(x1, y1, x3, y3);
    }

    public float getCharacterSpacing() {
        return canvas.getCharacterSpacing();
    }

    public void setShadingFill(PdfShadingPattern shading) {
        canvas.setShadingFill(shading);
    }

    public PdfPatternPainter createPattern(float width, float height, BaseColor color) {
        return canvas.createPattern(width, height, color);
    }

    public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
        canvas.setCMYKColorFillF(cyan, magenta, yellow, black);
    }

    public void transform(java.awt.geom.AffineTransform af) {
        canvas.transform(af);
    }

    public void closePathEoFillStroke() {
        canvas.closePathEoFillStroke();
    }

    public void fillStroke() {
        canvas.fillStroke();
    }

    public Graphics2D createGraphics(float width, float height, FontMapper fontMapper, boolean convertImagesToJPEG, float quality) {
        return canvas.createGraphics(width, height, fontMapper, convertImagesToJPEG, quality);
    }

    public void setGState(PdfGState gstate) {
        canvas.setGState(gstate);
    }

    public boolean isTagged() {
        return canvas.isTagged();
    }

    public void setLineDash(float unitsOn, float phase) {
        canvas.setLineDash(unitsOn, phase);
    }

    public CanvasBuilder saveState() {
        canvas.saveState();
        return this;
    }

    public PdfContentByte getDuplicate() {
        return canvas.getDuplicate();
    }

    public void drawTextField(float llx, float lly, float urx, float ury) {
        canvas.drawTextField(llx, lly, urx, ury);
    }

    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        canvas.curveTo(x1, y1, x2, y2, x3, y3);
    }

    public Graphics2D createPrinterGraphics(float width, float height, boolean convertImagesToJPEG, float quality, PrinterJob printerJob) {
        return canvas.createPrinterGraphics(width, height, convertImagesToJPEG, quality, printerJob);
    }

    public void resetRGBColorStroke() {
        canvas.resetRGBColorStroke();
    }

    public Graphics2D createPrinterGraphics(float width, float height, FontMapper fontMapper, boolean convertImagesToJPEG, float quality, PrinterJob printerJob) {
        return canvas.createPrinterGraphics(width, height, fontMapper, convertImagesToJPEG, quality, printerJob);
    }

    public void endText() {
        canvas.endText();
    }

    public void resetGrayStroke() {
        canvas.resetGrayStroke();
    }

    public void closePathFillStroke() {
        canvas.closePathFillStroke();
    }

    public void setPatternStroke(PdfPatternPainter p, BaseColor color, float tint) {
        canvas.setPatternStroke(p, color, tint);
    }

    public void newPath() {
        canvas.newPath();
    }

    public void remoteGoto(String filename, String name, float llx, float lly, float urx, float ury) {
        canvas.remoteGoto(filename, name, llx, lly, urx, ury);
    }

    public void setDefaultColorspace(PdfName name, PdfObject obj) {
        canvas.setDefaultColorspace(name, obj);
    }

    public void setLineDash(float[] array, float phase) {
        canvas.setLineDash(array, phase);
    }

    public void beginMarkedContentSequence(PdfName tag) {
        canvas.beginMarkedContentSequence(tag);
    }

    public PdfAppearance createAppearance(float width, float height) {
        return canvas.createAppearance(width, height);
    }

    public void setShadingStroke(PdfShadingPattern shading) {
        canvas.setShadingStroke(shading);
    }

    public float getEffectiveStringWidth(String text, boolean kerned) {
        return canvas.getEffectiveStringWidth(text, kerned);
    }

    public void addImage(Image image) throws DocumentException {
        canvas.addImage(image);
    }

    public void beginMarkedContentSequence(PdfStructureElement struc) {
        canvas.beginMarkedContentSequence(struc);
    }

    public void drawRadioField(float llx, float lly, float urx, float ury, boolean on) {
        canvas.drawRadioField(llx, lly, urx, ury, on);
    }

    public void lineTo(float x, float y) {
        canvas.lineTo(x, y);
    }

    public void setFontAndSize(BaseFont bf, float size) {
        canvas.setFontAndSize(bf, size);
    }

    public void setLineCap(int style) {
        canvas.setLineCap(style);
    }

    public void resetGrayFill() {
        canvas.resetGrayFill();
    }

    public void arc(float x1, float y1, float x2, float y2, float startAng, float extent) {
        canvas.arc(x1, y1, x2, y2, startAng, extent);
    }

    public void openMCBlock(IAccessibleElement element) {
        canvas.openMCBlock(element);
    }

    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        canvas.addImage(image, a, b, c, d, e, f);
    }

    public void addOutline(PdfOutline outline, String name) {
        canvas.addOutline(outline, name);
    }

    public Graphics2D createGraphics(float width, float height) {
        return canvas.createGraphics(width, height);
    }

    public void setGrayFill(float gray) {
        canvas.setGrayFill(gray);
    }

    public Graphics2D createPrinterGraphicsShapes(float width, float height, PrinterJob printerJob) {
        return canvas.createPrinterGraphicsShapes(width, height, printerJob);
    }

    public static PdfTextArray getKernArray(String text, BaseFont font) {
        return PdfContentByte.getKernArray(text, font);
    }

    public Graphics2D createPrinterGraphics(float width, float height, FontMapper fontMapper, PrinterJob printerJob) {
        return canvas.createPrinterGraphics(width, height, fontMapper, printerJob);
    }

    public void concatCTM(java.awt.geom.AffineTransform transform) {
        canvas.concatCTM(transform);
    }

    public void setLiteral(char c) {
        canvas.setLiteral(c);
    }

    public void resetCMYKColorFill() {
        canvas.resetCMYKColorFill();
    }

    public void reset() {
        canvas.reset();
    }

    public CanvasBuilder rectangle(float x, float y, float w, float h) {
        canvas.rectangle(x, y, w, h);
        return this;
    }

    public void paintShading(PdfShading shading) {
        canvas.paintShading(shading);
    }

    public PdfPatternPainter createPattern(float width, float height, float xstep, float ystep, BaseColor color) {
        return canvas.createPattern(width, height, xstep, ystep, color);
    }

    public void setPatternStroke(PdfPatternPainter p, BaseColor color) {
        canvas.setPatternStroke(p, color);
    }

    public float getLeading() {
        return canvas.getLeading();
    }

    public void paintShading(PdfShadingPattern shading) {
        canvas.paintShading(shading);
    }

    public CanvasBuilder setLineWidth(float w) {
        canvas.setLineWidth(w);
        return this;
    }

    public void setLiteral(float n) {
        canvas.setLiteral(n);
    }

    public void showTextAligned(int alignment, String text, float x, float y, float rotation) {
        canvas.showTextAligned(alignment, text, x, y, rotation);
    }

    public void concatCTM(AffineTransform transform) {
        canvas.concatCTM(transform);
    }

    public void sanityCheck() {
        canvas.sanityCheck();
    }

    public static ArrayList<float[]> bezierArc(float x1, float y1, float x2, float y2, float startAng, float extent) {
        return PdfContentByte.bezierArc(x1, y1, x2, y2, startAng, extent);
    }

    public void setLineJoin(int style) {
        canvas.setLineJoin(style);
    }

    public void moveText(float x, float y) {
        canvas.moveText(x, y);
    }

    public float getYTLM() {
        return canvas.getYTLM();
    }

    public PdfWriter getPdfWriter() {
        return canvas.getPdfWriter();
    }

    public void addImage(Image image, java.awt.geom.AffineTransform transform) throws DocumentException {
        canvas.addImage(image, transform);
    }

    public PdfOutline getRootOutline() {
        return canvas.getRootOutline();
    }

    public void beginMarkedContentSequence(PdfName tag, PdfDictionary property, boolean inline) {
        canvas.beginMarkedContentSequence(tag, property, inline);
    }

    public CanvasBuilder setColorStroke(BaseColor color) {
        canvas.setColorStroke(color);
        return this;
    }

    public void setLeading(float leading) {
        canvas.setLeading(leading);
    }

    public void setTextMatrix(float x, float y) {
        canvas.setTextMatrix(x, y);
    }

    public void transform(AffineTransform af) {
        canvas.transform(af);
    }

    public void setAction(PdfAction action, float llx, float lly, float urx, float ury) {
        canvas.setAction(action, llx, lly, urx, ury);
    }

    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        canvas.addTemplate(template, a, b, c, d, e, f);
    }

    public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
        canvas.setCMYKColorStrokeF(cyan, magenta, yellow, black);
    }

    public void addImage(Image image, boolean inlineImage) throws DocumentException {
        canvas.addImage(image, inlineImage);
    }

    public PdfDocument getPdfDocument() {
        return canvas.getPdfDocument();
    }

    public void setPatternStroke(PdfPatternPainter p) {
        canvas.setPatternStroke(p);
    }

    public void eoFill() {
        canvas.eoFill();
    }

    public void setRGBColorFillF(float red, float green, float blue) {
        canvas.setRGBColorFillF(red, green, blue);
    }

    public void addImage(Image image, AffineTransform transform) throws DocumentException {
        canvas.addImage(image, transform);
    }

    public void endLayer() {
        canvas.endLayer();
    }

    public CanvasBuilder stroke() {
        canvas.stroke();
        return this;
    }

    public void setFlatness(float flatness) {
        canvas.setFlatness(flatness);
    }

    public void newlineText() {
        canvas.newlineText();
    }

    public void setCharacterSpacing(float charSpace) {
        canvas.setCharacterSpacing(charSpace);
    }
}
