package org.switchyard.config.model.extensions.adapter.v1;

import javax.xml.namespace.QName;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseTypedModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.composite.InterfaceModel;
import org.switchyard.config.model.extensions.adapter.AdapterModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

public abstract class V1BaseAdaptModel extends BaseTypedModel implements AdapterModel {

	protected static final String INTERFACE = "interface";
	private InterfaceModel _interfaceModel;

	protected V1BaseAdaptModel(String type) {
        this(new QName(SwitchYardModel.DEFAULT_NAMESPACE, AdapterModel.ADAPTER + '.' + type));
    }

    protected V1BaseAdaptModel(QName qname) {
        super(qname);
    }

    protected V1BaseAdaptModel(Configuration config, Descriptor desc) {
        super(config, desc);
        
        // only one interface is supported
        for (Configuration adater_config : config.getChildrenStartsWith(InterfaceModel.INTERFACE)) {
        	InterfaceModel interfaceModel = (InterfaceModel)readModel(adater_config);
            if (interfaceModel != null) {
            	_interfaceModel = interfaceModel;
            	break;
            }
		}
    }
    
    @Override
    public InterfaceModel getInterfaceModel() {
    	return _interfaceModel;
    }
    
    @Override
    public AdapterModel setInterfaceModel(InterfaceModel interfaceModel) {
    	setChildModel(interfaceModel);
    	_interfaceModel = interfaceModel;
		return this;
	}
}
