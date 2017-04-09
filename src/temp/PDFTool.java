package projectpackage.support.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by Gvozd on 09.04.2017.
 */
public class PDFTool {
    static int FONT_SIZE_SMALL = 16;
    static int FONT_SIZE_BIG = 32;
    static int OFFSET = 40;

    public static void doPDF(String data){
        try {
            createTemplate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Receipt receipt = new Receipt(
                "This is a veeeeeeeeeeeeeeeeeeeeee" +
                        "eeeeeeeeeeeeeeeeeeeeery long purpose " +
                        "text, so it will overflow with font size = 16",
                123.45,
                "Name Surname");

        try {
            fillInReceipt(receipt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTemplate() throws Exception {
        Document document = new Document();

        Font font1 = new Font(Font.FontFamily.HELVETICA,
                FONT_SIZE_BIG, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.HELVETICA,
                FONT_SIZE_SMALL, Font.ITALIC | Font.UNDERLINE);

        PdfWriter.getInstance(document,
                new FileOutputStream("template.pdf"));

        document.open();

        // отцентрированный параграф
        Paragraph title = new Paragraph("Receipt", font1);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(FONT_SIZE_BIG);
        document.add(title);

        // параграф с текстом
        Paragraph purpose = new Paragraph("Purpose", font2);
        purpose.setSpacingAfter(FONT_SIZE_BIG);
        document.add(purpose);

        // параграф с добавленным чанком текста
        Paragraph amount = new Paragraph();
        amount.setFont(font2);
        amount.setSpacingAfter(8);
        amount.add(new Chunk("Amount"));
        document.add(amount);

        // параграф с фразой, в которую добавлен чанк
        Paragraph date = new Paragraph();
        date.setFont(font2);
        Phrase phrase = new Phrase();
        phrase.add(new Chunk("Date"));
        date.add(phrase);
        document.add(date);

        document.add(new Paragraph("Name", font2));

        Paragraph footer = new Paragraph(
                "Important - please retain for your records - ");

        // ссылка
        Anchor anchor = new Anchor("Javenue");
        anchor.setReference("http://www.javenue.info");
        footer.add(anchor);

        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(FONT_SIZE_BIG);
        document.add(footer);

        // картинка, загруженная по URL
        String imageUrl = "http://www.javenue.info/files/sample.png";
        // Image.getInstance("sample.png")
        Image stamp = Image.getInstance(new URL(imageUrl));
        stamp.setAlignment(Element.ALIGN_RIGHT);
        document.add(stamp);

        document.close();
    }

    public static void fillInReceipt(Receipt receipt)
            throws Exception {
        PdfReader reader = new PdfReader(
                new FileInputStream("template.pdf"));
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream("receipt.pdf"));

        PdfContentByte stream = stamper.getOverContent(1);
        stream.beginText();
        stream.setColorFill(BaseColor.BLUE);

        BaseFont font = BaseFont.createFont();

        float pageWidth = reader.getPageSize(1).getWidth();
        stream.setFontAndSize(font, FONT_SIZE_SMALL);
        float v = stream.getEffectiveStringWidth(
                receipt.getPurpose(), false);

        float fitSize = (pageWidth-OFFSET*2) * FONT_SIZE_SMALL/v;
        stream.setFontAndSize(font, fitSize);
        stream.setTextMatrix(OFFSET, 680);
        stream.showText(receipt.getPurpose());

        stream.setFontAndSize(font, FONT_SIZE_SMALL);

        String amount = NumberFormat.getCurrencyInstance()
                .format(receipt.getAmount());
        v = stream.getEffectiveStringWidth(amount, false);
        stream.setTextMatrix(pageWidth - v - OFFSET, 655);
        stream.showText(amount);

        v = stream.getEffectiveStringWidth(
                receipt.getDate() + "", false);
        stream.setTextMatrix(pageWidth - v - OFFSET, 630);
        stream.showText(receipt.getDate() + "");

        v = stream.getEffectiveStringWidth(
                receipt.getName(), false);
        stream.setTextMatrix(pageWidth - v - OFFSET, 605);
        stream.showText(receipt.getName());

        stream.endText();
        stamper.setFullCompression();
        stamper.close();
    }

    static class Receipt {
        private String purpose;
        private double amount;
        private Date date = new Date();
        private String name;

        public Receipt(String purpose, double amount, String name) {
            this.purpose = purpose;
            this.amount = amount;
            this.name = name;
        }

        public String getPurpose() { return purpose;}
        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public double getAmount() { return amount; }
        public void setAmount(double amount) {
            this.amount = amount;
        }

        public Date getDate() { return date; }
        public void setDate(Date date) { this.date = date; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
