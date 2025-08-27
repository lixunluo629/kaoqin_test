package tk.mybatis.spring.mapper;

import tk.mybatis.mapper.mapperhelper.MapperHelper;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/spring/mapper/MapperFactoryBean.class */
public class MapperFactoryBean<T> extends org.mybatis.spring.mapper.MapperFactoryBean<T> {
    private MapperHelper mapperHelper;

    @Override // org.mybatis.spring.mapper.MapperFactoryBean, org.mybatis.spring.support.SqlSessionDaoSupport, org.springframework.dao.support.DaoSupport
    protected void checkDaoConfig() {
        super.checkDaoConfig();
        if (this.mapperHelper.isExtendCommonMapper(getObjectType())) {
            this.mapperHelper.processConfiguration(getSqlSession().getConfiguration(), getObjectType());
        }
    }

    public void setMapperHelper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }
}
