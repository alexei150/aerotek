package com.swecor.aerotek.model.selection.Instalation.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "default_parameter_dictionary")
public class DefaultParameterDictionary {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;
    @Column(name = "value")
    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_parameter_id")
    private DefaultParameter defaultParameter;

    @Override
    public String toString() {
        return "DefaultParameterDictionary{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
