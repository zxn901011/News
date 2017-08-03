package com.zxn.news.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zxn on 2017-08-03.
 * Test类
 * 用native解析json
 * 真他妈复杂
 */

public class TestParseJson {
    private Test parseJson(String json){
        Test test=new Test();
        try {
            JSONObject object=new JSONObject(json);
            int retcode=object.optInt("retcode");
            test.setRetcode(retcode);
            JSONArray data=object.optJSONArray("data");
            if (data!=null&&data.length()>0){
                //创建集合装数据
                List<Test.DetailPagerData> detailPagerDatas= (List<Test.DetailPagerData>) data;
                //设置数据
                test.setData(detailPagerDatas);
                for (int i=0;i<data.length();i++){
                    JSONObject object1=data.getJSONObject(i);
                    //创建集合中的对象进行装数据
                    Test.DetailPagerData detailPagerData=new Test.DetailPagerData();
                    detailPagerDatas.add(detailPagerData);
                    int id=object1.optInt("id");
                    detailPagerData.setId(id);
                    int type=object1.optInt("type");
                    detailPagerData.setId(type);
                    String title=object1.optString("title");
                    detailPagerData.setTitle(title);
                    String url=object1.optString("url");
                    detailPagerData.setUrl(url);
                    String url1=object1.optString("url1");
                    detailPagerData.setUrl1(url1);
                    String dayurl=object1.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);
                    String excurl=object1.optString("excurl");
                    detailPagerData.setExcurl(excurl);
                    String weekurl=object1.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);
                    JSONArray children=object.optJSONArray("children");
                    if (children != null&&children.length()>0) {
                        List<Test.DetailPagerData.ChildrenData> childrenDatas= (List<Test.DetailPagerData.ChildrenData>) children;
                        detailPagerData.setChildren(childrenDatas);
                        for (int j=0;j<children.length();j++){
                            JSONObject childrenItem= (JSONObject) children.get(j);
                            Test.DetailPagerData.ChildrenData childrenData=new Test.DetailPagerData.ChildrenData();
                            int childId=childrenItem.optInt("id");
                            childrenData.setId(childId);
                            int childType=childrenItem.optInt("type");
                            childrenData.setType(childType);
                            String childTitle=childrenItem.optString("title");
                            childrenData.setTitle(childTitle);
                            String childUrl=childrenItem.optString("url");
                            childrenData.setUrl(childUrl);
                            childrenDatas.add(childrenData);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return test;
    }
}
