package com.chariotsolutions.nfc.plugin;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.content.Intent;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


public class MiFare {

  static JSONObject readNFC(JSONArray dataJson, Intent intent){

    String respuesta = "";
    Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
    MifareClassic mfc = MifareClassic.get(tagFromIntent);
    try {
      if(!mfc.isConnected()) {
        mfc.connect();
      }
      MiFareData mfdata=jsonToData(dataJson);
      boolean auth = mfc.authenticateSectorWithKeyA(mfdata.getSector() , mfdata.getLlave());
      if(auth){
        byte[] data = mfc.readBlock((mfdata.getSector()*4)+mfdata.getBloque());
         respuesta=bytesToHex(data);
      }else{
        respuesta="Login Fail";
      }
      mfc.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    JSONObject json = new JSONObject();
    try{
       json.put("TagData", respuesta);
       json.put("Id",bytesToHex(tagId));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json;
  }

  public static String bytesToHex(byte[] in) {
    final StringBuilder builder = new StringBuilder();
    for(byte b : in) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }
  static boolean writeNFC(JSONArray dataJson, Intent intent){

    boolean wrote =false;
    Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
    MifareClassic mfc = MifareClassic.get(tagFromIntent);
    try {
      if(!mfc.isConnected()){
        mfc.connect();
      }

      MiFareData mfdata=jsonToData(dataJson);
      boolean idValidation=Arrays.equals(mfdata.getId(),tagId);
      if(idValidation){



        boolean auth = mfc.authenticateSectorWithKeyA(mfdata.getSector() , mfdata.getLlave());
      if(auth){
        mfc.writeBlock((mfdata.getSector()*4)+mfdata.getBloque(), mfdata.getData());
        wrote =true;
      }
      mfc.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return wrote;
  }

  static MiFareData jsonToData(JSONArray dataJson){

    MiFareData mfdata= new MiFareData();
    JSONObject object;
    try {
       object = new JSONObject(dataJson.getString(0));
        if(object.has("Sector")) {
          mfdata.setSector((Integer) object.get("Sector"));
        }
        if(object.has("Block")) {
          mfdata.setBloque((Integer) object.get("Block"));
        }
        if(object.has("Key")) {
          mfdata.setLlave(hexStringToByteArray(String.valueOf(object.get("Key"))));
        }
        if(object.has("Data")) {
          mfdata.setData(hexStringToByteArray(String.valueOf(object.get("Data"))));
        }
      if(object.has("Id")) {
        mfdata.setId(hexStringToByteArray(String.valueOf(object.get("Id"))));
      }
    } catch (JSONException e) {
       e.printStackTrace();
    }
    return mfdata;
  }

  public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
        + Character.digit(s.charAt(i+1), 16));
    }
    return data;
  }
}
