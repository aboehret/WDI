package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import java.io.File;

import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.CityEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.NameEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.CityFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.CityFuserVoting;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.NameFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Restaurant;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.RestaurantXMLFormatter;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.RestaurantXMLReader;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class Restaurant_DataFusion 
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
		// Load the Data into FusibleDataSet
		logger.info("*\tLoading datasets\t*");
		FusibleDataSet<Restaurant, Attribute> ds1 = new FusibleHashedDataSet<>();
		new RestaurantXMLReader().loadFromXML(new File("data_fushion/data/input/michelin1_small.xml"), "/restaurants/restaurant", ds1); // path of the data set
		ds1.printDataSetDensityReport(); // this gives the data set density report

		FusibleDataSet<Restaurant, Attribute> ds2 = new FusibleHashedDataSet<>();
		new RestaurantXMLReader().loadFromXML(new File("data_fushion/data/input/michelin2_small.xml"), "/restaurants/restaurant", ds2);
		ds2.printDataSetDensityReport();

		FusibleDataSet<Restaurant, Attribute> ds3 = new FusibleHashedDataSet<>();
		new RestaurantXMLReader().loadFromXML(new File("data_fushion/data/input/michelin3_small.xml"), "/restaurants/restaurant", ds3);
		ds3.printDataSetDensityReport();
		
//		System.out.println(ds1.size());
//		System.out.println(ds2.size());
//		System.out.println(ds3.size());
		
		

		// Maintain Provenance
		// Scores (e.g. from rating)
		ds1.setScore(1.0);
		ds2.setScore(3.0);
		ds3.setScore(2.0);


		// load correspondences
		logger.info("*\tLoading correspondences\t*");
		CorrespondenceSet<Restaurant, Attribute> correspondences = new CorrespondenceSet<>();
		correspondences.loadCorrespondences(new File("data_fushion/data/correspondences/michelin_tripadvisor_correspondences_test.csv"),ds1, ds2);
		correspondences.loadCorrespondences(new File("data_fushion/data/correspondences/bookuber_tripadvisor_correspondences_test.csv"),ds2, ds3);

		// write group size distribution
		correspondences.printGroupSizeDistribution(); // aggregated for group size distribution

		// load the gold standard
		logger.info("*\tEvaluating results\t*");
		DataSet<Restaurant, Attribute> gs = new FusibleHashedDataSet<>();
		new RestaurantXMLReader().loadFromXML(new File("data_fushion/data/goldstandard/test.xml"), "/restaurants/restaurant", gs);

		for(Restaurant m : gs.get()) {
			logger.info(String.format("gs: %s", m.getIdentifier()));
		}

		// define the fusion strategy
		DataFusionStrategy<Restaurant, Attribute> strategy = new DataFusionStrategy<>(new RestaurantXMLReader());
		// write debug results to file
		strategy.activateDebugReport("data_fushion/data/output/debugResultsDatafusion.csv", -1, gs);
		
		// add attribute fusers // these are the fusers we want
		strategy.addAttributeFuser(Restaurant.NAME, new NameFuserLongestString(),new NameEvaluationRule());
		strategy.addAttributeFuser(Restaurant.CITY,new CityFuserVoting(), new CityEvaluationRule()); // Voting because
//		strategy.addAttributeFuser(Movie.DATE, new DateFuserFavourSource(),new DateEvaluationRule());
//		strategy.addAttributeFuser(Movie.ACTORS,new ActorsFuserUnion(),new ActorsEvaluationRule());
		
		// create the fusion engine
		DataFusionEngine<Restaurant, Attribute> engine = new DataFusionEngine<>(strategy);

		// print consistency report
		engine.printClusterConsistencyReport(correspondences, null);
		
		// print record groups sorted by consistency
		// run this file and see your group consistencies
		engine.writeRecordGroupsByConsistency(new File("data_fushion/data/output/recordGroupConsistencies.csv"), correspondences, null);

		// run the fusion
		logger.info("*\tRunning data fusion\t*");
		FusibleDataSet<Restaurant, Attribute> fusedDataSet = engine.run(correspondences, null);

		// write the result
		new RestaurantXMLFormatter().writeXML(new File("data_fushion/data/output/fused.xml"), fusedDataSet);

		// evaluate
		DataFusionEvaluator<Restaurant, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Restaurant, Attribute>());
		
		double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

		logger.info(String.format("*\tAccuracy: %.2f", accuracy));
		
		
    }
}

