package model;

import java.util.concurrent.ConcurrentHashMap;

public class Size {
    private ConcurrentHashMap<String,Integer> serwery;

    public Size() {
        this.serwery = new ConcurrentHashMap<>();
    }
    public Integer getSize(String serwer){
        return serwery.get(serwer);
    }
    public void dodajSerwer(String serwer, int rozmiar)
    {
        serwery.put(serwer,rozmiar);
    }
    public String getBestSerwer()
    {


     return serwery.get("SERWER1")> serwery.get("SERWER2") ? "SERWER2": "SERWER1";
    }
}
