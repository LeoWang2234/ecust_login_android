package com.example.cheng.enet;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

public class MainActivity extends AppCompatActivity {


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
//                System.out.println("进入回调函数");
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });


    }


}

class Login {

    public static String username;
    public static String password;

    public void login(final ICallback callback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                Log.i("Connecting", "connecting ...............");
                String redirectUrl = Login.this.sendGet();
                String[] macAndIP = Login.this.getMacAndIP(redirectUrl);
//                System.out.println("***********" + macAndIP[0] + "*******" + macAndIP[1]);
//                System.out.println("^^^^^^^^^^^^^" + macAndIP.length);
//                System.out.println(macAndIP[0]);
                if (macAndIP[0].length() == 0) {
                    Looper.prepare();
//                    System.out.println("空空空空空");
                    callback.callback(null);
                }
                try {
//                    System.out.println("正准备发送请求。。。。。");
                    Login.this.sendPost(macAndIP);
                    callback.callback("success");
                } catch (Exception e) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                } finally {
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
//        System.out.println("正发送 get 请求。。。。。。");
        String result = "";
        BufferedReader in = null;
        try {
//            String urlNameString = url + "?" + param;
            String urlNameString = "http://172.20.3.90/cgi-bin/login?cmd=redirect&arubalp=7cc24a6f-ca75-477c-89fa-5858bec3c5";
//            String urlNameString = "172.20.3.81";
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 关闭重定向
            connection.setInstanceFollowRedirects(true);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            connection.getResponseCode();
            System.out.println( connection.getURL().toString());
            result =  connection.getURL().toString();
        } catch (Exception e) {
//            System.out.println("发送GET请求出现异常！" + e);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {

            } finally {
            }
        }
//        System.out.println("返回结果。。。。。。");
//        System.out.println(result);
        return result;
    }

    private String[] getMacAndIP(String s) {
//        Log.i("man and ip", "mac......ip.........");
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

//        Log.i("post", "posting ...............");
        String mac = params[0];
        String ip = params[1];
        String url = "http://172.20.3.81:801/include/auth_action.php";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //添加请求头
        con.setRequestMethod("POST");
        con.setConnectTimeout(1000);

        username = username != null ? username : (username = PrefFileManager.getAccountPref().getString("username", null));
        password = password != null ? password : (password = PrefFileManager.getAccountPref().getString("password", null));
        password = Utils.decode(password.getBytes());

//        System.out.println("++++++++++++++++++++++++++++++++++" + username);
//        System.out.println("++++++++++++++++++++++++++++++++" + password);

        String urlParameters =
                "action=login&username=" + username + "@free&password={B}" + password + "&ac_id=3&user_ip=" + ip + "&nas_ip=''&user_mac=" + mac + "&ajax=1";

        //发送Post请求
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

//        int responseCode = con.getResponseCode();
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



