package com.example.top10downloaderapp

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class AppData(val title: String, val image: String, val summary: String, val releaseDate: String, val link: String)


class XMLParser{

    private val ns: String? = null

    fun parse(inputStream: InputStream): List<AppData> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readDataRssFeed(parser)
        }
    }

    private fun readDataRssFeed(parser: XmlPullParser): List<AppData> {

        val apps = mutableListOf<AppData>()


        parser.require(XmlPullParser.START_TAG, ns, "feed")
        var websiteTitle: String? = null
        var update: String

        while (parser.next() != XmlPullParser.END_TAG){
            if(parser.eventType != XmlPullParser.START_TAG){
                continue
            }

            when(parser.name){
                "title" -> websiteTitle = readTitle(parser)
                "updated" -> {
                    update = readUpdated(parser)
                    apps.add(
                        AppData(
                            websiteTitle!!,
                            "",
                            "",
                            update,
                            ""
                        )
                    )
                }
                "entry" -> {
                    parser.require(XmlPullParser.START_TAG, ns, "entry")

                    var title: String? = null
                    var image: String? = null
                    var summary: String? = null
                    var releaseDate: String? = null
                    var link: String? = null


                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.eventType != XmlPullParser.START_TAG) {
                            continue
                        }
                        when (parser.name) {
                            "id" -> link = readLink(parser)
                            "title" -> title = readTitle(parser)
                            "summary" -> summary = readSummary(parser)
                            "im:image" -> image = readImage(parser)
                            "im:releaseDate" -> releaseDate = readDate(parser)
                            else -> skip(parser)
                        }
                    }
                    apps.add(
                        AppData(
                            title.toString(),
                            image.toString(),
                            summary.toString(),
                            releaseDate.toString(),
                            link.toString()
                        )
                    )
                }
                else -> skip(parser)
            }

//            if (parser.name == "entry"){
//                parser.require(XmlPullParser.START_TAG, ns, "entry")
//
//                var title: String? = null
//                var image: String? = null
//                var summary: String? = null
//                var releaseDate: String? = null
//                var link: String? = null
//
//
//                while(parser.next() != XmlPullParser.END_TAG){
//                    if(parser.eventType != XmlPullParser.START_TAG){
//                        continue
//                    }
//                    when(parser.name){
//                        "id" -> link = readLink(parser)
//                        "title" -> title = readTitle(parser)
//                        "summary" -> summary = readSummary(parser)
//                        "im:image" -> image = readImage(parser)
//                        "im:releaseDate" -> releaseDate = readDate(parser)
//                        else -> skip(parser)
//                    }
//                }
//                apps.add(AppData(title.toString(), image.toString(), summary.toString(), releaseDate.toString(), link.toString()))
//            }


        }

        return apps
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val res = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return res
    }

    private fun readSummary(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "summary")
        val res = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "summary")
        return res
    }

    private fun readDate(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "im:releaseDate")
        val res = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "im:releaseDate")
        return res
    }

    private fun readImage(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "im:image")
        val res = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "im:image")
        return res
    }

    private fun readLink(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "id")
        val res = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "id")
        return res
    }

    private fun readUpdated(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "updated")
        val res = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "updated")
        return res
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}