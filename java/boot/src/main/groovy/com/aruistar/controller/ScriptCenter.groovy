package com.aruistar.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Created by liurui on 15/7/1.
 */
@Controller
class ScriptCenter {

    @RequestMapping("/script")
    @ResponseBody
    public String getScript(
            @RequestParam(value = "script", required = true) String scriptID,
            @RequestParam(value = "ui", required = true) String ui) {

        def script = """
            var a = getValue('名称');
            setValue('名称',a+'.');"""

        def first = """
        function main${scriptID}(){"""

        def last = """
            return true;
        }"""

        return first + script.replaceAll(~/etValue\(/, "etValue('${ui}',") + last
    }
}
