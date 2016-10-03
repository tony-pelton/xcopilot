package com.dsrts.xcopilot;

import gov.nasa.xpc.XPlaneConnect;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.SocketException;

@Service
public class XPlaneConnectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XPlaneConnectService.class);

    private XPlaneConnect xPlaneConnect;

    private PlanePosition planePosition;

    // ksts
    private PlanePosition planePositionKsts = new PlanePosition(38.51513f,-122.81252f);
    // nonsense
    private PlanePosition planePositionNonsense = new PlanePosition(38.51513f,122.81252f);

    public XPlaneConnectService() throws SocketException {
        this.xPlaneConnect = new XPlaneConnect();
        this.planePosition = planePositionKsts;
    }

    public void test() {
        if(planePositionKsts == planePosition) {
            planePosition = planePositionNonsense;
        } else {
            planePosition = planePositionKsts;
        }
    }

    public PlanePosition getPlanePosition() {
        return planePosition;
    }

    public void sendDREF(String dref,float value) {
        try {
            synchronized (xPlaneConnect) {
                xPlaneConnect.sendDREF(dref,value);
            }
        } catch (IOException e) {
            LOGGER.warn("sendDREF()",e);
        }
    }

    @Scheduled(fixedRate = 5000)
    private void scheduled() {
        try {
            float[] posi;
            synchronized (xPlaneConnect) {
                posi = xPlaneConnect.getPOSI(0);
            }
            planePosition = new PlanePosition(posi[0],posi[1]);
            LOGGER.info(planePosition.toString());
        } catch (IOException e) {
            LOGGER.warn("scheduled() : IO Exception; x-plane running ?");
        }
    }

    @PreDestroy
    private void destroy() throws Exception {
        LOGGER.info("destroy()");
        xPlaneConnect.close();
    }
    public static class PlanePosition {
        private float lattitude;
        private float longitude;

        public PlanePosition(float lattitude, float longitude) {
            this.lattitude = lattitude;
            this.longitude = longitude;
        }

        public float getLattitude() {
            return lattitude;
        }

        public float getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
