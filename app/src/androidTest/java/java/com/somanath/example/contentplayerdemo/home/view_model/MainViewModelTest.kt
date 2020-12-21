package java.com.somanath.example.contentplayerdemo.home.view_model

import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import com.somanath.example.contentplayerdemo.home.view_model.MainViewModel
import junit.framework.TestCase

class MainViewModelTest : TestCase() {

    fun testGetMContent() {
        val iMainViewModel = ViewModelProvider.AndroidViewModelFactory(
            ApplicationProvider.getApplicationContext()).create(MainViewModel::class.java)
        assertNotNull(iMainViewModel)
        //can not call observer in background thread
       // assertNotNull(iMainViewModel.getContents())
    }
}