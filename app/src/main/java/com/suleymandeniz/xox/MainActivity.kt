package com.suleymandeniz.xox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.suleymandeniz.xox.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var TV: ActivityMainBinding
    var oyuncu = "p1"
    var puanx = 0
    var puanO = 0
    var isSinglePlayer = false
    var buttons = mutableListOf<Button>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TV = ActivityMainBinding.inflate(layoutInflater)
        setContentView(TV.root)

        buttons = mutableListOf(TV.b1, TV.b2, TV.b3, TV.b4, TV.b5, TV.b6, TV.b7, TV.b8, TV.b9)

        // Oyun modu seçimi
        selectGameMode()

        TV.b1.setOnClickListener { butonlar(TV.b1) }
        TV.b2.setOnClickListener { butonlar(TV.b2) }
        TV.b3.setOnClickListener { butonlar(TV.b3) }
        TV.b4.setOnClickListener { butonlar(TV.b4) }
        TV.b5.setOnClickListener { butonlar(TV.b5) }
        TV.b6.setOnClickListener { butonlar(TV.b6) }
        TV.b7.setOnClickListener { butonlar(TV.b7) }
        TV.b8.setOnClickListener { butonlar(TV.b8) }
        TV.b9.setOnClickListener { butonlar(TV.b9) }
    }
    fun selectGameMode() {
        val options = arrayOf("Tek Oyuncu (Bilgisayara Karşı)", "İki Oyuncu")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Oyun Modu Seçin")
        builder.setItems(options) { _, which ->
            isSinglePlayer = which == 0
            Toast.makeText(this, if (isSinglePlayer) "Tek oyuncu modu seçildi! 🎮" else "İki oyuncu modu seçildi! 👥", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    fun butonlar(button: Button){
        if(button.text == ""){
            if(oyuncu == "p1"){
                button.text = "X"
                oyuncu = "p2"
            }
            else{
                button.text = "O"
                oyuncu = "p1"
            }
        }
        durumlar()
        // Tek oyuncu modunda bilgisayar hamlesi
        if (isSinglePlayer && oyuncu == "p2" && !isGameOver()) {
            computerMove()
        }
    }
    fun durumlar(){
        if ((TV.b1.text == "X" && TV.b2.text == "X" && TV.b3.text == "X")||
            (TV.b4.text == "X" && TV.b5.text == "X" && TV.b6.text == "X")||
            (TV.b7.text == "X" && TV.b8.text == "X" && TV.b9.text == "X")||

            (TV.b1.text == "X" && TV.b4.text == "X" && TV.b7.text == "X")||
            (TV.b2.text == "X" && TV.b5.text == "X" && TV.b8.text == "X")||
            (TV.b3.text == "X" && TV.b6.text == "X" && TV.b9.text == "X")||

            (TV.b1.text == "X" && TV.b5.text == "X" && TV.b9.text == "X")||
            (TV.b3.text == "X" && TV.b5.text == "X" && TV.b7.text == "X")){
            println("Tebrikler! X kazandı! 🎉")
            alert("Tebrikler! X kazandı! 🎉")
        }

        else if
            ((TV.b1.text == "O" && TV.b2.text == "O" && TV.b3.text == "O")||
            (TV.b4.text == "O" && TV.b5.text == "O" && TV.b6.text == "O")||
            (TV.b7.text == "O" && TV.b8.text == "O" && TV.b9.text == "O")||

            (TV.b1.text == "O" && TV.b4.text == "O" && TV.b7.text == "O")||
            (TV.b2.text == "O" && TV.b5.text == "O" && TV.b8.text == "O")||
            (TV.b3.text == "O" && TV.b6.text == "O" && TV.b9.text == "O")||

            (TV.b1.text == "O" && TV.b5.text == "O" && TV.b9.text == "O")||
            (TV.b3.text == "O" && TV.b5.text == "O" && TV.b7.text == "O")){
            println("Tebrikler! O kazandı! 🎊")
            alert("O Kazandı! 🎊")
        }
        else if((TV.b1.text != "")&&(TV.b2.text !="")&&(TV.b3.text != "")&&
            (TV.b4.text != "")&&(TV.b5.text !="")&&(TV.b6.text != "")&&
            (TV.b7.text != "")&&(TV.b8.text !="")&&(TV.b9.text != "")){
            println("Berabere! Yeniden oynayalım mı? 🤝")
            alert("Berabere! 🤝")
        }

    }
    fun alert(wins: String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Oyun Bitti! $wins")
        alertDialog.setMessage("Harika bir oyun! Tekrar oynamak ister misin? 🎮")
        alertDialog.setPositiveButton("Evet! 😄") { dialog, which ->
            clear()
            if (wins.contains("X")) {
                puanx++
            } else if(wins.contains("O")){
                puanO++
            }
            // Skor güncelleme
            TV.skorX.setText("X Skoru: $puanx 🏆")
            TV.skorO.setText("O Skoru: $puanO 🏆")
            Toast.makeText(applicationContext,"Yeni oyun başladı! 🚀", Toast.LENGTH_SHORT).show()
        }.setNegativeButton("Hayır, çık 😢") { dialog, which ->
            finish()
        }
        alertDialog.show()
    }

    fun computerMove() {
        val emptyButtons = buttons.filter { it.text == "" }
        if (emptyButtons.isNotEmpty()) {
            Handler(Looper.getMainLooper()).postDelayed({
                val randomButton = emptyButtons[Random.nextInt(emptyButtons.size)]
                randomButton.text = "O"
                oyuncu = "p1"
                durumlar()
            }, 1000) // 1 saniye gecikme
        }
    }

    fun isGameOver(): Boolean {
        // Kazanma koşulları kontrolü
        val winConditions = listOf(
            listOf(TV.b1, TV.b2, TV.b3), listOf(TV.b4, TV.b5, TV.b6), listOf(TV.b7, TV.b8, TV.b9),
            listOf(TV.b1, TV.b4, TV.b7), listOf(TV.b2, TV.b5, TV.b8), listOf(TV.b3, TV.b6, TV.b9),
            listOf(TV.b1, TV.b5, TV.b9), listOf(TV.b3, TV.b5, TV.b7)
        )
        for (condition in winConditions) {
            if (condition.all { it.text == "X" } || condition.all { it.text == "O" }) {
                return true
            }
        }
        // Beraberlik kontrolü
        return buttons.all { it.text != "" }
    }

    fun clear(){
        buttons.forEach { it.text = "" }
        oyuncu = "p1"
    }
}