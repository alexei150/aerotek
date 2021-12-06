package com.swecor.aerotek.service.media.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import org.springframework.stereotype.Component;

@Component
public class SectionWriter implements WritableSection {

    @Override
    public void write(SectionToConfigurator section,
                      Font normal,
                      Font bold,
                      Font bigBolt,
                      BaseColor flowLine,
                      BaseColor backgroundColor,
                      Document document) throws DocumentException {

        PdfPTable sectionTable = new PdfPTable(1);
        sectionTable.setWidthPercentage(100);
        sectionTable.setKeepTogether(true);
        PdfPCell headerCell = new PdfPCell(new Phrase(section.getSectionType().getName(), bigBolt));
        headerCell.setUseVariableBorders(true);
        headerCell.setPaddingBottom(4);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.TOP);
        headerCell.setBorderColorTop(flowLine);
        headerCell.setBackgroundColor(backgroundColor);

        sectionTable.addCell(headerCell);

        PdfPTable parametersTable = new PdfPTable(2);
        parametersTable.setWidthPercentage(100);
        parametersTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell formParametersCell = new PdfPCell();
        formParametersCell.setUseVariableBorders(true);
        formParametersCell.setBorder(Rectangle.RIGHT);
        formParametersCell.setBorderColorRight(flowLine);
        formParametersCell.addElement(getForm(section, normal, bold));

        parametersTable.addCell(formParametersCell);

        parametersTable.addCell(getCalculated(section, normal, bold));

        PdfPCell parametersTableCell = new PdfPCell(parametersTable);
        parametersTableCell.setBorder(Rectangle.NO_BORDER);

        sectionTable.addCell(parametersTableCell);

        document.add(sectionTable);
    }

    private PdfPTable getForm(SectionToConfigurator section, Font normal, Font bold){
        PdfPTable formTable = new PdfPTable(2);
        formTable.setWidthPercentage(100);

        for (ParameterToSelection parameter : section.getForm()){
            PdfPCell parameterNameCell = new PdfPCell(new Phrase(parameter.getParam().getName(), normal));
            parameterNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            parameterNameCell.setBorder(Rectangle.NO_BORDER);

            formTable.addCell(parameterNameCell);

            PdfPCell parameterValueCell = new PdfPCell(new Phrase(parameter.getValue(), bold));
            parameterValueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            parameterValueCell.setBorder(Rectangle.NO_BORDER);

            formTable.addCell(parameterValueCell);
        }

        return formTable;
    }

    private PdfPTable getCalculated(SectionToConfigurator section, Font normal, Font bold){

        PdfPTable formTable = new PdfPTable(2);
        formTable.setWidthPercentage(100);

        for (ParameterToSelection parameter : section.getCalculated()){
            PdfPCell parameterNameCell = new PdfPCell(new Phrase(parameter.getParam().getName(), normal));
            parameterNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            parameterNameCell.setBorder(Rectangle.NO_BORDER);

            formTable.addCell(parameterNameCell);

            PdfPCell parameterValueCell = new PdfPCell(new Phrase(parameter.getValue() + " " + parameter.getParam().getUnit(), bold));
            parameterValueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            parameterValueCell.setBorder(Rectangle.NO_BORDER);

            formTable.addCell(parameterValueCell);
        }
        return formTable;

    }
}
