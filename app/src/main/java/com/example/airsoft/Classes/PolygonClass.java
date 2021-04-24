package com.example.airsoft.Classes;

public class PolygonClass {
    private String polygonKey, polygonName, polygonAddress, polygonOrgcomID;
    private boolean polygonActuality;

    public PolygonClass(String polygonKey){
        this.polygonKey=polygonKey;
    }

    public String getPolygonKey(){return polygonKey;}
    public void setPolygonKey(String polygonKey){this.polygonKey=polygonKey;}

    public String getPolygonName(){return polygonName;}
    public void setPolygonName(String polygonName){this.polygonName=polygonName;}

    public String getPolygonAddress(){return polygonAddress;}
    public void setPolygonAddress(String polygonAddress){this.polygonAddress=polygonAddress;}

    public String getPolygonOrgcomID(){return polygonOrgcomID;}
    public void setPolygonOrgcomID(String polygonOrgcomID){this.polygonOrgcomID=polygonOrgcomID;}

    public boolean getPolygonActuality(){return polygonActuality;}
    public void setPolygonActuality(boolean polygonActuality){this.polygonActuality=polygonActuality;}
}
