package com.example.cheng.enet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

interface ICallback {

    public void callback(String params);
}

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        RelativeLayout reLayout = new RelativeLayout(this);

        new Login().login(new ICallback() {
            @Override
            public void callback(String params) {
//                new  AlertDialog.Builder(MainActivity.this)
//                        .setTitle("标题" )
//                        .setMessage("简单消息框" )
//                        .setPositiveButton("确定" ,  null )
//                        .show();
//                System.out.println("--------这是回调函数-----------");
//                if (params==null)
//                {
//
//                    System.out.println("返回的参数值为空");
//                    Looper.prepare();
//                    Toast toast=Toast.makeText(MainActivity.this, "你点击了按钮", Toast.LENGTH_SHORT);
//                    Looper.loop();
//                    toast.setGravity(Gravity.TOP,0,150);
//                    toast.show();
//                    Toast.makeText(getApplicationContext(), "您当前在线，不必登录！", Toast.LENGTH_LONG).show();
//
//                }
//                else if (params.equals("success"))
//                {
////                    Looper.prepare();
////                    System.out.println("返回的参数值不为空");
//                    Toast toast=Toast.makeText(MainActivity.this, "你点击了按钮", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.TOP,0,150);
//                    toast.show();
//                    Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_LONG).show();
////                    Looper.loop();
//                }
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                android.os.Process.killProcess(android.os.Process.myPid()) ;
            }
        });



    }

    //
    public void killSelf() {
        new  AlertDialog.Builder(MainActivity.this)
                .setTitle("标题" )
                .setMessage("简单消息框" )
                .setPositiveButton("确定" ,  null )
                .show();

    }
//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

class Login {

    public void login(final ICallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("Connecting", "connecting ...............");
                String redirectUrl = Login.this.sendGet();
                String[] macAndIP = Login.this.getMacAndIP(redirectUrl);
//                System.out.println("***********" + macAndIP[0] + "*******" + macAndIP[1]);
//                System.out.println("^^^^^^^^^^^^^" + macAndIP.length);
//                System.out.println(macAndIP[0]);
                if (macAndIP[0].length() == 0) {
                    Looper.prepare();
//                    System.out.println("空空空空空");
                    callback.callback(null);
                    return;
                }
                try {
                    Login.this.sendPost(macAndIP);
                    callback.callback("success");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();
    }

            /**
             * 向指定URL发送GET方法的请求
             *
             * @return URL 所代表远程资源的响应结果
             */
            public String sendGet() {
//                Log.i("get", "getting ...............");
                String result = "";
                BufferedReader in = null;
                try {
//            String urlNameString = url + "?" + param;
                    String urlNameString = "http://login.ecust.edu.cn/&arubalp=07ab5286-6c88-4ee7-997d-1563e8afb4";
//            String urlNameString = "172.20.3.81";
                    URL realUrl = new URL(urlNameString);
                    // 打开和URL之间的连接
                    HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
                    // 设置通用的请求属性
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    // 关闭重定向
                    connection.setInstanceFollowRedirects(false);
                    // 建立实际的连接
                    connection.connect();
                    // 获取所有响应头字段
                    Map<String, List<String>> map = connection.getHeaderFields();
                    // 遍历所有的响应头字段
                    for (String key : map.keySet()) {
                        System.out.println(key + "--->" + map.get(key));
                        if (key != null && key.equals("Location")) {
                            result = String.valueOf(map.get(key));
                            break;
                        }
                    }
                } catch (Exception e) {
//                    System.out.println("发送GET请求出现异常！" + e);
                    e.printStackTrace();
                }
                // 使用finally块来关闭输入流
                finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                return result;
            }

            private String[] getMacAndIP(String s) {
                Log.i("man and ip", "mac......ip.........");
                String mac = "";
                String ip = "";
                // 拿到了返回的重定向的地址
                String[] results = s.split("&");
                for (String value : results) {
                    String[] key_values = value.split("=");
                    if (key_values[0] != null && key_values[0].equals("mac")) {
                        mac = key_values[1];
                    }
                    if (key_values[0] != null && key_values[0].equals("ip")) {
                        ip = key_values[1];
                    }
                }
//                System.out.println(mac);
//                System.out.println(ip);
                return new String[]{mac, ip};
            }

            // HTTP POST请求
            private void sendPost(String[] params) throws Exception {

                Log.i("post", "posting ...............");
                String mac = params[0];
                String ip = params[1];
                String url = "http://172.20.3.81:801/include/auth_action.php";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //添加请求头
                con.setRequestMethod("POST");

                String urlParameters =
                        "action=login&username=Y30150635@free&password={B}d2FuZ2NoZW5n&ac_id=3&user_ip=" + ip + "&nas_ip=''&user_mac=" + mac + "&ajax=1";

                //发送Post请求
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
//                System.out.println("\nSending 'POST' request to URL : " + url);
//                System.out.println("Post parameters : " + urlParameters);
//                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //打印结果
//                System.out.println(response.toString());

            }
        }



