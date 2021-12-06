package com.swecor.aerotek.model.library.element;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.library.elementType.ElementType;
import com.swecor.aerotek.model.library.parameterValue.ParameterValue;
import com.swecor.aerotek.model.library.section.SectionElementsCount;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "element")
public class Element extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "brand")
    private String brand;
    @Column(name = "code")
    private String code;
    @Column(name = "index")
    private String index;
    @Column(name = "cost_price")
    private Float costPrice;
    @Column(name = "description")
    private String description;
    @Column(name = "note")
    private String note;
    @Column(name = "drawing_code")
    private String drawingCode;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "element_id")
    private Set<ParameterValue> parametersValues;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_type_id")
    private ElementType elementType;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "element_id")
    private Set<SectionElementsCount> sections;

    @JsonGetter("elementType")
    public String getTheElementType(){
        return elementType.getName();
    }

}
