package cn.hollycloud.iplatform.common.utils;

import cn.hollycloud.iplatform.common.constant.ValueConstant;
import cn.hollycloud.iplatform.common.exception.ServiceFailException;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2019-09-24
 * Time: 20:24
 */
@Component
public class HttpUtils {
    @Autowired(required = false)
    private RestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private <T> T doRequest(String url, HttpMethod method, HttpEntity requestEntity, Class<T> responseType, ParameterizedTypeReference type) {
        try {
            ResponseEntity<T> responseEntity = null;
            if (responseType != null) {
                responseEntity = restTemplate.exchange(url,
                        method, requestEntity, responseType);
            } else {
                responseEntity = restTemplate.exchange(url,
                        method, requestEntity, type);
            }
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                logger.error("{}请求出错,响应码:{}", url, responseEntity.getStatusCode());
                throw new ServiceFailException(StrUtil.format("{}请求出错,响应码:{}", url, responseEntity.getStatusCode()));
            }
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new ServiceFailException("网络请求错误");
        }
    }

    public <T> T doGet(String url, MultiValueMap params, MultiValueMap headers, Class<T> responseType, ParameterizedTypeReference type) {
        String urlParam = createUrlParamsByMap(params);
        if (StringUtils.isNotEmpty(urlParam)) {
            url += "?" + urlParam;
        }
        HttpEntity requestEntity = new HttpEntity(null, headers);
        return doRequest(url, HttpMethod.GET, requestEntity, responseType, type);
    }

    public <T> T doGet(String url, MultiValueMap params, MultiValueMap headers, Class<T> responseType) {
        return doGet(url, params, headers, responseType, null);
    }

    public <T> T doGet(String url, MultiValueMap params, Class<T> responseType) {
        return doGet(url, params, null, responseType);
    }

    public String doGet(String url, MultiValueMap params) {
        return doGet(url, params, String.class);
    }

    public String doGet(String url) {
        return doGet(url, null);
    }

    public <T> T doPost(String url, MultiValueMap params, MultiValueMap headers, Class<T> responseType, ParameterizedTypeReference type) {
        HttpEntity requestEntity = new HttpEntity(params, headers);
        return doRequest(url, HttpMethod.POST, requestEntity, responseType, type);
    }

    public <T> T doPost(String url, MultiValueMap params, MultiValueMap headers, Class<T> responseType) {
        return doPost(url, params, headers, responseType, null);
    }

    public <T> T doPost(String url, MultiValueMap params, Class<T> responseType) {
        return doPost(url, params, null, responseType);
    }

    public String doPost(String url, MultiValueMap params) {
        return doPost(url, params, String.class);
    }

    public String doPost(String url) {
        return doPost(url, null);
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    private String createUrlParamsByMap(MultiValueMap<String, Object> map) {
        try {
            if (map == null) {
                return "";
            }
            StringBuffer sb = new StringBuffer();
            Set<Map.Entry<String, List<Object>>> set = map.entrySet();
            for (Map.Entry<String, List<Object>> entry : set) {
                List<Object> list = entry.getValue();
                for (Object val : list) {
                    sb.append(entry.getKey() + "=" + URLEncoder.encode(val.toString(), ValueConstant.CHARSET));
                    sb.append("&");
                }
            }
            String s = sb.toString();
            if (s.endsWith("&")) {
                s = StringUtils.substringBeforeLast(s, "&");
            }
            return s;
        } catch (Exception e) {
            logger.error("map转url参数异常{}", e);
        }
        return "";
    }

}
