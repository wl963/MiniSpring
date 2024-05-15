package com.mini.beans.factory.xml;

import com.mini.beans.factory.config.BeanDefinition;
import com.mini.beans.factory.support.AutowireCapableBeanFactory;
import com.mini.core.Resource;
import com.mini.beans.factory.config.ConstructorArgumentValue;
import com.mini.beans.factory.config.ConstructorArgumentValues;
import com.mini.entity.PropertyValue;
import com.mini.entity.PropertyValues;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    AutowireCapableBeanFactory simpleBeanFactory;
    public XmlBeanDefinitionReader(AutowireCapableBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }
    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

            //处理属性
            List<Element> propertyElements=element.elements("property");
            PropertyValues pvs=new PropertyValues();
            List<String> refs=new ArrayList<>();
            for (Element element1:propertyElements)
            {
                String type = element1.attributeValue("type");
                String name = element1.attributeValue("name");
                String value = element1.attributeValue("value");
                String ref=element1.attributeValue("ref");
                boolean isRef=false;
                String pValue="";
                if(value!=null&&!value.equals(""))
                {
                    isRef=false;
                    pValue=value;
                } else if (ref!=null&&!ref.equals("")) {
                    isRef=true;
                    pValue=ref;
                    refs.add(ref);
                }
                pvs.addPropertyValue(new PropertyValue(type,name,pValue,isRef));
            }
            beanDefinition.setPropertyValues(pvs);
            String[] refArray=refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            //处理构造器参数
            List<Element> constructorElements=element.elements("constructor-arg");
            ConstructorArgumentValues avs=new ConstructorArgumentValues();
            for (Element e: constructorElements)
            {
                String type = e.attributeValue("type");
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");
                avs.addArgumentValue(new ConstructorArgumentValue(type,name,value));
            }
            beanDefinition.setConstructorArgumentValues(avs);
            this.simpleBeanFactory.registerBeanDefinition(beanID,beanDefinition);
        }
    }
}
