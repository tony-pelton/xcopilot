package com.dsrts.xcopilot;

import gov.nasa.xpc.XPlaneConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.SocketException;

@Service
@ManagedResource
public class XPlaneConnectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XPlaneConnectService.class);

    private XPlaneConnect xPlaneConnect;

    private GeoPoint planePosition;

    // ksts
    private GeoPoint planePositionKsts = new GeoPoint(38.51513f,-122.81252f);
    // nonsense
    private GeoPoint planePositionNonsense = new GeoPoint(38.51513f,122.81252f);

    public XPlaneConnectService() throws SocketException {
        this.xPlaneConnect = new XPlaneConnect();
        this.planePosition = planePositionKsts;
    }

    public GeoPoint getPlanePosition() {
        return planePosition;
    }

    @ManagedAttribute
    public int getPlanePositionTest() {
        if(planePosition == planePositionKsts) {
            return 1;
        } else {
            return 2;
        }
    }
    @ManagedAttribute
    public void setPlanePositionTest(int pos) {
        switch (pos) {
            case 1:
                planePosition = planePositionKsts;
                break;
            case 2:
                planePosition = planePositionNonsense;
                break;
        }
    }

    @EventListener
    private void sendDREF(XPlaneConnectSendEvent event) {
        LOGGER.info("sendDREF(event) : "+event.toString());
        sendDREF(event.getDref(),event.getFvalue());
    }

    private void sendDREF(String dref,float value) {
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
            planePosition = new GeoPoint(posi[0],posi[1]);
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
}
