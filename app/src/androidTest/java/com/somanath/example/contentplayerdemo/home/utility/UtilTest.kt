package com.somanath.example.contentplayerdemo.home.utility

import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase

class UtilTest : TestCase() {
    val ASSET_BASE_PATH = "app/src/main/assets/"
    fun testGetContentFromAssets() {
        val json = Util.getJsonFromAssets(
            InstrumentationRegistry.getInstrumentation().context,
            ASSET_BASE_PATH + Util.CONTENT_FILE_PATH
        )
     //   assertNotNull(json)
    }
}