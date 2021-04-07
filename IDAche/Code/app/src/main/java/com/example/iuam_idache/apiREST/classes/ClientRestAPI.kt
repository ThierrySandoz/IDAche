package com.example.iuam_idache.apiREST.classes

import android.util.Log
import com.example.iuam_idache.apiREST.models.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.example.iuam_idache.apiREST.interfaces.getLongCallback
import com.example.iuam_idache.apiREST.interfaces.getMyEventsCallback
import com.example.iuam_idache.apiREST.interfaces.getVoidCallback
import com.example.iuam_idache.apiREST.models.EventsAche
import java.util.*

class ClientRestAPI {
    // TODO Get timestamp with 2 hours plus (Ex: 15h -> 17h)
    fun getMyEvents(myUserID: Int, getMyEventsCallback: getMyEventsCallback) {
        val urlString = SERVEUR_LINK + EVENTS_USER + myUserID

        // request for get all events from DB
        object : RequestInBackGround(urlString) {
            override fun onResponseReceived(result: Any?) {
                val myEvents: MutableList<EventsAche> = ArrayList<EventsAche>()

                // response expected
                if (result != null && result is JSONArray) {

                    // for all events received
                    val jsonArray = result
                    for (i in 0 until jsonArray.length()) {
                        var jsonObject: JSONObject? = null
                        try {
                            jsonObject = jsonArray.getJSONObject(i)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        val event: EventsAche = eventObjectFromJsonString(jsonObject.toString())
                        myEvents.add(event)
                    }
                    getMyEventsCallback.onSuccess(myEvents)
                } else {
                    //Throwable fail = new SQLException();
                    getMyEventsCallback.onFailure()
                }
            }
        }
    }

    private fun eventObjectFromJsonString(jsonObjectString: String): EventsAche {
        val gson: Gson = GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss z").create()
        return gson.fromJson(jsonObjectString, EventsAche::class.java)
    }

    private fun userObjectFromJsonString(jsonObjectString: String): User {
        val gson: Gson = GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss z").create()
        return gson.fromJson(jsonObjectString, User::class.java)
    }

    fun deleteUser(user_id: Long, getVoidCallback: getVoidCallback) {
        val urlString = SERVEUR_LINK + DELETE_USER + user_id
        object : RequestInBackGround(urlString) {
            override fun onResponseReceived(result: Any?) {
                // response expected
                if (result != null && result is String) {
                    if (result.contentEquals("\"User deleted successfully!\"\n")) {
                        getVoidCallback.onSuccess()
                        return
                    }

                    //Throwable fail = new SQLException();
                    getVoidCallback.onFailure()
                }
            }
        }
    }

    fun updateUser(user: User, getLongCallback: getLongCallback) {
        val requestBody = userObjectToJsonUser(user)
        val urlString = SERVEUR_LINK + UPDATE_USER
        // request for update a user in DB
        object : RequestInBackGround(urlString, requestBody) {
            override fun onResponseReceived(result: Any?) {
                var myNewID: Long = -1

                // response expected
                if (result != null && result is JSONObject) {
                    try {
                        myNewID = result.getLong("id")
                        if (myNewID == user.user_id && myNewID != -1L) {
                            getLongCallback.onSuccess(myNewID)
                            return
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                //Throwable fail = new SQLException();
                getLongCallback.onFailure()
            }
        }
    }

    fun addNewEvents(myEvent: EventsAche, getLongCallback: getLongCallback) {
        val requestBody = eventObjectToJsonUser(myEvent)
        Log.v("DEBUGG", "requestBody : $requestBody")
        val urlString = SERVEUR_LINK + ADD_EVENT

        // request for add a user in DB
        object : RequestInBackGround(urlString, requestBody) {
            override fun onResponseReceived(result: Any?) {
                var idEvent: Long = -1
                if (result != null && result is JSONObject) {
                    try {
                        idEvent = result.getLong("id")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    getLongCallback.onSuccess(idEvent)
                } else {
                    //Throwable fail = new SQLException();
                    getLongCallback.onFailure()
                }
            }
        }
    }

    fun addUser(user: User, getLongCallback: getLongCallback) {
        val requestBody = userObjectToJsonUser(user)
        val urlString = SERVEUR_LINK + ADD_USER

        // request for add a user in DB
        object : RequestInBackGround(urlString, requestBody) {
            override fun onResponseReceived(result: Any?) {
                var myNewID: Long = -1

                // response expected // TODO test correctly type response
                if (result != null && result is JSONObject) {
                    try {
                        myNewID = result.getLong("id")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    getLongCallback.onSuccess(myNewID)
                } else {
                    //Throwable fail = new SQLException();
                    getLongCallback.onFailure()
                }
            }

        }
    }

    private fun userObjectToJsonUser(user: User): String {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()
        return gson.toJson(user, User::class.java)
    }

    private fun eventObjectToJsonUser(myEvent: EventsAche): String {
        val gson: Gson = GsonBuilder().create()
        return gson.toJson(myEvent, EventsAche::class.java)
    }

    companion object {
        private const val SERVEUR_LINK = "http://ec2-100-26-223-221.compute-1.amazonaws.com:5000/"
        private const val GET = "GET"
        private const val EVENTS = "events"
        private const val USERS = "users"
        private const val ADD_USER = "adduser"
        private const val ADD_EVENT = "addevent"
        private const val UPDATE_USER = "updateuser"

        // need ID : Ex : .../deleteuser/4
        private const val DELETE_USER = "deleteuser/"
        private const val EVENTS_USER = "event_from_user/"
    }
}