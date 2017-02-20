/*
 *
 *  Copyright (c) 2017 abuabdul.com, B2OPlus
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.b2oplus.omargs.exceptions

import org.b2oplus.omargs.Option
import org.b2oplus.omargs.OptionGroup

/**
 * Created by abuabdul
 */
open class ParseException(override val message: String) : Throwable(message)

open class AlreadySelectedException(override val message: String) : ParseException(message) {

    private var group: OptionGroup? = null
    private var option: Option? = null

    constructor(group: OptionGroup, option: Option) : this("The option '$option.opt' was specified but an option from this group has already been selected: '$group.selected'") {
        this.group = group
        this.option = option
    }
}

open class MissingOptionException(override val message: String) : ParseException(message) {

    private var missingOptions: List<String>? = null

    constructor(missingOptions: List<String>) : this(MissingOptionException.createMessage(missingOptions)) {
        this.missingOptions = missingOptions
    }

    companion object {
        private fun createMessage(missingOptions: List<*>): String {
            val buf = StringBuilder("Missing required option")
            buf.append(if (missingOptions.size == 1) "" else "s")
            buf.append(": ")

            val it = missingOptions.iterator()
            while (it.hasNext()) {
                buf.append(it.next())
                if (it.hasNext()) {
                    buf.append(", ")
                }
            }
            return buf.toString()
        }
    }
}

open class MissingArgumentException(override val message: String) : ParseException(message) {

    private var option: Option? = null

    constructor(option: Option) : this("Missing argument for option: $option.opt") {
        this.option = option
    }
}

open class UnrecognizedOptionException(override val message: String) : ParseException(message) {

    protected open var option: String? = null

    constructor(message: String, option: String?) : this(message) {
        this.option = option
    }
}

open class AmbiguousOptionException : UnrecognizedOptionException {

    protected open var matchingOptions: Collection<String>? = null

    constructor(message: String) : super(message)

    constructor(option: String, matchingOptions: Collection<String>) : super(createMessage(option, matchingOptions), option) {
        this.matchingOptions = matchingOptions
    }

    companion object {
        private fun createMessage(option: String, matchingOptions: Collection<String>): String {
            val buf = StringBuilder("Ambiguous option: '")
            buf.append(option)
            buf.append("'  (could be: ")

            val it = matchingOptions.iterator()
            while (it.hasNext()) {
                buf.append("'")
                buf.append(it.next())
                buf.append("'")
                if (it.hasNext()) {
                    buf.append(", ")
                }
            }
            buf.append(")")
            return buf.toString()
        }
    }
}