package com.wuwind.undercover.db.litepal;

import org.litepal.LitePal;
import org.litepal.Operator;
import org.litepal.crud.LitePalSupport;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wuhf on 2020/7/3.
 * Description ：
 */
public class DbSupport extends LitePalSupport {

    private int id;
    private int serviceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void saveFromService() {
        DbSupport first = Operator.where("serviceId = ?", ""+id).findFirst(getClass());
        if(null != first) {
            update(first.getId());
        } else {
            serviceId = id;
            save();
        }
    }

    public void delFromService() {
        DbSupport first = Operator.where("serviceId = ?", ""+id).findFirst(getClass());
//        DbSupport dbSupport = Operator.find(getClass(), id);
//        Object o = LitePal.find(getClass(), id);
        if(null != first) {
            first.delete();
        }
    }

//    private Class getGenericType() {
//        Type genType = getClass().getGenericSuperclass();
//        if (!(genType instanceof ParameterizedType)) {
//            return Object.class;
//        }
//        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
//        if (!(params[0] instanceof Class)) {
//            return Object.class;
//        }
//        return (Class) params[0];
//    }

}
