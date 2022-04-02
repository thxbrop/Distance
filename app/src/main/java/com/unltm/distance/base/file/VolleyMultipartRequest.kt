package com.unltm.distance.base.file

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import java.io.*

class VolleyMultipartRequest(
    method: Int,
    url: String,
    private val listener: Response.Listener<NetworkResponse>,
    errorListener: Response.ErrorListener
) : Request<NetworkResponse>(method, url, errorListener) {
    private val twoHyphens = "--"
    private val lineEnd = "\r\n"
    private val mHeaders = mutableMapOf<String, String>()
    private val boundary = "android_client_${url.hashCode()}-" + System.currentTimeMillis()

    override fun getHeaders(): MutableMap<String, String> = mHeaders

    override fun getBodyContentType(): String = "multipart/form-data;boundary=$boundary"

    override fun getBody(): ByteArray? {
        val bos = ByteArrayOutputStream()
        val dos = DataOutputStream(bos)
        try {
            val params = params
            if (params != null && params.size > 0) {
                textParse(dos, params, paramsEncoding)
            }
            val data: Map<String, DataPart>? = getByteData()
            if (!data.isNullOrEmpty()) {
                dataParse(dos, data)
            }
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
            return bos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<NetworkResponse> {
        return try {
            Response.success(
                response,
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: NetworkResponse?) {
        listener.onResponse(response)
    }

    override fun deliverError(error: VolleyError?) {
        errorListener?.onErrorResponse(error)
    }

    private fun getByteData(): Map<String, DataPart>? {
        return null
    }

    private fun textParse(
        dataOutputStream: DataOutputStream,
        params: Map<String, String>,
        encoding: String
    ) {
        try {
            for ((key, value) in params) {
                buildTextPart(dataOutputStream, key, value)
            }
        } catch (uee: UnsupportedEncodingException) {
            throw RuntimeException("Encoding not supported: $encoding", uee)
        }
    }

    private fun dataParse(dataOutputStream: DataOutputStream, data: Map<String, DataPart>) {
        for ((key, value) in data) {
            buildDataPart(dataOutputStream, value, key)
        }
    }

    private fun buildTextPart(
        dataOutputStream: DataOutputStream,
        parameterName: String,
        parameterValue: String
    ) {
        dataOutputStream.writeBytes(twoHyphens.toString() + boundary + lineEnd)
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"$parameterName\"$lineEnd")
        dataOutputStream.writeBytes(lineEnd)
        dataOutputStream.writeBytes(parameterValue + lineEnd)
    }

    private fun buildDataPart(
        dataOutputStream: DataOutputStream,
        dataFile: DataPart,
        inputName: String
    ) {
        dataOutputStream.writeBytes(twoHyphens.toString() + boundary + lineEnd)
        dataOutputStream.writeBytes(
            "Content-Disposition: form-data; name=\"" +
                    inputName + "\"; filename=\"" + dataFile.fileName + "\"" + lineEnd
        )
        if (dataFile.type != null && dataFile.type.trim().isNotEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.type + lineEnd)
        }
        dataOutputStream.writeBytes(lineEnd)
        val fileInputStream = ByteArrayInputStream(dataFile.content)
        var bytesAvailable: Int = fileInputStream.available()
        val maxBufferSize = 1024 * 1024
        var bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
        val buffer = ByteArray(bufferSize)
        var bytesRead: Int = fileInputStream.read(buffer, 0, bufferSize)
        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize)
            bytesAvailable = fileInputStream.available()
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
        }
        dataOutputStream.writeBytes(lineEnd)
    }

    data class DataPart(
        var fileName: String? = null,
        var content: ByteArray,
        val type: String? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DataPart) return false

            if (fileName != other.fileName) return false
            if (!content.contentEquals(other.content)) return false
            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            var result = fileName?.hashCode() ?: 0
            result = 31 * result + content.contentHashCode()
            result = 31 * result + (type?.hashCode() ?: 0)
            return result
        }
    }
}