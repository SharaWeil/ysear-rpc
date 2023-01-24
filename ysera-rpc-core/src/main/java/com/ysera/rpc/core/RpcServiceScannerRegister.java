package com.ysera.rpc.core;

import com.ysera.rpc.core.annotation.EnableYseraRpcClient;
import com.ysera.rpc.core.annotation.RpcClient;
import com.ysera.rpc.core.annotation.RpcService;
import com.ysera.rpc.core.proxy.YseraRpcProxyFactory;
import com.ysera.rpc.core.registry.RegistryClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author admin
 * @ClassName RpcServiceScannerRegister.java
 * @createTime 2023年01月21日 13:55:00
 */
public class RpcServiceScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static final String SPRING_BEAN_BASE_PACKAGE = "com.ysera.rpc";
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    ResourceLoader resourceLoader;
    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, @NonNull BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableYseraRpcClient.class.getName()));
        String[] rpcScanBasePackages = new String[0];
        if (null != annotationAttributes){
            rpcScanBasePackages = annotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[]{((StandardAnnotationMetadata) metadata).getIntrospectedClass().getPackage().getName()};
        }

        registerRpcRemoteServer(registry, rpcScanBasePackages);

        scanRpcService(registry, rpcScanBasePackages);

        registerRpcClient(registry, rpcScanBasePackages);

    }

    private void registerRpcRemoteServer(BeanDefinitionRegistry registry, String[] rpcScanBasePackages) {
        boolean isContains = false;
        for (String basePackage : rpcScanBasePackages) {
            if (StringUtils.equalsIgnoreCase(basePackage,SPRING_BEAN_BASE_PACKAGE)){
                isContains = true;
            }
        }
        List<String> packages = new ArrayList<>();
        if (!isContains){
            packages.addAll(Arrays.asList(rpcScanBasePackages));
            packages.add(SPRING_BEAN_BASE_PACKAGE);
        }
        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(registry);
        classPathBeanDefinitionScanner.scan(packages.toArray(new String[]{}));
    }

    private void scanRpcService(BeanDefinitionRegistry registry, String[] rpcScanBasePackages) {
        // scan RpcService
        YseraRpcScanner yseraRpcServiceScanner = new YseraRpcScanner(registry, true);
        AnnotationTypeFilter serviceAnnotationTypeFilter = new AnnotationTypeFilter(
                RpcService.class);
        yseraRpcServiceScanner.addIncludeFilter(serviceAnnotationTypeFilter);
        Set<BeanDefinitionHolder> beanDefinitionHolderSet = yseraRpcServiceScanner.doScan(rpcScanBasePackages);
        if (!beanDefinitionHolderSet.isEmpty()){
            beanDefinitionHolderSet.forEach(elem->{
                RegistryClient.addService(elem.getBeanDefinition());
            });
        }

    }

    private void registerRpcClient(BeanDefinitionRegistry registry, String[] rpcScanBasePackages) {
        //自定义的包扫描器
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                RpcClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);

        for (String basePackage : rpcScanBasePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    Assert.isTrue(annotationMetadata.isInterface(),
                            "@YseraRpc can only be specified on an interface");

                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(
                                    RpcClient.class.getCanonicalName());
                    assert attributes != null;
                    registerRpcClient(registry,annotationMetadata,attributes);
                }
            }
        }
    }

    private void registerRpcClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {

        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(YseraRpcProxyFactory.class);
        String serviceName = (String) attributes.get("value");
        definition.addPropertyValue("serviceName", serviceName);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        definition.addPropertyValue("type", className);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        // has a default, won't be null
        boolean primary = (Boolean) attributes.get("primary");
        beanDefinition.setPrimary(primary);

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

   class YseraRpcScanner  extends ClassPathBeanDefinitionScanner{

       public YseraRpcScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
           super(registry, useDefaultFilters);
       }

       @Override
       protected boolean isCandidateComponent(
               AnnotatedBeanDefinition beanDefinition) {
           boolean isCandidate = false;
           if (beanDefinition.getMetadata().isIndependent()) {
               if (!beanDefinition.getMetadata().isAnnotation()) {
                   isCandidate = true;
               }
           }
           return isCandidate;
       }

       @Override
       protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
           return super.doScan(basePackages);
       }
   }

}
