package org.apache.solr.search.GeocoderParser.JGeocoderParser;

import java.util.Map;

import net.sourceforge.jgeocoder.AddressComponent;
import net.sourceforge.jgeocoder.us.AddressParser;
import net.sourceforge.jgeocoder.us.AddressStandardizer;

import org.apache.solr.search.GeocoderParser.Address;
import org.apache.solr.search.GeocoderParser.GeocoderParser;

public class JGeocoderParser implements GeocoderParser{

	@Override
	public Address parse(String query) {
		Address parsed = new Address();

		Map<AddressComponent, String> preParsed = AddressParser.parseAddress(query);
		if(preParsed == null){
			return parsed;
		}
		
		Map<AddressComponent, String> fieldsMap = AddressStandardizer.normalizeParsedAddress(preParsed);
		if(fieldsMap == null){
			return parsed;
		}
		
		parsed.setCity(fieldsMap.get(AddressComponent.CITY));
		parsed.setNumber(fieldsMap.get(AddressComponent.NUMBER));
		parsed.setPrefix(fieldsMap.get(AddressComponent.PREDIR));
		parsed.setState(fieldsMap.get(AddressComponent.STATE));
		parsed.setStreet(fieldsMap.get(AddressComponent.STREET));
		parsed.setSuffix(fieldsMap.get(AddressComponent.POSTDIR));
		parsed.setType(fieldsMap.get(AddressComponent.TYPE));
		parsed.setZip(fieldsMap.get(AddressComponent.ZIP));
		
		return parsed;
	}

}
