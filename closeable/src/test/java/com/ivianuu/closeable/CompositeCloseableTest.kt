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

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class CompositeCloseableTest {

    private val compositeClosable = CompositeClosable()
    private val testCloseable = Closeable {}

    @Test
    fun testSize() {
        assertEquals(0, compositeClosable.size)
        compositeClosable.add(testCloseable)
        assertEquals(1, compositeClosable.size)
    }

    @Test
    fun testAdd() {
        compositeClosable.add(testCloseable)
        assertTrue(compositeClosable.closeables.contains(testCloseable))
    }

    @Test
    fun testRemove() {
        compositeClosable.add(testCloseable)
        assertFalse(testCloseable.isClosed)
        compositeClosable.remove(testCloseable)
        assertTrue(testCloseable.isClosed)
    }

    @Test
    fun testDelete() {
        compositeClosable.add(testCloseable)
        assertFalse(testCloseable.isClosed)
        compositeClosable.delete(testCloseable)
        assertFalse(testCloseable.isClosed)
    }

    @Test
    fun testClear() {
        compositeClosable.add(testCloseable)
        compositeClosable.clear()
        assertTrue(testCloseable.isClosed)
        assertFalse(compositeClosable.isClosed)
    }

    @Test
    fun testClose() {
        compositeClosable.add(testCloseable)
        compositeClosable.close()
        assertTrue(testCloseable.isClosed)
        assertTrue(compositeClosable.isClosed)
    }

    @Test
    fun testClosesClosableIfAlreadyClosed() {
        compositeClosable.close()
        compositeClosable.add(testCloseable)
        assertTrue(testCloseable.isClosed)
    }
}