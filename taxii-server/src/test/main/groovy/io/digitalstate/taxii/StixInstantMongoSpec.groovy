package io.digitalstate.taxii

import io.digitalstate.stix.bundle.BundleableObject
import io.digitalstate.stix.common.StixInstant
import io.digitalstate.stix.sdo.objects.AttackPattern
import io.digitalstate.stix.sdo.objects.AttackPatternSdo
import io.digitalstate.taxii.common.TaxiiParsers
import io.digitalstate.taxii.mongo.model.TaxiiMongoModel
import io.digitalstate.taxii.mongo.model.document.CollectionObjectDocument
import io.digitalstate.taxii.mongo.model.document.ImmutableCollectionObjectDocument
import org.bson.Document
import spock.lang.Specification

import java.time.Instant

class StixInstantMongoSpec extends Specification{


    def "Sub Second Precision test for Attack Pattern"(){

        when: " Generating a AttackPattern that is put inside of a CollectionObjectDocument"

        AttackPatternSdo attackPattern = AttackPattern.builder()
                .created(new StixInstant(Instant.now(),9))
                .name("Some Attack Pattern with 9 Digit Created Date Sub Second Precision")
                .build()

        CollectionObjectDocument collectionObjectDocument = ImmutableCollectionObjectDocument.builder()
                .tenantId("t1")
                .collectionId("c1")
                .object(attackPattern)
                .build()


        then: "Print the CollectionObjectDocument as a json string"

        String json = TaxiiParsers.getMongoMapper().writeValueAsString(collectionObjectDocument)
//        println "TAXII Mongo Json: " + json

        Document doc = Document.parse(json)
//        println "Mongo Document JSON: " + doc.toJson()

        then: "Convert Json Back into object"

        TaxiiMongoModel model = TaxiiParsers.getMongoMapper().readValue(doc.toJson(), TaxiiMongoModel.class)
//        println "Parsed Object back to Json: " + model.toJson()

    }
}