package com.swecor.aerotek.model.library.element;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElementCountResponse {

    private Integer id;
    private String name;
    private Integer count;
}
