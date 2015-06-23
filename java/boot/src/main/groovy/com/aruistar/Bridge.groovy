package com.aruistar

import groovy.util.logging.Log
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

    private Map fileMap = [:]

    @RemotingInclude
    String hello() {
        return "hello flex"
    }

    @RemotingInclude
    Map upload(Map map) {
        def result = [:]
        result.state = 0

        if (map.length > 0) {
            if (map.step == 1) {
                fileMap[map.uid] = [:]
                fileMap[map.uid].length = map.length
                fileMap[map.uid].step = 0

                deleteFile(map.name)
                //fileMap[map.uid].data = []
            }

            def localFile = fileMap[map.uid]
            if (localFile) {
                if (map.step == localFile.step + 1) {
                    //localFile.data << map.data
                    localFile.step = map.step;

                    result.state = 1

                    saveFile(map.name, map.data as byte[])

                    if (localFile.step == localFile.length) {
                        Log.println('数据队列获取完毕')
                        result.message = 'success'
                        Log.println("写入文件成功，地址：" + getPath(map.name))
                    }


                } else {
                    Log.println('包队列丢失')
                    result.state = 0
                }

            } else {
                Log.println('基础包丢失，原因：首包丢失')
                result.state = 0
            }


        } else {
            if (saveFile(map.name, map.data as byte[])) {
                result.state = 1;
                result.message = 'success'
                Log.println("写入文件成功，地址：" + getPath(map.name))
            }
        }

        return result;
    }

    private boolean saveFile(def name, byte[] data) {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(getPath(name)), true)
        fileOutputStream.write(data)
        fileOutputStream.close()

        return true;
    }

    private void deleteFile(def name) {
        new File(getPath(name)).delete()
    }

    private String getPath(def name) {
        return "/Users/liurui/develop/$name"
    }
}

