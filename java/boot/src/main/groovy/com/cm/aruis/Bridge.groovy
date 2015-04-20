package com.cm.aruis

import org.springframework.beans.factory.annotation.Value
import org.springframework.flex.remoting.RemotingDestination
import org.springframework.flex.remoting.RemotingInclude
import org.springframework.stereotype.Service

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
}

