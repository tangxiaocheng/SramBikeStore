package app.sram.bikestore

import app.sram.bikestore.data.MapApiResponse
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

private fun getFileFromPath(obj: Any, fileName: String?): File {
    val classLoader = obj.javaClass.classLoader!!
    val resource = classLoader.getResource(fileName)
    return File(resource.path)
}

@Throws(IOException::class)
fun readFileToString(path: String, obj: Any): String {
    return FileUtils.fileRead(getFileFromPath(obj, path).absolutePath)
//    return FileUtils.readFileToString(
//        getFileFromPath(obj, path),
//        StandardCharsets.UTF_8
//    )!!
}

fun parseJsonToList(mockBody: String, type: Type?): MapApiResponse {

    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create().fromJson(mockBody, type)
}
