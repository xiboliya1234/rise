package com.rise.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class mapmodel {


    public mapmodel(String sbmc, String x, String y,String lx) {
        this.name = sbmc;
        this.longitude = x;
        this.latitude = y;
        this.content = lx;
        this.img ="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F25fac10a-1e66-4a6d-a4a1-4bf07d2666a1%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1690684148&t=c2560eb44f5348fc210f74ccd4552555";


    }

    private String name;
    private String longitude;
    private String latitude;
    private String content;
    private String img ="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F25fac10a-1e66-4a6d-a4a1-4bf07d2666a1%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1690684148&t=c2560eb44f5348fc210f74ccd4552555";



}
