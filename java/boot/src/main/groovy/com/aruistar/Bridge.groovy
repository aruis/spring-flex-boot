package com.aruistar

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

    @RemotingInclude
    public String hello() {
        return "hello flex"
    }
}

