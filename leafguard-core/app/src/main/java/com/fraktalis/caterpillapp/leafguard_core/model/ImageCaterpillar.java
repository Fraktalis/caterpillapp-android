package com.fraktalis.caterpillapp.leafguard_core.model;

/**
 * Created by Administrator on 24/01/2018.
 */

public class ImageCaterpillar {
    String imageURL;
    String uploadId;

    public ImageCaterpillar() {
    }

    public ImageCaterpillar(ImageCaterpillar image, String ref) {
        this.imageURL = image.getImageURL();
        this.uploadId = ref;
    }

    public ImageCaterpillar(String url) {
        this.imageURL = url;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUploadId() {
        return uploadId;
    }
}
