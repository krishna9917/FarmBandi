package com.application.farmbandi.SharedMemory

import android.content.Context
import com.application.farmbandi.Utils.MyApp

import com.google.gson.Gson

object MyLocalMemory {

    private val mySharedMemory by lazy {
        MyApp.MySingleton.getAppContext()
            .getSharedPreferences("MyLocalMemory", Context.MODE_PRIVATE)
    }
    private val mySharedEditor by lazy {
        mySharedMemory.edit()
    }

    public fun putString(key: String, value: String) {
        mySharedEditor.putString(key, value)
        mySharedEditor.commit()
    }

    public fun putInteger(key: String, value: Int) {
        mySharedEditor.putInt(key, value)
        mySharedEditor.commit()
    }

    public fun putBoolean(key: String, value: Boolean) {
        mySharedEditor.putBoolean(key, value)
        mySharedEditor.commit()
    }

    public fun getString(key: String): String {
        return mySharedMemory.getString(key, "")!!
    }

    public fun getBoolean(key: String): Boolean {
        return mySharedMemory.getBoolean(key, false)!!
    }


    fun <T> putObject(key: String?, `object`: T) {
        val gson = Gson()
        val json = gson.toJson(`object`)
        mySharedEditor.putString(key, json)
        mySharedEditor.commit()
    }


    inline fun < reified T : Any> getObject(key: String, type: T): T
    {
        val objectString = getString(key)
        val gson = Gson()
        return gson.fromJson(objectString, T::class.java)
    }

    public fun clearMemory() {
        mySharedEditor.clear()
        mySharedEditor.commit()
    }


}