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
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.util.List;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVDataSetFormatter;

/**
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class RestaurantCSVFormatter extends CSVDataSetFormatter<Restaurant, Attribute> {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.CSVDataSetFormatter#getHeader(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	public String[] getHeader(List<Attribute> orderedHeader) {
		return new String[] { "id", "name", "cuisine", "facilitiesandservices", "telephone", "website", "openinghours", "country", "city", "postalcode", "street", "region", "province", "latitude", "longitude", "reviewcount", "rating", "award", "reviews", "currency", "pricerange", "minprice", "maxprice" };
	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.CSVDataSetFormatter#format(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	public String[] format(Restaurant record, DataSet<Restaurant, Attribute> dataset, List<Attribute> orderedHeader) {
		return new String[] {
				record.getIdentifier(),
				record.getName(),
				record.getCuisine(),
				record.getFacilitiesandservices(),
				record.getTelephone(),
				record.getWebsite(),
				record.getOpeninghours(),
				record.getCountry(),
				record.getCity(),
				record.getPostalcode(),
				record.getStreet(),
				record.getRegion(),
				record.getProvince(),
				Double.toString(record.getLatitude()),
				Double.toString(record.getLongitude()),
				Integer.toString(record.getReviewcount()),
				Integer.toString(record.getRating()),
				record.getAward(),
				record.getReviews(),
				record.getCurrency(),
				record.getPricerange(),
				Integer.toString(record.getMinprice()),
				Integer.toString(record.getMaxprice())
		};
	}

}
