package com.dsrts.xcopilot.service;

import com.dsrts.xcopilot.event.XPlaneConnectReceiveEvent;
import com.dsrts.xcopilot.event.XcopilotEvent;
import gov.nasa.xpc.XPlaneConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

@Service
public class XPlaneConnectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XPlaneConnectService.class);

    private XPlaneConnect xPlaneConnect;
    private ApplicationEventPublisher eventPublisher;

    private AtomicBoolean running = new AtomicBoolean();

    public XPlaneConnectService(ApplicationEventPublisher eventPublisher) throws SocketException {
        this.xPlaneConnect = new XPlaneConnect();
        this.eventPublisher = eventPublisher;
    }

    @EventListener(condition = "#event.getValue('senddref') != null")
    private void sendDREF(XcopilotEvent event) {
        LOGGER.info("sendDREF(event) : "+event.toString());
        sendDREF(event.getValue("senddref"),event.getValue("value"));
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

    @Scheduled(fixedRate = 10000)
    private void scheduled() {
        try {
            synchronized (xPlaneConnect) {
                DREF[] d_drefs = {
                        DREF.SIM_FLIGHTMODEL_POSITION_LATITUDE,
                        DREF.SIM_FLIGHTMODEL_POSITION_LONGITUDE
                };
                String[] s_drefs = new String[d_drefs.length];
                for(int idx = 0;idx < d_drefs.length;idx++) {
                    s_drefs[idx] = d_drefs[idx].getDref();
                }
                float[][] drefs_values = xPlaneConnect.getDREFs(s_drefs);
                if(!running.getAndSet(true)) {
                    LOGGER.info("scheduled() : x-plane is responding");
                }
                eventPublisher.publishEvent(new XPlaneConnectReceiveEvent(XPlaneConnectReceiveEvent.Group.TELEMETRY,d_drefs,drefs_values));
                if (LOGGER.isDebugEnabled()) {
                    for(int y = 0;y < drefs_values.length;y++) {
                        for(int x = 0; x < drefs_values[y].length;x++) {
                            LOGGER.debug(format("dref [%s] [%d]",s_drefs[y],drefs_values[y][x]),String.valueOf(drefs_values[y][x]));
                        }
                    }
                }
            }
        } catch (IOException e) {
            if(running.getAndSet(false)) {
                LOGGER.info("scheduled() : x-plane is not responding");
            }
        }
    }

    @PreDestroy
    private void destroy() throws Exception {
        xPlaneConnect.close();
    }
}
