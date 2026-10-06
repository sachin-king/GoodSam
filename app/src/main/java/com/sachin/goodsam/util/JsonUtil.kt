package com.sachin.goodsam.util



import com.google.gson.JsonElement

object JsonUtil {

    fun findString(element: JsonElement?, key: String): String? {
        if (element == null || element.isJsonNull) return null
        if (element.isJsonObject) {
            val obj = element.asJsonObject
            for ((k, v) in obj.entrySet()) {
                if (k.equals(key, ignoreCase = true) && v.isJsonPrimitive) return v.asString
            }
            for ((_, v) in obj.entrySet()) {
                findString(v, key)?.let { return it }
            }
        } else if (element.isJsonArray) {
            for (item in element.asJsonArray) {
                findString(item, key)?.let { return it }
            }
        }
        return null
    }
}