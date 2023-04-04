package com.example.onsaveinstancestatepro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onsaveinstancestatepro.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var dataList: MutableList<String>
    lateinit var myAdapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dataList = mutableListOf<String>()
        // 1. 번들에 데이터가 있으면 가져와서 기존변수에 저장하고, 없으면 무시한다.
        if(savedInstanceState != null){
            dataList = savedInstanceState.getStringArrayList("dataList")!!.toMutableList()
        }
        // 2. 인텐트를 돌려받을
        val activityResultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val result: String = it.data?.getStringExtra("result").toString()

                if(result != null && !result.equals("")){
                    dataList.add(result)
                }
                myAdapter.notifyDataSetChanged()
            }

        // 3. MyAdapter와 리사이클러뷰와 연결, 보여줄 리스트 모양 결정
        val layoutManager = LinearLayoutManager(this)
        myAdapter = MyAdapter(dataList)
        binding.mainRecyclerView.layoutManager = layoutManager
        binding.mainRecyclerView.adapter = myAdapter
        binding.mainRecyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))

        // 4. floating tab 클릭하면 인테트 요청
        binding.mainFab.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        // 6. sharedPreference 저장하는 방법
        binding.btnShared.setOnClickListener {
            val sharedPreferences = getSharedPreferences("dataList", Context.MODE_PRIVATE) // 공유장치를 불러오는 것
            val editor = sharedPreferences!!.edit()
            // gson arraylist를 안에 있는 값을 문자열로 바꿔주는 것
            val gson = Gson()
            val json: String = gson.toJson(dataList)
            editor.putString("oneMessage",json)
            editor.commit()
            Toast.makeText(this,"sharedPreference 저장",Toast.LENGTH_SHORT).show()
        }

        binding.btnRevert.setOnClickListener {
            val sharedPreference = getSharedPreferences("dataList", Context.MODE_PRIVATE)
            val data = sharedPreference.getString("oneMessage",null)
            val type: Type = object : TypeToken<ArrayList<String>?>(){}.type
            val gson = Gson()
            dataList = gson.fromJson<Any>(data,type) as ArrayList<String>

            val layoutManager = LinearLayoutManager(this)
            myAdapter = MyAdapter(dataList)
            binding.mainRecyclerView.layoutManager = layoutManager
            binding.mainRecyclerView.adapter = myAdapter
            binding.mainRecyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))

            Toast.makeText(this,"sharedPreference 복구",Toast.LENGTH_SHORT).show()
        }
    }

/*    // 3. 번들에 데이터가 있으면 가져와서 기존변수에 저장하고, 없으면 무시한다.
    // savedInstanceState = outState (같은 번들이다)
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null){
            dataList = savedInstanceState.getStringArrayList("dataList")!!.toMutableList()
        } else {
            dataList = mutableListOf<String>()
        }
    }*/

    // 5. 화면을 회전할 경우에 기존에 내용을 번들에 저장한다.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("dataList",ArrayList(dataList))
    }
}