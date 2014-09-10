package KMeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

public class Kmeans 
{
	
	public static List<Document> docsToVectors( Map<String, Map>  bigFileDictionary )
	{
		List<Document> documentVectorList = new ArrayList<Document>();
		
		for ( Entry<String, Map> fileMap : bigFileDictionary.entrySet() ) 
		{
			String               docName     = fileMap.getKey()  ;
			Map<String, Integer> wordFreqMap = fileMap.getValue();
			Map<String, Double>  tfMap       = Document.getTFMap    (wordFreqMap);
			Map<String, Double>  tfIdfMap    = Document.calcTFIDFMap (bigFileDictionary, tfMap) ;
		
			documentVectorList.add( new Document(docName, tfIdfMap) );
		}
		return documentVectorList;
	}
	
	public static Map<Integer, Map> intialiseCentroids ( int K )
	{
		Map<Integer, Map>  centroids    =   new HashMap<Integer, Map>();
		Set<String>        allWords     =   Files2BigDictionary.allTermsMap.keySet() ;
		Random r = new Random();
		
		for (int i = 0; i < K; i++) 
		{
			Map<String, Double> randomTFIDFMap =   new HashMap<String, Double>();
			for (String word : allWords) 
			{
				double randomTFIDFScore = (-1) + (1 - (-1)) * r.nextDouble(); //randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
				randomTFIDFMap.put(word, randomTFIDFScore);
			}
			centroids.put(i, randomTFIDFMap);
		}
		return centroids;
	}
	
	public static Double getSimilarityScore(Map<String, Double> A, Map<String, Double> B)
	{
		double score;
		
		double dotProduct = 0.0;
		double normA      = 0.0;
		double normB      = 0.0;
		
		if ( A.size() <= B.size() ) // Looping over smaller hashmap
		{
			for (Entry<String, Double> term : A.entrySet()) 
			{
				if(B.containsKey(term.getKey()))
				{
					dotProduct += term.getValue() * B.get(term.getKey());
					normA      += Math.pow(term.getValue(),      2);
					normB      += Math.pow(B.get(term.getKey()), 2);
				}
			}
		}
		else
		{
			for (Entry<String, Double> term : B.entrySet()) 
			{
				if ( A.containsKey(term.getKey()) )
				{
					dotProduct += term.getValue() * A.get(term.getKey());
					normB      += Math.pow(term.getValue(),      2);
					normA      += Math.pow(A.get(term.getKey()), 2);
				}
			}
		}
		
		score = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
		return score;
	}

	public static void expectation(Map<Integer, Map> centroids, List<Document> documentVectorList)
	{
		for (Document document : documentVectorList) 
		{
			BiMap<Integer, Double> scoresMap = HashBiMap.create();
			
			// Getting score for a document from each centroid and appending each score to scoresMap
			for ( Entry<Integer, Map> centroid : centroids.entrySet() ) 
			{
				double score = getSimilarityScore(centroid.getValue(), document.getTfIdfMap());
				scoresMap.put(centroid.getKey(), score);
			}
			
			// Getting the cluster with highest score
			List<Double> scores = Lists.newArrayList(scoresMap.values());
			Collections.sort(scores, Collections.reverseOrder());          // Sorting in descending order
			int k = scoresMap.inverse().get(scores.get(0));
			
			// Setting document to a cluster
			document.setClusterID(k);
			
			//System.out.println(k + " " + scoresMap);
		}
	}
	
	public static void main(String[] args) 
	{
		int K = 3;                           // Number of Clusters
		String directory          = args[0]; // Enter Directory name
		boolean ignore_stop_words = true;
		
		Map<String, Map>  bigFileDictionary   = Files2BigDictionary.filesToHashMap(directory, ignore_stop_words);
		List<Document>    documentVectorList  = docsToVectors ( bigFileDictionary );
		
		Map<Integer, Map> centroids         = intialiseCentroids(K);  // Get Initial centroids
		
		expectation(centroids, documentVectorList);
		
		/*
		// For 2 Documents as of now
		Map<String, Double> tfMap_1    = Document.getTFMap    ( bigFileDictionary.get("file_36.txt") );
		Map<String, Double> tfIdfMap_1 = Document.getTFIDFMap ( bigFileDictionary, tfMap_1);
		
		Map<String, Double> tfMap_2    = Document.getTFMap    ( bigFileDictionary.get("file_96.txt") );
		Map<String, Double> tfIdfMap_2 = Document.getTFIDFMap ( bigFileDictionary, tfMap_2);
		
		System.out.print( getSimilarityScore( tfIdfMap_1, tfIdfMap_2)  ); */
		
		
		
		
		
	}

}
