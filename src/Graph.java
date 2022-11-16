
import java.util.ArrayDeque;

import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.io.File;
import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;



public class Graph {
	
	private Map<String, Country> countries;
	private Map<String, Border> borders;
	private Map<Country, List<Border>> countryBorders;
	
	public Graph(){
		countries = new HashMap<String, Country>();
		borders = new HashMap<String, Border>();
		countryBorders = new HashMap<Country, List<Border>>();
	}

	public void calculerItineraireMinimisantNombreDeFrontieres(String cca3Depart, String cca3Arrive, String fichierDeSauvegarde) {
		
		/*
		 * ***********ALGO BFS***********
		 */
		
		LinkedList<Country>fileCountries = new LinkedList<Country>();
		Map<Country,Country>sommetsVisite = new HashMap<Country,Country>();
		
		Country departCountry = countries.get(cca3Depart);
 		Country arriveCountry = countries.get(cca3Arrive);
		
		fileCountries.add(departCountry);
		sommetsVisite.put(fileCountries.getFirst(), null);
		
		while(!fileCountries.isEmpty()) {
			
			
			
			Country element = fileCountries.getFirst();
			List<Border> borders = countryBorders.get(element);
			fileCountries.removeFirst();
			if(borders != null) {
				for(int i = 0; i<borders.size();i++) {
					Country e = countries.get(borders.get(i).getBorder());
					if(!sommetsVisite.containsKey(e)) {
						sommetsVisite.put(e, element);
						fileCountries.add(e);
					}
				}
			}
		}
		
		if(!sommetsVisite.containsKey(arriveCountry)) {
			throw new IllegalArgumentException();
		}
		
		Deque<Country> pileTemp = new ArrayDeque<Country>();
		pileTemp.push(arriveCountry);
		Country element = sommetsVisite.get(arriveCountry);
		
		while (element!=null) {
			pileTemp.push(element);
			element = sommetsVisite.get(element);
		}
		
		
		
		
		 
		try {
			
			//Creation de l'arbre et des elements
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("itineraire");
			doc.appendChild(rootElement);
			
			//ajout de l'attribut arrivee
			Attr arrivee = doc.createAttribute("arrivee");
			arrivee.setValue(countries.get(cca3Arrive).getName());
			rootElement.setAttributeNode(arrivee);
			
			//ajout de l'attribut depart
			Attr depart = doc.createAttribute("depart");
			depart.setValue(countries.get(cca3Depart).getName());
			rootElement.setAttributeNode(depart);
			
			//ajout de l'attribut nbPays
			Attr nbPays = doc.createAttribute("nbPays");
			nbPays.setValue(""+pileTemp.size());
			rootElement.setAttributeNode(nbPays);
			
			//ajout de l'attribut sommePopulation
			Attr sommePopulation = doc.createAttribute("sommePopulation");
			rootElement.setAttributeNode(sommePopulation);
			
			long sommePopu = 0;
			
			
			while(!pileTemp.isEmpty()) {
				Element pays = doc.createElement("pays");
				
				Attr cca3 = doc.createAttribute("cca3");
				cca3.setValue(pileTemp.getFirst().getCca3());
				pays.setAttributeNode(cca3);
				
				Attr nom = doc.createAttribute("nom");
				nom.setValue(pileTemp.getFirst().getName());
				pays.setAttributeNode(nom);
				
				
				Attr population = doc.createAttribute("population");
				population.setValue(""+pileTemp.getFirst().getPopulation());
				pays.setAttributeNode(population);
				
				
				sommePopu+=pileTemp.getFirst().getPopulation();
				
				
				rootElement.appendChild(pays);
				
				pileTemp.pop();
				
			}
			
			sommePopulation.setValue(""+sommePopu);
			
			//Creation du fichier
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transfomer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fichierDeSauvegarde));
			transfomer.transform(source, result);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
	}

	public void calculerItineraireMinimisantPopulationTotale(String cca3Depart, String cca3Arrive, String fichierDeSauvegarde) {
		
		Comparator<Country> comparator = new Comparator<Country>() {

			@Override
			public int compare(Country o1, Country o2) {
				int popDepuisDebut = (int)o1.getPopDepuisDebut()-(int)o2.getPopDepuisDebut();
				if(popDepuisDebut==0) {
					return o1.getPopulation()-o2.getPopulation();
				}
				return popDepuisDebut;
			}
			
		};
		System.out.println("******* Algo Dijkstra *******");
		ArrayDeque<Country> ordre = new ArrayDeque<Country>();
		SortedMap<Country,Long> etiquettesProv = new TreeMap<Country, Long>(comparator);
		HashMap<Country, Long> etiquettesDef = new HashMap<Country, Long>();
		
		Country origine = countries.get(cca3Depart);
		Country destination = countries.get(cca3Arrive);
		
		Country ctyCourant = origine;
		
		
		etiquettesProv.put(origine, (long)origine.getPopulation());
		
		HashMap<Country, Country> countryPrecedent = new HashMap<Country, Country>();
		countryPrecedent.put(origine, origine);
		
		while(!ctyCourant.equals(destination)) {
			Country ctyMin = etiquettesProv.firstKey();
			
			etiquettesDef.put(ctyMin, ctyMin.getPopDepuisDebut());
			etiquettesProv.remove(ctyMin);
			
			
			ctyCourant = ctyMin;
			
			for(Border b:ctyCourant.getBorders()) {
				Country c = countries.get(b.getBorder());
				if(!etiquettesDef.containsKey(c)) {
					long sommePopu;
					if(!etiquettesProv.containsKey(c)) {
						sommePopu = Long.valueOf(etiquettesDef.get(ctyCourant)+c.getPopulation());
						c.setPopDepuisDebut(sommePopu);
						etiquettesProv.put(c, sommePopu);
						countryPrecedent.put(c, ctyCourant);
					}else {
						Country bal = countryPrecedent.get(c);
						if(etiquettesDef.get(bal)+c.getPopulation()<etiquettesProv.get(c)){
							etiquettesProv.remove(c);
							sommePopu =c.getPopulation()+etiquettesDef.get(ctyCourant);
							c.setPopDepuisDebut(sommePopu);
							etiquettesProv.put(c, sommePopu);
							countryPrecedent.put(c, ctyCourant);
						}
					}
				}
			}
			
		}
		
		Country baladeur = ctyCourant;
		long popTotal = 0;
		
		while(!baladeur.equals(origine)) {
			System.out.println(baladeur.getCca3());;
			popTotal+=baladeur.getPopulation();
			
			ordre.push(baladeur);
			baladeur=countryPrecedent.get(baladeur);
			
		}
		
		ordre.push(baladeur);
		popTotal+=baladeur.getPopulation();
		System.out.println(origine.getCca3());
		System.out.println("nbPays:" + ordre.size());
		System.out.println("popTotal:"+popTotal);
		
		
		
try {
			
			//Creation de l'arbre et des elements
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("itineraire");
			doc.appendChild(rootElement);
			
			//ajout de l'attribut arrivee
			Attr arrivee = doc.createAttribute("arrivee");
			arrivee.setValue(countries.get(cca3Arrive).getName());
			rootElement.setAttributeNode(arrivee);
			
			//ajout de l'attribut depart
			Attr depart = doc.createAttribute("depart");
			depart.setValue(countries.get(cca3Depart).getName());
			rootElement.setAttributeNode(depart);
			
			//ajout de l'attribut nbPays
			Attr nbPays = doc.createAttribute("nbPays");
			nbPays.setValue(""+ordre.size());
			rootElement.setAttributeNode(nbPays);
			
			//ajout de l'attribut sommePopulation
			Attr sommePopulation = doc.createAttribute("sommePopulation");
			sommePopulation.setValue(""+popTotal);
			rootElement.setAttributeNode(sommePopulation);
			
			while(!ordre.isEmpty()) {
				Element pays = doc.createElement("pays");
				
				Attr cca3 = doc.createAttribute("cca3");
				cca3.setValue(ordre.getFirst().getCca3());
				pays.setAttributeNode(cca3);
				
				Attr nom = doc.createAttribute("nom");
				nom.setValue(ordre.getFirst().getName());
				pays.setAttributeNode(nom);
				
				
				Attr population = doc.createAttribute("population");
				population.setValue(""+ordre.getFirst().getPopulation());
				pays.setAttributeNode(population);
				
				
				
				
				
				rootElement.appendChild(pays);
				
				ordre.pop();
			}
			
			//Creation du fichier
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transfomer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fichierDeSauvegarde));
			transfomer.transform(source, result);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void setCoutries(Map<String, Country> coutries) {
		this.countries = coutries;
	}

	public Map<String, Border> getBorders() {
		return borders;
	}

	public void setBorders(Map<String, Border> borders) {
		this.borders = borders;
	}

	public Map<Country, List<Border>> getCountry_borders() {
		return countryBorders;
	}

	public void setCountryBorders(Map<Country, List<Border>> country_borders) {
		this.countryBorders = country_borders;
	}

}
