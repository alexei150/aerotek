package com.swecor.aerotek.model.selection.Instalation.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.library.sectionType.SectionType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "default_parameter")
public class DefaultParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "min_value")
    private String minValue;
    @Column(name = "max_value")
    private String maxValue;
    @Column(name = "unit")
    private String unit;

    @JsonIgnore
    @Column(name = "is_calculating")
    private boolean isCalculating;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_type_id")
    private SectionType sectionType;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "defaultParameter")
    private List<DefaultParameterDictionary> dictionary;

}
