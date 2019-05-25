package com.cloud.ibm.banking.IBMBanking.Persistence;

import org.hibernate.EmptyInterceptor;

public class SplitTableInterceptor extends EmptyInterceptor
{
    private String targetTableName;// 目标母表名
    private String tempTableName;// 操作子表名

    public SplitTableInterceptor() {}//为其在spring好好利用 我们生成公用无参构造方法

    public java.lang.String onPrepareStatement(java.lang.String sql) {
        sql = sql.replace("from "+targetTableName,"from "+tempTableName);
        return sql;

    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public String getTempTableName() {
        return tempTableName;
    }

    public void setTempTableName(String tempTableName) {
        this.tempTableName = tempTableName;
    }

}
