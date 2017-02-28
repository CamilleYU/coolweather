package com.coolweather.app.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY =1;
	public static final int LEVEL_COUNTY =2;
	private static boolean result;
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response,int currentLevel){
		List<Province> allProvinces=new ArrayList<Province>();
		if(!TextUtils.isEmpty(response)){//ÅÐ¶Ï×Ö·û´®ÊÇ·ñÎª¿Õ
			allProvinces=parseXMLWithSAX(response.toString(),currentLevel);
			for(int i=0;i<allProvinces.size();i++){
				coolWeatherDB.saveProvince(allProvinces.get(i));
			}
			return true;
			/*String[] allProvinces=response.split(",");
			if(allProvinces !=null && allProvinces.length>0){
				for(String p:allProvinces){
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}*/
		}
		
		return false;
	}
	
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId,int currentLevel){
		List<City> allCities=new ArrayList<City>();
		if(!TextUtils.isEmpty(response)){
			allCities=parseXMLWithSAXByProvinceId(response.toString(),currentLevel);
			for(int i=0;i<allCities.size();i++){
				allCities.get(i).setProvinceId(provinceId);
				coolWeatherDB.saveCity(allCities.get(i));
			}
			return true;
		}
		/*if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities !=null && allCities.length>0){
				for(String c:allCities){
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;	
			}
			
		}*/
		return false;
	}
	
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId,int currentLevel){
		List<County> allCounties=new ArrayList<County>();
		if(!TextUtils.isEmpty(response)){
			allCounties=parseXMLWithSAXByCityId(response.toString(),currentLevel);
			for(int i=0;i<allCounties.size();i++){
				allCounties.get(i).setCityId(cityId);
				coolWeatherDB.saveCounty(allCounties.get(i));	
			}
			return true;
		}
		
		return false;
		/*if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			if(allCounties !=null && allCounties.length>0){
				for(String c:allCounties){
					String[] array=c.split("\\|");
					County county=new County();
					county.setCityId(cityId);
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					coolWeatherDB.saveCounty(county);			
				}
				return true;		
			}
			
		}
		return false;*/
	}
	
	private static List<Province> parseXMLWithSAX(String xmlData,int currentLevel){
		SaxHandler handler=null;
		try {
			SAXParserFactory factory=SAXParserFactory.newInstance();
			XMLReader xmlReader=factory.newSAXParser().getXMLReader();
			handler=new SaxHandler(currentLevel);
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(xmlData)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return handler.getProvincesList();
		
	}
	
	private static List<City> parseXMLWithSAXByProvinceId(String xmlData,int currentLevel){
		SaxHandler handler=null;
		try {
			SAXParserFactory factory=SAXParserFactory.newInstance();
			XMLReader xmlReader=factory.newSAXParser().getXMLReader();
			handler=new SaxHandler(currentLevel);
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(xmlData)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return handler.getCitiesList();
		
	}
	
	
	private static List<County> parseXMLWithSAXByCityId(String xmlData,int currentLevel){
		SaxHandler handler=null;
		try {
			SAXParserFactory factory=SAXParserFactory.newInstance();
			XMLReader xmlReader=factory.newSAXParser().getXMLReader();
			handler=new SaxHandler(currentLevel);
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(xmlData)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return handler.getCountiesList();
	}
	
	

}
