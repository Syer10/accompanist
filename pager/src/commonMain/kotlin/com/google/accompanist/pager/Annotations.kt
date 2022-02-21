/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.accompanist.pager

/**
 * Denotes that the annotated element should be an int or long in the given range
 * <p>
 * Example:
 * <pre><code>
 *  &#64;IntRange(from=0,to=255)
 *  public int getAlpha() {
 *      ...
 *  }
 * </code></pre>
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.ANNOTATION_CLASS
)
internal annotation class IntRange(
    /** Smallest value, inclusive  */
    val from: Long = Long.MIN_VALUE,
    /** Largest value, inclusive  */
    val to: Long = Long.MAX_VALUE
)


/**
 * Denotes that the annotated element should be a float or double in the given range
 * <p>
 * Example:
 * <pre><code>
 *  &#64;FloatRange(from=0.0,to=1.0)
 *  public float getAlpha() {
 *      ...
 *  }
 * </code></pre>
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.ANNOTATION_CLASS
)
internal annotation class FloatRange(
    /**
     * Smallest value. Whether it is inclusive or not is determined
     * by [.fromInclusive]
     */
    val from: Double = Double.NEGATIVE_INFINITY,
    /**
     * Largest value. Whether it is inclusive or not is determined
     * by [.toInclusive]
     */
    val to: Double = Double.POSITIVE_INFINITY,
    /** Whether the from value is included in the range  */
    val fromInclusive: Boolean = true,
    /** Whether the to value is included in the range  */
    val toInclusive: Boolean = true
)
