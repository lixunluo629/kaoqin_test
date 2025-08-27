package com.moredian.onpremise.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/GeneratorMapper.class */
public class GeneratorMapper {
    @Test
    public void test() throws Exception {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(GeneratorMapper.class.getResourceAsStream("/generatorConfig.xml"));
        Context context = config.getContext("Mysql");
        List<TableConfiguration> configs = context.getTableConfigurations();
        for (TableConfiguration c : configs) {
            c.setCountByExampleStatementEnabled(false);
            c.setUpdateByExampleStatementEnabled(false);
            c.setUpdateByPrimaryKeyStatementEnabled(false);
            c.setDeleteByExampleStatementEnabled(false);
            c.setSelectByExampleStatementEnabled(false);
        }
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate((ProgressCallback) null);
    }
}
