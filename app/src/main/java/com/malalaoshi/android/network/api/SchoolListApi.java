package com.malalaoshi.android.network.api;

import com.malalaoshi.android.core.network.api.BaseApi;
import com.malalaoshi.android.core.usercenter.UserManager;
import com.malalaoshi.android.network.result.SchoolListResult;

import java.util.HashMap;

/**
 * Get shcool list
 * Created by tianwei on 4/17/16.
 */
public class SchoolListApi extends BaseApi {


    private static final String URL_SCHOOL = "/api/v1/schools";
    public static final String REGION = "region";

    @Override
    protected String getPath() {
        return URL_SCHOOL;
    }

    @Override
    protected boolean addAuthHeader() {
        return false;
    }

    @Override
    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put(REGION, UserManager.getInstance().getToken());
        return map;
    }

    public SchoolListResult get(long teacherId) throws Exception {
        String subUrl = "?teacher=" + teacherId;
        return httpGet(getPath()+subUrl, SchoolListResult.class);
    }

    public SchoolListResult getSchoolsByCityId(long cityId) throws Exception {
        String subUrl = "?region=" + cityId;
        return httpGet(getPath()+subUrl, SchoolListResult.class);
    }
}
