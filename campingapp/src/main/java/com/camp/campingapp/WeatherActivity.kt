package com.camp.campingapp
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camp.campingapp.databinding.ActivityWeatherBinding
import com.camp.campingapp.model.ModelWeather

import com.camp.campingapp.retrofit.ApiObject
import com.camp.campingapp.retrofit.ITEM
import com.camp.campingapp.retrofit.WEATHER
import com.camp.campingapp.recycler.WeatherAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


//class WeatherActivity : AppCompatActivity() {
//    val currentTime: Long = System.currentTimeMillis() // ms로 반환
//
//    lateinit var weatherRecyclerView: RecyclerView

//    val binding = ActivityWeatherBinding.inflate(layoutInflater)
//    private var base_date = "20230910"  // 발표 일자
//    private var base_time = "0600"      // 발표 시각
//
//
//
//    private var mFusedLocationProviderClient: FusedLocationProviderClient? =
//        null //현재 위치를 가져오기 위한 변수
//    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
//
//    //    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
//    lateinit var mLocationRequest: LocationRequest  // 위치 정보 요청의 매개변수를 저장하는
//    private val REQUEST_PERMISSION_LOCATION = 10
//
//    private var nx: String = ""
//    private var ny: String = ""
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_weather)
//
//        val tvDate = findViewById<TextView>(R.id.tvDate)
//        weatherRecyclerView = findViewById<RecyclerView>(R.id.weatherRecyclerView)
//        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
//
//        weatherRecyclerView.layoutManager = LinearLayoutManager(this@WeatherActivity)
//        tvDate.text =
//            SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Calendar.getInstance().time) + "날씨"
//
//        // 위치 정보를 가져오기 위한 초기화를 onCreate에서 실행
//        mLocationRequest = LocationRequest.create().apply {
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//            interval = 1000
//        }
//
//        if (checkPermissionForLocation(this)) {
//            startLocationUpdates()
//        }
//
//        btnRefresh.setOnClickListener {
//            // 새로고침 버튼 클릭 시 날씨 정보 다시 가져오도록 호출
//            setWeather(nx, ny)
//        }
//    }
//
//    private fun startLocationUpdates() {
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//
//        mFusedLocationProviderClient!!.requestLocationUpdates(
//            mLocationRequest,
//            mLocationCallback,
//            Looper.myLooper()
//        )
//    }
//
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            locationResult.lastLocation?.let { onLocationChanged(it) }
//        }
//    }
//
//    private fun onLocationChanged(location: Location) {
//        mLastLocation = location
//        nx = mLastLocation.longitude.toString()
//        ny = mLastLocation.latitude.toString()
//        setWeather(nx, ny)
//    }
//
//
//    private fun checkPermissionForLocation(context: Context): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                true
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_PERMISSION_LOCATION
//                )
//                false
//            }
//        } else {
//            true
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_PERMISSION_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startLocationUpdates()
//            } else {
//                Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//
//    // 날씨 가져와서 설정하기
//    private fun setWeather(nx: String, ny: String) {
//        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
//        // 현재 날짜, 시간 정보 가져오기
//        val cal = Calendar.getInstance()
//        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
//        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
//        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
//        // API 가져오기 적당하게 변환
//        base_time = getBaseTime(timeH, timeM)
//        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
//        if (timeH == "00" && base_time == "2330") {
//            cal.add(Calendar.DATE, -1).toString()
//            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
//        }
//
//        // 날씨 정보 가져오기
//        // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
//        val call = ApiObject.retrofitService.GetWeather(60, 1, "JSON", base_date, base_time, nx, ny)
//
//        // 비동기적으로 실행하기
//        call.enqueue(object : retrofit2.Callback<WEATHER> {
//            // 응답 성공 시
//            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
//                if (response.isSuccessful) {
//                    // 날씨 정보 가져오기
//                    val it: List<ITEM> = response.body()!!.response.body.items.item
//
//                    // 현재 시각부터 1시간 뒤의 날씨 6개를 담을 배열
//                    val weatherArr = arrayOf(
//                        ModelWeather(),
//                        ModelWeather(),
//                        ModelWeather(),
//                        ModelWeather(),
//                        ModelWeather(),
//                        ModelWeather()
//                    )
//
//                    // 배열 채우기
//                    var index = 0
//                    val totalCount = response.body()!!.response.body.totalCount - 1
//                    for (i in 0..totalCount) {
//                        index %= 6
//                        when (it[i].category) {
//                            "PTY" -> weatherArr[index].rainType = it[i].fcstValue     // 강수 형태
//                            "REH" -> weatherArr[index].humidity = it[i].fcstValue     // 습도
//                            "SKY" -> weatherArr[index].sky = it[i].fcstValue          // 하늘 상태
//                            "T1H" -> weatherArr[index].temp = it[i].fcstValue         // 기온
//                            else -> continue
//                        }
//                        index++
//                    }
//
//
//                    // 각 날짜 배열 시간 설정
//                    for (i in 0..5) weatherArr[i].fcstTime = it[i].fcstTime
//
//                    // 리사이클러 뷰에 데이터 연결
//                    weatherRecyclerView.adapter = WeatherAdapter(weatherArr)
//
//                    // 토스트 띄우기
////                    Toast.makeText(applicationContext, it[0].fcstDate + ", " + it[0].fcstTime + "의 날씨 정보입니다.", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            // 응답 실패 시
//            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
//                Log.d("api fail", t.message.toString())
//            }
//        })
//    }
//
//    // baseTime 설정하기
////    private fun getBaseTime(h : String, m : String) : String {
////        var result = ""
////
////        // 45분 전이면
////        if (m.toInt() < 45) {
////            // 0시면 2330
////            if (h == "00") result = "2330"
////            // 아니면 1시간 전 날씨 정보 부르기
////            else {
////                var resultH = h.toInt() - 1
////                // 1자리면 0 붙여서 2자리로 만들기
////                if (resultH < 10) result = "0" + resultH + "30"
////                // 2자리면 그대로
////                else result = resultH.toString() + "30"
////            }
////        }
////        // 45분 이후면 바로 정보 받아오기
////        else result = h + "30"
////
////        return result
////    }
//
//    private fun getBaseTime(h: String, m: String): String {
//        if (h == "00" && m.toInt() < 45) {
//            // 00시 45분 이전이면 전날 2330
//            return "2330"
//        } else {
//            // 그 외의 경우는 현재 시각에서 분을 조정하여 반환
//            val adjustedMinute = if (m.toInt() < 45) "00" else "30"
//            return "${if (h.length == 1) "0$h" else h}$adjustedMinute"
//        }
//    }
//}
class WeatherActivity : AppCompatActivity() {
    val currentTime : Long = System.currentTimeMillis() // ms로 반환

    lateinit var weatherRecyclerView : RecyclerView

    private var base_date = "20230910"  // 발표 일자
    private var base_time = "0600"      // 발표 시각
    private var nx = "55"               // 예보지점 X 좌표
    private var ny = "127"              // 예보지점 Y 좌표

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)


        val tvDate = findViewById<TextView>(R.id.tvDate)                                // 오늘 날짜 텍스트뷰
        weatherRecyclerView = findViewById<RecyclerView>(R.id.weatherRecyclerView)  // 날씨 리사이클러 뷰
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)                          // 새로고침 버튼

        // 리사이클러 뷰 매니저 설정
        weatherRecyclerView.layoutManager = LinearLayoutManager(this@WeatherActivity)

        // 오늘 날짜 텍스트뷰 설정
        tvDate.text = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Calendar.getInstance().time) + "날씨"

        // nx, ny지점의 날씨 가져와서 설정하기
        setWeather(nx, ny)

        // <새로고침> 버튼 누를 때 날씨 정보 다시 가져오기
        btnRefresh.setOnClickListener {
            setWeather(nx, ny)

        }
    }

    // 날씨 가져와서 설정하기
    private fun setWeather(nx : String, ny : String) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
        // API 가져오기 적당하게 변환
        base_time = getBaseTime(timeH, timeM)
        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
        if (timeH == "00" && base_time == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

        // 날씨 정보 가져오기
        // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = ApiObject.retrofitService.GetWeather(60, 1, "JSON", base_date, base_time, nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    val it: List<ITEM> = response.body()!!.response.body.items.item

                    // 현재 시각부터 1시간 뒤의 날씨 6개를 담을 배열
                    val weatherArr = arrayOf(ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather())

                    // 배열 채우기
                    var index = 0
                    val totalCount = response.body()!!.response.body.totalCount - 1
                    for (i in 0..totalCount) {
                        index %= 6
                        when(it[i].category) {
                            "PTY" -> weatherArr[index].rainType = it[i].fcstValue     // 강수 형태
                            "REH" -> weatherArr[index].humidity = it[i].fcstValue     // 습도
                            "SKY" -> weatherArr[index].sky = it[i].fcstValue          // 하늘 상태
                            "T1H" -> weatherArr[index].temp = it[i].fcstValue         // 기온
                            else -> continue
                        }
                        index++
                    }



                    // 각 날짜 배열 시간 설정
                    for (i in 0..5) weatherArr[i].fcstTime = it[i].fcstTime

                    // 리사이클러 뷰에 데이터 연결
                    weatherRecyclerView.adapter = WeatherAdapter(weatherArr)

                    // 토스트 띄우기
//                    Toast.makeText(applicationContext, it[0].fcstDate + ", " + it[0].fcstTime + "의 날씨 정보입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    // baseTime 설정하기
//    private fun getBaseTime(h : String, m : String) : String {
//        var result = ""
//
//        // 45분 전이면
//        if (m.toInt() < 45) {
//            // 0시면 2330
//            if (h == "00") result = "2330"
//            // 아니면 1시간 전 날씨 정보 부르기
//            else {
//                var resultH = h.toInt() - 1
//                // 1자리면 0 붙여서 2자리로 만들기
//                if (resultH < 10) result = "0" + resultH + "30"
//                // 2자리면 그대로
//                else result = resultH.toString() + "30"
//            }
//        }
//        // 45분 이후면 바로 정보 받아오기
//        else result = h + "30"
//
//        return result
//    }

    private fun getBaseTime(h: String, m: String): String {
//        시간들고오기
        val result: String
        if (h == "00" && m.toInt() < 45) {
            // 12시 자정이면 전날 23:30으로 설정
            result = "2330"
        } else if (m.toInt() >= 45) {
            // 45분 이후에는 현재 시각 그대로 설정
            result = h + "30"
        } else {
            // 그 외의 경우에는 1시간 전 시각으로 설정
            val resultH = (h.toInt() - 1).toString()
            result = if (resultH.length == 1) {
                "0$resultH" + "30"
            } else {
                resultH + "30"
            }
        }
        return result
    }


}





