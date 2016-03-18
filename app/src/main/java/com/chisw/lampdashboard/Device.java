package com.chisw.lampdashboard;

import org.alljoyn.bus.AboutObjectDescription;
import org.alljoyn.bus.Variant;

import java.util.Map;

public class Device {
    private String name;
    private String friendlyName;
    private int version;
    private short port;
    private AboutObjectDescription[] objectDescriptions;
    private Map<String, Variant> aboutData;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public AboutObjectDescription[] getObjectDescriptions() {
        return objectDescriptions;
    }

    public void setObjectDescriptions(AboutObjectDescription[] objectDescriptions) {
        this.objectDescriptions = objectDescriptions;
    }

    public Map<String, Variant> getAboutData() {
        return aboutData;
    }

    public void setAboutData(Map<String, Variant> aboutData) {
        this.aboutData = aboutData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
