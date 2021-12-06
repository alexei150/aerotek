package com.swecor.aerotek.persist.selction;

import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstHeatExchanger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoenEstHeatExchangerRepository extends JpaRepository<RoenEstHeatExchanger, Integer> {

    List<RoenEstHeatExchanger> findByModeAndCode (byte mode, String code);
}
