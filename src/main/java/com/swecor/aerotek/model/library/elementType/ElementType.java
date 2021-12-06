package com.swecor.aerotek.model.library.elementType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.library.element.Element;
import com.swecor.aerotek.model.library.parameter.Parameter;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "element_type")
public class ElementType extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "note")
    private String note;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "elementType")
    private Set<Element> elements;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parameter_element_type",
            joinColumns = {@JoinColumn(name = "element_type_id")},
            inverseJoinColumns = {@JoinColumn(name = "parameter_id")})
    private Set<Parameter> parameters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementType that = (ElementType) o;
        return id.equals(that.id) && name.equals(that.name) && note.equals(that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, note);
    }
}
