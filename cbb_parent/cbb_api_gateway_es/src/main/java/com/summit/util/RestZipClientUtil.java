package com.summit.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import net.sf.json.JSONObject;

/**
 * client 调用rest接口工具类
 *
 * @author 叶腾
 * @version 1.0
 */
public class RestZipClientUtil {

    public static String restPost(String url, JSONObject js) {
        ByteArrayOutputStream out = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost method = new HttpPost(url);
            method.addHeader("Accept", "text/html;charset=ISO-8859-1");
            method.addHeader("Accept-Encoding", "gzip");
            HttpEntity formEntity = new StringEntity(GzipUtils.compress(js.toString()), "ISO-8859-1");
            method.setEntity(formEntity);
            HttpResponse response = httpClient.execute(method);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("error...");
            }
            out = new ByteArrayOutputStream();

            GZIPInputStream gunzip = new GZIPInputStream(response.getEntity().getContent());
            byte[] buffer = new byte[1024];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toString();
    }
}
