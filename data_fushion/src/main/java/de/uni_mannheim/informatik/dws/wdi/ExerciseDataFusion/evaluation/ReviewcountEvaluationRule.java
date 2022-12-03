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
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;

import java.util.Arrays;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

/**
 * {@link EvaluationRule} for the directors of {@link Movie}s. The rule simply
 * compares the director of two {@link Movie}s and returns true, in case they
 * are identical.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class ReviewcountEvaluationRule extends EvaluationRule<Restaurant, Attribute> {

	@Override
	public boolean isEqual(Restaurant record1, Restaurant record2, Attribute schemaElement) {
		if(record1.getReviewcount()== null && record2.getReviewcount()==null)
			return true;
		else if(record1.getReviewcount()== null ^ record2.getReviewcount()==null)
			return false;
		else 
			return Arrays.asList(record1.getReviewcount()).containsAll(Arrays.asList(record2.getReviewcount())) || 
					Arrays.asList(record2.getReviewcount()).containsAll(Arrays.asList(record1.getReviewcount()));
	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.Object, java.lang.Object, de.uni_mannheim.informatik.wdi.model.Correspondence)
	 */
	@Override
	public boolean isEqual(Restaurant record1, Restaurant record2,
			Correspondence<Attribute, Matchable> schemaCorrespondence) {
		return isEqual(record1, record2, (Attribute)null);
	}
	
}
