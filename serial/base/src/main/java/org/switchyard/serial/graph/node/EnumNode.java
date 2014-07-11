package org.switchyard.serial.graph.node;

import org.switchyard.common.type.Classes;
import org.switchyard.serial.graph.Graph;

/**
 * A node representing a Enum value.
 */
@SuppressWarnings("serial")
public class EnumNode implements Node {

    private String _enumType;
    private String _valueName;

    /**
     * Default constructor.
     */
    public EnumNode() {
    }

    /**
     * Gets the enum type.
     * 
     * @return the enum type
     */
    public String getEnumType() {
        return _enumType;
    }

    /**
     * Sets the enum type.
     * 
     * @param enumType
     *            the enum type
     */
    public void setEnumType(String enumType) {
        _enumType = enumType;
    }

    /**
     * Gets the enum value name.
     * 
     * @return the enum value name
     */
    public String getValueName() {
        return _valueName;
    }

    /**
     * Sets the enum value name.
     * 
     * @param valueName
     *            the enum value name
     */
    public void setValueName(String valueName) {
        _valueName = valueName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compose(Object obj, Graph graph) {
        Enum<?> enumValue = (Enum<?>) obj;
        setEnumType(enumValue.getClass().getName());
        setValueName(enumValue.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object decompose(Graph graph) {
        Class enumClass = Classes.forName(getEnumType(), getClass().getClassLoader(), Thread.currentThread().getContextClassLoader());
        return Enum.valueOf(enumClass, getValueName());
    }
}
