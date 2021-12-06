package com.swecor.aerotek.model.selection.Instalation.drawModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightAndSize implements Comparable<WeightAndSize>{

    private String name;
    //Длина
    private int x;
    //Ширина
    private int y;
    //Высота
    private int z;
    private String basis;
    private double weight;
    @JsonIgnore
    private int position;

    @Override
    public int compareTo(WeightAndSize weightAndSize) {
        return this.position - weightAndSize.position;
    }
}
