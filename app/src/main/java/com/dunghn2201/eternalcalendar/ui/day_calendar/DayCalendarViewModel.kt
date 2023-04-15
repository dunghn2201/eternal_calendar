package com.dunghn2201.eternalcalendar.ui.day_calendar

import android.app.Application
import com.dunghn2201.eternalcalendar.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayCalendarViewModel @Inject constructor(application: Application) : BaseViewModel(application)
