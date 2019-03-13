package com.game.domain.consume;

public class Consume {
    private String openId;
    private String name;
    private String phone;
    private String address;
    private int itemId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getOpenId() {
        return openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {

        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setOpenId(String openId) {
        this.openId = openId;

    }
}
