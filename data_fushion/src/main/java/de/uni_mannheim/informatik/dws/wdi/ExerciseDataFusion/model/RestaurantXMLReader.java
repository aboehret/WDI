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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

/**
 * A {@link XMLMatchableReader} for {@link Movie}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class RestaurantXMLReader extends XMLMatchableReader<Restaurant, Attribute>
		implements FusibleFactory<Restaurant, Attribute> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(
	 * de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Restaurant, Attribute> dataset) {
		super.initialiseDataset(dataset);

		// the schema is defined in the Movie class and not interpreted from the file,
		// so we have to set the attributes manually
		dataset.addAttribute(Restaurant.NAME);
		dataset.addAttribute(Restaurant.CITY);
		dataset.addAttribute(Restaurant.POSTAL_CODE);
		dataset.addAttribute(Restaurant.STREET);
		dataset.addAttribute(Restaurant.COUNTRY);
		dataset.addAttribute(Restaurant.LATITUDE);
		dataset.addAttribute(Restaurant.LONGITUDE);
	}

	@Override
	public Restaurant createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Restaurant restaurant = new Restaurant(id, provenanceInfo);

		// fill the attributes
		restaurant.setName(getValueFromChildElement(node, "name"));

		// String cuisineValues = getValueFromChildElement(node, "cuisine");
		// List<String> listCuisine =
		// for(i =0;i<)
		// restaurant.setCuisine(listCuisine);

		restaurant.setCountry(getNonNullValueInLowerCase(getValueFromInsideChildElement(node, "location", "country")));

		restaurant.setCity(getNonNullValueInLowerCase(getValueFromInsideChildElement(node, "location", "city")));

		String postalCode = getNonNullValueInLowerCase(getValueFromInsideChildElement(node, "location", "postalcode"));
		postalCode = postalCode.replace("-", "");
		restaurant.setPostalcode(postalCode);

		String streetName = getNonNullValueInLowerCase(getValueFromInsideChildElement(node, "location", "street"));
		streetName = streetName.replace("-", "");
		restaurant.setStreet(streetName);

		String region = getNonNullValueInLowerCase(getValueFromInsideChildElement(node, "location", "region"));
		restaurant.setRegion(region);
		String province = getNonNullValueInLowerCase(getValueFromInsideChildElement(node, "location", "province"));
		restaurant.setProvince(province);
		





		String latitude = getNonNullValueInLowerCase(
				getValueFromInsideChildOfChildElement(node, "location", "geographiclocation", "latitude"));
		restaurant.setLatitude(latitude);
				System.out.println("latitude" + latitude);

		String longitude = getNonNullValueInLowerCase(
				getValueFromInsideChildOfChildElement(node, "location", "geographiclocation", "longitude"));
				restaurant.setLongitude(longitude);
				System.out.println("longitude" + longitude);

		System.out.println(
				id + " " + restaurant.getName() + " " + restaurant.getPostalcode() + " " + restaurant.getCountry());

		return restaurant;
	}

	public String getNonNullValueInLowerCase(String value) {
		if (value != null) {
			value = value.toLowerCase();
		} else {
			value = "";
		}
		return value;
	}

	public String getValueFromInsideChildElement(Node node, String childName, String ChildOfChildName) {

		// get all child nodes
		NodeList children = node.getChildNodes();

		// iterate over the child nodes until the node with childName is found
		for (int j = 0; j < children.getLength(); j++) {
			Node child = children.item(j);
			// check the node type and the name
			if (child.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && child.getNodeName().equals(childName)) {

				NodeList childrenofChild = child.getChildNodes();
				for (int i = 0; i < childrenofChild.getLength(); i++) {
					Node innerChild = childrenofChild.item(i);

					if (innerChild.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
							&& innerChild.getNodeName().equals(ChildOfChildName)) {
						return innerChild.getTextContent().trim();
					}
				}
			}
		}

		return null;
	}

	public String getValueFromInsideChildOfChildElement(Node node, String childName, String ChildOfChildName,
			String ChildOfChildOfChildName) {

		// get all child nodes
		NodeList children = node.getChildNodes();

		// iterate over the child nodes until the node with childName is found
		for (int j = 0; j < children.getLength(); j++) {
			Node child = children.item(j);
			// check the node type and the name
			if (child.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && child.getNodeName().equals(childName)) {

				NodeList childrenofChild = child.getChildNodes();
				for (int i = 0; i < childrenofChild.getLength(); i++) {
					Node innerChild = childrenofChild.item(i);

					if (innerChild.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
							&& innerChild.getNodeName().equals(ChildOfChildName)) {

						NodeList childrenofChildofChild = innerChild.getChildNodes();

						for (int k = 0; k < childrenofChildofChild.getLength(); k++) {
							Node innerOfInnerChild = childrenofChildofChild.item(k);

							if (innerOfInnerChild.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
									&& innerOfInnerChild.getNodeName().equals(ChildOfChildOfChildName)) {
								return innerOfInnerChild.getTextContent().trim();
							}
						}
					}
				}
			}
		}

		return null;
	}

	@Override
	public Restaurant createInstanceForFusion(RecordGroup<Restaurant, Attribute> cluster) {

		List<String> ids = new LinkedList<>();
		// System.out.println("Coming in instance for fusion");
		for (Restaurant m : cluster.getRecords()) {
			ids.add(m.getIdentifier());
		}

		Collections.sort(ids);

		String mergedId = StringUtils.join(ids, '+');

		return new Restaurant(mergedId, "fused");
	}
}
