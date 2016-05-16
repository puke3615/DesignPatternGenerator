package com.mogujie.pattern.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiao
 * @version 16/5/13
 * @Mark
 */
public class MethodInfo extends Info {

    public String name;
    private List<ParamInfo> paramInfoList;

    public List<ParamInfo> getParamInfoList() {
        if (paramInfoList == null) {
            paramInfoList = new ArrayList<>();
        }
        return paramInfoList;
    }
}
