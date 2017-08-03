package com.zxn.news.bean;

import java.util.List;

/**
 * Created by zxn on 2017-08-03.
 * Test 手动写json类
 */

public class Test {
    private int retcode;
    private List<DetailPagerData> data;
    private List<Integer> extend;

    public List<DetailPagerData> getData() {
        return data;
    }

    public void setData(List<DetailPagerData> data) {
        this.data = data;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public List<Integer> getExtend() {
        return extend;
    }

    public void setExtend(List<Integer> extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "Test{" +
                "retcode=" + retcode +
                ", data=" + data +
                ", extend=" + extend +
                '}';
    }

    public static class DetailPagerData{
        private List<ChildrenData> children;
        private int id;
        private int type;
        private String title;
        private String url;
        private String url1;
        private String excurl;
        private String dayurl;
        private String weekurl;

        public List<ChildrenData> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenData> children) {
            this.children = children;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getExcurl() {
            return excurl;
        }

        public void setExcurl(String excurl) {
            this.excurl = excurl;
        }

        public String getDayurl() {
            return dayurl;
        }

        public void setDayurl(String dayurl) {
            this.dayurl = dayurl;
        }

        public String getWeekurl() {
            return weekurl;
        }

        public void setWeekurl(String weekurl) {
            this.weekurl = weekurl;
        }

        @Override
        public String toString() {
            return "DetailPagerData{" +
                    "children=" + children +
                    ", id=" + id +
                    ", type=" + type +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", url1='" + url1 + '\'' +
                    ", excurl='" + excurl + '\'' +
                    ", dayurl='" + dayurl + '\'' +
                    ", weekurl='" + weekurl + '\'' +
                    '}';
        }

        public static class ChildrenData {
            private int id;
            private int type;
            private String title;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public String toString() {
                return "ChildrenData{" +
                        "id=" + id +
                        ", type=" + type +
                        ", title='" + title + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }
    }

}
