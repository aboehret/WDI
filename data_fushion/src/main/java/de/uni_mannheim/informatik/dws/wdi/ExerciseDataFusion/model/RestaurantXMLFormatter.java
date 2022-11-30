/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Movie}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class RestaurantXMLFormatter extends XMLFormatter<Restaurant> {

	

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("restaurant");
	}

	@Override
	public Element createElementFromRecord(Restaurant record, Document doc) {
		Element restaurant = doc.createElement("restaurant");

		restaurant.appendChild(createTextElement("id", record.getIdentifier(), doc));

		restaurant.appendChild(createTextElementWithProvenance("name",
				record.getName(),record.getMergedAttributeProvenance(Restaurant.NAME), doc));
		
//		restaurant.appendChild(createTextElement("cuisine", record.getCuisine(), doc));
		
//		restaurant.appendChild(createTextElementWithProvenance("city",
//				record.getCity(),record.getMergedAttributeProvenance(Restaurant.CITY), doc));
		
		restaurant.appendChild(createActorsElement(record, doc));

		

		return restaurant;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}
	
	
	protected Element createActorsElement(Restaurant record, Document doc) {
		Element location = doc.createElement("location");
		
//		Element actorRoot = actorFormatter.createRootElement(doc);
//		location.setAttribute("provenance",
//				record.getMergedAttributeProvenance(Restaurant.LOCATION));

//		for (Actor a : record.getActors()) {
//			actorRoot.appendChild(actorFormatter
//					.createElementFromRecord(a, doc));
//		}

		
		System.out.println("record.getCity()"+record.getCity());
		if(record.getCity()!=null) {
			location.appendChild(createTextElementWithProvenance("city",
					record.getCity(),record.getMergedAttributeProvenance(Restaurant.CITY), doc));
		}
		System.out.println("record.getCountry()"+record.getCountry());
		if(record.getCountry()!=null) {
			
			location.appendChild(createTextElement("country", record.getCountry(), doc));
		
		}
		System.out.println("record.getPostalcode()"+record.getPostalcode());
		if(record.getPostalcode()!=null) {
			
			location.appendChild(createTextElement("postalcode", record.getPostalcode(), doc));
		
		}
		

		return location;
	}


}