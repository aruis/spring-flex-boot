/**
 * Created by liurui on 15/6/19.
 */
package {
import flash.events.Event;
import flash.events.ProgressEvent;
import flash.net.FileReference;
import flash.utils.ByteArray;

import mx.rpc.events.ResultEvent;
import mx.utils.StringUtil;
import mx.utils.UIDUtil;

[Bindable]
public class File {

    private var _file:FileReference;

    public var label:String = '等待上传';
    public var state:int = -1;
    public var progress:Number = 0;
    public var uploadCompleteFunction:Function;
    public var uploadFailFunction:Function;

    public function File(fileReference:FileReference) {
        this._file = fileReference;
    }

    public function get size():Number {
        return _file.size;
    }

    public function get name():String {
        return _file.name;
    }

    public function get mDate():Date {
        return _file.modificationDate;
    }

    public function get cDate():Date {
        return _file.creationDate;
    }

    public function upload() {

//        _file.addEventListener(Event.SELECT, function (event:Event):void {
//
//        });

        _file.addEventListener(ProgressEvent.PROGRESS, function (event:ProgressEvent) {
            progress = parseFloat((event.bytesLoaded / event.bytesTotal * 50).toFixed(2));
        });

        _file.addEventListener(Event.COMPLETE, function (e:Event) {
            label = '正在上传文件';

            var stepSize:int = 1000 * 1000;

            var data:ByteArray = _file.data;
            var size:Number = _file.size;
            var uid:String = UIDUtil.createUID();
            var _file:Object = {
                name: _file.name,
                size: _file.size,
                cDate: _file.creationDate,
                mDate: _file.modificationDate
            };

            if (size > stepSize) {//大于1M 分包

                var ba:ByteArray;
                var list:Array = [];

                var num:int = data.length / stepSize;

                for (var i:int = 0; i <= num; i++) {
                    ba = new ByteArray();

                    if (i == num && data.length > i * stepSize) {
                        ba.writeBytes(data, stepSize * i, data.length - i * stepSize)
                    } else {
                        ba.writeBytes(data, stepSize * i, stepSize)
                    }

                    list.push(ba)
                }

                var step:int = 0;

                _file.length = list.length;
                _file.uid = uid;

                update();

                function update() {
                    step++;
                    progress = parseFloat((step / list.length * 50).toFixed(2)) + 50;

                    if (step > list.length) return;

                    _file.step = step;
                    _file.data = list[step - 1];
                    Call.java('bridge', 'upload', function (e:ResultEvent) {
                        if (e.result.state == 1) {
                            if (e.result.message == 'success') {
                                updateSuccess()
                            } else {
                                update()
                            }

                        } else {
                            uploadFailFunction();
                            trace('失败......')
                        }
                    }, _file);
                }

            } else {
                _file.data = data;
                Call.java('bridge', 'upload', function (e:ResultEvent) {
                    if (e.result.state == 1) {
                        if (e.result.message == 'success') {
                            updateSuccess()
                        }

                    } else {
                        uploadFailFunction();
                        trace('失败......')
                    }
                }, _file);
            }
        });

        label = '正在校验文件';
        state = 0;
        _file.load();
    }

    private function updateSuccess():void {
        state = 1;
        progress = 100;
        label = '文件上传完毕';

        if (uploadCompleteFunction) {
            uploadCompleteFunction();
        }
    }

    public function isStringNull(str:String):Boolean {
        if (str == null) {
            return true;
        } else if (StringUtil.trim(str).length == 0) {
            return true;
        } else if (StringUtil.trim(str) == "null" || StringUtil.trim(str) == "NULL") {
            return true;
        } else
            return false;
    }

    public function isStringNotNull(str:String):Boolean {
        return !isStringNull(str);
    }
}
}
