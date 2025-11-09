package com.example.myapplication

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var dpm: DevicePolicyManager
    private lateinit var admin: ComponentName
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        db = Firebase.database.reference
        dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        admin = ComponentName(this, MyDeviceAdminReceiver::class.java)

        // Permite que o app use Lock Task (modo quiosque)
        if (dpm.isDeviceOwnerApp(packageName)) {
            dpm.setLockTaskPackages(admin, arrayOf(packageName))
        }

        // Bloqueia o botão "voltar"
        onBackPressedDispatcher.addCallback(this) {
            // Nada acontece — bloqueia saída
        }

        // Inicia o serviço que escuta comandos
        startService(Intent(this, CommandService::class.java))

        // Começa a escutar comandos vindos do Firebase
        listenForCommands()
    }

    /**
     * Escuta comandos no Firebase em tempo real
     */
    private fun listenForCommands() {
        db.child("device/command").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                when (snapshot.value?.toString()?.lowercase()) {
                    "lock" -> {
                        enableKioskMode()
                        showInadimplente()
                        db.child("device/feedback").setValue("🚫 Cliente inadimplente — bloqueado")
                    }

                    "unlock" -> {
                        disableKioskMode()
                        showAdimplente()
                        db.child("device/feedback").setValue("✅ Cliente adimplente — acesso liberado")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                db.child("device/feedback").setValue("❌ Erro: ${error.message}")
            }
        })
    }

    /**
     * Entra no modo quiosque (Lock Task Mode)
     */
    private fun enableKioskMode() {
        try {
            startLockTask()
            hideSystemUI()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sai do modo quiosque
     */
    private fun disableKioskMode() {
        try {
            stopLockTask()
            showSystemUI()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Tela vermelha: cliente inadimplente
     */
    private fun showInadimplente() {
        statusText.text = "🚫 Cliente Inadimplente"
        statusText.setBackgroundColor(getColor(android.R.color.holo_red_dark))
    }

    /**
     * Tela verde: cliente adimplente
     */
    private fun showAdimplente() {
        statusText.text = "✅ Cliente Adimplente"
        statusText.setBackgroundColor(getColor(android.R.color.holo_green_dark))
    }

    /**
     * Oculta barra de status e navegação (modo imersivo moderno)
     */
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    /**
     * Mostra novamente as barras do sistema
     */
    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.show(WindowInsetsCompat.Type.systemBars())
    }

    /**
     * Ao voltar para o app (por ex. após comando remoto),
     * ajusta o estado de acordo com o último comando no Firebase.
     */
    override fun onResume() {
        super.onResume()
        db.child("device/command").get()
            .addOnSuccessListener { snapshot ->
                when (snapshot.value?.toString()?.lowercase()) {
                    "lock" -> {
                        enableKioskMode()
                        showInadimplente()
                    }

                    "unlock" -> {
                        disableKioskMode()
                        showAdimplente()
                    }
                }
            }
    }
}
