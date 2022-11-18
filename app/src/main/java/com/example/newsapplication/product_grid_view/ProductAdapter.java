package com.example.newsapplication.product_grid_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsapplication.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater = null;

    public ProductAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_card, parent, false);
        }

        TextView textViewName = view.findViewById(R.id.itemCardTextViewName);
        TextView textViewPrice = view.findViewById(R.id.itemCardTextViewPrice);
        TextView textViewRating = view.findViewById(R.id.itemCardTextViewRating);
        ImageView imageView = view.findViewById(R.id.itemCardImageView);
        HashMap<String, ?> data = (HashMap<String, ?>) getItem(position);
        if (data != null) {
            /*String nameProduct = (String) data.get("nameProduct");
            nameProduct = nameProduct.split(" ", 2)[0];*/
            String priceProduct = "$ " + (String) data.get("priceProduct");
            textViewName.setText((String) data.get("nameProduct"));
            textViewPrice.setText(priceProduct);
            textViewRating.setText((String) data.get("ratingProduct"));
            Picasso.get().load((String) data.get("imageProduct")).into(imageView);
            /*new GetImage(imageView).execute((String) data.get("imageProduct"));*/

            /*GetImage getImage = new GetImage(imageView);
            getImage.execute((String) data.get("imageProduct"));*/
        }

        return view;
    }

    private class GetImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetImage(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new java.net.URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

}
