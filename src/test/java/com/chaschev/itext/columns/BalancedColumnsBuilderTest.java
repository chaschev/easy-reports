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

        Rectangle pageSize = PageSize.LETTER;
        final int pageMarginTop = 36;
        final int pageMarginBottom = 36;

        Document document = new Document(pageSize, 18, 18, pageMarginTop, pageMarginBottom);

        b = ITextSingleton.INSTANCE.newBuilder(document).drawBorders();

        Font headerFont = FontFactory.getFont("OpenSansBold", BaseFont.WINANSI, BaseFont.EMBEDDED, 12f, Font.NORMAL);
        final Font subHeaderFont = FontFactory.getFont("OpenSansBold", BaseFont.WINANSI, BaseFont.EMBEDDED, 9f, Font.NORMAL);
        final Font textFont = FontFactory.getFont("OpenSans", BaseFont.WINANSI, BaseFont.EMBEDDED, 9f, Font.NORMAL);
        Font fixedWidthTextFont = FontFactory.getFont(FontFactory.COURIER, BaseFont.WINANSI, BaseFont.EMBEDDED, 8f, Font.NORMAL);
        Font textBoldFont = FontFactory.getFont("OpenSansBold", BaseFont.WINANSI, BaseFont.EMBEDDED, 9f, Font.NORMAL);

        b.styles()
            .add(new Style("header") {
                public void applyToChunk(ChunkBuilder c) {
                    c.setFont(subHeaderFont)
                        .setCharacterSpacing(0.5f);
                }
            })
            .add(new Style("subHeader") {
                public void applyToChunk(ChunkBuilder c) {
                    c.setFont(subHeaderFont)
                        .setCharacterSpacing(0.25f);
                }
            })
            .add(new Style("normal") {
                public void applyToChunk(ChunkBuilder c) {
                    c.setFont(textFont);
                }
            })

        ;
    }

    @BeforeClass
    public static void createDirs(){
    }

    @Test
    public void testSimpleText() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(new Rectangle(400, 300, 600, 400), b);

        builder.add(b.phrase("Test Phrase", "normal").build());

        builder.go();

        b.close().saveToFile(new File("output/testSimpleText1.pdf"));
    }

    @Test
    public void testSimpleText_TwoElements() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(new Rectangle(400, 300, 600, 400), b);

        builder.add(
            b.phrase("Test Phrase 1", "subHeader").build(),
            b.phrase("Test Phrase 2", "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File("output/testSimpleText1.pdf"));
    }
}
