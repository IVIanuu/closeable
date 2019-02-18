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

package com.ivianuu.closeable.rx

import com.ivianuu.closeable.Closeable
import com.ivianuu.closeable.CompositeClosable
import com.ivianuu.closeable.add
import io.reactivex.disposables.Disposable

/**
 * Returns a [Closeable] which disposes this disposable on close
 */
fun Disposable.asCloseable(): Closeable = DisposableCloseable(this)

/**
 * Adds the [disposable]
 */
fun CompositeClosable.add(disposable: Disposable) {
    add(disposable.asCloseable())
}

/**
 * Adds all [disposables]
 */
fun CompositeClosable.add(vararg disposables: Disposable) {
    add(disposables.map { it.asCloseable() })
}

/**
 * Adds all [disposables]
 */
fun CompositeClosable.add(disposables: Iterable<Disposable>) {
    add(disposables.map { it.asCloseable() })
}

/**
 * Adds this disposable to [closeables]
 */
fun Disposable.addTo(closeables: CompositeClosable): Disposable = apply {
    closeables.add(this)
}

/**
 * A [Closeable] which disposes the [disposable] on close
 */
class DisposableCloseable(private val disposable: Disposable) : Closeable {

    override val isClosed: Boolean
        get() = disposable.isDisposed
    
    override fun close() {
        disposable.dispose()
    }

}