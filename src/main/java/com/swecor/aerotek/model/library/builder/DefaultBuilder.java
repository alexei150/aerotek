package com.swecor.aerotek.model.library.builder;

import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.selection.Instalation.builder.InflowOrOutflowToBuilder;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "installation_builder")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class DefaultBuilder extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @NotNull(message = "inflow is null")
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "inflow")
    private InflowOrOutflowToBuilder inflow;
    @NotNull(message = "outflow is null")
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "outflow")
    private InflowOrOutflowToBuilder outflow;
    @Column(name = "outflow_is_up")
    private boolean outflowIsUp;

}
