package com.mini.beans.factory;

import com.mini.beans.factory.support.AutowireCapableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory,ConfigurableBeanFactory, AutowireCapableBeanFactory {
}
