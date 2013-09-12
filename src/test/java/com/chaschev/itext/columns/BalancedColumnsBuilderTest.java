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

import com.chaschev.itext.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import gnu.trove.list.array.TFloatArrayList;
import org.apache.commons.lang3.RandomStringUtils;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Random;

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
        final Font textFontItalic = FontFactory.getFont("OpenSansItalic", BaseFont.WINANSI, BaseFont.EMBEDDED, 9f, Font.NORMAL);
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
            })
            .add(new Style("comment") {
                @Override
                public void apply(Element element) {
                    super.apply(element);

                    if (element instanceof Phrase) {
                        Phrase p = (Phrase) element;
                        p.setLeading(11f);
                    }
                }

                public void applyToChunk(ChunkBuilder c) {
                    c.setFont(textFontItalic);
                }
            });
        return b;
    }

    @BeforeClass
    public static void createDirs() {
    }

    @Test
    public void testSanity() throws Exception {
        showComment("Sanity: a single line on the left.");

        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(b.phrase("Test Phrase", "normal").build());

        builder.go();

        expectations("Sanity", builder, 1, 0);
    }

    private void showComment(String text) {
        ColumnText.showTextAligned(
            b.getCanvas(),
            Element.ALIGN_CENTER,
            b.phrase(text, "comment").build(),
            (b.getDocument().right() + b.getDocument().left()) / 2,
            750,
            0
        );
    }

    private static Rectangle newRectangle() {
        final int off = 10;
        return new Rectangle(200 + off, 600, 400 + off, 670);
    }

    private static Rectangle newBottomRectangle(int top) {
        final int off = 10;
        return new Rectangle(200 + off, 100, 400 + off, top);
    }

    @Test
    public void testSimpleText_TwoElements1() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "subHeader").build(),
            b.phrase("Test Phrase 2", "normal").build()
        );

        builder.go();

        expectations("TwoElements1",builder, 2, 0);
    }

    @Test
    public void testSimpleText_TwoElements2() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2", "normal").build()
        );

        builder.go();

        expectations("TwoElements2", builder, 2, 0);
    }

    @Test
    public void testSimpleText_List1() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2", "normal").build()
        );

        builder.go();

        expectations("TwoElements2", builder, 2, 0);
    }


    @Test
    public void testSimpleText_ThreeElements() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase("Test Phrase 1", "normal").build(),
            b.phrase("Test Phrase 2", "normal").build(),
            b.phrase("Test Phrase 3", "normal").build()
        );

        builder.go();

        expectations("ThreeElements", builder, 3, 0);
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

        expectations("TwoElements", builder, 2, 0);
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

        expectations("ThreeLines", builder, 3, 0);
    }

    @Test
    public void testSimpleText_NineLines() throws Exception {
        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(newRectangle(), b);

        builder.add(
            b.phrase(testPhrase(1, 9), "normal").build()
        );

        builder.go();

        expectations("NineLines", builder, 5, 4);
    }


    @Test
    public void testSimpleText_TwoElements_Balance1() throws Exception {
        twoPhrasesTest(2, 4, 2, 4);
    }

    @Test
    public void testList_TwoElements_Balance1() throws Exception {
        twoPhrasesTest(2, 4, 2, 4, ContentType.LIST);
    }

    @Test
    public void testTable_TwoElements_Balance1() throws Exception {
        twoPhrasesTest(2, 4, 2, 4, ContentType.TABLE);
    }

    @Test
    public void testSimpleText_TwoElements_Balance2() throws Exception {
        twoPhrasesTest(2, 7, 5, 4);
    }

    @Test
    public void testList_TwoElements_Balance2() throws Exception {
        twoPhrasesTest(2, 7, 5, 4, ContentType.LIST);
    }

    @Test
    public void testTable_TwoElements_Balance2() throws Exception {
        twoPhrasesTest(2, 7, 5, 4, ContentType.TABLE);
    }

    @Test
    public void testSimpleText_TwoElements_Balance3() throws Exception {
        twoPhrasesTest(2, 6, 5, 3);
    }

    @Test
    public void testList_TwoElements_Balance3() throws Exception {
        twoPhrasesTest(2, 6, 5, 3, ContentType.LIST);
    }

    @Test
    public void testTable_TwoElements_Balance3() throws Exception {
        twoPhrasesTest(2, 6, 5, 3, ContentType.TABLE);
    }

    @Test
    public void testSimpleText_TwoElements_Balance4() throws Exception {
        twoPhrasesTest(7, 15, 11, 11);
    }

    @Test
    public void testList_TwoElements_Balance4() throws Exception {
        twoPhrasesTest(7, 15, 11, 11, ContentType.LIST);
    }

    @Test
    public void testTable_TwoElements_Balance4() throws Exception {
        twoPhrasesTest(7, 15, 11, 11, ContentType.TABLE);
    }

    @Test
    public void testSimpleText_TwoElements_Balance5() throws Exception {
        twoPhrasesTest(3, 6, 6, 3);
    }

    @Test
    public void testList_TwoElements_Balance5() throws Exception {
        twoPhrasesTest(3, 6, 6, 3, ContentType.LIST);
    }

    @Test
    public void testTable_TwoElements_Balance5() throws Exception {
        twoPhrasesTest(3, 6, 6, 3, ContentType.TABLE);
    }

    @Test
    public void testSimpleText_TwoElements_SmallImbalance1() throws Exception {
        twoPhrasesTest(6, 9, 6, 9);
    }
    @Test
    public void testList_TwoElements_SmallImbalance1() throws Exception {
        twoPhrasesTest(6, 9, 6, 9, ContentType.LIST);
    }

    @Test
    public void testTable_TwoElements_SmallImbalance1() throws Exception {
        twoPhrasesTest(6, 9, 6, 9, ContentType.TABLE);
    }

    @Test
    public void testList_TwoElements_SmallImbalance2() throws Exception {
        twoPhrasesTest(6, 12, 9, 9, ContentType.LIST);
    }

    @Test
    public void testTable_TwoElements_SmallImbalance2() throws Exception {
        twoPhrasesTest(6, 12, 9, 9, ContentType.TABLE);
    }

    @Test
    public void testSimpleText_TwoElements_SmallImbalance2() throws Exception {
        twoPhrasesTest(6, 12, 9, 9);
    }

    @Test
    public void testSimpleText_TwoElements_SmallImbalance3() throws Exception {
        twoPhrasesTest(6, 15, 11, 10);
    }

    @Test
    public void testList_TwoElements_SmallImbalance3() throws Exception {
        twoPhrasesTest(6, 15, 11, 10, ContentType.LIST);
    }

    @Test
    public void testTable_TwoElements_SmallImbalance3() throws Exception {
        twoPhrasesTest(6, 15, 11, 10, ContentType.TABLE);
    }

    @Test
    public void temp_PageBreak1() throws Exception {
        twoPhrasesTestPageBreak(7, 10, newBottomRectangle(120));
    }

    @Ignore
    @Test
    public void testSimpleText_TwoElements_PageBreak1() throws Exception {
        for(int i = 8;i<18;i++){
            for(int j = 1;j<18;j++){
                twoPhrasesTestPageBreak(i, j, newBottomRectangle(120));
            }
        }
    }

    @Test
    @Ignore
    public void multi_TwoTitledLists() throws Exception {
        for(int i = 3;i<18;i++){
            for(int j = 3;j<18;j++){
                twoTitledListsAndSpace(i,j, newRectangle());
            }
        }
    }

    @Test
    @Ignore
    public void multi_TwoTitledManualLists2() throws Exception {
        for(int i = 3;i<18;i++){
            for(int j = 3;j<18;j++){
                twoTitledManualListsAndSpace(i, j, newRectangle());
            }
        }
    }

    @Test
    @Ignore
    public void multi_TwoTitledManualListsPageBreak() throws Exception {
        for(int i = 3;i<18;i++){
            for(int j = 3;j<18;j++){
                twoTitledManualListsAndSpace(i, j, newBottomRectangle(120));
            }
        }
    }

    @Test
    public void tempSpace() throws Exception {
        twoPhrasesAndSpace(11, 10, newRectangle());
    }

    @Test
    @Ignore
    public void multi_TwoElements_Space() throws Exception {
        for (int i = 8; i < 18; i++) {
            for (int j = 3; j < 18; j++) {
                twoPhrasesAndSpace(i, j, newRectangle());
            }
        }
    }

    @Test
    @Ignore
    public void multi_TwoLists_Space() throws Exception {
        for (int i = 3; i < 18; i++) {
            for (int j = 3; j < 18; j++) {
                twoListsAndSpace(i, j, newRectangle());
            }
        }
    }

    @Test
    @Ignore
    public void multi_TwoTitledElements_Space() throws Exception {
        for (int i = 3; i < 15; i++) {
            for (int j = 5; j < 18; j++) {
                twoTitledPhrasesAndSpace(i,j,newRectangle());
            }
        }
    }

    public static String testPhrase(int phraseIndex, int lineCount) {
        StringBuilder sb = new StringBuilder(512);

        sb.append("Phrase ").append(phraseIndex).append(" - line 1/").append(lineCount).append("\n");

        for (int i = 0; i < lineCount - 1; i++) {
            sb.append("line ").append(i + 2).append("\n");
        }

        return sb.toString();
    }

    public List newTestList(int listIndex, int lineCount) {
        List list = new List(List.ORDERED, List.NUMERICAL);

        list.setPostSymbol(". ");
        list.setListSymbol(b.chunk("- ", "normal").build());

        list.add(new ListItem(b.phrase("List " + listIndex + " - line 1/" + lineCount, "subHeader").build()));

        for (int i = 0; i < lineCount - 1; i++) {
            list.add(new ListItem(b.phrase("line " + (i+2)).build()));
        }

        return list;
    }

    public List newTitledTestList(int listIndex, int lineCount) {
        List list = new List(false, false);

        list.setPostSymbol(". ");
        list.setListSymbol(b.chunk("- ", "normal").build());

        final ListItem subHeader = new ListItem(b.phrase("List " + listIndex + " Title (" + lineCount + " lines)\n\n", "subHeader").build());
        subHeader.setListSymbol(new Chunk(""));

        list.add(subHeader);

        final Random random = new Random(2);

        for (int i = 0; i < lineCount; i++) {
            final ListItem item = new ListItem(b.phrase("line " + (i + 1) + " " + RandomStringUtils.random(14+random.nextInt(50), "alsd fjasd df we asdf oiru")).build());
//            final ListItem item = new ListItem(b.phrase("line " + (i + 1) + " " + RandomStringUtils.random(14+random.nextInt(50), "alsd fjasd df we asdf oiru")).build());
            item.setListSymbol(b.chunk((i + 1)+". ").build());
            item.setIndentationLeft(0, true);
            list.add(item);
        }

        return list;
    }

    public List newTestListNoHeader(int lineCount) {
        List list = new List(List.ORDERED, List.NUMERICAL);

        list.setPostSymbol(". ");
        list.setListSymbol(b.chunk("- ", "normal").build());

        for (int i = 0; i < lineCount ; i++) {
            list.add(new ListItem(b.phrase("line " + (i+1)).build()));
        }

        return list;
    }

    public PdfPTable newTitledList(int listIndex, int lineCount) {
        final TableBuilder table = b.newTableBuilder(20, 100);

        table.setLockedWidth(true)
            .setTotalWidth(95)
            .addCell(table.cell(b.phrase("List " + listIndex + " \n title", "header").build()).setColspan(2).build());

        table
            .addCell(b.phrase("1.", "normal").build())
            .addCell(b.phrase("List " + listIndex + " - line 1/" + lineCount, "normal").build());

        for (int i = 0; i < lineCount - 1; i++) {
            table
                .addCell(b.phrase((i + 2) + ".", "normal").build())
                .addCell(b.phrase("line " + (i + 1), "normal").build());
        }

        table.getRow(0).setMayNotBreak(true);
        table.getRow(1).setMayNotBreak(true);

        return table.build();

    }

    public PdfPTable testTable(int listIndex, int lineCount) {
        final TableBuilder table = b.newTableBuilder(1);

        table.addCell(b.phrase("List " + listIndex + " - line 1/" + lineCount).build());

        for (int i = 0; i < lineCount - 1; i++) {
            table.addCell(b.phrase("line " + (i + 2)).build());
        }

        return table.build();
    }

    private void twoPhrasesTest(int lineCount1, int lineCount2, int expectedOnLeft, int expectedOnRight) {
        twoPhrasesTest(lineCount1, lineCount2, expectedOnLeft, expectedOnRight, newRectangle());
    }

    enum ContentType{
        PHRASE, LIST, TABLE
    }

    private void twoPhrasesTest(int lineCount1, int lineCount2, int expectedOnLeft, int expectedOnRight, Rectangle rectangle) {
        twoPhrasesTest(lineCount1, lineCount2, expectedOnLeft, expectedOnRight, rectangle, ContentType.PHRASE);
    }

    private void twoPhrasesTest(int lineCount1, int lineCount2, int expectedOnLeft, int expectedOnRight, ContentType type) {
        twoPhrasesTest(lineCount1, lineCount2, expectedOnLeft, expectedOnRight, newRectangle(), type);
    }

    private void twoPhrasesTest(int lineCount1, int lineCount2, int expectedOnLeft, int expectedOnRight, Rectangle rectangle, ContentType type) {
        ITextBuilder b = createTestBuilder();

        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(rectangle, b);

        switch (type) {
            case PHRASE:
                builder.add(
                    b.phrase(testPhrase(1, lineCount1), "normal").build(),
                    b.phrase(testPhrase(2, lineCount2), "normal").build()
                );

                break;
            case LIST:
                builder.add(newTestList(1, lineCount1), newTestList(2, lineCount2));

                break;
            case TABLE:
                builder.add(testTable(1, lineCount1), testTable(2, lineCount2));

                break;
        }

        builder.go();

        twoElementsExpectations(b, builder, expectedOnLeft, expectedOnRight, lineCount1, lineCount2, type);
    }

    private void twoPhrasesTestPageBreak(
        int lineCount1, int lineCount2, Rectangle rectangle) {
        ITextBuilder b = createTestBuilder();

        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(rectangle, b);

        builder.add(
            b.phrase(testPhrase(1, lineCount1), "normal").build(),
            b.phrase(testPhrase(2, lineCount2), "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File(String.format("output/BalancedSimpleText/PageBreak/TwoElements_h_%03.0f_%02d_%02d.pdf", rectangle.getTop(), lineCount1, lineCount2)));
    }

    private void twoTitledPhrasesAndSpace(
        int lineCount1, int lineCount2, Rectangle rectangle) {
        ITextBuilder b = createTestBuilder();

        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(rectangle, b);

        builder.add(
            newTitledList(1, lineCount1),
            new SpaceElement(11f),
            newTitledList(2, lineCount2)
        );

        builder.go();

        b.close().saveToFile(new File(String.format("output/BalancedSimpleText/TitledListAndSpace/TwoElements_h_%03.0f_%02d_%02d.pdf", rectangle.getTop(), lineCount1, lineCount2)));
    }

    private void twoPhrasesAndSpace(
        int lineCount1, int lineCount2, Rectangle rectangle) {
        ITextBuilder b = createTestBuilder();

        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(rectangle, b);

        builder.add(
            b.phrase(testPhrase(1, lineCount1), "normal").build(),
            new SpaceElement(11f),
            b.phrase(testPhrase(2, lineCount2), "normal").build()
        );

        builder.go();

        b.close().saveToFile(new File(String.format("output/BalancedSimpleText/Space/TwoElements_h_%03.0f_%02d_%02d.pdf", rectangle.getTop(), lineCount1, lineCount2)));
    }

    private void twoListsAndSpace(
        int lineCount1, int lineCount2, Rectangle rectangle) {
        ITextBuilder b = createTestBuilder();

        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(rectangle, b);

        builder.add(
            newTestList(1, lineCount1),
            new SpaceElement(11f),
            newTestList(2, lineCount2)
        );

        builder.go();

        b.close().saveToFile(new File(String.format("output/BalancedSimpleText/ListsAndSpace/TwoElements_h_%03.0f_%02d_%02d.pdf", rectangle.getTop(), lineCount1, lineCount2)));

    }

    private void twoTitledListsAndSpace(
        int lineCount1, int lineCount2, Rectangle rectangle) {
        ITextBuilder b = createTestBuilder();

        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(rectangle, b);

        builder.add(
            b.phrase("List 1 Title\n\n", "subHeader").build(),
            newTestListNoHeader(lineCount1),
            new SpaceElement(11f),
            b.phrase("List 2 Title\n\n", "subHeader").build(),
            newTestListNoHeader(lineCount2)
        );

        builder.go();

        b.close().saveToFile(new File(String.format("output/BalancedSimpleText/ListsAndSpace/TwoElements_h_%03.0f_%02d_%02d.pdf", rectangle.getTop(), lineCount1, lineCount2)));

    }

    private void twoTitledManualListsAndSpace(
        int lineCount1, int lineCount2, Rectangle rectangle) {
        ITextBuilder b = createTestBuilder();

        final BalancedColumnsBuilder builder = new BalancedColumnsBuilder(rectangle, b);

        builder.add(
            newTitledTestList(1, lineCount1),
            new SpaceElement(11f),
            newTitledTestList(2, lineCount2)
        );

        builder.go();

        b.close().saveToFile(new File(String.format("output/BalancedSimpleText/ListsAndSpace/Manual_h_%03.0f_%02d_%02d.pdf", rectangle.getTop(), lineCount1, lineCount2)));

    }

    private void twoElementsExpectations(ITextBuilder b, BalancedColumnsBuilder builder, int expectedOnLeft, int expectedOnRight, int lineCount1, int lineCount2, ContentType type) {
        showComment("Expected: " + expectedOnLeft +
            " lines on the left, " + expectedOnRight +
            " lines on the right.");

        final String s = type.toString().toLowerCase();

        b.close().saveToFile(new File(String.format("output/BalancedSimpleText/TwoElements%s/%02d_%02d.pdf",
            s.substring(0,1).toUpperCase() + s.substring(1),
            lineCount1, lineCount2)));

        int max = Math.max(expectedOnLeft, expectedOnRight);

        final float lineHeight = type == ContentType.TABLE ? 13f:11f;

        Assertions.assertThat((float) builder.bestResult.getLeftColumnHeight()).describedAs("left column height").is(equalToAnyOf(0.1f, max * lineHeight, expectedOnLeft * lineHeight));
        Assertions.assertThat((float) builder.bestResult.getRightColumnHeight()).describedAs("right column height").is(equalToAnyOf(0.1f, max * lineHeight, expectedOnRight * lineHeight));
    }

    private void expectations(String title, BalancedColumnsBuilder builder, int expectedOnLeft, int expectedOnRight) {
        showComment("Expected: " + expectedOnLeft +
            " lines on the left, " + expectedOnRight +
            " lines on the right.");

        b.close().saveToFile(new File(String.format("output/BalancedSimpleText/%s_%02d_%02d.pdf",
            title, expectedOnLeft, expectedOnRight)));

        int max = Math.max(expectedOnLeft, expectedOnRight);

        Assertions.assertThat((float) builder.bestResult.getLeftColumnHeight()).describedAs("left column height").is(equalToAnyOf(0.1f, max * 11f, expectedOnLeft * 11f));
        Assertions.assertThat((float) builder.bestResult.getRightColumnHeight()).describedAs("right column height").is(equalToAnyOf(0.1f, max * 11f, expectedOnRight * 11f));
    }

    public static org.fest.assertions.core.Condition<Float> equalToAnyOf(final float delta, final float... values) {

        return new org.fest.assertions.core.Condition<Float>("equal to any of " + new TFloatArrayList(values)) {
            @Override
            public boolean matches(Float x) {
                for (Float v : values) {
                    if (Math.abs(v - x) < delta) {
                        return true;
                    }
                }

                return false;
            }
        };
    }
}
