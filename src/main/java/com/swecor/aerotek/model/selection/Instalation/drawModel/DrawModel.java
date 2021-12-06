package com.swecor.aerotek.model.selection.Instalation.drawModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrawModel {

    List<WeightAndSize> weightsAndSizes;
    double totalWeight;
}
