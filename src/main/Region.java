package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Region {
	private String name;
	private String code;
	private List<Region> subRegions;
	private Map<String,Region> nameRegionMap;
	
	public Region(){
		nameRegionMap=new HashMap<String,Region>();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setSubRegions(List<Region> subRegions){
		this.subRegions=subRegions;
	}
	
	public List<Region> getSubRegions(){
		return this.subRegions;
	}

	public Map<String,Region> getNameRegionMap() {
		return nameRegionMap;
	}

	public void setNameRegionMap(Map<String,Region> nameRegionMap) {
		this.nameRegionMap = nameRegionMap;
	}
}
