package com.swecor.aerotek.model.library.section;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.library.element.Element;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "section_element")
public class SectionElementsCount extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id")
    private Element element;

    @Column(name = "element_count")
    private Integer elementsCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionElementsCount that = (SectionElementsCount) o;
        return id.equals(that.id) && section.equals(that.section) && element.equals(that.element) && elementsCount.equals(that.elementsCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, elementsCount);
    }

    @Override
    public String toString() {
        return "SectionElementsCount{" +
                "id=" + id +
                '}';
    }
}
