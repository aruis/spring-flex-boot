package com.cm.aruis

import groovyx.net.http.ContentType
import groovyx.net.http.EncoderRegistry
import groovyx.net.http.HTTPBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.flex.remoting.RemotingDestination
import org.springframework.flex.remoting.RemotingInclude
import org.springframework.stereotype.Service

import java.lang.reflect.Modifier

/**
 * 用于桥接flex到cm服务
 * Created by liurui on 14/10/24.
 */
@Service('bridge')
@RemotingDestination(channels = "my-amf")
class Bridge {

    @Value('${url}')
    String url
    @Value('${url}')
    String bus

    @RemotingInclude
    public String hello() {
        return "hello flex"
    }

    /**
     * 核心请求方法
     * @param param
     * @return
     */
    @RemotingInclude
    public ResultObject call(ParamObject param) {

        ResultObject resultObject = new ResultObject()

        def http = new HTTPBuilder(url)

        http.encoderRegistry = new EncoderRegistry(charset: 'utf-8')
        http.request(groovyx.net.http.Method.POST, groovyx.net.http.ContentType.JSON) {
            //设置url相关信息
            uri.path = bus
            //uri.query = asMap(param)
            body = asMap(param)
            requestContentType = ContentType.URLENC


            response.success = { resp, json ->
                resultObject.setPropertyByJSON(json.json);
                if (!(param.api.indexOf('BAPI_Hello') > -1)) {
                    println("\n" + new Date().format("yyyy-MM-dd HH:mm:ss") + "\n=========================\n"
                            + param.api + "\n" + param.data + "\n" + resultObject.state + "---->" + resultObject.message + "\n" + resultObject.data + "\n" + resultObject.extend + "\n");
                }
            }

            //根据响应状态码分别指定处理闭包
            response.'404' = { println 'not found' }
            //未根据响应码指定的失败处理闭包
            response.failure = { println "Unexpected failure: ${resp.statusLine}" }
        }

        return resultObject;
    }

    /**
     * 转换obj到map
     * @param obj
     * @return
     */
    public Map asMap(def obj) {
        def map = [:] as HashMap
        obj.class.getDeclaredFields().each {
            if (it.modifiers == Modifier.PUBLIC) {
                map.put(it.name, obj[it.name])
            }
        }
        return map
    }
}

