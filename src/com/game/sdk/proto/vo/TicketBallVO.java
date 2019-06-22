package com.game.sdk.proto.vo;

public class TicketBallVO {
    private String id;
    private int type;
    private float ticket;
    private GiveTicketVO param;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getTicket() {
        return ticket;
    }

    public void setTicket(float ticket) {
        this.ticket = ticket;
    }

    public GiveTicketVO getParam() {
        return param;
    }

    public void setParam(GiveTicketVO param) {
        this.param = param;
    }
}
