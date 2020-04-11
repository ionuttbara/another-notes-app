/*
 * Copyright 2020 Nicolas Maltais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maltaisn.notes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maltaisn.notes.model.NotesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class SharedViewModel @Inject constructor(
        private val notesRepository: NotesRepository) : ViewModel() {

    private var lastStatusChange: StatusChange? = null

    private val _statusChangeEvent = MutableLiveData<Event<StatusChange>>()
    val statusChangeEvent: LiveData<Event<StatusChange>>
        get() = _statusChangeEvent


    fun onStatusChange(statusChange: StatusChange) {
        lastStatusChange = statusChange
        _statusChangeEvent.send(statusChange)
    }

    fun undoStatusChange() {
        val change = lastStatusChange ?: return
        viewModelScope.launch {
            notesRepository.updateNotes(change.oldNotes)
        }
        lastStatusChange = null
    }

}
