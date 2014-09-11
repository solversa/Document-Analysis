package KMeans;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.math.BigDecimal;

public class Document implements Comparable<Document>
{
	int k;
	String docName;
	Double distance;
	Map<String, Double> tfIdfMap;
	
	// CONSTRUCTOR
	public Document(String docName, Map<String, Double> tfIdfMap)
	{
		this.docName  = docName;
		this.tfIdfMap = tfIdfMap;
	}
	
	// SET
	public void setClusterID(int k)
	{
		this.k = k;
	}
	public void setDistance(Double distance)
	{
		this.distance = distance;
	}
	

	//GET
	public int getClusterID()
	{
		return this.k;
	}
	public Double getDistance()
	{
		return this.distance;
	}
	public Map<String, Double> getTfIdfMap()
	{
		return this.tfIdfMap;
	}
	
	public static Map<String, Double> getTFMap (Map<String, Integer> wordFreqMap)
	{
		Map<String, Double> tfMap = new HashMap<String, Double>();
		int noOfTerms = wordFreqMap.size();

		for (Entry<String, Integer> word : wordFreqMap.entrySet() ) 
		{
			Double tf =  ((double) word.getValue()) / noOfTerms ;
			tfMap.put(word.getKey(), tf );
		}
		return tfMap;
	}
	
	public static Map<String, Double> calcTFIDFMap (Map<String, Map>  bigFileDictionary, Map<String, Double> tfMap)
	{
		Map<String, Double> tfIdfMap = new HashMap<String, Double>();
		int noOfDocs = bigFileDictionary.size();
		
		for ( Entry<String, Double> word : tfMap.entrySet() ) 
		{
			String term  = word.getKey();
			Double tf    = word.getValue();
			Double idf   = Math.log((double) noOfDocs / Files2BigDictionary.allTermsMap.get(term).docFreq);
			Double tfidf = tf * idf;
			
			tfIdfMap.put(term, tfidf);
		}
		return tfIdfMap;	
	}

	@Override
	public int compareTo(Document o) {
		// TODO Auto-generated method stub
		return Double.compare(this.distance, o.distance);
	}

}