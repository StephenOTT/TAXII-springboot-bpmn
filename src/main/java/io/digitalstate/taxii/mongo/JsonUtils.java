package io.digitalstate.taxii.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.TaxiiParsers;
import io.digitalstate.taxii.mongo.model.TaxiiMongoModel;

import java.util.List;

public class JsonUtils {
    public static String ListToJson(List<? extends TaxiiMongoModel> list){
        try {
            return TaxiiParsers.getJsonMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse into json string", e);
        }
    }

}
