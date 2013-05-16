package org.switchyard.config.model.extensions;

import org.switchyard.config.model.TypedModel;
import org.switchyard.config.model.extensions.adapter.AdapterModel;

public interface ExtensionsModel extends TypedModel {

	/** The "extensions" name. */
    public static final String EXTENSIONS = "extensions";
    
    public AdapterModel getAdapterModel();
    
    public ExtensionsModel setAdapterModel(AdapterModel adapterModel);

	public boolean hasAdapterModel();
}
