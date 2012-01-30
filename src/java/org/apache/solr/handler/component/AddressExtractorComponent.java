package org.apache.solr.handler.component;

import java.io.IOException;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.GeocoderQParser;
import org.apache.solr.search.GeocoderParser.Address;

public class AddressExtractorComponent extends SearchComponent {

	@Override
	public void prepare(ResponseBuilder rb) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(ResponseBuilder rb) throws IOException {
		GeocoderQParser parser;
		if(rb.getQparser() instanceof GeocoderQParser){
			parser = (GeocoderQParser) rb.getQparser();
		} else {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "the query parser of the request is not a GeocoderQParser");
		}
		
		Address address = parser.getAddress();
		
		NamedList<String> addressComponents = new NamedList<String>();

		addressComponents.add("number", address.getNumber());
		addressComponents.add("street", address.getStreet());
		addressComponents.add("prefix", address.getPrefix());
		addressComponents.add("suffix", address.getSuffix());
		addressComponents.add("type", address.getType());
		addressComponents.add("city", address.getCity());
		addressComponents.add("state", address.getState());
		addressComponents.add("zip", address.getZip());

		rb.rsp.add("address_info", addressComponents);
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

}
