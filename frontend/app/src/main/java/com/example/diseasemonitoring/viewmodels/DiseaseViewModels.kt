package com.example.diseasemonitoring.viewmodels

import android.util.Log
import androidx.lifecycle.*

import com.example.diseasemonitoring.api.RetrofitInstance
import com.example.diseasemonitoring.models.Disease
import com.example.diseasemonitoring.models.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel for Diseases
class DiseaseViewModels : ViewModel() {

    private val _disease = MutableLiveData<Disease>()
    val disease: LiveData<Disease> get() = _disease
    private val _diseaseList = MutableLiveData<List<Disease>>(emptyList())
    val diseaseList: LiveData<List<Disease>> get() = _diseaseList
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val staticUserId = "64fef678e1b2ec4f112d4f89"

    init {
        fetchAllDiseases(staticUserId)
    }

    fun addDisease(disease: Disease) {
        _isLoading.value = true
        val diseaseWithUserId = disease.copy(userId = staticUserId)
        RetrofitInstance.api.addDisease(diseaseWithUserId).enqueue(object : Callback<Disease> {
            override fun onResponse(call: Call<Disease>, response: Response<Disease>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _disease.value = it
                        val currentList = _diseaseList.value?.toMutableList() ?: mutableListOf()
                        currentList.add(it)
                        _diseaseList.value = currentList
                        Log.d("DiseaseViewModels", "Disease added successfully: $it")
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Disease>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error adding disease: ${t.message}"
                Log.e("DiseaseViewModels", "Error adding disease: ${t.message}")
            }
        })
    }

    private fun handleError(response: Response<*>) {
        val errorBody = response.errorBody()?.string()
        val statusCode = response.code()
        _errorMessage.value = "Error: $errorBody"
        Log.e("DiseaseViewModels", "API error (Code: $statusCode): $errorBody")
    }

    fun fetchDiseaseByName(userId: String, name: String) {
        _isLoading.value = true
        RetrofitInstance.api.getDiseaseUserIdAndByName(userId, name).enqueue(object : Callback<Disease> {
            override fun onResponse(call: Call<Disease>, response: Response<Disease>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _disease.value = response.body()
                    Log.d("DiseaseViewModels", "Fetched disease: ${response.body()}")
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Disease>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error fetching disease by name: ${t.message}"
                Log.e("DiseaseViewModels", "Error fetching disease: ${t.message}")
            }
        })
    }

    fun fetchAllDiseases(userId: String) {
        _isLoading.value = true
        RetrofitInstance.api.getAllDiseases(userId).enqueue(object : Callback<List<Disease>> {
            override fun onResponse(call: Call<List<Disease>>, response: Response<List<Disease>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _diseaseList.value = response.body() ?: emptyList()
                    Log.d("DiseaseViewModels", "Fetched all diseases: ${response.body()}")
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<List<Disease>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error fetching all diseases: ${t.message}"
                Log.e("DiseaseViewModels", "Error fetching all diseases: ${t.message}")
            }
        })
    }

    fun updateDisease(diseaseName: String, updatedDisease: Disease) {
        _isLoading.value = true
        val diseaseWithUserId = updatedDisease.copy(userId = staticUserId)
        RetrofitInstance.api.updateDisease(staticUserId, diseaseName, diseaseWithUserId).enqueue(object : Callback<Disease> {
            override fun onResponse(call: Call<Disease>, response: Response<Disease>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { updated ->
                        _disease.value = updated
                        _diseaseList.value = _diseaseList.value?.map {
                            if (it.name == diseaseName) updated else it
                        }
                        Log.d("DiseaseViewModels", "Disease updated successfully: $updated")
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Disease>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error updating disease: ${t.message}"
                Log.e("DiseaseViewModels", "Error updating disease: ${t.message}")
            }
        })
    }

    fun deleteDisease(diseaseName: String) {
        _isLoading.value = true
        RetrofitInstance.api.deleteDisease(staticUserId, diseaseName).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _diseaseList.value = _diseaseList.value?.filter { it.name != diseaseName }
                    Log.d("DiseaseViewModels", "Disease deleted successfully")
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error deleting disease: ${t.message}"
                Log.e("DiseaseViewModels", "Error deleting disease: ${t.message}")
            }
        })
    }
}

// ViewModel for User Authentication
class UserViewModel : ViewModel() {

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

    fun loginUser(email: String, password: String) {
        _userState.value = _userState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.loginUser(LoginRequest(email, password))
                if (response.success) {
                    _userState.value = _userState.value.copy(isLoggedIn = true, isLoading = false)
                } else {
                    _userState.value = _userState.value.copy(
                        isLoading = false,
                        errorMessage = response.message
                    )
                }
            } catch (e: Exception) {
                _userState.value = _userState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur de connexion : ${e.localizedMessage}"
                )
            }
        }
    }

    fun resetError() {
        _userState.value = _userState.value.copy(errorMessage = null)
    }
}

// User State Data Class
data class UserState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
)
