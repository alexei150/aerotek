package com.swecor.aerotek.model.selection.integration.heaterExchanger;

import com.swecor.aerotek.model.Auditing;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roenest_heat_exchanger")
public class RoenEstHeatExchanger extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "mode")
    private Byte mode;
    @Column(name = "code")
    private String code;
    @Column(name = "geometry")
    private Integer geometry;
    @Column(name = "length")
    private Integer length;
    @Column(name = "height")
    private Integer height;
    @Column(name = "num_rows")
    private Integer numRows;
    @Column(name = "tubes_type")
    private Integer tubesType;
    @Column(name = "fin_spacing")
    private Integer finSpacing;
    @Column(name = "fin_type")
    private Integer finType;
    @Column(name = "circuits_type")
    private Integer circuitsType;
    @Column(name = "num_circuits")
    private Integer numCircuits;
    @Column(name = "header_configuration")
    private Integer headerConfiguration;
    @Column(name = "header_value_1")
    private Integer headerValue1;
    @Column(name = "header_value_2")
    private Integer headerValue2;
}
