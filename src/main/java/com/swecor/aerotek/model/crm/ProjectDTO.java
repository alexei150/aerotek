package com.swecor.aerotek.model.crm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private Integer id;

    private String name;
    private String createdDate;
    private String address;
    private String company;
    private String manager;
    private String userName;
    private Integer installationsCount;
    private List<Integer> installationsIDs;
    private List<InstallationDTO> installations;
}
