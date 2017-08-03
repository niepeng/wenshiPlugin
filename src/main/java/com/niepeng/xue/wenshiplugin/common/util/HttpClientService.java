package com.niepeng.xue.wenshiplugin.common.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Service;
/**
 * @author 聂鹏
 * @version 1.0
 * @email lisenbiao@51huadian.cn
 * @date 17/8/3
 */

@Service
@Slf4j
@ImportResource(locations={"classpath:spring-httpclient.xml"})
public class HttpClientService {

  @Autowired
  private CloseableHttpClient                httpClient;
  @Autowired
  private RequestConfig                      requestConfig;
  @Autowired
  private PoolingHttpClientConnectionManager httpClientConnectionManager;

  /**
   * 执行GET请求
   *
   * @param url
   * @return
   * @throws IOException
   * @throws ClientProtocolException
   */
  public String doGet(String url) throws ClientProtocolException, IOException {
    // 创建http GET请求
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(this.requestConfig);

    CloseableHttpResponse response = null;
    try {
      // 执行请求
      response = httpClient.execute(httpGet);
      // 判断返回状态是否为200
      if (response.getStatusLine().getStatusCode() == 200) {
        return EntityUtils.toString(response.getEntity(), "UTF-8");
      }
    } finally {
      if (response != null) {
        response.close();
      }
    }
    return null;
  }

  /**
   * 带有参数的GET请求
   *
   * @param url
   * @param params
   * @return
   * @throws URISyntaxException
   * @throws IOException
   * @throws ClientProtocolException
   */
  public String doGet(String url, Map<String, String> params) throws ClientProtocolException, IOException, URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(url);
    for (String key : params.keySet()) {
      uriBuilder.addParameter(key, params.get(key));
    }
    return this.doGet(uriBuilder.build().toString());
  }

  /**
   * 执行POST请求
   *
   * @param url
   * @param params
   * @return
   * @throws IOException
   */
  public String doPost(String url, Map<String, String> params) throws IOException {
    // 创建http POST请求
    HttpPost httpPost = new HttpPost(url);
    httpPost.setConfig(this.requestConfig);
    httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    if (params != null) {
      // 设置2个post参数，一个是scope、一个是q
      List<NameValuePair> parameters = new ArrayList<NameValuePair>();
      for (String key : params.keySet()) {
        parameters.add(new BasicNameValuePair(key, params.get(key)));
      }
      // 构造一个form表单式的实体
      UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
      // 将请求实体设置到httpPost对象中
      httpPost.setEntity(formEntity);
    }

    CloseableHttpResponse response = null;
//        String result = null;
    try {
      // 执行请求
      response = httpClient.execute(httpPost);
//            String errorMessage="";
//            Integer statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == 200) {
//                HttpEntity entity = response.getEntity();
//                result = EntityUtils.toString(entity, "UTF-8");
//
//                log.info(result);
//                if (StringUtils.isBlank(result)) {
//                    errorMessage = "response is empty.";
//                }
//            } else {
//                errorMessage = "response statusCode is not 200." + "[status=" + statusCode + "][reason="
//                               + response.getStatusLine().getReasonPhrase() + "]";
//            }
//
//            if (StringUtils.isBlank(result)) {
//                log.error(errorMessage);
//                throw new RuntimeException(errorMessage);
//            }
//
//            return (T) unmarshalResult;
//      return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"));
      return EntityUtils.toString(response.getEntity(), "UTF-8");
    } finally {
      if (response != null) {
        response.close();
      }
    }
  }

  /**
   * 执行POST请求
   *
   * @param url
   * @return
   * @throws IOException
   */
  public String doPost(String url) throws IOException {
    return this.doPost(url, null);
  }

  /**
   * 提交json数据
   *
   * @param url
   * @param json
   * @return
   * @throws ClientProtocolException
   * @throws IOException
   */
  public String doPostJson(String url, String json) throws ClientProtocolException, IOException {
    // 创建http POST请求
    HttpPost httpPost = new HttpPost(url);
    httpPost.setConfig(this.requestConfig);

    if (json != null) {
      // 构造一个form表单式的实体
      StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
      // 将请求实体设置到httpPost对象中
      httpPost.setEntity(stringEntity);
    }

    CloseableHttpResponse response = null;
    try {
      // 执行请求
      response = this.httpClient.execute(httpPost);
      return EntityUtils.toString(response.getEntity(), "UTF-8");
    } finally {
      if (response != null) {
        response.close();
      }
    }
  }

  /**
   * static 发送 SSL POST 请求（HTTPS），K-V形式
   * @param apiUrl API接口URL
   * @param params 参数map
   * @return
   */
  public String doPostSSL(String apiUrl, Map<String, Object> params) {
    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(httpClientConnectionManager)
        .setDefaultRequestConfig(requestConfig).build();
    HttpPost httpPost = new HttpPost(apiUrl);
    CloseableHttpResponse response = null;
    String httpStr = null;

    try {
      httpPost.setConfig(requestConfig);
      List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
        pairList.add(pair);
      }
      httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
      response = httpClient.execute(httpPost);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != HttpStatus.SC_OK) {
        return null;
      }
      HttpEntity entity = response.getEntity();
      if (entity == null) {
        return null;
      }
      httpStr = EntityUtils.toString(entity, "utf-8");
    } catch (Exception e) {
      log.error("Exception ",e);
    } finally {
      if (response != null) {
        try {
          EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
          log.error("IOException ",e);
        }
      }
    }
    return httpStr;
  }

  /**
   * static 发送 SSL POST 请求（HTTPS），JSON形式
   * @param apiUrl API接口URL
   * @param json JSON对象
   * @return
   */
  public String doPostSSL(String apiUrl, Object json) {
    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(httpClientConnectionManager)
        .setDefaultRequestConfig(requestConfig).build();
    HttpPost httpPost = new HttpPost(apiUrl);
    CloseableHttpResponse response = null;
    String httpStr = null;

    try {
      httpPost.setConfig(requestConfig);
      StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");//解决中文乱码问题
      stringEntity.setContentEncoding("UTF-8");
      stringEntity.setContentType("application/json");
      httpPost.setEntity(stringEntity);
      response = httpClient.execute(httpPost);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != HttpStatus.SC_OK) {
        return null;
      }
      HttpEntity entity = response.getEntity();
      if (entity == null) {
        return null;
      }
      httpStr = EntityUtils.toString(entity, "utf-8");
    } catch (Exception e) {
      log.error("Exception ",e);
    } finally {
      if (response != null) {
        try {
          EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
          log.error("IOException ",e);
        }
      }
    }
    return httpStr;
  }

  /**
   * 创建SSL安全连接
   *
   * @return
   */
  private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
    SSLConnectionSocketFactory sslsf = null;
    try {
      SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
          return true;
        }
      }).build();
      sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

        @Override
        public boolean verify(String arg0, SSLSession arg1) {
          return true;
        }

        @Override
        public void verify(String host, SSLSocket ssl) throws IOException {
        }

        @Override
        public void verify(String host, X509Certificate cert) throws SSLException {
        }

        @Override
        public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
        }
      });
    } catch (GeneralSecurityException e) {
      log.error("GeneralSecurityException ",e);
    }
    return sslsf;
  }

}