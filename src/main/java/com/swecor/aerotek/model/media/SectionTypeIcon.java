package com.swecor.aerotek.model.media;

import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.library.sectionType.SectionType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "section_type_icon")
public class SectionTypeIcon extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_type_id")
    private SectionType sectionType;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private MediaContent image;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "icon_base_id")
    private MediaContent iconBase;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "icon_inflow_to_right_id")
    private MediaContent iconInflowToRight;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "icon_inflow_to_left_id")
    private MediaContent iconInflowToLeft;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "icon_outflow_to_right_id")
    private MediaContent iconOutflowToRight;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "icon_outflow_to_left_id")
    private MediaContent iconOutflowToLeft;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionTypeIcon that = (SectionTypeIcon) o;
        return Objects.equals(id, that.id) && Objects.equals(image, that.image) && Objects.equals(iconBase, that.iconBase) && Objects.equals(iconInflowToRight, that.iconInflowToRight) && Objects.equals(iconInflowToLeft, that.iconInflowToLeft) && Objects.equals(iconOutflowToRight, that.iconOutflowToRight) && Objects.equals(iconOutflowToLeft, that.iconOutflowToLeft);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, iconBase, iconInflowToRight, iconInflowToLeft, iconOutflowToRight, iconOutflowToLeft);
    }
}
