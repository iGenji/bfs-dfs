

import java.util.ArrayList;
import java.util.List;
//Finish
public class Country {
	
	private String capital;
	private String cca3;
	private String currency;
	private String languages;
	private String latlng;
	private String name;
	private int population;
	private String region;
	private String subRegion;
	private List<Border> borders;
	private long popDepuisDebut;
	
	
	public Country(String capital, String cca3, String currency, String languages, String latlng, String name,
			int population, String asia, String subRegion) {
		super();
		this.capital = capital;
		this.cca3 = cca3;
		this.currency = currency;
		this.languages = languages;
		this.latlng = latlng;
		this.name = name;
		this.population = population;
		this.region = asia;
		this.subRegion = subRegion;
		this.borders = new ArrayList<Border>();
		this.popDepuisDebut = population;
	}


	public String getCapital() {
		return capital;
	}


	public void setCapital(String capital) {
		this.capital = capital;
	}


	public String getCca3() {
		return cca3;
	}


	public void setCca3(String cca3) {
		this.cca3 = cca3;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getLanguages() {
		return languages;
	}


	public void setLanguages(String languages) {
		this.languages = languages;
	}


	public String getLatlng() {
		return latlng;
	}


	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getPopulation() {
		return population;
	}


	public void setPopulation(int population) {
		this.population = population;
	}


	public String getAsia() {
		return region;
	}


	public void setAsia(String asia) {
		this.region = asia;
	}


	public String getSubRegion() {
		return subRegion;
	}


	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}


	public List<Border> getBorders() {
		return borders;
	}


	public void setBorders(List<Border> borders) {
		this.borders = borders;
	}
	


	public long getPopDepuisDebut() {
		return popDepuisDebut;
	}


	public void setPopDepuisDebut(long popDepuisDebut) {
		this.popDepuisDebut = popDepuisDebut;
	}


	@Override
	public String toString() {
		return "Country [capital=" + capital + ", cca3=" + cca3 + ", currency=" + currency + ", languages=" + languages
				+ ", latlng=" + latlng + ", name=" + name + ", population=" + population + ", region=" + region
				+ ", subRegion=" + subRegion + "]";
	}
	
	
	
	
	
	
	
	
	

}
