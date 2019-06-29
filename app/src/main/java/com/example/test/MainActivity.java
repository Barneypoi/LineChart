package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    public JSONObject object;
    public ListView lv;
    public ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }


    private void init() {
        list.clear();
        lv=(ListView) findViewById(R.id.lv);
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址
                Request request=new Request.Builder()
                        .get()
                        .url("http://47.100.120.235:8081/check?value1=1").build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //获取到数据
                    assert response.body() != null;
                    String date=response.body().string();
                    //把数据传入解析josn数据方法
                    jsonJX(date);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();;

    }

    private void jsonJX(String date) {
        //判断数据是空
        if(date!=null){
            try {
                //将字符串转换成jsonObject对象
                //JSONObject jsonObject = new JSONObject(date);
                //获取返回数据中flag的值
                //String resultCode = jsonObject.getString("type");
                //如果返回的值是success则正确
                //if (resultCode.equals("FeatureCollection")){
                    //获取到json数据中里的activity数组内容
                    JSONArray resultJsonArray = new JSONArray(date);
                    //遍历
                    for(int i=0;i<resultJsonArray.length();i++){
                        object=resultJsonArray.getJSONObject(i);

                        Map<String, Object> map=new HashMap<String, Object>();

                        try {
                            //获取到json数据中的activity数组里的内容name
                            String name = object.getString("1");
                            //获取到json数据中的activity数组里的内容startTime
                            String shijian=object.getString("po");
                            //存入map
                            map.put("1", name);
                            map.put("po", shijian);
                            //ArrayList集合
                            list.add(map);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
               // }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }




    }
    //Handler运行在主线程中(UI线程中)，  它与子线程可以通过Message对象来传递数据
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Mybaseadapter list_item=new Mybaseadapter();
                    lv.setAdapter(list_item);
                    break;
            }


        }
    };
    //listview的适配器
    public class Mybaseadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();

        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = getLayoutInflater().inflate(R.layout.listview_item, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);
                viewHolder.shijian = (TextView) convertView.findViewById(R.id.shijian);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            BigDecimal b= new BigDecimal(list.get(position).get("1").toString());
            viewHolder.textView.setText(b.toPlainString());
            viewHolder.shijian.setText(list.get(position).get("po").toString());
            return convertView;
        }

    }

    final static class ViewHolder {
        TextView textView;
        TextView shijian;
    }

}
