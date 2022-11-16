
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

	private Graph graph;
	private Country country;
	private ArrayList<Border> borders = new ArrayList<Border>();
	private ArrayList<Country> countries = new ArrayList<Country>();
	private boolean bBorder = false;
	//Ok normalement sauf boolean
	private Map<String, Border> bordersGraph = new HashMap<String, Border>();
	private Map<Country, List<Border>> country_borders = new HashMap<Country, List<Border>>();
	private Map<String, Country> countries_graph = new HashMap<String, Country>();

	public SAXHandler() {
		//OK
		this.graph = new Graph();
	}

	public Graph getGraph() {
		for (Country country : countries) {
			country_borders.put(country, country.getBorders());
			countries_graph.put(country.getCca3(), country);
			System.out.println(country.getName());
		}
		graph.setBorders(bordersGraph);
		graph.setCountryBorders(country_borders);
		graph.setCoutries(countries_graph);
		
		
		return graph;
	}
	
	//cca3,latlng,name,population,region
	
	//methode appel� quand on voit une balise ouvrante
	@Override
	public void startElement(String uri,String localName, String qName, Attributes attributes) throws SAXException{
		if("country".equalsIgnoreCase(qName)) {
			country = new Country(attributes.getValue("capital"), attributes.getValue("cca3"), uri, qName, attributes.getValue("latlng"), attributes.getValue("name"),
					Integer.parseInt(attributes.getValue("population")), attributes.getValue("region"),
					attributes.getValue("subregion"));
			countries.add(country);
		}else if("border".equalsIgnoreCase(qName)) {
			bBorder = true;
		}
		
		//TO Finish
	}
	
	
	//methode qui est appel� quand on voit une balise fermante
	@Override
	public void endElement(String uri, String localName, String qName) {
		List<Border>toPut = new ArrayList<>(borders);
		
		if(qName.equalsIgnoreCase("country")) {
			country.setBorders(toPut);
			
			for (Border border : borders) {
				System.out.println(border.getBorder());
			}
			
			System.out.println("-----------------------------------------");
			
			borders.removeAll(borders);
		}
	}
	
	// Donn�es � l'int�rieur de balise
	@Override
	public void characters(char[] ch,int start, int lenght) throws SAXException {
		if(bBorder) {
			Border border = new Border(new String(ch,start,lenght));
			borders.add(border);
			bordersGraph.put(border.getBorder(), border);
			
			bBorder =false;
			
			
		}
	}

}
