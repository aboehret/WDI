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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

/**
 * A {@link AbstractRecord} representing a movie.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class Restaurant extends AbstractRecord<Attribute> implements Serializable {

	/*
	 * example entry <movie> <id>academy_awards_2</id> <title>True Grit</title>
	 * <director> <name>Joel Coen and Ethan Coen</name> </director> <actors> <actor>
	 * <name>Jeff Bridges</name> </actor> <actor> <name>Hailee Steinfeld</name>
	 * </actor> </actors> <date>2010-01-01</date> </movie>
	 */

	private static final long serialVersionUID = 1L;

	public Restaurant(String identifier, String provenance) {
		super(identifier, provenance);
	}

//	protected String provenance;
	private String name;
//	private String cuisine; // List

	private List<String> cuisine;

	private String facilitiesandservices; // List
	private String telephone;
	private String website;
	private String openinghours;
	private String country;
	private String city;
	private String postalcode;
	private String street;
	private String region;
	private String province;
	private Double latitude;
	private Double longitude;
	private Integer reviewcount;
	private Integer rating;
	private String award;
	private String reviews; // List
	// private Hashtable<String, String>() reviews;
	private String currency;
	private String pricerange;
	private Integer minprice;
	private Integer maxprice;

	@Override
	public String getIdentifier() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCuisine() {
		return cuisine;
	}

	public void setCuisine(List<String> cuisine) {
		this.cuisine = cuisine;
	}

	public String getFacilitiesandservices() {
		return facilitiesandservices;
	}

	public void setFacilitiesandservices(String facilitiesandservices) {
		this.facilitiesandservices = facilitiesandservices;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getOpeninghours() {
		return openinghours;
	}

	public void setOpeninghours(String openinghours) {
		this.openinghours = openinghours;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Integer getReviewcount() {
		return reviewcount;
	}

	public void setReviewcount(Integer reviewcount) {
		this.reviewcount = reviewcount;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public String getReviews() {
		return reviews;
	}

	public void setReviews(String reviews) {
		this.reviews = reviews;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPricerange() {
		return pricerange;
	}

	public void setPricerange(String pricerange) {
		this.pricerange = pricerange;
	}

	public Integer getMinprice() {
		return minprice;
	}

	public void setMinprice(Integer minprice) {
		this.minprice = minprice;
	}

	public Integer getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(Integer maxprice) {
		this.maxprice = maxprice;
	}

	private Map<Attribute, Collection<String>> provenance = new HashMap<>();
	private Collection<String> recordProvenance;


	public void setRecordProvenance(Collection<String> provenance) {
		recordProvenance = provenance;
	}

	public Collection<String> getRecordProvenance() {
		return recordProvenance;
	}

	public void setAttributeProvenance(Attribute attribute,
			Collection<String> provenance) {
		this.provenance.put(attribute, provenance);
	}

	public Collection<String> getAttributeProvenance(String attribute) {
		return provenance.get(attribute);
	}

	public String getMergedAttributeProvenance(Attribute attribute) {
		Collection<String> prov = provenance.get(attribute);

		if (prov != null) {
			return StringUtils.join(prov, "+");
		} else {
			return "";
		}
	}
	
	public static final Attribute NAME = new Attribute("Name");
	public static final Attribute CITY = new Attribute("City");
	public static final Attribute POSTAL_CODE = new Attribute("Postal code");
	public static final Attribute LOCATION = new Attribute("Location");
	public static final Attribute COUNTRY = new Attribute("Country");
	
	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==NAME)
			return getName() != null && !getName().isEmpty();
		else if(attribute==CITY)
			return getCity() != null && !getCity().isEmpty();
		else if(attribute==POSTAL_CODE)
			return getPostalcode() != null;
		else if(attribute==COUNTRY)
			return getCountry() != null && !getCountry().isEmpty();
		else
			return false;
	}
	
	@Override
	public String toString() {
		return String.format("[Restaurant %s: %s / %s / %s]", getIdentifier(), getName(),
				getCity()
				,getPostalcode()
				);
	}

	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Restaurant){
			return this.getIdentifier().equals(((Restaurant) obj).getIdentifier());
		}else
			return false;
	}


}
