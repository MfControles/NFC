package com.chariotsolutions.nfc.plugin;

public class MiFareData {

  private int sector;
  private int bloque;
  private byte[] llave;
  private byte[] data;
  private byte[] Id;

  public int getBloque() {
    return bloque;
  }

  public void setBloque(int bloque) {
    this.bloque = bloque;
  }

  public byte[] getLlave() {
    return llave;
  }

  public void setLlave(byte[] llave) {
    this.llave = llave;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
  public byte[] getId() {return  Id;}

  public void setId(byte[] Id) {
    this.Id = Id;
  }

  public int getSector() {
    return sector;
  }

  public void setSector(int sector) {
    this.sector = sector;
  }

}
