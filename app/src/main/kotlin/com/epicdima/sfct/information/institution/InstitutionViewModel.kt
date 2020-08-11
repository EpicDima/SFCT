package com.epicdima.sfct.information.institution

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.network.DataResult
import com.epicdima.sfct.core.usecases.GetDormitoryByIdUseCase
import com.epicdima.sfct.core.usecases.GetInstitutionByIdUseCase
import com.epicdima.sfct.utils.DownloadableViewModel
import kotlinx.coroutines.launch

/**
 * @author EpicDima
 */
class InstitutionViewModel @ViewModelInject constructor(
    private val getInstitutionByIdUseCase: GetInstitutionByIdUseCase,
    private val getDormitoryByIdUseCase: GetDormitoryByIdUseCase
) : DownloadableViewModel() {

    private var lastDownloadableId: Int = 0

    private val _institution = MutableLiveData<Institution?>()
    val institution: LiveData<Institution?> = _institution

    private val _dormitory = MutableLiveData<String?>()
    val dormitory: LiveData<String?> = _dormitory

    private val _loadingDormitory = MutableLiveData<Boolean>(false)
    val loadingDormitory: LiveData<Boolean> = _loadingDormitory

    fun downloadInstitution(id: Int, forced: Boolean = false) = viewModelScope.launch {
        if (lastDownloadableId != id || forced || rawStatus.isErrored()) {
            startLoading()
            lastDownloadableId = id
            _institution.value = when (val result = getInstitutionByIdUseCase.execute(id)) {
                is DataResult.Success -> {
                    resetError()
                    downloadDormitoryInfo(id)
                    result.data
                }
                is DataResult.Failure -> {
                    setError(result.throwable)
                    null
                }
            }
            endLoading()
        }
    }

    private fun downloadDormitoryInfo(id: Int) = viewModelScope.launch {
        _loadingDormitory.value = true
        _dormitory.value = when (val result = getDormitoryByIdUseCase.execute(id)) {
            is DataResult.Success -> result.data
            is DataResult.Failure -> null
        }
        _loadingDormitory.value = false
    }
}