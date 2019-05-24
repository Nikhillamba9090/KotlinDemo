package app.demoktx.constants

import android.os.Environment

object Constants {

    val BASE_URL = ""

    val BASE_DIR_PATH = Environment.getExternalStorageDirectory().toString() + "/.demox"
    val IMAGE_DIR_PATH = "$BASE_DIR_PATH/Image"
    val IMAGE_DIRECTORY_NAME = ".demox"
    var SAMPLE_IMAGE = "https://pbs.twimg.com/profile_images/467056300091002880/6FdDCgOf_400x400.jpeg"
}
