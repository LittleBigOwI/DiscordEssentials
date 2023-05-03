package net.philocraft.models;

import java.util.UUID;

public class Link {
    
    private UUID uuid;
    private String userid;
    private String code;

    public Link(UUID uuid, String userid, String code) {
        this.uuid = uuid;
        this.userid = userid;
        this.code = code;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getUserID() {
        return this.userid;
    }

    public String getCode() {
        return this.code;
    }

    public boolean hasUUID() {
        return (this.uuid != null);
    }

    public boolean hasUserID() {
        return (this.userid != null);
    }

    public boolean isComplete() {
        return (this.hasUUID() && this.hasUserID());
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUserID(String userid) {
        this.userid = userid;
    }

}
