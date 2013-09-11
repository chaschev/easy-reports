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

import com.chaschev.itext.ChunkBuilder;
import com.chaschev.itext.ITextBuilder;
import com.chaschev.itext.ITextSingleton;
import com.chaschev.itext.Style;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

/**
 * User: chaschev
 * Date: 9/9/13
 */
public class BalancedColumnsBuilderTest {
    private ITextBuilder b;

    @Before
    public void createITextBuilder() throws DocumentException {
        this.b = createTestBuilder();
    }

    public static ITextBuilder createTestBuilder() {
        Rectangle pageSize = PageSize.LETTER;
        final int pageMarginTop = 36;
        final int pageMarginBottom = 36;

        Document document = new Document(pageSize, 18, 18, pageMarginTop, pageMarginBottom);

        ITextBuilder b = ITextSingleton.INSTANCE.newBuilder(document).drawBorders();

        Font headerFont = FontFactory.getFont("OpenSansBold", BaseFont.WINANSI, BaseFont.EMBEDDED, 12f, Font.NORMAL);
        final Font subHeaderFont = FontFactory.getFont("OpenSansBold", BaseFont.WINANSI, BaseFont.EMBEDDED, 9f, Font.NORMAL);
        final Font textFont = FontFactory.getFont("OpenSans", BaseFont.WINANSI, BaseFont.EMBEDDED, 9f, Font.NORMAL);
        Font fixedWidthTextFont = FontFactory.getFont(FontFactory.COURIER, BaseFont.WINANSI, BaseFont.EMBEDDED, 8f, Font.NORMAL);
        Font textBoldFont = FontFactory.getFont("OpenSansBold", BaseFont.WINANSI, BaseFont.EMBEDDED, 9f, Font.NORMAL);

        b.styles()
            .add(new Style("header") {
                @Override
                public void apply(Element element) {
                    super.apply(element);

                    if (element instanceof Phrase) {
                        Phrase p = (Phrase) element;
                        p.setLeading(11f);
                    }
                }

                public void applyToChunk(ChunkBuilder c) {
                    c.setFont(subHeaderFont)
                        .setCharacterSpacing(0.5f);
                }
            })
            .add(new Style("subHeader") {
                @Override
                public void apply(Element element) {
                    super.apply(element);

                    if (element instanceof Phrase) {
                        Phrase p = (Phrase) element;
                        p.setLeading(11f);
                    }
                }

                public void applyToChunk(ChunkBuilder c) {

                    c.setFont(subHeaderFont)
                        .setCharacterSpacing(0.25f);
                }
            })
            .add(new Style("normal") {
                @Override
                public void apply(Element element) {
                    super.apply(element);

                    if (element instanceof Phrase) {
                        Phrase p = (Phrase) element;
                        p.setLeading(11f);
                    }
                }

                public void applyToChunk(ChunkBuilder c) {
                    c.setFont(textFont);
                }
            });
        return b;
    }

    @BeforeClass
    public static void createDirs(){
    }

    @Test
    public void testSanity() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(b.phrase("Test Phrase", "normal").build());

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/Sanity.pdf"));
    }

    private static Rectangle newRectangle() {
        return new Rectangle(200, 700, 400, 730);
    }

    @Test
    public void testSimpleText_TwoElements() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "subHeader").build(),
            b.phrase("Test Phrase 2", "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/TwoElements.pdf"));
    }

    @Test
    public void testSimpleText_TwoElements_Split() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2", "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/TwoElements_Split.pdf"));
    }

    @Test
     public void testSimpleText_ThreeElements_Split() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2", "normal").build(),
            b.phrase("Test Phrase 3", "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/ThreeElements_Split.pdf"));
    }

    @Test
    public void testSimpleText_TwoLines() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1\n" +
                "Test Phrase 2\n"
                , "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/TwoLines.pdf"));
    }

    @Test
    public void testSimpleText_ThreeLines() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1\n" +
                "Test Phrase 2\n" +
                "Test Phrase 3\n", "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/ThreeLines.pdf"));
    }

    @Test
      public void testSimpleText_TwoElements_Balance1() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2 - line 1\n" +
                "line 2\n" +
                "line 3", "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/TwoElements_Balance1.pdf"));
    }

    @Test
    public void testSimpleText_TwoElements_Balance2() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2 - line 1/6\n" +
                "line 2\n" +
                "line 3\n" +
                "line 4\n" +
                "line 5\n" +
                "line 6\n", "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/TwoElements_Balance2.pdf"));
    }

    @Test
    public void testSimpleText_TwoElements_Balance3() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2 - line 1/5\n" +
                "line 2\n" +
                "line 3\n" +
                "line 4\n" +
                "line 5\n"
                ,
                "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/TwoElements_Balance3.pdf"));
    }

    @Test
    public void testSimpleText_TwoElements_Balance4() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2 - line 1/14\n" +
                "line 2\n" +
                "line 3\n" +
                "line 4\n" +
                "line 5\n" +
                "line 6\n" +
                "line 7\n" +
                "line 8\n" +
                "line 9\n" +
                "line 10\n" +
                "line 11\n" +
                "line 12\n" +
                "line 13\n" +
                "line 14\n",
                "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/BalancedSimpleText/TwoElements_Balance4.pdf"));
    }

}
