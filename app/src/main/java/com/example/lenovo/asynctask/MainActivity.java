package com.example.lenovo.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) this.findViewById(R.id.button);
        mImageView = (ImageView) this.findViewById(R.id.image);
        mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AsyncTaskThread thread = new AsyncTaskThread();
                thread.execute("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488890345&di=08dfba1e6173d0a2e25adfb59d894469&imgtype=jpg&er=1&src=http%3A%2F%2Fi.ebayimg.com%2F00%2Fs%2FMTIwMFgxNjAw%2Fz%2F9TgAAOSwQoFWP309%2F%24_1.JPG%3Fset_id%3D880000500F");
            }
        });
    }
    class AsyncTaskThread extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            publishProgress(0);
            HttpClient client = new DefaultHttpClient();
            publishProgress(30);
            HttpGet get = new HttpGet(params[0]);
            final Bitmap bitmap;
            try {
                HttpResponse response = client.execute(get);
                bitmap = BitmapFactory.decodeStream(response.getEntity()
                        .getContent());
            } catch (Exception e) {
                return null;
            }
            publishProgress(100);
            return bitmap;
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                Toast.makeText(MainActivity.this, "成功获取图片", Toast.LENGTH_LONG)
                        .show();
                mImageView.setImageBitmap(result);
            } else {
                Toast.makeText(MainActivity.this, "获取图片失败", Toast.LENGTH_LONG)
                        .show();
            }
        }

        protected void onPreExecute() {
            mImageView.setImageBitmap(null);
            mProgressBar.setProgress(0);
        }

        protected void onCancelled() {
            mProgressBar.setProgress(0);
        }
    }

}
