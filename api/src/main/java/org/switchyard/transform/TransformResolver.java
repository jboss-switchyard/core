package org.switchyard.transform;

import javax.xml.namespace.QName;

/**
 * Transform resolver operations for the Transformer Registry.
 */
public interface TransformResolver {
    
    /**
     * Get a transformer.
     * @param from from
     * @param to to
     * @return transformer
     */
    Transformer<?, ?> getTransformer(QName from, QName to);
    
    /**
     * Get a transform sequence wiring transformers.
     * @param from from
     * @param to to
     * @return transformer
     */
    TransformSequence getTransformSequence(QName from, QName to);
}
