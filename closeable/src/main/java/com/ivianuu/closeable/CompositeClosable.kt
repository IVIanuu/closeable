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

package com.ivianuu.closeable

/**
 * Wraps a couple [Closeable]
 */
class CompositeClosable : Closeable {

    @Volatile override var isClosed = false
        private set

    /**
     * The size of all [Closeable]s
     */
    val size: Int get() = closeables.size

    internal val closeables = mutableListOf<Closeable>()

    /**
     * Adds all [closeables]
     */
    fun add(closeables: Iterable<Closeable>) {
        if (!isClosed) {
            synchronized(this) {
                this.closeables.addAll(closeables)
            }
        } else {
            closeables.forEach { it.close() }
        }
    }

    /**
     * Adds all [closeables]
     */
    fun add(vararg closeables: Closeable) {
        add(closeables.asIterable())
    }

    /**
     * Removes and closes the [closeable]
     */
    fun remove(closeable: Closeable) {
        delete(closeable)
        closeable.close()
    }

    /**
     * Removes but not closes the [closeable]
     */
    fun delete(closeable: Closeable): Unit = synchronized(this) {
        closeables.remove(closeable)
    }

    /**
     * Closes all added [Closeable]s but does not finally close this
     */
    fun clear() = synchronized(this) {
        closeables.forEach { it.close() }
        closeables.clear()
    }

    override fun close() {
        if (!isClosed) {
            isClosed = true
            clear()
        }
    }

}