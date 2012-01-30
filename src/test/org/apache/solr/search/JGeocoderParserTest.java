package org.apache.solr.search;

import static org.junit.Assert.*;

import org.apache.solr.search.GeocoderParser.Address;
import org.apache.solr.search.GeocoderParser.JGeocoderParser.JGeocoderParser;
import org.junit.Test;


public class JGeocoderParserTest {

	@Test
	public void test() {
		String addr1 = "123 main street St. louis Missouri";
		JGeocoderParser jcp = new JGeocoderParser();
		
		Address parsed = jcp.parse(addr1);
		
		assertEquals("123", parsed.getNumber());
		assertEquals("MAIN", parsed.getStreet());
		assertEquals("ST", parsed.getType());
		assertEquals("SAINT LOUIS", parsed.getCity());
		assertEquals("MO", parsed.getState());
		
		String addr2 = "136 West 55th Street New York NY 10019";
		Address parsed2 = jcp.parse(addr2);
		assertEquals("NEW YORK", parsed2.getCity());
		assertEquals("136", parsed2.getNumber());
		assertEquals("W", parsed2.getPrefix());
		assertEquals("NY", parsed2.getState());
		assertEquals("55TH", parsed2.getStreet());
		assertEquals("ST", parsed2.getType());
		assertEquals("10019", parsed2.getZip());
		
		String addr3 = "345 Spear Street, Floors 2-4, San Francisco, CA 94105";
		Address parsed3 = jcp.parse(addr3);
		assertEquals("SAN FRANCISCO", parsed3.getCity());
	
		
	}

}
