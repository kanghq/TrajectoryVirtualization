package com.hqkang.api.restserver.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class PointConv {
	
	private static HashMap<Point,Point> ptdb = new HashMap<Point, Point>();
	private static class SingletonHolder {  
        private static final PointConv INSTANCE = new PointConv();  
    }
	
    private PointConv (){}  
    public static final PointConv getInstance() {  
        return SingletonHolder.INSTANCE; 
    }  
    
    

	public static Double[] convert(double lon, double lat) {
		Point pt = new Point(lat, lon);
    	Double[] arr = new Double[2];

		if(ptdb.containsKey(pt))
		{
			 arr[0]=ptdb.get(pt).getLongitude();
			 arr[1]=ptdb.get(pt).getLatitude();
			 return arr;
		}
		
		String res = sendGet("http://api.zdoz.net/transgps.aspx"+"?lat="+lat+"&lng="+lon);
    	Map<String,BigDecimal> node=(Map)JSON.parse(res);  
    	
    	arr[0] =  node.get("Lng").doubleValue();
    	arr[1] =  node.get("Lat").doubleValue();
    	ptdb.put(pt, new Point(arr[1],arr[0]));
		return arr;
	}
	
	public static String sendGet(String url) {
        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.生成一个get请求
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            //3.执行get请求并返回结果
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = null;
        try {
            //4.处理结果，这里将结果返回为字符串
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
