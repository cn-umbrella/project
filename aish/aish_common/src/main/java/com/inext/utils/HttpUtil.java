package com.inext.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * httpclent
 *
 * @author gaoyuhai
 */
public class HttpUtil {

    public static final String CHARSET = "UTF-8";
    private static Logger loger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * @param url
     * @return
     */
    public static String doPost(String url, String params) throws ClientProtocolException, IOException {
        loger.debug("请求参数:" + params.toString());
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(params.toString(), "utf-8");
        //stringEntity.setContentType("application/json");
        httpPost.setHeader("Content-Type", "text/xml;charset=utf-8");

        httpPost.setEntity(stringEntity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } finally {
            response.close();
        }
        return result;
    }

    public static String doPostByJson(String url, String params) throws ClientProtocolException, IOException {
        loger.debug("请求参数:" + params.toString());
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(params.toString(), "utf-8");
        //stringEntity.setContentType("application/json");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        httpPost.setEntity(stringEntity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } finally {
            response.close();
        }
        return result;
    }

    public static String doPostByForm(String url, Map<String, String> params) throws ClientProtocolException, IOException {
        loger.debug("请求参数:" + params.toString());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            String result = "";
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("registerFrom", params.get("registerFrom")));
            nvps.add(new BasicNameValuePair("content", params.get("content")));
            // 转码 封装成请求实体
            HttpEntity reqEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
            httpPost.setEntity(reqEntity);
            // 提交表单请求 response是表单的响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity respEntity = response.getEntity();
                // 响应状态
                // 获取响应内容
                result = EntityUtils.toString(respEntity, Charset.forName("utf-8"));
                // 销毁
                EntityUtils.consume(respEntity);
                return result;
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }

    /**
     * 扫描图片信息
     *
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @param contentType 没有传入文件类型默认采用application/octet-stream
     *                    contentType非空采用filename匹配默认的图片类型
     * @return 返回response数据
     */
    @SuppressWarnings("rawtypes")
    public static String formUploadImage(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes(CHARSET));
            }
            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();

                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    contentType = new MimetypesFileTypeMap().getContentType(file);
                    //contentType非空采用filename匹配默认的图片类型
                    if (!"".equals(contentType)) {
                        if (filename.endsWith(".png")) {
                            contentType = "image/png";
                        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                            contentType = "image/jpeg";
                        } else if (filename.endsWith(".gif")) {
                            contentType = "image/gif";
                        } else if (filename.endsWith(".ico")) {
                            contentType = "image/image/x-icon";
                        }
                    }
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes(CHARSET));
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 读取返回数据
                StringBuffer strBuf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), CHARSET));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                reader.close();
                reader = null;
            } else {
                StringBuffer error = new StringBuffer();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream(), CHARSET));
                String line1 = null;
                while ((line1 = bufferedReader.readLine()) != null) {
                    error.append(line1).append("\n");
                }
                res = error.toString();
                bufferedReader.close();
                bufferedReader = null;
            }
            loger.info("返回请求参数:" + responseCode + " msg=" + res);
        } catch (Exception e) {
            loger.info("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    /**
     * 扫描图片信息 网络IO
     *
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @param contentType 没有传入文件类型默认采用application/octet-stream
     *                    contentType非空采用filename匹配默认的图片类型
     * @return 返回response数据
     */
    @SuppressWarnings("rawtypes")
    public static String formUploadImage2(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes(CHARSET));
            }
            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    URL fileUrl = new URL(inputValue);
                    HttpURLConnection fileConn = (HttpURLConnection)fileUrl.openConnection();
                    //设置超时间为3秒
                    fileConn.setConnectTimeout(3*1000);
                    //防止屏蔽程序抓取而返回403错误
                    fileConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                    //得到输入流
                    InputStream inputStream = fileConn.getInputStream();
                    //获取自己数组
                    byte[] getData = readInputStream(inputStream);

                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + inputValue
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes(CHARSET));
                    out.write(getData);
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 读取返回数据
                StringBuffer strBuf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), CHARSET));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                reader.close();
                reader = null;
            } else {
                StringBuffer error = new StringBuffer();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream(), CHARSET));
                String line1 = null;
                while ((line1 = bufferedReader.readLine()) != null) {
                    error.append(line1).append("\n");
                }
                res = error.toString();
                bufferedReader.close();
                bufferedReader = null;
            }
            loger.info("返回请求参数:" + responseCode + " msg=" + res);
        } catch (Exception e) {
            loger.info("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }


    /**
     *    * 从输入流中获取字节数组
     *    * @param inputStream
     *    * @return
     *    * @throws IOException
     *    
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static String doPost(String url, Map<String, Object> param) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                if (entry.getValue() != null) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            HttpResponse response = httpClient.execute(httpPost);
            String resp = EntityUtils.toString(response.getEntity(), "utf-8");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * String stringA="app_key=qzbauzh8cl2qzd9qssnkrfly4t4nk6m6" +
     * "&batch_num=ces001" +
     * "&content=【流浪猫】尊敬的用户，本次验证码为123456，请勿泄露！" +
     * "&dest_id=15267797477" +
     * "&mission_num=test001" +
     * "&nonce_str=5iliulang" +
     * "&sms_type=verify_code" +
     * "&time_stamp=20160520170033";
     *
     * @param xmlDto
     * @return
     */
//    public static String sortString(XmlDto xmlDto) {
//        if (null != xmlDto) {
//            StringBuffer sb = new StringBuffer();
//            sb.append("app_key=" + xmlDto.getHead().getApp_key())
//                    .append("&batch_num=" + xmlDto.getBody().getBatch_num())
//                    .append("&content=" + xmlDto.getBody().getContent());
//            List<Dest> list = xmlDto.getBody().getDests().getDest();
//            StringBuffer sb0 = new StringBuffer();
//            for (Dest d : list) {
//                sb0.append("&dest_id=" + d.getDest_id());
//            }
//            for (Dest d : list) {
//                sb0.append("&mission_num=" + d.getMission_num());
//            }
//            sb.append(sb0)
//                    .append("&nonce_str=" + xmlDto.getHead().getNonce_str())
//                    .append("&sms_type=" + xmlDto.getBody().getSms_type())
//                    .append("&time_stamp=" + xmlDto.getHead().getTime_stamp());
//
//            return sb.toString();
//        }
//        return null;
//    }
//
//    public static void main(String[] args) throws NoSuchAlgorithmException, ClientProtocolException, IOException {
//        String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
//        String appsecret = "&app_secret=a1df1b975bd9efc6a0d6d7d4c496f1007164";
//        XmlDto xmlDto = new XmlDto();
//        Head head = new Head();
//        head.setApp_key("qzbauzh8cl2qzd9qssnkrfly4t4nk6m6");
//        head.setNonce_str("5iliulang");
//        head.setSign("");
//        head.setTime_stamp("20160824143800");
//
//
//        Dest dest1 = new Dest();
//        dest1.setDest_id("13621877719");
//        dest1.setMission_num("kodou001");
//        List<Dest> list = new ArrayList<Dest>();
//        list.add(dest1);
////		list.add(dest1);
//        Dests ds = new Dests();
//        ds.setDest(list);
//        Body body = new Body();
//        body.setBatch_num("kodou1001");
//        body.setDests(ds);
//        body.setContent("【流浪猫】尊敬的用户，本次验证码为123456，请勿泄露！");
//        body.setSms_type("verify_code");
//
//
//        xmlDto.setHead(head);
//        xmlDto.setBody(body);

        //String string= XmlUtil.bulidMessage(xmlDto);
        //System.out.println(string);


//		String stringA="app_key=qzbauzh8cl2qzd9qssnkrfly4t4nk6m6" +
//				"&batch_num=ces001" +
//				"&content=【流浪猫】尊敬的用户，本次验证码为123456，请勿泄露！" +
//				"&dest_id=13621877719" +
//				"&mission_num=test001" +
//				"&nonce_str=5iliulang" +
//				"&sms_type=verify_code" +
//				"&time_stamp=20160824121100";
//		
//		System.out.println("stringA="+stringA);
//        String stringB = sortString(xmlDto);
//        System.out.println("排序后=" + stringB);
//
//
////		stringA = stringA+"&app_secret=a1df1b975bd9efc6a0d6d7d4c496f1007164";
//        stringB = stringB + appsecret;
////		String md5StrA = new MD5codingLowCase().code(stringA);
////		System.out.println(md5StrA);
//        System.out.println("加密前=" + stringB);
//        String md5StrB = new MD5codingLowCase().code(stringB);
//        System.out.println("加密后=" + md5StrB);
//        ;
//        xmlDto.getHead().setSign(md5StrB);
//
//        String stringXml = XmlUtil.bulidMessage(xmlDto);
//        String requestXml = xmlHead + stringXml;
//        System.out.println(requestXml);
//
//        System.out.println("----------------------发送---------------------------");
//        String result = doPost("http://api.5iliulang.com/api/v1/manySend", requestXml);
//        System.out.println("result=" + result);
//    }

    public static JSONObject postForForm(String url, Map<String, String> parms) {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<BasicNameValuePair> list = new ArrayList<>();
        parms.forEach((key, value) -> list.add(new BasicNameValuePair(key, value)));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            if (Objects.nonNull(parms) && parms.size() >0) {
                httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            }
            InputStream content = httpPost.getEntity().getContent();
            InputStreamReader inputStreamReader = new InputStreamReader(content, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readLine = bufferedReader.readLine();
            System.out.println("readLine===================================" + readLine);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            JSONObject result = JSON.parseObject(EntityUtils.toString(entity, "UTF-8"));
            loger.info("HttpUtil >>> postForForm 保存黑名单接口:{}",result);
            return result;
        }catch (IOException e) {
            loger.error("HttpUtil >>> postForForm 保存黑名单接口异常:{}", ExceptionUtils.getStackTrace(e));
        }finally {
            if (Objects.nonNull(httpClient)){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    loger.error("HttpUtil >>> postForForm 保存黑名单接口流关闭异常:{}",ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return null;
    }

}
