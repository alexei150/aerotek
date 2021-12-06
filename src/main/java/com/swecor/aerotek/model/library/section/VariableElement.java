package com.swecor.aerotek.model.library.section;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "variable_element")
public class VariableElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "order_key")
    private Integer orderKey;
    @Column(name = "standard_size")
    private Byte standardSize;
    @Column(name = "index")
    private String index;
    @Column(name = "section_type_id")
    private Integer sectionTypeId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Section.class)
    @JoinColumn(name = "section_id")
    private Section section;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableElement that = (VariableElement) o;
        return Objects.equals(id, that.id) && Objects.equals(orderKey, that.orderKey) && Objects.equals(standardSize, that.standardSize) && Objects.equals(index, that.index) && Objects.equals(sectionTypeId, that.sectionTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderKey, standardSize, index, sectionTypeId);
    }

    @Override
    public String toString() {
        return "VariableElement{" +
                "id=" + id +
                ", orderKey=" + orderKey +
                ", standardSize=" + standardSize +
                ", index='" + index + '\'' +
                ", sectionTypeId=" + sectionTypeId +
                '}';
    }
}
