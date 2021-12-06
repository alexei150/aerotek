package com.swecor.aerotek.model.library.parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.library.elementType.ElementType;
import com.swecor.aerotek.model.library.parameterValue.ParameterValue;
import com.swecor.aerotek.model.library.sectionType.SectionType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parameter")
public class Parameter extends Auditing {

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

    //todo Изменил List на Set, по всем законам логики должен быть Set, но он тут изначально и был и что то мне взбрело в голову его изменить на List, вроде все работает, но на всякий случяай подожди) может что отвалится
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parameter_section_type",
            joinColumns = {@JoinColumn(name = "parameter_id")},
            inverseJoinColumns = {@JoinColumn(name = "section_type_id")})
    private Set<SectionType> sectionTypes;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parameter_element_type",
            joinColumns = {@JoinColumn(name = "parameter_id")},
            inverseJoinColumns = {@JoinColumn(name = "element_type_id")})
    private Set<ElementType> elementTypes;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter", cascade = CascadeType.ALL)
    private Set<ParameterValue> parameterValues;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(id, parameter.id) && Objects.equals(name, parameter.name) && Objects.equals(minValue, parameter.minValue) && Objects.equals(maxValue, parameter.maxValue) && Objects.equals(unit, parameter.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minValue, maxValue, unit);
    }
}
