package com.example.onsaveinstancestatepro

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.onsaveinstancestatepro.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    val binding: ActivityAddBinding by lazy { ActivityAddBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    // 1. 인텐트 돌려 줄 버튼 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 2. 메뉴 이벤트 설정 (setOnClickListener랑 기능이 똑같음)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_add_save -> {
                intent.putExtra("result",binding.addEditView.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // 백키를 눌렀을 때 발생하는 콜백함수
    override fun onBackPressed() {
//        super.onBackPressed()
        intent.putExtra("result","")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}