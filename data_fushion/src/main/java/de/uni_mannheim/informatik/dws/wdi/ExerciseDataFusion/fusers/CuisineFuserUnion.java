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
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers;

import java.util.Arrays;
import java.util.List;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Actor;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * {@link AttributeValueFuser} for the actors of {@link Movie}s. 
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class CuisineFuserUnion extends AttributeValueFuser<List<String>, Restaurant, Attribute> {
	
	public CuisineFuserUnion() {
		super(new Union<String, Restaurant, Attribute>());
	}
	
	@Override
	public boolean hasValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Restaurant.CUISINE);
	}
	
	@Override
	public List<String> getValue(Restaurant record, Correspondence<Attribute, Matchable> correspondence) {
		return Arrays.asList(record.getCuisine());
	}

	@Override
	public void fuse(RecordGroup<Restaurant, Attribute> group, Restaurant fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<String>, Restaurant, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setCuisine(fused.getValue().toArray(new String[0]));
		fusedRecord.setAttributeProvenance(Restaurant.CUISINE, fused.getOriginalIds());
	}

}
