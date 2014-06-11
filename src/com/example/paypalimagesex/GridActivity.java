package com.example.paypalimagesex;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GridActivity extends Activity {

	private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath, arrName, arrDate;
    private ImageAdapter imageAdapter;
	GridView gridView;
	int id ;
	//private getPicsLoaded myAsychImageTask ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        gridView = (GridView) findViewById(R.id.gridView1);
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, 
        		MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        count = imagecursor.getCount();
        thumbnails = new Bitmap[count];
        arrPath = new String[count];
        arrName = new String[count];
        arrDate = new String[count];
        thumbnailsselection = new boolean[count];
        for (int i = 0; i < count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int nameColumn = imagecursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int date_Column = imagecursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);
            arrPath[i]= imagecursor.getString(dataColumnIndex);
            arrName[i]= imagecursor.getString(nameColumn);
            arrDate[i]= imagecursor.getString(date_Column);
        }
        imageAdapter = new ImageAdapter();
        gridView.setAdapter(imageAdapter);
        imagecursor.close();
    
    }
  

        public class ImageAdapter extends BaseAdapter {
            private LayoutInflater mInflater;
     
            public ImageAdapter() {
                mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
     
            public int getCount() {
                return count;
            }
     
            public Object getItem(int position) {
                return position;
            }
     
            public long getItemId(int position) {
                return position;
            }
     
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
               
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = mInflater.inflate(
                            R.layout.images_added, null);
                    holder.imageview = (ImageView) convertView.findViewById(R.id.grid_item_image);
                    holder.textview = (TextView) convertView.findViewById(R.id.grid_item_date);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.imageview.setId(position);
                holder.textview.setId(position);
                holder.imageview.setOnClickListener(new View.OnClickListener() {
     
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        id = v.getId();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
                        startActivity(intent);
                    }
                });
                holder.imageview.setImageBitmap(thumbnails[position]);
                holder.id = position;
                holder.textview.setText(arrName[position]+","+ arrDate[position]);
                return convertView;
            }
        }
        class ViewHolder {
            TextView textview;
			ImageView imageview;
            int id;
        }
}
