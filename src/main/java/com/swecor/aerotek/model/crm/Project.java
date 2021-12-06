package com.swecor.aerotek.model.crm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swecor.aerotek.model.Auditing;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "project")
public class Project extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "company")
    private String company;
    @Column(name = "manager")
    private String manager;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "project")
    private Set<InstallationToSelection> installations;
}
