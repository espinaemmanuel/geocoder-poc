package org.apache.solr.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.GeocoderParser.Address;
import org.apache.solr.search.GeocoderParser.JGeocoderParser.JGeocoderParser;

public class GeocoderQParser extends QParser {
	
	public static String GEOCODER_PREFIX = "geocoder";
	public static String FIELD_STREET = GEOCODER_PREFIX + ".street";
	public static String FIELD_TYPE = GEOCODER_PREFIX + ".type";
	public static String FIELD_FN = GEOCODER_PREFIX + ".from_number";
	public static String FIELD_TN = GEOCODER_PREFIX + ".to_number";
	public static String FIELD_ZIP = GEOCODER_PREFIX + ".zip";
	public static String FIELD_CITY = GEOCODER_PREFIX + ".city";
	public static String FIELD_STATE = GEOCODER_PREFIX + ".state";
	
	private static Set<String> fieldsSet = new HashSet<String>() ;
	
	static {
		fieldsSet.add(FIELD_STREET);
		fieldsSet.add(FIELD_TYPE);
		fieldsSet.add(FIELD_FN);
		fieldsSet.add(FIELD_TN);
		fieldsSet.add(FIELD_ZIP);
		fieldsSet.add(FIELD_CITY);
		fieldsSet.add(FIELD_STATE);
	}

	private JGeocoderParser jcp;
	private Address address;
	private IndexSchema schema;

	public GeocoderQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
		
		this.jcp = new JGeocoderParser();
		this.schema = req.getSchema();
	}
	
	public Address getAddress(){
		return address;
	}
	
	private Map<String, String> getFields(SolrParams solrParams){
		Map<String, String> ret = new HashMap<String, String>();
		
		for(String fieldName : fieldsSet){
			String val = solrParams.get(fieldName);
			if(val == null){
				val = fieldName.replaceFirst(GEOCODER_PREFIX + ".", "");
			}
			ret.put(fieldName, val);
		}
		
		return ret;		
	}
	
	@Override
	public Query parse() throws ParseException {
		SolrParams solrParams = SolrParams.wrapDefaults(localParams, params);
		
		address = jcp.parse(solrParams.get("q"));
		Map<String, String> fields = getFields(solrParams);
		
		BooleanQuery q = new BooleanQuery();
				
	    SchemaField sf_fn = schema.getField(fields.get(FIELD_FN));
	    SchemaField sf_tn = schema.getField(fields.get(FIELD_TN));
	    
		q.add(sf_fn.getType().getRangeQuery(this, sf_fn, null, address.getNumber(), true, true), Occur.MUST);
		q.add(sf_tn.getType().getRangeQuery(this, sf_tn, address.getNumber(), null, true, true), Occur.MUST);
		
		if(address.getStreet() != null){
			q.add(new TermQuery(new Term(fields.get(FIELD_STREET), address.getStreet())), Occur.MUST);
		}
		
		if(address.getType() != null){
			q.add(new TermQuery(new Term(fields.get(FIELD_TYPE), address.getType())), Occur.MUST);
		}
		
		if(address.getZip() != null){			
			q.add(new TermQuery(new Term(fields.get(FIELD_ZIP), address.getZip())), Occur.MUST);
		}
		
		if(address.getCity() != null){
			q.add(new TermQuery(new Term(fields.get(FIELD_CITY), address.getCity())), Occur.MUST);
		}
		
		if(address.getState() != null){
			q.add(new TermQuery(new Term(fields.get(FIELD_STATE), address.getState())), Occur.MUST);
		}
		
		return q;
	}
	
}