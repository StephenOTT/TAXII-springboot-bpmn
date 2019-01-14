package io.digitalstate.taxii.mongo.model.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.TaxiiParsers;
import io.digitalstate.taxii.model.status.TaxiiStatusFailureResource;
import org.bson.Document;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.io.IOException;

/**
 * This is a fake Document that only holds Writer Converters.
 * a Status Failure is a Object which is injected into a existing Status Document, and thus we do not
 * need any of the extra development structures such as Immutables or _id, etc.
 *
 * This interface remains in the document package as its part of the converters that are used.
 */
public interface StatusFailureDocument  {

    @WritingConverter
    public class MongoWriterConverter implements Converter<TaxiiStatusFailureResource, Document> {
        public Document convert(final TaxiiStatusFailureResource object) {
            try {
                return Document.parse(TaxiiParsers.getMongoMapper().writeValueAsString(object));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @ReadingConverter
    public class MongoReaderConverter implements Converter<Document, TaxiiStatusFailureResource> {
        public TaxiiStatusFailureResource convert(final Document object) {
            try {
                return TaxiiParsers.getMongoMapper().readValue(object.toJson(), TaxiiStatusFailureResource.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
