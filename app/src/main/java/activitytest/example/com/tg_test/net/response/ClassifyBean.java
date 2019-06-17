package com.example.testnet.response;

import java.util.List;

public class ClassifyBean {

    /**
     * log_id : 4191308424976361169
     * result : [{"baike_info":{"baike_url":"http://baike.baidu.com/item/%E7%BB%98%E7%94%BB/612451","description":"绘画(Drawing 或Painting)在技术层面上，是一个以表面作为支撑面，再在其之上加上颜色的做法，那些表面可以是纸张或布，加颜色的工具可以通过画笔、也可以通过刷子、海绵或是布条等，也可以运用软件进行绘画。在艺术用语的层面上，绘画的意义亦包含利用此艺术行为再加上图形、构图及其他美学方法去达到画家希望表达的概念及意思。绘画在美术中占大部分。","image_url":"http://imgsrc.baidu.com/baike/pic/item/0df431adcbef76097e729e6623dda3cc7dd99ec9.jpg"},"keyword":"绘画","root":"商品-工艺品","score":0.74954},{"baike_info":{"baike_url":"http://baike.baidu.com/item/%E7%BA%B9%E9%A5%B0/2488611","description":"纹饰指提花织物上的花纹图案。主要题材分为自然景物和各种几何图形(包括变体文字等)两大类，有写实、写意、变形等表现手法。设计纹样不仅题材要新颖、艺术上要灵活变化，还要结合织物组织结构特点、织造工艺和织物用途等因素。","image_url":"http://imgsrc.baidu.com/baike/pic/item/6f061d950a7b02084de6e0f360d9f2d3572cc8f1.jpg"},"keyword":"纹饰","root":"非自然图像-图像素材","score":0.588336},{"baike_info":{},"keyword":"简笔画","root":"非自然图像-简笔画","score":0.43092},{"baike_info":{"baike_url":"http://baike.baidu.com/item/%E4%B9%A6%E6%B3%95/177069","description":"书法，是中国及深受中国文化影响过的周边国家和地区特有的一种文字美的艺术表现形式。包括汉字书法、蒙古文书法、阿拉伯书法,英文书法等。其\u201c中国书法\u201d，是中国汉字特有的一种传统艺术。从广义讲，书法是指文字符号的书写法则。换言之，书法是指按照文字特点及其含义，以其书体笔法、结构和章法书写，使之成为富有美感的艺术作品。汉字书法为汉族独创的表现艺术，被誉为：无言的诗，无行的舞；无图的画，无声的乐等。2018年12月，教育部办公厅关于公布绍兴文理学院为书法中华优秀传统文化传承基地。","image_url":"http://imgsrc.baidu.com/baike/pic/item/35a85edf8db1cb136bb87fdeda54564e93584b2d.jpg"},"keyword":"书法","root":"商品-工艺品","score":0.27187},{"baike_info":{"baike_url":"http://baike.baidu.com/item/%E5%87%A4%E7%BA%B9/5967463","description":"凤象征吉祥的神，凤纹亦称凤鸟纹，包括凤纹及各种鸟纹。古老的汉族传统装饰纹样之一。凤纹在青铜器中是最为美丽的纹饰。它是由原始彩陶上的玄鸟演变而来的，西周基本形象是雉，早期凤纹有别于鸟纹最主要的特征是有上扬飞舞的羽翼。","image_url":"http://imgsrc.baidu.com/baike/pic/item/b7003af33a87e950c00aa00c13385343fbf2b4b5.jpg"},"keyword":"凤纹","root":"非自然图像-图像素材","score":0.091023}]
     * result_num : 5
     */

    private long log_id;
    private int result_num;
    private List<ResultBean> result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * baike_info : {"baike_url":"http://baike.baidu.com/item/%E7%BB%98%E7%94%BB/612451","description":"绘画(Drawing 或Painting)在技术层面上，是一个以表面作为支撑面，再在其之上加上颜色的做法，那些表面可以是纸张或布，加颜色的工具可以通过画笔、也可以通过刷子、海绵或是布条等，也可以运用软件进行绘画。在艺术用语的层面上，绘画的意义亦包含利用此艺术行为再加上图形、构图及其他美学方法去达到画家希望表达的概念及意思。绘画在美术中占大部分。","image_url":"http://imgsrc.baidu.com/baike/pic/item/0df431adcbef76097e729e6623dda3cc7dd99ec9.jpg"}
         * keyword : 绘画
         * root : 商品-工艺品
         * score : 0.74954
         */

        private BaikeInfoBean baike_info;
        private String keyword;
        private String root;
        private double score;

        public BaikeInfoBean getBaike_info() {
            return baike_info;
        }

        public void setBaike_info(BaikeInfoBean baike_info) {
            this.baike_info = baike_info;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public static class BaikeInfoBean {
            /**
             * baike_url : http://baike.baidu.com/item/%E7%BB%98%E7%94%BB/612451
             * description : 绘画(Drawing 或Painting)在技术层面上，是一个以表面作为支撑面，再在其之上加上颜色的做法，那些表面可以是纸张或布，加颜色的工具可以通过画笔、也可以通过刷子、海绵或是布条等，也可以运用软件进行绘画。在艺术用语的层面上，绘画的意义亦包含利用此艺术行为再加上图形、构图及其他美学方法去达到画家希望表达的概念及意思。绘画在美术中占大部分。
             * image_url : http://imgsrc.baidu.com/baike/pic/item/0df431adcbef76097e729e6623dda3cc7dd99ec9.jpg
             */

            private String baike_url;
            private String description;
            private String image_url;

            public String getBaike_url() {
                return baike_url;
            }

            public void setBaike_url(String baike_url) {
                this.baike_url = baike_url;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }
        }
    }
}
