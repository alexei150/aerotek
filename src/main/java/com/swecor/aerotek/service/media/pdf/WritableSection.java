package com.swecor.aerotek.service.media.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;

public interface WritableSection {

    void write(SectionToConfigurator section, Font normal, Font bold, Font bigBolt, BaseColor inflowLine, BaseColor backgroundColor, Document document) throws DocumentException;
}
