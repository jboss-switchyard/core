/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.switchyard.validate.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.logging.Logger;
import org.switchyard.common.cdi.CDIUtil;
import org.switchyard.common.type.Classes;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.validate.ValidateModel;
import org.switchyard.config.model.validate.ValidatesModel;
import org.switchyard.validate.Validator;
import org.switchyard.validate.ValidatorRegistry;
import org.switchyard.validate.config.model.JavaValidateModel;

/**
 * {@link ValidatorRegistry} loader class.
 *
 * @author <a href="mailto:tm.igarashi@gmail.com">Tomohisa Igarashi</a>
 */
public class ValidatorRegistryLoader {

    /**
     * Logger.
     */
    private static Logger _log = Logger.getLogger(ValidatorRegistryLoader.class);
    /**
     * Classpath location for out-of-the-box validation configurations.
     */
    public static final String VALIDATES_XML = "META-INF/switchyard/validates.xml";

    /**
     * Validators
     */
    private List<Validator> _validators = new LinkedList<Validator>();
    /**
     * The registry instance into which the validates were loaded.
     */
    private ValidatorRegistry _validatorRegistry;

    private List<CreationalContext<?>> _cdiCreationalContexts = new LinkedList<CreationalContext<?>>();

    /**
     * Public constructor.
     * @param validatorRegistry The registry instance.
     */
    public ValidatorRegistryLoader(ValidatorRegistry validatorRegistry) {
        if (validatorRegistry == null) {
            throw ValidateMessages.MESSAGES.nullValidatorRegistryArgument();
        }
        this._validatorRegistry = validatorRegistry;
    }

    /**
     * Register a set of validators in the validate registry associated with this deployment.
     * @param validates The validates model.
     * @throws DuplicateValidatorException an existing validator has already been registered for the from and to types
     */
    public void registerValidators(ValidatesModel validates) throws DuplicateValidatorException {
        if (validates == null) {
            return;
        }

        try {
            for (ValidateModel validateModel : validates.getValidates()) {
                Collection<Validator<?>> validators = newValidators(validateModel);

                for (Validator<?> validator : validators) {
                    if (_validatorRegistry.hasValidator(validator.getName())) {
                        Validator<?> registeredValidator = _validatorRegistry.getValidator(validator.getName());
                        throw ValidateMessages.MESSAGES.failedToRegisterValidator(toDescription(validator), toDescription(registeredValidator));
                    }

                    _log.debug("Adding validator =>"
                            + ", Name:" + validator.getName());
                    _validatorRegistry.addValidator(validator);
                    _validators.add(validator);
                }
            }
        } catch (DuplicateValidatorException e) {
            throw e;
        } catch (RuntimeException e) {
            // If there was an exception for any reason... remove all Validator instance that have
            // already been registered with the domain...
            unregisterValidators();
            throw e;
        }
    }

    /**
     * Create a new {@link org.switchyard.validate.Validator} instance from the supplied {@link ValidateModel} instance.
     * @param validateModel The ValidateModel instance.
     * @return The Validator instance.
     */
    public Validator<?> newValidator(ValidateModel validateModel) {
        return newValidators(validateModel).iterator().next();
    }

    /**
     * Create a Collection of {@link Validator} instances from the supplied {@link ValidateModel} instance.
     * @param validateModel The ValidateModel instance.
     * @return The Validator instance.
     */
    public Collection<Validator<?>> newValidators(ValidateModel validateModel) {

        Collection<Validator<?>> validators = null;

        if (validateModel instanceof JavaValidateModel) {
            JavaValidateModel javaValidateModel = JavaValidateModel.class.cast(validateModel);
            String bean = javaValidateModel.getBean();
            if (bean != null) {
                BeanManager beanManager = CDIUtil.lookupBeanManager();
                if (beanManager == null) {
                    throw ValidateMessages.MESSAGES.cdiBeanManagerNotFound();
                }
                Object validator = null;
                Set<Bean<?>> beans = beanManager.getBeans(bean);
                if (beans != null && !beans.isEmpty()) {
                    Bean<?> target = beans.iterator().next();
                    CreationalContext<?> context = beanManager.createCreationalContext(target);
                    validator = beanManager.getReference(target, Object.class, context);
                    _cdiCreationalContexts.add(context);
                }
                if (validator == null) {
                    throw ValidateMessages.MESSAGES.validatorBeanNotFound(bean);
                }
                validators = ValidatorUtil.newValidators(validator, validateModel.getName());

            } else {
                String className = ((JavaValidateModel) validateModel).getClazz();
                if (className == null) {
                    throw ValidateMessages.MESSAGES.beanOrClassRequired();
                }
                try {
                    Class<?> validateClass = Classes.forName(className, ValidatorUtil.class);
                    validators = ValidatorUtil.newValidators(validateClass, validateModel.getName());
                } catch (Exception e) {
                    throw ValidateMessages.MESSAGES.errorConstructingValidator(className, e);
                }
            }
        } else {
            ValidatorFactory factory = newValidatorFactory(validateModel);

            validators = new ArrayList<Validator<?>>();
            validators.add(factory.newValidator(validateModel));
        }

        if (validators == null || validators.isEmpty()) {
            throw ValidateMessages.MESSAGES.unknownValidateModel(validateModel.getClass().getName());
        }

        return validators;
    }

    private static ValidatorFactory newValidatorFactory(ValidateModel validateModel) {
        ValidatorFactoryClass validatorFactoryClass = validateModel.getClass().getAnnotation(ValidatorFactoryClass.class);

        if (validatorFactoryClass == null) {
            throw ValidateMessages.MESSAGES.validateModelNotAnnotated(validateModel.getClass().getName());
        }

        Class<?> factoryClass = validatorFactoryClass.value();

        if (!org.switchyard.validate.internal.ValidatorFactory.class.isAssignableFrom(factoryClass)) {
            throw ValidateMessages.MESSAGES.invalidValidatorFactoryImplementation(org.switchyard.validate.internal.ValidatorFactory.class.getName());
        }

        try {
            return (ValidatorFactory) factoryClass.newInstance();
        } catch (Exception e) {
            throw ValidateMessages.MESSAGES.failedToInstantiateValidatorFactory(factoryClass.getName());
        }
    }

    /**
     * Unregister all validators.
     */
    public void unregisterValidators() {
        for (Validator validator : _validators) {
            _validatorRegistry.removeValidator(validator);
        }
        for (CreationalContext<?> context : _cdiCreationalContexts) {
            context.release();
        }
        _cdiCreationalContexts.clear();
    }

    /**
     * Load the out of the box validators.
     * <p/>
     * Scans the classpath for {@link #VALIDATES_XML} runtime configuration resources.
     */
    public void loadOOTBValidates() {
        try {
            List<URL> resources = Classes.getResources(VALIDATES_XML, getClass());

            for (URL resource : resources) {
                InputStream configStream = resource.openStream();

                try {
                    ValidatesModel validatesModel = new ModelPuller<ValidatesModel>().pull(configStream);
                    registerValidators(validatesModel);
                } catch (final DuplicateValidatorException e) {
                    _log.debug(e.getMessage(), e);
                } finally {
                    configStream.close();
                }
            }
        } catch (IOException e) {
            throw ValidateMessages.MESSAGES.errorReadingValidator(VALIDATES_XML, e);
        }
    }

    private String toDescription(Validator<?> validator) {
        return validator.getClass().getName() + "(" + validator.getName() + ")";
    }
}
