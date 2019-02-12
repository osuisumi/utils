package com.isumi.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;

public class HttpClient {

    // 编码格式。发送编码格式统一用UTF-8
    private static final String ENCODING = "UTF-8";

    // 设置连接超时时间，单位毫秒。
    private static final int CONNECT_TIMEOUT = 6000;

    // 请求获取数据的超时时间(即响应时间)，单位毫秒。
    private static final int SOCKET_TIMEOUT = 6000;

    /**
     * 发送get请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * 发送get请求；带请求参数
     *
     * @param url 请求地址
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, null, params);
    }

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpGet.setConfig(requestConfig);

        // 设置请求头
        packageHeader(headers, httpGet);

        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        try {
            // 执行请求并获得响应结果
            return getString(httpResponse, httpClient, httpGet);
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
    }

    /**
     * 发送post请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doPost(String url) throws Exception {
        return doPost(url, null, null);
    }

    /**
     * 发送post请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> params) throws Exception {
        return doPost(url, null, params);
    }

    /**
     * 发送post请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        packageHeader(headers, httpPost);

        // 封装请求参数
        packageParam(params, httpPost);

        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        try {
            // 执行请求并获得响应结果
            return getString(httpResponse, httpClient, httpPost);
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
    }

    public static String doPost(String url,String stringEntity) throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(new StringEntity(stringEntity));
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求并获得响应结果
            return getString(httpResponse, httpClient, httpPost);
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
    }

    /**
     * 发送put请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doPut(String url) throws Exception {
        return doPut(url);
    }

    /**
     * 发送put请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doPut(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPut.setConfig(requestConfig);

        packageParam(params, httpPut);

        CloseableHttpResponse httpResponse = null;

        try {
            return getString(httpResponse, httpClient, httpPut);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * 发送delete请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doDelete(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpDelete.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        try {
            return getString(httpResponse, httpClient, httpDelete);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * 发送delete请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doDelete(String url, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<String, String>();
        }

        params.put("_method", "delete");
        return doPost(url, params);
    }

    /**
     * Description: 封装请求头
     * @param params
     * @param httpMethod
     */
    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 封装请求参数
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    public static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
        }
    }

    /**
     * Description: 获得响应结果
     *
     * @param httpResponse
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static String getString(CloseableHttpResponse httpResponse,
                                                       CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        // 执行请求
        httpResponse = httpClient.execute(httpMethod);

        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
            }
            return content;
        }
        return null;
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

    public static void downLoad(String url,String remoteFileName, String localFileName) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        OutputStream out = null;
        InputStream in = null;

        try {
            HttpGet httpGet = new HttpGet(url);

//            httpGet.addHeader("userName", userName);
//            httpGet.addHeader("passwd", passwd);
            httpGet.addHeader("fileName", remoteFileName);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            in = entity.getContent();

            long length = entity.getContentLength();
            if (length <= 0) {
                System.out.println("下载文件不存在！");
                return;
            }

            System.out.println("The response value of token:" + httpResponse.getFirstHeader("token"));

            File file = new File(localFileName);
            if(!file.exists()){
                file.createNewFile();
            }

            out = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int readLength = 0;
            while ((readLength=in.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                out.write(bytes);
            }

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}