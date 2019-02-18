/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.closeable.sample

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.closeable.Closeable
import com.ivianuu.closeable.CompositeClosable
import com.ivianuu.closeable.R
import com.ivianuu.closeable.coroutines.addTo
import com.ivianuu.closeable.rx.addTo
import io.reactivex.Observable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val closeables = CompositeClosable()

    private val prefsListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.registerOnSharedPreferenceChangeListener(prefsListener)
        closeables.add(Closeable {
            prefs.unregisterOnSharedPreferenceChangeListener(prefsListener)
        })

        GlobalScope.launch {
            delay(10000)
            // heavy work...
        }.addTo(closeables)

        Observable.interval(1, TimeUnit.SECONDS)
            .subscribe { Log.d("Rx", "log.. $it") }
            .addTo(closeables)
    }

    override fun onDestroy() {
        closeables.close() // close all
        super.onDestroy()
    }
}

