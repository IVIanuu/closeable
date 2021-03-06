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

package com.ivianuu.closeable.coroutines

import com.ivianuu.closeable.Closeable
import com.ivianuu.closeable.CompositeClosable
import com.ivianuu.closeable.add
import kotlinx.coroutines.Job

/**
 * Returns a [Closeable] which cancels this job on close
 */
fun Job.asCloseable(): Closeable = JobCloseable(this)

/**
 * Adds the [job]
 */
fun CompositeClosable.add(job: Job) {
    add(job.asCloseable())
}

/**
 * Adds all [jobs]
 */
fun CompositeClosable.add(vararg jobs: Job) {
    add(jobs.map { it.asCloseable() })
}

/**
 * Adds all [jobs]
 */
fun CompositeClosable.add(jobs: Iterable<Job>) {
    add(jobs.map { it.asCloseable() })
}

/**
 * Adds this disposable to [closeables]
 */
fun Job.addTo(closeables: CompositeClosable): Job = apply {
    closeables.add(this)
}

/**
 * A [Closeable] which cancels the [job] on close
 */
internal class JobCloseable(private val job: Job) : Closeable {

    override val isClosed: Boolean
        get() = job.isCancelled

    override fun close() {
        job.cancel()
    }

}