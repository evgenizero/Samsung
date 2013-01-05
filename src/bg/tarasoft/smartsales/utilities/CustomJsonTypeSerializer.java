package bg.tarasoft.smartsales.utilities;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import bg.tarasoft.smartsales.bean.LoggedActivity;

public class CustomJsonTypeSerializer extends JsonSerializer<Integer> {
	@Override
	public void serialize(Integer type, JsonGenerator aJsonGenerator,
			SerializerProvider aSerializerProvider) throws IOException,
			JsonProcessingException {

		String typeString = "";

		switch (type) {
		case LoggedActivity.CATEGORY:
			typeString = "c";
			break;
		case LoggedActivity.PRODUCT:
			typeString = "p";
			break;
		case LoggedActivity.SERIE:
			typeString = "s";
			break;

		}
		aJsonGenerator.writeString(typeString);
	}
}