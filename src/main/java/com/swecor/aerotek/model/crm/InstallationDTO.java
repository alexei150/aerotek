package com.swecor.aerotek.model.crm;

import com.swecor.aerotek.model.selection.Instalation.InstallationSelectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class InstallationDTO implements Comparable<InstallationDTO>{

    private Integer id;
    private String name;
    private String calculated;
    private String createdDate;
    private InstallationSelectionStatus status;
    private String consumption;

    @Override
    public int compareTo(InstallationDTO o) {
        return this.getId() - o.getId();
    }
}
