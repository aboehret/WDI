package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.RestaurantBlockingKeyByCity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantCityComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.RestaurantNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Restaurant;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.RestaurantXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_using_linear_combination2 
{
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("default");
	
    public static void main( String[] args ) throws Exception
    {
		// loading data
		logger.info("*\tLoading datasets\t*");
//		HashedDataSet<Restaurant, Attribute> dataMichelin = new HashedDataSet<>();
//		new RestaurantXMLReader().loadFromXML(new File("data/input/Michelin.xml"), "/restaurants/restaurant", dataMichelin);
//		HashedDataSet<Restaurant, Attribute> dataTripAdvisor= new HashedDataSet<>();
//		new RestaurantXMLReader().loadFromXML(new File("data/input/Trip_advisor.xml"), "/restaurants/restaurant", dataTripAdvisor);
		
		HashedDataSet<Restaurant, Attribute> dataMichelin = new HashedDataSet<>();
		new RestaurantXMLReader().loadFromXML(new File("data/input/Michelin.xml"), "/restaurants/restaurant", dataMichelin);
		HashedDataSet<Restaurant, Attribute> dataTripAdvisor= new HashedDataSet<>();
		new RestaurantXMLReader().loadFromXML(new File("data/input/BookaTable_Ubereats.xml"), "/restaurants/restaurant", dataTripAdvisor);
		
		System.out.println("Size of dataset -> dataMichelin: "+dataMichelin.size());
		System.out.println("Size of dataset -> dataTripAdvisor: "+dataTripAdvisor.size());
		

		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_academy_awards_2_actors_test.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<Restaurant, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
				0.7);
		// The gsTest is the test gold standard. It will also be added in the Debug report so that we get and idea if the record was mathing or non-matching as per the gold standard
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRuleRestaurant.csv", 1000, gsTest); 
		
		// add comparators
		matchingRule.addComparator(new RestaurantNameComparatorLevenshtein(), 0.7);
		matchingRule.addComparator(new RestaurantCityComparatorLevenshtein(), 0.3);
		
		// create a blocker (blocking strategy)
		// StandardRecordBlocker uses the blocking key as Title
		StandardRecordBlocker<Restaurant, Attribute> blocker = new StandardRecordBlocker<Restaurant, Attribute>(new RestaurantBlockingKeyByCity());
//		NoBlocker<Restaurant, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);
		blocker.setMeasureBlockSizes(true);
		//Write debug results to file:
		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<Restaurant, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Restaurant, Attribute>> correspondences = engine.runIdentityResolution(
				dataMichelin, dataTripAdvisor, null, matchingRule, // Here we pass the 2 data records and the matching rule we have created
				blocker);

		// Create a top-1 global matching
		//correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

//		 Alternative: Create a maximum-weight, bipartite matching
		//MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
		//maxWeight.run();
		//correspondences = maxWeight.getResult();

		// write the correspondences to the output file
		// Correspondences means the pairs that is compared and is matched
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/Restaurant_correspondences.csv"), correspondences);

		
		
		logger.info("*\tEvaluating result\t*");
		// evaluate your result
		MatchingEvaluator<Restaurant, Attribute> evaluator = new MatchingEvaluator<Restaurant, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);

		// print the evaluation result
		logger.info("Academy Awards <-> Actors");
		logger.info(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		logger.info(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		logger.info(String.format(
				"F1: %.4f",perfTest.getF1()));
    }
}
