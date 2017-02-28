package com.coolweather.app.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;



public class SaxHandler extends DefaultHandler {
	private List<Province> provincesList;
	private List<City> citiesList;
	private List<County> countiesList;
	private Province province;
	private City city;
	private County county;
	private String nodeName=null;
	private String tagName;
	private CoolWeatherDB coolWeatherDB;
	private int currentLevel;
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY =1;
	public static final int LEVEL_COUNTY =2;
	
	public SaxHandler(int currentLevel) {
		super();
		this.currentLevel = currentLevel;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String value=new String(ch, start, length);
		if(tagName!=null){
			if(currentLevel ==LEVEL_PROVINCE){
				if(tagName.equals("pyName")){
					province.setProvincePyName(value);
				}else if(tagName.equals("quName")){
					province.setProvinceName(value);	
				}
			
			}else if(currentLevel ==LEVEL_CITY){
				if(tagName.equals("pyName")){
					city.setCityPyName(value);
				}else if(tagName.equals("cityname")){
					city.setCityName(value);	
				}else if(tagName.equals("url")){
					city.setCityCode(value);
				}
			}else{
				if(tagName.equals("cityname")){
					county.setCountyName(value);	
				}else if(tagName.equals("url")){
					county.setCountyCode(value);
				}
			}
		}

	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(qName.equals("city")){
			provincesList.add(province);
			citiesList.add(city);
			countiesList.add(county);
			//coolWeatherDB.saveProvince(province);
			//coolWeatherDB.saveCity(city);
			//coolWeatherDB.saveCounty(county);
			province=null;
			city=null;
			county=null;
			
		}
		tagName=null;
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		//super.startDocument();
		provincesList=new ArrayList<Province>();
		citiesList=new ArrayList<City>();
		countiesList=new ArrayList<County>();
		
		
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
			if(qName.equals("city")){
				if(attributes !=null){
					if(attributes.getValue("quName") !=null){
						province=new Province();
						if(attributes !=null){
							province.setProvinceName(attributes.getValue("quName"));
							province.setProvincePyName(attributes.getValue("pyName"));
						}
					}else{
						if(currentLevel==LEVEL_CITY){
							city=new City();
							city.setCityName(attributes.getValue("cityname"));
							city.setCityPyName(attributes.getValue("pyName"));
							city.setCityCode(attributes.getValue("url"));
							
						}else if(currentLevel ==LEVEL_COUNTY){
							county=new County();
							county.setCountyName(attributes.getValue("cityname"));
							county.setCountyCode(attributes.getValue("url"));
							
						}
					}
					
				}
				
			}
		tagName=qName;
	}

	public List<Province> getProvincesList() {
		return provincesList;
	}

	public void setProvincesList(List<Province> provincesList) {
		this.provincesList = provincesList;
	}

	public List<City> getCitiesList() {
		return citiesList;
	}

	public void setCitiesList(List<City> citiesList) {
		this.citiesList = citiesList;
	}

	public List<County> getCountiesList() {
		return countiesList;
	}

	public void setCountiesList(List<County> countiesList) {
		this.countiesList = countiesList;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public County getCounty() {
		return county;
	}

	public void setCounty(County county) {
		this.county = county;
	}



}
