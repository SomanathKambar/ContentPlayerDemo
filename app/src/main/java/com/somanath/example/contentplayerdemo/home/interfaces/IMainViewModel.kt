package java.com.somanath.example.contentplayerdemo.home.interfaces

import androidx.lifecycle.MutableLiveData
import com.somanath.example.contentplayerdemo.home.model.Content

interface IMainViewModel {
    fun getContents() : MutableLiveData<Content>
    fun updateContent()
}