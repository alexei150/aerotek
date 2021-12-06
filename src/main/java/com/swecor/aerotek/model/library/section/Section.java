package com.swecor.aerotek.model.library.section;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.library.parameterValue.ParameterValue;
import com.swecor.aerotek.model.library.sectionType.SectionType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "section")
public class Section extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "drawing_code")
    private String drawingCode;
    @Column(name = "description")
    private String description;
    @Column(name = "note")
    private String note;
    @Column(name = "build_coefficient")
    private Float buildCoefficient;
    @Column(name = "hardware_coefficient")
    private Float hardwareCoefficient;
    @Column(name = "section_area")
    private Float sectionArea;
    @Column(name = "standard_size")
    private Byte standardSize;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_type_id")
    private SectionType sectionType;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "section_id")
    private Set<ParameterValue> parametersValues;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "section")
    private Set<SectionElementsCount> elements;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "section")
    private AcousticPerformance acousticPerformance;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "section")
    private Set<AirConsumption> airConsumptions;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "section")
    private Set<VariableElement> variableElements;

    @JsonGetter("sectionType")
    public String getTheSectionType(){
        return sectionType.getName();
    }

    @JsonGetter("sectionTypeCode")
    public String getTheSectionTypeCode(){
        return sectionType.getCode();
    }
}
