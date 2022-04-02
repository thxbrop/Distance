package com.unltm.test.config

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class HeaderStringRequest(
    url: String?,
    private val linkedHashMap: LinkedHashMap<String, String>,
    listener: Response.Listener<String>?,
    errorListener: Response.ErrorListener?
) : StringRequest(url, listener, errorListener) {
    override fun getHeaders(): MutableMap<String, String> {
        return linkedHashMap
    }
}