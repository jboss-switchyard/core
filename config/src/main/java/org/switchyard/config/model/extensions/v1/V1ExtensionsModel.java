package org.switchyard.config.model.extensions.v1;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseNamedModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.extensions.ExtensionsModel;
import org.switchyard.config.model.extensions.adapter.AdapterModel;

public class V1ExtensionsModel extends BaseNamedModel implements ExtensionsModel {

	private AdapterModel _adapterModel;


    public V1ExtensionsModel(Configuration config, Descriptor desc) {
        super(config, desc);

        // only one adapter is supported
        for (Configuration adater_config : config.getChildrenStartsWith(AdapterModel.ADAPTER)) {
        	_adapterModel = (AdapterModel)readModel(adater_config);
            if (_adapterModel != null) {
            	break;
            }
		}
    }

	@Override
	public String getType() {
		return EXTENSIONS;
	}

	@Override
	public AdapterModel getAdapterModel() {
		return _adapterModel;
	}

	@Override
	public ExtensionsModel setAdapterModel(AdapterModel adapterModel) {
		setChildModel(adapterModel);
		_adapterModel = adapterModel;
		return this;
	}
	
	@Override
	public boolean hasAdapterModel() {
		return _adapterModel != null;
	}
}
