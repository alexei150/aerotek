package com.swecor.aerotek.model.library.section;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "air_consumption")
public class AirConsumption extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "consumption")
    private Integer consumption;

    @Column(name = "pressure")
    private Float pressure;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Section.class)
    @JoinColumn(name = "section_id")
    private Section section;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirConsumption that = (AirConsumption) o;
        return id.equals(that.id) && consumption.equals(that.consumption) && pressure.equals(that.pressure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consumption, pressure);
    }

    @Override
    public String toString() {
        return "AirConsumption{" +
                "id=" + id +
                ", consumption=" + consumption +
                ", pressure=" + pressure +
                ", sectionId=" + section.getId() +
                '}';
    }
}
