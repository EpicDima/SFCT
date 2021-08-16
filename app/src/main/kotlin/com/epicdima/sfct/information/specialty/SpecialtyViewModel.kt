package com.epicdima.sfct.information.specialty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.model.Specialty
import com.epicdima.sfct.core.network.DataResult
import com.epicdima.sfct.core.usecases.GetSpecialtyByIdUseCase
import com.epicdima.sfct.utils.DownloadableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author EpicDima
 */
@HiltViewModel
class SpecialtyViewModel @Inject constructor(
    private val getSpecialtyByIdUseCase: GetSpecialtyByIdUseCase
) : DownloadableViewModel() {

    private var lastDownloadableId: Int = 0

    private val _institution = MutableLiveData<Institution?>()
    val institution: LiveData<Institution?> = _institution

    private val _specialty = MutableLiveData<Specialty?>()
    val specialty: LiveData<Specialty?> = _specialty

    fun downloadSpecialty(id: Int, forced: Boolean = false) = viewModelScope.launch {
        if (lastDownloadableId != id || forced || rawStatus.isErrored()) {
            startLoading()
            lastDownloadableId = id
            val temp = when (val result = getSpecialtyByIdUseCase.execute(id)) {
                is DataResult.Success -> {
                    resetError()
                    result.data
                }
                is DataResult.Failure -> {
                    setError(result.throwable)
                    null
                }
            }
            _institution.value = temp
            _specialty.value = temp?.specialties?.firstOrNull()
            endLoading()
        }
    }
}