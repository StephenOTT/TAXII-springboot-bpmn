package io.digitalstate.taxii.mongo.annotation;

import org.immutables.annotate.InjectAnnotation;
import org.springframework.data.mongodb.core.index.IndexDirection;

import java.lang.annotation.*;

/**
 * Annotation to apply Indexing annotation for Spring Data MongoDB.
 * This annotation is used to inject the Indexed annotation into the Fields.
 * When building Immutables, typically you are using Methods during the Immutables core code, and the Indexed annotation for MongoDB spring data only works on Fields
 * The content of this annotation is copied from org.springframework.data.mongodb.core.index.Indexed.class
 */
@Documented
@Target( { ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@InjectAnnotation(target = {InjectAnnotation.Where.FIELD}, code = "([[*]])", type = org.springframework.data.mongodb.core.index.Indexed.class)
public @interface Indexed {

    /**
     * If set to true reject all documents that contain a duplicate value for the indexed field.
     *
     * @return
     * @see <a href="https://docs.mongodb.org/manual/core/index-unique/">https://docs.mongodb.org/manual/core/index-unique/</a>
     */
    boolean unique() default false;

    IndexDirection direction() default IndexDirection.ASCENDING;

    /**
     * If set to true index will skip over any document that is missing the indexed field.
     *
     * @return
     * @see <a href="https://docs.mongodb.org/manual/core/index-sparse/">https://docs.mongodb.org/manual/core/index-sparse/</a>
     */
    boolean sparse() default false;

    /**
     * @return
     * @see <a href="https://docs.mongodb.org/manual/core/index-creation/#index-creation-duplicate-dropping">https://docs.mongodb.org/manual/core/index-creation/#index-creation-duplicate-dropping</a>
     * @deprecated since 2.1. No longer supported by MongoDB as of server version 3.0.
     */
    @Deprecated
    boolean dropDups() default false;

    /**
     * Index name. <br />
     * <br />
     * The name will only be applied as is when defined on root level. For usage on nested or embedded structures the
     * provided name will be prefixed with the path leading to the entity. <br />
     * <br />
     * The structure below
     *
     * <pre>
     * <code>
     * &#64;Document
     * class Root {
     *   Hybrid hybrid;
     *   Nested nested;
     * }
     *
     * &#64;Document
     * class Hybrid {
     *   &#64;Indexed(name="index") String h1;
     * }
     *
     * class Nested {
     *   &#64;Indexed(name="index") String n1;
     * }
     * </code>
     * </pre>
     *
     * resolves in the following index structures
     *
     * <pre>
     * <code>
     * db.root.createIndex( { hybrid.h1: 1 } , { name: "hybrid.index" } )
     * db.root.createIndex( { nested.n1: 1 } , { name: "nested.index" } )
     * db.hybrid.createIndex( { h1: 1} , { name: "index" } )
     * </code>
     * </pre>
     *
     * @return
     */
    String name() default "";

    /**
     * If set to {@literal true} then MongoDB will ignore the given index name and instead generate a new name. Defaults
     * to {@literal false}.
     *
     * @return
     * @since 1.5
     */
    boolean useGeneratedName() default false;

    /**
     * If {@literal true} the index will be created in the background.
     *
     * @return
     * @see <a href="https://docs.mongodb.org/manual/core/indexes/#background-construction">https://docs.mongodb.org/manual/core/indexes/#background-construction</a>
     */
    boolean background() default false;

    /**
     * Configures the number of seconds after which the collection should expire. Defaults to -1 for no expiry.
     *
     * @return
     * @see <a href="https://docs.mongodb.org/manual/tutorial/expire-data/">https://docs.mongodb.org/manual/tutorial/expire-data/</a>
     */
    int expireAfterSeconds() default -1;
}
