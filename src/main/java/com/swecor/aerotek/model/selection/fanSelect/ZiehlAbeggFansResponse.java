package com.swecor.aerotek.model.selection.fanSelect;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZiehlAbeggFansResponse {

    @JsonProperty("CALC_ALTITUDE")
    private double calcAltitude;
    @JsonProperty("ZA_MAINS_SUPPLY")
    private String zaMainsSupply;
    @JsonProperty("CALC_AIR_DENSITY")
    private double calcAirDensity;
    @JsonProperty("ZA_PSYS")
    private double zaPsys;
    @JsonProperty("DENSITY_INFLUENCE")
    private String densityInfluence;
    @JsonProperty("ZA_LW6")
    private double zaLw6;
    @JsonProperty("ZA_LW5")
    private double zaLw5;
    @JsonProperty("ZA_FBP")
    private double zaFbp;
    @JsonProperty("ZA_SFP_CLASS")
    private double zaSfpClass;
    @JsonProperty("INSTALLATION_HEIGHT_MM")
    private double installationHeightMm;
    @JsonProperty("ZA_SFP")
    private double zaSfp;
    @JsonProperty("ZA_PSF")
    private double zaPsf;
    @JsonProperty("ERP_N_TRAGET")
    private String erpNTraget;
    @JsonProperty("ZA_WEIGHT")
    private double zaWeight;
    @JsonProperty("ZA_ETAF_SYS")
    private double zaEtafSys;
    @JsonProperty("INSTALLATION_POS_VU")
    private String installationPosVu;
    @JsonProperty("INSTALLATION_POS_VO")
    private String installationPosVo;
    @JsonProperty("ZA_ETAF_L")
    private double zaEtafL;
    @JsonProperty("ZA_BG")
    private double zaBg;
    @JsonProperty("ZA_IN")
    private double zaIn;
    @JsonProperty("ZA_UN")
    private double zaUn;
    @JsonProperty("ZA_PL")
    private double zaPl;
    @JsonProperty("ZA_PF")
    private double zaPf;
    @JsonProperty("ZA_PD")
    private double zaPD;
    @JsonProperty("ZA_P1")
    private double zaP1;
    @JsonProperty("ZA_QV")
    private double zaQv;
    @JsonProperty("ERP_VSD")
    private String erpVsd;
    @JsonProperty("NOMINAL_IECMOTOR_EFFICIENCY")
    private double nominalIecmotorEfficiency;
    @JsonProperty("ARTICLE_NO")
    private String articleNo;
    @JsonProperty("IS_VALID")
    private String isValid;
    @JsonProperty("INSTALLATION_POS")
    private String installationPos;
    @JsonProperty("ERP_N_STAT")
    private double erpNStat;
    @JsonProperty("FEI_FACTOR")
    private double feiFactor;
    @JsonProperty("INSTALLATION_LENGTH_MM")
    private double installationLengthMm;
    @JsonProperty("ZA_ETAM")
    private double zaEtam;
    @JsonProperty("ZA_ETAF")
    private double zaEtaf;
    @JsonProperty("ZA_PF_MAINS_OPERATED")
    private double zaPfMainsOperated;
    @JsonProperty("NOZZLE_GUARD")
    private String nozzleGuard;
    @JsonProperty("MOTOR_POLES")
    private double motorPoles;
    @JsonProperty("ZA_N")
    private double zaN;
    @JsonProperty("ZA_F")
    private double zaF;
    @JsonProperty("ZA_U")
    private double zaU;
    @JsonProperty("ZA_NMAX")
    private double zaNmax;
    @JsonProperty("INSTALLATION_POS_H")
    private String installationPosH;
    @JsonProperty("ZA_LWA6")
    private double zaLwa6;
    @JsonProperty("ZA_LWA5")
    private double zaLwa5;
    @JsonProperty("ZAWALL_ARRANGEMENT")
    private double zawallArrangement;
    @JsonProperty("ZA_PSF_MAINS_OPERATED")
    private double zaPsfMainsOperated;
    @JsonProperty("MEASUREMENT_ID")
    private String measurementID;
    @JsonProperty("PRODUCT_IMG")
    private String productImg;
    @JsonProperty("ZA_QV_MAINS_OPERATED")
    private double zaQvMainsOperated;
    @JsonProperty("MOTOR_IE_CLASS")
    private String motorIeClass;
    @JsonProperty("INDEX")
    private double index;
    @JsonProperty("GRILL_INFLUENCE")
    private String grillInfluence;
    @JsonProperty("ZA_ETASF_SYS")
    private double zaEtasfSys;
    @JsonProperty("ZA_ETASF_L")
    private double zaEtasfL;
    @JsonProperty("IDCTEXT")
    private String idctext;
    @JsonProperty("AIR_VELOCITY")
    private double airVelocity;
    @JsonProperty("SESSIONID")
    private String sessionid;
    @JsonProperty("ERP_METHOD")
    private String erpMethod;
    @JsonProperty("CALC_PL_MAX")
    private double calcPlMax;
    @JsonProperty("INSTALLATION_WIDTH_MM")
    private double installationWidthMm;
    @JsonProperty("ERP_CLASS")
    private String erpClass;
    @JsonProperty("ZA_ETAF_L_MAINS_OPERATED")
    private double zaEtafLMainsOperated;
    @JsonProperty("ZA_ETASF_L_MAINS_OPERATED")
    private double zaEtasfLMainsOperated;
    @JsonProperty("KFACTOR")
    private double kfactor;
    @JsonProperty("ZA_ETASF")
    private double zaEtasf;
    @JsonProperty("TYPE")
    private String type;
    @JsonProperty("IS_EC")
    private String isEc;
    @JsonProperty("ERP_N_ACTUAL")
    private String erpNActual;
    @JsonProperty("CALC_N_RATED")
    private double calcNRated;
    @JsonProperty("FAN_EFFICIENCY_GRADE")
    private double fanEfficiencyGrade;

    @JsonProperty("EC_TYPE")
    private String ecType;
    @JsonProperty("CAPACITOR_CAPACITANCE")
    private String capacitorCapacitance;
    @JsonProperty("COSPHI")
    private String cosphi;
    @JsonProperty("NOMINAL_SPEED")
    private String nominalSpeed;
    @JsonProperty("EFFICIENCY_TOT")
    private String efficiencyTot;
    @JsonProperty("CIRCUIT")
    private String circuit;
    @JsonProperty("NOMINAL_VOLTAGE")
    private double nominalVoltage;
    @JsonProperty("EFFICIENCY_STAT")
    private String efficiencyStat;
    @JsonProperty("PROTECTION_CLASS_IP")
    private String protectionClassIP;
    @JsonProperty("NOMINAL_FREQUENCY")
    private String nominalFrequency;
    @JsonProperty("PHASE_DIFFERENCE")
    private String phaseDifference;
    @JsonProperty("MIN_TEMPERATURE_C")
    private double minTemperatureC;
    @JsonProperty("CAPACITOR_VOLTAGE")
    private String capacitorVoltage;
    @JsonProperty("PROTECTION_CLASS_THCL")
    private String protectionClassThcl;
    @JsonProperty("CURRENT_PHASE")
    private String currentPhase;
    @JsonProperty("NOMINAL_CURRENT")
    private double nominalCurrent;
    @JsonProperty("INCREASE_OF_CURRENT")
    private String increaseOfCurrent;
    @JsonProperty("MAX_TEMPERATURE_C")
    private double maxTemperatureC;
    @JsonProperty("POWER_OUTPUT_KW")
    private double powerOutputKw;
    @JsonProperty("VOLTAGE_TOLERANCE")
    private String voltageTolerance;
    @JsonProperty("MAX_FREQUENCY")
    private double maxFrequency;
}
