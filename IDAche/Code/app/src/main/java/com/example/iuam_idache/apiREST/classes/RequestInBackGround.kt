package com.example.iuam_idache.apiREST.classes

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.*

@Suppress("DEPRECATION")
abstract class RequestInBackGround : AsyncTask<String?, Void?, Any?> {
    var TAG = "TAG"
    private var jsonArray: JSONArray? = null
    private var jsonObject: JSONObject? = null
    private var requestBody: String? = null

    constructor(urlString: String?) {
        this.execute(urlString)
    }

    constructor(urlString: String?, reqBody: String?) {
        requestBody = reqBody
        this.execute(urlString)
    }

    abstract fun onResponseReceived(result: Any?)

    // TODO optimise les try & catch
    protected override fun doInBackground(vararg strings: String?): Any? {
        Log.d(TAG, "------- >  doInBackground :")
        if (strings != null && strings[0] !== "") Log.d(TAG, strings[0]!!) else {
            Log.d(TAG, "Url not correct ! ")
            return null
        }
        var url: URL? = null
        val test: String
        try {
            url = URL(strings[0])
            val conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 20000 // milli
            conn.connectTimeout = 15000 // milli

            //  Set the Request Content-Type Header Parameter
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            // Set Response Format Type
            conn.setRequestProperty("Accept", "application/json")

            // if there is a request body
            if (requestBody != null && requestBody !== "") {
                // Ensure the Connection Will Be Used to Send Content
                conn.doOutput = true
                // Set the Request Method
                conn.requestMethod = "POST"
                val wr = OutputStreamWriter(conn.outputStream)
                wr.write(requestBody)
                wr.flush()
            } else {
                // Set the Request Method
                conn.requestMethod = "GET"
            }

            conn.connect()

            // Read the code response
            println(conn.responseCode)
            if (conn.responseCode != 200) {
                Log.v(TAG, "Error request.. code return =  " + conn.responseCode)
                return null
            }

            // Read body responses
            val `is` = conn.inputStream
            val reader: Reader = InputStreamReader(`is`, "UTF-8")
            // TODO warning... buffer size limited !!
            val buffer = CharArray(500)
            reader.read(buffer)
            test = String(buffer)
            try {
                jsonArray = JSONArray(test)
                return jsonArray
            } catch (e: JSONException) {
                try {
                    jsonObject = JSONObject(test)
                    return jsonObject
                } catch (e2: JSONException) {
                    //e2.printStackTrace();
                    //e.printStackTrace();
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: ProtocolException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Read body responses TODO read ici ?
        var br: BufferedReader? = null
        try {
            br = BufferedReader(InputStreamReader(url!!.openStream()))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val sb = StringBuilder()
        var line: String? = null
        while (true) {
            try {
                if (br!!.readLine().also { line = it } == null) break
            } catch (e: IOException) {
                e.printStackTrace()
                return null;
            }
            sb.append(
                """
                    $line
                    
                    """.trimIndent()
            )
        }
        try {
            br!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val jsonString = sb.toString()
        println("JSON: $jsonString")
        try {
            jsonArray = JSONArray(jsonString)
            return jsonArray
        } catch (e: JSONException) {
            //e.printStackTrace();
        }
        return jsonString
    }

    // Appeller lors de la reception de la réponse
    override fun onPostExecute(result: Any?) {
        Log.d(TAG, "------- >  Request Finished")
        super.onPostExecute(result)
        onResponseReceived(result)
    }
}