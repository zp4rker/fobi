package com.zp4rker.fobi.updater

import com.zp4rker.fobi.VERSION
import com.zp4rker.fobi.config
import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

private fun fetchLatest(): JSONObject {
    val url = URL("https://api.github.com/repos/zp4rker/fobi/releases/latest")
    val token = config.getString("github-access-token")

    val con = (url.openConnection() as HttpURLConnection).apply { requestMethod = "GET" }
    con.setRequestProperty("Authorization", "token $token")

    return JSONObject(con.inputStream.bufferedReader().use(BufferedReader::readText))
}

fun checkUpdates(): Boolean {
    val remote = fetchLatest().getString("name").drop(1).split("-")
    val local = VERSION.drop(1).split("-")

    // check release type
    when (local[1]) {
        "alpha" -> if (remote[1] == "beta" || remote[1] == "release") return true
        "beta" -> if (remote[1] == "release") return true
    }

    // compare versions
    val remoteVersion = DefaultArtifactVersion(remote[0])
    val localVersion = DefaultArtifactVersion(local[0])
    if (localVersion < remoteVersion) return true

    // return false by default
    return false
}

fun download() {
    val release = fetchLatest()
    val downloadUrl = release.getJSONArray("assets").getJSONObject(0).getString("browser_download_url")
    with(File("fobi.jar.update")) {
        if (!exists()) createNewFile()
        writeBytes(URL(downloadUrl).readBytes())
    }
}

fun restart() {
    Runtime.getRuntime().exec(config.getString("restart-script"))
    exitProcess(0)
}