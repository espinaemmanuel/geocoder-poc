package org.apache.solr.search;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.CommonParams;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeocoderParserTest extends SolrTestCaseJ4 {
	
	static String[][] data = {{"1", "HEARST", "AVE", "2301", "2399", "2300", "2398", "94709", "94709", "BERKELEY", "CA", "37.874525,-122.26468,", "37.874825,-122.26278"},
			{"2", "HEARST", "AVE", "2401", "2499", "2400", "2498", "94709", "94709", "BERKELEY", "CA", "37.874825,-122.26278", "37.875025,-122.26078"},
			{"3", "HEARST", "AVE", "2501", "2599", "2500", "2598", "94709", "94709", "BERKELEY", "CA", "37.875025,-122.26078", "37.875325,-122.25888,"},
			{"4", "STOCKTON", "ST", "1", "99", "2", "98", "94108", "94108", "SAN FRANCISCO", "CA", "37.785744,-122.405831", "37.786615,-122.406399"},
			{"5", "STOCKTON", "ST", "735", "799", "744", "798", "94108", "94108", "SAN FRANCISCO", "CA", "37.792777,-122.407645", "37.793178,-122.407727"},
			{"6", "STOCKTON", "ST", "421", "499", "438", "498", "94108", "94108", "SAN FRANCISCO", "CA", "37.789901,-122.407053", "37.790366,-122.407149"}};
	
	
	
	  @BeforeClass
	  public static void beforeClass() throws Exception {
	    initCore("solrconfig-geocoder.xml","schema-geocoder.xml");
	    
	    lrf = h.getRequestFactory
	      ("geocoder", 0, 20,
	       CommonParams.VERSION,"2.2", "indent", "on"
	       );
	    
	    for(String[] segment: data){
		    assertNull(h.validateUpdate(adoc("id", segment[0],
	                 "street", segment[1],
	                 "type", segment[2],
	                 "from_left_number", segment[3],
	                 "to_left_number", segment[4],
	                 "from_right_number", segment[5],
	                 "to_right_number", segment[6],
	                 "zipl", segment[7],
	                 "zipr", segment[8],
	                 "city", segment[9],
	                 "state", segment[10],
	                 "from", segment[11],
	                 "to", segment[12])));	    	
	    }

	    assertNull(h.validateUpdate(commit()));
	  }
	  
	  @Test
	  public void testSomeStuff() throws Exception {
		    assertQ("basic match",
		    	    req("1 Stockton Street San Francisco, CA 94108")
		            ,"//*[@numFound='1']"
		            );
	  }

}
