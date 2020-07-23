package org.first.mnkotlin

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class CustomAsyncTask : AsyncTask<Void, Void, Void>() {
    private var finalAddress = "금천구"
    private var mlResult = "치킨"
    private lateinit var br : BufferedReader
    private lateinit var inputLine : String
    private var address = ArrayList<String>()
    private var title = ArrayList<String>()
    private var telephone = ArrayList<String>()



    override fun doInBackground(vararg params: Void?): Void? {
        getNaverSearchInfo()
        return null
    }

    fun getNaverSearchInfo(){
        var clientId = "jM0OipNRR9S8PFY6YRCi"
        var clientSecret = "zIgaauxwO_"

        try{
            var text = URLEncoder.encode(finalAddress + mlResult, "UTF-8")
            var apiURL = "https://openapi.naver.com/v1/search/local?query=" + text
            var url = URL(apiURL)
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.setRequestProperty("X-Naver-Client-Id", clientId)
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
            var responseCode = con.responseCode
            if(responseCode == 200){
                // 정상 호출
                br = BufferedReader(InputStreamReader(con.inputStream))
            }
            else{
                br = BufferedReader(InputStreamReader(con.errorStream))
            }

            var response = StringBuffer()
            var inputLine = br.readLine()
            while(inputLine != null){
                inputLine = br.readLine()
                response.append(inputLine)
            }


            br.close()
//            val parser = JSONParser()
//            val obj: Any = parser.parse(response.toString())
            val jsonObject = JSONObject("{" + response.toString() + "}")
            val getArray: JSONArray = jsonObject.get("items") as JSONArray
            for (i in 0 until getArray.length()) {
                val `object`: JSONObject = getArray.get(i) as JSONObject
                val getTitle = `object`.get("title") as String
                address.add(`object`.get("address") as String)
                telephone.add(`object`.get("telephone") as String)
                val titleFilter1 = getTitle.replace("<b>".toRegex(), "")
                val titleFilter2 = titleFilter1.replace("</b>".toRegex(), "")
                title.add(titleFilter2)

                Log.e("TATATA1", `object`.get("address") as String)
                Log.e("TATATA2", `object`.get("telephone") as String)
                Log.e("TATATA3", titleFilter2)


            }

        } catch (e : Exception){
            e.printStackTrace()
        }
    }
}