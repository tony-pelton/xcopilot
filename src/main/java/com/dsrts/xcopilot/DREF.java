package com.dsrts.xcopilot;

enum DREF {
    SIM_FLIGHTMODEL_POSITION_LATITUDE("sim/flightmodel/position/latitude"),
    SIM_FLIGHTMODEL_POSITION_LONGITUDE("sim/flightmodel/position/longitude"),
    SIM_FLIGHTMODEL_POSITION_MAGNETIC_VARIATION("sim/flightmodel/position/magnetic_variation"),
    SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ("sim/cockpit/radios/nav1_freq_hz"),
    SIM_COCKPIT_RADIOS_NAV1_OBS_DEGT("sim/cockpit/radios/nav1_obs_degt"),
    SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ("sim/cockpit/radios/nav2_freq_hz"),
    SIM_COCKPIT_RADIOS_NAV2_OBS_DEGT("sim/cockpit/radios/nav2_obs_degt");

    private String dref;

    DREF(String dref) {
        this.dref = dref;
    }

    String getDref() {
        return dref;
    }
}
