package com.swecor.aerotek.model.library.parameterValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.library.element.Element;
import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.library.section.Section;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parameter_value")
public class ParameterValue extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "value")
    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Element.class)
    @JoinColumn(name = "element_id")
    private Element element;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Section.class)
    @JoinColumn(name = "section_id")
    private Section section;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Parameter.class)
    @JoinColumn(name = "parameter_id")
    private Parameter parameter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterValue that = (ParameterValue) o;
        return id.equals(that.id) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }

    @Override
    public String toString() {
        return "ParameterValue{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
