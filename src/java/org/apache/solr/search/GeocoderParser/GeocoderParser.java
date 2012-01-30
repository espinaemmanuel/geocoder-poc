package org.apache.solr.search.GeocoderParser;

public interface GeocoderParser {

	public Address parse(String query);
}
