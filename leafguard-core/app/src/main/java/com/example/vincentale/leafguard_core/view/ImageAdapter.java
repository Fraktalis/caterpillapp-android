package com.example.vincentale.leafguard_core.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vincentale.leafguard_core.R;

import java.io.File;

/**
 * Created by Administrator on 18/12/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private File imagesFile;

    public ImageAdapter(File folderFile) {
        imagesFile = folderFile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_images_relative_layout, parent, false);
        return new ViewHolder(view);
    }

    // Get images, resize it and put it in the ImageView to display it
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File imageFile = imagesFile.listFiles()[position];
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        if (imageBitmap != null) {
            int nh = (int) ( imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 512, nh, true);
            holder.getImageView().setImageBitmap(scaled);
        }

    }

    // If we need to get the number of images, count it
    @Override
    public int getItemCount() {
        if (imagesFile.listFiles() == null) {
            return 0;
        }
        return imagesFile.listFiles().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageGalleryView);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
