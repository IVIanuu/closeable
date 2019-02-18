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

import com.ivianuu.closeable.Closeable
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

typealias CloseListener = () -> Unit

/**
 * Wraps the [closeable] and adds listener capabilities
 */
class ListenableCloseable(val closeable: Closeable) : Closeable by closeable {

    private val listeners = mutableListOf<CloseListener>()
    private val listenersLock = ReentrantLock()

    fun onClose(listener: CloseListener): Closeable = listenersLock.withLock {
        val closeable = Closeable {
            listenersLock.withLock { listeners.remove(listener) }
        }

        listeners.add(listener)

        closeable
    }

    override fun close() {
        if (!isClosed) {
            closeable.close()
            listeners.toList().forEach { it() }
            listenersLock.withLock { listeners.clear() }
        }
    }

}

/**
 * Returns a [ListenableCloseable] which wraps this closeable
 */
fun Closeable.asListenableCloseable(): ListenableCloseable = ListenableCloseable(this)