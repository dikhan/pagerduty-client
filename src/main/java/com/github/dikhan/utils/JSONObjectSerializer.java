package com.github.dikhan.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.json.JSONObject;

import java.io.IOException;

public class JSONObjectSerializer extends StdSerializer<JSONObject>{
    public JSONObjectSerializer() {
        this(null);
    }

    public JSONObjectSerializer(Class<JSONObject> jsonObject) {
        super(jsonObject);
    }

    @Override
    public void serialize(JSONObject jsonObject, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException{
        jgen.writeRawValue(jsonObject.toString());
    }
}
