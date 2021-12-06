package com.swecor.aerotek.service.media.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.swecor.aerotek.model.selection.Instalation.AcousticPerformanceDTO;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.drawModel.WeightAndSize;
import com.swecor.aerotek.persist.security.UserRepository;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;

import static com.swecor.aerotek.service.media.pdf.HeaderFooterPageEvent.FONT;

@Component
@Transactional
public class PdfBuilder {

    private final UserRepository userRepository;

    private final SectionWriter sectionWriter;

    private final Font normal, bold, small, smallBold, bigBold;

    private final BaseColor inflowLineColor = new BaseColor(35, 167, 255);

    private final BaseColor outflowLineColor = new BaseColor(222, 60, 60);

    private final BaseColor inflowBackgroundColor = new BaseColor(203, 220, 228);

    private final BaseColor outflowBackgroundColor = new BaseColor(224, 138, 138);

    private final BaseColor tableHeaderColor = new BaseColor(224, 138, 138);

    @Value("${upload.path-pdf}")
    private String propertyPath;

    public PdfBuilder(UserRepository userRepository, SectionWriter sectionWriter) throws IOException, DocumentException {
        this.userRepository = userRepository;
        this.sectionWriter = sectionWriter;
        this.normal = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
        this.bold = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10, Font.BOLD);
        this.small = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 8);
        this.smallBold = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 8, Font.BOLD);
        this.bigBold = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 18, Font.BOLD);
    }

    public void buildPdf(InstallationToSelection installation) throws IOException, DocumentException {
        String uploadPath = Paths.get(propertyPath).toString();
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String resultFilename = UUID.randomUUID().toString();

        Path path = Paths.get(uploadPath + "/" + "installation.jpg");

        Document document = new Document(PageSize.A4, 20, 20, 100, 25);
        PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(uploadPath + "/" + resultFilename + ".pdf"));
        writer.setPageEvent(new HeaderFooterPageEvent(installation, userRepository));

        document.open();

        document.add(new Paragraph("\n"));

        document.add(getStartTable(installation));

        Image InstallationImg = Image.getInstance(path.toAbsolutePath().toString());

        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin()) / InstallationImg.getWidth()) * 100;
        InstallationImg.scalePercent(scaler);

        document.add(InstallationImg);

        Paragraph underImageParagraph = new Paragraph("Изображение Установки может отличаться от реальной модели\n" +
                "Производитель имеет право на внесение изменений без предварительного уведомления\n", small);
        underImageParagraph.setAlignment(Element.ALIGN_CENTER);

        document.add(underImageParagraph);

        document.newPage();

        printInstallation(installation, document);

        printAcousticTable(installation, document);

        printWeightAndDimensions(installation, document);

        document.close();
    }

    private PdfPTable getStartTable(InstallationToSelection installation) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{60, 13, 13, 13});

        setStartTableR1(table, installation);
        setStartTableR2(table, installation);
        setStartTableR3(table, installation);
        setStartTableR4(table, installation);

        return table;
    }

    private void setStartTableR1(PdfPTable table, InstallationToSelection installation) {

        Paragraph c1P = new Paragraph();
        c1P.add(new Phrase("Модель установки: ", normal));
        c1P.add(new Phrase(installation.getName(), bold));
        PdfPCell c1 = new PdfPCell(c1P);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);

        PdfPCell c2 = new PdfPCell();
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2.setBorder(Rectangle.NO_BORDER);

        table.addCell(c2);

        PdfPCell c3 = new PdfPCell();
        c3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3.setBorder(Rectangle.NO_BORDER);

        table.addCell(c3);

        PdfPCell c4 = new PdfPCell();
        c4.setHorizontalAlignment(Element.ALIGN_LEFT);
        c4.setBorder(Rectangle.NO_BORDER);

        table.addCell(c4);
    }

    private void setStartTableR2(PdfPTable table, InstallationToSelection installation) {

        Paragraph c1P = new Paragraph();
        c1P.add(new Phrase("Исполнение: ", normal));
        c1P.add(new Phrase("стандартное", bold));
        PdfPCell c1 = new PdfPCell(c1P);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);

        PdfPCell c2 = new PdfPCell();
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2.setBorder(Rectangle.NO_BORDER);

        table.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase("Приток: ", normal));
        c3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3.setBorder(Rectangle.NO_BORDER);

        table.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase("Вытяжка: ", normal));
        c4.setHorizontalAlignment(Element.ALIGN_LEFT);
        c4.setBorder(Rectangle.NO_BORDER);

        table.addCell(c4);
    }

    private void setStartTableR3(PdfPTable table, InstallationToSelection installation) {

        Paragraph c1P = new Paragraph();
        c1P.add(new Phrase("Типоразмер: ", normal));
        c1P.add(new Phrase(String.valueOf(installation.getStandardSize()), bold));
        PdfPCell c1 = new PdfPCell(c1P);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase("Расход: ", normal));
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c2.setBorder(Rectangle.NO_BORDER);

        table.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase(installation.getInflowConsumption() + "м³/ч", normal));
        c3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3.setBorder(Rectangle.NO_BORDER);

        table.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase(installation.getOutflowConsumption() + "м³/ч", normal));
        c4.setHorizontalAlignment(Element.ALIGN_LEFT);
        c4.setBorder(Rectangle.NO_BORDER);

        table.addCell(c4);
    }

    private void setStartTableR4(PdfPTable table, InstallationToSelection installation) {

        Paragraph c1P = new Paragraph();
        c1P.add(new Phrase("Опциональное оснащение: ", normal));
        c1P.add(new Phrase("гибкие вставки", bold));
        PdfPCell c1 = new PdfPCell(c1P);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase("Напор: ", normal));
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c2.setBorder(Rectangle.NO_BORDER);

        table.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase(installation.getInflowPressure() + "Па", normal));
        c3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3.setBorder(Rectangle.NO_BORDER);

        table.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase(installation.getOutflowPressure() + "Па", normal));
        c4.setHorizontalAlignment(Element.ALIGN_LEFT);
        c4.setBorder(Rectangle.NO_BORDER);

        table.addCell(c4);
    }

    private void printInstallation(InstallationToSelection installation, Document document) throws DocumentException {
        boolean isInflow = !installation.getInflowParameters().isEmpty();
        boolean isOutflow = !installation.getOutflowParameters().isEmpty();

        if (!isInflow && !isOutflow) {
            throw new InstallationFlowIsEmpty();
        } else if (isInflow && isOutflow) {
            //приток
            printInflow(
                    installation.getInflowParameters(),
                    document);

            document.newPage();

            //вытяжка
            printOutflow(
                    installation.getOutflowParameters(),
                    document);
        } else if (isInflow) {
            //приток
            printInflow(
                    installation.getInflowParameters(),
                    document);
        } else if (isOutflow) {
            //вытяжка
            printOutflow(
                    installation.getOutflowParameters(),
                    document);
        }

    }

    private void printInflow(List<SectionToConfigurator> sections, Document document) throws DocumentException {
        Paragraph sectionParagraph = new Paragraph("Приточная часть", bigBold);
        sectionParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(sectionParagraph);
        document.add(new Paragraph("\n"));

        for (SectionToConfigurator section : sections) {
            sectionWriter.write(section, normal, bold, bigBold, inflowLineColor, inflowBackgroundColor, document);
        }

    }

    private void printOutflow(List<SectionToConfigurator> sections, Document document) throws DocumentException {
        Paragraph sectionParagraph = new Paragraph("Вытяжная часть", bigBold);
        sectionParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(sectionParagraph);
        document.add(new Paragraph("\n"));

        for (SectionToConfigurator section : sections) {
            sectionWriter.write(section, normal, bold, bigBold, outflowLineColor, outflowBackgroundColor, document);
        }

    }

    private void printAcousticTable(InstallationToSelection installation, Document document) throws DocumentException {

        Paragraph acousticParagraph = new Paragraph("Акустические характеристики", bigBold);
        acousticParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(acousticParagraph);
        document.add(new Paragraph("\n"));

        PdfPTable acousticTable = new PdfPTable(2);
        acousticTable.setKeepTogether(true);
        acousticTable.setWidths(new int[]{30, 70});

        setAcousticFirstHeader(acousticTable);
        setAcousticSecondHeader(acousticTable);

        boolean isInflow = !installation.getInflowParameters().isEmpty();
        boolean isOutflow = !installation.getOutflowParameters().isEmpty();

        if (!isInflow && !isOutflow) {
            throw new InstallationFlowIsEmpty();
        } else if (isInflow && isOutflow) {
            setInflowAndOutflowAcoustic(installation.getAcousticTable(), acousticTable, small);
        } else if (isInflow) {
            //приток
            setInflowAcoustic(installation.getAcousticTable(), acousticTable, small);

        } else if (isOutflow) {
            //вытяжка
            setOutflowAcoustic(installation.getAcousticTable(), acousticTable, small);
        }

        document.add(acousticTable);

        Paragraph underAcousticTable = new Paragraph("Примечание: уровень акустического давления приведен с учетом с учетом проведения\n" +
                "измерений в безэховом помещении на расстоянии 1 м.\n", small);
        underAcousticTable.setAlignment(Element.ALIGN_CENTER);

        document.add(underAcousticTable);

    }

    private void printWeightAndDimensions(InstallationToSelection installation, Document document) throws DocumentException {

        Paragraph weightAndDimensionsParagraph = new Paragraph("Массогабаритные характеристики", bigBold);
        weightAndDimensionsParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(weightAndDimensionsParagraph);
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(6);
        table.setKeepTogether(true);
        table.setWidths(new int[]{40, 12, 12, 12, 12, 12});
        table.setWidthPercentage(100);

        PdfPCell r1c1 = new PdfPCell();
        r1c1.setUseVariableBorders(true);
        r1c1.setBorderColor(inflowLineColor);
        r1c1.setBackgroundColor(inflowBackgroundColor);

        table.addCell(r1c1);

        PdfPCell r1c2 = new PdfPCell(new Phrase("Ширина, мм", smallBold));
        r1c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.setUseVariableBorders(true);
        r1c2.setBorderColor(inflowLineColor);
        r1c2.setBackgroundColor(inflowBackgroundColor);
        r1c2.setPaddingBottom(4);

        table.addCell(r1c2);

        PdfPCell r1c3 = new PdfPCell(new Phrase("Высота, мм", smallBold));
        r1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c3.setUseVariableBorders(true);
        r1c3.setBorderColor(inflowLineColor);
        r1c3.setBackgroundColor(inflowBackgroundColor);

        table.addCell(r1c3);

        PdfPCell r1c4 = new PdfPCell(new Phrase("Длина, мм", smallBold));
        r1c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c4.setUseVariableBorders(true);
        r1c4.setBorderColor(inflowLineColor);
        r1c4.setBackgroundColor(inflowBackgroundColor);

        table.addCell(r1c4);

        PdfPCell r1c5 = new PdfPCell(new Phrase("Основание, мм", smallBold));
        r1c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c5.setUseVariableBorders(true);
        r1c5.setBorderColor(inflowLineColor);
        r1c5.setBackgroundColor(inflowBackgroundColor);

        table.addCell(r1c5);

        PdfPCell r1c6 = new PdfPCell(new Phrase("Вес, кг", smallBold));
        r1c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c6.setUseVariableBorders(true);
        r1c6.setBorderColor(inflowLineColor);
        r1c6.setBackgroundColor(inflowBackgroundColor);

        table.addCell(r1c6);

        for (WeightAndSize weightAndSize : installation.getDrawModel().getWeightsAndSizes()){

            PdfPCell rNc1 = new PdfPCell(new Phrase(String.valueOf(weightAndSize.getName()), normal));
            rNc1.setUseVariableBorders(true);
            rNc1.setBorderColor(inflowLineColor);
            rNc1.setPaddingBottom(4);

            table.addCell(rNc1);

            PdfPCell rNc2 = new PdfPCell(new Phrase(String.valueOf(weightAndSize.getY()), normal));
            rNc2.setHorizontalAlignment(Element.ALIGN_CENTER);
            rNc2.setUseVariableBorders(true);
            rNc2.setBorderColor(inflowLineColor);

            table.addCell(rNc2);

            PdfPCell rNc3 = new PdfPCell(new Phrase(String.valueOf(weightAndSize.getZ()), normal));
            rNc3.setHorizontalAlignment(Element.ALIGN_CENTER);
            rNc3.setUseVariableBorders(true);
            rNc3.setBorderColor(inflowLineColor);

            table.addCell(rNc3);

            PdfPCell rNc4 = new PdfPCell(new Phrase(String.valueOf(weightAndSize.getX()), normal));
            rNc4.setHorizontalAlignment(Element.ALIGN_CENTER);
            rNc4.setUseVariableBorders(true);
            rNc4.setBorderColor(inflowLineColor);

            table.addCell(rNc4);

            PdfPCell rNc5 = new PdfPCell(new Phrase(String.valueOf(weightAndSize.getBasis()), normal));
            rNc5.setHorizontalAlignment(Element.ALIGN_CENTER);
            rNc5.setUseVariableBorders(true);
            rNc5.setBorderColor(inflowLineColor);

            table.addCell(rNc5);

            PdfPCell rNc6 = new PdfPCell(new Phrase(String.valueOf(weightAndSize.getWeight()), normal));
            rNc6.setHorizontalAlignment(Element.ALIGN_CENTER);
            rNc6.setUseVariableBorders(true);
            rNc6.setBorderColor(inflowLineColor);

            table.addCell(rNc6);
        }

        PdfPCell totalWeightC1 = new PdfPCell(new Phrase("Всего: ", bold));
        totalWeightC1.setUseVariableBorders(true);
        totalWeightC1.setBorderColorBottom(inflowLineColor);
        totalWeightC1.setBorder(Rectangle.BOTTOM);
        totalWeightC1.setBackgroundColor(inflowBackgroundColor);

        table.addCell(totalWeightC1);

        PdfPCell totalWeightC2 = new PdfPCell();
        totalWeightC2.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalWeightC2.setUseVariableBorders(true);
        totalWeightC2.setBorder(Rectangle.BOTTOM);
        totalWeightC2.setBorderColorBottom(inflowLineColor);
        totalWeightC2.setBackgroundColor(inflowBackgroundColor);
        totalWeightC2.setPaddingBottom(4);

        table.addCell(totalWeightC2);

        PdfPCell totalWeightC3 = new PdfPCell();
        totalWeightC3.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalWeightC3.setUseVariableBorders(true);
        totalWeightC3.setBorderColorBottom(inflowLineColor);
        totalWeightC3.setBorder(Rectangle.BOTTOM);
        totalWeightC3.setBackgroundColor(inflowBackgroundColor);

        table.addCell(totalWeightC3);

        PdfPCell totalWeightC4 = new PdfPCell();
        totalWeightC4.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalWeightC4.setUseVariableBorders(true);
        totalWeightC4.setBorderColorBottom(inflowLineColor);
        totalWeightC4.setBorder(Rectangle.BOTTOM);
        totalWeightC4.setBackgroundColor(inflowBackgroundColor);

        table.addCell(totalWeightC4);

        PdfPCell totalWeightC5 = new PdfPCell();
        totalWeightC5.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalWeightC5.setUseVariableBorders(true);
        totalWeightC5.setBorderColorBottom(inflowLineColor);
        totalWeightC5.setBorder(Rectangle.BOTTOM);
        totalWeightC5.setBackgroundColor(inflowBackgroundColor);

        table.addCell(totalWeightC5);

        PdfPCell totalWeightC6 = new PdfPCell(new Phrase(String.valueOf(installation.getDrawModel().getTotalWeight()), bold));
        totalWeightC6.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalWeightC6.setUseVariableBorders(true);
        totalWeightC6.setBorderColorBottom(inflowLineColor);
        totalWeightC6.setBorder(Rectangle.BOTTOM);
        totalWeightC6.setBackgroundColor(inflowBackgroundColor);

        table.addCell(totalWeightC6);



        document.add(table);

        Paragraph underWeightAndDimensionsParagraph = new Paragraph("Внимание: габаритные характеристики приведены без учета патрубков теплообменников.", small);
        underWeightAndDimensionsParagraph.setAlignment(Element.ALIGN_CENTER);

        document.add(underWeightAndDimensionsParagraph);



    }

    private void setAcousticFirstHeader(PdfPTable acousticTable) {

        PdfPCell r1c1 = new PdfPCell();
        r1c1.setUseVariableBorders(true);
        r1c1.setBorderColor(inflowLineColor);
        r1c1.setBackgroundColor(inflowBackgroundColor);

        acousticTable.addCell(r1c1);

        PdfPCell r1c2 = new PdfPCell(new Phrase("dB(A)", bold));
        r1c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.setUseVariableBorders(true);
        r1c2.setBorderColor(inflowLineColor);
        r1c2.setBackgroundColor(inflowBackgroundColor);

        acousticTable.addCell(r1c2);

    }

    private void setAcousticSecondHeader(PdfPTable acousticTable) {

        PdfPCell r1c1table = new PdfPCell(new Phrase("Частота, Hz", bold));
        r1c1table.setUseVariableBorders(true);
        r1c1table.setBorderColor(inflowLineColor);
        r1c1table.setBackgroundColor(inflowBackgroundColor);
        r1c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r1c1table);

        PdfPCell r1c2table = new PdfPCell();
        r1c2table.setUseVariableBorders(true);
        r1c2table.setBorderColor(inflowLineColor);
        r1c2table.setBackgroundColor(inflowBackgroundColor);

        PdfPTable r1c2 = new PdfPTable(9);
        r1c2.setWidthPercentage(100);

        PdfPCell hZCell = new PdfPCell(new Phrase("63", normal));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);


        hZCell = new PdfPCell(new Phrase("125", normal));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase("250", normal));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase("500", normal));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase("1000", normal));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase("2000", normal));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase("4000", normal));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase("8000", normal));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase("Сумма", normal));
        hZCell.setBorder(Rectangle.NO_BORDER);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        r1c2table.addElement(r1c2);

        acousticTable.addCell(r1c2table);

    }

    private void setInflowAcoustic(List<AcousticPerformanceDTO> acousticPerformanceTable, PdfPTable acousticTable, Font style) {

        //Первая строка
        PdfPCell r1c1table = new PdfPCell(new Phrase("Приток, всасывание", bold));
        r1c1table.setUseVariableBorders(true);
        r1c1table.setBorderColor(inflowLineColor);
        r1c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r1c1table);

        PdfPCell r1c2table = new PdfPCell();
        r1c2table.setUseVariableBorders(true);
        r1c2table.setBorderColor(inflowLineColor);

        PdfPTable r1c2 = new PdfPTable(9);
        r1c2.setWidthPercentage(100);

        PdfPCell hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz63(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);


        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz125(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz250(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz500(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz1000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz2000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz4000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz8000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getSum(), style));
        hZCell.setBorder(Rectangle.NO_BORDER);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        r1c2table.addElement(r1c2);

        acousticTable.addCell(r1c2table);

        //Вторая
        PdfPCell r2c1table = new PdfPCell(new Phrase("Приток, нагнетание", bold));
        r2c1table.setUseVariableBorders(true);
        r2c1table.setBorderColor(inflowLineColor);
        r2c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r2c1table);

        PdfPCell r2c2table = new PdfPCell();
        r2c2table.setUseVariableBorders(true);
        r2c2table.setBorderColor(inflowLineColor);

        PdfPTable r2c2 = new PdfPTable(9);
        r2c2.setWidthPercentage(100);

        PdfPCell hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz63(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);


        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz125(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz250(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz500(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz1000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz2000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz4000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz8000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getSum(), style));
        hZCellr2.setBorder(Rectangle.NO_BORDER);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        r2c2table.addElement(r2c2);

        acousticTable.addCell(r2c2table);

        //Третья
        PdfPCell r3c1table = new PdfPCell(new Phrase("Приток, к окружению", bold));
        r3c1table.setUseVariableBorders(true);
        r3c1table.setBorderColor(inflowLineColor);
        r3c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r3c1table);

        PdfPCell r3c2table = new PdfPCell();
        r3c2table.setUseVariableBorders(true);
        r3c2table.setBorderColor(inflowLineColor);

        PdfPTable r3c2 = new PdfPTable(9);
        r3c2.setWidthPercentage(100);

        PdfPCell hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz63(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);


        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz125(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz250(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz500(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz1000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz2000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz4000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz8000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getSum(), style));
        hZCellr3.setBorder(Rectangle.NO_BORDER);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        r3c2table.addElement(r3c2);

        acousticTable.addCell(r3c2table);

    }

    private void setOutflowAcoustic(List<AcousticPerformanceDTO> acousticPerformanceTable, PdfPTable acousticTable, Font style) {

        //Первая строка
        PdfPCell r1c1table = new PdfPCell(new Phrase("Вытяжка, всасывание", bold));
        r1c1table.setUseVariableBorders(true);
        r1c1table.setBorderColor(inflowLineColor);
        r1c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r1c1table);

        PdfPCell r1c2table = new PdfPCell();
        r1c2table.setUseVariableBorders(true);
        r1c2table.setBorderColor(inflowLineColor);

        PdfPTable r1c2 = new PdfPTable(9);
        r1c2.setWidthPercentage(100);

        PdfPCell hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz63(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);


        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz125(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz250(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz500(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz1000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz2000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz4000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz8000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getSum(), style));
        hZCell.setBorder(Rectangle.NO_BORDER);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        r1c2table.addElement(r1c2);

        acousticTable.addCell(r1c2table);

        //Вторая
        PdfPCell r2c1table = new PdfPCell(new Phrase("Вытяжка, нагнетание", bold));
        r2c1table.setUseVariableBorders(true);
        r2c1table.setBorderColor(inflowLineColor);
        r2c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r2c1table);

        PdfPCell r2c2table = new PdfPCell();
        r2c2table.setUseVariableBorders(true);
        r2c2table.setBorderColor(inflowLineColor);

        PdfPTable r2c2 = new PdfPTable(9);
        r2c2.setWidthPercentage(100);

        PdfPCell hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz63(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);


        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz125(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz250(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz500(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz1000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz2000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz4000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz8000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getSum(), style));
        hZCellr2.setBorder(Rectangle.NO_BORDER);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        r2c2table.addElement(r2c2);

        acousticTable.addCell(r2c2table);

        //Третья
        PdfPCell r3c1table = new PdfPCell(new Phrase("Вытяжка, к окружению", bold));
        r3c1table.setUseVariableBorders(true);
        r3c1table.setBorderColor(inflowLineColor);
        r3c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r3c1table);

        PdfPCell r3c2table = new PdfPCell();
        r3c2table.setUseVariableBorders(true);
        r3c2table.setBorderColor(inflowLineColor);

        PdfPTable r3c2 = new PdfPTable(9);
        r3c2.setWidthPercentage(100);

        PdfPCell hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz63(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);


        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz125(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz250(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz500(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz1000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz2000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz4000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz8000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getSum(), style));
        hZCellr3.setBorder(Rectangle.NO_BORDER);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        r3c2table.addElement(r3c2);

        acousticTable.addCell(r3c2table);

    }

    private void setInflowAndOutflowAcoustic(List<AcousticPerformanceDTO> acousticPerformanceTable, PdfPTable acousticTable, Font style) {

        //Первая строка
        PdfPCell r1c1table = new PdfPCell(new Phrase("Приток, всасывание", bold));
        r1c1table.setUseVariableBorders(true);
        r1c1table.setBorderColor(inflowLineColor);
        r1c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r1c1table);

        PdfPCell r1c2table = new PdfPCell();
        r1c2table.setUseVariableBorders(true);
        r1c2table.setBorderColor(inflowLineColor);

        PdfPTable r1c2 = new PdfPTable(9);
        r1c2.setWidthPercentage(100);

        PdfPCell hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz63(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);


        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz125(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz250(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz500(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz1000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz2000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz4000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getHz8000(), style));
        hZCell.setUseVariableBorders(true);
        hZCell.setBorder(Rectangle.RIGHT);
        hZCell.setBorderColorRight(inflowLineColor);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        hZCell = new PdfPCell(new Phrase(acousticPerformanceTable.get(0).getSum(), style));
        hZCell.setBorder(Rectangle.NO_BORDER);
        hZCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        r1c2.addCell(hZCell);

        r1c2table.addElement(r1c2);

        acousticTable.addCell(r1c2table);

        //Вторая
        PdfPCell r2c1table = new PdfPCell(new Phrase("Приток, нагнетание", bold));
        r2c1table.setUseVariableBorders(true);
        r2c1table.setBorderColor(inflowLineColor);
        r2c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r2c1table);

        PdfPCell r2c2table = new PdfPCell();
        r2c2table.setUseVariableBorders(true);
        r2c2table.setBorderColor(inflowLineColor);

        PdfPTable r2c2 = new PdfPTable(9);
        r2c2.setWidthPercentage(100);

        PdfPCell hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz63(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);


        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz125(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz250(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz500(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz1000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz2000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz4000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getHz8000(), style));
        hZCellr2.setUseVariableBorders(true);
        hZCellr2.setBorder(Rectangle.RIGHT);
        hZCellr2.setBorderColorRight(inflowLineColor);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        hZCellr2 = new PdfPCell(new Phrase(acousticPerformanceTable.get(1).getSum(), style));
        hZCellr2.setBorder(Rectangle.NO_BORDER);
        hZCellr2.setHorizontalAlignment(Element.ALIGN_CENTER);
        r2c2.addCell(hZCellr2);

        r2c2table.addElement(r2c2);

        acousticTable.addCell(r2c2table);

        //Третья
        PdfPCell r3c1table = new PdfPCell(new Phrase("Приток, к окружению", bold));
        r3c1table.setUseVariableBorders(true);
        r3c1table.setBorderColor(inflowLineColor);
        r3c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r3c1table);

        PdfPCell r3c2table = new PdfPCell();
        r3c2table.setUseVariableBorders(true);
        r3c2table.setBorderColor(inflowLineColor);

        PdfPTable r3c2 = new PdfPTable(9);
        r3c2.setWidthPercentage(100);

        PdfPCell hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz63(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz125(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz250(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz500(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz1000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz2000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz4000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getHz8000(), style));
        hZCellr3.setUseVariableBorders(true);
        hZCellr3.setBorder(Rectangle.RIGHT);
        hZCellr3.setBorderColorRight(inflowLineColor);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        hZCellr3 = new PdfPCell(new Phrase(acousticPerformanceTable.get(2).getSum(), style));
        hZCellr3.setBorder(Rectangle.NO_BORDER);
        hZCellr3.setHorizontalAlignment(Element.ALIGN_CENTER);
        r3c2.addCell(hZCellr3);

        r3c2table.addElement(r3c2);

        acousticTable.addCell(r3c2table);

        //Четвертая
        PdfPCell r4c1table = new PdfPCell(new Phrase("Вытяжка, всасывание", bold));
        r4c1table.setUseVariableBorders(true);
        r4c1table.setBorderColor(inflowLineColor);
        r4c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r4c1table);

        PdfPCell r4c2table = new PdfPCell();
        r4c2table.setUseVariableBorders(true);
        r4c2table.setBorderColor(inflowLineColor);

        PdfPTable r4c2 = new PdfPTable(9);
        r4c2.setWidthPercentage(100);

        PdfPCell hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getHz63(), style));
        hZCellr4.setUseVariableBorders(true);
        hZCellr4.setBorder(Rectangle.RIGHT);
        hZCellr4.setBorderColorRight(inflowLineColor);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getHz125(), style));
        hZCellr4.setUseVariableBorders(true);
        hZCellr4.setBorder(Rectangle.RIGHT);
        hZCellr4.setBorderColorRight(inflowLineColor);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getHz250(), style));
        hZCellr4.setUseVariableBorders(true);
        hZCellr4.setBorder(Rectangle.RIGHT);
        hZCellr4.setBorderColorRight(inflowLineColor);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getHz500(), style));
        hZCellr4.setUseVariableBorders(true);
        hZCellr4.setBorder(Rectangle.RIGHT);
        hZCellr4.setBorderColorRight(inflowLineColor);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getHz1000(), style));
        hZCellr4.setUseVariableBorders(true);
        hZCellr4.setBorder(Rectangle.RIGHT);
        hZCellr4.setBorderColorRight(inflowLineColor);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getHz2000(), style));
        hZCellr4.setUseVariableBorders(true);
        hZCellr4.setBorder(Rectangle.RIGHT);
        hZCellr4.setBorderColorRight(inflowLineColor);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getHz4000(), style));
        hZCellr4.setUseVariableBorders(true);
        hZCellr4.setBorder(Rectangle.RIGHT);
        hZCellr4.setBorderColorRight(inflowLineColor);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getHz8000(), style));
        hZCellr4.setUseVariableBorders(true);
        hZCellr4.setBorder(Rectangle.RIGHT);
        hZCellr4.setBorderColorRight(inflowLineColor);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        hZCellr4 = new PdfPCell(new Phrase(acousticPerformanceTable.get(3).getSum(), style));
        hZCellr4.setBorder(Rectangle.NO_BORDER);
        hZCellr4.setHorizontalAlignment(Element.ALIGN_CENTER);
        r4c2.addCell(hZCellr4);

        r4c2table.addElement(r4c2);

        acousticTable.addCell(r4c2table);

        //Пятая
        PdfPCell r5c1table = new PdfPCell(new Phrase("Вытяжка, нагнетание", bold));
        r5c1table.setUseVariableBorders(true);
        r5c1table.setBorderColor(inflowLineColor);
        r5c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r5c1table);

        PdfPCell r5c2table = new PdfPCell();
        r5c2table.setUseVariableBorders(true);
        r5c2table.setBorderColor(inflowLineColor);

        PdfPTable r5c2 = new PdfPTable(9);
        r5c2.setWidthPercentage(100);

        PdfPCell hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getHz63(), style));
        hZCellr5.setUseVariableBorders(true);
        hZCellr5.setBorder(Rectangle.RIGHT);
        hZCellr5.setBorderColorRight(inflowLineColor);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getHz125(), style));
        hZCellr5.setUseVariableBorders(true);
        hZCellr5.setBorder(Rectangle.RIGHT);
        hZCellr5.setBorderColorRight(inflowLineColor);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getHz250(), style));
        hZCellr5.setUseVariableBorders(true);
        hZCellr5.setBorder(Rectangle.RIGHT);
        hZCellr5.setBorderColorRight(inflowLineColor);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getHz500(), style));
        hZCellr5.setUseVariableBorders(true);
        hZCellr5.setBorder(Rectangle.RIGHT);
        hZCellr5.setBorderColorRight(inflowLineColor);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getHz1000(), style));
        hZCellr5.setUseVariableBorders(true);
        hZCellr5.setBorder(Rectangle.RIGHT);
        hZCellr5.setBorderColorRight(inflowLineColor);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getHz2000(), style));
        hZCellr5.setUseVariableBorders(true);
        hZCellr5.setBorder(Rectangle.RIGHT);
        hZCellr5.setBorderColorRight(inflowLineColor);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getHz4000(), style));
        hZCellr5.setUseVariableBorders(true);
        hZCellr5.setBorder(Rectangle.RIGHT);
        hZCellr5.setBorderColorRight(inflowLineColor);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getHz8000(), style));
        hZCellr5.setUseVariableBorders(true);
        hZCellr5.setBorder(Rectangle.RIGHT);
        hZCellr5.setBorderColorRight(inflowLineColor);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        hZCellr5 = new PdfPCell(new Phrase(acousticPerformanceTable.get(4).getSum(), style));
        hZCellr5.setBorder(Rectangle.NO_BORDER);
        hZCellr5.setHorizontalAlignment(Element.ALIGN_CENTER);
        r5c2.addCell(hZCellr5);

        r5c2table.addElement(r5c2);

        acousticTable.addCell(r5c2table);

        //Шестая
        PdfPCell r6c1table = new PdfPCell(new Phrase("Вытяжка, к окружению", bold));
        r6c1table.setUseVariableBorders(true);
        r6c1table.setBorderColor(inflowLineColor);
        r6c1table.setHorizontalAlignment(Element.ALIGN_CENTER);

        acousticTable.addCell(r6c1table);

        PdfPCell r6c2table = new PdfPCell();
        r6c2table.setUseVariableBorders(true);
        r6c2table.setBorderColor(inflowLineColor);

        PdfPTable r6c2 = new PdfPTable(9);
        r6c2.setWidthPercentage(100);

        PdfPCell hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getHz63(), style));
        hZCellr6.setUseVariableBorders(true);
        hZCellr6.setBorder(Rectangle.RIGHT);
        hZCellr6.setBorderColorRight(inflowLineColor);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getHz125(), style));
        hZCellr6.setUseVariableBorders(true);
        hZCellr6.setBorder(Rectangle.RIGHT);
        hZCellr6.setBorderColorRight(inflowLineColor);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getHz250(), style));
        hZCellr6.setUseVariableBorders(true);
        hZCellr6.setBorder(Rectangle.RIGHT);
        hZCellr6.setBorderColorRight(inflowLineColor);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getHz500(), style));
        hZCellr6.setUseVariableBorders(true);
        hZCellr6.setBorder(Rectangle.RIGHT);
        hZCellr6.setBorderColorRight(inflowLineColor);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getHz1000(), style));
        hZCellr6.setUseVariableBorders(true);
        hZCellr6.setBorder(Rectangle.RIGHT);
        hZCellr6.setBorderColorRight(inflowLineColor);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getHz2000(), style));
        hZCellr6.setUseVariableBorders(true);
        hZCellr6.setBorder(Rectangle.RIGHT);
        hZCellr6.setBorderColorRight(inflowLineColor);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getHz4000(), style));
        hZCellr6.setUseVariableBorders(true);
        hZCellr6.setBorder(Rectangle.RIGHT);
        hZCellr6.setBorderColorRight(inflowLineColor);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getHz8000(), style));
        hZCellr6.setUseVariableBorders(true);
        hZCellr6.setBorder(Rectangle.RIGHT);
        hZCellr6.setBorderColorRight(inflowLineColor);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        hZCellr6 = new PdfPCell(new Phrase(acousticPerformanceTable.get(5).getSum(), style));
        hZCellr6.setBorder(Rectangle.NO_BORDER);
        hZCellr6.setHorizontalAlignment(Element.ALIGN_CENTER);
        r6c2.addCell(hZCellr6);

        r6c2table.addElement(r6c2);

        acousticTable.addCell(r6c2table);
    }




}