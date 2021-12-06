package com.swecor.aerotek.model.library.sectionType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.media.SectionTypeIcon;
import com.swecor.aerotek.model.selection.Instalation.flow.DefaultParameter;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "section_type")
public class SectionType extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "note")
    private String note;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parameter_section_type",
            joinColumns = {@JoinColumn(name = "section_type_id")},
            inverseJoinColumns = {@JoinColumn(name = "parameter_id")})
    private Set<Parameter> parameters;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sectionType")
    private Set<Section> Sections;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sectionType")
    private Set<DefaultParameter> defaultParameters;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sectionType")
    private SectionTypeIcon sectionTypeIcons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionType that = (SectionType) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, note);
    }

    @Override
    public String toString() {
        return "SectionType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
