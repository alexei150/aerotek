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
@Table(name = "acoustic_performance")
public class AcousticPerformance extends Auditing {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "hz_63")
    private double hz63;
    @Column(name = "hz_125")
    private double hz125;
    @Column(name = "hz_250")
    private double hz250;
    @Column(name = "hz_500")
    private double hz500;
    @Column(name = "hz_1000")
    private double hz1000;
    @Column(name = "hz_2000")
    private double hz2000;
    @Column(name = "hz_4000")
    private double hz4000;
    @Column(name = "hz_8000")
    private double hz8000;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    public AcousticPerformance(double hz63, double hz125, double hz250, double hz500, double hz1000, double hz2000, double hz4000, double hz8000) {
        this.hz63 = hz63;
        this.hz125 = hz125;
        this.hz250 = hz250;
        this.hz500 = hz500;
        this.hz1000 = hz1000;
        this.hz2000 = hz2000;
        this.hz4000 = hz4000;
        this.hz8000 = hz8000;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcousticPerformance that = (AcousticPerformance) o;
        return Double.compare(that.id, id) == 0 && Double.compare(that.hz63, hz63) == 0 && Double.compare(that.hz125, hz125) == 0 && Double.compare(that.hz250, hz250) == 0 && Double.compare(that.hz500, hz500) == 0 && Double.compare(that.hz1000, hz1000) == 0 && Double.compare(that.hz2000, hz2000) == 0 && Double.compare(that.hz4000, hz4000) == 0 && Double.compare(that.hz8000, hz8000) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hz63, hz125, hz250, hz500, hz1000, hz2000, hz4000, hz8000);
    }

    @Override
    public String toString() {
        return "AcousticPerformance{" +
                "id=" + id +
                ", hz63=" + hz63 +
                ", hz125=" + hz125 +
                ", hz250=" + hz250 +
                ", hz500=" + hz500 +
                ", hz1000=" + hz1000 +
                ", hz2000=" + hz2000 +
                ", hz4000=" + hz4000 +
                ", hz8000=" + hz8000 +
                '}';
    }
}
