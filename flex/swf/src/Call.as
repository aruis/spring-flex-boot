/**
 * Created by aruis on 14-1-22.
 * 用于远程方法调用
 */
package {
import mx.rpc.AbstractOperation;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

import spark.components.Alert;

public final class Call {

    public static function easyJava(callback:Function = null, param:Object = null, isload:Boolean = false, info:String = "数据处理中...", p = null, errorCallBack:Function = null):void {
        java('bridge', 'call', callback, param, isload, info, p, errorCallBack);
    }

    public static function java(dest:String, method:String, callback:Function = null, param:Object = null, isload:Boolean = false, info:String = "数据处理中...", p = null, errorCallBack:Function = null):void {
        if (method == null)
            return;


        var ro:RemoteObject = new RemoteObject(dest);
        ro.endpoint = "http://localhost:8080/messagebroker2/amf";
        var aopt:AbstractOperation = ro.getOperation(method);


        if (isload) {//是否显示过渡窗口，
        } else {
            ro.showBusyCursor = false;
        }

        if (callback != null) {//回调函数不为null，添加回调函数

            aopt.addEventListener(ResultEvent.RESULT, callback);

        }
        if (errorCallBack == null) {
            aopt.addEventListener(FaultEvent.FAULT, function (fault:FaultEvent):void {
                Alert.show(fault.message + '\n' + fault.fault)
            }, false, 0, true);
        } else {
            aopt.addEventListener(FaultEvent.FAULT, errorCallBack);
        }
        if (param == null) {
            aopt.send();
        } else {
            aopt.send(param);
        }
        if (ro) {
            ro = null;
            method = null;
            param = null;
            aopt = null;
        }
    }

    private static function aopt_resultHandler(event:ResultEvent):void {

    }


    private function aopt_resultHandler(event:ResultEvent):void {
    }
}
}
