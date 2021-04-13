package com.example.iuam_idache.apiREST.interfaces

import com.example.iuam_idache.apiREST.models.EventsAche

interface getMyEventsCallback {
    fun onSuccess(myEvents: List<EventsAche?>?)
    fun onFailure()
}