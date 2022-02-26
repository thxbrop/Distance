package com.unltm.distance.base.contracts

import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.unltm.distance.application

val volley by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Volley.newRequestQueue(application) }

val gson by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Gson() }