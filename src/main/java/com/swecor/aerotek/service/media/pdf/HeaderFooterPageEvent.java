package com.swecor.aerotek.service.media.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.swecor.aerotek.model.security.User;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.persist.security.UserRepository;
import com.swecor.aerotek.rest.exceptions.library.EmailIsAbsent;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    public static final String FONT = "JetBrainsMono-LightItalic.ttf";

    private final String propertyPath = "C:/opt/aerotek/content-storage";

    private final BaseColor lineColor = new BaseColor(35, 167, 230);

    private Font normal, bold, small;

    private InstallationToSelection installation;

    private UserRepository userRepository;

    PdfTemplate total;

    String uploadPath = Paths.get(propertyPath + "/" + "pdf").toString();
    Path logoPath = Paths.get(uploadPath + "/" + "aerotekLogo.png");

    public HeaderFooterPageEvent(InstallationToSelection installation, UserRepository userRepository) {
        try {
            this.userRepository = userRepository;
            this.installation = installation;
            this.normal = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
            this.bold = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10, Font.BOLD);
            this.small = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void onStartPage(PdfWriter writer, Document document) {

        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        addLogoRow(table, logoPath);
        addIdRow(table);
        addEmptyRow(table);
        addLastRows(table, installation);

        table.setTotalWidth(document.getPageSize().getWidth()
                - document.leftMargin() - document.rightMargin());
        table.writeSelectedRows(0, -1, document.leftMargin(),
                830, writer.getDirectContent());

    }

    public void onEndPage(PdfWriter writer, Document document) {

        PdfPTable table = new PdfPTable(3);
        try {
            table.setWidths(new int[]{13, 13, 24});
            table.getDefaultCell().setFixedHeight(10);
//            table.getDefaultCell().setBorder(Rectangle.TOP);

            PdfPCell r1c1 = new PdfPCell();
            r1c1.setUseVariableBorders(true);
            r1c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            r1c1.setBorder(Rectangle.TOP);
            r1c1.setBorderColorTop(lineColor);
            table.addCell(r1c1);

            PdfPCell r1c2 = new PdfPCell();
            r1c2.setUseVariableBorders(true);
            r1c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            r1c2.setBorder(Rectangle.TOP);
            r1c2.setBorderColorTop(lineColor);
            r1c2.setPhrase(new Phrase(String.format("Лист %d /", writer.getPageNumber()), small));
            table.addCell(r1c2);

            PdfPCell r1c3 = new PdfPCell(Image.getInstance(total));
            r1c3.setUseVariableBorders(true);
            r1c3.setHorizontalAlignment(Element.ALIGN_LEFT);
            r1c3.setBorder(Rectangle.TOP);
            r1c3.setBorderColorTop(lineColor);
            table.addCell(r1c3);
            table.setTotalWidth(document.getPageSize().getWidth()
                    - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin(),
                    document.bottomMargin() + 5, writer.getDirectContent());

        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 12);
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber()), small),
                2, 2, 0);
    }

    private void addLogoRow(PdfPTable table, Path aerotekLogo) throws IOException, BadElementException {

        Image logo = Image.getInstance(aerotekLogo.toAbsolutePath().toString());
        logo.scalePercent(60);

        PdfPCell r1c1 = new PdfPCell(logo);
        r1c1.setBorder(Rectangle.NO_BORDER);
        table.addCell(r1c1);

        PdfPCell r1c2 = new PdfPCell();
        r1c2.setBorder(Rectangle.NO_BORDER);
        table.addCell(r1c2);

        PdfPCell r1c3 = new PdfPCell(getContacts());
        r1c3.setBorder(Rectangle.NO_BORDER);
        r1c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        r1c1.setIndent(50);
        table.addCell(r1c3);
    }

    private void addIdRow(PdfPTable table) {
        PdfPCell r2c1 = new PdfPCell(new Phrase("ID: " + installation.getId(), normal));
        r2c1.setUseVariableBorders(true);
        r2c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        r2c1.setBorder(Rectangle.TOP);
        r2c1.setBorderColorTop(lineColor);

        table.addCell(r2c1);

        PdfPCell r2c2 = new PdfPCell(new Phrase("Предложение № IDП_12340", normal));
        r2c2.setUseVariableBorders(true);
        r2c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        r2c2.setBorder(Rectangle.TOP);
        r2c2.setBorderColorTop(lineColor);

        table.addCell(r2c2);

        String createdDate = installation.getCreatedDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        PdfPCell r2c3 = new PdfPCell(new Phrase("От: " + createdDate, normal));
        r2c3.setUseVariableBorders(true);
        r2c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        r2c3.setBorder(Rectangle.TOP);
        r2c3.setBorderColorTop(lineColor);

        table.addCell(r2c3);
    }

    private void addEmptyRow(PdfPTable table){
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
    }

    private void addLastRows(PdfPTable table, InstallationToSelection installation) throws DocumentException {

        Paragraph leftP = new Paragraph();
        leftP.add(new Phrase("Объект: ", normal));
        leftP.add(new Phrase("Московский кремль", bold));
        PdfPCell r3c1 = new PdfPCell(leftP);
        r3c1.setUseVariableBorders(true);
        r3c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        r3c1.setBorder(Rectangle.BOTTOM);
        r3c1.setBorderColorBottom(lineColor);

        table.addCell(r3c1);

        Paragraph centreP = new Paragraph();
        centreP.add(new Phrase("Обозначение: ", normal));
        centreP.add(new Phrase(installation.getName(), bold));
        PdfPCell r3c2 = new PdfPCell(centreP);
        r3c2.setUseVariableBorders(true);
        r3c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        r3c2.setBorder(Rectangle.BOTTOM);
        r3c2.setBorderColorBottom(lineColor);

        table.addCell(r3c2);

        Paragraph rightP = new Paragraph();
        rightP.add(new Phrase("Рассчитал: ", normal));
        rightP.add(new Phrase(getCreatedByAbbreviation(installation), bold));
        PdfPCell r3c3 = new PdfPCell(rightP);
        r3c3.setUseVariableBorders(true);
        r3c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        r3c3.setBorder(Rectangle.BOTTOM);
        r3c3.setBorderColorBottom(lineColor);

        table.addCell(r3c3);
    }

    private PdfPTable getContacts() {

        PdfPTable contactTable = new PdfPTable(1);
        contactTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        contactTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

        contactTable.addCell(new Phrase("Aerotek Professional \n", bold));
        contactTable.addCell(new Phrase("Tel. +7 (495) 234 77 99 \n", normal));
        contactTable.addCell(new Phrase("e-mail: info@aerotek-rus.ru", normal));

        return contactTable;
    }

    private String getCreatedByAbbreviation(InstallationToSelection installation){

        User user = userRepository.findByEmail(installation.getCreatedBy()).orElseThrow(EmailIsAbsent::new);

        return user.getLastName() +
                " " +
                user.getFirstName().charAt(0) +
                "." +
                user.getPatronymic().charAt(0) +
                ".";
    }
}

