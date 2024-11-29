package com.sample.snoozeloo.model.typeConverter

import android.net.Uri
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class UriTypeAdapter : TypeAdapter<Uri>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Uri?) {
        out.value(value?.toString())
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Uri? {
        val uriString = `in`.nextString()
        return if (uriString.isEmpty()) null else Uri.parse(uriString)
    }
}