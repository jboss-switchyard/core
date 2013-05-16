package org.switchyard.config.model.extensions.adapter;

import org.switchyard.config.model.TypedModel;
import org.switchyard.config.model.composite.InterfaceModel;

public interface AdapterModel extends TypedModel {
	
	/** The default "adapter" namespace. */
    public static final String DEFAULT_NAMESPACE = "urn:switchyard-config:adapter:1.0";

	/** The "adapter" name. */
    public static final String ADAPTER = "adapter";
    
    public InterfaceModel getInterfaceModel();
    
    public AdapterModel setInterfaceModel(InterfaceModel interfaceModel);
}
