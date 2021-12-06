package com.swecor.aerotek.model.selection.Instalation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.crm.Project;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.model.selection.Instalation.builder.InstallationBuilderDTO;
import com.swecor.aerotek.model.selection.Instalation.drawModel.DrawModel;
import com.swecor.aerotek.model.selection.Instalation.temperature.FlowTemperature;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "installation_to_selection")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class InstallationToSelection extends Auditing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "inflow_pressure")
    private Integer inflowPressure;
    @Column(name = "outflow_pressure")
    private Integer outflowPressure;
    @Column(name = "inflow_consumption")
    private Integer inflowConsumption;
    @Column(name = "outflow_consumption")
    private Integer outflowConsumption;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "inflow_temperature")
    private FlowTemperature inflowTemperature;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "outflow_temperature")
    private FlowTemperature outflowTemperature;
    @Column(name = "standard_size")
    private byte standardSize;
    @Column(name = "description")
    private String description;
    //Состояние подбора
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private InstallationSelectionStatus status;
    @Column(name = "thickness")
    //Опциональное оснащение(25мм/45мм)
    private Integer thickness;
    //Область задания опционального оснащения. Гибка вставка
    @Column(name = "insertion")
    private boolean insertion;
    //Исполнение(внутренее=false или наружное=true)
    @Column(name = "outside")
    private boolean outside;
    @Column(name = "summer_mode")
    private boolean summerMode;
    @Column(name = "winter_mode")
    private boolean winterMode;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "builder")
    private InstallationBuilderDTO builder;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "inflow_parameters")
    private List<SectionToConfigurator> inflowParameters;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "outflow_parameters")
    private List<SectionToConfigurator> outflowParameters;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "draw_model")
    private DrawModel drawModel;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "acoustic_table")
    private List<AcousticPerformanceDTO> acousticTable;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstallationToSelection that = (InstallationToSelection) o;
        return standardSize == that.standardSize && insertion == that.insertion && outside == that.outside && summerMode == that.summerMode && winterMode == that.winterMode && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(inflowPressure, that.inflowPressure) && Objects.equals(outflowPressure, that.outflowPressure) && Objects.equals(inflowConsumption, that.inflowConsumption) && Objects.equals(outflowConsumption, that.outflowConsumption) && Objects.equals(inflowTemperature, that.inflowTemperature) && Objects.equals(outflowTemperature, that.outflowTemperature) && Objects.equals(description, that.description) && status == that.status && Objects.equals(thickness, that.thickness) && Objects.equals(builder, that.builder) && Objects.equals(inflowParameters, that.inflowParameters) && Objects.equals(outflowParameters, that.outflowParameters) && Objects.equals(drawModel, that.drawModel) && acousticTable.equals(that.acousticTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, inflowPressure, outflowPressure, inflowConsumption, outflowConsumption, inflowTemperature, outflowTemperature, standardSize, description, status, thickness, insertion, outside, summerMode, winterMode, builder, inflowParameters, outflowParameters, drawModel, acousticTable);
    }

    //todo без понятия что это за сущность, судя по всему что то связанное с ножками
//    private Basis basis;
//    private MediaContent icon;
//    private MediaContent image;
}
