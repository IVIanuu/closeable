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
 * A closeable/cancelable component
 */
interface Closeable {

    /**
     * Whether or not this closable is closed
     */
    val isClosed: Boolean

    /**
     * Closes this instance
     */
    fun close()

}

/**
 * Runs the [block] and closes the closeable after that
 */
fun <T : Closeable, R> T.use(block: (T) -> R): R {
    return block.invoke(this).also { close() }
}